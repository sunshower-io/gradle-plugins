package io.sunshower.build.gradle

import io.sunshower.build.gradle.tasks.InstallBillOfMaterials
import io.sunshower.build.gradle.tasks.UploadBom
import io.sunshower.build.gradle.tasks.VersionSet
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
/**
 * Created by dustinlish on 4/11/16.
 */
class BomPlugin implements Plugin<Project> {

    private static Logger logger = Logging.getLogger(BomPlugin)

    @Override
    void apply(Project project) {
        addTasks(project)
    }

    void addTasks(Project project) {
        project.extensions.create("bom", BomPluginExtension)

        project.task('installBillOfMaterials', type: InstallBillOfMaterials)

        project.task('setBomVersion', type: VersionSet) {
            version = project.property('version')
        }

        project.task('uploadBom', type: UploadBom) {
            artifactory_contextUrl = getArtifactoryContextUrl(project)
            artifactory_user = getArtifactoryUser(project)
            artifactory_password = getArtifactoryPassword(project)
            repoKey = getRepoKey(project)
        }

    }

    private String getArtifactoryUser(Project project) {
        project.hasProperty('artifactory_user') ? project.artifactory_user : ''
    }

    private String getArtifactoryPassword(Project project) {
        project.hasProperty('artifactory_password') ? project.artifactory_password : ''
    }

    private String getArtifactoryContextUrl(Project project) {
        project.hasProperty('artifactory_contextUrl') ? project.artifactory_contextUrl : ''
    }

    private String getRepoKey(Project project) {
        def version = project.property('version')
        if (version =~ /(?i)FINAL$/) {
            return 'libs-release-local'
        } else if (version =~ /(?i)BUILD$/) {
            return 'libs-dev-local'
        } else {
            return 'libs-snapshot-local'
        }
    }

}
