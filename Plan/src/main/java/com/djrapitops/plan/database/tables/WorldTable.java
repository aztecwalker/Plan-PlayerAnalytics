package main.java.com.djrapitops.plan.database.tables;

import com.djrapitops.plugin.utilities.Verify;
import main.java.com.djrapitops.plan.api.exceptions.DBCreateTableException;
import main.java.com.djrapitops.plan.database.databases.SQLDB;
import main.java.com.djrapitops.plan.database.processing.ExecStatement;
import main.java.com.djrapitops.plan.database.processing.QueryAllStatement;
import main.java.com.djrapitops.plan.database.processing.QueryStatement;
import main.java.com.djrapitops.plan.database.sql.Sql;
import main.java.com.djrapitops.plan.database.sql.TableSqlParser;
import main.java.com.djrapitops.plan.utilities.MiscUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Table class representing database table plan_worlds.
 * <p>
 * Used for storing id references to world names.
 *
 * @author Rsl1122
 * @since 3.6.0 / Database version 7
 */
public class WorldTable extends Table {

    public final String statementSelectID;
    private final String columnWorldId = "id";
    private final String columnWorldName = "world_name";

    /**
     * Constructor.
     *
     * @param db         Database this table is a part of.
     * @param usingMySQL Database is a MySQL database.
     */
    public WorldTable(SQLDB db, boolean usingMySQL) {
        super("plan_worlds", db, usingMySQL);
        statementSelectID = "(SELECT " + columnWorldId + " FROM " + tableName + " WHERE (" + columnWorldName + "=?))";
    }

    @Override
    public void createTable() throws DBCreateTableException {
        createTable(TableSqlParser.createTable(tableName)
                .primaryKeyIDColumn(usingMySQL, columnWorldId)
                .column(columnWorldName, Sql.varchar(100)).notNull()
                .primaryKey(usingMySQL, columnWorldId)
                .toString()
        );
    }

    /**
     * Used to get the available world names.
     *
     * @return List of all world names in the database.
     * @throws SQLException Database error occurs.
     */
    public List<String> getWorlds() throws SQLException {
        String sql = "SELECT * FROM " + tableName;

        return query(new QueryAllStatement<List<String>>(sql) {
            @Override
            public List<String> processResults(ResultSet set) throws SQLException {
                List<String> worldNames = new ArrayList<>();
                while (set.next()) {
                    String worldName = set.getString(columnWorldName);
                    worldNames.add(worldName);
                }
                return worldNames;
            }
        });
    }

    /**
     * Used to save a list of world names.
     * <p>
     * Already saved names will not be saved.
     *
     * @param worlds List of world names.
     * @throws SQLException Database error occurs.
     */
    public void saveWorlds(Collection<String> worlds) throws SQLException {
        Verify.nullCheck(worlds);
        Set<String> worldsToSave = new HashSet<>(worlds);

        List<String> saved = getWorlds();
        worldsToSave.removeAll(saved);
        if (Verify.isEmpty(worlds)) {
            return;
        }

        String sql = "INSERT INTO " + tableName + " ("
                + columnWorldName
                + ") VALUES (?)";

        executeBatch(new ExecStatement(sql) {
            @Override
            public void prepare(PreparedStatement statement) throws SQLException {
                for (String world : worldsToSave) {
                    statement.setString(1, world);
                    statement.addBatch();
                }
            }
        });
    }

    public String getColumnID() {
        return columnWorldId;
    }

    public String getColumnWorldName() {
        return columnWorldName;
    }

    public Set<String> getWorldNames() throws SQLException {
        return getWorldNames(MiscUtils.getIPlan().getServerUuid());
    }

    public Set<String> getWorldNames(UUID serverUUID) throws SQLException {
        WorldTimesTable worldTimesTable = db.getWorldTimesTable();
        SessionsTable sessionsTable = db.getSessionsTable();
        ServerTable serverTable = db.getServerTable();

        String statementSelectServerID = serverTable.statementSelectServerID;

        String worldIDColumn = worldTimesTable + "." + worldTimesTable.getColumnWorldId();
        String worldSessionIDColumn = worldTimesTable + "." + worldTimesTable.getColumnSessionID();
        String sessionIDColumn = sessionsTable + "." + sessionsTable.getColumnID();
        String sessionServerIDColumn = sessionsTable + "." + sessionsTable.getcolumnServerID();

        String sql = "SELECT DISTINCT " +
                columnWorldName + " FROM " +
                tableName +
                " JOIN " + worldTimesTable + " on " + worldIDColumn + "=" + tableName + "." + columnWorldId +
                " JOIN " + sessionsTable + " on " + worldSessionIDColumn + "=" + sessionIDColumn +
                " WHERE " + statementSelectServerID + "=" + sessionServerIDColumn;

        return query(new QueryStatement<Set<String>>(sql, 1000) {
            @Override
            public void prepare(PreparedStatement statement) throws SQLException {
                statement.setString(1, serverUUID.toString());
            }

            @Override
            public Set<String> processResults(ResultSet set) throws SQLException {
                Set<String> worldNames = new HashSet<>();
                while (set.next()) {
                    worldNames.add(set.getString(columnWorldName));
                }
                return worldNames;
            }
        });
    }
}
