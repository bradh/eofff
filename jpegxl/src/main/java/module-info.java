module net.frogmouth.rnd.eofff.jpegxl {
    requires net.frogmouth.rnd.eofff.isobmff;
    requires java.desktop;
    requires org.slf4j;

    uses net.frogmouth.rnd.eofff.isobmff.BoxParser;

    provides net.frogmouth.rnd.eofff.isobmff.BoxParser with
            net.frogmouth.rnd.eofff.jpegxl.fileformat.SignatureBoxParser,
            net.frogmouth.rnd.eofff.jpegxl.fileformat.CodestreamBoxParser,
            net.frogmouth.rnd.eofff.jpegxl.fileformat.BitstreamReconstructionDataBoxParser;

    exports net.frogmouth.rnd.eofff.jpegxl.fileformat;
}
