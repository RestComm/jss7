package org.mobicents.ss7.extension;

import org.jboss.as.controller.AbstractBoottimeAddStepHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.ServiceVerificationHandler;
import org.jboss.as.controller.registry.Resource;
import org.jboss.as.controller.services.path.PathManager;
import org.jboss.as.controller.services.path.PathManagerService;
import org.jboss.as.jmx.MBeanServerService;
import org.jboss.dmr.ModelNode;
import org.jboss.logging.Logger;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;
import org.mobicents.ss7.service.SS7ExtensionService;
import org.mobicents.ss7.service.SS7ServiceInterface;

import javax.management.MBeanServer;
import java.util.List;

/**
 * Handler responsible for adding the subsystem resource to the model
 *
 * @author <a href="mailto:sergey.povarnin@telestax.com">Sergey Povarnin</a>
 */
class SubsystemAdd extends AbstractBoottimeAddStepHandler {

  static final SubsystemAdd INSTANCE = new SubsystemAdd();

  private final Logger log = Logger.getLogger(SubsystemAdd.class);

  private SubsystemAdd() {
  }

  /** {@inheritDoc} */
  @Override
  protected void populateModel(ModelNode operation, ModelNode model) throws OperationFailedException {
    log.info("Populating the model: "+model);
    //model.setEmptyObject();
  }

  /** {@inheritDoc} */
  @Override
  public void performBoottime(OperationContext context, ModelNode operation, ModelNode model,
                              ServiceVerificationHandler verificationHandler, List<ServiceController<?>> newControllers)
      throws OperationFailedException {

    ModelNode fullModel = Resource.Tools.readModel(context.readResource(PathAddress.EMPTY_ADDRESS));

    SS7ExtensionService service = SS7ExtensionService.INSTANCE;
    service.setModel(fullModel);

    ServiceName name = SS7ExtensionService.getServiceName();
    ServiceController<SS7ServiceInterface> controller = context.getServiceTarget()
        .addService(name, service)
        .addDependency(PathManagerService.SERVICE_NAME, PathManager.class, service.getPathManagerInjector())
        .addDependency(MBeanServerService.SERVICE_NAME, MBeanServer.class, service.getMbeanServer())
        .addListener(verificationHandler)
        .setInitialMode(ServiceController.Mode.ACTIVE)
        .install();
    newControllers.add(controller);
  }
}