package org.mobicents.ss7.extension;

import org.jboss.as.controller.AbstractRemoveStepHandler;

class SS7MbeanRemove extends AbstractRemoveStepHandler {

    static final SS7MbeanRemove INSTANCE = new SS7MbeanRemove();

    private SS7MbeanRemove() {
    }
}