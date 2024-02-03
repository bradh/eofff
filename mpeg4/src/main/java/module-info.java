@SuppressWarnings("module") // That is not a software version number - its the MPEG version
module net.frogmouth.rnd.eofff.mpeg4 {
    requires net.frogmouth.rnd.eofff.isobmff;
    requires org.slf4j;

    uses net.frogmouth.rnd.eofff.isobmff.BoxParser;

    provides net.frogmouth.rnd.eofff.isobmff.BoxParser with
            net.frogmouth.rnd.eofff.mpeg4.esds.ESDBoxParser,
            net.frogmouth.rnd.eofff.mpeg4.iods.ObjectDescriptorBoxParser,
            net.frogmouth.rnd.eofff.mpeg4.mp4a.MP4AudioSampleEntryParser;

    exports net.frogmouth.rnd.eofff.mpeg4.esds;
    exports net.frogmouth.rnd.eofff.mpeg4.iods;
    exports net.frogmouth.rnd.eofff.mpeg4.mp4a;
}
