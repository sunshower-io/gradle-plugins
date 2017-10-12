package io.sunshower.build.gradle

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification
/**
 * Created by dustinlish on 4/18/16.
 */
class UploadBomCmdBuilderSpec extends Specification {

    Project project

    def setup() {
        project = ProjectBuilder.builder().build()
        project.apply plugin: 'bom'
    }

    def "Explicitly setting artifactory_user adds property to cmd"() {
        given:
        Task task = project.tasks.findByName('uploadBom')
        task.artifactory_user = 'user'

        when:
        Properties props = task.buildProperties()

        then:
        props.containsKey("artifactory_user")
        props.getProperty('artifactory_user') == 'user'
        !props.containsKey('artifactory_password')
    }

    def "Explicitly setting artifactory_user and artifactory_password adds properties to cmd"() {
        given:
        Task task = project.tasks.findByName('uploadBom')
        task.artifactory_user = 'user'
        task.artifactory_password = 'password'

        when:
        Properties props = task.buildProperties()

        then:
        props.containsKey('artifactory_user')
        props.containsKey('artifactory_password')
        props.getProperty('artifactory_user') == 'user'
        props.getProperty('artifactory_password') == 'password'
        !props.containsKey('artifactoryBaseUrl')
    }

    def "Explicitly setting all properties adds properties to cmd"() {
        given:
        Task task = project.tasks.findByName('uploadBom')
        task.artifactory_user = 'user'
        task.artifactory_password = 'password'
        task.artifactory_contextUrl = 'http://ehh.com'
        task.repoKey = 'libs-ehh-local'

        when:
        Properties props = task.buildProperties()

        then:
        props.containsKey('artifactory_user')
        props.containsKey('artifactory_password')
        props.containsKey('artifactory_contextUrl')
        props.containsKey('repoKey')
    }

}
