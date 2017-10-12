package io.sunshower.build.gradle

/**
 * Created by dustinlish on 4/26/16.
 */
class BomPluginExtension {

    String rootBomPath = System.getProperty('user.dir') + '/bom'

    String mavenHome = System.hasProperty("M2_HOME") ?
            System.getProperty("M2_HOME") :
            ''

}
