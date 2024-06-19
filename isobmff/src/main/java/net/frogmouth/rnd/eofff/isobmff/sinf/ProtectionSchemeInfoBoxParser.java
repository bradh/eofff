package net.frogmouth.rnd.eofff.isobmff.sinf;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.BaseBoxParser;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class ProtectionSchemeInfoBoxParser extends BaseBoxParser {
    public ProtectionSchemeInfoBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return ProtectionSchemeInfoBox.SINF_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        ProtectionSchemeInfoBox box = new ProtectionSchemeInfoBox();
        box.addNestedBoxes(parseContext.parseNestedBoxes(initialOffset + boxSize));
        return box;
    }
}
