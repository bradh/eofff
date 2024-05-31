package net.frogmouth.rnd.eofff.isobmff.grpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public abstract class AbstractEntityToGroupBox extends FullBox implements EntityToGroup {

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

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = getStringBuilder(nestingLevel);
        return sb.toString();
    }

    protected StringBuilder getStringBuilder(int nestingLevel) {
        StringBuilder sb = getBaseStringBuilder(nestingLevel);
        sb.append("group_id=");
        sb.append(groupId);
        sb.append(", entity_ids=");
        sb.append(entityIds);
        return sb;
    }
}
