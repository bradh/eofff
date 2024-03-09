module net.frogmouth.rnd.eofff.isobmff {
    requires org.slf4j;
    requires com.google.auto.service;

    uses net.frogmouth.rnd.eofff.isobmff.BoxParser;

    provides net.frogmouth.rnd.eofff.isobmff.BoxParser with
            net.frogmouth.rnd.eofff.isobmff.co64.ChunkLargeOffsetBoxParser,
            net.frogmouth.rnd.eofff.isobmff.colr.ColourInformationBoxParser,
            net.frogmouth.rnd.eofff.isobmff.cprt.CopyrightBoxParser,
            net.frogmouth.rnd.eofff.isobmff.cslg.CompositionToDecodeBoxParser,
            net.frogmouth.rnd.eofff.isobmff.ctts.CompositionOffsetBoxParser,
            net.frogmouth.rnd.eofff.isobmff.dinf.DataInformationBoxParser,
            net.frogmouth.rnd.eofff.isobmff.dref.DataReferenceBoxParser,
            net.frogmouth.rnd.eofff.isobmff.edts.EditBoxParser,
            net.frogmouth.rnd.eofff.isobmff.elng.ExtendedLanguageBoxParser,
            net.frogmouth.rnd.eofff.isobmff.elst.EditListBoxParser,
            net.frogmouth.rnd.eofff.isobmff.free.FreeBoxParser,
            net.frogmouth.rnd.eofff.isobmff.free.SkipBoxParser,
            net.frogmouth.rnd.eofff.isobmff.ftyp.FileTypeBoxParser,
            net.frogmouth.rnd.eofff.isobmff.grpl.GroupsListBoxParser,
            net.frogmouth.rnd.eofff.isobmff.hdlr.HandlerBoxParser,
            net.frogmouth.rnd.eofff.isobmff.hinf.HintBytesSentParser,
            net.frogmouth.rnd.eofff.isobmff.hinf.HintBytesSent64Parser,
            net.frogmouth.rnd.eofff.isobmff.hinf.HintImmediateBytesSentParser,
            net.frogmouth.rnd.eofff.isobmff.hinf.HintLargestPacketParser,
            net.frogmouth.rnd.eofff.isobmff.hinf.HintLongestPacketParser,
            net.frogmouth.rnd.eofff.isobmff.hinf.HintMaxRateParser,
            net.frogmouth.rnd.eofff.isobmff.hinf.HintMaxRelativeTimeParser,
            net.frogmouth.rnd.eofff.isobmff.hinf.HintMediaBytesSentParser,
            net.frogmouth.rnd.eofff.isobmff.hinf.HintMinRelativeTimeParser,
            net.frogmouth.rnd.eofff.isobmff.hinf.HintRepeatedBytesSentParser,
            net.frogmouth.rnd.eofff.isobmff.hinf.HintStatisticsBoxParser,
            net.frogmouth.rnd.eofff.isobmff.hinf.HintTotalBytesSent64Parser,
            net.frogmouth.rnd.eofff.isobmff.hinf.HintTotalBytesSentParser,
            net.frogmouth.rnd.eofff.isobmff.hinf.HintTotalPacketsSentParser,
            net.frogmouth.rnd.eofff.isobmff.hinf.HintTotalPacketsSent64Parser,
            net.frogmouth.rnd.eofff.isobmff.hmhd.HintMediaHeaderBoxParser,
            net.frogmouth.rnd.eofff.isobmff.hnti.HintInformationParser,
            net.frogmouth.rnd.eofff.isobmff.mdat.MediaDataBoxParser,
            net.frogmouth.rnd.eofff.isobmff.mdhd.MediaHeaderBoxParser,
            net.frogmouth.rnd.eofff.isobmff.idat.ItemDataBoxParser,
            net.frogmouth.rnd.eofff.isobmff.iinf.ItemInfoBoxParser,
            net.frogmouth.rnd.eofff.isobmff.imda.IdentifiedMediaDataBoxParser,
            net.frogmouth.rnd.eofff.isobmff.infe.ItemInfoEntryParser,
            net.frogmouth.rnd.eofff.isobmff.iref.ItemReferenceBoxParser,
            net.frogmouth.rnd.eofff.isobmff.iloc.ItemLocationBoxParser,
            net.frogmouth.rnd.eofff.isobmff.iprp.ItemPropertyContainerBoxParser,
            net.frogmouth.rnd.eofff.isobmff.iprp.ItemPropertyAssociationParser,
            net.frogmouth.rnd.eofff.isobmff.iprp.ItemPropertiesBoxParser,
            net.frogmouth.rnd.eofff.isobmff.kind.KindBoxParser,
            net.frogmouth.rnd.eofff.isobmff.mebx.BoxedMetadataSampleEntryParser,
            // net.frogmouth.rnd.eofff.isobmff.mebx.MetadataKeyTableBoxParser,
            net.frogmouth.rnd.eofff.isobmff.meta.MetaBoxParser,
            net.frogmouth.rnd.eofff.isobmff.mehd.MovieExtendsHeaderBoxParser,
            net.frogmouth.rnd.eofff.isobmff.mfhd.MovieFragmentHeaderBoxParser,
            net.frogmouth.rnd.eofff.isobmff.moof.MovieFragmentBoxParser,
            net.frogmouth.rnd.eofff.isobmff.mdia.MediaBoxParser,
            net.frogmouth.rnd.eofff.isobmff.minf.MediaInformationBoxParser,
            net.frogmouth.rnd.eofff.isobmff.mfra.MovieFragmentRandomAccessBoxParser,
            net.frogmouth.rnd.eofff.isobmff.mfro.MovieFragmentRandomAccessOffsetBoxParser,
            net.frogmouth.rnd.eofff.isobmff.moov.MovieBoxParser,
            net.frogmouth.rnd.eofff.isobmff.mvex.MovieExtendsBoxParser,
            net.frogmouth.rnd.eofff.isobmff.mvhd.MovieHeaderBoxParser,
            net.frogmouth.rnd.eofff.isobmff.nmhd.NullMediaHeaderBoxParser,
            net.frogmouth.rnd.eofff.isobmff.padb.PaddingBitsBoxParser,
            net.frogmouth.rnd.eofff.isobmff.pasp.PixelAspectRatioBoxParser,
            net.frogmouth.rnd.eofff.isobmff.pdin.ProgressiveDownloadInfoBoxParser,
            net.frogmouth.rnd.eofff.isobmff.saio.SampleAuxiliaryInformationOffsetsBoxParser,
            net.frogmouth.rnd.eofff.isobmff.saiz.SampleAuxiliaryInformationSizesBoxParser,
            net.frogmouth.rnd.eofff.isobmff.pitm.PrimaryItemBoxParser,
            net.frogmouth.rnd.eofff.isobmff.sbgp.SampleToGroupBoxParser,
            net.frogmouth.rnd.eofff.isobmff.sdp.RTPTrackSDPHintInformationParser,
            net.frogmouth.rnd.eofff.isobmff.sgpd.SampleGroupDescriptionBoxParser,
            net.frogmouth.rnd.eofff.isobmff.sidx.SegmentIndexBoxParser,
            net.frogmouth.rnd.eofff.isobmff.smhd.SoundMediaHeaderBoxParser,
            net.frogmouth.rnd.eofff.isobmff.stbl.SampleTableBoxParser,
            net.frogmouth.rnd.eofff.isobmff.sthd.SubtitleMediaHeaderBoxParser,
            net.frogmouth.rnd.eofff.isobmff.strk.SubTrackBoxParser,
            net.frogmouth.rnd.eofff.isobmff.stsc.SampleToChunkBoxParser,
            net.frogmouth.rnd.eofff.isobmff.stsd.SampleDescriptionBoxParser,
            net.frogmouth.rnd.eofff.isobmff.stsh.ShadowSyncSampleBoxParser,
            net.frogmouth.rnd.eofff.isobmff.stco.ChunkOffsetBoxParser,
            net.frogmouth.rnd.eofff.isobmff.stss.SyncSampleBoxParser,
            net.frogmouth.rnd.eofff.isobmff.stsz.SampleSizeBoxParser,
            net.frogmouth.rnd.eofff.isobmff.stts.TimeToSampleBoxParser,
            net.frogmouth.rnd.eofff.isobmff.styp.SegmentTypeBoxParser,
            net.frogmouth.rnd.eofff.isobmff.stz2.CompactSampleSizeBoxParser,
            net.frogmouth.rnd.eofff.isobmff.traf.TrackFragmentBoxParser,
            net.frogmouth.rnd.eofff.isobmff.tfdt.TrackFragmentDecodeTimeBoxParser,
            net.frogmouth.rnd.eofff.isobmff.tfhd.TrackFragmentHeaderBoxParser,
            net.frogmouth.rnd.eofff.isobmff.tfra.TrackFragmentRandomAccessBoxParser,
            net.frogmouth.rnd.eofff.isobmff.tkhd.TrackHeaderBoxParser,
            net.frogmouth.rnd.eofff.isobmff.trak.TrackBoxParser,
            net.frogmouth.rnd.eofff.isobmff.tref.TrackReferenceBoxParser,
            net.frogmouth.rnd.eofff.isobmff.trex.TrackExtendsBoxParser,
            net.frogmouth.rnd.eofff.isobmff.trgr.TrackGroupBoxParser,
            net.frogmouth.rnd.eofff.isobmff.trun.TrackRunBoxParser,
            net.frogmouth.rnd.eofff.isobmff.tsel.TrackSelectionBoxParser,
            net.frogmouth.rnd.eofff.isobmff.ttyp.TrackTypeBoxParser,
            net.frogmouth.rnd.eofff.isobmff.txtC.TextConfigBoxParser,
            net.frogmouth.rnd.eofff.isobmff.udta.UserDataBoxParser,
            net.frogmouth.rnd.eofff.isobmff.uuid.UUIDBoxParser,
            net.frogmouth.rnd.eofff.isobmff.vmhd.VideoMediaHeaderBoxParser,
            net.frogmouth.rnd.eofff.isobmff.xml.XMLBoxParser;

    uses net.frogmouth.rnd.eofff.isobmff.dref.DataReferenceParser;

    provides net.frogmouth.rnd.eofff.isobmff.dref.DataReferenceParser with
            net.frogmouth.rnd.eofff.isobmff.dref.DataEntryImdaBoxParser,
            net.frogmouth.rnd.eofff.isobmff.dref.DataEntrySeqNumImdaBoxParser,
            net.frogmouth.rnd.eofff.isobmff.dref.DataEntryUrlBoxParser,
            net.frogmouth.rnd.eofff.isobmff.dref.DataEntryUrnBoxParser;

    uses net.frogmouth.rnd.eofff.isobmff.grpl.EntityToGroupParser;

    provides net.frogmouth.rnd.eofff.isobmff.grpl.EntityToGroupParser with
            net.frogmouth.rnd.eofff.isobmff.grpl.AlternativesEntityToGroupBoxParser;

    uses net.frogmouth.rnd.eofff.isobmff.iref.ItemReferenceFactory;

    provides net.frogmouth.rnd.eofff.isobmff.iref.ItemReferenceFactory with
            net.frogmouth.rnd.eofff.isobmff.iref.FileDeliveryReferenceFactory,
            net.frogmouth.rnd.eofff.isobmff.iref.FontReferenceFactory,
            net.frogmouth.rnd.eofff.isobmff.iref.ItemLocationReferenceFactory;

    uses net.frogmouth.rnd.eofff.isobmff.trgr.TrackGroupTypeParser;

    provides net.frogmouth.rnd.eofff.isobmff.trgr.TrackGroupTypeParser with
            net.frogmouth.rnd.eofff.isobmff.trgr.MultiSourcePresentationTrackGroupParser,
            net.frogmouth.rnd.eofff.isobmff.trgr.StereoVideoGroupBoxParser;

    uses net.frogmouth.rnd.eofff.isobmff.iprp.PropertyParser;
    uses net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntryParser;

    provides net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntryParser with
            net.frogmouth.rnd.eofff.isobmff.sampleentry.SimpleTextSampleEntryParser,
            net.frogmouth.rnd.eofff.isobmff.sampleentry.XMLMetaDataSampleEntryParser,
            net.frogmouth.rnd.eofff.isobmff.sampleentry.XMLSubtitleSampleEntryParser;

    exports net.frogmouth.rnd.eofff.isobmff;
    exports net.frogmouth.rnd.eofff.isobmff.sampleentry;
    exports net.frogmouth.rnd.eofff.isobmff.co64;
    exports net.frogmouth.rnd.eofff.isobmff.colr;
    exports net.frogmouth.rnd.eofff.isobmff.cprt;
    exports net.frogmouth.rnd.eofff.isobmff.cslg;
    exports net.frogmouth.rnd.eofff.isobmff.ctts;
    exports net.frogmouth.rnd.eofff.isobmff.dinf;
    exports net.frogmouth.rnd.eofff.isobmff.dref;
    exports net.frogmouth.rnd.eofff.isobmff.edts;
    exports net.frogmouth.rnd.eofff.isobmff.elng;
    exports net.frogmouth.rnd.eofff.isobmff.elst;
    exports net.frogmouth.rnd.eofff.isobmff.free;
    exports net.frogmouth.rnd.eofff.isobmff.ftyp;
    exports net.frogmouth.rnd.eofff.isobmff.grpl;
    exports net.frogmouth.rnd.eofff.isobmff.hdlr;
    exports net.frogmouth.rnd.eofff.isobmff.hinf;
    exports net.frogmouth.rnd.eofff.isobmff.hmhd;
    exports net.frogmouth.rnd.eofff.isobmff.hnti;
    exports net.frogmouth.rnd.eofff.isobmff.idat;
    exports net.frogmouth.rnd.eofff.isobmff.iinf;
    exports net.frogmouth.rnd.eofff.isobmff.iloc;
    exports net.frogmouth.rnd.eofff.isobmff.imda;
    exports net.frogmouth.rnd.eofff.isobmff.infe;
    exports net.frogmouth.rnd.eofff.isobmff.iprp;
    exports net.frogmouth.rnd.eofff.isobmff.iref;
    exports net.frogmouth.rnd.eofff.isobmff.kind;
    exports net.frogmouth.rnd.eofff.isobmff.mdat;
    exports net.frogmouth.rnd.eofff.isobmff.mdhd;
    exports net.frogmouth.rnd.eofff.isobmff.mdia;
    exports net.frogmouth.rnd.eofff.isobmff.mebx;
    exports net.frogmouth.rnd.eofff.isobmff.mehd;
    exports net.frogmouth.rnd.eofff.isobmff.meta;
    exports net.frogmouth.rnd.eofff.isobmff.mfhd;
    exports net.frogmouth.rnd.eofff.isobmff.mfra;
    exports net.frogmouth.rnd.eofff.isobmff.mfro;
    exports net.frogmouth.rnd.eofff.isobmff.minf;
    exports net.frogmouth.rnd.eofff.isobmff.moof;
    exports net.frogmouth.rnd.eofff.isobmff.moov;
    exports net.frogmouth.rnd.eofff.isobmff.mvex;
    exports net.frogmouth.rnd.eofff.isobmff.mvhd;
    exports net.frogmouth.rnd.eofff.isobmff.nmhd;
    exports net.frogmouth.rnd.eofff.isobmff.padb;
    exports net.frogmouth.rnd.eofff.isobmff.pasp;
    exports net.frogmouth.rnd.eofff.isobmff.pdin;
    exports net.frogmouth.rnd.eofff.isobmff.pitm;
    exports net.frogmouth.rnd.eofff.isobmff.saio;
    exports net.frogmouth.rnd.eofff.isobmff.saiz;
    exports net.frogmouth.rnd.eofff.isobmff.sbgp;
    exports net.frogmouth.rnd.eofff.isobmff.sdp;
    exports net.frogmouth.rnd.eofff.isobmff.sgpd;
    exports net.frogmouth.rnd.eofff.isobmff.sidx;
    exports net.frogmouth.rnd.eofff.isobmff.smhd;
    exports net.frogmouth.rnd.eofff.isobmff.stbl;
    exports net.frogmouth.rnd.eofff.isobmff.stco;
    exports net.frogmouth.rnd.eofff.isobmff.sthd;
    exports net.frogmouth.rnd.eofff.isobmff.strk;
    exports net.frogmouth.rnd.eofff.isobmff.stsc;
    exports net.frogmouth.rnd.eofff.isobmff.stsd;
    exports net.frogmouth.rnd.eofff.isobmff.stsh;
    exports net.frogmouth.rnd.eofff.isobmff.stss;
    exports net.frogmouth.rnd.eofff.isobmff.stsz;
    exports net.frogmouth.rnd.eofff.isobmff.stts;
    exports net.frogmouth.rnd.eofff.isobmff.styp;
    exports net.frogmouth.rnd.eofff.isobmff.stz2;
    exports net.frogmouth.rnd.eofff.isobmff.tfdt;
    exports net.frogmouth.rnd.eofff.isobmff.tfhd;
    exports net.frogmouth.rnd.eofff.isobmff.tkhd;
    exports net.frogmouth.rnd.eofff.isobmff.traf;
    exports net.frogmouth.rnd.eofff.isobmff.trak;
    exports net.frogmouth.rnd.eofff.isobmff.tref;
    exports net.frogmouth.rnd.eofff.isobmff.trex;
    exports net.frogmouth.rnd.eofff.isobmff.tfra;
    exports net.frogmouth.rnd.eofff.isobmff.trgr;
    exports net.frogmouth.rnd.eofff.isobmff.trun;
    exports net.frogmouth.rnd.eofff.isobmff.tsel;
    exports net.frogmouth.rnd.eofff.isobmff.ttyp;
    exports net.frogmouth.rnd.eofff.isobmff.txtC;
    exports net.frogmouth.rnd.eofff.isobmff.udta;
    exports net.frogmouth.rnd.eofff.isobmff.uuid;
    exports net.frogmouth.rnd.eofff.isobmff.vmhd;
    exports net.frogmouth.rnd.eofff.isobmff.xml;
}
