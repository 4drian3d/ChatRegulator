package me.dreamerzero.chatregulator.commands;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.List;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.permission.Tristate;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus.Internal;

import me.dreamerzero.chatregulator.InfractionPlayer;
import me.dreamerzero.chatregulator.ChatRegulator;
import me.dreamerzero.chatregulator.config.ConfigManager;
import me.dreamerzero.chatregulator.config.Configuration;
import me.dreamerzero.chatregulator.config.Messages;
import me.dreamerzero.chatregulator.utils.GeneralUtils;
import me.dreamerzero.chatregulator.utils.PlaceholderUtils;
import me.dreamerzero.chatregulator.enums.InfractionType;
import me.dreamerzero.chatregulator.enums.Permissions;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.placeholder.Placeholder;
import net.kyori.adventure.text.minimessage.placeholder.PlaceholderResolver;

/**
 * Main Plugin Command
 */
@Internal
public class ChatRegulatorCommand implements SimpleCommand {
    private Map<UUID, InfractionPlayer> infractionPlayers;
    private Messages.Config messages;
    private ProxyServer server;
    /**
     * ChatRegulatorCommand Contructor
     * @param infractionPlayers the list of infractor players
     * @param server the proxy server
     */
    public ChatRegulatorCommand(@NotNull Map<UUID, InfractionPlayer> infractionPlayers, @NotNull ProxyServer server){
        this.infractionPlayers = infractionPlayers;
        this.messages = Configuration.getMessages();
        this.server = server;
    }

    @Override
    public void execute(final Invocation invocation) {
        String[] args = invocation.arguments();
        Audience source = invocation.source();
        MiniMessage mm = MiniMessage.miniMessage();
        PlaceholderResolver commandPlaceholder = PlaceholderResolver.placeholders(Placeholder.raw("command", invocation.alias()));

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
            default: source.sendMessage(mm.deserialize(
                messages.getGeneralMessages().getUnknowMessage(),
                PlaceholderResolver.placeholders(Placeholder.raw("args", args[0]))));
                break;
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
        var placeholders = PlaceholderUtils.getGlobalPlaceholders();
        messages.getGeneralMessages().getStatsFormat().forEach(ln -> source.sendMessage(mm.deserialize(ln, placeholders)));
    }

    private void parsePlayerCommand(String[] args, Audience source, MiniMessage mm){
        var gconfig = messages.getGeneralMessages();
        if(args.length >= 2){
            server.getPlayer(args[1]).ifPresentOrElse(player -> {
                InfractionPlayer infractionPlayer = InfractionPlayer.get(player);
                var placeholders = PlaceholderUtils.getPlaceholders(infractionPlayer);
                for(String line : gconfig.getPlayerFormat()){
                    source.sendMessage(mm.deserialize(line, placeholders));
                }
            }, () -> {
                infractionPlayers.forEach((uuid, iPlayer) -> {
                    if(iPlayer.username().equals(args[0])){
                        var placeholders = PlaceholderUtils.getPlaceholders(iPlayer);
                        for(String line : gconfig.getPlayerFormat()){
                            source.sendMessage(mm.deserialize(line, placeholders));
                        }
                        return;
                    }
                });
                source.sendMessage(mm.deserialize(
                    gconfig.playerNotFound(),
                    PlaceholderResolver.placeholders(
                        Placeholder.raw("player", args[1])
                    )
                ));
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
                infractionPlayers.forEach((uuid, iPlayer)->{
                    if(iPlayer.username().equals(args[1])){
                        var placeholders = PlaceholderUtils.getPlaceholders(iPlayer);
                        gmessages.getPlayerFormat().forEach(ln -> source.sendMessage(mm.deserialize(ln, placeholders)));
                        return;
                    }
                });
                source.sendMessage(mm.deserialize(
                    gmessages.playerNotFound(),
                    PlaceholderResolver.placeholders(
                        Placeholder.raw("player", args[1])
                    )
                ));
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
                        PlaceholderResolver serverPlaceholder = PlaceholderResolver.placeholders(Placeholder.raw("server", args[2]));
                        server.getServer(args[2]).ifPresentOrElse(serverObjetive -> {
                            serverObjetive.sendMessage(GeneralUtils.spacesComponent);
                            source.sendMessage(mm.deserialize(clearmessages.getServerMessage(), serverPlaceholder));
                        }, () -> source.sendMessage(mm.deserialize(clearmessages.getNotFoundServerMessage(), serverPlaceholder)));
                        break;
                    } else if (source instanceof Player) {
                        Player player = (Player)source;
                        player.getCurrentServer().ifPresent(playerServer -> {
                            playerServer.getServer().sendMessage(GeneralUtils.spacesComponent);
                            player.sendMessage(mm.deserialize(
                                clearmessages.getServerMessage(),
                                PlaceholderResolver.placeholders(
                                    Placeholder.raw("server", playerServer.getServerInfo().getName())
                                )
                            ));
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
                                    PlaceholderResolver.placeholders(
                                        Placeholder.raw("player", args[2])
                                    )
                                )
                            )
                        );
                    } else {
                        source.sendMessage(mm.deserialize(messages.getGeneralMessages().noArgument()));
                    }
                    break;
                default:
                    server.sendMessage(GeneralUtils.spacesComponent);
                    source.sendMessage(mm.deserialize(clearmessages.getGlobalMessage()));
                    break;
            }
        } else {
            server.sendMessage(GeneralUtils.spacesComponent);
            source.sendMessage(mm.deserialize(clearmessages.getGlobalMessage()));
        }
    }

    private void parseReloadCommand(Audience sender, MiniMessage mm){
        sender.sendMessage(mm.deserialize(messages.getGeneralMessages().getReloadMessage()));
        ChatRegulator.getInstance().reloadConfig();
    }

    private static final List<String> subCommandsCompletions = List.of("info", "help", "clear", "stats", "player");

    @Override
    public CompletableFuture<List<String>> suggestAsync(final Invocation invocation) {
        String[] args = invocation.arguments();
        if(args.length == 0){
            return CompletableFuture.completedFuture(subCommandsCompletions);
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
                            case "server": return getServerNames();
                            case "player": return this.getPlayerNames();
                            default: return List.of();
                        }
                    }
                case "reset":
                    return args.length == 1 ? this.getPlayerNames() : subCommandsCompletions;
                default: return List.of();
            }
        });
    }

    private List<String> getPlayerNames(){
        List<String> players = new ArrayList<>();
        for(var player : server.getAllPlayers()){
            players.add(player.getUsername());
        }
        return players;
    }

    private List<String> getServerNames(){
        List<String> servers = new ArrayList<>();
        for(var sv : server.getAllServers()){
            servers.add(sv.getServerInfo().getName());
        }
        return servers;
    }

    @Override
    public boolean hasPermission(final Invocation invocation) {
        return invocation.source().getPermissionValue(Permissions.COMMAND) == Tristate.TRUE;
    }

}
