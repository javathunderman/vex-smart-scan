import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SmartScanTests {

    @Test
    void oldSKUTest() throws IOException {
        assertEquals("RE-VRC-19-5411", PreviousSeasons.getOldSKU("2616J", "RE-VRC-19-8953", false));
    }
    @Test
    void oldCCWMTest() throws IOException {
        assertEquals(0.0, PreviousSeasons.setPCCWM("4001A", "RE-VRC-19-8953", true), 0.001);
    }
}