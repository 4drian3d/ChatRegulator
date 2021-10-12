package net.dreamerzero.chatregulator.commands;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.ArrayList;
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

/**
 * Main Plugin Command
 */
public class ChatRegulatorCommand implements SimpleCommand {
    private Map<UUID, InfractionPlayer> infractionPlayers;
    private Yaml config;
    private ProxyServer server;
    /**
     * ChatRegulatorCommand Contructor
     * @param infractionPlayers the list of infractor players
     * @param config the plugin config
     * @param server the proxy server
     */
    public ChatRegulatorCommand(Map<UUID, InfractionPlayer> infractionPlayers, Yaml config, ProxyServer server){
        this.infractionPlayers = infractionPlayers;
        this.config = config;
        this.server = server;
    }

    @Override
    public void execute(Invocation invocation) {
        String args[] = invocation.arguments();
        var source = invocation.source();
        MiniMessage mm = MiniMessage.miniMessage();

        if(args.length == 0){
            source.sendMessage(mm.parse(config.getString("general.messages.info")));
        } else if(args.length >= 1){
            switch(args[0].toLowerCase()){
                case "info":
                    source.sendMessage(mm.parse(config.getString("general.messages.info")));
                    break;
                case "stats":
                    for(String line : config.getStringList("general.messages.stats")){
                        source.sendMessage(mm.parse(line, PlaceholderUtils.getGlobalTemplates()));
                    }
                    break;
                case "player":
                    if(args.length >= 2){
                        var optionalPlayer = server.getPlayer(args[1]);
                        if(optionalPlayer.isPresent()){
                            Player player = optionalPlayer.get();
                            InfractionPlayer infractionPlayer = InfractionPlayer.get(player);
                            for(String line : config.getStringList("general.messages.player")){
                                source.sendMessage(mm.parse(line, PlaceholderUtils.getTemplates(infractionPlayer)));
                            }
                        } else {
                            for(Entry<UUID, InfractionPlayer> entry : infractionPlayers.entrySet()){
                                if(entry.getValue().username() == args[0]){
                                    for(String line : config.getStringList("general.messages.player")){
                                        source.sendMessage(mm.parse(line, PlaceholderUtils.getTemplates(entry.getValue())));
                                    }
                                    break;
                                }
                            }
                            source.sendMessage(mm.parse(config.getString("general.messages.player-not-found"), "player", args[1]));
                            break;
                        }
                        break;
                    } else {
                        source.sendMessage(mm.parse(config.getString("general.messages.no-argument")));
                        break;
                    }
                case "reset":
                    if(args.length >= 2){
                        var optionalPlayer = server.getPlayer(args[1]);
                        if(optionalPlayer.isPresent()){
                            Player player = optionalPlayer.get();
                            InfractionPlayer infractionPlayer = InfractionPlayer.get(player);
                            ConfigManager cManager = new ConfigManager(config);
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
                                    case "all": case "general":
                                        infractionPlayer.setViolations(InfractionType.SPAM, 0);
                                        infractionPlayer.setViolations(InfractionType.FLOOD, 0);
                                        infractionPlayer.setViolations(InfractionType.REGULAR, 0);
                                        cManager.sendResetMessage(source, InfractionType.NONE, infractionPlayer);
                                        break;
                                }
                            } else {
                                infractionPlayer.setViolations(InfractionType.SPAM, 0);
                                infractionPlayer.setViolations(InfractionType.FLOOD, 0);
                                infractionPlayer.setViolations(InfractionType.REGULAR, 0);
                                cManager.sendResetMessage(source, InfractionType.NONE, infractionPlayer);
                            }
                        } else {
                            for(Entry<UUID, InfractionPlayer> entry : infractionPlayers.entrySet()){
                                if(entry.getValue().username() == args[1]){
                                    for(String line : config.getStringList("general.messages.player")){
                                        source.sendMessage(mm.parse(line,PlaceholderUtils.getTemplates(entry.getValue())));
                                    }
                                    break;
                                }
                            }
                            source.sendMessage(mm.parse(config.getString("general.messages.player-not-found"), "player", args[1]));
                            break;
                        }
                        break;
                    } else {
                        source.sendMessage(mm.parse(config.getString("general.messages.no-argument")));
                        break;
                    }
                case "clear":
                    if(args.length >= 2){
                        switch(args[1].toLowerCase()){
                            case "server":
                                if(args.length >= 3){
                                    Optional<RegisteredServer> optionalServer = server.getServer(args[2]);
                                    if(optionalServer.isPresent()){
                                        optionalServer.get().sendMessage(GeneralUtils.spacesComponent);
                                        source.sendMessage(mm.parse(config.getString("clear.messages.cleared-server-chat"), "server", args[2]));
                                    } else {
                                        source.sendMessage(mm.parse(config.getString("clear.messages.no-server-found"), "server", args[2]));
                                    }
                                    break;
                                } else {
                                    if(source instanceof Player) {
                                        Player player = (Player)source;
                                        RegisteredServer playerServer = player.getCurrentServer().get().getServer();
                                        playerServer.sendMessage(GeneralUtils.spacesComponent);
                                        source.sendMessage(mm.parse(config.getString("clear.messages.cleared-server-chat"), "server", playerServer.getServerInfo().getName()));
                                    } else {
                                        source.sendMessage(mm.parse(config.getString("general.messages.no-argument")));
                                    }
                                }
                                break;
                            case "player":
                                if(args.length >= 3){
                                    Optional<Player> optionalPlayer = server.getPlayer(args[2]);
                                    if(optionalPlayer.isPresent()){
                                        Player player = optionalPlayer.get();
                                        player.sendMessage(GeneralUtils.spacesComponent);
                                        source.sendMessage(mm.parse(
                                            config.getString("clear.messages.cleared-player-chat"),
                                            PlaceholderUtils.getTemplates(InfractionPlayer.get(player))));
                                    } else {
                                        source.sendMessage(mm.parse(config.getString("general.messages.player-not-found"), "player", args[2]));
                                    }
                                } else {
                                    source.sendMessage(mm.parse(config.getString("general.messages.no-argument")));
                                }
                                break;
                            default:
                                Audience.audience(server.getAllPlayers()).sendMessage(GeneralUtils.spacesComponent);
                                source.sendMessage(mm.parse(config.getString("clear.messages.global-chat-cleared")));
                                break;
                        }
                    } else {
                        Audience.audience(server.getAllPlayers()).sendMessage(GeneralUtils.spacesComponent);
                        source.sendMessage(mm.parse(config.getString("clear.messages.global-chat-cleared")));
                        break;
                    }
                    break;
                default: source.sendMessage(mm.parse(config.getString("general.messages.unknown-command"), "args", args[0])); break;
            }
        }
    }

    @Override
    public List<String> suggest(final Invocation invocation) {
        String args[] = invocation.arguments();
        switch(args.length){
            case 0: return List.of("info", "stats", "player");
            case 1: if(args[0] == "player"){
                //TODO: Change this in java 16 update
                /*
                infractionPlayers.entrySet().stream()
                    .limit(config.getInt("general.limit-tab-complete"))
                    .map(x -> x.getValue().username()).toList()
                */
                ArrayList<String> players = new ArrayList<>();
                infractionPlayers.entrySet().forEach(infractionPlayer -> {
                    players.add(infractionPlayer.getValue().username());
                });
                return players;
            }
            default: return List.of("");
        }
    }

    @Override
    public boolean hasPermission(final Invocation invocation) {
        return invocation.source().getPermissionValue("chatregulator.command") == Tristate.TRUE;
    }

}
