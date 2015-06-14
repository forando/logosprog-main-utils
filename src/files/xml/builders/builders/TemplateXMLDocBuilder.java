/*
 * Copyright (c) 2015. This code is a LogosProg property. All Rights Reserved.
 */

package files.xml.builders.builders;

import files.xml.builders.XMLDataFileManager;
import org.w3c.dom.Document;
import system.ConsoleMessage;

import java.io.IOException;

/**
 * Created by forando on 14.06.15.<br>
 * This class constructs external data.xml files <b>from default
 * template files</b>.
 */
public class TemplateXMLDocBuilder implements XMLDocBuilder {
    Document doc = null;

    XMLDataFileManager manager;

    public TemplateXMLDocBuilder(String fileName,String rootDir, String subDir) throws IOException{
            manager = new XMLDataFileManager(fileName, rootDir, subDir);
    }


    @Override
    public Document build() throws IOException {

        if (manager.fileExists()){
            doc = manager.getDocument();
        }else{
            boolean created;
            created = manager.generateDefaultFile();
            if (created){
                doc = manager.getDocument();
            }else {
                ConsoleMessage.printErrorMessage("An error occurred during " + manager.getFileName() + " file creation.");
            }
        }

        return null;
    }

    protected Document getXMLDocument(){
        return doc;
    }

    protected XMLDataFileManager getManager(){
        return manager;
    }
}
