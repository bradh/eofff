package net.frogmouth.rnd.eofff.isobmff.moov;

import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class MovieBoxBuilder extends AbstractContainerBoxBuilder<MovieBox> {

    public MovieBoxBuilder() {}

    @Override
    public MovieBox build() {
        int size = getBoxSize();
        MovieBox box = new MovieBox(size, new FourCC("moov"));
        box.addNestedBoxes(nestedBoxes);
        return box;
    }
}
