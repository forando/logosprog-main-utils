/*
 * Copyright (c) 2015. This code is a LogosProg property. All Rights Reserved.
 */

package files.xml.parser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Created by forando on 16.06.15.<br>
 *     This class parses .xml using org.w3c.dom library
 */
public class NodeManager {

    /**
     * This method is used to get a list of matched by tag name xml nodes.<br>
     * @param document org.w3c.dom {@link Document} object.
     * @param nodeNames An array of node names, <b>nested each one in another</b>,
     *                 including the final one the returned nodes are named with.<br>
     *                 <b>IMPORTANT:</b> All parent nodes, the desired <b>nodeList</b> is nested in,
     *                 must have <b>only one representative of itself</b> in the xml document.
     *                 <br>Otherwise the first instance of each parent node will be picked
     *                 up for the further searching.
     * @return A list of org.w3c.dom nodes.
     * @throws NullPointerException If nodeNames array does not contain items or it's NULL.
     */
    public NodeList getNodeList(Document document, String... nodeNames)throws NullPointerException{
        if (nodeNames == null || nodeNames.length <1) throw new NullPointerException("String[] nodeNames");
        NodeList list = document.getDocumentElement().getElementsByTagName(nodeNames[0]);
        int i = 1;
        while (i < nodeNames.length){
            Element element = (Element)list.item(0);
            list = element.getElementsByTagName(nodeNames[i]);
            ++i;
        }
        return list;
    }

    /**
     * This method is used to get a matched by tag name xml node.<br>
     * @param document org.w3c.dom {@link Document} object.
     * @param nodeNames An array of node names, <b>nested each one in another</b>,
     *                 including the final one the returned node is named with.<br>
     *                 <b>IMPORTANT:</b> All parent nodes, the desired <b>node</b> is nested in,
     *                 must have <b>only one representative of itself</b> in the xml document.
     *                 <br>Otherwise the first instance of each parent node will be picked
     *                 up for the further searching.
     * @return An {@link Element} object/node.
     * @throws NullPointerException If nodeNames array does not contain items or it's NULL.
     */
    public Element getNode(Document document, String... nodeNames)throws NullPointerException{

        return (Element)this.getNodeList(document, nodeNames).item(0);
    }

    /**
     * Removes all Element Nodes of a given name from any given Node (lets call it <b>rootNode</b> for this method).
     * @param document org.w3c.dom {@link Document} object.
     * @param nodeNameToRemove Only nodes of the given name will be removed from the rootNode
     * @param nodeNames An array of node names, <b>nested each one in another</b>,
     *                 including the final, which is rootNode name.<br>
     *                 <b>IMPORTANT:</b> All parent nodes, the desired <b>rootNode</b> is nested in,
     *                 must have <b>only one representative of itself</b> in the xml document.
     *                 <br>Otherwise the first instance of each parent node will be picked
     *                 up for the further searching
     * @return The empty <b>rootNode</b>
     * @throws NullPointerException If nodeNames array does not contain items or it's NULL.
     */
    public Node removeAllInNode(Document document, String nodeNameToRemove, String... nodeNames)throws NullPointerException{
        Node rootNode = getNode(document, nodeNames);
        removeAll(rootNode, Node.ELEMENT_NODE, nodeNameToRemove);
        removeAll(rootNode, Node.TEXT_NODE, nodeNameToRemove);
        removeAll(rootNode, Node.COMMENT_NODE, nodeNameToRemove);
        rootNode.setTextContent("");
        document.normalize();
        return rootNode;
    }

    /**
     * Removes all child nodes of a defined type from the given Parent Node<br>
     *     <b>It's recommended</b> to call <b>document.normalize()</b> right after this method invocation.<br>
     *     See: <a href="http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work">Normalization in DOM parsing with java - how does it work?</a>
     * @param node The Parent Node all child nodes must be removed from
     * @param nodeType Only nodes of this type will be removed from the Parent Node
     * @param name Only nodes of the given name will be removed from the Parent Node
     */
    public void removeAll(Node node, short nodeType, String name) {
        if (node.getNodeType() == nodeType && (name == null || node.getNodeName().equals(name))) {
            node.getParentNode().removeChild(node);
        } else {
            NodeList list = node.getChildNodes();
            for (int i = 0; i < list.getLength(); i++) {
                removeAll(list.item(i), nodeType, name);
            }
        }
    }
}
