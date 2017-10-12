package io.sunshower.build.gradle.tasks

import io.sunshower.build.gradle.client.DefaultMavenClient
import io.sunshower.build.gradle.client.MavenClient
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
/**
 * Created by dustinlish on 4/11/16.
 */
abstract class BomTask extends DefaultTask {

    BomTask(String description) {
        this.description = description
        group = 'Bill Of Materials'
    }

    @TaskAction
    int start() {
        if (project.bom.mavenHome != null && project.bom.mavenHome != '') {
            return executeAction(new DefaultMavenClient(project.bom.mavenHome as String))
        }

        return executeAction(new DefaultMavenClient())
    }

    abstract int executeAction(MavenClient client)

}
