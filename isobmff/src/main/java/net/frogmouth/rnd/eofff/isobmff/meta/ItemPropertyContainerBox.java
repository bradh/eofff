package net.frogmouth.rnd.eofff.isobmff.meta;

import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.meta.property.AbstractItemProperty;

public class ItemPropertyContainerBox extends BaseBox {

    private List<AbstractItemProperty> properties = new ArrayList<>();

    public ItemPropertyContainerBox(long size, FourCC name) {
        super(size, name);
    }

    @Override
    public String getFullName() {
        return "ItemPropertyContainerBox";
    }

    public List<AbstractItemProperty> getProperties() {
        return new ArrayList<>(properties);
    }

    public void addProperty(AbstractItemProperty property) {
        properties.add(property);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        sb.append("':");
        int i = 1;
        for (AbstractItemProperty property : properties) {
            sb.append("\n\t\t");
            sb.append(i);
            sb.append(".\t");
            if (property == null) {
                sb.append("[Unhandled property]");
            } else {
                sb.append(property.toString());
            }
            i += 1;
        }
        return sb.toString();
    }
}
