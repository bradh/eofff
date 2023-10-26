/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.frogmouth.rnd.eofff.tools.gimi;

import org.jmisb.api.klv.IKlvKey;
import org.jmisb.api.klv.IKlvValue;
import org.jmisb.api.klv.IMisbMessage;
import org.jmisb.api.klv.INestedKlvValue;
import org.jmisb.core.klv.ArrayUtils;

public class MetadataUtils {

    protected static void dumpMisbMessage(IMisbMessage misb) {
        outputTopLevelMessageHeader(misb);
        outputNestedKlvValue(misb, 1);
    }

    protected static void outputNestedKlvValue(
            INestedKlvValue nestedKlvValue, int indentationLevel) {
        for (IKlvKey identifier : nestedKlvValue.getIdentifiers()) {
            IKlvValue value = nestedKlvValue.getField(identifier);
            outputValueWithIndentation(value, indentationLevel);
            // if this has nested content, output that at the next indentation level
            if (value instanceof INestedKlvValue iNestedKlvValue) {
                outputNestedKlvValue(iNestedKlvValue, indentationLevel + 1);
            }
        }
    }

    protected static void outputTopLevelMessageHeader(IMisbMessage misbMessage) {
        String displayHeader = misbMessage.displayHeader();
        if (displayHeader.equalsIgnoreCase("Unknown")) {
            System.out.println(
                    displayHeader
                            + " ["
                            + ArrayUtils.toHexString(misbMessage.getUniversalLabel().getBytes())
                                    .trim()
                            + "]");
            outputUnknownMessageContent(misbMessage.frameMessage(true));
        } else {
            System.out.println(displayHeader);
        }
    }

    protected static void outputUnknownMessageContent(byte[] frameMessage) {
        System.out.println(ArrayUtils.toHexString(frameMessage, 16, true));
    }

    protected static void outputValueWithIndentation(IKlvValue value, int indentationLevel) {
        for (int i = 0; i < indentationLevel; ++i) {
            System.out.print("\t");
        }
        System.out.println(value.getDisplayName() + ": " + value.getDisplayableValue());
    }
}
