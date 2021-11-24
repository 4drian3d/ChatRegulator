package net.dreamerzero.chatregulator.modules.checks;

import java.util.List;

import de.leonhard.storage.Yaml;

public class CommandCheck extends AbstractCheck {
    private Yaml blacklist;

    public CommandCheck(Yaml blacklist){
        this.blacklist = blacklist;
    }

    @Override
    public void check(String message) {
        List<String> blockedCommands = blacklist.getStringList("blocked-commands");
        for (String blockedCommand : blockedCommands){
            if(blockedCommand.equalsIgnoreCase(message)) {
                super.string = message;
                super.pattern = blockedCommand;
                super.matcher = null;
                super.detected = true;
                return;
            }
        }
        super.detected = false;
    }

    @Override
    public String getInfractionWord(){
        return super.string;
    }

}
