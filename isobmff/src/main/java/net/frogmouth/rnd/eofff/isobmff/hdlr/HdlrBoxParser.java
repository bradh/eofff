package net.frogmouth.rnd.eofff.isobmff.hdlr;

import java.nio.charset.StandardCharsets;
import net.frogmouth.rnd.eofff.isobmff.BaseBoxParser;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.BoxParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public class HdlrBoxParser extends BoxParser {

    public HdlrBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return new FourCC("hdlr");
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        int version = parseContext.getByte();
        if (!isSupportedVersion(version)) {
            // TODO: LOG
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        byte[] flags = new byte[3];
        parseContext.getBytes(flags);
        HdlrBox box = new HdlrBox(boxSize, boxName);
        box.setVersion(version);
        box.setFlags(flags);
        box.setPreDefined(parseContext.getInteger());
        box.setHandlerType(parseContext.readFourCC().toString());
        box.setReserved0(parseContext.getInteger());
        box.setReserved1(parseContext.getInteger());
        box.setReserved2(parseContext.getInteger());
        int nameLength = (int) ((initialOffset + boxSize) - parseContext.getCursorPosition());
        byte[] dst = new byte[nameLength];
        parseContext.getBytes(dst);
        String name = new String(dst, 0, dst.length - 1, StandardCharsets.UTF_8);
        box.setName(name);
        return box;
    }

    protected boolean isSupportedVersion(int version) {
        return version == 0x00;
    }

    private Box parseAsBaseBox(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        BaseBoxParser parser = new BaseBoxParser();
        return parser.parse(parseContext, initialOffset, boxSize, boxName);
    }
}
