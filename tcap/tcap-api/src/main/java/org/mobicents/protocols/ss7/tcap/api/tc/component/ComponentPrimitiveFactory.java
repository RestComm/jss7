package org.mobicents.protocols.ss7.tcap.api.tc.component;

public interface ComponentPrimitiveFactory {

	public TCInvokeRequest createTCInvokeRequest();

	public TCRejectRequest createTCRejectRequest();

	public TCResultRequest createTCResultRequest();

}
