package io.sunshower.build.gradle.tasks

import io.sunshower.build.gradle.client.MavenClient
/**
 * Created by dustinlish on 4/11/16.
 */
class InstallBillOfMaterials extends BomTask {

    InstallBillOfMaterials() {
        super('Installs Bill-Of-Materials')
    }

    @Override
    int executeAction(MavenClient client) {
        return client.run(project.bom.rootBomPath, ['clean', 'install'], new Properties())
    }

}
