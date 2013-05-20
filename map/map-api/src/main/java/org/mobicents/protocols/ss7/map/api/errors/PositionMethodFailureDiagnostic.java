/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.mobicents.protocols.ss7.map.api.errors;

/**
 *
 * PositionMethodFailure-Diagnostic ::= ENUMERATED { congestion (0), insufficientResources (1), insufficientMeasurementData (2),
 * inconsistentMeasurementData (3), locationProcedureNotCompleted (4), locationProcedureNotSupportedByTargetMS (5),
 * qoSNotAttainable (6), positionMethodNotAvailableInNetwork (7), positionMethodNotAvailableInLocationArea (8), ... } --
 * exception handling: -- any unrecognized value shall be ignored
 *
 *
 * @author sergey vetyutnev
 *
 */
public enum PositionMethodFailureDiagnostic {
    congestion(0), insufficientResources(1), insufficientMeasurementData(2), inconsistentMeasurementData(3), locationProcedureNotCompleted(
            4), locationProcedureNotSupportedByTargetMS(5), qoSNotAttainable(6), positionMethodNotAvailableInNetwork(7), positionMethodNotAvailableInLocationArea(
            8);

    private int code;

    private PositionMethodFailureDiagnostic(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static PositionMethodFailureDiagnostic getInstance(int code) {
        switch (code) {
            case 0:
                return congestion;
            case 1:
                return insufficientResources;
            case 2:
                return insufficientMeasurementData;
            case 3:
                return inconsistentMeasurementData;
            case 4:
                return locationProcedureNotCompleted;
            case 5:
                return locationProcedureNotSupportedByTargetMS;
            case 6:
                return qoSNotAttainable;
            case 7:
                return positionMethodNotAvailableInNetwork;
            case 8:
                return positionMethodNotAvailableInLocationArea;
            default:
                return null;
        }
    }

}
