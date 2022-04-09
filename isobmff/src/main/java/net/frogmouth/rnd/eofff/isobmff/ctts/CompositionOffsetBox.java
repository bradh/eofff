package net.frogmouth.rnd.eofff.isobmff.ctts;

import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;

/**
 * Composition Time to Sample Box.
 *
 * <p>See ISO/IEC 14496-12:2015 Section 8.6.1.3.
 */
public class CompositionOffsetBox extends FullBox {
    private final List<CompositionOffsetBoxEntry> entries = new ArrayList<>();

    public CompositionOffsetBox(long size, FourCC name) {
        super(size, name);
    }

    @Override
    public String getFullName() {
        return "CompositionOffsetBox";
    }

    public List<CompositionOffsetBoxEntry> getEntries() {
        return new ArrayList<>(this.entries);
    }

    public void addEntry(CompositionOffsetBoxEntry item) {
        this.entries.add(item);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        sb.append("' (version=");
        sb.append(getVersion());
        sb.append("): item_count=");
        sb.append(getEntries().size());
        for (CompositionOffsetBoxEntry item : getEntries()) {
            sb.append("\n");
            sb.append("\t\t\t\t\t  ");
            sb.append(item.toString());
        }
        return sb.toString();
    }
}
