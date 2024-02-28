package net.frogmouth.rnd.eofff.isobmff.tsel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Track Selection Box.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.1.3.
 */
public class TrackSelectionBox extends FullBox {
    public static final FourCC TSEL_ATOM = new FourCC("tsel");

    private long switchGroup = 0;
    private List<FourCC> attributes = new ArrayList<>();

    public TrackSelectionBox() {
        super(TSEL_ATOM);
    }

    @Override
    public String getFullName() {
        return "TrackSelectionBox";
    }

    public long getSwitchGroup() {
        return switchGroup;
    }

    public void setSwitchGroup(long switchGroup) {
        this.switchGroup = switchGroup;
    }

    public List<FourCC> getAttributes() {
        return attributes;
    }

    public void addAttribute(FourCC attribute) {
        this.attributes.add(attribute);
    }

    @Override
    public long getBodySize() {
        return Integer.BYTES + attributes.size() * FourCC.BYTES;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writeUnsignedInt32(switchGroup);
        for (FourCC attribute : attributes) {
            stream.writeFourCC(attribute);
        }
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("switch_group=");
        sb.append(switchGroup);
        sb.append(", attributes=");
        sb.append(attributes.toString());
        return sb.toString();
    }
}
