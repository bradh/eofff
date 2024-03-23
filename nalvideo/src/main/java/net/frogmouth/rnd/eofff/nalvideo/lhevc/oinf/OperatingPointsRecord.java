package net.frogmouth.rnd.eofff.nalvideo.lhevc.oinf;

import static net.frogmouth.rnd.eofff.nalvideo.FormatUtils.addIndent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

/**
 * Operating points record.
 *
 * <p>See ISO/IEC 14496-15:2022 9.6.2.
 */
public class OperatingPointsRecord {

    private int scalabilityMask;
    private final List<ProfileTierLevel> profileTierLevels = new ArrayList<>();
    private final List<OperatingPoint> operatingPoints = new ArrayList<>();
    private final List<LayerInfo> layerInfos = new ArrayList<>();

    public static OperatingPointsRecord parseFrom(ParseContext parseContext, long limit) {
        OperatingPointsRecord record = new OperatingPointsRecord();
        record.setScalabilityMask(parseContext.readUnsignedInt16());
        int numProfileTierLevel = parseContext.readUnsignedInt8() & 0x3F;
        for (int i = 0; i < numProfileTierLevel; i++) {
            ProfileTierLevel profileTierLevel = new ProfileTierLevel();
            profileTierLevel.parseFrom(parseContext);
            record.appendProfileTierLevel(profileTierLevel);
        }
        int numOperatingPoints = parseContext.readUnsignedInt16();
        for (int i = 0; i < numOperatingPoints; i++) {
            OperatingPoint operatingPoint = new OperatingPoint();
            operatingPoint.parseFrom(parseContext);
            record.appendOperatingPoint(operatingPoint);
        }
        int maxLayerCount = parseContext.readUnsignedInt8();
        for (int i = 0; i < maxLayerCount; i++) {
            LayerInfo layerInfo = new LayerInfo();
            layerInfo.parseFrom(parseContext, record.scalabilityMask);
            record.appendLayerInfo(layerInfo);
        }
        return record;
    }

    public void setScalabilityMask(int scalabilityMask) {
        this.scalabilityMask = scalabilityMask;
    }

    public int getScalabilityMask() {
        return scalabilityMask;
    }

    public List<ProfileTierLevel> getProfileTierLevels() {
        return profileTierLevels;
    }

    public void appendProfileTierLevel(ProfileTierLevel profileTierLevel) {
        profileTierLevels.add(profileTierLevel);
    }

    public List<OperatingPoint> getOperatingPoints() {
        return operatingPoints;
    }

    public void appendOperatingPoint(OperatingPoint operatingPoint) {
        operatingPoints.add(operatingPoint);
    }

    public List<LayerInfo> getLayerInfos() {
        return layerInfos;
    }

    public void appendLayerInfo(LayerInfo layerInfo) {
        layerInfos.add(layerInfo);
    }

    public long getSize() {
        long size = 0;
        // TODO
        return size;
    }

    public void addToStringBuilder(StringBuilder sb, int nestingLevel) {
        sb.append("\n");
        addIndent(nestingLevel, sb);
        sb.append("scalability_mask=");
        sb.append(scalabilityMask);
        sb.append(", num_profile_tier_level=");
        sb.append(profileTierLevels.size());
        for (ProfileTierLevel l : this.profileTierLevels) {
            sb.append("\n");
            addIndent(nestingLevel + 1, sb);
            sb.append("general_profile_space=");
            sb.append(l.getGeneral_profile_space());
            sb.append(", general_tier_flag=");
            sb.append(l.getGeneral_tier_flag());
            sb.append(", general_profile_idc=");
            sb.append(l.getGeneral_profile_idc());
            sb.append(", general_profile_compatibility_flags=");
            sb.append(
                    HexFormat.of()
                            .withPrefix("0x")
                            .withDelimiter(" ")
                            .formatHex(l.getGeneral_profile_compatibility_flags()));
            sb.append(", general_constraint_indicator_flags=");
            sb.append(
                    HexFormat.of()
                            .withPrefix("0x")
                            .withDelimiter(" ")
                            .formatHex(l.getGeneral_constraint_indicator_flags()));
            sb.append(", general_level_idc=");
            sb.append(l.getGeneral_level_idc());
        }
        sb.append("\n");
        addIndent(nestingLevel, sb);
        sb.append("num_operating_points=");
        sb.append(this.operatingPoints.size());
        for (OperatingPoint p : this.operatingPoints) {
            sb.append("\n");
            addIndent(nestingLevel + 1, sb);
            sb.append("output_layer_set_idx=");
            sb.append(p.getOutputLayerSetIdx());
            sb.append(", max_temporal_id=");
            sb.append(p.getMaxTemporalId());
            sb.append(", layer_count=");
            sb.append(p.getLayers().size());
            for (OperatingPoint.Layer l : p.getLayers()) {
                sb.append("\n");
                addIndent(nestingLevel + 2, sb);
                sb.append("ptl_idx=");
                sb.append(l.ptlIdx());
                sb.append(", layer_id=");
                sb.append(l.layerId());
                sb.append(", is_outputlayer=");
                sb.append(l.outputLayer());
                sb.append(", is_alternate_outputlayer=");
                sb.append(l.alternateOutputLayer());
            }
            sb.append("\n");
            addIndent(nestingLevel + 1, sb);
            sb.append("minPicWidth=");
            sb.append(p.getMinPicWidth());
            sb.append(", minPicHeight=");
            sb.append(p.getMinPicHeight());
            sb.append(", maxPicWidth=");
            sb.append(p.getMaxPicWidth());
            sb.append(", maxPicHeight=");
            sb.append(p.getMaxPicHeight());
            sb.append(", maxChromaFormat=");
            sb.append(p.getMaxChromaFormat());
            sb.append(", maxBitDepthMinus8=");
            sb.append(p.getMaxBitDepthMinus8());
            if (p.getAvgFrameRate() != null) {
                sb.append(", avgFrameRate=");
                sb.append(p.getAvgFrameRate());
            }
            if (p.getConstantFrameRate() != null) {
                sb.append(", constantFrameRate=");
                sb.append(p.getConstantFrameRate());
            }
            if (p.getMaxBitRate() != null) {
                sb.append(", maxBitRate=");
                sb.append(p.getMaxBitRate());
            }
            if (p.getAvgBitRate() != null) {
                sb.append(", avgBitRate=");
                sb.append(p.getAvgBitRate());
            }
        }
        sb.append("\n");
        addIndent(nestingLevel, sb);
        sb.append("max_layer_count=");
        sb.append(this.layerInfos.size());
        for (LayerInfo l : this.layerInfos) {
            sb.append("\n");
            addIndent(nestingLevel + 1, sb);
            sb.append("layerID=");
            sb.append(l.getLayerID());
            sb.append(", num_direct_ref_layers=");
            sb.append(l.getDirectRefLayerIDs().size());
            sb.append(" [");
            for (var id : l.getDirectRefLayerIDs()) {
                sb.append(id);
                sb.append(" ");
            }
            sb.append("], dimension_identifiers=[");
            for (var id : l.getDimensionIdentifiers()) {
                if (id == -1) {
                    sb.append(".");
                } else {
                    sb.append(id);
                }
                sb.append(" ");
            }
            sb.append("]");
        }
    }

    public void writeTo(OutputStreamWriter stream) throws IOException {
        // TODO
    }
}
