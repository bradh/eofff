package net.frogmouth.rnd.eofff.imagefileformat.extensions.properties;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

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
public class ItemPropertiesBox extends BaseBox {
    private ItemPropertyContainerBox itemProperties;
    private final List<ItemPropertyAssociation> propertyAssociations = new ArrayList<>();

    public static FourCC IPRP_ATOM = new FourCC("iprp");

    public ItemPropertiesBox() {
        super(IPRP_ATOM);
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

    public List<AbstractItemProperty> getPropertiesForItem(long itemId) {
        List<AbstractItemProperty> properties = new ArrayList<>();
        for (ItemPropertyAssociation association : propertyAssociations) {
            for (AssociationEntry associationEntry : association.getEntries()) {
                if (associationEntry.getItemId() == itemId) {
                    for (PropertyAssociation propertyAssociation :
                            associationEntry.getAssociations()) {
                        int index = propertyAssociation.getPropertyIndex();
                        properties.add(itemProperties.getProperties().get(index - 1));
                    }
                }
            }
        }
        return properties;
    }

    @Override
    public long getBodySize() {
        long size = 0;
        size += itemProperties.getSize();
        for (ItemPropertyAssociation propertyAssociation : propertyAssociations) {
            size += propertyAssociation.getSize();
        }
        return size;
    }

    @Override
    public void writeTo(OutputStreamWriter writer) throws IOException {
        this.writeBoxHeader(writer);
        itemProperties.writeTo(writer);
        for (ItemPropertyAssociation propertyAssociation : propertyAssociations) {
            propertyAssociation.writeTo(writer);
        }
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
