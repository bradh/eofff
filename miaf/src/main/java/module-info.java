/** Implementation of ISO/IEC 23000-22 "Multi-image application format". */
module net.frogmouth.rnd.eofff.miaf {
    requires com.google.auto.service;
    requires net.frogmouth.rnd.eofff.isobmff;
    requires org.slf4j;

    uses net.frogmouth.rnd.eofff.isobmff.iref.ItemReferenceFactory;

    provides net.frogmouth.rnd.eofff.isobmff.iref.ItemReferenceFactory with
            net.frogmouth.rnd.eofff.miaf.itemreferences.PremultipliedImageReferenceFactory;

    exports net.frogmouth.rnd.eofff.miaf.itemreferences;
}
