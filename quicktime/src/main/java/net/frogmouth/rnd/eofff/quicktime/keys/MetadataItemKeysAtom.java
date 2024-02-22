package net.frogmouth.rnd.eofff.quicktime.keys;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Metadata item list atom.
 *
 * <p>See https://developer.apple.com/documentation/quicktime-file-format/metadata_item_keys_atom
 */
public class MetadataItemKeysAtom extends FullBox {

    public static final FourCC KEYS_ATOM = new FourCC("keys");
    private final List<MetadataItemEntry> entries = new ArrayList<>();

    public MetadataItemKeysAtom() {
        super(KEYS_ATOM);
    }

    @Override
    public String getFullName() {
        return "Metadata item keys atom";
    }

    public List<MetadataItemEntry> getEntries() {
        return entries;
    }

    public void addEntry(MetadataItemEntry entry) {
        this.entries.add(entry);
    }

    @Override
    public long getBodySize() {
        long size = 0;
        size += Integer.BYTES;
        for (MetadataItemEntry entry : entries) {
            size += entry.getSize();
        }
        return size;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writeUnsignedInt32(entries.size());
        for (MetadataItemEntry entry : entries) {
            entry.writeTo(stream);
        }
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        // TODO
        return sb.toString();
    }
}
