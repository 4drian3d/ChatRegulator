package net.dreamerzero.chatregulator.commands;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.List;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.permission.Tristate;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import de.leonhard.storage.Yaml;
import net.dreamerzero.chatregulator.InfractionPlayer;
import net.dreamerzero.chatregulator.config.ConfigManager;
import net.dreamerzero.chatregulator.utils.GeneralUtils;
import net.dreamerzero.chatregulator.utils.PlaceholderUtils;
import net.dreamerzero.chatregulator.utils.TypeUtils.InfractionType;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;
import net.kyori.adventure.text.minimessage.template.TemplateResolver;

/**
 * Main Plugin Command
 */
public class ChatRegulatorCommand implements SimpleCommand {
    private Map<UUID, InfractionPlayer> infractionPlayers;
    private Yaml messages;
    private ProxyServer server;
    private ConfigManager cManager;
    /**
     * ChatRegulatorCommand Contructor
     * @param infractionPlayers the list of infractor players
     * @param messages the plugin messages
     * @param server the proxy server
     */
    public ChatRegulatorCommand(Map<UUID, InfractionPlayer> infractionPlayers, Yaml messages, ProxyServer server, Yaml config){
        this.infractionPlayers = infractionPlayers;
        this.messages = messages;
        this.server = server;
        this.cManager = new ConfigManager(messages, config);
    }

    @Override
    public void execute(Invocation invocation) {
        String args[] = invocation.arguments();
        var source = invocation.source();
        MiniMessage mm = MiniMessage.miniMessage();
        TemplateResolver commandTemplate = TemplateResolver.templates(Template.template("command", invocation.alias()));

        if(args.length == 0){
            source.sendMessage(mm.deserialize(messages.getString("general.info"), commandTemplate));
        } else if(args.length >= 1){
            switch(args[0].toLowerCase()){
                case "info": case "help":
                    if(args.length == 1){
                        messages.getStringList("general.help.main").forEach(line -> source.sendMessage(mm.deserialize(line, commandTemplate)));
                    } else {
                        switch(args[1]){
                            case "reset": messages.getStringList("general.help.reset").forEach(line -> source.sendMessage(mm.deserialize(line, commandTemplate))); break;
                            case "clear": messages.getStringList("general.help.clear").forEach(line -> source.sendMessage(mm.deserialize(line, commandTemplate))); break;
                            case "player": messages.getStringList("general.help.player").forEach(line -> source.sendMessage(mm.deserialize(line, commandTemplate))); break;
                            default: source.sendMessage(mm.deserialize(messages.getString("general.no-argument"))); break;
                        }
                    }
                    break;
                case "stats":
                    for(String line : messages.getStringList("general.stats")){
                        source.sendMessage(mm.deserialize(line, PlaceholderUtils.getGlobalTemplates()));
                    }
                    break;
                case "player":
                    if(args.length >= 2){
                        var optionalPlayer = server.getPlayer(args[1]);
                        if(optionalPlayer.isPresent()){
                            Player player = optionalPlayer.get();
                            InfractionPlayer infractionPlayer = InfractionPlayer.get(player);
                            for(String line : messages.getStringList("general.player")){
                                source.sendMessage(mm.deserialize(line, PlaceholderUtils.getTemplates(infractionPlayer)));
                            }
                        } else {
                            for(Entry<UUID, InfractionPlayer> entry : infractionPlayers.entrySet()){
                                InfractionPlayer iPlayer = entry.getValue();
                                if(iPlayer.username() == args[0]){
                                    for(String line : messages.getStringList("general.player")){
                                        source.sendMessage(mm.deserialize(line, PlaceholderUtils.getTemplates(iPlayer)));
                                    }
                                    break;
                                }
                            }
                            TemplateResolver platerTemplate = TemplateResolver.templates(Template.template("player", args[1]));
                            source.sendMessage(mm.deserialize(messages.getString("general.player-not-found"), platerTemplate));
                            break;
                        }
                        break;
                    } else {
                        source.sendMessage(mm.deserialize(messages.getString("general.no-argument")));
                        break;
                    }
                case "reset":
                    if(args.length >= 2){
                        var optionalPlayer = server.getPlayer(args[1]);
                        if(optionalPlayer.isPresent()){
                            Player player = optionalPlayer.get();
                            InfractionPlayer infractionPlayer = InfractionPlayer.get(player);
                            if(args.length >= 3){
                                switch(args[2].toLowerCase()){
                                    case "infractions": case "regular":
                                        infractionPlayer.setViolations(InfractionType.REGULAR, 0);
                                        cManager.sendResetMessage(source, InfractionType.REGULAR, infractionPlayer);
                                        break;
                                    case "flood":
                                        infractionPlayer.setViolations(InfractionType.FLOOD, 0);
                                        cManager.sendResetMessage(source, InfractionType.FLOOD, infractionPlayer);
                                        break;
                                    case "spam":
                                        infractionPlayer.setViolations(InfractionType.SPAM, 0);
                                        cManager.sendResetMessage(source, InfractionType.SPAM, infractionPlayer);
                                        break;
                                    case "command": case "commands":
                                        infractionPlayer.setViolations(InfractionType.BCOMMAND, 0);
                                        cManager.sendResetMessage(source, InfractionType.BCOMMAND, infractionPlayer);
                                        break;
                                    case "unicode":
                                        infractionPlayer.setViolations(InfractionType.UNICODE, 0);
                                        cManager.sendResetMessage(source, InfractionType.UNICODE, infractionPlayer);
                                        break;
                                    case "all": case "general":
                                        infractionPlayer.setViolations(InfractionType.SPAM, 0);
                                        infractionPlayer.setViolations(InfractionType.FLOOD, 0);
                                        infractionPlayer.setViolations(InfractionType.REGULAR, 0);
                                        infractionPlayer.setViolations(InfractionType.BCOMMAND, 0);
                                        infractionPlayer.setViolations(InfractionType.UNICODE, 0);
                                        cManager.sendResetMessage(source, InfractionType.NONE, infractionPlayer);
                                        break;
                                }
                            } else {
                                infractionPlayer.setViolations(InfractionType.SPAM, 0);
                                infractionPlayer.setViolations(InfractionType.FLOOD, 0);
                                infractionPlayer.setViolations(InfractionType.REGULAR, 0);
                                infractionPlayer.setViolations(InfractionType.BCOMMAND, 0);
                                infractionPlayer.setViolations(InfractionType.UNICODE, 0);
                                cManager.sendResetMessage(source, InfractionType.NONE, infractionPlayer);
                            }
                        } else {
                            for(Entry<UUID, InfractionPlayer> entry : infractionPlayers.entrySet()){
                                InfractionPlayer iPlayer = entry.getValue();
                                if(iPlayer.username() == args[1]){
                                    for(String line : messages.getStringList("general.player")){
                                        source.sendMessage(mm.deserialize(line,PlaceholderUtils.getTemplates(iPlayer)));
                                    }
                                    break;
                                }
                            }
                            TemplateResolver platerTemplate = TemplateResolver.templates(Template.template("player", args[1]));
                            source.sendMessage(mm.deserialize(messages.getString("general.player-not-found"), platerTemplate));
                            break;
                        }
                        break;
                    } else {
                        source.sendMessage(mm.deserialize(messages.getString("general.no-argument")));
                        break;
                    }
                case "clear":
                    if(args.length >= 2){
                        switch(args[1].toLowerCase()){
                            case "server":
                                if(args.length >= 3){
                                    Optional<RegisteredServer> optionalServer = server.getServer(args[2]);
                                    var serverTemplate = TemplateResolver.templates(Template.template("server", args[2]));
                                    if(optionalServer.isPresent()){
                                        optionalServer.get().sendMessage(GeneralUtils.spacesComponent);
                                        source.sendMessage(mm.deserialize(messages.getString("clear.cleared-server-chat"), serverTemplate));
                                    } else {
                                        source.sendMessage(mm.deserialize(messages.getString("clear.no-server-found"), serverTemplate));
                                    }
                                    break;
                                } else {
                                    if(source instanceof Player) {
                                        Player player = (Player)source;
                                        RegisteredServer playerServer = player.getCurrentServer().get().getServer();
                                        var servername = playerServer.getServerInfo().getName();
                                        var serverTemplate = TemplateResolver.templates(Template.template("server", servername));
                                        playerServer.sendMessage(GeneralUtils.spacesComponent);
                                        source.sendMessage(mm.deserialize(messages.getString("clear.cleared-server-chat"), serverTemplate));
                                    } else {
                                        source.sendMessage(mm.deserialize(messages.getString("general.no-argument")));
                                    }
                                }
                                break;
                            case "player":
                                if(args.length >= 3){
                                    Optional<Player> optionalPlayer = server.getPlayer(args[2]);
                                    if(optionalPlayer.isPresent()){
                                        Player player = optionalPlayer.get();
                                        player.sendMessage(GeneralUtils.spacesComponent);
                                        source.sendMessage(mm.deserialize(
                                            messages.getString("clear.cleared-player-chat"),
                                            PlaceholderUtils.getTemplates(InfractionPlayer.get(player))));
                                    } else {
                                        var playerTemplate = TemplateResolver.templates(Template.template("player", args[2]));
                                        source.sendMessage(mm.deserialize(messages.getString("general.player-not-found"), playerTemplate));
                                    }
                                } else {
                                    source.sendMessage(mm.deserialize(messages.getString("general.no-argument")));
                                }
                                break;
                            default:
                                Audience.audience(server.getAllPlayers()).sendMessage(GeneralUtils.spacesComponent);
                                source.sendMessage(mm.deserialize(messages.getString("clear.global-chat-cleared")));
                                break;
                        }
                    } else {
                        Audience.audience(server.getAllPlayers()).sendMessage(GeneralUtils.spacesComponent);
                        source.sendMessage(mm.deserialize(messages.getString("clear.global-chat-cleared")));
                        break;
                    }
                    break;
                default: source.sendMessage(mm.deserialize(messages.getString("general.unknown-command"), TemplateResolver.templates(Template.template("args", args[0])))); break;
            }
        }
    }

    @Override
    public List<String> suggest(final Invocation invocation) {
        String args[] = invocation.arguments();
        if(args.length == 0){
            return List.of("info", "help", "clear", "stats", "player");
        } else if(args.length >= 1){
            switch(args[0]){
                case "player":
                    return infractionPlayers.entrySet().stream()
                        .limit(messages.getInt("general.limit-tab-complete"))
                        .map(x -> x.getValue().username())
                        .collect(Collectors.toList());
                case "help": case "info": return List.of("clear", "player", "reset");
                case "clear":
                    if(args.length <= 1){
                        return List.of("server", "player");
                    } else {
                        switch(args[1]){
                            case "server": return server.getAllServers().stream()
                                .map(sv -> sv.getServerInfo().getName())
                                .collect(Collectors.toList());
                            case "player": return server.getAllPlayers().stream()
                                .map(Player::getUsername)
                                .collect(Collectors.toList());
                        }
                    }
                    break;
                case "reset":
                    if(args.length == 1){
                        return server.getAllPlayers().stream()
                                .map(Player::getUsername)
                                .collect(Collectors.toList());
                    } else {
                        return List.of("infractions", "regular", "flood", "spam", "all");
                    }
            }
        }
        return List.of("");
    }

    @Override
    public boolean hasPermission(final Invocation invocation) {
        return invocation.source().getPermissionValue("chatregulator.command") == Tristate.TRUE;
    }

}
