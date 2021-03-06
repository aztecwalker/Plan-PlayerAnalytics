/* 
 * Licence is provided in the jar as license.yml also here:
 * https://github.com/Rsl1122/Plan-PlayerAnalytics/blob/master/Plan/src/main/resources/license.yml
 */
package main.java.com.djrapitops.plan.utilities.html.tables;

import main.java.com.djrapitops.plan.data.container.GeoInfo;
import main.java.com.djrapitops.plan.utilities.FormatUtils;
import main.java.com.djrapitops.plan.utilities.comparators.GeoInfoComparator;
import main.java.com.djrapitops.plan.utilities.html.Html;

import java.util.List;

/**
 * Utility Class for creating IP Table for inspect page.
 *
 * @author Rsl1122
 */
public class IpTableCreator {


    public IpTableCreator() {
        throw new IllegalStateException("Utility class");
    }

    public static String createTable(List<GeoInfo> geoInfo) {
        geoInfo.sort(new GeoInfoComparator());
        StringBuilder html = new StringBuilder();
        if (geoInfo.isEmpty()) {
            html.append(Html.TABLELINE_3.parse("No Connections", "-", "-"));
        } else {
            for (GeoInfo info : geoInfo) {
                long date = info.getDate();
                html.append(Html.TABLELINE_3.parse(
                        FormatUtils.formatIP(info.getIp()),
                        info.getGeolocation(),
                        date != 0 ? FormatUtils.formatTimeStampYear(date) : "-"
                ));
            }
        }
        return html.toString();
    }
}