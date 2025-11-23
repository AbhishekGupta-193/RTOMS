package eatclub.rtoms.Config;

import org.junit.jupiter.api.Test;
import org.springframework.context.support.StaticApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

class DotEnvApplicationInitializerTest {

    @Test
    void testInitializerRuns() {
        DotEnvApplicationInitializer init = new DotEnvApplicationInitializer();
        StaticApplicationContext ctx = new StaticApplicationContext();

        init.initialize(ctx);

        // Since DotEnv may not load anything, just ensure no crash
        assertTrue(true);
    }
}
