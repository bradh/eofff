package net.frogmouth.rnd.ngiis.av1;

import java.io.IOException;
import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class OBUParser {

    protected List<OBU> parseMemorySegment(MemorySegment segment) throws IOException {
        OBUParseContext parser = new OBUParseContext(segment);
        List<OBU> obus = new ArrayList<>();
        while (parser.hasRemaining()) {
            OBU obu = parser.readOBU();
            obus.add(obu);
        }
        if (parser.hasRemaining()) {
            System.out.println(
                    "parsed only "
                            + parser.getCursorPosition()
                            + " of "
                            + segment.byteSize()
                            + " bytes");
        }
        return obus;
    }

    public List<OBU> parse(Path testFile) throws IOException {
        MemorySegment segment = getMemorySegment(testFile);
        return parseMemorySegment(segment);
    }

    protected MemorySegment getMemorySegment(Path testFile) throws IOException {
        FileChannel channel = FileChannel.open(testFile, StandardOpenOption.READ);
        MemorySegment segment =
                channel.map(FileChannel.MapMode.READ_ONLY, 0, Files.size(testFile), Arena.ofAuto());
        return segment;
    }
}
