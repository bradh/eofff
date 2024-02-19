package net.frogmouth.rnd.eofff.isobmff.grpl;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

@AutoService(EntityToGroupParser.class)
public class AlternativesEntityToGroupBoxParser extends AbstractEntityToGroupBoxParser {

    @Override
    public FourCC getFourCC() {
        return AlternativesEntityToGroupBox.ALTR;
    }

    @Override
    public EntityToGroup parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        AlternativesEntityToGroupBox altr = new AlternativesEntityToGroupBox();
        parseEntityToGroupBox(parseContext, altr, initialOffset, boxSize, boxName);
        return altr;
    }
}
