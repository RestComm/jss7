/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package org.mobicents.ss7.linkset.oam;

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