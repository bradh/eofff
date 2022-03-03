module net.frogmouth.rnd.eofff.isobmff {
    requires jdk.incubator.foreign;
    requires org.slf4j;

    uses net.frogmouth.rnd.eofff.isobmff.BoxParser;

    provides net.frogmouth.rnd.eofff.isobmff.BoxParser with
            net.frogmouth.rnd.eofff.isobmff.ftyp.FileTypeBoxParser,
            net.frogmouth.rnd.eofff.isobmff.hdlr.HdlrBoxParser,
            net.frogmouth.rnd.eofff.isobmff.mdat.MediaDataBoxParser,
            net.frogmouth.rnd.eofff.isobmff.meta.ItemDataBoxParser,
            net.frogmouth.rnd.eofff.isobmff.meta.ItemInfoBoxParser,
            net.frogmouth.rnd.eofff.isobmff.meta.ItemInfoEntryParser,
            net.frogmouth.rnd.eofff.isobmff.meta.ItemReferenceBoxParser,
            net.frogmouth.rnd.eofff.isobmff.meta.ILocBoxParser,
            net.frogmouth.rnd.eofff.isobmff.meta.MetaBoxParser,
            net.frogmouth.rnd.eofff.isobmff.meta.PitmBoxParser,
            net.frogmouth.rnd.eofff.isobmff.moof.MovieFragmentBoxParser,
            net.frogmouth.rnd.eofff.isobmff.moof.MovieFragmentHeaderBoxParser,
            net.frogmouth.rnd.eofff.isobmff.moof.TrackFragmentBoxParser,
            net.frogmouth.rnd.eofff.isobmff.moof.TrackFragmentDecodeTimeBoxParser,
            net.frogmouth.rnd.eofff.isobmff.moof.TrackFragmentHeaderBoxParser,
            net.frogmouth.rnd.eofff.isobmff.moof.TrackRunBoxParser,
            net.frogmouth.rnd.eofff.isobmff.moov.MediaBoxParser,
            net.frogmouth.rnd.eofff.isobmff.moov.MediaInformationBoxParser,
            net.frogmouth.rnd.eofff.isobmff.moov.MovieBoxParser,
            net.frogmouth.rnd.eofff.isobmff.moov.MovieHeaderBoxParser,
            net.frogmouth.rnd.eofff.isobmff.moov.SampleTableBoxParser,
            net.frogmouth.rnd.eofff.isobmff.moov.TrackBoxParser,
            net.frogmouth.rnd.eofff.isobmff.moov.TrackHeaderBoxParser,
            net.frogmouth.rnd.eofff.isobmff.sidx.SegmentIndexBoxParser,
            net.frogmouth.rnd.eofff.isobmff.styp.SegmentTypeBoxParser;

    exports net.frogmouth.rnd.eofff.isobmff;
    exports net.frogmouth.rnd.eofff.isobmff.ftyp;
    exports net.frogmouth.rnd.eofff.isobmff.hdlr;
    exports net.frogmouth.rnd.eofff.isobmff.mdat;
    exports net.frogmouth.rnd.eofff.isobmff.meta;
    exports net.frogmouth.rnd.eofff.isobmff.moof;
    exports net.frogmouth.rnd.eofff.isobmff.moov;
    exports net.frogmouth.rnd.eofff.isobmff.sidx;
    exports net.frogmouth.rnd.eofff.isobmff.styp;
}
