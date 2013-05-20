/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

package org.mobicents.protocols.ss7.map.api.service.supplementary;

import java.util.ArrayList;

import org.mobicents.protocols.ss7.map.api.primitives.EMLPPPriority;

/**
 *
 GenericServiceInfo ::= SEQUENCE { ss-Status SS-Status, cliRestrictionOption CliRestrictionOption OPTIONAL, ...,
 * maximumEntitledPriority [0] EMLPP-Priority OPTIONAL, defaultPriority [1] EMLPP-Priority OPTIONAL, ccbs-FeatureList [2]
 * CCBS-FeatureList OPTIONAL, nbrSB [3] MaxMC-Bearers OPTIONAL, nbrUser [4] MC-Bearers OPTIONAL, nbrSN [5] MC-Bearers OPTIONAL }
 *
 * CCBS-FeatureList ::= SEQUENCE SIZE (1..5) OF CCBS-Feature
 *
 * MC-Bearers ::= INTEGER (1..7) MaxMC-Bearers ::= INTEGER (2..7)
 *
 * @author sergey vetyutnev
 *
 */
public interface GenericServiceInfo {

    SSStatus getSsStatus();

    CliRestrictionOption getCliRestrictionOption();

    EMLPPPriority getMaximumEntitledPriority();

    EMLPPPriority getDefaultPriority();

    ArrayList<CCBSFeature> getCcbsFeatureList();

    Integer getNbrSB();

    Integer getNbrUser();

    Integer getNbrSN();

}
