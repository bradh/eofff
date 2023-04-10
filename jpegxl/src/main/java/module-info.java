module net.frogmouth.rnd.eofff.jpegxl {
    requires jdk.incubator.foreign;
    requires net.frogmouth.rnd.eofff.isobmff;
    requires org.slf4j;

    uses net.frogmouth.rnd.eofff.isobmff.BoxParser;

    provides net.frogmouth.rnd.eofff.isobmff.BoxParser with
            net.frogmouth.rnd.eofff.jpegxl.fileformat.SignatureBoxParser;

    exports net.frogmouth.rnd.eofff.jpegxl.fileformat;
}
