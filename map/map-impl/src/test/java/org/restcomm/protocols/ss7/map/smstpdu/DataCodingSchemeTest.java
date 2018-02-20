/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.restcomm.protocols.ss7.map.smstpdu;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.nio.charset.Charset;

import org.restcomm.protocols.ss7.map.api.smstpdu.CharacterSet;
import org.restcomm.protocols.ss7.map.api.smstpdu.DataCodingGroup;
import org.restcomm.protocols.ss7.map.api.smstpdu.DataCodingSchemaIndicationType;
import org.restcomm.protocols.ss7.map.api.smstpdu.DataCodingSchemaMessageClass;
import org.restcomm.protocols.ss7.map.smstpdu.DataCodingSchemeImpl;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class DataCodingSchemeTest {

    @Test(groups = { "functional.decode", "smstpdu" })
    public void testDecode() throws Exception {

        
        
//        Charset utf8Charset = Charset.forName("UTF-8");
        Charset utf8Charset = Charset.forName("UTF-16BE");
//        byte[] data = new byte[] { 0x31, 0x32, 0x33, 0x02, 0x1b, 0x3c, 0x1b, 0x3e, 0x20, 0x04, 0x05 };
        byte[] data = new byte[] { 0x04, 0x10, 0x04, 0x11, 0x04, 0x12, 0x04, 0x13, 0x04, 0x14 };
        String s = new String(data, utf8Charset);        
        
        
        
        DataCodingSchemeImpl dcs = new DataCodingSchemeImpl(0);
        assertEquals(dcs.getDataCodingGroup(), DataCodingGroup.GeneralGroup);
        assertEquals(dcs.getCharacterSet(), CharacterSet.GSM7);
        assertFalse(dcs.getIsCompressed());
        assertNull(dcs.getMessageClass());
        assertNull(dcs.getDataCodingSchemaIndicationType());
        assertNull(dcs.getSetIndicationActive());

        dcs = new DataCodingSchemeImpl(8);
        assertEquals(dcs.getDataCodingGroup(), DataCodingGroup.GeneralGroup);
        assertEquals(dcs.getCharacterSet(), CharacterSet.UCS2);
        assertFalse(dcs.getIsCompressed());
        assertNull(dcs.getMessageClass());
        assertNull(dcs.getDataCodingSchemaIndicationType());
        assertNull(dcs.getSetIndicationActive());

        dcs = new DataCodingSchemeImpl(52);
        assertEquals(dcs.getDataCodingGroup(), DataCodingGroup.GeneralGroup);
        assertEquals(dcs.getCharacterSet(), CharacterSet.GSM8);
        assertTrue(dcs.getIsCompressed());
        assertEquals(dcs.getMessageClass(), DataCodingSchemaMessageClass.Class0);
        assertNull(dcs.getDataCodingSchemaIndicationType());
        assertNull(dcs.getSetIndicationActive());

        dcs = new DataCodingSchemeImpl(26);
        assertEquals(dcs.getDataCodingGroup(), DataCodingGroup.GeneralGroup);
        assertEquals(dcs.getCharacterSet(), CharacterSet.UCS2);
        assertFalse(dcs.getIsCompressed());
        assertEquals(dcs.getMessageClass(), DataCodingSchemaMessageClass.Class2);
        assertNull(dcs.getDataCodingSchemaIndicationType());
        assertNull(dcs.getSetIndicationActive());

        dcs = new DataCodingSchemeImpl(64);
        assertEquals(dcs.getDataCodingGroup(), DataCodingGroup.MessageMarkedForAutomaticDeletionGroup);
        assertEquals(dcs.getCharacterSet(), CharacterSet.GSM7);
        assertFalse(dcs.getIsCompressed());
        assertNull(dcs.getMessageClass());
        assertNull(dcs.getDataCodingSchemaIndicationType());
        assertNull(dcs.getSetIndicationActive());

        dcs = new DataCodingSchemeImpl(72);
        assertEquals(dcs.getDataCodingGroup(), DataCodingGroup.MessageMarkedForAutomaticDeletionGroup);
        assertEquals(dcs.getCharacterSet(), CharacterSet.UCS2);
        assertFalse(dcs.getIsCompressed());
        assertNull(dcs.getMessageClass());
        assertNull(dcs.getDataCodingSchemaIndicationType());
        assertNull(dcs.getSetIndicationActive());

        dcs = new DataCodingSchemeImpl(116);
        assertEquals(dcs.getDataCodingGroup(), DataCodingGroup.MessageMarkedForAutomaticDeletionGroup);
        assertEquals(dcs.getCharacterSet(), CharacterSet.GSM8);
        assertTrue(dcs.getIsCompressed());
        assertEquals(dcs.getMessageClass(), DataCodingSchemaMessageClass.Class0);
        assertNull(dcs.getDataCodingSchemaIndicationType());
        assertNull(dcs.getSetIndicationActive());

        dcs = new DataCodingSchemeImpl(90);
        assertEquals(dcs.getDataCodingGroup(), DataCodingGroup.MessageMarkedForAutomaticDeletionGroup);
        assertEquals(dcs.getCharacterSet(), CharacterSet.UCS2);
        assertFalse(dcs.getIsCompressed());
        assertEquals(dcs.getMessageClass(), DataCodingSchemaMessageClass.Class2);
        assertNull(dcs.getDataCodingSchemaIndicationType());
        assertNull(dcs.getSetIndicationActive());

        dcs = new DataCodingSchemeImpl(192);
        assertEquals(dcs.getDataCodingGroup(), DataCodingGroup.MessageWaitingIndicationGroupDiscardMessage);
        assertEquals(dcs.getCharacterSet(), CharacterSet.GSM7);
        assertFalse(dcs.getIsCompressed());
        assertNull(dcs.getMessageClass());
        assertEquals(dcs.getDataCodingSchemaIndicationType(), DataCodingSchemaIndicationType.VoicemailMessageWaiting);
        assertFalse(dcs.getSetIndicationActive());

        dcs = new DataCodingSchemeImpl(201);
        assertEquals(dcs.getDataCodingGroup(), DataCodingGroup.MessageWaitingIndicationGroupDiscardMessage);
        assertEquals(dcs.getCharacterSet(), CharacterSet.GSM7);
        assertFalse(dcs.getIsCompressed());
        assertNull(dcs.getMessageClass());
        assertEquals(dcs.getDataCodingSchemaIndicationType(), DataCodingSchemaIndicationType.FaxMessageWaiting);
        assertTrue(dcs.getSetIndicationActive());

        dcs = new DataCodingSchemeImpl(210);
        assertEquals(dcs.getDataCodingGroup(), DataCodingGroup.MessageWaitingIndicationGroupStoreMessage);
        assertEquals(dcs.getCharacterSet(), CharacterSet.GSM7);
        assertFalse(dcs.getIsCompressed());
        assertNull(dcs.getMessageClass());
        assertEquals(dcs.getDataCodingSchemaIndicationType(), DataCodingSchemaIndicationType.ElectronicMailMessageWaiting);
        assertFalse(dcs.getSetIndicationActive());

        dcs = new DataCodingSchemeImpl(235);
        assertEquals(dcs.getDataCodingGroup(), DataCodingGroup.MessageWaitingIndicationGroupStoreMessage);
        assertEquals(dcs.getCharacterSet(), CharacterSet.UCS2);
        assertFalse(dcs.getIsCompressed());
        assertNull(dcs.getMessageClass());
        assertEquals(dcs.getDataCodingSchemaIndicationType(), DataCodingSchemaIndicationType.OtherMessageWaiting);
        assertTrue(dcs.getSetIndicationActive());

        dcs = new DataCodingSchemeImpl(241);
        assertEquals(dcs.getDataCodingGroup(), DataCodingGroup.DataCodingMessageClass);
        assertEquals(dcs.getCharacterSet(), CharacterSet.GSM7);
        assertFalse(dcs.getIsCompressed());
        assertEquals(dcs.getMessageClass(), DataCodingSchemaMessageClass.Class1);
        assertNull(dcs.getDataCodingSchemaIndicationType());
        assertNull(dcs.getSetIndicationActive());

        dcs = new DataCodingSchemeImpl(247);
        assertEquals(dcs.getDataCodingGroup(), DataCodingGroup.DataCodingMessageClass);
        assertEquals(dcs.getCharacterSet(), CharacterSet.GSM8);
        assertFalse(dcs.getIsCompressed());
        assertEquals(dcs.getMessageClass(), DataCodingSchemaMessageClass.Class3);
        assertNull(dcs.getDataCodingSchemaIndicationType());
        assertNull(dcs.getSetIndicationActive());
    }

    @Test(groups = { "functional.encode", "smstpdu" })
    public void testEncode() throws Exception {

        DataCodingSchemeImpl dcs = new DataCodingSchemeImpl(DataCodingGroup.GeneralGroup, null, null, null, CharacterSet.GSM7,
                false);
        assertEquals(dcs.getCode(), 0);

        dcs = new DataCodingSchemeImpl(DataCodingGroup.GeneralGroup, null, null, null, CharacterSet.UCS2, false);
        assertEquals(dcs.getCode(), 8);

        dcs = new DataCodingSchemeImpl(DataCodingGroup.GeneralGroup, DataCodingSchemaMessageClass.Class0, null, null,
                CharacterSet.GSM8, true);
        assertEquals(dcs.getCode(), 52);

        dcs = new DataCodingSchemeImpl(DataCodingGroup.GeneralGroup, DataCodingSchemaMessageClass.Class2, null, null,
                CharacterSet.UCS2, false);
        assertEquals(dcs.getCode(), 26);

        dcs = new DataCodingSchemeImpl(DataCodingGroup.MessageMarkedForAutomaticDeletionGroup, null, null, null,
                CharacterSet.GSM7, false);
        assertEquals(dcs.getCode(), 64);

        dcs = new DataCodingSchemeImpl(DataCodingGroup.MessageMarkedForAutomaticDeletionGroup, null, null, null,
                CharacterSet.UCS2, false);
        assertEquals(dcs.getCode(), 72);

        dcs = new DataCodingSchemeImpl(DataCodingGroup.MessageMarkedForAutomaticDeletionGroup,
                DataCodingSchemaMessageClass.Class0, null, null, CharacterSet.GSM8, true);
        assertEquals(dcs.getCode(), 116);

        dcs = new DataCodingSchemeImpl(DataCodingGroup.MessageMarkedForAutomaticDeletionGroup,
                DataCodingSchemaMessageClass.Class2, null, null, CharacterSet.UCS2, false);
        assertEquals(dcs.getCode(), 90);

        dcs = new DataCodingSchemeImpl(DataCodingGroup.MessageWaitingIndicationGroupDiscardMessage, null,
                DataCodingSchemaIndicationType.VoicemailMessageWaiting, false, CharacterSet.GSM7, false);
        assertEquals(dcs.getCode(), 192);

        dcs = new DataCodingSchemeImpl(DataCodingGroup.MessageWaitingIndicationGroupDiscardMessage, null,
                DataCodingSchemaIndicationType.FaxMessageWaiting, true, CharacterSet.GSM7, false);
        assertEquals(dcs.getCode(), 201);

        dcs = new DataCodingSchemeImpl(DataCodingGroup.MessageWaitingIndicationGroupStoreMessage, null,
                DataCodingSchemaIndicationType.ElectronicMailMessageWaiting, false, CharacterSet.GSM7, false);
        assertEquals(dcs.getCode(), 210);

        dcs = new DataCodingSchemeImpl(DataCodingGroup.MessageWaitingIndicationGroupStoreMessage, null,
                DataCodingSchemaIndicationType.OtherMessageWaiting, true, CharacterSet.UCS2, false);
        assertEquals(dcs.getCode(), 235);

        dcs = new DataCodingSchemeImpl(DataCodingGroup.DataCodingMessageClass, DataCodingSchemaMessageClass.Class1, null, null,
                CharacterSet.GSM7, false);
        assertEquals(dcs.getCode(), 241);

        dcs = new DataCodingSchemeImpl(DataCodingGroup.DataCodingMessageClass, DataCodingSchemaMessageClass.Class3, null, null,
                CharacterSet.GSM8, false);
        assertEquals(dcs.getCode(), 247);
    }
}
