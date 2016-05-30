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

package org.mobicents.protocols.ss7.tools.simulator.tests.ati;

import org.apache.log4j.Level;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextName;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextVersion;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPDialogListener;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPProvider;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.primitives.SubscriberIdentity;
import org.mobicents.protocols.ss7.map.api.service.mobility.MAPDialogMobility;
import org.mobicents.protocols.ss7.map.api.service.mobility.MAPServiceMobilityListener;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.AuthenticationFailureReportRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.AuthenticationFailureReportResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.SendAuthenticationInfoRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.SendAuthenticationInfoResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.faultRecovery.ForwardCheckSSIndicationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.faultRecovery.ResetRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.faultRecovery.RestoreDataRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.faultRecovery.RestoreDataResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.imei.CheckImeiRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.imei.CheckImeiResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.CancelLocationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.CancelLocationResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.PurgeMSRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.PurgeMSResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.SendIdentificationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.SendIdentificationResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.UpdateGprsLocationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.UpdateGprsLocationResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.UpdateLocationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.UpdateLocationResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.oam.ActivateTraceModeRequest_Mobility;
import org.mobicents.protocols.ss7.map.api.service.mobility.oam.ActivateTraceModeResponse_Mobility;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeInterrogationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeInterrogationResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeSubscriptionInterrogationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeSubscriptionInterrogationResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.DomainType;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ProvideSubscriberInfoRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ProvideSubscriberInfoResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.DeleteSubscriberDataRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.DeleteSubscriberDataResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.InsertSubscriberDataRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.InsertSubscriberDataResponse;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;
import org.mobicents.protocols.ss7.tools.simulator.Stoppable;
import org.mobicents.protocols.ss7.tools.simulator.common.AddressNatureType;
import org.mobicents.protocols.ss7.tools.simulator.common.TesterBase;
import org.mobicents.protocols.ss7.tools.simulator.level3.MapMan;
import org.mobicents.protocols.ss7.tools.simulator.level3.NumberingPlanMapType;
import org.mobicents.protocols.ss7.tools.simulator.management.TesterHost;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class TestAtiClientMan extends TesterBase implements TestAtiClientManMBean, Stoppable, MAPDialogListener, MAPServiceMobilityListener {

    public static String SOURCE_NAME = "TestAtiClient";

    private final String name;
    private MapMan mapMan;

    private boolean isStarted = false;
    private int countAtiReq = 0;
    private int countAtiResp = 0;
    private String currentRequestDef = "";
    private boolean needSendSend = false;
    private boolean needSendClose = false;

    public TestAtiClientMan() {
        super(SOURCE_NAME);
        this.name = "???";
    }

    public TestAtiClientMan(String name) {
        super(SOURCE_NAME);
        this.name = name;
    }

    public void setTesterHost(TesterHost testerHost) {
        this.testerHost = testerHost;
    }

    public void setMapMan(MapMan val) {
        this.mapMan = val;
    }

    @Override
    public AddressNatureType getAddressNature() {
        return new AddressNatureType(this.testerHost.getConfigurationData().getTestAtiClientConfigurationData().getAddressNature().getIndicator());
    }

    @Override
    public String getAddressNature_Value() {
        return new AddressNatureType(this.testerHost.getConfigurationData().getTestAtiClientConfigurationData().getAddressNature().getIndicator()).toString();
    }

    @Override
    public void setAddressNature(AddressNatureType val) {
        this.testerHost.getConfigurationData().getTestAtiClientConfigurationData().setAddressNature(AddressNature.getInstance(val.intValue()));
        this.testerHost.markStore();
    }

    @Override
    public NumberingPlanMapType getNumberingPlan() {
        return new NumberingPlanMapType(this.testerHost.getConfigurationData().getTestAtiClientConfigurationData().getNumberingPlan().getIndicator());
    }

    @Override
    public String getNumberingPlan_Value() {
        return new NumberingPlanMapType(this.testerHost.getConfigurationData().getTestAtiClientConfigurationData().getNumberingPlan().getIndicator())
                .toString();
    }

    @Override
    public void setNumberingPlan(NumberingPlanMapType val) {
        this.testerHost.getConfigurationData().getTestAtiClientConfigurationData().setNumberingPlan(NumberingPlan.getInstance(val.intValue()));
        this.testerHost.markStore();
    }

    @Override
    public SubscriberIdentityType getSubscriberIdentityType() {
        return new SubscriberIdentityType(
                this.testerHost.getConfigurationData().getTestAtiClientConfigurationData().isSubscriberIdentityTypeIsImsi() ? SubscriberIdentityType.VALUE_IMSI
                        : SubscriberIdentityType.VALUE_ISDN);
    }

    @Override
    public String getSubscriberIdentityType_Value() {
        return new SubscriberIdentityType(
                this.testerHost.getConfigurationData().getTestAtiClientConfigurationData().isSubscriberIdentityTypeIsImsi() ? SubscriberIdentityType.VALUE_IMSI
                        : SubscriberIdentityType.VALUE_ISDN).toString();
    }

    @Override
    public void setSubscriberIdentityType(SubscriberIdentityType val) {
        this.testerHost.getConfigurationData().getTestAtiClientConfigurationData()
                .setSubscriberIdentityTypeIsImsi(val.intValue() == SubscriberIdentityType.VALUE_IMSI);
        this.testerHost.markStore();
    }

    @Override
    public boolean isGetLocationInformation() {
        return this.testerHost.getConfigurationData().getTestAtiClientConfigurationData().isGetLocationInformation();
    }

    @Override
    public void setGetLocationInformation(boolean val) {
        this.testerHost.getConfigurationData().getTestAtiClientConfigurationData().setGetLocationInformation(val);
        this.testerHost.markStore();
    }

    @Override
    public boolean isGetSubscriberState() {
        return this.testerHost.getConfigurationData().getTestAtiClientConfigurationData().isGetSubscriberState();
    }

    @Override
    public void setGetSubscriberState(boolean val) {
        this.testerHost.getConfigurationData().getTestAtiClientConfigurationData().setGetSubscriberState(val);
        this.testerHost.markStore();
    }

    @Override
    public boolean isGetCurrentLocation() {
        return this.testerHost.getConfigurationData().getTestAtiClientConfigurationData().isGetCurrentLocation();
    }

    @Override
    public void setGetCurrentLocation(boolean val) {
        this.testerHost.getConfigurationData().getTestAtiClientConfigurationData().setGetCurrentLocation(val);
        this.testerHost.markStore();
    }

    @Override
    public AtiDomainType getGetRequestedDomain() {
        DomainType dt = this.testerHost.getConfigurationData().getTestAtiClientConfigurationData().getGetRequestedDomain();
        if (dt != null)
            return new AtiDomainType(dt.getType());
        else
            return new AtiDomainType(AtiDomainType.NO_VALUE);
    }

    @Override
    public String getGetRequestedDomain_Value() {
        return new AtiDomainType(this.testerHost.getConfigurationData().getTestAtiClientConfigurationData().getGetRequestedDomain().getType()).toString();
    }

    @Override
    public void setGetRequestedDomain(AtiDomainType val) {
        if (val.intValue() == AtiDomainType.NO_VALUE)
            this.testerHost.getConfigurationData().getTestAtiClientConfigurationData().setGetRequestedDomain(null);
        else
            this.testerHost.getConfigurationData().getTestAtiClientConfigurationData().setGetRequestedDomain(DomainType.getInstance(val.intValue()));
        this.testerHost.markStore();
    }

    @Override
    public boolean isGetImei() {
        return this.testerHost.getConfigurationData().getTestAtiClientConfigurationData().isGetImei();
    }

    @Override
    public void setGetImei(boolean val) {
        this.testerHost.getConfigurationData().getTestAtiClientConfigurationData().setGetImei(val);
        this.testerHost.markStore();
    }

    @Override
    public boolean isGetMsClassmark() {
        return this.testerHost.getConfigurationData().getTestAtiClientConfigurationData().isGetMsClassmark();
    }

    @Override
    public void setGetMsClassmark(boolean val) {
        this.testerHost.getConfigurationData().getTestAtiClientConfigurationData().setGetMsClassmark(val);
        this.testerHost.markStore();
    }

    @Override
    public boolean isGetMnpRequestedInfo() {
        return this.testerHost.getConfigurationData().getTestAtiClientConfigurationData().isGetMnpRequestedInfo();
    }

    @Override
    public void setGetMnpRequestedInfo(boolean val) {
        this.testerHost.getConfigurationData().getTestAtiClientConfigurationData().setGetMnpRequestedInfo(val);
        this.testerHost.markStore();
    }

    @Override
    public String getGsmSCFAddress() {
        return this.testerHost.getConfigurationData().getTestAtiClientConfigurationData().getGsmScfAddress();
    }

    @Override
    public void setGsmSCFAddress(String val) {
        this.testerHost.getConfigurationData().getTestAtiClientConfigurationData().setGsmScfAddress(val);
        this.testerHost.markStore();
    }

    @Override
    public void putAddressNature(String val) {
        AddressNatureType x = AddressNatureType.createInstance(val);
        if (x != null)
            this.setAddressNature(x);
    }

    @Override
    public void putNumberingPlan(String val) {
        NumberingPlanMapType x = NumberingPlanMapType.createInstance(val);
        if (x != null)
            this.setNumberingPlan(x);
    }

    @Override
    public void putSubscriberIdentityType(String val) {
        SubscriberIdentityType x = SubscriberIdentityType.createInstance(val);
        if (x != null)
            this.setSubscriberIdentityType(x);
    }

    @Override
    public void putGetRequestedDomain(String val) {
        AtiDomainType x = AtiDomainType.createInstance(val);
        if (x != null)
            this.setGetRequestedDomain(x);
    }

    @Override
    public String getCurrentRequestDef() {
        return "LastDialog: " + currentRequestDef;
    }

    @Override
    public String getState() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append(SOURCE_NAME);
        sb.append(": ");
        sb.append("<br>Count: countAtiReq-");
        sb.append(countAtiReq);
        sb.append(", countAtiResp-");
        sb.append(countAtiResp);
        sb.append("</html>");
        return sb.toString();
    }

    public boolean start() {
        this.countAtiReq = 0;
        this.countAtiResp = 0;

        MAPProvider mapProvider = this.mapMan.getMAPStack().getMAPProvider();
        mapProvider.getMAPServiceMobility().acivate();
        mapProvider.getMAPServiceMobility().addMAPServiceListener(this);
        mapProvider.addMAPDialogListener(this);
        this.testerHost.sendNotif(SOURCE_NAME, "ATI Client has been started", "", Level.INFO);
        isStarted = true;

        return true;
    }

    @Override
    public void stop() {
        MAPProvider mapProvider = this.mapMan.getMAPStack().getMAPProvider();
        isStarted = false;
        mapProvider.getMAPServiceMobility().deactivate();
        mapProvider.getMAPServiceMobility().removeMAPServiceListener(this);
        mapProvider.removeMAPDialogListener(this);
        this.testerHost.sendNotif(SOURCE_NAME, "ATI Client has been stopped", "", Level.INFO);
    }

    @Override
    public void execute() {
    }

    @Override
    public String closeCurrentDialog() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String performAtiRequest(String address) {
        if (!isStarted)
            return "The tester is not started";
        if (address == null || address.equals(""))
            return "Address is empty";

        currentRequestDef = "";

        return doAtiRequest(address);
    }

    private String doAtiRequest(String address) {

        MAPProvider mapProvider = this.mapMan.getMAPStack().getMAPProvider();

        MAPApplicationContextName acn = MAPApplicationContextName.anyTimeEnquiryContext;
        MAPApplicationContextVersion vers = MAPApplicationContextVersion.version3;
        MAPApplicationContext mapAppContext = MAPApplicationContext.getInstance(acn, vers);

        SubscriberIdentity subscriberIdentity;
        if (this.testerHost.getConfigurationData().getTestAtiClientConfigurationData().isSubscriberIdentityTypeIsImsi()) {
            IMSI imsi = mapProvider.getMAPParameterFactory().createIMSI(address);
            subscriberIdentity = mapProvider.getMAPParameterFactory().createSubscriberIdentity(imsi);
        } else {
            ISDNAddressString msisdn = mapProvider.getMAPParameterFactory().createISDNAddressString(
                    this.testerHost.getConfigurationData().getTestAtiClientConfigurationData().getAddressNature(),
                    this.testerHost.getConfigurationData().getTestAtiClientConfigurationData().getNumberingPlan(), address);
            subscriberIdentity = mapProvider.getMAPParameterFactory().createSubscriberIdentity(msisdn);
        }
        RequestedInfo requestedInfo = mapProvider.getMAPParameterFactory().createRequestedInfo(
                this.testerHost.getConfigurationData().getTestAtiClientConfigurationData().isGetLocationInformation(),
                this.testerHost.getConfigurationData().getTestAtiClientConfigurationData().isGetSubscriberState(), null,
                this.testerHost.getConfigurationData().getTestAtiClientConfigurationData().isGetCurrentLocation(),
                this.testerHost.getConfigurationData().getTestAtiClientConfigurationData().getGetRequestedDomain(),
                this.testerHost.getConfigurationData().getTestAtiClientConfigurationData().isGetImei(),
                this.testerHost.getConfigurationData().getTestAtiClientConfigurationData().isGetMsClassmark(),
                this.testerHost.getConfigurationData().getTestAtiClientConfigurationData().isGetMnpRequestedInfo());
        ISDNAddressString gsmSCFAddress = mapProvider.getMAPParameterFactory().createISDNAddressString(
                this.testerHost.getConfigurationData().getTestAtiClientConfigurationData().getAddressNature(),
                this.testerHost.getConfigurationData().getTestAtiClientConfigurationData().getNumberingPlan(),
                this.testerHost.getConfigurationData().getTestAtiClientConfigurationData().getGsmScfAddress());

        try {
            MAPDialogMobility curDialog = mapProvider.getMAPServiceMobility().createNewDialog(mapAppContext, this.mapMan.createOrigAddress(), null,
                    this.mapMan.createDestAddress(address, this.testerHost.getConfigurationData().getTestSmsServerConfigurationData().getHlrSsn()), null);

            curDialog.addAnyTimeInterrogationRequest(subscriberIdentity, requestedInfo, gsmSCFAddress, null);
            curDialog.send();

            String mtData = createAtiReqData(curDialog.getLocalDialogId(), address);
            currentRequestDef += "Sent AtiRequest;";
            this.countAtiReq++;
            this.testerHost.sendNotif(SOURCE_NAME, "Sent: AtiRequest: " + address, mtData, Level.DEBUG);

            return "AtiRequest has been sent";
        } catch (MAPException ex) {
            return "Exception when sending AtiRequest: " + ex.toString();
        }
    }

    private String createAtiReqData(long dialogId, String address) {
        StringBuilder sb = new StringBuilder();
        sb.append("dialogId=");
        sb.append(dialogId);
        sb.append(", address=\"");
        sb.append(address);
        sb.append("\"");
        return sb.toString();
    }

    @Override
    public void onAnyTimeInterrogationResponse(AnyTimeInterrogationResponse ind) {
        if (!isStarted)
            return;

        this.countAtiResp++;

        MAPDialogMobility curDialog = ind.getMAPDialog();
        long invokeId = curDialog.getLocalDialogId();

        currentRequestDef += "Rsvd AtiResp;";
        String uData = this.createAtiRespData(invokeId, ind);
        this.testerHost.sendNotif(SOURCE_NAME, "Rcvd: atiResp", uData, Level.DEBUG);
    }

    public void onAnyTimeSubscriptionInterrogationRequest(AnyTimeSubscriptionInterrogationRequest request) {

    }

    public void onAnyTimeSubscriptionInterrogationResponse(AnyTimeSubscriptionInterrogationResponse response) {

    }

    private String createAtiRespData(long dialogId, AnyTimeInterrogationResponse ind) {
        StringBuilder sb = new StringBuilder();
        sb.append("dialogId=");
        sb.append(dialogId);
        sb.append(", ind=\"");
        sb.append(ind);
        sb.append("\"");
        return sb.toString();
    }

    @Override
    public void onRejectComponent(MAPDialog mapDialog, Long invokeId, Problem problem, boolean isLocalOriginated) {
        super.onRejectComponent(mapDialog, invokeId, problem, isLocalOriginated);
        if (isLocalOriginated)
            needSendClose = true;
    }

    @Override
    public void onDialogDelimiter(MAPDialog mapDialog) {
        try {
            if (needSendSend) {
                needSendSend = false;
                mapDialog.send();
                return;
            }
        } catch (Exception e) {
            this.testerHost.sendNotif(SOURCE_NAME, "Exception when invoking send() : " + e.getMessage(), e, Level.ERROR);
            return;
        }
        try {
            if (needSendClose) {
                needSendClose = false;
                mapDialog.close(false);
                return;
            }
        } catch (Exception e) {
            this.testerHost.sendNotif(SOURCE_NAME, "Exception when invoking close() : " + e.getMessage(), e, Level.ERROR);
            return;
        }
    }

    @Override
    public void onActivateTraceModeRequest_Mobility(ActivateTraceModeRequest_Mobility arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onActivateTraceModeResponse_Mobility(ActivateTraceModeResponse_Mobility arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAnyTimeInterrogationRequest(AnyTimeInterrogationRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCancelLocationRequest(CancelLocationRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCancelLocationResponse(CancelLocationResponse arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCheckImeiRequest(CheckImeiRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCheckImeiResponse(CheckImeiResponse arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDeleteSubscriberDataRequest(DeleteSubscriberDataRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDeleteSubscriberDataResponse(DeleteSubscriberDataResponse arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onForwardCheckSSIndicationRequest(ForwardCheckSSIndicationRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onInsertSubscriberDataRequest(InsertSubscriberDataRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onInsertSubscriberDataResponse(InsertSubscriberDataResponse arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProvideSubscriberInfoRequest(ProvideSubscriberInfoRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProvideSubscriberInfoResponse(ProvideSubscriberInfoResponse arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPurgeMSRequest(PurgeMSRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPurgeMSResponse(PurgeMSResponse arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onResetRequest(ResetRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onRestoreDataRequest(RestoreDataRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onRestoreDataResponse(RestoreDataResponse arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSendAuthenticationInfoRequest(SendAuthenticationInfoRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSendAuthenticationInfoResponse(SendAuthenticationInfoResponse arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSendIdentificationRequest(SendIdentificationRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSendIdentificationResponse(SendIdentificationResponse arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onUpdateGprsLocationRequest(UpdateGprsLocationRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onUpdateGprsLocationResponse(UpdateGprsLocationResponse arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onUpdateLocationRequest(UpdateLocationRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onUpdateLocationResponse(UpdateLocationResponse arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAuthenticationFailureReportRequest(AuthenticationFailureReportRequest arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAuthenticationFailureReportResponse(AuthenticationFailureReportResponse arg0) {
        // TODO Auto-generated method stub

    }

}
