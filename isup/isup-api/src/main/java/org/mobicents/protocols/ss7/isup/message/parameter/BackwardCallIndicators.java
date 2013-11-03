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
 * Start time:11:00:19 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:11:00:19 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface BackwardCallIndicators extends ISUPParameter {
    int _PARAMETER_CODE = 0x11;

    /**
     * See q.763 3.5 Charge indicator no indication
     */
    int _CHARGE_INDICATOR_NOINDICATION = 0;
    /**
     * See q.763 3.5 Charge indicator no charge
     */
    int _CHARGE_INDICATOR_NOCHARGE = 1;
    /**
     * See q.763 3.5 Charge indicator charge
     */
    int _CHARGE_INDICATOR_CHARGE = 2;

    /**
     * See q.763 3.5 Called party's status indicator no indication
     */
    int _CPSI_NO_INDICATION = 0;
    /**
     * See q.763 3.5 Called party's status indicator subscriber free
     */
    int _CPSI_SUBSCRIBER_FREE = 1;
    /**
     * See q.763 3.5 Called party's status indicator connect when free (national use)
     */
    int _CPSI_CONNECT_WHEN_FREE = 2;

    /**
     * See q.763 3.5 Called party's category indicator
     */
    int _CPCI_NOINDICATION = 0;
    /**
     * See q.763 3.5 Called party's category indicator
     */
    int _CPCI_ORDINARYSUBSCRIBER = 1;
    /**
     * See q.763 3.5 Called party's category indicator
     */
    int _CPCI_PAYPHONE = 2;

    /**
     * See q.763 3.5 End-to-end method indicator (Note 2)
     */
    int _ETEMI_NOMETHODAVAILABLE = 0;
    /**
     * See q.763 3.5 End-to-end method indicator (Note 2)
     */
    int _ETEMI_PASSALONG = 1;
    /**
     * See q.763 3.5 End-to-end method indicator (Note 2)
     */
    int _ETEMI_SCCP = 2;
    /**
     * See q.763 3.5 End-to-end method indicator (Note 2)
     */
    int _ETEMI_SCCP_AND_PASSALONG = 3;

    /**
     * See q.763 3.5 Interworking indicator (Note 2) no interworking encountered (Signalling System No. 7 all the way)
     */
    boolean _II_NO_IE = false;
    /**
     * See q.763 3.5 Interworking indicator (Note 2) interworking encountered
     */
    boolean _II_IE = true;

    /**
     * See q.763 3.5 End-to-end information indicator (national use) (Note 2) no end-to-end information available
     */
    boolean _ETEII_NO_IA = false;
    /**
     * See q.763 3.5 End-to-end information indicator (national use) (Note 2) end-to-end information available
     */
    boolean _ETEII_IA = true;

    /**
     * See q.763 3.5 ISDN user part indicator (Note 2) ISDN user part not used all the way
     */
    boolean _ISDN_UPI_NOT_UATW = false;
    /**
     * See q.763 3.5 ISDN user part indicator (Note 2) ISDN user part used all the way
     */
    boolean _ISDN_UPI_UATW = true;

    /**
     * See q.763 3.5 ISDN access indicator terminating access non-ISDN
     */
    boolean _ISDN_AI_TA_NOT_ISDN = false;
    /**
     * See q.763 3.5 ISDN access indicator terminating access ISDN
     */
    boolean _ISDN_AI_TA_ISDN = true;

    /**
     * See q.763 3.5 Echo control device indicator incoming echo control device not included
     */
    boolean _ECDI_IECD_NOT_INCLUDED = false;
    /**
     * See q.763 3.5 Echo control device indicator incoming echo control device included
     */
    boolean _ECDI_IECD_INCLUDED = true;

    /**
     * See q.763 3.5 Holding indicator (national use)
     */
    boolean _HI_NOT_REQUESTED = false;
    /**
     * See q.763 3.5 Holding indicator (national use)
     */
    boolean _HI_REQUESTED = true;

    /**
     * See q.763 3.5 SCCP method indicator (Note 2) no indication
     */
    int _SCCP_MI_NO_INDICATION = 0;
    /**
     * See q.763 3.5 SCCP method indicator (Note 2) connectionless method available (national use)
     */
    int _SCCP_MI_CONNECTIONLESS = 1;
    /**
     * See q.763 3.5 SCCP method indicator (Note 2) connection oriented method available
     */
    int _SCCP_MI_CONNECTION_ORIENTED = 2;
    /**
     * See q.763 3.5 SCCP method indicator (Note 2) connectionless and connection oriented methods available (national use)
     */
    int _SCCP_MI_CONNLESS_AND_CONN_ORIENTED = 3;

    int getChargeIndicator();

    void setChargeIndicator(int chargeIndicator);

    int getCalledPartysStatusIndicator();

    void setCalledPartysStatusIndicator(int calledPartysStatusIndicator);

    int getCalledPartysCategoryIndicator();

    void setCalledPartysCategoryIndicator(int calledPartysCategoryIndicator);

    int getEndToEndMethodIndicator();

    void setEndToEndMethodIndicator(int endToEndMethodIndicator);

    boolean isInterworkingIndicator();

    void setInterworkingIndicator(boolean interworkingIndicator);

    boolean isEndToEndInformationIndicator();

    void setEndToEndInformationIndicator(boolean endToEndInformationIndicator);

    boolean isIsdnUserPartIndicator();

    void setIsdnUserPartIndicator(boolean isdnUserPartIndicator);

    boolean isIsdnAccessIndicator();

    void setIsdnAccessIndicator(boolean isdnAccessIndicator);

    boolean isEchoControlDeviceIndicator();

    void setEchoControlDeviceIndicator(boolean echoControlDeviceIndicator);

    boolean isHoldingIndicator();

    void setHoldingIndicator(boolean holdingIndicator);

    int getSccpMethodIndicator();

    void setSccpMethodIndicator(int sccpMethodIndicator);

}
