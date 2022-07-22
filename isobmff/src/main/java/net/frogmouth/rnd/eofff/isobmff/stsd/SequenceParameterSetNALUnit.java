package net.frogmouth.rnd.eofff.isobmff.stsd;

import java.io.IOException;
import java.io.OutputStream;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;

public class SequenceParameterSetNALUnit {

    private byte[] naluBytes;

    public byte[] getNaluBytes() {
        return naluBytes;
    }

    public void setNaluBytes(byte[] naluBytes) {
        this.naluBytes = naluBytes;
    }

    void writeTo(OutputStream stream) throws IOException {
        stream.write(BaseBox.shortToBytes((short) naluBytes.length));
        stream.write(naluBytes);
    }
}
