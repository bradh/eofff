package net.frogmouth.rnd.eofff.isobmff.trgr;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Stereo video track group.
 *
 * <p>This is {@code ster}, indicating this track is either the left or right view of a stereo pair
 * suitable for playback on a stereoscopic display.
 *
 * <p>See ISO/IEC 14496-12:2022(E) Section 8.3.4.4.
 */
public class StereoVideoGroupBox extends BaseTrackGroupTypeBox {

    private boolean leftViewFlag;
    public static final long HIGH_BIT_FLAG = 0x80000000;
    public static final FourCC STER = new FourCC("ster");

    public StereoVideoGroupBox() {
        super(STER);
    }

    public boolean isLeftViewFlag() {
        return leftViewFlag;
    }

    public void setLeftViewFlag(boolean leftViewFlag) {
        this.leftViewFlag = leftViewFlag;
    }

    @Override
    public String getFullName() {
        return "StereoVideoGroupBox";
    }

    @Override
    public long getBodySize() {
        long size = getBaseBodySize();
        size += Integer.BYTES;
        return size;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        writeBaseTrackGroupTypeBoxContent(stream);
        if (leftViewFlag) {
            stream.writeUnsignedInt32(HIGH_BIT_FLAG);
        } else {
            stream.writeUnsignedInt32(0);
        }
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("track_group_id=");
        sb.append(this.getTrackGroupId());
        sb.append(", left_view_flag=");
        sb.append(leftViewFlag);
        return sb.toString();
    }
}
