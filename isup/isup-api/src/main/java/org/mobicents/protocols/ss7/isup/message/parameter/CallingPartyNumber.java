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
 * Start time:11:58:14 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:11:58:14 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface CallingPartyNumber extends NAINumber, ISUPParameter {
    int _PARAMETER_CODE = 0x0A;

    /**
     * numbering plan indicator indicator value. See Q.763 - 3.9d
     */
    int _NPI_ISDN = 1;
    /**
     * numbering plan indicator indicator value. See Q.763 - 3.9d
     */
    int _NPI_DATA = 3;
    /**
     * numbering plan indicator indicator value. See Q.763 - 3.9d
     */
    int _NPI_TELEX = 4;

    // FIXME: change to boolean
    /**
     * number incomplete indicator indicator value. See Q.763 - 3.10c
     */
    int _NI_INCOMPLETE = 1;
    /**
     * number incomplete indicator indicator value. See Q.763 - 3.10c
     */
    int _NI_COMPLETE = 0;

    /**
     * address presentation restricted indicator indicator value. See Q.763 - 3.10e
     */
    int _APRI_ALLOWED = 0;

    /**
     * address presentation restricted indicator indicator value. See Q.763 - 3.10e
     */
    int _APRI_RESTRICTED = 1;

    /**
     * address presentation restricted indicator indicator value. See Q.763 - 3.10e
     */
    int _APRI_NOT_AVAILABLE = 2;

    /**
     * address presentation restricted indicator indicator value. See Q.763 - 3.10e
     */
    int _APRI_RESERVED = 3;

    /**
     * screening indicator indicator value. See Q.763 - 3.10f
     */
    int _SI_USER_PROVIDED_NOTVERIFIED = 0;
    /**
     * screening indicator indicator value. See Q.763 - 3.10f
     */
    int _SI_USER_PROVIDED_VERIFIED_PASSED = 1;
    /**
     * screening indicator indicator value. See Q.763 - 3.10f
     */
    int _SI_USER_PROVIDED_FAILED = 2;

    /**
     * screening indicator indicator value. See Q.763 - 3.10f
     */
    int _SI_NETWORK_PROVIDED = 3;

    int getNumberingPlanIndicator();

    void setNumberingPlanIndicator(int numberingPlanIndicator);

    int getNumberIncompleteIndicator();

    void setNumberIncompleteIndicator(int numberIncompleteIndicator);

    int getAddressRepresentationRestrictedIndicator();

    void setAddressRepresentationREstrictedIndicator(int addressRepresentationREstrictedIndicator);

    int getScreeningIndicator();

    void setScreeningIndicator(int screeningIndicator);

}
