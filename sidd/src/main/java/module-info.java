module net.frogmouth.rnd.eofff.sidd {
    requires jakarta.activation;
    requires jakarta.xml.bind;

    exports net.frogmouth.rnd.eofff.sidd;
    exports net.frogmouth.rnd.eofff.sidd.v2.gen;
    exports net.frogmouth.rnd.eofff.sidd.sicommon.v1.gen;

    opens net.frogmouth.rnd.eofff.sidd.v2.gen;
    opens net.frogmouth.rnd.eofff.sidd.sicommon.v1.gen;
    opens net.frogmouth.rnd.eofff.sidd.sfa.gen;
    opens net.frogmouth.rnd.eofff.sidd.ism.v13.gen;
    opens net.frogmouth.rnd.eofff.sidd.ism.v13.gen.classificationall;
    opens net.frogmouth.rnd.eofff.sidd.ism.v13.gen.complieswith;
    opens net.frogmouth.rnd.eofff.sidd.ism.v13.gen.dissem;
    opens net.frogmouth.rnd.eofff.sidd.ism.v13.gen.exemptfrom;
    opens net.frogmouth.rnd.eofff.sidd.ism.v13.gen.nonuscontrols;
    opens net.frogmouth.rnd.eofff.sidd.ism.v13.gen.notice;
    opens net.frogmouth.rnd.eofff.sidd.ism.v13.gen.poctype;
}
