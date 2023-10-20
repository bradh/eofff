package net.frogmouth.rnd.eofff.tools.gimi;

import static net.frogmouth.rnd.eofff.tools.gimi.GIMIValidator.dumpMisbMessage;
import static org.testng.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.PropertyAssociation;
import net.frogmouth.rnd.eofff.imagefileformat.properties.udes.UserDescriptionProperty;
import net.frogmouth.rnd.eofff.imagefileformat.properties.uuid.UUIDProperty;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FileParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.ftyp.Brand;
import net.frogmouth.rnd.eofff.isobmff.ftyp.FileTypeBox;
import net.frogmouth.rnd.eofff.isobmff.iinf.ItemInfoBox;
import net.frogmouth.rnd.eofff.isobmff.iloc.ILocExtent;
import net.frogmouth.rnd.eofff.isobmff.iloc.ILocItem;
import net.frogmouth.rnd.eofff.isobmff.iloc.ItemLocationBox;
import net.frogmouth.rnd.eofff.isobmff.infe.ItemInfoEntry;
import net.frogmouth.rnd.eofff.isobmff.iref.ItemReferenceBox;
import net.frogmouth.rnd.eofff.isobmff.iref.SingleItemReferenceBox;
import net.frogmouth.rnd.eofff.isobmff.mdat.MediaDataBox;
import net.frogmouth.rnd.eofff.isobmff.meta.MetaBox;
import net.frogmouth.rnd.eofff.isobmff.meta.MetaBoxBuilder;
import net.frogmouth.rnd.eofff.isobmff.pitm.PrimaryItemBox;
import net.frogmouth.rnd.eofff.tools.ItemDataBoxBuilder;
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
import org.testng.annotations.Test;

public class ConvertToGIMITest {

    private static final UUID PRIMARY_ITEM_CONTENT_ID_UUID = UUID.randomUUID();
    private static final String PRIMARY_ITEM_CONTENT_ID =
            "urn:uuid:" + PRIMARY_ITEM_CONTENT_ID_UUID;
    private static final UUID SECURITY_ITEM_CONTENT_ID_UUID = UUID.randomUUID();
    private static final String SECURITY_ITEM_CONTENT_ID =
            "urn:uuid:" + SECURITY_ITEM_CONTENT_ID_UUID;
    private static final UUID ST0601_ITEM_CONTENT_ID_UUID = UUID.randomUUID();
    private static final String ST0601_ITEM_CONTENT_ID = "urn:uuid:" + ST0601_ITEM_CONTENT_ID_UUID;

    private long fakeSecurityItemId;
    private final String FAKE_SECURITY_MIME_TYPE = "application/x.fake-dni-arh+xml";
    // TODO: we should have a proper XML implementation for this
    private final String FAKE_SECURITY_XML =
            """
            <?xml version="1.0" encoding="UTF-8"?>
            <FakeSecurity xmlns="http://www.opengis.net/CodeSprint2023Oct/Security">
                <FakeLevel>UNRESTRICTED</FakeLevel>
                <FakeCaveat>DOWN-UNDER</FakeCaveat>
                <FakeCaveat>EASY-AS BRO</FakeCaveat>
                <FakeRelTo>NZ</FakeRelTo>
                <FakeRelTo>AU</FakeRelTo>
                <FakeDeclassOn>2023-12-25</FakeDeclassOn>
            </FakeSecurity>""";

    private long generalMetadataItemId;
    private static final UUID CONTENT_ID_UUID =
            UUID.fromString("aac8ab7d-f519-5437-b7d3-c973d155e253");
    private static final String ST0601_URI = "urn:smpte:ul:060E2B34.020B0101.0E010301.01000000";
    private long highestItemId;
    private final int imageHeight;
    private final int imageWidth;
    private String path;
    private final FileDirectory directory;
    private final GeoTransform transform;

    public ConvertToGIMITest() throws IOException {
        path =
                "/home/bradh/gdal_hacks/ACT Government/Imagery/Aerial Photography/ACT2020_wgs_84_trimmed.tif";
        mil.nga.tiff.TIFFImage tiffImage = mil.nga.tiff.TiffReader.readTiff(new File(path));
        List<mil.nga.tiff.FileDirectory> directories = tiffImage.getFileDirectories();
        directory = directories.get(0);
        List<Double> pixelScale = directory.getModelPixelScale();
        List<Double> modelTiePoint = directory.getModelTiepoint();
        imageHeight = (int) directory.getImageHeight();
        imageWidth = (int) directory.getImageWidth();
        transform = new GeoTransform(modelTiePoint, pixelScale);
    }

    public List<Box> parseFile() throws IOException {
        Path testFile =
                Paths.get(
                        "/home/bradh/gdal_hacks/ACT Government/Imagery/Aerial Photography/ACT2020_wgs_84_trimmed.heic");
        FileParser fileParser = new FileParser();
        return fileParser.parse(testFile);
    }

    @Test
    public void hackBoxes() throws IOException {
        List<Box> sourceBoxes = new ArrayList<>();
        sourceBoxes.addAll(parseFile());
        FileTypeBox ftyp = (FileTypeBox) getTopLevelBoxByFourCC(sourceBoxes, "ftyp");
        ftyp.addCompatibleBrand(new Brand("geo1"));
        ftyp.addCompatibleBrand(new Brand("unif"));
        System.out.println(ftyp.toString());
        MetaBox meta = (MetaBox) getTopLevelBoxByFourCC(sourceBoxes, "meta");
        ItemLocationBox iloc = null;
        ItemInfoBox iinf = null;
        ItemPropertiesBox iprp = null;
        ItemReferenceBox iref = null;
        long primaryItem = 1;
        MetaBoxBuilder metaBoxBuilder = new MetaBoxBuilder();
        for (Box box : meta.getNestedBoxes()) {
            metaBoxBuilder.addNestedBox(box);
            if (box instanceof ItemLocationBox ilocBox) {
                iloc = ilocBox;
                iloc.setVersion(1); // TODO: we should handle this inside ItemLocationBox
                iloc.setIndexSize(4);
            }
            if (box instanceof ItemInfoBox iinfBox) {
                iinf = iinfBox;
            }
            if (box instanceof ItemPropertiesBox iprpBox) {
                iprp = iprpBox;
            }
            if (box instanceof PrimaryItemBox pitm) {
                primaryItem = pitm.getItemID();
            }
            if (box instanceof ItemReferenceBox irefBox) {
                iref = irefBox;
            }
        }
        getHighestItemId(iinf);
        if (iloc == null) {
            fail("iloc is needed");
            throw new UnsupportedOperationException("cannot hack boxes without iloc");
        }
        if (iinf == null) {
            fail("iinf is needed");
            throw new UnsupportedOperationException("cannot hack boxes without iinf");
        }
        if (iprp == null) {
            fail("iprp is needed");
            throw new UnsupportedOperationException("cannot hack boxes without iprp");
        }
        if (iref == null) {
            iref = new ItemReferenceBox();
            metaBoxBuilder.addNestedBox(iref);
        }

        ItemDataBoxBuilder itemDataBoxBuilder = new ItemDataBoxBuilder();
        itemDataBoxBuilder.addData(getFakeSecurityXMLBytes(true));
        itemDataBoxBuilder.addData(getST0601MetadataBytes(true));
        long ilocOffset = 0;
        {
            ItemInfoEntry fakeSecurityItem = new ItemInfoEntry();
            fakeSecurityItem.setVersion(2);
            fakeSecurityItemId = getNextItemId();
            fakeSecurityItem.setItemID(fakeSecurityItemId);
            FourCC mime_fourcc = new FourCC("mime");
            fakeSecurityItem.setItemType(mime_fourcc.asUnsigned());
            fakeSecurityItem.setContentType(FAKE_SECURITY_MIME_TYPE);
            fakeSecurityItem.setItemName("Security Marking (Fake XML)");
            iinf.addItem(fakeSecurityItem);
        }
        {
            ILocItem fakeSecurityItemLocation = new ILocItem();
            fakeSecurityItemLocation.setConstructionMethod(1);
            fakeSecurityItemLocation.setItemId(fakeSecurityItemId);
            fakeSecurityItemLocation.setBaseOffset(ilocOffset);
            ILocExtent securityExtent = new ILocExtent();
            securityExtent.setExtentIndex(0);
            securityExtent.setExtentOffset(0);
            securityExtent.setExtentLength(getFakeSecurityXMLBytes(false).length);
            ilocOffset += securityExtent.getExtentLength();
            fakeSecurityItemLocation.addExtent(securityExtent);
            iloc.addItem(fakeSecurityItemLocation);
        }
        {
            // copyright statement item
            int index = iprp.getItemProperties().addProperty(makeUserDescription_copyright());
            AssociationEntry entry = new AssociationEntry();
            entry.setItemId(primaryItem);
            PropertyAssociation association = new PropertyAssociation();
            association.setEssential(false);
            association.setPropertyIndex(index);
            entry.addAssociation(association);
            iprp.addAssociationEntry(entry);
        }
        {
            int index = iprp.getItemProperties().addProperty(makeContentIdPropertyPrimaryItem());
            AssociationEntry entry = new AssociationEntry();
            entry.setItemId(primaryItem);
            PropertyAssociation association = new PropertyAssociation();
            association.setEssential(false);
            association.setPropertyIndex(index);
            entry.addAssociation(association);
            iprp.addAssociationEntry(entry);
        }
        {
            ItemInfoEntry st0601MetadataItem = new ItemInfoEntry();
            st0601MetadataItem.setVersion(2);
            generalMetadataItemId = getNextItemId();
            st0601MetadataItem.setItemID(generalMetadataItemId);
            FourCC mime_fourcc = new FourCC("uri ");
            st0601MetadataItem.setItemType(mime_fourcc.asUnsigned());
            st0601MetadataItem.setItemUriType(ST0601_URI);
            st0601MetadataItem.setItemName("General Metadata (ST 0601)");
            iinf.addItem(st0601MetadataItem);
        }
        {
            ILocItem st0601MetadataItemLocation = new ILocItem();
            st0601MetadataItemLocation.setConstructionMethod(1);
            st0601MetadataItemLocation.setItemId(generalMetadataItemId);
            st0601MetadataItemLocation.setBaseOffset(ilocOffset);
            ILocExtent metadataExtent = new ILocExtent();
            metadataExtent.setExtentIndex(0);
            metadataExtent.setExtentOffset(0);
            metadataExtent.setExtentLength(getST0601MetadataBytes(false).length);
            ilocOffset += metadataExtent.getExtentLength();
            st0601MetadataItemLocation.addExtent(metadataExtent);
            iloc.addItem(st0601MetadataItemLocation);
        }
        {
            SingleItemReferenceBox st0601DescribedPrimaryItem =
                    new SingleItemReferenceBox(new FourCC("cdsc"));
            st0601DescribedPrimaryItem.setFromItemId(generalMetadataItemId);
            st0601DescribedPrimaryItem.addReference(primaryItem);
            iref.addItem(st0601DescribedPrimaryItem);
        }
        {
            int index = iprp.getItemProperties().addProperty(makeContentIdPropertySecurityItem());
            AssociationEntry entry = new AssociationEntry();
            entry.setItemId(this.fakeSecurityItemId);
            PropertyAssociation association = new PropertyAssociation();
            association.setEssential(false);
            association.setPropertyIndex(index);
            entry.addAssociation(association);
            iprp.addAssociationEntry(entry);
        }
        {
            int index =
                    iprp.getItemProperties()
                            .addProperty(makeContentIdPropertyGeneralMetadataItem());
            AssociationEntry entry = new AssociationEntry();
            entry.setItemId(this.generalMetadataItemId);
            PropertyAssociation association = new PropertyAssociation();
            association.setEssential(false);
            association.setPropertyIndex(index);
            entry.addAssociation(association);
            iprp.addAssociationEntry(entry);
        }
        metaBoxBuilder.addNestedBox(itemDataBoxBuilder.build());
        MetaBox outputMetaBox = metaBoxBuilder.build();
        MediaDataBox mdat = (MediaDataBox) getTopLevelBoxByFourCC(sourceBoxes, "mdat");
        long mdatOffset =
                ftyp.getSize() + outputMetaBox.getSize() + (mdat.getSize() - mdat.getBodySize());
        for (ILocItem item : iloc.getItems()) {
            if (item.getConstructionMethod() == 0) {
                item.setBaseOffset(mdatOffset);
            }
        }
        System.out.println(iloc.toString());

        List<Box> outputBoxes = new ArrayList<>();
        outputBoxes.add(ftyp);
        outputBoxes.add(outputMetaBox);
        outputBoxes.add(mdat);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        for (Box box : outputBoxes) {
            box.writeTo(streamWriter);
        }
        File testOut = new File("simple_gimi.heic");
        Files.write(testOut.toPath(), baos.toByteArray(), StandardOpenOption.CREATE);
    }

    private static Box getTopLevelBoxByFourCC(List<Box> boxes, String fourCC) {
        for (Box box : boxes) {
            if (box.getFourCC().equals(new FourCC(fourCC))) {
                return box;
            }
        }
        return null;
    }

    private UUIDProperty makeContentIdPropertyPrimaryItem() {
        UUIDProperty contentIdProperty = new UUIDProperty();
        contentIdProperty.setExtendedType(CONTENT_ID_UUID);
        contentIdProperty.setPayload(PRIMARY_ITEM_CONTENT_ID.getBytes(StandardCharsets.UTF_8));
        System.out.println(contentIdProperty.toString());
        return contentIdProperty;
    }

    private UUIDProperty makeContentIdPropertySecurityItem() {
        UUIDProperty contentIdProperty = new UUIDProperty();
        contentIdProperty.setExtendedType(CONTENT_ID_UUID);
        contentIdProperty.setPayload(SECURITY_ITEM_CONTENT_ID.getBytes(StandardCharsets.UTF_8));
        System.out.println(contentIdProperty.toString());
        return contentIdProperty;
    }

    private UUIDProperty makeContentIdPropertyGeneralMetadataItem() {
        UUIDProperty contentIdProperty = new UUIDProperty();
        contentIdProperty.setExtendedType(CONTENT_ID_UUID);
        contentIdProperty.setPayload(ST0601_ITEM_CONTENT_ID.getBytes(StandardCharsets.UTF_8));
        System.out.println(contentIdProperty.toString());
        return contentIdProperty;
    }

    private AbstractItemProperty makeUserDescription_copyright() {
        UserDescriptionProperty udes = new UserDescriptionProperty();
        udes.setLang("en-AU");
        udes.setDescriptiveName("Copyright Statement");
        udes.setDescription("CC-BY Australian Capital Territory and MetroMap");
        udes.setTags("copyright");
        return udes;
    }

    private byte[] getFakeSecurityXMLBytes(boolean dump) {
        byte[] securityXMLBytes = FAKE_SECURITY_XML.getBytes(StandardCharsets.UTF_8);
        return securityXMLBytes;
    }

    private long getNextItemId() {
        highestItemId += 1;
        return highestItemId;
    }

    private void getHighestItemId(ItemInfoBox iinf) {
        highestItemId = -1;
        for (ItemInfoEntry item : iinf.getItems()) {
            long id = item.getItemID();
            if (id > highestItemId) {
                highestItemId = id;
            }
        }
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
        // Pick a rough estimate - TODO
        LocalDateTime ldt = LocalDateTime.of(LocalDate.of(2020, 5, 15), LocalTime.of(2, 0, 0));
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
