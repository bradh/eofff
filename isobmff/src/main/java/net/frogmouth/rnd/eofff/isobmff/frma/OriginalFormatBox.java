package net.frogmouth.rnd.eofff.isobmff.frma;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Original Format Box (frma).
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.12.3
 */
public class OriginalFormatBox extends BaseBox {
    public static final FourCC FRMA_ATOM = new FourCC("frma");
    private FourCC dataFormat;

    public OriginalFormatBox() {
        super(FRMA_ATOM);
    }

    @Override
    public String getFullName() {
        return "OriginalFormatBox";
    }

    public FourCC getDataFormat() {
        return dataFormat;
    }

    public void setDataFormat(FourCC dataFormat) {
        this.dataFormat = dataFormat;
    }

    @Override
    public long getBodySize() {
        return FourCC.BYTES;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writeFourCC(dataFormat);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("data_format=");
        sb.append(dataFormat.toString());
        return sb.toString();
    }
}
