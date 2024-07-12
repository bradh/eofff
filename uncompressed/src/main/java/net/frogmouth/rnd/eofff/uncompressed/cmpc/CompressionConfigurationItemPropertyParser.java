package net.frogmouth.rnd.eofff.uncompressed.cmpc;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.iprp.AbstractItemProperty;
import net.frogmouth.rnd.eofff.isobmff.iprp.PropertyParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.iprp.PropertyParser.class)
public class CompressionConfigurationItemPropertyParser implements PropertyParser {
    private static final Logger LOG =
            LoggerFactory.getLogger(CompressionConfigurationItemPropertyParser.class);

    public CompressionConfigurationItemPropertyParser() {}

    @Override
    public FourCC getFourCC() {
        return CompressionConfigurationItemProperty.CMPC_ATOM;
    }

    @Override
    public AbstractItemProperty parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        CompressionConfigurationItemProperty box = new CompressionConfigurationItemProperty();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return null;
        }
        box.setFlags(parseFlags(parseContext));
        box.setCompressionType(parseContext.readFourCC());
        int v = parseContext.readUnsignedInt8();
        box.setMustDecompressIndividualEntities((v & 0x80) == 0x80);
        box.setCompressedRangeType(CompressionRangeType.getTypeForValue((v & 0x7f)));
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
