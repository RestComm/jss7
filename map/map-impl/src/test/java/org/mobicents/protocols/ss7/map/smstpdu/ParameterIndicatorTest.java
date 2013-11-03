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

package org.mobicents.protocols.ss7.map.smstpdu;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class ParameterIndicatorTest {
    @Test(groups = { "functional.encode", "smstpdu" })
    public void testEncode() throws Exception {

        // ParameterIndicatorImpl ind = new ParameterIndicatorImpl(boolean TP_UDLPresence, boolean getTP_DCSPresence, boolean
        // getTP_PIDPresence);
        ParameterIndicatorImpl ind = new ParameterIndicatorImpl(true, false, false);
        assertTrue(ind.getTP_UDLPresence());
        assertFalse(ind.getTP_DCSPresence());
        assertFalse(ind.getTP_PIDPresence());

        ind = new ParameterIndicatorImpl(false, true, false);
        assertFalse(ind.getTP_UDLPresence());
        assertTrue(ind.getTP_DCSPresence());
        assertFalse(ind.getTP_PIDPresence());

        ind = new ParameterIndicatorImpl(false, false, true);
        assertFalse(ind.getTP_UDLPresence());
        assertFalse(ind.getTP_DCSPresence());
        assertTrue(ind.getTP_PIDPresence());
    }
}
