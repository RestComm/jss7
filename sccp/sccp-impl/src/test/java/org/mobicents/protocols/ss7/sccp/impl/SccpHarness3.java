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

package org.mobicents.protocols.ss7.sccp.impl;

import org.mobicents.protocols.ss7.Util;
import org.mobicents.protocols.ss7.sccp.Router;
import org.mobicents.protocols.ss7.sccp.SccpProtocolVersion;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.SccpResource;
import org.mobicents.protocols.ss7.sccp.impl.Mtp3UserPartImpl;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.protocols.ss7.sccp.parameter.ParameterFactory;

import java.io.FileOutputStream;

/**
 * @author amit bhayani
 *
 */
public abstract class SccpHarness3 extends SccpHarness {

    protected String sccpStack3Name = null;

    protected SccpStackImpl sccpStack3;
    protected SccpProvider sccpProvider3;

    protected Mtp3UserPartImpl mtp3UserPart3 = new Mtp3UserPartImpl();

    protected Router router3 = null;

    protected SccpResource resource3 = null;

    protected ParameterFactory parameterFactory;
    /**
	 *
	 */
    public SccpHarness3() {
        super();
//        mtp3UserPart1.setOtherPart(mtp3UserPart2);
        mtp3UserPart1.setOtherPart(mtp3UserPart3);
//        mtp3UserPart2.setOtherPart(mtp3UserPart1);
        mtp3UserPart2.setOtherPart(mtp3UserPart3);
        mtp3UserPart3.setOtherPart(mtp3UserPart1);
        mtp3UserPart3.setOtherPart(mtp3UserPart2);
    }

    protected void createStack3() {
        sccpStack3 = createStack(sccpStack3Name);
    }

    protected void setUpStack1() throws Exception {
        createStack1();
        mtp3UserPart1.addDpc(getStack1PC());

        sccpStack1.setMtp3UserPart(1, mtp3UserPart1);
        sccpStack1.start();
        sccpStack1.removeAllResourses();
        sccpStack1.getRouter().addMtp3ServiceAccessPoint(1, 1, getStack1PC(), 2, 0, null);
        sccpStack1.getRouter().addMtp3Destination(1, 1, getStack2PC(), getStack2PC(), 0, 255, 255);
        sccpStack1.getRouter().addMtp3Destination(1, 2, getStack3PC(), getStack3PC(), 0, 255, 255);

        sccpProvider1 = sccpStack1.getSccpProvider();

        router1 = sccpStack1.getRouter();

        resource1 = sccpStack1.getSccpResource();

        resource1.addRemoteSpc(1, getStack2PC(), 0, 0);
        resource1.addRemoteSpc(3, getStack3PC(), 0, 0);
        resource1.addRemoteSsn(1, getStack2PC(), getSSN(), 0, false);
        resource1.addRemoteSsn(2, getStack3PC(), getSSN(), 0, false);
        this.parameterFactory = this.sccpProvider1.getParameterFactory();

    }

    protected void setUpStack2() throws Exception {
        createStack2();
        mtp3UserPart2.addDpc(getStack2PC());

        sccpStack2.setMtp3UserPart(1, mtp3UserPart2);
        sccpStack2.start();
        sccpStack2.removeAllResourses();
        sccpStack2.getRouter().addMtp3ServiceAccessPoint(1, 1, getStack2PC(), 2, 0, null);
        sccpStack2.getRouter().addMtp3Destination(1, 1, getStack1PC(), getStack1PC(), 0, 255, 255);
        sccpStack2.getRouter().addMtp3Destination(1, 2, getStack3PC(), getStack3PC(), 0, 255, 255);

        sccpProvider2 = sccpStack2.getSccpProvider();

        router2 = sccpStack2.getRouter();

        resource2 = sccpStack2.getSccpResource();

        resource2.addRemoteSpc(02, getStack1PC(), 0, 0);
        resource2.addRemoteSpc(3, getStack3PC(), 0, 0);
        resource2.addRemoteSsn(1, getStack1PC(), getSSN(), 0, false);
        resource2.addRemoteSsn(2, getStack3PC(), getSSN(), 0, false);

    }

    protected void setUpStack3() throws Exception {
        createStack3();
        mtp3UserPart3.addDpc(getStack3PC());

        sccpStack3.setMtp3UserPart(1, mtp3UserPart3);
        sccpStack3.start();
        sccpStack3.removeAllResourses();
        sccpStack3.getRouter().addMtp3ServiceAccessPoint(1, 1, getStack3PC(), 2, 0, null);
        sccpStack3.getRouter().addMtp3Destination(1, 1, getStack1PC(), getStack1PC(), 0, 255, 255);
        sccpStack3.getRouter().addMtp3Destination(1, 2, getStack2PC(), getStack2PC(), 0, 255, 255);

        sccpProvider3 = sccpStack3.getSccpProvider();

        router3 = sccpStack3.getRouter();

        resource3 = sccpStack3.getSccpResource();

        resource3.addRemoteSpc(02, getStack1PC(), 0, 0);
        resource3.addRemoteSpc(1, getStack2PC(), 0, 0);
        resource3.addRemoteSsn(1, getStack1PC(), getSSN(), 0, false);
        resource3.addRemoteSsn(2, getStack2PC(), getSSN(), 0, false);

    }

    private void tearDownStack1() {
        sccpStack1.removeAllResourses();
        sccpStack1.stop();
    }

    private void tearDownStack2() {
        sccpStack2.removeAllResourses();
        sccpStack2.stop();
    }

    private void tearDownStack3() {
        sccpStack3.removeAllResourses();
        sccpStack3.stop();
    }

    protected int getStack3PC() {
        if (sccpStack1.getSccpProtocolVersion() == SccpProtocolVersion.ANSI)
            return 8000003;
        else
        return 3;
    }

    public void setUp() throws Exception {
        this.setUpStack1();
        this.setUpStack2();
        this.setUpStack3();
    }

    public void tearDown() {
        this.tearDownStack1();
        this.tearDownStack2();
        this.tearDownStack3();
    }
}
