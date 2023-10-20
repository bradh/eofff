package net.frogmouth.rnd.eofff.imagefileformat.extensions.properties;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class ItemPropertyContainerBox extends BaseBox {

    public static FourCC IPCO_ATOM = new FourCC("ipco");

    private final List<AbstractItemProperty> properties = new ArrayList<>();

    public ItemPropertyContainerBox() {
        super(IPCO_ATOM);
    }

    @Override
    public String getFullName() {
        return "ItemPropertyContainerBox";
    }

    public List<AbstractItemProperty> getProperties() {
        return new ArrayList<>(properties);
    }

    /**
     * Add a property to the property container box.
     *
     * @param property the property to add
     * @return the 1-base property index for the property that was added
     */
    public int addProperty(AbstractItemProperty property) {
        properties.add(property);
        return properties.size();
    }

    @Override
    public long getBodySize() {
        long size = 0;
        for (AbstractItemProperty itemProperty : properties) {
            size += itemProperty.getSize();
        }
        return size;
    }

    @Override
    public void writeTo(OutputStreamWriter writer) throws IOException {
        this.writeBoxHeader(writer);
        for (AbstractItemProperty itemProperty : properties) {
            itemProperty.writeTo(writer);
        }
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
