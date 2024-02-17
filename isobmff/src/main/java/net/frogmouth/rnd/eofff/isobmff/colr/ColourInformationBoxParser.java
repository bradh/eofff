package net.frogmouth.rnd.eofff.isobmff.colr;

import static net.frogmouth.rnd.eofff.isobmff.colr.ColourInformationBox.COLR_ATOM;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.BoxParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class ColourInformationBoxParser extends BoxParser {

    public ColourInformationBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return COLR_ATOM;
    }

    @Override
    public ColourInformationBox parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        ColourInformationBox box = new ColourInformationBox();
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
