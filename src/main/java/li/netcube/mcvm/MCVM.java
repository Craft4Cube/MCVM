package li.netcube.mcvm;

import com.mojang.realmsclient.client.FileDownload;
import li.netcube.mcvm.server.commands.*;
import li.netcube.mcvm.common.ui.GuiProxy;
import li.netcube.mcvm.util.*;
import li.netcube.mcvm.util.vm.VirtualMachine;
import li.netcube.mcvm.util.vm.commands.ICommandParser;
import li.netcube.mcvm.util.vm.commands.RedstoneCommandParser;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.logging.log4j.Logger;
import scala.collection.parallel.ParSeqLike;

import javax.annotation.Nullable;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;

@Mod(modid = MCVM.MODID, name = MCVM.NAME, version = "@VERSION@", useMetadata = true)
public class MCVM
{

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("mcvm");

    public static final String MODID = "mcvm";
    public static final String NAME = "Minecraft Virtual Machines";

    public static Logger logger;

    public static ArrayList<VirtualMachine> virtualMachines = new ArrayList<>();

    public static ArrayList<ICommandParser> registeredCommandParsers = new ArrayList<>();

    public static IniFile modConfig;

    @Mod.Instance
    public static MCVM instance;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiProxy());
        INSTANCE.registerMessage(VMControlMessageHandler.class, VMControlMessage.class, 0, Side.SERVER);

        ProgressBarWindow installProgress = null;

        if (!GraphicsEnvironment.isHeadless()) {
            installProgress = new ProgressBarWindow();
        }

        if (installProgress != null) {
            installProgress.createAndShowGUI();
        }

        boolean osIsCompatible = false;

        if (SystemUtils.IS_OS_WINDOWS) {
            try {
                File destinationFile = new File("mcvmRedistWin.zip");
                File fileHash = new File("mcvmRedistWin.md5");
                URL redistURL = new URL("https://netcube.li/mcvm/mcvmRedistWin.zip");
                URL hashURL = new URL("https://netcube.li/mcvm/getHash.php?filename=mcvmRedistWin.zip");
                updateRedistPackage(installProgress, destinationFile, fileHash, redistURL, hashURL);
                osIsCompatible = true;
            } catch (Exception ignored) {}
        } else if (SystemUtils.IS_OS_LINUX) {
            try {
                File destinationFile = new File("mcvmRedistLinux.zip");
                File fileHash = new File("mcvmRedistLinux.md5");
                URL redistURL = new URL("https://netcube.li/mcvm/mcvmRedistLinux.zip");
                URL hashURL = new URL("https://netcube.li/mcvm/getHash.php?filename=mcvmRedistLinux.zip");
                updateRedistPackage(installProgress, destinationFile, fileHash, redistURL, hashURL);
                osIsCompatible = true;
            } catch (Exception ignored) {}
        }

        if (installProgress != null) {
            installProgress.closeWindow();
        }

        registerCommandParser(new RedstoneCommandParser());

        File iniFile = new File("mcvm.ini");

        try {
            if (!iniFile.exists())
            {
                iniFile.createNewFile();
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(iniFile));
                bufferedWriter.write("[VirtualMachine]\r\n");
                bufferedWriter.write("customArguments=\n\n");
                bufferedWriter.flush();
                bufferedWriter.close();
            }
            modConfig = new IniFile("mcvm.ini");
        } catch (Exception ignored) {}

    }

    private void updateRedistPackage(@Nullable ProgressBarWindow installProgress, File destinationFile, File fileHash, URL redistURL, URL hashURL) {
        String remote_md5 = "";
        String local_md5 = "";

        if (destinationFile.exists()) {

            if (fileHash.exists()) {
                fileHash.delete();
            }

            try {
                if (installProgress != null) {
                    installProgress.setStatus("Downloading MD5...");
                } else {
                    logger.info("Downloading MD5...");
                }
                FileDownloadHelper hashDownloadHelper = new FileDownloadHelper(hashURL, fileHash);
                Thread downloadThread = new Thread() {
                    public void run() {
                        while (hashDownloadHelper.getStatus() == FileDownloadHelper.DOWNLOADING) {
                            if (installProgress != null) {
                                installProgress.setProgress((int) hashDownloadHelper.getProgress());
                            }
                            logger.info((int)hashDownloadHelper.getProgress());
                            try {
                                Thread.sleep(500);
                            } catch (Exception ignored) {}
                        }
                    }
                };
                downloadThread.run();
                downloadThread.join();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (installProgress != null) {
                installProgress.setStatus("Checking MD5...");
            } else {
                logger.info("Checking MD5...");
            }

            try {
                if (fileHash.exists()) {
                    FileInputStream fis = new FileInputStream(fileHash);
                    byte[] data = new byte[(int) fileHash.length()];
                    fis.read(data);
                    fis.close();

                    remote_md5 = new String(data, "UTF-8");
                }
            } catch (Exception e) {
            }

            try {
                FileInputStream fis = new FileInputStream(destinationFile);
                local_md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(fis);
                fis.close();
            } catch (Exception e) {
            }
        }


        if (!destinationFile.exists() || !remote_md5.equals(local_md5)) {
            try {
                if (destinationFile.exists()) {
                    destinationFile.delete();
                }

                if (installProgress != null) {
                    installProgress.setStatus("Downloading MCVM Redistributable...");
                } else {
                    logger.info("Downloading MCVM Redistributable...");
                }

                FileDownloadHelper fileDownloadHelper = new FileDownloadHelper(redistURL, destinationFile);
                Thread downloadThread = new Thread() {
                    public void run() {
                        while (fileDownloadHelper.getStatus() == FileDownloadHelper.DOWNLOADING) {
                            if (installProgress != null) {
                                installProgress.setProgress((int) fileDownloadHelper.getProgress());
                            }
                            logger.info((int)fileDownloadHelper.getProgress());
                            try {
                                Thread.sleep(500);
                            } catch (Exception ignored) {}
                        }
                    }
                };
                downloadThread.run();
                downloadThread.join();

                if (installProgress != null) {
                    installProgress.setStatus("Extracting MCVM Redistributable...");
                } else {
                    logger.info("Extracting MCVM Redistributable...");
                }

                UnZipHelper unZipHelper = new UnZipHelper(destinationFile, new File(destinationFile.getAbsolutePath()).getParentFile());
                Thread extractThread = new Thread() {
                    public void run() {
                        while (unZipHelper.getStatus() == 0) {
                            if (installProgress != null) {
                                installProgress.setProgress((int) unZipHelper.getProgress());
                            }
                            //logger.info(unZipHelper.getProgress());
                        }
                    }
                };
                extractThread.run();
                extractThread.join();

                if (unZipHelper.getStatus() == -1) {
                    throw new java.io.IOException();
                }
            } catch (Exception e) {
                logger.fatal("Could not download/extract MCVM Redistributable! Mod will only work as client. (You can only play on a server)");
                e.printStackTrace();
                if (destinationFile.exists()) {
                    destinationFile.delete();
                }
            }
        } else {
            if (installProgress != null) {
                installProgress.setStatus("Latest MCVM Redistributable is installed! This is a good thing.");
            } else {
                logger.info("Latest MCVM Redistributable is installed! This is a good thing.");
            }
        }
    }

    @EventHandler
    public void serverLoad(FMLServerStartingEvent event)
    {
        // register server commands
        //event.registerServerCommand(new createHDD());  //Removed: Should not be used!
        event.registerServerCommand(new createCD());
        event.registerServerCommand(new createFLOPPY());
    }

    @EventHandler
	public void serverStopping(FMLServerStoppingEvent event){
    	while (virtualMachines.size() > 0) {
    	    //System.out.println("Stopping VM... ");
    	    VirtualMachine vm = virtualMachines.get(0);
            virtualMachines.remove(0);
            if (vm.isRunning()) {
                vm.stop();
            }
        }
    }

    public static void registerCommandParser(ICommandParser commandParser) {
        registeredCommandParsers.add(commandParser);
    }
}