package net.frogmouth.rnd.eofff.tools.gimi;

import static org.testng.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;
import mil.nga.tiff.FileDirectory;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.AbstractItemProperty;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.AssociationEntry;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemPropertiesBox;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemPropertyAssociation;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemPropertyContainerBox;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.PropertyAssociation;
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
import net.frogmouth.rnd.eofff.isobmff.iref.ItemReferenceBox;
import net.frogmouth.rnd.eofff.isobmff.iref.SingleItemReferenceBox;
import net.frogmouth.rnd.eofff.isobmff.mdat.MediaDataBox;
import net.frogmouth.rnd.eofff.isobmff.meta.MetaBox;
import net.frogmouth.rnd.eofff.isobmff.pitm.PrimaryItemBox;
import net.frogmouth.rnd.eofff.tools.ItemDataBoxBuilder;
import net.frogmouth.rnd.eofff.tools.MediaDataBoxBuilder;
import net.frogmouth.rnd.eofff.uncompressed.cmpd.ComponentDefinition;
import net.frogmouth.rnd.eofff.uncompressed.cmpd.ComponentDefinitionBox;
import net.frogmouth.rnd.eofff.uncompressed.uncc.Component;
import net.frogmouth.rnd.eofff.uncompressed.uncc.ComponentFormat;
import net.frogmouth.rnd.eofff.uncompressed.uncc.Interleaving;
import net.frogmouth.rnd.eofff.uncompressed.uncc.SamplingType;
import net.frogmouth.rnd.eofff.uncompressed.uncc.UncompressedFrameConfigBox;
import org.jmisb.api.klv.BerDecoder;
import org.jmisb.api.klv.BerField;
import org.jmisb.api.klv.UniversalLabel;
import org.jmisb.st0601.FullCornerLatitude;
import org.jmisb.st0601.FullCornerLongitude;
import org.jmisb.st0601.IUasDatalinkValue;
import org.jmisb.st0601.PrecisionTimeStamp;
import org.jmisb.st0601.UasDatalinkMessage;
import org.jmisb.st0601.UasDatalinkString;
import org.jmisb.st0601.UasDatalinkTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

public class CreateGIMIGridTest extends GIMIValidator {
    private static final Logger LOG = LoggerFactory.getLogger(CreateGIMIGridTest.class);

    private static final long MAIN_ITEM_ID = 1000;
    private static final long ALTERNATE_ITEM_ID = 1100;
    private static final long FAKE_SECURITY_ITEM_ID = 1200;

    private static final int LENGTH_OF_FREEBOX_HEADER = 8;
    private static final int NUM_BYTES_PER_PIXEL_RGB = 3;

    private final int tileWidth;
    private final int tileHeight;
    private static final int NUM_TILE_ROWS = 16;
    private static final int NUM_TILE_COLUMNS = 20;
    private final int imageHeight;
    private final int imageWidth;
    private static final int MDAT_START_GRID = 27000;
    private static final int MDAT_START_GRID_DATA = MDAT_START_GRID + 2 * Integer.BYTES;
    private final int tileSizeBytes;

    private static final UUID GRID_ITEM_CONTENT_ID_UUID = UUID.randomUUID();
    private static final String GRID_ITEM_CONTENT_ID = "urn:uuid:" + GRID_ITEM_CONTENT_ID_UUID;

    private static final UUID FAKE_SECURITY_CONTENT_ID_UUID = UUID.randomUUID();
    private static final String FAKE_SECURITY_CONTENT_ID =
            "urn:uuid:" + FAKE_SECURITY_CONTENT_ID_UUID;
    private final String FAKE_SECURITY_MIME_TYPE = "application/x.fake-dni-arh+xml";
    private final String FAKE_SECURITY_XML =
            """
            <?xml version="1.0" encoding="UTF-8"?>
            <FakeSecurity xmlns="http://www.opengis.net/CodeSprint2023Oct/Security">
                <FakeLevel>SECRETIVE-ISH</FakeLevel>
                <FakeCaveat>DOWN-UNDER</FakeCaveat>
                <FakeCaveat>SUBURBIA</FakeCaveat>
                <FakeRelTo>UK</FakeRelTo>
                <FakeRelTo>AU</FakeRelTo>
                <FakeDeclassOn>2024-10-01</FakeDeclassOn>
            </FakeSecurity>""";

    private static final long ST0601_METADATA_ITEM_ID = 1300;
    private static final String ST0601_URI = "urn:smpte:ul:060E2B34.020B0101.0E010301.01000000";
    private static final UUID ST0601_METADATA_CONTENT_ID_UUID = UUID.randomUUID();
    private static final String ST0601_METADATA_CONTENT_ID =
            "urn:uuid:" + ST0601_METADATA_CONTENT_ID_UUID;

    private final FileDirectory directory;
    private final GeoTransform transform;
    private static final UUID CONTENT_ID_UUID =
            UUID.fromString("aac8ab7d-f519-5437-b7d3-c973d155e253");

    private final String path;

    public CreateGIMIGridTest() throws IOException {
        path = "/home/bradh/gdal_hacks/ACT2017-epsg4326-trimmed.tif";
        mil.nga.tiff.TIFFImage tiffImage = mil.nga.tiff.TiffReader.readTiff(new File(path));
        List<mil.nga.tiff.FileDirectory> directories = tiffImage.getFileDirectories();
        directory = directories.get(0);
        tileWidth = (int) directory.getTileWidth();
        tileHeight = (int) directory.getTileHeight();
        imageHeight = (int) directory.getImageHeight();
        imageWidth = (int) directory.getImageWidth();
        tileSizeBytes = tileWidth * tileHeight * NUM_BYTES_PER_PIXEL_RGB;
        List<Double> pixelScale = directory.getModelPixelScale();
        List<Double> modelTiePoint = directory.getModelTiepoint();
        transform = new GeoTransform(modelTiePoint, pixelScale);
    }

    @Test
    public void writeFile_rgb_grid() throws IOException {
        List<Box> boxes = new ArrayList<>();
        FileTypeBox ftyp = createFileTypeBox();
        boxes.add(ftyp);
        MetaBox meta = createMetaBox_rgb();
        boxes.add(meta);
        long lengthOfPreviousBoxes = ftyp.getSize() + meta.getSize();
        long numberOfFillBytes = MDAT_START_GRID - lengthOfPreviousBoxes - LENGTH_OF_FREEBOX_HEADER;
        FreeBox free = new FreeBox();
        free.setData(new byte[(int) numberOfFillBytes]);
        boxes.add(free);
        MediaDataBox mdat = createMediaDataBox_rgb_grid();
        boxes.add(mdat);
        writeBoxes(boxes, "test_gimi_rgb_grid.heif");
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

    private PrimaryItemBox makePrimaryItemBox() {
        PrimaryItemBox pitm = new PrimaryItemBox();
        pitm.setItemID(MAIN_ITEM_ID);
        return pitm;
    }

    private MetaBox createMetaBox_rgb() throws IOException {
        MetaBox meta = new MetaBox();
        List<Box> boxes = new ArrayList<>();
        boxes.add(makeHandlerBox());
        boxes.add(makePrimaryItemBox());
        boxes.add(makeItemInfoBoxGrid());
        boxes.add(makeItemLocationBox_grid());
        boxes.add(makeItemDataBox_grid());
        boxes.add(makeItemPropertiesBox_grid());
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
            infe2.setItemID(FAKE_SECURITY_ITEM_ID);
            FourCC mime_fourcc = new FourCC("mime");
            infe2.setItemType(mime_fourcc.asUnsigned());
            infe2.setContentType(FAKE_SECURITY_MIME_TYPE);
            infe2.setItemName("Security Marking (Fake XML)");
            iinf.addItem(infe2);
        }
        {
            ItemInfoEntry infe3 = new ItemInfoEntry();
            infe3.setVersion(2);
            infe3.setItemID(ST0601_METADATA_ITEM_ID);
            FourCC mime_fourcc = new FourCC("uri ");
            infe3.setItemType(mime_fourcc.asUnsigned());
            infe3.setItemUriType(ST0601_URI);
            infe3.setItemName("General Metadata (ST 0601)");
            iinf.addItem(infe3);
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
        {
            ILocItem st0601MetadataItemLocation = new ILocItem();
            st0601MetadataItemLocation.setConstructionMethod(1);
            st0601MetadataItemLocation.setItemId(ST0601_METADATA_ITEM_ID);
            st0601MetadataItemLocation.setBaseOffset(offset);
            ILocExtent metadataExtent = new ILocExtent();
            metadataExtent.setExtentIndex(0);
            metadataExtent.setExtentOffset(0);
            metadataExtent.setExtentLength(getST0601MetadataBytes(false).length);
            offset += metadataExtent.getExtentLength();
            st0601MetadataItemLocation.addExtent(metadataExtent);
            iloc.addItem(st0601MetadataItemLocation);
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
        itemDataBoxBuilder.addData(this.getST0601MetadataBytes(true));
        return itemDataBoxBuilder.build();
    }

    private Box makeItemPropertiesBox_grid() {
        ItemPropertiesBox iprp = new ItemPropertiesBox();
        ItemPropertyContainerBox ipco = new ItemPropertyContainerBox();
        ipco.addProperty(makeComponentDefinitionBox_rgb3()); // 1
        ipco.addProperty(makeUncompressedFrameConfigBox_grid_item()); // 2
        ipco.addProperty(makeImageSpatialExtentsProperty_grid_tile()); // 3
        ipco.addProperty(makeImageSpatialExtentsProperty_grid()); // 4
        ipco.addProperty(makeUncompressedFrameConfigBox_tiled_item()); // 5
        ipco.addProperty(makeContentIdPropertyGridItem()); // 6
        ipco.addProperty(makeContentIdPropertyFakeSecurity()); // 7
        ipco.addProperty(makeContentIdST0601Metadata()); // 8
        ipco.addProperty(makeUserDescription_copyright()); // 9
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
                associationToCopyrightUserDescription.setPropertyIndex(9);
                associationToCopyrightUserDescription.setEssential(false);
                entry.addAssociation(associationToCopyrightUserDescription);
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
                associationToCopyrightUserDescription.setPropertyIndex(9);
                associationToCopyrightUserDescription.setEssential(false);
                entry.addAssociation(associationToCopyrightUserDescription);
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
        {
            ItemPropertyAssociation assoc = new ItemPropertyAssociation();
            AssociationEntry entry = new AssociationEntry();
            entry.setItemId(ST0601_METADATA_ITEM_ID);
            {
                PropertyAssociation associationToContentId = new PropertyAssociation();
                associationToContentId.setPropertyIndex(8);
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
            SingleItemReferenceBox cdsc = new SingleItemReferenceBox(new FourCC("cdsc"));
            cdsc.setFromItemId(FAKE_SECURITY_ITEM_ID);
            cdsc.addReference(MAIN_ITEM_ID);
            iref.addItem(cdsc);
        }
        {
            SingleItemReferenceBox cdsc = new SingleItemReferenceBox(new FourCC("cdsc"));
            cdsc.setFromItemId(FAKE_SECURITY_ITEM_ID);
            cdsc.addReference(ALTERNATE_ITEM_ID);
            iref.addItem(cdsc);
        }
        {
            SingleItemReferenceBox cdsc = new SingleItemReferenceBox(new FourCC("cdsc"));
            cdsc.setFromItemId(ST0601_METADATA_ITEM_ID);
            cdsc.addReference(MAIN_ITEM_ID);
            iref.addItem(cdsc);
        }
        {
            SingleItemReferenceBox cdsc = new SingleItemReferenceBox(new FourCC("cdsc"));
            cdsc.setFromItemId(ST0601_METADATA_ITEM_ID);
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
        uncc.setNumTileColumnsMinusOne(0);
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
        MediaDataBoxBuilder mdatBuilder = new MediaDataBoxBuilder();
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
                mdatBuilder.addData(tileBytes);
            }
        }

        return mdatBuilder.build();
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
        contentIdProperty.setPayload(GRID_ITEM_CONTENT_ID.getBytes(StandardCharsets.UTF_8));
        System.out.println(contentIdProperty.toString());
        return contentIdProperty;
    }

    private UUIDProperty makeContentIdPropertyFakeSecurity() {
        UUIDProperty contentIdProperty = new UUIDProperty();
        contentIdProperty.setExtendedType(CONTENT_ID_UUID);
        contentIdProperty.setPayload(FAKE_SECURITY_CONTENT_ID.getBytes(StandardCharsets.UTF_8));
        System.out.println(contentIdProperty.toString());
        return contentIdProperty;
    }

    private UUIDProperty makeContentIdST0601Metadata() {
        UUIDProperty contentIdProperty = new UUIDProperty();
        contentIdProperty.setExtendedType(CONTENT_ID_UUID);
        contentIdProperty.setPayload(ST0601_METADATA_CONTENT_ID.getBytes(StandardCharsets.UTF_8));
        System.out.println(contentIdProperty.toString());
        return contentIdProperty;
    }

    private byte[] getST0601MetadataBytes(boolean dump) {
        SortedMap<UasDatalinkTag, IUasDatalinkValue> map = new TreeMap<>();
        map.put(UasDatalinkTag.UasLdsVersionNumber, new org.jmisb.st0601.ST0601Version((short) 19));
        map.put(
                UasDatalinkTag.CornerLatPt1,
                new FullCornerLatitude(
                        transform.getLatitude(0, 0), FullCornerLatitude.CORNER_LAT_1));
        map.put(
                UasDatalinkTag.CornerLonPt1,
                new FullCornerLongitude(
                        transform.getLongitude(0, 0), FullCornerLongitude.CORNER_LON_1));
        map.put(
                UasDatalinkTag.CornerLatPt2,
                new FullCornerLatitude(
                        transform.getLatitude(imageWidth, 0), FullCornerLatitude.CORNER_LAT_2));
        map.put(
                UasDatalinkTag.CornerLonPt2,
                new FullCornerLongitude(
                        transform.getLongitude(imageWidth, 0), FullCornerLongitude.CORNER_LON_2));
        map.put(
                UasDatalinkTag.CornerLatPt3,
                new FullCornerLatitude(
                        transform.getLatitude(imageWidth, imageHeight),
                        FullCornerLatitude.CORNER_LAT_3));
        map.put(
                UasDatalinkTag.CornerLonPt3,
                new FullCornerLongitude(
                        transform.getLongitude(imageWidth, imageHeight),
                        FullCornerLongitude.CORNER_LON_3));
        map.put(
                UasDatalinkTag.CornerLatPt4,
                new FullCornerLatitude(
                        transform.getLatitude(0, imageHeight), FullCornerLatitude.CORNER_LAT_4));
        map.put(
                UasDatalinkTag.CornerLonPt4,
                new FullCornerLongitude(
                        transform.getLongitude(0, imageHeight), FullCornerLongitude.CORNER_LON_4));
        // Pick a rough estimate
        LocalDateTime ldt = LocalDateTime.of(LocalDate.of(2017, 5, 15), LocalTime.of(2, 0, 0));
        map.put(UasDatalinkTag.PrecisionTimeStamp, new PrecisionTimeStamp(ldt));
        map.put(
                UasDatalinkTag.MissionId,
                new UasDatalinkString(UasDatalinkString.MISSION_ID, path));

        var st0601 = new UasDatalinkMessage(map);
        if (dump) {
            dumpMisbMessage(st0601);
        }
        byte[] st0601bytes = st0601.frameMessage(false);
        BerField ber = BerDecoder.decode(st0601bytes, UniversalLabel.LENGTH, false);
        int lengthOfLength = ber.getLength();
        byte[] st0601ValueBytes = new byte[ber.getValue()];
        System.arraycopy(
                st0601bytes,
                UniversalLabel.LENGTH + lengthOfLength,
                st0601ValueBytes,
                0,
                st0601ValueBytes.length);
        return st0601ValueBytes;
    }
}
