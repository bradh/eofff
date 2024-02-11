package net.frogmouth.rnd.eofff.nalvideo;

import com.google.auto.service.AutoService;
import java.nio.charset.StandardCharsets;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntry;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntryParser;

@AutoService(net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntryParser.class)
public class HEVCSampleEntryParser implements SampleEntryParser {
    public HEVCSampleEntryParser() {}

    @Override
    public FourCC getFourCC() {
        return HEVCSampleEntry.HVC1_ATOM;
    }

    // TODO: we need to share this
    @Override
    public SampleEntry parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        HEVCSampleEntry box = new HEVCSampleEntry();
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
