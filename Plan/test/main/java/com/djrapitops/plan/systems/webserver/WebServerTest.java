package main.java.com.djrapitops.plan.systems.webserver;

import com.sun.net.httpserver.HttpExchange;
import main.java.com.djrapitops.plan.Plan;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import test.java.utils.MockUtils;
import test.java.utils.TestInit;

import java.io.IOException;
import java.util.HashMap;

/**
 * //TODO Class Javadoc Comment
 *
 * @author Rsl1122
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({JavaPlugin.class})
public class WebServerTest {

    private WebServer webServer;
    private RequestHandler requestHandler;

    @Before
    public void setUp() throws Exception {
        TestInit testInit = TestInit.init();
        Plan plugin = testInit.getPlanMock();
        webServer = new WebServer(plugin);
        requestHandler = new RequestHandler(plugin, webServer);
    }

    @Test
    public void testMockSetup() throws IOException {
        HttpExchange exchange = MockUtils.getHttpExchange(
                "POST",
                "/api/pingwebapi",
                "",
                new HashMap<>()
        );
        requestHandler.handle(exchange);
        System.out.println(MockUtils.getResponseStream(exchange));
    }
}