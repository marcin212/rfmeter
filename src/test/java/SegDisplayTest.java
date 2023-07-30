import com.utilitymeters.powermeter.client.screens.wigets.SegDisplay;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class SegDisplayTest {

    @Test
    void testSegDisplay_4_2_with_1_234_567() {
        var seg = new SegDisplay(4, 2, true);
        Assertions.assertEquals("123M",seg.convertToDisplayNum(1_234_567).numberText());
        Assertions.assertEquals(".",seg.convertToDisplayNum(1_234_567).dotText());
    }

    @Test
    void testSegDisplay_4_2_with_0() {
        var seg = new SegDisplay(4, 2, true);
        Assertions.assertEquals(" 000",seg.convertToDisplayNum(0).numberText());
        Assertions.assertEquals(" .",seg.convertToDisplayNum(0).dotText());

    }

    @Test
    void testSegDisplay_4_2_with_10() {
        var seg = new SegDisplay(4, 2, true);
        Assertions.assertEquals("1000",seg.convertToDisplayNum(10).numberText());
        Assertions.assertEquals(" .",seg.convertToDisplayNum(10).dotText());
    }

    @Test
    void testSegDisplay_4_2_with_100() {
        var seg = new SegDisplay(4, 2, true);
        Assertions.assertEquals("010K",seg.convertToDisplayNum(100).numberText());
        Assertions.assertEquals(".",seg.convertToDisplayNum(100).dotText());
    }

    @Test
    void testSegDisplay_4_2_with_1_000() {
        var seg = new SegDisplay(4, 2, true);
        Assertions.assertEquals("100K",seg.convertToDisplayNum(1_000).numberText());
        Assertions.assertEquals(".",seg.convertToDisplayNum(1_000).dotText());
    }

    @Test
    void testSegDisplay_4_2_with_9_223_372_036_854_775_807L() {
        var seg = new SegDisplay(4, 2, true);
        Assertions.assertEquals("922E",seg.convertToDisplayNum(9_223_372_036_854_775_807L).numberText());
        Assertions.assertEquals(".",seg.convertToDisplayNum(9_223_372_036_854_775_807L).dotText());
    }

    @Test
    void testSegDisplay_6_4_with_1_234_567() {
        var seg = new SegDisplay(6, 4, true);
        Assertions.assertEquals("12345M",seg.convertToDisplayNum(1_234_567).numberText());
        Assertions.assertEquals(".",seg.convertToDisplayNum(1_234_567).dotText());
    }

    @Test
    void testSegDisplay_6_4_with_0() {
        var seg = new SegDisplay(6, 4, true);
        Assertions.assertEquals(" 00000",seg.convertToDisplayNum(0).numberText());
        Assertions.assertEquals(" .",seg.convertToDisplayNum(0).dotText());
    }

    @Test
    void testSegDisplay_6_4_with_10() {
        var seg = new SegDisplay(6, 4, true);
        Assertions.assertEquals("100000",seg.convertToDisplayNum(10).numberText());
        Assertions.assertEquals(" .",seg.convertToDisplayNum(10).dotText());
    }

    @Test
    void testSegDisplay_6_4_with_100() {
        var seg = new SegDisplay(6, 4, true);
        Assertions.assertEquals("01000K",seg.convertToDisplayNum(100).numberText());
        Assertions.assertEquals(".",seg.convertToDisplayNum(100).dotText());
    }


    @Test
    void testSegDisplay_6_4_with_1_001() {
        var seg = new SegDisplay(6, 4, true);
        Assertions.assertEquals("10010K",seg.convertToDisplayNum(1_001).numberText());
        Assertions.assertEquals(".",seg.convertToDisplayNum(1_001).dotText());
    }

    @Test
    void testSegDisplay_6_4_with_9_223_372_036_854_775_807L() {
        var seg = new SegDisplay(6, 4, true);
        Assertions.assertEquals("92233E",seg.convertToDisplayNum(9_223_372_036_854_775_807L).numberText());
        Assertions.assertEquals(".",seg.convertToDisplayNum(9_223_372_036_854_775_807L).dotText());
    }













    @Test
    void testSegDisplayWithoutPrefix_4_2_with_1_234_567() {
        var seg = new SegDisplay(4, 2, false);
        Assertions.assertEquals("----",seg.convertToDisplayNum(1_234_567).numberText());
        Assertions.assertEquals("",seg.convertToDisplayNum(1_234_567).dotText());
    }

    @Test
    void testSegDisplayWithoutPrefix_4_2_with_0() {
        var seg = new SegDisplay(4, 2, false);
        Assertions.assertEquals(" 000",seg.convertToDisplayNum(0).numberText());
        Assertions.assertEquals(" .",seg.convertToDisplayNum(0).dotText());
    }

    @Test
    void testSegDisplayWithoutPrefix_4_2_with_10() {
        var seg = new SegDisplay(4, 2, false);
        Assertions.assertEquals("1000",seg.convertToDisplayNum(10).numberText());
        Assertions.assertEquals(" .",seg.convertToDisplayNum(10).dotText());
    }

    @Test
    void testSegDisplayWithoutPrefix_4_2_with_100() {
        var seg = new SegDisplay(4, 2, false);
        Assertions.assertEquals("----",seg.convertToDisplayNum(100).numberText());
        Assertions.assertEquals("",seg.convertToDisplayNum(100).dotText());
    }

    @Test
    void testSegDisplayWithoutPrefix_4_2_with_1_000() {
        var seg = new SegDisplay(4, 2, false);
        Assertions.assertEquals("----",seg.convertToDisplayNum(1_000).numberText());
        Assertions.assertEquals("",seg.convertToDisplayNum(1_000).dotText());
    }

    @Test
    void testSegDisplayWithoutPrefix_4_2_with_9_223_372_036_854_775_807L() {
        var seg = new SegDisplay(4, 2, false);
        Assertions.assertEquals("----",seg.convertToDisplayNum(9_223_372_036_854_775_807L).numberText());
        Assertions.assertEquals("",seg.convertToDisplayNum(9_223_372_036_854_775_807L).dotText());
    }

    @Test
    void testSegDisplayWithoutPrefix_6_4_with_1_234_567() {
        var seg = new SegDisplay(6, 4, false);
        Assertions.assertEquals("------",seg.convertToDisplayNum(1_234_567).numberText());
        Assertions.assertEquals("",seg.convertToDisplayNum(1_234_567).dotText());
    }

    @Test
    void testSegDisplayWithoutPrefix_6_4_with_0() {
        var seg = new SegDisplay(6, 4, false);
        Assertions.assertEquals(" 00000",seg.convertToDisplayNum(0).numberText());
        Assertions.assertEquals(" .",seg.convertToDisplayNum(0).dotText());
    }

    @Test
    void testSegDisplayWithoutPrefix_6_4_with_10() {
        var seg = new SegDisplay(6, 4, false);
        Assertions.assertEquals("100000",seg.convertToDisplayNum(10).numberText());
        Assertions.assertEquals(" .",seg.convertToDisplayNum(10).dotText());
    }

    @Test
    void testSegDisplayWithoutPrefix_6_4_with_100() {
        var seg = new SegDisplay(6, 4, false);
        Assertions.assertEquals("------",seg.convertToDisplayNum(100).numberText());
        Assertions.assertEquals("",seg.convertToDisplayNum(100).dotText());
    }


    @Test
    void testSegDisplayWithoutPrefix_6_4_with_1_001() {
        var seg = new SegDisplay(6, 4, false);
        Assertions.assertEquals("------",seg.convertToDisplayNum(1_001).numberText());
        Assertions.assertEquals("",seg.convertToDisplayNum(1_001).dotText());
    }

    @Test
    void testSegDisplayWithoutPrefix_6_4_with_9_223_372_036_854_775_807L() {
        var seg = new SegDisplay(6, 4, false);
        Assertions.assertEquals("------",seg.convertToDisplayNum(9_223_372_036_854_775_807L).numberText());
        Assertions.assertEquals("",seg.convertToDisplayNum(9_223_372_036_854_775_807L).dotText());
    }

    @Test
    void testSegDisplay_10_4_with_9_223_372_036_854_775_807L() {
        var seg = new SegDisplay(10, 4, true);
        Assertions.assertEquals(" 92233720P",seg.convertToDisplayNum(9_223_372_036_854_775_807L).numberText());
        Assertions.assertEquals("    .",seg.convertToDisplayNum(9_223_372_036_854_775_807L).dotText());
    }
}
