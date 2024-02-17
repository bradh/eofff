package net.frogmouth.rnd.eofff.gopro;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

// Media UUID, plus some extra stuff.
public class MUID extends BaseBox {

    private List<Long> values = new ArrayList<>();
    public static final FourCC MUID_ATOM = new FourCC("MUID");

    public MUID() {
        super(MUID_ATOM);
    }

    @Override
    public String getFullName() {
        return "GoPro Media Unique ID";
    }

    public List<Long> getValues() {
        return values;
    }

    public void setValues(List<Long> values) {
        this.values = values;
    }

    @Override
    public void writeTo(OutputStreamWriter writer) throws IOException {
        this.writeBoxHeader(writer);
        for (long value : values) {
            writer.writeUnsignedInt32(value);
        }
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append(values);
        return sb.toString();
    }

    @Override
    public long getBodySize() {
        return values.size() * Integer.BYTES;
    }
}
