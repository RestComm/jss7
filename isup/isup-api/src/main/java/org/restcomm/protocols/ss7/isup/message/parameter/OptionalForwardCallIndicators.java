/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

/**
 * Start time:13:37:40 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.restcomm.protocols.ss7.isup.message.parameter;

/**
 * Start time:13:37:40 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface OptionalForwardCallIndicators extends ISUPParameter {
    int _PARAMETER_CODE = 0x08;

    /**
     * See Q.763 3.38 Simple segmentation indicator : no additional information will be sent
     */
    boolean _SSI_NO_ADDITIONAL_INFO = false;

    /**
     * See Q.763 3.38 Simple segmentation indicator : additional information will be sent in a segmentation message
     */
    boolean _SSI_ADDITIONAL_INFO = true;

    /**
     * See Q.763 3.38 Connected line identity request indicator :
     */
    boolean _CLIRI_NOT_REQUESTED = false;

    /**
     * See Q.763 3.38 Connected line identity request indicator :
     */
    boolean _CLIRI_REQUESTED = true;
    /**
     * See Q.763 3.38 Closed user group call indicator : non-CUG call
     */
    int _CUGCI_NON_CUG_CALL = 0;

    /**
     * See Q.763 3.38 Closed user group call indicator : closed user group call, outgoing access allowed
     */
    int _CUGCI_CUG_CALL_OAL = 2;

    /**
     * See Q.763 3.38 Closed user group call indicator : closed user group call, outgoing access not allowed
     */
    int _CUGCI_CUG_CALL_OANL = 3;

    int getClosedUserGroupCallIndicator();

    void setClosedUserGroupCallIndicator(int closedUserGroupCallIndicator);

    boolean isSimpleSegmentationIndicator();

    void setSimpleSegmentationIndicator(boolean simpleSegmentationIndicator);

    boolean isConnectedLineIdentityRequestIndicator();

    void setConnectedLineIdentityRequestIndicator(boolean connectedLineIdentityRequestIndicator);
}
