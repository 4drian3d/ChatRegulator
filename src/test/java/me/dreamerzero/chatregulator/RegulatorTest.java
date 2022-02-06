package me.dreamerzero.chatregulator;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import me.dreamerzero.chatregulator.utils.TestsUtils;

public class RegulatorTest {
    @BeforeAll
    static void initialization(){
        ChatRegulator pl = TestsUtils.createRegulator();
        ChatRegulator.setPlugin(pl);
    }

    @Test
    void setRegulator(){
        ChatRegulator plugin = ChatRegulator.getInstance();
        assertNotNull(plugin);
        assertNotNull(plugin.getLogger());
        assertNotNull(plugin.getProxy());
    }

    public static void set(ChatRegulator pl){
        ChatRegulator.setPlugin(pl);
    }
}
