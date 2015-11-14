package org.mobicents.protocols.ss7.map.api.primitives;

/**
 *
 Network identification plan 0 0 0 0 unknown (no interpretation) 0 0 0 1 3 - digit carrier identification 0 0 1 0 4 - digit
 * carrier identification 0 0 1 1 } to } spare (no interpretation) 1 1 1 1 }
 *
 * -- values are defined in ANSI T1.113.3.
 *
 * @author Lasith Waruna Perera
 *
 */
public enum NetworkIdentificationPlanValue {

    unknown(0), threeDigitCarrierIdentification(0x01), fourDigitCarrierIdentification(0x02), spare_1(0x03), spare_2(0x04), spare_3(
            0x05), spare_4(0x06), spare_5(0x07), spare_6(0x08), spare_7(0x09), spare_8(0x0A), spare_9(0x0B), spare_10(0x0C), spare_11(
            0x0D), spare_12(0x0E), spare_13(0x0F);

    private int code;

    private NetworkIdentificationPlanValue(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static NetworkIdentificationPlanValue getInstance(int code) {
        switch (code) {
            case 0:
                return NetworkIdentificationPlanValue.unknown;
            case 0x01:
                return NetworkIdentificationPlanValue.threeDigitCarrierIdentification;
            case 0x02:
                return NetworkIdentificationPlanValue.fourDigitCarrierIdentification;
            case 0x03:
                return NetworkIdentificationPlanValue.spare_1;
            case 0x04:
                return NetworkIdentificationPlanValue.spare_2;
            case 0x05:
                return NetworkIdentificationPlanValue.spare_3;
            case 0x06:
                return NetworkIdentificationPlanValue.spare_4;
            case 0x07:
                return NetworkIdentificationPlanValue.spare_5;
            case 0x08:
                return NetworkIdentificationPlanValue.spare_6;
            case 0x09:
                return NetworkIdentificationPlanValue.spare_7;
            case 0x0A:
                return NetworkIdentificationPlanValue.spare_8;
            case 0x0B:
                return NetworkIdentificationPlanValue.spare_9;
            case 0x0C:
                return NetworkIdentificationPlanValue.spare_10;
            case 0x0D:
                return NetworkIdentificationPlanValue.spare_11;
            case 0x0E:
                return NetworkIdentificationPlanValue.spare_12;
            case 0x0F:
                return NetworkIdentificationPlanValue.spare_13;
            default:
                return null;
        }
    }

}
