package net.frogmouth.rnd.eofff.isobmff.meta;

import java.io.IOException;
import java.io.OutputStream;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class IlstBox extends BaseBox {
    private byte[] data;

    public IlstBox(long size, FourCC name) {
        super(size, name);
    }

    @Override
    public String getFullName() {
        return "ILSTBox";
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public void writeTo(OutputStream stream) throws IOException {
        stream.write(this.getSizeAsBytes());
        stream.write(getFourCC().toBytes());
        stream.write(data);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        sb.append("': bytes=");
        sb.append(data.length);
        sb.append(" bytes");
        return sb.toString();
    }
}
