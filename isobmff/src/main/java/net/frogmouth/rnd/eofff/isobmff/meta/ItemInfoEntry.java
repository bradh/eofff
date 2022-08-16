package net.frogmouth.rnd.eofff.isobmff.meta;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class ItemInfoEntry extends FullBox {

    public static final int MIME = 0x6d696d65;
    public static final int URI = 0x75726920;
    private long itemID;
    private int itemProtectionIndex;
    private long itemType;
    private String itemName;
    private String contentType;
    private String contentEncoding;
    private String itemUriType;
    private long extensionType;
    private ItemInfoExtension extension;

    public ItemInfoEntry(FourCC name) {
        super(name);
    }

    @Override
    public long getSize() {
        long size = Integer.BYTES + FourCC.BYTES + 1 + 3;
        if ((getVersion() == 2) && (itemID < (1 << 16))) {
            size += Short.BYTES;
        } else {
            size += Integer.BYTES;
        }
        size += Short.BYTES + Integer.BYTES;
        if (itemName != null) {
            size += itemName.getBytes(StandardCharsets.US_ASCII).length;
        }
        size += Byte.BYTES;
        if (this.itemType == new FourCC("mime").hashCode()) {
            // TODO
        } else if (this.itemType == new FourCC("uri ").hashCode()) {
            size += itemUriType.getBytes(StandardCharsets.US_ASCII).length;
            size += Byte.BYTES;
        }
        return size;
    }

    public long getItemID() {
        return itemID;
    }

    public void setItemID(long itemID) {
        this.itemID = itemID;
    }

    public int getItemProtectionIndex() {
        return itemProtectionIndex;
    }

    public void setItemProtectionIndex(int itemProtectionIndex) {
        this.itemProtectionIndex = itemProtectionIndex;
    }

    public long getItemType() {
        return itemType;
    }

    public String getItemTypeAsText() {
        ByteBuffer bb = ByteBuffer.allocate(Integer.BYTES);
        bb.putInt((int) itemType);
        return new String(bb.array(), StandardCharsets.US_ASCII);
    }

    public void setItemType(long itemType) {
        this.itemType = itemType;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentEncoding() {
        return contentEncoding;
    }

    public void setContentEncoding(String contentEncoding) {
        this.contentEncoding = contentEncoding;
    }

    public String getItemUriType() {
        return itemUriType;
    }

    public void setItemUriType(String itemUriType) {
        this.itemUriType = itemUriType;
    }

    public long getExtensionType() {
        return extensionType;
    }

    public void setExtensionType(long extensionType) {
        this.extensionType = extensionType;
    }

    public ItemInfoExtension getExtension() {
        return extension;
    }

    public void setExtension(ItemInfoExtension extension) {
        this.extension = extension;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        stream.writeInt((int) this.getSize());
        stream.writeFourCC(getFourCC());
        // TODO: handle version 0 and 1
        if ((getVersion() == 2) && (itemID >= (1 << 16))) {
            setVersion(3);
        }
        stream.write(getVersionAndFlagsAsBytes());
        if (getVersion() == 2) {
            stream.writeShort((short) this.itemID);
        } else {
            stream.writeInt((int) this.itemID);
        }
        stream.writeShort((short) itemProtectionIndex);
        stream.writeInt((int) this.itemType);
        if (itemName != null) {
            stream.write(itemName.getBytes(StandardCharsets.US_ASCII));
        }
        stream.writeByte(0); // Null terminator for string.
        if (this.getItemTypeAsText().equals("mime")) {
            // TODO
        } else if (this.getItemTypeAsText().equals("uri ")) {
            stream.write(itemUriType.getBytes(StandardCharsets.US_ASCII));
            stream.writeByte(0); // Null terminator for string.
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" '");
        sb.append(getFourCC());
        sb.append("': item_id=");
        sb.append(getItemID());
        sb.append(", item_name=");
        sb.append(getItemName());
        sb.append(", item_type=");
        sb.append(getItemTypeAsText());
        if (getItemType() == MIME) {
            sb.append(", content_type=");
            sb.append(getContentType());
            sb.append(", content_encoding=");
            sb.append(getContentEncoding());
        }
        return sb.toString();
    }
}
