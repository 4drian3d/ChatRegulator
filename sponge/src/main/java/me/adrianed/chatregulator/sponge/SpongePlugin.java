package me.adrianed.chatregulator.sponge;

import org.spongepowered.api.Server;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.StartedEngineEvent;
import org.spongepowered.plugin.builtin.jvm.Plugin;

import com.google.inject.Inject;
import org.apache.logging.log4j.Logger;

@Plugin("chatregulator")
public class SpongePlugin {
    private final Logger logger;

    @Inject
    public SpongePlugin(Logger logger) {
        this.logger = logger;
    }

    @Listener
    public void onServerStart(final StartedEngineEvent<Server> event) {
        logger.info("ChatRegulator Sponge");
    }
}
