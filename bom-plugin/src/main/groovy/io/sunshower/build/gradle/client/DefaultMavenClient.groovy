package io.sunshower.build.gradle.client

import org.apache.maven.shared.invoker.*
import org.gradle.api.GradleException
/**
 * Created by dustinlish on 4/11/16.
 */
class DefaultMavenClient implements MavenClient {

    private final Invoker invoker

    DefaultMavenClient() {
        this.invoker = new DefaultInvoker()
    }

    DefaultMavenClient(String mavenHome) {
        this()
        invoker.setMavenHome(new File(mavenHome))
    }

    @Override
    int run(String rootBomPath, List<String> goals, Properties properties) {
        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile(new File(rootBomPath))
        request.setGoals(goals)
        request.setProperties(properties)

        InvocationResult result
        try {
            result = invoker.execute(request)
        } catch (IllegalStateException ex) {
            throw new GradleException("${ex.message}\n\n" +
                    "Set 'M2_HOME' environment variable or specify system property 'maven.home'")
        }

        if (result.getExitCode() != 0) {
            throw new GradleException("${result.executionException}\n" +
                    "${rootBomPath} : goals=[${goals}], properties=${properties}")
        }

        return result.exitCode
    }

}
