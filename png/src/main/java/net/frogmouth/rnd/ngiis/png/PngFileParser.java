package net.frogmouth.rnd.ngiis.png;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class PngFileParser extends AbstractPngParser {
    public List<PngChunk> parse(Path testFile) throws IOException {
        MemorySegment segment = getMemorySegment(testFile);
        return parseMemorySegment(segment);
    }

    public BufferedImage render(Path testFile) throws IOException {
        MemorySegment segment = getMemorySegment(testFile);
        return render(segment);
    }

    protected MemorySegment getMemorySegment(Path testFile) throws IOException {
        FileChannel channel = FileChannel.open(testFile, StandardOpenOption.READ);
        MemorySegment segment =
                channel.map(FileChannel.MapMode.READ_ONLY, 0, Files.size(testFile), Arena.ofAuto());
        return segment;
    }
}
