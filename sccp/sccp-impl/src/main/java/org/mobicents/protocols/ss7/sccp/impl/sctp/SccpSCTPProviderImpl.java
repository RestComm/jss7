package org.mobicents.protocols.ss7.sccp.impl.sctp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.mtp.Mtp3;
import org.mobicents.protocols.ss7.mtp.ActionReference;
import org.mobicents.protocols.ss7.sccp.SccpListener;
import org.mobicents.protocols.ss7.sccp.impl.SccpProviderImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ProtocolClassImpl;
import org.mobicents.protocols.ss7.sccp.impl.ud.UnitDataImpl;
import org.mobicents.protocols.ss7.sccp.impl.ud.XUnitDataImpl;
import org.mobicents.protocols.ss7.sccp.parameter.ProtocolClass;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.sccp.ud.UDBase;
import org.mobicents.protocols.ss7.stream.MTPListener;
import org.mobicents.protocols.ss7.stream.MTPProvider;
import org.mobicents.protocols.ss7.stream.MTPProviderFactory;




public class SccpSCTPProviderImpl extends SccpProviderImpl implements MTPListener {
	private static final Logger logger = Logger.getLogger(SccpSCTPProviderImpl.class);

	private MTPProvider mtpProvider;
	private boolean linkUp  =false;

	private int opc;

	private int dpc;

	private int sls;

	private int ssi;

	private int si;

	/** Creates a new instance of SccpProviderImpl */
	public SccpSCTPProviderImpl(Properties props) {
		this(MTPProviderFactory.getInstance().getProvider(props),props);
		
		
	}

	public SccpSCTPProviderImpl(MTPProvider provider,Properties props) {
		this.mtpProvider = provider;
		this.mtpProvider.addMtpListener(this);
		//FIXME: move this to mtp provider?
		this.opc = Integer.parseInt(props.getProperty("sccp.opc"));
        this.dpc = Integer.parseInt(props.getProperty("sccp.dpc"));
        this.sls = Integer.parseInt(props.getProperty("sccp.sls"));
        this.ssi = Integer.parseInt(props.getProperty("sccp.ssi"));
        //this.si  = Integer.parseInt(props.getProperty("sccp.si"));
        this.si = Mtp3._SI_SERVICE_SCCP;
	}

	public void receive(byte[] arg2) {
		// add check for SIO parts?
		// if(logger.isInfoEnabled())
	    //logger.info("Received MSU on L4, service: "+service+",subservice: "+subservice);
		new DeliveryHandler(arg2, super.listener).run();

	}




	public void linkDown() {
		//add more?
		if(linkUp)
		{
			this.linkUp = false;
			if(listener!=null)
					listener.linkDown();
		}
		
	}



	public void linkUp() {
		if(!linkUp)
		{
			this.linkUp = true;
			if(listener!=null)
					listener.linkUp();
		}
		
	}

	public void send(SccpAddress calledParty, SccpAddress callingParty, byte[] data, ActionReference ar) throws IOException {
		
		//FIXME:
		if (this.linkUp) {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ProtocolClass pc = null;
			if(ar instanceof UDBase)
			{
				pc = ((UDBase)ar).getpClass();
			}
			
			if(pc == null)
			{
				pc = new ProtocolClassImpl(0, 0);
			}
			UnitDataImpl unitData = new UnitDataImpl(pc, calledParty, callingParty, data);
			unitData.encode(out);
			byte[] buf = out.toByteArray();
			// this.mtp3.send(si, ssf,buf);
			//this.txBuffer.add(ByteBuffer.wrap(buf));

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			if(ar != null)
			{
				//we know how to route!
				bos.write(ar.getBackRouteHeader());
				bos.write(buf);
				buf = bos.toByteArray();
			}else
			{
				//we must build it. damn that Dialogic...
				byte[] sif = new byte[5];
				bos.write(sif);
				bos.write(buf);
				buf = bos.toByteArray();
				if(logger.isInfoEnabled())
				{
					logger.info("Sccp TCP Provider default MTP3 Label: DPC["+dpc+"] OPC["+opc+"] SLS["+sls+"] SI["+si+"] SSI["+ssi+"]");
				}
				Mtp3.writeRoutingLabel(buf, si, ssi, sls, dpc, opc);
				
			}
			if(logger.isInfoEnabled())
			{
				logger.info("Sending SCCP buff: "+Arrays.toString(buf));
			}
			this.mtpProvider.send(buf);
		}else
		{
			throw new IOException("Link is not up!");
		}

	}

	public void shutdown() {
		// if(this.mtp3 != null)
		// {
		// this.mtp3.stop();
		// }
		this.mtpProvider.removeMtpListener(this);
		this.mtpProvider.stop();
		this.mtpProvider = null;

	}

	


	



	private static class DeliveryHandler implements Runnable {
		//this is MTP3 message, we need to decode!
		private byte[] msg;
		private SccpListener listener;
		public DeliveryHandler(byte[] msg,SccpListener listener) {
			super();
			this.msg = msg;
			this.listener = listener;
		}

		public void run() {
			try {
				if (this.listener != null) {
					//offset for sif !
					ByteArrayInputStream bin = new ByteArrayInputStream(msg,5,msg.length);
					DataInputStream in = new DataInputStream(bin);
					int mt;

					mt = in.readUnsignedByte();

					switch (mt) {
					case UnitDataImpl._MT:
						UnitDataImpl unitData = new UnitDataImpl();
						unitData.setBackRouteHeader(msg);
						unitData.decode(in);

						this.listener.onMessage(unitData.getCalledParty(), unitData.getCallingParty(), unitData.getData(),unitData);

						break;
					// 0x11
					case XUnitDataImpl._MT:
						XUnitDataImpl xunitData = new XUnitDataImpl();
						xunitData.setBackRouteHeader(msg);
						xunitData.decode(in);

						this.listener.onMessage(xunitData.getCalledParty(), xunitData.getCallingParty(), xunitData.getData(),xunitData);
						break;
					default:
						logger.error("Undefined message type, MT:" + mt+", Message dump: \n"+Arrays.toString(msg));
						
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error("Failed to pass UD", e);
			}

		}

	}

}
