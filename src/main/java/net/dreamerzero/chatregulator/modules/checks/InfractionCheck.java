package net.dreamerzero.chatregulator.modules.checks;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.leonhard.storage.Yaml;

/**
 * Utilities for the detection of restringed words
 */
public class InfractionCheck extends AbstractCheck {
    private Yaml blacklist;

    /**
     * Create a new infringement test
     * @param blacklist the blacklist config
     */
    public InfractionCheck(Yaml blacklist){
        this.blacklist = blacklist;
    }

    @Override
    public void check(String string){
        List<String> blockedWords = blacklist.getStringList("blocked-words");
        super.string = string;
        for (String blockedWord : blockedWords){
            Matcher match = Pattern.compile(blockedWord).matcher(string);
            if(match.find()){
                super.pattern = blockedWord;
                super.matcher = match;
                super.detected = true;
                return;
            }
        }
        super.detected = false;
    }

    public String replaceInfraction(){
        return super.matcher.replaceAll("***");
    }
}
