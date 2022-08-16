package net.frogmouth.rnd.eofff.isobmff.moof;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;

public class TrackFragmentDecodeTimeBox extends FullBox {
    private long baseMediaDecodeTime;

    public TrackFragmentDecodeTimeBox(FourCC name) {
        super(name);
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
