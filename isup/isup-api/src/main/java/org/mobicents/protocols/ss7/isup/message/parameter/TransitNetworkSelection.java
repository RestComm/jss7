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
 * Start time:14:13:30 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:14:13:30 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface TransitNetworkSelection extends ISUPParameter {

    int _PARAMETER_CODE = 0x23;

    // FIXME: add C defs
    /**
     * See Q.763 3.53 Type of network identification : CCITT/ITU-T-standardized identification
     */
    int _TONI_ITU_T = 0;
    /**
     * See Q.763 3.53 Type of network identification : national network identification
     */
    int _TONI_NNI = 2;

    /**
     * See Q.763 3.53 Network identification plan : For CCITT/ITU-T-standardized identification : unknown
     */
    int _NIP_UNKNOWN = 0;

    /**
     * See Q.763 3.53 Network identification plan : For CCITT/ITU-T-standardized identification : public data network
     * identification code (DNIC), ITU-T Recommendation X.121
     */
    int _NIP_PDNIC = 3;

    /**
     * See Q.763 3.53 Network identification plan : For CCITT/ITU-T-standardized identification : public land Mobile Network
     * Identification Code (MNIC), ITU-T Recommendation E.212
     */
    int _NIP_PLMNIC = 6;

    int getTypeOfNetworkIdentification();

    void setTypeOfNetworkIdentification(int typeOfNetworkIdentification);

    int getNetworkIdentificationPlan();

    void setNetworkIdentificationPlan(int networkIdentificationPlan);

    String getAddress();

    void setAddress(String address);

    boolean isOddFlag();

}
