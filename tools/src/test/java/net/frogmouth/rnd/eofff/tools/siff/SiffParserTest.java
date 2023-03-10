package net.frogmouth.rnd.eofff.tools.siff;

import static org.testng.Assert.*;

import java.awt.Point;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BandedSampleModel;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferUShort;
import java.awt.image.DirectColorModel;
import java.awt.image.PixelInterleavedSampleModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.SinglePixelPackedSampleModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.AbstractItemProperty;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemPropertiesBox;
import net.frogmouth.rnd.eofff.imagefileformat.properties.image.ImageSpatialExtentsProperty;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FileParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ftyp.FileTypeBox;
import net.frogmouth.rnd.eofff.isobmff.iloc.ILocItem;
import net.frogmouth.rnd.eofff.isobmff.iloc.ItemLocationBox;
import net.frogmouth.rnd.eofff.isobmff.mdat.MediaDataBox;
import net.frogmouth.rnd.eofff.isobmff.meta.ILocExtent;
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
        convertToPNG("test_siff_rgb3.mp4", "test_siff_rgb3.png");
    }

    @Test
    public void parse_bgr() throws IOException {
        convertToPNG("test_siff_bgr.mp4", "test_siff_bgr.png");
    }

    @Test
    public void parse_rgba() throws IOException {
        convertToPNG("test_siff_rgba.mp4", "test_siff_rgba.png");
    }

    @Test
    public void parse_abgr() throws IOException {
        convertToPNG("test_siff_abgr.mp4", "test_siff_abgr.png");
    }

    @Test
    public void parse_rgb_component() throws IOException {
        convertToPNG("test_siff_rgb_component.mp4", "test_siff_rgb_component.png");
    }

    @Test
    public void parse_rgb565_block_be() throws IOException {
        convertToPNG("test_siff_rgb565_block_be.mp4", "test_siff_rgb565_block_be.png");
    }

    @Test
    public void parse_rgb565_block_le() throws IOException {
        convertToPNG("test_siff_rgb565_block_le.mp4", "test_siff_rgb565_block_le.png");
    }

    @Test
    public void parse_rgb555_block_be() throws IOException {
        convertToPNG("test_siff_rgb555_block_be.mp4", "test_siff_rgb555_block_be.png");
    }

    @Test
    public void parse_rgb555_block_le() throws IOException {
        convertToPNG("test_siff_rgb555_block_le.mp4", "test_siff_rgb555_block_le.png");
    }

    @Test
    public void parse_rgb555_block_be_pad_lsb() throws IOException {
        convertToPNG(
                "test_siff_rgb555_block_be_pad_lsb.mp4", "test_siff_rgb555_block_be_pad_lsb.png");
    }

    @Test
    public void parse_rgb555_block_le_pad_lsb() throws IOException {
        convertToPNG(
                "test_siff_rgb555_block_le_pad_lsb.mp4", "test_siff_rgb555_block_le_pad_lsb.png");
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
            FourCC profile = uncC.getProfile();
            byte[] data = getData(boxes, primaryItemId);
            ByteOrder blockEndian =
                    uncC.isBlockLittleEndian() ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN;

            if (profile.equals(new FourCC("rgb3"))
                    || profile.equals(new FourCC("rgba"))
                    || profile.equals(new FourCC("abgr"))) {
                SampleModel sampleModel = getSampleModel(uncC, cmpd, ispe);
                ColorModel colourModel = getColourModelRgb(uncC, cmpd);
                BufferedImage target =
                        buildBufferedImage(data, sampleModel, colourModel, blockEndian);
                writeOutput(outputPath, target);
            } else if (profile.equals(new FourCC("gene"))) {
                // we need to check more cases
                SampleModel sampleModel = getSampleModel(uncC, cmpd, ispe);
                ColorModel colourModel = getColourModel(uncC, cmpd);
                if ((sampleModel != null) && (colourModel != null)) {
                    switch (uncC.getInterleaveType()) {
                        case 0:
                            {
                                BufferedImage target =
                                        buildBufferedImageBanded(data, sampleModel, colourModel);
                                writeOutput(outputPath, target);
                                break;
                            }
                        case 1:
                            {
                                BufferedImage target =
                                        buildBufferedImage(
                                                data, sampleModel, colourModel, blockEndian);
                                writeOutput(outputPath, target);
                                break;
                            }
                        default:
                            fail("unsupported interleave type");
                            break;
                    }
                }
            } else {
                fail("unsupported profile: " + profile.toString());
            }
        } else {
            fail("missing ispe, uncC or cmpd");
        }
    }

    private SampleModel getSampleModel(
            UncompressedFrameConfigBox uncC,
            ComponentDefinitionBox cmpd,
            ImageSpatialExtentsProperty ispe) {
        int width = (int) ispe.getImageWidth();
        int height = (int) ispe.getImageHeight();
        int pixelStride = getPixelStride(uncC);
        int rowStride = getRowStride(uncC, ispe);
        switch (uncC.getInterleaveType()) {
            case 0:
                // TODO: don't hard code number of bands
                SampleModel bandedSampleModel =
                        new BandedSampleModel(DataBuffer.TYPE_BYTE, width, height, 3);
                return bandedSampleModel;
            case 1:
                int[] bandOffsets = getBandOffsetsRGBA(uncC, cmpd);
                boolean isShortAligned = isShortAligned(uncC);
                if (bandOffsetsAreValid(bandOffsets)) {
                    if (isShortAligned) {
                        // TODO: this might be useful for more stuff, on byte, ushort and uint
                        // boundaries.
                        int scanlineStride = width; // for now
                        int[] bitmasks = getBitMaskRGB(uncC, cmpd);
                        SampleModel singlePixelPackedSampleModel =
                                new SinglePixelPackedSampleModel(
                                        DataBuffer.TYPE_USHORT,
                                        width,
                                        height,
                                        scanlineStride,
                                        bitmasks);
                        return singlePixelPackedSampleModel;
                    }
                    SampleModel pixelInterleavedComponentModel =
                            new PixelInterleavedSampleModel(
                                    DataBuffer.TYPE_BYTE,
                                    width,
                                    height,
                                    pixelStride,
                                    rowStride,
                                    bandOffsets);
                    return pixelInterleavedComponentModel;

                } else {
                    return null;
                }

            default:
                return null;
        }
    }

    private int[] getBitMaskRGB(UncompressedFrameConfigBox uncC, ComponentDefinitionBox cmpd) {
        int[] bandOffsets = this.getBandOffsetsRGBA(uncC, cmpd);
        int numRedBits =
                uncC.getComponents().get(bandOffsets[0]).getComponentBitDepthMinusOne() + 1;
        int numGreenBits =
                uncC.getComponents().get(bandOffsets[1]).getComponentBitDepthMinusOne() + 1;
        int numBlueBits =
                uncC.getComponents().get(bandOffsets[2]).getComponentBitDepthMinusOne() + 1;
        int numPadBits = 0;
        if (uncC.isBlockPadLSB()) {
            numPadBits =
                    uncC.getBlockSize() * Byte.SIZE - (numRedBits + numGreenBits + numBlueBits);
        }
        int redMask = fillBits(numPadBits + numBlueBits + numGreenBits, numRedBits);
        int greenMask = fillBits(numPadBits + numBlueBits, numGreenBits);
        int blueMask = fillBits(numPadBits, numBlueBits);
        int[] bitmasks = new int[] {redMask, greenMask, blueMask};
        return bitmasks;
    }

    private ColorModel getColourModelRgb(
            UncompressedFrameConfigBox uncC, ComponentDefinitionBox cmpd) {
        boolean alphaIsPremultiplied = true;
        boolean hasAlpha = getHasAlpha(uncC, cmpd);
        ColorModel colourModel =
                new ComponentColorModel(
                        ColorSpace.getInstance(ColorSpace.CS_sRGB),
                        hasAlpha,
                        alphaIsPremultiplied,
                        hasAlpha ? Transparency.TRANSLUCENT : Transparency.OPAQUE,
                        DataBuffer.TYPE_BYTE);
        return colourModel;
    }

    private ColorModel getColourModel(
            UncompressedFrameConfigBox uncC, ComponentDefinitionBox cmpd) {
        if (uncC.getBlockSize() > 0) {
            int[] bitMasks = getBitMaskRGB(uncC, cmpd);
            ColorModel colourModel =
                    new DirectColorModel(
                            uncC.getBlockSize() * Byte.SIZE, bitMasks[0], bitMasks[1], bitMasks[2]);
            return colourModel;
        } else {
            boolean alphaIsPremultiplied = true;
            boolean hasAlpha = getHasAlpha(uncC, cmpd);
            ColorModel colourModel =
                    new ComponentColorModel(
                            ColorSpace.getInstance(ColorSpace.CS_sRGB),
                            hasAlpha,
                            alphaIsPremultiplied,
                            hasAlpha ? Transparency.TRANSLUCENT : Transparency.OPAQUE,
                            DataBuffer.TYPE_BYTE);
            return colourModel;
        }
    }

    private BufferedImage buildBufferedImage(
            byte[] data, SampleModel sampleModel, ColorModel colourModel, ByteOrder blockEndian) {
        if (sampleModel.getDataType() == DataBuffer.TYPE_BYTE) {
            DataBuffer dataBuffer = new DataBufferByte(data, data.length);
            WritableRaster raster =
                    Raster.createWritableRaster(sampleModel, dataBuffer, (Point) null);
            BufferedImage target = new BufferedImage(colourModel, raster, true, null);
            return target;
        } else {
            DataBuffer dataBuffer =
                    new DataBufferUShort(byteArrayToShortArray(data, blockEndian), data.length / 2);
            WritableRaster raster =
                    Raster.createWritableRaster(sampleModel, dataBuffer, (Point) null);
            BufferedImage target = new BufferedImage(colourModel, raster, true, null);
            return target;
        }
    }

    private BufferedImage buildBufferedImageBanded(
            byte[] data, SampleModel sampleModel, ColorModel colourModel) {
        int numBanks = 3;
        int lengthOfBank = data.length / numBanks;
        byte[][] bankData = new byte[numBanks][lengthOfBank];
        for (int bankIndex = 0; bankIndex < numBanks; bankIndex++) {
            System.arraycopy(data, bankIndex * lengthOfBank, bankData[bankIndex], 0, lengthOfBank);
        }
        DataBufferByte dataBuffer = new DataBufferByte(bankData, lengthOfBank);
        WritableRaster raster = Raster.createWritableRaster(sampleModel, dataBuffer, (Point) null);
        BufferedImage target = new BufferedImage(colourModel, raster, true, null);
        return target;
    }

    private void writeOutput(String outputPath, BufferedImage target) throws IOException {
        File outputFile = new File(outputPath);
        ImageIO.write(target, "PNG", outputFile);
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

    private static Box getTopLevelBoxByFourCC(List<Box> boxes, String fourCC) {
        for (Box box : boxes) {
            if (box.getFourCC().equals(new FourCC(fourCC))) {
                return box;
            }
        }
        return null;
    }

    private byte[] getData(List<Box> boxes, long primaryItemId) {
        MediaDataBox mdat = (MediaDataBox) getTopLevelBoxByFourCC(boxes, "mdat");
        MetaBox meta = (MetaBox) getTopLevelBoxByFourCC(boxes, "meta");
        ItemLocationBox iloc = (ItemLocationBox) findChildBox(meta, new FourCC("iloc"));
        for (ILocItem item : iloc.getItems()) {
            if (primaryItemId == item.getItemId()) {
                // TODO: we need to do the construction properly, off what is in iloc
                List<ILocExtent> extents = item.getExtents();
                if (extents.size() != 1) {
                    // TODO: need to handle split extent
                    fail("multiple extents is not yet handled");
                }
                ILocExtent ilocExtent = extents.get(0);
                long startOffset = ilocExtent.getExtentOffset();
                int length = (int) ilocExtent.getExtentLength();
                int dataOffset = (int) (startOffset - mdat.getInitialOffset());
                byte[] data = new byte[length];
                System.arraycopy(mdat.getData(), dataOffset, data, 0, length);
                return data;
            }
        }
        return null;
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
            bandOffsets = new int[] {-1, -1, -1, -1};
        } else {
            bandOffsets = new int[] {-1, -1, -1};
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

    private boolean bandOffsetsAreValid(int[] bandOffsets) {
        for (int bandOffset : bandOffsets) {
            if (bandOffset < 0) {
                return false;
            }
        }
        return true;
    }

    private boolean isShortAligned(UncompressedFrameConfigBox uncC) {
        // TODO: temporary hack
        return uncC.getBlockSize() == 2;
    }

    private short[] byteArrayToShortArray(byte[] data, ByteOrder endian) {
        short[] shorts = new short[data.length / Short.BYTES];
        ByteBuffer.wrap(data).order(endian).asShortBuffer().get(shorts);
        return shorts;
    }

    private int fillBits(int initialOffset, int numBits) {
        int mask = 0;
        for (int i = 0; i < initialOffset + numBits; i++) {
            if (i < initialOffset) {
                continue;
            }
            mask |= (0x1 << i);
        }
        return mask;
    }
}
