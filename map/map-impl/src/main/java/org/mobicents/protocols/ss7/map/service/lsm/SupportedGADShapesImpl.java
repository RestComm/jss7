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

package org.mobicents.protocols.ss7.map.service.lsm;

import org.mobicents.protocols.ss7.map.api.service.lsm.SupportedGADShapes;
import org.mobicents.protocols.ss7.map.primitives.BitStringBase;

/**
 * @author amit bhayani
 *
 */
public class SupportedGADShapesImpl extends BitStringBase implements SupportedGADShapes {

    private static final int _INDEX_ELLIPSOID_POINT = 0;
    private static final int _INDEX_ELLIPSOID_POINT_WITH_UNCERTAINTY_CIRCLE = 1;
    private static final int _INDEX_ELLIPSOID_POINT_WITH_UNCERTAINTY_ELLIPSE = 2;
    private static final int _INDEX_POLYGON = 3;
    private static final int _INDEX_ELLIPSOID_POINT_WITH_ALTITUDE = 4;
    private static final int _INDEX_ELLIPSOID_WITH_ALTITUDE_AND_UNCERTAINTY_ELIPSOID = 5;
    private static final int _INDEX_ELLIPSOID_ARC = 6;

    public SupportedGADShapesImpl() {
        super(7, 16, 7, "SupportedGADShapes");
    }

    public SupportedGADShapesImpl(boolean ellipsoidPoint, boolean ellipsoidPointWithUncertaintyCircle,
            boolean ellipsoidPointWithUncertaintyEllipse, boolean polygon, boolean ellipsoidPointWithAltitude,
            boolean ellipsoidPointWithAltitudeAndUncertaintyElipsoid, boolean ellipsoidArc) {
        super(7, 16, 7, "SupportedGADShapes");

        if (ellipsoidPoint)
            this.bitString.set(_INDEX_ELLIPSOID_POINT);
        if (ellipsoidPointWithUncertaintyCircle)
            this.bitString.set(_INDEX_ELLIPSOID_POINT_WITH_UNCERTAINTY_CIRCLE);
        if (ellipsoidPointWithUncertaintyEllipse)
            this.bitString.set(_INDEX_ELLIPSOID_POINT_WITH_UNCERTAINTY_ELLIPSE);
        if (polygon)
            this.bitString.set(_INDEX_POLYGON);
        if (ellipsoidPointWithAltitude)
            this.bitString.set(_INDEX_ELLIPSOID_POINT_WITH_ALTITUDE);
        if (ellipsoidPointWithAltitudeAndUncertaintyElipsoid)
            this.bitString.set(_INDEX_ELLIPSOID_WITH_ALTITUDE_AND_UNCERTAINTY_ELIPSOID);
        if (ellipsoidArc)
            this.bitString.set(_INDEX_ELLIPSOID_ARC);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm.SupportedGADShapes# getEllipsoidPoint()
     */
    public boolean getEllipsoidPoint() {
        return this.bitString.get(_INDEX_ELLIPSOID_POINT);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm.SupportedGADShapes# getEllipsoidPointWithUncertaintyCircle()
     */
    public boolean getEllipsoidPointWithUncertaintyCircle() {
        return this.bitString.get(_INDEX_ELLIPSOID_POINT_WITH_UNCERTAINTY_CIRCLE);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm.SupportedGADShapes# getEllipsoidPointWithUncertaintyEllipse()
     */
    public boolean getEllipsoidPointWithUncertaintyEllipse() {
        return this.bitString.get(_INDEX_ELLIPSOID_POINT_WITH_UNCERTAINTY_ELLIPSE);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm.SupportedGADShapes#getPolygon ()
     */
    public boolean getPolygon() {
        return this.bitString.get(_INDEX_POLYGON);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm.SupportedGADShapes# getEllipsoidPointWithAltitude()
     */
    public boolean getEllipsoidPointWithAltitude() {
        return this.bitString.get(_INDEX_ELLIPSOID_POINT_WITH_ALTITUDE);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm.SupportedGADShapes#
     * getEllipsoidPointWithAltitudeAndUncertaintyElipsoid()
     */
    public boolean getEllipsoidPointWithAltitudeAndUncertaintyElipsoid() {
        return this.bitString.get(_INDEX_ELLIPSOID_WITH_ALTITUDE_AND_UNCERTAINTY_ELIPSOID);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm.SupportedGADShapes# getEllipsoidArc()
     */
    public boolean getEllipsoidArc() {
        return this.bitString.get(_INDEX_ELLIPSOID_ARC);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.getEllipsoidPoint()) {
            sb.append("ellipsoidPoint");
        }
        if (this.getEllipsoidPointWithUncertaintyCircle()) {
            sb.append(", ellipsoidPointWithUncertaintyCircle");
        }
        if (this.getEllipsoidPointWithUncertaintyEllipse()) {
            sb.append(", ellipsoidPointWithUncertaintyEllipse");
        }
        if (this.getPolygon()) {
            sb.append(", polygon");
        }
        if (this.getEllipsoidPointWithAltitude()) {
            sb.append(", ellipsoidPointWithAltitude");
        }
        if (this.getEllipsoidPointWithAltitudeAndUncertaintyElipsoid()) {
            sb.append(", ellipsoidPointWithAltitudeAndUncertaintyElipsoid");
        }
        if (this.getEllipsoidArc()) {
            sb.append(", ellipsoidArc");
        }

        sb.append("]");

        return sb.toString();
    }
}
