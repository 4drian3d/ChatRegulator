package me.adrianed.chatregulator.api;

import java.nio.file.Path;

import me.adrianed.chatregulator.api.configuration.Configuration;
import me.adrianed.chatregulator.api.configuration.Messages;
import me.adrianed.chatregulator.api.logger.RegulatorLogger;

public interface RegulatorPlugin {
    Path path();

    RegulatorLogger logger();

    Configuration configuration();

    Messages messages();
}