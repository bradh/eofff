package net.frogmouth.rnd.eofff.tools.gimi;

import static org.testng.Assert.assertTrue;

import jakarta.xml.bind.JAXBException;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;
import java.util.logging.Level;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.groups.AlternativesBox;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.groups.GroupsListBox;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.AssociationEntry;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemPropertiesBox;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemPropertyAssociation;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemPropertyContainerBox;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.PropertyAssociation;
import net.frogmouth.rnd.eofff.imagefileformat.properties.image.ImageSpatialExtentsProperty;
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
import net.frogmouth.rnd.eofff.sidd.SIDDParser;
import net.frogmouth.rnd.eofff.sidd.v2.gen.ImageCornersType;
import net.frogmouth.rnd.eofff.sidd.v2.gen.ImageCornersType.ICP;
import net.frogmouth.rnd.eofff.sidd.v2.gen.SIDD;
import net.frogmouth.rnd.eofff.tools.ItemDataBoxBuilder;
import net.frogmouth.rnd.eofff.tools.MediaDataBoxBuilder;
import net.frogmouth.rnd.eofff.uncompressed.cmpd.ComponentDefinition;
import net.frogmouth.rnd.eofff.uncompressed.cmpd.ComponentDefinitionBox;
import net.frogmouth.rnd.eofff.uncompressed.uncc.Component;
import net.frogmouth.rnd.eofff.uncompressed.uncc.ComponentFormat;
import net.frogmouth.rnd.eofff.uncompressed.uncc.Interleaving;
import net.frogmouth.rnd.eofff.uncompressed.uncc.SamplingType;
import net.frogmouth.rnd.eofff.uncompressed.uncc.UncompressedFrameConfigBox;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.common.NitfReader;
import org.codice.imaging.nitf.core.common.impl.NitfInputStreamReader;
import org.codice.imaging.nitf.core.dataextension.DataExtensionSegment;
import org.codice.imaging.nitf.core.header.impl.NitfParser;
import org.codice.imaging.nitf.core.image.ImageSegment;
import org.codice.imaging.nitf.core.impl.SlottedParseStrategy;
import org.jmisb.api.common.KlvParseException;
import org.jmisb.api.klv.BerDecoder;
import org.jmisb.api.klv.BerField;
import org.jmisb.api.klv.IKlvKey;
import org.jmisb.api.klv.IKlvValue;
import org.jmisb.api.klv.IMisbMessage;
import org.jmisb.api.klv.INestedKlvValue;
import org.jmisb.api.klv.UniversalLabel;
import org.jmisb.core.klv.ArrayUtils;
import org.jmisb.mimd.st1902.MimdId;
import org.jmisb.mimd.st1902.MimdIdReference;
import org.jmisb.mimd.st1903.MIMD;
import org.jmisb.mimd.st1903.MIMD_SecurityOptions;
import org.jmisb.mimd.st1903.MIMD_Version;
import org.jmisb.mimd.st1903.Security;
import org.jmisb.mimd.st1903.Security_Classification;
import org.jmisb.mimd.st1903.Security_ClassifyingMethod;
import org.jmisb.st0601.FullCornerLatitude;
import org.jmisb.st0601.FullCornerLongitude;
import org.jmisb.st0601.IUasDatalinkValue;
import org.jmisb.st0601.UasDatalinkMessage;
import org.jmisb.st0601.UasDatalinkTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

public class CreateGIMITest {

    private static final Logger LOG = LoggerFactory.getLogger(CreateGIMITest.class);

    private static final long MAIN_ITEM_ID = 10;
    private static final long CONTENT_ID_ITEM_ID = 12;
    private static final long SECURITY_ITEM_ID = 20;
    private static final long SECURITY_CONTENT_ID_ITEM_ID = 22;
    private static final long FAKE_SECURITY_ITEM_ID = 24;
    private static final long FAKE_SECURITY_CONTENT_ID_ITEM_ID = 26;
    private static final long SECURITY_GROUP_ID = 29;

    private static final long MIMD_METADATA_ITEM_ID = 30;
    private static final long MIMD_METADATA_CONTENT_ID_ITEM_ID = 32;
    private static final String MIMD_URI = "urn:smpte:ul:060E2B34.02050101.0E010504.00000000";
    private static final long ST0601_METADATA_ITEM_ID = 34;
    private static final long ST0601_METADATA_CONTENT_ID_ITEM_ID = 36;
    private static final String ST0601_URI = "urn:smpte:ul:060E2B34.020B0101.0E010301.01000000";

    private static final UUID IMAGE_CONTENT_ID_UUID = UUID.randomUUID();
    private static final String IMAGE_CONTENT_ID = "urn:uuid:" + IMAGE_CONTENT_ID_UUID.toString();

    private static final UUID MIMD_METADATA_CONTENT_ID_UUID = UUID.randomUUID();
    private static final String MIMD_METADATA_CONTENT_ID =
            "urn:uuid:" + MIMD_METADATA_CONTENT_ID_UUID;

    private static final UUID ST0601_METADATA_CONTENT_ID_UUID = UUID.randomUUID();
    private static final String ST0601_METADATA_CONTENT_ID =
            "urn:uuid:" + ST0601_METADATA_CONTENT_ID_UUID;

    private final String CONTENT_ITEM_URI_TYPE = "urn:uuid:aac8ab7d-f519-5437-b7d3-c973d155e253";

    private static final UUID SECURITY_CONTENT_ID_UUID = UUID.randomUUID();
    private static final String SECURITY_CONTENT_ID = "urn:uuid:" + SECURITY_CONTENT_ID_UUID;
    private final String ISM_SECURITY_MIME_TYPE = "application/dni-arh+xml";
    private final String SECURITY_XML =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                    + "<arh:ExternalSecurity ism:ownerProducer=\"USA\" ism:classification=\"U\" ism:resourceElement=\"false\" "
                    + "xmlns:ism=\"urn:us:gov:ic:ism\" xmlns:arh=\"urn:us:gov:ic:arh\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"/>";

    private static final UUID FAKE_SECURITY_CONTENT_ID_UUID = UUID.randomUUID();
    private static final String FAKE_SECURITY_CONTENT_ID =
            "urn:uuid:" + FAKE_SECURITY_CONTENT_ID_UUID;
    private final String FAKE_SECURITY_MIME_TYPE = "application/x-fake-security";
    private final String FAKE_SECURITY_XML =
            """
            <?xml version="1.0" encoding="UTF-8"?>
            <FakeSecurity xmlns="http://www.opengis.net/CodeSprint2023Oct/Security">
                <FakeLevel>SECRETIVE-ISH</FakeLevel>
                <FakeCaveat>GOOD IMAGERY</FakeCaveat>
                <FakeCaveat>CAPELLA</FakeCaveat>
                <FakeRelTo>UK</FakeRelTo>
                <FakeRelTo>AU</FakeRelTo>
                <FakeDeclassOn>2023-10-29</FakeDeclassOn>
            </FakeSecurity>""";

    protected static final int LENGTH_OF_FREEBOX_HEADER = 8;
    private static final int MDAT_START = 2500;
    private static final int MDAT_START_DATA = MDAT_START + 2 * Integer.BYTES;
    private SIDD sidd;

    private final SlottedParseStrategy parseStrategy;

    public CreateGIMITest() throws FileNotFoundException, NitfFormatException, JAXBException {
        parseStrategy = new SlottedParseStrategy(SlottedParseStrategy.ALL_SEGMENT_DATA);
        InputStream instream =
                new FileInputStream(
                        "/home/bradh/tempdata/CAPELLA_C02_SP_SIDD_HH_20210212074558_20210212074600.ntf");
        NitfReader reader = new NitfInputStreamReader(new BufferedInputStream(instream));
        NitfParser.parse(reader, parseStrategy);
        String siddXml = getDESXML();
        SIDDParser siddParser = new SIDDParser();
        sidd = siddParser.parse(siddXml);
    }

    @Test
    public void writeFile_from_SIDD() throws IOException, JAXBException {
        List<Box> boxes = new ArrayList<>();
        FileTypeBox ftyp = createFileTypeBox();
        boxes.add(ftyp);
        MetaBox meta = createMetaBox();
        boxes.add(meta);
        long lengthOfPreviousBoxes = ftyp.getSize() + meta.getSize();
        long numberOfFillBytes = MDAT_START - lengthOfPreviousBoxes - LENGTH_OF_FREEBOX_HEADER;
        FreeBox free = new FreeBox();
        free.setData(new byte[(int) numberOfFillBytes]);
        boxes.add(free);
        MediaDataBox mdat = createMediaDataBox();
        boxes.add(mdat);
        writeBoxes(boxes, "test_gimi_sidd.heif");
    }

    protected void writeBoxes(List<Box> boxes, String outputPathName) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        for (Box box : boxes) {
            if (box instanceof FileTypeBox ftyp) {
                validateFileTypeBox(ftyp);
            }
            if (box instanceof MetaBox metaBox) {
                // validateMetaBox(metaBox);
            }
            box.writeTo(streamWriter);
        }
        File testOut = new File(outputPathName);
        Files.write(testOut.toPath(), baos.toByteArray(), StandardOpenOption.CREATE);
    }

    protected void validateFileTypeBox(FileTypeBox ftyp) {
        // TODO: check these
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

    protected FileTypeBox createFileTypeBox() {
        FileTypeBox fileTypeBox = new FileTypeBox(new FourCC("ftyp"));
        fileTypeBox.setMajorBrand(new Brand("mif1"));
        fileTypeBox.setMinorVersion(0);
        fileTypeBox.addCompatibleBrand(new Brand("mif1"));
        fileTypeBox.addCompatibleBrand(new Brand("mif2"));
        fileTypeBox.addCompatibleBrand(new Brand("ns01"));
        fileTypeBox.addCompatibleBrand(new Brand("unif"));
        return fileTypeBox;
    }

    private MetaBox createMetaBox() throws IOException, JAXBException {
        MetaBox meta = new MetaBox();
        List<Box> boxes = new ArrayList<>();
        boxes.add(makeHandlerBox());
        boxes.add(makePrimaryItemBox());
        boxes.add(makeItemInfoBox());
        boxes.add(makeItemLocationBox());
        boxes.add(makeItemDataBox());
        boxes.add(makeItemPropertiesBox());
        boxes.add(makeItemReferenceBox());
        boxes.add(makeGroupsListBox());
        meta.addNestedBoxes(boxes);
        return meta;
    }

    protected HandlerBox makeHandlerBox() {
        HandlerBox hdlr = new HandlerBox();
        hdlr.setHandlerType("pict");
        hdlr.setName("");
        return hdlr;
    }

    protected PrimaryItemBox makePrimaryItemBox() {
        PrimaryItemBox pitm = new PrimaryItemBox();
        pitm.setItemID(MAIN_ITEM_ID);
        return pitm;
    }

    private ItemInfoBox makeItemInfoBox() {
        ItemInfoBox iinf = new ItemInfoBox();
        {
            ItemInfoEntry infe1 = new ItemInfoEntry();
            infe1.setVersion(2);
            infe1.setItemID(MAIN_ITEM_ID);
            FourCC unci = new FourCC("unci");
            infe1.setItemType(unci.asUnsigned());
            infe1.setItemName("Primary item");
            iinf.addItem(infe1);
        }
        {
            ItemInfoEntry infe2 = new ItemInfoEntry();
            infe2.setVersion(2);
            infe2.setItemID(CONTENT_ID_ITEM_ID);
            FourCC uri_fourcc = new FourCC("uri ");
            infe2.setItemType(uri_fourcc.asUnsigned());
            infe2.setItemUriType(CONTENT_ITEM_URI_TYPE);
            infe2.setItemName("Main item ContentID");
            iinf.addItem(infe2);
        }
        {
            ItemInfoEntry infe3 = new ItemInfoEntry();
            infe3.setVersion(2);
            infe3.setItemID(SECURITY_ITEM_ID);
            FourCC mime_fourcc = new FourCC("mime");
            infe3.setItemType(mime_fourcc.asUnsigned());
            infe3.setContentType(ISM_SECURITY_MIME_TYPE);
            infe3.setItemName("Security Marking (ISM.XML)");
            iinf.addItem(infe3);
        }
        {
            ItemInfoEntry infe4 = new ItemInfoEntry();
            infe4.setVersion(2);
            infe4.setItemID(SECURITY_CONTENT_ID_ITEM_ID);
            FourCC uri_fourcc = new FourCC("uri ");
            infe4.setItemType(uri_fourcc.asUnsigned());
            infe4.setItemUriType(CONTENT_ITEM_URI_TYPE);
            infe4.setItemName("Security item ContentID");
            iinf.addItem(infe4);
        }
        {
            ItemInfoEntry infe5 = new ItemInfoEntry();
            infe5.setVersion(2);
            infe5.setItemID(MIMD_METADATA_ITEM_ID);
            FourCC uri_fourcc = new FourCC("uri ");
            infe5.setItemType(uri_fourcc.asUnsigned());
            infe5.setItemUriType(MIMD_URI);
            infe5.setItemName("General Metadata (MIMD)");
            iinf.addItem(infe5);
        }
        {
            ItemInfoEntry infe6 = new ItemInfoEntry();
            infe6.setVersion(2);
            infe6.setItemID(MIMD_METADATA_CONTENT_ID_ITEM_ID);
            FourCC uri_fourcc = new FourCC("uri ");
            infe6.setItemType(uri_fourcc.asUnsigned());
            infe6.setItemUriType(CONTENT_ITEM_URI_TYPE);
            infe6.setItemName("General Metadata (MIMD) item ContentID");
            iinf.addItem(infe6);
        }
        {
            ItemInfoEntry infe7 = new ItemInfoEntry();
            infe7.setVersion(2);
            infe7.setItemID(ST0601_METADATA_ITEM_ID);
            FourCC uri_fourcc = new FourCC("uri ");
            infe7.setItemType(uri_fourcc.asUnsigned());
            infe7.setItemUriType(ST0601_URI);
            infe7.setItemName("General Metadata (ST 0601)");
            iinf.addItem(infe7);
        }
        {
            ItemInfoEntry infe8 = new ItemInfoEntry();
            infe8.setVersion(2);
            infe8.setItemID(ST0601_METADATA_CONTENT_ID_ITEM_ID);
            FourCC uri_fourcc = new FourCC("uri ");
            infe8.setItemType(uri_fourcc.asUnsigned());
            infe8.setItemUriType(CONTENT_ITEM_URI_TYPE);
            infe8.setItemName("General Metadata (ST 0601) item ContentID");
            iinf.addItem(infe8);
        }
        {
            ItemInfoEntry infe9 = new ItemInfoEntry();
            infe9.setVersion(2);
            infe9.setItemID(FAKE_SECURITY_ITEM_ID);
            FourCC mime_fourcc = new FourCC("mime");
            infe9.setItemType(mime_fourcc.asUnsigned());
            infe9.setContentType(FAKE_SECURITY_MIME_TYPE);
            infe9.setItemName("Security Marking (Fake XML)");
            iinf.addItem(infe9);
        }
        {
            ItemInfoEntry infe10 = new ItemInfoEntry();
            infe10.setVersion(2);
            infe10.setItemID(FAKE_SECURITY_CONTENT_ID_ITEM_ID);
            FourCC uri_fourcc = new FourCC("uri ");
            infe10.setItemType(uri_fourcc.asUnsigned());
            infe10.setItemUriType(CONTENT_ITEM_URI_TYPE);
            infe10.setItemName("Fake Security item ContentID");
            iinf.addItem(infe10);
        }
        return iinf;
    }

    private ItemLocationBox makeItemLocationBox() throws IOException, JAXBException {
        ItemLocationBox iloc = new ItemLocationBox();
        iloc.setOffsetSize(4);
        iloc.setLengthSize(4);
        iloc.setBaseOffsetSize(4);
        iloc.setIndexSize(4);
        iloc.setVersion(1);
        {
            ImageSegment is = parseStrategy.getDataSource().getImageSegments().get(0);
            ILocItem mainItemLocation = new ILocItem();
            mainItemLocation.setConstructionMethod(0);
            mainItemLocation.setItemId(MAIN_ITEM_ID);
            ILocExtent mainItemExtent = new ILocExtent();
            mainItemExtent.setExtentIndex(0);
            mainItemExtent.setExtentOffset(MDAT_START_DATA);
            mainItemExtent.setExtentLength(is.getDataLength());
            mainItemLocation.addExtent(mainItemExtent);
            iloc.addItem(mainItemLocation);
        }
        long offset = 0;
        {
            ILocItem contentIdLocation = new ILocItem();
            contentIdLocation.setConstructionMethod(1);
            contentIdLocation.setItemId(CONTENT_ID_ITEM_ID);
            ILocExtent contentIdExtent = new ILocExtent();
            contentIdExtent.setExtentIndex(0);
            contentIdExtent.setExtentOffset(offset);
            contentIdExtent.setExtentLength(getImageContentIdBytes(false).length);
            offset += contentIdExtent.getExtentLength();
            contentIdLocation.addExtent(contentIdExtent);
            iloc.addItem(contentIdLocation);
        }
        {
            ILocItem securityItemLocation = new ILocItem();
            securityItemLocation.setConstructionMethod(1);
            securityItemLocation.setItemId(SECURITY_ITEM_ID);
            ILocExtent securityExtent = new ILocExtent();
            securityExtent.setExtentIndex(0);
            securityExtent.setExtentOffset(offset);
            securityExtent.setExtentLength(getSecurityXMLBytes(false).length);
            offset += securityExtent.getExtentLength();
            securityItemLocation.addExtent(securityExtent);
            iloc.addItem(securityItemLocation);
        }
        {
            ILocItem securityContentIdLocation = new ILocItem();
            securityContentIdLocation.setConstructionMethod(1);
            securityContentIdLocation.setItemId(SECURITY_CONTENT_ID_ITEM_ID);
            ILocExtent contentIdExtent = new ILocExtent();
            contentIdExtent.setExtentIndex(0);
            contentIdExtent.setExtentOffset(offset);
            contentIdExtent.setExtentLength(getSecurityContentIdBytes(false).length);
            offset += contentIdExtent.getExtentLength();
            securityContentIdLocation.addExtent(contentIdExtent);
            iloc.addItem(securityContentIdLocation);
        }
        {
            ILocItem fakeSecurityItemLocation = new ILocItem();
            fakeSecurityItemLocation.setConstructionMethod(1);
            fakeSecurityItemLocation.setItemId(FAKE_SECURITY_ITEM_ID);
            ILocExtent securityExtent = new ILocExtent();
            securityExtent.setExtentIndex(0);
            securityExtent.setExtentOffset(offset);
            securityExtent.setExtentLength(getFakeSecurityXMLBytes(false).length);
            offset += securityExtent.getExtentLength();
            fakeSecurityItemLocation.addExtent(securityExtent);
            iloc.addItem(fakeSecurityItemLocation);
        }
        {
            ILocItem fakeSecurityContentIdLocation = new ILocItem();
            fakeSecurityContentIdLocation.setConstructionMethod(1);
            fakeSecurityContentIdLocation.setItemId(FAKE_SECURITY_CONTENT_ID_ITEM_ID);
            ILocExtent contentIdExtent = new ILocExtent();
            contentIdExtent.setExtentIndex(0);
            contentIdExtent.setExtentOffset(offset);
            contentIdExtent.setExtentLength(getFakeSecurityContentIdBytes(false).length);
            offset += contentIdExtent.getExtentLength();
            fakeSecurityContentIdLocation.addExtent(contentIdExtent);
            iloc.addItem(fakeSecurityContentIdLocation);
        }
        {
            ILocItem mimdItemLocation = new ILocItem();
            mimdItemLocation.setConstructionMethod(1);
            mimdItemLocation.setItemId(MIMD_METADATA_ITEM_ID);
            ILocExtent mimdExtent = new ILocExtent();
            mimdExtent.setExtentIndex(0);
            mimdExtent.setExtentOffset(offset);
            mimdExtent.setExtentLength(getMIMDMetadataBytes(false).length);
            offset += mimdExtent.getExtentLength();
            mimdItemLocation.addExtent(mimdExtent);
            iloc.addItem(mimdItemLocation);
        }
        {
            ILocItem metadataContentIdLocation = new ILocItem();
            metadataContentIdLocation.setConstructionMethod(1);
            metadataContentIdLocation.setItemId(MIMD_METADATA_CONTENT_ID_ITEM_ID);
            ILocExtent contentIdExtent = new ILocExtent();
            contentIdExtent.setExtentIndex(0);
            contentIdExtent.setExtentOffset(offset);
            contentIdExtent.setExtentLength(getMIMDMetadataContentIdBytes(false).length);
            offset += contentIdExtent.getExtentLength();
            metadataContentIdLocation.addExtent(contentIdExtent);
            iloc.addItem(metadataContentIdLocation);
        }
        {
            ILocItem st0601ItemLocation = new ILocItem();
            st0601ItemLocation.setConstructionMethod(1);
            st0601ItemLocation.setItemId(ST0601_METADATA_ITEM_ID);
            ILocExtent st0601extent = new ILocExtent();
            st0601extent.setExtentIndex(0);
            st0601extent.setExtentOffset(offset);
            st0601extent.setExtentLength(getST0601MetadataBytes(false).length);
            offset += st0601extent.getExtentLength();
            st0601ItemLocation.addExtent(st0601extent);
            iloc.addItem(st0601ItemLocation);
        }
        {
            ILocItem metadataContentIdLocation = new ILocItem();
            metadataContentIdLocation.setConstructionMethod(1);
            metadataContentIdLocation.setItemId(ST0601_METADATA_CONTENT_ID_ITEM_ID);
            ILocExtent contentIdExtent = new ILocExtent();
            contentIdExtent.setExtentIndex(0);
            contentIdExtent.setExtentOffset(offset);
            contentIdExtent.setExtentLength(getST0601MetadataContentIdBytes(false).length);
            offset += contentIdExtent.getExtentLength();
            metadataContentIdLocation.addExtent(contentIdExtent);
            iloc.addItem(metadataContentIdLocation);
        }
        return iloc;
    }

    private ItemDataBox makeItemDataBox() throws IOException, JAXBException {
        ItemDataBoxBuilder itemDataBoxBuilder = new ItemDataBoxBuilder();
        itemDataBoxBuilder.addData(this.getImageContentIdBytes(false));
        itemDataBoxBuilder.addData(this.getSecurityXMLBytes(false));
        itemDataBoxBuilder.addData(this.getSecurityContentIdBytes(false));
        itemDataBoxBuilder.addData(this.getFakeSecurityXMLBytes(false));
        itemDataBoxBuilder.addData(this.getFakeSecurityContentIdBytes(false));
        itemDataBoxBuilder.addData(this.getMIMDMetadataBytes(false));
        itemDataBoxBuilder.addData(this.getMIMDMetadataContentIdBytes(false));
        itemDataBoxBuilder.addData(this.getST0601MetadataBytes(false));
        itemDataBoxBuilder.addData(this.getST0601MetadataContentIdBytes(false));
        // TODO: what else?
        return itemDataBoxBuilder.build();
    }

    private Box makeItemPropertiesBox() {
        ItemPropertiesBox iprp = new ItemPropertiesBox();
        ImageSegment is = parseStrategy.getDataSource().getImageSegments().get(0);
        ItemPropertyContainerBox ipco = new ItemPropertyContainerBox();
        ipco.addProperty(makeComponentDefinitionBox());
        ipco.addProperty(makeUncompressedFrameConfigBox());
        ipco.addProperty(makeImageSpatialExtentsProperty(is));
        iprp.setItemProperties(ipco);
        {
            ItemPropertyAssociation assoc = new ItemPropertyAssociation();
            AssociationEntry entry = new AssociationEntry();
            entry.setItemId(MAIN_ITEM_ID);
            {
                PropertyAssociation associationToComponentDefinitionBox = new PropertyAssociation();
                associationToComponentDefinitionBox.setPropertyIndex(1);
                associationToComponentDefinitionBox.setEssential(true);
                entry.addAssociation(associationToComponentDefinitionBox);
            }
            {
                PropertyAssociation associationToUncompressedFrameConfigBox =
                        new PropertyAssociation();
                associationToUncompressedFrameConfigBox.setPropertyIndex(2);
                associationToUncompressedFrameConfigBox.setEssential(true);
                entry.addAssociation(associationToUncompressedFrameConfigBox);
            }
            {
                PropertyAssociation associationToImageSpatialExtentsProperty =
                        new PropertyAssociation();
                associationToImageSpatialExtentsProperty.setPropertyIndex(3);
                associationToImageSpatialExtentsProperty.setEssential(true);
                entry.addAssociation(associationToImageSpatialExtentsProperty);
            }
            assoc.addEntry(entry);
            iprp.addItemPropertyAssociation(assoc);
        }
        return iprp;
    }

    private ImageSpatialExtentsProperty makeImageSpatialExtentsProperty(ImageSegment is) {
        ImageSpatialExtentsProperty ispe = new ImageSpatialExtentsProperty();
        ispe.setImageHeight(is.getNumberOfRows());
        ispe.setImageWidth(is.getNumberOfColumns());
        return ispe;
    }

    private Box makeItemReferenceBox() {
        ItemReferenceBox iref = new ItemReferenceBox();
        {
            SingleItemReferenceBox content_id_cdsc = new SingleItemReferenceBox(new FourCC("cdsc"));
            content_id_cdsc.setFromItemId(CONTENT_ID_ITEM_ID);
            content_id_cdsc.addReference(MAIN_ITEM_ID);
            iref.addItem(content_id_cdsc);
        }
        {
            SingleItemReferenceBox security_content_id_cdsc =
                    new SingleItemReferenceBox(new FourCC("cdsc"));
            security_content_id_cdsc.setFromItemId(SECURITY_CONTENT_ID_ITEM_ID);
            security_content_id_cdsc.addReference(SECURITY_ITEM_ID);
            iref.addItem(security_content_id_cdsc);
        }
        {
            SingleItemReferenceBox fake_security_content_id_cdsc =
                    new SingleItemReferenceBox(new FourCC("cdsc"));
            fake_security_content_id_cdsc.setFromItemId(FAKE_SECURITY_CONTENT_ID_ITEM_ID);
            fake_security_content_id_cdsc.addReference(FAKE_SECURITY_ITEM_ID);
            iref.addItem(fake_security_content_id_cdsc);
        }
        {
            SingleItemReferenceBox mimd_metadata_content_id_cdsc =
                    new SingleItemReferenceBox(new FourCC("cdsc"));
            mimd_metadata_content_id_cdsc.setFromItemId(MIMD_METADATA_CONTENT_ID_ITEM_ID);
            mimd_metadata_content_id_cdsc.addReference(MIMD_METADATA_ITEM_ID);
            iref.addItem(mimd_metadata_content_id_cdsc);
        }
        {
            SingleItemReferenceBox st0601_metadata_content_id_cdsc =
                    new SingleItemReferenceBox(new FourCC("cdsc"));
            st0601_metadata_content_id_cdsc.setFromItemId(ST0601_METADATA_CONTENT_ID_ITEM_ID);
            st0601_metadata_content_id_cdsc.addReference(ST0601_METADATA_ITEM_ID);
            iref.addItem(st0601_metadata_content_id_cdsc);
        }
        {
            SingleItemReferenceBox st0601_metadata_cdsc =
                    new SingleItemReferenceBox(new FourCC("cdsc"));
            st0601_metadata_cdsc.setFromItemId(ST0601_METADATA_ITEM_ID);
            st0601_metadata_cdsc.addReference(MAIN_ITEM_ID);
            iref.addItem(st0601_metadata_cdsc);
        }
        {
            SingleItemReferenceBox mimd_metadata_cdsc =
                    new SingleItemReferenceBox(new FourCC("cdsc"));
            mimd_metadata_cdsc.setFromItemId(MIMD_METADATA_ITEM_ID);
            mimd_metadata_cdsc.addReference(MAIN_ITEM_ID);
            iref.addItem(mimd_metadata_cdsc);
        }
        return iref;
    }

    private ComponentDefinitionBox makeComponentDefinitionBox() {
        ComponentDefinitionBox cmpd = new ComponentDefinitionBox();
        ComponentDefinition monoComponent = new ComponentDefinition(0, null);
        cmpd.addComponentDefinition(monoComponent);
        return cmpd;
    }

    private UncompressedFrameConfigBox makeUncompressedFrameConfigBox() {
        UncompressedFrameConfigBox uncc = new UncompressedFrameConfigBox();
        uncc.setProfile(new FourCC("gene"));
        uncc.addComponent(new Component(0, 7, ComponentFormat.UnsignedInteger, 0));
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

    private MediaDataBox createMediaDataBox() throws IOException {
        MediaDataBoxBuilder mdatBuilder = new MediaDataBoxBuilder();
        for (ImageSegment is : parseStrategy.getDataSource().getImageSegments()) {
            long dataLength = is.getDataLength();
            byte[] data = new byte[(int) dataLength];
            is.getData().readFully(data);
            mdatBuilder.addData(data);
        }
        return mdatBuilder.build();
    }

    private byte[] getImageContentIdBytes(boolean dump) {
        byte[] contentIdBytes = IMAGE_CONTENT_ID.getBytes(StandardCharsets.UTF_8);
        return contentIdBytes;
    }

    private byte[] getSecurityXMLBytes(boolean dump) {
        byte[] securityXMLBytes = SECURITY_XML.getBytes(StandardCharsets.UTF_8);
        return securityXMLBytes;
    }

    private byte[] getFakeSecurityXMLBytes(boolean dump) {
        byte[] securityXMLBytes = FAKE_SECURITY_XML.getBytes(StandardCharsets.UTF_8);
        return securityXMLBytes;
    }

    private byte[] getSecurityContentIdBytes(boolean dump) {
        byte[] contentIdBytes = SECURITY_CONTENT_ID.getBytes(StandardCharsets.UTF_8);
        return contentIdBytes;
    }

    private byte[] getFakeSecurityContentIdBytes(boolean dump) {
        byte[] contentIdBytes = FAKE_SECURITY_CONTENT_ID.getBytes(StandardCharsets.UTF_8);
        return contentIdBytes;
    }

    private byte[] getMIMDMetadataContentIdBytes(boolean dump) {
        byte[] contentIdBytes = MIMD_METADATA_CONTENT_ID.getBytes(StandardCharsets.UTF_8);
        return contentIdBytes;
    }

    private byte[] getST0601MetadataContentIdBytes(boolean dump) {
        byte[] contentIdBytes = ST0601_METADATA_CONTENT_ID.getBytes(StandardCharsets.UTF_8);
        return contentIdBytes;
    }

    private byte[] getMIMDMetadataBytes(boolean dump) throws IOException {
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

    private String getDESXML() {
        DataExtensionSegment des = parseStrategy.getDataSource().getDataExtensionSegments().get(0);
        byte[] desData = new byte[(int) des.getDataLength()];
        final StringBuilder xml = new StringBuilder();
        des.consume(
                (x) -> {
                    try {
                        x.readFully(desData);
                        String s = new String(desData, StandardCharsets.UTF_8);
                        xml.append(s);
                    } catch (IOException ex) {
                        java.util.logging.Logger.getLogger(CreateGIMITest.class.getName())
                                .log(Level.SEVERE, null, ex);
                    }
                });
        return xml.toString();
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

    private byte[] getST0601MetadataBytes(boolean dump) throws IOException, JAXBException {
        SortedMap<UasDatalinkTag, IUasDatalinkValue> map = new TreeMap<>();
        map.put(UasDatalinkTag.UasLdsVersionNumber, new org.jmisb.st0601.ST0601Version((short) 19));
        ImageCornersType imageCornersType = sidd.getGeoData().getImageCorners();
        for (ICP corner : imageCornersType.getICP()) {
            if (corner.getIndex().contains("FRFC")) {
                map.put(
                        UasDatalinkTag.CornerLatPt1,
                        new FullCornerLatitude(corner.getLat(), FullCornerLatitude.CORNER_LAT_1));
                map.put(
                        UasDatalinkTag.CornerLonPt1,
                        new FullCornerLongitude(corner.getLon(), FullCornerLongitude.CORNER_LON_1));
            }
            if (corner.getIndex().contains("FRLC")) {
                map.put(
                        UasDatalinkTag.CornerLatPt2,
                        new FullCornerLatitude(corner.getLat(), FullCornerLatitude.CORNER_LAT_2));
                map.put(
                        UasDatalinkTag.CornerLonPt2,
                        new FullCornerLongitude(corner.getLon(), FullCornerLongitude.CORNER_LON_2));
            }
            if (corner.getIndex().contains("LRLC")) {
                map.put(
                        UasDatalinkTag.CornerLatPt3,
                        new FullCornerLatitude(corner.getLat(), FullCornerLatitude.CORNER_LAT_3));
                map.put(
                        UasDatalinkTag.CornerLonPt3,
                        new FullCornerLongitude(corner.getLon(), FullCornerLongitude.CORNER_LON_3));
            }
            if (corner.getIndex().contains("LRFC")) {
                map.put(
                        UasDatalinkTag.CornerLatPt4,
                        new FullCornerLatitude(corner.getLat(), FullCornerLatitude.CORNER_LAT_4));
                map.put(
                        UasDatalinkTag.CornerLonPt4,
                        new FullCornerLongitude(corner.getLon(), FullCornerLongitude.CORNER_LON_4));
            }
        }
        // TODO: timestamp
        // TODO: mission id
        // TODO: sensor lat / lon
        // TODO: sensor elevation - probably out of range.

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

    private GroupsListBox makeGroupsListBox() {
        GroupsListBox grpl = new GroupsListBox();
        List<Box> entityGroups = new ArrayList<>();
        entityGroups.add(makeSecurityAlternativesGroup());
        grpl.addNestedBoxes(entityGroups);
        return grpl;
    }

    private AlternativesBox makeSecurityAlternativesGroup() {
        AlternativesBox altr = new AlternativesBox();
        altr.setGroupId(SECURITY_GROUP_ID);
        altr.addEntity(SECURITY_ITEM_ID);
        altr.addEntity(FAKE_SECURITY_ITEM_ID);
        return altr;
    }
}
