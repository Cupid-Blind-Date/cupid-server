import HTTPClient.HTTPResponse
import net.grinder.plugin.http.HTTPRequest
import net.grinder.script.GTest
import net.grinder.scriptengine.groovy.junit.GrinderRunner
import net.grinder.scriptengine.groovy.junit.annotation.BeforeProcess
import net.grinder.scriptengine.groovy.junit.annotation.BeforeThread
import org.junit.Test
import org.junit.runner.RunWith

import static net.grinder.script.Grinder.grinder
import static org.hamcrest.Matchers.is
import static org.junit.Assert.assertThat

/**
 * A simple example using the HTTP plugin that shows the retrieval of a
 * single page via HTTP.
 *
 * This script is auto generated by ngrinder.
 */
@RunWith(GrinderRunner)
class AsyncTest {

    public static GTest test;
    public static HTTPRequest request;

    // This method is executed once per a process.
    @BeforeProcess
    public static void beforeClass() {
        test = new GTest(1, "test");
        request = new HTTPRequest();
        test.record(request);
        grinder.logger.info("before process.");
    }

    // This method is executed once per a thread.
    @BeforeThread
    public void beforeThread() {
        grinder.statistics.delayReports = true;
        grinder.logger.info("before thread.");
    }

    // This method is continuously executed until you stop the test
    @Test
    public void test() {
        HTTPResponse result = request.GET("http://localhost:8080/test/async/virtual-thread/block")
        if (result.statusCode == 301 || result.statusCode == 302) {
            grinder.logger.warn("Warning. The response may not be correct. The response code was {}.", result.statusCode);
        } else {
            assertThat(result.statusCode, is(200));
        }
    }
}
