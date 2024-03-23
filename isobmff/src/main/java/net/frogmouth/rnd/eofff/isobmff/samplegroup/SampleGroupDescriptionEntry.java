package net.frogmouth.rnd.eofff.isobmff.samplegroup;

import net.frogmouth.rnd.eofff.isobmff.FourCC;

/** Base class for sample group entries. */
public abstract class SampleGroupDescriptionEntry implements SampleGroupEntry {

    private final FourCC groupingType;
    private final String fullName;

    private static final String INDENT = "    ";

    public SampleGroupDescriptionEntry(FourCC grouping_type, String fullName) {
        this.groupingType = grouping_type;
        this.fullName = fullName;
    }

    @Override
    public String getFullName() {
        return fullName;
    }

    @Override
    public FourCC getGroupingType() {
        return groupingType;
    }

    // TODO: refactor this into some formatter class
    protected void addIndent(int nestingLevel, StringBuilder sb) {
        for (int i = 0; i < nestingLevel; i++) {
            sb.append(INDENT);
        }
    }

    protected StringBuilder getBaseStringBuilder(int nestingLevel) {
        StringBuilder sb = new StringBuilder();
        addIndent(nestingLevel, sb);
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getGroupingType());
        sb.append("': ");
        return sb;
    }
}
