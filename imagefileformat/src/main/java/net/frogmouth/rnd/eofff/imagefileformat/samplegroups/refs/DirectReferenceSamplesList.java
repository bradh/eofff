package net.frogmouth.rnd.eofff.imagefileformat.samplegroups.refs;

import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.samplegroup.VisualSampleGroupEntry;

/**
 * Direct reference samples list (refs).
 *
 * <p>See ISO/IEC 23008-12:2022 Section 7.4.1.
 */
public class DirectReferenceSamplesList extends VisualSampleGroupEntry {
    public static final FourCC REFS_ATOM = new FourCC("refs");

    private long sampleId;
    private final List<Long> directReferenceSampleIds = new ArrayList<>();

    public DirectReferenceSamplesList() {
        super(REFS_ATOM, "DirectReferenceSamplesList");
    }

    public long getSampleId() {
        return sampleId;
    }

    public void setSampleId(long sampleId) {
        this.sampleId = sampleId;
    }

    public List<Long> getDirectReferenceSampleIds() {
        return directReferenceSampleIds;
    }

    public void addDirectReferenceSampleId(long id) {
        directReferenceSampleIds.add(id);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("sample_id=");
        sb.append(sampleId);
        sb.append(", num_direct_reference_samples=");
        sb.append(directReferenceSampleIds.size());
        for (Long id : directReferenceSampleIds) {
            sb.append("\n");
            addIndent(nestingLevel + 1, sb);
            sb.append(id);
        }
        return sb.toString();
    }
}
