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
package org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement;

import static org.testng.Assert.assertEquals;

import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.BearerServiceCodeValue;
import org.testng.annotations.Test;

/**
 * @author Amit Bhayani
 *
 */
public class BearerServiceCodeValueTest {

    /**
	 *
	 */
    public BearerServiceCodeValueTest() {
        // TODO Auto-generated constructor stub
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void test() throws Exception {
        int code = BearerServiceCodeValue.Asynchronous9_6kbps.getBearerServiceCode();

        BearerServiceCodeValue valueFromCode = BearerServiceCodeValue.getInstance(code);

        assertEquals(valueFromCode, BearerServiceCodeValue.Asynchronous9_6kbps);
    }

}
