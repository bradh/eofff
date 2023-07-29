package net.frogmouth.rnd.eofff.uncompressed.itai;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class TAITimeStampPacket {

    public static final long BYTES = Long.BYTES + Byte.BYTES;

    public static final long TIME_STAMP_INVALID = 0xFFFFFFFFFFFFFFFFl;
    private long TAI_time_stamp = TIME_STAMP_INVALID;
    private byte status_bits = 0x00;

    public long getTAITimeStamp() {
        return TAI_time_stamp;
    }

    public void setTAITimeStamp(long TAI_time_stamp) {
        this.TAI_time_stamp = TAI_time_stamp;
    }

    public byte getStatusBits() {
        return status_bits;
    }

    public void setStatusBits(byte status_bits) {
        this.status_bits = status_bits;
    }

    @Override
    public String toString() {
        // TODO: make this nicer, maybe use a string builder
        return "TAITimeStampPacket{"
                + "TAI_time_stamp="
                + TAI_time_stamp
                + ", status_bits="
                + status_bits
                + '}';
    }

    public void writeTo(OutputStreamWriter stream) throws IOException {
        stream.writeLong(TAI_time_stamp);
        stream.writeByte(status_bits);
    }
}
