package net.frogmouth.rnd.eofff.gopro;

import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.BaseBoxParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public class MUIDParser extends BaseBoxParser {

    private static final long BYTES_IN_BOX_HEADER = Integer.BYTES + FourCC.BYTES;

    public MUIDParser() {}

    @Override
    public FourCC getFourCC() {
        return MUID.MUID_ATOM;
    }

    @Override
    public MUID parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        MUID box = new MUID();
        List<Long> values = new ArrayList<>();
        while (parseContext.hasRemainingUntil(initialOffset + boxSize)) {
            values.add(parseContext.readUnsignedInt32());
        }
        box.setValues(values);
        return box;
    }
}
