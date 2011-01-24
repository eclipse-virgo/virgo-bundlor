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
import java.util.Set;

import org.eclipse.virgo.bundlor.support.ArtifactAnalyzer;
import org.eclipse.virgo.bundlor.support.partialmanifest.PartialManifest;
import org.eclipse.virgo.bundlor.util.ClassNameUtils;
import org.eclipse.virgo.util.math.Sets;

/**
 * An analyzer for a Hibernate Mapping file. Analyzes the list of package names that are found in the
 * <ul>
 * <li><code>class</code></li>
 * <li><code>id</code></li>
 * <li><code>generator</code></li>
 * <li><code>composite-id</code></li>
 * <li><code>discriminator</code></li>
 * <li><code>version</code></li>
 * <li><code>property</code></li>
 * <li><code>many-to-one</code></li>
 * <li><code>one-to-one</code></li>
 * <li><code>one-to-many</code></li>
 * <li><code>many-to-many</code></li>
 * <li><code>component</code></li>
 * <li><code>dynamic-component</code></li>
 * <li><code>subclass</code></li>
 * <li><code>joined-subclass</code></li>
 * <li><code>union-subclass</code></li>
 * <li><code>import</code></li>
 * </ul>
 * elements.
 * <p />
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Threadsafe.
 * 
 * @author Ben Hale
 */
public final class HibernateMappingArtifactAnalyzer implements ArtifactAnalyzer {

    private static final Set<String> BASIC_HIBERNATE_TYPES = Sets.asSet(//
        "integer",//
        "long",//
        "short",//
        "float",//
        "double",//
        "character",//
        "byte",//
        "boolean",//
        "yes_no",//
        "true_false",//
        "string",//
        "date",//
        "time",//
        "timestamp",//
        "calendar",//
        "calendar_date",//        
        "big_decimal",//
        "big_integer",//
        "locale",//
        "timezone",//
        "currency",//
        "class",//
        "binary",//
        "text",//
        "serializable",//
        "clob",//
        "blob",//
        "imm_date",//
        "imm_time",//
        "imm_timestamp",//
        "imm_calendar",//
        "imm_calendar_date",//
        "imm_serializable",//
        "imm_binary");

    private static final Set<String> GENERATOR_TYPES = Sets.asSet(//
        "increment",//
        "identity",//
        "sequence",//
        "hilo",//
        "seqhilo",//
        "uuid",//
        "guid",//
        "native",//
        "assigned",//
        "select",//
        "foreign",//
        "sequence-identity");

    private static final String PACKAGE_EXPRESSION = "//hibernate-mapping/@package";

    private static final String CLASS_EXPRESSION = //
    "//class/@name | " + //
        "//id/@type | " + //
        "//generator/@class | " + //
        "//composite-id/@class | " + //
        "//discriminator/@type | " + //
        "//version/@type | " + //
        "//property/@type | " + //
        "//many-to-one/@class | " + //
        "//one-to-one/@class | " + //
        "//one-to-many/@class | " + //
        "//many-to-many/@class | " + //
        "//component/@class | " + //
        "//dynamic-component/@class | " + //
        "//subclass/@name | " + //
        "//joined-subclass/@name | " + //
        "//union-subclass/@name | " + //
        "//import/@class";

    public void analyse(InputStream artifact, String artifactName, PartialManifest partialManifest) throws Exception {
        XmlArtifactAnalyzer analyzer = new StandardXmlArtifactAnalyzer(artifact);
        PackagePrefixValueAnalyzer packagePrefixValueAnalyzer = new PackagePrefixValueAnalyzer();
        analyzer.analyzeValues(PACKAGE_EXPRESSION, packagePrefixValueAnalyzer);
        analyzer.analyzeValues(CLASS_EXPRESSION, new HibernateValueAnalyzer(packagePrefixValueAnalyzer.getPackagePrefix(), partialManifest));
    }

    public boolean canAnalyse(String artefactName) {
        return artefactName.endsWith(".hbm");
    }

    private static class PackagePrefixValueAnalyzer implements ValueAnalyzer {

        private volatile String packagePrefix = "";

        public void analyse(String value) {
            this.packagePrefix = value;
        }

        public String getPackagePrefix() {
            return this.packagePrefix;
        }
    }

    private static class HibernateValueAnalyzer implements ValueAnalyzer {

        private final String packagePrefix;

        private final PartialManifest partialManifest;

        public HibernateValueAnalyzer(String packagePrefix, PartialManifest partialManifest) {
            this.packagePrefix = packagePrefix;
            this.partialManifest = partialManifest;
        }

        public void analyse(String value) {
            if (!BASIC_HIBERNATE_TYPES.contains(value) && !GENERATOR_TYPES.contains(value)) {
                String candidateType;
                if (value.contains(".")) {
                    candidateType = value.trim();
                } else {
                    candidateType = packagePrefix + "." + value.trim();
                }
                if (ClassNameUtils.isValidFqn(candidateType)) {
                    partialManifest.recordReferencedType(candidateType);
                }
            }
        }

    }

}
