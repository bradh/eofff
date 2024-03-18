package net.frogmouth.rnd.eofff.isobmff.leva;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class LevelAssignment {

    private long trackID;
    private boolean paddingFlag;
    private int assignmentType;

    private FourCC groupingType = null;
    private long groupingTypeParameter = 0;
    private long subTrackID = 0;

    public long getTrackID() {
        return trackID;
    }

    public void setTrackID(long trackID) {
        this.trackID = trackID;
    }

    public boolean isPaddingFlag() {
        return paddingFlag;
    }

    public void setPaddingFlag(boolean paddingFlag) {
        this.paddingFlag = paddingFlag;
    }

    public int getAssignmentType() {
        return assignmentType;
    }

    public void setAssignmentType(int assignmentType) {
        this.assignmentType = assignmentType;
    }

    public FourCC getGroupingType() {
        return groupingType;
    }

    public void setGroupingType(FourCC groupingType) {
        this.groupingType = groupingType;
    }

    public long getGroupingTypeParameter() {
        return groupingTypeParameter;
    }

    public void setGroupingTypeParameter(long groupingTypeParameter) {
        this.groupingTypeParameter = groupingTypeParameter;
    }

    public long getSubTrackID() {
        return subTrackID;
    }

    public void setSubTrackID(long subTrackID) {
        this.subTrackID = subTrackID;
    }

    void writeTo(OutputStreamWriter stream) throws IOException {
        stream.writeUnsignedInt32(trackID);
        int paddingAndAssignment = (paddingFlag ? 0x80 : 0x00);
        paddingAndAssignment |= (assignmentType & 0x7F);
        stream.writeUnsignedInt8(paddingAndAssignment);
        switch (assignmentType) {
            case 0 -> stream.writeFourCC(groupingType);
            case 1 -> {
                stream.writeFourCC(groupingType);
                stream.writeUnsignedInt32(groupingTypeParameter);
            }
            case 2 -> {}
            case 3 -> {}
            case 4 -> stream.writeUnsignedInt32(subTrackID);
            default -> {}
        }
    }

    public long getSize() {
        long size = 0;
        size += Integer.BYTES;
        size += Byte.BYTES; // padding_flag and assignment_type
        switch (assignmentType) {
            case 0:
                size += FourCC.BYTES;
                break;
            case 1:
                size += FourCC.BYTES;
                size += Integer.BYTES;
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                size += Integer.BYTES;
                break;
            default:
                break;
        }
        return size;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("trackID=");
        sb.append(trackID);
        sb.append(", paddingFlag=");
        sb.append(paddingFlag);
        sb.append(", assignmentType=");
        sb.append(assignmentType);
        switch (assignmentType) {
            case 0:
                sb.append(", groupingType=");
                sb.append(groupingType);
                break;
            case 1:
                sb.append(", groupingType=");
                sb.append(groupingType);
                sb.append(", groupingTypeParameter=");
                sb.append(groupingTypeParameter);
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                sb.append(", subTrackID=");
                sb.append(subTrackID);
                break;
            default:
                break;
        }
        return sb.toString();
    }
}
