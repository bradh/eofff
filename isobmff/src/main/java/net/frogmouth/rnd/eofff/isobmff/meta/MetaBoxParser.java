package net.frogmouth.rnd.eofff.isobmff.meta;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.BoxFactoryManager;
import net.frogmouth.rnd.eofff.isobmff.BoxParser;

public class MetaBoxParser extends BoxParser {

    public MetaBoxParser() {}

    @Override
    public String getFourCC() {
        return "meta";
    }

    @Override
    public Box parse(ByteBuffer byteBuffer, long initialOffset, long boxSize, String boxName) {
        MetaBox box = new MetaBox(boxSize, boxName);
        int version = byteBuffer.get();
        if (version != 0) {
            byteBuffer.position((int) (initialOffset + boxSize));
            return new BaseBox(boxSize, boxName);
        }
        byte[] flags = new byte[3];
        byteBuffer.get(flags);
        box.setVersion(version);
        box.setFlags(flags);
        box.addNestedBoxes(parseNestedBoxes(byteBuffer, (int) (initialOffset + boxSize)));
        return box;
    }

    private List<Box> parseNestedBoxes(ByteBuffer byteBuffer, int limit) {
        List<Box> nestedBoxes = new ArrayList<>();
        while (byteBuffer.position() < limit) {
            nestedBoxes.add(parseBox(byteBuffer));
        }
        return nestedBoxes;
    }

    private Box parseBox(ByteBuffer byteBuffer) {
        long offset = byteBuffer.position();
        long boxSize = byteBuffer.getInt();
        byte[] labelBytes = new byte[4];
        byteBuffer.get(labelBytes);
        String boxName = new String(labelBytes, StandardCharsets.US_ASCII);
        BoxParser parser = BoxFactoryManager.getParser(boxName);
        Box box = parser.parse(byteBuffer, offset, boxSize, boxName);
        return box;
    }
}
