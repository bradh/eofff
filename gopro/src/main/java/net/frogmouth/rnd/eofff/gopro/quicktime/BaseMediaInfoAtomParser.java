package net.frogmouth.rnd.eofff.gopro.quicktime;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class BaseMediaInfoAtomParser extends FullBoxParser {
    private static final Logger LOG = LoggerFactory.getLogger(BaseMediaInfoAtomParser.class);

    public BaseMediaInfoAtomParser() {}

    @Override
    public FourCC getFourCC() {
        return BaseMediaInfoAtom.GMIN_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        BaseMediaInfoAtom box = new BaseMediaInfoAtom();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        box.setGraphicsmode(parseContext.readUnsignedInt16());
        int red = parseContext.readUnsignedInt16();
        int green = parseContext.readUnsignedInt16();
        int blue = parseContext.readUnsignedInt16();
        box.setOpcolor(new int[] {red, green, blue});
        box.setBalance(parseContext.readInt16());
        parseContext.skipBytes(2);
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return (version == 0x00);
    }
}
