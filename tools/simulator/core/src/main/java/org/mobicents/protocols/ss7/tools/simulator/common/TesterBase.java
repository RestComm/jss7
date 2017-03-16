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

package org.mobicents.protocols.ss7.tools.simulator.common;

import org.apache.log4j.Level;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPDialogListener;
import org.mobicents.protocols.ss7.map.api.MAPMessage;
import org.mobicents.protocols.ss7.map.api.MAPServiceListener;
import org.mobicents.protocols.ss7.map.api.dialog.MAPAbortProviderReason;
import org.mobicents.protocols.ss7.map.api.dialog.MAPAbortSource;
import org.mobicents.protocols.ss7.map.api.dialog.MAPNoticeProblemDiagnostic;
import org.mobicents.protocols.ss7.map.api.dialog.MAPRefuseReason;
import org.mobicents.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessage;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;
import org.mobicents.protocols.ss7.tools.simulator.management.TesterHost;

public abstract class TesterBase implements MAPDialogListener, MAPServiceListener {

    protected TesterHost testerHost;
    protected final String className;

    public TesterBase(String name) {
        this.className = name;
    }

    public void setTesterHost(TesterHost testerHost) {
        this.testerHost = testerHost;
    }

    @Override
    public void onErrorComponent(MAPDialog dlg, Long invokeId, MAPErrorMessage msg) {
        String uData = msg.toString() + ", dlgId=" + dlg.getLocalDialogId() + ", invokeId=" + invokeId;
        this.testerHost.sendNotif(this.className, "Rcvd: Error component", uData, Level.DEBUG);
    }

    @Override
    public void onRejectComponent(MAPDialog mapDialog, Long invokeId, Problem problem, boolean isLocalOriginated) {
        String uData = problem.toString() + ", dlgId=" + mapDialog.getLocalDialogId() + ", InvokeId=" + invokeId
                + ", isLocalOriginated=" + isLocalOriginated;
        this.testerHost.sendNotif(this.className, "Rcvd: RejectComponent", uData, Level.DEBUG);
    }

    @Override
    public void onInvokeTimeout(MAPDialog arg0, Long arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onMAPMessage(MAPMessage arg0) {
    }

    @Override
    public void onDialogAccept(MAPDialog arg0, MAPExtensionContainer arg1) {
    }

    @Override
    public void onDialogClose(MAPDialog arg0) {
    }

    @Override
    public void onDialogDelimiter(MAPDialog arg0) {
    }

    @Override
    public void onDialogNotice(MAPDialog mapDialog, MAPNoticeProblemDiagnostic notice) {
        String uData = "dialogNotice=" + notice.toString() + ", dlgId=" + mapDialog.getLocalDialogId();
        this.testerHost.sendNotif(this.className, "Rcvd: DialogNotice", uData, Level.DEBUG);
    }

    @Override
    public void onDialogProviderAbort(MAPDialog mapDialog, MAPAbortProviderReason abortProviderReason,
            MAPAbortSource abortSource, MAPExtensionContainer extensionContainer) {
        String uData = "abortProviderReason=" + abortProviderReason + ", abortSource=" + abortSource + ", dlgId="
                + mapDialog.getLocalDialogId();
        this.testerHost.sendNotif(this.className, "Rcvd: DialogProviderAbort", uData, Level.DEBUG);
    }

    @Override
    public void onDialogReject(MAPDialog mapDialog, MAPRefuseReason refuseReason,
            ApplicationContextName alternativeApplicationContext, MAPExtensionContainer extensionContainer) {
        String uData = "refuseReason=" + refuseReason + ", alternativeApplicationContext=" + alternativeApplicationContext
                + ", dlgId=" + mapDialog.getLocalDialogId();
        this.testerHost.sendNotif(this.className, "Rcvd: DialogReject", uData, Level.DEBUG);
    }

    @Override
    public void onDialogRelease(MAPDialog arg0) {
    }

    @Override
    public void onDialogRequest(MAPDialog arg0, AddressString arg1, AddressString arg2, MAPExtensionContainer arg3) {
    }

    @Override
    public void onDialogRequestEricsson(MAPDialog arg0, AddressString arg1, AddressString arg2, AddressString arg3, AddressString arg4) {
    }

    @Override
    public void onDialogTimeout(MAPDialog arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDialogUserAbort(MAPDialog mapDialog, MAPUserAbortChoice userReason, MAPExtensionContainer extensionContainer) {
        String uData = "userReason=" + userReason + ", dlgId=" + mapDialog.getLocalDialogId();
        this.testerHost.sendNotif(this.className, "Rcvd: DialogUserAbort", uData, Level.DEBUG);
    }

}
