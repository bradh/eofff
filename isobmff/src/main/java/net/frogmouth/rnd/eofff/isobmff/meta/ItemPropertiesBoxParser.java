package net.frogmouth.rnd.eofff.isobmff.meta;

import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.meta.propertyassociation.ItemPropertyAssociation;

public class ItemPropertiesBoxParser extends FullBoxParser {

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
            System.out.println(
                    "Err, wrong box type, expecting ItemPropertyContainerBox: "
                            + propertiesBox.getFourCC().toString());
            return box;
        }
        box.setItemProperties((ItemPropertyContainerBox) propertiesBox);
        while (parseContext.hasRemainingUntil(initialOffset + boxSize)) {
            Box associationBox = parseContext.parseBox();
            if (!(associationBox instanceof ItemPropertyAssociation)) {
                System.out.println(
                        "Err, wrong box type, expecting ItemPropertyAssociation: "
                                + associationBox.getFourCC().toString());
                return box;
            }
            box.addItemPropertyAssociation((ItemPropertyAssociation) associationBox);
        }
        return box;
    }
}
