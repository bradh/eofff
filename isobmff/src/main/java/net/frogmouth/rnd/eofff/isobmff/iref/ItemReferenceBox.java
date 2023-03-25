package net.frogmouth.rnd.eofff.isobmff.iref;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Item Reference Box.
 *
 * <p>See ISO/IEC 14496-12:2015 Section 8.11.12.
 */
public class ItemReferenceBox extends FullBox {
    public static final FourCC IREF_ATOM = new FourCC("iref");
    List<SingleItemReferenceBox> items = new ArrayList<>();

    public ItemReferenceBox() {
        super(IREF_ATOM);
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
    public long getBodySize() {
        long size = 0;
        for (SingleItemReferenceBox singleItemReferenceBox : this.items) {
            size += singleItemReferenceBox.getSize(getVersion());
        }
        return size;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        for (SingleItemReferenceBox singleItemReferenceBox : this.items) {
            singleItemReferenceBox.writeTo(stream, getVersion());
        }
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
