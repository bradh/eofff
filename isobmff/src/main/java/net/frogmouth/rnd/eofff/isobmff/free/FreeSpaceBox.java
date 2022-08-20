package net.frogmouth.rnd.eofff.isobmff.free;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class FreeSpaceBox extends BaseBox {

    protected byte[] data;

    protected FreeSpaceBox(FourCC name) {
        super(name);
    }

    @Override
    public String getFullName() {
        return "Free Space Box";
    }

    // @Override
    public long getSize() {
        return Integer.BYTES + FourCC.BYTES + data.length;
    }

    @Override
    public long getBodySize() {
        return data.length;
    }

    public byte[] getData() {
        return data.clone();
    }

    public void setData(byte[] data) {
        this.data = data.clone();
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
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
