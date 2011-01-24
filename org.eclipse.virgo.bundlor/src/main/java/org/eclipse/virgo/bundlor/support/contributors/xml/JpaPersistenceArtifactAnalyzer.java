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

import org.eclipse.virgo.bundlor.support.ArtifactAnalyzer;
import org.eclipse.virgo.bundlor.support.partialmanifest.PartialManifest;
import org.eclipse.virgo.bundlor.util.ClassNameUtils;

/**
 * An analyzer for JPA <code>persistence.xml</code> and <code>orm.xml</code> files.
 * <p />
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Threadsafe.
 * 
 * @author Ben Hale
 */
public final class JpaPersistenceArtifactAnalyzer implements ArtifactAnalyzer {

    private static final String ORM_FILE = "META-INF/orm.xml";

    private static final String PERSISTENCE_FILE = "META-INF/persistence.xml";

    private static final String ORM_PACKAGE_EXPRESSION = "//entity-mappings/package";

    private static final String ORM_CLASS_EXPRESSION = //
    "//element-collection/@target-class | " + //
        "//embeddable/@class | " + //
        "//entity/@class | " + //
        "//entity-listener/@class | " + //
        "//entity-result/@entity-class | " + //
        "//id-class/@class | " + //
        "//many-to-many/@target-entity | " + //
        "//many-to-one/@target-entity | " + //
        "//map-key-class/@class | " + //
        "//mapped-superclass/@class | " + //
        "//named-native-query/@result-class | " + //
        "//one-to-many/@target-entity | " + //
        "//one-to-one/@target-entity";

    private static final String PERSISTENCE_EXPRESSION = "//persistence-unit/provider | //persistence-unit/class";

    public void analyse(InputStream artifact, String artifactName, PartialManifest partialManifest) throws Exception {
        StandardXmlArtifactAnalyzer analyzer = new StandardXmlArtifactAnalyzer(artifact);
        if (ORM_FILE.equals(artifactName)) {
            PackagePrefixValueAnalyzer packagePrefixValueAnalyzer = new PackagePrefixValueAnalyzer();
            analyzer.analyzeValues(ORM_PACKAGE_EXPRESSION, packagePrefixValueAnalyzer);
            analyzer.analyzeValues(ORM_CLASS_EXPRESSION, new JpaValueAnalyzer(packagePrefixValueAnalyzer.getPackagePrefix(), partialManifest));
        } else if (PERSISTENCE_FILE.equals(artifactName)) {
            analyzer.analyzeValues(PERSISTENCE_EXPRESSION, new AllClassesValueAnalyzer(partialManifest));
        }
    }

    public boolean canAnalyse(String artifactName) {
        return PERSISTENCE_FILE.equals(artifactName) || ORM_FILE.equals(artifactName);
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

    private static class JpaValueAnalyzer implements ValueAnalyzer {

        private final String packagePrefix;

        private final PartialManifest partialManifest;

        public JpaValueAnalyzer(String packagePrefix, PartialManifest partialManifest) {
            this.packagePrefix = packagePrefix;
            this.partialManifest = partialManifest;
        }

        public void analyse(String value) {
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
