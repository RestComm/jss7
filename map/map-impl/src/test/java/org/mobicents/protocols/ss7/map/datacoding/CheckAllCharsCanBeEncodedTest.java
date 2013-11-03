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

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class CheckAllCharsCanBeEncodedTest {

    @Test(groups = { "functional.encode", "datacoding" })
    public void testCheckAllCharsCanBeEncoded() throws Exception {

        boolean res = GSMCharset.checkAllCharsCanBeEncoded("", GSMCharset.BYTE_TO_CHAR_DefaultAlphabet,
                GSMCharset.BYTE_TO_CHAR_DefaultAlphabetExtentionTable);
        assertTrue(res);

        res = GSMCharset.checkAllCharsCanBeEncoded("aA12", GSMCharset.BYTE_TO_CHAR_DefaultAlphabet,
                GSMCharset.BYTE_TO_CHAR_DefaultAlphabetExtentionTable);
        assertTrue(res);

        res = GSMCharset.checkAllCharsCanBeEncoded("A[", GSMCharset.BYTE_TO_CHAR_DefaultAlphabet,
                GSMCharset.BYTE_TO_CHAR_DefaultAlphabetExtentionTable);
        assertTrue(res);

        res = GSMCharset.checkAllCharsCanBeEncoded("A[", GSMCharset.BYTE_TO_CHAR_DefaultAlphabet, null);
        assertFalse(res);

        res = GSMCharset.checkAllCharsCanBeEncoded("A[", null, null);
        assertFalse(res);

        res = GSMCharset.checkAllCharsCanBeEncoded("A\u0424", GSMCharset.BYTE_TO_CHAR_DefaultAlphabet,
                GSMCharset.BYTE_TO_CHAR_DefaultAlphabetExtentionTable);
        assertFalse(res);

    }
}
