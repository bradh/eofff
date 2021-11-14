package net.frogmouth.rnd.eofff.isobmff;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import jdk.incubator.foreign.MemorySegment;
import jdk.incubator.foreign.ResourceScope;

public class FileParser {
    public List<Box> parse(Path testFile) throws IOException {
        List<Box> boxes = new ArrayList<>();
        MemorySegment segment =
                MemorySegment.mapFile(
                        testFile,
                        0,
                        Files.size(testFile),
                        FileChannel.MapMode.READ_ONLY,
                        ResourceScope.newImplicitScope());
        ParseContext parseContext = new ParseContext(segment);
        while (parseContext.hasRemaining()) {
            long offset = parseContext.getCursorPosition();
            long boxSize = parseContext.readUnsignedInt32();
            FourCC boxName = parseContext.readFourCC();
            BoxParser parser = BoxFactoryManager.getParser(boxName);
            Box box = parser.parse(parseContext, offset, boxSize, boxName);
            parseContext.setCursorPosition(offset + boxSize);
            boxes.add(box);
        }
        return boxes;
    }
}
