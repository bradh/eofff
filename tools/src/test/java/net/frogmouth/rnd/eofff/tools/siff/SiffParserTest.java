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
import net.frogmouth.rnd.eofff.uncompressed.cmpd.ComponentDefinition;
import net.frogmouth.rnd.eofff.uncompressed.cmpd.ComponentDefinitionBox;
import net.frogmouth.rnd.eofff.uncompressed.uncc.Component;
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

        ImageSpatialExtentsProperty ispe = null;
        UncompressedFrameConfigBox uncC = null;
        ComponentDefinitionBox cmpd = null;
        for (AbstractItemProperty prop : properties) {
            if (prop instanceof ImageSpatialExtentsProperty box) {
                ispe = box;
            }
            if (prop instanceof UncompressedFrameConfigBox box) {
                uncC = box;
            }
            if (prop instanceof ComponentDefinitionBox box) {
                cmpd = box;
            }
        }

        if ((ispe != null) && (uncC != null) && (cmpd != null)) {
            int width = (int) ispe.getImageWidth();
            int height = (int) ispe.getImageHeight();
            FourCC profile = uncC.getProfile();
            int pixelStride = getPixelStride(uncC);
            int rowStride = getRowStride(uncC, ispe);
            byte[] data = getData(boxes);
            boolean alphaIsPremultiplied = true;
            boolean hasAlpha = getHasAlpha(uncC, cmpd);

            int[] bandOffset = getBandOffsetsRGBA(uncC, cmpd);
            if (profile.equals(new FourCC("rgb3"))
                    || profile.equals(new FourCC("rgba"))
                    || profile.equals(new FourCC("abgr"))) {
                ColorModel colourModel =
                        new ComponentColorModel(
                                ColorSpace.getInstance(ColorSpace.CS_sRGB),
                                hasAlpha,
                                alphaIsPremultiplied,
                                hasAlpha ? Transparency.TRANSLUCENT : Transparency.OPAQUE,
                                DataBuffer.TYPE_BYTE);
                BufferedImage target =
                        buildBufferedImage(
                                data,
                                width,
                                height,
                                pixelStride,
                                rowStride,
                                bandOffset,
                                colourModel);
                writeOutput(outputPath, target);
            } else {
                fail("unsupported profile: " + profile.toString());
            }
        } else {
            fail("missing ispe, uncC or cmpd");
        }
    }

    private BufferedImage buildBufferedImage(
            byte[] data,
            int width,
            int height,
            int pixelStride,
            int rowStride,
            int[] bandOffset,
            ColorModel colourModel) {
        DataBuffer dataBuffer = new DataBufferByte(data, data.length);
        PixelInterleavedSampleModel sampleModel =
                new PixelInterleavedSampleModel(
                        DataBuffer.TYPE_BYTE, width, height, pixelStride, rowStride, bandOffset);
        WritableRaster raster = Raster.createWritableRaster(sampleModel, dataBuffer, (Point) null);

        BufferedImage target = new BufferedImage(colourModel, raster, true, null);
        return target;
    }

    private void writeOutput(String outputPath, BufferedImage target) throws IOException {
        File outputFile = new File(outputPath);
        ImageIO.write(target, "PNG", outputFile);
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

    private int getPixelStride(UncompressedFrameConfigBox uncC) {
        // TODO: this will need more work.
        return uncC.getComponents().size();
    }

    private int getRowStride(UncompressedFrameConfigBox uncC, ImageSpatialExtentsProperty ispe) {
        return getPixelStride(uncC) * (int) ispe.getImageWidth();
    }

    private boolean getHasAlpha(UncompressedFrameConfigBox uncC, ComponentDefinitionBox cmpd) {
        return uncC.getComponents().stream()
                .map(component -> component.getComponentIndex())
                .map(index -> cmpd.getComponentDefinitions().get(index).getComponentType())
                .anyMatch(component_type -> (component_type == 7));
    }

    private int[] getBandOffsetsRGBA(UncompressedFrameConfigBox uncC, ComponentDefinitionBox cmpd) {
        int[] bandOffsets;
        if (getHasAlpha(uncC, cmpd)) {
            bandOffsets = new int[4];
        } else {
            bandOffsets = new int[3];
        }
        for (int i = 0; i < uncC.getComponents().size(); i++) {
            Component component = uncC.getComponents().get(i);
            int component_index = component.getComponentIndex();
            ComponentDefinition componentDefinition =
                    cmpd.getComponentDefinitions().get(component_index);
            switch (componentDefinition.getComponentType()) {
                case 4:
                    bandOffsets[0] = i;
                    break;
                case 5:
                    bandOffsets[1] = i;
                    break;
                case 6:
                    bandOffsets[2] = i;
                    break;
                case 7:
                    bandOffsets[3] = i;
                    break;
                default:
                    LOG.info(
                            "got unexpected band component definition:"
                                    + componentDefinition.getComponentType());
            }
        }
        return bandOffsets;
    }
}
