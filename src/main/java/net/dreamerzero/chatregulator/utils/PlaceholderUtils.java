package net.dreamerzero.chatregulator.utils;

import java.util.Set;

import com.velocitypowered.api.proxy.Player;

import net.dreamerzero.chatregulator.InfractionPlayer;
import net.dreamerzero.chatregulator.modules.Statistics;
import net.dreamerzero.chatregulator.utils.TypeUtils.InfractionType;
import net.kyori.adventure.text.minimessage.Template;
import net.kyori.adventure.text.minimessage.template.TemplateResolver;

/**
 * Player Data Collection Utilities
 */
public class PlaceholderUtils {
    /**
     * Obtain placeholders from an {@link InfractionPlayer}
     * @param player the {@link InfractionPlayer}
     * @return placeholders based on this player
     */
    public static TemplateResolver getTemplates(final InfractionPlayer player){
        Set<Template> templates = Set.of(
            Template.template("player", player.username()),
            Template.template("name", player.username()),
            Template.template("flood", String.valueOf(player.getViolations().getCount(InfractionType.FLOOD))),
            Template.template("spam", String.valueOf(player.getViolations().getCount(InfractionType.SPAM))),
            Template.template("regular", String.valueOf(player.getViolations().getCount(InfractionType.REGULAR))),
            Template.template("unicode", String.valueOf(player.getViolations().getCount(InfractionType.UNICODE))),
            Template.template("caps", String.valueOf(player.getViolations().getCount(InfractionType.CAPS)))
        );
        Player p = player.getPlayer();
        if(p != null){
            p.getCurrentServer().ifPresent(server ->
                templates.add(Template.template("server", server.getServer().getServerInfo().getName())));
        }

        return TemplateResolver.templates(templates);
    }

    /**
     * Obtain the global placeholders
     * @return global placeholders
     */
    public static TemplateResolver getGlobalTemplates(){
        return TemplateResolver.templates(
            Template.template("flood", String.valueOf(Statistics.getViolationCount(InfractionType.FLOOD))),
            Template.template("spam", String.valueOf(Statistics.getViolationCount(InfractionType.SPAM))),
            Template.template("regular", String.valueOf(Statistics.getViolationCount(InfractionType.REGULAR))),
            Template.template("command", String.valueOf(Statistics.getViolationCount(InfractionType.BCOMMAND))),
            Template.template("unicode", String.valueOf(Statistics.getViolationCount(InfractionType.UNICODE))),
            Template.template("caps", String.valueOf(Statistics.getViolationCount(InfractionType.CAPS)))
        );
    }

    private PlaceholderUtils(){}
}
