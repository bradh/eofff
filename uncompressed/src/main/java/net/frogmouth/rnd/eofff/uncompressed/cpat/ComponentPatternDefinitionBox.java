package net.frogmouth.rnd.eofff.uncompressed.cpat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemFullProperty;

/**
 * Component Palette Box.
 *
 * <p>See ISO/IEC 23001-17:2024 Section 6.1.3.
 */
public class ComponentPatternDefinitionBox extends ItemFullProperty {

    public static final FourCC CPAT_ATOM = new FourCC("cpat");

    private int patternWidth;
    private final List<PatternDefinitionEntry> entries = new ArrayList<>();

    public ComponentPatternDefinitionBox() {
        super(CPAT_ATOM);
    }

    public int getPatternWidth() {
        return patternWidth;
    }

    public void setPatternWidth(int patternWidth) {
        this.patternWidth = patternWidth;
    }

    public void addEntry(PatternDefinitionEntry entry) {
        this.entries.add(entry);
    }

    public List<PatternDefinitionEntry> getEntries() {
        return entries;
    }

    @Override
    public String getFullName() {
        return "ComponentPatternDefinitionBox";
    }

    @Override
    public long getBodySize() {
        long size = 0;
        size += Short.BYTES;
        size += Short.BYTES;
        size += (PatternDefinitionEntry.BYTES * entries.size());
        return size;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writeUnsignedInt16(patternWidth);
        int patternHeight = entries.size() / patternWidth;
        if (patternHeight * patternWidth != entries.size()) {
            throw new IOException("The number of pattern entries must fill the pattern");
        }
        stream.writeUnsignedInt16(patternHeight);
        for (PatternDefinitionEntry entry : this.entries) {
            entry.writeTo(stream);
        }
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        sb.append("': pattern_width=");
        sb.append(this.patternWidth);
        sb.append(", pattern_height=");
        sb.append(entries.size() / patternWidth);
        for (PatternDefinitionEntry entry : this.entries) {
            entry.appendToStringBuilder(sb);
        }
        return sb.toString();
    }
}
