package net.frogmouth.rnd.eofff.demos;

import com.aayushatharva.brotli4j.Brotli4jLoader;
import com.aayushatharva.brotli4j.encoder.Encoder;
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
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Deflater;
import javax.imageio.ImageIO;
import net.frogmouth.rnd.eofff.imagefileformat.properties.image.ImageSpatialExtentsProperty;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.free.FreeBox;
import net.frogmouth.rnd.eofff.isobmff.ftyp.Brand;
import net.frogmouth.rnd.eofff.isobmff.ftyp.FileTypeBox;
import net.frogmouth.rnd.eofff.isobmff.hdlr.HandlerBox;
import net.frogmouth.rnd.eofff.isobmff.iinf.ItemInfoBox;
import net.frogmouth.rnd.eofff.isobmff.iloc.ILocExtent;
import net.frogmouth.rnd.eofff.isobmff.iloc.ILocItem;
import net.frogmouth.rnd.eofff.isobmff.iloc.ItemLocationBox;
import net.frogmouth.rnd.eofff.isobmff.infe.ItemInfoEntry;
import net.frogmouth.rnd.eofff.isobmff.iprp.AssociationEntry;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemPropertiesBox;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemPropertyAssociation;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemPropertyContainerBox;
import net.frogmouth.rnd.eofff.isobmff.iprp.PropertyAssociation;
import net.frogmouth.rnd.eofff.isobmff.mdat.MediaDataBox;
import net.frogmouth.rnd.eofff.isobmff.meta.MetaBox;
import net.frogmouth.rnd.eofff.isobmff.pitm.PrimaryItemBox;
import net.frogmouth.rnd.eofff.uncompressed.cmpc.CompressionConfigurationItemProperty;
import net.frogmouth.rnd.eofff.uncompressed.cmpd.ComponentDefinition;
import net.frogmouth.rnd.eofff.uncompressed.cmpd.ComponentDefinitionBox;
import net.frogmouth.rnd.eofff.uncompressed.icbr.ItemCompressedByteRangeInfo;
import net.frogmouth.rnd.eofff.uncompressed.uncc.Component;
import net.frogmouth.rnd.eofff.uncompressed.uncc.ComponentFormat;
import net.frogmouth.rnd.eofff.uncompressed.uncc.Interleaving;
import net.frogmouth.rnd.eofff.uncompressed.uncc.SamplingType;
import net.frogmouth.rnd.eofff.uncompressed.uncc.UncompressedFrameConfigBox;
import org.testng.annotations.Test;

/** Write file. */
public class WriteGenericallyCompressedFileTest {

    private static final long MAIN_ITEM_ID = 10;
    private static final int LENGTH_OF_FREEBOX_HEADER = 8;
    protected static final int IMAGE_WIDTH = 128;
    protected static final int IMAGE_HEIGHT = 72;
    private static final int MDAT_START = 370;
    protected static final int NUM_BYTES_PER_PIXEL_RGB = 3;

    private static final int IMAGE_DATA_START = MDAT_START + 8; // assumes mdat header is 8 bytes.

    public WriteGenericallyCompressedFileTest() {}

    @Test
    public void zlib() throws IOException {
        MediaDataBox mdat = createMediaDataBox_rgb_component_zlib();

        List<Box> boxes = new ArrayList<>();
        FileTypeBox ftyp = createFileTypeBox();
        boxes.add(ftyp);
        MetaBox meta = createMetaBox_rgb_component_whole_file_zlib(mdat.getBodySize());
        boxes.add(meta);
        long lengthOfPreviousBoxes = ftyp.getSize() + meta.getSize();
        long numberOfFillBytes = MDAT_START - lengthOfPreviousBoxes - LENGTH_OF_FREEBOX_HEADER;
        FreeBox free = new FreeBox();
        free.setData(new byte[(int) numberOfFillBytes]);
        boxes.add(free);
        boxes.add(mdat);
        writeBoxes(boxes, "rgb_generic_compressed_zlib.heif");
    }

    @Test
    public void deflate() throws IOException {
        MediaDataBox mdat = createMediaDataBox_rgb_component_deflate();

        List<Box> boxes = new ArrayList<>();
        FileTypeBox ftyp = createFileTypeBox();
        boxes.add(ftyp);
        MetaBox meta = createMetaBox_rgb_component_whole_file_deflate(mdat.getBodySize());
        boxes.add(meta);
        long lengthOfPreviousBoxes = ftyp.getSize() + meta.getSize();
        long numberOfFillBytes = MDAT_START - lengthOfPreviousBoxes - LENGTH_OF_FREEBOX_HEADER;
        FreeBox free = new FreeBox();
        free.setData(new byte[(int) numberOfFillBytes]);
        boxes.add(free);
        boxes.add(mdat);
        writeBoxes(boxes, "rgb_generic_compressed_defl.heif");
    }

    @Test
    public void brotli() throws IOException {
        MediaDataBox mdat = createMediaDataBox_rgb_component_brotli();

        List<Box> boxes = new ArrayList<>();
        FileTypeBox ftyp = createFileTypeBox();
        boxes.add(ftyp);
        MetaBox meta = createMetaBox_rgb_component_whole_file_brotli(mdat.getBodySize());
        boxes.add(meta);
        long lengthOfPreviousBoxes = ftyp.getSize() + meta.getSize();
        long numberOfFillBytes = MDAT_START - lengthOfPreviousBoxes - LENGTH_OF_FREEBOX_HEADER;
        FreeBox free = new FreeBox();
        free.setData(new byte[(int) numberOfFillBytes]);
        boxes.add(free);
        boxes.add(mdat);
        writeBoxes(boxes, "rgb_generic_compressed_brotli.heif");
    }

    private FileTypeBox createFileTypeBox() {
        FileTypeBox fileTypeBox = new FileTypeBox();
        fileTypeBox.setMajorBrand(new Brand("mif1"));
        fileTypeBox.setMinorVersion(0);
        fileTypeBox.addCompatibleBrand(new Brand("mif1"));
        fileTypeBox.addCompatibleBrand(new Brand("mif2"));
        fileTypeBox.addCompatibleBrand(new Brand("unif"));
        return fileTypeBox;
    }

    private MetaBox createMetaBox_rgb_component_whole_file_zlib(long extentLength)
            throws IOException {
        MetaBox meta = new MetaBox();
        List<Box> boxes = new ArrayList<>();
        boxes.add(makeHandlerBox());
        boxes.add(makePrimaryItemBox());
        boxes.add(makeItemInfoBox());
        boxes.add(makeItemLocationBox_rgb3(extentLength));
        boxes.add(makeItemPropertiesBox_rgb_component_whole_file_zlib());
        meta.addNestedBoxes(boxes);
        return meta;
    }

    private MetaBox createMetaBox_rgb_component_whole_file_deflate(long extentLength)
            throws IOException {
        MetaBox meta = new MetaBox();
        List<Box> boxes = new ArrayList<>();
        boxes.add(makeHandlerBox());
        boxes.add(makePrimaryItemBox());
        boxes.add(makeItemInfoBox());
        boxes.add(makeItemLocationBox_rgb3(extentLength));
        boxes.add(makeItemPropertiesBox_rgb_component_whole_file_deflate());
        meta.addNestedBoxes(boxes);
        return meta;
    }

    private MetaBox createMetaBox_rgb_component_whole_file_brotli(long extentLength)
            throws IOException {
        MetaBox meta = new MetaBox();
        List<Box> boxes = new ArrayList<>();
        boxes.add(makeHandlerBox());
        boxes.add(makePrimaryItemBox());
        boxes.add(makeItemInfoBox());
        boxes.add(makeItemLocationBox_rgb3(extentLength));
        boxes.add(makeItemPropertiesBox_rgb_component_whole_file_brotli());
        meta.addNestedBoxes(boxes);
        return meta;
    }

    private HandlerBox makeHandlerBox() {
        HandlerBox hdlr = new HandlerBox();
        hdlr.setHandlerType("pict");
        hdlr.setName("");
        return hdlr;
    }

    private PrimaryItemBox makePrimaryItemBox() {
        PrimaryItemBox pitm = new PrimaryItemBox();
        pitm.setItemID(MAIN_ITEM_ID);
        return pitm;
    }

    private ItemInfoBox makeItemInfoBox() {
        ItemInfoBox iinf = new ItemInfoBox();
        ItemInfoEntry infe0 = new ItemInfoEntry();
        infe0.setVersion(2);
        infe0.setItemID(MAIN_ITEM_ID);
        FourCC unci = new FourCC("unci");
        infe0.setItemType(unci.asUnsigned());
        infe0.setItemName("Generically compressed Image");
        iinf.addItem(infe0);
        return iinf;
    }

    private ItemLocationBox makeItemLocationBox_rgb3(long extentLength) {
        ItemLocationBox iloc = new ItemLocationBox();
        iloc.setOffsetSize(4);
        iloc.setLengthSize(4);
        iloc.setBaseOffsetSize(4);
        iloc.setIndexSize(4);
        iloc.setVersion(1);
        ILocItem mainItemLocation = new ILocItem();
        mainItemLocation.setConstructionMethod(0);
        mainItemLocation.setItemId(MAIN_ITEM_ID);
        ILocExtent mainItemExtent = new ILocExtent();
        mainItemExtent.setExtentIndex(0);
        mainItemExtent.setExtentOffset(IMAGE_DATA_START);
        mainItemExtent.setExtentLength(extentLength);
        mainItemLocation.addExtent(mainItemExtent);
        iloc.addItem(mainItemLocation);
        return iloc;
    }

    private Box makeItemPropertiesBox_rgb_component_whole_file_zlib() {
        ItemPropertiesBox iprp = new ItemPropertiesBox();
        ItemPropertyContainerBox ipco = new ItemPropertyContainerBox();
        ipco.addProperty(makeComponentDefinitionBox_rgb_generic());
        ipco.addProperty(makeUncompressedFrameConfigBox_rgb_component());
        ipco.addProperty(makeImageSpatialExtentsProperty());
        ipco.addProperty(makeCompressionConfigurationBoxWholeFileZlib());
        ipco.addProperty(makeCompressedByteRanges());
        iprp.setItemProperties(ipco);

        iprp.addItemPropertyAssociation(makePropertyAssociations());

        return iprp;
    }

    private Box makeItemPropertiesBox_rgb_component_whole_file_deflate() {
        ItemPropertiesBox iprp = new ItemPropertiesBox();
        ItemPropertyContainerBox ipco = new ItemPropertyContainerBox();
        ipco.addProperty(makeComponentDefinitionBox_rgb_generic());
        ipco.addProperty(makeUncompressedFrameConfigBox_rgb_component());
        ipco.addProperty(makeImageSpatialExtentsProperty());
        ipco.addProperty(makeCompressionConfigurationBoxWholeFileDeflate());
        ipco.addProperty(makeCompressedByteRanges());
        iprp.setItemProperties(ipco);

        iprp.addItemPropertyAssociation(makePropertyAssociations());

        return iprp;
    }

    private Box makeItemPropertiesBox_rgb_component_whole_file_brotli() {
        ItemPropertiesBox iprp = new ItemPropertiesBox();
        ItemPropertyContainerBox ipco = new ItemPropertyContainerBox();
        ipco.addProperty(makeComponentDefinitionBox_rgb_generic());
        ipco.addProperty(makeUncompressedFrameConfigBox_rgb_component());
        ipco.addProperty(makeImageSpatialExtentsProperty());
        ipco.addProperty(makeCompressionConfigurationBoxWholeFileBrotli());
        ipco.addProperty(makeCompressedByteRanges());
        iprp.setItemProperties(ipco);

        iprp.addItemPropertyAssociation(makePropertyAssociations());

        return iprp;
    }

    private ItemPropertyAssociation makePropertyAssociations() {
        ItemPropertyAssociation itemPropertyAssociation = new ItemPropertyAssociation();
        {
            AssociationEntry mainItemAssociations = new AssociationEntry();
            mainItemAssociations.setItemId(MAIN_ITEM_ID);

            PropertyAssociation associationToComponentDefinitionBox = new PropertyAssociation();
            associationToComponentDefinitionBox.setPropertyIndex(1);
            associationToComponentDefinitionBox.setEssential(true);
            mainItemAssociations.addAssociation(associationToComponentDefinitionBox);

            PropertyAssociation associationToUncompressedFrameConfigBox = new PropertyAssociation();
            associationToUncompressedFrameConfigBox.setPropertyIndex(2);
            associationToUncompressedFrameConfigBox.setEssential(true);
            mainItemAssociations.addAssociation(associationToUncompressedFrameConfigBox);

            PropertyAssociation associationToImageSpatialExtentsProperty =
                    new PropertyAssociation();
            associationToImageSpatialExtentsProperty.setPropertyIndex(3);
            associationToImageSpatialExtentsProperty.setEssential(true);
            mainItemAssociations.addAssociation(associationToImageSpatialExtentsProperty);

            PropertyAssociation associationToCompressionConfigurationBox =
                    new PropertyAssociation();
            associationToCompressionConfigurationBox.setPropertyIndex(4);
            associationToCompressionConfigurationBox.setEssential(true);
            mainItemAssociations.addAssociation(associationToCompressionConfigurationBox);

            PropertyAssociation associationToCompressedByteRanges = new PropertyAssociation();
            associationToCompressedByteRanges.setPropertyIndex(5);
            associationToCompressedByteRanges.setEssential(true);
            mainItemAssociations.addAssociation(associationToCompressedByteRanges);

            itemPropertyAssociation.addEntry(mainItemAssociations);
        }
        return itemPropertyAssociation;
    }

    private ComponentDefinitionBox makeComponentDefinitionBox_rgb_generic() {
        ComponentDefinitionBox cmpd = new ComponentDefinitionBox();
        ComponentDefinition redComponent = new ComponentDefinition(4, null);
        cmpd.addComponentDefinition(redComponent);
        ComponentDefinition greenComponent = new ComponentDefinition(5, null);
        cmpd.addComponentDefinition(greenComponent);
        ComponentDefinition blueComponent = new ComponentDefinition(6, null);
        cmpd.addComponentDefinition(blueComponent);
        return cmpd;
    }

    private CompressionConfigurationItemProperty makeCompressionConfigurationBoxWholeFileZlib() {
        CompressionConfigurationItemProperty cmpc = new CompressionConfigurationItemProperty();
        cmpc.setCompressionType(new FourCC("zlib"));
        cmpc.setCanDecompressContiguousRanges(true);
        cmpc.setCompressedRangeType(1);
        return cmpc;
    }

    private CompressionConfigurationItemProperty makeCompressionConfigurationBoxWholeFileDeflate() {
        CompressionConfigurationItemProperty cmpc = new CompressionConfigurationItemProperty();
        cmpc.setCompressionType(new FourCC("defl"));
        cmpc.setCanDecompressContiguousRanges(true);
        cmpc.setCompressedRangeType(1);
        return cmpc;
    }

    private CompressionConfigurationItemProperty makeCompressionConfigurationBoxWholeFileBrotli() {
        CompressionConfigurationItemProperty cmpc = new CompressionConfigurationItemProperty();
        cmpc.setCompressionType(new FourCC("brot"));
        cmpc.setCanDecompressContiguousRanges(true);
        cmpc.setCompressedRangeType(1);
        return cmpc;
    }

    private ItemCompressedByteRangeInfo makeCompressedByteRanges() {
        ItemCompressedByteRangeInfo icbr = new ItemCompressedByteRangeInfo();
        // TODO: we can break up the ranges
        return icbr;
    }

    private UncompressedFrameConfigBox makeUncompressedFrameConfigBox_rgb_component() {
        UncompressedFrameConfigBox uncc = new UncompressedFrameConfigBox();
        uncc.setProfile(new FourCC("gene"));
        uncc.addComponent(new Component(0, 7, ComponentFormat.UnsignedInteger, 0));
        uncc.addComponent(new Component(1, 7, ComponentFormat.UnsignedInteger, 0));
        uncc.addComponent(new Component(2, 7, ComponentFormat.UnsignedInteger, 0));
        uncc.setSamplingType(SamplingType.NoSubsampling);
        uncc.setInterleaveType(Interleaving.Component);
        uncc.setBlockSize(0);
        uncc.setComponentLittleEndian(false);
        uncc.setBlockPadLSB(false);
        uncc.setBlockLittleEndian(false);
        uncc.setBlockReversed(false);
        uncc.setPadUnknown(false);
        uncc.setPixelSize(0);
        uncc.setRowAlignSize(0);
        uncc.setTileAlignSize(0);
        uncc.setNumTileColumnsMinusOne(0);
        uncc.setNumTileColumnsMinusOne(0);
        return uncc;
    }

    private ImageSpatialExtentsProperty makeImageSpatialExtentsProperty() {
        ImageSpatialExtentsProperty ispe = new ImageSpatialExtentsProperty();
        ispe.setImageHeight(IMAGE_HEIGHT);
        ispe.setImageWidth(IMAGE_WIDTH);
        return ispe;
    }

    private MediaDataBox createMediaDataBox_rgb_component_zlib() throws IOException {
        MediaDataBox mdat = new MediaDataBox();
        byte[] data = createMediaDataBoxZlibContent();
        mdat.setData(data);
        return mdat;
    }

    private MediaDataBox createMediaDataBox_rgb_component_deflate() throws IOException {
        MediaDataBox mdat = new MediaDataBox();
        byte[] data = createMediaDataBoxDeflateContent();
        mdat.setData(data);
        return mdat;
    }

    private MediaDataBox createMediaDataBox_rgb_component_brotli() throws IOException {
        MediaDataBox mdat = new MediaDataBox();
        byte[] data = createMediaDataBoxBrotliContent();
        mdat.setData(data);
        return mdat;
    }

    private byte[] createMediaDataBoxZlibContent() throws IOException {
        byte[] data = createUncompressedData();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] temp = new byte[1024];
        Deflater compressor = new Deflater();
        compressor.setInput(data);
        compressor.finish();
        while (!compressor.finished()) {
            int compressedSize = compressor.deflate(temp);
            outputStream.write(temp, 0, compressedSize);
        }
        compressor.end();
        return outputStream.toByteArray();
    }

    private byte[] createMediaDataBoxDeflateContent() throws IOException {
        byte[] data = createUncompressedData();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] temp = new byte[1024];
        Deflater compressor = new Deflater(Deflater.BEST_COMPRESSION, true);
        compressor.setInput(data);
        compressor.finish();
        while (!compressor.finished()) {
            int compressedSize = compressor.deflate(temp);
            outputStream.write(temp, 0, compressedSize);
        }
        compressor.end();
        return outputStream.toByteArray();
    }

    private byte[] createMediaDataBoxBrotliContent() throws IOException {
        byte[] data = createUncompressedData();
        Brotli4jLoader.ensureAvailability();
        byte[] brotliCompressed = Encoder.compress(data);
        // TODO: compress with Brotli, somehow
        return brotliCompressed;
    }

    private byte[] createUncompressedData() throws IOException {
        SampleModel sampleModel =
                new BandedSampleModel(DataBuffer.TYPE_BYTE, IMAGE_WIDTH, IMAGE_HEIGHT, 3);
        WritableRaster raster = Raster.createWritableRaster(sampleModel, (Point) null);
        ColorModel colourModel =
                new ComponentColorModel(
                        ColorSpace.getInstance(ColorSpace.CS_sRGB),
                        false,
                        true,
                        Transparency.OPAQUE,
                        DataBuffer.TYPE_BYTE);
        BufferedImage image = new BufferedImage(colourModel, raster, true, null);
        drawColouredRectangles(image);
        ImageIO.write(image, "PNG", new File("ref_component.png"));
        DataBufferByte buffer = (DataBufferByte) image.getRaster().getDataBuffer();
        int numBanks = buffer.getNumBanks();
        int totalSize = 0;
        for (int i = 0; i < numBanks; i++) {
            totalSize += buffer.getData(i).length;
        }
        byte[] data = new byte[totalSize];
        int destination = 0;
        for (int i = 0; i < numBanks; i++) {
            System.arraycopy(buffer.getData(i), 0, data, destination, buffer.getData(i).length);
            destination += buffer.getData(i).length;
        }
        return data;
    }

    protected static final Color[] COLOURS =
            new Color[] {
                Color.RED,
                Color.GREEN,
                Color.BLUE,
                Color.BLACK,
                Color.WHITE,
                Color.DARK_GRAY,
                Color.CYAN,
                Color.MAGENTA,
                Color.LIGHT_GRAY,
                Color.YELLOW,
                Color.PINK,
                Color.ORANGE,
                Color.GRAY
            };

    private void drawColouredRectangles(BufferedImage image) {
        Graphics2D g2 = image.createGraphics();
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 4; c++) {
                g2.setColor(COLOURS[r * 4 + c]);
                g2.fillRect(
                        c * IMAGE_WIDTH / 4,
                        r * IMAGE_HEIGHT / 3,
                        IMAGE_WIDTH / 4,
                        IMAGE_HEIGHT / 3);
            }
        }
    }

    private void writeBoxes(List<Box> boxes, String outputPathName) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        for (Box box : boxes) {
            // consider validating here
            box.writeTo(streamWriter);
        }
        File testOut = new File(outputPathName);
        Files.write(testOut.toPath(), baos.toByteArray(), StandardOpenOption.CREATE);
    }
}
