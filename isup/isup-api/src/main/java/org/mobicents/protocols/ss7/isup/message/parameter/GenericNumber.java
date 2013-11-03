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

package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:13:07:30 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface GenericNumber extends NAINumber, Number, ISUPParameter {
    int _PARAMETER_CODE = 0xC0;

    /**
     * number qualifier indicator indicator value. See Q.763 - 3.26a
     */
    int _NQIA_CALLED_NUMBER = 1;
    /**
     * number qualifier indicator indicator value. See Q.763 - 3.26a
     */
    int _NQIA_CONNECTED_NUMBER = 5;
    /**
     * number qualifier indicator indicator value. See Q.763 - 3.26a
     */
    int _NQIA_CALLING_PARTY_NUMBER = 6;
    /**
     * number qualifier indicator indicator value. See Q.763 - 3.26a
     */
    int _NQIA_ORIGINAL_CALLED_NUMBER = 7;
    /**
     * number qualifier indicator indicator value. See Q.763 - 3.26a
     */
    int _NQIA_REDIRECTING_NUMBER = 8;
    /**
     * number qualifier indicator indicator value. See Q.763 - 3.26a
     */
    int _NQIA_REDIRECTION_NUMBER = 9;

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

    /**
     * number incomplete indicator indicator value. See Q.763 - 3.10c
     */
    boolean _NI_INCOMPLETE = true;
    /**
     * number incomplete indicator indicator value. See Q.763 - 3.10c
     */
    boolean _NI_COMPLETE = false;
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
     * address presentation restricted indicator indicator value. See Q.763 - 3.16d
     */
    int _APRI_SPARE = 3;

    /**
     * screening indicator indicator value. See Q.763 - 3.26g
     */
    int _SI_USER_PROVIDED_NVERIFIED_PASSED = 0;
    /**
     * screening indicator indicator value. See Q.763 - 3.26g
     */
    int _SI_USER_PROVIDED_VERIFIED_PASSED = 1;
    /**
     * screening indicator indicator value. See Q.763 - 3.26g
     */
    int _SI_USER_PROVIDED_VERIFIED_FAILED = 2;

    /**
     * screening indicator indicator value. See Q.763 - 3.26g
     */
    int _SI_NETWORK_PROVIDED = 3;

    int getNumberQualifierIndicator();

    void setNumberQualifierIndicator(int numberQualifierIndicator);

    int getNumberingPlanIndicator();

    void setNumberingPlanIndicator(int numberingPlanIndicator);

    int getAddressRepresentationRestrictedIndicator();

    void setAddressRepresentationRestrictedIndicator(int addressRepresentationREstrictedIndicator);

    boolean isNumberIncomplete();

    void setNumberIncompleter(boolean numberIncomplete);

    int getScreeningIndicator();

    void setScreeningIndicator(int screeningIndicator);

}
