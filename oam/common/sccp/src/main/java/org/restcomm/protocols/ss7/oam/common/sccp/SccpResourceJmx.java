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
package org.restcomm.protocols.ss7.oam.common.sccp;

import java.util.Map;

import org.restcomm.protocols.ss7.sccp.ConcernedSignalingPointCode;
import org.restcomm.protocols.ss7.sccp.RemoteSignalingPointCode;
import org.restcomm.protocols.ss7.sccp.RemoteSubSystem;
import org.restcomm.protocols.ss7.sccp.SccpResource;

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
     * @see org.restcomm.protocols.ss7.sccp.SccpResource#addConcernedSpc(int, int)
     */
    @Override
    public void addConcernedSpc(int concernedSpcId, int remoteSpc) throws Exception {
        this.wrappedSccpResource.addConcernedSpc(concernedSpcId, remoteSpc);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.sccp.SccpResource#addRemoteSpc(int, int, int, int)
     */
    @Override
    public void addRemoteSpc(int remoteSpcId, int remoteSpc, int remoteSpcFlag, int mask) throws Exception {
        this.wrappedSccpResource.addRemoteSpc(remoteSpcId, remoteSpc, remoteSpcFlag, mask);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.sccp.SccpResource#addRemoteSsn(int, int, int, int, boolean)
     */
    @Override
    public void addRemoteSsn(int remoteSsnid, int remoteSpc, int remoteSsn, int remoteSsnFlag,
            boolean markProhibitedWhenSpcResuming) throws Exception {
        this.wrappedSccpResource.addRemoteSsn(remoteSsnid, remoteSpc, remoteSsn, remoteSsnFlag, markProhibitedWhenSpcResuming);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.sccp.SccpResource#getConcernedSpc(int)
     */
    @Override
    public ConcernedSignalingPointCode getConcernedSpc(int concernedSpcId) {
        return this.wrappedSccpResource.getConcernedSpc(concernedSpcId);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.sccp.SccpResource#getConcernedSpcByPC(int)
     */
    @Override
    public ConcernedSignalingPointCode getConcernedSpcByPC(int remotePC) {
        return this.wrappedSccpResource.getConcernedSpcByPC(remotePC);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.sccp.SccpResource#getConcernedSpcs()
     */
    @Override
    public Map<Integer, ConcernedSignalingPointCode> getConcernedSpcs() {
        return this.wrappedSccpResource.getConcernedSpcs();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.sccp.SccpResource#getRemoteSpc(int)
     */
    @Override
    public RemoteSignalingPointCode getRemoteSpc(int remoteSpcId) {
        return this.wrappedSccpResource.getRemoteSpc(remoteSpcId);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.sccp.SccpResource#getRemoteSpcByPC(int)
     */
    @Override
    public RemoteSignalingPointCode getRemoteSpcByPC(int remotePC) {
        return this.wrappedSccpResource.getRemoteSpcByPC(remotePC);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.sccp.SccpResource#getRemoteSpcs()
     */
    @Override
    public Map<Integer, RemoteSignalingPointCode> getRemoteSpcs() {
        return this.wrappedSccpResource.getRemoteSpcs();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.sccp.SccpResource#getRemoteSsn(int)
     */
    @Override
    public RemoteSubSystem getRemoteSsn(int remoteSsnid) {
        return this.wrappedSccpResource.getRemoteSsn(remoteSsnid);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.sccp.SccpResource#getRemoteSsn(int, int)
     */
    @Override
    public RemoteSubSystem getRemoteSsn(int spc, int remoteSsn) {
        return this.wrappedSccpResource.getRemoteSsn(spc, remoteSsn);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.sccp.SccpResource#getRemoteSsns()
     */
    @Override
    public Map<Integer, RemoteSubSystem> getRemoteSsns() {
        return this.wrappedSccpResource.getRemoteSsns();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.sccp.SccpResource#modifyConcernedSpc(int, int)
     */
    @Override
    public void modifyConcernedSpc(int concernedSpcId, int remoteSpc) throws Exception {
        this.wrappedSccpResource.modifyConcernedSpc(concernedSpcId, remoteSpc);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.sccp.SccpResource#modifyRemoteSpc(int, int, int, int)
     */
    @Override
    public void modifyRemoteSpc(int remoteSpcId, int remoteSpc, int remoteSpcFlag, int mask) throws Exception {
        this.wrappedSccpResource.modifyRemoteSpc(remoteSpcId, remoteSpc, remoteSpcFlag, mask);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.sccp.SccpResource#modifyRemoteSsn(int, int, int, int, boolean)
     */
    @Override
    public void modifyRemoteSsn(int remoteSsnid, int remoteSpc, int remoteSsn, int remoteSsnFlag,
            boolean markProhibitedWhenSpcResuming) throws Exception {
        this.wrappedSccpResource.modifyRemoteSsn(remoteSsnid, remoteSpc, remoteSsn, remoteSsnFlag,
                markProhibitedWhenSpcResuming);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.sccp.SccpResource#removeConcernedSpc(int)
     */
    @Override
    public void removeConcernedSpc(int concernedSpcId) throws Exception {
        this.wrappedSccpResource.removeConcernedSpc(concernedSpcId);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.sccp.SccpResource#removeRemoteSpc(int)
     */
    @Override
    public void removeRemoteSpc(int remoteSpcId) throws Exception {
        this.wrappedSccpResource.removeRemoteSpc(remoteSpcId);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.sccp.SccpResource#removeRemoteSsn(int)
     */
    @Override
    public void removeRemoteSsn(int remoteSsnid) throws Exception {
        this.wrappedSccpResource.removeRemoteSsn(remoteSsnid);
    }

}
