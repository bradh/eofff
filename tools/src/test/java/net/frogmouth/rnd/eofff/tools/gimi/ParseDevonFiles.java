package net.frogmouth.rnd.eofff.tools.gimi;

import static org.testng.Assert.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FileParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ftyp.FileTypeBox;
import org.testng.annotations.Test;

public class ParseDevonFiles {

    public ParseDevonFiles() {}

    public List<Box> parseFile() throws IOException {
        Path testFile =
                Paths.get(
                        "/home/bradh/code-sprint-22-ngiis/GIMI Test Images/gimi_1_sentinel_hevc.heif");
        FileParser fileParser = new FileParser();
        return fileParser.parse(testFile);
    }

    @Test
    public void checkBoxes() throws IOException {
        List<Box> sourceBoxes = new ArrayList<>();
        sourceBoxes.addAll(parseFile());
        FileTypeBox ftyp = (FileTypeBox) getTopLevelBoxByFourCC(sourceBoxes, "ftyp");
        System.out.println(ftyp);
    }

    private static Box getTopLevelBoxByFourCC(List<Box> boxes, String fourCC) {
        for (Box box : boxes) {
            if (box.getFourCC().equals(new FourCC(fourCC))) {
                return box;
            }
        }
        return null;
    }
}
