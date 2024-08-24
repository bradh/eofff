package net.frogmouth.rnd.eofff.uncompressed_experiments;

import static org.testng.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import mil.nga.tiff.FileDirectory;
import net.frogmouth.rnd.eofff.imagefileformat.itemreferences.ContentDescribes;
import net.frogmouth.rnd.eofff.imagefileformat.itemreferences.DerivedImage;
import net.frogmouth.rnd.eofff.imagefileformat.items.grid.GridItem;
import net.frogmouth.rnd.eofff.imagefileformat.properties.image.ImageSpatialExtentsProperty;
import net.frogmouth.rnd.eofff.imagefileformat.properties.udes.UserDescriptionProperty;
import net.frogmouth.rnd.eofff.imagefileformat.properties.uuid.UUIDProperty;
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
import net.frogmouth.rnd.eofff.isobmff.iprp.AbstractItemProperty;
import net.frogmouth.rnd.eofff.isobmff.iprp.AssociationEntry;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemPropertiesBox;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemPropertyAssociation;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemPropertyContainerBox;
import net.frogmouth.rnd.eofff.isobmff.iprp.PropertyAssociation;
import net.frogmouth.rnd.eofff.isobmff.iref.ItemReferenceBox;
import net.frogmouth.rnd.eofff.isobmff.mdat.MediaDataBox;
import net.frogmouth.rnd.eofff.isobmff.meta.MetaBox;
import net.frogmouth.rnd.eofff.isobmff.pitm.PrimaryItemBox;
import net.frogmouth.rnd.eofff.uncompressed.cmpd.ComponentDefinition;
import net.frogmouth.rnd.eofff.uncompressed.cmpd.ComponentDefinitionBox;
import net.frogmouth.rnd.eofff.uncompressed.itai.TAITimeStampBox;
import net.frogmouth.rnd.eofff.uncompressed.itai.TAITimeStampPacket;
import net.frogmouth.rnd.eofff.uncompressed.taic.TAIClockInfoItemProperty;
import net.frogmouth.rnd.eofff.uncompressed.uncc.Component;
import net.frogmouth.rnd.eofff.uncompressed.uncc.ComponentFormat;
import net.frogmouth.rnd.eofff.uncompressed.uncc.Interleaving;
import net.frogmouth.rnd.eofff.uncompressed.uncc.SamplingType;
import net.frogmouth.rnd.eofff.uncompressed.uncc.UncompressedFrameConfigBox;
import net.frogmouth.rnd.eofff.uncompressed_experiments.geo.CoordinateReferenceSystemProperty;
import net.frogmouth.rnd.eofff.uncompressed_experiments.geo.ModelTransformationProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import org.threeten.extra.scale.TaiInstant;
import org.threeten.extra.scale.UtcInstant;

public class CreateGIMIGridTest extends GIMIValidator {
    private static final Logger LOG = LoggerFactory.getLogger(CreateGIMIGridTest.class);

    private static final long MAIN_ITEM_ID = 1000;
    private static final long ALTERNATE_ITEM_ID = 1100;
    private static final long FAKE_SECURITY_ITEM_ID = 1200;

    private static final int LENGTH_OF_FREEBOX_HEADER = 8;
    private static final int NUM_BYTES_PER_PIXEL_RGB = 3;

    private final int tileWidth;
    private final int tileHeight;
    private static final int NUM_TILE_ROWS = 18;
    private static final int NUM_TILE_COLUMNS = 19;
    private final int imageHeight;
    private final int imageWidth;
    private static final int MDAT_START_GRID = 27000 + 1415 + 154;
    private static final int MDAT_START_GRID_DATA = MDAT_START_GRID + 2 * Integer.BYTES;
    private final int tileSizeBytes;

    private static final UUID GRID_ITEM_CONTENT_ID_UUID = UUID.randomUUID();

    private static final UUID FAKE_SECURITY_CONTENT_ID_UUID = UUID.randomUUID();
    private final String FAKE_SECURITY_MIME_TYPE = "application/x.fake-dni-arh+xml";
    private final String FAKE_SECURITY_XML =
            """
            <?xml version="1.0" encoding="UTF-8"?>
            <FakeSecurity xmlns="http://www.opengis.net/CodeSprint2023Oct/Security">
                <FakeLevel>UNCLASSFIIED</FakeLevel>
            </FakeSecurity>""";

    private final FileDirectory directory;
    private static final UUID CONTENT_ID_UUID =
            UUID.fromString("aac8ab7d-f519-5437-b7d3-c973d155e253");

    private final LocalDateTime TIMESTAMP =
            LocalDateTime.of(LocalDate.of(2017, 5, 7), LocalTime.MIN);
    List<Double> pixelScale;
    List<Double> modelTiePoint;

    private final String path;

    public CreateGIMIGridTest() throws IOException {
        path = "/home/bradh/gdal_hacks/ACT2017-cog.tif";
        mil.nga.tiff.TIFFImage tiffImage = mil.nga.tiff.TiffReader.readTiff(new File(path));
        List<mil.nga.tiff.FileDirectory> directories = tiffImage.getFileDirectories();
        directory = directories.get(0);
        tileWidth = (int) directory.getTileWidth();
        tileHeight = (int) directory.getTileHeight();
        imageHeight = (int) directory.getImageHeight();
        imageWidth = (int) directory.getImageWidth();
        tileSizeBytes = tileWidth * tileHeight * NUM_BYTES_PER_PIXEL_RGB;
        pixelScale = directory.getModelPixelScale();
        modelTiePoint = directory.getModelTiepoint();
    }

    @Test
    public void writeFile_rgb_grid_no_pymd() throws IOException {
        List<Box> boxes = new ArrayList<>();
        FileTypeBox ftyp = createFileTypeBox();
        boxes.add(ftyp);
        MetaBox meta = createMetaBox_rgb_grid();
        boxes.add(meta);
        long lengthOfPreviousBoxes = ftyp.getSize() + meta.getSize();
        long numberOfFillBytes = MDAT_START_GRID - lengthOfPreviousBoxes - LENGTH_OF_FREEBOX_HEADER;
        FreeBox free = new FreeBox();
        free.setData(new byte[(int) numberOfFillBytes]);
        boxes.add(free);
        MediaDataBox mdat = createMediaDataBox_rgb_grid();
        boxes.add(mdat);
        writeBoxes(boxes, "ACT2017_gimi_rgb_grid_no_pymd.heif");
    }

    @Test
    public void writeFile_rgb_tile_no_pymd() throws IOException {
        List<Box> boxes = new ArrayList<>();
        FileTypeBox ftyp = createFileTypeBox();
        boxes.add(ftyp);
        MetaBox meta = createMetaBox_rgb_tile();
        boxes.add(meta);
        long lengthOfPreviousBoxes = ftyp.getSize() + meta.getSize();
        long numberOfFillBytes = MDAT_START_GRID - lengthOfPreviousBoxes - LENGTH_OF_FREEBOX_HEADER;
        FreeBox free = new FreeBox();
        free.setData(new byte[(int) numberOfFillBytes]);
        boxes.add(free);
        MediaDataBox mdat = createMediaDataBox_rgb_grid();
        boxes.add(mdat);
        writeBoxes(boxes, "ACT2017_gimi_rgb_tile_no_pymd.heif");
    }

    @Test
    public void writeFile_rgb_grid_or_tile_no_pymd() throws IOException {
        List<Box> boxes = new ArrayList<>();
        FileTypeBox ftyp = createFileTypeBox();
        boxes.add(ftyp);
        MetaBox meta = createMetaBox_rgb_grid_or_tile();
        boxes.add(meta);
        long lengthOfPreviousBoxes = ftyp.getSize() + meta.getSize();
        long numberOfFillBytes = MDAT_START_GRID - lengthOfPreviousBoxes - LENGTH_OF_FREEBOX_HEADER;
        FreeBox free = new FreeBox();
        free.setData(new byte[(int) numberOfFillBytes]);
        boxes.add(free);
        MediaDataBox mdat = createMediaDataBox_rgb_grid();
        boxes.add(mdat);
        writeBoxes(boxes, "ACT2017_gimi_rgb_grid_or_tile_no_pymd.heif");
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
        Files.write(
                testOut.toPath(),
                baos.toByteArray(),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);
    }

    private FileTypeBox createFileTypeBox() {
        FileTypeBox fileTypeBox = new FileTypeBox();
        fileTypeBox.setMajorBrand(new Brand("mif1"));
        fileTypeBox.setMinorVersion(0);
        fileTypeBox.addCompatibleBrand(new Brand("mif1"));
        fileTypeBox.addCompatibleBrand(new Brand("mif2"));
        fileTypeBox.addCompatibleBrand(new Brand("geo1"));
        fileTypeBox.addCompatibleBrand(new Brand("unif"));
        return fileTypeBox;
    }

    private HandlerBox makeHandlerBox() {
        HandlerBox hdlr = new HandlerBox();
        hdlr.setHandlerType("pict");
        hdlr.setName("");
        return hdlr;
    }

    private PrimaryItemBox makePrimaryItemBox(long itemID) {
        PrimaryItemBox pitm = new PrimaryItemBox();
        pitm.setItemID(itemID);
        return pitm;
    }

    private MetaBox createMetaBox_rgb_grid() throws IOException {
        MetaBox meta = new MetaBox();
        List<Box> boxes = new ArrayList<>();
        boxes.add(makeHandlerBox());
        boxes.add(makePrimaryItemBox(MAIN_ITEM_ID));
        boxes.add(makeItemInfoBoxGrid());
        boxes.add(makeItemLocationBox_grid());
        boxes.add(makeItemDataBox_grid());
        boxes.add(makeItemPropertiesBox_grid());
        boxes.add(makeItemReferenceBoxGrid());
        meta.addNestedBoxes(boxes);
        return meta;
    }

    private MetaBox createMetaBox_rgb_tile() throws IOException {
        MetaBox meta = new MetaBox();
        List<Box> boxes = new ArrayList<>();
        boxes.add(makeHandlerBox());
        boxes.add(makePrimaryItemBox(ALTERNATE_ITEM_ID));
        boxes.add(makeItemInfoBoxTile());
        boxes.add(makeItemLocationBox_tile());
        boxes.add(makeItemDataBox_tile());
        boxes.add(makeItemPropertiesBox_tile());
        boxes.add(makeItemReferenceBoxTile());
        meta.addNestedBoxes(boxes);
        return meta;
    }

    private MetaBox createMetaBox_rgb_grid_or_tile() throws IOException {
        MetaBox meta = new MetaBox();
        List<Box> boxes = new ArrayList<>();
        boxes.add(makeHandlerBox());
        boxes.add(makePrimaryItemBox(MAIN_ITEM_ID));
        boxes.add(makeItemInfoBoxGridOrTile());
        boxes.add(makeItemLocationBox_grid_or_tile());
        boxes.add(makeItemDataBox_grid());
        boxes.add(makeItemPropertiesBox_grid_or_tile());
        boxes.add(makeItemReferenceBoxGridOrTile());
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
                infexy.setFlags(1); // Hidden
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
            ItemInfoEntry infe2 = new ItemInfoEntry();
            infe2.setVersion(2);
            infe2.setItemID(FAKE_SECURITY_ITEM_ID);
            FourCC mime_fourcc = new FourCC("mime");
            infe2.setItemType(mime_fourcc.asUnsigned());
            infe2.setContentType(FAKE_SECURITY_MIME_TYPE);
            infe2.setItemName("Security Marking (Fake XML)");
            iinf.addItem(infe2);
        }
        return iinf;
    }

    private ItemInfoBox makeItemInfoBoxTile() {
        ItemInfoBox iinf = new ItemInfoBox();
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
            infe2.setItemID(FAKE_SECURITY_ITEM_ID);
            FourCC mime_fourcc = new FourCC("mime");
            infe2.setItemType(mime_fourcc.asUnsigned());
            infe2.setContentType(FAKE_SECURITY_MIME_TYPE);
            infe2.setItemName("Security Marking (Fake XML)");
            iinf.addItem(infe2);
        }
        return iinf;
    }

    private ItemInfoBox makeItemInfoBoxGridOrTile() {
        ItemInfoBox iinf = new ItemInfoBox();
        for (int y = 0; y < NUM_TILE_ROWS; y++) {
            for (int x = 0; x < NUM_TILE_COLUMNS; x++) {
                int itemID = getItemIdForGridComponent(x, y);
                ItemInfoEntry infexy = new ItemInfoEntry();
                infexy.setVersion(2);
                infexy.setFlags(1); // Hidden
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
            infe0.setItemName("Grid view");
            iinf.addItem(infe0);
        }
        {
            ItemInfoEntry infe1 = new ItemInfoEntry();
            infe1.setVersion(2);
            infe1.setItemID(ALTERNATE_ITEM_ID);
            FourCC unci = new FourCC("unci");
            infe1.setItemType(unci.asUnsigned());
            infe1.setItemName("Tile view");
            iinf.addItem(infe1);
        }
        {
            ItemInfoEntry infe2 = new ItemInfoEntry();
            infe2.setVersion(2);
            infe2.setItemID(FAKE_SECURITY_ITEM_ID);
            FourCC mime_fourcc = new FourCC("mime");
            infe2.setItemType(mime_fourcc.asUnsigned());
            infe2.setContentType(FAKE_SECURITY_MIME_TYPE);
            infe2.setItemName("Security Marking (Fake XML)");
            iinf.addItem(infe2);
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
                itemxyextent.setExtentOffset(tileNumber * tileSizeBytes);
                itemxyextent.setExtentLength(tileSizeBytes);
                itemxyloc.addExtent(itemxyextent);
                iloc.addItem(itemxyloc);
            }
        }
        int offset = 0;
        {
            ILocItem gridItemLocation = new ILocItem();
            gridItemLocation.setConstructionMethod(1);
            gridItemLocation.setItemId(MAIN_ITEM_ID);
            ILocExtent gridItemExtent = new ILocExtent();
            gridItemExtent.setExtentIndex(0);
            gridItemExtent.setExtentOffset(0);
            gridItemExtent.setExtentLength(this.getGridItemAsBytes(false).length);
            offset += gridItemExtent.getExtentLength();
            gridItemLocation.addExtent(gridItemExtent);
            iloc.addItem(gridItemLocation);
        }
        {
            ILocItem fakeSecurityItemLocation = new ILocItem();
            fakeSecurityItemLocation.setConstructionMethod(1);
            fakeSecurityItemLocation.setItemId(FAKE_SECURITY_ITEM_ID);
            fakeSecurityItemLocation.setBaseOffset(offset);
            ILocExtent securityExtent = new ILocExtent();
            securityExtent.setExtentIndex(0);
            securityExtent.setExtentOffset(0);
            securityExtent.setExtentLength(getFakeSecurityXMLBytes(false).length);
            offset += securityExtent.getExtentLength();
            fakeSecurityItemLocation.addExtent(securityExtent);
            iloc.addItem(fakeSecurityItemLocation);
        }
        return iloc;
    }

    private ItemLocationBox makeItemLocationBox_tile() throws IOException {
        ItemLocationBox iloc = new ItemLocationBox();
        iloc.setOffsetSize(4);
        iloc.setLengthSize(4);
        iloc.setBaseOffsetSize(4);
        iloc.setIndexSize(4);
        iloc.setVersion(1);
        {
            ILocItem alternateItemLocation = new ILocItem();
            alternateItemLocation.setConstructionMethod(0);
            alternateItemLocation.setItemId(ALTERNATE_ITEM_ID);
            alternateItemLocation.setBaseOffset(MDAT_START_GRID_DATA);
            ILocExtent alternateItemExtent = new ILocExtent();
            alternateItemExtent.setExtentIndex(0);
            alternateItemExtent.setExtentOffset(0);
            alternateItemExtent.setExtentLength(tileSizeBytes * NUM_TILE_COLUMNS * NUM_TILE_ROWS);
            alternateItemLocation.addExtent(alternateItemExtent);
            iloc.addItem(alternateItemLocation);
        }
        int offset = 0;
        {
            ILocItem fakeSecurityItemLocation = new ILocItem();
            fakeSecurityItemLocation.setConstructionMethod(1);
            fakeSecurityItemLocation.setItemId(FAKE_SECURITY_ITEM_ID);
            fakeSecurityItemLocation.setBaseOffset(offset);
            ILocExtent securityExtent = new ILocExtent();
            securityExtent.setExtentIndex(0);
            securityExtent.setExtentOffset(0);
            securityExtent.setExtentLength(getFakeSecurityXMLBytes(false).length);
            offset += securityExtent.getExtentLength();
            fakeSecurityItemLocation.addExtent(securityExtent);
            iloc.addItem(fakeSecurityItemLocation);
        }
        return iloc;
    }

    private ItemLocationBox makeItemLocationBox_grid_or_tile() throws IOException {
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
                itemxyextent.setExtentOffset(tileNumber * tileSizeBytes);
                itemxyextent.setExtentLength(tileSizeBytes);
                itemxyloc.addExtent(itemxyextent);
                iloc.addItem(itemxyloc);
            }
        }
        {
            ILocItem alternateItemLocation = new ILocItem();
            alternateItemLocation.setConstructionMethod(0);
            alternateItemLocation.setItemId(ALTERNATE_ITEM_ID);
            alternateItemLocation.setBaseOffset(MDAT_START_GRID_DATA);
            ILocExtent alternateItemExtent = new ILocExtent();
            alternateItemExtent.setExtentIndex(0);
            alternateItemExtent.setExtentOffset(0);
            alternateItemExtent.setExtentLength(tileSizeBytes * NUM_TILE_COLUMNS * NUM_TILE_ROWS);
            alternateItemLocation.addExtent(alternateItemExtent);
            iloc.addItem(alternateItemLocation);
        }
        int offset = 0;
        {
            ILocItem gridItemLocation = new ILocItem();
            gridItemLocation.setConstructionMethod(1);
            gridItemLocation.setItemId(MAIN_ITEM_ID);
            ILocExtent gridItemExtent = new ILocExtent();
            gridItemExtent.setExtentIndex(0);
            gridItemExtent.setExtentOffset(0);
            gridItemExtent.setExtentLength(this.getGridItemAsBytes(false).length);
            offset += gridItemExtent.getExtentLength();
            gridItemLocation.addExtent(gridItemExtent);
            iloc.addItem(gridItemLocation);
        }
        {
            ILocItem fakeSecurityItemLocation = new ILocItem();
            fakeSecurityItemLocation.setConstructionMethod(1);
            fakeSecurityItemLocation.setItemId(FAKE_SECURITY_ITEM_ID);
            fakeSecurityItemLocation.setBaseOffset(offset);
            ILocExtent securityExtent = new ILocExtent();
            securityExtent.setExtentIndex(0);
            securityExtent.setExtentOffset(0);
            securityExtent.setExtentLength(getFakeSecurityXMLBytes(false).length);
            offset += securityExtent.getExtentLength();
            fakeSecurityItemLocation.addExtent(securityExtent);
            iloc.addItem(fakeSecurityItemLocation);
        }
        return iloc;
    }

    private byte[] getFakeSecurityXMLBytes(boolean dump) {
        byte[] securityXMLBytes = FAKE_SECURITY_XML.getBytes(StandardCharsets.UTF_8);
        return securityXMLBytes;
    }

    private ItemDataBox makeItemDataBox_grid() throws IOException {
        ItemDataBoxBuilder itemDataBoxBuilder = new ItemDataBoxBuilder();
        itemDataBoxBuilder.addData(this.getGridItemAsBytes(true));
        itemDataBoxBuilder.addData(this.getFakeSecurityXMLBytes(true));
        return itemDataBoxBuilder.build();
    }

    private ItemDataBox makeItemDataBox_tile() throws IOException {
        ItemDataBoxBuilder itemDataBoxBuilder = new ItemDataBoxBuilder();
        itemDataBoxBuilder.addData(this.getFakeSecurityXMLBytes(true));
        return itemDataBoxBuilder.build();
    }

    private Box makeItemPropertiesBox_grid() {
        ItemPropertiesBox iprp = new ItemPropertiesBox();
        ItemPropertyContainerBox ipco = new ItemPropertyContainerBox();
        ipco.addProperty(makeComponentDefinitionBox_rgb3()); // 1
        ipco.addProperty(makeUncompressedFrameConfigBox_grid_item()); // 2
        ipco.addProperty(makeImageSpatialExtentsProperty_grid_tile()); // 3
        ipco.addProperty(makeImageSpatialExtentsProperty_grid()); // 4
        ipco.addProperty(makeContentIdPropertyGridItem()); // 5
        ipco.addProperty(makeContentIdPropertyFakeSecurity()); // 6
        ipco.addProperty(makeUserDescription_copyright()); // 7
        ipco.addProperty(makeClockInfoItemProperty()); // 8
        ipco.addProperty(makeTimeStampBox()); // 9
        ipco.addProperty(makeModelTransformationProperty()); // 10
        ipco.addProperty(makeWKT2Property()); // 11
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
                PropertyAssociation associationToImageSpatialExtentsProperty =
                        new PropertyAssociation();
                associationToImageSpatialExtentsProperty.setPropertyIndex(4);
                associationToImageSpatialExtentsProperty.setEssential(false);
                entry.addAssociation(associationToImageSpatialExtentsProperty);
            }
            {
                PropertyAssociation associationToContentId = new PropertyAssociation();
                associationToContentId.setPropertyIndex(5);
                associationToContentId.setEssential(false);
                entry.addAssociation(associationToContentId);
            }
            {
                PropertyAssociation associationToCopyrightUserDescription =
                        new PropertyAssociation();
                associationToCopyrightUserDescription.setPropertyIndex(7);
                associationToCopyrightUserDescription.setEssential(false);
                entry.addAssociation(associationToCopyrightUserDescription);
            }
            {
                PropertyAssociation associationToClockInfo = new PropertyAssociation();
                associationToClockInfo.setPropertyIndex(8);
                associationToClockInfo.setEssential(false);
                entry.addAssociation(associationToClockInfo);
            }
            {
                PropertyAssociation assocationToTimestampTAI = new PropertyAssociation();
                assocationToTimestampTAI.setPropertyIndex(9);
                assocationToTimestampTAI.setEssential(false);
                entry.addAssociation(assocationToTimestampTAI);
            }
            {
                PropertyAssociation associationToTransformMatrix = new PropertyAssociation();
                associationToTransformMatrix.setPropertyIndex(10);
                associationToTransformMatrix.setEssential(false);
                entry.addAssociation(associationToTransformMatrix);
            }
            {
                PropertyAssociation associationToWkt2 = new PropertyAssociation();
                associationToWkt2.setPropertyIndex(11);
                associationToWkt2.setEssential(false);
                entry.addAssociation(associationToWkt2);
            }
            assoc.addEntry(entry);
            iprp.addItemPropertyAssociation(assoc);
        }

        {
            ItemPropertyAssociation assoc = new ItemPropertyAssociation();
            AssociationEntry entry = new AssociationEntry();
            entry.setItemId(FAKE_SECURITY_ITEM_ID);
            {
                PropertyAssociation associationToContentId = new PropertyAssociation();
                associationToContentId.setPropertyIndex(6);
                associationToContentId.setEssential(false);
                entry.addAssociation(associationToContentId);
            }
            assoc.addEntry(entry);
            iprp.addItemPropertyAssociation(assoc);
        }
        return iprp;
    }

    private Box makeItemPropertiesBox_tile() {
        ItemPropertiesBox iprp = new ItemPropertiesBox();
        ItemPropertyContainerBox ipco = new ItemPropertyContainerBox();
        ipco.addProperty(makeComponentDefinitionBox_rgb3()); // 1
        ipco.addProperty(makeImageSpatialExtentsProperty_grid()); // 2
        ipco.addProperty(makeUncompressedFrameConfigBox_tiled_item()); // 3
        ipco.addProperty(makeContentIdPropertyGridItem()); // 4
        ipco.addProperty(makeContentIdPropertyFakeSecurity()); // 5
        ipco.addProperty(makeUserDescription_copyright()); // 6
        ipco.addProperty(makeClockInfoItemProperty()); // 7
        ipco.addProperty(makeTimeStampBox()); // 8
        ipco.addProperty(makeModelTransformationProperty()); // 9
        ipco.addProperty(makeWKT2Property()); // 10
        iprp.setItemProperties(ipco);

        {
            ItemPropertyAssociation assoc = new ItemPropertyAssociation();
            AssociationEntry entry = new AssociationEntry();
            entry.setItemId(ALTERNATE_ITEM_ID);
            {
                PropertyAssociation associationToImageSpatialExtentsProperty =
                        new PropertyAssociation();
                associationToImageSpatialExtentsProperty.setPropertyIndex(2);
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
                associationToUncompressedFrameConfigBox.setPropertyIndex(3);
                associationToUncompressedFrameConfigBox.setEssential(true);
                entry.addAssociation(associationToUncompressedFrameConfigBox);
            }
            {
                PropertyAssociation associationToContentId = new PropertyAssociation();
                associationToContentId.setPropertyIndex(4);
                associationToContentId.setEssential(false);
                entry.addAssociation(associationToContentId);
            }
            {
                PropertyAssociation associationToCopyrightUserDescription =
                        new PropertyAssociation();
                associationToCopyrightUserDescription.setPropertyIndex(6);
                associationToCopyrightUserDescription.setEssential(false);
                entry.addAssociation(associationToCopyrightUserDescription);
            }
            {
                PropertyAssociation associationToClockInfo = new PropertyAssociation();
                associationToClockInfo.setPropertyIndex(7);
                associationToClockInfo.setEssential(false);
                entry.addAssociation(associationToClockInfo);
            }
            {
                PropertyAssociation associationToTimestampTAI = new PropertyAssociation();
                associationToTimestampTAI.setPropertyIndex(8);
                associationToTimestampTAI.setEssential(false);
                entry.addAssociation(associationToTimestampTAI);
            }
            {
                PropertyAssociation associationToTransformMatrix = new PropertyAssociation();
                associationToTransformMatrix.setPropertyIndex(9);
                associationToTransformMatrix.setEssential(false);
                entry.addAssociation(associationToTransformMatrix);
            }
            {
                PropertyAssociation associationToWkt2 = new PropertyAssociation();
                associationToWkt2.setPropertyIndex(10);
                associationToWkt2.setEssential(false);
                entry.addAssociation(associationToWkt2);
            }
            assoc.addEntry(entry);
            iprp.addItemPropertyAssociation(assoc);
        }

        {
            ItemPropertyAssociation assoc = new ItemPropertyAssociation();
            AssociationEntry entry = new AssociationEntry();
            entry.setItemId(FAKE_SECURITY_ITEM_ID);
            {
                PropertyAssociation associationToContentId = new PropertyAssociation();
                associationToContentId.setPropertyIndex(5);
                associationToContentId.setEssential(false);
                entry.addAssociation(associationToContentId);
            }
            assoc.addEntry(entry);
            iprp.addItemPropertyAssociation(assoc);
        }
        return iprp;
    }

    private Box makeItemPropertiesBox_grid_or_tile() {
        ItemPropertiesBox iprp = new ItemPropertiesBox();
        ItemPropertyContainerBox ipco = new ItemPropertyContainerBox();
        ipco.addProperty(makeComponentDefinitionBox_rgb3()); // 1
        ipco.addProperty(makeUncompressedFrameConfigBox_grid_item()); // 2
        ipco.addProperty(makeImageSpatialExtentsProperty_grid_tile()); // 3
        ipco.addProperty(makeImageSpatialExtentsProperty_grid()); // 4
        ipco.addProperty(makeUncompressedFrameConfigBox_tiled_item()); // 5
        ipco.addProperty(makeContentIdPropertyGridItem()); // 6
        ipco.addProperty(makeContentIdPropertyFakeSecurity()); // 7
        ipco.addProperty(makeUserDescription_copyright()); // 8
        ipco.addProperty(makeClockInfoItemProperty()); // 9
        ipco.addProperty(makeTimeStampBox()); // 10
        ipco.addProperty(makeModelTransformationProperty()); // 11
        ipco.addProperty(makeWKT2Property()); // 12
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
                PropertyAssociation associationToImageSpatialExtentsProperty =
                        new PropertyAssociation();
                associationToImageSpatialExtentsProperty.setPropertyIndex(4);
                associationToImageSpatialExtentsProperty.setEssential(false);
                entry.addAssociation(associationToImageSpatialExtentsProperty);
            }
            {
                PropertyAssociation associationToContentId = new PropertyAssociation();
                associationToContentId.setPropertyIndex(6);
                associationToContentId.setEssential(false);
                entry.addAssociation(associationToContentId);
            }
            {
                PropertyAssociation associationToCopyrightUserDescription =
                        new PropertyAssociation();
                associationToCopyrightUserDescription.setPropertyIndex(8);
                associationToCopyrightUserDescription.setEssential(false);
                entry.addAssociation(associationToCopyrightUserDescription);
            }
            {
                PropertyAssociation associationToClockInfo = new PropertyAssociation();
                associationToClockInfo.setPropertyIndex(9);
                associationToClockInfo.setEssential(false);
                entry.addAssociation(associationToClockInfo);
            }
            {
                PropertyAssociation assocationToTimestampTAI = new PropertyAssociation();
                assocationToTimestampTAI.setPropertyIndex(10);
                assocationToTimestampTAI.setEssential(false);
                entry.addAssociation(assocationToTimestampTAI);
            }
            {
                PropertyAssociation associationToTransformMatrix = new PropertyAssociation();
                associationToTransformMatrix.setPropertyIndex(11);
                associationToTransformMatrix.setEssential(false);
                entry.addAssociation(associationToTransformMatrix);
            }
            {
                PropertyAssociation associationToWkt2 = new PropertyAssociation();
                associationToWkt2.setPropertyIndex(12);
                associationToWkt2.setEssential(false);
                entry.addAssociation(associationToWkt2);
            }
            assoc.addEntry(entry);
            iprp.addItemPropertyAssociation(assoc);
        }

        {
            ItemPropertyAssociation assoc = new ItemPropertyAssociation();
            AssociationEntry entry = new AssociationEntry();
            entry.setItemId(ALTERNATE_ITEM_ID);
            {
                PropertyAssociation associationToImageSpatialExtentsProperty =
                        new PropertyAssociation();
                associationToImageSpatialExtentsProperty.setPropertyIndex(4);
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
                associationToUncompressedFrameConfigBox.setPropertyIndex(5);
                associationToUncompressedFrameConfigBox.setEssential(true);
                entry.addAssociation(associationToUncompressedFrameConfigBox);
            }
            {
                PropertyAssociation associationToContentId = new PropertyAssociation();
                associationToContentId.setPropertyIndex(6);
                associationToContentId.setEssential(false);
                entry.addAssociation(associationToContentId);
            }
            {
                PropertyAssociation associationToCopyrightUserDescription =
                        new PropertyAssociation();
                associationToCopyrightUserDescription.setPropertyIndex(8);
                associationToCopyrightUserDescription.setEssential(false);
                entry.addAssociation(associationToCopyrightUserDescription);
            }
            {
                PropertyAssociation associationToClockInfo = new PropertyAssociation();
                associationToClockInfo.setPropertyIndex(9);
                associationToClockInfo.setEssential(false);
                entry.addAssociation(associationToClockInfo);
            }
            {
                PropertyAssociation assocationToTimestampTAI = new PropertyAssociation();
                assocationToTimestampTAI.setPropertyIndex(10);
                assocationToTimestampTAI.setEssential(false);
                entry.addAssociation(assocationToTimestampTAI);
            }
            {
                PropertyAssociation associationToTransformMatrix = new PropertyAssociation();
                associationToTransformMatrix.setPropertyIndex(11);
                associationToTransformMatrix.setEssential(false);
                entry.addAssociation(associationToTransformMatrix);
            }
            {
                PropertyAssociation associationToWkt2 = new PropertyAssociation();
                associationToWkt2.setPropertyIndex(12);
                associationToWkt2.setEssential(false);
                entry.addAssociation(associationToWkt2);
            }
            assoc.addEntry(entry);
            iprp.addItemPropertyAssociation(assoc);
        }

        {
            ItemPropertyAssociation assoc = new ItemPropertyAssociation();
            AssociationEntry entry = new AssociationEntry();
            entry.setItemId(FAKE_SECURITY_ITEM_ID);
            {
                PropertyAssociation associationToContentId = new PropertyAssociation();
                associationToContentId.setPropertyIndex(7);
                associationToContentId.setEssential(false);
                entry.addAssociation(associationToContentId);
            }
            assoc.addEntry(entry);
            iprp.addItemPropertyAssociation(assoc);
        }
        return iprp;
    }

    private AbstractItemProperty makeUserDescription_copyright() {
        UserDescriptionProperty udes = new UserDescriptionProperty();
        udes.setLang("en-AU");
        udes.setDescriptiveName("Copyright Statement");
        udes.setDescription(
                "CCBY \"Jacobs Group (Australia) Pty Ltd and Australian Capital Territory\"");
        udes.setTags("copyright");
        return udes;
    }

    private ImageSpatialExtentsProperty makeImageSpatialExtentsProperty_grid_tile() {
        ImageSpatialExtentsProperty ispe = new ImageSpatialExtentsProperty();
        ispe.setImageHeight(tileHeight);
        ispe.setImageWidth(tileWidth);
        return ispe;
    }

    private ImageSpatialExtentsProperty makeImageSpatialExtentsProperty_grid() {
        ImageSpatialExtentsProperty ispe = new ImageSpatialExtentsProperty();
        ispe.setImageHeight(imageHeight);
        ispe.setImageWidth(imageWidth);
        return ispe;
    }

    private Box makeItemReferenceBoxGrid() {
        ItemReferenceBox iref = new ItemReferenceBox();
        {
            DerivedImage primary_item_dimg = new DerivedImage();
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
            ContentDescribes cdsc = new ContentDescribes();
            cdsc.setFromItemId(FAKE_SECURITY_ITEM_ID);
            cdsc.addReference(MAIN_ITEM_ID);
            iref.addItem(cdsc);
        }
        return iref;
    }

    private Box makeItemReferenceBoxTile() {
        ItemReferenceBox iref = new ItemReferenceBox();
        {
            ContentDescribes cdsc = new ContentDescribes();
            cdsc.setFromItemId(FAKE_SECURITY_ITEM_ID);
            cdsc.addReference(ALTERNATE_ITEM_ID);
            iref.addItem(cdsc);
        }
        return iref;
    }

    private Box makeItemReferenceBoxGridOrTile() {
        ItemReferenceBox iref = new ItemReferenceBox();
        {
            DerivedImage primary_item_dimg = new DerivedImage();
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
            ContentDescribes cdsc = new ContentDescribes();
            cdsc.setFromItemId(FAKE_SECURITY_ITEM_ID);
            cdsc.addReference(MAIN_ITEM_ID);
            iref.addItem(cdsc);
        }
        {
            ContentDescribes cdsc = new ContentDescribes();
            cdsc.setFromItemId(FAKE_SECURITY_ITEM_ID);
            cdsc.addReference(ALTERNATE_ITEM_ID);
            iref.addItem(cdsc);
        }
        return iref;
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

    private UncompressedFrameConfigBox makeUncompressedFrameConfigBox_grid_item() {
        UncompressedFrameConfigBox uncc = new UncompressedFrameConfigBox();
        uncc.setProfile(new FourCC("rgb3"));
        uncc.addComponent(new Component(0, 7, ComponentFormat.UnsignedInteger, 0));
        uncc.addComponent(new Component(1, 7, ComponentFormat.UnsignedInteger, 0));
        uncc.addComponent(new Component(2, 7, ComponentFormat.UnsignedInteger, 0));
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
        uncc.setNumTileRowsMinusOne(0);
        return uncc;
    }

    private UncompressedFrameConfigBox makeUncompressedFrameConfigBox_tiled_item() {
        UncompressedFrameConfigBox uncc = new UncompressedFrameConfigBox();
        uncc.setProfile(new FourCC("gene"));
        uncc.addComponent(new Component(0, 7, ComponentFormat.UnsignedInteger, 0));
        uncc.addComponent(new Component(1, 7, ComponentFormat.UnsignedInteger, 0));
        uncc.addComponent(new Component(2, 7, ComponentFormat.UnsignedInteger, 0));
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
        uncc.setNumTileColumnsMinusOne(NUM_TILE_COLUMNS - 1);
        uncc.setNumTileRowsMinusOne(NUM_TILE_ROWS - 1);
        return uncc;
    }

    private MediaDataBox createMediaDataBox_rgb_grid() throws IOException {
        MediaDataBox mdat = new MediaDataBox();
        System.out.println(
                String.format(
                        "Tile width: %d, tile height: %d",
                        directory.getTileWidth(), directory.getTileHeight()));
        System.out.println(directory.getTileByteCounts());
        int numTilesX =
                (directory.getImageWidth().intValue() + directory.getTileWidth().intValue() - 1)
                        / directory.getTileWidth().intValue();
        int numTilesY =
                (directory.getImageHeight().intValue() + directory.getTileHeight().intValue() - 1)
                        / directory.getTileHeight().intValue();

        for (int y = 0; y < numTilesY; y++) {
            for (int x = 0; x < numTilesX; x++) {
                byte[] tileBytes = directory.getTileOrStrip(x, y, 0);
                mdat.appendData(tileBytes);
            }
        }

        return mdat;
    }

    private int getItemIdForGridComponent(int x, int y) {
        return y * NUM_TILE_COLUMNS + x + 1;
    }

    private byte[] getGridItemAsBytes(boolean dump) throws IOException {
        GridItem gridItem = new GridItem();
        gridItem.setRows(NUM_TILE_ROWS);
        gridItem.setColumns(NUM_TILE_COLUMNS);
        gridItem.setOutput_width(NUM_TILE_COLUMNS * tileWidth);
        gridItem.setOutput_height(NUM_TILE_ROWS * tileHeight);
        // TODO: dump if needed
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        gridItem.writeTo(streamWriter);
        return baos.toByteArray();
    }

    private UUIDProperty makeContentIdPropertyGridItem() {
        UUIDProperty contentIdProperty = new UUIDProperty();
        contentIdProperty.setExtendedType(CONTENT_ID_UUID);
        ByteBuffer bb = ByteBuffer.allocate(16);
        bb.putLong(GRID_ITEM_CONTENT_ID_UUID.getMostSignificantBits());
        bb.putLong(GRID_ITEM_CONTENT_ID_UUID.getLeastSignificantBits());
        contentIdProperty.setPayload(bb.array());
        return contentIdProperty;
    }

    private UUIDProperty makeContentIdPropertyFakeSecurity() {
        UUIDProperty contentIdProperty = new UUIDProperty();
        contentIdProperty.setExtendedType(CONTENT_ID_UUID);
        ByteBuffer bb = ByteBuffer.allocate(16);
        bb.putLong(FAKE_SECURITY_CONTENT_ID_UUID.getMostSignificantBits());
        bb.putLong(FAKE_SECURITY_CONTENT_ID_UUID.getLeastSignificantBits());
        contentIdProperty.setPayload(bb.array());
        return contentIdProperty;
    }

    private TAITimeStampBox makeTimeStampBox() {
        TAITimeStampBox itai = new TAITimeStampBox();
        TAITimeStampPacket time_stamp_packet = new TAITimeStampPacket();
        Instant instant = TIMESTAMP.toInstant(ZoneOffset.UTC);
        UtcInstant utcInstant = UtcInstant.of(instant);
        TaiInstant taiInstant = utcInstant.toTaiInstant();
        long timestamp =
                GIMIUtils.NANOS_PER_SECOND * taiInstant.getTaiSeconds() + taiInstant.getNano();
        time_stamp_packet.setTAITimeStamp(timestamp);
        time_stamp_packet.setStatusBits((byte) 0x02);
        itai.setTimeStampPacket(time_stamp_packet);
        return itai;
    }

    private TAIClockInfoItemProperty makeClockInfoItemProperty() {

        TAIClockInfoItemProperty taic = new TAIClockInfoItemProperty();
        taic.setTimeUncertainty(24 * 60 * 60 * GIMIUtils.NANOS_PER_SECOND);
        taic.setCorrectionOffset(0);
        taic.setClockDriftRate(Float.NaN);
        taic.setReferenceSourceType((byte) 0x01);
        return taic;
    }

    private ModelTransformationProperty makeModelTransformationProperty() {
        ModelTransformationProperty modelTransformation = new ModelTransformationProperty();
        assertEquals(this.pixelScale.size(), 3);
        assertEquals(this.pixelScale.get(2), 0.0);
        double sx = this.pixelScale.get(0);
        double sy = this.pixelScale.get(1);
        assertEquals(this.modelTiePoint.size(), 6);
        double tx = this.modelTiePoint.get(3) + this.modelTiePoint.get(0) / sx;
        double ty = this.modelTiePoint.get(4) + this.modelTiePoint.get(1) / sy;
        modelTransformation.setM00(sx);
        modelTransformation.setM01(0.0);
        modelTransformation.setM03(tx);
        modelTransformation.setM10(0.0);
        modelTransformation.setM11(-1.0 * sy);
        modelTransformation.setM13(ty);
        return modelTransformation;
    }

    private CoordinateReferenceSystemProperty makeWKT2Property() {
        CoordinateReferenceSystemProperty prop = new CoordinateReferenceSystemProperty();
        // TODO: make sure it is not hard coded
        prop.setCrsEncoding(new FourCC("wkt2"));
        prop.setCrs(
                "PROJCRS[\"GDA94 / MGA zone 55\",BASEGEOGCRS[\"GDA94\",DATUM[\"Geocentric Datum of Australia 1994\",ELLIPSOID[\"GRS 1980\",6378137,298.257222101,LENGTHUNIT[\"metre\",1]]],PRIMEM[\"Greenwich\",0,ANGLEUNIT[\"degree\",0.0174532925199433]],ID[\"EPSG\",4283]],CONVERSION[\"Map Grid of Australia zone 55\",METHOD[\"Transverse Mercator\",ID[\"EPSG\",9807]],PARAMETER[\"Latitude of natural origin\",0,ANGLEUNIT[\"degree\",0.0174532925199433],ID[\"EPSG\",8801]],PARAMETER[\"Longitude of natural origin\",147,ANGLEUNIT[\"degree\",0.0174532925199433],ID[\"EPSG\",8802]],PARAMETER[\"Scale factor at natural origin\",0.9996,SCALEUNIT[\"unity\",1],ID[\"EPSG\",8805]],PARAMETER[\"False easting\",500000,LENGTHUNIT[\"metre\",1],ID[\"EPSG\",8806]],PARAMETER[\"False northing\",10000000,LENGTHUNIT[\"metre\",1],ID[\"EPSG\",8807]]],CS[Cartesian,2],AXIS[\"(E)\",east,ORDER[1],LENGTHUNIT[\"metre\",1]],AXIS[\"(N)\",north,ORDER[2],LENGTHUNIT[\"metre\",1]],USAGE[SCOPE[\"Engineering survey, topographic mapping.\"],AREA[\"Australia - onshore and offshore between 144°E and 150°E.\"],BBOX[-50.89,144,-9.23,150.01]],ID[\"EPSG\",28355]]");
        return prop;
    }
}
