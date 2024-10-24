package net.frogmouth.rnd.ngiis.av1;

import static org.testng.Assert.*;

import java.io.IOException;
import java.nio.file.Path;
import org.testng.annotations.Test;

public class OBUParserTest {

    public OBUParserTest() {}

    @Test
    public void obuStream() throws IOException {
        OBUParser parser = new OBUParser();
        // TODO: return something
        parser.parse(Path.of("/home/bradh/eofff/av1/src/test/resources/mini.dat"));
    }
}
