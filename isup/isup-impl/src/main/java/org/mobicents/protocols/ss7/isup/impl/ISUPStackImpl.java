/**
 * Start time:12:14:57 2009-09-04<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.impl;

import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.mobicents.protocols.ss7.isup.ISUPProvider;
import org.mobicents.protocols.ss7.isup.ISUPStack;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.stream.MTPProvider;


/**
 * Start time:12:14:57 2009-09-04<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public class ISUPStackImpl implements ISUPStack {


    private MTPProvider mtpTransportProvider;
    private ISUPMtpProviderImpl isupMtpProvider;
    
    private SccpProvider sccpTransportProvider;
    private ISUPSccpProviderImpl isupSccpProvider;
    
    private boolean started = false;
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(8);
    private long _GENERAL_TRANSACTION_TIMEOUT = 120 * 1000;
    private long _CLIENT_TRANSACTION_ANSWER_TIMEOUT = 30 * 1000;

    public ISUPStackImpl(MTPProvider transportProvider, Properties properties) {
        super();
        this.mtpTransportProvider = transportProvider;

        this.isupMtpProvider = new ISUPMtpProviderImpl(this.mtpTransportProvider, this,properties);

    }

    public ISUPStackImpl(SccpProvider transportProvider) {
        super();
        this.sccpTransportProvider = transportProvider;

       this.isupSccpProvider = new ISUPSccpProviderImpl(this.sccpTransportProvider, this);

    }
    
    public ISUPProvider getIsupProvider() {
    	if(isupMtpProvider!=null)
    	{
    		return isupMtpProvider;
    	}else
    	{
    		return this.isupSccpProvider;
    	}
    }

    public void start() {
        if (!started) {
            configure();
            if(this.mtpTransportProvider!=null)
            {
            	this.mtpTransportProvider.addMtpListener(this.isupMtpProvider);
            }else
            {
            	this.sccpTransportProvider.setSccpListener(this.isupSccpProvider);
            }
            this.started = true;
        }
    }

    public void stop() {
        if (started) {
            this.mtpTransportProvider.removeMtpListener(this.isupMtpProvider);
            this.mtpTransportProvider.stop();
            terminate();
            this.started = false;

        }
    }

    // ///////////////
    // CONF METHOD //
    // ///////////////
    /**
     *
     */
    private void configure() {
        this.executor = Executors.newScheduledThreadPool(8);

    }

    /**
     *
     */
    private void terminate() {
        this.executor.shutdownNow();
    }
    //possibly something similar as in MGCP

    ScheduledExecutorService getExecutors() {
        return this.executor;
    }

    /**
     * @return
     */
    public long getTransactionGeneralTimeout() {
        return _GENERAL_TRANSACTION_TIMEOUT;
    }

    /**
     * @return
     */
    public long getClientTransactionAnswerTimeout() {
        return _CLIENT_TRANSACTION_ANSWER_TIMEOUT;
    }
}
