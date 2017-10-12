package io.sunshower.build.gradle

import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Shared
import spock.lang.Specification

/**
 * Created by dustinlish on 4/11/16.
 */
class TaskExecutionSpec extends Specification {

    Project project

    @Shared String testBomRoot =
            escapedPath(ClassLoader.getSystemResource('bom/pom.xml').file)


    def setup() {
        project = ProjectBuilder.builder().build()
        project.apply plugin: 'bom'
    }

    def "installBillOfMaterials task returns success"() {
        given:
        Task task = project.tasks.findByName('installBillOfMaterials')
        project.bom.rootBomPath = testBomRoot

        when:
        int exitValue = task.start()

        then:
        assert exitValue == 0
    }

    def "verify maven plugins exist for uploadingBom"() {
        given:
        Task task = project.tasks.findByName('uploadBom')
        project.bom.rootBomPath = testBomRoot

        when:
        task.verifyMavenPlugins()

        then:
        noExceptionThrown()
    }

    def "maven-source-plugin missing throws GradleException"() {
        given:
        Task task = project.tasks.findByName('uploadBom')
        project.bom.rootBomPath =
                escapedPath(ClassLoader.getSystemResource('bom-source-plugin-missing/pom.xml').file)


        when:
        task.verifyMavenPlugins()

        then:
        GradleException ex = thrown()
        ex.message == 'maven-source-plugin is required in pom.xml to upload BOM'
    }

    def "artifactory-maven-plugin missing throws GradleException"() {
        given:
        Task task = project.tasks.findByName('uploadBom')
        project.bom.rootBomPath =
                escapedPath(ClassLoader.getSystemResource('bom-artifactory-plugin-missing/pom.xml').file)

        when:
        task.verifyMavenPlugins()

        then:
        GradleException ex = thrown()
        ex.message == 'artifactory-maven-plugin is required in pom.xml to upload BOM'
    }

    def "verify plugins checks pom.xml when given root bom directory"() {
        given:
        Task task = project.tasks.findByName('uploadBom')
        project.bom.rootBomPath = escapedPath(ClassLoader.getSystemResource('bom').file)

        when:
        task.verifyMavenPlugins()

        then:
        noExceptionThrown()
    }

    def "FileNotFoundException thrown if pom.xml not found"() {
        given:
        Task task = project.tasks.findByName('uploadBom')
        project.bom.rootBomPath = escapedPath(ClassLoader.getSystemResource('bom-no-pom').file)

        when:
        task.verifyMavenPlugins()

        then:
        thrown(FileNotFoundException)
    }

    def "setBomVersion changes bom to specified version"() {
        given:
        def version = '1.0.0.Final'
        Task task = project.tasks.findByName('setBomVersion')
        task.version = version
        project.bom.rootBomPath = testBomRoot

        when:
        int exitValue = task.start()

        then:
        assertVersionsMatch(version)
        assert exitValue == 0
    }


    def assertVersionsMatch(String version) {
        def dir = new File(escapedPath(ClassLoader.getSystemResource('bom').path))
        def versions = []
        dir.eachFileRecurse { currentFile ->
            if (currentFile.isFile() && currentFile.name =~ /xml$/) {
                def pom = new XmlSlurper().parse(currentFile)
                versions.add(pom.version)
                if (pom.parent.version != '')
                    versions.add(pom.parent.version)
            }
        }

        versions.every {
            it == version
        }
    }

    def escapedPath(String path) {
        return path.replaceAll("%20", "\\ ")
    }

}
