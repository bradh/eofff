package net.frogmouth.rnd.eofff.imagefileformat.extensions.properties;

import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.BoxParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ItemPropertyContainerBoxParser extends BoxParser {
    private static final Logger LOG = LoggerFactory.getLogger(ItemPropertyContainerBoxParser.class);

    public ItemPropertyContainerBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return new FourCC("ipco");
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        ItemPropertyContainerBox box = new ItemPropertyContainerBox(boxSize, boxName);
        while (parseContext.hasRemainingUntil(initialOffset + boxSize)) {
            long initialPropertyPosition = parseContext.getCursorPosition();
            long propertyBoxSize = parseContext.readUnsignedInt32();
            FourCC propertyBoxName = parseContext.readFourCC();
            LOG.trace("Got property: {}", propertyBoxName);
            PropertyParser parser = PropertyFactoryManager.getParser(propertyBoxName);
            box.addProperty(
                    parser.parse(
                            parseContext,
                            initialPropertyPosition,
                            propertyBoxSize,
                            propertyBoxName));
        }
        return box;
    }
}
