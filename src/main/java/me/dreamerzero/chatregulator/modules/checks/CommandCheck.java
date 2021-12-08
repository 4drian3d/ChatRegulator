package me.dreamerzero.chatregulator.modules.checks;

import java.util.Set;

import me.dreamerzero.chatregulator.config.Configuration;
import me.dreamerzero.chatregulator.utils.TypeUtils.InfractionType;

public class CommandCheck extends AbstractCheck {
    private Set<String> blockedCommands;

    public CommandCheck(){
        this.blockedCommands = Configuration.getBlacklist().getBlockedCommands();
    }

    @Override
    public void check(String message) {
        for (String blockedCommand : blockedCommands){
            if(blockedCommand.equalsIgnoreCase(message)) {
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
