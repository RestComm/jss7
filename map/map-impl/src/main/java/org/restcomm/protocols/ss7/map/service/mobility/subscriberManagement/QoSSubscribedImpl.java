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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.QoSSubscribed;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.QoSSubscribed_DelayClass;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.QoSSubscribed_MeanThroughput;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.QoSSubscribed_PeakThroughput;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.QoSSubscribed_PrecedenceClass;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.QoSSubscribed_ReliabilityClass;
import org.restcomm.protocols.ss7.map.primitives.OctetStringBase;

/**
 *
 * @author daniel bichara
 * @author sergey vetyutnev
 *
 */
public class QoSSubscribedImpl extends OctetStringBase implements QoSSubscribed {

    public QoSSubscribedImpl() {
        super(3, 3, "QoSSubscribed");
    }

    public QoSSubscribedImpl(byte[] data) {
        super(3, 3, "QoSSubscribed", data);
    }

    public QoSSubscribedImpl(QoSSubscribed_ReliabilityClass reliabilityClass, QoSSubscribed_DelayClass delayClass,
            QoSSubscribed_PrecedenceClass precedenceClass, QoSSubscribed_PeakThroughput peakThroughput, QoSSubscribed_MeanThroughput meanThroughput) {
        super(3, 3, "QoSSubscribed");

        this.setData(reliabilityClass, delayClass, precedenceClass, peakThroughput, meanThroughput);
    }

    protected void setData(QoSSubscribed_ReliabilityClass reliabilityClass, QoSSubscribed_DelayClass delayClass,
            QoSSubscribed_PrecedenceClass precedenceClass, QoSSubscribed_PeakThroughput peakThroughput, QoSSubscribed_MeanThroughput meanThroughput) {
        this.data = new byte[3];

        this.data[0] = (byte) (((delayClass != null ? delayClass.getCode() : 0) << 3) + (reliabilityClass != null ? reliabilityClass.getCode() : 0));
        this.data[1] = (byte) (((peakThroughput != null ? peakThroughput.getCode() : 0) << 4) + (precedenceClass != null ? precedenceClass.getCode() : 0));
        this.data[2] = (byte) (meanThroughput != null ? meanThroughput.getCode() : 0);
    }

    public byte[] getData() {
        return data;
    }

    private boolean checkDataLen() {
        if (this.data != null && this.data.length == 3)
            return true;
        else
            return false;
    }

    @Override
    public QoSSubscribed_ReliabilityClass getReliabilityClass() {
        if (!checkDataLen())
            return null;

        return QoSSubscribed_ReliabilityClass.getInstance(this.data[0] & 0x07);
    }

    @Override
    public QoSSubscribed_DelayClass getDelayClass() {
        if (!checkDataLen())
            return null;

        return QoSSubscribed_DelayClass.getInstance((this.data[0] & 0x38) >> 3);
    }

    @Override
    public QoSSubscribed_PrecedenceClass getPrecedenceClass() {
        if (!checkDataLen())
            return null;

        return QoSSubscribed_PrecedenceClass.getInstance(this.data[1] & 0x07);
    }

    @Override
    public QoSSubscribed_PeakThroughput getPeakThroughput() {
        if (!checkDataLen())
            return null;

        return QoSSubscribed_PeakThroughput.getInstance((this.data[1] & 0xF0) >> 4);
    }

    @Override
    public QoSSubscribed_MeanThroughput getMeanThroughput() {
        if (!checkDataLen())
            return null;

        return QoSSubscribed_MeanThroughput.getInstance(this.data[2] & 0x1F);
    }

    @Override
    public String toString() {
        if (checkDataLen()) {
            QoSSubscribed_ReliabilityClass reliabilityClass = getReliabilityClass();
            QoSSubscribed_DelayClass delayClass = getDelayClass();
            QoSSubscribed_PrecedenceClass precedenceClass = getPrecedenceClass();
            QoSSubscribed_PeakThroughput peakThroughput = getPeakThroughput();
            QoSSubscribed_MeanThroughput meanThroughput = getMeanThroughput();

            StringBuilder sb = new StringBuilder();
            sb.append(_PrimitiveName);
            sb.append(" [");
            if (reliabilityClass != null) {
                sb.append("reliabilityClass=");
                sb.append(reliabilityClass);
                sb.append(", ");
            }
            if (delayClass != null) {
                sb.append("delayClass=");
                sb.append(delayClass);
                sb.append(", ");
            }
            if (precedenceClass != null) {
                sb.append("precedenceClass=");
                sb.append(precedenceClass);
                sb.append(", ");
            }
            if (peakThroughput != null) {
                sb.append("peakThroughput=");
                sb.append(peakThroughput);
                sb.append(", ");
            }
            if (meanThroughput != null) {
                sb.append("meanThroughput=");
                sb.append(meanThroughput);
                sb.append(", ");
            }
            sb.append("]");

            return sb.toString();
        } else {
            return super.toString();
        }
    }

}
