package net.frogmouth.rnd.eofff.tools.siff;

import static net.frogmouth.rnd.eofff.yuv.ColourSpace.YUV420;
import static org.testng.Assert.*;

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
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.AbstractItemProperty;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.AssociationEntry;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemPropertiesBox;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemPropertyAssociation;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemPropertyContainerBox;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.PropertyAssociation;
import net.frogmouth.rnd.eofff.imagefileformat.items.rgan.Rectangle;
import net.frogmouth.rnd.eofff.imagefileformat.items.rgan.Region;
import net.frogmouth.rnd.eofff.imagefileformat.items.rgan.RegionItem;
import net.frogmouth.rnd.eofff.imagefileformat.properties.image.ImageSpatialExtentsProperty;
import net.frogmouth.rnd.eofff.imagefileformat.properties.udes.UserDescriptionProperty;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.free.FreeBox;
import net.frogmouth.rnd.eofff.isobmff.ftyp.Brand;
import net.frogmouth.rnd.eofff.isobmff.ftyp.FileTypeBox;
import net.frogmouth.rnd.eofff.isobmff.hdlr.HandlerBox;
import net.frogmouth.rnd.eofff.isobmff.idat.ItemDataBox;
import net.frogmouth.rnd.eofff.isobmff.iinf.ItemInfoBox;
import net.frogmouth.rnd.eofff.isobmff.iloc.ILocItem;
import net.frogmouth.rnd.eofff.isobmff.iloc.ItemLocationBox;
import net.frogmouth.rnd.eofff.isobmff.infe.ItemInfoEntry;
import net.frogmouth.rnd.eofff.isobmff.iref.ItemReferenceBox;
import net.frogmouth.rnd.eofff.isobmff.iref.SingleItemReferenceBox;
import net.frogmouth.rnd.eofff.isobmff.mdat.MediaDataBox;
import net.frogmouth.rnd.eofff.isobmff.meta.ILocExtent;
import net.frogmouth.rnd.eofff.isobmff.meta.MetaBox;
import net.frogmouth.rnd.eofff.isobmff.pitm.PrimaryItemBox;
import net.frogmouth.rnd.eofff.uncompressed.cmpd.ComponentDefinition;
import net.frogmouth.rnd.eofff.uncompressed.cmpd.ComponentDefinitionBox;
import net.frogmouth.rnd.eofff.uncompressed.cpal.ComponentPaletteBox;
import net.frogmouth.rnd.eofff.uncompressed.cpal.PaletteComponent;
import net.frogmouth.rnd.eofff.uncompressed.sbpm.PixelCoordinate;
import net.frogmouth.rnd.eofff.uncompressed.sbpm.SensorBadPixelsMapBox;
import net.frogmouth.rnd.eofff.uncompressed.uncc.Component;
import net.frogmouth.rnd.eofff.uncompressed.uncc.Interleaving;
import net.frogmouth.rnd.eofff.uncompressed.uncc.SamplingType;
import net.frogmouth.rnd.eofff.uncompressed.uncc.UncompressedFrameConfigBox;
import net.frogmouth.rnd.eofff.yuv.Y4mReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

public class CreateFileTest {
    private static final Logger LOG = LoggerFactory.getLogger(CreateFileTest.class);
    private static final long MAIN_ITEM_ID = 0x1777;
    private static final long REGION_ITEM_ID = 0x1788;
    private static final int IMAGE_WIDTH = 1280;
    private static final int IMAGE_HEIGHT = 720;
    private static final int NUM_BYTES_PER_PIXEL_RGB = 3;
    private static final int NUM_BYTES_PER_PIXEL_RGBA = 4;
    private static final int NUM_BYTES_PER_PIXEL_RGB_PALETTE = 1;
    private static final int LENGTH_OF_FREEBOX_HEADER = 8;
    private static final int MDAT_START = 2000;
    private static final int IMAGE_DATA_START = MDAT_START + 8; // assumes mdat header is 8 bytes.
    private static final Color[] COLOURS =
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

    @Test
    public void writeFile_rgb3() throws IOException {
        List<Box> boxes = new ArrayList<>();
        FileTypeBox ftyp = createFileTypeBox();
        boxes.add(ftyp);
        MetaBox meta = createMetaBox_rgb3();
        boxes.add(meta);
        long lengthOfPreviousBoxes = ftyp.getSize() + meta.getSize();
        long numberOfFillBytes = MDAT_START - lengthOfPreviousBoxes - LENGTH_OF_FREEBOX_HEADER;
        FreeBox free = new FreeBox();
        free.setData(new byte[(int) numberOfFillBytes]);
        boxes.add(free);
        MediaDataBox mdat = createMediaDataBox_rgb3();
        boxes.add(mdat);
        writeBoxes(boxes, "test_siff_rgb3.mp4");
    }

    @Test
    public void writeFile_bgr() throws IOException {
        List<Box> boxes = new ArrayList<>();
        FileTypeBox ftyp = createFileTypeBox();
        boxes.add(ftyp);
        MetaBox meta = createMetaBox_bgr();
        boxes.add(meta);
        long lengthOfPreviousBoxes = ftyp.getSize() + meta.getSize();
        long numberOfFillBytes = MDAT_START - lengthOfPreviousBoxes - LENGTH_OF_FREEBOX_HEADER;
        FreeBox free = new FreeBox();
        free.setData(new byte[(int) numberOfFillBytes]);
        boxes.add(free);
        MediaDataBox mdat = createMediaDataBox_bgr();
        boxes.add(mdat);
        writeBoxes(boxes, "test_siff_bgr.mp4");
    }

    @Test
    public void writeFile_rgba() throws IOException {
        List<Box> boxes = new ArrayList<>();
        FileTypeBox ftyp = createFileTypeBox();
        boxes.add(ftyp);
        MetaBox meta = createMetaBox_rgba();
        boxes.add(meta);
        long lengthOfPreviousBoxes = ftyp.getSize() + meta.getSize();
        long numberOfFillBytes = MDAT_START - lengthOfPreviousBoxes - LENGTH_OF_FREEBOX_HEADER;
        FreeBox free = new FreeBox();
        free.setData(new byte[(int) numberOfFillBytes]);
        boxes.add(free);
        MediaDataBox mdat = createMediaDataBox_rgba();
        boxes.add(mdat);
        writeBoxes(boxes, "test_siff_rgba.mp4");
    }

    @Test
    public void writeFile_abgr() throws IOException {
        List<Box> boxes = new ArrayList<>();
        FileTypeBox ftyp = createFileTypeBox();
        boxes.add(ftyp);
        MetaBox meta = createMetaBox_abgr();
        boxes.add(meta);
        long lengthOfPreviousBoxes = ftyp.getSize() + meta.getSize();
        long numberOfFillBytes = MDAT_START - lengthOfPreviousBoxes - LENGTH_OF_FREEBOX_HEADER;
        FreeBox free = new FreeBox();
        free.setData(new byte[(int) numberOfFillBytes]);
        boxes.add(free);
        MediaDataBox mdat = createMediaDataBox_abgr();
        boxes.add(mdat);
        writeBoxes(boxes, "test_siff_abgr.mp4");
    }

    @Test
    public void writeFile_rgb_component() throws IOException {
        List<Box> boxes = new ArrayList<>();
        FileTypeBox ftyp = createFileTypeBox();
        boxes.add(ftyp);
        MetaBox meta = createMetaBox_rgb_component();
        boxes.add(meta);
        long lengthOfPreviousBoxes = ftyp.getSize() + meta.getSize();
        long numberOfFillBytes = MDAT_START - lengthOfPreviousBoxes - LENGTH_OF_FREEBOX_HEADER;
        FreeBox free = new FreeBox();
        free.setData(new byte[(int) numberOfFillBytes]);
        boxes.add(free);
        MediaDataBox mdat = createMediaDataBox_rgb_component();
        boxes.add(mdat);
        writeBoxes(boxes, "test_siff_rgb_component.mp4");
    }

    @Test
    public void writeFile_rgb_component_rgan() throws IOException {
        List<Box> boxes = new ArrayList<>();
        FileTypeBox ftyp = createFileTypeBox();
        boxes.add(ftyp);
        MetaBox meta = createMetaBox_rgb_component_rgan();
        boxes.add(meta);
        long lengthOfPreviousBoxes = ftyp.getSize() + meta.getSize();
        long numberOfFillBytes = MDAT_START - lengthOfPreviousBoxes - LENGTH_OF_FREEBOX_HEADER;
        FreeBox free = new FreeBox();
        free.setData(new byte[(int) numberOfFillBytes]);
        boxes.add(free);
        MediaDataBox mdat = createMediaDataBox_rgb_component();
        boxes.add(mdat);
        writeBoxes(boxes, "test_siff_rgb_component_rgan.mp4");
    }

    @Test
    public void writeFile_rgb_palette() throws IOException {
        BufferedImage image =
                new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_BYTE_INDEXED);
        drawColouredRectangles(image);
        ImageIO.write(image, "PNG", new File("ref_byte_indexed.png"));
        List<Box> boxes = new ArrayList<>();
        FileTypeBox ftyp = createFileTypeBox();
        boxes.add(ftyp);
        MetaBox meta = createMetaBox_rgb_palette(image.getColorModel());
        boxes.add(meta);
        long lengthOfPreviousBoxes = ftyp.getSize() + meta.getSize();
        long numberOfFillBytes = MDAT_START - lengthOfPreviousBoxes - LENGTH_OF_FREEBOX_HEADER;
        FreeBox free = new FreeBox();
        free.setData(new byte[(int) numberOfFillBytes]);
        boxes.add(free);
        MediaDataBox mdat = createMediaDataBox_rgb_palette(image);
        boxes.add(mdat);
        writeBoxes(boxes, "test_siff_rgb_palette.mp4");
    }

    @Test
    public void writeFile_rgb565_block_be() throws IOException {
        List<Box> boxes = buildBoxes_rgb565(ByteOrder.BIG_ENDIAN);
        writeBoxes(boxes, "test_siff_rgb565_block_be.mp4");
    }

    @Test
    public void writeFile_rgb565_block_le() throws IOException {
        List<Box> boxes = buildBoxes_rgb565(ByteOrder.LITTLE_ENDIAN);
        writeBoxes(boxes, "test_siff_rgb565_block_le.mp4");
    }

    @Test
    public void writeFile_rgb555_block_be() throws IOException {
        List<Box> boxes = buildBoxes_rgb555(ByteOrder.BIG_ENDIAN, false);
        writeBoxes(boxes, "test_siff_rgb555_block_be.mp4");
    }

    @Test
    public void writeFile_rgb555_block_le() throws IOException {
        List<Box> boxes = buildBoxes_rgb555(ByteOrder.LITTLE_ENDIAN, false);
        writeBoxes(boxes, "test_siff_rgb555_block_le.mp4");
    }

    @Test
    public void writeFile_rgb555_block_be_pad_lsb() throws IOException {
        List<Box> boxes = buildBoxes_rgb555(ByteOrder.BIG_ENDIAN, true);
        writeBoxes(boxes, "test_siff_rgb555_block_be_pad_lsb.mp4");
    }

    @Test
    public void writeFile_rgb555_block_le_pad_lsb() throws IOException {
        List<Box> boxes = buildBoxes_rgb555(ByteOrder.LITTLE_ENDIAN, true);
        writeBoxes(boxes, "test_siff_rgb555_block_le_pad_lsb.mp4");
    }

    @Test
    public void writeFile_yuv444() throws IOException {
        writeFileYUV("/home/bradh/yuvdata/in_to_tree_444_720p50.y4m", "test_siff_yuv444.mp4");
    }

    @Test
    public void writeFile_v308() throws IOException {
        writeFileYUV(
                "/home/bradh/yuvdata/in_to_tree_444_720p50.y4m",
                "test_siff_v308.mp4",
                new FourCC("v308"));
    }

    @Test
    public void writeFile_yuv422() throws IOException {
        writeFileYUV("/home/bradh/yuvdata/controlled_burn_1080p.y4m", "test_siff_yuv422.mp4");
    }

    @Test
    public void writeFile_2vuy() throws IOException {
        writeFileYUV(
                "/home/bradh/yuvdata/controlled_burn_1080p.y4m",
                "test_siff_2vuy.mp4",
                new FourCC("2vuy"));
    }

    @Test
    public void writeFile_yuv2() throws IOException {
        writeFileYUV(
                "/home/bradh/yuvdata/controlled_burn_1080p.y4m",
                "test_siff_yuv2.mp4",
                new FourCC("yuv2"));
    }

    @Test
    public void writeFile_yvyu() throws IOException {
        writeFileYUV(
                "/home/bradh/yuvdata/controlled_burn_1080p.y4m",
                "test_siff_yvyu.mp4",
                new FourCC("yvyu"));
    }

    @Test
    public void writeFile_vyuy() throws IOException {
        writeFileYUV(
                "/home/bradh/yuvdata/controlled_burn_1080p.y4m",
                "test_siff_vyuy.mp4",
                new FourCC("vyuy"));
    }

    @Test
    public void writeFile_i420() throws IOException {
        writeFileYUV(
                "/home/bradh/yuvdata/pedestrian_area_1080p25.y4m",
                "test_siff_i420.mp4",
                new FourCC("i420"));
    }

    @Test
    public void writeFile_nv12() throws IOException {
        writeFileYUV(
                "/home/bradh/yuvdata/pedestrian_area_1080p25.y4m",
                "test_siff_nv12.mp4",
                new FourCC("nv12"));
    }

    @Test
    public void writeFile_nv21() throws IOException {
        writeFileYUV(
                "/home/bradh/yuvdata/pedestrian_area_1080p25.y4m",
                "test_siff_nv21.mp4",
                new FourCC("nv21"));
    }

    @Test
    public void writeFile_bgr_sbpm() throws IOException {
        List<Box> boxes = new ArrayList<>();
        FileTypeBox ftyp = createFileTypeBox();
        boxes.add(ftyp);
        MetaBox meta = createMetaBox_bgr_sbpm();
        boxes.add(meta);
        long lengthOfPreviousBoxes = ftyp.getSize() + meta.getSize();
        long numberOfFillBytes = MDAT_START - lengthOfPreviousBoxes - LENGTH_OF_FREEBOX_HEADER;
        FreeBox free = new FreeBox();
        free.setData(new byte[(int) numberOfFillBytes]);
        boxes.add(free);
        MediaDataBox mdat = createMediaDataBox_bgr_sbpm();
        boxes.add(mdat);
        writeBoxes(boxes, "test_siff_bgr_sbpm.mp4");
    }

    private void writeFileYUV(String inFile, String outFile) throws IOException {
        Path path = Path.of(inFile);
        SeekableByteChannel fileChannel = FileChannel.open(path, StandardOpenOption.READ);
        Y4mReader reader = new Y4mReader(fileChannel);
        reader.readHeader();
        List<Box> boxes = new ArrayList<>();
        FileTypeBox ftyp = createFileTypeBox();
        boxes.add(ftyp);
        MetaBox meta = createMetaBox_yuv(reader, null);
        boxes.add(meta);
        long lengthOfPreviousBoxes = ftyp.getSize() + meta.getSize();
        long numberOfFillBytes = MDAT_START - lengthOfPreviousBoxes - LENGTH_OF_FREEBOX_HEADER;
        FreeBox free = new FreeBox();
        free.setData(new byte[(int) numberOfFillBytes]);
        boxes.add(free);
        MediaDataBox mdat = createMediaDataBox_yuv(reader, null);
        boxes.add(mdat);
        writeBoxes(boxes, outFile);
    }

    private void writeFileYUV(String inFile, String outFile, FourCC profile) throws IOException {
        Path path = Path.of(inFile);
        SeekableByteChannel fileChannel = FileChannel.open(path, StandardOpenOption.READ);
        Y4mReader reader = new Y4mReader(fileChannel);
        reader.readHeader();
        List<Box> boxes = new ArrayList<>();
        FileTypeBox ftyp = createFileTypeBox();
        boxes.add(ftyp);
        MetaBox meta = createMetaBox_yuv(reader, profile);
        boxes.add(meta);
        long lengthOfPreviousBoxes = ftyp.getSize() + meta.getSize();
        long numberOfFillBytes = MDAT_START - lengthOfPreviousBoxes - LENGTH_OF_FREEBOX_HEADER;
        FreeBox free = new FreeBox();
        free.setData(new byte[(int) numberOfFillBytes]);
        boxes.add(free);
        MediaDataBox mdat = createMediaDataBox_yuv(reader, profile);
        boxes.add(mdat);
        writeBoxes(boxes, outFile);
    }

    private List<Box> buildBoxes_rgb565(ByteOrder blockEndian) throws IOException {
        List<Box> boxes = new ArrayList<>();
        FileTypeBox ftyp = createFileTypeBox();
        boxes.add(ftyp);
        MetaBox meta = createMetaBox_rgb565(blockEndian);
        boxes.add(meta);
        long lengthOfPreviousBoxes = ftyp.getSize() + meta.getSize();
        long numberOfFillBytes = MDAT_START - lengthOfPreviousBoxes - LENGTH_OF_FREEBOX_HEADER;
        FreeBox free = new FreeBox();
        free.setData(new byte[(int) numberOfFillBytes]);
        boxes.add(free);
        MediaDataBox mdat = createMediaDataBox_rgb565(blockEndian);
        boxes.add(mdat);
        return boxes;
    }

    private List<Box> buildBoxes_rgb555(ByteOrder blockEndian, boolean padLSB) throws IOException {
        List<Box> boxes = new ArrayList<>();
        FileTypeBox ftyp = createFileTypeBox();
        boxes.add(ftyp);
        MetaBox meta = createMetaBox_rgb555(blockEndian, padLSB);
        boxes.add(meta);
        long lengthOfPreviousBoxes = ftyp.getSize() + meta.getSize();
        long numberOfFillBytes = MDAT_START - lengthOfPreviousBoxes - LENGTH_OF_FREEBOX_HEADER;
        FreeBox free = new FreeBox();
        free.setData(new byte[(int) numberOfFillBytes]);
        boxes.add(free);
        MediaDataBox mdat = createMediaDataBox_rgb555(blockEndian, padLSB);
        boxes.add(mdat);
        return boxes;
    }

    private void writeBoxes(List<Box> boxes, String outputPathName) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        for (Box box : boxes) {
            if (box instanceof FileTypeBox ftyp) {
                validateFileTypeBox(ftyp);
            }
            if (box instanceof MetaBox metaBox) {
                validateMetaBox(metaBox);
            }
            box.writeTo(streamWriter);
        }
        File testOut = new File(outputPathName);
        Files.write(testOut.toPath(), baos.toByteArray(), StandardOpenOption.CREATE);
    }

    private FileTypeBox createFileTypeBox() {
        FileTypeBox fileTypeBox = new FileTypeBox(new FourCC("ftyp"));
        fileTypeBox.setMajorBrand(new Brand("mif1"));
        fileTypeBox.setMinorVersion(0);
        fileTypeBox.addCompatibleBrand(new Brand("mif1"));
        fileTypeBox.addCompatibleBrand(new Brand("mif2"));
        fileTypeBox.addCompatibleBrand(new Brand("ns01"));
        fileTypeBox.addCompatibleBrand(new Brand("unif"));
        return fileTypeBox;
    }

    private MetaBox createMetaBox_rgb3() {
        MetaBox meta = new MetaBox();
        List<Box> boxes = new ArrayList<>();
        boxes.add(makeHandlerBox());
        boxes.add(makePrimaryItemBox());
        boxes.add(makeItemInfoBox());
        boxes.add(makeItemLocationBox_rgb3());
        boxes.add(makeItemPropertiesBox_rgb3());
        meta.addNestedBoxes(boxes);
        return meta;
    }

    private MetaBox createMetaBox_rgb_component() {
        MetaBox meta = new MetaBox();
        List<Box> boxes = new ArrayList<>();
        boxes.add(makeHandlerBox());
        boxes.add(makePrimaryItemBox());
        boxes.add(makeItemInfoBox());
        boxes.add(makeItemLocationBox_rgb_component());
        boxes.add(makeItemPropertiesBox_rgb_component());
        meta.addNestedBoxes(boxes);
        return meta;
    }

    private MetaBox createMetaBox_rgb_component_rgan() throws IOException {
        MetaBox meta = new MetaBox();
        List<Box> boxes = new ArrayList<>();
        boxes.add(makeHandlerBox());
        boxes.add(makePrimaryItemBox());
        boxes.add(makeItemInfoBox_with_rgan());
        boxes.add(makeItemLocationBox_rgb_component_with_rgan());
        boxes.add(makeItemDataBox_with_rgan());
        boxes.add(makeItemPropertiesBox_rgb_component_with_rgan());
        boxes.add(makeItemReferenceBox_with_rgan());
        meta.addNestedBoxes(boxes);
        return meta;
    }

    private MetaBox createMetaBox_rgba() {
        MetaBox meta = new MetaBox();
        List<Box> boxes = new ArrayList<>();
        boxes.add(makeHandlerBox());
        boxes.add(makePrimaryItemBox());
        boxes.add(makeItemInfoBox());
        boxes.add(makeItemLocationBox_rgba());
        boxes.add(makeItemPropertiesBox_rgba());
        meta.addNestedBoxes(boxes);
        return meta;
    }

    private MetaBox createMetaBox_abgr() {
        MetaBox meta = new MetaBox();
        List<Box> boxes = new ArrayList<>();
        boxes.add(makeHandlerBox());
        boxes.add(makePrimaryItemBox());
        boxes.add(makeItemInfoBox());
        boxes.add(makeItemLocationBox_abgr());
        boxes.add(makeItemPropertiesBox_abgr());
        meta.addNestedBoxes(boxes);
        return meta;
    }

    private MetaBox createMetaBox_bgr() {
        MetaBox meta = new MetaBox();
        List<Box> boxes = new ArrayList<>();
        boxes.add(makeHandlerBox());
        boxes.add(makePrimaryItemBox());
        boxes.add(makeItemInfoBox());
        boxes.add(makeItemLocationBox_bgr());
        boxes.add(makeItemPropertiesBox_bgr());
        meta.addNestedBoxes(boxes);
        return meta;
    }

    private MetaBox createMetaBox_bgr_sbpm() {
        MetaBox meta = new MetaBox();
        List<Box> boxes = new ArrayList<>();
        boxes.add(makeHandlerBox());
        boxes.add(makePrimaryItemBox());
        boxes.add(makeItemInfoBox());
        boxes.add(makeItemLocationBox_bgr());
        boxes.add(makeItemPropertiesBox_bgr_sbpm());
        meta.addNestedBoxes(boxes);
        return meta;
    }

    private MetaBox createMetaBox_rgb_palette(ColorModel colourModel) {
        MetaBox meta = new MetaBox();
        List<Box> boxes = new ArrayList<>();
        boxes.add(makeHandlerBox());
        boxes.add(makePrimaryItemBox());
        boxes.add(makeItemInfoBox());
        boxes.add(makeItemLocationBox_rgb_palette());
        boxes.add(makeItemPropertiesBox_rgb_palette(colourModel));
        meta.addNestedBoxes(boxes);
        return meta;
    }

    private MetaBox createMetaBox_rgb565(ByteOrder blockEndian) {
        MetaBox meta = new MetaBox();
        List<Box> boxes = new ArrayList<>();
        boxes.add(makeHandlerBox());
        boxes.add(makePrimaryItemBox());
        boxes.add(makeItemInfoBox());
        boxes.add(makeItemLocationBox_rgb565());
        boxes.add(makeItemPropertiesBox_rgb565(blockEndian));
        meta.addNestedBoxes(boxes);
        return meta;
    }

    private MetaBox createMetaBox_rgb555(ByteOrder blockEndian, boolean padLSB) {
        MetaBox meta = new MetaBox();
        List<Box> boxes = new ArrayList<>();
        boxes.add(makeHandlerBox());
        boxes.add(makePrimaryItemBox());
        boxes.add(makeItemInfoBox());
        boxes.add(makeItemLocationBox_rgb555());
        boxes.add(makeItemPropertiesBox_rgb555(blockEndian, padLSB));
        meta.addNestedBoxes(boxes);
        return meta;
    }

    private MetaBox createMetaBox_yuv(Y4mReader reader, FourCC profile) {
        MetaBox meta = new MetaBox();
        List<Box> boxes = new ArrayList<>();
        boxes.add(makeHandlerBox());
        boxes.add(makePrimaryItemBox());
        boxes.add(makeItemInfoBox());
        boxes.add(makeItemLocationBox_yuv(reader));
        boxes.add(makeItemPropertiesBox_yuv(reader, profile));
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
        infe0.setItemName("Uncompressed Image");
        iinf.addItem(infe0);
        return iinf;
    }

    private ItemInfoBox makeItemInfoBox_with_rgan() {
        ItemInfoBox iinf = makeItemInfoBox();
        ItemInfoEntry infe1 = new ItemInfoEntry();
        infe1.setVersion(2);
        infe1.setItemID(REGION_ITEM_ID);
        FourCC rgan = new FourCC("rgan");
        infe1.setItemType(rgan.asUnsigned());
        infe1.setItemName("Region Annotation");
        iinf.addItem(infe1);
        return iinf;
    }

    private ItemLocationBox makeItemLocationBox_bgr() {
        return makeItemLocationBox_rgb3();
    }

    private ItemLocationBox makeItemLocationBox_rgb_component() {
        return makeItemLocationBox_rgb3();
    }

    private ItemLocationBox makeItemLocationBox_rgb_component_with_rgan() throws IOException {
        ItemLocationBox iloc = makeItemLocationBox_rgb3();
        ILocItem regionItemLocation = new ILocItem();
        regionItemLocation.setConstructionMethod(1);
        regionItemLocation.setItemId(REGION_ITEM_ID);
        ILocExtent rganExtent = new ILocExtent();
        rganExtent.setExtentIndex(0);
        rganExtent.setExtentOffset(0);
        rganExtent.setExtentLength(getRegionAnnotationBytes().length);
        regionItemLocation.addExtent(rganExtent);
        iloc.addItem(regionItemLocation);
        return iloc;
    }

    private ItemDataBox makeItemDataBox_with_rgan() throws IOException {
        ItemDataBox idat = new ItemDataBox();
        idat.setData(this.getRegionAnnotationBytes());
        return idat;
    }

    private ItemLocationBox makeItemLocationBox_rgb_palette() {
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
        mainItemExtent.setExtentLength(
                IMAGE_HEIGHT * IMAGE_WIDTH * NUM_BYTES_PER_PIXEL_RGB_PALETTE);
        mainItemLocation.addExtent(mainItemExtent);
        iloc.addItem(mainItemLocation);
        return iloc;
    }

    private ItemLocationBox makeItemLocationBox_rgb565() {
        return makeItemLocationBox_two_byte_per_pixel();
    }

    private ItemLocationBox makeItemLocationBox_rgb555() {
        return makeItemLocationBox_two_byte_per_pixel();
    }

    private ItemLocationBox makeItemLocationBox_yuv(Y4mReader reader) {
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
        mainItemExtent.setExtentLength(reader.getFrameSizeBytes());
        mainItemLocation.addExtent(mainItemExtent);
        iloc.addItem(mainItemLocation);
        return iloc;
    }

    private ItemLocationBox makeItemLocationBox_two_byte_per_pixel() {
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
        mainItemExtent.setExtentLength(IMAGE_HEIGHT * IMAGE_WIDTH * 2);
        mainItemLocation.addExtent(mainItemExtent);
        iloc.addItem(mainItemLocation);
        return iloc;
    }

    private ItemLocationBox makeItemLocationBox_rgb3() {
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
        mainItemExtent.setExtentLength(IMAGE_HEIGHT * IMAGE_WIDTH * NUM_BYTES_PER_PIXEL_RGB);
        mainItemLocation.addExtent(mainItemExtent);
        iloc.addItem(mainItemLocation);
        return iloc;
    }

    private ItemLocationBox makeItemLocationBox_rgba() {
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
        mainItemExtent.setExtentLength(IMAGE_HEIGHT * IMAGE_WIDTH * NUM_BYTES_PER_PIXEL_RGBA);
        mainItemLocation.addExtent(mainItemExtent);
        iloc.addItem(mainItemLocation);
        return iloc;
    }

    private ItemLocationBox makeItemLocationBox_abgr() {
        return makeItemLocationBox_rgba();
    }

    private Box makeItemPropertiesBox_rgb3() {
        ItemPropertiesBox iprp = new ItemPropertiesBox();
        ItemPropertyContainerBox ipco = new ItemPropertyContainerBox();
        ipco.addProperty(makeComponentDefinitionBox_rgb3());
        ipco.addProperty(makeUncompressedFrameConfigBox_rgb3());
        ipco.addProperty(makeImageSpatialExtentsProperty());
        iprp.setItemProperties(ipco);
        ItemPropertyAssociation componentDefinitionAssociation = new ItemPropertyAssociation();
        AssociationEntry componentDefinitionAssociationEntry = new AssociationEntry();
        componentDefinitionAssociationEntry.setItemId(MAIN_ITEM_ID);

        PropertyAssociation associationToComponentDefinitionBox = new PropertyAssociation();
        associationToComponentDefinitionBox.setPropertyIndex(1);
        associationToComponentDefinitionBox.setEssential(true);
        componentDefinitionAssociationEntry.addAssociation(associationToComponentDefinitionBox);

        PropertyAssociation associationToUncompressedFrameConfigBox = new PropertyAssociation();
        associationToUncompressedFrameConfigBox.setPropertyIndex(2);
        associationToUncompressedFrameConfigBox.setEssential(true);
        componentDefinitionAssociationEntry.addAssociation(associationToUncompressedFrameConfigBox);

        PropertyAssociation associationToImageSpatialExtentsProperty = new PropertyAssociation();
        associationToImageSpatialExtentsProperty.setPropertyIndex(3);
        associationToImageSpatialExtentsProperty.setEssential(false);
        componentDefinitionAssociationEntry.addAssociation(
                associationToImageSpatialExtentsProperty);

        componentDefinitionAssociation.addEntry(componentDefinitionAssociationEntry);
        iprp.addItemPropertyAssociation(componentDefinitionAssociation);
        return iprp;
    }

    private Box makeItemPropertiesBox_rgb_component() {
        ItemPropertiesBox iprp = new ItemPropertiesBox();
        ItemPropertyContainerBox ipco = new ItemPropertyContainerBox();
        ipco.addProperty(makeComponentDefinitionBox_rgb3());
        ipco.addProperty(makeUncompressedFrameConfigBox_rgb_component());
        ipco.addProperty(makeImageSpatialExtentsProperty());
        iprp.setItemProperties(ipco);
        ItemPropertyAssociation componentDefinitionAssociation = new ItemPropertyAssociation();
        AssociationEntry componentDefinitionAssociationEntry = new AssociationEntry();
        componentDefinitionAssociationEntry.setItemId(MAIN_ITEM_ID);

        PropertyAssociation associationToComponentDefinitionBox = new PropertyAssociation();
        associationToComponentDefinitionBox.setPropertyIndex(1);
        associationToComponentDefinitionBox.setEssential(true);
        componentDefinitionAssociationEntry.addAssociation(associationToComponentDefinitionBox);

        PropertyAssociation associationToUncompressedFrameConfigBox = new PropertyAssociation();
        associationToUncompressedFrameConfigBox.setPropertyIndex(2);
        associationToUncompressedFrameConfigBox.setEssential(true);
        componentDefinitionAssociationEntry.addAssociation(associationToUncompressedFrameConfigBox);

        PropertyAssociation associationToImageSpatialExtentsProperty = new PropertyAssociation();
        associationToImageSpatialExtentsProperty.setPropertyIndex(3);
        associationToImageSpatialExtentsProperty.setEssential(false);
        componentDefinitionAssociationEntry.addAssociation(
                associationToImageSpatialExtentsProperty);

        componentDefinitionAssociation.addEntry(componentDefinitionAssociationEntry);
        iprp.addItemPropertyAssociation(componentDefinitionAssociation);
        return iprp;
    }

    private Box makeItemPropertiesBox_rgb_component_with_rgan() {
        ItemPropertiesBox iprp = new ItemPropertiesBox();
        ItemPropertyContainerBox ipco = new ItemPropertyContainerBox();
        ipco.addProperty(makeComponentDefinitionBox_rgb3());
        ipco.addProperty(makeUncompressedFrameConfigBox_rgb_component());
        ipco.addProperty(makeImageSpatialExtentsProperty());
        ipco.addProperty(makeUserDescription_rgan());
        ipco.addProperty(makeUserDescription());
        iprp.setItemProperties(ipco);

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
            associationToImageSpatialExtentsProperty.setEssential(false);
            mainItemAssociations.addAssociation(associationToImageSpatialExtentsProperty);

            PropertyAssociation associationToUserDescription = new PropertyAssociation();
            associationToUserDescription.setPropertyIndex(5);
            associationToUserDescription.setEssential(false);
            mainItemAssociations.addAssociation(associationToUserDescription);

            itemPropertyAssociation.addEntry(mainItemAssociations);
        }
        {
            AssociationEntry regionAssociationEntry = new AssociationEntry();
            regionAssociationEntry.setItemId(REGION_ITEM_ID);
            PropertyAssociation associationToRegionUserDescription = new PropertyAssociation();
            associationToRegionUserDescription.setPropertyIndex(4);
            associationToRegionUserDescription.setEssential(false);
            regionAssociationEntry.addAssociation(associationToRegionUserDescription);
            itemPropertyAssociation.addEntry(regionAssociationEntry);
        }
        iprp.addItemPropertyAssociation(itemPropertyAssociation);

        return iprp;
    }

    private Box makeItemReferenceBox_with_rgan() {
        ItemReferenceBox iref = new ItemReferenceBox();
        SingleItemReferenceBox rgan_csdc_primary_item =
                new SingleItemReferenceBox(new FourCC("cdsc"));
        rgan_csdc_primary_item.setFromItemId(REGION_ITEM_ID);
        rgan_csdc_primary_item.addReference(MAIN_ITEM_ID);
        iref.addItem(rgan_csdc_primary_item);
        return iref;
    }

    private Box makeItemPropertiesBox_rgb565(ByteOrder blockEndian) {
        ItemPropertiesBox iprp = new ItemPropertiesBox();
        ItemPropertyContainerBox ipco = new ItemPropertyContainerBox();
        ipco.addProperty(makeComponentDefinitionBox_rgb3());
        ipco.addProperty(makeUncompressedFrameConfigBox_rgb565(blockEndian));
        ipco.addProperty(makeImageSpatialExtentsProperty());
        iprp.setItemProperties(ipco);
        ItemPropertyAssociation componentDefinitionAssociation = new ItemPropertyAssociation();
        AssociationEntry componentDefinitionAssociationEntry = new AssociationEntry();
        componentDefinitionAssociationEntry.setItemId(MAIN_ITEM_ID);

        PropertyAssociation associationToComponentDefinitionBox = new PropertyAssociation();
        associationToComponentDefinitionBox.setPropertyIndex(1);
        associationToComponentDefinitionBox.setEssential(true);
        componentDefinitionAssociationEntry.addAssociation(associationToComponentDefinitionBox);

        PropertyAssociation associationToUncompressedFrameConfigBox = new PropertyAssociation();
        associationToUncompressedFrameConfigBox.setPropertyIndex(2);
        associationToUncompressedFrameConfigBox.setEssential(true);
        componentDefinitionAssociationEntry.addAssociation(associationToUncompressedFrameConfigBox);

        PropertyAssociation associationToImageSpatialExtentsProperty = new PropertyAssociation();
        associationToImageSpatialExtentsProperty.setPropertyIndex(3);
        associationToImageSpatialExtentsProperty.setEssential(false);
        componentDefinitionAssociationEntry.addAssociation(
                associationToImageSpatialExtentsProperty);

        componentDefinitionAssociation.addEntry(componentDefinitionAssociationEntry);
        iprp.addItemPropertyAssociation(componentDefinitionAssociation);
        return iprp;
    }

    private Box makeItemPropertiesBox_rgb555(ByteOrder blockEndian, boolean padLSB) {
        ItemPropertiesBox iprp = new ItemPropertiesBox();
        ItemPropertyContainerBox ipco = new ItemPropertyContainerBox();
        ipco.addProperty(makeComponentDefinitionBox_rgb3());
        ipco.addProperty(makeUncompressedFrameConfigBox_rgb555(blockEndian, padLSB));
        ipco.addProperty(makeImageSpatialExtentsProperty());
        iprp.setItemProperties(ipco);
        ItemPropertyAssociation componentDefinitionAssociation = new ItemPropertyAssociation();
        AssociationEntry componentDefinitionAssociationEntry = new AssociationEntry();
        componentDefinitionAssociationEntry.setItemId(MAIN_ITEM_ID);

        PropertyAssociation associationToComponentDefinitionBox = new PropertyAssociation();
        associationToComponentDefinitionBox.setPropertyIndex(1);
        associationToComponentDefinitionBox.setEssential(true);
        componentDefinitionAssociationEntry.addAssociation(associationToComponentDefinitionBox);

        PropertyAssociation associationToUncompressedFrameConfigBox = new PropertyAssociation();
        associationToUncompressedFrameConfigBox.setPropertyIndex(2);
        associationToUncompressedFrameConfigBox.setEssential(true);
        componentDefinitionAssociationEntry.addAssociation(associationToUncompressedFrameConfigBox);

        PropertyAssociation associationToImageSpatialExtentsProperty = new PropertyAssociation();
        associationToImageSpatialExtentsProperty.setPropertyIndex(3);
        associationToImageSpatialExtentsProperty.setEssential(false);
        componentDefinitionAssociationEntry.addAssociation(
                associationToImageSpatialExtentsProperty);

        componentDefinitionAssociation.addEntry(componentDefinitionAssociationEntry);
        iprp.addItemPropertyAssociation(componentDefinitionAssociation);
        return iprp;
    }

    private Box makeItemPropertiesBox_bgr() {
        ItemPropertiesBox iprp = new ItemPropertiesBox();
        ItemPropertyContainerBox ipco = new ItemPropertyContainerBox();
        ipco.addProperty(makeComponentDefinitionBox_rgb3());
        ipco.addProperty(makeUncompressedFrameConfigBox_bgr());
        ipco.addProperty(makeImageSpatialExtentsProperty());
        iprp.setItemProperties(ipco);
        ItemPropertyAssociation componentDefinitionAssociation = new ItemPropertyAssociation();
        AssociationEntry componentDefinitionAssociationEntry = new AssociationEntry();
        componentDefinitionAssociationEntry.setItemId(MAIN_ITEM_ID);

        PropertyAssociation associationToComponentDefinitionBox = new PropertyAssociation();
        associationToComponentDefinitionBox.setPropertyIndex(1);
        associationToComponentDefinitionBox.setEssential(true);
        componentDefinitionAssociationEntry.addAssociation(associationToComponentDefinitionBox);

        PropertyAssociation associationToUncompressedFrameConfigBox = new PropertyAssociation();
        associationToUncompressedFrameConfigBox.setPropertyIndex(2);
        associationToUncompressedFrameConfigBox.setEssential(true);
        componentDefinitionAssociationEntry.addAssociation(associationToUncompressedFrameConfigBox);

        PropertyAssociation associationToImageSpatialExtentsProperty = new PropertyAssociation();
        associationToImageSpatialExtentsProperty.setPropertyIndex(3);
        associationToImageSpatialExtentsProperty.setEssential(false);
        componentDefinitionAssociationEntry.addAssociation(
                associationToImageSpatialExtentsProperty);

        componentDefinitionAssociation.addEntry(componentDefinitionAssociationEntry);
        iprp.addItemPropertyAssociation(componentDefinitionAssociation);
        return iprp;
    }

    private Box makeItemPropertiesBox_bgr_sbpm() {
        ItemPropertiesBox iprp = new ItemPropertiesBox();
        ItemPropertyContainerBox ipco = new ItemPropertyContainerBox();
        ipco.addProperty(makeComponentDefinitionBox_rgb3());
        ipco.addProperty(makeUncompressedFrameConfigBox_bgr());
        ipco.addProperty(makeImageSpatialExtentsProperty());
        ipco.addProperty(makeSensorBadPixelsMap());
        ipco.addProperty(makeUserDescription());
        iprp.setItemProperties(ipco);
        ItemPropertyAssociation componentDefinitionAssociation = new ItemPropertyAssociation();
        AssociationEntry componentDefinitionAssociationEntry = new AssociationEntry();
        componentDefinitionAssociationEntry.setItemId(MAIN_ITEM_ID);

        PropertyAssociation associationToComponentDefinitionBox = new PropertyAssociation();
        associationToComponentDefinitionBox.setPropertyIndex(1);
        associationToComponentDefinitionBox.setEssential(true);
        componentDefinitionAssociationEntry.addAssociation(associationToComponentDefinitionBox);

        PropertyAssociation associationToUncompressedFrameConfigBox = new PropertyAssociation();
        associationToUncompressedFrameConfigBox.setPropertyIndex(2);
        associationToUncompressedFrameConfigBox.setEssential(true);
        componentDefinitionAssociationEntry.addAssociation(associationToUncompressedFrameConfigBox);

        PropertyAssociation associationToImageSpatialExtentsProperty = new PropertyAssociation();
        associationToImageSpatialExtentsProperty.setPropertyIndex(3);
        associationToImageSpatialExtentsProperty.setEssential(false);
        componentDefinitionAssociationEntry.addAssociation(
                associationToImageSpatialExtentsProperty);

        PropertyAssociation associationToSensorBadPixelsMapBox = new PropertyAssociation();
        associationToSensorBadPixelsMapBox.setPropertyIndex(4);
        associationToSensorBadPixelsMapBox.setEssential(false);
        componentDefinitionAssociationEntry.addAssociation(associationToSensorBadPixelsMapBox);

        PropertyAssociation associationToUserDescription = new PropertyAssociation();
        associationToUserDescription.setPropertyIndex(5);
        associationToUserDescription.setEssential(false);
        componentDefinitionAssociationEntry.addAssociation(associationToUserDescription);

        componentDefinitionAssociation.addEntry(componentDefinitionAssociationEntry);

        iprp.addItemPropertyAssociation(componentDefinitionAssociation);
        return iprp;
    }

    private AbstractItemProperty makeSensorBadPixelsMap() {
        SensorBadPixelsMapBox sbpm = new SensorBadPixelsMapBox();
        sbpm.addComponentIndex(0);
        sbpm.addComponentIndex(1);
        sbpm.addComponentIndex(2);
        sbpm.setCorrectionApplied(false);
        sbpm.addBadRow(100);
        sbpm.addBadRow(101);
        sbpm.addBadRow(102);
        sbpm.addBadRow(105);
        sbpm.addBadRow(104);
        sbpm.addBadRow(103);
        sbpm.addBadColumn(80);
        sbpm.addBadColumn(81);
        sbpm.addBadColumn(82);
        sbpm.addBadPixel(new PixelCoordinate(120, 160));
        sbpm.addBadPixel(new PixelCoordinate(120, 161));
        sbpm.addBadPixel(new PixelCoordinate(121, 160));
        sbpm.addBadPixel(new PixelCoordinate(121, 161));
        sbpm.addBadPixel(new PixelCoordinate(122, 160));
        sbpm.addBadPixel(new PixelCoordinate(122, 161));
        sbpm.addBadPixel(new PixelCoordinate(123, 160));
        sbpm.addBadPixel(new PixelCoordinate(123, 161));
        return sbpm;
    }

    private AbstractItemProperty makeUserDescription() {
        UserDescriptionProperty udes = new UserDescriptionProperty();
        udes.setLang("en-AU");
        udes.setDescriptiveName("Coloured squares");
        udes.setDescription("Four by Three grid with some bad pixels");
        udes.setTags("udes,grid");
        return udes;
    }

    private AbstractItemProperty makeUserDescription_rgan() {
        UserDescriptionProperty udes = new UserDescriptionProperty();
        udes.setLang("en-AU");
        udes.setDescriptiveName("Yellow square");
        udes.setDescription("The square in the third row, second column is yellow");
        udes.setTags("udes,yellow,single square");
        return udes;
    }

    private Box makeItemPropertiesBox_rgba() {
        ItemPropertiesBox iprp = new ItemPropertiesBox();
        ItemPropertyContainerBox ipco = new ItemPropertyContainerBox();
        ipco.addProperty(makeComponentDefinitionBox_rgba());
        ipco.addProperty(makeUncompressedFrameConfigBox_rgba());
        ipco.addProperty(makeImageSpatialExtentsProperty());
        iprp.setItemProperties(ipco);
        ItemPropertyAssociation componentDefinitionAssociation = new ItemPropertyAssociation();
        AssociationEntry componentDefinitionAssociationEntry = new AssociationEntry();
        componentDefinitionAssociationEntry.setItemId(MAIN_ITEM_ID);

        PropertyAssociation associationToComponentDefinitionBox = new PropertyAssociation();
        associationToComponentDefinitionBox.setPropertyIndex(1);
        associationToComponentDefinitionBox.setEssential(true);
        componentDefinitionAssociationEntry.addAssociation(associationToComponentDefinitionBox);

        PropertyAssociation associationToUncompressedFrameConfigBox = new PropertyAssociation();
        associationToUncompressedFrameConfigBox.setPropertyIndex(2);
        associationToUncompressedFrameConfigBox.setEssential(true);
        componentDefinitionAssociationEntry.addAssociation(associationToUncompressedFrameConfigBox);

        PropertyAssociation associationToImageSpatialExtentsProperty = new PropertyAssociation();
        associationToImageSpatialExtentsProperty.setPropertyIndex(3);
        associationToImageSpatialExtentsProperty.setEssential(false);
        componentDefinitionAssociationEntry.addAssociation(
                associationToImageSpatialExtentsProperty);

        componentDefinitionAssociation.addEntry(componentDefinitionAssociationEntry);
        iprp.addItemPropertyAssociation(componentDefinitionAssociation);
        return iprp;
    }

    private Box makeItemPropertiesBox_rgb_palette(ColorModel colourModel) {
        ItemPropertiesBox iprp = new ItemPropertiesBox();
        ItemPropertyContainerBox ipco = new ItemPropertyContainerBox();
        ipco.addProperty(makeComponentDefinitionBox_rgb_palette());
        ipco.addProperty(makeUncompressedFrameConfigBox_rgb_palette());
        ipco.addProperty(makeImageSpatialExtentsProperty());
        ipco.addProperty(makeComponentPaletteBox(colourModel));
        iprp.setItemProperties(ipco);
        ItemPropertyAssociation componentDefinitionAssociation = new ItemPropertyAssociation();
        AssociationEntry componentDefinitionAssociationEntry = new AssociationEntry();
        componentDefinitionAssociationEntry.setItemId(MAIN_ITEM_ID);

        PropertyAssociation associationToComponentDefinitionBox = new PropertyAssociation();
        associationToComponentDefinitionBox.setPropertyIndex(1);
        associationToComponentDefinitionBox.setEssential(true);
        componentDefinitionAssociationEntry.addAssociation(associationToComponentDefinitionBox);

        PropertyAssociation associationToUncompressedFrameConfigBox = new PropertyAssociation();
        associationToUncompressedFrameConfigBox.setPropertyIndex(2);
        associationToUncompressedFrameConfigBox.setEssential(true);
        componentDefinitionAssociationEntry.addAssociation(associationToUncompressedFrameConfigBox);

        PropertyAssociation associationToImageSpatialExtentsProperty = new PropertyAssociation();
        associationToImageSpatialExtentsProperty.setPropertyIndex(3);
        associationToImageSpatialExtentsProperty.setEssential(false);
        componentDefinitionAssociationEntry.addAssociation(
                associationToImageSpatialExtentsProperty);

        PropertyAssociation associationToComponentPaletteBox = new PropertyAssociation();
        associationToComponentPaletteBox.setPropertyIndex(4);
        associationToComponentPaletteBox.setEssential(true);
        componentDefinitionAssociationEntry.addAssociation(associationToComponentPaletteBox);

        componentDefinitionAssociation.addEntry(componentDefinitionAssociationEntry);
        iprp.addItemPropertyAssociation(componentDefinitionAssociation);
        return iprp;
    }

    private AbstractItemProperty makeComponentPaletteBox(ColorModel colourModel) {
        ComponentPaletteBox cpal = new ComponentPaletteBox();
        cpal.addComponent(new PaletteComponent(1, 7, 0));
        cpal.addComponent(new PaletteComponent(2, 7, 0));
        cpal.addComponent(new PaletteComponent(3, 7, 0));
        IndexColorModel icm = (IndexColorModel) colourModel;
        int mapSize = icm.getMapSize();
        byte[][] values = new byte[mapSize][3];
        byte[] reds = new byte[mapSize];
        icm.getReds(reds);
        byte[] greens = new byte[mapSize];
        icm.getGreens(greens);
        byte[] blues = new byte[mapSize];
        icm.getBlues(blues);
        for (int i = 0; i < mapSize; i++) {
            values[i][0] = reds[i];
            values[i][1] = greens[i];
            values[i][2] = blues[i];
        }
        cpal.setComponentValues(values);
        return cpal;
    }

    private Box makeItemPropertiesBox_abgr() {
        ItemPropertiesBox iprp = new ItemPropertiesBox();
        ItemPropertyContainerBox ipco = new ItemPropertyContainerBox();
        ipco.addProperty(makeComponentDefinitionBox_abgr());
        ipco.addProperty(makeUncompressedFrameConfigBox_abgr());
        ipco.addProperty(makeImageSpatialExtentsProperty());
        iprp.setItemProperties(ipco);
        ItemPropertyAssociation componentDefinitionAssociation = new ItemPropertyAssociation();
        AssociationEntry componentDefinitionAssociationEntry = new AssociationEntry();
        componentDefinitionAssociationEntry.setItemId(MAIN_ITEM_ID);

        PropertyAssociation associationToComponentDefinitionBox = new PropertyAssociation();
        associationToComponentDefinitionBox.setPropertyIndex(1);
        associationToComponentDefinitionBox.setEssential(true);
        componentDefinitionAssociationEntry.addAssociation(associationToComponentDefinitionBox);

        PropertyAssociation associationToUncompressedFrameConfigBox = new PropertyAssociation();
        associationToUncompressedFrameConfigBox.setPropertyIndex(2);
        associationToUncompressedFrameConfigBox.setEssential(true);
        componentDefinitionAssociationEntry.addAssociation(associationToUncompressedFrameConfigBox);

        PropertyAssociation associationToImageSpatialExtentsProperty = new PropertyAssociation();
        associationToImageSpatialExtentsProperty.setPropertyIndex(3);
        associationToImageSpatialExtentsProperty.setEssential(false);
        componentDefinitionAssociationEntry.addAssociation(
                associationToImageSpatialExtentsProperty);

        componentDefinitionAssociation.addEntry(componentDefinitionAssociationEntry);
        iprp.addItemPropertyAssociation(componentDefinitionAssociation);
        return iprp;
    }

    private Box makeItemPropertiesBox_yuv(Y4mReader reader, FourCC profile) {
        ItemPropertiesBox iprp = new ItemPropertiesBox();
        ItemPropertyContainerBox ipco = new ItemPropertyContainerBox();
        ipco.addProperty(makeComponentDefinitionBox_yuv());
        ipco.addProperty(makeUncompressedFrameConfigBox_yuv(reader, profile));
        ipco.addProperty(makeImageSpatialExtentsProperty_yuv(reader));
        iprp.setItemProperties(ipco);
        ItemPropertyAssociation componentDefinitionAssociation = new ItemPropertyAssociation();
        AssociationEntry componentDefinitionAssociationEntry = new AssociationEntry();
        componentDefinitionAssociationEntry.setItemId(MAIN_ITEM_ID);

        PropertyAssociation associationToComponentDefinitionBox = new PropertyAssociation();
        associationToComponentDefinitionBox.setPropertyIndex(1);
        associationToComponentDefinitionBox.setEssential(true);
        componentDefinitionAssociationEntry.addAssociation(associationToComponentDefinitionBox);

        PropertyAssociation associationToUncompressedFrameConfigBox = new PropertyAssociation();
        associationToUncompressedFrameConfigBox.setPropertyIndex(2);
        associationToUncompressedFrameConfigBox.setEssential(true);
        componentDefinitionAssociationEntry.addAssociation(associationToUncompressedFrameConfigBox);

        PropertyAssociation associationToImageSpatialExtentsProperty = new PropertyAssociation();
        associationToImageSpatialExtentsProperty.setPropertyIndex(3);
        associationToImageSpatialExtentsProperty.setEssential(false);
        componentDefinitionAssociationEntry.addAssociation(
                associationToImageSpatialExtentsProperty);

        componentDefinitionAssociation.addEntry(componentDefinitionAssociationEntry);
        iprp.addItemPropertyAssociation(componentDefinitionAssociation);
        return iprp;
    }

    private ComponentDefinitionBox makeComponentDefinitionBox_rgb3() {
        ComponentDefinitionBox cmpd = new ComponentDefinitionBox();
        ComponentDefinition redComponent = new ComponentDefinition(4, null);
        cmpd.addComponentDefinition(redComponent);
        ComponentDefinition greenComponent = new ComponentDefinition(5, null);
        cmpd.addComponentDefinition(greenComponent);
        ComponentDefinition blueComponent = new ComponentDefinition(6, null);
        cmpd.addComponentDefinition(blueComponent);
        return cmpd;
    }

    private ComponentDefinitionBox makeComponentDefinitionBox_rgb_palette() {
        ComponentDefinitionBox cmpd = new ComponentDefinitionBox();
        ComponentDefinition paletteComponent = new ComponentDefinition(10, null);
        cmpd.addComponentDefinition(paletteComponent);
        ComponentDefinition redComponent = new ComponentDefinition(4, null);
        cmpd.addComponentDefinition(redComponent);
        ComponentDefinition greenComponent = new ComponentDefinition(5, null);
        cmpd.addComponentDefinition(greenComponent);
        ComponentDefinition blueComponent = new ComponentDefinition(6, null);
        cmpd.addComponentDefinition(blueComponent);
        return cmpd;
    }

    private ComponentDefinitionBox makeComponentDefinitionBox_rgba() {
        ComponentDefinitionBox cmpd = new ComponentDefinitionBox();
        ComponentDefinition redComponent = new ComponentDefinition(4, null);
        cmpd.addComponentDefinition(redComponent);
        ComponentDefinition greenComponent = new ComponentDefinition(5, null);
        cmpd.addComponentDefinition(greenComponent);
        ComponentDefinition blueComponent = new ComponentDefinition(6, null);
        cmpd.addComponentDefinition(blueComponent);
        ComponentDefinition alphaComponent = new ComponentDefinition(7, null);
        cmpd.addComponentDefinition(alphaComponent);
        return cmpd;
    }

    private ComponentDefinitionBox makeComponentDefinitionBox_abgr() {
        ComponentDefinitionBox cmpd = new ComponentDefinitionBox();
        ComponentDefinition redComponent = new ComponentDefinition(7, null);
        cmpd.addComponentDefinition(redComponent);
        ComponentDefinition greenComponent = new ComponentDefinition(6, null);
        cmpd.addComponentDefinition(greenComponent);
        ComponentDefinition blueComponent = new ComponentDefinition(5, null);
        cmpd.addComponentDefinition(blueComponent);
        ComponentDefinition alphaComponent = new ComponentDefinition(4, null);
        cmpd.addComponentDefinition(alphaComponent);
        return cmpd;
    }

    private ComponentDefinitionBox makeComponentDefinitionBox_yuv() {
        ComponentDefinitionBox cmpd = new ComponentDefinitionBox();
        ComponentDefinition yComponent = new ComponentDefinition(1, null);
        cmpd.addComponentDefinition(yComponent);
        ComponentDefinition cbComponent = new ComponentDefinition(2, null);
        cmpd.addComponentDefinition(cbComponent);
        ComponentDefinition crComponent = new ComponentDefinition(3, null);
        cmpd.addComponentDefinition(crComponent);
        return cmpd;
    }

    private UncompressedFrameConfigBox makeUncompressedFrameConfigBox_rgb3() {
        UncompressedFrameConfigBox uncc = new UncompressedFrameConfigBox();
        uncc.setProfile(new FourCC("rgb3"));
        uncc.addComponent(new Component(0, 7, 0, 0));
        uncc.addComponent(new Component(1, 7, 0, 0));
        uncc.addComponent(new Component(2, 7, 0, 0));
        uncc.setSamplingType(SamplingType.NoSubsampling);
        uncc.setInterleaveType(Interleaving.Pixel);
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

    private UncompressedFrameConfigBox makeUncompressedFrameConfigBox_rgb_component() {
        UncompressedFrameConfigBox uncc = new UncompressedFrameConfigBox();
        uncc.setProfile(new FourCC("gene"));
        uncc.addComponent(new Component(0, 7, 0, 0));
        uncc.addComponent(new Component(1, 7, 0, 0));
        uncc.addComponent(new Component(2, 7, 0, 0));
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

    private UncompressedFrameConfigBox makeUncompressedFrameConfigBox_rgb565(ByteOrder endian) {
        UncompressedFrameConfigBox uncc = new UncompressedFrameConfigBox();
        uncc.setProfile(new FourCC("gene"));
        uncc.addComponent(new Component(0, 4, 0, 0));
        uncc.addComponent(new Component(1, 5, 0, 0));
        uncc.addComponent(new Component(2, 4, 0, 0));
        uncc.setSamplingType(SamplingType.NoSubsampling);
        uncc.setInterleaveType(Interleaving.Pixel);
        uncc.setBlockSize(2);
        uncc.setComponentLittleEndian(false);
        uncc.setBlockPadLSB(false);
        uncc.setBlockLittleEndian(endian == ByteOrder.LITTLE_ENDIAN);
        uncc.setBlockReversed(false);
        uncc.setPadUnknown(false);
        uncc.setPixelSize(0);
        uncc.setRowAlignSize(0);
        uncc.setTileAlignSize(0);
        uncc.setNumTileColumnsMinusOne(0);
        uncc.setNumTileColumnsMinusOne(0);
        return uncc;
    }

    private UncompressedFrameConfigBox makeUncompressedFrameConfigBox_rgb555(
            ByteOrder endian, boolean padLSB) {
        UncompressedFrameConfigBox uncc = new UncompressedFrameConfigBox();
        uncc.setProfile(new FourCC("gene"));
        uncc.addComponent(new Component(0, 4, 0, 0));
        uncc.addComponent(new Component(1, 4, 0, 0));
        uncc.addComponent(new Component(2, 4, 0, 0));
        uncc.setSamplingType(SamplingType.NoSubsampling);
        uncc.setInterleaveType(Interleaving.Pixel);
        uncc.setBlockSize(2);
        uncc.setComponentLittleEndian(false);
        uncc.setBlockPadLSB(padLSB);
        uncc.setBlockLittleEndian(endian == ByteOrder.LITTLE_ENDIAN);
        uncc.setBlockReversed(false);
        uncc.setPadUnknown(false);
        uncc.setPixelSize(0);
        uncc.setRowAlignSize(0);
        uncc.setTileAlignSize(0);
        uncc.setNumTileColumnsMinusOne(0);
        uncc.setNumTileColumnsMinusOne(0);
        return uncc;
    }

    private UncompressedFrameConfigBox makeUncompressedFrameConfigBox_bgr() {
        UncompressedFrameConfigBox uncc = new UncompressedFrameConfigBox();
        uncc.setProfile(new FourCC("gene"));
        uncc.addComponent(new Component(2, 7, 0, 0));
        uncc.addComponent(new Component(1, 7, 0, 0));
        uncc.addComponent(new Component(0, 7, 0, 0));
        uncc.setSamplingType(SamplingType.NoSubsampling);
        uncc.setInterleaveType(Interleaving.Pixel);
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

    private UncompressedFrameConfigBox makeUncompressedFrameConfigBox_rgba() {
        UncompressedFrameConfigBox uncc = new UncompressedFrameConfigBox();
        uncc.setProfile(new FourCC("rgba"));
        uncc.addComponent(new Component(0, 7, 0, 0));
        uncc.addComponent(new Component(1, 7, 0, 0));
        uncc.addComponent(new Component(2, 7, 0, 0));
        uncc.addComponent(new Component(3, 7, 0, 0));
        uncc.setSamplingType(SamplingType.NoSubsampling);
        uncc.setInterleaveType(Interleaving.Pixel);
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

    private UncompressedFrameConfigBox makeUncompressedFrameConfigBox_rgb_palette() {
        UncompressedFrameConfigBox uncc = new UncompressedFrameConfigBox();
        uncc.setProfile(new FourCC("gene"));
        uncc.addComponent(new Component(0, 7, 0, 0));
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

    private UncompressedFrameConfigBox makeUncompressedFrameConfigBox_abgr() {
        UncompressedFrameConfigBox uncc = new UncompressedFrameConfigBox();
        uncc.setProfile(new FourCC("abgr"));
        uncc.addComponent(new Component(0, 7, 0, 0));
        uncc.addComponent(new Component(1, 7, 0, 0));
        uncc.addComponent(new Component(2, 7, 0, 0));
        uncc.addComponent(new Component(3, 7, 0, 0));
        uncc.setSamplingType(SamplingType.NoSubsampling);
        uncc.setInterleaveType(Interleaving.Pixel);
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

    private UncompressedFrameConfigBox makeUncompressedFrameConfigBox_yuv(
            Y4mReader reader, FourCC profile) {
        if ((profile != null)
                && (!profile.equals(new FourCC("2vuy")))
                && (!profile.equals(new FourCC("yuv2")))
                && (!profile.equals(new FourCC("yvyu")))
                && (!profile.equals(new FourCC("vyuy")))
                && (!profile.equals(new FourCC("i420")))
                && (!profile.equals(new FourCC("v308")))
                && (!profile.equals(new FourCC("nv12")))
                && (!profile.equals(new FourCC("nv21")))) {
            fail("need to handle specified profile");
        }
        UncompressedFrameConfigBox uncc = new UncompressedFrameConfigBox();
        if (profile != null) {
            uncc.setProfile(profile);
        } else {
            uncc.setProfile(new FourCC("gene"));
        }
        if (profile == null) {
            uncc.addComponent(new Component(0, 7, 0, 0));
            uncc.addComponent(new Component(1, 7, 0, 0));
            uncc.addComponent(new Component(2, 7, 0, 0));
        } else if (profile.equals(new FourCC("2vuy"))) {
            uncc.addComponent(new Component(1, 7, 0, 0));
            uncc.addComponent(new Component(0, 7, 0, 0));
            uncc.addComponent(new Component(2, 7, 0, 0));
            uncc.addComponent(new Component(0, 7, 0, 0));
        } else if (profile.equals(new FourCC("yuv2"))) {
            uncc.addComponent(new Component(0, 7, 0, 0));
            uncc.addComponent(new Component(1, 7, 0, 0));
            uncc.addComponent(new Component(0, 7, 0, 0));
            uncc.addComponent(new Component(2, 7, 0, 0));
        } else if (profile.equals(new FourCC("yvyu"))) {
            uncc.addComponent(new Component(0, 7, 0, 0));
            uncc.addComponent(new Component(2, 7, 0, 0));
            uncc.addComponent(new Component(0, 7, 0, 0));
            uncc.addComponent(new Component(1, 7, 0, 0));
        } else if (profile.equals(new FourCC("vyuy"))) {
            uncc.addComponent(new Component(2, 7, 0, 0));
            uncc.addComponent(new Component(0, 7, 0, 0));
            uncc.addComponent(new Component(1, 7, 0, 0));
            uncc.addComponent(new Component(0, 7, 0, 0));
        } else if (profile.equals(new FourCC("v308"))) {
            uncc.addComponent(new Component(2, 7, 0, 0));
            uncc.addComponent(new Component(0, 7, 0, 0));
            uncc.addComponent(new Component(1, 7, 0, 0));
        } else if (profile.equals(new FourCC("i420"))) {
            uncc.addComponent(new Component(0, 7, 0, 0));
            uncc.addComponent(new Component(1, 7, 0, 0));
            uncc.addComponent(new Component(2, 7, 0, 0));
        } else if (profile.equals(new FourCC("nv12"))) {
            uncc.addComponent(new Component(0, 7, 0, 0));
            uncc.addComponent(new Component(1, 7, 0, 0));
            uncc.addComponent(new Component(2, 7, 0, 0));
        } else if (profile.equals(new FourCC("nv21"))) {
            uncc.addComponent(new Component(0, 7, 0, 0));
            uncc.addComponent(new Component(2, 7, 0, 0));
            uncc.addComponent(new Component(1, 7, 0, 0));

        } else {
            fail("need to handle specified profile");
        }
        uncc.setSamplingType(SamplingType.NoSubsampling);
        switch (reader.getColourSpace()) {
            case YUV420:
                uncc.setSamplingType(SamplingType.YCbCr420);
                break;
            case YUV422:
                uncc.setSamplingType(SamplingType.YCbCr422);
                break;
            case YUV444:
                uncc.setSamplingType(SamplingType.NoSubsampling);
                break;
            default:
                fail("unhandled YUV sampling");
                break;
        }
        if (profile == null) {
            uncc.setInterleaveType(Interleaving.Component);
        } else if ((profile.equals(new FourCC("yuv2")))
                || (profile.equals(new FourCC("2vuy")))
                || (profile.equals(new FourCC("yvyu")))
                || (profile.equals(new FourCC("vyuy")))) {
            uncc.setInterleaveType(Interleaving.MultiY);
        } else if (profile.equals(new FourCC("i420"))) {
            uncc.setInterleaveType(Interleaving.Component);
        } else if ((profile.equals(new FourCC("nv12"))) || (profile.equals(new FourCC("nv21")))) {
            uncc.setInterleaveType(Interleaving.Mixed);
        } else if (profile.equals(new FourCC("v308"))) {
            uncc.setInterleaveType(Interleaving.Pixel);
        } else {
            fail("need to handle specified profile");
        }
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

    private ImageSpatialExtentsProperty makeImageSpatialExtentsProperty_yuv(Y4mReader reader) {
        ImageSpatialExtentsProperty ispe = new ImageSpatialExtentsProperty();
        ispe.setImageHeight(reader.getFrameHeight());
        ispe.setImageWidth(reader.getFrameWidth());
        return ispe;
    }

    private MediaDataBox createMediaDataBox_rgb3() {
        MediaDataBox mdat = new MediaDataBox();
        byte[] bytes = new byte[IMAGE_WIDTH * IMAGE_HEIGHT * NUM_BYTES_PER_PIXEL_RGB];
        int index = 0;
        for (int r = 0; r < IMAGE_HEIGHT / 4; r++) {
            for (int c = 0; c < IMAGE_WIDTH; c++) {
                bytes[index] = (byte) 0xFF;
                bytes[index + 1] = (byte) 0x00;
                bytes[index + 2] = (byte) 0x00;
                index += NUM_BYTES_PER_PIXEL_RGB;
            }
        }
        for (int r = 0; r < IMAGE_HEIGHT / 4; r++) {
            for (int c = 0; c < IMAGE_WIDTH; c++) {
                bytes[index] = (byte) 0x00;
                bytes[index + 1] = (byte) 0xFF;
                bytes[index + 2] = (byte) 0x00;
                index += NUM_BYTES_PER_PIXEL_RGB;
            }
        }
        for (int r = 0; r < IMAGE_HEIGHT / 4; r++) {
            for (int c = 0; c < IMAGE_WIDTH; c++) {
                bytes[index] = (byte) 0x00;
                bytes[index + 1] = (byte) 0x00;
                bytes[index + 2] = (byte) 0xFF;
                index += NUM_BYTES_PER_PIXEL_RGB;
            }
        }
        for (int r = 0; r < IMAGE_HEIGHT / 4; r++) {
            for (int c = 0; c < IMAGE_WIDTH; c++) {
                bytes[index] = (byte) 0xff;
                bytes[index + 1] = (byte) 0xFF;
                bytes[index + 2] = (byte) 0xff;
                index += NUM_BYTES_PER_PIXEL_RGB;
            }
        }
        mdat.setData(bytes);
        return mdat;
    }

    private MediaDataBox createMediaDataBox_rgb_component() throws IOException {
        MediaDataBox mdat = new MediaDataBox();
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
        mdat.setData(data);
        return mdat;
    }

    private MediaDataBox createMediaDataBox_rgb_palette(BufferedImage image) throws IOException {
        MediaDataBox mdat = new MediaDataBox();
        DataBufferByte buffer = (DataBufferByte) image.getRaster().getDataBuffer();
        mdat.setData(buffer.getData());
        return mdat;
    }

    private MediaDataBox createMediaDataBox_rgba() {
        MediaDataBox mdat = new MediaDataBox();
        byte[] bytes = new byte[IMAGE_WIDTH * IMAGE_HEIGHT * NUM_BYTES_PER_PIXEL_RGBA];
        int index = 0;
        for (int r = 0; r < IMAGE_HEIGHT / 4; r++) {
            for (int c = 0; c < IMAGE_WIDTH; c++) {
                bytes[index] = (byte) 0xFF;
                bytes[index + 1] = (byte) 0x00;
                bytes[index + 2] = (byte) 0x00;
                bytes[index + 3] = (byte) 0xff;
                index += NUM_BYTES_PER_PIXEL_RGBA;
            }
        }
        for (int r = 0; r < IMAGE_HEIGHT / 4; r++) {
            for (int c = 0; c < IMAGE_WIDTH; c++) {
                bytes[index] = (byte) 0x00;
                bytes[index + 1] = (byte) 0xFF;
                bytes[index + 2] = (byte) 0x00;
                bytes[index + 3] = (byte) 0xff;
                index += NUM_BYTES_PER_PIXEL_RGBA;
            }
        }
        for (int r = 0; r < IMAGE_HEIGHT / 4; r++) {
            for (int c = 0; c < IMAGE_WIDTH; c++) {
                bytes[index] = (byte) 0x00;
                bytes[index + 1] = (byte) 0x00;
                bytes[index + 2] = (byte) 0xFF;
                bytes[index + 3] = (byte) 0xff;
                index += NUM_BYTES_PER_PIXEL_RGBA;
            }
        }
        for (int r = 0; r < IMAGE_HEIGHT / 4; r++) {
            for (int c = 0; c < IMAGE_WIDTH; c++) {
                bytes[index] = (byte) 0xff;
                bytes[index + 1] = (byte) 0xFF;
                bytes[index + 2] = (byte) 0xff;
                bytes[index + 3] = (byte) 0xff;
                index += NUM_BYTES_PER_PIXEL_RGBA;
            }
        }
        mdat.setData(bytes);
        return mdat;
    }

    private MediaDataBox createMediaDataBox_abgr() throws IOException {
        MediaDataBox mdat = new MediaDataBox();
        BufferedImage image =
                new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
        drawColouredRectangles(image);
        ImageIO.write(image, "PNG", new File("ref.png"));
        DataBufferByte buffer = (DataBufferByte) image.getRaster().getDataBuffer();
        mdat.setData(buffer.getData());
        return mdat;
    }

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

    private void overdrawBadPixels(BufferedImage image) {
        Graphics2D g2 = image.createGraphics();
        for (int r = 100; r < 105; r++) {
            g2.setColor(Color.WHITE);
            g2.drawLine(0, r, IMAGE_WIDTH - 1, r);
        }

        for (int c = 80; c < 82; c++) {
            g2.setColor(Color.BLACK);
            g2.drawLine(c, 0, c, IMAGE_HEIGHT - 1);
        }

        image.setRGB(120, 160, Color.BLACK.getRGB());
        image.setRGB(121, 160, Color.BLACK.getRGB());
        image.setRGB(122, 160, Color.BLACK.getRGB());
        image.setRGB(123, 160, Color.BLACK.getRGB());
        image.setRGB(120, 161, Color.BLACK.getRGB());
        image.setRGB(121, 161, Color.BLACK.getRGB());
        image.setRGB(122, 161, Color.BLACK.getRGB());
        image.setRGB(123, 161, Color.BLACK.getRGB());
    }

    private MediaDataBox createMediaDataBox_bgr() throws IOException {
        MediaDataBox mdat = new MediaDataBox();
        BufferedImage image =
                new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_3BYTE_BGR);
        drawColouredRectangles(image);
        DataBufferByte buffer = (DataBufferByte) image.getRaster().getDataBuffer();
        mdat.setData(buffer.getData());
        return mdat;
    }

    private MediaDataBox createMediaDataBox_bgr_sbpm() throws IOException {
        MediaDataBox mdat = new MediaDataBox();
        BufferedImage image =
                new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_3BYTE_BGR);
        drawColouredRectangles(image);
        overdrawBadPixels(image);
        DataBufferByte buffer = (DataBufferByte) image.getRaster().getDataBuffer();
        mdat.setData(buffer.getData());
        return mdat;
    }

    private MediaDataBox createMediaDataBox_rgb565(ByteOrder blockEndian) throws IOException {
        MediaDataBox mdat = new MediaDataBox();
        BufferedImage image =
                new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_USHORT_565_RGB);
        drawColouredRectangles(image);
        SampleModel sm = image.getSampleModel();
        System.out.println(sm.toString());
        ColorModel cm = image.getColorModel();
        System.out.println(cm.toString());
        DataBufferUShort buffer = (DataBufferUShort) image.getRaster().getDataBuffer();
        mdat.setData(shortArrayToByteArray(buffer.getData(), blockEndian));
        return mdat;
    }

    private MediaDataBox createMediaDataBox_rgb555(ByteOrder blockEndian, boolean padLSB)
            throws IOException {
        MediaDataBox mdat = new MediaDataBox();
        BufferedImage image =
                new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_USHORT_555_RGB);
        drawColouredRectangles(image);
        SampleModel sm = image.getSampleModel();
        System.out.println(sm.toString());
        ColorModel cm = image.getColorModel();
        System.out.println(cm.toString());
        DataBufferUShort buffer = (DataBufferUShort) image.getRaster().getDataBuffer();
        if (padLSB) {
            short[] shortsUnshifted = buffer.getData();
            short[] shortsShifted = new short[shortsUnshifted.length];
            for (int i = 0; i < shortsUnshifted.length; i++) {
                shortsShifted[i] = (short) (shortsUnshifted[i] << 1);
            }
            mdat.setData(shortArrayToByteArray(shortsShifted, blockEndian));
        } else {
            mdat.setData(shortArrayToByteArray(buffer.getData(), blockEndian));
        }
        return mdat;
    }

    private MediaDataBox createMediaDataBox_yuv(Y4mReader reader, FourCC profile)
            throws IOException {
        MediaDataBox mdat = new MediaDataBox();
        int numFrames = 0;
        while (reader.hasMoreFrames()) {
            byte[] frame = reader.getFrame();
            numFrames += 1;
            if (numFrames == 100) {
                if (profile == null) {
                    mdat.setData(frame);
                } else if (profile.equals(new FourCC("2vuy"))) {
                    // Base data is assumed to be 4:2:2 planar
                    byte[] data = new byte[frame.length];
                    int yOffset = 0;
                    int cbOffset = 2 * frame.length / 4;
                    int crOffset = 3 * frame.length / 4;
                    for (int i = 0; i < frame.length / 4; i++) {
                        data[i * 4 + 0] = frame[cbOffset + i];
                        data[i * 4 + 1] = frame[yOffset + 2 * i];
                        data[i * 4 + 2] = frame[crOffset + i];
                        data[i * 4 + 3] = frame[yOffset + 2 * i + 1];
                    }
                    mdat.setData(data);
                } else if (profile.equals(new FourCC("yuv2"))) {
                    // Base data is assumed to be 4:2:2 planar
                    byte[] data = new byte[frame.length];
                    int yOffset = 0;
                    int cbOffset = 2 * frame.length / 4;
                    int crOffset = 3 * frame.length / 4;
                    for (int i = 0; i < frame.length / 4; i++) {
                        data[i * 4 + 0] = frame[yOffset + 2 * i];
                        data[i * 4 + 1] = frame[cbOffset + i];
                        data[i * 4 + 2] = frame[yOffset + 2 * i + 1];
                        data[i * 4 + 3] = frame[crOffset + i];
                    }
                    mdat.setData(data);
                } else if (profile.equals(new FourCC("yvyu"))) {
                    // Base data is assumed to be 4:2:2 planar
                    byte[] data = new byte[frame.length];
                    int yOffset = 0;
                    int cbOffset = 2 * frame.length / 4;
                    int crOffset = 3 * frame.length / 4;
                    for (int i = 0; i < frame.length / 4; i++) {
                        data[i * 4 + 0] = frame[yOffset + 2 * i];
                        data[i * 4 + 1] = frame[crOffset + i];
                        data[i * 4 + 2] = frame[yOffset + 2 * i + 1];
                        data[i * 4 + 3] = frame[cbOffset + i];
                    }
                    mdat.setData(data);
                } else if (profile.equals(new FourCC("vyuy"))) {
                    // Base data is assumed to be 4:2:2 planar
                    byte[] data = new byte[frame.length];
                    int yOffset = 0;
                    int cbOffset = 2 * frame.length / 4;
                    int crOffset = 3 * frame.length / 4;
                    for (int i = 0; i < frame.length / 4; i++) {
                        data[i * 4 + 0] = frame[crOffset + i];
                        data[i * 4 + 1] = frame[yOffset + 2 * i];
                        data[i * 4 + 2] = frame[cbOffset + i];
                        data[i * 4 + 3] = frame[yOffset + 2 * i + 1];
                    }
                    mdat.setData(data);
                } else if (profile.equals(new FourCC("v308"))) {
                    // Base data is assumed to be 4:4:4 planar
                    byte[] data = new byte[frame.length];
                    int yOffset = 0;
                    int cbOffset = frame.length / 3;
                    int crOffset = 2 * frame.length / 3;
                    for (int i = 0; i < frame.length / 3; i++) {
                        data[i * 3] = frame[crOffset + i];
                        data[i * 3 + 1] = frame[yOffset + i];
                        data[i * 3 + 2] = frame[cbOffset + i];
                    }
                    mdat.setData(data);
                } else if (profile.equals(new FourCC("i420"))) {
                    mdat.setData(frame);
                } else if (profile.equals(new FourCC("nv12"))) {
                    // Base data is assumed to be 4:2:0 planar
                    assert ((frame.length % 6) == 0);
                    byte[] data = new byte[frame.length];

                    int cbOffset = 4 * frame.length / 6;
                    int crOffset = 5 * frame.length / 6;
                    byte[] interleavedChroma = new byte[frame.length - cbOffset];

                    for (int i = 0; i < frame.length / 6; i++) {
                        interleavedChroma[2 * i] = frame[cbOffset + i];
                        interleavedChroma[2 * i + 1] = frame[crOffset + i];
                    }
                    System.arraycopy(frame, 0, data, 0, cbOffset);
                    System.arraycopy(interleavedChroma, 0, data, cbOffset, data.length - cbOffset);
                    mdat.setData(data);
                } else if (profile.equals(new FourCC("nv21"))) {
                    // Base data is assumed to be 4:2:0 planar
                    assert ((frame.length % 6) == 0);
                    byte[] data = new byte[frame.length];

                    int cbOffset = 4 * frame.length / 6;
                    int crOffset = 5 * frame.length / 6;
                    byte[] interleavedChroma = new byte[frame.length - cbOffset];

                    for (int i = 0; i < frame.length / 6; i++) {
                        interleavedChroma[2 * i] = frame[crOffset + i];
                        interleavedChroma[2 * i + 1] = frame[cbOffset + i];
                    }
                    System.arraycopy(frame, 0, data, 0, cbOffset);
                    System.arraycopy(interleavedChroma, 0, data, cbOffset, data.length - cbOffset);
                    mdat.setData(data);
                } else {
                    fail("need to handle specified profile");
                }
                break;
            }
        }
        return mdat;
    }

    public byte[] shortArrayToByteArray(short[] data, ByteOrder byteOrder) {
        byte[] bytes = new byte[data.length * Short.BYTES];
        int c = 0;
        for (int i = 0; i < data.length; i++) {
            if (byteOrder.equals(ByteOrder.BIG_ENDIAN)) {
                bytes[c++] = (byte) (((data[i]) >>> 8) & 0xFF);
                bytes[c++] = (byte) (((data[i])) & 0xFF);

            } else {
                bytes[c++] = (byte) (((data[i])) & 0xFF);
                bytes[c++] = (byte) (((data[i]) >>> 8) & 0xFF);
            }
        }
        return bytes;
    }

    private void validateFileTypeBox(FileTypeBox ftyp) {
        assertTrue(
                ftyp.getCompatibleBrands().contains(new Brand("ns01")),
                "NGA.STND.0078_0,1-02 SIFF files shall include the ns01 brand");
        // TODO: -03
        assertTrue(
                ftyp.getCompatibleBrands().contains(new Brand("mif2")),
                "NGA.STND.0078_0.1-04 SIFF files shall include the mif2 brand in the compatible brands list");
        // TODO: -05
        assertTrue(
                ftyp.getCompatibleBrands().contains(new Brand("unif")),
                "NGA.STND.0078_0.1-06 SIFF files shall include the unif brand in the compatible brands list");
        // TODO: -07
        // TODO: -08
        // TODO: -09
    }

    private void validateMetaBox(MetaBox metaBox) {
        for (Box box : metaBox.getNestedBoxes()) {
            if (box instanceof ItemPropertiesBox iprp) {
                validateItemPropertiesBox(iprp);
            } else if (box instanceof HandlerBox hdlr) {
                // Not yet
            } else if (box instanceof PrimaryItemBox pitm) {
                // Not yet
            } else if (box instanceof ItemInfoBox iinf) {
                // Not yet
            } else if (box instanceof ItemLocationBox iloc) {
                // Not yet
            } else if (box instanceof ItemDataBox idat) {
                // Not yet
            } else if (box instanceof ItemReferenceBox iref) {
                // Not yet
            } else {
                fail(
                        "metaBox validation is not yet implemented for this case: "
                                + box.getFullName());
            }
        }
    }

    private void validateItemPropertiesBox(ItemPropertiesBox iprp) {
        ItemPropertyContainerBox ipco = iprp.getItemProperties();
        validateItemProperties(ipco.getProperties());
    }

    private void validateItemProperties(List<AbstractItemProperty> properties) {
        ComponentDefinitionBox cmpd = null;
        UncompressedFrameConfigBox uncC = null;
        ImageSpatialExtentsProperty ispe = null;
        ComponentPaletteBox cpal = null;
        SensorBadPixelsMapBox sbpm = null;
        UserDescriptionProperty udes = null;

        for (AbstractItemProperty property : properties) {
            if (property instanceof ComponentDefinitionBox componentDefinitionBox) {
                cmpd = componentDefinitionBox;
            } else if (property instanceof UncompressedFrameConfigBox uncompressedFrameConfigBox) {
                uncC = uncompressedFrameConfigBox;
            } else if (property
                    instanceof ImageSpatialExtentsProperty imageSpatialExtentsProperty) {
                ispe = imageSpatialExtentsProperty;
            } else if (property instanceof ComponentPaletteBox componentPaletteBox) {
                cpal = componentPaletteBox;
            } else if (property instanceof SensorBadPixelsMapBox sensorBadPixelsMapBox) {
                sbpm = sensorBadPixelsMapBox;
            } else if (property instanceof UserDescriptionProperty userDescriptionProperty) {
                udes = userDescriptionProperty;
            } else {
                fail("TODO: property: " + property.toString());
            }
        }
        if (cmpd == null) {
            fail("ComponentDefinitionBox is required (see 4.3)");
        }
        if (uncC == null) {
            fail("UncompressedFrameConfigBox is required (see 4.3)");
        }
        if (ispe == null) {
            fail("ImageSpatialExtentsProperty is required (see 4.3)");
        }
        checkUnccComponentsAreInRange(uncC.getComponents(), cmpd.getComponentDefinitions());
        checkAtMostOneFAComponent(uncC.getComponents(), cmpd.getComponentDefinitions());
        // TODO: check we have ComponentPatternDefinitionBox if FA is present.
        checkAtMostOnePaletteComponent(uncC.getComponents(), cmpd.getComponentDefinitions());
        checkComponentPaletteBoxIsPresentIfNeeded(
                uncC.getComponents(), cmpd.getComponentDefinitions(), cpal);
        checkComponentFormatIsValid(uncC.getComponents());
        checkComponentAlignSizeIsValid(uncC.getComponents());
        checkComponentAlignSizeIsConsistent(uncC);
        checkTileWidthIsConsistentWithFrameWidth(uncC, ispe);
        checkTileHeightIsConsistentWithFrameHeight(uncC, ispe);
        checkSamplingTypeIsValid(uncC);
        // TODO: sampling_type == 1 checks
        // TODO: sampling_type == 2 checks
        // TODO: sampling_type == 3 checks
        checkInterleaveTypeIsValid(uncC);
        // TODO: interleave consistency checks
        checkBlockingConsistency(uncC);
        checkBlockReversed(uncC);
        checkPixelSizeConsistency(uncC);
        checkTileAlignConsistency(uncC);
        checkProfile(uncC, cmpd);
        checkSensorBadPixelsMap(sbpm, cmpd);
    }

    private void checkUnccComponentsAreInRange(
            List<Component> components, List<ComponentDefinition> componentDefinitions) {
        for (Component component : components) {
            assertTrue(
                    component.getComponentIndex() < componentDefinitions.size(),
                    "5.1.1. 'The component_index field shall be strictly less than the component_count field value of the associated ComponentDefinitionBox'");
        }
    }

    private void checkAtMostOnePaletteComponent(
            List<Component> components, List<ComponentDefinition> componentDefinitions) {
        int numberOfP = getNumberOfPaletteComponents(components, componentDefinitions);
        if (numberOfP > 1) {
            fail(
                    "5.2.1.2 'There shall be at most one component with type 10 (P) present in the component list'");
        }
    }

    private void checkComponentPaletteBoxIsPresentIfNeeded(
            List<Component> components,
            List<ComponentDefinition> componentDefinitions,
            ComponentPaletteBox cpal) {
        int numberOfP = getNumberOfPaletteComponents(components, componentDefinitions);
        if ((numberOfP > 0) && (cpal == null)) {
            fail(
                    "6.1.2.1 'This box shall be present if and only if there is an associated ComponentDefinitionBox present, in which there is a component defined with a component_type of 10 ('P')'");
        }
        if ((numberOfP == 0) && (cpal != null)) {
            fail(
                    "6.1.2.1 'This box shall be present if and only if there is an associated ComponentDefinitionBox present, in which there is a component defined with a component_type of 10 ('P')'");
        }
    }

    private void checkAtMostOneFAComponent(
            List<Component> components, List<ComponentDefinition> componentDefinitions) {
        int numberOfFA = getNumberOfFAComponents(components, componentDefinitions);
        if (numberOfFA > 1) {
            fail(
                    "5.2.1.2 'There shall be at most one component with type 11 (FA) present in the component list'");
        }
    }

    private int getNumberOfPaletteComponents(
            List<Component> components, List<ComponentDefinition> componentDefinitions) {
        return getNumberOfComponentsWithType(components, componentDefinitions, 10);
    }

    private int getNumberOfFAComponents(
            List<Component> components, List<ComponentDefinition> componentDefinitions) {
        return getNumberOfComponentsWithType(components, componentDefinitions, 11);
    }

    private int getNumberOfComponentsWithType(
            List<Component> components,
            List<ComponentDefinition> componentDefinitions,
            int componentType) {
        int count = 0;
        for (Component component : components) {
            if (componentDefinitions.get(component.getComponentIndex()).getComponentType()
                    == componentType) {
                count += 1;
            }
        }
        return count;
    }

    private void checkComponentFormatIsValid(List<Component> components) {
        for (Component component : components) {
            if ((component.getComponentFormat() < 0) || (component.getComponentFormat() > 2)) {
                fail("Table 2. component_format field is outside of defined range");
            }
        }
    }

    private void checkComponentAlignSizeIsValid(List<Component> components) {
        for (Component component : components) {
            int component_bit_depth = component.getComponentBitDepthMinusOne() + 1;
            if (component.getComponentAlignSize() != 0) {
                assertTrue(
                        component.getComponentAlignSize() * 8 > component_bit_depth,
                        "5.2.1.3 ' component_align_size shall be either 0 or such that component_align_size*8 is greater than component_bit_depth'");
            }
        }
    }

    private void checkComponentAlignSizeIsConsistent(UncompressedFrameConfigBox uncC) {
        if (uncC.isComponentLittleEndian()) {
            assertFalse(
                    uncC.isBlockLittleEndian(),
                    "5.2.1.3 ' If components_little_endian is 1, block_little_endian shall be 0 and component_align_size of each component shall be different from 0.'");
            for (Component component : uncC.getComponents()) {
                assertTrue(
                        component.getComponentAlignSize() != 0,
                        "5.2.1.3 ' If components_little_endian is 1, block_little_endian shall be 0 and component_align_size of each component shall be different from 0.'");
            }
        }
    }

    private void checkTileWidthIsConsistentWithFrameWidth(
            UncompressedFrameConfigBox uncC, ImageSpatialExtentsProperty ispe) {
        assertEquals(
                ispe.getImageWidth() % (uncC.getNumTileColumnsMinusOne() + 1),
                0,
                "5.2.1.4 'The frame width shall be a multiple of num_tile_cols_minus_one+1");
    }

    private void checkTileHeightIsConsistentWithFrameHeight(
            UncompressedFrameConfigBox uncC, ImageSpatialExtentsProperty ispe) {
        assertEquals(
                ispe.getImageHeight() % (uncC.getNumTileRowsMinusOne() + 1),
                0,
                "5.2.1.4 'The frame height shall be a multiple of num_tile_rows_minus_one+1");
    }

    private void checkSamplingTypeIsValid(UncompressedFrameConfigBox uncC) {
        if (uncC.getSamplingType().equals(SamplingType.Reserved)) {
            fail("Table 3. sampling_type field is outside of defined range");
        }
    }

    private void checkInterleaveTypeIsValid(UncompressedFrameConfigBox uncC) {
        if (uncC.getInterleaveType().equals(Interleaving.Reserved)) {
            fail("Table 4. interleave_type field is outside of defined range");
        }
    }

    private void checkBlockingConsistency(UncompressedFrameConfigBox uncC) {
        if (uncC.getBlockSize() == 0) {
            assertFalse(
                    uncC.isBlockPadLSB(),
                    "5.2.1.7 'If the block size is 0 (blocking is not used within the sample data), block_pad_lsb, block_little_endian and block_reversed shall all be 0'");
            assertFalse(
                    uncC.isBlockLittleEndian(),
                    "5.2.1.7 'If the block size is 0 (blocking is not used within the sample data), block_pad_lsb, block_little_endian and block_reversed shall all be 0'");
            assertFalse(
                    uncC.isBlockReversed(),
                    "5.2.1.7 'If the block size is 0 (blocking is not used within the sample data), block_pad_lsb, block_little_endian and block_reversed shall all be 0'");
        } else {
            // TODO: see what is possible.
        }
    }

    private void checkBlockReversed(UncompressedFrameConfigBox uncC) {
        if (!uncC.isBlockLittleEndian()) {
            assertFalse(
                    uncC.isBlockReversed(),
                    "5.2.1.7 'block_reversed shall be 0 if block_little_endian is 0");
        }
    }

    private void checkPixelSizeConsistency(UncompressedFrameConfigBox uncC) {
        if (uncC.getPixelSize() > 0) {
            // TODO: check pixel_size is large enough
        }
        if ((!uncC.getInterleaveType().equals(Interleaving.Pixel))
                && (!uncC.getInterleaveType().equals(Interleaving.MultiY))) {
            assertEquals(
                    uncC.getPixelSize(),
                    0,
                    "5.2.1.7 'pixel_size shall be 0 if interleave_type is different to 1 or 5'");
        }
    }

    private void checkTileAlignConsistency(UncompressedFrameConfigBox uncC) {
        if ((uncC.getNumTileColumnsMinusOne() == 0) && (uncC.getNumTileRowsMinusOne() == 0)) {
            assertEquals(
                    uncC.getTileAlignSize(),
                    0,
                    "5.2.1.7 'tile_align_size shall be 0 if a single tile is used'");
        }
    }

    private void checkProfile(UncompressedFrameConfigBox uncC, ComponentDefinitionBox cmpd) {
        if ((uncC.getProfile().equals(new FourCC("gene"))) || (uncC.getProfile().hashCode() == 0)) {
            return;
        }
        List<ComponentDefinition> componentDefinitions = cmpd.getComponentDefinitions();
        List<Component> components = uncC.getComponents();
        switch (uncC.getProfile().toString()) {
            case "2vuy":
                assertEquals(
                        components.size(),
                        4,
                        "5.3.2 Table 5 2vuy requires [{2,7},{1,7},{3,7},{1,7}], 1, 5");
                assertEquals(
                        componentDefinitions
                                .get(components.get(0).getComponentIndex())
                                .getComponentType(),
                        2,
                        "5.3.2 Table 5 2vuy requires [{2,7},{1,7},{3,7},{1,7}], 1, 5");
                assertEquals(
                        components.get(0).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 yuv2 requires [{1,7},{2,7},{1,7},{3,7}], 1, 5");
                assertEquals(
                        componentDefinitions
                                .get(components.get(1).getComponentIndex())
                                .getComponentType(),
                        1,
                        "5.3.2 Table 5 2vuy requires [{2,7},{1,7},{3,7},{1,7}], 1, 5");
                assertEquals(
                        components.get(1).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 2vuy requires [{2,7},{1,7},{3,7},{1,7}], 1, 5");
                assertEquals(
                        componentDefinitions
                                .get(components.get(2).getComponentIndex())
                                .getComponentType(),
                        3,
                        "5.3.2 Table 5 2vuy requires [{2,7},{1,7},{3,7},{1,7}], 1, 5");
                assertEquals(
                        components.get(2).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 2vuy requires [{2,7},{1,7},{3,7},{1,7}], 1, 5");
                assertEquals(
                        componentDefinitions
                                .get(components.get(3).getComponentIndex())
                                .getComponentType(),
                        1,
                        "5.3.2 Table 5 2vuy requires [{2,7},{1,7},{3,7},{1,7}], 1, 5");
                assertEquals(
                        components.get(3).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 2vuy requires [{2,7},{1,7},{3,7},{1,7}], 1, 5");

                assertEquals(
                        uncC.getSamplingType().getEncodedValue(),
                        1,
                        "5.3.2 Table 5 2vuy requires [{2,7},{1,7},{3,7},{1,7}], 1, 5");
                assertEquals(
                        uncC.getInterleaveType().getEncodedValue(),
                        5,
                        "5.3.2 Table 5 2vuy requires [{2,7},{1,7},{3,7},{1,7}], 1, 5");
                checkAllOtherFieldsAreZero(uncC);
                break;
            case "yuv2":
                assertEquals(
                        components.size(),
                        4,
                        "5.3.2 Table 5 yuv2 requires [{1,7},{2,7},{1,7},{3,7}], 1, 5");
                assertEquals(
                        componentDefinitions
                                .get(components.get(0).getComponentIndex())
                                .getComponentType(),
                        1,
                        "5.3.2 Table 5 yuv2 requires [{1,7},{2,7},{1,7},{3,7}], 1, 5");
                assertEquals(
                        components.get(0).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 yuv2 requires [{1,7},{2,7},{1,7},{3,7}], 1, 5");
                assertEquals(
                        componentDefinitions
                                .get(components.get(1).getComponentIndex())
                                .getComponentType(),
                        2,
                        "5.3.2 Table 5 yuv2 requires [{1,7},{2,7},{1,7},{3,7}], 1, 5");
                assertEquals(
                        components.get(1).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 yuv2 requires [{1,7},{2,7},{1,7},{3,7}], 1, 5");
                assertEquals(
                        componentDefinitions
                                .get(components.get(2).getComponentIndex())
                                .getComponentType(),
                        1,
                        "5.3.2 Table 5 yuv2 requires [{1,7},{2,7},{1,7},{3,7}], 1, 5");
                assertEquals(
                        components.get(2).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 yuv2 requires [{1,7},{2,7},{1,7},{3,7}], 1, 5");
                assertEquals(
                        componentDefinitions
                                .get(components.get(3).getComponentIndex())
                                .getComponentType(),
                        3,
                        "5.3.2 Table 5 yuv2 requires [{1,7},{2,7},{1,7},{3,7}], 1, 5");
                assertEquals(
                        components.get(3).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 yuv2 requires [{1,7},{2,7},{1,7},{3,7}], 1, 5");

                assertEquals(
                        uncC.getSamplingType().getEncodedValue(),
                        1,
                        "5.3.2 Table 5 yuv2 requires [{1,7},{2,7},{1,7},{3,7}], 1, 5");
                assertEquals(
                        uncC.getInterleaveType().getEncodedValue(),
                        5,
                        "5.3.2 Table 5 yuv2 requires [{1,7},{2,7},{1,7},{3,7}], 1, 5");
                checkAllOtherFieldsAreZero(uncC);
                break;
            case "yvyu":
                assertEquals(
                        components.size(),
                        4,
                        "5.3.2 Table 5 yvyu requires [{1,7},{3,7},{1,7},{2,7}], 1, 5");
                assertEquals(
                        componentDefinitions
                                .get(components.get(0).getComponentIndex())
                                .getComponentType(),
                        1,
                        "5.3.2 Table 5 yvyu requires [{1,7},{3,7},{1,7},{2,7}], 1, 5");
                assertEquals(
                        components.get(0).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 yvyu requires [{1,7},{3,7},{1,7},{2,7}], 1, 5");
                assertEquals(
                        componentDefinitions
                                .get(components.get(1).getComponentIndex())
                                .getComponentType(),
                        3,
                        "5.3.2 Table 5 yvyu requires [{1,7},{3,7},{1,7},{2,7}], 1, 5");
                assertEquals(
                        components.get(1).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 yvyu requires [{1,7},{3,7},{1,7},{2,7}], 1, 5");
                assertEquals(
                        componentDefinitions
                                .get(components.get(2).getComponentIndex())
                                .getComponentType(),
                        1,
                        "5.3.2 Table 5 yvyu requires [{1,7},{3,7},{1,7},{2,7}], 1, 5");
                assertEquals(
                        components.get(2).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 yvyu requires [{1,7},{3,7},{1,7},{2,7}], 1, 5");
                assertEquals(
                        componentDefinitions
                                .get(components.get(3).getComponentIndex())
                                .getComponentType(),
                        2,
                        "5.3.2 Table 5 yvyu requires [{1,7},{3,7},{1,7},{2,7}], 1, 5");
                assertEquals(
                        components.get(3).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 yvyu requires [{1,7},{3,7},{1,7},{2,7}], 1, 5");

                assertEquals(
                        uncC.getSamplingType().getEncodedValue(),
                        1,
                        "5.3.2 Table 5 yvyu requires [{1,7},{3,7},{1,7},{2,7}], 1, 5");
                assertEquals(
                        uncC.getInterleaveType().getEncodedValue(),
                        5,
                        "5.3.2 Table 5 yvyu requires [{1,7},{3,7},{1,7},{2,7}], 1, 5");
                checkAllOtherFieldsAreZero(uncC);
                break;
            case "vyuy":
                assertEquals(
                        components.size(),
                        4,
                        "5.3.2 Table 5 vyuy requires [{3,7},{1,7},{2,7},{1,7}], 1, 5");
                assertEquals(
                        componentDefinitions
                                .get(components.get(0).getComponentIndex())
                                .getComponentType(),
                        3,
                        "5.3.2 Table 5 vyuy requires [{3,7},{1,7},{2,7},{1,7}], 1, 5");
                assertEquals(
                        components.get(0).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 vyuy requires [{3,7},{1,7},{2,7},{1,7}], 1, 5");
                assertEquals(
                        componentDefinitions
                                .get(components.get(1).getComponentIndex())
                                .getComponentType(),
                        1,
                        "5.3.2 Table 5 vyuy requires [{3,7},{1,7},{2,7},{1,7}], 1, 5");
                assertEquals(
                        components.get(1).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 yvyu requires [{1,7},{3,7},{1,7},{2,7}], 1, 5");
                assertEquals(
                        componentDefinitions
                                .get(components.get(2).getComponentIndex())
                                .getComponentType(),
                        2,
                        "5.3.2 Table 5 vyuy requires [{3,7},{1,7},{2,7},{1,7}], 1, 5");
                assertEquals(
                        components.get(2).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 vyuy requires [{3,7},{1,7},{2,7},{1,7}], 1, 5");
                assertEquals(
                        componentDefinitions
                                .get(components.get(3).getComponentIndex())
                                .getComponentType(),
                        1,
                        "5.3.2 Table 5 vyuy requires [{3,7},{1,7},{2,7},{1,7}], 1, 5");
                assertEquals(
                        components.get(3).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 vyuy requires [{3,7},{1,7},{2,7},{1,7}], 1, 5");

                assertEquals(
                        uncC.getSamplingType().getEncodedValue(),
                        1,
                        "5.3.2 Table 5 vyuy requires [{3,7},{1,7},{2,7},{1,7}], 1, 5");
                assertEquals(
                        uncC.getInterleaveType().getEncodedValue(),
                        5,
                        "5.3.2 Table 5 vyuy requires [{3,7},{1,7},{2,7},{1,7}], 1, 5");
                checkAllOtherFieldsAreZero(uncC);
                break;
            case "v308":
                assertEquals(
                        components.size(),
                        3,
                        "5.3.2 Table 5 v308 requires [{3,7},{1,7},{2,7}], 0, 1");
                assertEquals(
                        componentDefinitions
                                .get(components.get(0).getComponentIndex())
                                .getComponentType(),
                        3,
                        "5.3.2 Table 5 v308 requires [{3,7},{1,7},{2,7}], 0, 1");
                assertEquals(
                        components.get(0).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 v308 requires [{3,7},{1,7},{2,7}], 0, 1");
                assertEquals(
                        componentDefinitions
                                .get(components.get(1).getComponentIndex())
                                .getComponentType(),
                        1,
                        "5.3.2 Table 5 v308 requires [{3,7},{1,7},{2,7}], 0, 1");
                assertEquals(
                        components.get(1).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 v308 requires [{3,7},{1,7},{2,7}], 0, 1");
                assertEquals(
                        componentDefinitions
                                .get(components.get(2).getComponentIndex())
                                .getComponentType(),
                        2,
                        "5.3.2 Table 5 v308 requires [{3,7},{1,7},{2,7}], 0, 1");
                assertEquals(
                        components.get(2).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 v308 requires [{3,7},{1,7},{2,7}], 0, 1");
                assertEquals(
                        uncC.getSamplingType().getEncodedValue(),
                        0,
                        "5.3.2 Table 5 v308 requires [{3,7},{1,7},{2,7}], 0, 1");
                assertEquals(
                        uncC.getInterleaveType().getEncodedValue(),
                        1,
                        "5.3.2 Table 5 v308 requires [{3,7},{1,7},{2,7}], 0, 1");
                checkAllOtherFieldsAreZero(uncC);
                break;
            case "rgb3":
                assertEquals(
                        components.size(),
                        3,
                        "5.3.2 Table 5 rgb3 requires [{4,7},{5,7},{6,7}], 0, 1");
                assertEquals(
                        componentDefinitions
                                .get(components.get(0).getComponentIndex())
                                .getComponentType(),
                        4,
                        "5.3.2 Table 5 rgb3 requires [{4,7},{5,7},{6,7}], 0, 1");
                assertEquals(
                        components.get(0).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 rgb3 requires [{4,7},{5,7},{6,7}], 0, 1");
                assertEquals(
                        componentDefinitions
                                .get(components.get(1).getComponentIndex())
                                .getComponentType(),
                        5,
                        "5.3.2 Table 5 rgb3 requires [{4,7},{5,7},{6,7}], 0, 1");
                assertEquals(
                        components.get(1).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 rgb3 requires [{4,7},{5,7},{6,7}], 0, 1");
                assertEquals(
                        componentDefinitions
                                .get(components.get(2).getComponentIndex())
                                .getComponentType(),
                        6,
                        "5.3.2 Table 5 rgb3 requires [{4,7},{5,7},{6,7}], 0, 1");
                assertEquals(
                        components.get(2).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 rgb3 requires [{4,7},{5,7},{6,7}], 0, 1");
                assertEquals(
                        uncC.getSamplingType().getEncodedValue(),
                        0,
                        "5.3.2 Table 5 rgb3 requires [{4,7},{5,7},{6,7}], 0, 1");
                assertEquals(
                        uncC.getInterleaveType().getEncodedValue(),
                        1,
                        "5.3.2 Table 5 rgb3 requires [{4,7},{5,7},{6,7}], 0, 1");
                checkAllOtherFieldsAreZero(uncC);
                break;
            case "rgba":
                assertEquals(
                        components.size(),
                        4,
                        "5.3.2 Table 5 rgba requires [{4,7},{5,7},{6,7},{7,7}], 0, 1");
                assertEquals(
                        componentDefinitions
                                .get(components.get(0).getComponentIndex())
                                .getComponentType(),
                        4,
                        "5.3.2 Table 5 rgba requires [{4,7},{5,7},{6,7},{7,7}], 0, 1");
                assertEquals(
                        components.get(0).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 rgba requires [{4,7},{5,7},{6,7},{7,7}], 0, 1");
                assertEquals(
                        componentDefinitions
                                .get(components.get(1).getComponentIndex())
                                .getComponentType(),
                        5,
                        "5.3.2 Table 5 rgba requires [{4,7},{5,7},{6,7},{7,7}], 0, 1");
                assertEquals(
                        components.get(1).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 rgba requires [{4,7},{5,7},{6,7},{7,7}], 0, 1");
                assertEquals(
                        componentDefinitions
                                .get(components.get(2).getComponentIndex())
                                .getComponentType(),
                        6,
                        "5.3.2 Table 5 rgba requires [{4,7},{5,7},{6,7},{7,7}], 0, 1");
                assertEquals(
                        components.get(2).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 rgba requires [{4,7},{5,7},{6,7},{7,7}], 0, 1");
                assertEquals(
                        componentDefinitions
                                .get(components.get(3).getComponentIndex())
                                .getComponentType(),
                        7,
                        "5.3.2 Table 5 rgba requires [{4,7},{5,7},{6,7},{7,7}], 0, 1");
                assertEquals(
                        components.get(3).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 rgba requires [{4,7},{5,7},{6,7},{7,7}], 0, 1");
                assertEquals(
                        uncC.getSamplingType().getEncodedValue(),
                        0,
                        "5.3.2 Table 5 rgba requires [{4,7},{5,7},{6,7},{7,7}], 0, 1");

                assertEquals(
                        uncC.getInterleaveType().getEncodedValue(),
                        1,
                        "5.3.2 Table 5 rgba requires [{4,7},{5,7},{6,7},{7,7}], 0, 1");
                checkAllOtherFieldsAreZero(uncC);
                break;
            case "i420":
                assertEquals(
                        components.size(),
                        3,
                        "5.3.2 Table 5 i420 requires [{1,7},{2,7},{3,7}], 2, 0");
                assertEquals(
                        componentDefinitions
                                .get(components.get(0).getComponentIndex())
                                .getComponentType(),
                        1,
                        "5.3.2 Table 5 i420 requires [{1,7},{2,7},{3,7}], 2, 0");
                assertEquals(
                        components.get(0).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 i420 requires [{1,7},{2,7},{3,7}], 2, 0");
                assertEquals(
                        componentDefinitions
                                .get(components.get(1).getComponentIndex())
                                .getComponentType(),
                        2,
                        "5.3.2 Table 5 i420 requires [{1,7},{2,7},{3,7}], 2, 0");
                assertEquals(
                        components.get(1).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 i420 requires [{1,7},{2,7},{3,7}], 2, 0");
                assertEquals(
                        componentDefinitions
                                .get(components.get(2).getComponentIndex())
                                .getComponentType(),
                        3,
                        "5.3.2 Table 5 i420 requires [{1,7},{2,7},{3,7}], 2, 0");
                assertEquals(
                        components.get(2).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 i420 requires [{1,7},{2,7},{3,7}], 2, 0");
                assertEquals(
                        uncC.getSamplingType().getEncodedValue(),
                        2,
                        "5.3.2 Table 5 i420 requires [{1,7},{2,7},{3,7}], 2, 0");
                assertEquals(
                        uncC.getInterleaveType().getEncodedValue(),
                        0,
                        "5.3.2 Table 5 i420 requires [{1,7},{2,7},{3,7}], 2, 0");
                checkAllOtherFieldsAreZero(uncC);
                break;
            case "nv12":
                assertEquals(
                        components.size(),
                        3,
                        "5.3.2 Table 5 nv12 requires [{1,7},{2,7},{3,7}], 2, 2");
                assertEquals(
                        componentDefinitions
                                .get(components.get(0).getComponentIndex())
                                .getComponentType(),
                        1,
                        "5.3.2 Table 5 nv12 requires [{1,7},{2,7},{3,7}], 2, 2");
                assertEquals(
                        components.get(0).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 nv12 requires [{1,7},{2,7},{3,7}], 2, 2");
                assertEquals(
                        componentDefinitions
                                .get(components.get(1).getComponentIndex())
                                .getComponentType(),
                        2,
                        "5.3.2 Table 5 nv12 requires [{1,7},{2,7},{3,7}], 2, 2");
                assertEquals(
                        components.get(1).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 nv12 requires [{1,7},{2,7},{3,7}], 2, 2");
                assertEquals(
                        componentDefinitions
                                .get(components.get(2).getComponentIndex())
                                .getComponentType(),
                        3,
                        "5.3.2 Table 5 nv12 requires [{1,7},{2,7},{3,7}], 2, 2");
                assertEquals(
                        components.get(2).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 nv12 requires [{1,7},{2,7},{3,7}], 2, 2");
                assertEquals(
                        uncC.getSamplingType().getEncodedValue(),
                        2,
                        "5.3.2 Table 5 nv12 requires [{1,7},{2,7},{3,7}], 2, 2");
                assertEquals(
                        uncC.getInterleaveType().getEncodedValue(),
                        2,
                        "5.3.2 Table 5 nv12 requires [{1,7},{2,7},{3,7}], 2, 2");
                checkAllOtherFieldsAreZero(uncC);
                break;
            case "nv21":
                assertEquals(
                        components.size(),
                        3,
                        "5.3.2 Table 5 nv21 requires [{1,7},{3,7},{2,7}], 2, 2");
                assertEquals(
                        componentDefinitions
                                .get(components.get(0).getComponentIndex())
                                .getComponentType(),
                        1,
                        "5.3.2 Table 5 nv21 requires [{1,7},{3,7},{2,7}], 2, 2");
                assertEquals(
                        components.get(0).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 nv21 requires [{1,7},{3,7},{2,7}], 2, 2");
                assertEquals(
                        componentDefinitions
                                .get(components.get(1).getComponentIndex())
                                .getComponentType(),
                        3,
                        "5.3.2 Table 5 nv21 requires [{1,7},{3,7},{2,7}], 2, 2");
                assertEquals(
                        components.get(1).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 nv21 requires [{1,7},{3,7},{2,7}], 2, 2");
                assertEquals(
                        componentDefinitions
                                .get(components.get(2).getComponentIndex())
                                .getComponentType(),
                        2,
                        "5.3.2 Table 5 nv21 requires [{1,7},{3,7},{2,7}], 2, 2");
                assertEquals(
                        components.get(2).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 nv21 requires [{1,7},{3,7},{2,7}], 2, 2");
                assertEquals(
                        uncC.getSamplingType().getEncodedValue(),
                        2,
                        "5.3.2 Table 5 nv21 requires [{1,7},{3,7},{2,7}], 2, 2");
                assertEquals(
                        uncC.getInterleaveType().getEncodedValue(),
                        2,
                        "5.3.2 Table 5 nv21 requires [{1,7},{3,7},{2,7}], 2, 2");
                checkAllOtherFieldsAreZero(uncC);
                break;
            case "abgr":
                assertEquals(
                        components.size(),
                        4,
                        "5.3.2 Table 5 abgr requires [{7,7},{6,7},{5,7},{4,7}], 0, 1");
                assertEquals(
                        componentDefinitions
                                .get(components.get(0).getComponentIndex())
                                .getComponentType(),
                        7,
                        "5.3.2 Table 5 abgr requires [{7,7},{6,7},{5,7},{4,7}], 0, 1");
                assertEquals(
                        components.get(0).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 abgr requires [{7,7},{6,7},{5,7},{4,7}], 0, 1");
                assertEquals(
                        componentDefinitions
                                .get(components.get(1).getComponentIndex())
                                .getComponentType(),
                        6,
                        "5.3.2 Table 5 abgr requires [{7,7},{6,7},{5,7},{4,7}], 0, 1");
                assertEquals(
                        components.get(1).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 abgr requires [{7,7},{6,7},{5,7},{4,7}], 0, 1");
                assertEquals(
                        componentDefinitions
                                .get(components.get(2).getComponentIndex())
                                .getComponentType(),
                        5,
                        "5.3.2 Table 5 abgr requires [{7,7},{6,7},{5,7},{4,7}], 0, 1");
                assertEquals(
                        components.get(2).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 abgr requires [{7,7},{6,7},{5,7},{4,7}], 0, 1");
                assertEquals(
                        componentDefinitions
                                .get(components.get(3).getComponentIndex())
                                .getComponentType(),
                        4,
                        "5.3.2 Table 5 abgr requires [{7,7},{6,7},{5,7},{4,7}], 0, 1");
                assertEquals(
                        components.get(3).getComponentBitDepthMinusOne(),
                        7,
                        "5.3.2 Table 5 abgr requires [{7,7},{6,7},{5,7},{4,7}], 0, 1");
                assertEquals(
                        uncC.getSamplingType().getEncodedValue(),
                        0,
                        "5.3.2 Table 5 abgr requires [{7,7},{6,7},{5,7},{4,7}], 0, 1");

                assertEquals(
                        uncC.getInterleaveType().getEncodedValue(),
                        1,
                        "5.3.2 Table 5 abgr requires [{7,7},{6,7},{5,7},{4,7}], 0, 1");
                checkAllOtherFieldsAreZero(uncC);
                break;
            default:
                fail("unhandled profile case: " + uncC.getProfile().toString());
        }
    }

    private void checkAllOtherFieldsAreZero(UncompressedFrameConfigBox uncC) {
        String message =
                String.format(
                        "5.3.2 requires all other fields to be zero for profile: %s",
                        uncC.getProfile().toString());
        for (Component component : uncC.getComponents()) {
            assertEquals(component.getComponentFormat(), 0, message);
            assertEquals(component.getComponentAlignSize(), 0, message);
        }
        assertEquals(uncC.getBlockSize(), 0, message);
        assertFalse(uncC.isComponentLittleEndian(), message);
        assertFalse(uncC.isBlockPadLSB(), message);
        assertFalse(uncC.isBlockLittleEndian(), message);
        assertFalse(uncC.isBlockReversed(), message);
        assertFalse(uncC.isPadUnknown(), message);
        assertEquals(uncC.getPixelSize(), 0, message);
        assertEquals(uncC.getRowAlignSize(), 0, message);
        assertEquals(uncC.getTileAlignSize(), 0, message);
        assertEquals(uncC.getNumTileRowsMinusOne(), 0, message);
        assertEquals(uncC.getNumTileColumnsMinusOne(), 0, message);
    }

    private void checkSensorBadPixelsMap(SensorBadPixelsMapBox sbpm, ComponentDefinitionBox cmpd) {
        // TODO: implement checks
    }

    private byte[] getRegionAnnotationBytes() throws IOException {
        RegionItem rgan = new RegionItem();
        rgan.setReferenceWidth(IMAGE_WIDTH);
        rgan.setReferenceHeight(IMAGE_HEIGHT);
        Region rectangleRegion =
                new Rectangle(
                        IMAGE_WIDTH / 4, 2 * IMAGE_HEIGHT / 3, IMAGE_WIDTH / 4, IMAGE_HEIGHT / 3);
        rgan.addRegion(rectangleRegion);
        Region pointRegion =
                new net.frogmouth.rnd.eofff.imagefileformat.items.rgan.Point(
                        3 * IMAGE_WIDTH / 8, 5 * IMAGE_HEIGHT / 6);
        rgan.addRegion(pointRegion);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        rgan.writeTo(streamWriter);
        return baos.toByteArray();
    }
}
