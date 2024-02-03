/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.frogmouth.rnd.eofff.gopro.gpmf;

import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class GPMFItemFactory {
    static GPMFItem getItem(FourCC fourCC, int type, int sampleSize, int repeat) {
        switch (type) {
            case 0 -> {
                return new GPMFContainerItem(fourCC, sampleSize * repeat);
            }
            case 66 -> { // 'B'
                if ((sampleSize == Byte.BYTES) && (repeat == 1)) {
                    return new GPMFUnsignedByteItem(fourCC);
                } else {
                    return new GPMFUnsignedByteMultiItem(fourCC, sampleSize, repeat);
                }
            }
            case 70 -> { // 'F'
                if ((sampleSize == FourCC.BYTES) && (repeat == 1)) {
                    return new GPMFFourCCItem(fourCC);
                } else {
                    // Can be added if needed
                    return new GPMFUnhandledItem(fourCC, sampleSize, repeat);
                }
            }
            case 74 -> {
                if ((sampleSize == Long.BYTES) && (repeat == 1)) {
                    return new GPMFUnsignedLongItem(fourCC);
                } else {
                    return new GPMFUnsignedLongMultiItem(fourCC, sampleSize, repeat);
                }
            }
            case 76 -> { // L
                if ((sampleSize == Integer.BYTES) && (repeat == 1)) {
                    return new GPMFUnsignedIntItem(fourCC);
                } else {
                    return new GPMFUnsignedIntMultiItem(fourCC, sampleSize, repeat);
                }
            }
            case 83 -> { // "S"
                if ((sampleSize == Short.BYTES) && (repeat == 1)) {
                    return new GPMFUnsignedShortItem(fourCC);
                } else {
                    return new GPMFUnsignedShortMultiItem(fourCC, sampleSize, repeat);
                }
            }
            case 85 -> { // "U"
                // Should be UTC Date and Time
                return new GPMFStringItem(fourCC, sampleSize, repeat);
            }
            case 98 -> { // 'b'
                if ((sampleSize == Byte.BYTES) && (repeat == 1)) {
                    return new GPMFSignedByteItem(fourCC);
                } else {
                    return new GPMFSignedByteMultiItem(fourCC, sampleSize, repeat);
                }
            }
            case 99 -> { // 'c'
                return new GPMFStringItem(fourCC, sampleSize, repeat);
            }
            case 102 -> { // 'f'
                if ((sampleSize == Float.BYTES) && (repeat == 1)) {
                    return new GPMFFloatItem(fourCC);
                } else {
                    return new GPMFFloatMultiItem(fourCC, sampleSize, repeat);
                }
            }
            case 108 -> { // 'l'
                if ((sampleSize == Integer.BYTES) && (repeat == 1)) {
                    return new GPMFSignedIntItem(fourCC);
                } else {
                    return new GPMFSignedIntMultiItem(fourCC, sampleSize, repeat);
                }
            }
            case 115 -> { // 's'
                if ((sampleSize == Short.BYTES) && (repeat == 1)) {
                    return new GPMFSignedShortItem(fourCC);
                } else {
                    return new GPMFSignedShortMultiItem(fourCC, sampleSize, repeat);
                }
            }
            default -> {
                System.out.println(String.format("\t ** Unhandled type : %d", type));
                return new GPMFUnhandledItem(fourCC, sampleSize, repeat);
            }
        }
    }
}
