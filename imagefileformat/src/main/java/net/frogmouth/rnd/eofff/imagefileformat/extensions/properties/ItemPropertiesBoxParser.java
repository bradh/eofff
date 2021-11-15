package net.frogmouth.rnd.eofff.imagefileformat.extensions.properties;

import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ItemPropertiesBoxParser extends FullBoxParser {
    private static final Logger LOG = LoggerFactory.getLogger(ItemPropertiesBoxParser.class);

    public ItemPropertiesBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return new FourCC("iprp");
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        ItemPropertiesBox box = new ItemPropertiesBox(boxSize, boxName);
        Box propertiesBox = parseContext.parseBox();
        if (!(propertiesBox instanceof ItemPropertyContainerBox)) {
            LOG.error(
                    "Err, wrong box type, expecting ItemPropertyContainerBox, but got {}",
                    propertiesBox.getFourCC().toString());
            return box;
        }
        box.setItemProperties((ItemPropertyContainerBox) propertiesBox);
        while (parseContext.hasRemainingUntil(initialOffset + boxSize)) {
            Box associationBox = parseContext.parseBox();
            if (!(associationBox instanceof ItemPropertyAssociation)) {
                LOG.error(
                        "Err, wrong box type, expecting ItemPropertyAssociation, but got {}",
                        associationBox.getFourCC().toString());
                return box;
            }
            box.addItemPropertyAssociation((ItemPropertyAssociation) associationBox);
        }
        return box;
    }
}
