package net.frogmouth.rnd.eofff.isobmff.meta;

import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;

public class ItemReferenceBox extends FullBox {
    List<SingleItemReferenceBox> items = new ArrayList<>();

    public ItemReferenceBox(long size, FourCC name) {
        super(size, name);
    }

    @Override
    public String getFullName() {
        return "ItemReferenceBox";
    }

    public List<SingleItemReferenceBox> getItems() {
        return new ArrayList<>(this.items);
    }

    public void addItem(SingleItemReferenceBox item) {
        this.items.add(item);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        sb.append("': item_count=");
        sb.append(getItems().size());
        int i = 1;
        for (SingleItemReferenceBox item : getItems()) {
            sb.append("\n\t   ");
            sb.append(i);
            sb.append(")");
            sb.append(item.toString());
            i += 1;
        }
        return sb.toString();
    }
}
