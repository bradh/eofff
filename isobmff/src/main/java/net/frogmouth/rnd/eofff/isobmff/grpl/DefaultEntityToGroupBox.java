package net.frogmouth.rnd.eofff.isobmff.grpl;

import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class DefaultEntityToGroupBox extends AbstractEntityToGroupBox implements EntityToGroup {

    public DefaultEntityToGroupBox(FourCC name) {
        super(name);
    }

    @Override
    public String getFullName() {
        return "Unimplemented EntityToGroupBox";
    }
}
