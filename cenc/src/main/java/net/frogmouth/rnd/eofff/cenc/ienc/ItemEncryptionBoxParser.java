package net.frogmouth.rnd.eofff.cenc.ienc;

import static net.frogmouth.rnd.eofff.cenc.CommonEncryptionConstants.KEY_IDENTIFIER_BYTES;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.iprp.AbstractItemProperty;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemFullPropertyParser;
import net.frogmouth.rnd.eofff.isobmff.iprp.PropertyParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.iprp.PropertyParser.class)
public class ItemEncryptionBoxParser extends ItemFullPropertyParser implements PropertyParser {

    private static final Logger LOG = LoggerFactory.getLogger(ItemEncryptionBoxParser.class);

    public ItemEncryptionBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return ItemEncryptionBox.IENC_ATOM;
    }

    @Override
    public AbstractItemProperty parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        ItemEncryptionBox box = new ItemEncryptionBox();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return null;
        }
        box.setFlags(parseFlags(parseContext));
        parseContext.skipBytes(1); // reserved
        if (version == 0) {
            parseContext.skipBytes(1); // reserved
            box.setCryptByteBlock(0);
            box.setSkipByteBlock(0);
        } else {
            int temp = parseContext.readUnsignedInt8();
            box.setCryptByteBlock((temp >> 4) & 0x0F);
            box.setSkipByteBlock(temp & 0x0F);
        }
        int numKeys = parseContext.readUnsignedInt8();
        for (int i = 0; i < numKeys; i++) {
            ItemEncryptionEntry entry = new ItemEncryptionEntry();
            entry.setPerSampleIVSize(parseContext.readUnsignedInt8());
            entry.setKeyIdentifier(parseContext.getBytes(KEY_IDENTIFIER_BYTES));
            if (entry.getPerSampleIVSize() == 0) {
                int constantIVSize = parseContext.readUnsignedInt8();
                entry.setConstantIV(parseContext.getBytes(constantIVSize));
            }
            box.addEntry(entry);
        }
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return ((version == 0x00) || (version == 0x01));
    }
}
