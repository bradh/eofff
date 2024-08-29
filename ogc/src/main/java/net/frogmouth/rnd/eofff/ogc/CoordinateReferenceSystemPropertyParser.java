package net.frogmouth.rnd.eofff.ogc;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.iprp.AbstractItemProperty;
import net.frogmouth.rnd.eofff.isobmff.iprp.PropertyParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.iprp.PropertyParser.class)
public class CoordinateReferenceSystemPropertyParser implements PropertyParser {
    private static final Logger LOG =
            LoggerFactory.getLogger(CoordinateReferenceSystemPropertyParser.class);

    public CoordinateReferenceSystemPropertyParser() {}

    @Override
    public FourCC getFourCC() {
        return CoordinateReferenceSystemProperty.MCRS_ATOM;
    }

    @Override
    public AbstractItemProperty parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        CoordinateReferenceSystemProperty box = new CoordinateReferenceSystemProperty();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return null;
        }
        box.setFlags(parseFlags(parseContext));
        box.setCrsEncoding(parseContext.readFourCC());
        box.setCrs(parseContext.readNullDelimitedString(boxSize));
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return (version == 0x00);
    }

    protected int parseFlags(ParseContext parseContext) {
        byte[] flags = new byte[3];
        parseContext.readBytes(flags);
        return ((flags[0] & 0xFF) << 16) | ((flags[1] & 0xFF) << 8) | (flags[2] & 0xFF);
    }
}
