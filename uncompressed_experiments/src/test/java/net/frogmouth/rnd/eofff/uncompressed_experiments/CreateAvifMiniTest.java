package net.frogmouth.rnd.eofff.uncompressed_experiments;

import static org.testng.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.imagefileformat.mini.MinimizedImageBox;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FileParser;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.ftyp.Brand;
import net.frogmouth.rnd.eofff.isobmff.ftyp.FileTypeBox;
import net.frogmouth.rnd.eofff.isobmff.mdat.MediaDataBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

public class CreateAvifMiniTest extends GIMIValidator {

    private static final Logger LOG = LoggerFactory.getLogger(CreateAvifMiniTest.class);
    private MediaDataBox mdat;
    private final List<Box> sourceBoxes;

    public CreateAvifMiniTest() throws IOException, InterruptedException {
        {
            FileParser fileParser = new FileParser();
            sourceBoxes =
                    fileParser.parse(
                            Path.of("/home/bradh/eofff/uncompressed_experiments/Untitled.avif"));
            for (Box box : sourceBoxes) {
                if (box instanceof MediaDataBox mdatSource) {
                    mdat = mdatSource;
                }
            }
        }
    }

    @Test
    public void writeAVIF() throws IOException {
        List<Box> boxes = new ArrayList<>();
        FileTypeBox ftyp = createFileTypeBox();
        boxes.add(ftyp);

        MinimizedImageBox mini = createMiniBox();
        boxes.add(mini);
        writeBoxes(boxes, "mini.avif");
    }

    private void writeBoxes(List<Box> boxes, String outputPathName) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        for (Box box : boxes) {
            box.writeTo(streamWriter);
        }
        File testOut = new File(outputPathName);
        Files.write(
                testOut.toPath(),
                baos.toByteArray(),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);
    }

    private FileTypeBox createFileTypeBox() {
        FileTypeBox fileTypeBox = new FileTypeBox();
        fileTypeBox.setMajorBrand(new Brand("mif3"));
        fileTypeBox.setMinorVersion((int) new Brand("avif").asUnsigned());
        return fileTypeBox;
    }

    private MinimizedImageBox createMiniBox() {
        MinimizedImageBox mini = new MinimizedImageBox();
        // TODO
        mini.setChromaSubsampling(MinimizedImageBox.ChromaSubsampling.None);
        mini.setMainItemCodecConfig(new byte[0]);
        mini.setMainItemData(mdat.getData());
        return mini;
    }
}
