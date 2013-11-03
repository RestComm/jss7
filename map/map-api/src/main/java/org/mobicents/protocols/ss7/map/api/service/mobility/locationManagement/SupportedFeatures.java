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

package org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement;

import java.io.Serializable;

/**
 *
 SupportedFeatures::= BIT STRING { odb-all-apn (0), odb-HPLMN-APN (1), odb-VPLMN-APN (2), odb-all-og (3),
 * odb-all-international-og (4), odb-all-int-og-not-to-HPLMN-country (5), odb-all-interzonal-og (6),
 * odb-all-interzonal-og-not-to-HPLMN-country (7), odb-all-interzonal-og-and-internat-og-not-to-HPLMN-country (8), regSub (9),
 * trace (10), lcs-all-PrivExcep (11), lcs-universal (12), lcs-CallSessionRelated (13), lcs-CallSessionUnrelated (14),
 * lcs-PLMN-operator (15), lcs-ServiceType (16), lcs-all-MOLR-SS (17), lcs-basicSelfLocation (18), lcs-autonomousSelfLocation
 * (19), lcs-transferToThirdParty (20), sm-mo-pp (21), barring-OutgoingCalls (22), baoc (23), boic (24), boicExHC (25)} (SIZE
 * (26..40))
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface SupportedFeatures extends Serializable {

    boolean getOdbAllApn();

    boolean getOdbHPLMNApn();

    boolean getOdbVPLMNApn();

    boolean getOdbAllOg();

    boolean getOdbAllInternationalOg();

    boolean getOdbAllIntOgNotToHPLMNCountry();

    boolean getOdbAllInterzonalOg();

    boolean getOdbAllInterzonalOgNotToHPLMNCountry();

    boolean getOdbAllInterzonalOgandInternatOgNotToHPLMNCountry();

    boolean getRegSub();

    boolean getTrace();

    boolean getLcsAllPrivExcep();

    boolean getLcsUniversal();

    boolean getLcsCallSessionRelated();

    boolean getLcsCallSessionUnrelated();

    boolean getLcsPLMNOperator();

    boolean getLcsServiceType();

    boolean getLcsAllMOLRSS();

    boolean getLcsBasicSelfLocation();

    boolean getLcsAutonomousSelfLocation();

    boolean getLcsTransferToThirdParty();

    boolean getSmMoPp();

    boolean getBarringOutgoingCalls();

    boolean getBaoc();

    boolean getBoic();

    boolean getBoicExHC();

}
