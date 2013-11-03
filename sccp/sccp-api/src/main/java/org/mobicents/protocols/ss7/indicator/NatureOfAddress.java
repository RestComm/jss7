/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.protocols.ss7.indicator;

import java.io.IOException;

/**
 * Refer ITU-T Q.713 Page 9 of pdf Nature of address indicator.
 *
 * @author kulikov
 * @author amit bhayani
 */
public enum NatureOfAddress {
    UNKNOWN(0), SUBSCRIBER(1), RESERVED_NATIONAL_2(2), NATIONAL(3), INTERNATIONAL(4), SPARE_5(5), SPARE_6(6), SPARE_7(7), SPARE_8(
            8), SPARE_9(9), SPARE_10(10), SPARE_11(11), SPARE_12(12), SPARE_13(13), SPARE_14(14), SPARE_15(15), SPARE_16(16), SPARE_17(
            17), SPARE_18(18), SPARE_19(19), SPARE_20(20), SPARE_21(21), SPARE_22(22), SPARE_23(23), SPARE_24(24), SPARE_25(25), SPARE_26(
            26), SPARE_27(27), SPARE_28(28), SPARE_29(29), SPARE_30(30), SPARE_31(31), SPARE_32(32), SPARE_33(33), SPARE_34(34), SPARE_35(
            35), SPARE_36(36), SPARE_37(37), SPARE_38(38), SPARE_39(39), SPARE_40(40), SPARE_41(41), SPARE_42(42), SPARE_43(43), SPARE_44(
            44), SPARE_45(45), SPARE_46(46), SPARE_47(47), SPARE_48(48), SPARE_49(49), SPARE_50(50), SPARE_51(51), SPARE_52(52), SPARE_53(
            53), SPARE_54(54), SPARE_55(55), SPARE_56(56), SPARE_57(57), SPARE_58(58), SPARE_59(59), SPARE_60(60), SPARE_61(61), SPARE_62(
            62), SPARE_63(63), SPARE_64(64), SPARE_65(65), SPARE_66(66), SPARE_67(67), SPARE_68(68), SPARE_69(69), SPARE_70(70), SPARE_71(
            71), SPARE_72(72), SPARE_73(73), SPARE_74(74), SPARE_75(75), SPARE_76(76), SPARE_77(77), SPARE_78(78), SPARE_79(79), SPARE_80(
            80), SPARE_81(81), SPARE_82(82), SPARE_83(83), SPARE_84(84), SPARE_85(85), SPARE_86(86), SPARE_87(87), SPARE_88(88), SPARE_89(
            89), SPARE_90(90), SPARE_91(91), SPARE_92(92), SPARE_93(93), SPARE_94(94), SPARE_95(95), SPARE_96(96), SPARE_97(97), SPARE_98(
            98), SPARE_99(99), SPARE_100(100), SPARE_101(101), SPARE_102(102), SPARE_103(103), SPARE_104(104), SPARE_105(105), SPARE_106(
            106), SPARE_107(107), SPARE_108(108), SPARE_109(109), SPARE_110(110), SPARE_111(111), RESERVED_NATIONAL_112(112), RESERVED_NATIONAL_113(
            113), RESERVED_NATIONAL_114(114), RESERVED_NATIONAL_115(115), RESERVED_NATIONAL_116(116), RESERVED_NATIONAL_117(117), RESERVED_NATIONAL_118(
            118), RESERVED_NATIONAL_119(119), RESERVED_NATIONAL_120(120), RESERVED_NATIONAL_121(121), RESERVED_NATIONAL_122(122), RESERVED_NATIONAL_123(
            123), RESERVED_NATIONAL_124(124), RESERVED_NATIONAL_125(125), RESERVED_NATIONAL_126(126), RESERVED(127);

    private int value;

    private NatureOfAddress(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static NatureOfAddress valueOf(int v) throws IOException {
        switch (v) {
            case 0:
                return UNKNOWN;
            case 1:
                return SUBSCRIBER;
            case 2:
                return RESERVED_NATIONAL_2;
            case 3:
                return NATIONAL;
            case 4:
                return INTERNATIONAL;
            case 5:
                return SPARE_5;
            case 6:
                return SPARE_6;
            case 7:
                return SPARE_7;
            case 8:
                return SPARE_8;
            case 9:
                return SPARE_9;
            case 10:
                return SPARE_10;
            case 11:
                return SPARE_11;
            case 12:
                return SPARE_12;
            case 13:
                return SPARE_13;
            case 14:
                return SPARE_14;
            case 15:
                return SPARE_15;
            case 16:
                return SPARE_16;
            case 17:
                return SPARE_17;
            case 18:
                return SPARE_18;
            case 19:
                return SPARE_19;
            case 20:
                return SPARE_20;
            case 21:
                return SPARE_21;
            case 22:
                return SPARE_22;
            case 23:
                return SPARE_23;
            case 24:
                return SPARE_24;
            case 25:
                return SPARE_25;
            case 26:
                return SPARE_26;
            case 27:
                return SPARE_27;
            case 28:
                return SPARE_28;
            case 29:
                return SPARE_29;
            case 30:
                return SPARE_30;
            case 31:
                return SPARE_31;
            case 32:
                return SPARE_32;
            case 33:
                return SPARE_33;
            case 34:
                return SPARE_34;
            case 35:
                return SPARE_35;
            case 36:
                return SPARE_36;
            case 37:
                return SPARE_37;
            case 38:
                return SPARE_38;
            case 39:
                return SPARE_39;
            case 40:
                return SPARE_40;
            case 41:
                return SPARE_41;
            case 42:
                return SPARE_42;
            case 43:
                return SPARE_43;
            case 44:
                return SPARE_44;
            case 45:
                return SPARE_45;
            case 46:
                return SPARE_46;
            case 47:
                return SPARE_47;
            case 48:
                return SPARE_48;
            case 49:
                return SPARE_49;
            case 50:
                return SPARE_50;
            case 51:
                return SPARE_51;
            case 52:
                return SPARE_52;
            case 53:
                return SPARE_53;
            case 54:
                return SPARE_54;
            case 55:
                return SPARE_55;
            case 56:
                return SPARE_56;
            case 57:
                return SPARE_57;
            case 58:
                return SPARE_58;
            case 59:
                return SPARE_59;
            case 60:
                return SPARE_60;
            case 61:
                return SPARE_61;
            case 62:
                return SPARE_62;
            case 63:
                return SPARE_63;
            case 64:
                return SPARE_64;
            case 65:
                return SPARE_65;
            case 66:
                return SPARE_66;
            case 67:
                return SPARE_67;
            case 68:
                return SPARE_68;
            case 69:
                return SPARE_69;
            case 70:
                return SPARE_70;
            case 71:
                return SPARE_71;
            case 72:
                return SPARE_72;
            case 73:
                return SPARE_73;
            case 74:
                return SPARE_74;
            case 75:
                return SPARE_75;
            case 76:
                return SPARE_76;
            case 77:
                return SPARE_77;
            case 78:
                return SPARE_78;
            case 79:
                return SPARE_79;
            case 80:
                return SPARE_80;
            case 81:
                return SPARE_81;
            case 82:
                return SPARE_82;
            case 83:
                return SPARE_83;
            case 84:
                return SPARE_84;
            case 85:
                return SPARE_85;
            case 86:
                return SPARE_86;
            case 87:
                return SPARE_87;
            case 88:
                return SPARE_88;
            case 89:
                return SPARE_89;
            case 90:
                return SPARE_90;
            case 91:
                return SPARE_91;
            case 92:
                return SPARE_92;
            case 93:
                return SPARE_93;
            case 94:
                return SPARE_94;
            case 95:
                return SPARE_95;
            case 96:
                return SPARE_96;
            case 97:
                return SPARE_97;
            case 98:
                return SPARE_98;
            case 99:
                return SPARE_99;
            case 100:
                return SPARE_100;
            case 101:
                return SPARE_101;
            case 102:
                return SPARE_102;
            case 103:
                return SPARE_103;
            case 104:
                return SPARE_104;
            case 105:
                return SPARE_105;
            case 106:
                return SPARE_106;
            case 107:
                return SPARE_107;
            case 108:
                return SPARE_108;
            case 109:
                return SPARE_109;
            case 110:
                return SPARE_110;
            case 111:
                return SPARE_111;
            case 112:
                return RESERVED_NATIONAL_112;
            case 113:
                return RESERVED_NATIONAL_113;
            case 114:
                return RESERVED_NATIONAL_114;
            case 115:
                return RESERVED_NATIONAL_115;
            case 116:
                return RESERVED_NATIONAL_116;
            case 117:
                return RESERVED_NATIONAL_117;
            case 118:
                return RESERVED_NATIONAL_118;
            case 119:
                return RESERVED_NATIONAL_119;
            case 120:
                return RESERVED_NATIONAL_120;
            case 121:
                return RESERVED_NATIONAL_121;
            case 122:
                return RESERVED_NATIONAL_122;
            case 123:
                return RESERVED_NATIONAL_123;
            case 124:
                return RESERVED_NATIONAL_124;
            case 125:
                return RESERVED_NATIONAL_125;
            case 126:
                return RESERVED_NATIONAL_126;
            case 127:
                return RESERVED;
            default:
                throw new IOException("Unrecognized Nature of Address. Must be between 0 to 127 and value is=" + v);
        }
    }
}
