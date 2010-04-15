/**
 * 
 */
package org.mobicents.protocols.ss7.tcap;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.ss7.sccp.SccpListener;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.api.ComponentPrimitiveFactory;
import org.mobicents.protocols.ss7.tcap.api.DialogPrimitiveFactory;
import org.mobicents.protocols.ss7.tcap.api.TCAPProvider;
import org.mobicents.protocols.ss7.tcap.api.tc.component.TCInvokeIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.component.TCInvokeRequest;
import org.mobicents.protocols.ss7.tcap.api.tc.component.TCRejectIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.component.TCRejectRequest;
import org.mobicents.protocols.ss7.tcap.api.tc.component.TCResultIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.component.TCResultRequest;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.asn.TcapFactory;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.Reject;
import org.mobicents.protocols.ss7.tcap.asn.comp.Return;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCBeginMessage;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCContinueMessage;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCEndMessage;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCUniMessage;
import org.mobicents.protocols.ss7.tcap.tc.component.ComponentPrimitiveFactoryImpl;
import org.mobicents.protocols.ss7.tcap.tc.dialog.events.DialogPrimitiveFactoryImpl;
import org.mobicents.protocols.ss7.tcap.tc.dialog.events.TCBeginIndicationImpl;

/**
 * @author baranowb
 * 
 */
public class TCAProviderImpl implements TCAPProvider, SccpListener {

	private ComponentPrimitiveFactory componentPrimitiveFactory;
	private DialogPrimitiveFactory dialogPrimitiveFactory;
	private SccpProvider transportProvider;
	private TCAPStackImpl stack;

	TCAProviderImpl(SccpProvider transportProvider, TCAPStackImpl stack) {
		super();
		this.transportProvider = transportProvider;
		this.stack = stack;

		this.componentPrimitiveFactory = new ComponentPrimitiveFactoryImpl();
		this.dialogPrimitiveFactory = new DialogPrimitiveFactoryImpl();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.protocols.ss7.tcap.api.TCAPProvider#
	 * getComopnentPrimitiveFactory()
	 */
	public ComponentPrimitiveFactory getComopnentPrimitiveFactory() {

		return this.componentPrimitiveFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.api.TCAPProvider#getDialogPrimitiveFactory
	 * ()
	 */
	public DialogPrimitiveFactory getDialogPrimitiveFactory() {

		return this.dialogPrimitiveFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.tcap.api.TCAPProvider#getNewDialog()
	 */
	public Dialog getNewDialog() {
		// TODO Auto-generated method stub
		return null;
	}

	public void onMessage(SccpAddress arg0, SccpAddress arg1, byte[] asnData) {
		try {
			// asnData - it should pass
			AsnInputStream ais = new AsnInputStream(new ByteArrayInputStream(asnData));

			// this should have TC message tag :)
			int tag = ais.readTag();

			switch (tag) {
			// continue first, usually we will get more of those. small perf
			case TCContinueMessage._TAG:
				TCContinueMessage tcm = TcapFactory.createTCContinueMessage(ais);
				break;
				
			case TCBeginMessage._TAG:
				TCBeginMessage tcb = TcapFactory.createTCBeginMessage(ais);			
				
				break;

			case TCEndMessage._TAG:
				TCEndMessage teb = TcapFactory.createTCEndMessage(ais);
				break;

			case TCUniMessage._TAG:
				TCUniMessage tub = TcapFactory.createTCUniMessage(ais);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
