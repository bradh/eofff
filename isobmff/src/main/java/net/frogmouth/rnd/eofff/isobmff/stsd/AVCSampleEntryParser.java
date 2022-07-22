package net.frogmouth.rnd.eofff.isobmff.stsd;

import java.nio.charset.StandardCharsets;
import net.frogmouth.rnd.eofff.isobmff.BaseBoxParser;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public class AVCSampleEntryParser extends BaseBoxParser {
    public AVCSampleEntryParser() {}

    @Override
    public FourCC getFourCC() {
        return new FourCC("avc1");
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        AVCSampleEntry box = new AVCSampleEntry(boxSize, boxName);
        parseContext.skipBytes(6);
        int data_reference_index = parseContext.readUnsignedInt16();
        box.setDataReferenceIndex(data_reference_index);
        parseContext.skipBytes(Short.BYTES);
        parseContext.skipBytes(Short.BYTES);
        parseContext.skipBytes(3 * Integer.BYTES);
        box.setWidth(parseContext.readUnsignedInt16());
        box.setHeight(parseContext.readUnsignedInt16());
        box.setHorizontalResolution(parseContext.readUnsignedInt32());
        box.setVerticalResolution(parseContext.readUnsignedInt32());
        parseContext.skipBytes(Integer.BYTES);
        box.setFrameCount(parseContext.readUnsignedInt16());
        int compressorNameLength = parseContext.readByte();
        byte[] compressorNameBytes = parseContext.getBytes(compressorNameLength);
        box.setCompressorName(new String(compressorNameBytes, StandardCharsets.US_ASCII));
        parseContext.skipBytes(32 - compressorNameLength - 1);
        int depth = parseContext.readUnsignedInt16();
        box.setDepth(depth);
        parseContext.skipBytes(Short.BYTES);
        box.addNestedBoxes(parseContext.parseNestedBoxes(initialOffset + boxSize));
        return box;
    }
}
