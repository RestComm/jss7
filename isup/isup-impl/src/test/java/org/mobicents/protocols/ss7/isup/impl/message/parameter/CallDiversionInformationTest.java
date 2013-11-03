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

/**
 * Start time:13:37:14 2009-04-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 *
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.testng.annotations.Test;

/**
 * Start time:13:37:14 2009-04-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>ca </a>
 */
public class CallDiversionInformationTest extends ParameterHarness {

    public CallDiversionInformationTest() {
        super();

        super.badBodies.add(new byte[0]);
        super.badBodies.add(new byte[2]);

        super.goodBodies.add(getBody1());

    }

    private byte[] getBody1() {
        // Notif sub options : 010 - presentation allowed
        // redirect reason : 0100 - deflection during alerting
        // whole : 00100010
        return new byte[] { 0x22 };
    }

    @Test(groups = { "functional.encode", "functional.decode", "parameter" })
    public void testBody1EncodedValues() throws ParameterException {
        CallDiversionInformationImpl cdi = new CallDiversionInformationImpl(getBody1());
        String[] methodNames = { "getNotificationSubscriptionOptions", "getRedirectingReason" };
        Object[] expectedValues = { cdi._NSO_P_A_WITH_RN, cdi._REDIRECTING_REASON_DDA };
        super.testValues(cdi, methodNames, expectedValues);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.isup.messages.parameters.ParameterHarness#getTestedComponent()
     */

    public AbstractISUPParameter getTestedComponent() throws ParameterException {
        return new CallDiversionInformationImpl(new byte[1]);
    }

}
