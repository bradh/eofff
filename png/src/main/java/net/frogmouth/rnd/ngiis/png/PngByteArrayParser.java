package net.frogmouth.rnd.ngiis.png;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.foreign.MemorySegment;
import java.util.List;

public class PngByteArrayParser extends AbstractPngParser {
    public List<PngChunk> parse(byte[] bytes) throws IOException {
        MemorySegment segment = MemorySegment.ofArray(bytes);
        return parseMemorySegment(segment);
    }

    public BufferedImage render(byte[] bytes) throws IOException {
        MemorySegment segment = MemorySegment.ofArray(bytes);
        return render(segment);
    }
}
