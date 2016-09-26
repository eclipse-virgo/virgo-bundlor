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

package org.eclipse.virgo.bundlor.support.asm;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.util.Set;
import java.util.jar.JarFile;

import org.junit.Test;

import org.eclipse.virgo.bundlor.support.partialmanifest.ReadablePartialManifest;
import org.eclipse.virgo.bundlor.support.partialmanifest.StandardReadablePartialManifest;

import eg.DependsOnNestedInterface;
import eg.DependsOnNestedType;
import eg.DependsOnStaticFieldViaSubType;
import eg.DependsOnSun;
import eg.DependsViaAnnotationAnnotation;
import eg.DependsViaCast;
import eg.DependsViaCatch;
import eg.DependsViaClassAnnotation;
import eg.DependsViaClassLiteral;
import eg.DependsViaEnum;
import eg.DependsViaField;
import eg.DependsViaFieldAnnotation;
import eg.DependsViaInstanceOf;
import eg.DependsViaInstanceOfInSimpleLambda;
import eg.DependsViaInterface;
import eg.DependsViaLocal;
import eg.DependsViaMethodAnnotation;
import eg.DependsViaNew;
import eg.DependsViaNewMultiArray;
import eg.DependsViaParameter;
import eg.DependsViaParameterAnnotation;
import eg.DependsViaParameterArray;
import eg.DependsViaReturn;
import eg.DependsViaStaticFieldAnnotation;
import eg.DependsViaStaticMethodAnnotation;
import eg.DependsViaStaticMethodCall;
import eg.DependsViaStaticMethodCallInDefaultImplementation;
import eg.DependsViaStaticWithArgType;
import eg.DependsViaSuperClass;
import eg.DependsViaThrowable;
import eg.DependsViaTypeAnnotation;
import eg.EnumDependsViaInterface;
import eg.NestedClassDependsViaInterface;
import eg.NoDependencies;

public class AsmTypeArtifactAnalyzerTests {

    @Test
    public void viaAnnotationAnnotation() throws Exception {
        ReadablePartialManifest model = analyse(DependsViaAnnotationAnnotation.class.getName());
        assertExportsPackage(model, "eg");
        assertImportsPackage(model, "deps");
        assertUses(model, "eg", "deps");
    }

    @Test
    public void viaField() throws Exception {
        ReadablePartialManifest model = analyse(DependsViaField.class.getName());
        assertExportsPackage(model, "eg");
        assertImportsPackage(model, "deps");
        assertNotUses(model, "eg", "deps");
    }

    @Test
    public void viaFieldAnnotation() throws Exception {
        ReadablePartialManifest model = analyse(DependsViaFieldAnnotation.class.getName());
        assertExportsPackage(model, "eg");
        assertImportsPackage(model, "deps");
        assertUses(model, "eg", "deps");
    }

    @Test
    public void viaLocal() throws Exception {
        ReadablePartialManifest model = analyse(DependsViaLocal.class.getName());
        assertExportsPackage(model, "eg");
        assertImportsPackage(model, "deps");
        assertNotUses(model, "eg", "deps");
    }

    @Test
    public void viaMethodReturn() throws Exception {
        ReadablePartialManifest model = analyse(DependsViaReturn.class.getName());
        assertExportsPackage(model, "eg");
        assertImportsPackage(model, "deps");
        assertUses(model, "eg", "deps");
    }

    @Test
    public void viaMethodParam() throws Exception {
        ReadablePartialManifest model = analyse(DependsViaParameter.class.getName());
        assertExportsPackage(model, "eg");
        assertImportsPackage(model, "deps");
        assertUses(model, "eg", "deps");
    }

    @Test
    public void viaMethodParamArray() throws Exception {
        ReadablePartialManifest model = analyse(DependsViaParameterArray.class.getName());
        assertExportsPackage(model, "eg");
        assertImportsPackage(model, "deps");
        assertUses(model, "eg", "deps");
    }

    @Test
    public void viaMethodThrows() throws Exception {
        ReadablePartialManifest model = analyse(DependsViaThrowable.class.getName());
        assertExportsPackage(model, "eg");
        assertImportsPackage(model, "deps.nested");
        assertUses(model, "eg", "deps.nested");
    }

    @Test
    public void viaMethodAnnotation() throws Exception {
        ReadablePartialManifest model = analyse(DependsViaMethodAnnotation.class.getName());
        assertExportsPackage(model, "eg");
        assertImportsPackage(model, "deps");
        assertUses(model, "eg", "deps");
    }

    @Test
    public void viaParameterAnnotation() throws Exception {
        ReadablePartialManifest model = analyse(DependsViaParameterAnnotation.class.getName());
        assertExportsPackage(model, "eg");
        assertImportsPackage(model, "deps");
        assertUses(model, "eg", "deps");
    }

    @Test
    public void viaTypeAnnotation() throws Exception {
        ReadablePartialManifest model = analyse(DependsViaTypeAnnotation.class.getName());
        assertExportsPackage(model, "eg");
        assertImportsPackage(model, "deps");
        assertUses(model, "eg", "deps");
    }

    @Test
    public void viaSuperClass() throws Exception {
        ReadablePartialManifest model = analyse(DependsViaSuperClass.class.getName());
        assertExportsPackage(model, "eg");
        assertImportsPackage(model, "deps");
        assertUses(model, "eg", "deps");
    }

    @Test
    public void viaInterface() throws Exception {
        ReadablePartialManifest model = analyse(DependsViaInterface.class.getName());
        assertExportsPackage(model, "eg");
        assertImportsPackage(model, "deps");
        assertUses(model, "eg", "deps");
    }

    @Test
    public void nestedClassViaInterface() throws Exception {
        ReadablePartialManifest model = analyse(NestedClassDependsViaInterface.Nested.class.getName());
        assertExportsPackage(model, "eg");
        assertImportsPackage(model, "deps");
        assertUses(model, "eg", "deps");
    }

    @Test
    public void anonymousNestedClassViaInterface() throws Exception {
        ReadablePartialManifest model = analyse("eg.AnonymousNestedClassDependsViaInterface$1");
        assertImportsPackage(model, "deps");
        assertUses(model, "eg", "deps");
    }

    @Test
    public void deeplyNestedClassViaInterface() throws Exception {
        ReadablePartialManifest model = analyse("eg.DeeplyNestedClassDependsViaInterface$Nested$1$1");
        assertImportsPackage(model, "deps");
        assertUses(model, "eg", "deps");
    }

    @Test
    public void nestedTypeDoesNotHavePackageImport() throws Exception {
        ReadablePartialManifest model = analyse(DependsOnNestedType.class.getName());
        Set<String> importedPackages = model.getImportedPackages();
        assertImportsPackage(model, "deps");
        assertFalse(importedPackages.contains("deps.ClassWithNestedType"));
    }

    @Test
    public void viaCatch() throws Exception {
        ReadablePartialManifest model = analyse(DependsViaCatch.class.getName());
        assertExportsPackage(model, "eg");
        assertImportsPackage(model, "deps.nested");
        assertImportsPackage(model, "deps");
        assertNotUses(model, "eg", "deps");
    }

    @Test
    public void viaNew() throws Exception {
        ReadablePartialManifest model = analyse(DependsViaNew.class.getName());
        assertExportsPackage(model, "eg");
        assertImportsPackage(model, "deps");
        assertNotUses(model, "eg", "deps");
    }

    @Test
    public void viaCast() throws Exception {
        ReadablePartialManifest model = analyse(DependsViaCast.class.getName());
        assertExportsPackage(model, "eg");
        assertImportsPackage(model, "deps");
        assertNotUses(model, "eg", "deps");
    }

    @Test
    public void viaEnum() throws Exception {
        ReadablePartialManifest model = analyse(DependsViaEnum.class.getName());
        assertExportsPackage(model, "eg");
        assertImportsPackage(model, "deps");
        assertUses(model, "eg", "deps");
    }

    @Test
    public void enumViaInterface() throws Exception {
        ReadablePartialManifest model = analyse(EnumDependsViaInterface.class.getName());
        assertExportsPackage(model, "eg");
        assertImportsPackage(model, "deps");
        assertUses(model, "eg", "deps");
    }

    @Test
    public void viaInstanceOf() throws Exception {
        ReadablePartialManifest model = analyse(DependsViaInstanceOf.class.getName());
        assertExportsPackage(model, "eg");
        assertImportsPackage(model, "deps");
        assertNotUses(model, "eg", "deps");
    }

    @Test
    public void viaStatic() throws Exception {
        ReadablePartialManifest model = analyse(DependsViaStaticMethodCall.class.getName());
        assertExportsPackage(model, "eg");
        assertImportsPackage(model, "deps");
        assertNotUses(model, "eg", "deps");
    }

    @Test
    public void viaStaticWithArgs() throws Exception {
        ReadablePartialManifest model = analyse(DependsViaStaticWithArgType.class.getName());
        assertExportsPackage(model, "eg");
        assertImportsPackage(model, "deps");
        assertImportsPackage(model, "deps.nested");
        assertNotUses(model, "eg", "deps");
        assertNotUses(model, "eg", "deps.nested");
    }

    @Test
    public void sunDependency() throws Exception {
        ReadablePartialManifest model = analyse(DependsOnSun.class.getName());
        assertExportsPackage(model, "eg");
        assertNotImportsPackage(model, "sun.misc");
    }

    @Test
    public void viaClassAnnotation() throws Exception {
        ReadablePartialManifest model = analyse(DependsViaClassAnnotation.class.getName());
        assertExportsPackage(model, "eg");
        assertImportsPackage(model, "deps");
        assertUses(model, "eg", "deps");
    }

    @Test
    public void viaClassLiteral() throws Exception {
        ReadablePartialManifest model = analyse(DependsViaClassLiteral.class.getName());
        assertExportsPackage(model, "eg");
        assertImportsPackage(model, "deps");
        assertNotUses(model, "eg", "deps");
    }

    @Test
    public void viaStaticField() throws Exception {
        ReadablePartialManifest model = analyse(DependsOnStaticFieldViaSubType.class.getName());
        assertExportsPackage(model, "eg");
        assertImportsPackage(model, "deps");
        assertImportsPackage(model, "deps.sub");
        assertNotUses(model, "eg", "deps");
        assertNotUses(model, "eg", "deps.sub");
    }

    @Test
    public void viaStaticFieldAnnotation() throws Exception {
        ReadablePartialManifest model = analyse(DependsViaStaticFieldAnnotation.class.getName());
        assertExportsPackage(model, "eg");
        assertImportsPackage(model, "deps");
        assertUses(model, "eg", "deps");
    }

    @Test
    public void viaStaticMethodAnnotation() throws Exception {
        ReadablePartialManifest model = analyse(DependsViaStaticMethodAnnotation.class.getName());
        assertExportsPackage(model, "eg");
        assertImportsPackage(model, "deps");
        assertUses(model, "eg", "deps");
    }

    @Test
    public void viaMultiArray() throws Exception {
        ReadablePartialManifest model = analyse(DependsViaNewMultiArray.class.getName());
        assertExportsPackage(model, "eg");
        assertImportsPackage(model, "deps");
        assertNotUses(model, "eg", "deps");
    }

    @Test
    public void notViaNoDependencies() throws Exception {
        ReadablePartialManifest model = analyse(NoDependencies.class.getName());
        assertExportsPackage(model, "eg");
        assertNotImportsPackage(model, "deps");
        assertNotUses(model, "eg", "deps");
    }

    @Test
    public void viaNestedInterface() throws Exception {
        ReadablePartialManifest model = analyse(DependsOnNestedInterface.class.getName());
        assertExportsPackage(model, "eg");
        assertImportsPackage(model, "deps");
        assertNotUses(model, "eg", "deps");
    }

    @Test
    public void viaInstanceOfInSimpleLamda() throws Exception {
    	ReadablePartialManifest model = analyse(DependsViaInstanceOfInSimpleLambda.class.getName());
        assertExportsPackage(model, "eg");
        assertImportsPackage(model, "deps");
        assertNotUses(model, "eg", "deps");
    }
    
    @Test
    public void viaDefaultInterfaceImplementation() throws Exception {
    	ReadablePartialManifest model = analyse(DependsViaStaticMethodCallInDefaultImplementation.class.getName());
        assertExportsPackage(model, "eg");
        assertImportsPackage(model, "deps");
        assertNotUses(model, "eg", "deps");
    }
    
    @Test
    public void viaJava14CompiledClassLiteral() throws Exception {
        JarFile jarFile = new JarFile("build/test-jars/wicket-ioc-1.3.3.jar");
        InputStream inputStream = jarFile.getInputStream(jarFile.getEntry("org/apache/wicket/injection/Injector.class"));
        ReadablePartialManifest model = analyse(inputStream, "org/apache/wicket/injection/Injector.class");        
        assertImportsPackage(model, "org.apache.wicket.markup.html");
        assertImportsPackage(model, "org.apache.wicket.markup.html.panel");
        assertImportsPackage(model, "org.apache.wicket");
        jarFile.close();
    }

    @Test
    public void exportsPublicClass() throws Exception {
        ReadablePartialManifest model = analyse(TestClass.PublicClass.class.getName());
        assertExportsPackage(model, "org.eclipse.virgo.bundlor.support.asm");
    }

    @Test
    public void exportsProtectedClass() throws Exception {
        ReadablePartialManifest model = analyse(TestClass.ProtectedClass.class.getName());
        assertExportsPackage(model, "org.eclipse.virgo.bundlor.support.asm");
    }

    protected ReadablePartialManifest analyse(String className) throws Exception {
        String resourceName = className.replaceAll("\\.", "/") + ".class";
        InputStream stream = getClass().getClassLoader().getResourceAsStream(resourceName);
        return analyse(stream, resourceName);
    }

    protected ReadablePartialManifest analyse(InputStream classInputStream, String resourceName) throws Exception {
        AsmTypeArtifactAnalyzer analyser = new AsmTypeArtifactAnalyzer();
        ReadablePartialManifest model = new StandardReadablePartialManifest();
        analyser.analyse(classInputStream, resourceName, model);
        return model;
    }

    protected void assertExportsPackage(ReadablePartialManifest model, String expectedPackage) {
        assertTrue("Expected package '" + expectedPackage + "' to be exported", model.getExportedPackages().contains(expectedPackage));
    }

    protected void assertNotExportsPackage(ReadablePartialManifest model, String expectedPackage) {
        assertFalse("Unexpected package '" + expectedPackage + ".", model.getExportedPackages().contains(expectedPackage));
    }

    protected void assertImportsPackage(ReadablePartialManifest model, String expectedPackage) {
        assertTrue("Expected package '" + expectedPackage + "' to be imported", model.getImportedPackages().contains(expectedPackage));
    }

    protected void assertNotImportsPackage(ReadablePartialManifest model, String unexpectedPackage) {
        assertFalse("Unexpected package '" + unexpectedPackage + "'.", model.getImportedPackages().contains(unexpectedPackage));
    }

    protected void assertUses(ReadablePartialManifest model, String usingPackage, String usedPackage) {
        assertTrue(String.format("Expected package '%s' to use '%s'.", usingPackage, usedPackage), model.getUses(usingPackage).contains(usedPackage));
    }

    protected void assertNotUses(ReadablePartialManifest model, String usingPackage, String usedPackage) {
        assertFalse(String.format("Expected package '%s' *not* to use '%s'.", usingPackage, usedPackage), model.getUses(usingPackage).contains(
            usedPackage));
    }
}
