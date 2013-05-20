/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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
package org.mobicents.ss7.linkset.oam;

import javolution.util.FastMap;

public interface LinksetManager {
    public LinksetFactoryFactory getLinksetFactoryFactory();
    public void setLinksetFactoryFactory(LinksetFactoryFactory linksetFactoryFactory);
    public FastMap<String, Linkset> getLinksets();
    public String getPersistDir();
    public void setPersistDir(String persistDir);
    public Layer4 getLayer4();
    public void setLayer4(Layer4 layer4);
    public void start();
    public void stop();
    public String getName();
    
    public String showLinkset(String[] options) throws Exception;
    public String createLinkset(String[] options) throws Exception ;
    public String deleteLinkset(String[] options) throws Exception ;
    public String activateLinkset(String[] options) throws Exception;
    public String deactivateLinkset(String[] options) throws Exception;
    
    public String createLink(String[] options) throws Exception;
    public String deleteLink(String[] options) throws Exception ;
    public String activateLink(String[] options) throws Exception;
    public String deactivateLink(String[] options) throws Exception;
}
