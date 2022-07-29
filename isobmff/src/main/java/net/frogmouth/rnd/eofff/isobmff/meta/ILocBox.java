package net.frogmouth.rnd.eofff.isobmff.meta;

import static net.frogmouth.rnd.eofff.isobmff.BaseBox.intToBytes;
import static net.frogmouth.rnd.eofff.isobmff.BaseBox.shortToBytes;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;

/**
 * Item Location Box.
 *
 * <p>See ISO/IEC 14496-12:2015 Section 8.11.3.
 */
public class ILocBox extends FullBox {
    private int offsetSize;
    private int lengthSize;
    private int baseOffsetSize;
    private int indexSize;
    private final List<ILocItem> items = new ArrayList<>();

    public ILocBox(long size, FourCC name) {
        super(size, name);
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
    public void writeTo(OutputStream stream) throws IOException {
        stream.write(this.getSizeAsBytes());
        stream.write(getFourCC().toBytes());
        stream.write(getVersionAndFlagsAsBytes());
        int sizes = (offsetSize << 12) + (lengthSize << 8) + (baseOffsetSize << 4);
        if ((getVersion() == 1) || (getVersion() == 2)) {
            sizes |= indexSize;
        }
        stream.write(shortToBytes((short) sizes));
        if (getVersion() < 2) {
            stream.write(shortToBytes((short) items.size()));
        } else {
            stream.write(intToBytes(items.size()));
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
