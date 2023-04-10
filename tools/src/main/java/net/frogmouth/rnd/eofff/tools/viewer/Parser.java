/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.frogmouth.rnd.eofff.tools.viewer;

import static net.frogmouth.rnd.eofff.uncompressed.uncc.Interleaving.Component;
import static net.frogmouth.rnd.eofff.uncompressed.uncc.Interleaving.Pixel;

import java.awt.Color;
import java.awt.Graphics2D;
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
import java.awt.image.IndexColorModel;
import java.awt.image.PixelInterleavedSampleModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.SinglePixelPackedSampleModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.AbstractItemProperty;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemPropertiesBox;
import net.frogmouth.rnd.eofff.imagefileformat.properties.image.ImageSpatialExtentsProperty;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FileParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ftyp.FileTypeBox;
import net.frogmouth.rnd.eofff.isobmff.iloc.ILocExtent;
import net.frogmouth.rnd.eofff.isobmff.iloc.ILocItem;
import net.frogmouth.rnd.eofff.isobmff.iloc.ItemLocationBox;
import net.frogmouth.rnd.eofff.isobmff.mdat.MediaDataBox;
import net.frogmouth.rnd.eofff.isobmff.meta.MetaBox;
import net.frogmouth.rnd.eofff.isobmff.pitm.PrimaryItemBox;
import net.frogmouth.rnd.eofff.uncompressed.cmpd.ComponentDefinition;
import net.frogmouth.rnd.eofff.uncompressed.cmpd.ComponentDefinitionBox;
import net.frogmouth.rnd.eofff.uncompressed.cpal.ComponentPaletteBox;
import net.frogmouth.rnd.eofff.uncompressed.uncc.Component;
import net.frogmouth.rnd.eofff.uncompressed.uncc.SamplingType;
import net.frogmouth.rnd.eofff.uncompressed.uncc.UncompressedFrameConfigBox;
import net.frogmouth.rnd.eofff.yuv.ColourSpace;
import net.frogmouth.rnd.eofff.yuv.ColourSpaceConverter;
import net.frogmouth.rnd.eofff.yuv.OutputFormat;
import net.frogmouth.rnd.eofff.yuv.OutputFormat_BGR_Bytes;
import net.frogmouth.rnd.eofff.yuv.converters.SourceFormat;
import net.frogmouth.rnd.eofff.yuv.converters.YUV420Converter;
import net.frogmouth.rnd.eofff.yuv.converters.YUVConverter;

/**
 * @author bradh
 */
class Parser {
    private BufferedImage target;

    Parser(String[] args) throws IOException {

        List<Box> boxes = new ArrayList<>();
        boxes.addAll(parseFile(args[0]));
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
        ComponentPaletteBox cpal = null;
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
            if (prop instanceof ComponentPaletteBox box) {
                cpal = box;
            }
        }
        byte[] itemData = getData(boxes, primaryItemId);
        if ((ispe != null) && (uncC != null) && (cmpd != null)) {
            if ((uncC.getNumTileColumnsMinusOne() == 0) && (uncC.getNumTileRowsMinusOne() == 0)) {
                parseAsSingleTile(uncC, itemData, cmpd, ispe, cpal);
            } else {
                target =
                        new BufferedImage(
                                (int) ispe.getImageWidth(),
                                (int) ispe.getImageHeight(),
                                BufferedImage.TYPE_4BYTE_ABGR_PRE);
                target.getGraphics().setColor(Color.YELLOW);
                target.getGraphics()
                        .fillRect(0, 0, (int) ispe.getImageWidth(), (int) ispe.getImageWidth());
                long tileWidth = ispe.getImageWidth() / (1 + uncC.getNumTileColumnsMinusOne());
                long tileHeight = ispe.getImageHeight() / (1 + uncC.getNumTileRowsMinusOne());
                // TODO: avoid hard coding the 3
                long tileSizeBytes = tileWidth * tileHeight * 3;
                Graphics2D targetGraphics = (Graphics2D) target.getGraphics();
                for (int y = 0; y <= uncC.getNumTileRowsMinusOne(); y++) {
                    for (int x = 0; x <= uncC.getNumTileColumnsMinusOne(); x++) {
                        long dataOffset =
                                (x + y * (1 + uncC.getNumTileColumnsMinusOne())) * tileSizeBytes;
                        byte[] tileData = new byte[(int) tileSizeBytes];
                        System.arraycopy(
                                itemData, (int) dataOffset, tileData, 0, (int) tileSizeBytes);
                        System.out.println(
                                String.format(
                                        "Making tile (x=%d, y=%d), offset=%d", x, y, dataOffset));
                        BufferedImage tile =
                                renderTile(tileWidth, tileHeight, tileData, uncC, cmpd, cpal);
                        if (tile != null) {
                            targetGraphics.drawImage(
                                    tile, (int) (x * tileWidth), (int) (y * tileHeight), null);
                        }
                    }
                }
            }
        } else {
            System.out.println("FAIL. missing ispe, uncC or cmpd");
        }
    }

    private BufferedImage renderTile(
            long tileWidth,
            long tileHeight,
            byte[] itemData,
            UncompressedFrameConfigBox uncC,
            ComponentDefinitionBox cmpd,
            ComponentPaletteBox cpal) {
        SampleModel sampleModel = getSampleModel(uncC, cmpd, tileWidth, tileHeight);
        ColorModel colourModel = getColourModel(uncC, cmpd);
        if ((sampleModel != null) && (colourModel != null)) {
            switch (uncC.getInterleaveType()) {
                case Component:
                    {
                        return buildBufferedImageBanded(itemData, sampleModel, colourModel);
                    }
                case Pixel:
                    {
                        ByteOrder blockEndian =
                                uncC.isBlockLittleEndian()
                                        ? ByteOrder.LITTLE_ENDIAN
                                        : ByteOrder.BIG_ENDIAN;
                        return buildBufferedImage(itemData, sampleModel, colourModel, blockEndian);
                    }
                default:
                    System.out.println("FAIL. unsupported interleave type");
                    break;
            }
        }
        return null;
    }

    private void parseAsSingleTile(
            UncompressedFrameConfigBox uncC,
            byte[] itemData,
            ComponentDefinitionBox cmpd,
            ImageSpatialExtentsProperty ispe,
            ComponentPaletteBox cpal) {
        FourCC profile = uncC.getProfile();
        ByteOrder blockEndian =
                uncC.isBlockLittleEndian() ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN;

        if (profile.equals(new FourCC("rgb3"))
                || profile.equals(new FourCC("rgba"))
                || profile.equals(new FourCC("abgr"))) {
            SampleModel sampleModel = getSampleModel(uncC, cmpd, ispe);
            ColorModel colourModel = getColourModelRgb(uncC, cmpd);
            target = buildBufferedImage(itemData, sampleModel, colourModel, blockEndian);

        } else if ((profile.hashCode() == 0)
                || profile.equals(new FourCC("gene"))
                || profile.equals(new FourCC("2vuy"))
                || profile.equals(new FourCC("yuv2"))
                || profile.equals(new FourCC("yvyu"))
                || profile.equals(new FourCC("vyuy"))
                || profile.equals(new FourCC("v308"))
                || profile.equals(new FourCC("i420"))
                || profile.equals(new FourCC("nv12"))
                || profile.equals(new FourCC("nv21"))) {
            if (isRGB(uncC, cmpd)) {
                // we need to check more cases
                SampleModel sampleModel = getSampleModel(uncC, cmpd, ispe);
                ColorModel colourModel = getColourModel(uncC, cmpd);
                if ((sampleModel != null) && (colourModel != null)) {
                    switch (uncC.getInterleaveType()) {
                        case Component:
                            {
                                target =
                                        buildBufferedImageBanded(
                                                itemData, sampleModel, colourModel);
                                break;
                            }
                        case Pixel:
                            {
                                target =
                                        buildBufferedImage(
                                                itemData, sampleModel, colourModel, blockEndian);
                                break;
                            }
                        default:
                            System.out.println("FAIL. unsupported interleave type");
                            break;
                    }
                }
            } else if (isYCbCr(uncC, cmpd)) {
                OutputFormat outputFormat =
                        new OutputFormat_BGR_Bytes(
                                (int) (ispe.getImageHeight() * ispe.getImageWidth()));
                byte[] rgbData;
                if (profile.equals(new FourCC("2vuy"))) {
                    YUVConverter converter =
                            new YUV420Converter(
                                    (int) ispe.getImageHeight(),
                                    (int) ispe.getImageWidth(),
                                    SourceFormat.TwoYUV);
                    rgbData = converter.convert(itemData, outputFormat);
                } else if (profile.equals(new FourCC("yuv2"))) {
                    YUVConverter converter =
                            new YUV420Converter(
                                    (int) ispe.getImageHeight(),
                                    (int) ispe.getImageWidth(),
                                    SourceFormat.YUV2);
                    rgbData = converter.convert(itemData, outputFormat);
                } else if (profile.equals(new FourCC("yvyu"))) {
                    YUVConverter converter =
                            new YUV420Converter(
                                    (int) ispe.getImageHeight(),
                                    (int) ispe.getImageWidth(),
                                    SourceFormat.YVYU);
                    rgbData = converter.convert(itemData, outputFormat);
                } else if (profile.equals(new FourCC("vyuy"))) {
                    YUVConverter converter =
                            new YUV420Converter(
                                    (int) ispe.getImageHeight(),
                                    (int) ispe.getImageWidth(),
                                    SourceFormat.VYUY);
                    rgbData = converter.convert(itemData, outputFormat);
                } else if (profile.equals(new FourCC("v308"))) {
                    rgbData =
                            ColourSpaceConverter.V308Converter(
                                    (int) ispe.getImageHeight(),
                                    (int) ispe.getImageWidth(),
                                    itemData,
                                    outputFormat);
                } else if (profile.equals(new FourCC("nv12"))) {
                    rgbData =
                            ColourSpaceConverter.NV12Converter(
                                    (int) ispe.getImageHeight(),
                                    (int) ispe.getImageWidth(),
                                    itemData,
                                    outputFormat);
                } else if (profile.equals(new FourCC("nv21"))) {
                    rgbData =
                            ColourSpaceConverter.NV21Converter(
                                    (int) ispe.getImageHeight(),
                                    (int) ispe.getImageWidth(),
                                    itemData,
                                    outputFormat);
                } else {
                    ColourSpace colourSpace = ColourSpace.YUV444;
                    if (uncC.getSamplingType().equals(SamplingType.YCbCr420)) {
                        colourSpace = ColourSpace.YUV420;
                    } else if (uncC.getSamplingType().equals(SamplingType.YCbCr422)) {
                        colourSpace = ColourSpace.YUV422;
                    }
                    rgbData =
                            ColourSpaceConverter.YuvConverter(
                                    colourSpace,
                                    (int) ispe.getImageHeight(),
                                    (int) ispe.getImageWidth(),
                                    itemData,
                                    outputFormat);
                }
                target =
                        new BufferedImage(
                                (int) ispe.getImageWidth(),
                                (int) ispe.getImageHeight(),
                                BufferedImage.TYPE_3BYTE_BGR);
                byte[] imgData = ((DataBufferByte) target.getRaster().getDataBuffer()).getData();
                System.arraycopy(rgbData, 0, imgData, 0, rgbData.length);

            } else if (isPalette(uncC, cmpd, cpal)) {
                // we need to check more cases
                SampleModel sampleModel = getSampleModel(uncC, cmpd, ispe);
                ColorModel colourModel = getIndexColourModel(cpal);
                if ((sampleModel != null) && (colourModel != null)) {

                    target = buildBufferedImage(itemData, sampleModel, colourModel, blockEndian);
                }

            } else {
                System.out.println("FAIL. unsupported component combination");
            }
        } else {
            System.out.println("FAIL. unsupported profile: " + profile.toString());
        }
    }

    private List<Box> parseFile(String inputPath) throws IOException {
        Path testFile = Paths.get(inputPath);
        FileParser fileParser = new FileParser();
        return fileParser.parse(testFile);
    }

    private SampleModel getSampleModel(
            UncompressedFrameConfigBox uncC,
            ComponentDefinitionBox cmpd,
            ImageSpatialExtentsProperty ispe) {
        return getSampleModel(uncC, cmpd, ispe.getImageWidth(), ispe.getImageHeight());
    }

    private SampleModel getSampleModel(
            UncompressedFrameConfigBox uncC, ComponentDefinitionBox cmpd, long width, long height) {
        int pixelStride = getPixelStride(uncC);
        long rowStride = getRowStride(uncC, width);
        switch (uncC.getInterleaveType()) {
            case Component:
                SampleModel bandedSampleModel =
                        new BandedSampleModel(
                                DataBuffer.TYPE_BYTE,
                                (int) width,
                                (int) height,
                                uncC.getComponents().size());
                return bandedSampleModel;
            case Pixel:
                int[] bandOffsets = getBandOffsetsRGBA(uncC, cmpd);
                boolean isShortAligned = isShortAligned(uncC);
                if (bandOffsetsAreValid(bandOffsets)) {
                    if (isShortAligned) {
                        // TODO: this might be useful for more stuff, on byte, ushort and uint
                        // boundaries.
                        long scanlineStride = width; // for now
                        int[] bitmasks = getBitMaskRGB(uncC, cmpd);
                        SampleModel singlePixelPackedSampleModel =
                                new SinglePixelPackedSampleModel(
                                        DataBuffer.TYPE_USHORT,
                                        (int) width,
                                        (int) height,
                                        (int) scanlineStride,
                                        bitmasks);
                        return singlePixelPackedSampleModel;
                    }
                    SampleModel pixelInterleavedComponentModel =
                            new PixelInterleavedSampleModel(
                                    DataBuffer.TYPE_BYTE,
                                    (int) width,
                                    (int) height,
                                    pixelStride,
                                    (int) rowStride,
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

    private ColorModel getIndexColourModel(ComponentPaletteBox cpal) {
        byte[] cmap =
                new byte[cpal.getComponentValues().length * cpal.getComponentValues()[0].length];
        for (int i = 0; i < cpal.getComponentValues().length; i++) {
            System.arraycopy(
                    cpal.getComponentValues()[i],
                    0,
                    cmap,
                    i * cpal.getComponentValues()[i].length,
                    cpal.getComponentValues()[i].length);
        }
        IndexColorModel cm =
                new IndexColorModel(8, cpal.getComponentValues().length, cmap, 0, false);
        return cm;
    }

    private BufferedImage buildBufferedImage(
            byte[] data, SampleModel sampleModel, ColorModel colourModel, ByteOrder blockEndian) {
        if (sampleModel.getDataType() == DataBuffer.TYPE_BYTE) {
            DataBuffer dataBuffer = new DataBufferByte(data, data.length);
            WritableRaster raster =
                    Raster.createWritableRaster(sampleModel, dataBuffer, (Point) null);
            BufferedImage image = new BufferedImage(colourModel, raster, true, null);
            return image;
        } else {
            DataBuffer dataBuffer =
                    new DataBufferUShort(byteArrayToShortArray(data, blockEndian), data.length / 2);
            WritableRaster raster =
                    Raster.createWritableRaster(sampleModel, dataBuffer, (Point) null);
            BufferedImage image = new BufferedImage(colourModel, raster, true, null);
            return image;
        }
    }

    private BufferedImage buildBufferedImageBanded(
            byte[] data, SampleModel sampleModel, ColorModel colourModel) {
        int numBanks = 3;
        int lengthOfBank = data.length / numBanks;
        byte[][] bankData = new byte[numBanks][lengthOfBank];
        for (int bankIndex = 0; bankIndex < numBanks; bankIndex++) {
            System.arraycopy(
                    data,
                    (int) (bankIndex * lengthOfBank),
                    bankData[bankIndex],
                    0,
                    sampleModel.getHeight() * sampleModel.getWidth());
        }
        DataBufferByte dataBuffer = new DataBufferByte(bankData, lengthOfBank);
        WritableRaster raster = Raster.createWritableRaster(sampleModel, dataBuffer, (Point) null);
        BufferedImage image = new BufferedImage(colourModel, raster, true, null);
        return image;
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
                    System.out.println("FAIL. multiple extents is not yet handled");
                }
                ILocExtent ilocExtent = extents.get(0);
                long startOffset = ilocExtent.getExtentOffset();
                int length = (int) ilocExtent.getExtentLength();
                int dataOffset =
                        (int) (startOffset - mdat.getInitialOffset() + item.getBaseOffset());
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

    private long getRowStride(UncompressedFrameConfigBox uncC, long imageWidth) {
        return getPixelStride(uncC) * imageWidth;
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
                    System.out.println(
                            "FAIL. got unexpected band component definition:"
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

    private boolean isRGB(UncompressedFrameConfigBox uncC, ComponentDefinitionBox cmpd) {
        int redComponents = 0;
        int greenComponents = 0;
        int blueComponents = 0;
        for (int i = 0; i < uncC.getComponents().size(); i++) {
            Component component = uncC.getComponents().get(i);
            int component_index = component.getComponentIndex();
            ComponentDefinition componentDefinition =
                    cmpd.getComponentDefinitions().get(component_index);
            switch (componentDefinition.getComponentType()) {
                case 4:
                    redComponents += 1;
                    break;
                case 5:
                    greenComponents += 1;
                    break;
                case 6:
                    blueComponents += 1;
                    break;
            }
        }
        return (redComponents == 1) && (greenComponents == 1) && (blueComponents == 1);
    }

    private boolean isYCbCr(UncompressedFrameConfigBox uncC, ComponentDefinitionBox cmpd) {
        int yComponents = 0;
        int cbComponents = 0;
        int crComponents = 0;
        for (int i = 0; i < uncC.getComponents().size(); i++) {
            Component component = uncC.getComponents().get(i);
            int component_index = component.getComponentIndex();
            ComponentDefinition componentDefinition =
                    cmpd.getComponentDefinitions().get(component_index);
            switch (componentDefinition.getComponentType()) {
                case 1:
                    yComponents += 1;
                    break;
                case 2:
                    cbComponents += 1;
                    break;
                case 3:
                    crComponents += 1;
                    break;
            }
        }
        return (yComponents >= 1) && (cbComponents == 1) && (crComponents == 1);
    }

    private boolean isPalette(
            UncompressedFrameConfigBox uncC,
            ComponentDefinitionBox cmpd,
            ComponentPaletteBox cpal) {
        if (cpal == null) {
            return false;
        }
        if (uncC.getComponents().size() == 1) {
            int component_index = uncC.getComponents().get(0).getComponentIndex();
            return (cmpd.getComponentDefinitions().get(component_index).getComponentType() == 10);
        }
        return false;
    }

    void show() {
        new net.coobird.gui.simpleimageviewer4j.Viewer(target).show();
    }
}
