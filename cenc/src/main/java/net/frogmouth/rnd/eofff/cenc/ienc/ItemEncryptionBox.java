package net.frogmouth.rnd.eofff.cenc.ienc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemFullProperty;

public class ItemEncryptionBox extends ItemFullProperty {
    public static final FourCC IENC_ATOM = new FourCC("ienc");

    private int cryptByteBlock;
    private int skipByteBlock;
    private final List<ItemEncryptionEntry> entries = new ArrayList<>();

    public ItemEncryptionBox() {
        super(IENC_ATOM);
    }

    public int getCryptByteBlock() {
        return cryptByteBlock;
    }

    public void setCryptByteBlock(int cryptByteBlock) {
        this.cryptByteBlock = cryptByteBlock;
    }

    public int getSkipByteBlock() {
        return skipByteBlock;
    }

    public void setSkipByteBlock(int skipByteBlock) {
        this.skipByteBlock = skipByteBlock;
    }

    public List<ItemEncryptionEntry> getEntries() {
        return entries;
    }

    public void addEntry(ItemEncryptionEntry entry) {
        entries.add(entry);
    }

    @Override
    public long getBodySize() {
        int size = 0;
        // TODO
        return size;
    }

    @Override
    public String getFullName() {
        return "ItemEncryptionBox";
    }

    @Override
    public void writeTo(OutputStreamWriter writer) throws IOException {
        this.writeBoxHeader(writer);
        // TODO
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        if (getVersion() > 0) {
            sb.append("crypt_byte_block=");
            sb.append(this.cryptByteBlock);
            sb.append(", skip_byte_block=");
            sb.append(this.skipByteBlock);
            sb.append(", ");
        }
        sb.append("num_keys=");
        sb.append(this.entries.size());
        for (var entry : this.entries) {
            entry.append_to(sb);
        }
        return sb.toString();
    }
}
