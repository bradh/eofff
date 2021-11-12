package net.frogmouth.rnd.eofff.isobmff.hdlr;

import java.nio.charset.StandardCharsets;
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
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, String boxName) {
        HdlrBox box = new HdlrBox(boxSize, boxName);
        int version = parseContext.getByte();
        // TODO: check supported versions
        // if (version != 0) {
        //    byteBuffer.position((int) (initialOffset + boxSize));
        //    return new BaseBox(boxSize, boxName);
        // }
        byte[] flags = new byte[3];
        parseContext.getBytes(flags);
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
}
