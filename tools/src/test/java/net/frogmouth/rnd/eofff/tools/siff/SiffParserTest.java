package net.frogmouth.rnd.eofff.tools.siff;

import static org.testng.Assert.*;

import java.awt.Point;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.PixelInterleavedSampleModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.AbstractItemProperty;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemPropertiesBox;
import net.frogmouth.rnd.eofff.imagefileformat.properties.image.ImageSpatialExtentsProperty;
import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBox;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FileParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ftyp.FileTypeBox;
import net.frogmouth.rnd.eofff.isobmff.mdat.MediaDataBox;
import net.frogmouth.rnd.eofff.isobmff.meta.MetaBox;
import net.frogmouth.rnd.eofff.isobmff.pitm.PrimaryItemBox;
import net.frogmouth.rnd.eofff.uncompressed.uncc.UncompressedFrameConfigBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

public class SiffParserTest {
    private static final Logger LOG = LoggerFactory.getLogger(SiffParserTest.class);

    public List<Box> parseFile(String inputPath) throws IOException {
        Path testFile = Paths.get(inputPath);
        FileParser fileParser = new FileParser();
        return fileParser.parse(testFile);
    }

    @Test
    public void parse_rgb3() throws IOException {
        convertToPNG("test_siff_rgb3.mp4", "siff_rgb3.png");
    }

    @Test
    public void parse_rgba() throws IOException {
        convertToPNG("test_siff_rgba.mp4", "siff_rgba.png");
    }

    @Test
    public void parse_abgr() throws IOException {
        convertToPNG("test_siff_abgr.mp4", "siff_abgr.png");
    }

    private void convertToPNG(String inputPath, String outputPath) throws IOException {
        List<Box> boxes = new ArrayList<>();
        boxes.addAll(parseFile(inputPath));
        FileTypeBox ftyp = (FileTypeBox) getTopLevelBoxByFourCC(boxes, "ftyp");
        System.out.println(ftyp);
        MetaBox meta = (MetaBox) getTopLevelBoxByFourCC(boxes, "meta");
        PrimaryItemBox pitm = (PrimaryItemBox) findChildBox(meta, PrimaryItemBox.PITM_ATOM);
        long primaryItemId = pitm.getItemID();
        System.out.println("Primary item: " + primaryItemId);
        ItemPropertiesBox iprp =
                (ItemPropertiesBox) findChildBox(meta, ItemPropertiesBox.IPRP_ATOM);
        List<AbstractItemProperty> properties = iprp.getPropertiesForItem(primaryItemId);
        System.out.println("properties: " + properties);
        int width = 0;
        int height = 0;
        FourCC profile = null;
        for (AbstractItemProperty prop : properties) {
            if (prop instanceof ImageSpatialExtentsProperty ispe) {
                height = (int) ispe.getImageHeight();
                width = (int) ispe.getImageWidth();
            }
            if (prop instanceof UncompressedFrameConfigBox uncC) {
                profile = uncC.getProfile();
            }
        }

        if ((width > 0) && (height > 0)) {
            if (profile != null) {
                byte[] data = getData(boxes);
                if (profile.equals(new FourCC("rgb3"))) {
                    DataBuffer dataBuffer = new DataBufferByte(data, data.length);
                    PixelInterleavedSampleModel sampleModel =
                            new PixelInterleavedSampleModel(
                                    DataBuffer.TYPE_BYTE,
                                    width,
                                    height,
                                    3,
                                    3 * width,
                                    new int[] {0, 1, 2});
                    WritableRaster raster =
                            Raster.createWritableRaster(sampleModel, dataBuffer, (Point) null);
                    ColorModel colourModel =
                            new ComponentColorModel(
                                    ColorSpace.getInstance(ColorSpace.CS_sRGB),
                                    false,
                                    true,
                                    Transparency.OPAQUE,
                                    DataBuffer.TYPE_BYTE);
                    BufferedImage target = new BufferedImage(colourModel, raster, true, null);
                    File outputFile = new File(outputPath);
                    ImageIO.write(target, "PNG", outputFile);
                } else if (profile.equals(new FourCC("rgba"))) {
                    DataBuffer dataBuffer = new DataBufferByte(data, data.length);
                    PixelInterleavedSampleModel sampleModel =
                            new PixelInterleavedSampleModel(
                                    DataBuffer.TYPE_BYTE,
                                    width,
                                    height,
                                    4,
                                    4 * width,
                                    new int[] {0, 1, 2});
                    WritableRaster raster =
                            Raster.createWritableRaster(sampleModel, dataBuffer, (Point) null);
                    ColorModel colourModel =
                            new ComponentColorModel(
                                    ColorSpace.getInstance(ColorSpace.CS_sRGB),
                                    false,
                                    true,
                                    Transparency.OPAQUE,
                                    DataBuffer.TYPE_BYTE);
                    BufferedImage target = new BufferedImage(colourModel, raster, true, null);
                    File outputFile = new File(outputPath);
                    ImageIO.write(target, "PNG", outputFile);
                } else if (profile.equals(new FourCC("abgr"))) {
                    DataBuffer dataBuffer = new DataBufferByte(data, data.length);
                    PixelInterleavedSampleModel sampleModel =
                            new PixelInterleavedSampleModel(
                                    DataBuffer.TYPE_BYTE,
                                    width,
                                    height,
                                    4,
                                    4 * width,
                                    new int[] {3, 2, 1});
                    WritableRaster raster =
                            Raster.createWritableRaster(sampleModel, dataBuffer, (Point) null);
                    ColorModel colourModel =
                            new ComponentColorModel(
                                    ColorSpace.getInstance(ColorSpace.CS_sRGB),
                                    false,
                                    true,
                                    Transparency.OPAQUE,
                                    DataBuffer.TYPE_BYTE);
                    BufferedImage target = new BufferedImage(colourModel, raster, true, null);
                    File outputFile = new File(outputPath);
                    ImageIO.write(target, "PNG", outputFile);
                } else {
                    fail("unsupported profile: " + profile.toString());
                }
            } else {
                fail("missing uncC or profile");
            }
        } else {
            fail("missing ispe width or height");
        }
    }

    private static Box findBox(AbstractContainerBox parent, String... fourCCs) {
        Box child = null;
        for (String fourCC : fourCCs) {
            child = findChildBox(parent, fourCC);
            if (child instanceof AbstractContainerBox abstractContainerBox) {
                parent = abstractContainerBox;
            }
        }
        return child;
    }

    private static Box findChildBox(MetaBox parent, FourCC fourCC) {
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

    private static Box findChildBox(AbstractContainerBox parent, String fourCC) {
        if (parent == null) {
            return null;
        }
        for (Box box : parent.getNestedBoxes()) {
            if (box.getFourCC().equals(new FourCC(fourCC))) {
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

    private byte[] getData(List<Box> boxes) {
        MediaDataBox mdat = (MediaDataBox) getTopLevelBoxByFourCC(boxes, "mdat");
        // TODO: we need to do the construction properly, off what is in iloc
        return mdat.getData();
    }
}
