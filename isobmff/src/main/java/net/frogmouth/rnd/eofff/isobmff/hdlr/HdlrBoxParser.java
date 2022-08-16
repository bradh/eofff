package net.frogmouth.rnd.eofff.isobmff.hdlr;

import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HdlrBoxParser extends FullBoxParser {
    private static final Logger LOG = LoggerFactory.getLogger(HdlrBoxParser.class);

    public HdlrBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return new FourCC("hdlr");
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        HdlrBox box = new HdlrBox(boxName);
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        box.setPreDefined(parseContext.readInt32());
        box.setHandlerType(parseContext.readFourCC().toString());
        box.setReserved0(parseContext.readInt32());
        box.setReserved1(parseContext.readInt32());
        box.setReserved2(parseContext.readInt32());
        box.setName(
                parseContext.readNullDelimitedString(
                        (initialOffset + boxSize) - parseContext.getCursorPosition()));
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return version == 0x00;
    }
}
