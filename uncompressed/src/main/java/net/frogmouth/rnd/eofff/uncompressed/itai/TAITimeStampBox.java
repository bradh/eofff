package net.frogmouth.rnd.eofff.uncompressed.itai;

import java.io.IOException;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemFullProperty;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * TAI Time Stamp.
 *
 * <p>This is currently Technology under Consideration for 23001-17.
 */
public class TAITimeStampBox extends ItemFullProperty {

    public static final FourCC ITAI_ATOM = new FourCC("itai");

    private TAITimeStampPacket time_stamp_packet = new TAITimeStampPacket();

    public TAITimeStampBox() {
        super(ITAI_ATOM);
    }

    @Override
    public String getFullName() {
        return "TAITimeStampBox";
    }

    @Override
    public long getBodySize() {
        return TAITimeStampPacket.BYTES;
    }

    public TAITimeStampPacket getTimeStampPacket() {
        return time_stamp_packet;
    }

    public void setTimeStampPacket(TAITimeStampPacket time_stamp_packet) {
        this.time_stamp_packet = time_stamp_packet;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        time_stamp_packet.writeTo(stream);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        sb.append("': TAI_time_stamp: ");
        if (time_stamp_packet == null) {
            sb.append(" unknown");
        } else {
            if (time_stamp_packet.getTAITimeStamp() == TAITimeStampPacket.TIME_STAMP_INVALID) {
                sb.append("invalid");
            } else {
                sb.append(time_stamp_packet.getTAITimeStamp());
            }
            sb.append(", status_bits: ");
            sb.append(String.format("0x%02x", time_stamp_packet.getStatusBits()));
        }
        return sb.toString();
    }
}
