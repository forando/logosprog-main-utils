/*
 * Copyright (c) 2015. This code is a LogosProg property. All Rights Reserved.
 */

package files;

import java.io.*;

/**
 * Created by forando on 14.06.15.<br>
 * This class provides basic operations with files that are
 * used by each module/application. That's why they are called
 * system files.
 */
public class SystemFileManager {

    /**
     * A name to be given to a new file
     */
    protected String fileName;
    /**
     * The complete path to root directory.
     */
    protected String rootDir;
    /**
     * Optional. The subdirectory to root directory.
     *          if it's given, then it will be included in the fileName path
     */
    protected String subDir;

    public SystemFileManager(String fileName, String rootDir, String... subDir) throws IOException{
        if (fileName == null || rootDir == null || rootDir.isEmpty() || fileName.isEmpty())
            throw new IOException("You must pass in fileName and rootDir");
        this.fileName = fileName;
        this.rootDir = rootDir;
        if (null != subDir && subDir.length > 0){
            //next: proceed with multiple subdirectories
            this.subDir = subDir[0];
        }
    }

    public String getFileName(){
        return fileName;
    }


    public boolean fileExists(){

        if (null != subDir){
            return (new File(getFilePath())).exists();
        }else{
            return (new File(getFilePath())).exists();
        }
    }

    /**
     * This method constructs whole path to desired system file.<br>
     *     This is where you can change the default location of all
     *     files of your application.
     *     if it's given, then it will be included in the fileName path
     * @return Constructed path to the requested file
     */
    protected String getFilePath(){
        if (null != subDir){
            return rootDir + File.separator + subDir + File.separator + fileName;
        }else{
            return rootDir + File.separator + fileName;
        }

    }

    /**
     * Creates parent DIR if it does not exist and than creates
     * an empty file
     * @return True - if operation is successful
     */
    public boolean createEmptyFile(){

        boolean result = false;
        boolean dirCreated = true;

        File f = new File(getFilePath());

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


    public boolean deleteDefaultFile(){
        boolean result = false;

        File f = new File(getFilePath());
        try{
            if (f.delete()){
                result = true;
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return result;
    }

    /**
     * This method copies default file from internal app folders to specific
     * project folder.
     * @return True - if operation is successful.
     */
    public boolean generateDefaultFile(){

        boolean result = false;

        InputStream is = null;
        OutputStream os = null;
        String path;

        if (null != subDir) {
            path = getFilePath();
        }else{
            path = getFilePath();
        }

        if (!createEmptyFile()) return false;

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
