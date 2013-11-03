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
import javolution.xml.XMLBinding;

/**
 * <p>
 * Factory class that holds map of {@link LinksetFactory}.
 * </p>
 *
 * @author amit bhayani
 *
 */
public class LinksetFactoryFactory {

    private FastMap<String, LinksetFactory> linksetFactories = new FastMap<String, LinksetFactory>();

    /**
     * Call back method to add new {@link LinksetFactory}
     *
     * @param factory
     */
    public void addFactory(LinksetFactory factory) {
        linksetFactories.put(factory.getName(), factory);
    }

    /**
     * Call back method to remove existing {@link LinksetFactory}
     *
     * @param factory
     */
    public void removeFactory(LinksetFactory factory) {
        linksetFactories.remove(factory);
    }

    public void loadBinding(XMLBinding binding) {
        FastMap.Entry<String, LinksetFactory> e = this.linksetFactories.head();
        FastMap.Entry<String, LinksetFactory> end = this.linksetFactories.tail();
        for (; (e = e.getNext()) != end;) {
            LinksetFactory linksetFactory = e.getValue();
            if (linksetFactory.getLinkName() != null)
                binding.setAlias(linksetFactory.getLinkClass(), linksetFactory.getLinkName());

            if (linksetFactory.getLinksetName() != null)
                binding.setAlias(linksetFactory.getLinksetClass(), linksetFactory.getLinksetName());
        }
    }

    /**
     * Create a new {@link Linkset} depending on the options passed.
     *
     * @param options
     * @return
     * @throws Exception
     */
    public Linkset createLinkset(String[] options) throws Exception {
        if (options == null) {
            throw new Exception(LinkOAMMessages.INVALID_COMMAND);
        }

        // The expected command is "linkset create <likset-type> <options>"
        // Expect atleast length to 3
        if (options.length < 3) {
            throw new Exception(LinkOAMMessages.INVALID_COMMAND);
        }

        String type = options[2];

        if (type == null) {
            throw new Exception(LinkOAMMessages.INVALID_COMMAND);
        }

        LinksetFactory linksetFactory = linksetFactories.get(type);

        if (linksetFactory == null) {
            throw new Exception(LinkOAMMessages.INVALID_COMMAND);
        }
        return linksetFactory.createLinkset(options);
    }

    public FastMap<String, LinksetFactory> getLinksetFactories() {
        return linksetFactories;
    }
}