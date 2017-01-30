package org.mobicents.ss7.extension;

import org.jboss.as.controller.AbstractAddStepHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.ServiceVerificationHandler;
import org.jboss.as.controller.SimpleAttributeDefinition;
import org.jboss.dmr.ModelNode;
import org.jboss.msc.service.ServiceController;
import org.mobicents.ss7.service.SS7ExtensionService;

import java.util.List;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.mobicents.ss7.extension.SS7MbeanDefinition.MBEAN_ATTRIBUTES;

/**
 * Created by sergeypovarnin on 26.01.17.
 */
class SS7MbeanAdd extends AbstractAddStepHandler {

    static final SS7MbeanAdd INSTANCE = new SS7MbeanAdd();

    private SS7MbeanAdd() {
        //
    }

    @Override
    protected void populateModel(final ModelNode operation, final ModelNode model) throws OperationFailedException {
        PathAddress address = PathAddress.pathAddress(operation.require(OP_ADDR));
        String name = SS7MbeanDefinition.NAME.getName();
        model.get(name).set(address.getLastElement().getValue());

        for (SimpleAttributeDefinition def : MBEAN_ATTRIBUTES) {
            def.validateAndSet(operation, model);
        }
    }

    @Override
    protected void performRuntime(OperationContext context, ModelNode operation, ModelNode model,
                                  ServiceVerificationHandler verificationHandler, List<ServiceController<?>> newControllers)
            throws OperationFailedException {

        final PathAddress address = PathAddress.pathAddress(operation.get(OP_ADDR));
        final String mbeanName = address.getLastElement().getValue();
        System.out.println("MBean NAME from address: "+mbeanName);

        SS7ExtensionService service = SS7ExtensionService.INSTANCE;
        service.addMbean(mbeanName);
    }
}
