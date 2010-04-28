import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPDialogListener;
import org.mobicents.protocols.ss7.map.api.MAPProvider;
import org.mobicents.protocols.ss7.map.api.MAPServiceListener;
import org.mobicents.protocols.ss7.map.api.dialog.MAPAcceptInfo;
import org.mobicents.protocols.ss7.map.api.dialog.MAPCloseInfo;
import org.mobicents.protocols.ss7.map.api.dialog.MAPOpenInfo;
import org.mobicents.protocols.ss7.map.api.dialog.MAPProviderAbortInfo;
import org.mobicents.protocols.ss7.map.api.dialog.MAPRefuseInfo;
import org.mobicents.protocols.ss7.map.api.dialog.MAPUserAbortInfo;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSConfirm;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSIndication;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSConfirm;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSIndication;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

public class MAPExample implements MAPDialogListener, MAPServiceListener {

	MAPProvider mapProvider;

	SccpAddress destAddress = null;
	byte[] destReference = new byte[] { (byte) 0x96, 0x02, 0x24, (byte) 0x80,
			0x03, 0x00, (byte) 0x80, 0x00, (byte) 0xf2 };
	SccpAddress origAddress = null;
	byte[] origReference = new byte[] { (byte) 0x91, 0x13, 0x26, (byte) 0x98,
			(byte) 0x86, 0x03, (byte) 0xf0 };

	public void run() throws Exception {
		
		MAPDialog mapDialog = mapProvider.createNewDialog(
				MAPApplicationContext.networkUnstructuredSsContextV2,
				destAddress, destReference, origAddress, origReference);
		
		byte ussdDataCodingScheme = 0x0f;
		
		// USSD String: *125*+31628839999#
		byte[]  ussdString =  new byte[]{(byte)0xaa, (byte)0x98, (byte)0xac, (byte)0xa6, 0x5a, (byte)0xcd, 0x62, 0x36, 0x19, 0x0e, 0x37, (byte)0xcb, (byte)0xe5, 0x72, (byte)0xb9, 0x11};
		
		mapDialog.addProcessUnstructuredSSRequest(ussdDataCodingScheme, ussdString);
		
		//This will initiate the TC-BEGIN with INVOKE component
		mapDialog.send();
	}

	public void onMAPAcceptInfo(MAPAcceptInfo mapAccptInfo) {
		// TODO Auto-generated method stub

	}

	public void onMAPCloseInfo(MAPCloseInfo mapCloseInfo) {
		// TODO Auto-generated method stub

	}

	public void onMAPOpenInfo(MAPOpenInfo mapOpenInfo) {
		// TODO Auto-generated method stub

	}

	public void onMAPProviderAbortInfo(MAPProviderAbortInfo mapProviderAbortInfo) {
		// TODO Auto-generated method stub

	}

	public void onMAPRefuseInfo(MAPRefuseInfo mapRefuseInfo) {
		// TODO Auto-generated method stub

	}

	public void onMAPUserAbortInfo(MAPUserAbortInfo mapUserAbortInfo) {
		// TODO Auto-generated method stub

	}

	public void onProcessUnstructuredSSConfirm(
			ProcessUnstructuredSSConfirm procUnstrCnfrm) {
		// TODO Auto-generated method stub

	}

	public void onProcessUnstructuredSSIndication(
			ProcessUnstructuredSSIndication procUnstrInd) {
		// TODO Auto-generated method stub

	}

	public void onUnstructuredSSConfirm(UnstructuredSSConfirm unstrCnfrm) {
		// TODO Auto-generated method stub

	}

	public void onUnstructuredSSIndication(UnstructuredSSIndication unstrInd) {
		// TODO Auto-generated method stub

	}

}
