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
 * An analyzer for an EclipseLink <code>eclipselink-orm.xml</code> file.
 * <p />
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Threadsafe.
 * 
 * @author Ben Hale
 */
public final class EclipseLinkPersistenceArtifactAnalyzer implements ArtifactAnalyzer {

    private static final String FILE = "META-INF/eclipselink-orm.xml";

    private static final String PACKAGE_EXPRESSION = "//entity-mappings/package";

    private static final String CLASS_EXPRESSION = //
        "//cache-interceptor/@class | " + //
        "//converter/@class | " + //
        "//copy-policy/@class | " + //
        "//customizer/@class | " + //
        "//discriminator-class/@value | " + //
        "//id-class/@class | " + //
        "//element-collection/@target-class | " + //
        "//entity/@class | " + //
        "//entity-listener/@class | " + //
        "//entity-result/@entity-class | " + //
        "//embeddable/@class | " + //
        "//many-to-many/@target-entity | " + //
        "//many-to-one/@target-entity | " + //
        "//map-key-class/@class | " + //
        "//mapped-superclass/@class | " + //
        "//named-native-query/@result-class | " + //
        "//named-stored-procedure-query/@result-class | " + //
        "//object-type-converter/@data-type | " + //
        "//object-type-converter/@object-type | " + //
        "//one-to-many/@target-entity | " + //
        "//one-to-one/@target-entity | " + //
        "//property/@value-type | " + //
        "//query-redirectors/@all-queries | " + //
        "//query-redirectors/@read-all | " + //
        "//query-redirectors/@read-object | " + //
        "//query-redirectors/@report | " + //
        "//query-redirectors/@update | " + //
        "//query-redirectors/@insert | " + //
        "//query-redirectors/@delete | " + //
        "//read-transformer/@transformer-class | " + //
        "//stored-procedure-parameter/@type | " + //
        "//struct-converter/@converter | " + //
        "//type-converter/@data-type | " + //
        "//type-converter/@object-type | " + //
        "//variable-one-to-one/@target-interface | " + //
        "//write-transformer/@transformer-class";

    public void analyse(InputStream artifact, String artifactName, PartialManifest partialManifest) throws Exception {
        StandardXmlArtifactAnalyzer analyzer = new StandardXmlArtifactAnalyzer(artifact);
        PackagePrefixValueAnalyzer packagePrefixValueAnalyzer = new PackagePrefixValueAnalyzer();
        analyzer.analyzeValues(PACKAGE_EXPRESSION, packagePrefixValueAnalyzer);
        analyzer.analyzeValues(CLASS_EXPRESSION, new EclipseLinkValueAnalyzer(packagePrefixValueAnalyzer.getPackagePrefix(), partialManifest));
    }

    public boolean canAnalyse(String artifactName) {
        return FILE.equals(artifactName);
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

    private static class EclipseLinkValueAnalyzer implements ValueAnalyzer {

        private final String packagePrefix;

        private final PartialManifest partialManifest;

        public EclipseLinkValueAnalyzer(String packagePrefix, PartialManifest partialManifest) {
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
