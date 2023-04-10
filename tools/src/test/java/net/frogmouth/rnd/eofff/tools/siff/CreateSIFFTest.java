package net.frogmouth.rnd.eofff.tools.siff;

import static org.testng.Assert.*;

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
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
import net.frogmouth.rnd.eofff.imagefileformat.properties.udes.UserDescriptionProperty;
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

public class CreateSIFFTest extends UncompressedTestSupport {
    private static final Logger LOG = LoggerFactory.getLogger(CreateSIFFTest.class);
    private static final long REGION_ITEM_ID = 0x1788;
    private static final long FILE_METADATA_ITEM_ID = 0x1902;
    private static final String MIMD_URI = "urn:nsg:KLV:ul:060E2B34.02050101.0E010504.00000000";
    private static final long PRIMARY_TIMESTAMP_ITEM_ID = 0x1603;
    private static final String NANO_TIMESTAMP_URI =
            "urn:nsg:KLV:ul:060E2B34.02050101.0E010302.09000000";
    private static final long CORE_ID_ITEM_ID = 0x1204;
    private static final String CORE_ID_URI = "urn:nsg:KLV:ul:060E2B34.01010101.0E010405.03000000";

    private static final byte[] MIIS_UUID_BYTES =
            new byte[] {
                (byte) 0x36,
                (byte) 0x41,
                (byte) 0xa9,
                (byte) 0xe2,
                (byte) 0x62,
                (byte) 0x4f,
                (byte) 0x43,
                (byte) 0x46,
                (byte) 0x8f,
                (byte) 0x8e,
                (byte) 0x66,
                (byte) 0x6a,
                (byte) 0x9b,
                (byte) 0xaa,
                (byte) 0x08,
                (byte) 0xbd
            };
    private final UUID miisUuid;

    public CreateSIFFTest() {
        ByteBuffer byteBuffer = ByteBuffer.wrap(MIIS_UUID_BYTES);
        long high = byteBuffer.getLong();
        long low = byteBuffer.getLong();
        miisUuid = new UUID(high, low);
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
        writeBoxes(boxes, "test_siff_rgb_component_rgan.heif");
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

    private ItemInfoBox makeItemInfoBox_with_rgan() {
        ItemInfoBox iinf = makeItemInfoBox();
        {
            ItemInfoEntry infe1 = new ItemInfoEntry();
            infe1.setVersion(2);
            infe1.setItemID(REGION_ITEM_ID);
            FourCC rgan = new FourCC("rgan");
            infe1.setItemType(rgan.asUnsigned());
            infe1.setItemName("Region Annotation");
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

    private ItemLocationBox makeItemLocationBox_rgb_component_with_rgan() throws IOException {
        ItemLocationBox iloc = makeItemLocationBox_rgb3();
        {
            ILocItem regionItemLocation = new ILocItem();
            regionItemLocation.setConstructionMethod(1);
            regionItemLocation.setItemId(REGION_ITEM_ID);
            ILocExtent rganExtent = new ILocExtent();
            rganExtent.setExtentIndex(0);
            rganExtent.setExtentOffset(0);
            rganExtent.setExtentLength(getRegionAnnotationBytes().length);
            regionItemLocation.addExtent(rganExtent);
            iloc.addItem(regionItemLocation);
        }
        {
            ILocItem mimdItemLocation = new ILocItem();
            mimdItemLocation.setConstructionMethod(1);
            mimdItemLocation.setItemId(FILE_METADATA_ITEM_ID);
            ILocExtent mimdExtent = new ILocExtent();
            mimdExtent.setExtentIndex(0);
            mimdExtent.setExtentOffset(getRegionAnnotationBytes().length);
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
            nanoTimestampExtent.setExtentOffset(
                    getRegionAnnotationBytes().length + getFileMetadataBytes(false).length);
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
                    getRegionAnnotationBytes().length
                            + getFileMetadataBytes(false).length
                            + getNanoTimestampBytes(false).length);
            coreIdExtent.setExtentLength(this.getMiisCoreIdAsBytes(false).length);
            coreIdItemLocation.addExtent(coreIdExtent);
            iloc.addItem(coreIdItemLocation);
        }
        return iloc;
    }

    private ItemDataBox makeItemDataBox_with_rgan() throws IOException {
        ItemDataBoxBuilder itemDataBoxBuilder = new ItemDataBoxBuilder();
        itemDataBoxBuilder.addData(this.getRegionAnnotationBytes());
        itemDataBoxBuilder.addData(this.getFileMetadataBytes(true));
        itemDataBoxBuilder.addData(this.getNanoTimestampBytes(true));
        itemDataBoxBuilder.addData(this.getMiisCoreIdAsBytes(true));
        return itemDataBoxBuilder.build();
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
        {
            SingleItemReferenceBox rgan_csdc_primary_item =
                    new SingleItemReferenceBox(new FourCC("cdsc"));
            rgan_csdc_primary_item.setFromItemId(REGION_ITEM_ID);
            rgan_csdc_primary_item.addReference(MAIN_ITEM_ID);
            iref.addItem(rgan_csdc_primary_item);
        }
        {
            SingleItemReferenceBox timestamp_csdc_primary_item =
                    new SingleItemReferenceBox(new FourCC("cdsc"));
            timestamp_csdc_primary_item.setFromItemId(PRIMARY_TIMESTAMP_ITEM_ID);
            timestamp_csdc_primary_item.addReference(MAIN_ITEM_ID);
            iref.addItem(timestamp_csdc_primary_item);
        }
        {
            SingleItemReferenceBox coreId_csdc_primary_item =
                    new SingleItemReferenceBox(new FourCC("cdsc"));
            coreId_csdc_primary_item.setFromItemId(CORE_ID_ITEM_ID);
            coreId_csdc_primary_item.addReference(MAIN_ITEM_ID);
            iref.addItem(coreId_csdc_primary_item);
        }
        return iref;
    }

    private AbstractItemProperty makeUserDescription_rgan() {
        UserDescriptionProperty udes = new UserDescriptionProperty();
        udes.setLang("en-AU");
        udes.setDescriptiveName("Yellow square");
        udes.setDescription("The square in the third row, second column is yellow");
        udes.setTags("udes,yellow,single square");
        return udes;
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
}
