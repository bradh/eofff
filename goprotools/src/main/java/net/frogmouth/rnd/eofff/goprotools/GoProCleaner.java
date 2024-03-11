package net.frogmouth.rnd.eofff.goprotools;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.frogmouth.rnd.eofff.imagefileformat.properties.hevc.HEVCConfigurationItemProperty;
import net.frogmouth.rnd.eofff.imagefileformat.properties.image.ImageSpatialExtentsProperty;
import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBox;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FileParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.dinf.DataInformationBox;
import net.frogmouth.rnd.eofff.isobmff.dref.DataEntryBaseBox;
import net.frogmouth.rnd.eofff.isobmff.dref.DataEntryUrlBox;
import net.frogmouth.rnd.eofff.isobmff.dref.DataReferenceBox;
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
import net.frogmouth.rnd.eofff.isobmff.iprp.AssociationEntry;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemPropertiesBox;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemPropertyAssociation;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemPropertyContainerBox;
import net.frogmouth.rnd.eofff.isobmff.iprp.PropertyAssociation;
import net.frogmouth.rnd.eofff.isobmff.mdat.MediaDataBox;
import net.frogmouth.rnd.eofff.isobmff.mdia.MediaBox;
import net.frogmouth.rnd.eofff.isobmff.meta.MetaBox;
import net.frogmouth.rnd.eofff.isobmff.minf.MediaInformationBox;
import net.frogmouth.rnd.eofff.isobmff.moov.MovieBox;
import net.frogmouth.rnd.eofff.isobmff.nmhd.NullMediaHeaderBox;
import net.frogmouth.rnd.eofff.isobmff.pitm.PrimaryItemBox;
import net.frogmouth.rnd.eofff.isobmff.saio.SampleAuxiliaryInformationOffsetsBox;
import net.frogmouth.rnd.eofff.isobmff.saiz.SampleAuxiliaryInformationSizesBox;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.AudioSampleEntry;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntry;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.VisualSampleEntry;
import net.frogmouth.rnd.eofff.isobmff.stbl.SampleTableBox;
import net.frogmouth.rnd.eofff.isobmff.stco.ChunkOffsetBox;
import net.frogmouth.rnd.eofff.isobmff.stsd.SampleDescriptionBox;
import net.frogmouth.rnd.eofff.isobmff.stss.SyncSampleBox;
import net.frogmouth.rnd.eofff.isobmff.stsz.SampleSizeBox;
import net.frogmouth.rnd.eofff.isobmff.trak.TrackBox;
import net.frogmouth.rnd.eofff.mpeg4.esds.ESDBox;
import net.frogmouth.rnd.eofff.nalvideo.HEVCConfigurationBox;
import net.frogmouth.rnd.eofff.nalvideo.HEVCDecoderConfigurationRecord;
import net.frogmouth.rnd.eofff.nalvideo.HVC1SampleEntry;
import net.frogmouth.rnd.eofff.uncompressed.taic.TAIClockInfoBox;
import net.frogmouth.rnd.eofff.uncompressed.taic.TAIClockInfoItemProperty;

class GoProCleaner {

    private static final int SECURITY_ITEM_ID = 20;
    private static final int IMAGE_ITEM_ID = 30;
    private static final String CONTENT_ID_UUID_URI =
            "urn:uuid:aac8ab7d-f519-5437-b7d3-c973d155e253";

    private final String SECURITY_MIME_TYPE = "application/x.fake-dni-arh+xml";
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

    private int nextItemId = 30;

    private final List<Box> sourceBoxes = new ArrayList<>();
    private final List<Box> destinationBoxes = new ArrayList<>();

    public GoProCleaner(String filename) throws IOException {
        sourceBoxes.addAll(parseFile(filename));
    }

    private List<Box> parseFile(String inputPath) throws IOException {
        Path testFile = Paths.get(inputPath);
        FileParser fileParser = new FileParser();
        return fileParser.parse(testFile);
    }

    private static Box extractChildBox(AbstractContainerBox parent, String fourCC) {
        FourCC fourcc = new FourCC(fourCC);
        if (parent == null) {
            return null;
        }
        for (Box box : parent.getNestedBoxes()) {
            if (box.getFourCC().equals(fourcc)) {
                parent.removeNestedBox(box);
                return box;
            }
        }
        return null;
    }

    private Box extractTopLevelBox(String fourCC) {
        for (Box box : sourceBoxes) {
            if (box.getFourCC().equals(new FourCC(fourCC))) {
                sourceBoxes.remove(box);
                return box;
            }
        }
        return null;
    }

    private static Box findChildBox(AbstractContainerBox parent, String fourCC) {
        FourCC fourcc = new FourCC(fourCC);
        if (parent == null) {
            return null;
        }
        for (Box box : parent.getNestedBoxes()) {
            if (box.getFourCC().equals(fourcc)) {
                return box;
            }
        }
        return null;
    }

    private Box findTopLevelBox(String fourCC) {
        for (Box box : sourceBoxes) {
            if (box.getFourCC().equals(new FourCC(fourCC))) {
                return box;
            }
        }
        return null;
    }

    void cleanFile() {
        FileTypeBox ftyp = (FileTypeBox) extractTopLevelBox("ftyp");
        ftyp.addCompatibleBrand(Brand.HEIC);
        destinationBoxes.add(ftyp);
        MetaBox meta = makeMetaBox();
        // TODO: we should check relative sizes for ftyp
        long offsetAdjustment = Brand.BYTES + meta.getSize();
        destinationBoxes.add(meta);
        boolean freeIsBeforeMdat = false;
        boolean mdatIsBeforeMoov = false;
        boolean seenMdat = false;
        for (Box box : sourceBoxes) {
            if (box instanceof MediaDataBox) {
                seenMdat = true;
            } else if ((!seenMdat) && (box instanceof FreeBox)) {
                freeIsBeforeMdat = true;
            } else if ((seenMdat) && (box instanceof MovieBox)) {
                mdatIsBeforeMoov = true;
            }
        }
        Box free = extractTopLevelBox("free");
        if (freeIsBeforeMdat) {
            // We will throw this away
            offsetAdjustment -= free.getSize();
        }
        MovieBox moov = (MovieBox) extractTopLevelBox("moov");
        MovieBox cleanMoov = (MovieBox) cleanMoov(moov);
        if (mdatIsBeforeMoov) {
            offsetAdjustment += cleanMoov.getSize();
        }
        adjustOffsets(cleanMoov, offsetAdjustment);
        adjustOffsets(meta, offsetAdjustment);
        destinationBoxes.add(cleanMoov);
        destinationBoxes.addAll(sourceBoxes);
    }

    void writeOutFile(String filename) throws IOException {
        writeBoxes(destinationBoxes, filename);
    }

    private byte[] getSecurityXMLBytes(boolean dump) {
        byte[] securityXMLBytes = FAKE_SECURITY_XML.getBytes(StandardCharsets.UTF_8);
        return securityXMLBytes;
    }

    private HEVCDecoderConfigurationRecord findHEVCDecderConfigurationRecord() {
        SampleTableBox stbl = findSampleTableBox();
        // TODO: this should be more robust
        SampleDescriptionBox stsd = (SampleDescriptionBox) findChildBox(stbl, "stsd");
        HVC1SampleEntry hvc1 = (HVC1SampleEntry) stsd.getSampleEntries().get(0);
        HEVCConfigurationBox hvcC = (HEVCConfigurationBox) hvc1.getNestedBoxes().get(0);
        return hvcC.getHevcConfig();
    }

    private SampleTableBox findSampleTableBox() {
        // TODO: this should be more robust
        MovieBox moov = (MovieBox) findTopLevelBox("moov");
        TrackBox trak = (TrackBox) findChildBox(moov, "trak");
        MediaBox mdia = (MediaBox) findChildBox(trak, "mdia");
        MediaInformationBox minf = (MediaInformationBox) findChildBox(mdia, "minf");
        SampleTableBox stbl = (SampleTableBox) findChildBox(minf, "stbl");
        return stbl;
    }

    public MetaBox makeMetaBox() {
        MetaBox meta = new MetaBox();

        HandlerBox hdlr = new HandlerBox();
        hdlr.setHandlerType("pict");
        hdlr.setName("File metadata and images");
        meta.addNestedBox(hdlr);

        PrimaryItemBox pitm = new PrimaryItemBox();
        pitm.setItemID(IMAGE_ITEM_ID);
        meta.addNestedBox(pitm);

        ItemLocationBox iloc = new ItemLocationBox();
        iloc.setVersion(1);
        iloc.setOffsetSize(4);
        iloc.setLengthSize(4);
        iloc.setBaseOffsetSize(4);
        iloc.setIndexSize(4);
        int numImages = 0;
        long ilocOffset = 0;
        {
            {
                ILocItem securityItemLocation = new ILocItem();
                securityItemLocation.setConstructionMethod(1);
                securityItemLocation.setItemId(SECURITY_ITEM_ID);
                securityItemLocation.setBaseOffset(ilocOffset);
                ILocExtent securityExtent = new ILocExtent();
                securityExtent.setExtentIndex(0);
                securityExtent.setExtentOffset(0);
                securityExtent.setExtentLength(getSecurityXMLBytes(false).length);
                ilocOffset += securityExtent.getExtentLength();
                securityItemLocation.addExtent(securityExtent);
                iloc.addItem(securityItemLocation);
            }
            {
                SampleTableBox stbl = this.findSampleTableBox();
                ChunkOffsetBox stco = (ChunkOffsetBox) findChildBox(stbl, "stco");
                SampleSizeBox stsz = (SampleSizeBox) findChildBox(stbl, "stsz");
                SyncSampleBox stss = (SyncSampleBox) findChildBox(stbl, "stss");
                // There should be a SampleToChunkBox check, but GoPro is always 1:1, so hack for
                // now, TODO later
                List<Long> syncSamples = stss.getEntries();
                for (int i = 1; i <= stco.getEntries().size(); i++) {
                    if (syncSamples.contains((long) i)) {
                        long offset = stco.getEntries().get(i - 1);
                        long length = stsz.getEntries().get(i - 1);
                        ILocItem imageLocation = makeImageLocationItem(offset, length, numImages);
                        numImages += 1;
                        iloc.addItem(imageLocation);
                    }
                }
            }
        }
        meta.addNestedBox(iloc);
        ItemInfoBox iinf = new ItemInfoBox();
        {
            {
                ItemInfoEntry fakeSecurityItem = new ItemInfoEntry();
                fakeSecurityItem.setVersion(2);
                fakeSecurityItem.setItemID(SECURITY_ITEM_ID);
                FourCC mime_fourcc = new FourCC("mime");
                fakeSecurityItem.setItemType(mime_fourcc.asUnsigned());
                fakeSecurityItem.setContentType(SECURITY_MIME_TYPE);
                fakeSecurityItem.setItemName("Security Marking (Fake XML)");
                iinf.addItem(fakeSecurityItem);
            }
            for (int i = 0; i < numImages; i++) {
                ItemInfoEntry imageItem = new ItemInfoEntry();
                imageItem.setVersion(2);
                imageItem.setItemID(IMAGE_ITEM_ID + i);
                FourCC hvc1_fourcc = new FourCC("hvc1");
                imageItem.setItemType(hvc1_fourcc.asUnsigned());
                // TODO: it would be nice to flag the frame number
                imageItem.setItemName("Still image (HEVC)");
                iinf.addItem(imageItem);
            }
        }
        meta.addNestedBox(iinf);

        ItemPropertiesBox iprp = new ItemPropertiesBox();
        ItemPropertyContainerBox ipco = new ItemPropertyContainerBox();
        HEVCConfigurationItemProperty hvcC = new HEVCConfigurationItemProperty();
        HEVCDecoderConfigurationRecord config = findHEVCDecderConfigurationRecord();
        hvcC.setHevcConfig(config);
        ipco.addProperty(hvcC); // prop = 1

        ImageSpatialExtentsProperty ispe = new ImageSpatialExtentsProperty();
        ispe.setImageHeight(2988);
        ispe.setImageWidth(5312);
        ipco.addProperty(ispe); // prop = 2

        TAIClockInfoItemProperty taic = new TAIClockInfoItemProperty();
        taic.setReferenceSourceType((byte) 2);
        ipco.addProperty(taic); // prop = 3

        iprp.setItemProperties(ipco);

        for (int i = 0; i < numImages; i++) {
            ItemPropertyAssociation assoc = new ItemPropertyAssociation();
            AssociationEntry entry = new AssociationEntry();
            entry.setItemId(IMAGE_ITEM_ID + i);
            {
                PropertyAssociation associationToHVCCProperty = new PropertyAssociation();
                associationToHVCCProperty.setPropertyIndex(1);
                associationToHVCCProperty.setEssential(true);
                entry.addAssociation(associationToHVCCProperty);
            }
            {
                PropertyAssociation associationToISPE = new PropertyAssociation();
                associationToISPE.setPropertyIndex(2);
                associationToISPE.setEssential(false);
                entry.addAssociation(associationToISPE);
            }
            {
                PropertyAssociation associationToTAIC = new PropertyAssociation();
                associationToTAIC.setPropertyIndex(3);
                associationToTAIC.setEssential(false);
                entry.addAssociation(associationToTAIC);
            }
            assoc.addEntry(entry);
            iprp.addItemPropertyAssociation(assoc);
        }

        meta.addNestedBox(iprp);

        ItemDataBox idat = new ItemDataBox();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.writeBytes(getSecurityXMLBytes(true));
        idat.setData(baos.toByteArray());
        meta.addNestedBox(idat);
        return meta;
    }

    private ILocItem makeImageLocationItem(long offset, long length, int imageNumber) {
        ILocItem imageLocation = new ILocItem();
        imageLocation.setConstructionMethod(0);
        imageLocation.setItemId(IMAGE_ITEM_ID + imageNumber);
        imageLocation.setBaseOffset(0);
        ILocExtent imageExtent = new ILocExtent();
        imageExtent.setExtentIndex(0);
        imageExtent.setExtentOffset(offset);
        imageExtent.setExtentLength(length);
        imageLocation.addExtent(imageExtent);
        return imageLocation;
    }

    protected void writeBoxes(List<Box> cleanBoxes, String outputPathName) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        for (Box box : cleanBoxes) {
            box.writeTo(streamWriter);
        }
        File testOut = new File(outputPathName);
        Files.write(testOut.toPath(), baos.toByteArray(), StandardOpenOption.CREATE);
    }

    private Box cleanMoov(MovieBox moov) {
        MovieBox cleanMoov = new MovieBox();
        Box mvhd = extractChildBox(moov, "mvhd");
        cleanMoov.appendNestedBox(mvhd);
        Box udta = extractChildBox(moov, "udta");
        for (Box box : moov.getNestedBoxes()) {
            if (box.getFourCC().toString().equals("iods")) {
                continue;
            }
            Box cleanBox =
                    switch (box.getFourCC().toString()) {
                        case "trak" -> cleanTrak((TrackBox) box);
                        default -> box;
                    };
            cleanMoov.appendNestedBox(cleanBox);
        }
        if (udta != null) {
            cleanMoov.appendNestedBox(udta);
        }
        return cleanMoov;
    }

    private Box cleanTrak(TrackBox trak) {
        TrackBox cleanTrak = new TrackBox();
        for (Box box : trak.getNestedBoxes()) {
            Box cleanBox =
                    switch (box.getFourCC().toString()) {
                        case "mdia" -> cleanMdia((MediaBox) box);
                        default -> box;
                    };
            cleanTrak.appendNestedBox(cleanBox);
        }
        HandlerBox mediaHandler = findMediaHandler(cleanTrak);
        boolean haveMeta = false;
        for (Box box : trak.getNestedBoxes()) {
            if (box.getFourCC().toString().equals("meta")) {
                haveMeta = true;
            }
        }
        int trakContentId_id = this.nextItemId++;
        String trakContentId = "urn:uuid:" + UUID.randomUUID();
        byte[] trakContentIdBytes = trakContentId.getBytes(StandardCharsets.UTF_8);
        if (!haveMeta) {
            MetaBox trakMeta = new MetaBox();
            if (mediaHandler != null) {
                trakMeta.addNestedBox(mediaHandler);
            }
            ItemLocationBox iloc = new ItemLocationBox();
            iloc.setVersion(1);
            iloc.setOffsetSize(4);
            iloc.setLengthSize(4);
            iloc.setBaseOffsetSize(4);
            iloc.setIndexSize(4);
            long ilocOffset = 0;
            {
                ILocItem securityItemLocation = new ILocItem();
                securityItemLocation.setConstructionMethod(1);
                securityItemLocation.setItemId(trakContentId_id);
                securityItemLocation.setBaseOffset(ilocOffset);
                ILocExtent securityExtent = new ILocExtent();
                securityExtent.setExtentIndex(0);
                securityExtent.setExtentOffset(ilocOffset);
                securityExtent.setExtentLength(trakContentIdBytes.length);
                ilocOffset += trakContentIdBytes.length;
                securityItemLocation.addExtent(securityExtent);
                iloc.addItem(securityItemLocation);
            }
            trakMeta.addNestedBox(iloc);
            ItemInfoBox iinf = new ItemInfoBox();
            {
                ItemInfoEntry fakeSecurityItem = new ItemInfoEntry();
                fakeSecurityItem.setVersion(2);
                fakeSecurityItem.setItemID(trakContentId_id);
                FourCC uri_fourcc = new FourCC("uri ");
                fakeSecurityItem.setItemType(uri_fourcc.asUnsigned());
                fakeSecurityItem.setItemUriType(CONTENT_ID_UUID_URI);
                fakeSecurityItem.setItemName("Content ID");
                iinf.addItem(fakeSecurityItem);
            }
            trakMeta.addNestedBox(iinf);
            {
                ItemDataBox idat = new ItemDataBox();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                baos.writeBytes(trakContentIdBytes);
                idat.setData(baos.toByteArray());
                trakMeta.addNestedBox(idat);
            }
            cleanTrak.appendNestedBox(trakMeta);
        }
        return cleanTrak;
    }

    private HandlerBox findMediaHandler(TrackBox trak) {
        MediaBox mdia = null;
        for (Box box : trak.getNestedBoxes()) {
            if (box instanceof MediaBox mediaBox) {
                mdia = mediaBox;
            }
        }
        if (mdia == null) {
            return null;
        }
        for (Box box : mdia.getNestedBoxes()) {
            if (box instanceof HandlerBox handlerBox) {
                return handlerBox;
            }
        }
        return null;
    }

    private Box cleanMdia(MediaBox mdia) {
        MediaBox cleanMdia = new MediaBox();
        for (Box box : mdia.getNestedBoxes()) {
            Box cleanBox =
                    switch (box.getFourCC().toString()) {
                        case "minf" -> cleanMinf((MediaInformationBox) box);
                        default -> box;
                    };
            cleanMdia.appendNestedBox(cleanBox);
        }
        return cleanMdia;
    }

    private Box cleanMinf(MediaInformationBox minf) {
        MediaInformationBox cleanMinf = new MediaInformationBox();
        for (Box box : minf.getNestedBoxes()) {
            Box cleanBox =
                    switch (box.getFourCC().toString()) {
                        case "dinf" -> cleanDinf((DataInformationBox) box);
                        case "stbl" -> cleanStbl((SampleTableBox) box);
                        case "gmhd" -> new NullMediaHeaderBox();
                        default -> box;
                    };
            cleanMinf.appendNestedBox(cleanBox);
        }
        return cleanMinf;
    }

    private Box cleanDinf(DataInformationBox dinf) {
        DataInformationBox cleanDinf = new DataInformationBox();
        for (Box box : dinf.getNestedBoxes()) {
            Box cleanBox =
                    switch (box.getFourCC().toString()) {
                        case "dref" -> cleanDref((DataReferenceBox) box);
                        default -> box;
                    };
            cleanDinf.appendNestedBox(cleanBox);
        }
        return cleanDinf;
    }

    private DataReferenceBox cleanDref(DataReferenceBox dref) {
        DataReferenceBox cleanDref = new DataReferenceBox();
        for (DataEntryBaseBox dataRef : dref.getEntries()) {
            DataEntryBaseBox cleanRef = cleanDataReference(dataRef);
            cleanDref.addDataReference(cleanRef);
        }
        return cleanDref;
    }

    private DataEntryBaseBox cleanDataReference(DataEntryBaseBox dataRef) {
        return switch (dataRef.getFourCC().toString()) {
            case "alis" -> sameFileDataReference();
            default -> dataRef;
        };
    }

    private DataEntryBaseBox sameFileDataReference() {
        DataEntryUrlBox url = new DataEntryUrlBox();
        url.setFlags(DataEntryUrlBox.MEDIA_DATA_IN_SAME_FILE_FLAG);
        return url;
    }

    private Box cleanStbl(SampleTableBox stbl) {
        SampleTableBox cleanStbl = new SampleTableBox();
        long sampleCount = 0;
        for (Box box : stbl.getNestedBoxes()) {
            Box cleanBox =
                    switch (box.getFourCC().toString()) {
                        case "stsd" -> cleanStsd((SampleDescriptionBox) box);
                        default -> box;
                    };
            cleanStbl.appendNestedBox(cleanBox);
            if (box instanceof SampleSizeBox stsz) {
                sampleCount = stsz.getSampleCount();
            }
        }
        SampleAuxiliaryInformationSizesBox saiz = new SampleAuxiliaryInformationSizesBox();
        saiz.setDefaultSampleInfoSize(Long.BYTES + Byte.BYTES);
        saiz.setSampleCount(sampleCount);
        cleanStbl.appendNestedBox(saiz);
        SampleAuxiliaryInformationOffsetsBox saio = new SampleAuxiliaryInformationOffsetsBox();
        // TODO: work out the offsets
        Long offset = 9999l;
        saio.addOffset(offset);
        cleanStbl.appendNestedBox(saio);
        // TODO: actually write the values to mdat
        return cleanStbl;
    }

    private Box cleanStsd(SampleDescriptionBox dirty) {
        var clean = new SampleDescriptionBox();
        for (SampleEntry entry : dirty.getSampleEntries()) {
            SampleEntry cleanEntry = cleanSampleEntry(entry);
            clean.appendSampleEntry(cleanEntry);
        }
        return clean;
    }

    private SampleEntry cleanSampleEntry(SampleEntry dirty) {
        TAIClockInfoBox taic = new TAIClockInfoBox();
        taic.setReferenceSourceType((byte) 2);
        if (dirty instanceof AudioSampleEntry audioDirty) {
            AudioSampleEntry clean = audioDirty;
            for (Box child : clean.getNestedBoxes()) {
                if (child instanceof ESDBox ugly) {
                    clean.removeNestedBox(ugly);
                }
            }
            clean.appendNestedBox(taic);
            return clean;
        } else if (dirty instanceof VisualSampleEntry visualSampleEntry) {
            visualSampleEntry.appendNestedBox(taic);
            return visualSampleEntry;
        } else {
            return dirty;
        }
    }

    private Box updateChunkOffset(ChunkOffsetBox stco, long offsetAdjustment) {
        ChunkOffsetBox updated = new ChunkOffsetBox();
        for (Long chunk_offset : stco.getEntries()) {
            updated.addEntry(chunk_offset + offsetAdjustment);
        }
        return updated;
    }

    private void adjustOffsets(MovieBox cleanMoov, long offsetAdjustment) {
        for (Box box : cleanMoov.getNestedBoxes()) {
            if (box instanceof TrackBox trak) {
                for (Box trakChild : trak.getNestedBoxes()) {
                    if (trakChild instanceof MediaBox mdia) {
                        for (Box mdiaChild : mdia.getNestedBoxes()) {
                            if (mdiaChild instanceof MediaInformationBox minf) {
                                for (Box minfChild : minf.getNestedBoxes()) {
                                    if (minfChild instanceof SampleTableBox stbl) {
                                        for (Box stblChild : stbl.getNestedBoxes()) {
                                            if (stblChild instanceof ChunkOffsetBox stco_source) {
                                                ChunkOffsetBox stco_dest = new ChunkOffsetBox();
                                                for (Long chunk_offset : stco_source.getEntries()) {
                                                    stco_dest.addEntry(
                                                            chunk_offset + offsetAdjustment);
                                                }
                                                extractChildBox(stbl, "stco");
                                                stbl.appendNestedBox(stco_dest);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void adjustOffsets(MetaBox mdat, long offsetAdjustment) {
        for (Box box : mdat.getNestedBoxes()) {
            if (box instanceof ItemLocationBox iloc) {
                for (ILocItem entry : iloc.getItems()) {
                    if (entry.getConstructionMethod() == 0) {
                        // File origin offset
                        entry.setBaseOffset(offsetAdjustment);
                    }
                }
            }
        }
    }
}
