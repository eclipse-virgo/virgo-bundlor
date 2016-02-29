package org.eclipse.virgo.bundlor;

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Ignore;
import org.junit.Test

import junit.framework.Assert
import static org.junit.Assert.assertThat
import static org.junit.Assert.assertTrue

public class BundlorPluginTests {

    @Test
    public void bundlorPluginAddsDefaultTaskToProject() {
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: 'java'
        project.apply plugin: 'org.eclipse.virgo.bundlor'

        assertTrue project.tasks.bundlor instanceof DefaultTask
    }

    @Test
    @Ignore
    public void bundlorPluginProcessesTemplateMf() {
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: 'java'
        project.apply plugin: 'bundlor'

        def task = project.tasks.bundlor
        assertThat task.name = 'bundlor'
    }
}
