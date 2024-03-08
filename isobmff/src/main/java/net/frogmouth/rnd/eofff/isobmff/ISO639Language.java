package net.frogmouth.rnd.eofff.isobmff;

import java.nio.charset.StandardCharsets;

/**
 * ISO 639-2/T Language code.
 *
 * <p>This is a packed representation used in ISO/IEC 14496-12, and borrowed for other uses (e.g.
 * 3GPP).
 */
public class ISO639Language {

    private final String language;

    public static final int BYTES = Short.BYTES;

    /**
     * Constructor.
     *
     * @param langCode three character language code
     */
    public ISO639Language(String langCode) {
        this.language = langCode;
    }

    public String getLanguage() {
        return language;
    }

    public static ISO639Language readPackedLanguageCode(ParseContext parseContext) {
        int packedLanguageBits = parseContext.readUnsignedInt16();
        byte char0 = (byte) (((packedLanguageBits >> 10) & 0x001F) + 0x60);
        byte char1 = (byte) (((packedLanguageBits >> 5) & 0x001F) + 0x60);
        byte char2 = (byte) ((packedLanguageBits & 0x001F) + 0x60);
        String language = new String(new byte[] {char0, char1, char2}, StandardCharsets.US_ASCII);
        return new ISO639Language(language);
    }

    public short asPackedBytes() {
        byte[] languageBytes = language.getBytes(StandardCharsets.US_ASCII);
        short packedLanguage = (short) (languageBytes[2] - 0x60);
        packedLanguage |= (short) (languageBytes[1] - 0x60) << 5;
        packedLanguage |= (short) (languageBytes[0] - 0x60) << 10;
        return packedLanguage;
    }

    @Override
    public String toString() {
        return language;
    }
}
