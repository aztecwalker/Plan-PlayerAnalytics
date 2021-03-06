package main.java.com.djrapitops.plan.command;

import com.djrapitops.plugin.command.CommandType;
import com.djrapitops.plugin.command.TreeCommand;
import com.djrapitops.plugin.command.defaultcmds.StatusCommand;
import main.java.com.djrapitops.plan.Plan;
import main.java.com.djrapitops.plan.command.commands.*;
import main.java.com.djrapitops.plan.settings.Permissions;
import main.java.com.djrapitops.plan.settings.Settings;
import main.java.com.djrapitops.plan.settings.locale.Locale;
import main.java.com.djrapitops.plan.settings.locale.Msg;

/**
 * TreeCommand for the /plan command, and all subcommands.
 * <p>
 * Uses the Abstract Plugin Framework for easier command management.
 *
 * @author Rsl1122
 * @since 1.0.0
 */
public class PlanCommand extends TreeCommand<Plan> {

    /**
     * CommandExecutor class Constructor.
     * <p>
     * Initializes Subcommands
     *
     * @param plugin Current instance of Plan
     */
    public PlanCommand(Plan plugin) {
        super(plugin, "plan", CommandType.CONSOLE, "", "", "plan");
        super.setDefaultCommand("inspect");
        super.setColorScheme(plugin.getColorScheme());
    }

    @Override
    public String[] addHelp() {
        return Locale.get(Msg.CMD_HELP_PLAN).toArray();
    }

    @Override
    public void addCommands() {
        add(
                new InspectCommand(plugin),
                new AnalyzeCommand(plugin),
                new SearchCommand(plugin),
                new InfoCommand(plugin),
                new ReloadCommand(plugin),
                new ManageCommand(plugin),
                new StatusCommand<>(plugin, Permissions.MANAGE.getPermission(), plugin.getColorScheme()),
                new ListCommand()
        );
        RegisterCommand registerCommand = new RegisterCommand(plugin);
        add(
                registerCommand,
                new WebUserCommand(plugin, registerCommand),
                new NetworkCommand(plugin),
                new ListServersCommand(plugin));

        if (Settings.DEV_MODE.isTrue()) {
            add(new DevCommand(plugin));
        }
    }
}
