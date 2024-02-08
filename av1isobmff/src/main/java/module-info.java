module net.frogmouth.rnd.eofff.av1isobmff {
    requires net.frogmouth.rnd.eofff.isobmff;
    requires net.frogmouth.rnd.eofff.cicp;
    requires org.slf4j;

    uses net.frogmouth.rnd.eofff.isobmff.BoxParser;
    uses net.frogmouth.rnd.eofff.isobmff.iprp.PropertyParser;

    provides net.frogmouth.rnd.eofff.isobmff.iprp.PropertyParser with
            net.frogmouth.rnd.eofff.av1isobmff.av1C.AV1CodecConfigurationBoxParser;

    exports net.frogmouth.rnd.eofff.av1isobmff.av1C;
}
