package org.mobicents.ss7.extension;

import org.jboss.as.controller.AbstractAddStepHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.ServiceVerificationHandler;
import org.jboss.dmr.ModelNode;
import org.jboss.msc.service.ServiceController;
import org.mobicents.ss7.service.SS7ExtensionService;

import java.util.List;

class SS7MbeanPropertyAdd extends AbstractAddStepHandler {

    public static final SS7MbeanPropertyAdd INSTANCE = new SS7MbeanPropertyAdd();

    private SS7MbeanPropertyAdd() {
    }

    @Override
    protected void populateModel(final ModelNode operation, final ModelNode model) throws OperationFailedException {
        final PathAddress pathAddress = PathAddress.pathAddress(operation.get("address"));
        String name = SS7MbeanPropertyDefinition.NAME_ATTR.getName();

        SS7MbeanPropertyDefinition.NAME_ATTR.validateAndSet(operation, model);
        SS7MbeanPropertyDefinition.VALUE_ATTR.validateAndSet(operation, model);
    }

    @Override
    protected void performRuntime(OperationContext context, ModelNode operation, ModelNode model,
                                  ServiceVerificationHandler verificationHandler, List<ServiceController<?>> newControllers)
            throws OperationFailedException {

        final PathAddress pathAddress = PathAddress.pathAddress(operation.get("address"));
        final String mbeanName = pathAddress.getElement(1).getValue();
        final ModelNode nameNode = SS7MbeanPropertyDefinition.NAME_ATTR.resolveModelAttribute(context, model);
        final String name = nameNode.isDefined() ? nameNode.asString() : null;
        final ModelNode valueNode = SS7MbeanPropertyDefinition.VALUE_ATTR.resolveModelAttribute(context, model);
        final String value = valueNode.isDefined() ? valueNode.asString() : null;

        SS7ExtensionService service = SS7ExtensionService.INSTANCE;
        service.addProperty(mbeanName, name, value);
    }
}