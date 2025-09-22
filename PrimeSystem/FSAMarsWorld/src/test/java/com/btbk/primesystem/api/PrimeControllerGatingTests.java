package com.btbk.primesystem.api;

import com.btbk.primesystem.model.Attributes;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PrimeControllerGatingTests {
    // Helper to call the gating logic
    private Attributes gate(Attributes a, PrimeController.CharacterType t) {
        // Use reflection to call private static method
        try {
            var m = PrimeController.class.getDeclaredMethod("applyCharacterGates", Attributes.class, PrimeController.CharacterType.class);
            m.setAccessible(true);
            return (Attributes) m.invoke(null, a, t);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Base attributes: all set to 100 except force/soul/spirit (-1)
    private Attributes base() {
        return new Attributes(100, 100, 100, 100.0, 100.0, 100.0, 100.0, -1.0, -1.0, -1.0);
    }

    @Test
    void testMagicGating() {
        Attributes gated = gate(base(), PrimeController.CharacterType.MAGIC);
        assertEquals(-1.0, gated.divine(), "MAGIC should gate divine");
        assertEquals(100.0, gated.mind(), "MAGIC should not gate mind");
    }

    @Test
    void testHumanoidGating() {
        Attributes gated = gate(base(), PrimeController.CharacterType.HUMANOID);
        assertEquals(-1.0, gated.divine(), "HUMANOID should gate divine");
        assertEquals(100.0, gated.mind(), "HUMANOID should not gate mind");
    }

    @Test
    void testBeastGating() {
        Attributes gated = gate(base(), PrimeController.CharacterType.BEAST);
        assertEquals(-1.0, gated.mind(), "BEAST should gate mind");
        assertEquals(-1.0, gated.intent(), "BEAST should gate intent");
        assertEquals(-1.0, gated.divine(), "BEAST should gate divine");
    }

    @Test
    void testMbeastGating() {
        Attributes gated = gate(base(), PrimeController.CharacterType.MBEAST);
        assertEquals(-1.0, gated.intent(), "MBEAST should gate intent");
        assertEquals(-1.0, gated.divine(), "MBEAST should gate divine");
    }

    @Test
    void testGolemGating() {
        Attributes gated = gate(base(), PrimeController.CharacterType.GOLEM);
        assertEquals(-1.0, gated.endurance(), "GOLEM should gate endurance");
        assertEquals(-1.0, gated.intent(), "GOLEM should gate intent");
        assertEquals(-1.0, gated.divine(), "GOLEM should gate divine");
        assertEquals(-1.0, gated.spirit(), "GOLEM should gate spirit");
        assertEquals(-1.0, gated.force(), "GOLEM should gate force");
        assertEquals(-1.0, gated.soul(), "GOLEM should gate soul");
    }

    @Test
    void testDivGating() {
        Attributes gated = gate(base(), PrimeController.CharacterType.DIV);
        assertEquals(100.0, gated.divine(), "DIV should not gate divine");
    }

    @Test
    void testDiv2Gating() {
        Attributes gated = gate(base(), PrimeController.CharacterType.DIV2);
        assertEquals(-1.0, gated.mind(), "DIV2 should gate mind");
    }
}

