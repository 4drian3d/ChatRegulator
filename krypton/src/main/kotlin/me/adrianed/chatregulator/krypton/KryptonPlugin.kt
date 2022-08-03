package me.adrianed.chatregulator.krypton

import com.google.inject.Inject
import org.apache.logging.log4j.Logger
import org.kryptonmc.api.Server
import org.kryptonmc.api.event.Listener
import org.kryptonmc.api.event.server.ServerStartEvent
import org.kryptonmc.api.plugin.annotation.Plugin

@Plugin(
    "chatregulator",
    "ChatRegulator",
    "4.0.0",
    "A ChatRegulator for Krypton"
)
class KryptonPlugin @Inject constructor(
    val server: Server,
    val logger: Logger
) {

    @Listener
    fun onStart(event: ServerStartEvent) {
        logger.info("ChatRegulator aea")
    }
    
}