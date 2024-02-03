package net.frogmouth.rnd.eofff.ts26_244.auth;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.ISO639Language;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Author Box.
 *
 * <p>See ETSI TS 126 244 V15.0.0 (2018-07) Section 8.2. Note that 3GPP TS 26.244 is identical.
 */
public class AuthorBox extends FullBox {

    private ISO639Language language;
    private String author;
    public static final FourCC AUTH_ATOM = new FourCC("auth");

    public AuthorBox() {
        super(AUTH_ATOM);
    }

    @Override
    public String getFullName() {
        return "Author box";
    }

    @Override
    public long getBodySize() {
        long size = 0;
        size += ISO639Language.BYTES;
        size += author.getBytes(StandardCharsets.UTF_8).length;
        return size;
    }

    public ISO639Language getLanguage() {
        return language;
    }

    public void setLanguage(ISO639Language language) {
        this.language = language;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writePackedLanguageCode(language);
        stream.writeNullTerminatedString(author);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("language: ");
        sb.append(language.toString());
        sb.append(", author: ");
        sb.append(author);
        return sb.toString();
    }
}
