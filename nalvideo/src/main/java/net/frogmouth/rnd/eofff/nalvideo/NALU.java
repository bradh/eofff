package net.frogmouth.rnd.eofff.nalvideo;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class NALU {

    private byte[] nalUnit;

    public byte[] getNalUnit() {
        return nalUnit;
    }

    public void setNalUnit(byte[] nalUnit) {
        this.nalUnit = nalUnit;
    }

    public int getNumBytes() {
        return nalUnit.length + Short.BYTES;
    }

    public void writeTo(OutputStreamWriter writer) throws IOException {
        writer.writeUnsignedInt16(nalUnit.length);
        writer.write(nalUnit);
    }

    void addToStringBuilder(int i, StringBuilder sb) {
        sb.append("NALU: ");
        sb.append(nalUnit.length);
        sb.append(" bytes");
    }
}
