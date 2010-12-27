package org.mobicents.ss7.linkset.oam;

import javolution.util.FastMap;

public class LinksetFactoryFactory {

    private FastMap<String, LinksetFactory> linksetFactories = new FastMap<String, LinksetFactory>();

    public void addFactory(LinksetFactory factory) {
        linksetFactories.put(factory.getName(), factory);
    }

    public void removeFactory(LinksetFactory factory) {
        linksetFactories.remove(factory);
    }

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
}
