package net.frogmouth.rnd.eofff.nalvideo;

import static net.frogmouth.rnd.eofff.nalvideo.FormatUtils.addIndent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class HEVCDecoderConfigurationArray {
    private boolean array_completion;
    private int nal_unit_type;
    private final List<NALU> nalus = new ArrayList<>();

    public boolean isArray_completion() {
        return array_completion;
    }

    public void setArray_completion(boolean array_completion) {
        this.array_completion = array_completion;
    }

    public int getNal_unit_type() {
        return nal_unit_type;
    }

    public void setNal_unit_type(int nal_unit_type) {
        this.nal_unit_type = nal_unit_type;
    }

    public void addNALU(NALU nalu) {
        this.nalus.add(nalu);
    }

    public List<NALU> getNALUs() {
        return new ArrayList<>(this.nalus);
    }

    public int getNumBytes() {
        int count = 0;
        count += 1;
        count += 2;
        for (NALU nalu : nalus) {
            count += (nalu.getNumBytes());
        }
        return count;
    }

    public void writeTo(OutputStreamWriter writer) throws IOException {
        writer.writeByte((array_completion ? 0x80 : 0) | (nal_unit_type & 0b111111));
        writer.writeUnsignedInt16(nalus.size());
        for (NALU nalu : nalus) {
            nalu.writeTo(writer);
        }
    }

    public void addToStringBuilder(int nestingLevel, StringBuilder sb) {
        sb.append("\n");
        addIndent(nestingLevel, sb);
        sb.append("Array:");
        sb.append("\n");
        addIndent(nestingLevel + 1, sb);
        sb.append("array_completeness: ");
        sb.append(this.array_completion);
        sb.append("\n");
        addIndent(nestingLevel + 1, sb);
        sb.append("NAL_unit_type: ");
        sb.append(nal_unit_type);
        sb.append("\n");
        addIndent(nestingLevel + 1, sb);
        sb.append("numNalus: ");
        sb.append(nalus.size());
        for (NALU nalu : nalus) {
            sb.append("\n");
            addIndent(nestingLevel + 1, sb);
            nalu.addToStringBuilder(nestingLevel + 1, sb);
        }
    }
}
