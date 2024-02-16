package net.frogmouth.rnd.eofff.demos;

import static org.testng.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.frogmouth.rnd.eofff.imagefileformat.properties.image.ImageSpatialExtentsProperty;
import net.frogmouth.rnd.eofff.imagefileformat.properties.uuid.UUIDProperty;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.dinf.DataInformationBox;
import net.frogmouth.rnd.eofff.isobmff.dref.DataEntryUrlBox;
import net.frogmouth.rnd.eofff.isobmff.dref.DataReferenceBox;
import net.frogmouth.rnd.eofff.isobmff.ftyp.Brand;
import net.frogmouth.rnd.eofff.isobmff.ftyp.FileTypeBox;
import net.frogmouth.rnd.eofff.isobmff.hdlr.HandlerBox;
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
import net.frogmouth.rnd.eofff.isobmff.meta.MetaBox;
import net.frogmouth.rnd.eofff.isobmff.pitm.PrimaryItemBox;
import net.frogmouth.rnd.eofff.uncompressed.cmpd.ComponentDefinition;
import net.frogmouth.rnd.eofff.uncompressed.cmpd.ComponentDefinitionBox;
import net.frogmouth.rnd.eofff.uncompressed.uncc.Component;
import net.frogmouth.rnd.eofff.uncompressed.uncc.ComponentFormat;
import net.frogmouth.rnd.eofff.uncompressed.uncc.Interleaving;
import net.frogmouth.rnd.eofff.uncompressed.uncc.SamplingType;
import net.frogmouth.rnd.eofff.uncompressed.uncc.UncompressedFrameConfigBox;
import org.testng.annotations.Test;

public class WrapHDF5ImageTest {

    private static final int MAIN_ITEM_ID = 10;
    private static final UUID PRIMARY_ITEM_CONTENT_ID_UUID = UUID.randomUUID();
    private static final String PRIMARY_ITEM_CONTENT_ID =
            "urn:uuid:" + PRIMARY_ITEM_CONTENT_ID_UUID;

    private static final int IMAGE_WIDTH = 4032;
    private static final int IMAGE_HEIGHT = 3024;
    private static final int NUM_BYTES_PER_PIXEL = 3;

    private static final UUID CONTENT_ID_UUID =
            UUID.fromString("aac8ab7d-f519-5437-b7d3-c973d155e253");

    @Test
    public void wrapHDF() throws IOException {
        String sourceFile = "demo_image2.h5";
        String wrapFile = "h5_wrap.heif";
        doWrapping(sourceFile, wrapFile);
    }

    private void doWrapping(String sourceFile, String wrapFile) throws IOException {
        List<Box> boxes = new ArrayList<>();
        FileTypeBox ftyp = createFileTypeBox();
        boxes.add(ftyp);
        MetaBox meta = createMetaBox(sourceFile);
        boxes.add(meta);
        writeBoxes(boxes, wrapFile);
    }

    protected FileTypeBox createFileTypeBox() {
        FileTypeBox fileTypeBox = new FileTypeBox();
        fileTypeBox.setMajorBrand(new Brand("mif1"));
        fileTypeBox.setMinorVersion(0);
        fileTypeBox.addCompatibleBrand(new Brand("mif1"));
        fileTypeBox.addCompatibleBrand(new Brand("mif2"));
        fileTypeBox.addCompatibleBrand(new Brand("unif"));
        return fileTypeBox;
    }

    private MetaBox createMetaBox(String sourceFile) throws IOException {
        MetaBox meta = new MetaBox();
        List<Box> boxes = new ArrayList<>();
        boxes.add(makeHandlerBox());
        boxes.add(makePrimaryItemBox());
        boxes.add(makeDataInformationBox(sourceFile));
        boxes.add(makeItemInfoBox());
        boxes.add(makeItemLocationBox());
        boxes.add(makeItemPropertiesBox());
        boxes.add(makeItemReferenceBox());
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

    protected DataInformationBox makeDataInformationBox(String dataFileName) {
        DataInformationBox dinf = new DataInformationBox();
        DataReferenceBox dref = new DataReferenceBox();
        DataEntryUrlBox url = new DataEntryUrlBox();
        url.setLocation(dataFileName);
        dref.addDataReference(url);
        dinf.appendNestedBox(dref);
        return dinf;
    }

    protected ItemInfoBox makeItemInfoBox() {
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

    private ItemLocationBox makeItemLocationBox() {
        ItemLocationBox iloc = new ItemLocationBox();
        iloc.setOffsetSize(4);
        iloc.setLengthSize(4);
        iloc.setBaseOffsetSize(4);
        iloc.setIndexSize(4);
        iloc.setVersion(1);
        ILocItem mainItemLocation = new ILocItem();
        mainItemLocation.setConstructionMethod(2);
        mainItemLocation.setDataReferenceIndex(1);
        mainItemLocation.setItemId(MAIN_ITEM_ID);
        mainItemLocation.setBaseOffset(1400);
        ILocExtent mainItemExtent = new ILocExtent();
        mainItemExtent.setExtentIndex(0);
        mainItemExtent.setExtentOffset(0);
        mainItemExtent.setExtentLength(IMAGE_HEIGHT * IMAGE_WIDTH * NUM_BYTES_PER_PIXEL);
        mainItemLocation.addExtent(mainItemExtent);
        iloc.addItem(mainItemLocation);
        return iloc;
    }

    private Box makeItemPropertiesBox() {
        ItemPropertiesBox iprp = new ItemPropertiesBox();
        ItemPropertyContainerBox ipco = new ItemPropertyContainerBox();
        ipco.addProperty(makeComponentDefinitionBox_mono());
        ipco.addProperty(makeUncompressedFrameConfigBox_mono());
        ipco.addProperty(makeImageSpatialExtentsProperty());
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
            associationToImageSpatialExtentsProperty.setEssential(true);
            mainItemAssociations.addAssociation(associationToImageSpatialExtentsProperty);

            itemPropertyAssociation.addEntry(mainItemAssociations);
        }
        iprp.addItemPropertyAssociation(itemPropertyAssociation);

        {
            addPropertyFor(iprp, makeContentId(PRIMARY_ITEM_CONTENT_ID), MAIN_ITEM_ID);
        }
        return iprp;
    }

    protected ComponentDefinitionBox makeComponentDefinitionBox_mono() {
        ComponentDefinitionBox cmpd = new ComponentDefinitionBox();
        ComponentDefinition redComponent = new ComponentDefinition(4, null);
        cmpd.addComponentDefinition(redComponent);
        ComponentDefinition greenComponent = new ComponentDefinition(5, null);
        cmpd.addComponentDefinition(greenComponent);
        ComponentDefinition blueComponent = new ComponentDefinition(6, null);
        cmpd.addComponentDefinition(blueComponent);
        return cmpd;
    }

    private UncompressedFrameConfigBox makeUncompressedFrameConfigBox_mono() {
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
        uncc.setNumTileColumnsMinusOne(0);
        uncc.setNumTileColumnsMinusOne(0);
        return uncc;
    }

    protected ImageSpatialExtentsProperty makeImageSpatialExtentsProperty() {
        ImageSpatialExtentsProperty ispe = new ImageSpatialExtentsProperty();
        ispe.setImageHeight(IMAGE_HEIGHT);
        ispe.setImageWidth(IMAGE_WIDTH);
        return ispe;
    }

    private Box makeItemReferenceBox() {
        ItemReferenceBox iref = new ItemReferenceBox();
        return iref;
    }

    protected void writeBoxes(List<Box> boxes, String outputPathName) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        for (Box box : boxes) {
            // consider validating here
            box.writeTo(streamWriter);
        }
        File testOut = new File(outputPathName);
        Files.write(testOut.toPath(), baos.toByteArray(), StandardOpenOption.CREATE);
    }

    private void addPropertyFor(
            ItemPropertiesBox iprp, AbstractItemProperty property, long targetItem) {
        int index = iprp.getItemProperties().addProperty(property);
        AssociationEntry entry = new AssociationEntry();
        entry.setItemId(targetItem);
        PropertyAssociation association = new PropertyAssociation();
        association.setEssential(false);
        association.setPropertyIndex(index);
        entry.addAssociation(association);
        iprp.addAssociationEntry(entry);
    }

    private UUIDProperty makeContentId(String contentId) {
        UUIDProperty contentIdProperty = new UUIDProperty();
        contentIdProperty.setExtendedType(CONTENT_ID_UUID);
        contentIdProperty.setPayload(contentId.getBytes(StandardCharsets.UTF_8));
        System.out.println(contentIdProperty.toString());
        return contentIdProperty;
    }
}
