package main.java.com.djrapitops.plan.utilities.comparators;

import main.java.com.djrapitops.plan.settings.locale.Message;
import main.java.com.djrapitops.plan.settings.locale.Msg;

import java.util.Comparator;
import java.util.Map;

/**
 * Compares Locale Map Entries and sorts them alphabetically according to the Enum Names.
 *
 * @author Rsl1122
 * @since 3.6.2
 */
public class LocaleEntryComparator implements Comparator<Map.Entry<Msg, Message>> {

    @Override
    public int compare(Map.Entry<Msg, Message> o1, Map.Entry<Msg, Message> o2) {
        return String.CASE_INSENSITIVE_ORDER.compare(o1.getKey().name(), o2.getKey().name());
    }
}
