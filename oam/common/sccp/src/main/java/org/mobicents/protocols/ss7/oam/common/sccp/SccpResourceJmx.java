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
package org.mobicents.protocols.ss7.oam.common.sccp;

import java.util.Map;

import org.mobicents.protocols.ss7.sccp.ConcernedSignalingPointCode;
import org.mobicents.protocols.ss7.sccp.RemoteSignalingPointCode;
import org.mobicents.protocols.ss7.sccp.RemoteSubSystem;
import org.mobicents.protocols.ss7.sccp.SccpResource;

/**
 * @author Amit Bhayani
 *
 */
public class SccpResourceJmx implements SccpResourceJmxMBean {

    private final SccpResource wrappedSccpResource;


    public SccpResourceJmx(SccpResource wrappedSccpResource) {
        this.wrappedSccpResource = wrappedSccpResource;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.sccp.SccpResource#addConcernedSpc(int, int)
     */
    @Override
    public void addConcernedSpc(int arg0, int arg1) throws Exception {
        this.wrappedSccpResource.addConcernedSpc(arg0, arg1);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.sccp.SccpResource#addRemoteSpc(int, int, int, int)
     */
    @Override
    public void addRemoteSpc(int arg0, int arg1, int arg2, int arg3) throws Exception {
        this.wrappedSccpResource.addRemoteSpc(arg0, arg1, arg2, arg3);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.sccp.SccpResource#addRemoteSsn(int, int, int, int, boolean)
     */
    @Override
    public void addRemoteSsn(int arg0, int arg1, int arg2, int arg3, boolean arg4) throws Exception {
        this.wrappedSccpResource.addRemoteSsn(arg0, arg1, arg2, arg3, arg4);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.sccp.SccpResource#getConcernedSpc(int)
     */
    @Override
    public ConcernedSignalingPointCode getConcernedSpc(int arg0) {
        return this.wrappedSccpResource.getConcernedSpc(arg0);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.sccp.SccpResource#getConcernedSpcByPC(int)
     */
    @Override
    public ConcernedSignalingPointCode getConcernedSpcByPC(int arg0) {
        return this.wrappedSccpResource.getConcernedSpcByPC(arg0);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.sccp.SccpResource#getConcernedSpcs()
     */
    @Override
    public Map<Integer, ConcernedSignalingPointCode> getConcernedSpcs() {
        return this.wrappedSccpResource.getConcernedSpcs();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.sccp.SccpResource#getRemoteSpc(int)
     */
    @Override
    public RemoteSignalingPointCode getRemoteSpc(int arg0) {
        return this.wrappedSccpResource.getRemoteSpc(arg0);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.sccp.SccpResource#getRemoteSpcByPC(int)
     */
    @Override
    public RemoteSignalingPointCode getRemoteSpcByPC(int arg0) {
        return this.wrappedSccpResource.getRemoteSpcByPC(arg0);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.sccp.SccpResource#getRemoteSpcs()
     */
    @Override
    public Map<Integer, RemoteSignalingPointCode> getRemoteSpcs() {
        return this.wrappedSccpResource.getRemoteSpcs();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.sccp.SccpResource#getRemoteSsn(int)
     */
    @Override
    public RemoteSubSystem getRemoteSsn(int arg0) {
        return this.wrappedSccpResource.getRemoteSsn(arg0);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.sccp.SccpResource#getRemoteSsn(int, int)
     */
    @Override
    public RemoteSubSystem getRemoteSsn(int arg0, int arg1) {
        return this.wrappedSccpResource.getRemoteSsn(arg0, arg1);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.sccp.SccpResource#getRemoteSsns()
     */
    @Override
    public Map<Integer, RemoteSubSystem> getRemoteSsns() {
        return this.wrappedSccpResource.getRemoteSsns();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.sccp.SccpResource#modifyConcernedSpc(int, int)
     */
    @Override
    public void modifyConcernedSpc(int arg0, int arg1) throws Exception {
        this.wrappedSccpResource.modifyConcernedSpc(arg0, arg1);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.sccp.SccpResource#modifyRemoteSpc(int, int, int, int)
     */
    @Override
    public void modifyRemoteSpc(int arg0, int arg1, int arg2, int arg3) throws Exception {
        this.wrappedSccpResource.modifyRemoteSpc(arg0, arg1, arg2, arg3);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.sccp.SccpResource#modifyRemoteSsn(int, int, int, int, boolean)
     */
    @Override
    public void modifyRemoteSsn(int arg0, int arg1, int arg2, int arg3, boolean arg4) throws Exception {
        this.wrappedSccpResource.modifyRemoteSsn(arg0, arg1, arg2, arg3, arg4);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.sccp.SccpResource#removeConcernedSpc(int)
     */
    @Override
    public void removeConcernedSpc(int arg0) throws Exception {
        this.wrappedSccpResource.removeConcernedSpc(arg0);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.sccp.SccpResource#removeRemoteSpc(int)
     */
    @Override
    public void removeRemoteSpc(int arg0) throws Exception {
        this.wrappedSccpResource.removeRemoteSpc(arg0);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.sccp.SccpResource#removeRemoteSsn(int)
     */
    @Override
    public void removeRemoteSsn(int arg0) throws Exception {
        this.wrappedSccpResource.removeRemoteSsn(arg0);
    }

}
