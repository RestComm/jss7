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
 * Start time:12:49:54 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:12:49:54 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface ForwardCallIndicators extends ISUPParameter {
    // FIXME: check this against Q, if it has everything.
    int _PARAMETER_CODE = 0x07;

    /**
     * See q.763 3.5 National/international call indicator (Note 1) : call to be treated as a national call
     */
    boolean _NCI_NATIONAL_CALL = false;

    /**
     * See q.763 3.5 National/international call indicator (Note 1) : call to be treated as an international call
     */
    boolean _NCI_INTERNATIONAL_CALL = true;

    /**
     * See q.763 3.5 End-to-end method indicator (Note 2) : no end-to-end method available (only link-by-link method available)
     */
    int _ETEMI_NOMETHODAVAILABLE = 0;
    /**
     * See q.763 3.5 End-to-end method indicator (Note 2) : pass-along method available (national use)
     */
    int _ETEMI_PASSALONG = 1;
    /**
     * See q.763 3.5 End-to-end method indicator (Note 2) : SCCP method available
     */
    int _ETEMI_SCCP = 2;
    /**
     * See q.763 3.5 End-to-end method indicator (Note 2) : pass-along and SCCP methods available (national use)
     */
    int _ETEMI_SCCP_AND_PASSALONG = 3;
    /**
     * See q.763 3.5 End-to-end information indicator (national use) (Note 2) : no end-to-end information available
     */
    boolean _ETEII_NOT_AVAILABLE = false;
    /**
     * See q.763 3.5 End-to-end information indicator (national use) (Note 2) : end-to-end information available
     */
    boolean _ETEII_AVAILABLE = true;
    /**
     * See q.763 3.5 Interworking indicator (Note 2)
     */
    boolean _II_NOT_ENCOUTNERED = false;
    /**
     * See q.763 3.5 Interworking indicator (Note 2)
     */
    boolean _II_ENCOUTNERED = true;
    /**
     * See q.763 3.5 ISDN access indicator : originating access non-ISDN
     */
    boolean _ISDN_AI_OA_N_ISDN = false;
    /**
     * See q.763 3.5 ISDN access indicator : originating access ISDN
     */
    boolean _ISDN_AI_OA_ISDN = true;

    /**
     * See q.763 3.5 SCCP method indicator (Note 2) : no indication
     */
    int _SCCP_MI_NOINDICATION = 0;
    /**
     * See q.763 3.5 SCCP method indicator (Note 2) : connectionless method available (national use)
     */
    int _SCCP_MI_CONNECTIONLESS = 1;
    /**
     * See q.763 3.5 SCCP method indicator (Note 2) : connection oriented method available
     */
    int _SCCP_MI_CONNECTION_ORIENTED = 2;
    /**
     * See q.763 3.5 SCCP method indicator (Note 2) : connectionless and connection oriented methods available (national use)
     */
    int _SCCP_MI_CL_AND_CO = 3;

    /**
     * See q.763 3.23 ISDN user part indicator (Note 2) : ISDN user part not used all the way
     */
    boolean _ISDN_UPI_NOTUSED = false;
    /**
     * See q.763 3.23 ISDN user part indicator (Note 2) : ISDN user part used all the way
     */
    boolean _ISDN_UPI_USED = true;

    /**
     * See q.763 3.23 ISDN user part preference indicator : ISDN user part preferred all the way
     */
    int _ISDN_UPRI_PREFERED_ALL_THE_WAY = 0;

    /**
     * See q.763 3.23 ISDN user part preference indicator : ISDN user part not required all the way
     */
    int _ISDN_UPRI_NRATW = 1;

    /**
     * See q.763 3.23 ISDN user part preference indicator : ISDN user part required all the way
     */
    int _ISDN_UPRI_RATW = 2;

    boolean isNationalCallIdentificator();

    void setNationalCallIdentificator(boolean nationalCallIdentificator);

    int getEndToEndMethodIndicator();

    void setEndToEndMethodIndicator(int endToEndMethodIndicator);

    boolean isInterworkingIndicator();

    void setInterworkingIndicator(boolean interworkingIndicator);

    boolean isEndToEndInformationIndicator();

    void setEndToEndInformationIndicator(boolean endToEndInformationIndicator);

    boolean isIsdnUserPartIndicator();

    void setIsdnUserPartIndicator(boolean isdnUserPartIndicator);

    int getIsdnUserPartReferenceIndicator();

    void setIsdnUserPartReferenceIndicator(int isdnUserPartReferenceIndicator);

    int getSccpMethodIndicator();

    void setSccpMethodIndicator(int sccpMethodIndicator);

    boolean isIsdnAccessIndicator();

    void setIsdnAccessIndicator(boolean isdnAccessIndicator);

}
