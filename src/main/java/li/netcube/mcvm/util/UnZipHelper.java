package li.netcube.mcvm.util;

import org.apache.commons.compress.utils.IOUtils;
import org.lwjgl.Sys;

import java.io.*;
import java.lang.reflect.Executable;
import java.nio.channels.FileChannel;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class UnZipHelper {
    private int progress;
    private int status;


    public UnZipHelper(File zipfile, File folder) {
        Thread unzipThread = new Thread() {
            public void run() {
                try{

                    if(!folder.exists()){
                        folder.mkdir();
                    }

                    int maxcount = 1;
                    int count = 1;

                    ZipFile zipFile = new ZipFile(zipfile);
                    try {
                        Enumeration<? extends ZipEntry> entries = zipFile.entries();
                        while (entries.hasMoreElements())
                        {
                            entries.nextElement();
                            maxcount++;
                        }

                        entries = zipFile.entries();
                        while (entries.hasMoreElements()) {
                            ZipEntry entry = entries.nextElement();
                            File entryDestination = new File(folder,  entry.getName());
                            progress = (maxcount/count)*100;
                            count++;
                            if (entryDestination.exists()) {
                                FileDeleteHelper.deleteDirectory(entryDestination);
                            }
                            if (entry.isDirectory()) {
                                entryDestination.mkdirs();
                            } else {
                                entryDestination.getParentFile().mkdirs();
                                InputStream in = zipFile.getInputStream(entry);
                                OutputStream out = new FileOutputStream(entryDestination);

                                byte[] buf = new byte[512];
                                int len = 0;
                                while ((len = in.read(buf)) >= 0)
                                {
                                    out.write(buf, 0, len);
                                }

                                in.close();
                                out.close();
                            }
                        }
                    } finally {
                        zipFile.close();
                    }

                    status = 1;
                }catch(IOException ex){
                    ex.printStackTrace();
                    status = -1;
                }
            }
        };
        status = 0;
        unzipThread.run();
    }

    public int getProgress() {
        return progress;
    }

    public int getStatus() {
        return status;
    }
}

