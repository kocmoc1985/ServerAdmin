/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import statics.HelpMy;

/**
 *
 * @author KOCMOC
 */
public class MyZip {

    public static void main(String[] args) {
        zipDirectory("lib");
    }

    public static void zipFile(String path) {
        //
        String outputPath = path + ".zip";
        //
        try {
            zip_file(path, outputPath);
            HelpMy.renameFile(outputPath, outputPath);
        } catch (IOException ex) {
            Logger.getLogger(MyZip.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void zip_file(String pathtxt, String pathgz) throws IOException {
        //
        FileInputStream in = null;
        GZIPOutputStream out = null;
        //
        try {
            in = new FileInputStream(pathtxt);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MyZip.class.getName()).log(Level.SEVERE, null, ex);
        }
        //
        try {
            out = new GZIPOutputStream(new FileOutputStream(pathgz));
        } catch (IOException ex) {
            Logger.getLogger(MyZip.class.getName()).log(Level.SEVERE, null, ex);
        }
        //
        byte[] buffer = new byte[4096];
        int bytesRead;
        //
        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }
        //
        in.close();
        out.close();
    }

    public static void zipDirectory(String path) {
        //
        String outputPath = path + ".zip";
        //
        try {
            zipDirectory(path, outputPath);
        } catch (IOException | IllegalArgumentException ex) {
            Logger.getLogger(MyZip.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void zipDirectory(String dir, String zipfile)
            throws IOException, IllegalArgumentException {
        // Check that the directory is a directory, and get its contents
        File d = new File(dir);
        if (!d.isDirectory()) {
            throw new IllegalArgumentException("Not a directory:  "
                    + dir);
        }
        String[] entries = d.list();
        byte[] buffer = new byte[4096]; // Create a buffer for copying
        int bytesRead;

        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipfile));

        for (int i = 0; i < entries.length; i++) {
            File f = new File(d, entries[i]);
            if (f.isDirectory()) {
                continue;//Ignore directory
            }
            FileInputStream in = new FileInputStream(f); // Stream to read file
            ZipEntry entry = new ZipEntry(f.getPath()); // Make a ZipEntry
            out.putNextEntry(entry); // Store entry
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            in.close();
        }
        out.close();
        //Compress.zipDirectory(from, from + ".zip");
    }

    public static void unzip(String path) throws FileNotFoundException, IOException {
        String source = path;
        GZIPInputStream in = new GZIPInputStream(new FileInputStream(source));
        String target = path.replaceAll(".zip", "");
        OutputStream out = new FileOutputStream(target);
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

}
