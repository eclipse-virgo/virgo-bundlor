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

package org.eclipse.virgo.bundlor.support.partialmanifest;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


/**
 * Implementation of {@link PartialManifest} that provides programmatic access to the collected data.
 * <p/>
 * 
 * <strong>Concurrent Semantics</strong><br />
 * 
 * Not thread safe.
 * 
 * @author Rob Harrop
 * @author Glyn Normington
 * @author Ben Hale
 */
public class StandardReadablePartialManifest implements ReadablePartialManifest {

    private static final String[] EXCLUDED_PREFIXES = { "java.", "sun." };

    public final Set<String> exportedPackages = new TreeSet<String>();

    private final Set<String> importedTypes = new TreeSet<String>();

    private final Map<String, Set<String>> uses = new HashMap<String, Set<String>>();

    private final Set<String> localTypes = new TreeSet<String>();

    private final Set<String> referencedPackages = new TreeSet<String>();

    // condensed is true if and only if the results of the condense method are up to date.
    private boolean condensed = false;

    // importedPackages is computed by the condense method and is valid if and only if condensed is
    // true
    private final Set<String> importedPackages = new TreeSet<String>();

    // unsatisifiedTypesByPackage is computed by the condense method and is valid if and only if
    // condensed is true
    public final Map<String, Set<String>> unsatisfiedTypesByPackage = new HashMap<String, Set<String>>();

    public static final Set<String> EMPTY_SET = new TreeSet<String>();

    /**
     * @inheritDoc
     */
    public void recordExportPackage(String packageName) {
        if (isRecordablePackage(packageName)) {
            this.exportedPackages.add(packageName);
        }
    }

    protected void unrecordExportPackage(String packageName) {
        if (packageName != null) {
            this.exportedPackages.remove(packageName);
        }
    }

    /*
     * Condense the <code>PartialManifest</code> by analysing the imports and exports and removing imports for packages
     * that can be satisfied internally, i.e. packages for which there is an export.
     */
    public void condense() {
        if (!this.condensed) {
            this.importedPackages.clear();
            this.unsatisfiedTypesByPackage.clear();

            Set<String> unsatisfiableTypes = new TreeSet<String>(this.importedTypes);
            unsatisfiableTypes.removeAll(this.localTypes);

            for (String unsatisfiableType : unsatisfiableTypes) {
                String packageName = getPackageName(unsatisfiableType);

                getUnsatisfiedTypesForPackage(packageName).add(unsatisfiableType);

                if (isRecordablePackage(packageName)) {
                    this.importedPackages.add(packageName);
                }
            }
            this.condensed = true;

            this.referencedPackages.removeAll(this.exportedPackages);
            this.importedPackages.addAll(this.referencedPackages);
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean isRecordablePackage(String packageName) {

        if (packageName == null) {
            return false;
        }

        for (String prefix : EXCLUDED_PREFIXES) {
            if (packageName.startsWith(prefix)) {
                return false;
            }
        }

        return true;

    }

    private Set<String> getUnsatisfiedTypesForPackage(String packageName) {
        Set<String> typesInPackage = this.unsatisfiedTypesByPackage.get(packageName);

        if (typesInPackage == null) {
            typesInPackage = new TreeSet<String>();
            this.unsatisfiedTypesByPackage.put(packageName, typesInPackage);
        }
        return typesInPackage;
    }

    protected static String getPackageName(String fullyQualifiedTypeName) {
        if (fullyQualifiedTypeName != null) {

            int index = fullyQualifiedTypeName.lastIndexOf('.');

            if (index > -1) {
                return fullyQualifiedTypeName.substring(0, index);
            }
        }

        return null;
    }

    /**
     * @inheritDoc
     */
    public void recordUsesPackage(String usingPackage, String usedPackage) {
        if (isRecordablePackage(usingPackage) && isRecordablePackage(usedPackage) && !usingPackage.equals(usedPackage)) {
            Set<String> usesSet = getUsesSet(usingPackage);
            usesSet.add(usedPackage);
        }
    }

    protected void removeUses(String usingPackage, Set<String> usedPackages) {
        if (usedPackages.size() > 0) {
            Set<String> existingUses = this.uses.get(usingPackage);
            existingUses.removeAll(usedPackages);
            if (existingUses.size() == 0) {
                this.uses.remove(usingPackage);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public Set<String> getExportedPackages() {
        condense();
        return this.exportedPackages;
    }

    /**
     * {@inheritDoc}
     */
    public Set<String> getImportedPackages() {
        condense();
        return this.importedPackages;
    }

    /**
     * {@inheritDoc}
     */
    public Set<String> getUses(String exportingPackage) {
        return getUsesSet(exportingPackage);
    }

    private Set<String> getUsesSet(String exportingPackage) {
        Set<String> usesSet = this.uses.get(exportingPackage);
        if (usesSet == null) {
            usesSet = new TreeSet<String>();
            this.uses.put(exportingPackage, usesSet);
        }
        return usesSet;
    }

    /**
     * {@inheritDoc}
     */
    public void recordReferencedType(String fullyQualifiedTypeName) {
        if (fullyQualifiedTypeName != null) {
            condensed = false;
            this.importedTypes.add(fullyQualifiedTypeName);
        }
    }

    public void recordReferencedPackage(String fullyQualifiedPackageName) {
        if (fullyQualifiedPackageName != null && (isRecordablePackage(fullyQualifiedPackageName))) {
            condensed = false;
            this.referencedPackages.add(fullyQualifiedPackageName);
        }
    }

    protected void removeImportedType(String fullyQualifiedTypeName) {
        if (fullyQualifiedTypeName != null) {

            condensed = false;
            this.importedTypes.remove(fullyQualifiedTypeName);
        }
    }

    protected void removeReferencedPackage(String fullyQualifiedPackageName) {
        if (fullyQualifiedPackageName != null) {

            condensed = false;
            this.referencedPackages.remove(fullyQualifiedPackageName);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void recordType(String fullyQualifiedTypeName) {
        if (fullyQualifiedTypeName != null) {
            recordExportPackage(getPackageName(fullyQualifiedTypeName));

            condensed = false;
            this.localTypes.add(fullyQualifiedTypeName);
        }
    }

    protected void unrecordType(String fullyQualifiedTypeName) {
        if (fullyQualifiedTypeName != null) {

            condensed = false;
            this.localTypes.remove(fullyQualifiedTypeName);
        }
    }

    public Set<String> getUnsatisfiedTypes(String packageName) {
        condense();
        Set<String> unsatisfiedTypes = this.unsatisfiedTypesByPackage.get(packageName);

        return unsatisfiedTypes == null ? EMPTY_SET : unsatisfiedTypes;
    }
}
