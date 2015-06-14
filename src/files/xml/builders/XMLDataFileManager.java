/*
 * Copyright (c) 2015. This code is a LogosProg property. All Rights Reserved.
 */

package files.xml.builders;

import files.SystemFileManager;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by forando on 14.06.15.<br>
 * This class provides basic operations with *.xml files that are
 * used by each module (application).
 */
public class XMLDataFileManager extends SystemFileManager {


    Document document = null;

    public XMLDataFileManager(String fileName, String rootDir, String... subDir) throws IOException {
        super(fileName, rootDir, subDir);


    }

    public Document getDocument() throws IOException {
        if (fileExists()){
            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
            f.setValidating(false);
            DocumentBuilder builder = null;
            try {
                builder = f.newDocumentBuilder();
                document = builder.parse(new File(getFilePath()));
            } catch (ParserConfigurationException | SAXException e) {
                e.printStackTrace();
                throw new IOException("File: " + getFilePath() + " not found");
            }
        }else{
            throw new IOException("File: " + getFilePath() + " not found");
        }

        return document;
    }

    public void saveDocument(Document document)throws IOException{
        DOMSource source = new DOMSource(document);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = transformerFactory.newTransformer();
            StreamResult result = new StreamResult(getFilePath());
            transformer.transform(source, result);
        } catch (TransformerException e) {
            e.printStackTrace();
            throw new IOException("There was an error during xml saving");
        }
    }

    public String getFileName(){
        return fileName;
    }
}
