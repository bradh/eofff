package net.frogmouth.rnd.eofff.nalvideo;

import java.nio.charset.StandardCharsets;
import net.frogmouth.rnd.eofff.isobmff.BaseBoxParser;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public class HEVCSampleEntryParser extends BaseBoxParser {
    public HEVCSampleEntryParser() {}

    @Override
    public FourCC getFourCC() {
        return new FourCC("hvc1");
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        HEVCSampleEntry box = new HEVCSampleEntry(boxName);
        parseContext.getBytes(6);
        int data_reference_index = parseContext.readUnsignedInt16();
        box.setDataReferenceIndex(data_reference_index);
        parseContext.getBytes(Short.BYTES);
        parseContext.getBytes(Short.BYTES);
        parseContext.getBytes(3 * Integer.BYTES);
        box.setWidth(parseContext.readUnsignedInt16());
        box.setHeight(parseContext.readUnsignedInt16());
        box.setHorizontalResolution(parseContext.readUnsignedInt32());
        box.setVerticalResolution(parseContext.readUnsignedInt32());
        parseContext.getBytes(Integer.BYTES);
        box.setFrameCount(parseContext.readUnsignedInt16());
        int compressorNameLength = parseContext.readByte();
        byte[] compressorNameBytes = parseContext.getBytes(compressorNameLength);
        box.setCompressorName(new String(compressorNameBytes, StandardCharsets.US_ASCII));
        parseContext.getBytes(32 - compressorNameLength - 1);
        int depth = parseContext.readUnsignedInt16();
        box.setDepth(depth);
        parseContext.getBytes(Short.BYTES);
        box.addNestedBoxes(parseContext.parseNestedBoxes(initialOffset + boxSize));
        return box;
    }
}
