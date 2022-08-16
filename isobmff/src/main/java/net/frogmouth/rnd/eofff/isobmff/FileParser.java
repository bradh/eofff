package net.frogmouth.rnd.eofff.isobmff;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import jdk.incubator.foreign.MemorySegment;
import jdk.incubator.foreign.ResourceScope;

public class FileParser extends AbstractParser {
    public List<Box> parse(Path testFile) throws IOException {
        MemorySegment segment =
                MemorySegment.mapFile(
                        testFile,
                        0,
                        Files.size(testFile),
                        FileChannel.MapMode.READ_ONLY,
                        ResourceScope.newImplicitScope());
        return parseMemorySegment(segment);
    }
}
