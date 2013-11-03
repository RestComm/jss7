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

package org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement;

import java.io.Serializable;

/**
 *
 ODB-GeneralData ::= BIT STRING { allOG-CallsBarred (0), internationalOGCallsBarred (1),
 * internationalOGCallsNotToHPLMN-CountryBarred (2), interzonalOGCallsBarred (6), interzonalOGCallsNotToHPLMN-CountryBarred (7),
 * interzonalOGCallsAndInternationalOGCallsNotToHPLMN-CountryBarred (8), premiumRateInformationOGCallsBarred (3),
 * premiumRateEntertainementOGCallsBarred (4), ss-AccessBarred (5), allECT-Barred (9), chargeableECT-Barred (10),
 * internationalECT-Barred (11), interzonalECT-Barred (12), doublyChargeableECT-Barred (13), multipleECT-Barred (14),
 * allPacketOrientedServicesBarred (15), roamerAccessToHPLMN-AP-Barred (16), roamerAccessToVPLMN-AP-Barred (17),
 * roamingOutsidePLMNOG-CallsBarred (18), allIC-CallsBarred (19), roamingOutsidePLMNIC-CallsBarred (20),
 * roamingOutsidePLMNICountryIC-CallsBarred (21), roamingOutsidePLMN-Barred (22), roamingOutsidePLMN-CountryBarred (23),
 * registrationAllCF-Barred (24), registrationCFNotToHPLMN-Barred (25), registrationInterzonalCF-Barred (26),
 * registrationInterzonalCFNotToHPLMN-Barred (27), registrationInternationalCF-Barred (28)} (SIZE (15..32)) -- exception
 * handling: reception of unknown bit assignments in the -- ODB-GeneralData type shall be treated like unsupported
 * ODB-GeneralData -- When the ODB-GeneralData type is removed from the HLR for a given subscriber, -- in
 * NoteSubscriberDataModified operation sent toward the gsmSCF -- all bits shall be set to O.
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface ODBGeneralData extends Serializable {

    boolean getAllOGCallsBarred();

    boolean getInternationalOGCallsBarred();

    boolean getInternationalOGCallsNotToHPLMNCountryBarred();

    boolean getInterzonalOGCallsBarred();

    boolean getInterzonalOGCallsNotToHPLMNCountryBarred();

    boolean getInterzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred();

    boolean getPremiumRateInformationOGCallsBarred();

    boolean getPremiumRateEntertainementOGCallsBarred();

    boolean getSsAccessBarred();

    boolean getAllECTBarred();

    boolean getChargeableECTBarred();

    boolean getInternationalECTBarred();

    boolean getInterzonalECTBarred();

    boolean getDoublyChargeableECTBarred();

    boolean getMultipleECTBarred();

    boolean getAllPacketOrientedServicesBarred();

    boolean getRoamerAccessToHPLMNAPBarred();

    boolean getRoamerAccessToVPLMNAPBarred();

    boolean getRoamingOutsidePLMNOGCallsBarred();

    boolean getAllICCallsBarred();

    boolean getRoamingOutsidePLMNICCallsBarred();

    boolean getRoamingOutsidePLMNICountryICCallsBarred();

    boolean getRoamingOutsidePLMNBarred();

    boolean getRoamingOutsidePLMNCountryBarred();

    boolean getRegistrationAllCFBarred();

    boolean getRegistrationCFNotToHPLMNBarred();

    boolean getRegistrationInterzonalCFBarred();

    boolean getRegistrationInterzonalCFNotToHPLMNBarred();

    boolean getRegistrationInternationalCFBarred();

}
