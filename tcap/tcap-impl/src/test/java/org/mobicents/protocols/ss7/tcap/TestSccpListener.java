package org.mobicents.protocols.ss7.tcap;

import org.mobicents.protocols.ss7.sccp.NetworkIdState;
import org.mobicents.protocols.ss7.sccp.RemoteSccpStatus;
import org.mobicents.protocols.ss7.sccp.SccpConnection;
import org.mobicents.protocols.ss7.sccp.SccpListener;
import org.mobicents.protocols.ss7.sccp.SignallingPointStatus;
import org.mobicents.protocols.ss7.sccp.message.SccpDataMessage;
import org.mobicents.protocols.ss7.sccp.message.SccpNoticeMessage;
import org.mobicents.protocols.ss7.sccp.parameter.Credit;
import org.mobicents.protocols.ss7.sccp.parameter.ErrorCause;
import org.mobicents.protocols.ss7.sccp.parameter.Importance;
import org.mobicents.protocols.ss7.sccp.parameter.ProtocolClass;
import org.mobicents.protocols.ss7.sccp.parameter.RefusalCause;
import org.mobicents.protocols.ss7.sccp.parameter.ReleaseCause;
import org.mobicents.protocols.ss7.sccp.parameter.ResetCause;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

public class TestSccpListener implements SccpListener {

	private static final long serialVersionUID = 1L;

	private boolean congestedStatusReceived = false;
	
	@Override
	public void onMessage(SccpDataMessage message) {
		
	}

	@Override
	public void onNotice(SccpNoticeMessage message) {
		
	}

	@Override
	public void onCoordResponse(int ssn, int multiplicityIndicator) {
		
	}

	@Override
	public void onState(int dpc, int ssn, boolean inService,
			int multiplicityIndicator) {
		
	}

	@Override
	public void onPcState(int dpc, SignallingPointStatus status, Integer restrictedImportanceLevel, RemoteSccpStatus remoteSccpStatus) {
		if(status.equals(status.CONGESTED))
		    congestedStatusReceived = true;
	}

	@Override
	public void onNetworkIdState(int networkId, NetworkIdState networkIdState) {
		
	}

	public boolean isCongestedStatusReceived() {
		return congestedStatusReceived;
	}

    @Override
    public void onConnectIndication(SccpConnection conn, SccpAddress calledAddress, SccpAddress callingAddress,
            ProtocolClass clazz, Credit credit, byte[] data, Importance importance) throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onConnectConfirm(SccpConnection conn, byte[] data) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onDisconnectIndication(SccpConnection conn, ReleaseCause reason, byte[] data) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onDisconnectIndication(SccpConnection conn, RefusalCause reason, byte[] data) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onDisconnectIndication(SccpConnection conn, ErrorCause errorCause) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onResetIndication(SccpConnection conn, ResetCause reason) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onResetConfirm(SccpConnection conn) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onData(SccpConnection conn, byte[] data) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onDisconnectConfirm(SccpConnection conn) {
        // TODO Auto-generated method stub
        
    }

}
