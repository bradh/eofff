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
import net.frogmouth.rnd.eofff.imagefileformat.extensions.groups.ImagePyramidEntityGroup;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.groups.PyramidEntityEntry;
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
import net.frogmouth.rnd.eofff.isobmff.grpl.AlternativesEntityToGroupBox;
import net.frogmouth.rnd.eofff.isobmff.grpl.GroupsListBox;
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
import net.frogmouth.rnd.eofff.uncompressed_experiments.geo.ModelTransformationProperty;
import net.frogmouth.rnd.eofff.uncompressed_experiments.geo.WellKnownText2Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import org.threeten.extra.scale.TaiInstant;
import org.threeten.extra.scale.UtcInstant;

public class CreateGIMIGridPyramidTest extends GIMIValidator {
    private static final Logger LOG = LoggerFactory.getLogger(CreateGIMIGridPyramidTest.class);

    private static final long MAIN_ITEM_ID = 10000;
    private static final long FAKE_SECURITY_ITEM_ID = 12000;

    private static final int LENGTH_OF_FREEBOX_HEADER = 8;
    private static final int NUM_BYTES_PER_PIXEL_RGB = 3;

    private final int tileWidth;
    private final int tileHeight;
    private static final int MDAT_START_GRID = 43832;
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

    private static final UUID CONTENT_ID_UUID =
            UUID.fromString("aac8ab7d-f519-5437-b7d3-c973d155e253");

    private final LocalDateTime TIMESTAMP =
            LocalDateTime.of(LocalDate.of(2017, 5, 7), LocalTime.MIN);

    List<mil.nga.tiff.FileDirectory> directories;

    private final String path;

    public CreateGIMIGridPyramidTest() throws IOException {
        path = "/home/bradh/gdal_hacks/ACT2017-cog.tif";
        mil.nga.tiff.TIFFImage tiffImage = mil.nga.tiff.TiffReader.readTiff(new File(path));
        directories = tiffImage.getFileDirectories();
        final FileDirectory directory = directories.get(0);
        tileWidth = (int) directory.getTileWidth();
        tileHeight = (int) directory.getTileHeight();
        tileSizeBytes = tileWidth * tileHeight * NUM_BYTES_PER_PIXEL_RGB;
    }

    @Test
    public void writeFile_rgb_grid_pymd() throws IOException {
        List<Box> boxes = new ArrayList<>();
        FileTypeBox ftyp = createFileTypeBox();
        boxes.add(ftyp);
        MetaBox meta = createMetaBox_rgb_grid_pymd();
        boxes.add(meta);
        long lengthOfPreviousBoxes = ftyp.getSize() + meta.getSize();
        long numberOfFillBytes = MDAT_START_GRID - lengthOfPreviousBoxes - LENGTH_OF_FREEBOX_HEADER;
        FreeBox free = new FreeBox();
        free.setData(new byte[(int) numberOfFillBytes]);
        boxes.add(free);
        MediaDataBox mdat = createMediaDataBox_rgb_grid_pymd();
        boxes.add(mdat);
        writeBoxes(boxes, "ACT2017_gimi_rgb_grid_pymd.heif");
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

    private MetaBox createMetaBox_rgb_grid_pymd() throws IOException {
        MetaBox meta = new MetaBox();
        List<Box> boxes = new ArrayList<>();
        boxes.add(makeHandlerBox());
        boxes.add(makePrimaryItemBox(MAIN_ITEM_ID));
        boxes.add(makeItemInfoBoxPyramid());
        boxes.add(makeItemLocationBox_grid_pymd());
        boxes.add(makeItemDataBox_grid_pymd());
        boxes.add(makeItemPropertiesBox_grid_pymd());
        boxes.add(makeItemReferenceBoxGrid_pymd());
        GroupsListBox grpl = new GroupsListBox();
        AlternativesEntityToGroupBox altr = new AlternativesEntityToGroupBox();
        ImagePyramidEntityGroup pymd = new ImagePyramidEntityGroup();
        pymd.setTileSizeX(tileWidth);
        pymd.setTileSizeY(tileHeight);
        int numDirectories = directories.size();
        for (int directoryIndex = 0; directoryIndex < numDirectories; directoryIndex++) {
            altr.addEntity(MAIN_ITEM_ID + directoryIndex);
            pymd.addEntity(MAIN_ITEM_ID + directoryIndex);
            int binning = 1 << directoryIndex;
            int numTilesX = this.getNumTilesX(directoryIndex);
            int numTilesY = this.getNumTilesY(directoryIndex);
            PyramidEntityEntry entry =
                    new PyramidEntityEntry(binning, numTilesY - 1, numTilesX - 1);
            pymd.addPyramidEntry(entry);
        }
        grpl.addGrouping(pymd);
        boxes.add(grpl);
        meta.addNestedBoxes(boxes);
        return meta;
    }

    private ItemInfoBox makeItemInfoBoxPyramid() {
        ItemInfoBox iinf = new ItemInfoBox();
        int numDirectories = directories.size();
        for (int directoryIndex = 0; directoryIndex < numDirectories; directoryIndex++) {
            for (int y = 0; y < this.getNumTilesY(directoryIndex); y++) {
                for (int x = 0; x < this.getNumTilesX(directoryIndex); x++) {
                    int itemID = getItemIdForGridComponent(x, y, directoryIndex);
                    ItemInfoEntry infexy = new ItemInfoEntry();
                    infexy.setVersion(2);
                    infexy.setFlags(1); // Hidden
                    infexy.setItemID(itemID);
                    FourCC unci = new FourCC("unci");
                    infexy.setItemType(unci.asUnsigned());
                    infexy.setItemName(
                            String.format(
                                    "Grid input image [%d, %d, zoom = %d]", x, y, directoryIndex));
                    iinf.addItem(infexy);
                }
            }
            {
                ItemInfoEntry infe0 = new ItemInfoEntry();
                infe0.setVersion(2);
                infe0.setItemID(MAIN_ITEM_ID + directoryIndex);
                if (directoryIndex != 0) {
                    infe0.setFlags(1); // Hidden
                } else {
                    infe0.setFlags(0);
                }
                FourCC grid = new FourCC("grid");
                infe0.setItemType(grid.asUnsigned());
                infe0.setItemName(String.format("Grid Image [zoom = %d]", directoryIndex));
                iinf.addItem(infe0);
            }
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

    private ItemLocationBox makeItemLocationBox_grid_pymd() throws IOException {
        ItemLocationBox iloc = new ItemLocationBox();
        iloc.setOffsetSize(4);
        iloc.setLengthSize(4);
        iloc.setBaseOffsetSize(4);
        iloc.setIndexSize(4);
        iloc.setVersion(1);
        int numDirectories = directories.size();
        int mdatOffset = 0;
        int idatOffset = 0;
        for (int directoryIndex = numDirectories - 1; directoryIndex >= 0; directoryIndex--) {
            for (int y = 0; y < this.getNumTilesY(directoryIndex); y++) {
                for (int x = 0; x < this.getNumTilesX(directoryIndex); x++) {
                    int itemID = getItemIdForGridComponent(x, y, directoryIndex);
                    ILocItem itemxyloc = new ILocItem();
                    itemxyloc.setConstructionMethod(0);
                    itemxyloc.setItemId(itemID);
                    itemxyloc.setBaseOffset(MDAT_START_GRID_DATA);
                    ILocExtent itemxyextent = new ILocExtent();
                    itemxyextent.setExtentIndex(0);
                    itemxyextent.setExtentOffset(mdatOffset);
                    itemxyextent.setExtentLength(tileSizeBytes);
                    mdatOffset += tileSizeBytes;
                    itemxyloc.addExtent(itemxyextent);
                    iloc.addItem(itemxyloc);
                }
            }

            {
                ILocItem gridItemLocation = new ILocItem();
                gridItemLocation.setConstructionMethod(1);
                gridItemLocation.setItemId(MAIN_ITEM_ID + directoryIndex);
                ILocExtent gridItemExtent = new ILocExtent();
                gridItemExtent.setExtentIndex(0);
                gridItemExtent.setExtentOffset(idatOffset);
                gridItemExtent.setExtentLength(
                        this.getPyramidGridItemAsBytes(directoryIndex).length);
                idatOffset += gridItemExtent.getExtentLength();
                gridItemLocation.addExtent(gridItemExtent);
                iloc.addItem(gridItemLocation);
            }
        }
        {
            ILocItem fakeSecurityItemLocation = new ILocItem();
            fakeSecurityItemLocation.setConstructionMethod(1);
            fakeSecurityItemLocation.setItemId(FAKE_SECURITY_ITEM_ID);
            fakeSecurityItemLocation.setBaseOffset(idatOffset);
            ILocExtent securityExtent = new ILocExtent();
            securityExtent.setExtentIndex(0);
            securityExtent.setExtentOffset(0);
            securityExtent.setExtentLength(getFakeSecurityXMLBytes(false).length);
            fakeSecurityItemLocation.addExtent(securityExtent);
            iloc.addItem(fakeSecurityItemLocation);
        }
        return iloc;
    }

    private byte[] getFakeSecurityXMLBytes(boolean dump) {
        byte[] securityXMLBytes = FAKE_SECURITY_XML.getBytes(StandardCharsets.UTF_8);
        return securityXMLBytes;
    }

    private ItemDataBox makeItemDataBox_grid_pymd() throws IOException {
        ItemDataBoxBuilder itemDataBoxBuilder = new ItemDataBoxBuilder();
        itemDataBoxBuilder.addData(this.getPyramidGridItemsAsBytes(true));
        itemDataBoxBuilder.addData(this.getFakeSecurityXMLBytes(true));
        return itemDataBoxBuilder.build();
    }

    private Box makeItemPropertiesBox_grid_pymd() {
        ItemPropertiesBox iprp = new ItemPropertiesBox();
        ItemPropertyContainerBox ipco = new ItemPropertyContainerBox();
        ipco.addProperty(makeComponentDefinitionBox_rgb3()); // 1
        ipco.addProperty(makeUncompressedFrameConfigBox_grid_item()); // 2
        ipco.addProperty(makeImageSpatialExtentsProperty_grid_tile()); // 3
        ipco.addProperty(makeContentIdPropertyGridItem()); // 4
        ipco.addProperty(makeContentIdPropertyFakeSecurity()); // 5
        ipco.addProperty(makeUserDescription_copyright()); // 6
        ipco.addProperty(makeClockInfoItemProperty()); // 7
        ipco.addProperty(makeTimeStampBox()); // 8
        ipco.addProperty(makeWKT2Property()); // 9
        ipco.addProperty(makeModelTransformationProperty()); // 10
        int numDirectories = directories.size();
        for (int directoryIndex = numDirectories - 1; directoryIndex >= 0; directoryIndex--) {
            ipco.addProperty(
                    makeImageSpatialExtentsPropertyForLevel(
                            directoryIndex)); // 11 to 11 + numberDirectories - 1 (e.g. 11 to 16)
        }
        iprp.setItemProperties(ipco);

        ItemPropertyAssociation assoc = new ItemPropertyAssociation();
        for (int directoryIndex = numDirectories - 1; directoryIndex >= 0; directoryIndex--) {
            {
                for (int y = 0; y < this.getNumTilesY(directoryIndex); y++) {
                    for (int x = 0; x < this.getNumTilesX(directoryIndex); x++) {
                        int itemID = getItemIdForGridComponent(x, y, directoryIndex);
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
                        gridItemAssociations.addAssociation(
                                associationToUncompressedFrameConfigBox);

                        PropertyAssociation associationToImageSpatialExtentsProperty =
                                new PropertyAssociation();
                        associationToImageSpatialExtentsProperty.setPropertyIndex(3);
                        associationToImageSpatialExtentsProperty.setEssential(true);
                        gridItemAssociations.addAssociation(
                                associationToImageSpatialExtentsProperty);
                        assoc.addEntry(gridItemAssociations);
                    }
                }
            }
        }
        for (int directoryIndex = numDirectories - 1; directoryIndex >= 0; directoryIndex--) {

            AssociationEntry entry = new AssociationEntry();
            entry.setItemId(MAIN_ITEM_ID + directoryIndex);
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
                PropertyAssociation assocationToTimestampTAI = new PropertyAssociation();
                assocationToTimestampTAI.setPropertyIndex(8);
                assocationToTimestampTAI.setEssential(false);
                entry.addAssociation(assocationToTimestampTAI);
            }
            if (directoryIndex == 0) {
                {
                    PropertyAssociation associationToWkt2 = new PropertyAssociation();
                    associationToWkt2.setPropertyIndex(9);
                    associationToWkt2.setEssential(false);
                    entry.addAssociation(associationToWkt2);
                }
                {
                    PropertyAssociation associationToTransformMatrix = new PropertyAssociation();
                    associationToTransformMatrix.setPropertyIndex(10);
                    associationToTransformMatrix.setEssential(false);
                    entry.addAssociation(associationToTransformMatrix);
                }
            }
            {
                PropertyAssociation associationToImageSpatialExtent = new PropertyAssociation();
                associationToImageSpatialExtent.setPropertyIndex(
                        11 + numDirectories - 1 - directoryIndex);
                associationToImageSpatialExtent.setEssential(true);
                entry.addAssociation(associationToImageSpatialExtent);
            }

            assoc.addEntry(entry);
        }

        {
            AssociationEntry entry = new AssociationEntry();
            entry.setItemId(FAKE_SECURITY_ITEM_ID);
            {
                PropertyAssociation associationToContentId = new PropertyAssociation();
                associationToContentId.setPropertyIndex(5);
                associationToContentId.setEssential(false);
                entry.addAssociation(associationToContentId);
            }
            assoc.addEntry(entry);
        }
        iprp.addItemPropertyAssociation(assoc);
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

    private Box makeItemReferenceBoxGrid_pymd() {
        ItemReferenceBox iref = new ItemReferenceBox();
        int numDirectories = directories.size();
        for (int directoryIndex = numDirectories - 1; directoryIndex >= 0; directoryIndex--) {
            {
                DerivedImage primary_item_dimg = new DerivedImage();
                primary_item_dimg.setFromItemId(MAIN_ITEM_ID + directoryIndex);
                for (int y = 0; y < this.getNumTilesY(directoryIndex); y++) {
                    for (int x = 0; x < this.getNumTilesX(directoryIndex); x++) {
                        int itemID = getItemIdForGridComponent(x, y, directoryIndex);
                        primary_item_dimg.addReference(itemID);
                    }
                }
                iref.addItem(primary_item_dimg);
            }
            {
                ContentDescribes cdsc = new ContentDescribes();
                cdsc.setFromItemId(FAKE_SECURITY_ITEM_ID);
                cdsc.addReference(MAIN_ITEM_ID + directoryIndex);
                iref.addItem(cdsc);
            }
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

    private MediaDataBox createMediaDataBox_rgb_grid_pymd() throws IOException {
        MediaDataBox mdat = new MediaDataBox();
        int numDirectories = directories.size();
        for (int directoryIndex = numDirectories - 1; directoryIndex >= 0; directoryIndex--) {
            FileDirectory directory = directories.get(directoryIndex);
            int width = directory.getImageWidth().intValue();
            int height = directory.getImageHeight().intValue();
            int numTilesX = this.getNumTilesX(directoryIndex);
            int numTilesY = this.getNumTilesY(directoryIndex);
            for (int y = 0; y < numTilesY; y++) {
                for (int x = 0; x < numTilesX; x++) {
                    byte[] tileBytes = directory.getTileOrStrip(x, y, 0);
                    mdat.appendData(tileBytes);
                }
            }
        }
        return mdat;
    }

    private int getItemIdForGridComponent(int x, int y, int z) {
        return (z + 1) * 1000 + y * this.getNumTilesX(z) + x + 1;
    }

    private byte[] getPyramidGridItemsAsBytes(boolean dump) throws IOException {
        int numDirectories = directories.size();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        for (int directoryIndex = numDirectories - 1; directoryIndex >= 0; directoryIndex--) {
            GridItem gridItem = getPyramidGridItem(directoryIndex);

            gridItem.writeTo(streamWriter);
        }
        return baos.toByteArray();
    }

    private byte[] getPyramidGridItemAsBytes(int directoryIndex) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        this.getPyramidGridItem(directoryIndex).writeTo(streamWriter);
        return baos.toByteArray();
    }

    protected GridItem getPyramidGridItem(int directoryIndex) {
        GridItem gridItem = new GridItem();
        gridItem.setRows(this.getNumTilesY(directoryIndex));
        gridItem.setColumns(this.getNumTilesX(directoryIndex));
        gridItem.setOutput_width(this.directories.get(directoryIndex).getImageWidth().intValue());
        gridItem.setOutput_height(this.directories.get(directoryIndex).getImageHeight().intValue());
        return gridItem;
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
        FileDirectory directory = directories.get(0);
        List<Double> pixelScale = directory.getModelPixelScale();
        List<Double> modelTiePoint = directory.getModelTiepoint();
        ModelTransformationProperty modelTransformation = new ModelTransformationProperty();
        assertEquals(pixelScale.size(), 3);
        assertEquals(pixelScale.get(2), 0.0);
        double sx = pixelScale.get(0);
        double sy = pixelScale.get(1);
        assertEquals(modelTiePoint.size(), 6);
        double tx = modelTiePoint.get(3) + modelTiePoint.get(0) / sx;
        double ty = modelTiePoint.get(4) + modelTiePoint.get(1) / sy;
        modelTransformation.setM00(sx);
        modelTransformation.setM01(0.0);
        modelTransformation.setM03(tx);
        modelTransformation.setM10(0.0);
        modelTransformation.setM11(-1.0 * sy);
        modelTransformation.setM13(ty);
        return modelTransformation;
    }

    private WellKnownText2Property makeWKT2Property() {
        WellKnownText2Property prop = new WellKnownText2Property();
        // TODO: make sure it is not hard coded
        prop.setWkt2(
                "PROJCRS[\"GDA94 / MGA zone 55\",BASEGEOGCRS[\"GDA94\",DATUM[\"Geocentric Datum of Australia 1994\",ELLIPSOID[\"GRS 1980\",6378137,298.257222101,LENGTHUNIT[\"metre\",1]]],PRIMEM[\"Greenwich\",0,ANGLEUNIT[\"degree\",0.0174532925199433]],ID[\"EPSG\",4283]],CONVERSION[\"Map Grid of Australia zone 55\",METHOD[\"Transverse Mercator\",ID[\"EPSG\",9807]],PARAMETER[\"Latitude of natural origin\",0,ANGLEUNIT[\"degree\",0.0174532925199433],ID[\"EPSG\",8801]],PARAMETER[\"Longitude of natural origin\",147,ANGLEUNIT[\"degree\",0.0174532925199433],ID[\"EPSG\",8802]],PARAMETER[\"Scale factor at natural origin\",0.9996,SCALEUNIT[\"unity\",1],ID[\"EPSG\",8805]],PARAMETER[\"False easting\",500000,LENGTHUNIT[\"metre\",1],ID[\"EPSG\",8806]],PARAMETER[\"False northing\",10000000,LENGTHUNIT[\"metre\",1],ID[\"EPSG\",8807]]],CS[Cartesian,2],AXIS[\"(E)\",east,ORDER[1],LENGTHUNIT[\"metre\",1]],AXIS[\"(N)\",north,ORDER[2],LENGTHUNIT[\"metre\",1]],USAGE[SCOPE[\"Engineering survey, topographic mapping.\"],AREA[\"Australia - onshore and offshore between 144°E and 150°E.\"],BBOX[-50.89,144,-9.23,150.01]],ID[\"EPSG\",28355]]");
        return prop;
    }

    private ImageSpatialExtentsProperty makeImageSpatialExtentsPropertyForLevel(
            int directoryIndex) {
        ImageSpatialExtentsProperty ispe = new ImageSpatialExtentsProperty();
        FileDirectory directory = directories.get(directoryIndex);
        ispe.setImageHeight(directory.getImageHeight().intValue());
        ispe.setImageWidth(directory.getImageWidth().intValue());
        return ispe;
    }

    private int getNumTilesX(int directoryIndex) {
        FileDirectory directory = directories.get(directoryIndex);
        int numTilesX =
                (directory.getImageWidth().intValue() + directory.getTileWidth().intValue() - 1)
                        / directory.getTileWidth().intValue();
        return numTilesX;
    }

    private int getNumTilesY(int directoryIndex) {
        FileDirectory directory = directories.get(directoryIndex);
        int numTilesY =
                (directory.getImageHeight().intValue() + directory.getTileHeight().intValue() - 1)
                        / directory.getTileHeight().intValue();
        return numTilesY;
    }
}
