package net.frogmouth.rnd.eofff.imagefileformat.properties.colr;

import static net.frogmouth.rnd.eofff.imagefileformat.properties.colr.ColourInformationProperty.COLR_ATOM;

import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.AbstractItemProperty;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.PropertyParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ColourInformationPropertyParser implements PropertyParser {

    private static final Logger LOG =
            LoggerFactory.getLogger(ColourInformationPropertyParser.class);

    public ColourInformationPropertyParser() {}

    @Override
    public FourCC getFourCC() {
        return COLR_ATOM;
    }

    @Override
    public AbstractItemProperty parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        ColourInformationProperty box = new ColourInformationProperty();
        box.setColourType(parseContext.readFourCC());
        if (box.getColourType().equals(new FourCC("nclx"))) {
            box.setColourPrimaries((short) parseContext.readUnsignedInt16());
            box.setTransferCharacteristics((short) parseContext.readUnsignedInt16());
            box.setMatrixCoefficients((short) parseContext.readUnsignedInt16());
            var flag = parseContext.readByte();
            box.setFullRange(((flag & 0x80) == 0x80));
        } else {
            throw new UnsupportedOperationException(
                    "need to parse: " + box.getColourType().toString());
        }
        return box;
    }
}
