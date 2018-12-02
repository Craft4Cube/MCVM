package li.netcube.mcvm.util.vm;

import li.netcube.mcvm.MCVM;

import java.io.*;
import java.util.concurrent.TimeUnit;

import li.netcube.mcvm.common.ui.ComputerContainerTileEntity;
import li.netcube.mcvm.util.tcpClient.*;
import li.netcube.mcvm.util.vm.commands.ICommandParser;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.SystemUtils;
import org.lwjgl.Sys;


public class VirtualMachine {
    public File hdd0 = null;
    public File hdd1 = null;

    public File floppy0 = null;
    public File floppy1 = null;

    public File cd0 = null;
    public File cd1 = null;

    public String memory;

    public Process process = null;

    public String nic = null;

    public int id = 0;

    public int VNCPort = 40000;
    public int MONPort = 40100;
    public int COMPort = 40200;

    public String password;

    public TCPClient monitor;
    public TCPClient serial;

    public BlockPos computerPos;

    private Thread serialThread;
    private Thread processWatcherThread;

    private ComputerContainerTileEntity ccte;


    public VirtualMachine(ComputerContainerTileEntity ccte, int id, String password, String memory, File hdd0, File hdd1, File floppy0, File floppy1, File cd0, File cd1, String nic, BlockPos computerPos) {
        this.ccte = ccte;

        this.id = id;

        this.VNCPort = 40000 + id;
        this.MONPort = 40100 + id;
        this.COMPort = 40200 + id;

        this.memory = memory;
        this.hdd0 = hdd0;
        this.hdd1 = hdd1;
        this.floppy0 = floppy0;
        this.floppy1 = floppy1;
        this.cd0 = cd0;
        this.cd1 = cd1;

        this.nic = nic;

        this.computerPos = computerPos;

        this.password = password;
    }

    public void setUid(int id) {

        this.id = id;

        this.VNCPort = 40000 + id;
        this.MONPort = 40100 + id;
        this.COMPort = 40200 + id;
    }

    public void setPassword(String password) {
        this.password = password;
        //System.out.println(this.password);
    }

    public void start() {
        if (this.process == null || !this.process.isAlive()) {
            if (!(this.memory.contains("256") || this.memory.contains("512") || this.memory.contains("768") || this.memory.contains("1024"))) {
                return;
            }

            //If qemu is already running try to stop it.
            try {
                TCPClient tmpMon = new TCPClient("127.0.0.1", this.MONPort);
                tmpMon.getOutputStream().write(("quit\n").getBytes("UTF-8"));
                tmpMon.getOutputStream().flush();
                tmpMon.close();
            } catch (Exception ignored) {}

            //-accel hax
            //-accel tcg,thread=multi

            String commandLine = "-vga std -smp 4 -usbdevice tablet -k bin/qemu/keymaps/mcvm -boot menu=on,splash=resource/splash.bmp,splash-time=2500 -monitor tcp::" + MONPort + ",server,nowait -serial tcp::" + COMPort + ",server,nowait -vnc :" + (VNCPort - 5900) + ",password";

            if (!MCVM.modConfig.getString("VirtualMachine", "customArguments", "").equals("")) {
                commandLine += " " + MCVM.modConfig.getString("VirtualMachine","customArguments", "");
            }

            if (this.nic != null) commandLine = commandLine  + " -netdev user,id=n0 -device " + this.nic + ",netdev=n0";
            else commandLine = commandLine + " -net none";

            if (this.hdd0 != null) commandLine = commandLine + " -drive if=ide,index=0,media=disk,file=\"" + this.hdd0 + "\"";
            if (this.hdd1 != null) commandLine = commandLine + " -drive if=ide,index=1,media=disk,file=\"" + this.hdd1 + "\"";
            commandLine = commandLine + " -drive if=ide,index=2,media=cdrom" + ((this.cd0 != null) ? ",file=\"" + this.cd0 + "\"" : "");
            commandLine = commandLine + " -drive if=ide,index=3,media=cdrom" + ((this.cd1 != null) ? ",file=\"" + this.cd1 + "\"" : "");

            commandLine = commandLine + " -drive if=floppy,index=0" + ((this.floppy0 != null) ? ",file=\"" + this.floppy0 + "\"" : "");
            commandLine = commandLine + " -drive if=floppy,index=1" + ((this.floppy1 != null) ? ",file=\"" + this.floppy1 + "\"" : "");

            commandLine = commandLine + " -m " + this.memory;

            Runtime r = Runtime.getRuntime();
            try {
                ProcessBuilder builder = new ProcessBuilder();
                File gameFolder = new File(new File("./").getAbsolutePath()).getParentFile();
                if (SystemUtils.IS_OS_WINDOWS) {
                    this.process = Runtime.getRuntime().exec("bin/qemu/qemu-system-x86_64w.exe " + commandLine, null, gameFolder);
                } else if (SystemUtils.IS_OS_LINUX) {
                    this.process = Runtime.getRuntime().exec("qemu-system-x86_64 " + commandLine, null, gameFolder);
                }
                builder.directory(gameFolder);
                //System.out.println(commandLine);
                //builder.redirectOutput(new File("vmlog.txt"));
                //builder.redirectError(new File("errlog.txt"));
                //System.out.println();

                MCVM.virtualMachines.add(this);

                Thread.sleep(500);

                this.monitor = new TCPClient("127.0.0.1", this.MONPort);
                this.serial = new TCPClient("127.0.0.1", this.COMPort);

                this.monitor.getOutputStream().write(("change vnc password" + "\n").getBytes("UTF-8"));
                this.monitor.getOutputStream().write((this.password + "\n").getBytes("UTF-8"));
                //System.out.println(this.password);
                this.monitor.getOutputStream().flush();

                serialThread = new Thread() {
                    public void run() {
                        try {
                            InputStream is = serial.getInputStream();
                            OutputStream os = serial.getOutputStream();
                            BufferedReader br = new BufferedReader(new InputStreamReader(is));
                            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
                            String lastCommand;

                            while (process.isAlive()) {
                                try {
                                    lastCommand = br.readLine();
                                    bw.write(parseCommand(lastCommand));
                                    bw.newLine();
                                    bw.flush();
                                } catch (Exception ignored) {}
                            }

                        } catch (Exception ignored) {}
                    }
                };
                serialThread.start();

                processWatcherThread = new Thread() {
                    public void run() {
                        while (process.isAlive()) {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        TileEntity tileEntity = ccte;
                        if (tileEntity instanceof ComputerContainerTileEntity) {
                            ((ComputerContainerTileEntity) tileEntity).outPowerSides = new int[] {0,0,0,0,0,0};
                            ccte.setPoweredOn(false);
                            ccte.getWorld().notifyNeighborsOfStateChange(computerPos, ccte.getWorld().getBlockState(computerPos).getBlock(), true);
                        }
                    }
                };
                processWatcherThread.start();

                ccte.setPoweredOn(true);

                ccte.getWorld().scheduleBlockUpdate(computerPos, ccte.getWorld().getBlockState(computerPos).getBlock(), 4, 1);

            } catch (Exception ignored) {
                //ignored.printStackTrace();
            }
        }
    }

    public void stop() {
        try {
            if (this.isRunning()) {
                //this.monitor.getOutputStream().write("quit\n".getBytes("UTF-8"));
                //this.monitor.getOutputStream().flush();
                //this.process.waitFor(5, TimeUnit.SECONDS);
                if (this.isRunning()) {
                    this.process.destroyForcibly();
                }
                MCVM.virtualMachines.remove(this);
                serialThread.stop();
                processWatcherThread.stop();
                serial.close();
                monitor.close();
                ccte.setPoweredOn(false);
            }
        } catch (Exception ignored) {}
    }

    public void reset() {
        //system_reset
        try {
            if (this.isRunning()) {
                this.monitor.getOutputStream().write(("system_reset\n").getBytes("UTF-8"));
                this.monitor.getOutputStream().flush();

                while (this.monitor.getInputStream().available() > 0) {
                    //System.out.print((char)this.monitor.getInputStream().read());
                    this.monitor.getInputStream().read();
                }
            }
        } catch (Exception ignored) {}
    }

    public void setFloppy0(File file) {
        try {
            this.floppy0 = file;
            if (this.isRunning()) {
                this.monitor.getOutputStream().write("eject -f floppy0\n".getBytes("UTF-8"));
                this.monitor.getOutputStream().flush();
                if (this.floppy0 != null) {
                    this.monitor.getOutputStream().write(("change floppy0 \"" + this.floppy0.toString().replace("\\", "\\\\") + "\"\n").getBytes("UTF-8"));
                    this.monitor.getOutputStream().flush();
                }
                //System.out.println("FLOPPY0 CHANGE!");

                while (this.monitor.getInputStream().available() > 0) {
                    //System.out.print((char)this.monitor.getInputStream().read());
                    this.monitor.getInputStream().read();
                }
            }
        } catch (Exception ignored) {}
    }

    public void setFloppy1(File file) {
        try {
            this.floppy1 = file;
            if (this.isRunning()) {
                this.monitor.getOutputStream().write("eject -f floppy1\n".getBytes("UTF-8"));
                this.monitor.getOutputStream().flush();
                if (this.floppy1 != null) {
                    this.monitor.getOutputStream().write(("change floppy1 \"" + this.floppy1.toString().replace("\\", "\\\\") + "\"\n").getBytes("UTF-8"));
                    this.monitor.getOutputStream().flush();
                }
                //System.out.println("FLOPPY1 CHANGE!");

                while (this.monitor.getInputStream().available() > 0) {
                    //System.out.print((char)this.monitor.getInputStream().read());
                    this.monitor.getInputStream().read();
                }
            }
        } catch (Exception ignored) {}
    }

    public void setCd0(File file) {
        try {
            this.cd0 = file;
            if (this.isRunning()) {
                this.monitor.getOutputStream().write("eject -f ide1-cd0\n".getBytes("UTF-8"));
                this.monitor.getOutputStream().flush();
                if (this.cd0 != null) {
                    this.monitor.getOutputStream().write(("change ide1-cd0 \"" + this.cd0.toString().replace("\\", "\\\\") + "\"\n").getBytes("UTF-8"));
                    this.monitor.getOutputStream().flush();
                }
                //System.out.println("IDE1-CD0 CHANGE!");

                while (this.monitor.getInputStream().available() > 0) {
                    //System.out.print((char)this.monitor.getInputStream().read());
                    this.monitor.getInputStream().read();
                }
            }
        } catch (Exception ignored) {}
    }

    public void setCd1(File file) {
        try {
            this.cd1 = file;
            if (this.isRunning()) {
                this.monitor.getOutputStream().write("eject -f ide1-cd1\n".getBytes("UTF-8"));
                this.monitor.getOutputStream().flush();
                if (this.cd1 != null) {
                    this.monitor.getOutputStream().write(("change ide1-cd1 \"" + this.cd1.toString().replace("\\", "\\\\") + "\"\n").getBytes("UTF-8"));
                    this.monitor.getOutputStream().flush();
                }
                //System.out.println("IDE1-CD1 CHANGE!");

                while (this.monitor.getInputStream().available() > 0) {
                    //System.out.print((char)this.monitor.getInputStream().read());
                    this.monitor.getInputStream().read();
                }
            }
        } catch (Exception ignored) {}
    }

    public boolean isRunning() {
        if (this.process == null) {
            return false;
        }
        if (this.process.isAlive()) {
            return true;
        }
        return false;
    }

    public String parseCommand(String command) {
        TileEntity tileEntity = ccte;
        World world = ccte.getWorld();
        if (tileEntity instanceof ComputerContainerTileEntity) {
            for (ICommandParser commandParser : MCVM.registeredCommandParsers) {
                if (commandParser.canHandleCommand(command, (ComputerContainerTileEntity)tileEntity)) {
                    return commandParser.parseCommand(command, (ComputerContainerTileEntity)tileEntity);
                }
            }
        }
        return "error.unknown.command";
    }
}
