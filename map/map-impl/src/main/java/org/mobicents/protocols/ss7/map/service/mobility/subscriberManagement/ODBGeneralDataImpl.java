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
package org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement;

import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBGeneralData;
import org.mobicents.protocols.ss7.map.primitives.BitStringBase;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class ODBGeneralDataImpl extends BitStringBase implements ODBGeneralData {

    private static final int _INDEX_allOGCallsBarred = 0;
    private static final int _INDEX_internationalOGCallsBarred = 1;
    private static final int _INDEX_internationalOGCallsNotToHPLMNCountryBarred = 2;
    private static final int _INDEX_interzonalOGCallsBarred = 6;
    private static final int _INDEX_interzonalOGCallsNotToHPLMNCountryBarred = 7;
    private static final int _INDEX_interzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred = 8;
    private static final int _INDEX_premiumRateInformationOGCallsBarred = 3;
    private static final int _INDEX_premiumRateEntertainementOGCallsBarred = 4;
    private static final int _INDEX_ssAccessBarred = 5;
    private static final int _INDEX_allECTBarred = 9;
    private static final int _INDEX_chargeableECTBarred = 10;
    private static final int _INDEX_internationalECTBarred = 11;
    private static final int _INDEX_interzonalECTBarred = 12;
    private static final int _INDEX_doublyChargeableECTBarred = 13;
    private static final int _INDEX_multipleECTBarred = 14;
    private static final int _INDEX_allPacketOrientedServicesBarred = 15;
    private static final int _INDEX_roamerAccessToHPLMNAPBarred = 16;
    private static final int _INDEX_roamerAccessToVPLMNAPBarred = 17;
    private static final int _INDEX_roamingOutsidePLMNOGCallsBarred = 18;
    private static final int _INDEX_allICCallsBarred = 19;
    private static final int _INDEX_roamingOutsidePLMNICCallsBarred = 20;
    private static final int _INDEX_roamingOutsidePLMNICountryICCallsBarred = 21;
    private static final int _INDEX_roamingOutsidePLMNBarred = 22;
    private static final int _INDEX_roamingOutsidePLMNCountryBarred = 23;
    private static final int _INDEX_registrationAllCFBarred = 24;
    private static final int _INDEX_registrationCFNotToHPLMNBarred = 25;
    private static final int _INDEX_registrationInterzonalCFBarred = 26;
    private static final int _INDEX_registrationInterzonalCFNotToHPLMNBarred = 27;
    private static final int _INDEX_registrationInternationalCFBarred = 28;

    public ODBGeneralDataImpl() {
        super(15, 32, 29, "ODBGeneralData");
    }

    public ODBGeneralDataImpl(boolean allOGCallsBarred, boolean internationalOGCallsBarred,
            boolean internationalOGCallsNotToHPLMNCountryBarred, boolean interzonalOGCallsBarred,
            boolean interzonalOGCallsNotToHPLMNCountryBarred,
            boolean interzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred,
            boolean premiumRateInformationOGCallsBarred, boolean premiumRateEntertainementOGCallsBarred,
            boolean ssAccessBarred, boolean allECTBarred, boolean chargeableECTBarred, boolean internationalECTBarred,
            boolean interzonalECTBarred, boolean doublyChargeableECTBarred, boolean multipleECTBarred,
            boolean allPacketOrientedServicesBarred, boolean roamerAccessToHPLMNAPBarred, boolean roamerAccessToVPLMNAPBarred,
            boolean roamingOutsidePLMNOGCallsBarred, boolean allICCallsBarred, boolean roamingOutsidePLMNICCallsBarred,
            boolean roamingOutsidePLMNICountryICCallsBarred, boolean roamingOutsidePLMNBarred,
            boolean roamingOutsidePLMNCountryBarred, boolean registrationAllCFBarred, boolean registrationCFNotToHPLMNBarred,
            boolean registrationInterzonalCFBarred, boolean registrationInterzonalCFNotToHPLMNBarred,
            boolean registrationInternationalCFBarred) {
        super(15, 32, 29, "ODBGeneralData");

        if (allOGCallsBarred)
            this.bitString.set(_INDEX_allOGCallsBarred);
        if (internationalOGCallsBarred)
            this.bitString.set(_INDEX_internationalOGCallsBarred);
        if (internationalOGCallsNotToHPLMNCountryBarred)
            this.bitString.set(_INDEX_internationalOGCallsNotToHPLMNCountryBarred);
        if (interzonalOGCallsBarred)
            this.bitString.set(_INDEX_interzonalOGCallsBarred);
        if (interzonalOGCallsNotToHPLMNCountryBarred)
            this.bitString.set(_INDEX_interzonalOGCallsNotToHPLMNCountryBarred);
        if (interzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred)
            this.bitString.set(_INDEX_interzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred);
        if (premiumRateInformationOGCallsBarred)
            this.bitString.set(_INDEX_premiumRateInformationOGCallsBarred);
        if (premiumRateEntertainementOGCallsBarred)
            this.bitString.set(_INDEX_premiumRateEntertainementOGCallsBarred);
        if (ssAccessBarred)
            this.bitString.set(_INDEX_ssAccessBarred);
        if (allECTBarred)
            this.bitString.set(_INDEX_allECTBarred);
        if (chargeableECTBarred)
            this.bitString.set(_INDEX_chargeableECTBarred);
        if (internationalECTBarred)
            this.bitString.set(_INDEX_internationalECTBarred);
        if (interzonalECTBarred)
            this.bitString.set(_INDEX_interzonalECTBarred);
        if (doublyChargeableECTBarred)
            this.bitString.set(_INDEX_doublyChargeableECTBarred);
        if (multipleECTBarred)
            this.bitString.set(_INDEX_multipleECTBarred);
        if (allPacketOrientedServicesBarred)
            this.bitString.set(_INDEX_allPacketOrientedServicesBarred);
        if (roamerAccessToHPLMNAPBarred)
            this.bitString.set(_INDEX_roamerAccessToHPLMNAPBarred);
        if (roamerAccessToVPLMNAPBarred)
            this.bitString.set(_INDEX_roamerAccessToVPLMNAPBarred);
        if (roamingOutsidePLMNOGCallsBarred)
            this.bitString.set(_INDEX_roamingOutsidePLMNOGCallsBarred);
        if (allICCallsBarred)
            this.bitString.set(_INDEX_allICCallsBarred);
        if (roamingOutsidePLMNICCallsBarred)
            this.bitString.set(_INDEX_roamingOutsidePLMNICCallsBarred);
        if (roamingOutsidePLMNICountryICCallsBarred)
            this.bitString.set(_INDEX_roamingOutsidePLMNICountryICCallsBarred);
        if (roamingOutsidePLMNBarred)
            this.bitString.set(_INDEX_roamingOutsidePLMNBarred);
        if (roamingOutsidePLMNCountryBarred)
            this.bitString.set(_INDEX_roamingOutsidePLMNCountryBarred);
        if (registrationAllCFBarred)
            this.bitString.set(_INDEX_registrationAllCFBarred);
        if (registrationCFNotToHPLMNBarred)
            this.bitString.set(_INDEX_registrationCFNotToHPLMNBarred);
        if (registrationInterzonalCFBarred)
            this.bitString.set(_INDEX_registrationInterzonalCFBarred);
        if (registrationInterzonalCFNotToHPLMNBarred)
            this.bitString.set(_INDEX_registrationInterzonalCFNotToHPLMNBarred);
        if (registrationInternationalCFBarred)
            this.bitString.set(_INDEX_registrationInternationalCFBarred);
    }

    @Override
    public boolean getAllOGCallsBarred() {
        return this.bitString.get(_INDEX_allOGCallsBarred);
    }

    @Override
    public boolean getInternationalOGCallsBarred() {
        return this.bitString.get(_INDEX_internationalOGCallsBarred);
    }

    @Override
    public boolean getInternationalOGCallsNotToHPLMNCountryBarred() {
        return this.bitString.get(_INDEX_internationalOGCallsNotToHPLMNCountryBarred);
    }

    @Override
    public boolean getInterzonalOGCallsBarred() {
        return this.bitString.get(_INDEX_interzonalOGCallsBarred);
    }

    @Override
    public boolean getInterzonalOGCallsNotToHPLMNCountryBarred() {
        return this.bitString.get(_INDEX_interzonalOGCallsNotToHPLMNCountryBarred);
    }

    @Override
    public boolean getInterzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred() {
        return this.bitString.get(_INDEX_interzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred);
    }

    @Override
    public boolean getPremiumRateInformationOGCallsBarred() {
        return this.bitString.get(_INDEX_premiumRateInformationOGCallsBarred);
    }

    @Override
    public boolean getPremiumRateEntertainementOGCallsBarred() {
        return this.bitString.get(_INDEX_premiumRateEntertainementOGCallsBarred);
    }

    @Override
    public boolean getSsAccessBarred() {
        return this.bitString.get(_INDEX_ssAccessBarred);
    }

    @Override
    public boolean getAllECTBarred() {
        return this.bitString.get(_INDEX_allECTBarred);
    }

    @Override
    public boolean getChargeableECTBarred() {
        return this.bitString.get(_INDEX_chargeableECTBarred);
    }

    @Override
    public boolean getInternationalECTBarred() {
        return this.bitString.get(_INDEX_internationalECTBarred);
    }

    @Override
    public boolean getInterzonalECTBarred() {
        return this.bitString.get(_INDEX_interzonalECTBarred);
    }

    @Override
    public boolean getDoublyChargeableECTBarred() {
        return this.bitString.get(_INDEX_doublyChargeableECTBarred);
    }

    @Override
    public boolean getMultipleECTBarred() {
        return this.bitString.get(_INDEX_multipleECTBarred);
    }

    @Override
    public boolean getAllPacketOrientedServicesBarred() {
        return this.bitString.get(_INDEX_allPacketOrientedServicesBarred);
    }

    @Override
    public boolean getRoamerAccessToHPLMNAPBarred() {
        return this.bitString.get(_INDEX_roamerAccessToHPLMNAPBarred);
    }

    @Override
    public boolean getRoamerAccessToVPLMNAPBarred() {
        return this.bitString.get(_INDEX_roamerAccessToVPLMNAPBarred);
    }

    @Override
    public boolean getRoamingOutsidePLMNOGCallsBarred() {
        return this.bitString.get(_INDEX_roamingOutsidePLMNOGCallsBarred);
    }

    @Override
    public boolean getAllICCallsBarred() {
        return this.bitString.get(_INDEX_allICCallsBarred);
    }

    @Override
    public boolean getRoamingOutsidePLMNICCallsBarred() {
        return this.bitString.get(_INDEX_roamingOutsidePLMNICCallsBarred);
    }

    @Override
    public boolean getRoamingOutsidePLMNICountryICCallsBarred() {
        return this.bitString.get(_INDEX_roamingOutsidePLMNICountryICCallsBarred);
    }

    @Override
    public boolean getRoamingOutsidePLMNBarred() {
        return this.bitString.get(_INDEX_roamingOutsidePLMNBarred);
    }

    @Override
    public boolean getRoamingOutsidePLMNCountryBarred() {
        return this.bitString.get(_INDEX_roamingOutsidePLMNCountryBarred);
    }

    @Override
    public boolean getRegistrationAllCFBarred() {
        return this.bitString.get(_INDEX_registrationAllCFBarred);
    }

    @Override
    public boolean getRegistrationCFNotToHPLMNBarred() {
        return this.bitString.get(_INDEX_registrationCFNotToHPLMNBarred);
    }

    @Override
    public boolean getRegistrationInterzonalCFBarred() {
        return this.bitString.get(_INDEX_registrationInterzonalCFBarred);
    }

    @Override
    public boolean getRegistrationInterzonalCFNotToHPLMNBarred() {
        return this.bitString.get(_INDEX_registrationInterzonalCFNotToHPLMNBarred);
    }

    @Override
    public boolean getRegistrationInternationalCFBarred() {
        return this.bitString.get(_INDEX_registrationInternationalCFBarred);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ODBGeneralData [");
        if (getAllOGCallsBarred())
            sb.append("allOGCallsBarred, ");
        if (getInternationalOGCallsBarred())
            sb.append("internationalOGCallsBarred, ");
        if (getInternationalOGCallsNotToHPLMNCountryBarred())
            sb.append("internationalOGCallsNotToHPLMNCountryBarred, ");
        if (getInterzonalOGCallsBarred())
            sb.append("interzonalOGCallsBarred, ");
        if (getInterzonalOGCallsNotToHPLMNCountryBarred())
            sb.append("interzonalOGCallsNotToHPLMNCountryBarred, ");
        if (getInterzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred())
            sb.append("interzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred, ");
        if (getPremiumRateInformationOGCallsBarred())
            sb.append("premiumRateInformationOGCallsBarred, ");
        if (getPremiumRateEntertainementOGCallsBarred())
            sb.append("premiumRateEntertainementOGCallsBarred, ");
        if (getSsAccessBarred())
            sb.append("ssAccessBarred, ");
        if (getAllECTBarred())
            sb.append("allECTBarred, ");
        if (getChargeableECTBarred())
            sb.append("chargeableECTBarred, ");
        if (getInternationalECTBarred())
            sb.append("internationalECTBarred, ");
        if (getInterzonalECTBarred())
            sb.append("interzonalECTBarred, ");
        if (getDoublyChargeableECTBarred())
            sb.append("doublyChargeableECTBarred, ");
        if (getMultipleECTBarred())
            sb.append("multipleECTBarred, ");
        if (getAllPacketOrientedServicesBarred())
            sb.append("allPacketOrientedServicesBarred, ");
        if (getRoamerAccessToHPLMNAPBarred())
            sb.append("roamerAccessToHPLMNAPBarred, ");
        if (getRoamerAccessToVPLMNAPBarred())
            sb.append("roamerAccessToVPLMNAPBarred, ");
        if (getRoamingOutsidePLMNOGCallsBarred())
            sb.append("roamingOutsidePLMNOGCallsBarred");
        if (getAllICCallsBarred())
            sb.append("allICCallsBarred, ");
        if (getRoamingOutsidePLMNICCallsBarred())
            sb.append("roamingOutsidePLMNICCallsBarred, ");
        if (getRoamingOutsidePLMNICountryICCallsBarred())
            sb.append("roamingOutsidePLMNICountryICCallsBarred, ");
        if (getRoamingOutsidePLMNBarred())
            sb.append("roamingOutsidePLMNBarred, ");
        if (getRoamingOutsidePLMNCountryBarred())
            sb.append("roamingOutsidePLMNCountryBarred, ");
        if (getRegistrationAllCFBarred())
            sb.append("registrationAllCFBarred, ");
        if (getRegistrationCFNotToHPLMNBarred())
            sb.append("registrationCFNotToHPLMNBarred, ");
        if (getRegistrationInterzonalCFBarred())
            sb.append("registrationInterzonalCFBarred, ");
        if (getRegistrationInterzonalCFNotToHPLMNBarred())
            sb.append("registrationInterzonalCFNotToHPLMNBarred, ");
        if (getRegistrationInternationalCFBarred())
            sb.append("registrationInternationalCFBarred, ");
        sb.append("]");

        return sb.toString();
    }

}
