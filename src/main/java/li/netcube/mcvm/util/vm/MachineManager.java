package li.netcube.mcvm.util.vm;

import li.netcube.mcvm.MCVM;
import org.apache.commons.io.FileUtils;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.lang3.SystemUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class MachineManager {
    // Images



    public static boolean createHddImage(File img_file, String size) {
        Process process;
        Runtime r = Runtime.getRuntime();
        boolean success = false;
        try {
            ProcessBuilder builder = new ProcessBuilder();
            File gameFolder = new File(".");
            if (SystemUtils.IS_OS_WINDOWS) {
                process = Runtime.getRuntime().exec("bin\\qemu\\qemu-img.exe create -f qcow2 " + "\"" + img_file + "\" " + size, null, gameFolder);
                process.waitFor();
            } else if (SystemUtils.IS_OS_LINUX) {
                process = Runtime.getRuntime().exec("qemu-img create -f qcow2 \"" + img_file + "\" " + size, null, gameFolder);
                process.waitFor();
            }
            //builder.redirectOutput(new File("syslog.txt"));
            //builder.redirectError(new File("errlog.txt"));

            success = img_file.exists();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
