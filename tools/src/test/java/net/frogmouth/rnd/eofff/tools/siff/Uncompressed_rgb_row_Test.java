package net.frogmouth.rnd.eofff.tools.siff;

import static net.frogmouth.rnd.eofff.tools.siff.UncompressedTestSupport.IMAGE_HEIGHT;
import static net.frogmouth.rnd.eofff.tools.siff.UncompressedTestSupport.IMAGE_WIDTH;
import static net.frogmouth.rnd.eofff.tools.siff.UncompressedTestSupport.MAIN_ITEM_ID;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.AssociationEntry;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemPropertiesBox;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemPropertyAssociation;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemPropertyContainerBox;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.PropertyAssociation;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.free.FreeBox;
import net.frogmouth.rnd.eofff.isobmff.ftyp.FileTypeBox;
import net.frogmouth.rnd.eofff.isobmff.iloc.ItemLocationBox;
import net.frogmouth.rnd.eofff.isobmff.mdat.MediaDataBox;
import net.frogmouth.rnd.eofff.isobmff.meta.MetaBox;
import net.frogmouth.rnd.eofff.uncompressed.uncc.Component;
import net.frogmouth.rnd.eofff.uncompressed.uncc.ComponentFormat;
import net.frogmouth.rnd.eofff.uncompressed.uncc.Interleaving;
import net.frogmouth.rnd.eofff.uncompressed.uncc.SamplingType;
import net.frogmouth.rnd.eofff.uncompressed.uncc.UncompressedFrameConfigBox;
import org.testng.annotations.Test;

public class Uncompressed_rgb_row_Test extends UncompressedTestSupport {
    @Test
    public void writeFile_rgb_row() throws IOException {
        List<Box> boxes = new ArrayList<>();
        FileTypeBox ftyp = createFileTypeBox();
        boxes.add(ftyp);
        MetaBox meta = createMetaBox_rgb_row();
        boxes.add(meta);
        long lengthOfPreviousBoxes = ftyp.getSize() + meta.getSize();
        long numberOfFillBytes = MDAT_START - lengthOfPreviousBoxes - LENGTH_OF_FREEBOX_HEADER;
        FreeBox free = new FreeBox();
        free.setData(new byte[(int) numberOfFillBytes]);
        boxes.add(free);
        MediaDataBox mdat = createMediaDataBox_rgb_row();
        boxes.add(mdat);
        writeBoxes(boxes, "test_uncompressed_rgb_row.heif");
    }

    private MediaDataBox createMediaDataBox_rgb_row() throws IOException {
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
        DataBufferByte buffer = (DataBufferByte) image.getRaster().getDataBuffer();
        int numBanks = buffer.getNumBanks();
        int totalSize = 0;
        for (int i = 0; i < numBanks; i++) {
            totalSize += buffer.getData(i).length;
        }
        byte[] data = new byte[totalSize];
        int destination = 0;
        // Copy row-by-row
        for (int r = 0; r < IMAGE_HEIGHT; r++) {
            for (int i = 0; i < numBanks; i++) {
                System.arraycopy(
                        buffer.getData(i), r * IMAGE_WIDTH, data, destination, IMAGE_WIDTH);
                destination += IMAGE_WIDTH;
            }
        }
        mdat.setData(data);
        return mdat;
    }

    private MetaBox createMetaBox_rgb_row() {
        MetaBox meta = new MetaBox();
        List<Box> boxes = new ArrayList<>();
        boxes.add(makeHandlerBox());
        boxes.add(makePrimaryItemBox());
        boxes.add(makeItemInfoBox());
        boxes.add(makeItemLocationBox_rgb_row());
        boxes.add(makeItemPropertiesBox_rgb_row());
        meta.addNestedBoxes(boxes);
        return meta;
    }

    private ItemLocationBox makeItemLocationBox_rgb_row() {
        return makeItemLocationBox_rgb_generic();
    }

    private Box makeItemPropertiesBox_rgb_row() {
        ItemPropertiesBox iprp = new ItemPropertiesBox();
        ItemPropertyContainerBox ipco = new ItemPropertyContainerBox();
        ipco.addProperty(makeComponentDefinitionBox_rgb_generic());
        ipco.addProperty(makeUncompressedFrameConfigBox_rgb_row());
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

    private UncompressedFrameConfigBox makeUncompressedFrameConfigBox_rgb_row() {
        UncompressedFrameConfigBox uncc = new UncompressedFrameConfigBox();
        uncc.setProfile(new FourCC("gene"));
        uncc.addComponent(new Component(0, 7, ComponentFormat.UnsignedInteger, 0));
        uncc.addComponent(new Component(1, 7, ComponentFormat.UnsignedInteger, 0));
        uncc.addComponent(new Component(2, 7, ComponentFormat.UnsignedInteger, 0));
        uncc.setSamplingType(SamplingType.NoSubsampling);
        uncc.setInterleaveType(Interleaving.Row);
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
}
