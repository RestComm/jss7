package org.mobicents.protocols.ss7.map.api.primitives;

/**
 *
 1) Type of network identification 0 0 0 spare (no interpretation) 0 0 1 spare (no interpretation) 0 1 0 national network
 * identification 0 1 1 } to } spare (no interpretation) 1 1 1 }
 *
 * -- values are defined in ANSI T1.113.3.
 *
 * @author Lasith Waruna Perera
 *
 */
public enum NetworkIdentificationTypeValue {

    spare_1(0), spare_2(0x01), nationalNetworkIdentification(0x02), spare_3(0x03), spare_4(0x04), spare_5(0x05), spare_6(0x06), spare_7(
            0x07);

    private int code;

    private NetworkIdentificationTypeValue(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static NetworkIdentificationTypeValue getInstance(int code) {
        switch (code) {
            case 0:
                return NetworkIdentificationTypeValue.spare_1;
            case 0x01:
                return NetworkIdentificationTypeValue.spare_2;
            case 0x02:
                return NetworkIdentificationTypeValue.nationalNetworkIdentification;
            case 0x03:
                return NetworkIdentificationTypeValue.spare_3;
            case 0x04:
                return NetworkIdentificationTypeValue.spare_4;
            case 0x05:
                return NetworkIdentificationTypeValue.spare_5;
            case 0x06:
                return NetworkIdentificationTypeValue.spare_6;
            case 0x07:
                return NetworkIdentificationTypeValue.spare_7;
            default:
                return null;
        }
    }

}
