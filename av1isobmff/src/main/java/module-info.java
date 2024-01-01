module net.frogmouth.rnd.eofff.av1isobmff {
    requires net.frogmouth.rnd.eofff.imagefileformat;
    requires net.frogmouth.rnd.eofff.isobmff;
    requires net.frogmouth.rnd.eofff.cicp;
    requires org.slf4j;

    uses net.frogmouth.rnd.eofff.isobmff.BoxParser;
    uses net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.PropertyParser;

    provides net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.PropertyParser with
            net.frogmouth.rnd.eofff.av1isobmff.av1C.AV1CodecConfigurationBoxParser;

    exports net.frogmouth.rnd.eofff.av1isobmff.av1C;
}
