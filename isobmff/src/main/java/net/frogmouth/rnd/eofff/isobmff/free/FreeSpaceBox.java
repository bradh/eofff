package net.frogmouth.rnd.eofff.isobmff.free;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Free Space Box.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.1.2
 */
public abstract class FreeSpaceBox extends BaseBox {

    protected byte[] data;

    /**
     * Constructor
     *
     * @param free_type the fourCC - `free` or `skip`
     */
    protected FreeSpaceBox(FourCC free_type) {
        super(free_type);
    }

    @Override
    public String getFullName() {
        return "Free Space Box";
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
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append(data.length);
        sb.append(" bytes");
        return sb.toString();
    }
}
