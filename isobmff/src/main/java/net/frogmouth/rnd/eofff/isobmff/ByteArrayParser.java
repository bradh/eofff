package net.frogmouth.rnd.eofff.isobmff;

import java.io.IOException;
import java.lang.foreign.MemorySegment;
import java.util.List;

public class ByteArrayParser extends AbstractParser {
    public List<Box> parse(byte[] bytes) throws IOException {
        MemorySegment segment = MemorySegment.ofArray(bytes);
        return parseMemorySegment(segment);
    }
}
