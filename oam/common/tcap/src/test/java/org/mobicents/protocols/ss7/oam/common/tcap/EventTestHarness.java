/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.mobicents.protocols.ss7.oam.common.tcap;

import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.api.TCAPException;
import org.mobicents.protocols.ss7.tcap.api.TCAPProvider;
import org.mobicents.protocols.ss7.tcap.api.TCAPStack;
import org.mobicents.protocols.ss7.tcap.api.TCListener;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCContinueIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCEndIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCNoticeIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCPAbortIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCUniIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCUserAbortIndication;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;

/**
 *
 * @author baranowb
 *
 */
public class EventTestHarness implements TCListener {

    public static final long[] _ACN_ = new long[] { 0, 4, 0, 0, 1, 0, 19, 2 };
    
    protected Dialog dialog;
    protected TCAPStack stack;
    protected SccpAddress thisAddress;
    protected SccpAddress remoteAddress;
    protected TCAPProvider tcapProvider;

    protected int sequence = 0;

    protected ApplicationContextName acn;
//    protected UserInformation ui;

    public EventTestHarness(TCAPStack stack, SccpAddress thisAddress, SccpAddress remoteAddress) {
        super();
        this.stack = stack;
        this.thisAddress = thisAddress;
        this.remoteAddress = remoteAddress;
        this.tcapProvider = this.stack.getProvider();
        this.tcapProvider.addTCListener(this);
    }

    public void startClientDialog() throws TCAPException {
        if (dialog != null) {
            throw new IllegalStateException("Dialog exists...");
        }
        dialog = this.tcapProvider.getNewDialog(thisAddress, remoteAddress);
    }


    @Override
    public void onTCUni(TCUniIndication ind) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onTCBegin(TCBeginIndication ind) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onTCContinue(TCContinueIndication ind) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onTCEnd(TCEndIndication ind) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onTCUserAbort(TCUserAbortIndication ind) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onTCPAbort(TCPAbortIndication ind) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onTCNotice(TCNoticeIndication ind) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onDialogReleased(Dialog d) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onInvokeTimeout(Invoke tcInvokeRequest) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onDialogTimeout(Dialog d) {
        // TODO Auto-generated method stub
        
    }

}
