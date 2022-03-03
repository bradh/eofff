package net.frogmouth.rnd.eofff.imagefileformat.extensions.properties;

import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ItemPropertyAssociationParser extends FullBoxParser {
    private static final Logger LOG = LoggerFactory.getLogger(ItemPropertyAssociationParser.class);

    public ItemPropertyAssociationParser() {}

    @Override
    public FourCC getFourCC() {
        return new FourCC("ipma");
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        ItemPropertyAssociation box = new ItemPropertyAssociation(boxSize, boxName);
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        long entryCount = parseContext.readUnsignedInt32();
        for (int i = 0; i < entryCount; ++i) {
            AssociationEntry entry = new AssociationEntry();
            if (version < 1) {
                entry.setItemId(parseContext.readUnsignedInt16());
            } else {
                entry.setItemId(parseContext.readUnsignedInt32());
            }
            int associationCount = parseContext.readUnsignedInt8();
            for (int j = 0; j < associationCount; j++) {
                PropertyAssociation association = new PropertyAssociation();
                int propertyIndex;
                boolean essential;
                if (box.isFlagSet(0x01)) {
                    int value = parseContext.readUnsignedInt16();
                    essential = ((value & 0x8000) == 0x8000);
                    propertyIndex = value & 0x7FFF;
                } else {
                    int value = parseContext.readUnsignedInt8();
                    essential = ((value & 0x80) == 0x80);
                    propertyIndex = value & 0x7F;
                }
                association.setEssential(essential);
                association.setPropertyIndex(propertyIndex);
                entry.addAssociation(association);
            }
            box.addEntry(entry);
        }
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return ((version == 0x00) || (version == 0x01));
    }
}
