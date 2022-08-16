package net.frogmouth.rnd.eofff.isobmff.meta;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class IlstBox extends BaseBox {
    // TODO: this looks woeful.
    private byte[] data;

    public IlstBox(FourCC name) {
        super(name);
    }

    @Override
    public String getFullName() {
        return "ILSTBox";
    }

    @Override
    public long getSize() {
        return Integer.BYTES + FourCC.BYTES + data.length;
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
        sb.append("': bytes=");
        sb.append(data.length);
        sb.append(" bytes");
        return sb.toString();
    }
}
