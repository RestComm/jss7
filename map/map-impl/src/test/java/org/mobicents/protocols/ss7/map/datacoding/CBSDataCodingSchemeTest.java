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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import org.mobicents.protocols.ss7.map.api.datacoding.CBSDataCodingGroup;
import org.mobicents.protocols.ss7.map.api.datacoding.CBSNationalLanguage;
import org.mobicents.protocols.ss7.map.api.smstpdu.CharacterSet;
import org.mobicents.protocols.ss7.map.api.smstpdu.DataCodingSchemaMessageClass;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class CBSDataCodingSchemeTest {

    @Test(groups = { "functional.decode", "datacoding" })
    public void testDecode() throws Exception {

        CBSDataCodingSchemeImpl dcs = new CBSDataCodingSchemeImpl(0);
        assertEquals(dcs.getDataCodingGroup(), CBSDataCodingGroup.GeneralGsm7);
        assertEquals(dcs.getNationalLanguageShiftTable(), CBSNationalLanguage.German);
        assertEquals(dcs.getCharacterSet(), CharacterSet.GSM7);
        assertNull(dcs.getMessageClass());
        assertFalse(dcs.getIsCompressed());

        dcs = new CBSDataCodingSchemeImpl(0x0B);
        assertEquals(dcs.getDataCodingGroup(), CBSDataCodingGroup.GeneralGsm7);
        assertEquals(dcs.getNationalLanguageShiftTable(), CBSNationalLanguage.Greek);
        assertEquals(dcs.getCharacterSet(), CharacterSet.GSM7);
        assertNull(dcs.getMessageClass());
        assertFalse(dcs.getIsCompressed());

        dcs = new CBSDataCodingSchemeImpl(0x22);
        assertEquals(dcs.getDataCodingGroup(), CBSDataCodingGroup.GeneralGsm7);
        assertEquals(dcs.getNationalLanguageShiftTable(), CBSNationalLanguage.Arabic);
        assertEquals(dcs.getCharacterSet(), CharacterSet.GSM7);
        assertNull(dcs.getMessageClass());
        assertFalse(dcs.getIsCompressed());

        dcs = new CBSDataCodingSchemeImpl(0x0F);
        assertEquals(dcs.getDataCodingGroup(), CBSDataCodingGroup.GeneralGsm7);
        assertEquals(dcs.getNationalLanguageShiftTable(), CBSNationalLanguage.LanguageUnspecified);
        assertEquals(dcs.getCharacterSet(), CharacterSet.GSM7);
        assertNull(dcs.getMessageClass());
        assertFalse(dcs.getIsCompressed());

        dcs = new CBSDataCodingSchemeImpl(0x10);
        assertEquals(dcs.getDataCodingGroup(), CBSDataCodingGroup.GeneralWithLanguageIndication);
        assertNull(dcs.getNationalLanguageShiftTable());
        assertEquals(dcs.getCharacterSet(), CharacterSet.GSM7);
        assertNull(dcs.getMessageClass());
        assertFalse(dcs.getIsCompressed());

        dcs = new CBSDataCodingSchemeImpl(0x11);
        assertEquals(dcs.getDataCodingGroup(), CBSDataCodingGroup.GeneralWithLanguageIndication);
        assertNull(dcs.getNationalLanguageShiftTable());
        assertEquals(dcs.getCharacterSet(), CharacterSet.UCS2);
        assertNull(dcs.getMessageClass());
        assertFalse(dcs.getIsCompressed());

        dcs = new CBSDataCodingSchemeImpl(0x78);
        assertEquals(dcs.getDataCodingGroup(), CBSDataCodingGroup.GeneralDataCodingIndication);
        assertNull(dcs.getNationalLanguageShiftTable());
        assertEquals(dcs.getCharacterSet(), CharacterSet.UCS2);
        assertEquals(dcs.getMessageClass(), DataCodingSchemaMessageClass.Class0);
        assertTrue(dcs.getIsCompressed());

        dcs = new CBSDataCodingSchemeImpl(0x57);
        assertEquals(dcs.getDataCodingGroup(), CBSDataCodingGroup.GeneralDataCodingIndication);
        assertNull(dcs.getNationalLanguageShiftTable());
        assertEquals(dcs.getCharacterSet(), CharacterSet.GSM8);
        assertEquals(dcs.getMessageClass(), DataCodingSchemaMessageClass.Class3);
        assertFalse(dcs.getIsCompressed());

        dcs = new CBSDataCodingSchemeImpl(0x4C);
        assertEquals(dcs.getDataCodingGroup(), CBSDataCodingGroup.GeneralDataCodingIndication);
        assertNull(dcs.getNationalLanguageShiftTable());
        assertEquals(dcs.getCharacterSet(), CharacterSet.Reserved);
        assertNull(dcs.getMessageClass());
        assertFalse(dcs.getIsCompressed());

        dcs = new CBSDataCodingSchemeImpl(0x99);
        assertEquals(dcs.getDataCodingGroup(), CBSDataCodingGroup.MessageWithUserDataHeader);
        assertNull(dcs.getNationalLanguageShiftTable());
        assertEquals(dcs.getCharacterSet(), CharacterSet.UCS2);
        assertEquals(dcs.getMessageClass(), DataCodingSchemaMessageClass.Class1);
        assertFalse(dcs.getIsCompressed());

        dcs = new CBSDataCodingSchemeImpl(0xD0);
        assertEquals(dcs.getDataCodingGroup(), CBSDataCodingGroup.I1ProtocolMessage);
        assertNull(dcs.getNationalLanguageShiftTable());
        assertNull(dcs.getCharacterSet());
        assertNull(dcs.getMessageClass());
        assertFalse(dcs.getIsCompressed());

        dcs = new CBSDataCodingSchemeImpl(0xE0);
        assertEquals(dcs.getDataCodingGroup(), CBSDataCodingGroup.DefinedByTheWAPForum);
        assertNull(dcs.getNationalLanguageShiftTable());
        assertNull(dcs.getCharacterSet());
        assertNull(dcs.getMessageClass());
        assertFalse(dcs.getIsCompressed());

        dcs = new CBSDataCodingSchemeImpl(0xF6);
        assertEquals(dcs.getDataCodingGroup(), CBSDataCodingGroup.DataCodingMessageClass);
        assertNull(dcs.getNationalLanguageShiftTable());
        assertEquals(dcs.getCharacterSet(), CharacterSet.GSM8);
        assertEquals(dcs.getMessageClass(), DataCodingSchemaMessageClass.Class2);
        assertFalse(dcs.getIsCompressed());

        dcs = new CBSDataCodingSchemeImpl(0xF3);
        assertEquals(dcs.getDataCodingGroup(), CBSDataCodingGroup.DataCodingMessageClass);
        assertNull(dcs.getNationalLanguageShiftTable());
        assertEquals(dcs.getCharacterSet(), CharacterSet.GSM7);
        assertEquals(dcs.getMessageClass(), DataCodingSchemaMessageClass.Class3);
        assertFalse(dcs.getIsCompressed());

        dcs = new CBSDataCodingSchemeImpl(0xC0);
        assertEquals(dcs.getDataCodingGroup(), CBSDataCodingGroup.Reserved);
        assertNull(dcs.getNationalLanguageShiftTable());
        assertNull(dcs.getCharacterSet());
        assertNull(dcs.getMessageClass());
        assertFalse(dcs.getIsCompressed());
    }

    @Test(groups = { "functional.encode", "datacoding" })
    public void testEncode() throws Exception {

        CBSDataCodingSchemeImpl dcs = new CBSDataCodingSchemeImpl(CBSDataCodingGroup.GeneralGsm7, CharacterSet.GSM7,
                CBSNationalLanguage.German, null, false);
        assertEquals(dcs.getCode(), 0);

        dcs = new CBSDataCodingSchemeImpl(CBSDataCodingGroup.GeneralGsm7, CharacterSet.GSM7, CBSNationalLanguage.Greek, null,
                false);
        assertEquals(dcs.getCode(), 0x0B);

        dcs = new CBSDataCodingSchemeImpl(CBSDataCodingGroup.GeneralGsm7, CharacterSet.GSM7, CBSNationalLanguage.Arabic, null,
                false);
        assertEquals(dcs.getCode(), 0x22);

        dcs = new CBSDataCodingSchemeImpl(CBSDataCodingGroup.GeneralGsm7, CharacterSet.GSM7,
                CBSNationalLanguage.LanguageUnspecified, null, false);
        assertEquals(dcs.getCode(), 0x0F);

        dcs = new CBSDataCodingSchemeImpl(CBSDataCodingGroup.GeneralWithLanguageIndication, CharacterSet.GSM7, null, null,
                false);
        assertEquals(dcs.getCode(), 0x10);

        dcs = new CBSDataCodingSchemeImpl(CBSDataCodingGroup.GeneralWithLanguageIndication, CharacterSet.UCS2, null, null,
                false);
        assertEquals(dcs.getCode(), 0x11);

        dcs = new CBSDataCodingSchemeImpl(CBSDataCodingGroup.GeneralDataCodingIndication, CharacterSet.UCS2, null,
                DataCodingSchemaMessageClass.Class0, true);
        assertEquals(dcs.getCode(), 0x78);

        dcs = new CBSDataCodingSchemeImpl(CBSDataCodingGroup.GeneralDataCodingIndication, CharacterSet.GSM8, null,
                DataCodingSchemaMessageClass.Class3, false);
        assertEquals(dcs.getCode(), 0x57);

        dcs = new CBSDataCodingSchemeImpl(CBSDataCodingGroup.GeneralDataCodingIndication, CharacterSet.Reserved, null, null,
                false);
        assertEquals(dcs.getCode(), 0x4C);

        dcs = new CBSDataCodingSchemeImpl(CBSDataCodingGroup.MessageWithUserDataHeader, CharacterSet.UCS2, null,
                DataCodingSchemaMessageClass.Class1, false);
        assertEquals(dcs.getCode(), 0x99);

        dcs = new CBSDataCodingSchemeImpl(CBSDataCodingGroup.I1ProtocolMessage, null, null, null, false);
        assertEquals(dcs.getCode(), 0xD0);

        dcs = new CBSDataCodingSchemeImpl(CBSDataCodingGroup.DefinedByTheWAPForum, null, null, null, false);
        assertEquals(dcs.getCode(), 0xE0);

        dcs = new CBSDataCodingSchemeImpl(CBSDataCodingGroup.DataCodingMessageClass, CharacterSet.GSM8, null,
                DataCodingSchemaMessageClass.Class2, false);
        assertEquals(dcs.getCode(), 0xF6);

        dcs = new CBSDataCodingSchemeImpl(CBSDataCodingGroup.DataCodingMessageClass, CharacterSet.GSM7, null,
                DataCodingSchemaMessageClass.Class3, false);
        assertEquals(dcs.getCode(), 0xF3);
    }
}
