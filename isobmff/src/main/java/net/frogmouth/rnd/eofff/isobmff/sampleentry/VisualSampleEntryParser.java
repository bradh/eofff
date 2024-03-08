package net.frogmouth.rnd.eofff.isobmff.sampleentry;

import java.nio.charset.StandardCharsets;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public class VisualSampleEntryParser extends BaseSampleEntryParser {

    protected void parseVisualSampleEntry(ParseContext parseContext, VisualSampleEntry box) {
        this.parseBaseSampleEntry(parseContext, box);
        parseContext.skipBytes(Short.BYTES); // pre_defined
        parseContext.skipBytes(Short.BYTES); // reserved
        parseContext.skipBytes(3 * Integer.BYTES); // pre_defined
        box.setWidth(parseContext.readUnsignedInt16());
        box.setHeight(parseContext.readUnsignedInt16());
        box.setHorizontalResolution(parseContext.readUnsignedInt32());
        box.setVerticalResolution(parseContext.readUnsignedInt32());
        parseContext.skipBytes(Integer.BYTES); // reserved
        box.setFrameCount(parseContext.readUnsignedInt16());
        int compressorNameLength = parseContext.readByte();
        byte[] compressorNameBytes = parseContext.getBytes(compressorNameLength);
        box.setCompressorName(new String(compressorNameBytes, StandardCharsets.US_ASCII));
        parseContext.skipBytes(32 - compressorNameLength - 1);
        box.setDepth(parseContext.readUnsignedInt16());
        parseContext.skipBytes(Short.BYTES); // pre_defined
    }
}
