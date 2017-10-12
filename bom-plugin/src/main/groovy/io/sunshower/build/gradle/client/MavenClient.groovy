package io.sunshower.build.gradle.client

/**
 * Created by dustinlish on 4/11/16.
 */
interface MavenClient {

    int run(String rootBomPath, List<String> cmds, Properties properties)

}