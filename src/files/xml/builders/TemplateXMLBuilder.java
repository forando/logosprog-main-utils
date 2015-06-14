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
public abstract class TemplateXMLBuilder extends TemplateFileBuilder<Document> {

    //Document document = null;

    //XMLDataFileManager manager;

    /**
     *
     * @param fileName A file name that will be used to construct {@link Document} object from
     * @param rootDir Application root directory
     * @param subDir Optional. A subdirectory name the file will be located
     * @param is A {@link InputStream} object of an internal template .xml file that will be
     *           used to generate default file from
     * @throws IOException If either <b>fileName</b> or <b>rootDir</b> or <b>is</b> is null
     */
    public TemplateXMLBuilder(String fileName, String rootDir, String subDir, InputStream is) throws IOException {
        super(fileName, rootDir, subDir);
        if (is == null) throw new IOException("InputStreame of an internal .xml template file is NULL!");
        this.build(is);
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
