package net.frogmouth.rnd.eofff.imagefileformat.extensions.groups;

import net.frogmouth.rnd.eofff.isobmff.FourCC;

// TODO: move to isobmff module
public class AlternativesBox extends AbstractEntityToGroupBox {

    public static final FourCC ALTR_ATOM = new FourCC("altr");

    public AlternativesBox() {
        super(ALTR_ATOM);
    }

    @Override
    public String getFullName() {
        return "Alternatives";
    }
}
