package me.dreamerzero.chatregulator.commands;

import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.List;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.permission.Tristate;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import me.dreamerzero.chatregulator.InfractionPlayer;
import me.dreamerzero.chatregulator.Regulator;
import me.dreamerzero.chatregulator.config.ConfigManager;
import me.dreamerzero.chatregulator.config.Configuration;
import me.dreamerzero.chatregulator.config.Messages;
import me.dreamerzero.chatregulator.utils.GeneralUtils;
import me.dreamerzero.chatregulator.utils.PlaceholderUtils;
import me.dreamerzero.chatregulator.enums.InfractionType;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.placeholder.Placeholder;
import net.kyori.adventure.text.minimessage.placeholder.PlaceholderResolver;

/**
 * Main Plugin Command
 */
public class ChatRegulatorCommand implements SimpleCommand {
    private Map<UUID, InfractionPlayer> infractionPlayers;
    private Messages.Config messages;
    private ProxyServer server;
    /**
     * ChatRegulatorCommand Contructor
     * @param infractionPlayers the list of infractor players
     * @param server the proxy server
     */
    public ChatRegulatorCommand(Map<UUID, InfractionPlayer> infractionPlayers, ProxyServer server){
        this.infractionPlayers = infractionPlayers;
        this.messages = Configuration.getMessages();
        this.server = server;
    }

    @Override
    public void execute(Invocation invocation) {
        String[] args = invocation.arguments();
        Audience source = invocation.source();
        MiniMessage mm = MiniMessage.miniMessage();
        PlaceholderResolver commandPlaceholder = PlaceholderResolver.resolving("command", invocation.alias());

        if(args.length == 0){
            source.sendMessage(mm.deserialize(messages.getGeneralMessages().getInfoMessage(), commandPlaceholder));
            return;
        }
        switch(args[0].toLowerCase()){
            case "info": case "help": parseHelpCommand(args, source, mm, commandPlaceholder); break;
            case "stats": parseStatsCommand(source, mm); break;
            case "player": parsePlayerCommand(args, source, mm); break;
            case "reset": parseResetCommand(args, source, mm); break;
            case "clear": parseClearCommand(args, source, mm); break;
            case "reload": parseReloadCommand(source, mm); break;
            default: source.sendMessage(mm.deserialize(messages.getGeneralMessages().getUnknowMessage(), PlaceholderResolver.resolving("args", args[0]))); break;
        }
    }

    private void parseHelpCommand(String[] args, Audience source, MiniMessage mm, PlaceholderResolver commandPlaceholder){
        var hmessages = messages.getGeneralMessages().getHelpMessages();
        if(args.length == 1){
            hmessages.getMainHelp().forEach(line -> source.sendMessage(mm.deserialize(line, commandPlaceholder)));
        } else {
            switch(args[1]){
                case "reset": hmessages.getResethelp().forEach(line -> source.sendMessage(mm.deserialize(line, commandPlaceholder))); break;
                case "clear": hmessages.getClearHelp().forEach(line -> source.sendMessage(mm.deserialize(line, commandPlaceholder))); break;
                case "player": hmessages.getPlayerHelp().forEach(line -> source.sendMessage(mm.deserialize(line, commandPlaceholder))); break;
                default: source.sendMessage(mm.deserialize(messages.getGeneralMessages().noArgument())); break;
            }
        }
    }

    private void parseStatsCommand(Audience source, MiniMessage mm){
        for(String line : messages.getGeneralMessages().getStatsFormat()){
            source.sendMessage(mm.deserialize(line, PlaceholderUtils.getGlobalPlaceholders()));
        }
    }

    private void parsePlayerCommand(String[] args, Audience source, MiniMessage mm){
        var gconfig = messages.getGeneralMessages();
        if(args.length >= 2){
            server.getPlayer(args[1]).ifPresentOrElse(player -> {
                InfractionPlayer infractionPlayer = InfractionPlayer.get(player);
                for(String line : gconfig.getPlayerFormat()){
                    source.sendMessage(mm.deserialize(line, PlaceholderUtils.getPlaceholders(infractionPlayer)));
                }
            }, () -> {
                for(Entry<UUID, InfractionPlayer> entry : infractionPlayers.entrySet()){
                    InfractionPlayer iPlayer = entry.getValue();
                    if(iPlayer.username().equals(args[0])){
                        for(String line : gconfig.getPlayerFormat()){
                            source.sendMessage(mm.deserialize(line, PlaceholderUtils.getPlaceholders(iPlayer)));
                        }
                        break;
                    }
                }
                PlaceholderResolver playerPlaceholder = PlaceholderResolver.placeholders(Placeholder.placeholder("player", args[1]));
                source.sendMessage(mm.deserialize(gconfig.playerNotFound(), playerPlaceholder));
            });
        } else {
            source.sendMessage(mm.deserialize(gconfig.noArgument()));
        }
    }

    private void parseResetCommand(String[] args, Audience source, MiniMessage mm){
        if(args.length >= 2){
            server.getPlayer(args[1]).ifPresentOrElse(player -> {
                InfractionPlayer infractionPlayer = InfractionPlayer.get(player);
                var violations = infractionPlayer.getViolations();
                if(args.length >= 3){
                    switch(args[2].toLowerCase()){
                        case "infractions": case "regular":
                            violations.resetViolations(InfractionType.REGULAR);
                            ConfigManager.sendResetMessage(source, InfractionType.REGULAR, infractionPlayer);
                            break;
                        case "flood":
                            violations.resetViolations(InfractionType.FLOOD);
                            ConfigManager.sendResetMessage(source, InfractionType.FLOOD, infractionPlayer);
                            break;
                        case "spam":
                            violations.resetViolations(InfractionType.SPAM);
                            ConfigManager.sendResetMessage(source, InfractionType.SPAM, infractionPlayer);
                            break;
                        case "command": case "commands":
                            violations.resetViolations(InfractionType.BCOMMAND);
                            ConfigManager.sendResetMessage(source, InfractionType.BCOMMAND, infractionPlayer);
                            break;
                        case "unicode":
                            violations.resetViolations(InfractionType.UNICODE);
                            ConfigManager.sendResetMessage(source, InfractionType.UNICODE, infractionPlayer);
                            break;
                        case "caps":
                            violations.resetViolations(InfractionType.CAPS);
                            ConfigManager.sendResetMessage(source, InfractionType.CAPS, infractionPlayer);
                            break;
                        default: break;
                    }
                }
                violations.resetViolations(
                    InfractionType.SPAM,
                    InfractionType.FLOOD,
                    InfractionType.REGULAR,
                    InfractionType.BCOMMAND,
                    InfractionType.UNICODE
                );
                ConfigManager.sendResetMessage(source, InfractionType.NONE, infractionPlayer);
            }, () -> {
                var gmessages = messages.getGeneralMessages();
                for(Entry<UUID, InfractionPlayer> entry : infractionPlayers.entrySet()){
                    InfractionPlayer iPlayer = entry.getValue();
                    if(iPlayer.username().equals(args[1])){
                        for(String line : gmessages.getPlayerFormat()){
                            source.sendMessage(mm.deserialize(line,PlaceholderUtils.getPlaceholders(iPlayer)));
                        }
                        break;
                    }
                }
                PlaceholderResolver playerPlaceholder = PlaceholderResolver.placeholders(Placeholder.placeholder("player", args[1]));
                source.sendMessage(mm.deserialize(gmessages.playerNotFound(), playerPlaceholder));
            });
        } else {
            source.sendMessage(mm.deserialize(messages.getGeneralMessages().noArgument()));
        }
    }

    private void parseClearCommand(String[] args, Audience source, MiniMessage mm){
        var clearmessages = messages.getClearMessages();
        if(args.length >= 2){
            switch(args[1].toLowerCase()){
                case "server":
                    if(args.length >= 3){
                        PlaceholderResolver serverPlaceholder = PlaceholderResolver.resolving("server", args[2]);
                        server.getServer(args[2]).ifPresentOrElse(serverObjetive -> {
                            serverObjetive.sendMessage(GeneralUtils.spacesComponent);
                            source.sendMessage(mm.deserialize(clearmessages.getServerMessage(), serverPlaceholder));
                        }, () -> source.sendMessage(mm.deserialize(clearmessages.getNotFoundServerMessage(), serverPlaceholder)));
                        break;
                    } else if (source instanceof Player) {
                        Player player = (Player)source;
                        player.getCurrentServer().ifPresent(playerServer -> {
                            PlaceholderResolver serverPlaceholder = PlaceholderResolver.resolving("server", playerServer.getServerInfo().getName());
                            playerServer.getServer().sendMessage(GeneralUtils.spacesComponent);
                            source.sendMessage(mm.deserialize(clearmessages.getServerMessage(), serverPlaceholder));
                        });
                    } else {
                        source.sendMessage(mm.deserialize(messages.getGeneralMessages().noArgument()));
                    }
                    break;
                case "player":
                    if(args.length >= 3){
                        server.getPlayer(args[2]).ifPresentOrElse(player -> {
                            player.sendMessage(GeneralUtils.spacesComponent);
                            source.sendMessage(mm.deserialize(
                                clearmessages.getPlayerMessage(),
                                PlaceholderUtils.getPlaceholders(InfractionPlayer.get(player))));
                        }, () ->
                            source.sendMessage(
                                mm.deserialize(
                                    messages.getGeneralMessages().playerNotFound(),
                                    PlaceholderResolver.resolving("player", args[2])
                                )
                            )
                        );
                    } else {
                        source.sendMessage(mm.deserialize(messages.getGeneralMessages().noArgument()));
                    }
                    break;
                default:
                    Audience.audience(server.getAllPlayers()).sendMessage(GeneralUtils.spacesComponent);
                    source.sendMessage(mm.deserialize(clearmessages.getGlobalMessage()));
                    break;
            }
        } else {
            Audience.audience(server.getAllPlayers()).sendMessage(GeneralUtils.spacesComponent);
            source.sendMessage(mm.deserialize(clearmessages.getGlobalMessage()));
        }
    }

    private void parseReloadCommand(Audience sender, MiniMessage mm){
        sender.sendMessage(mm.deserialize(messages.getGeneralMessages().getReloadMessage()));
        Regulator.getInstance().reloadConfig();
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(final Invocation invocation) {
        String[] args = invocation.arguments();
        if(args.length == 0){
            return CompletableFuture.supplyAsync(() -> List.of("info", "help", "clear", "stats", "player"));
        }
        return CompletableFuture.supplyAsync(() -> {
            switch(args[0]){
                case "player":
                    return infractionPlayers.entrySet().stream()
                        .limit(Configuration.getConfig().getGeneralConfig().tabCompleteLimit())
                        .map(x -> x.getValue().username())
                        .collect(Collectors.toList());
                case "help": case "info": return List.of("clear", "player", "reset");
                case "clear":
                    if(args.length < 2){
                        return List.of("server", "player");
                    } else {
                        switch(args[1]){
                            case "server": return server.getAllServers().stream()
                                .map(sv -> sv.getServerInfo().getName())
                                .collect(Collectors.toList());
                            case "player": return server.getAllPlayers().stream()
                                .map(Player::getUsername)
                                .collect(Collectors.toList());
                            default: return List.of();
                        }
                    }
                case "reset":
                    if(args.length == 1){
                        return server.getAllPlayers().stream()
                                .map(Player::getUsername)
                                .collect(Collectors.toList());
                    } else {
                        return List.of("infractions", "regular", "flood", "spam", "unicode", "caps", "all");
                    }
                default: return List.of();
            }
        });
    }

    @Override
    public boolean hasPermission(final Invocation invocation) {
        return invocation.source().getPermissionValue("chatregulator.command") == Tristate.TRUE;
    }

}
