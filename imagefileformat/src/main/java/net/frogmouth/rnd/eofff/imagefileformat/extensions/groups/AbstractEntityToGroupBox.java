package net.frogmouth.rnd.eofff.imagefileformat.extensions.groups;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

// TODO: move to isobmff module
public abstract class AbstractEntityToGroupBox extends FullBox {

    private long groupId;
    private final List<Long> entityIds = new ArrayList<>();

    public AbstractEntityToGroupBox(FourCC name) {
        super(name);
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public List<Long> getEntities() {
        return new ArrayList<>(entityIds);
    }

    public void addEntity(long id) {
        this.entityIds.add(id);
    }

    @Override
    public abstract String getFullName();

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        sb.append("': group_id=");
        sb.append(this.groupId);
        sb.append(", entity_id=[");
        for (Long entityId : this.getEntities()) {
            sb.append(entityId);
            sb.append(",");
        }
        sb.append("];");
        return sb.toString();
    }

    @Override
    public void writeTo(OutputStreamWriter writer) throws IOException {
        this.writeBoxHeader(writer);
        writer.writeUnsignedInt32(this.groupId);
        writer.writeUnsignedInt32(this.getEntities().size());
        for (Long entityId : this.getEntities()) {
            writer.writeUnsignedInt32(entityId);
        }
    }

    @Override
    public long getBodySize() {
        int size = 0;
        size += Integer.BYTES;
        size += Integer.BYTES;
        size += (this.getEntities().size() * Integer.BYTES);
        return size;
    }
}
