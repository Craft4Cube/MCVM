package li.netcube.mcvm.util.vm;

import li.netcube.mcvm.MCVM;
import li.netcube.mcvm.util.StreamGobbler;
import org.apache.commons.io.FileUtils;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.lang3.SystemUtils;
import org.lwjgl.Sys;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MachineManager {
    // Images



    public static boolean createHddImage(File img_file, String size) {
        Process process;
        Runtime r = Runtime.getRuntime();
        boolean success = false;
        try {
            List<String> commandLine = new ArrayList<String>();
            ProcessBuilder processBuilder = new ProcessBuilder();

            if (SystemUtils.IS_OS_WINDOWS) {
                commandLine.add("bin/qemu/qemu-img.exe");
            } else if (SystemUtils.IS_OS_LINUX) {
                commandLine.add("qemu-img");
            }

            commandLine.add("create");
            commandLine.add("-f");
            commandLine.add("qcow2");
            commandLine.add(img_file.toString());
            commandLine.add(size);

            String[] aCommandLine = new String[commandLine.size()];
            aCommandLine = commandLine.toArray(aCommandLine);

            File gameFolder = new File(new File("./").getAbsolutePath()).getParentFile();

            if (SystemUtils.IS_OS_WINDOWS || SystemUtils.IS_OS_LINUX) {
                //this.process = Runtime.getRuntime().exec(String.join(" ", commandLine), null, gameFolder);
                process = Runtime.getRuntime().exec(aCommandLine, null, gameFolder);
                StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream());
                StreamGobbler outputGobbler = new StreamGobbler(process.getInputStream());
                errorGobbler.start();
                outputGobbler.start();
                process.waitFor();
            }
            success = img_file.exists();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }

        return success;
    }

    public static boolean createFloppyImage(File img_file) {
        Runtime r = Runtime.getRuntime();
        boolean success = false;
        File blank_img = new File("resource/blank.img");
        try {
            FileUtils.copyFile(blank_img, img_file);
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }

    public static boolean checkImage(File img_file) {
        return img_file.exists();
    }

    // End Images


    // Image Tools

    public static File getImageFile(String filename, String type, MinecraftServer server) {
        if (server.isDedicatedServer()) {
            return new File(server.getFolderName() + "/" + filename + type);
        } else {
            return new File("saves/" + server.getFolderName() + "/" + filename + type);
        }
    }

    // End Image Tools


    // Machines

    public static void startMachine(VirtualMachine vm) {
        vm.start();
    }

    public static void stopMachine(VirtualMachine vm) {
        vm.stop();
    }

    // End Machines

}
