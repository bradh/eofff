package net.frogmouth.rnd.eofff.isobmff;

import java.io.IOException;
import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class FileParser extends AbstractParser {
    public List<Box> parse(Path testFile) throws IOException {
        FileChannel channel = FileChannel.open(testFile, StandardOpenOption.READ);
        MemorySegment segment =
                channel.map(FileChannel.MapMode.READ_ONLY, 0, Files.size(testFile), Arena.ofAuto());
        return parseMemorySegment(segment);
    }
}
