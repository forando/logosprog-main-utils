/*
 * Copyright (c) 2015. This code is a LogosProg property. All Rights Reserved.
 */

package files.xml.parser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Created by forando on 16.06.15.<br>
 *     This class parses .xml using org.w3c.dom library
 */
public class NodeManager {

    /**
     * This method is used to get a list of matched by tag name xml nodes.<br>
     * @param document org.w3c.dom {@link Document} object.
     * @param tagNames An array of node names, <b>nested each one in another</b>,
     *                 including the final one the returned nodes are named with.<br>
     *                 <b>IMPORTANT:</b> All parent nodes, the desired <b>nodeList</b> is nested in,
     *                 must have <b>only one representative of itself</b> in the xml document.
     *                 <br>Otherwise the first instance of each parent node will be picked
     *                 up for further searching.
     * @return A list of org.w3c.dom nodes.
     * @throws NullPointerException If tagNames array does not contain items or it's NULL.
     */
    public NodeList getNodeList(Document document, String... tagNames)throws NullPointerException{
        if (tagNames == null || tagNames.length <1) throw new NullPointerException("String[] tagNames");
        NodeList list = document.getDocumentElement().getElementsByTagName(tagNames[0]);
        int i = 1;
        while (tagNames.length < i){
            Element element = (Element)list.item(0);
            list = element.getElementsByTagName(tagNames[i]);
            ++i;
        }
        return list;
    }

    /**
     * This method is used to get a matched by tag name xml node.<br>
     * @param document org.w3c.dom {@link Document} object.
     * @param tagNames An array of node names, <b>nested each one in another</b>,
     *                 including the final one the returned node is named with.<br>
     *                 <b>IMPORTANT:</b> All parent nodes, the desired <b>node</b> is nested in,
     *                 must have <b>only one representative of itself</b> in the xml document.
     *                 <br>Otherwise the first instance of each parent node will be picked
     *                 up for further searching.
     * @return An {@link Element} object/node.
     * @throws NullPointerException If tagNames array does not contain items or it's NULL.
     */
    public Element getNode(Document document, String... tagNames)throws NullPointerException{

        return (Element)this.getNodeList(document, tagNames).item(0);
    }
}
