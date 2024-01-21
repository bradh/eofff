package net.frogmouth.rnd.eofff.goprotools;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBox;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FileParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.hdlr.HandlerBox;
import net.frogmouth.rnd.eofff.isobmff.mdia.MediaBox;
import net.frogmouth.rnd.eofff.isobmff.moov.MovieBox;
import net.frogmouth.rnd.eofff.isobmff.trak.TrackBox;

class GoProParser {

    private final List<Box> boxes = new ArrayList<>();

    public GoProParser(String filename) throws IOException {
        boxes.addAll(parseFile(filename));
    }

    private List<Box> parseFile(String inputPath) throws IOException {
        Path testFile = Paths.get(inputPath);
        FileParser fileParser = new FileParser();
        return fileParser.parse(testFile);
    }

    public void dumpBoxes() {
        for (Box box : boxes) {
            System.out.println(box.toString(0));
        }
    }

    private static Box findChildBox(AbstractContainerBox parent, FourCC fourCC) {
        if (parent == null) {
            return null;
        }
        for (Box box : parent.getNestedBoxes()) {
            if (box.getFourCC().equals(fourCC)) {
                return box;
            }
        }
        return null;
    }

    private static Box getTopLevelBoxByFourCC(List<Box> boxes, String fourCC) {
        for (Box box : boxes) {
            if (box.getFourCC().equals(new FourCC(fourCC))) {
                return box;
            }
        }
        return null;
    }

    void findMetadataTrack() throws IOException {
        MovieBox moov = (MovieBox) getTopLevelBoxByFourCC(boxes, "moov");
        if (moov == null) {
            throw new IOException("Missing essential moov box");
        }
        for (Box box : moov.getNestedBoxes()) {
            if (box instanceof TrackBox trak) {
                MediaBox mdia = (MediaBox) findChildBox(trak, new FourCC("mdia"));
                if (mdia != null) {
                    HandlerBox hdlr = (HandlerBox) findChildBox(mdia, new FourCC("hdlr"));
                    if (hdlr != null) {
                        if (hdlr.getHandlerType().equals("meta")) {}
                    }
                }
            }
        }
    }
}
