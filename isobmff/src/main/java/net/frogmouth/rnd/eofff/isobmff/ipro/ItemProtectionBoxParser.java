package net.frogmouth.rnd.eofff.isobmff.ipro;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.sinf.ProtectionSchemeInfoBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class ItemProtectionBoxParser extends FullBoxParser {
    private static final Logger LOG = LoggerFactory.getLogger(ItemProtectionBoxParser.class);

    public ItemProtectionBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return ItemProtectionBox.IPRO_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        ItemProtectionBox box = new ItemProtectionBox();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        int protectionCount = parseContext.readUnsignedInt16();
        for (int i = 0; i < protectionCount; i++) {
            Box maybeSinf = parseContext.parseBox();
            if (maybeSinf instanceof ProtectionSchemeInfoBox sinf) {
                box.appendProtectionSchemeInfoBox(sinf);
            } else if (maybeSinf == null) {
                LOG.warn("Got null box instead of ProtectionSchemeInfoBox");
            } else {
                LOG.warn(
                        "Expected ProtectionSchemeInfoBox, got "
                                + maybeSinf.getFullName()
                                + " instead.");
            }
        }
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return (version == 0x00);
    }
}
