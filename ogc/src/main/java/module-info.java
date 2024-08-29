module net.frogmouth.rnd.eofff.ogc {
    requires net.frogmouth.rnd.eofff.isobmff;
    requires com.google.auto.service;
    requires org.slf4j;

    uses net.frogmouth.rnd.eofff.isobmff.iprp.PropertyParser;

    provides net.frogmouth.rnd.eofff.isobmff.iprp.PropertyParser with
            net.frogmouth.rnd.eofff.ogc.CoordinateReferenceSystemPropertyParser,
            net.frogmouth.rnd.eofff.ogc.ModelTransformationPropertyParser,
            net.frogmouth.rnd.eofff.ogc.ModelTiePointsPropertyParser;

    exports net.frogmouth.rnd.eofff.ogc;
}
