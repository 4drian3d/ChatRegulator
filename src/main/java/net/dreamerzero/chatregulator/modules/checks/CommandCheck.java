package net.dreamerzero.chatregulator.modules.checks;

import java.util.Set;

import net.dreamerzero.chatregulator.config.Configuration;

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

}
