package net.frogmouth.rnd.eofff.cenc.tenc;

import static net.frogmouth.rnd.eofff.cenc.CommonEncryptionConstants.KEY_IDENTIFIER_BYTES;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class TrackEncryptionBoxParser extends FullBoxParser {
    private static final Logger LOG = LoggerFactory.getLogger(TrackEncryptionBoxParser.class);

    public TrackEncryptionBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return TrackEncryptionBox.TENC_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        TrackEncryptionBox box = new TrackEncryptionBox();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        parseContext.skipBytes(1);
        if (version == 0) {
            parseContext.skipBytes(1);
        } else {
            int v = parseContext.readUnsignedInt8();
            box.setDefaultCryptByteBlock(v >> 4);
            box.setDefaultSkipByteBlock(v & 0x0F);
        }
        box.setDefaultIsProtected(parseContext.readUnsignedInt8());
        box.setDefaultPerSampleIVSize(parseContext.readUnsignedInt8());
        box.setDefaultKeyIdentifier(parseContext.getBytes(KEY_IDENTIFIER_BYTES));
        if ((box.getDefaultIsProtected() == 1) && (box.getDefaultPerSampleIVSize() == 0)) {
            int defaultConstantIVSize = parseContext.readUnsignedInt8();
            box.setDefaultConstantIV(parseContext.getBytes(defaultConstantIVSize));
        }
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return ((version == 0x00) || (version == 0x01));
    }
}
