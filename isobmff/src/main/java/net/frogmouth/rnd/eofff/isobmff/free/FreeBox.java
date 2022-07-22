package net.frogmouth.rnd.eofff.isobmff.free;

import java.io.IOException;
import java.io.OutputStream;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class FreeBox extends BaseBox {

    private byte[] data;

    public FreeBox(long size, FourCC name) {
        super(size, name);
    }

    @Override
    public String getFullName() {
        return "Free Space Box";
    }

    public byte[] getData() {
        return data.clone();
    }

    public void setData(byte[] data) {
        this.data = data.clone();
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
        sb.append("': ");
        sb.append(data.length);
        sb.append(" bytes");
        return sb.toString();
    }
}
