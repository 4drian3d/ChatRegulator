package me.dreamerzero.chatregulator.modules.checks;

import java.util.Set;

import me.dreamerzero.chatregulator.config.Configuration;
import me.dreamerzero.chatregulator.enums.InfractionType;
import me.dreamerzero.chatregulator.utils.CommandUtils;

public class CommandCheck extends AbstractCheck {
    private final Set<String> blockedCommands;

    public CommandCheck(){
        this.blockedCommands = Configuration.getBlacklist().getBlockedCommands();
    }

    @Override
    public void check(String message) {
        for (String blockedCommand : blockedCommands){
            if(CommandUtils.isStartingString(message, blockedCommand)){
                super.string = message;
                super.pattern = blockedCommand;
                super.matcher = null;
                super.detected = true;
                return;
            }
        }
    }

    @Override
    public String getInfractionWord(){
        return super.string;
    }

    @Override
    public InfractionType type() {
        return InfractionType.BCOMMAND;
    }

}
