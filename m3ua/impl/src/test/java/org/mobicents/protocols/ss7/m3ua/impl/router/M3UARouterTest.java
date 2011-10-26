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

package org.mobicents.protocols.ss7.m3ua.impl.router;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mobicents.protocols.ss7.m3ua.M3UAProvider;
import org.mobicents.protocols.ss7.m3ua.impl.As;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ParameterFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.RoutingKeyImpl;
import org.mobicents.protocols.ss7.m3ua.impl.tcp.TcpProvider;
import org.mobicents.protocols.ss7.m3ua.parameter.DestinationPointCode;
import org.mobicents.protocols.ss7.m3ua.parameter.OPCList;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingKey;
import org.mobicents.protocols.ss7.m3ua.parameter.ServiceIndicators;
import org.mobicents.protocols.ss7.m3ua.parameter.TrafficModeType;

/**
 * @author amit bhayani
 * 
 */
public class M3UARouterTest {
    private ParameterFactoryImpl factory = new ParameterFactoryImpl();
    private ServerM3UARouter m3uaRouter = null;
    private M3UAProvider m3uaProvider = TcpProvider.provider();

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        m3uaRouter = new ServerM3UARouter();
    }

    @After
    public void tearDown() {
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testRoute() throws Exception {

        // RK DPC=123, OPC=1,2,3 SI=1,2
        RoutingContext rc = factory.createRoutingContext(new long[] { 1 });
        DestinationPointCode[] dpc = new DestinationPointCode[] { factory.createDestinationPointCode(123, (short) 0) };
        ServiceIndicators[] servInds = new ServiceIndicators[] { factory.createServiceIndicators(new short[] { 1, 2 }) };
        OPCList[] opcList = new OPCList[] { factory.createOPCList(new int[] { 1, 2, 3 }, new short[] { 0, 0, 0 }) };

        RoutingKeyImpl rk1 = (RoutingKeyImpl) factory.createRoutingKey(null, rc, null, null, dpc, servInds, opcList);

        this.m3uaRouter.addRk(rk1, new As("AsImpl1", rc, rk1, null));

        // RK DPC=123 OPC=4 SI=2
        rc = factory.createRoutingContext(new long[] { 2 });
        dpc = new DestinationPointCode[] { factory.createDestinationPointCode(123, (short) 0) };
        servInds = new ServiceIndicators[] { factory.createServiceIndicators(new short[] { 2 }) };
        opcList = new OPCList[] { factory.createOPCList(new int[] { 4 }, new short[] { 0, 0, 0 }) };

        RoutingKeyImpl rk2 = (RoutingKeyImpl) factory.createRoutingKey(null, rc, null, null, dpc, servInds, opcList);

        this.m3uaRouter.addRk(rk2, new As("AsImpl2", rc, rk2, null));

        // RK DPC=123
        rc = factory.createRoutingContext(new long[] { 3 });
        dpc = new DestinationPointCode[] { factory.createDestinationPointCode(123, (short) 0) };

        RoutingKeyImpl rk3 = (RoutingKeyImpl) factory.createRoutingKey(null, rc, null, null, dpc, null, null);

        this.m3uaRouter.addRk(rk3, new As("AsImpl3", rc, rk3, null));

        // Retrieve As for message where dpc=123 opc=1, si=2
        As resultAs = this.m3uaRouter.getAs(123, 1, (short) 2);

        assertNotNull(resultAs);
        assertEquals(resultAs.getRoutingContext().getRoutingContexts()[0], 1l);

        // Retrieve As for message where dpc=123 and opc=4 si=3
        resultAs = this.m3uaRouter.getAs(123, 4, (short) 3);
        assertNull(resultAs);

        // Retrieve As for message where dpc=123 and opc=5 si=3
        resultAs = this.m3uaRouter.getAs(123, 5, (short) 3);
        assertNotNull(resultAs);
        assertEquals(resultAs.getRoutingContext().getRoutingContexts()[0], 3l);

    }

}
