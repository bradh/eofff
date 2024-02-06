package net.frogmouth.rnd.eofff.isobmff.cprt;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.ISO639Language;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Copyright Box.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.10.2
 */
public class CopyrightBox extends FullBox {

    private ISO639Language language;
    private String notice;
    public static final FourCC CPRT_ATOM = new FourCC("cprt");

    public CopyrightBox() {
        super(CPRT_ATOM);
    }

    @Override
    public String getFullName() {
        return "CopyrightBox";
    }

    @Override
    public long getBodySize() {
        long size = 0;
        size += ISO639Language.BYTES;
        size += notice.getBytes(StandardCharsets.UTF_8).length;
        return size;
    }

    public ISO639Language getLanguage() {
        return language;
    }

    public void setLanguage(ISO639Language language) {
        this.language = language;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writePackedLanguageCode(language);
        stream.writeNullTerminatedString(notice);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("language: ");
        sb.append(language.toString());
        sb.append(", notice: ");
        sb.append(notice);
        return sb.toString();
    }
}
