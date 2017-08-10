package org.mobicents.protocols.ss7.map;

import org.mobicents.protocols.ss7.map.api.MAPServiceBase;

public enum MAPServiceType {
    Lsm {
        @Override
        public MAPServiceBase getService(MAPProviderImpl mapProviderImpl) {
            return mapProviderImpl.getMAPServiceLsm();
        }
    },
    Mobility {
        @Override
        public MAPServiceBase getService(MAPProviderImpl mapProviderImpl) {
            return mapProviderImpl.getMAPServiceMobility();
        }
    },
    Oam {
        @Override
        public MAPServiceBase getService(MAPProviderImpl mapProviderImpl) {
            return mapProviderImpl.getMAPServiceOam();
        }
    },
    PdpContextActivation {
        @Override
        public MAPServiceBase getService(MAPProviderImpl mapProviderImpl) {
            return mapProviderImpl.getMAPServicePdpContextActivation();
        }
    },
    Sms {
        @Override
        public MAPServiceBase getService(MAPProviderImpl mapProviderImpl) {
            return mapProviderImpl.getMAPServiceSms();
        }
    },
    Suppplementary {
        @Override
        public MAPServiceBase getService(MAPProviderImpl mapProviderImpl) {
            return mapProviderImpl.getMAPServiceSupplementary();
        }
    },
    CallHandling {
        @Override
        public MAPServiceBase getService(MAPProviderImpl mapProviderImpl) {
            return mapProviderImpl.getMAPServiceCallHandling();
        }
    };


    MAPServiceType() {
    }
    public abstract MAPServiceBase getService(MAPProviderImpl mapProviderImpl);
}
