package net.frogmouth.rnd.eofff.gopro.quicktime;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntry;

public class GoProMetadataSampleEntry extends BaseBox implements SampleEntry {

    // No description for what this actually is. Assuming 4 bytes
    private long value;

    public static final FourCC GPMD_ATOM = new FourCC("gpmd");

    public GoProMetadataSampleEntry() {
        super(GPMD_ATOM);
    }

    @Override
    public String getFullName() {
        return "GoPro Metadata sample description";
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writeUnsignedInt32(value);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("value: ");
        sb.append(value);
        return sb.toString();
    }

    @Override
    public long getBodySize() {
        return Integer.BYTES;
    }
}
