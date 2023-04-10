package net.frogmouth.rnd.eofff.tools.siff;

import static net.frogmouth.rnd.eofff.yuv.ColourSpace.YUV420;
import static org.testng.Assert.*;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.AssociationEntry;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemPropertiesBox;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemPropertyAssociation;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemPropertyContainerBox;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.PropertyAssociation;
import net.frogmouth.rnd.eofff.imagefileformat.properties.image.ImageSpatialExtentsProperty;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.free.FreeBox;
import net.frogmouth.rnd.eofff.isobmff.ftyp.FileTypeBox;
import net.frogmouth.rnd.eofff.isobmff.iloc.ILocExtent;
import net.frogmouth.rnd.eofff.isobmff.iloc.ILocItem;
import net.frogmouth.rnd.eofff.isobmff.iloc.ItemLocationBox;
import net.frogmouth.rnd.eofff.isobmff.mdat.MediaDataBox;
import net.frogmouth.rnd.eofff.isobmff.meta.MetaBox;
import net.frogmouth.rnd.eofff.uncompressed.cmpd.ComponentDefinition;
import net.frogmouth.rnd.eofff.uncompressed.cmpd.ComponentDefinitionBox;
import net.frogmouth.rnd.eofff.uncompressed.uncc.Component;
import net.frogmouth.rnd.eofff.uncompressed.uncc.ComponentFormat;
import net.frogmouth.rnd.eofff.uncompressed.uncc.Interleaving;
import net.frogmouth.rnd.eofff.uncompressed.uncc.SamplingType;
import net.frogmouth.rnd.eofff.uncompressed.uncc.UncompressedFrameConfigBox;
import net.frogmouth.rnd.eofff.yuv.Y4mReader;
import org.testng.annotations.Test;

public class Uncompressed_yuv_Test extends UncompressedTestSupport {

    @Test
    public void writeFile_yuv444() throws IOException {
        writeFileYUV(
                "/home/bradh/yuvdata/in_to_tree_444_720p50.y4m", "test_uncompressed_yuv444.heif");
    }

    @Test
    public void writeFile_v308() throws IOException {
        writeFileYUV(
                "/home/bradh/yuvdata/in_to_tree_444_720p50.y4m",
                "test_uncompressed_v308.heif",
                new FourCC("v308"));
    }

    @Test
    public void writeFile_yuv422() throws IOException {
        writeFileYUV(
                "/home/bradh/yuvdata/controlled_burn_1080p.y4m", "test_uncompressed_yuv422.heif");
    }

    @Test
    public void writeFile_2vuy() throws IOException {
        writeFileYUV(
                "/home/bradh/yuvdata/controlled_burn_1080p.y4m",
                "test_uncompressed_2vuy.heif",
                new FourCC("2vuy"));
    }

    @Test
    public void writeFile_yuv2() throws IOException {
        writeFileYUV(
                "/home/bradh/yuvdata/controlled_burn_1080p.y4m",
                "test_uncompressed_yuv2.heif",
                new FourCC("yuv2"));
    }

    @Test
    public void writeFile_yvyu() throws IOException {
        writeFileYUV(
                "/home/bradh/yuvdata/controlled_burn_1080p.y4m",
                "test_uncompressed_yvyu.heif",
                new FourCC("yvyu"));
    }

    @Test
    public void writeFile_vyuy() throws IOException {
        writeFileYUV(
                "/home/bradh/yuvdata/controlled_burn_1080p.y4m",
                "test_uncompressed_vyuy.heif",
                new FourCC("vyuy"));
    }

    @Test
    public void writeFile_i420() throws IOException {
        writeFileYUV(
                "/home/bradh/yuvdata/pedestrian_area_1080p25.y4m",
                "test_uncompressed_i420.heif",
                new FourCC("i420"));
    }

    @Test
    public void writeFile_nv12() throws IOException {
        writeFileYUV(
                "/home/bradh/yuvdata/pedestrian_area_1080p25.y4m",
                "test_uncompressed_nv12.heif",
                new FourCC("nv12"));
    }

    @Test
    public void writeFile_nv21() throws IOException {
        writeFileYUV(
                "/home/bradh/yuvdata/pedestrian_area_1080p25.y4m",
                "test_uncompressed_nv21.heif",
                new FourCC("nv21"));
    }

    private void writeFileYUV(String inFile, String outFile) throws IOException {
        Path path = Path.of(inFile);
        SeekableByteChannel fileChannel = FileChannel.open(path, StandardOpenOption.READ);
        Y4mReader reader = new Y4mReader(fileChannel);
        reader.readHeader();
        List<Box> boxes = new ArrayList<>();
        FileTypeBox ftyp = createFileTypeBox();
        boxes.add(ftyp);
        MetaBox meta = createMetaBox_yuv(reader, null);
        boxes.add(meta);
        long lengthOfPreviousBoxes = ftyp.getSize() + meta.getSize();
        long numberOfFillBytes = MDAT_START - lengthOfPreviousBoxes - LENGTH_OF_FREEBOX_HEADER;
        FreeBox free = new FreeBox();
        free.setData(new byte[(int) numberOfFillBytes]);
        boxes.add(free);
        MediaDataBox mdat = createMediaDataBox_yuv(reader, null);
        boxes.add(mdat);
        writeBoxes(boxes, outFile);
    }

    private void writeFileYUV(String inFile, String outFile, FourCC profile) throws IOException {
        Path path = Path.of(inFile);
        SeekableByteChannel fileChannel = FileChannel.open(path, StandardOpenOption.READ);
        Y4mReader reader = new Y4mReader(fileChannel);
        reader.readHeader();
        List<Box> boxes = new ArrayList<>();
        FileTypeBox ftyp = createFileTypeBox();
        boxes.add(ftyp);
        MetaBox meta = createMetaBox_yuv(reader, profile);
        boxes.add(meta);
        long lengthOfPreviousBoxes = ftyp.getSize() + meta.getSize();
        long numberOfFillBytes = MDAT_START - lengthOfPreviousBoxes - LENGTH_OF_FREEBOX_HEADER;
        FreeBox free = new FreeBox();
        free.setData(new byte[(int) numberOfFillBytes]);
        boxes.add(free);
        MediaDataBox mdat = createMediaDataBox_yuv(reader, profile);
        boxes.add(mdat);
        writeBoxes(boxes, outFile);
    }

    private MetaBox createMetaBox_yuv(Y4mReader reader, FourCC profile) {
        MetaBox meta = new MetaBox();
        List<Box> boxes = new ArrayList<>();
        boxes.add(makeHandlerBox());
        boxes.add(makePrimaryItemBox());
        boxes.add(makeItemInfoBox());
        boxes.add(makeItemLocationBox_yuv(reader));
        boxes.add(makeItemPropertiesBox_yuv(reader, profile));
        meta.addNestedBoxes(boxes);
        return meta;
    }

    private ItemLocationBox makeItemLocationBox_yuv(Y4mReader reader) {
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
        mainItemExtent.setExtentLength(reader.getFrameSizeBytes());
        mainItemLocation.addExtent(mainItemExtent);
        iloc.addItem(mainItemLocation);
        return iloc;
    }

    private Box makeItemPropertiesBox_yuv(Y4mReader reader, FourCC profile) {
        ItemPropertiesBox iprp = new ItemPropertiesBox();
        ItemPropertyContainerBox ipco = new ItemPropertyContainerBox();
        ipco.addProperty(makeComponentDefinitionBox_yuv());
        ipco.addProperty(makeUncompressedFrameConfigBox_yuv(reader, profile));
        ipco.addProperty(makeImageSpatialExtentsProperty_yuv(reader));
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

    private ComponentDefinitionBox makeComponentDefinitionBox_yuv() {
        ComponentDefinitionBox cmpd = new ComponentDefinitionBox();
        ComponentDefinition yComponent = new ComponentDefinition(1, null);
        cmpd.addComponentDefinition(yComponent);
        ComponentDefinition cbComponent = new ComponentDefinition(2, null);
        cmpd.addComponentDefinition(cbComponent);
        ComponentDefinition crComponent = new ComponentDefinition(3, null);
        cmpd.addComponentDefinition(crComponent);
        return cmpd;
    }

    private UncompressedFrameConfigBox makeUncompressedFrameConfigBox_yuv(
            Y4mReader reader, FourCC profile) {
        if ((profile != null)
                && (!profile.equals(new FourCC("2vuy")))
                && (!profile.equals(new FourCC("yuv2")))
                && (!profile.equals(new FourCC("yvyu")))
                && (!profile.equals(new FourCC("vyuy")))
                && (!profile.equals(new FourCC("i420")))
                && (!profile.equals(new FourCC("v308")))
                && (!profile.equals(new FourCC("nv12")))
                && (!profile.equals(new FourCC("nv21")))) {
            fail("need to handle specified profile");
        }
        UncompressedFrameConfigBox uncc = new UncompressedFrameConfigBox();
        if (profile != null) {
            uncc.setProfile(profile);
        } else {
            uncc.setProfile(new FourCC("gene"));
        }
        if (profile == null) {
            uncc.addComponent(new Component(0, 7, ComponentFormat.UnsignedInteger, 0));
            uncc.addComponent(new Component(1, 7, ComponentFormat.UnsignedInteger, 0));
            uncc.addComponent(new Component(2, 7, ComponentFormat.UnsignedInteger, 0));
        } else if (profile.equals(new FourCC("2vuy"))) {
            uncc.addComponent(new Component(1, 7, ComponentFormat.UnsignedInteger, 0));
            uncc.addComponent(new Component(0, 7, ComponentFormat.UnsignedInteger, 0));
            uncc.addComponent(new Component(2, 7, ComponentFormat.UnsignedInteger, 0));
            uncc.addComponent(new Component(0, 7, ComponentFormat.UnsignedInteger, 0));
        } else if (profile.equals(new FourCC("yuv2"))) {
            uncc.addComponent(new Component(0, 7, ComponentFormat.UnsignedInteger, 0));
            uncc.addComponent(new Component(1, 7, ComponentFormat.UnsignedInteger, 0));
            uncc.addComponent(new Component(0, 7, ComponentFormat.UnsignedInteger, 0));
            uncc.addComponent(new Component(2, 7, ComponentFormat.UnsignedInteger, 0));
        } else if (profile.equals(new FourCC("yvyu"))) {
            uncc.addComponent(new Component(0, 7, ComponentFormat.UnsignedInteger, 0));
            uncc.addComponent(new Component(2, 7, ComponentFormat.UnsignedInteger, 0));
            uncc.addComponent(new Component(0, 7, ComponentFormat.UnsignedInteger, 0));
            uncc.addComponent(new Component(1, 7, ComponentFormat.UnsignedInteger, 0));
        } else if (profile.equals(new FourCC("vyuy"))) {
            uncc.addComponent(new Component(2, 7, ComponentFormat.UnsignedInteger, 0));
            uncc.addComponent(new Component(0, 7, ComponentFormat.UnsignedInteger, 0));
            uncc.addComponent(new Component(1, 7, ComponentFormat.UnsignedInteger, 0));
            uncc.addComponent(new Component(0, 7, ComponentFormat.UnsignedInteger, 0));
        } else if (profile.equals(new FourCC("v308"))) {
            uncc.addComponent(new Component(2, 7, ComponentFormat.UnsignedInteger, 0));
            uncc.addComponent(new Component(0, 7, ComponentFormat.UnsignedInteger, 0));
            uncc.addComponent(new Component(1, 7, ComponentFormat.UnsignedInteger, 0));
        } else if (profile.equals(new FourCC("i420"))) {
            uncc.addComponent(new Component(0, 7, ComponentFormat.UnsignedInteger, 0));
            uncc.addComponent(new Component(1, 7, ComponentFormat.UnsignedInteger, 0));
            uncc.addComponent(new Component(2, 7, ComponentFormat.UnsignedInteger, 0));
        } else if (profile.equals(new FourCC("nv12"))) {
            uncc.addComponent(new Component(0, 7, ComponentFormat.UnsignedInteger, 0));
            uncc.addComponent(new Component(1, 7, ComponentFormat.UnsignedInteger, 0));
            uncc.addComponent(new Component(2, 7, ComponentFormat.UnsignedInteger, 0));
        } else if (profile.equals(new FourCC("nv21"))) {
            uncc.addComponent(new Component(0, 7, ComponentFormat.UnsignedInteger, 0));
            uncc.addComponent(new Component(2, 7, ComponentFormat.UnsignedInteger, 0));
            uncc.addComponent(new Component(1, 7, ComponentFormat.UnsignedInteger, 0));

        } else {
            fail("need to handle specified profile");
        }
        uncc.setSamplingType(SamplingType.NoSubsampling);
        switch (reader.getColourSpace()) {
            case YUV420:
                uncc.setSamplingType(SamplingType.YCbCr420);
                break;
            case YUV422:
                uncc.setSamplingType(SamplingType.YCbCr422);
                break;
            case YUV444:
                uncc.setSamplingType(SamplingType.NoSubsampling);
                break;
            default:
                fail("unhandled YUV sampling");
                break;
        }
        if (profile == null) {
            uncc.setInterleaveType(Interleaving.Component);
        } else if ((profile.equals(new FourCC("yuv2")))
                || (profile.equals(new FourCC("2vuy")))
                || (profile.equals(new FourCC("yvyu")))
                || (profile.equals(new FourCC("vyuy")))) {
            uncc.setInterleaveType(Interleaving.MultiY);
        } else if (profile.equals(new FourCC("i420"))) {
            uncc.setInterleaveType(Interleaving.Component);
        } else if ((profile.equals(new FourCC("nv12"))) || (profile.equals(new FourCC("nv21")))) {
            uncc.setInterleaveType(Interleaving.Mixed);
        } else if (profile.equals(new FourCC("v308"))) {
            uncc.setInterleaveType(Interleaving.Pixel);
        } else {
            fail("need to handle specified profile");
        }
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

    private ImageSpatialExtentsProperty makeImageSpatialExtentsProperty_yuv(Y4mReader reader) {
        ImageSpatialExtentsProperty ispe = new ImageSpatialExtentsProperty();
        ispe.setImageHeight(reader.getFrameHeight());
        ispe.setImageWidth(reader.getFrameWidth());
        return ispe;
    }

    private MediaDataBox createMediaDataBox_yuv(Y4mReader reader, FourCC profile)
            throws IOException {
        MediaDataBox mdat = new MediaDataBox();
        int numFrames = 0;
        while (reader.hasMoreFrames()) {
            byte[] frame = reader.getFrame();
            numFrames += 1;
            if (numFrames == 100) {
                if (profile == null) {
                    mdat.setData(frame);
                } else if (profile.equals(new FourCC("2vuy"))) {
                    // Base data is assumed to be 4:2:2 planar
                    byte[] data = new byte[frame.length];
                    int yOffset = 0;
                    int cbOffset = 2 * frame.length / 4;
                    int crOffset = 3 * frame.length / 4;
                    for (int i = 0; i < frame.length / 4; i++) {
                        data[i * 4 + 0] = frame[cbOffset + i];
                        data[i * 4 + 1] = frame[yOffset + 2 * i];
                        data[i * 4 + 2] = frame[crOffset + i];
                        data[i * 4 + 3] = frame[yOffset + 2 * i + 1];
                    }
                    mdat.setData(data);
                } else if (profile.equals(new FourCC("yuv2"))) {
                    // Base data is assumed to be 4:2:2 planar
                    byte[] data = new byte[frame.length];
                    int yOffset = 0;
                    int cbOffset = 2 * frame.length / 4;
                    int crOffset = 3 * frame.length / 4;
                    for (int i = 0; i < frame.length / 4; i++) {
                        data[i * 4 + 0] = frame[yOffset + 2 * i];
                        data[i * 4 + 1] = frame[cbOffset + i];
                        data[i * 4 + 2] = frame[yOffset + 2 * i + 1];
                        data[i * 4 + 3] = frame[crOffset + i];
                    }
                    mdat.setData(data);
                } else if (profile.equals(new FourCC("yvyu"))) {
                    // Base data is assumed to be 4:2:2 planar
                    byte[] data = new byte[frame.length];
                    int yOffset = 0;
                    int cbOffset = 2 * frame.length / 4;
                    int crOffset = 3 * frame.length / 4;
                    for (int i = 0; i < frame.length / 4; i++) {
                        data[i * 4 + 0] = frame[yOffset + 2 * i];
                        data[i * 4 + 1] = frame[crOffset + i];
                        data[i * 4 + 2] = frame[yOffset + 2 * i + 1];
                        data[i * 4 + 3] = frame[cbOffset + i];
                    }
                    mdat.setData(data);
                } else if (profile.equals(new FourCC("vyuy"))) {
                    // Base data is assumed to be 4:2:2 planar
                    byte[] data = new byte[frame.length];
                    int yOffset = 0;
                    int cbOffset = 2 * frame.length / 4;
                    int crOffset = 3 * frame.length / 4;
                    for (int i = 0; i < frame.length / 4; i++) {
                        data[i * 4 + 0] = frame[crOffset + i];
                        data[i * 4 + 1] = frame[yOffset + 2 * i];
                        data[i * 4 + 2] = frame[cbOffset + i];
                        data[i * 4 + 3] = frame[yOffset + 2 * i + 1];
                    }
                    mdat.setData(data);
                } else if (profile.equals(new FourCC("v308"))) {
                    // Base data is assumed to be 4:4:4 planar
                    byte[] data = new byte[frame.length];
                    int yOffset = 0;
                    int cbOffset = frame.length / 3;
                    int crOffset = 2 * frame.length / 3;
                    for (int i = 0; i < frame.length / 3; i++) {
                        data[i * 3] = frame[crOffset + i];
                        data[i * 3 + 1] = frame[yOffset + i];
                        data[i * 3 + 2] = frame[cbOffset + i];
                    }
                    mdat.setData(data);
                } else if (profile.equals(new FourCC("i420"))) {
                    mdat.setData(frame);
                } else if (profile.equals(new FourCC("nv12"))) {
                    // Base data is assumed to be 4:2:0 planar
                    assert ((frame.length % 6) == 0);
                    byte[] data = new byte[frame.length];

                    int cbOffset = 4 * frame.length / 6;
                    int crOffset = 5 * frame.length / 6;
                    byte[] interleavedChroma = new byte[frame.length - cbOffset];

                    for (int i = 0; i < frame.length / 6; i++) {
                        interleavedChroma[2 * i] = frame[cbOffset + i];
                        interleavedChroma[2 * i + 1] = frame[crOffset + i];
                    }
                    System.arraycopy(frame, 0, data, 0, cbOffset);
                    System.arraycopy(interleavedChroma, 0, data, cbOffset, data.length - cbOffset);
                    mdat.setData(data);
                } else if (profile.equals(new FourCC("nv21"))) {
                    // Base data is assumed to be 4:2:0 planar
                    assert ((frame.length % 6) == 0);
                    byte[] data = new byte[frame.length];

                    int cbOffset = 4 * frame.length / 6;
                    int crOffset = 5 * frame.length / 6;
                    byte[] interleavedChroma = new byte[frame.length - cbOffset];

                    for (int i = 0; i < frame.length / 6; i++) {
                        interleavedChroma[2 * i] = frame[crOffset + i];
                        interleavedChroma[2 * i + 1] = frame[cbOffset + i];
                    }
                    System.arraycopy(frame, 0, data, 0, cbOffset);
                    System.arraycopy(interleavedChroma, 0, data, cbOffset, data.length - cbOffset);
                    mdat.setData(data);
                } else {
                    fail("need to handle specified profile");
                }
                break;
            }
        }
        return mdat;
    }
}
