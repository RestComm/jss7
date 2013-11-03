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

package org.mobicents.protocols.ss7.map.primitives;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class OidContainerTest {

    private long[] getSourceData() {
        return new long[] { 1, 205, 3 };
    }

    private String getString() {
        return "1.205.3";
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {

        OidContainer oid = new OidContainer();
        oid.parseSerializedData(getString());

        assertEquals(oid.getData(), getSourceData());
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {

        OidContainer oid = new OidContainer(getSourceData());
        String s1 = oid.getSerializedData();

        assertEquals(s1, getString());
    }

}
