package net.frogmouth.rnd.eofff.isobmff.meta;

import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public class ItemInfoBoxParser extends FullBoxParser {

    public ItemInfoBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return new FourCC("iinf");
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        ItemInfoBox box = new ItemInfoBox(boxSize, boxName);
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            // TODO: LOG
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        long itemCount;
        if (version == 0x00) {
            itemCount = parseContext.readUnsignedInt16();
        } else {
            itemCount = parseContext.readUnsignedInt32();
        }
        for (long i = 0; i < itemCount; i++) {
            ItemInfoEntry entry = parseItemInfoEntry(parseContext);
            box.addItem(entry);
        }
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return ((version == 0x00) || (version == 0x01) || (version == 0x02) || (version == 0x03));
    }

    private ItemInfoEntry parseItemInfoEntry(ParseContext parseContext) {
        Box box = parseContext.parseBox();
        if (!(box instanceof ItemInfoEntry)) {
            System.out.println("need to parse " + box.getFourCC().toString());
            return null;
        }
        ItemInfoEntry entryBox = (ItemInfoEntry) box;
        return entryBox;
    }
}
