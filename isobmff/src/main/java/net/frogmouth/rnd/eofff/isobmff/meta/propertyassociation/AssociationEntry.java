package net.frogmouth.rnd.eofff.isobmff.meta.propertyassociation;

import java.util.ArrayList;
import java.util.List;

public class AssociationEntry {
    private long itemId;
    private final List<PropertyAssociation> associations = new ArrayList<>();

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public List<PropertyAssociation> getAssociations() {
        return new ArrayList<>(this.associations);
    }

    public void addAssociation(PropertyAssociation association) {
        this.associations.add(association);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\titem_id=").append(itemId);
        for (PropertyAssociation association : this.associations) {
            sb.append("\n\t\t\t").append(association.toString());
        }
        return sb.toString();
    }
}
