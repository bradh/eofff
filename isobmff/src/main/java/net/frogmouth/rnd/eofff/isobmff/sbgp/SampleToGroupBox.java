package net.frogmouth.rnd.eofff.isobmff.sbgp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Sample to Group Box.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.9.2.
 */
public class SampleToGroupBox extends FullBox {
    public static final FourCC SBGP_ATOM = new FourCC("sbgp");

    private long groupingType;
    private Long groupingTypeParameter;
    private final List<SampleToGroupBoxEntry> entries = new ArrayList<>();

    public SampleToGroupBox() {
        super(SBGP_ATOM);
    }

    @Override
    public String getFullName() {
        return "SampleToGroupBox";
    }

    public long getGroupingType() {
        return groupingType;
    }

    public void setGroupingType(long groupingType) {
        this.groupingType = groupingType;
    }

    public Long getGroupingTypeParameter() {
        return groupingTypeParameter;
    }

    public void setGroupingTypeParameter(long groupingTypeParameter) {
        this.groupingTypeParameter = groupingTypeParameter;
    }

    public List<SampleToGroupBoxEntry> getEntries() {
        return new ArrayList<>(entries);
    }

    public void addEntry(SampleToGroupBoxEntry entry) {
        this.entries.add(entry);
    }

    @Override
    public void writeTo(OutputStreamWriter writer) throws IOException {
        if (groupingTypeParameter != null) {
            setVersion(1);
        }
        this.writeBoxHeader(writer);
        writer.writeUnsignedInt32(groupingType);
        if (groupingTypeParameter != null) {
            writer.writeUnsignedInt32(groupingTypeParameter);
        }
        writer.writeUnsignedInt32(entries.size());
        for (SampleToGroupBoxEntry item : entries) {
            item.writeTo(writer);
        }
    }

    @Override
    public long getBodySize() {
        long size = 0;
        size += Integer.BYTES;
        if (groupingTypeParameter != null) {
            size += Integer.BYTES;
        }
        size += Integer.BYTES;
        size += (SampleToGroupBoxEntry.BYTES * entries.size());
        return size;
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("grouping_type=");
        sb.append(new FourCC((int) getGroupingType()));
        if (getGroupingTypeParameter() != null) {
            sb.append(", grouping_type_parameter=");
            sb.append(getGroupingTypeParameter());
        }
        sb.append(", entry_count=");
        sb.append(getEntries().size());
        for (SampleToGroupBoxEntry item : getEntries()) {
            sb.append("\n");
            this.addIndent(nestingLevel + 1, sb);
            sb.append(item.toString());
        }
        return sb.toString();
    }
}
