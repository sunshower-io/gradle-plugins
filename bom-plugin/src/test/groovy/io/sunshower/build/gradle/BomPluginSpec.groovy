package io.sunshower.build.gradle

import io.sunshower.build.gradle.tasks.InstallBillOfMaterials
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

/**
 * Created by dustinlish on 4/11/16.
 */
class BomPluginSpec extends Specification {

    Project project

    def setup() {
        project = ProjectBuilder.builder().build()
    }

    def "Project adds installBillOfMaterials task"() {
        def installBillOfMaterials = 'installBillOfMaterials'
        expect:
        project.tasks.findByName(installBillOfMaterials) == null

        when:
        project.task(installBillOfMaterials, type: InstallBillOfMaterials)

        then:
        Task task = project.tasks.findByName(installBillOfMaterials)
        task != null
        task.description == 'Installs Bill-Of-Materials'
        task.group == 'Bill Of Materials'
    }

}
