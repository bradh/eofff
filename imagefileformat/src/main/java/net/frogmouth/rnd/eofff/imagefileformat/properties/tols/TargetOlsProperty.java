package net.frogmouth.rnd.eofff.imagefileformat.properties.tols;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemFullProperty;

/**
 * Target Output Layer Set Property (tols).
 *
 * <p>See ISO/IEC 23008-12:2022 Section 6.5.29
 */
public class TargetOlsProperty extends ItemFullProperty {
    public static final FourCC TOLS_ATOM = new FourCC("tols");

    private int targetOlsIndex;

    public TargetOlsProperty() {
        super(TOLS_ATOM);
    }

    public int getTargetOlsIndex() {
        return targetOlsIndex;
    }

    public void setTargetOlsIndex(int targetOlsIndex) {
        this.targetOlsIndex = targetOlsIndex;
    }

    @Override
    public long getBodySize() {
        return Short.BYTES;
    }

    @Override
    public void writeTo(OutputStreamWriter writer) throws IOException {
        this.writeBoxHeader(writer);
        writer.writeUnsignedInt16(targetOlsIndex);
    }

    @Override
    public String getFullName() {
        return "TargetOlsProperty";
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("target_ols_index=");
        sb.append(targetOlsIndex);
        return sb.toString();
    }
}
