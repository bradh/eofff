module net.frogmouth.rnd.eofff.isobmff {
    requires jdk.incubator.foreign;
    requires org.slf4j;

    uses net.frogmouth.rnd.eofff.isobmff.BoxParser;

    provides net.frogmouth.rnd.eofff.isobmff.BoxParser with
            net.frogmouth.rnd.eofff.isobmff.ftyp.FtypBoxParser,
            net.frogmouth.rnd.eofff.isobmff.hdlr.HdlrBoxParser,
            net.frogmouth.rnd.eofff.isobmff.mdat.MediaDataBoxParser,
            net.frogmouth.rnd.eofff.isobmff.meta.ItemInfoBoxParser,
            net.frogmouth.rnd.eofff.isobmff.meta.ItemInfoEntryParser,
            net.frogmouth.rnd.eofff.isobmff.meta.ItemReferenceBoxParser,
            net.frogmouth.rnd.eofff.isobmff.meta.ILocBoxParser,
            net.frogmouth.rnd.eofff.isobmff.meta.MetaBoxParser,
            net.frogmouth.rnd.eofff.isobmff.meta.PitmBoxParser,
            net.frogmouth.rnd.eofff.isobmff.moov.MovieBoxParser;

    exports net.frogmouth.rnd.eofff.isobmff;
    exports net.frogmouth.rnd.eofff.isobmff.ftyp;
    exports net.frogmouth.rnd.eofff.isobmff.hdlr;
    exports net.frogmouth.rnd.eofff.isobmff.mdat;
    exports net.frogmouth.rnd.eofff.isobmff.meta;
    exports net.frogmouth.rnd.eofff.isobmff.moov;
}
