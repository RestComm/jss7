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

package org.mobicents.protocols.ss7.sccp.impl;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import org.mobicents.protocols.ss7.Util;
import org.mobicents.protocols.ss7.sccp.ConcernedSignalingPointCode;
import org.mobicents.protocols.ss7.sccp.RemoteSignalingPointCode;
import org.mobicents.protocols.ss7.sccp.RemoteSubSystem;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author amit bhayani
 *
 */
public class SccpResourceTest {

    private SccpResourceImpl resource = null;

    public SccpResourceTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUp() {
        resource = new SccpResourceImpl("SccpResourceTest");
        resource.setPersistDir(Util.getTmpTestDir());
        resource.start();
        resource.removeAllResourses();

    }

    @AfterMethod
    public void tearDown() {
        resource.removeAllResourses();
        resource.stop();
    }

    @Test(groups = { "sccpresource", "functional.encode" })
    public void testSerialization() throws Exception {

        resource.addRemoteSpc(1, 6034, 0, 0);
        resource.addRemoteSpc(2, 6045, 0, 0);

        resource.addRemoteSsn(1, 6034, 8, 0, false);
        resource.addRemoteSsn(2, 6045, 8, 0, false);

        resource.addConcernedSpc(1, 603);
        resource.addConcernedSpc(2, 604);

        SccpResourceImpl resource1 = new SccpResourceImpl("SccpResourceTest");
        resource1.setPersistDir(Util.getTmpTestDir());
        resource1.start();

        assertEquals(resource1.getRemoteSpcs().size(), 2);
        RemoteSignalingPointCode rsp1Temp = resource1.getRemoteSpc(1);
        assertNotNull(rsp1Temp);
        assertEquals(rsp1Temp.getRemoteSpc(), 6034);

        assertEquals(resource1.getRemoteSsns().size(), 2);
        RemoteSubSystem rss1Temp = resource1.getRemoteSsn(1);
        assertEquals(rss1Temp.getRemoteSsn(), 8);

        assertEquals(resource1.getConcernedSpcs().size(), 2);
        ConcernedSignalingPointCode cspc1Temp = resource1.getConcernedSpc(1);
        assertEquals(cspc1Temp.getRemoteSpc(), 603);
    }

}
