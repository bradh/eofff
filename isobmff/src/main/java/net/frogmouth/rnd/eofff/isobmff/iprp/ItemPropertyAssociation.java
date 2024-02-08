package net.frogmouth.rnd.eofff.isobmff.iprp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Item property association box.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.11.14.
 */
public class ItemPropertyAssociation extends FullBox {
    public static final FourCC IPMA_ATOM = new FourCC("ipma");
    private final List<AssociationEntry> entries = new ArrayList<>();

    public ItemPropertyAssociation() {
        super(IPMA_ATOM);
    }

    @Override
    public long getBodySize() {
        int count = Integer.BYTES;
        for (AssociationEntry entry : entries) {
            count += entry.getSize(getVersion(), getFlags());
        }
        return count;
    }

    @Override
    public String getFullName() {
        return "ItemPropertyAssociation";
    }

    @Override
    public void writeTo(OutputStreamWriter writer) throws IOException {
        this.writeBoxHeader(writer);
        Collections.sort(entries, Comparator.comparingLong(AssociationEntry::getItemId));
        writer.writeUnsignedInt32(entries.size());
        for (AssociationEntry entry : entries) {
            entry.writeTo(writer, getVersion(), getFlags());
        }
    }

    public void addEntry(AssociationEntry entry) {
        this.entries.add(entry);
    }

    public List<AssociationEntry> getEntries() {
        return new ArrayList<>(this.entries);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        sb.append("': ");
        for (AssociationEntry entry : entries) {
            sb.append("\n\t");
            sb.append(entry.toString());
        }
        return sb.toString();
    }
}
