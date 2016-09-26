/*******************************************************************************
 * Copyright (c) 2008, 2010 VMware Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   VMware Inc. - initial contribution
 *******************************************************************************/

package org.eclipse.virgo.bundlor.support.contributors.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

final class StandardXmlArtifactAnalyzer implements XmlArtifactAnalyzer {

    private final Document document;

    private final NamespaceContext namespaceContext;

    StandardXmlArtifactAnalyzer(InputStream inputStream) throws ParserConfigurationException, SAXException, IOException {
        this(inputStream, null, null);
    }

    StandardXmlArtifactAnalyzer(InputStream inputStream, EntityResolver entityResolver)
        throws ParserConfigurationException, SAXException, IOException {
        this(inputStream, null, entityResolver);
    }

    StandardXmlArtifactAnalyzer(InputStream inputStream, Map<String, String> namespaceMappings)
        throws ParserConfigurationException, SAXException, IOException {
        this(inputStream, namespaceMappings, null);
    }

    StandardXmlArtifactAnalyzer(InputStream inputStream, Map<String, String> namespaceMapping, EntityResolver entityResolver)
        throws ParserConfigurationException, SAXException, IOException {
        this.namespaceContext = getNamespaceContext(namespaceMapping);
        this.document = getDocument(inputStream, entityResolver);
    }

    public void analyzeValues(String expression, ValueAnalyzer analyzer) throws Exception {
        NodeList nodes = (NodeList) getXPathExpression(expression).evaluate(this.document, XPathConstants.NODESET);

        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
                analyzeRawValue(((Attr) node).getValue(), analyzer);
            } else if (node.getNodeType() == Node.ELEMENT_NODE) {
                analyzeRawValue(((Element) node).getTextContent(), analyzer);
            }
        }
    }

    private void analyzeRawValue(String rawValue, ValueAnalyzer analyzer) {
        if (rawValue.contains(",")) {
            for (String componentValue : rawValue.split(",")) {
                analyzer.analyse(componentValue.trim());
            }
        } else {
            analyzer.analyse(rawValue.trim());
        }
    }

    private final XPathExpression getXPathExpression(String expressionString) throws XPathExpressionException {
        XPathFactory newInstance = XPathFactory.newInstance();
        XPath xpath = newInstance.newXPath();
        if (this.namespaceContext != null) {
            xpath.setNamespaceContext(this.namespaceContext);
        }
        return xpath.compile(expressionString);
    }

    private NamespaceContext getNamespaceContext(Map<String, String> namespaceMapping) {
        if (namespaceMapping != null) {
            return new MapNamespaceContext(namespaceMapping);
        }
        return null;
    }

    private boolean isNamespaceAware() {
        return this.namespaceContext != null;
    }

    private Document getDocument(InputStream inputStream, EntityResolver entityResolver)
        throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory xmlFact = DocumentBuilderFactory.newInstance();
        xmlFact.setNamespaceAware(isNamespaceAware());

        if (System.getProperty("not-load-external-dtd") != null) {
            xmlFact.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        }

        DocumentBuilder builder = xmlFact.newDocumentBuilder();
        if (entityResolver != null) {
            builder.setEntityResolver(entityResolver);
        }

        return builder.parse(new InputSource(inputStream));
    }

    private static final class MapNamespaceContext implements NamespaceContext {

        private final Map<String, String> namespaceMapping;

        public MapNamespaceContext(Map<String, String> namespaceMapping) {
            this.namespaceMapping = namespaceMapping;
        }

        public String getNamespaceURI(String prefix) {
            if (namespaceMapping != null && namespaceMapping.containsKey(prefix)) {
                return namespaceMapping.get(prefix);
            }
            return XMLConstants.NULL_NS_URI;
        }

        public String getPrefix(String namespace) {
            if (namespaceMapping != null && namespaceMapping.containsValue(namespace)) {
                for (Map.Entry<String, String> entry : namespaceMapping.entrySet()) {
                    if (namespace.equals(entry.getValue())) {
                        return entry.getKey();
                    }
                }
            }
            return null;
        }

        @SuppressWarnings({ "rawtypes" })
        public Iterator getPrefixes(String namespace) {
            return null;
        }
    }
}
