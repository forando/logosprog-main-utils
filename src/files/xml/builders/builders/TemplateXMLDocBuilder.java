/*
 * Copyright (c) 2015. This code is a LogosProg property. All Rights Reserved.
 */

package files.xml.builders.builders;

import files.xml.builders.XMLDataFileManager;
import org.w3c.dom.Document;

import java.io.IOException;

/**
 * Created by forando on 14.06.15.<br>
 * This class constructs external data.xml files <b>from default
 * template files</b>.
 */
public class TemplateXMLDocBuilder implements XMLDocBuilder {
    protected Document doc = null;

    /**
     * The name of a default data.xml file to be used for external file creation.<br>
     *     <b>Attention!</b> this name must be exactly the same as the
     *     name of the default file.
     */
    private String fileName;

    /**
     * The complete path to root directory.
     */
    protected String rootDir;

    /**
     * The subdirectory to root directory where the file has to be put.
     */
    private String subDir;

    public TemplateXMLDocBuilder(String fileName,String rootDir, String subDir) {
        this.fileName = fileName;
        this.rootDir = rootDir;
        this.subDir = subDir;
    }


    @Override
    public Document build() throws IOException {
        XMLDataFileManager manager = new XMLDataFileManager(fileName,rootDir, subDir);

        if (manager.fileExists()){
            doc = manager.getDocument();
        }else{
            boolean created;
            created = manager.generateDefaultFile();
            if (created){
                doc = manager.getDocument();
            }else {
                APP.printErrorMessage("An error occurred during " + fileName + " file creation.");
            }
        }

        return null;
    }
}
