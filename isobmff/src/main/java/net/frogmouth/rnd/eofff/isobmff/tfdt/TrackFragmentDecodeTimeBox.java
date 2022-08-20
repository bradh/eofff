package net.frogmouth.rnd.eofff.isobmff.tfdt;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;

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
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        sb.append("': baseMediaDecodeTime=");
        sb.append(getBaseMediaDecodeTime());
        return sb.toString();
    }
}
