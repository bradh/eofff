package net.frogmouth.rnd.eofff.isobmff.iref;

import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.meta.SingleItemReferenceBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ItemReferenceBoxParser extends FullBoxParser {
    private static final Logger LOG = LoggerFactory.getLogger(ItemReferenceBoxParser.class);

    public ItemReferenceBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return ItemReferenceBox.IREF_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        ItemReferenceBox box = new ItemReferenceBox();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        while (parseContext.hasRemainingUntil(initialOffset + boxSize)) {
            long refBoxSize = parseContext.readUnsignedInt32();
            FourCC refBoxName = parseContext.readFourCC();
            SingleItemReferenceBox refBox = new SingleItemReferenceBox(refBoxName);
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
