package net.dreamerzero.chatregulator.listener;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.event.command.CommandExecuteEvent.CommandResult;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import org.slf4j.Logger;

import net.dreamerzero.chatregulator.Regulator;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;

public class CommandListener {
    private final ProxyServer server;
    private Logger logger;

    public CommandListener(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;
    }

    @Subscribe
    public void onCommand(CommandExecuteEvent event){
        if (!(event.getCommandSource() instanceof Player player)) {
            return;
        }
        var command = event.getCommand();

        List<String> commandsChecked = Regulator.getConfig().getStringList("commands-checked");
        boolean isCommand = false;

        for (String commandChecked : commandsChecked) {
            if (command.startsWith(commandChecked)) {
                isCommand = true;
                break;
            }
        }
        if (!isCommand) return;

        List<String> blockedWords = Regulator.getBlackList().getStringList("blocked-words");

        List<Template> TEMPLATES = List.of(
            Template.of("player", player.getUsername()),
            Template.of("message", command),
            Template.of("server", player.getCurrentServer().get().getServerInfo().getName()));

        for (String blockedWord : blockedWords) {
            Matcher match = Pattern.compile(blockedWord).matcher(command);

            if (match.find()) {
                event.setResult(CommandResult.denied());
                player.sendMessage(
                    MiniMessage.get().parse(
                        Regulator.getConfig().getString("messages.blocked-message"), TEMPLATES));
                server.getAllPlayers().stream().filter(
                    op -> op.hasPermission("regulator.notifications")).forEach(op -> {
                        op.sendMessage(
                            MiniMessage.get().parse(
                                Regulator.getConfig().getString("messages.infraction-detected"), TEMPLATES));
                    });
                if (Regulator.getConfig().getBoolean("debug")){
                    logger.info("User Detected: " + player.getUsername());
                    logger.info("Command: " + command);
                    logger.info("Pattern:" + match.pattern());
                    logger.info("Results: " + match.results().toList().toString());
                }
                break;
            }
        }
    }
}
