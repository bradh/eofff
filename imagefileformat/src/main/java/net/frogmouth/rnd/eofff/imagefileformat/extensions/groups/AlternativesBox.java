package net.frogmouth.rnd.eofff.imagefileformat.extensions.groups;

import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class AlternativesBox extends AbstractEntityToGroupBox {

    public AlternativesBox(long size, FourCC name) {
        super(size, name);
    }

    @Override
    public String getFullName() {
        return "Alternatives";
    }
}
