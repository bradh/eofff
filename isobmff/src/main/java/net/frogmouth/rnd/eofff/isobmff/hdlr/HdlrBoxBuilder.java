package net.frogmouth.rnd.eofff.isobmff.hdlr;

import java.nio.charset.StandardCharsets;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class HdlrBoxBuilder {

    private int version;
    private int flags;
    private String handlerType;
    private String name = "";

    public HdlrBoxBuilder() {}

    public HdlrBoxBuilder withVersion(int version) {
        this.version = version;
        return this;
    }

    public HdlrBoxBuilder withFlags(int flags) {
        this.flags = flags;
        return this;
    }

    public HdlrBoxBuilder withHandlerType(String handlerType) {
        this.handlerType = handlerType;
        return this;
    }

    public HdlrBoxBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public HdlrBox build() {
        int size =
                Integer.BYTES
                        + FourCC.BYTES
                        + 1
                        + 3
                        + Integer.BYTES
                        + FourCC.BYTES
                        + 3 * Integer.BYTES
                        + name.getBytes(StandardCharsets.US_ASCII).length
                        + 1;

        HdlrBox box = new HdlrBox(size, new FourCC("hdlr"));
        box.setVersion(version);
        box.setFlags(flags);
        box.setPreDefined(0);
        box.setHandlerType(handlerType);
        box.setReserved0(0);
        box.setReserved1(0);
        box.setReserved2(0);
        box.setName(name);
        return box;
    }
}
