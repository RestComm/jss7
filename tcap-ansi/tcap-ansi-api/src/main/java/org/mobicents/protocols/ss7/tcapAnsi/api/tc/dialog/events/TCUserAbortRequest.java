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

package org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events;

import org.mobicents.protocols.ss7.tcapAnsi.api.asn.ApplicationContext;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.Confidentiality;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.SecurityContext;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.UserInformation;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.UserInformationElement;

/**
 *
 * @author amit bhayani
 * @author baranowb
 */
public interface TCUserAbortRequest extends DialogRequest {

    ApplicationContext getApplicationContextName();

    void setApplicationContextName(ApplicationContext acn);

    UserInformation getUserInformation();

    void setUserInformation(UserInformation val);

    SecurityContext getSecurityContext();

    void setSecurityContext(SecurityContext val);

    Confidentiality getConfidentiality();

    void setConfidentiality(Confidentiality val);


    UserInformationElement getUserAbortInformation();

    void setUserAbortInformation(UserInformationElement val);

}
