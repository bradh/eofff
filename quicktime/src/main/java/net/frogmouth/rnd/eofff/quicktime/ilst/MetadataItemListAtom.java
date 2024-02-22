package net.frogmouth.rnd.eofff.quicktime.ilst;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Metadata item list atom.
 *
 * <p>See https://developer.apple.com/documentation/quicktime-file-format/metadata_item_list_atom
 */
public class MetadataItemListAtom extends BaseBox {
    // TODO: this looks woeful.
    private byte[] data;

    public MetadataItemListAtom(FourCC name) {
        super(name);
    }

    @Override
    public String getFullName() {
        return "ILSTBox";
    }

    @Override
    public long getBodySize() {
        return data.length;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        stream.writeInt((int) this.getSize());
        stream.writeFourCC(getFourCC());
        stream.write(data);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("bytes=");
        sb.append(data.length);
        sb.append(" bytes");
        return sb.toString();
    }
}
