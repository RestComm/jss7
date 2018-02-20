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

package org.restcomm.protocols.ss7.map.primitives;

import static org.testng.Assert.assertEquals;

import org.restcomm.protocols.ss7.map.primitives.OidContainer;
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
