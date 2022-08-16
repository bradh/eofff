package net.frogmouth.rnd.eofff.nalvideo;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class SequenceParameterSetNALUnit {

    private byte[] naluBytes;

    public byte[] getNaluBytes() {
        return naluBytes;
    }

    public void setNaluBytes(byte[] naluBytes) {
        this.naluBytes = naluBytes;
    }

    public long getSize() {
        return Short.BYTES + naluBytes.length;
    }

    void writeTo(OutputStreamWriter stream) throws IOException {
        stream.writeShort((short) naluBytes.length);
        stream.write(naluBytes);
    }
}
