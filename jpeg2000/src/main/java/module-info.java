module net.frogmouth.rnd.eofff.jpeg2000 {
    requires net.frogmouth.rnd.eofff.imagefileformat;
    requires net.frogmouth.rnd.eofff.isobmff;
    requires org.slf4j;

    uses net.frogmouth.rnd.eofff.isobmff.BoxParser;

    provides net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.PropertyParser with
            net.frogmouth.rnd.eofff.jpeg2000.J2KHeaderItemPropertyParser;

    exports net.frogmouth.rnd.eofff.jpeg2000;
}
