/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

/*
 * Copyright (c) 2016. This code is a LogosProg property. All Rights Reserved.
 */

@file:Suppress("MemberVisibilityCanPrivate", "unused")

package com.logosprog.mainutils.files.xml.parser

import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList

/**
 * Parses .xml using org.w3c.dom library
 */
class NodeManager {

    /**
     * This method is used to get a list of matched by tag name xml nodes **from the entire Document**.
     * @param document org.w3c.dom [Document] object.
     *
     * @param nodeNames An array of node names, **nested each one in another**,
     *                  including the final one the returned nodes are named with.
     *                  **IMPORTANT:** All parent nodes, the desired **nodeList** is nested in,
     *                  must have **only one representative of itself** in the xml document.
     *                  Otherwise the first instance of each parent node will be picked
     *                  up for the further searching.
     *
     * @return A list of org.w3c.dom nodes.
     *
     * @throws NullPointerException If nodeNames array does not contain items or it's NULL.
     */
    @Throws(NullPointerException::class)
    fun getNodeList(document: Document, vararg nodeNames: String): NodeList? {
        if (nodeNames.isEmpty()) throw NullPointerException("String[] nodeNames")
        var list = document.documentElement.getElementsByTagName(nodeNames[0]) ?: return null
        var i = 1
        while (i < nodeNames.size) {
            val element = list.item(0) as Element
            list = element.getElementsByTagName(nodeNames[i])
            ++i
        }
        return list
    }

    /**
     * This method is used to get a list of matched by tag name xml nodes **from the defined Node**.
     * @param node org.w3c.dom [Node] object.
     *
     * @param nodeNames An array of node names, **nested each one in another**,
     *                  including the final one the returned nodes are named with.
     *                  **IMPORTANT:** All parent nodes, the desired **nodeList** is nested in,
     *                  must have **only one representative of itself** in the xml node.
     *                  Otherwise the first instance of each parent node will be picked
     *                  up for the further searching.
     *
     * @return A list of org.w3c.dom nodes.
     * @throws NullPointerException If nodeNames array does not contain items or it's NULL.
     */
    @Throws(NullPointerException::class)
    fun getNodeList(node: Node, vararg nodeNames: String): NodeList? {
        if (nodeNames.isEmpty()) throw NullPointerException("String[] nodeNames")
        //checking if node can be cast to Element
        if (node.nodeType != Node.ELEMENT_NODE) return null
        val nodeElement = node as Element
        var list = nodeElement.getElementsByTagName(nodeNames[0]) ?: return null
        var i = 1
        while (i < nodeNames.size) {
            if (list.item(0) == null) return null
            val element = list.item(0) as Element
            list = element.getElementsByTagName(nodeNames[i])
            ++i
        }
        return list
    }

    /**
     * This method is used to get a matched by tag name xml node.
     * @param node org.w3c.dom [Node] object.
     * @param nodeNames An array of node names, **nested each one in another**,
     *                  including the final one the returned node is named with.
     *                  **IMPORTANT:** All parent nodes, the desired **node** is nested in,
     *                  must have **only one representative of itself** in the xml document.
     *                  Otherwise the first instance of each parent node will be picked
     *                  up for the further searching.
     *
     * @return An [Element] object/node.
     * @throws NullPointerException If nodeNames array does not contain items or it's NULL.
     */
    @Throws(NullPointerException::class)
    fun getNode(node: Node, vararg nodeNames: String): Element? {
        val nodeElement = this.getNodeList(node, *nodeNames)?.item(0) ?: return null
        //checking if node can be cast to Element
        return if (nodeElement.nodeType == Node.ELEMENT_NODE)
            nodeElement as Element
        else
            null
    }

    /**
     * This method is used to get a matched by tag name xml node.
     * @param document org.w3c.dom [Document] object.
     * @param nodeNames An array of node names, **nested each one in another**,
     *                  including the final one the returned node is named with.
     *                  **IMPORTANT:** All parent nodes, the desired **node** is nested in,
     *                  must have **only one representative of itself** in the xml document.
     *                  Otherwise the first instance of each parent node will be picked
     *                  up for the further searching.
     *
     * @return An [Element] object/node.
     * @throws NullPointerException If nodeNames array does not contain items or it's NULL.
     */
    @Throws(NullPointerException::class)
    fun getNode(document: Document, vararg nodeNames: String): Element? {
        val nodeElement = this.getNodeList(document, *nodeNames)?.item(0) ?: return null
        //checking if node can be cast to Element
        return if (nodeElement.nodeType == Node.ELEMENT_NODE)
            nodeElement as Element
        else
            null
    }

    /**
     * Removes all Element Nodes of a given name from any given Node (lets call it **rootNode** for this method).
     * @param document org.w3c.dom [Document] object.
     *
     * @param nodeNameToRemove Only nodes of the given name will be removed from the rootNode
     *
     * @param nodeNames An array of node names, **nested each one in another**,
     *                  including the final, which is rootNode name.
     *                  **IMPORTANT:** All parent nodes, the desired **rootNode** is nested in,
     *                  must have **only one representative of itself** in the xml document.
     *                  Otherwise the first instance of each parent node will be picked
     *                  up for the further searching
     *
     * @return The empty **rootNode**
     *
     * @throws NullPointerException If nodeNames array does not contain items or it's NULL.
     */
    @Throws(NullPointerException::class)
    fun removeAllInNode(document: Document, nodeNameToRemove: String, vararg nodeNames: String): Node? {
        val rootNode = getNode(document, *nodeNames)
        removeAll(rootNode, Node.ELEMENT_NODE, nodeNameToRemove)
        removeAll(rootNode, Node.TEXT_NODE, nodeNameToRemove)
        removeAll(rootNode, Node.COMMENT_NODE, nodeNameToRemove)
        rootNode?.textContent = ""
        document.normalize()
        return rootNode
    }

    /**
     * Removes all child nodes of a defined type from the given Parent Node
     * **It's recommended** to call **document.normalize()** right after this method invocation.
     * See: [Normalization in DOM parsing with java - how does it work?](http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work)
     * @param node The Parent Node all child nodes must be removed from
     *
     * @param nodeType Only nodes of this type will be removed from the Parent Node
     *
     * @param name Only nodes of the given name will be removed from the Parent Node
     */
    fun removeAll(node: Node?, nodeType: Short, name: String?) {
        if (node == null) return
        if (node.nodeType == nodeType && (name == null || node.nodeName == name)) {
            node.parentNode.removeChild(node)
        } else {
            val list = node.childNodes
            for (i in 0 until list.length) {
                removeAll(list.item(i), nodeType, name)
            }
        }
    }

    /**
     * Creates node like: **&lt;nodeName&gt; text &lt;/nodeName&gt;**
     * @param document org.w3c.dom [Document] object.
     *
     * @param nodeName A name to be given to the node
     *
     * @param text A text to be inserted in the node
     *
     * @return Constructed [Element] object
     */
    fun createSimpleElementNode(document: Document, nodeName: String, text: String): Node {
        val node = document.createElement(nodeName)
        node.textContent = text
        return node
    }
}
