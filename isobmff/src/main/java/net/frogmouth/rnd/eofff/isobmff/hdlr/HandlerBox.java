package net.frogmouth.rnd.eofff.isobmff.hdlr;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Handler Box.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.4.3.
 */
public class HandlerBox extends FullBox {

    public static final FourCC HDLR_ATOM = new FourCC("hdlr");

    private int preDefined;
    private String handlerType;
    private int reserved0;
    private int reserved1;
    private int reserved2;
    private String name;

    public HandlerBox() {
        super(HDLR_ATOM);
    }

    @Override
    public String getFullName() {
        return "HandlerBox";
    }

    // @Override
    public long getSize() {
        long size =
                Integer.BYTES
                        + FourCC.BYTES
                        + 1
                        + 3
                        + Integer.BYTES
                        + FourCC.BYTES
                        + 3 * Integer.BYTES
                        + name.getBytes(StandardCharsets.US_ASCII).length
                        + 1;
        return size;
    }

    @Override
    public long getBodySize() {
        long size =
                Integer.BYTES
                        + FourCC.BYTES
                        + 3 * Integer.BYTES
                        + name.getBytes(StandardCharsets.US_ASCII).length
                        + 1;
        return size;
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
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writeInt(this.preDefined);
        stream.write(this.handlerType.getBytes(StandardCharsets.US_ASCII));
        stream.writeInt(this.reserved0);
        stream.writeInt(this.reserved1);
        stream.writeInt(this.reserved2);
        stream.writeNullTerminatedString(name);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("handler_type: '");
        sb.append(getHandlerType());
        sb.append("', name: ");
        sb.append(name);
        return sb.toString();
    }
}
