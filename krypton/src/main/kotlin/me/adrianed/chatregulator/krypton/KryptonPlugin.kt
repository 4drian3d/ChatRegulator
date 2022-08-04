package me.adrianed.chatregulator.krypton

import com.google.inject.Inject
import org.apache.logging.log4j.Logger
import org.kryptonmc.api.Server
import org.kryptonmc.api.event.Listener
import org.kryptonmc.api.event.server.ServerStartEvent
import org.kryptonmc.api.plugin.annotation.Plugin
import org.kryptonmc.api.plugin.annotation.DataFolder

import me.adrianed.chatregulator.api.RegulatorPlugin
import me.adrianed.chatregulator.api.logger.Log4JLogger
import me.adrianed.chatregulator.api.logger.RegulatorLogger
import me.adrianed.chatregulator.api.configuration.Configuration
import me.adrianed.chatregulator.api.configuration.Messages
import me.adrianed.chatregulator.api.configuration.Loader

import java.nio.file.Path

@Plugin(
    "chatregulator",
    "ChatRegulator",
    "4.0.0-SNAPSHOT",
    "A ChatRegulator for Krypton"
)
class KryptonPlugin @Inject constructor(
    val server: Server,
    val kryptonLogger: Logger,
    @DataFolder val path: Path
) : RegulatorPlugin {
    lateinit var config: Configuration
    lateinit var messages: Messages


    @Listener
    fun onStart(event: ServerStartEvent) {
        this.config = Loader.loadConfiguration(this)
        this.messages = Loader.loadMessages(this)
        logger().info("ChatRegulator Krypton")
    }

    override fun path() : Path = this.path
    override fun logger() : RegulatorLogger = Log4JLogger(/*this.kryptonLogger*/)
    override fun configuration() : Configuration {
        //TODO
        return this.config
    }

    override fun messages() : Messages {
        return this.messages
    }
    
}