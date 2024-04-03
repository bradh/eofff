package net.frogmouth.rnd.eofff.isobmff.stri;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Sub track information box (stri)
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.14.4
 */
public class SubTrackInformationBox extends FullBox {
    public static final FourCC STRI_ATOM = new FourCC("stri");

    private int switchGroup = 0;
    private int alternateGroup = 0;
    private long subTrackId = 0;
    private final List<FourCC> attributes = new ArrayList<>();

    public SubTrackInformationBox() {
        super(STRI_ATOM);
    }

    @Override
    public String getFullName() {
        return "SubTrackSampleGroupBox";
    }

    @Override
    public long getBodySize() {
        long size = 0;
        size += Short.BYTES;
        size += Short.BYTES;
        size += Integer.BYTES;
        size += (attributes.size() * FourCC.BYTES);
        return size;
    }

    public int getSwitchGroup() {
        return switchGroup;
    }

    public void setSwitchGroup(int switchGroup) {
        this.switchGroup = switchGroup;
    }

    public int getAlternateGroup() {
        return alternateGroup;
    }

    public void setAlternateGroup(int alternateGroup) {
        this.alternateGroup = alternateGroup;
    }

    public long getSubTrackId() {
        return subTrackId;
    }

    public void setSubTrackId(long subTrackId) {
        this.subTrackId = subTrackId;
    }

    public List<FourCC> getAttributeList() {
        return attributes;
    }

    public void addAttribute(final FourCC attribute) {
        attributes.add(attribute);
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writeUnsignedInt16(switchGroup);
        stream.writeUnsignedInt16(alternateGroup);
        stream.writeUnsignedInt32(subTrackId);
        for (FourCC attribute : attributes) {
            stream.writeFourCC(attribute);
        }
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("switch_group=");
        sb.append(this.switchGroup);
        sb.append(", alternate_group=");
        sb.append(this.alternateGroup);
        sb.append(", sub_track_ID=");
        sb.append(this.subTrackId);
        sb.append(", attribute_list=");
        for (FourCC attribute : attributes) {
            sb.append(attribute.toString());
            sb.append(" ");
        }
        return sb.toString();
    }
}
