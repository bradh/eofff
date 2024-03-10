/**
 * Implementation of ISO/IEC 14496-15 "Carriage of NAL unit structured video in the ISO Base Media
 * File Format".
 *
 * <p>In the family of file formats there are some parts that are ‘building blocks’; primary among
 * them is the ISO Base Media File Format (ISO/IEC 14496-12, partly implemented as the {@code
 * isobmff} module.
 *
 * <p>Another important building block specifies how streams conforming to a set of video standards
 * are stored in file formats that are based on the ISO Base Media File Format. These video
 * standards are, in turn, based on a common structuring concept: the Network Adaptation Layer Unit,
 * or NAL unit. The two standards in question are:
 *
 * <ul>
 *   <li>Advanced Video Coding; this includes not only AVC but also Scalable Video Coding (SVC) and
 *       Multi-view Video Coding (MVC). See ISO/IEC-14496-10.
 *   <li>High Efficiency Video Coding, as described in ITU H.265 and ISO/IEC 23008-2.
 *       <p>That building block was previously called the AVC File Format, but is now called
 *       “Carriage of NAL unit structured video in the ISO Base Media File Format” (ISO/IEC
 *       14496-15). That standard is partly implemented by this module.
 * </ul>
 */
module net.frogmouth.rnd.eofff.nalvideo {
    requires net.frogmouth.rnd.eofff.isobmff;
    requires com.google.auto.service;
    requires org.slf4j;

    uses net.frogmouth.rnd.eofff.isobmff.BoxParser;

    provides net.frogmouth.rnd.eofff.isobmff.BoxParser with
            net.frogmouth.rnd.eofff.nalvideo.AVCConfigurationBoxParser,
            net.frogmouth.rnd.eofff.nalvideo.HEVCConfigurationBoxParser,
            net.frogmouth.rnd.eofff.nalvideo.LHEVCConfigurationBoxParser,
            net.frogmouth.rnd.eofff.nalvideo.btrt.MPEG4BitRateBoxParser;

    uses net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntryParser;

    provides net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntryParser with
            net.frogmouth.rnd.eofff.nalvideo.AVC1SampleEntryParser,
            net.frogmouth.rnd.eofff.nalvideo.HEV1SampleEntryParser,
            net.frogmouth.rnd.eofff.nalvideo.HEV2SampleEntryParser,
            net.frogmouth.rnd.eofff.nalvideo.HVC1SampleEntryParser,
            net.frogmouth.rnd.eofff.nalvideo.HVC2SampleEntryParser,
            net.frogmouth.rnd.eofff.nalvideo.HVT1SampleEntryParser;

    exports net.frogmouth.rnd.eofff.nalvideo;
    exports net.frogmouth.rnd.eofff.nalvideo.btrt;
}
