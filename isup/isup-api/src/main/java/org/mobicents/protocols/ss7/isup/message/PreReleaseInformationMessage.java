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

package org.mobicents.protocols.ss7.isup.message;

import org.mobicents.protocols.ss7.isup.message.parameter.ApplicationTransport;
import org.mobicents.protocols.ss7.isup.message.parameter.MessageCompatibilityInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.OptionalBackwardCallIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.OptionalForwardCallIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.ParameterCompatibilityInformation;

/**
 * Start time:09:54:07 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface PreReleaseInformationMessage extends ISUPMessage {
    /**
     * Pre-release information Message, Q.763 reference table 52 <br>
     * {@link PreReleaseInformationMessage}
     */
    int MESSAGE_CODE = 0x42;

    MessageCompatibilityInformation getMessageCompatibilityInformation();

    void setMessageCompatibilityInformation(MessageCompatibilityInformation mci);

    ParameterCompatibilityInformation getParameterCompatibilityInformation();

    void setParameterCompatibilityInformation(ParameterCompatibilityInformation pci);

    OptionalForwardCallIndicators getOptionalForwardCallIndicators();

    void setOptionalForwardCallIndicators(OptionalForwardCallIndicators obci);

    OptionalBackwardCallIndicators getOptionalBackwardCallIndicators();

    void setOptionalBackwardCallIndicators(OptionalBackwardCallIndicators obci);

    ApplicationTransport getApplicationTransport();

    void setApplicationTransport(ApplicationTransport at);
}
