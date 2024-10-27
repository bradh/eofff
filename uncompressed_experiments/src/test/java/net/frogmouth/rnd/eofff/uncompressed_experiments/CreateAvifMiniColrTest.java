package net.frogmouth.rnd.eofff.uncompressed_experiments;

import static org.testng.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.av1isobmff.av1C.AV1CodecConfigurationBox;
import net.frogmouth.rnd.eofff.imagefileformat.mini.MinimizedImageBox;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FileParser;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.ftyp.Brand;
import net.frogmouth.rnd.eofff.isobmff.ftyp.FileTypeBox;
import net.frogmouth.rnd.eofff.isobmff.iprp.AbstractItemProperty;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemPropertiesBox;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemPropertyContainerBox;
import net.frogmouth.rnd.eofff.isobmff.mdat.MediaDataBox;
import net.frogmouth.rnd.eofff.isobmff.meta.MetaBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

public class CreateAvifMiniColrTest extends GIMIValidator {

    private static final Logger LOG = LoggerFactory.getLogger(CreateAvifMiniColrTest.class);
    private MediaDataBox mdat;
    private AV1CodecConfigurationBox av1C;
    private final List<Box> sourceBoxes;

    public CreateAvifMiniColrTest() throws IOException, InterruptedException, URISyntaxException {
        {
            FileParser fileParser = new FileParser();
            sourceBoxes =
                    fileParser.parse(
                            Path.of(
                                    CreateAvifMiniColrTest.class
                                            .getResource("/lightning128x128_colr.avif")
                                            .toURI()));
            for (Box box : sourceBoxes) {
                if (box instanceof MetaBox meta) {
                    for (Box metaChild : meta.getNestedBoxes()) {
                        if (metaChild instanceof ItemPropertiesBox iprp) {
                            ItemPropertyContainerBox ipco = iprp.getItemProperties();
                            for (AbstractItemProperty prop : ipco.getProperties()) {
                                if (prop instanceof AV1CodecConfigurationBox av1Csource) {
                                    this.av1C = av1Csource;
                                }
                            }
                        }
                    }
                }
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
        writeBoxes(boxes, "lightning_mini_colr.avif");
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

    private MinimizedImageBox createMiniBox() throws IOException {
        MinimizedImageBox mini = new MinimizedImageBox();
        mini.setFullRangeFlag(true);
        if (av1C.isChroma_subsampling_y()) {
            mini.setChromaSubsampling(MinimizedImageBox.ChromaSubsampling.HorizontalAndVertical);
        } else if (av1C.isChroma_subsampling_x()) {
            mini.setChromaSubsampling(MinimizedImageBox.ChromaSubsampling.Horizontal);
        } else {
            mini.setChromaSubsampling(MinimizedImageBox.ChromaSubsampling.None);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        this.av1C.writeBodyTo(streamWriter);
        mini.setMainItemCodecConfig(baos.toByteArray());
        mini.setMainItemData(mdat.getData());
        return mini;
    }
}
