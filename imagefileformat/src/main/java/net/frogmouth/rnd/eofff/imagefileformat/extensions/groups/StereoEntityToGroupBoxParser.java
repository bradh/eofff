package net.frogmouth.rnd.eofff.imagefileformat.extensions.groups;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.grpl.AbstractEntityToGroupBoxParser;
import net.frogmouth.rnd.eofff.isobmff.grpl.EntityToGroup;
import net.frogmouth.rnd.eofff.isobmff.grpl.EntityToGroupParser;

@AutoService(EntityToGroupParser.class)
public class StereoEntityToGroupBoxParser extends AbstractEntityToGroupBoxParser {

    @Override
    public FourCC getFourCC() {
        return StereoEntityToGroupBox.STER_ATOM;
    }

    @Override
    public EntityToGroup parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        StereoEntityToGroupBox ster = new StereoEntityToGroupBox();
        parseEntityToGroupBox(parseContext, ster, initialOffset, boxSize, boxName);
        return ster;
    }
}
