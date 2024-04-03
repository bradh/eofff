package net.frogmouth.rnd.eofff.isobmff.stsg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Sub track sample group box (stsg)
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.14.6
 */
public class SubTrackSampleGroupBox extends FullBox {
    public static final FourCC STSG_ATOM = new FourCC("stsg");

    public FourCC groupingType;
    public final List<Long> groupDescriptionIndexes = new ArrayList<>();

    public SubTrackSampleGroupBox() {
        super(STSG_ATOM);
    }

    @Override
    public String getFullName() {
        return "SubTrackSampleGroupBox";
    }

    @Override
    public long getBodySize() {
        long size = 0;
        size += FourCC.BYTES;
        size += Short.BYTES;
        size += (groupDescriptionIndexes.size() * Integer.BYTES);
        return size;
    }

    public FourCC getGroupingType() {
        return groupingType;
    }

    public void setGroupingType(FourCC groupingType) {
        this.groupingType = groupingType;
    }

    public List<Long> getGroupDescriptionIndexes() {
        return groupDescriptionIndexes;
    }

    public void addGroupDescriptionIndex(Long index) {
        groupDescriptionIndexes.add(index);
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writeFourCC(groupingType);
        stream.writeUnsignedInt16((groupDescriptionIndexes.size()));
        for (Long index : this.groupDescriptionIndexes) {
            stream.writeUnsignedInt32(index);
        }
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("grouping_type=");
        sb.append(this.groupingType.toString());
        sb.append(", item_count=");
        sb.append(this.groupDescriptionIndexes.size());
        sb.append(", group_description_index=[");
        for (var index : this.groupDescriptionIndexes) {
            sb.append(index);
            sb.append(" ");
        }
        sb.append("]");
        return sb.toString();
    }
}
