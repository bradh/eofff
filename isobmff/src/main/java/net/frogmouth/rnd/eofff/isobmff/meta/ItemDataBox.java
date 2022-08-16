package net.frogmouth.rnd.eofff.isobmff.meta;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class ItemDataBox extends BaseBox {
    private byte[] data;

    public ItemDataBox(FourCC name) {
        super(name);
    }

    @Override
    public String getFullName() {
        return "ItemDataBox";
    }

    @Override
    public long getSize() {
        long size = Integer.BYTES + FourCC.BYTES + data.length;
        return size;
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
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        sb.append("': data (length=");
        sb.append(data.length);
        sb.append(")");
        return sb.toString();
    }
}
