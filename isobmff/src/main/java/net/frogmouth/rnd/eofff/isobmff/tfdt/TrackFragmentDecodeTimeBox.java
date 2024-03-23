package net.frogmouth.rnd.eofff.isobmff.tfdt;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class TrackFragmentDecodeTimeBox extends FullBox {
    public static final FourCC TFDT_ATOM = new FourCC("tfdt");
    private long baseMediaDecodeTime;

    public TrackFragmentDecodeTimeBox() {
        super(TFDT_ATOM);
    }

    @Override
    public String getFullName() {
        return "Track Fragment Decode Time Box";
    }

    public long getBaseMediaDecodeTime() {
        return baseMediaDecodeTime;
    }

    public void setBaseMediaDecodeTime(long baseMediaDecodeTime) {
        this.baseMediaDecodeTime = baseMediaDecodeTime;
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("baseMediaDecodeTime=");
        sb.append(getBaseMediaDecodeTime());
        return sb.toString();
    }

    @Override
    public void writeTo(OutputStreamWriter writer) throws IOException {
        this.writeBoxHeader(writer);
        if (getVersion() == 1) {
            writer.writeLong(baseMediaDecodeTime);
        } else {
            writer.writeUnsignedInt32(baseMediaDecodeTime);
        }
    }

    @Override
    public long getBodySize() {
        if (getVersion() == 1) {
            return Long.BYTES;
        } else {
            return Integer.BYTES;
        }
    }
}
