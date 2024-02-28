package net.frogmouth.rnd.eofff.isobmff.hinf;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Hint statistics box - payload identifier (payt).
 *
 * <p>See ISO/IEC 14496-12:2022 Section 9.1.5
 */
public class HintPayloadID extends BaseBox {
    public static final FourCC PAYT_ATOM = new FourCC("payt");
    private long payloadID;
    private String rtpmap;

    public HintPayloadID() {
        super(PAYT_ATOM);
    }

    @Override
    public String getFullName() {
        return "PayloadID";
    }

    public long getPayloadID() {
        return payloadID;
    }

    public void setPayloadID(long payloadID) {
        this.payloadID = payloadID;
    }

    public String getRtpmap() {
        return rtpmap;
    }

    public void setRtpmap(String rtpmap) {
        this.rtpmap = rtpmap;
    }

    @Override
    public long getBodySize() {
        return Integer.BYTES + Byte.BYTES + rtpmap.getBytes(StandardCharsets.UTF_8).length;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writeUnsignedInt32(payloadID);
        byte[] bytes = rtpmap.getBytes(StandardCharsets.UTF_8);
        stream.writeUnsignedInt8(bytes.length);
        stream.write(bytes);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("payloadID=");
        sb.append(payloadID);
        sb.append(", rtpmap_string=");
        sb.append(rtpmap);
        return sb.toString();
    }
}
