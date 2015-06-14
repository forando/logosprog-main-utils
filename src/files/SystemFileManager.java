/*
 * Copyright (c) 2015. This code is a LogosProg property. All Rights Reserved.
 */

package files;

import java.io.*;

/**
 * Created by forando on 14.06.15.<br>
 * This class provides basic static operations with files that are
 * used by each module/application. That's why they are called
 * system files.
 */
public class SystemFileManager {
    public static boolean systemFileExists(String fileName, String rootDir, String... subDIR){

        if (null != subDIR && subDIR.length > 0){
            return (new File(SystemFileManager.getFilePath(fileName, subDIR[0]))).exists();
        }else{
            return (new File(SystemFileManager.getFilePath(fileName, rootDir))).exists();
        }
    }

    /**
     * This method constructs whole path to desired system file.<br>
     *     This is where you can change the default location of all
     *     files of your application.
     * @param fileName The system file name
     * @param rootDir The complete path to root directory.
     * @param subDIR Optional. The subdirectory to root directory.
     *               if it's given, then it will be included in the fileName path
     * @return Constructed path to the requested file
     */
    protected static String getFilePath(String fileName, String rootDir, String... subDIR){
        if (null != subDIR && subDIR.length > 0){
            return rootDir + File.separator + subDIR[0] + File.separator + fileName;
        }else{
            return rootDir + File.separator + fileName;
        }

    }

    /**
     * Creates parent DIR if it does not exist and than creates
     * an empty file
     * @param path Complete path including file name with extension
     * @return True - if operation is successful
     */
    public static boolean createEmptyFile(String path){

        boolean result = false;
        boolean dirCreated = true;

        File f = new File(path);

        /*
        Checking if directory for files already exists
         */
        if (!f.getParentFile().exists()) dirCreated = f.getParentFile().mkdirs();

        try {
            result = dirCreated && f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * This method copies default file from internal app folders to specific
     * project folder.
     * @param fileName A name to be given to a new file.
     * @param rootDir The complete path to root directory.
     * @param subDIR Optional. The subdirectory to root directory.
     *               if it's given, then it will be included in the fileName path
     * @return True - if operation is successful.
     */
    public static boolean generateDefaultSystemFile(String fileName, String rootDir, String... subDIR){

        boolean result = false;

        InputStream is = null;
        OutputStream os = null;
        String path;

        if (null != subDIR && subDIR.length > 0) {
            path = SystemFileManager.getFilePath(fileName, subDIR[0]);
        }else{
            path = SystemFileManager.getFilePath(fileName, rootDir);
        }

        if (!SystemFileManager.createEmptyFile(path)) return false;

        try {

            is = SystemFileManager.class.getResourceAsStream(fileName);
            os = new FileOutputStream(path);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            result = true;
        }catch(IOException ex){
            ex.printStackTrace();
        }finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
