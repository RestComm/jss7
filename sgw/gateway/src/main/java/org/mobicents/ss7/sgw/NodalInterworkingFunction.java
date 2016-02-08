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

package org.mobicents.ss7.sgw;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

import javolution.util.FastList;
import javolution.util.FastMap;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.M3UAManagement;
import org.mobicents.protocols.ss7.m3ua.impl.M3UAManagementImpl;
import org.mobicents.protocols.ss7.mtp.Mtp3EndCongestionPrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3PausePrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3ResumePrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3StatusPrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3TransferPrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3TransferPrimitiveFactory;
import org.mobicents.protocols.ss7.mtp.Mtp3UserPartListener;
import org.mobicents.protocols.ss7.scheduler.IntConcurrentHashMap;
import org.mobicents.protocols.ss7.scheduler.Scheduler;
import org.mobicents.protocols.ss7.scheduler.Task;
import org.mobicents.protocols.stream.api.SelectorKey;
import org.mobicents.ss7.linkset.oam.Layer4;
import org.mobicents.ss7.linkset.oam.Linkset;
import org.mobicents.ss7.linkset.oam.LinksetManager;
import org.mobicents.ss7.linkset.oam.LinksetSelector;
import org.mobicents.ss7.linkset.oam.LinksetStream;

/** */
public class NodalInterworkingFunction extends Task implements Layer4, Mtp3UserPartListener {

    private static Logger logger = Logger.getLogger(NodalInterworkingFunction.class);

    private LinksetSelector linkSetSelector = new LinksetSelector();
    private LinksetStream linksetStream = null;

    private LinksetManager linksetManager = null;

    private M3UAManagementImpl m3UAManagement = null;
    private Mtp3TransferPrimitiveFactory mtp3TransferPrimitiveFactory = null;

    private boolean started = false;

    private int OP_READ_WRITE = 3;

    // max data size is 2176;
    private byte[] rxBuffer = new byte[2176];
    private byte[] tempBuffer;

    private ConcurrentLinkedQueue<byte[]> mtpqueue = new ConcurrentLinkedQueue<byte[]>();
    private ConcurrentLinkedQueue<Mtp3TransferPrimitive> m3uaqueue = new ConcurrentLinkedQueue<Mtp3TransferPrimitive>();

    private IntConcurrentHashMap<Linkset> linksets = new IntConcurrentHashMap<Linkset>();

    public NodalInterworkingFunction(Scheduler scheduler) {
        super(scheduler);
    }

    public LinksetManager getLinksetManager() {
        return linksetManager;
    }

    public void setLinksetManager(LinksetManager linksetManager) {
        this.linksetManager = linksetManager;
    }

    public int getQueueNumber() {
        return scheduler.INTERNETWORKING_QUEUE;
    }

    public M3UAManagement getM3UAManagement() {
        return m3UAManagement;
    }

    public void setM3UAManagement(M3UAManagement m3UAManagement) {
        this.m3UAManagement = (M3UAManagementImpl) m3UAManagement;
        this.m3UAManagement.addMtp3UserPartListener(this);
        this.mtp3TransferPrimitiveFactory = this.m3UAManagement.getMtp3TransferPrimitiveFactory();
    }

    // Layer4 methods
    public void add(Linkset linkset) {
        try {
            linksets.add(linkset, linkset.getApc());

            linksetStream = linkset.getLinksetStream();
            linksetStream.register(this.linkSetSelector);
        } catch (IOException ex) {
            logger.error(String.format("Registration for %s LinksetStream failed", linkset.getName()), ex);
        }
    }

    public void remove(Linkset linkset) {
        linksets.remove(linkset.getApc());

    }

    // Life cycle methods
    public void start() throws Exception {

        // Linkset
        this.linksetManager.setLayer4(this);

        // Add all linkset stream
        FastMap<String, Linkset> map = this.linksetManager.getLinksets();
        for (FastMap.Entry<String, Linkset> e = map.head(), end = map.tail(); (e = e.getNext()) != end;) {
            Linkset value = e.getValue();
            this.add(value);
        }

        this.started = true;
        this.activate(false);
        scheduler.submit(this, scheduler.INTERNETWORKING_QUEUE);
    }

    public void stop() throws Exception {
        this.started = false;
    }

    @Override
    public long perform() {
        Mtp3TransferPrimitive currPrimitive;
        if (!started) {
            return 0;
        }

        try {
            FastList<SelectorKey> selected = linkSetSelector.selectNow(OP_READ_WRITE, 1);
            for (FastList.Node<SelectorKey> n = selected.head(), end = selected.tail(); (n = n.getNext()) != end;) {
                SelectorKey key = n.getValue();
                int size = ((LinksetStream) key.getStream()).read(rxBuffer);
                if (size > 0) {
                    tempBuffer = new byte[size];
                    System.arraycopy(rxBuffer, 0, tempBuffer, 0, size);

                    currPrimitive = mtp3TransferPrimitiveFactory.createMtp3TransferPrimitive(tempBuffer);
                    this.m3UAManagement.sendMessage(currPrimitive);
                }
            }
        } catch (IOException e) {
        }

        try {
            currPrimitive = null;
            while ((currPrimitive = m3uaqueue.poll()) != null) {
                Linkset value = linksets.get(currPrimitive.getDpc());
                if (value != null)
                    value.getLinksetStream().write(currPrimitive.encodeMtp3());
            }
        } catch (IOException e) {
        }

        scheduler.submit(this, scheduler.INTERNETWORKING_QUEUE);
        return 0;
    }

    public void onMtp3TransferMessage(Mtp3TransferPrimitive msg) {
        m3uaqueue.offer(msg);
    }

    public void onMtp3PauseMessage(Mtp3PausePrimitive msg) {
        // not used
    }

    public void onMtp3ResumeMessage(Mtp3ResumePrimitive msg) {
        // not used
    }

    public void onMtp3StatusMessage(Mtp3StatusPrimitive msg) {
        // not used
    }

    @Override
    public void onMtp3EndCongestionMessage(Mtp3EndCongestionPrimitive msg) {
        // not used
    }
}