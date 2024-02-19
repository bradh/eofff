package net.frogmouth.rnd.eofff.isobmff.grpl;

import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class AlternativesEntityToGroupBox extends AbstractEntityToGroupBox
        implements EntityToGroup {

    public static final FourCC ALTR = new FourCC("altr");

    public AlternativesEntityToGroupBox() {
        super(ALTR);
    }

    @Override
    public String getFullName() {
        return "Alternatives";
    }
}
