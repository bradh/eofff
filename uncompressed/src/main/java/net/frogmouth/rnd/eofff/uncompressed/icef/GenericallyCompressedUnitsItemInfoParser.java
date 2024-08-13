package net.frogmouth.rnd.eofff.uncompressed.icef;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.iprp.AbstractItemProperty;
import net.frogmouth.rnd.eofff.isobmff.iprp.PropertyParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.iprp.PropertyParser.class)
public class GenericallyCompressedUnitsItemInfoParser implements PropertyParser {
    private static final Logger LOG =
            LoggerFactory.getLogger(GenericallyCompressedUnitsItemInfoParser.class);

    public GenericallyCompressedUnitsItemInfoParser() {}

    @Override
    public FourCC getFourCC() {
        return GenericallyCompressedUnitsItemInfo.ICEF_ATOM;
    }

    @Override
    public AbstractItemProperty parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        GenericallyCompressedUnitsItemInfo box = new GenericallyCompressedUnitsItemInfo();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return null;
        }
        box.setFlags(parseFlags(parseContext));
        int codes = parseContext.readUnsignedInt8();
        int unit_offset_code = ((codes & 0b11100000) >> 5);
        int unit_size_code = ((codes & 0b00011100) >> 2);
        long implied_offset = 0;
        long numRanges = parseContext.readUnsignedInt32();
        for (int r = 0; r < numRanges; r++) {
            long offset = 0;
            long size = 0;
            switch (unit_offset_code) {
                case 0:
                    offset = implied_offset;
                    break;
                case 1:
                    offset = parseContext.readUnsignedInt16();
                    break;
                case 2:
                    offset = parseContext.readUnsignedInt24();
                    break;
                case 3:
                    offset = parseContext.readUnsignedInt32();
                    break;
                case 4:
                    offset = parseContext.readUnsignedInt64();
                    break;
                default:
                    LOG.warn("Got unsupported offset code: " + unit_offset_code);
                    return null;
            }
            switch (unit_size_code) {
                case 0:
                    size = parseContext.readUnsignedInt8();
                    break;
                case 1:
                    size = parseContext.readUnsignedInt16();
                    break;
                case 2:
                    size = parseContext.readUnsignedInt(24);
                    break;
                case 3:
                    size = parseContext.readUnsignedInt32();
                    break;
                case 4:
                    size = parseContext.readUnsignedInt64();
                    break;
                default:
                    LOG.warn("Got unsupported size code: " + unit_size_code);
                    return null;
            }
            CompressedUnitInfo unitInfo = new CompressedUnitInfo(offset, size);
            box.addCompressedUnitInfo(unitInfo);
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
