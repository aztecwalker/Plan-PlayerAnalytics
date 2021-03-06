package main.java.com.djrapitops.plan.data;

import com.djrapitops.plugin.api.TimeAmount;
import main.java.com.djrapitops.plan.data.container.Session;
import main.java.com.djrapitops.plan.settings.Settings;
import main.java.com.djrapitops.plan.utilities.MiscUtils;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import test.java.utils.TestInit;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * //TODO Class Javadoc Comment
 *
 * @author Rsl1122
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(JavaPlugin.class)
public class PlayerProfileTest {

    @Before
    public void setUp() throws Exception {
        TestInit.init();
    }

    @Test
    public void testMaxActivityIndex() throws Exception {
        PlayerProfile p = new PlayerProfile(null, null, 0L);
        List<Session> sessions = new ArrayList<>();

        long date = MiscUtils.getTime();
        long week = TimeAmount.WEEK.ms();
        long weekAgo = date - week;
        long twoWeeksAgo = date - 2L * week;
        long threeWeeksAgo = date - 3L * week;

        long requiredPlaytime = Settings.ACTIVE_PLAY_THRESHOLD.getNumber() * TimeAmount.MINUTE.ms();
        int requiredLogins = Settings.ACTIVE_LOGIN_THRESHOLD.getNumber();

        for (int i = 0; i < requiredLogins; i++) {
            sessions.add(new Session(0, weekAgo, weekAgo + requiredPlaytime * 4L, 0, 0));
            sessions.add(new Session(0, twoWeeksAgo, twoWeeksAgo + requiredPlaytime * 4L, 0, 0));
            sessions.add(new Session(0, threeWeeksAgo, threeWeeksAgo + requiredPlaytime * 4L, 0, 0));
        }
        p.setSessions(null, sessions);

        assertEquals(5.0, p.getActivityIndex(date));
    }

    @Test
    public void testMaxActivityIndex2() throws Exception {
        PlayerProfile p = new PlayerProfile(null, null, 0L);
        List<Session> sessions = new ArrayList<>();

        long date = MiscUtils.getTime();
        long week = TimeAmount.WEEK.ms();
        long weekAgo = date - week;
        long twoWeeksAgo = date - 2L * week;
        long threeWeeksAgo = date - 3L * week;

        long requiredPlaytime = Settings.ACTIVE_PLAY_THRESHOLD.getNumber() * TimeAmount.MINUTE.ms();
        int requiredLogins = Settings.ACTIVE_LOGIN_THRESHOLD.getNumber();

        for (int i = 0; i < requiredLogins * 2; i++) {
            sessions.add(new Session(0, weekAgo, weekAgo + requiredPlaytime * 3L, 0, 0));
            sessions.add(new Session(0, twoWeeksAgo, twoWeeksAgo + requiredPlaytime * 3L, 0, 0));
            sessions.add(new Session(0, threeWeeksAgo, threeWeeksAgo + requiredPlaytime * 3L, 0, 0));
        }
        p.setSessions(null, sessions);

        assertEquals(5.0, p.getActivityIndex(date));
    }

    @Test
    public void testActivityIndexOne() throws Exception {
        PlayerProfile p = new PlayerProfile(null, null, 0L);
        List<Session> sessions = new ArrayList<>();

        long date = MiscUtils.getTime();
        long week = TimeAmount.WEEK.ms();
        long weekAgo = date - week;
        long twoWeeksAgo = date - 2L * week;
        long threeWeeksAgo = date - 3L * week;

        int requiredLogins = Settings.ACTIVE_LOGIN_THRESHOLD.getNumber();
        long requiredPlaytime = Settings.ACTIVE_PLAY_THRESHOLD.getNumber() * TimeAmount.MINUTE.ms() / requiredLogins;

        for (int i = 0; i < requiredLogins; i++) {
            sessions.add(new Session(i, weekAgo, weekAgo + requiredPlaytime, 0, 0));
            sessions.add(new Session(i * 2, twoWeeksAgo, twoWeeksAgo + requiredPlaytime, 0, 0));
            sessions.add(new Session(i * 3, threeWeeksAgo, threeWeeksAgo + requiredPlaytime, 0, 0));
        }
        p.setSessions(null, sessions);

        assertTrue(2.0 <= p.getActivityIndex(date));
    }

    @Test(timeout = 500)
    public void testMethodTimeout() throws Exception {
        PlayerProfile p = new PlayerProfile(null, null, 0L);
        List<Session> sessions = new ArrayList<>();
        long date = 0;

        for (int i = 0; i < 5000; i++) {
            sessions.add(new Session(0, 0, 0, 0, 0));
        }
        p.setSessions(null, sessions);
        p.getActivityIndex(0);
    }

}