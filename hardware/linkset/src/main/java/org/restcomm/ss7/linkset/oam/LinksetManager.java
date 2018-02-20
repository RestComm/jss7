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
package org.restcomm.ss7.linkset.oam;

import javolution.util.FastMap;

public interface LinksetManager {
    LinksetFactoryFactory getLinksetFactoryFactory();

    void setLinksetFactoryFactory(LinksetFactoryFactory linksetFactoryFactory);

    FastMap<String, Linkset> getLinksets();

    String getPersistDir();

    void setPersistDir(String persistDir);

    Layer4 getLayer4();

    void setLayer4(Layer4 layer4);

    void start();

    void stop();

    String getName();

    String showLinkset(String[] options) throws Exception;

    String createLinkset(String[] options) throws Exception;

    String deleteLinkset(String[] options) throws Exception;

    String activateLinkset(String[] options) throws Exception;

    String deactivateLinkset(String[] options) throws Exception;

    String createLink(String[] options) throws Exception;

    String deleteLink(String[] options) throws Exception;

    String activateLink(String[] options) throws Exception;

    String deactivateLink(String[] options) throws Exception;
}