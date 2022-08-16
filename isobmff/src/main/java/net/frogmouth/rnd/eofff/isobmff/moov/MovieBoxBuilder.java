package net.frogmouth.rnd.eofff.isobmff.moov;

import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBoxBuilder;

public class MovieBoxBuilder extends AbstractContainerBoxBuilder<MovieBox> {

    public MovieBoxBuilder() {}

    @Override
    public MovieBox build() {
        MovieBox box = new MovieBox();
        box.addNestedBoxes(nestedBoxes);
        return box;
    }
}
