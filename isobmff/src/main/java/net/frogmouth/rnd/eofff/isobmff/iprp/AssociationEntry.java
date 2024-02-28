package net.frogmouth.rnd.eofff.isobmff.iprp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class AssociationEntry {
    private long itemId;
    private final List<PropertyAssociation> associations = new ArrayList<>();
    private static final String INDENT = "    ";

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

    void addAssociations(List<PropertyAssociation> associations) {
        this.associations.addAll(associations);
    }

    public int getSize(int version, int flags) {
        int size = 0;
        if (version < 1) {
            size += Short.BYTES;
        } else {
            size += Integer.BYTES;
        }
        size += Byte.BYTES;
        for (PropertyAssociation association : associations) {
            size += association.getSize(flags);
        }
        return size;
    }

    public String toString(int nestingLevel) {
        StringBuilder sb = new StringBuilder();
        addIndent(nestingLevel, sb);
        sb.append("item_id=").append(itemId);
        for (PropertyAssociation association : this.associations) {
            sb.append("\n");
            this.addIndent(nestingLevel + 1, sb);
            sb.append(association.toString());
        }
        return sb.toString();
    }

    private void addIndent(int nestingLevel, StringBuilder sb) {
        for (int i = 0; i < nestingLevel; i++) {
            sb.append(INDENT);
        }
    }

    void writeTo(OutputStreamWriter writer, int version, int flags) throws IOException {
        if (version < 1) {
            writer.writeUnsignedInt16(itemId);
        } else {
            writer.writeUnsignedInt32(itemId);
        }
        writer.writeByte(associations.size());
        for (PropertyAssociation association : associations) {
            association.writeTo(writer, flags);
        }
    }
}
