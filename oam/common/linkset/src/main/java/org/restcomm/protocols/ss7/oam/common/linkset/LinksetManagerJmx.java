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
package org.restcomm.protocols.ss7.oam.common.linkset;

import javolution.util.FastMap;

import org.restcomm.protocols.ss7.oam.common.jmx.MBeanHost;
import org.restcomm.protocols.ss7.oam.common.jmxss7.Ss7Layer;
import org.restcomm.ss7.linkset.oam.Layer4;
import org.restcomm.ss7.linkset.oam.Linkset;
import org.restcomm.ss7.linkset.oam.LinksetFactoryFactory;
import org.restcomm.ss7.linkset.oam.LinksetManager;

/**
 * @author Lasith Waruna Perera
 *
 */
public class LinksetManagerJmx implements LinksetManagerJmxMBean {

    private final MBeanHost ss7Management;
    private final LinksetManager wrappedLinksetManager;

    public LinksetManagerJmx(MBeanHost ss7Management, LinksetManager wrappedLinksetManager) {
        this.ss7Management = ss7Management;
        this.wrappedLinksetManager = wrappedLinksetManager;
    }

    @Override
    public String activateLink(String[] arg0) throws Exception {
        return this.wrappedLinksetManager.activateLink(arg0);
    }

    @Override
    public String activateLinkset(String[] arg0) throws Exception {
        return this.wrappedLinksetManager.activateLinkset(arg0);
    }

    @Override
    public String createLink(String[] arg0) throws Exception {
        return this.wrappedLinksetManager.createLink(arg0);
    }

    @Override
    public String createLinkset(String[] arg0) throws Exception {
        return this.wrappedLinksetManager.createLinkset(arg0);
    }

    @Override
    public String deactivateLink(String[] arg0) throws Exception {
        return this.wrappedLinksetManager.deactivateLink(arg0);
    }

    @Override
    public String deactivateLinkset(String[] arg0) throws Exception {
        return this.wrappedLinksetManager.deactivateLinkset(arg0);
    }

    @Override
    public String deleteLink(String[] arg0) throws Exception {
        return this.wrappedLinksetManager.deleteLink(arg0);
    }

    @Override
    public String deleteLinkset(String[] arg0) throws Exception {
        return this.wrappedLinksetManager.deleteLinkset(arg0);
    }

    @Override
    public Layer4 getLayer4() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public LinksetFactoryFactory getLinksetFactoryFactory() {
        return this.wrappedLinksetManager.getLinksetFactoryFactory();
    }

    @Override
    public FastMap<String, Linkset> getLinksets() {
        return this.wrappedLinksetManager.getLinksets();
    }

    @Override
    public String getName() {
        return this.wrappedLinksetManager.getName();
    }

    @Override
    public String getPersistDir() {
        return this.wrappedLinksetManager.getPersistDir();
    }

    @Override
    public void setLayer4(Layer4 arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setLinksetFactoryFactory(LinksetFactoryFactory arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setPersistDir(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public String showLinkset(String[] arg0) throws Exception {
        return this.wrappedLinksetManager.showLinkset(arg0);
    }

    @Override
    public void start() {
        this.ss7Management.registerMBean(Ss7Layer.LINKSET, LinksetManagementType.MANAGEMENT, this.getName(), this);
    }

    @Override
    public void stop() {
        // TODO Auto-generated method stub

    }

}
