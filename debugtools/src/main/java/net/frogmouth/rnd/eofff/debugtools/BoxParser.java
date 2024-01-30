package net.frogmouth.rnd.eofff.debugtools;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FileParser;

class BoxParser {

    private final List<Box> boxes = new ArrayList<>();

    public BoxParser(Path file) throws IOException {
        boxes.addAll(parseFile(file));
    }

    private List<Box> parseFile(Path inputPath) throws IOException {
        FileParser fileParser = new FileParser();
        return fileParser.parse(inputPath);
    }

    public void dumpBoxes() {
        for (Box box : boxes) {
            System.out.println(box.toString(0));
        }
    }
}
