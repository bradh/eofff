package net.frogmouth.rnd.eofff.isobmff;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import jdk.incubator.foreign.MemorySegment;
import jdk.incubator.foreign.ResourceScope;

public class FileParser extends AbstractParser {
    public List<Box> parse(Path testFile) throws IOException {
        List<Box> boxes = new ArrayList<>();
        MemorySegment segment =
                MemorySegment.mapFile(
                        testFile,
                        0,
                        Files.size(testFile),
                        FileChannel.MapMode.READ_ONLY,
                        ResourceScope.newImplicitScope());
        ByteBuffer byteBuffer = segment.asByteBuffer();
        while (byteBuffer.hasRemaining()) {
            long offset = byteBuffer.position();
            long boxSize = readInteger(byteBuffer);
            String boxName = readFourCC(byteBuffer);
            BoxParser parser = BoxFactoryManager.getParser(boxName);
            Box box = parser.parse(byteBuffer, offset, boxSize, boxName);
            boxes.add(box);
        }
        return boxes;
    }
}
