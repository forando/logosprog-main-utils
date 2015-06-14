/*
 * Copyright (c) 2015. This code is a LogosProg property. All Rights Reserved.
 */

package files.xml.builders;

import files.TemplateFileBuilder;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import system.ConsoleMessage;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by forando on 14.06.15.<br>
 * This class constructs external data.xml files <b>from default
 * template files</b>.
 */
public class TemplateXMLBuilder extends TemplateFileBuilder<Document> {

    //Document document = null;

    //XMLDataFileManager manager;

    public TemplateXMLBuilder(String fileName, String rootDir, String subDir) throws IOException {
        super(fileName, rootDir, subDir);
    }

    @Override
    public Document build(InputStream is) throws IOException {
        if (fileExists()){
            mainObj = getObjectFromExternalFile();
        }else{
            boolean created = generateDefaultFile(is);
            if (created){
                mainObj = getObjectFromExternalFile();
            }else {
                ConsoleMessage.printErrorMessage("An error occurred during " + getFileName() + " file creation.");
            }
        }
        return mainObj;
    }

    @Override
    protected Document getObjectFromExternalFile() throws IOException {
        Document doc = null;
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        f.setValidating(false);
        DocumentBuilder builder = null;
        try {
            builder = f.newDocumentBuilder();
            doc = builder.parse(new File(getFilePath()));
        } catch (ParserConfigurationException | SAXException e) {
            e.printStackTrace();
            throw new IOException("File: " + getFilePath() + " not found");
        }
        return doc;
    }

    @Override
    protected void setMainObject()throws IOException{
        DOMSource source = new DOMSource(mainObj);
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

    public void saveXMLDocument() throws IOException {
        setMainObject();
    }

    /**
     * <b>Attention!!!</b> you must be sure that {@link #build(InputStream)} method
     * has been invoked before this one.
     * @return {@link Document} object
     */
    public Document getXMLDocument(){
        return getMainObject();
    }
}
