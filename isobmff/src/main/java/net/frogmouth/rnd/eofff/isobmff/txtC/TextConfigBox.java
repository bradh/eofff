package net.frogmouth.rnd.eofff.isobmff.txtC;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Text Configuration Box (txtC).
 *
 * <p>See ISO/IEC 14496-12:2022 Section 12.3.3.
 */
public class TextConfigBox extends FullBox {
    public static final FourCC TXTC_ATOM = new FourCC("txtC");
    private String textConfig;

    public TextConfigBox() {
        super(TXTC_ATOM);
    }

    @Override
    public String getFullName() {
        return "TextConfigBox";
    }

    @Override
    public long getBodySize() {
        long size = 0;
        size += textConfig.getBytes(StandardCharsets.UTF_8).length;
        size += 1;
        return size;
    }

    public String getTextConfig() {
        return textConfig;
    }

    public void setTextConfig(String textConfig) {
        this.textConfig = textConfig;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writeNullTerminatedString(textConfig);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("text_config=");
        sb.append(textConfig);
        return sb.toString();
    }
}
