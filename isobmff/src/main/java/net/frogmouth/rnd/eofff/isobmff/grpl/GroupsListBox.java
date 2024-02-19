package net.frogmouth.rnd.eofff.isobmff.grpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Groups List Box.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.18.2.
 */
public class GroupsListBox extends BaseBox {

    public static final FourCC GRPL_ATOM = new FourCC("grpl");

    private List<EntityToGroup> groupings = new ArrayList<>();

    public GroupsListBox() {
        super(GRPL_ATOM);
    }

    public GroupsListBox(FourCC name) {
        super(name);
    }

    public List<EntityToGroup> getGroupings() {
        return groupings;
    }

    public void addGrouping(EntityToGroup grouping) {
        groupings.add(grouping);
    }

    @Override
    public String getFullName() {
        return "GroupsListBox";
    }

    @Override
    public long getBodySize() {
        long size = 0;
        for (EntityToGroup grouping : groupings) {
            size += grouping.getSize();
        }
        return size;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        for (EntityToGroup grouping : groupings) {
            grouping.writeTo(stream);
        }
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("(Container)");
        for (EntityToGroup grouping : groupings) {
            sb.append("\n");
            sb.append(grouping.toString(nestingLevel + 1));
        }
        return sb.toString();
    }
}
