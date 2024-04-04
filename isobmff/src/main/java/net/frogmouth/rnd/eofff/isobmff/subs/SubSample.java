package net.frogmouth.rnd.eofff.isobmff.subs;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class SubSample {
    public static final long VERSION0_BYTES = Short.BYTES + Byte.BYTES + Byte.BYTES + Integer.BYTES;
    public static final long VERSION1_BYTES =
            Integer.BYTES + Byte.BYTES + Byte.BYTES + Integer.BYTES;

    private long subsampleSize;
    private int subsamplePriority;
    private int discardable;
    private long codecSpecificParameters;

    public long getSubsampleSize() {
        return subsampleSize;
    }

    public void setSubsampleSize(long subsampleSize) {
        this.subsampleSize = subsampleSize;
    }

    public int getSubsamplePriority() {
        return subsamplePriority;
    }

    public void setSubsamplePriority(int subsamplePriority) {
        this.subsamplePriority = subsamplePriority;
    }

    public int getDiscardable() {
        return discardable;
    }

    public void setDiscardable(int discardable) {
        this.discardable = discardable;
    }

    public long getCodecSpecificParameters() {
        return codecSpecificParameters;
    }

    public void setCodecSpecificParameters(long codecSpecificParameters) {
        this.codecSpecificParameters = codecSpecificParameters;
    }

    void writeTo(StringBuilder sb) {
        sb.append("subsample_size=");
        sb.append(subsampleSize);
        sb.append(", subsample_priority=");
        sb.append(subsamplePriority);
        sb.append(", discardable=");
        sb.append(discardable);
        sb.append(", codec_specific_parameter=");
        sb.append(codecSpecificParameters);
    }

    void writeTo(OutputStreamWriter stream, int version) throws IOException {
        if (version == 1) {
            stream.writeUnsignedInt32(subsampleSize);
        } else {
            stream.writeUnsignedInt16(subsampleSize);
        }
        stream.writeUnsignedInt8(subsamplePriority);
        stream.writeUnsignedInt8(discardable);
        stream.writeUnsignedInt32(codecSpecificParameters);
    }
}
