/**
 * 
 */
package org.mobicents.protocols.ss7.tcap;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.ss7.sccp.SccpListener;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.api.ComponentPrimitiveFactory;
import org.mobicents.protocols.ss7.tcap.api.DialogPrimitiveFactory;
import org.mobicents.protocols.ss7.tcap.api.TCAPException;
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

	private static final Logger logger = Logger.getLogger(TCAProviderImpl.class);
	// boundry for Uni directional dialogs :), tx id is always encoded
	// on 4 octets, so this is its max value
	private static final long _4_OCTETS_LONG_FILL = 4294967295l;

	private ComponentPrimitiveFactory componentPrimitiveFactory;
	private DialogPrimitiveFactory dialogPrimitiveFactory;
	private SccpProvider transportProvider;
	private TCAPStackImpl stack;

	// originating TX id ~=Dialog, its direct mapping, but not described
	// explicitly...
	private Map<Long, DialogImpl> dialogs = new HashMap<Long, DialogImpl>();

	TCAProviderImpl(SccpProvider transportProvider, TCAPStackImpl stack) {
		super();
		this.transportProvider = transportProvider;
		this.stack = stack;

		this.componentPrimitiveFactory = new ComponentPrimitiveFactoryImpl();
		this.dialogPrimitiveFactory = new DialogPrimitiveFactoryImpl();
	}

	// some help methods... crude but will work for first impl.
	private Long getAvailableTxId() throws TCAPException {
		for (long l = 0; l <= _4_OCTETS_LONG_FILL; l++) {
			Long ll = new Long(l);
			if (this.dialogs.containsKey(ll)) {

			} else {
				return ll;
			}
		}
		throw new TCAPException("Not enough resources!");
	}

	private Long getAvailableUniTxId() throws TCAPException {
		for (long l = -1; l >= Long.MIN_VALUE; l--) {
			Long ll = new Long(l);
			if (this.dialogs.containsKey(ll)) {

			} else {
				return ll;
			}
		}
		throw new TCAPException("Not enough resources!");
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
	 * @see
	 * org.mobicents.protocols.ss7.tcap.api.TCAPProvider#getNewDialog(org.mobicents
	 * .protocols.ss7.sccp.parameter.SccpAddress,
	 * org.mobicents.protocols.ss7.sccp.parameter.SccpAddress)
	 */
	public Dialog getNewDialog(SccpAddress localAddress, SccpAddress remoteAddress) throws TCAPException {

		Long id = this.getAvailableUniTxId();
		return _getDialog(localAddress, remoteAddress, id);
	}

	private Dialog _getDialog(SccpAddress localAddress, SccpAddress remoteAddress, Long id) {
		if (localAddress == null) {
			throw new NullPointerException("LocalAddress must not be null");
		}

		if (remoteAddress == null) {
			throw new NullPointerException("RemoteAddress must not be null");
		}
		DialogImpl di = new DialogImpl(localAddress, remoteAddress, id);
		this.dialogs.put(di.getDialogId(), di);
		return di;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.api.TCAPProvider#getNewUnstructuredDialog
	 * (org.mobicents.protocols.ss7.sccp.parameter.SccpAddress,
	 * org.mobicents.protocols.ss7.sccp.parameter.SccpAddress)
	 */
	public Dialog getNewUnstructuredDialog(SccpAddress localAddress, SccpAddress remoteAddress) throws TCAPException {
		Long id = this.getAvailableTxId();
		return _getDialog(localAddress, remoteAddress, id);
	}

	public void onMessage(SccpAddress localAddress, SccpAddress remoteAddress, byte[] asnData) {
		try {
			// FIXME: Qs state that OtxID and DtxID consittute to dialog id.....

			// asnData - it should pass
			AsnInputStream ais = new AsnInputStream(new ByteArrayInputStream(asnData));

			// this should have TC message tag :)
			int tag = ais.readTag();

			switch (tag) {
			// continue first, usually we will get more of those. small perf
			case TCContinueMessage._TAG:
				TCContinueMessage tcm = TcapFactory.createTCContinueMessage(ais);
				// received continue, destID == localDialogId(originatingTxId of
				// begin);
				Long dialogId = tcm.getDestinationTransactionId();
				DialogImpl di = this.dialogs.get(dialogId);
				if (di == null) {
					logger.error("No dialog/transaction for id: " + dialogId);
				} else {
					di.processContinue(tcm, localAddress, remoteAddress);
				}
				break;

			case TCBeginMessage._TAG:
				TCBeginMessage tcb = TcapFactory.createTCBeginMessage(ais);

				// received continue, destID == localDialogId(originatingTxId of
				// begin);

				di = (DialogImpl) this.getNewDialog(localAddress, remoteAddress);
				di.processBegin(tcb, localAddress, remoteAddress);

				break;

			case TCEndMessage._TAG:
				TCEndMessage teb = TcapFactory.createTCEndMessage(ais);
				dialogId = teb.getDestinationTransactionId();
				di = this.dialogs.get(dialogId);
				if (di == null) {
					logger.error("No dialog/transaction for id: " + dialogId);
				} else {
					di.processEnd(teb, localAddress, remoteAddress);
				}
				break;

			case TCUniMessage._TAG:
				TCUniMessage tub = TcapFactory.createTCUniMessage(ais);
				di = (DialogImpl) this.getNewUnstructuredDialog(localAddress, remoteAddress);
				di.processUni(tub, localAddress, remoteAddress);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
