package io.sunshower.build.gradle.tasks

import io.sunshower.build.gradle.client.MavenClient
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input

/**
 * Created by dustinlish on 4/11/16.
 */
class UploadBom extends BomTask {

    private static final ARTIFACTORY_USER = "artifactory_user"
    private static final ARTIFACTORY_PASSWORD = "artifactory_password"
    private static final REPO_KEY = "repoKey"
    private static final CONTEXT_URL = "artifactory_contextUrl"

    @Input
    String artifactory_user

    @Input
    String artifactory_password

    @Input
    String repoKey

    @Input
    String artifactory_contextUrl

    UploadBom() {
        super('Uploads Bill-Of-Materials to Artifactory')
    }

    @Override
    int executeAction(MavenClient client) {
        verifyMavenPlugins()
        return client.run(project.bom.rootBomPath, ['deploy'], buildProperties())
    }

    def buildProperties() {
        Properties props = new Properties()

        if (artifactory_user != null && artifactory_user != '') {
            props.setProperty(ARTIFACTORY_USER, artifactory_user)
        }
        if (artifactory_password != null && artifactory_password != '') {
            props.setProperty(ARTIFACTORY_PASSWORD, artifactory_password)
        }
        if (repoKey != null && repoKey != '') {
            props.setProperty(REPO_KEY, repoKey)
        }
        if (artifactory_contextUrl != null && artifactory_contextUrl != '') {
            props.setProperty(CONTEXT_URL, artifactory_contextUrl)
        }

        return props
    }

    def verifyMavenPlugins() {
        def pomFile = project.bom.rootBomPath

        if (!(pomFile =~ /pom\.xml$/)) {
            pomFile = "${project.bom.rootBomPath}/pom.xml"
        }

        def pom = new XmlSlurper().parse(new File("$pomFile"))
        if (!pom.build.pluginManagement.plugins.plugin.find { it.artifactId == 'maven-source-plugin' }) {
            throw new GradleException("maven-source-plugin is required in pom.xml to upload BOM")
        }

        if (!pom.build.plugins.plugin.find{ it.artifactId == 'artifactory-maven-plugin' }) {
            throw new GradleException("artifactory-maven-plugin is required in pom.xml to upload BOM")
        }
    }
}
