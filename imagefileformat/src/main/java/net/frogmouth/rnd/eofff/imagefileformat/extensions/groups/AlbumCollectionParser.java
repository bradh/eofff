package net.frogmouth.rnd.eofff.imagefileformat.extensions.groups;

import static net.frogmouth.rnd.eofff.imagefileformat.extensions.groups.AlbumCollection.ALBC_ATOM;

import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public class AlbumCollectionParser extends AbstractEntityToGroupBoxParser {

    public AlbumCollectionParser() {}

    @Override
    public FourCC getFourCC() {
        return ALBC_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        AlbumCollection box = new AlbumCollection();
        return parseEntityToGroupBox(parseContext, box, initialOffset, boxSize, boxName);
    }
}
