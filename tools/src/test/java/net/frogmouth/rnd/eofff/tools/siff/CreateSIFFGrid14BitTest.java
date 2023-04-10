package net.frogmouth.rnd.eofff.tools.siff;

import static org.testng.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import mil.nga.tiff.FileDirectory;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.AssociationEntry;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemPropertiesBox;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemPropertyAssociation;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemPropertyContainerBox;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.PropertyAssociation;
import net.frogmouth.rnd.eofff.imagefileformat.items.grid.GridItem;
import net.frogmouth.rnd.eofff.imagefileformat.properties.image.CleanAperture;
import net.frogmouth.rnd.eofff.imagefileformat.properties.image.ImageSpatialExtentsProperty;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.free.FreeBox;
import net.frogmouth.rnd.eofff.isobmff.ftyp.FileTypeBox;
import net.frogmouth.rnd.eofff.isobmff.idat.ItemDataBox;
import net.frogmouth.rnd.eofff.isobmff.iinf.ItemInfoBox;
import net.frogmouth.rnd.eofff.isobmff.iloc.ILocExtent;
import net.frogmouth.rnd.eofff.isobmff.iloc.ILocItem;
import net.frogmouth.rnd.eofff.isobmff.iloc.ItemLocationBox;
import net.frogmouth.rnd.eofff.isobmff.infe.ItemInfoEntry;
import net.frogmouth.rnd.eofff.isobmff.iref.ItemReferenceBox;
import net.frogmouth.rnd.eofff.isobmff.iref.SingleItemReferenceBox;
import net.frogmouth.rnd.eofff.isobmff.mdat.MediaDataBox;
import net.frogmouth.rnd.eofff.isobmff.meta.MetaBox;
import net.frogmouth.rnd.eofff.tools.ItemDataBoxBuilder;
import net.frogmouth.rnd.eofff.tools.MediaDataBoxBuilder;
import net.frogmouth.rnd.eofff.uncompressed.cmpd.ComponentDefinition;
import net.frogmouth.rnd.eofff.uncompressed.cmpd.ComponentDefinitionBox;
import net.frogmouth.rnd.eofff.uncompressed.uncc.Component;
import net.frogmouth.rnd.eofff.uncompressed.uncc.ComponentFormat;
import net.frogmouth.rnd.eofff.uncompressed.uncc.Interleaving;
import net.frogmouth.rnd.eofff.uncompressed.uncc.SamplingType;
import net.frogmouth.rnd.eofff.uncompressed.uncc.UncompressedFrameConfigBox;
import org.jmisb.api.common.KlvParseException;
import org.jmisb.api.klv.BerDecoder;
import org.jmisb.api.klv.BerField;
import org.jmisb.api.klv.IKlvKey;
import org.jmisb.api.klv.IKlvValue;
import org.jmisb.api.klv.IMisbMessage;
import org.jmisb.api.klv.INestedKlvValue;
import org.jmisb.api.klv.UniversalLabel;
import org.jmisb.api.klv.st0603.NanoPrecisionTimeStamp;
import org.jmisb.api.klv.st1204.CoreIdentifier;
import org.jmisb.core.klv.ArrayUtils;
import org.jmisb.mimd.st1902.MimdId;
import org.jmisb.mimd.st1902.MimdIdReference;
import org.jmisb.mimd.st1903.MIMD;
import org.jmisb.mimd.st1903.MIMD_SecurityOptions;
import org.jmisb.mimd.st1903.MIMD_Version;
import org.jmisb.mimd.st1903.Security;
import org.jmisb.mimd.st1903.Security_Classification;
import org.jmisb.mimd.st1903.Security_ClassifyingMethod;
import org.jmisb.st1603.localset.CorrectionMethod;
import org.jmisb.st1603.localset.ITimeTransferValue;
import org.jmisb.st1603.localset.ReferenceSource;
import org.jmisb.st1603.localset.ST1603DocumentVersion;
import org.jmisb.st1603.localset.TimeTransferKey;
import org.jmisb.st1603.localset.TimeTransferLocalSet;
import org.jmisb.st1603.localset.TimeTransferMethod;
import org.jmisb.st1603.localset.TimeTransferParameters;
import org.jmisb.st1603.nanopack.NanoTimeTransferPack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

public class CreateSIFFGrid14BitTest extends UncompressedTestSupport {
    private static final Logger LOG = LoggerFactory.getLogger(CreateSIFFGrid14BitTest.class);
    protected static final long ALTERNATE_ITEM_ID = 0x1776;
    private static final long FILE_METADATA_ITEM_ID = 0x1902;
    private static final String MIMD_URI = "urn:nsg:KLV:ul:060E2B34.02050101.0E010504.00000000";
    private static final long PRIMARY_TIMESTAMP_ITEM_ID = 0x1603;
    private static final String NANO_TIMESTAMP_URI =
            "urn:nsg:KLV:ul:060E2B34.02050101.0E010302.09000000";
    private static final long CORE_ID_ITEM_ID = 0x1204;
    private static final String CORE_ID_URI = "urn:nsg:KLV:ul:060E2B34.01010101.0E010405.03000000";

    private static final int TILE_WIDTH = 256;
    private static final int TILE_HEIGHT = 256;
    private static final int NUM_TILE_ROWS = 62;
    private static final int NUM_TILE_COLUMNS = 59;
    private static final int IMAGE_HEIGHT_GRID = TILE_HEIGHT * NUM_TILE_ROWS;
    private static final int IMAGE_WIDTH_GRID = TILE_WIDTH * NUM_TILE_COLUMNS;
    private static final int VALID_PIXELS_WIDTH = 15801;
    private static final int VALID_PIXELS_HEIGHT = 15881;
    private static final int MDAT_START_GRID = 300000; // TODO
    private static final int MDAT_START_GRID_DATA = MDAT_START_GRID + 2 * Integer.BYTES;
    private static final int NUM_BYTES_PER_PIXEL_14_BIT = 2;
    private static final int TILE_SIZE_BYTES =
            TILE_WIDTH * TILE_HEIGHT * NUM_BYTES_PER_PIXEL_14_BIT;

    private static final byte[] MIIS_UUID_BYTES =
            new byte[] {
                (byte) 0xbd,
                (byte) 0x11,
                (byte) 0x19,
                (byte) 0x91,
                (byte) 0x57,
                (byte) 0xba,
                (byte) 0x4a,
                (byte) 0x55,
                (byte) 0xb6,
                (byte) 0x95,
                (byte) 0x98,
                (byte) 0x79,
                (byte) 0xaa,
                (byte) 0xb7,
                (byte) 0x25,
                (byte) 0xb9
            };
    private final UUID miisUuid;

    public CreateSIFFGrid14BitTest() {
        ByteBuffer byteBuffer = ByteBuffer.wrap(MIIS_UUID_BYTES);
        long high = byteBuffer.getLong();
        long low = byteBuffer.getLong();
        miisUuid = new UUID(high, low);
    }

    @Test
    public void writeFile_14bit() throws IOException {
        List<Box> boxes = new ArrayList<>();
        FileTypeBox ftyp = createFileTypeBox();
        boxes.add(ftyp);
        MetaBox meta = createMetaBox_14bit_mono(ByteOrder.BIG_ENDIAN);
        boxes.add(meta);
        long lengthOfPreviousBoxes = ftyp.getSize() + meta.getSize();
        long numberOfFillBytes = MDAT_START_GRID - lengthOfPreviousBoxes - LENGTH_OF_FREEBOX_HEADER;
        FreeBox free = new FreeBox();
        free.setData(new byte[(int) numberOfFillBytes]);
        boxes.add(free);
        MediaDataBox mdat = createMediaDataBox_mono_grid(ByteOrder.BIG_ENDIAN);
        boxes.add(mdat);
        writeBoxes(boxes, "test_siff_14bit.heif");
    }

    @Test
    public void writeFile_14bit_le() throws IOException {
        List<Box> boxes = new ArrayList<>();
        FileTypeBox ftyp = createFileTypeBox();
        boxes.add(ftyp);
        MetaBox meta = createMetaBox_14bit_mono(ByteOrder.LITTLE_ENDIAN);
        boxes.add(meta);
        long lengthOfPreviousBoxes = ftyp.getSize() + meta.getSize();
        long numberOfFillBytes = MDAT_START_GRID - lengthOfPreviousBoxes - LENGTH_OF_FREEBOX_HEADER;
        FreeBox free = new FreeBox();
        free.setData(new byte[(int) numberOfFillBytes]);
        boxes.add(free);
        MediaDataBox mdat = createMediaDataBox_mono_grid(ByteOrder.LITTLE_ENDIAN);
        boxes.add(mdat);
        writeBoxes(boxes, "test_siff_14bit_le.heif");
    }

    private MetaBox createMetaBox_14bit_mono(ByteOrder endian) throws IOException {
        MetaBox meta = new MetaBox();
        List<Box> boxes = new ArrayList<>();
        boxes.add(makeHandlerBox());
        boxes.add(makePrimaryItemBox());
        boxes.add(makeItemInfoBoxGrid());
        boxes.add(makeItemLocationBox_grid());
        boxes.add(makeItemDataBox_grid());
        boxes.add(makeItemPropertiesBox_grid(endian));
        boxes.add(makeItemReferenceBox());
        meta.addNestedBoxes(boxes);
        return meta;
    }

    private ItemInfoBox makeItemInfoBoxGrid() {
        ItemInfoBox iinf = new ItemInfoBox();
        for (int y = 0; y < NUM_TILE_ROWS; y++) {
            for (int x = 0; x < NUM_TILE_COLUMNS; x++) {
                int itemID = getItemIdForGridComponent(x, y);
                ItemInfoEntry infexy = new ItemInfoEntry();
                infexy.setVersion(2);
                infexy.setItemID(itemID);
                FourCC unci = new FourCC("unci");
                infexy.setItemType(unci.asUnsigned());
                infexy.setItemName(String.format("Grid input image [%d, %d]", x, y));
                iinf.addItem(infexy);
            }
        }
        {
            ItemInfoEntry infe0 = new ItemInfoEntry();
            infe0.setVersion(2);
            infe0.setItemID(MAIN_ITEM_ID);
            FourCC grid = new FourCC("grid");
            infe0.setItemType(grid.asUnsigned());
            infe0.setItemName("Grid Image");
            iinf.addItem(infe0);
        }
        {
            ItemInfoEntry infe1 = new ItemInfoEntry();
            infe1.setVersion(2);
            infe1.setItemID(ALTERNATE_ITEM_ID);
            FourCC unci = new FourCC("unci");
            infe1.setItemType(unci.asUnsigned());
            infe1.setItemName("Alternate view");
            iinf.addItem(infe1);
        }
        {
            ItemInfoEntry infe2 = new ItemInfoEntry();
            infe2.setVersion(2);
            infe2.setItemID(FILE_METADATA_ITEM_ID);
            FourCC uri_fourcc = new FourCC("uri ");
            infe2.setItemType(uri_fourcc.asUnsigned());
            infe2.setItemUriType(MIMD_URI);
            infe2.setItemName("File metadata (MIMD)");
            iinf.addItem(infe2);
        }
        {
            ItemInfoEntry infe3 = new ItemInfoEntry();
            infe3.setVersion(2);
            infe3.setItemID(PRIMARY_TIMESTAMP_ITEM_ID);
            FourCC uri_fourcc = new FourCC("uri ");
            infe3.setItemType(uri_fourcc.asUnsigned());
            infe3.setItemUriType(NANO_TIMESTAMP_URI);
            infe3.setItemName("Nano timestamp metadata (ST 1603)");
            iinf.addItem(infe3);
        }
        {
            ItemInfoEntry infe4 = new ItemInfoEntry();
            infe4.setVersion(2);
            infe4.setItemID(CORE_ID_ITEM_ID);
            FourCC uri_fourcc = new FourCC("uri ");
            infe4.setItemType(uri_fourcc.asUnsigned());
            infe4.setItemUriType(CORE_ID_URI);
            infe4.setItemName("Core ID metadata (ST 1204)");
            iinf.addItem(infe4);
        }
        return iinf;
    }

    private ItemLocationBox makeItemLocationBox_grid() throws IOException {
        ItemLocationBox iloc = new ItemLocationBox();
        iloc.setOffsetSize(4);
        iloc.setLengthSize(4);
        iloc.setBaseOffsetSize(4);
        iloc.setIndexSize(4);
        iloc.setVersion(1);
        for (int y = 0; y < NUM_TILE_ROWS; y++) {
            for (int x = 0; x < NUM_TILE_COLUMNS; x++) {
                int itemID = getItemIdForGridComponent(x, y);
                int tileNumber = y * NUM_TILE_COLUMNS + x;
                ILocItem itemxyloc = new ILocItem();
                itemxyloc.setConstructionMethod(0);
                itemxyloc.setItemId(itemID);
                itemxyloc.setBaseOffset(MDAT_START_GRID_DATA);
                ILocExtent itemxyextent = new ILocExtent();
                itemxyextent.setExtentIndex(0);
                itemxyextent.setExtentOffset(tileNumber * TILE_SIZE_BYTES);
                itemxyextent.setExtentLength(TILE_SIZE_BYTES);
                itemxyloc.addExtent(itemxyextent);
                iloc.addItem(itemxyloc);
            }
        }
        {
            ILocItem mimdItemLocation = new ILocItem();
            mimdItemLocation.setConstructionMethod(1);
            mimdItemLocation.setItemId(FILE_METADATA_ITEM_ID);
            ILocExtent mimdExtent = new ILocExtent();
            mimdExtent.setExtentIndex(0);
            mimdExtent.setExtentOffset(0);
            mimdExtent.setExtentLength(getFileMetadataBytes(false).length);
            mimdItemLocation.addExtent(mimdExtent);
            iloc.addItem(mimdItemLocation);
        }
        {
            ILocItem nanoTimestampItemLocation = new ILocItem();
            nanoTimestampItemLocation.setConstructionMethod(1);
            nanoTimestampItemLocation.setItemId(PRIMARY_TIMESTAMP_ITEM_ID);
            ILocExtent nanoTimestampExtent = new ILocExtent();
            nanoTimestampExtent.setExtentIndex(0);
            nanoTimestampExtent.setExtentOffset(getFileMetadataBytes(false).length);
            nanoTimestampExtent.setExtentLength(getNanoTimestampBytes(false).length);
            nanoTimestampItemLocation.addExtent(nanoTimestampExtent);
            iloc.addItem(nanoTimestampItemLocation);
        }
        {
            ILocItem coreIdItemLocation = new ILocItem();
            coreIdItemLocation.setConstructionMethod(1);
            coreIdItemLocation.setItemId(CORE_ID_ITEM_ID);
            ILocExtent coreIdExtent = new ILocExtent();
            coreIdExtent.setExtentIndex(0);
            coreIdExtent.setExtentOffset(
                    getFileMetadataBytes(false).length + getNanoTimestampBytes(false).length);
            coreIdExtent.setExtentLength(this.getMiisCoreIdAsBytes(false).length);
            coreIdItemLocation.addExtent(coreIdExtent);
            iloc.addItem(coreIdItemLocation);
        }
        {
            ILocItem gridItemLocation = new ILocItem();
            gridItemLocation.setConstructionMethod(1);
            gridItemLocation.setItemId(MAIN_ITEM_ID);
            ILocExtent gridItemExtent = new ILocExtent();
            gridItemExtent.setExtentIndex(0);
            gridItemExtent.setExtentOffset(
                    getFileMetadataBytes(false).length
                            + getNanoTimestampBytes(false).length
                            + this.getMiisCoreIdAsBytes(false).length);
            gridItemExtent.setExtentLength(this.getGridItemAsBytes(false).length);
            gridItemLocation.addExtent(gridItemExtent);
            iloc.addItem(gridItemLocation);
        }
        {
            ILocItem alternateItemLocation = new ILocItem();
            alternateItemLocation.setConstructionMethod(0);
            alternateItemLocation.setItemId(ALTERNATE_ITEM_ID);
            alternateItemLocation.setBaseOffset(MDAT_START_GRID_DATA);
            ILocExtent alternateItemExtent = new ILocExtent();
            alternateItemExtent.setExtentIndex(0);
            alternateItemExtent.setExtentOffset(0);
            alternateItemExtent.setExtentLength(TILE_SIZE_BYTES * NUM_TILE_COLUMNS * NUM_TILE_ROWS);
            alternateItemLocation.addExtent(alternateItemExtent);
            iloc.addItem(alternateItemLocation);
        }
        return iloc;
    }

    private ItemDataBox makeItemDataBox_grid() throws IOException {
        ItemDataBoxBuilder itemDataBoxBuilder = new ItemDataBoxBuilder();
        itemDataBoxBuilder.addData(this.getFileMetadataBytes(true));
        itemDataBoxBuilder.addData(this.getNanoTimestampBytes(true));
        itemDataBoxBuilder.addData(this.getMiisCoreIdAsBytes(true));
        itemDataBoxBuilder.addData(this.getGridItemAsBytes(true));
        return itemDataBoxBuilder.build();
    }

    private Box makeItemPropertiesBox_grid(ByteOrder endian) {
        ItemPropertiesBox iprp = new ItemPropertiesBox();
        ItemPropertyContainerBox ipco = new ItemPropertyContainerBox();
        ipco.addProperty(makeComponentDefinitionBox_mono());
        ipco.addProperty(makeUncompressedFrameConfigBox_grid_item(endian));
        ipco.addProperty(makeImageSpatialExtentsProperty_grid_tile());
        ipco.addProperty(makeCleanApertureBox());
        ipco.addProperty(makeImageSpatialExtentsProperty_grid());
        ipco.addProperty(makeUncompressedFrameConfigBox_tiled_item(endian));
        iprp.setItemProperties(ipco);

        {
            ItemPropertyAssociation itemPropertyAssociation = new ItemPropertyAssociation();
            for (int y = 0; y < NUM_TILE_ROWS; y++) {
                for (int x = 0; x < NUM_TILE_COLUMNS; x++) {
                    int itemID = getItemIdForGridComponent(x, y);
                    AssociationEntry gridItemAssociations = new AssociationEntry();
                    gridItemAssociations.setItemId(itemID);

                    PropertyAssociation associationToComponentDefinitionBox =
                            new PropertyAssociation();
                    associationToComponentDefinitionBox.setPropertyIndex(1);
                    associationToComponentDefinitionBox.setEssential(true);
                    gridItemAssociations.addAssociation(associationToComponentDefinitionBox);

                    PropertyAssociation associationToUncompressedFrameConfigBox =
                            new PropertyAssociation();
                    associationToUncompressedFrameConfigBox.setPropertyIndex(2);
                    associationToUncompressedFrameConfigBox.setEssential(true);
                    gridItemAssociations.addAssociation(associationToUncompressedFrameConfigBox);

                    PropertyAssociation associationToImageSpatialExtentsProperty =
                            new PropertyAssociation();
                    associationToImageSpatialExtentsProperty.setPropertyIndex(3);
                    associationToImageSpatialExtentsProperty.setEssential(true);
                    gridItemAssociations.addAssociation(associationToImageSpatialExtentsProperty);
                    itemPropertyAssociation.addEntry(gridItemAssociations);
                }
            }
            iprp.addItemPropertyAssociation(itemPropertyAssociation);
        }
        {
            ItemPropertyAssociation assoc = new ItemPropertyAssociation();
            AssociationEntry entry = new AssociationEntry();
            entry.setItemId(MAIN_ITEM_ID);
            {
                PropertyAssociation associationToClap = new PropertyAssociation();
                associationToClap.setPropertyIndex(4);
                associationToClap.setEssential(true);
                entry.addAssociation(associationToClap);
            }
            {
                PropertyAssociation associationToImageSpatialExtentsProperty =
                        new PropertyAssociation();
                associationToImageSpatialExtentsProperty.setPropertyIndex(5);
                associationToImageSpatialExtentsProperty.setEssential(false);
                entry.addAssociation(associationToImageSpatialExtentsProperty);
            }
            assoc.addEntry(entry);
            iprp.addItemPropertyAssociation(assoc);
        }

        {
            ItemPropertyAssociation assoc = new ItemPropertyAssociation();
            AssociationEntry entry = new AssociationEntry();
            entry.setItemId(ALTERNATE_ITEM_ID);
            {
                PropertyAssociation associationToClap = new PropertyAssociation();
                associationToClap.setPropertyIndex(4);
                associationToClap.setEssential(true);
                entry.addAssociation(associationToClap);
            }
            {
                PropertyAssociation associationToImageSpatialExtentsProperty =
                        new PropertyAssociation();
                associationToImageSpatialExtentsProperty.setPropertyIndex(5);
                associationToImageSpatialExtentsProperty.setEssential(false);
                entry.addAssociation(associationToImageSpatialExtentsProperty);
            }
            {
                PropertyAssociation associationToComponentDefinitionBox = new PropertyAssociation();
                associationToComponentDefinitionBox.setPropertyIndex(1);
                associationToComponentDefinitionBox.setEssential(true);
                entry.addAssociation(associationToComponentDefinitionBox);

                PropertyAssociation associationToUncompressedFrameConfigBox =
                        new PropertyAssociation();
                associationToUncompressedFrameConfigBox.setPropertyIndex(6);
                associationToUncompressedFrameConfigBox.setEssential(true);
                entry.addAssociation(associationToUncompressedFrameConfigBox);
            }
            assoc.addEntry(entry);
            iprp.addItemPropertyAssociation(assoc);
        }

        return iprp;
    }

    private ImageSpatialExtentsProperty makeImageSpatialExtentsProperty_grid_tile() {
        ImageSpatialExtentsProperty ispe = new ImageSpatialExtentsProperty();
        ispe.setImageHeight(TILE_HEIGHT);
        ispe.setImageWidth(TILE_WIDTH);
        return ispe;
    }

    private ImageSpatialExtentsProperty makeImageSpatialExtentsProperty_grid() {
        ImageSpatialExtentsProperty ispe = new ImageSpatialExtentsProperty();
        ispe.setImageHeight(IMAGE_HEIGHT_GRID);
        ispe.setImageWidth(IMAGE_WIDTH_GRID);
        return ispe;
    }

    private CleanAperture makeCleanApertureBox() {
        CleanAperture clap = new CleanAperture();
        clap.setCleanApertureWidthN(VALID_PIXELS_WIDTH);
        clap.setCleanApertureWidthD(1);
        clap.setCleanApertureHeightN(VALID_PIXELS_HEIGHT);
        clap.setCleanApertureHeightD(1);
        clap.setHorizOffN(0);
        clap.setHorizOffD(1);
        clap.setVertOffN(0);
        clap.setVertOffD(1);
        return clap;
    }

    private Box makeItemReferenceBox() {
        ItemReferenceBox iref = new ItemReferenceBox();
        {
            SingleItemReferenceBox primary_item_dimg =
                    new SingleItemReferenceBox(new FourCC("dimg"));
            primary_item_dimg.setFromItemId(MAIN_ITEM_ID);
            for (int y = 0; y < NUM_TILE_ROWS; y++) {
                for (int x = 0; x < NUM_TILE_COLUMNS; x++) {
                    int itemID = getItemIdForGridComponent(x, y);
                    primary_item_dimg.addReference(itemID);
                }
            }
            iref.addItem(primary_item_dimg);
        }
        {
            SingleItemReferenceBox timestamp_cdsc = new SingleItemReferenceBox(new FourCC("cdsc"));
            timestamp_cdsc.setFromItemId(PRIMARY_TIMESTAMP_ITEM_ID);
            timestamp_cdsc.addReference(MAIN_ITEM_ID);
            // timestamp_cdsc.addReference(ALTERNATE_ITEM_ID);
            iref.addItem(timestamp_cdsc);
        }
        {
            SingleItemReferenceBox coreId_cdsc = new SingleItemReferenceBox(new FourCC("cdsc"));
            coreId_cdsc.setFromItemId(CORE_ID_ITEM_ID);
            coreId_cdsc.addReference(MAIN_ITEM_ID);
            // coreId_cdsc.addReference(ALTERNATE_ITEM_ID);
            iref.addItem(coreId_cdsc);
        }
        return iref;
    }

    private ComponentDefinitionBox makeComponentDefinitionBox_mono() {
        ComponentDefinitionBox cmpd = new ComponentDefinitionBox();
        ComponentDefinition monoComponent = new ComponentDefinition(0, null);
        cmpd.addComponentDefinition(monoComponent);
        return cmpd;
    }

    private UncompressedFrameConfigBox makeUncompressedFrameConfigBox_grid_item(ByteOrder endian) {
        UncompressedFrameConfigBox uncc = new UncompressedFrameConfigBox();
        uncc.setProfile(new FourCC("gene"));
        uncc.addComponent(new Component(0, 13, ComponentFormat.UnsignedInteger, 2));
        uncc.setSamplingType(SamplingType.NoSubsampling);
        uncc.setInterleaveType(Interleaving.Component);
        uncc.setBlockSize(0);
        uncc.setComponentLittleEndian(endian == ByteOrder.LITTLE_ENDIAN);
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

    private UncompressedFrameConfigBox makeUncompressedFrameConfigBox_tiled_item(ByteOrder endian) {
        UncompressedFrameConfigBox uncc = new UncompressedFrameConfigBox();
        uncc.setProfile(new FourCC("gene"));
        uncc.addComponent(new Component(0, 13, ComponentFormat.UnsignedInteger, 2));
        uncc.setSamplingType(SamplingType.NoSubsampling);
        uncc.setInterleaveType(Interleaving.Component);
        uncc.setBlockSize(0);
        uncc.setComponentLittleEndian(endian == ByteOrder.LITTLE_ENDIAN);
        uncc.setBlockPadLSB(false);
        uncc.setBlockLittleEndian(false);
        uncc.setBlockReversed(false);
        uncc.setPadUnknown(false);
        uncc.setPixelSize(0);
        uncc.setRowAlignSize(0);
        uncc.setTileAlignSize(0);
        uncc.setNumTileColumnsMinusOne(NUM_TILE_COLUMNS - 1);
        uncc.setNumTileRowsMinusOne(NUM_TILE_ROWS - 1);
        return uncc;
    }

    private MediaDataBox createMediaDataBox_mono_grid(ByteOrder endian) throws IOException {
        MediaDataBoxBuilder mdatBuilder = new MediaDataBoxBuilder();
        mil.nga.tiff.TIFFImage tiffImage =
                mil.nga.tiff.TiffReader.readTiff(
                        new File(
                                "/home/bradh/coding/eofff-refactor/eofff/tools/landsat9/unc_b8.tif"));
        List<mil.nga.tiff.FileDirectory> directories = tiffImage.getFileDirectories();
        FileDirectory directory = directories.get(0);
        // mil.nga.tiff.Rasters rasters = directory.readRasters();
        System.out.println(
                String.format(
                        "Tile width: %d, tile height: %d",
                        directory.getTileWidth(), directory.getTileHeight()));
        // System.out.println(directory.getTileByteCounts());
        int numTilesX =
                (directory.getImageWidth().intValue() + directory.getTileWidth().intValue() - 1)
                        / directory.getTileWidth().intValue();
        int numTilesY =
                (directory.getImageHeight().intValue() + directory.getTileHeight().intValue() - 1)
                        / directory.getTileHeight().intValue();

        for (int y = 0; y < numTilesY; y++) {
            for (int x = 0; x < numTilesX; x++) {
                byte[] tileBytes = directory.getTileOrStrip(x, y, 0);
                for (int i = 0; i < tileBytes.length; i += Short.BYTES) {
                    int tileValue = (tileBytes[i] & 0xFF) << 8;
                    tileValue |= ((tileBytes[i + 1] & 0xFF));
                    if (tileValue < 0) {
                        System.out.println("unexpected negative value: " + tileValue);
                    }
                    // We need padding at the top (MSB)
                    int shiftedValue = tileValue >> 2;
                    byte[] shiftedBytes;
                    if (endian == ByteOrder.BIG_ENDIAN) {
                        shiftedBytes =
                                new byte[] {
                                    (byte) ((shiftedValue >> 8) | 0xFF),
                                    (byte) (shiftedValue & 0xFF)
                                };
                    } else {
                        shiftedBytes =
                                new byte[] {
                                    (byte) (shiftedValue & 0xFF),
                                    (byte) ((shiftedValue >> 8) | 0xFF)
                                };
                    }
                    mdatBuilder.addData(shiftedBytes);
                }
            }
        }

        return mdatBuilder.build();
    }

    private byte[] getFileMetadataBytes(boolean dump) throws IOException {
        try {
            MIMD mimd = new MIMD();
            mimd.setVersion(new MIMD_Version(1));
            mimd.setSecurityOptions(buildSecurityOptions());
            mimd.setCompositeProductSecurity(
                    new MimdIdReference(
                            SECURITY_ID_SERIAL,
                            SECURITY_ID_GROUP,
                            "CompositeProductSecurity",
                            "Security"));
            if (dump) {
                dumpMisbMessage(mimd);
            }
            byte[] mimdBytes = mimd.frameMessage(false);
            BerField ber = BerDecoder.decode(mimdBytes, UniversalLabel.LENGTH, false);
            int lengthOfLength = ber.getLength();
            byte[] mimdValueBytes = new byte[ber.getValue()];
            System.arraycopy(
                    mimdBytes,
                    UniversalLabel.LENGTH + lengthOfLength,
                    mimdValueBytes,
                    0,
                    mimdValueBytes.length);
            return mimdValueBytes;
        } catch (KlvParseException ex) {
            throw new IOException(ex.toString());
        }
    }

    private byte[] getNanoTimestampBytes(boolean dump) throws IOException {
        long nanos = System.currentTimeMillis() * 1000 * 1000;
        NanoPrecisionTimeStamp timestamp = new NanoPrecisionTimeStamp(nanos);
        Map<TimeTransferKey, ITimeTransferValue> localSetValues = new HashMap<>();
        localSetValues.put(TimeTransferKey.DocumentVersion, new ST1603DocumentVersion(2));
        localSetValues.put(
                TimeTransferKey.TimeTransferParameters,
                new TimeTransferParameters(
                        ReferenceSource.Unknown,
                        CorrectionMethod.Unknown,
                        TimeTransferMethod.Unknown));
        TimeTransferLocalSet localSet = new TimeTransferLocalSet(localSetValues);
        NanoTimeTransferPack nanoPack = new NanoTimeTransferPack(timestamp, localSet);
        if (dump) {
            dumpMisbMessage(nanoPack);
        }
        return nanoPack.frameMessage(false);
    }

    private byte[] getMiisCoreIdAsBytes(boolean dump) {
        CoreIdentifier miisCoreId = new CoreIdentifier();
        miisCoreId.setMinorUUID(miisUuid);
        miisCoreId.setVersion(1);
        if (dump) {
            System.out.println("CoreID: " + miisCoreId.getTextRepresentation());
        }
        byte[] miisCoreIdAsBytes = miisCoreId.getRawBytesRepresentation();
        return miisCoreIdAsBytes;
    }

    private static final int SECURITY_ID_GROUP = 1;
    private static final int SECURITY_ID_SERIAL = 2;

    private static MIMD_SecurityOptions buildSecurityOptions() throws KlvParseException {
        Security security = new Security();
        MimdId securityId = new MimdId(SECURITY_ID_SERIAL, SECURITY_ID_GROUP);
        security.setMimdId(securityId);
        security.setClassification(new Security_Classification("UNCLASSIFIED//"));
        security.setClassifyingMethod(new Security_ClassifyingMethod("US-1"));
        List<Security> securities = new ArrayList<>();
        securities.add(security);
        MIMD_SecurityOptions securityOptions = new MIMD_SecurityOptions(securities);
        return securityOptions;
    }

    private static void dumpMisbMessage(IMisbMessage mimd) {
        outputTopLevelMessageHeader(mimd);
        outputNestedKlvValue(mimd, 1);
    }

    private static void outputTopLevelMessageHeader(IMisbMessage misbMessage) {
        String displayHeader = misbMessage.displayHeader();
        if (displayHeader.equalsIgnoreCase("Unknown")) {
            System.out.println(
                    displayHeader
                            + " ["
                            + ArrayUtils.toHexString(misbMessage.getUniversalLabel().getBytes())
                                    .trim()
                            + "]");
            outputUnknownMessageContent(misbMessage.frameMessage(true));
        } else {
            System.out.println(displayHeader);
        }
    }

    private static void outputUnknownMessageContent(byte[] frameMessage) {
        System.out.println(ArrayUtils.toHexString(frameMessage, 16, true));
    }

    private static void outputNestedKlvValue(INestedKlvValue nestedKlvValue, int indentationLevel) {
        for (IKlvKey identifier : nestedKlvValue.getIdentifiers()) {
            IKlvValue value = nestedKlvValue.getField(identifier);
            outputValueWithIndentation(value, indentationLevel);
            // if this has nested content, output that at the next indentation level
            if (value instanceof INestedKlvValue) {
                outputNestedKlvValue((INestedKlvValue) value, indentationLevel + 1);
            }
        }
    }

    private static void outputValueWithIndentation(IKlvValue value, int indentationLevel) {
        for (int i = 0; i < indentationLevel; ++i) {
            System.out.print("\t");
        }
        System.out.println(value.getDisplayName() + ": " + value.getDisplayableValue());
    }

    private int getItemIdForGridComponent(int x, int y) {
        return y * NUM_TILE_COLUMNS + x + 1;
    }

    private byte[] getGridItemAsBytes(boolean dump) throws IOException {
        GridItem gridItem = new GridItem();
        gridItem.setRows(NUM_TILE_ROWS);
        gridItem.setColumns(NUM_TILE_COLUMNS);
        gridItem.setOutput_width(NUM_TILE_COLUMNS * TILE_WIDTH);
        gridItem.setOutput_height(NUM_TILE_ROWS * TILE_HEIGHT);
        // TODO: dump if needed
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        gridItem.writeTo(streamWriter);
        return baos.toByteArray();
    }
}
