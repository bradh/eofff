package net.frogmouth.rnd.eofff.jpeg2000.fileformat.jp2c;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class ContiguousCodestreamBox extends BaseBox {

    public static final FourCC JP2C_ATOM = new FourCC("jp2c");
    private long initialOffset;
    private byte[] data;

    public ContiguousCodestreamBox() {
        super(JP2C_ATOM);
    }

    @Override
    public String getFullName() {
        return "Contiguous Codestream Box";
    }

    @Override
    public long getBodySize() {
        return data.length;
    }

    public long getInitialOffset() {
        return initialOffset;
    }

    public void setInitialOffset(long initialOffset) {
        this.initialOffset = initialOffset;
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
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("length=");
        sb.append(data.length);
        return sb.toString();
    }
}
