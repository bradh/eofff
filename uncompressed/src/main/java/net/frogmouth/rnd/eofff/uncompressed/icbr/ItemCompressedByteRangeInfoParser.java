package net.frogmouth.rnd.eofff.uncompressed.icbr;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.iprp.AbstractItemProperty;
import net.frogmouth.rnd.eofff.isobmff.iprp.PropertyParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.iprp.PropertyParser.class)
public class ItemCompressedByteRangeInfoParser implements PropertyParser {
    private static final Logger LOG =
            LoggerFactory.getLogger(ItemCompressedByteRangeInfoParser.class);

    public ItemCompressedByteRangeInfoParser() {}

    @Override
    public FourCC getFourCC() {
        return ItemCompressedByteRangeInfo.ICBR_ATOM;
    }

    @Override
    public AbstractItemProperty parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        ItemCompressedByteRangeInfo box = new ItemCompressedByteRangeInfo();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return null;
        }
        box.setFlags(parseFlags(parseContext));
        long numRanges = parseContext.readUnsignedInt32();
        for (int r = 0; r < numRanges; r++) {
            if (version == 1) {
                box.addRange(
                        new ByteRange(
                                parseContext.readUnsignedInt64(),
                                parseContext.readUnsignedInt64()));
            } else if (version == 0) {
                box.addRange(
                        new ByteRange(
                                parseContext.readUnsignedInt32(),
                                parseContext.readUnsignedInt32()));
            }
        }
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return (version == 0x00);
    }

    // TODO: move this into a base class
    protected int parseFlags(ParseContext parseContext) {
        byte[] flags = new byte[3];
        parseContext.readBytes(flags);
        return ((flags[0] & 0xFF) << 16) | ((flags[1] & 0xFF) << 8) | (flags[2] & 0xFF);
    }
}
