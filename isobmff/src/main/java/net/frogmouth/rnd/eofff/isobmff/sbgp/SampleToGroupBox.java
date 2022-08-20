package net.frogmouth.rnd.eofff.isobmff.sbgp;

import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;

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

    // TODO: write

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        sb.append("':, grouping_type=");
        sb.append(new FourCC((int) getGroupingType()));
        if (getGroupingTypeParameter() != null) {
            sb.append(", grouping_type_parameter=");
            sb.append(getGroupingTypeParameter());
        }
        sb.append(", entry_count=");
        sb.append(getEntries().size());
        for (SampleToGroupBoxEntry item : getEntries()) {
            sb.append("\n");
            sb.append("\t\t\t\t\t  ");
            sb.append(item.toString());
        }
        return sb.toString();
    }
}
