/* 
 * Licence is provided in the jar as license.yml also here:
 * https://github.com/Rsl1122/Plan-PlayerAnalytics/blob/master/Plan/src/main/resources/license.yml
 */
package main.java.com.djrapitops.plan.systems.webserver.webapi.bungee;

import main.java.com.djrapitops.plan.api.IPlan;
import main.java.com.djrapitops.plan.api.exceptions.WebAPIException;
import main.java.com.djrapitops.plan.api.exceptions.WebAPINotFoundException;
import main.java.com.djrapitops.plan.systems.info.InformationManager;
import main.java.com.djrapitops.plan.systems.webserver.response.Response;
import main.java.com.djrapitops.plan.systems.webserver.webapi.WebAPI;

import java.util.Map;
import java.util.UUID;

/**
 * WebAPI for checking if a page is in webserver cache.
 *
 * @author Rsl1122
 */
public class IsCachedWebAPI extends WebAPI {

    @Override
    public Response onRequest(IPlan plugin, Map<String, String> variables) {
        try {
            String target = variables.get("target");
            InformationManager infoManager = plugin.getInfoManager();
            boolean cached = false;
            switch (target) {
                case "inspectPage":
                    if (infoManager.isCached(UUID.fromString(variables.get("uuid")))) {
                        cached = true;
                    }
                    break;
                case "analysisPage":
                    if (infoManager.isAnalysisCached(UUID.fromString(variables.get("serverUUID")))) {
                        cached = true;
                    }
                    break;
                default:
                    return badRequest("Faulty Target");
            }
            if (cached) {
                return success();
            } else {
                return fail("Not Cached");
            }
        } catch (NullPointerException e) {
            return badRequest(e.toString());
        }
    }

    @Override
    public void sendRequest(String address) throws WebAPIException {
        throw new IllegalStateException("Wrong method call for this WebAPI, call sendRequest(String, UUID, UUID) instead.");
    }

    public boolean isInspectCached(String address, UUID uuid) throws WebAPIException {
        addVariable("uuid", uuid.toString());
        addVariable("target", "inspectPage");
        try {
            super.sendRequest(address);
            return true;
        } catch (WebAPINotFoundException e) {
            return false;
        }
    }

    public boolean isAnalysisCached(String address, UUID serverUUID) throws WebAPIException {
        addVariable("serverUUID", serverUUID.toString());
        addVariable("target", "analysisPage");
        try {
            super.sendRequest(address);
            return true;
        } catch (WebAPINotFoundException e) {
            return false;
        }
    }
}