package net.frogmouth.rnd.eofff.isobmff.meta;

import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public class ItemReferenceBoxParser extends FullBoxParser {

    public ItemReferenceBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return new FourCC("iref");
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        ItemReferenceBox box = new ItemReferenceBox(boxSize, boxName);
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            // TODO: LOG
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        while (parseContext.hasRemainingUntil(initialOffset + boxSize)) {
            long refBoxSize = parseContext.readUnsignedInt32();
            FourCC refBoxName = parseContext.readFourCC();
            SingleItemReferenceBox refBox = new SingleItemReferenceBox(refBoxSize, refBoxName);
            if (version == 0x00) {
                refBox.setFromItemId(parseContext.readUnsignedInt16());
                int refCount = parseContext.readUnsignedInt16();
                for (int i = 0; i < refCount; i++) {
                    refBox.addReference(parseContext.readUnsignedInt16());
                }
            } else if (version == 0x01) {
                refBox.setFromItemId(parseContext.readUnsignedInt32());
                int refCount = parseContext.readUnsignedInt16();
                for (int i = 0; i < refCount; i++) {
                    refBox.addReference(parseContext.readUnsignedInt32());
                }
            }
            box.addItem(refBox);
        }
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return ((version == 0x00) || (version == 0x01));
    }
}
