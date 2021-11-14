package net.frogmouth.rnd.eofff.isobmff.meta;

import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.meta.propertyassociation.ItemPropertyAssociation;

/**
 * Item Properties Box.
 *
 * <p>The ItemPropertiesBox enables the association of any item with an ordered set of item
 * properties. Item properties are small data records.
 *
 * <p>The ItemPropertiesBox consists of two parts: ItemPropertyContainerBox that contains an
 * implicitly indexed list of item properties, and one or more ItemPropertyAssociation boxes that
 * associate items with item properties.
 *
 * <p>Refer to ISO/IEC 23008-12:2017(E) Section 9.3 "Item Properties Box".
 */
public class ItemPropertiesBox extends FullBox {
    private ItemPropertyContainerBox itemProperties;
    private final List<ItemPropertyAssociation> propertyAssociations = new ArrayList<>();

    public ItemPropertiesBox(long size, FourCC name) {
        super(size, name);
    }

    @Override
    public String getFullName() {
        return "ItemPropertiesBox";
    }

    public ItemPropertyContainerBox getItemProperties() {
        return itemProperties;
    }

    public void setItemProperties(ItemPropertyContainerBox itemProperties) {
        this.itemProperties = itemProperties;
    }

    public List<ItemPropertyAssociation> getItemPropertyAssociations() {
        return new ArrayList<>(this.propertyAssociations);
    }

    public void addItemPropertyAssociation(ItemPropertyAssociation association) {
        this.propertyAssociations.add(association);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        sb.append("': ");
        sb.append("\n\t  ");
        sb.append(itemProperties.toString());
        for (ItemPropertyAssociation itemPropertyAssociation : propertyAssociations) {
            sb.append("\n\t");
            sb.append(itemPropertyAssociation.toString());
        }
        return sb.toString();
    }
}
