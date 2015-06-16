/*
 * Copyright (c) 2015. This code is a LogosProg property. All Rights Reserved.
 */

package files.xml.parser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Created by forando on 16.06.15.<br>
 *     This class parses .xml file and returns its elements
 */
public class NodeManager {
    Element rootElement;


    public NodeManager(Document doc) {
        this.rootElement = doc.getDocumentElement();
    }

    public NodeList getNodeList(String... tagNames)throws NullPointerException{
        if (tagNames == null) throw new NullPointerException("String[] tagNames");

        return null;
    }

    public Element getElement(String... tagNames)throws NullPointerException{
        if (tagNames == null) throw new NullPointerException("String[] tagNames");


        return null;
    }
}
