package org.mobicents.ss7;

import org.mobicents.protocols.ss7.management.console.LinksetListener;
import org.mobicents.protocols.ss7.mtp.oam.LinksetManager;

public class LinksetListenerImpl implements LinksetListener {

    private LinksetManager linksetManager = null;

    protected LinksetListenerImpl(LinksetManager linksetManager) {
        this.linksetManager = linksetManager;
    }

    public String execute(String[] options) {

        String firstOption = null;
        int i = 0;
        while (i < options.length && (firstOption = options[i]) == null) {
            i++;
        }

        if (firstOption == null) {
            return "Invalid Command";
        }

        if (firstOption.compareTo("create") == 0) {
            options[i] = null;
            return this.linksetManager.createLinkset(options);
        }

        return "Invalid Command";
    }

}
