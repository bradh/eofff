package net.frogmouth.rnd.eofff.isobmff;

import java.io.IOException;
import java.util.List;
import jdk.incubator.foreign.MemorySegment;

public class ByteArrayParser extends AbstractParser {
    public List<Box> parse(byte[] bytes) throws IOException {
        MemorySegment segment = MemorySegment.ofArray(bytes);
        return parseMemorySegment(segment);
    }
}
