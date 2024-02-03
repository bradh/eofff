package net.frogmouth.rnd.eofff.isobmff.infe;

import static net.frogmouth.rnd.eofff.isobmff.infe.ItemInfoEntry.MIME;
import static net.frogmouth.rnd.eofff.isobmff.infe.ItemInfoEntry.URI;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class ItemInfoEntryParser extends FullBoxParser {
    private static final Logger LOG = LoggerFactory.getLogger(ItemInfoEntryParser.class);

    public ItemInfoEntryParser() {}

    @Override
    public FourCC getFourCC() {
        return ItemInfoEntry.INFE_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        ItemInfoEntry box = new ItemInfoEntry();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        if ((version == 0x00) || (version == 0x01)) {
            box.setItemID(parseContext.readUnsignedInt16());
            box.setItemProtectionIndex(parseContext.readUnsignedInt16());
            box.setItemName(parseContext.readNullDelimitedString(boxSize));
            box.setContentType(parseContext.readNullDelimitedString(boxSize));
            box.setContentEncoding(parseContext.readNullDelimitedString(boxSize));
        }
        if (version == 1) {
            long extensionType = parseContext.readUnsignedInt32();
        }
        if (version >= 0x02) {
            if (version == 0x02) {
                box.setItemID(parseContext.readUnsignedInt16());
            } else if (version == 0x03) {
                box.setItemID(parseContext.readUnsignedInt32());
            }
            box.setItemProtectionIndex(parseContext.readUnsignedInt16());
            box.setItemType(parseContext.readUnsignedInt32());
            box.setItemName(parseContext.readNullDelimitedString(boxSize));
            if (box.getItemType() == MIME) {
                box.setContentType(parseContext.readNullDelimitedString(boxSize));
                box.setContentEncoding(parseContext.readNullDelimitedString(boxSize));
            } else if (box.getItemType() == URI) {
                box.setItemUriType(parseContext.readNullDelimitedString(boxSize));
            }
        }

        return box;
    }

    private boolean isSupportedVersion(int version) {
        return ((version == 0x00) || (version == 0x01) || (version == 0x02) || (version == 0x03));
    }
}
