package net.dreamerzero.chatregulator.modules.checks;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dreamerzero.chatregulator.config.Configuration;

public class CommandTest {
    @BeforeAll
    static void loadConfig(){
        Logger logger = LoggerFactory.getLogger(CommandTest.class);
        Configuration.loadConfig(Paths.get("build", "reports", "tests", "test"), logger);
    }

    @Test
    @DisplayName("Command Check")
    void blockedCommandsTest(){
        String command = "execute";

        CommandCheck cCheck = new CommandCheck();

        cCheck.check(command);

        assertTrue(cCheck.isInfraction());
    }
}
