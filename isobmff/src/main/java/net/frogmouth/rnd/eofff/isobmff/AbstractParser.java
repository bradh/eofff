package net.frogmouth.rnd.eofff.isobmff;

import java.lang.foreign.MemorySegment;
import java.util.ArrayList;
import java.util.List;

public class AbstractParser {

    protected List<Box> parseMemorySegment(MemorySegment segment)
            throws UnsupportedOperationException {
        ParseContext parseContext = new ParseContext(segment);
        List<Box> boxes = new ArrayList<>();
        while (parseContext.hasRemaining()) {
            long offset = parseContext.getCursorPosition();
            long boxSize = parseContext.readUnsignedInt32();
            FourCC boxName = parseContext.readFourCC();
            if (boxSize == 1) {
                boxSize = parseContext.readUnsignedInt64();
            } else if (boxSize == 0) {
                throw new UnsupportedOperationException("We don't do that yet");
            }
            BoxParser parser = BoxFactoryManager.getParser(boxName);
            Box box = parser.parse(parseContext, offset, boxSize, boxName);
            parseContext.setCursorPosition(offset + boxSize);
            boxes.add(box);
        }
        return boxes;
    }
}
