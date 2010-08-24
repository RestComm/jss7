/*
 * The Java Call Control API for CAMEL 2
 *
 * The source code contained in this file is in in the public domain.
 * It can be used in any project or product without prior permission,
 * license or royalty payments. There is  NO WARRANTY OF ANY KIND,
 * EXPRESS, IMPLIED OR STATUTORY, INCLUDING, WITHOUT LIMITATION,
 * THE IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE,
 * AND DATA ACCURACY.  We do not warrant or make any representations
 * regarding the use of the software or the  results thereof, including
 * but not limited to the correctness, accuracy, reliability or
 * usefulness of the software.
 */

package org.mobicents.protocols.ss7.sccp.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ConfigurationException;
import org.mobicents.protocols.StartFailedException;
import org.mobicents.protocols.ss7.mtp.Mtp3;
import org.mobicents.protocols.ss7.mtp.RoutingLabel;
import org.mobicents.protocols.ss7.sccp.SccpListener;
import org.mobicents.protocols.ss7.sccp.SccpParameterFactory;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.SccpUnitDataFactory;
import org.mobicents.protocols.ss7.sccp.impl.parameter.AddressFactoryImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ProtocolClassImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.SccpParameterFactoryImpl;
import org.mobicents.protocols.ss7.sccp.impl.ud.SccpUnitDataFactoryImpl;
import org.mobicents.protocols.ss7.sccp.impl.ud.UnitDataImpl;
import org.mobicents.protocols.ss7.sccp.impl.ud.XUnitDataImpl;
import org.mobicents.protocols.ss7.sccp.parameter.AddressFactory;
import org.mobicents.protocols.ss7.sccp.parameter.ProtocolClass;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.sccp.ud.UDBase;

/**
 * 
 * @author Oleg Kulikov
 * @author baranowb
 */
public abstract class AbstractSccpProviderImpl implements SccpProvider {

	private final static Logger logger = Logger.getLogger(AbstractSccpProviderImpl.class);

	protected static final ThreadGroup THREAD_GROUP = new ThreadGroup("Sccp-Provider");
	
	public static final String CONFIG_PROVIDER = "sccp.provider";
	public static final String CONFIG_OPC = "sccp.opc";
	public static final String CONFIG_DPC = "sccp.dpc";
	public static final String CONFIG_SLS = "sccp.sls";
	public static final String CONFIG_SSI = "sccp.ssi";
	public static final String CONFIG_THREADS = "sccp.threads";

	//protected List<SccpListener> listeners = new ArrayList<SccpListener>();
	protected SccpListener listener = null;
	protected final SccpUnitDataFactory udFactory;
	protected final SccpParameterFactory paramFactory;

	protected RoutingLabel routingLabel;
	protected Executor executor;
	protected int threadCount = 4;
	public AbstractSccpProviderImpl() {
		super();
		this.paramFactory = new SccpParameterFactoryImpl();
		this.udFactory = new SccpUnitDataFactoryImpl();
	}
	
	
	
	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.sccp.SccpProvider#configure(java.util.Properties)
	 */
	public void configure(Properties props) throws ConfigurationException {
		

		int opc = Integer.parseInt(props.getProperty(AbstractSccpProviderImpl.CONFIG_OPC));
		int dpc = Integer.parseInt(props.getProperty(AbstractSccpProviderImpl.CONFIG_DPC));
		int sls = Integer.parseInt(props.getProperty(AbstractSccpProviderImpl.CONFIG_SLS));
		int ssi = Integer.parseInt(props.getProperty(AbstractSccpProviderImpl.CONFIG_SSI));
		int si = Mtp3._SI_SERVICE_SCCP;
		this.routingLabel = new RoutingLabel(opc, dpc, sls, si, ssi);
		this.threadCount = Integer.parseInt(props.getProperty(AbstractSccpProviderImpl.CONFIG_THREADS, ""+threadCount));
		this.executor = Executors.newFixedThreadPool(this.threadCount);
	}



	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.sccp.SccpProvider#addSccpListener(org.mobicents
	 * .protocols.ss7.sccp.SccpListener)
	 */
	public void addSccpListener(SccpListener listener) {
		if (listener == null) {
			throw new NullPointerException();
		}
		//if (!this.listeners.contains(listener)) {
		//	this.listeners.add(listener);
		//}
		this.listener = listener;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.sccp.SccpProvider#removeSccpListener(org.
	 * mobicents.protocols.ss7.sccp.SccpListener)
	 */
	public void removeSccpListener(SccpListener listener) {
		if (listener == null) {
			throw new NullPointerException();
		}
//		if (this.listeners.contains(listener)) {
//			this.listeners.remove(listener);
//		}
		this.listener = null;

	}

	public SccpParameterFactory getSccpParameterFactory() {

		return paramFactory;
	}

	public SccpUnitDataFactory getUnitDataFactory() {

		return udFactory;
	}

        public AddressFactory getAddressFactory() {
            return new AddressFactoryImpl();
        }
        
	public abstract void start() throws IllegalStateException, StartFailedException;

	public abstract void stop();

	// ///////////////////
	// utility methods //
	// ///////////////////

	protected void receive(final byte[] mtpMSU) {
		
		this.executor.execute(new Runnable() {
			
			public void run() {
				try {

					if (listener != null) {
						// offset for sif !
						ByteArrayInputStream bin = new ByteArrayInputStream(mtpMSU, 5, mtpMSU.length);
						DataInputStream in = new DataInputStream(bin);
						int mt;

						mt = in.readUnsignedByte();

						switch (mt) {
						case UnitDataImpl._MT:
							UnitDataImpl unitData = new UnitDataImpl();
							unitData.setBackRouteHeader(mtpMSU);
							unitData.decode(in);

							listener.onMessage(unitData.getCalledParty(), unitData.getCallingParty(), unitData.getData(), unitData);

							break;
						// 0x11
						case XUnitDataImpl._MT:
							XUnitDataImpl xunitData = new XUnitDataImpl();
							xunitData.setBackRouteHeader(mtpMSU);
							xunitData.decode(in);

							listener.onMessage(xunitData.getCalledParty(), xunitData.getCallingParty(), xunitData.getData(), xunitData);
							break;
						default:
							logger.error("Undefined message type, MT:" + mt + ", Message dump: \n" + Arrays.toString(mtpMSU));

						}

					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					logger.error("Failed to pass UD", e);
				}
				
			}
		});

	}

	protected byte[] encodeToMSU(SccpAddress calledParty, SccpAddress callingParty, byte[] data, RoutingLabel ar) throws IOException
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ProtocolClass pc = null;
		if (ar instanceof UDBase) {
			pc = ((UDBase) ar).getpClass();
		}

		if (pc == null) {
			pc = new ProtocolClassImpl(0, 0);
		}
		UnitDataImpl unitData = new UnitDataImpl(pc, calledParty, callingParty, data);
		unitData.encode(out);
		byte[] buf = out.toByteArray();
		// this.mtp3.send(si, ssf,buf);
		// this.txBuffer.add(ByteBuffer.wrap(buf));

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		if (ar != null) {
			// we know how to route!
			bos.write(ar.getBackRouteHeader());
			bos.write(buf);
			buf = bos.toByteArray();
		} else {
			// we must build it. damn that Dialogic...
			bos.write(routingLabel.getBackRouteHeader());
			bos.write(buf);
			buf = bos.toByteArray();
			if (logger.isInfoEnabled()) {
				logger.info("Sccp Provider default MTP3 Label: " + routingLabel);
			}

		}
		if (logger.isInfoEnabled()) {
			logger.info("Sending SCCP buff: " + Arrays.toString(buf));
		}
		return buf;
	}
}
