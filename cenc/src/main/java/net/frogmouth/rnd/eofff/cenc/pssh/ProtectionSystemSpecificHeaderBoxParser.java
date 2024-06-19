package net.frogmouth.rnd.eofff.cenc.pssh;

import static net.frogmouth.rnd.eofff.cenc.CommonEncryptionConstants.KEY_IDENTIFIER_BYTES;

import com.google.auto.service.AutoService;
import java.util.UUID;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class ProtectionSystemSpecificHeaderBoxParser extends FullBoxParser {
    private static final Logger LOG =
            LoggerFactory.getLogger(ProtectionSystemSpecificHeaderBoxParser.class);

    public ProtectionSystemSpecificHeaderBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return ProtectionSystemSpecificHeaderBox.PSSH_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        ProtectionSystemSpecificHeaderBox box = new ProtectionSystemSpecificHeaderBox();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        UUID systemID = parseContext.readUUID();
        box.setSystemID(systemID);
        if (version > 0) {
            long keyIdentifierCount = parseContext.readUnsignedInt32();
            for (long i = 0; i < keyIdentifierCount; i++) {
                box.addKeyIdentifier(parseContext.getBytes(KEY_IDENTIFIER_BYTES));
            }
        }
        long dataSize = parseContext.readUnsignedInt32();
        box.setData(parseContext.getBytes(dataSize));
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return ((version == 0x00) || (version == 0x01));
    }
}
