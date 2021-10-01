package net.dreamerzero.chatregulator.utils;

import com.velocitypowered.api.proxy.Player;

import net.dreamerzero.chatregulator.utils.TypeUtils.InfractionType;

public class InfractionPlayer {
    private final Player player;
    private String lastMessage;
    private int floodViolations;
    private int regularViolations;
    private int spamViolations;

    public InfractionPlayer(Player player){
        this.player = player;
        this.lastMessage = null;
        this.floodViolations = 0;
        this.regularViolations = 0;
        this.spamViolations = 0;
    }

    public String getLastMessage(){
        return lastMessage;
    }

    public void setLastMessage(String newLastMessage){
        lastMessage = newLastMessage;
    }

    public void addViolation(InfractionType type){
        switch(type){
            case SPAM: spamViolations += 1; break;
            case REGULAR: regularViolations += 1; break;
            case FLOOD: floodViolations += 1; break;
            case NONE: return;
        }
    }

    public void setViolations(InfractionType type, int newViolationsCount){
        switch(type){
            case SPAM: spamViolations = newViolationsCount; break;
            case REGULAR: regularViolations = newViolationsCount; break;
            case FLOOD: floodViolations = newViolationsCount; break;
            case NONE: return;
        }
    }

    public int getFloodInfractions(){
        return floodViolations;
    }

    public int getRegularInfractions(){
        return regularViolations;
    }


    public int getSpamInfractions(){
        return spamViolations;
    }

    public Player getPlayer(){
        return player;
    }
}
