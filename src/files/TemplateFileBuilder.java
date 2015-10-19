/*
 * Copyright (c) 2015. This code is a LogosProg property. All Rights Reserved.
 */

package files;

import system.ConsoleMessage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by forando on 14.06.15.<br>
 * This class constructs external system files <b>from default
 * template files</b>.
 */
public abstract class TemplateFileBuilder<T> extends SystemFileManager implements ObjectFromFileBuilder<T, InputStream> {

    /**
     * The object of predefined type that we construct from external file
     */
    private T mainObj = null;


    /**
     *
     * @param fileName A file name that will be used to construct {@link T} object from
     * @param rootDir Application root directory
     * @param subDir Optional. A subdirectory name the file will be located
     * @throws IOException If either <b>fileName</b> or <b>rootDir</b> is null
     */
    public TemplateFileBuilder(String fileName, String rootDir, String subDir) throws IOException {
        super(fileName, rootDir, subDir);
    }

    @Override
    public T build(InputStream element) throws IOException {
        if (fileExists()){
            mainObj = getObjectFromExternalFile();
        }else{
            boolean created = generateDefaultFile(element);
            if (created){
                mainObj = getObjectFromExternalFile();
                if (listener != null) listener.onFileGenerated();
            }else {
                ConsoleMessage.printErrorMessage("An error occurred during " + getFileName() + " file creation.");
            }
        }
        return mainObj;
    }

    /**
     * Implement this method to construct for user a {@link T} object from external file
     * @return a {@link T} object
     * @throws IOException If {@link T} object construction failed.
     */
    protected abstract T getObjectFromExternalFile() throws IOException;

    /**
     * Implement this method to return for user {@link #mainObj} <b>if it's
     * been already created</b>
     * @return {@link #mainObj}
     */
    protected T getMainObject(){
        return mainObj;
    }

    /**
     * This method must be implemented in order to save a {@link T}
     * object back to external file.
     * @throws IOException If saving to external file failed.
     */
    protected abstract void setMainObject()throws IOException;

    /**
     * This method copies default file from internal app folders to specific
     * project folder.
     * @return True - if operation is successful.
     */
    protected boolean generateDefaultFile(InputStream is){

        boolean result = false;

        OutputStream os = null;
        String path;

        if (null != subDir) {
            path = getFilePath();
        }else{
            path = getFilePath();
        }

        if (!createEmptyFile()) return false;

        try {

            //is = SystemFileManager.class.getResourceAsStream(fileName);
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

    TemplateFileBuilderListener listener;

    public void addTemplateFileBuilderListener(TemplateFileBuilderListener listener){
        this.listener = listener;
    }


    public interface TemplateFileBuilderListener{
        /**
         * Notifies when the desired file has been just generated from the default template file.
         */
        void onFileGenerated();
    }
}
