package net.frogmouth.rnd.eofff.isobmff.elng;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Extended Language Box.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.4.6.
 */
public class ExtendedLanguageBox extends FullBox {

    private String language;
    public static final FourCC ELNG_ATOM = new FourCC("elng");

    public ExtendedLanguageBox() {
        super(ELNG_ATOM);
    }

    @Override
    public String getFullName() {
        return "ExtendedLanguageBox";
    }

    @Override
    public long getBodySize() {
        return language.getBytes(StandardCharsets.UTF_8).length;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writeNullTerminatedString(language);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("extended_language=");
        sb.append(this.language);
        return sb.toString();
    }
}
