package net.frogmouth.rnd.eofff.uncompressed_experiments;

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
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Deflater;
import javax.imageio.ImageIO;
import net.frogmouth.rnd.eofff.imagefileformat.itemreferences.DerivedImage;
import net.frogmouth.rnd.eofff.imagefileformat.items.grid.GridItem;
import net.frogmouth.rnd.eofff.imagefileformat.properties.colr.ColourInformationProperty;
import net.frogmouth.rnd.eofff.imagefileformat.properties.hevc.HEVCConfigurationItemProperty;
import net.frogmouth.rnd.eofff.imagefileformat.properties.image.ImageSpatialExtentsProperty;
import net.frogmouth.rnd.eofff.imagefileformat.properties.pixi.PixelInformationProperty;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.free.FreeBox;
import net.frogmouth.rnd.eofff.isobmff.ftyp.Brand;
import net.frogmouth.rnd.eofff.isobmff.ftyp.FileTypeBox;
import net.frogmouth.rnd.eofff.isobmff.hdlr.HandlerBox;
import net.frogmouth.rnd.eofff.isobmff.idat.ItemDataBox;
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
import net.frogmouth.rnd.eofff.isobmff.iref.ItemReferenceBox;
import net.frogmouth.rnd.eofff.isobmff.mdat.MediaDataBox;
import net.frogmouth.rnd.eofff.isobmff.meta.MetaBox;
import net.frogmouth.rnd.eofff.isobmff.pitm.PrimaryItemBox;
import net.frogmouth.rnd.eofff.nalvideo.HEVCDecoderConfigurationArray;
import net.frogmouth.rnd.eofff.nalvideo.HEVCDecoderConfigurationRecord;
import net.frogmouth.rnd.eofff.nalvideo.NALU;
import net.frogmouth.rnd.eofff.uncompressed.cmpc.CompressionConfigurationItemProperty;
import net.frogmouth.rnd.eofff.uncompressed.cmpc.CompressionRangeType;
import net.frogmouth.rnd.eofff.uncompressed.cmpd.ComponentDefinition;
import net.frogmouth.rnd.eofff.uncompressed.cmpd.ComponentDefinitionBox;
import net.frogmouth.rnd.eofff.uncompressed.icef.CompressedUnitInfo;
import net.frogmouth.rnd.eofff.uncompressed.icef.GenericallyCompressedUnitsItemInfo;
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
    protected static final int NUM_TILES_HORIZ = 4;
    protected static final int NUM_TILES_VERT = 2;
    protected static final int TILE_WIDTH = IMAGE_WIDTH / NUM_TILES_HORIZ;
    protected static final int TILE_HEIGHT = IMAGE_HEIGHT / NUM_TILES_VERT;
    private static final int MDAT_START = 450;
    private static final int ROW_MDAT_START = 950;
    private static final int PIXEL_MDAT_START = 75000;
    protected static final int NUM_BYTES_PER_PIXEL_RGB = 3;

    private static final int MDAT_HEADER_BYTES = 8;

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
    public void zlib_row() throws IOException {
        GenericallyCompressedUnitsItemInfo icef = new GenericallyCompressedUnitsItemInfo();
        MediaDataBox mdat = createMediaDataBox_rgb_component_row_zlib(icef);

        List<Box> boxes = new ArrayList<>();
        FileTypeBox ftyp = createFileTypeBox();
        boxes.add(ftyp);
        MetaBox meta = createMetaBox_rgb_component_row_zlib(mdat.getBodySize(), icef);
        boxes.add(meta);
        long lengthOfPreviousBoxes = ftyp.getSize() + meta.getSize();
        long numberOfFillBytes = ROW_MDAT_START - lengthOfPreviousBoxes - LENGTH_OF_FREEBOX_HEADER;
        FreeBox free = new FreeBox();
        free.setData(new byte[(int) numberOfFillBytes]);
        boxes.add(free);
        boxes.add(mdat);
        writeBoxes(boxes, "rgb_generic_compressed_zlib_rows.heif");
    }

    @Test
    public void zlib_pixel() throws IOException {
        GenericallyCompressedUnitsItemInfo icef = new GenericallyCompressedUnitsItemInfo();
        MediaDataBox mdat = createMediaDataBox_rgb_component_zlib_pixel(icef);

        List<Box> boxes = new ArrayList<>();
        FileTypeBox ftyp = createFileTypeBox();
        boxes.add(ftyp);
        MetaBox meta = createMetaBox_rgb_component_zlib_pixel(mdat.getBodySize(), icef);
        boxes.add(meta);
        long lengthOfPreviousBoxes = ftyp.getSize() + meta.getSize();
        long numberOfFillBytes =
                PIXEL_MDAT_START - lengthOfPreviousBoxes - LENGTH_OF_FREEBOX_HEADER;
        FreeBox free = new FreeBox();
        free.setData(new byte[(int) numberOfFillBytes]);
        boxes.add(free);
        boxes.add(mdat);
        writeBoxes(boxes, "rgb_generic_compressed_zlib_pixel.heif");
    }

    @Test
    public void zlib_tiled() throws IOException {
        GenericallyCompressedUnitsItemInfo icef = new GenericallyCompressedUnitsItemInfo();
        MediaDataBox mdat = createMediaDataBox_rgb_component_zlib_tiled(icef);

        List<Box> boxes = new ArrayList<>();
        FileTypeBox ftyp = createFileTypeBox();
        boxes.add(ftyp);
        MetaBox meta = createMetaBox_rgb_component_tile_zlib(mdat.getBodySize(), icef);
        boxes.add(meta);
        long lengthOfPreviousBoxes = ftyp.getSize() + meta.getSize();
        long numberOfFillBytes = MDAT_START - lengthOfPreviousBoxes - LENGTH_OF_FREEBOX_HEADER;
        FreeBox free = new FreeBox();
        free.setData(new byte[(int) numberOfFillBytes]);
        boxes.add(free);
        boxes.add(mdat);
        writeBoxes(boxes, "rgb_generic_compressed_zlib_tiled.heif");
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
    public void deflate_tiled() throws IOException {
        GenericallyCompressedUnitsItemInfo icef = new GenericallyCompressedUnitsItemInfo();
        MediaDataBox mdat = createMediaDataBox_rgb_component_deflate_tiled(icef);

        List<Box> boxes = new ArrayList<>();
        FileTypeBox ftyp = createFileTypeBox();
        boxes.add(ftyp);
        MetaBox meta = createMetaBox_rgb_component_tile_deflate(mdat.getBodySize(), icef);
        boxes.add(meta);
        long lengthOfPreviousBoxes = ftyp.getSize() + meta.getSize();
        long numberOfFillBytes = MDAT_START - lengthOfPreviousBoxes - LENGTH_OF_FREEBOX_HEADER;
        FreeBox free = new FreeBox();
        free.setData(new byte[(int) numberOfFillBytes]);
        boxes.add(free);
        boxes.add(mdat);
        writeBoxes(boxes, "rgb_generic_compressed_tile_deflate.heif");
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

    @Test
    public void mixed_grid() throws IOException {
        final int GRID_ITEM_ID = 1;
        final int BROTLI_ITEM_ID = 2;
        final int DEFLATE_ITEM_ID = 3;
        final int ZLIB_ITEM_ID = 4;
        final int HEVC_ITEM_ID = 5;
        final int tileWidth = 400;
        final int tileHeight = 300;
        MediaDataBox mdat = new MediaDataBox();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] compressedBrotliBytes = createDataBrotliTile(tileWidth, tileHeight);
        baos.write(compressedBrotliBytes);
        byte[] compressedDeflateBytes = createDataDeflateTile(tileWidth, tileHeight);
        baos.write(compressedDeflateBytes);
        byte[] compressedZlibBytes = createDataZlibTile(tileWidth, tileHeight);
        baos.write(compressedZlibBytes);
        byte[] compressedHevcBytes = readCompressedHevcBytes();
        baos.write(compressedHevcBytes);
        int mdatOffset = 0;
        ILocItem brotliItemLocation = new ILocItem();
        {
            brotliItemLocation.setConstructionMethod(0);
            brotliItemLocation.setItemId(BROTLI_ITEM_ID);
            ILocExtent brotliItemExtent = new ILocExtent();
            brotliItemExtent.setExtentIndex(0);
            brotliItemExtent.setExtentOffset(mdatOffset);
            mdatOffset += compressedBrotliBytes.length;
            brotliItemExtent.setExtentLength(compressedBrotliBytes.length);
            brotliItemLocation.addExtent(brotliItemExtent);
        }
        ILocItem deflateItemLocation = new ILocItem();
        {
            deflateItemLocation.setConstructionMethod(0);
            deflateItemLocation.setItemId(DEFLATE_ITEM_ID);
            ILocExtent deflateItemExtent = new ILocExtent();
            deflateItemExtent.setExtentIndex(0);
            deflateItemExtent.setExtentOffset(mdatOffset);
            mdatOffset += compressedDeflateBytes.length;
            deflateItemExtent.setExtentLength(compressedDeflateBytes.length);
            deflateItemLocation.addExtent(deflateItemExtent);
        }
        ILocItem zlibItemLocation = new ILocItem();
        {
            zlibItemLocation.setConstructionMethod(0);
            zlibItemLocation.setItemId(ZLIB_ITEM_ID);
            ILocExtent zlibItemExtent = new ILocExtent();
            zlibItemExtent.setExtentIndex(0);
            zlibItemExtent.setExtentOffset(mdatOffset);
            mdatOffset += compressedZlibBytes.length;
            zlibItemExtent.setExtentLength(compressedZlibBytes.length);
            zlibItemLocation.addExtent(zlibItemExtent);
        }
        ILocItem hevcItemLocation = new ILocItem();
        {
            hevcItemLocation.setConstructionMethod(0);
            hevcItemLocation.setItemId(HEVC_ITEM_ID);
            ILocExtent hevcItemExtent = new ILocExtent();
            hevcItemExtent.setExtentIndex(0);
            hevcItemExtent.setExtentOffset(mdatOffset);
            // mdatOffset += compressedZlibBytes.length;
            hevcItemExtent.setExtentLength(compressedHevcBytes.length);
            hevcItemLocation.addExtent(hevcItemExtent);
        }
        makeHevcReferenceItem(tileWidth, tileHeight);
        mdat.setData(baos.toByteArray());

        List<Box> boxes = new ArrayList<>();
        FileTypeBox ftyp = createFileTypeBox();
        boxes.add(ftyp);
        MetaBox meta = new MetaBox();
        List<Box> metaChildren = new ArrayList<>();
        metaChildren.add(makeHandlerBox());
        PrimaryItemBox pitm = new PrimaryItemBox();
        pitm.setItemID(GRID_ITEM_ID);
        metaChildren.add(pitm);
        ItemInfoBox iinf = new ItemInfoBox();
        {
            ItemInfoEntry gridEntry = new ItemInfoEntry();
            gridEntry.setVersion(2);
            gridEntry.setItemID(GRID_ITEM_ID);
            FourCC grid = new FourCC("grid");
            gridEntry.setItemType(grid.asUnsigned());
            gridEntry.setItemName("Grid Image");
            iinf.addItem(gridEntry);
        }
        {
            ItemInfoEntry brotliItemEntry = new ItemInfoEntry();
            brotliItemEntry.setVersion(2);
            brotliItemEntry.setFlags(1); // Hidden
            brotliItemEntry.setItemID(BROTLI_ITEM_ID);
            FourCC unci = new FourCC("unci");
            brotliItemEntry.setItemType(unci.asUnsigned());
            brotliItemEntry.setItemName("Grid input image (0,0) - Brotli");
            iinf.addItem(brotliItemEntry);
        }
        {
            ItemInfoEntry deflateItemEntry = new ItemInfoEntry();
            deflateItemEntry.setVersion(2);
            deflateItemEntry.setFlags(1); // Hidden
            deflateItemEntry.setItemID(DEFLATE_ITEM_ID);
            FourCC unci = new FourCC("unci");
            deflateItemEntry.setItemType(unci.asUnsigned());
            deflateItemEntry.setItemName("Grid input image (0,1) - Deflate");
            iinf.addItem(deflateItemEntry);
        }
        {
            ItemInfoEntry zlibItemEntry = new ItemInfoEntry();
            zlibItemEntry.setVersion(2);
            zlibItemEntry.setFlags(1); // Hidden
            zlibItemEntry.setItemID(ZLIB_ITEM_ID);
            FourCC unci = new FourCC("unci");
            zlibItemEntry.setItemType(unci.asUnsigned());
            zlibItemEntry.setItemName("Grid input image (1,0) - Zlib");
            iinf.addItem(zlibItemEntry);
        }
        {
            ItemInfoEntry hevcItemEntry = new ItemInfoEntry();
            hevcItemEntry.setVersion(2);
            hevcItemEntry.setFlags(1); // Hidden
            hevcItemEntry.setItemID(HEVC_ITEM_ID);
            FourCC unci = new FourCC("hvc1");
            hevcItemEntry.setItemType(unci.asUnsigned());
            hevcItemEntry.setItemName("Grid input image (1,1) - HEVC");
            iinf.addItem(hevcItemEntry);
        }
        metaChildren.add(iinf);
        ItemDataBox idat = new ItemDataBox();
        ItemLocationBox iloc = new ItemLocationBox();
        iloc.setOffsetSize(4);
        iloc.setLengthSize(4);
        iloc.setBaseOffsetSize(4);
        iloc.setIndexSize(4);
        iloc.setVersion(1);
        {
            GridItem gridItem = new GridItem();
            gridItem.setRows(2);
            gridItem.setColumns(2);
            gridItem.setOutput_width(2 * tileWidth);
            gridItem.setOutput_height(2 * tileHeight);
            ByteArrayOutputStream baosGrid = new ByteArrayOutputStream();
            OutputStreamWriter streamWriterGrid = new OutputStreamWriter(baosGrid);
            gridItem.writeTo(streamWriterGrid);
            byte[] gridBytes = baosGrid.toByteArray();
            ILocItem gridItemLocation = new ILocItem();
            gridItemLocation.setConstructionMethod(1);
            gridItemLocation.setItemId(GRID_ITEM_ID);
            ILocExtent gridItemExtent = new ILocExtent();
            gridItemExtent.setExtentIndex(0);
            gridItemExtent.setExtentOffset(0);
            gridItemExtent.setExtentLength(gridBytes.length);
            gridItemLocation.addExtent(gridItemExtent);
            iloc.addItem(gridItemLocation);
            idat.setData(gridBytes);
        }
        iloc.addItem(brotliItemLocation);
        iloc.addItem(deflateItemLocation);
        iloc.addItem(zlibItemLocation);
        iloc.addItem(hevcItemLocation);
        metaChildren.add(iloc);
        metaChildren.add(idat);
        ItemPropertiesBox iprp = new ItemPropertiesBox();
        ItemPropertyContainerBox ipco = new ItemPropertyContainerBox();
        ipco.addProperty(makeComponentDefinitionBox_rgb_generic()); // 1
        ipco.addProperty(makeUncompressedFrameConfigBox_rgb_component()); // 2
        {
            ImageSpatialExtentsProperty ispeGrid = new ImageSpatialExtentsProperty();
            ispeGrid.setImageHeight(2 * tileHeight);
            ispeGrid.setImageWidth(2 * tileWidth);
            ipco.addProperty(ispeGrid); // 3
        }
        {
            ImageSpatialExtentsProperty ispeItem = new ImageSpatialExtentsProperty();
            ispeItem.setImageHeight(tileHeight);
            ispeItem.setImageWidth(tileWidth);
            ipco.addProperty(ispeItem); // 4
        }
        ipco.addProperty(makeCompressionConfigurationBoxWholeFileBrotli()); // 5
        ipco.addProperty(makeCompressionConfigurationBoxWholeFileDeflate()); // 6
        ipco.addProperty(makeCompressionConfigurationBoxWholeFileZlib()); // 7
        {
            HEVCConfigurationItemProperty hvcC = new HEVCConfigurationItemProperty();
            HEVCDecoderConfigurationRecord config = new HEVCDecoderConfigurationRecord();
            config.setConfigurationVersion(1);
            config.setGeneral_profile_space(0);
            config.setGeneral_tier_flag(0);
            config.setGeneral_profile_idc(3);
            byte[] compatibilityFlags = new byte[] {0b01110000, 0x00, 0x00, 0x00};
            config.setGeneral_profile_compatibility_flags(compatibilityFlags);
            byte[] indicatorFlags = new byte[] {0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
            config.setGeneral_constraint_indicator_flags(indicatorFlags);
            config.setGeneral_level_idc(60);
            config.setMin_spatial_segmentation_idc(0);
            config.setParallelismType(0);
            config.setChromaFormat(0); // TODO: 4:2:0
            config.setBitDepthLumaMinus8(0);
            config.setBitDepthChromaMinus8(0);
            config.setAvgFrameRate(0);
            config.setConstantFrameRate(0);
            config.setNumTemporalLayers(1);
            config.setTemporalIdNested(1);
            HEVCDecoderConfigurationArray array1 = new HEVCDecoderConfigurationArray();
            array1.setNal_unit_type(32);
            NALU nalu1 = new NALU();
            nalu1.setNalUnit(
                    new byte[] {
                        0x40,
                        0x01,
                        0x0c,
                        0x01,
                        (byte) 0xff,
                        (byte) 0xff,
                        0x03,
                        0x70,
                        0x00,
                        0x00,
                        0x03,
                        0x00,
                        (byte) 0x90,
                        0x00,
                        0x00,
                        0x03,
                        0x00,
                        0x00,
                        0x03,
                        0x00,
                        0x3c,
                        (byte) 0xba,
                        0x02,
                        0x40
                    });
            array1.addNALU(nalu1);
            array1.setArray_completion(false);
            config.addArray(array1);
            HEVCDecoderConfigurationArray array2 = new HEVCDecoderConfigurationArray();
            array2.setNal_unit_type(33);
            array2.setArray_completion(false);
            NALU nalu2 = new NALU();
            nalu2.setNalUnit(
                    new byte[] {
                        0x42,
                        0x01,
                        0x01,
                        0x03,
                        0x70,
                        0x00,
                        0x00,
                        0x03,
                        0x00,
                        (byte) 0x90,
                        0x00,
                        0x00,
                        0x03,
                        0x00,
                        0x00,
                        0x03,
                        0x00,
                        0x3c,
                        (byte) 0xa0,
                        0x0c,
                        (byte) 0x88,
                        0x04,
                        (byte) 0xc7,
                        (byte) 0xde,
                        0x5b,
                        (byte) 0xa9,
                        0x24,
                        (byte) 0xa6,
                        (byte) 0xb9,
                        (byte) 0xb8,
                        0x08,
                        0x68,
                        0x30,
                        0x20,
                        0x00,
                        0x00,
                        0x03,
                        0x00,
                        0x20,
                        0x00,
                        0x00,
                        0x03,
                        0x00,
                        0x21
                    });
            array2.addNALU(nalu2);
            array2.setArray_completion(false);
            config.addArray(array2);
            HEVCDecoderConfigurationArray array3 = new HEVCDecoderConfigurationArray();
            array3.setNal_unit_type(34);
            NALU nalu3 = new NALU();
            nalu3.setNalUnit(new byte[] {0x44, 0x01, (byte) 0xc1, 0x72, (byte) 0xb0, 0x62, 0x40});
            array3.addNALU(nalu3);
            config.addArray(array3);
            hvcC.setHevcConfig(config);
            ipco.addProperty(hvcC); // 8
        }
        {
            ColourInformationProperty colr = new ColourInformationProperty();
            colr.setColourType(new FourCC("nclx"));
            colr.setColourPrimaries((short) 1);
            colr.setTransferCharacteristics((short) 13);
            colr.setMatrixCoefficients((short) 6);
            colr.setFullRange(true);
            ipco.addProperty(colr); // 9
        }
        {
            PixelInformationProperty pixi = new PixelInformationProperty();
            pixi.addChannel(8);
            pixi.addChannel(8);
            pixi.addChannel(8);
            ipco.addProperty(pixi); // 10
        }
        iprp.setItemProperties(ipco);
        ItemPropertyAssociation ipma = new ItemPropertyAssociation();
        ItemPropertyAssociation assoc = new ItemPropertyAssociation();
        {
            AssociationEntry entry = new AssociationEntry();
            entry.setItemId(GRID_ITEM_ID);
            {
                PropertyAssociation associationToImageSpatialExtentsProperty =
                        new PropertyAssociation();
                associationToImageSpatialExtentsProperty.setPropertyIndex(3);
                associationToImageSpatialExtentsProperty.setEssential(false);
                entry.addAssociation(associationToImageSpatialExtentsProperty);
            }
            assoc.addEntry(entry);
        }
        {
            AssociationEntry entry = new AssociationEntry();
            entry.setItemId(BROTLI_ITEM_ID);
            {
                PropertyAssociation associationToImageSpatialExtentsProperty =
                        new PropertyAssociation();
                associationToImageSpatialExtentsProperty.setPropertyIndex(4);
                associationToImageSpatialExtentsProperty.setEssential(true);
                entry.addAssociation(associationToImageSpatialExtentsProperty);
            }
            {
                PropertyAssociation associationToComponentDefinition = new PropertyAssociation();
                associationToComponentDefinition.setPropertyIndex(1);
                associationToComponentDefinition.setEssential(true);
                entry.addAssociation(associationToComponentDefinition);
            }
            {
                PropertyAssociation associationToUncompressedFrameConfigBox =
                        new PropertyAssociation();
                associationToUncompressedFrameConfigBox.setPropertyIndex(2);
                associationToUncompressedFrameConfigBox.setEssential(true);
                entry.addAssociation(associationToUncompressedFrameConfigBox);
            }
            {
                PropertyAssociation associationToCompressionConfiguration =
                        new PropertyAssociation();
                associationToCompressionConfiguration.setPropertyIndex(5);
                associationToCompressionConfiguration.setEssential(true);
                entry.addAssociation(associationToCompressionConfiguration);
            }
            assoc.addEntry(entry);
        }
        {
            AssociationEntry entry = new AssociationEntry();
            entry.setItemId(DEFLATE_ITEM_ID);
            {
                PropertyAssociation associationToImageSpatialExtentsProperty =
                        new PropertyAssociation();
                associationToImageSpatialExtentsProperty.setPropertyIndex(4);
                associationToImageSpatialExtentsProperty.setEssential(true);
                entry.addAssociation(associationToImageSpatialExtentsProperty);
            }
            {
                PropertyAssociation associationToComponentDefinition = new PropertyAssociation();
                associationToComponentDefinition.setPropertyIndex(1);
                associationToComponentDefinition.setEssential(true);
                entry.addAssociation(associationToComponentDefinition);
            }
            {
                PropertyAssociation associationToUncompressedFrameConfigBox =
                        new PropertyAssociation();
                associationToUncompressedFrameConfigBox.setPropertyIndex(2);
                associationToUncompressedFrameConfigBox.setEssential(true);
                entry.addAssociation(associationToUncompressedFrameConfigBox);
            }
            {
                PropertyAssociation associationToCompressionConfiguration =
                        new PropertyAssociation();
                associationToCompressionConfiguration.setPropertyIndex(6);
                associationToCompressionConfiguration.setEssential(true);
                entry.addAssociation(associationToCompressionConfiguration);
            }
            assoc.addEntry(entry);
        }
        {
            AssociationEntry entry = new AssociationEntry();
            entry.setItemId(ZLIB_ITEM_ID);
            {
                PropertyAssociation associationToImageSpatialExtentsProperty =
                        new PropertyAssociation();
                associationToImageSpatialExtentsProperty.setPropertyIndex(4);
                associationToImageSpatialExtentsProperty.setEssential(true);
                entry.addAssociation(associationToImageSpatialExtentsProperty);
            }
            {
                PropertyAssociation associationToComponentDefinition = new PropertyAssociation();
                associationToComponentDefinition.setPropertyIndex(1);
                associationToComponentDefinition.setEssential(true);
                entry.addAssociation(associationToComponentDefinition);
            }
            {
                PropertyAssociation associationToUncompressedFrameConfigBox =
                        new PropertyAssociation();
                associationToUncompressedFrameConfigBox.setPropertyIndex(2);
                associationToUncompressedFrameConfigBox.setEssential(true);
                entry.addAssociation(associationToUncompressedFrameConfigBox);
            }
            {
                PropertyAssociation associationToCompressionConfiguration =
                        new PropertyAssociation();
                associationToCompressionConfiguration.setPropertyIndex(7);
                associationToCompressionConfiguration.setEssential(true);
                entry.addAssociation(associationToCompressionConfiguration);
            }
            assoc.addEntry(entry);
        }
        {
            AssociationEntry entry = new AssociationEntry();
            entry.setItemId(HEVC_ITEM_ID);
            {
                PropertyAssociation associationToImageSpatialExtentsProperty =
                        new PropertyAssociation();
                associationToImageSpatialExtentsProperty.setPropertyIndex(4);
                associationToImageSpatialExtentsProperty.setEssential(false);
                entry.addAssociation(associationToImageSpatialExtentsProperty);
            }
            {
                PropertyAssociation associationToHevcCompressionConfiguration =
                        new PropertyAssociation();
                associationToHevcCompressionConfiguration.setPropertyIndex(8);
                associationToHevcCompressionConfiguration.setEssential(true);
                entry.addAssociation(associationToHevcCompressionConfiguration);
            }
            {
                PropertyAssociation associationToColourBox = new PropertyAssociation();
                associationToColourBox.setPropertyIndex(9);
                associationToColourBox.setEssential(false);
                entry.addAssociation(associationToColourBox);
            }
            {
                PropertyAssociation associationToPixelInformationProperty =
                        new PropertyAssociation();
                associationToPixelInformationProperty.setPropertyIndex(10);
                associationToPixelInformationProperty.setEssential(false);
                entry.addAssociation(associationToPixelInformationProperty);
            }
            assoc.addEntry(entry);
        }
        iprp.addItemPropertyAssociation(assoc);
        metaChildren.add(iprp);
        ItemReferenceBox iref = new ItemReferenceBox();
        {
            DerivedImage grid_item_dimg = new DerivedImage();
            grid_item_dimg.setFromItemId(GRID_ITEM_ID);
            grid_item_dimg.addReference(BROTLI_ITEM_ID);
            grid_item_dimg.addReference(DEFLATE_ITEM_ID);
            grid_item_dimg.addReference(ZLIB_ITEM_ID);
            grid_item_dimg.addReference(HEVC_ITEM_ID);
            iref.addItem(grid_item_dimg);
        }
        metaChildren.add(iref);
        meta.addNestedBoxes(metaChildren);
        boxes.add(meta);
        long lengthOfPreviousBoxes = ftyp.getSize() + meta.getSize();
        for (var item : iloc.getItems()) {
            if (item.getConstructionMethod() == 0) {
                item.setBaseOffset(lengthOfPreviousBoxes + MDAT_HEADER_BYTES);
            }
        }
        boxes.add(mdat);
        writeBoxes(boxes, "rgb_generic_compressed_mixed.heif");
    }

    private byte[] createDataBrotliTile(int width, int height) throws IOException {
        SampleModel sampleModel = new BandedSampleModel(DataBuffer.TYPE_BYTE, width, height, 3);
        WritableRaster raster = Raster.createWritableRaster(sampleModel, (Point) null);
        ColorModel colourModel =
                new ComponentColorModel(
                        ColorSpace.getInstance(ColorSpace.CS_sRGB),
                        false,
                        true,
                        Transparency.OPAQUE,
                        DataBuffer.TYPE_BYTE);
        BufferedImage image = new BufferedImage(colourModel, raster, true, null);
        Graphics2D g2 = image.createGraphics();
        g2.setColor(Color.RED);
        g2.fillRect(0, 0, width, height);
        g2.setColor(Color.BLACK);
        g2.drawString("Brotli (0,0)", 100, 100);
        ImageIO.write(image, "PNG", new File("ref_brotli_uncompressed.png"));
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
        Brotli4jLoader.ensureAvailability();
        return Encoder.compress(data);
    }

    private byte[] createDataDeflateTile(int width, int height) throws IOException {
        SampleModel sampleModel = new BandedSampleModel(DataBuffer.TYPE_BYTE, width, height, 3);
        WritableRaster raster = Raster.createWritableRaster(sampleModel, (Point) null);
        ColorModel colourModel =
                new ComponentColorModel(
                        ColorSpace.getInstance(ColorSpace.CS_sRGB),
                        false,
                        true,
                        Transparency.OPAQUE,
                        DataBuffer.TYPE_BYTE);
        BufferedImage image = new BufferedImage(colourModel, raster, true, null);
        Graphics2D g2 = image.createGraphics();
        g2.setColor(Color.GREEN);
        g2.fillRect(0, 0, width, height);
        g2.setColor(Color.BLACK);
        g2.drawString("Deflate (0,1)", 100, 100);
        ImageIO.write(image, "PNG", new File("ref_deflate_uncompressed.png"));
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
        return deflateData(data);
    }

    private byte[] createDataZlibTile(int width, int height) throws IOException {
        SampleModel sampleModel = new BandedSampleModel(DataBuffer.TYPE_BYTE, width, height, 3);
        WritableRaster raster = Raster.createWritableRaster(sampleModel, (Point) null);
        ColorModel colourModel =
                new ComponentColorModel(
                        ColorSpace.getInstance(ColorSpace.CS_sRGB),
                        false,
                        true,
                        Transparency.OPAQUE,
                        DataBuffer.TYPE_BYTE);
        BufferedImage image = new BufferedImage(colourModel, raster, true, null);
        Graphics2D g2 = image.createGraphics();
        g2.setColor(Color.YELLOW);
        g2.fillRect(0, 0, width, height);
        g2.setColor(Color.BLACK);
        g2.drawString("Zlib (1,0)", 100, 100);
        ImageIO.write(image, "PNG", new File("ref_zlib_uncompressed.png"));
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
        return zlibData(data);
    }

    private void makeHevcReferenceItem(int width, int height) throws IOException {
        SampleModel sampleModel = new BandedSampleModel(DataBuffer.TYPE_BYTE, width, height, 3);
        WritableRaster raster = Raster.createWritableRaster(sampleModel, (Point) null);
        ColorModel colourModel =
                new ComponentColorModel(
                        ColorSpace.getInstance(ColorSpace.CS_sRGB),
                        false,
                        true,
                        Transparency.OPAQUE,
                        DataBuffer.TYPE_BYTE);
        BufferedImage image = new BufferedImage(colourModel, raster, true, null);
        Graphics2D g2 = image.createGraphics();
        g2.setColor(Color.PINK);
        g2.fillRect(0, 0, width, height);
        g2.setColor(Color.BLACK);
        g2.drawString("HEVC (1,1)", 100, 100);
        ImageIO.write(image, "PNG", new File("ref_hevc_uncompressed.png"));
    }

    private byte[] readCompressedHevcBytes() throws IOException {
        return Files.readAllBytes(Paths.get("/home/bradh/testbed20/hevc_body.bin"));
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
        boxes.add(makeItemLocationBox_rgb3(MDAT_START + 8, extentLength));
        boxes.add(makeItemPropertiesBox_rgb_component_whole_file_zlib());
        meta.addNestedBoxes(boxes);
        return meta;
    }

    private MetaBox createMetaBox_rgb_component_row_zlib(
            long extentLength, GenericallyCompressedUnitsItemInfo icef) throws IOException {
        MetaBox meta = new MetaBox();
        List<Box> boxes = new ArrayList<>();
        boxes.add(makeHandlerBox());
        boxes.add(makePrimaryItemBox());
        boxes.add(makeItemInfoBox());
        boxes.add(makeItemLocationBox_rgb3(ROW_MDAT_START + 8, extentLength));
        boxes.add(makeItemPropertiesBox_rgb_component_row_zlib(icef));
        meta.addNestedBoxes(boxes);
        return meta;
    }

    private MetaBox createMetaBox_rgb_component_zlib_pixel(
            long extentLength, GenericallyCompressedUnitsItemInfo icef) throws IOException {
        MetaBox meta = new MetaBox();
        List<Box> boxes = new ArrayList<>();
        boxes.add(makeHandlerBox());
        boxes.add(makePrimaryItemBox());
        boxes.add(makeItemInfoBox());
        boxes.add(makeItemLocationBox_rgb3(PIXEL_MDAT_START + 8, extentLength));
        boxes.add(makeItemPropertiesBox_rgb_component_pixel_zlib(icef));
        meta.addNestedBoxes(boxes);
        return meta;
    }

    private MetaBox createMetaBox_rgb_component_tile_zlib(
            long extentLength, GenericallyCompressedUnitsItemInfo icef) throws IOException {
        MetaBox meta = new MetaBox();
        List<Box> boxes = new ArrayList<>();
        boxes.add(makeHandlerBox());
        boxes.add(makePrimaryItemBox());
        boxes.add(makeItemInfoBox());
        boxes.add(makeItemLocationBox_rgb3(MDAT_START + 8, extentLength));
        boxes.add(makeItemPropertiesBox_rgb_component_tile_zlib(icef));
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
        boxes.add(makeItemLocationBox_rgb3(MDAT_START + 8, extentLength));
        boxes.add(makeItemPropertiesBox_rgb_component_whole_file_deflate());
        meta.addNestedBoxes(boxes);
        return meta;
    }

    private MetaBox createMetaBox_rgb_component_tile_deflate(
            long extentLength, GenericallyCompressedUnitsItemInfo icef) throws IOException {
        MetaBox meta = new MetaBox();
        List<Box> boxes = new ArrayList<>();
        boxes.add(makeHandlerBox());
        boxes.add(makePrimaryItemBox());
        boxes.add(makeItemInfoBox());
        boxes.add(makeItemLocationBox_rgb3(MDAT_START + 8, extentLength));
        boxes.add(makeItemPropertiesBox_rgb_component_tile_deflate(icef));
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
        boxes.add(makeItemLocationBox_rgb3(MDAT_START + 8, extentLength));
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

    private ItemLocationBox makeItemLocationBox_rgb3(long offset, long extentLength) {
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
        mainItemExtent.setExtentOffset(offset);
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
        iprp.setItemProperties(ipco);

        iprp.addItemPropertyAssociation(makePropertyAssociations_whole_file());

        return iprp;
    }

    private Box makeItemPropertiesBox_rgb_component_row_zlib(
            GenericallyCompressedUnitsItemInfo icef) {
        ItemPropertiesBox iprp = new ItemPropertiesBox();
        ItemPropertyContainerBox ipco = new ItemPropertyContainerBox();
        ipco.addProperty(makeComponentDefinitionBox_rgb_generic());
        ipco.addProperty(makeUncompressedFrameConfigBox_rgb_component());
        ipco.addProperty(makeImageSpatialExtentsProperty());
        ipco.addProperty(makeCompressionConfigurationBoxRowZlib());
        ipco.addProperty(icef);
        iprp.setItemProperties(ipco);

        iprp.addItemPropertyAssociation(makePropertyAssociations_with_icef());

        return iprp;
    }

    private Box makeItemPropertiesBox_rgb_component_pixel_zlib(
            GenericallyCompressedUnitsItemInfo icef) {
        ItemPropertiesBox iprp = new ItemPropertiesBox();
        ItemPropertyContainerBox ipco = new ItemPropertyContainerBox();
        ipco.addProperty(makeComponentDefinitionBox_rgb_generic());
        ipco.addProperty(makeUncompressedFrameConfigBox_rgb_component());
        ipco.addProperty(makeImageSpatialExtentsProperty());
        ipco.addProperty(makeCompressionConfigurationBoxPixelZlib());
        ipco.addProperty(icef);
        iprp.setItemProperties(ipco);

        iprp.addItemPropertyAssociation(makePropertyAssociations_with_icef());

        return iprp;
    }

    private Box makeItemPropertiesBox_rgb_component_tile_zlib(
            GenericallyCompressedUnitsItemInfo icef) {
        ItemPropertiesBox iprp = new ItemPropertiesBox();
        ItemPropertyContainerBox ipco = new ItemPropertyContainerBox();
        ipco.addProperty(makeComponentDefinitionBox_rgb_generic());
        ipco.addProperty(makeUncompressedFrameConfigBox_rgb_component_tiled());
        ipco.addProperty(makeImageSpatialExtentsProperty());
        ipco.addProperty(makeCompressionConfigurationBoxTileZlib());
        ipco.addProperty(icef);
        iprp.setItemProperties(ipco);

        iprp.addItemPropertyAssociation(makePropertyAssociations_with_icef());

        return iprp;
    }

    private Box makeItemPropertiesBox_rgb_component_whole_file_deflate() {
        ItemPropertiesBox iprp = new ItemPropertiesBox();
        ItemPropertyContainerBox ipco = new ItemPropertyContainerBox();
        ipco.addProperty(makeComponentDefinitionBox_rgb_generic());
        ipco.addProperty(makeUncompressedFrameConfigBox_rgb_component());
        ipco.addProperty(makeImageSpatialExtentsProperty());
        ipco.addProperty(makeCompressionConfigurationBoxWholeFileDeflate());
        ipco.addProperty(new GenericallyCompressedUnitsItemInfo());
        iprp.setItemProperties(ipco);

        iprp.addItemPropertyAssociation(makePropertyAssociations_whole_file());

        return iprp;
    }

    private Box makeItemPropertiesBox_rgb_component_tile_deflate(
            GenericallyCompressedUnitsItemInfo icef) {
        ItemPropertiesBox iprp = new ItemPropertiesBox();
        ItemPropertyContainerBox ipco = new ItemPropertyContainerBox();
        ipco.addProperty(makeComponentDefinitionBox_rgb_generic());
        ipco.addProperty(makeUncompressedFrameConfigBox_rgb_component_tiled());
        ipco.addProperty(makeImageSpatialExtentsProperty());
        ipco.addProperty(makeCompressionConfigurationBoxTileDeflate());
        ipco.addProperty(icef);
        iprp.setItemProperties(ipco);

        iprp.addItemPropertyAssociation(makePropertyAssociations_with_icef());

        return iprp;
    }

    private Box makeItemPropertiesBox_rgb_component_whole_file_brotli() {
        ItemPropertiesBox iprp = new ItemPropertiesBox();
        ItemPropertyContainerBox ipco = new ItemPropertyContainerBox();
        ipco.addProperty(makeComponentDefinitionBox_rgb_generic());
        ipco.addProperty(makeUncompressedFrameConfigBox_rgb_component());
        ipco.addProperty(makeImageSpatialExtentsProperty());
        ipco.addProperty(makeCompressionConfigurationBoxWholeFileBrotli());
        iprp.setItemProperties(ipco);

        iprp.addItemPropertyAssociation(makePropertyAssociations_whole_file());

        return iprp;
    }

    private ItemPropertyAssociation makePropertyAssociations_whole_file() {
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

            itemPropertyAssociation.addEntry(mainItemAssociations);
        }
        return itemPropertyAssociation;
    }

    private ItemPropertyAssociation makePropertyAssociations_with_icef() {
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
        cmpc.setCompressedRangeType(CompressionRangeType.FULL_ITEM);
        return cmpc;
    }

    private CompressionConfigurationItemProperty makeCompressionConfigurationBoxRowZlib() {
        CompressionConfigurationItemProperty cmpc = new CompressionConfigurationItemProperty();
        cmpc.setCompressionType(new FourCC("zlib"));
        cmpc.setCompressedRangeType(CompressionRangeType.ROW);
        return cmpc;
    }

    private CompressionConfigurationItemProperty makeCompressionConfigurationBoxPixelZlib() {
        CompressionConfigurationItemProperty cmpc = new CompressionConfigurationItemProperty();
        cmpc.setCompressionType(new FourCC("zlib"));
        cmpc.setCompressedRangeType(CompressionRangeType.PIXEL);
        return cmpc;
    }

    private CompressionConfigurationItemProperty makeCompressionConfigurationBoxTileZlib() {
        CompressionConfigurationItemProperty cmpc = new CompressionConfigurationItemProperty();
        cmpc.setCompressionType(new FourCC("zlib"));
        cmpc.setCompressedRangeType(CompressionRangeType.TILE);
        return cmpc;
    }

    private CompressionConfigurationItemProperty makeCompressionConfigurationBoxWholeFileDeflate() {
        CompressionConfigurationItemProperty cmpc = new CompressionConfigurationItemProperty();
        cmpc.setCompressionType(new FourCC("defl"));
        cmpc.setCompressedRangeType(CompressionRangeType.FULL_ITEM);
        return cmpc;
    }

    private CompressionConfigurationItemProperty makeCompressionConfigurationBoxTileDeflate() {
        CompressionConfigurationItemProperty cmpc = new CompressionConfigurationItemProperty();
        cmpc.setCompressionType(new FourCC("defl"));
        cmpc.setCompressedRangeType(CompressionRangeType.TILE);
        return cmpc;
    }

    private CompressionConfigurationItemProperty makeCompressionConfigurationBoxWholeFileBrotli() {
        CompressionConfigurationItemProperty cmpc = new CompressionConfigurationItemProperty();
        cmpc.setCompressionType(new FourCC("brot"));
        cmpc.setCompressedRangeType(CompressionRangeType.FULL_ITEM);
        return cmpc;
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
        uncc.setNumTileRowsMinusOne(0);
        return uncc;
    }

    private UncompressedFrameConfigBox makeUncompressedFrameConfigBox_rgb_component_tiled() {
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
        uncc.setNumTileColumnsMinusOne(NUM_TILES_HORIZ - 1);
        uncc.setNumTileRowsMinusOne(NUM_TILES_VERT - 1);
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

    private MediaDataBox createMediaDataBox_rgb_component_row_zlib(
            GenericallyCompressedUnitsItemInfo icef) throws IOException {
        MediaDataBox mdat = new MediaDataBox();
        byte[] data = createMediaDataBoxZlibContentRows(icef);
        mdat.setData(data);
        return mdat;
    }

    private MediaDataBox createMediaDataBox_rgb_component_zlib_pixel(
            GenericallyCompressedUnitsItemInfo icef) throws IOException {
        MediaDataBox mdat = new MediaDataBox();
        byte[] data = createMediaDataBoxZlibPixelContent(icef);
        mdat.setData(data);
        return mdat;
    }

    private MediaDataBox createMediaDataBox_rgb_component_zlib_tiled(
            GenericallyCompressedUnitsItemInfo icef) throws IOException {
        MediaDataBox mdat = new MediaDataBox();
        byte[] data = createMediaDataBoxZlibTiledContent(icef);
        mdat.setData(data);
        return mdat;
    }

    private MediaDataBox createMediaDataBox_rgb_component_deflate_tiled(
            GenericallyCompressedUnitsItemInfo icef) throws IOException {
        MediaDataBox mdat = new MediaDataBox();
        byte[] data = createMediaDataBoxDeflateTiledContent(icef);
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

        return zlibData(data);
    }

    private byte[] createMediaDataBoxZlibContentRows(GenericallyCompressedUnitsItemInfo icef)
            throws IOException {
        byte[] data = createUncompressedData();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] temp = new byte[1024];
        long dataOffset = 0;
        for (int row = 0; row < IMAGE_HEIGHT; row++) {
            ByteArrayOutputStream rowData = new ByteArrayOutputStream();
            Deflater compressor = new Deflater();
            compressor.setInput(data, row * IMAGE_WIDTH * 3, IMAGE_WIDTH * 3);
            compressor.finish();
            while (!compressor.finished()) {
                int compressedSize = compressor.deflate(temp);
                rowData.write(temp, 0, compressedSize);
            }
            compressor.end();
            outputStream.writeBytes(rowData.toByteArray());
            CompressedUnitInfo range = new CompressedUnitInfo(dataOffset, rowData.size());
            dataOffset += rowData.size();
            icef.addCompressedUnitInfo(range);
        }
        return outputStream.toByteArray();
    }

    private byte[] createMediaDataBoxZlibPixelContent(GenericallyCompressedUnitsItemInfo icef)
            throws IOException {
        byte[] data = createUncompressedData();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] temp = new byte[1024];
        long dataOffset = 0;
        for (int row = 0; row < IMAGE_HEIGHT; row++) {
            for (int col = 0; col < IMAGE_WIDTH; col++) {
                ByteArrayOutputStream pixelData = new ByteArrayOutputStream();
                Deflater compressor = new Deflater();
                compressor.setInput(data, (row * IMAGE_WIDTH + col) * 3, 3);
                compressor.finish();
                while (!compressor.finished()) {
                    int compressedSize = compressor.deflate(temp);
                    pixelData.write(temp, 0, compressedSize);
                }
                compressor.end();
                outputStream.writeBytes(pixelData.toByteArray());
                CompressedUnitInfo range = new CompressedUnitInfo(dataOffset, pixelData.size());
                dataOffset += pixelData.size();
                icef.addCompressedUnitInfo(range);
            }
        }
        return outputStream.toByteArray();
    }

    private byte[] createMediaDataBoxZlibTiledContent(GenericallyCompressedUnitsItemInfo icef)
            throws IOException {
        byte[] data = createUncompressedDataTiled();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] temp = new byte[4096];
        long dataOffset = 0;
        for (int tileIndex = 0; tileIndex < NUM_TILES_HORIZ * NUM_TILES_VERT; tileIndex++) {
            ByteArrayOutputStream tileData = new ByteArrayOutputStream();
            Deflater compressor = new Deflater();
            compressor.setInput(
                    data, tileIndex * TILE_WIDTH * TILE_HEIGHT * 3, TILE_WIDTH * TILE_HEIGHT * 3);
            compressor.finish();
            while (!compressor.finished()) {
                int compressedSize = compressor.deflate(temp);
                tileData.write(temp, 0, compressedSize);
            }
            compressor.end();
            outputStream.writeBytes(tileData.toByteArray());
            CompressedUnitInfo range = new CompressedUnitInfo(dataOffset, tileData.size());
            dataOffset += tileData.size();
            icef.addCompressedUnitInfo(range);
        }
        return outputStream.toByteArray();
    }

    protected byte[] zlibData(byte[] data) {
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

        return deflateData(data);
    }

    protected byte[] deflateData(byte[] data) {
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

    private byte[] createMediaDataBoxDeflateTiledContent(GenericallyCompressedUnitsItemInfo icef)
            throws IOException {
        byte[] data = createUncompressedDataTiled();
        // TODO: we should be able to share this code
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] temp = new byte[4096];
        long dataOffset = 0;
        for (int tileIndex = 0; tileIndex < NUM_TILES_HORIZ * NUM_TILES_VERT; tileIndex++) {
            ByteArrayOutputStream tileData = new ByteArrayOutputStream();
            Deflater compressor = new Deflater(Deflater.BEST_COMPRESSION, true);
            compressor.setInput(
                    data, tileIndex * TILE_WIDTH * TILE_HEIGHT * 3, TILE_WIDTH * TILE_HEIGHT * 3);
            compressor.finish();
            while (!compressor.finished()) {
                int compressedSize = compressor.deflate(temp);
                tileData.write(temp, 0, compressedSize);
            }
            compressor.end();
            outputStream.writeBytes(tileData.toByteArray());
            CompressedUnitInfo range = new CompressedUnitInfo(dataOffset, tileData.size());
            dataOffset += tileData.size();
            icef.addCompressedUnitInfo(range);
        }
        return outputStream.toByteArray();
    }

    private byte[] createMediaDataBoxBrotliContent() throws IOException {
        byte[] data = createUncompressedData();
        Brotli4jLoader.ensureAvailability();
        byte[] brotliCompressed = Encoder.compress(data);
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

    private byte[] createUncompressedDataTiled() throws IOException {
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
        ImageIO.write(image, "PNG", new File("ref_component_tiled.png"));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (int r = 0; r < NUM_TILES_VERT; r++) {
            for (int c = 0; c < NUM_TILES_HORIZ; c++) {
                BufferedImage subimageSource =
                        image.getSubimage(c * TILE_WIDTH, r * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT);
                BufferedImage subimage = deepCopy(subimageSource);
                String filename = String.format("ref_component_tiled_%d_%d.png", r, c);
                ImageIO.write(subimage, "PNG", new File(filename));
                DataBufferByte buffer = (DataBufferByte) subimage.getRaster().getDataBuffer();
                int numBanks = buffer.getNumBanks();
                for (int i = 0; i < numBanks; i++) {
                    baos.writeBytes(buffer.getData(i));
                }
            }
        }
        return baos.toByteArray();
    }

    public static BufferedImage deepCopy(BufferedImage source) {
        SampleModel sampleModel =
                new BandedSampleModel(
                        DataBuffer.TYPE_BYTE, source.getWidth(), source.getHeight(), 3);
        WritableRaster raster = Raster.createWritableRaster(sampleModel, (Point) null);
        BufferedImage b = new BufferedImage(source.getColorModel(), raster, true, null);
        Graphics2D g = b.createGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();
        return b;
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
        Files.write(
                testOut.toPath(),
                baos.toByteArray(),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);
    }
}
