package net.frogmouth.rnd.eofff.isobmff.hdlr;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;

public class HdlrBox extends FullBox {
    private int preDefined;
    private String handlerType;
    private int reserved0;
    private int reserved1;
    private int reserved2;
    private String name;

    public HdlrBox(long size, FourCC name) {
        super(size, name);
    }

    @Override
    public String getFullName() {
        return "HandlerBox";
    }

    public int getPreDefined() {
        return preDefined;
    }

    public void setPreDefined(int preDefined) {
        this.preDefined = preDefined;
    }

    public String getHandlerType() {
        return handlerType;
    }

    public void setHandlerType(String handlerType) {
        this.handlerType = handlerType;
    }

    public int getReserved0() {
        return reserved0;
    }

    public void setReserved0(int reserved0) {
        this.reserved0 = reserved0;
    }

    public int getReserved1() {
        return reserved1;
    }

    public void setReserved1(int reserved1) {
        this.reserved1 = reserved1;
    }

    public int getReserved2() {
        return reserved2;
    }

    public void setReserved2(int reserved2) {
        this.reserved2 = reserved2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void writeTo(OutputStream stream) throws IOException {
        stream.write(this.getSizeAsBytes());
        stream.write(getFourCC().toBytes());
        stream.write(getVersionAndFlagsAsBytes());
        stream.write(intToBytes(this.preDefined));
        stream.write(this.handlerType.getBytes(StandardCharsets.US_ASCII));
        stream.write(intToBytes(this.reserved0));
        stream.write(intToBytes(this.reserved1));
        stream.write(intToBytes(this.reserved2));
        stream.write(name.getBytes(StandardCharsets.US_ASCII));
        stream.write(0); // NULL terminator
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        sb.append("': '");
        sb.append(getHandlerType());
        sb.append("'");
        return sb.toString();
    }
}
