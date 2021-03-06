/* 
 * Licence is provided in the jar as license.yml also here:
 * https://github.com/Rsl1122/Plan-PlayerAnalytics/blob/master/Plan/src/main/resources/license.yml
 */
package main.java.com.djrapitops.plan.systems.webserver;

import com.djrapitops.plugin.api.Benchmark;
import com.djrapitops.plugin.api.utility.log.Log;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.java.com.djrapitops.plan.settings.Settings;
import main.java.com.djrapitops.plan.systems.webserver.response.Response;
import main.java.com.djrapitops.plan.systems.webserver.webapi.WebAPIManager;

import java.io.IOException;

/**
 * HttpHandler for webserver request management.
 *
 * @author Rsl1122
 */
public class APIRequestHandler implements HttpHandler {

    private final APIResponseHandler responseHandler;

    APIRequestHandler(WebAPIManager webAPI) {
        responseHandler = new APIResponseHandler(webAPI);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Headers responseHeaders = exchange.getResponseHeaders();
        Request request = new Request(exchange);
        String requestString = request.toString();
        Benchmark.start("", requestString);
        int responseCode = -1;
        try {
            Response response = responseHandler.getAPIResponse(request);
            responseCode = response.getCode();
            response.setResponseHeaders(responseHeaders);
            response.send(exchange);
        } catch (Exception e) {
            if (Settings.DEV_MODE.isTrue()) {
                Log.toLog(this.getClass().getName(), e);
            }
        } finally {
            exchange.close();
            if (Settings.DEV_MODE.isTrue()) {
                Log.debug(requestString + " Response code: " + responseCode + " took " + Benchmark.stop("", requestString) + " ms");
            }
        }
    }


}