/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

package org.restcomm.protocols.ss7.map.datacoding;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.restcomm.protocols.ss7.map.datacoding.GSMCharset;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class CheckAllCharsCanBeEncodedTest {

    @Test(groups = { "functional.encode", "datacoding" })
    public void testCheckAllCharsCanBeEncoded() throws Exception {

        boolean res = GSMCharset.checkAllCharsCanBeEncoded("", GSMCharset.basicMap,
                GSMCharset.basicExtentionMap);
        assertTrue(res);

        res = GSMCharset.checkAllCharsCanBeEncoded("aA12", GSMCharset.basicMap,
                GSMCharset.basicExtentionMap);
        assertTrue(res);

        res = GSMCharset.checkAllCharsCanBeEncoded("A[", GSMCharset.basicMap,
                GSMCharset.basicExtentionMap);
        assertTrue(res);

        res = GSMCharset.checkAllCharsCanBeEncoded("A[", GSMCharset.basicMap, null);
        assertFalse(res);

        res = GSMCharset.checkAllCharsCanBeEncoded("A[", null, null);
        assertFalse(res);

        res = GSMCharset.checkAllCharsCanBeEncoded("A\u0424", GSMCharset.basicMap,
                GSMCharset.basicExtentionMap);
        assertFalse(res);

    }
}
