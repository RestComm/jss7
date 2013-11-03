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
 *
 */
package org.mobicents.protocols.ss7.sccp.impl.parameter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.mobicents.protocols.ss7.sccp.parameter.Segmentation;

/**
 * See Q.713 3.17
 *
 * @author baranowb
 *
 */
public class SegmentationImpl extends AbstractParameter implements Segmentation {

    private static final int _TRUE = 1;
    private static final int _FALSE = 0;
    private boolean firstSegIndication = false;
    private boolean class1Selected = false;
    private byte remainingSegments = 0x0F;
    private int segmentationLocalRef;

    public SegmentationImpl() {
        // TODO Auto-generated constructor stub
    }

    public SegmentationImpl(boolean firstSegIndication, boolean class1Selected, byte remainingSegments, int segmentationLocalRef) {
        super();
        this.firstSegIndication = firstSegIndication;
        this.class1Selected = class1Selected;
        this.remainingSegments = remainingSegments;
        this.segmentationLocalRef = segmentationLocalRef;
    }

    /**
     * Bit 8 of octet 1 is used for First segment indication and is coded as follows:
     * <ul>
     * <li>0: in all segments but the first;</li>
     * <li>1: first segment.</li>
     * </ul>
     *
     * @return <ul>
     *         <li><b>true</b></li> - in case first segment indication bit is equal to 1(first segment)
     *         <li><b>false</b> - in case segment indication is equal 0(in all segments but the first)</li>
     *         </ul>
     */
    public boolean isFirstSegIndication() {
        return firstSegIndication;
    }

    /**
     * Bit 8 of octet 1 is used for First segment indication and is coded as follows:
     * <ul>
     * <li>0: in all segments but the first;</li>
     * <li>1: first segment.</li>
     * </ul>
     * <ul>
     * <li><b>true</b></li> - in case first segment indication bit is equal to 1(first segment)
     * <li><b>false</b> - in case segment indication is equal 0(in all segments but the first)</li>
     * </ul>
     *
     * @param firstSegIndication
     */
    public void setFirstSegIndication(boolean firstSegIndication) {
        this.firstSegIndication = firstSegIndication;
    }

    /**
     * Bit 7 of octet 1 is used to keep in the message in sequence delivery option required by the SCCP user and is coded as
     * follows:
     *
     * @return <li><b>true</b></li> - class 1 selected <li><b>false</b> - class 0 selected</li> </ul>
     */
    public boolean isClass1Selected() {
        return class1Selected;
    }

    /**
     * Bit 7 of octet 1 is used to keep in the message in sequence delivery option required by the SCCP user and is coded as
     * follows:
     *
     * @return <li><b>true</b></li> - class 1 selected <li><b>false</b> - class 0 selected</li> </ul>
     */
    public void setClass1Selected(boolean class1Selected) {
        this.class1Selected = class1Selected;
    }

    /**
     * Bits 4-1 of octet 1 are used to indicate the number of remaining segments. The values 0000 to 1111 are possible; the
     * value 0000 indicates the last segment.
     *
     * @return
     */
    public byte getRemainingSegments() {
        return remainingSegments;
    }

    /**
     * Bits 4-1 of octet 1 are used to indicate the number of remaining segments. The values 0000 to 1111 are possible; the
     * value 0000 indicates the last segment.
     *
     * @param remainingSegments
     */
    public void setRemainingSegments(byte remainingSegments) {
        if (remainingSegments < 0 || remainingSegments > 0x0F) {
            throw new IllegalArgumentException("Wrong value of remaining segments: " + remainingSegments);
        }
        this.remainingSegments = remainingSegments;
    }

    public int getSegmentationLocalRef() {
        return segmentationLocalRef;
    }

    public void setSegmentationLocalRef(int segmentationLocalRef) {
        this.segmentationLocalRef = segmentationLocalRef;

    }

    public void decode(InputStream in) throws IOException {
        byte[] buffer = new byte[in.read()];
        if (in.read(buffer) != buffer.length) {
            throw new IOException();
        }
        this.decode(buffer);
    }

    public void encode(OutputStream os) throws IOException {
        byte[] buffer = this.encode();
        os.write(buffer.length);
        os.write(buffer);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.sccp.OptionalParameter#decode(byte[])
     */

    public void decode(byte[] buffer) throws IOException {
        int v = buffer[0];
        this.firstSegIndication = ((v >> 7) & 0x01) == _TRUE;
        this.class1Selected = ((v >> 6) & 0x01) == _TRUE;
        this.remainingSegments = (byte) (v & 0x0F);
        this.segmentationLocalRef = (buffer[1] & 0xFF) + ((buffer[2] & 0xFF) << 8) + ((buffer[3] & 0xFF) << 16);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.sccp.OptionalParameter#encode()
     */

    public byte[] encode() throws IOException {
        byte[] buffer = new byte[4];
        int v = this.remainingSegments & 0x0F;
        v |= ((this.class1Selected ? _TRUE : _FALSE) << 6);
        v |= ((this.firstSegIndication ? _TRUE : _FALSE) << 7);
        buffer[0] = (byte) v;
        buffer[1] = (byte) (this.segmentationLocalRef & 0xFF);
        buffer[2] = (byte) ((this.segmentationLocalRef >> 8) & 0xFF);
        buffer[3] = (byte) ((this.segmentationLocalRef >> 16) & 0xFF);

        return buffer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Segmentation [remainingSegments=");
        sb.append(this.getRemainingSegments());
        if (this.isFirstSegIndication())
            sb.append(" firstSegment");
        if (this.isClass1Selected())
            sb.append(" class1Selected");
        sb.append(" localRef=");
        sb.append(this.getSegmentationLocalRef());
        sb.append("]");
        return sb.toString();
    }

    // public int hashCode() {
    // final int prime = 31;
    // int result = 1;
    // result = prime * result + (class1Selected ? 1231 : 1237);
    // result = prime * result + (firstSegIndication ? 1231 : 1237);
    // result = prime * result + remainingSegments;
    // result = prime * result + Arrays.hashCode(segmentationLocalRef);
    // return result;
    // }
    //
    //
    // public boolean equals(Object obj) {
    // if (this == obj)
    // return true;
    // if (obj == null)
    // return false;
    // if (getClass() != obj.getClass())
    // return false;
    // SegmentationImpl other = (SegmentationImpl) obj;
    // if (class1Selected != other.class1Selected)
    // return false;
    // if (firstSegIndication != other.firstSegIndication)
    // return false;
    // if (remainingSegments != other.remainingSegments)
    // return false;
    // if (!Arrays.equals(segmentationLocalRef, other.segmentationLocalRef))
    // return false;
    // return true;
    // }
}
