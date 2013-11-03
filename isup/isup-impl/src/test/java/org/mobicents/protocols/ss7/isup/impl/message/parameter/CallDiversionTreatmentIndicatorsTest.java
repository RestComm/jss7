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

package org.mobicents.protocols.ss7.isup.impl.message.parameter;

/**
 *
 * Start time:13:47:44 2009-04-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public class CallDiversionTreatmentIndicatorsTest extends ParameterHarness {

    public CallDiversionTreatmentIndicatorsTest() {
        super();

        // super.badBodies.add(new byte[0]);

        super.goodBodies.add(getBody1());

    }

    private byte[] getBody1() {
        byte[] b = new byte[10];
        b[9] = (byte) (b[9] | (0x01 << 7));
        return b;
    }

    public AbstractISUPParameter getTestedComponent() {
        return new CallDiversionTreatmentIndicatorsImpl();
    }

}
