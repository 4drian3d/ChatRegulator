package net.dreamerzero.chatregulator.commands;

import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.List;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.permission.Tristate;
import com.velocitypowered.api.proxy.ProxyServer;

import de.leonhard.storage.Yaml;
import net.dreamerzero.chatregulator.config.ConfigManager;
import net.dreamerzero.chatregulator.utils.InfractionPlayer;
import net.dreamerzero.chatregulator.utils.TypeUtils.InfractionType;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;

public class ChatRegulatorCommand implements SimpleCommand {
    private Map<UUID, InfractionPlayer> infractionPlayers;
    private Yaml config;
    private ProxyServer server;
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
            int floods = 0, regular = 0, spam = 0;

            for(Entry<UUID, InfractionPlayer> player : infractionPlayers.entrySet()){
                regular = regular + player.getValue().getRegularInfractions();
                floods = floods + player.getValue().getFloodInfractions();
                spam = spam + player.getValue().getSpamInfractions();
            }

            ArrayList<Template> templates = new ArrayList<>();
            templates.add(Template.of("flood", String.valueOf(floods)));
            templates.add(Template.of("spam", String.valueOf(spam)));
            templates.add(Template.of("regular", String.valueOf(regular)));
            switch(args[0]){
                case "info":
                    source.sendMessage(mm.parse(config.getString("general.messages.info")));
                    break;
                case "stats":
                    for(String line : config.getStringList("general.messages.stats")){
                        source.sendMessage(mm.parse(line, templates));
                    }
                    break;
                case "player":
                    if(args.length >= 2){
                        var optionalPlayer = server.getPlayer(args[1]);
                        if(optionalPlayer.isPresent()){
                            var infractionPlayer = infractionPlayers.get(optionalPlayer.get().getUniqueId());
                            for(String line : config.getStringList("general.messages.player")){
                                source.sendMessage(mm.parse(line,
                                    Template.of("player", infractionPlayer.username()),
                                    Template.of("regular", String.valueOf(infractionPlayer.getRegularInfractions())),
                                    Template.of("flood", String.valueOf(infractionPlayer.getFloodInfractions())),
                                    Template.of("spam", String.valueOf(infractionPlayer.getSpamInfractions()))));
                            }
                        } else {
                            for(Entry<UUID, InfractionPlayer> entry : infractionPlayers.entrySet()){
                                var username = entry.getValue().username();
                                if(entry.getValue().username() == args[1]){
                                    var infractionPlayer = entry.getValue();
                                    for(String line : config.getStringList("general.messages.player")){
                                        source.sendMessage(mm.parse(line,
                                            Template.of("player", username),
                                            Template.of("regular", String.valueOf(infractionPlayer.getRegularInfractions())),
                                            Template.of("flood", String.valueOf(infractionPlayer.getFloodInfractions())),
                                            Template.of("spam", String.valueOf(infractionPlayer.getSpamInfractions()))));
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
                            var infractionPlayer = infractionPlayers.get(optionalPlayer.get().getUniqueId());
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
                                var username = entry.getValue().username();
                                if(entry.getValue().username() == args[1]){
                                    var infractionPlayer = entry.getValue();
                                    for(String line : config.getStringList("general.messages.player")){
                                        source.sendMessage(mm.parse(line,
                                            Template.of("player", username),
                                            Template.of("regular", String.valueOf(infractionPlayer.getRegularInfractions())),
                                            Template.of("flood", String.valueOf(infractionPlayer.getFloodInfractions())),
                                            Template.of("spam", String.valueOf(infractionPlayer.getSpamInfractions()))));
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
                ArrayList<String> playerList = new ArrayList<>();
                server.getAllPlayers().forEach(player -> playerList.add(player.getUsername()));
                return playerList;
            }
            default: return null;
        }
    }

    @Override
    public boolean hasPermission(final Invocation invocation) {
        return invocation.source().getPermissionValue("chatregulator.command") == Tristate.TRUE;
    }

}
