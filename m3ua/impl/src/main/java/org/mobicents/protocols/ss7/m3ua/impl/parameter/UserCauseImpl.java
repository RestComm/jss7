package org.mobicents.protocols.ss7.m3ua.impl.parameter;

import org.mobicents.protocols.ss7.m3ua.parameter.Parameter;
import org.mobicents.protocols.ss7.m3ua.parameter.UserCause;

/**
 * 
 * @author amit bhayani
 * 
 */
public class UserCauseImpl extends ParameterImpl implements UserCause {

    private int user = 0;
    private int cause = 0;

    private byte[] value;

    protected UserCauseImpl(byte[] value) {
        this.tag = Parameter.User_Cause;

        this.user = 0;
        this.user |= value[0] & 0xFF;
        this.user <<= 8;
        this.user |= value[1] & 0xFF;

        this.cause = 0;
        this.cause |= value[2] & 0xFF;
        this.cause <<= 8;
        this.cause |= value[3] & 0xFF;

        this.value = value;
    }

    protected UserCauseImpl(int user, int cause) {
        this.tag = Parameter.User_Cause;
        this.user = user;
        this.cause = cause;
        encode();
    }

    private void encode() {
        // create byte array taking into account data, point codes and
        // indicators;
        this.value = new byte[4];
        // encode routing context
        value[0] = (byte) (this.user >> 8);
        value[1] = (byte) (this.user);

        value[2] = (byte) (this.cause >> 8);
        value[3] = (byte) (this.cause);

    }

    @Override
    protected byte[] getValue() {
        return value;
    }

    public int getCause() {
        return this.cause;
    }

    public int getUser() {
        return this.user;
    }

    @Override
    public String toString() {
        return String.format("UserCause cause = %d user = %d", this.cause,
                this.user);
    }

}
