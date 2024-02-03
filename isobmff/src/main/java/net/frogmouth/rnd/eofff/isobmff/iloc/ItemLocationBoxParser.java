package net.frogmouth.rnd.eofff.isobmff.iloc;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class ItemLocationBoxParser extends FullBoxParser {
    private static final Logger LOG = LoggerFactory.getLogger(ItemLocationBoxParser.class);

    public ItemLocationBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return ItemLocationBox.ILOC_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        ItemLocationBox box = new ItemLocationBox();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        int sizes = parseContext.readUnsignedInt16();
        box.setOffsetSize((sizes & 0x00F000) >> 12);
        box.setLengthSize((sizes & 0x000F00) >> 8);
        box.setBaseOffsetSize((sizes & 0x000000F0) >> 4);
        if ((version == 0x01) || (version == 0x02)) {
            box.setIndexSize(sizes & 0x0F);
        } else {
            box.setIndexSize(0);
        }
        long itemCount;
        if (version < 0x02) {
            itemCount = parseContext.readUnsignedInt16();
        } else {
            itemCount = parseContext.readUnsignedInt32();
        }
        for (long i = 0; i < itemCount; i++) {
            ILocItem item = new ILocItem();
            if (version < 0x02) {
                item.setItemId(parseContext.readUnsignedInt16());
            } else if (version == 0x02) {
                item.setItemId(parseContext.readUnsignedInt32());
            }
            if ((version == 0x01) || (version == 0x02)) {
                int temp = parseContext.readUnsignedInt16();
                int constructionMethod = temp & 0x000F;
                item.setConstructionMethod(constructionMethod);
            }
            item.setDataReferenceIndex(parseContext.readUnsignedInt16());
            item.setBaseOffset(parseContext.readUnsignedInt(box.getBaseOffsetSize() * 8));
            int extentCount = parseContext.readUnsignedInt16();
            for (int j = 0; j < extentCount; j++) {
                ILocExtent extent = new ILocExtent();
                if (((version == 0x01) || (version == 0x02)) && (box.getIndexSize() > 0)) {
                    extent.setExtentIndex(parseContext.readUnsignedInt(box.getIndexSize() * 8));
                }
                extent.setExtentOffset(parseContext.readUnsignedInt(box.getOffsetSize() * 8));
                extent.setExtentLength(parseContext.readUnsignedInt(box.getLengthSize() * 8));
                item.addExtent(extent);
            }
            box.addItem(item);
        }
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return ((version == 0x00) || (version == 0x01) || (version == 0x02));
    }
}
