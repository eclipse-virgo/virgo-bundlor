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

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.virgo.bundlor.support.ArtifactAnalyzer;
import org.eclipse.virgo.bundlor.support.partialmanifest.PartialManifest;

/**
 * An analyzer for an Application Context located in a JAR file. Analyzes the list of package names that are found in
 * bean declaration <code>class</code> attributes.
 * <p />
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Not threadsafe.
 * 
 * @author Ben Hale
 */
public final class SpringApplicationContextArtifactAnalyzer implements ArtifactAnalyzer {

    private static final Map<String, String> NAMESPACE_MAPPING;

    static {
        NAMESPACE_MAPPING = new HashMap<String, String>();
        NAMESPACE_MAPPING.put("beans", "http://www.springframework.org/schema/beans");
        NAMESPACE_MAPPING.put("aop", "http://www.springframework.org/schema/aop");
        NAMESPACE_MAPPING.put("context", "http://www.springframework.org/schema/context");
        NAMESPACE_MAPPING.put("jee", "http://www.springframework.org/schema/jee");
        NAMESPACE_MAPPING.put("jms", "http://www.springframework.org/schema/jms");
        NAMESPACE_MAPPING.put("lang", "http://www.springframework.org/schema/lang");
        NAMESPACE_MAPPING.put("jms", "http://www.springframework.org/schema/jms");
        NAMESPACE_MAPPING.put("util", "http://www.springframework.org/schema/util");
        NAMESPACE_MAPPING.put("oxm", "http://www.springframework.org/schema/oxm");

        NAMESPACE_MAPPING.put("osgi", "http://www.springframework.org/schema/osgi");

        NAMESPACE_MAPPING.put("webflow", "http://www.springframework.org/schema/webflow-config");
    }

    private static final String PACKAGE_EXPRESSION = // 
    "//context:component-scan/@base-package";

    private static final String CLASS_EXPRESSION = //
    "//beans:bean/@class | " + //
        "//aop:declare-parents/@implement-interface | " + //
        "//aop:declare-parents/@default-impl | " + //
        "//context:load-time-weaver/@weaver-class | " + //
        "//context:component-scan/@name-generator | " + //
        "//context:component-scan/@scope-resolver | " + //
        "//jee:jndi-lookup/@expected-type | " + //
        "//jee:jndi-lookup/@proxy-interface | " + //
        "//jee:remote-slsb/@home-interface | " + //
        "//jee:remote-slsb/@business-interface | " + //
        "//jee:local-slsb/@business-interface | " + //
        "//jms:listener-container/@container-class | " + //
        "//lang:jruby/@script-interfaces | " + //
        "//lang:bsh/@script-interfaces | " + //
        "//oxm:class-to-be-bound/@name | " + //
        "//oxm:jibx-marshaller/@target-class | " + //
        "//osgi:reference/@interface | " + //
        "//osgi:service/@interface | " + //
        "//util:list/@list-class | " + //
        "//util:map/@map-class | " + //
        "//util:set/@set-class | " + //
        "//webflow:flow-builder/@class | " + //
        "//webflow:attribute/@type | " + //
        "//osgi:service/osgi:interfaces/beans:value | " + //
        "//osgi:reference/osgi:interfaces/beans:value";

    public void analyse(InputStream artifact, String artifactName, PartialManifest partialManifest) throws Exception {
        XmlArtifactAnalyzer analyzer = new StandardXmlArtifactAnalyzer(artifact, NAMESPACE_MAPPING);
        analyzer.analyzeValues(PACKAGE_EXPRESSION, new AllPackagesValueAnalyzer(partialManifest));
        analyzer.analyzeValues(CLASS_EXPRESSION, new AllClassesValueAnalyzer(partialManifest));
    }

    public boolean canAnalyse(String artifactName) {
        return artifactName.endsWith(".xml");
    }

}
