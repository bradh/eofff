package net.frogmouth.rnd.eofff.imagefileformat.extensions.groups;

import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class AlternativesBox extends AbstractEntityToGroupBox {

    public AlternativesBox(FourCC name) {
        super(name);
    }

    @Override
    public String getFullName() {
        return "Alternatives";
    }
}
