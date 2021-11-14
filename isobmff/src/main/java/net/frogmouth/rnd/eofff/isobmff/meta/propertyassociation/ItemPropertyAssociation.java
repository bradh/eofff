package net.frogmouth.rnd.eofff.isobmff.meta.propertyassociation;

import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;

public class ItemPropertyAssociation extends FullBox {

    private final List<AssociationEntry> entries = new ArrayList<>();

    public ItemPropertyAssociation(long size, FourCC name) {
        super(size, name);
    }

    @Override
    public String getFullName() {
        return "ItemPropertyAssociation";
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
