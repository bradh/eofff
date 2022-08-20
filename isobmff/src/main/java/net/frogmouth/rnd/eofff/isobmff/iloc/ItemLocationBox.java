package net.frogmouth.rnd.eofff.isobmff.iloc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Item Location Box.
 *
 * <p>See ISO/IEC 14496-12:2015 Section 8.11.3.
 */
public class ItemLocationBox extends FullBox {
    public static final FourCC ILOC_ATOM = new FourCC("iloc");
    private int offsetSize;
    private int lengthSize;
    private int baseOffsetSize;
    private int indexSize;
    private final List<ILocItem> items = new ArrayList<>();

    public ItemLocationBox() {
        super(ILOC_ATOM);
    }

    @Override
    public long getSize() {
        long size = Integer.BYTES + FourCC.BYTES + 1 + 3;
        size += 2; // sizes (or sizes + reserved)
        if (getVersion() < 2) {
            size += Short.BYTES;
        } else {
            size += Integer.BYTES;
        }
        for (ILocItem item : this.items) {
            size += item.getSize(getVersion());
        }
        return size;
    }

    @Override
    public long getBodySize() {
        long size = 0;
        size += 2; // sizes (or sizes + reserved)
        if (getVersion() < 2) {
            size += Short.BYTES;
        } else {
            size += Integer.BYTES;
        }
        for (ILocItem item : this.items) {
            size += item.getSize(getVersion());
        }
        return size;
    }

    @Override
    public String getFullName() {
        return "ItemLocationBox";
    }

    public int getOffsetSize() {
        return offsetSize;
    }

    public void setOffsetSize(int offsetSize) {
        this.offsetSize = offsetSize;
    }

    public int getLengthSize() {
        return lengthSize;
    }

    public void setLengthSize(int lengthSize) {
        this.lengthSize = lengthSize;
    }

    public int getBaseOffsetSize() {
        return baseOffsetSize;
    }

    public void setBaseOffsetSize(int baseOffsetSize) {
        this.baseOffsetSize = baseOffsetSize;
    }

    public int getIndexSize() {
        return indexSize;
    }

    public void setIndexSize(int indexSize) {
        this.indexSize = indexSize;
    }

    public List<ILocItem> getItems() {
        return new ArrayList<>(this.items);
    }

    public void addItem(ILocItem item) {
        this.items.add(item);
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        int sizes = (offsetSize << 12) + (lengthSize << 8) + (baseOffsetSize << 4);
        if ((getVersion() == 1) || (getVersion() == 2)) {
            sizes |= indexSize;
        }
        stream.writeShort((short) sizes);
        if (getVersion() < 2) {
            stream.writeShort((short) items.size());
        } else {
            stream.writeInt(items.size());
        }
        for (ILocItem item : items) {
            item.writeTo(stream, getVersion());
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
        for (ILocItem item : getItems()) {
            sb.append("\n");
            sb.append("\t  ");
            sb.append(item.toString());
        }
        return sb.toString();
    }
}
