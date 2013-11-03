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

package org.mobicents.protocols.ss7.map.datacoding;

import org.mobicents.protocols.ss7.map.api.datacoding.CBSDataCodingGroup;
import org.mobicents.protocols.ss7.map.api.datacoding.CBSDataCodingScheme;
import org.mobicents.protocols.ss7.map.api.datacoding.CBSNationalLanguage;
import org.mobicents.protocols.ss7.map.api.smstpdu.CharacterSet;
import org.mobicents.protocols.ss7.map.api.smstpdu.DataCodingSchemaMessageClass;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class CBSDataCodingSchemeImpl implements CBSDataCodingScheme {

    private int code;

    public CBSDataCodingSchemeImpl(int code) {
        this.code = code;
    }

    public CBSDataCodingSchemeImpl(CBSDataCodingGroup dataCodingGroup, CharacterSet characterSet,
            CBSNationalLanguage nationalLanguageShiftTable, DataCodingSchemaMessageClass messageClass, boolean isCompressed) {

        if (dataCodingGroup == null) {
            this.code = 15;
            return;
        }

        switch (dataCodingGroup) {
            case GeneralGsm7:
                if (nationalLanguageShiftTable != null)
                    this.code = nationalLanguageShiftTable.getCode();
                else
                    this.code = 15; // default language
                break;
            case GeneralWithLanguageIndication:
                if (characterSet == CharacterSet.GSM7) {
                    this.code = 0x10;
                } else {
                    this.code = 0x11;
                }
                break;
            case GeneralDataCodingIndication:
                this.code = 0x40;
                if (isCompressed)
                    this.code |= 0x20;
                if (messageClass != null)
                    this.code |= (0x10 + messageClass.getCode());
                if (characterSet != null)
                    this.code |= (characterSet.getCode() << 2);
                break;
            case MessageWithUserDataHeader:
                this.code = 0x90;
                if (messageClass != null)
                    this.code |= messageClass.getCode();
                if (characterSet != null)
                    this.code |= (characterSet.getCode() << 2);
                break;
            case I1ProtocolMessage:
                this.code = 0xD0;
                break;
            case DefinedByTheWAPForum:
                this.code = 0xE0;
                break;
            case DataCodingMessageClass:
                this.code = 0xF0;
                if (messageClass != null)
                    this.code |= messageClass.getCode();
                if (characterSet != null && characterSet == CharacterSet.GSM8)
                    this.code |= 0x04;
                break;
            default:
                this.code = 15;
                break;
        }
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public CBSDataCodingGroup getDataCodingGroup() {
        switch ((this.code & 0xF0) >> 4) {
            case 0:
            case 2:
            case 3:
                return CBSDataCodingGroup.GeneralGsm7;
            case 1:
                return CBSDataCodingGroup.GeneralWithLanguageIndication;
            case 9:
                return CBSDataCodingGroup.MessageWithUserDataHeader;
            case 13:
                return CBSDataCodingGroup.I1ProtocolMessage;
            case 14:
                return CBSDataCodingGroup.DefinedByTheWAPForum;
            case 15:
                return CBSDataCodingGroup.DataCodingMessageClass;

            default: {
                if ((this.code & 0xC0) == 0x40) {
                    return CBSDataCodingGroup.GeneralDataCodingIndication;
                }
            }
        }

        return CBSDataCodingGroup.Reserved;
    }

    @Override
    public CBSNationalLanguage getNationalLanguageShiftTable() {
        if (this.getDataCodingGroup() == CBSDataCodingGroup.GeneralGsm7)
            return CBSNationalLanguage.getInstance((code & 0xF0) >> 4, (code & 0x0F));
        else
            return null;
    }

    @Override
    public CharacterSet getCharacterSet() {
        switch (this.getDataCodingGroup()) {
            case GeneralGsm7:
                return CharacterSet.GSM7;
            case GeneralWithLanguageIndication:
                if ((this.code & 0x0F) == 1)
                    return CharacterSet.UCS2;
                else
                    return CharacterSet.GSM7;
            case GeneralDataCodingIndication:
            case MessageWithUserDataHeader:
                return CharacterSet.getInstance((code & 0x0C) >> 2);
            case DataCodingMessageClass:
                if ((code & 0x04) != 0)
                    return CharacterSet.GSM8;
                else
                    return CharacterSet.GSM7;
        }

        return null;
    }

    @Override
    public DataCodingSchemaMessageClass getMessageClass() {
        switch (this.getDataCodingGroup()) {
            case GeneralDataCodingIndication:
                if ((code & 0x10) != 0)
                    return DataCodingSchemaMessageClass.getInstance(code & 0x03);
                else
                    return null;
            case MessageWithUserDataHeader:
            case DataCodingMessageClass:
                return DataCodingSchemaMessageClass.getInstance(code & 0x03);
        }

        return null;
    }

    @Override
    public boolean getIsCompressed() {
        if (this.getDataCodingGroup() == CBSDataCodingGroup.GeneralDataCodingIndication && (code & 0x20) != 0)
            return true;
        else
            return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + code;
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CBSDataCodingSchemeImpl other = (CBSDataCodingSchemeImpl) obj;
        if (code != other.code)
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("CBSDataCodingScheme [");
        sb.append("Code=");
        sb.append(this.code);
        sb.append(", CBSDataCodingGroup=");
        sb.append(this.getDataCodingGroup());
        if (this.getMessageClass() != null) {
            sb.append(", MessageClass=");
            sb.append(this.getMessageClass());
        }
        if (this.getNationalLanguageShiftTable() != null) {
            sb.append(", NationalLanguageShiftTable=");
            sb.append(this.getNationalLanguageShiftTable());
        }
        if (this.getCharacterSet() != null) {
            sb.append(", CharacterSet=");
            sb.append(this.getCharacterSet());
        }
        if (this.getIsCompressed())
            sb.append(", Compressed");
        sb.append("]");

        return sb.toString();
    }
}
