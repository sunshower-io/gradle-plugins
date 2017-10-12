package io.sunshower.build.gradle.tasks

import io.sunshower.build.gradle.client.MavenClient
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional

/**
 * Created by dustinlish on 4/11/16.
 */
class VersionSet extends BomTask {

    @Input
    String version

    VersionSet() {
        super('Sets Bill-Of-Materials Version')
    }

    @Override
    int executeAction(MavenClient client) {
        Properties props = new Properties()
        props.setProperty("newVersion", version)
        return client.run(project.bom.rootBomPath, ['versions:set'], props)
    }
}
