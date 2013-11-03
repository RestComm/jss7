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
 * Start time:13:36:04 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:13:36:04 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface OptionalBackwardCallIndicators extends ISUPParameter {
    int _PARAMETER_CODE = 0x29;

    /**
     * See Q.763 3.37 In-band information indicator
     */
    boolean _IBII_NO_INDICATION = false;
    /**
     * See Q.763 3.37 In-band information indicator
     */
    boolean _IBII_AVAILABLE = true;

    /**
     * See Q.763 3.37 Call diversion may occur indicator
     */
    boolean _CDI_NO_INDICATION = false;

    /**
     * See Q.763 3.37 Call diversion may occur indicator
     */
    boolean _CDI_MAY_OCCUR = true;

    /**
     * See Q.763 3.37 Simple segmentation indicator
     */
    boolean _SSIR_NO_ADDITIONAL_INFO = false;

    /**
     * See Q.763 3.37 Simple segmentation indicator
     */
    boolean _SSIR_ADDITIONAL_INFO = true;

    /**
     * See Q.763 3.37 MLPP user indicator
     */
    boolean _MLLPUI_NO_INDICATION = false;

    /**
     * See Q.763 3.37 MLPP user indicator
     */
    boolean _MLLPUI_USER = true;

    boolean isInbandInformationIndicator();

    void setInbandInformationIndicator(boolean inbandInformationIndicator);

    boolean isCallDiversionMayOccurIndicator();

    void setCallDiversionMayOccurIndicator(boolean callDiversionMayOccurIndicator);

    boolean isSimpleSegmentationIndicator();

    void setSimpleSegmentationIndicator(boolean simpleSegmentationIndicator);

    boolean isMllpUserIndicator();

    void setMllpUserIndicator(boolean mllpUserIndicator);
}
