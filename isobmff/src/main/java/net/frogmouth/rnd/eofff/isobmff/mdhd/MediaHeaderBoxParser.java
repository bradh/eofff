package net.frogmouth.rnd.eofff.isobmff.mdhd;

import java.nio.charset.StandardCharsets;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MediaHeaderBoxParser extends FullBoxParser {

    private static final Logger LOG = LoggerFactory.getLogger(MediaHeaderBoxParser.class);

    public MediaHeaderBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return new FourCC("mdhd");
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        MediaHeaderBox box = new MediaHeaderBox(boxName);
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        if (version == 1) {
            box.setCreationTime(parseContext.readUnsignedInt64());
            box.setModificationTime(parseContext.readUnsignedInt64());
            box.setTimescale(parseContext.readUnsignedInt32());
            box.setDuration(parseContext.readUnsignedInt64());
        } else { // version == 0
            box.setCreationTime(parseContext.readUnsignedInt32());
            box.setModificationTime(parseContext.readUnsignedInt32());
            box.setTimescale(parseContext.readUnsignedInt32());
            box.setDuration(parseContext.readUnsignedInt32());
        }
        int packedLanguageBits = parseContext.readUnsignedInt16();
        byte char0 = (byte) (((packedLanguageBits >> 10) & 0x001F) + 0x60);
        byte char1 = (byte) (((packedLanguageBits >> 5) & 0x001F) + 0x60);
        byte char2 = (byte) ((packedLanguageBits & 0x001F) + 0x60);
        String language = new String(new byte[] {char0, char1, char2}, StandardCharsets.US_ASCII);
        box.setLanguage(language);
        parseContext.readUnsignedInt16();
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return ((version == 0x00) || (version == 0x01));
    }
}
