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

    public Linkset createLinkset(String[] options) {
        if (options == null) {
            return null;
        }

        String firstOption = null;
        int i = 0;

        while (i < options.length && (firstOption = options[i]) == null) {
            i++;
        }

        if (firstOption == null) {
            return null;
        }

        LinksetFactory linksetFactory = linksetFactories.get(firstOption);

        if (linksetFactory == null) {
            return null;
        }

        options[i] = null;

        return linksetFactory.createLinkset(options);
    }
}
