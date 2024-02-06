package net.frogmouth.rnd.eofff.isobmff.stz2;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.stsz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class CompactSampleSizeBoxParser extends FullBoxParser {
    private static final Logger LOG = LoggerFactory.getLogger(CompactSampleSizeBoxParser.class);

    public CompactSampleSizeBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return CompactSampleSizeBox.STZ2_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        CompactSampleSizeBox box = new CompactSampleSizeBox();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        parseContext.skipBytes(3);
        box.setFieldSize(parseContext.readUnsignedInt8());
        long sample_count = parseContext.readUnsignedInt32();
        for (int i = 0; i < sample_count; i++) {
            if (box.getFieldSize() == 16) {
                box.addEntry(parseContext.readUnsignedInt16());
            } else if (box.getFieldSize() == 8) {
                box.addEntry(parseContext.readUnsignedInt8());
            } else if (box.getFieldSize() == 4) {
                int v = parseContext.readUnsignedInt8();
                int entry = (v >> 4) & 0x0F;
                int nextEntry = v & 0xF;
                box.addEntry(entry);
                box.addEntry(nextEntry);
                i++;
            }
        }
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return (version == 0x00);
    }
}
