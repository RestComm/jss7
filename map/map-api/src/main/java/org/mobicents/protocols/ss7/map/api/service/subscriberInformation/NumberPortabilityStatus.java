package org.mobicents.protocols.ss7.map.api.service.subscriberInformation;

/**
 * NumberPortabilityStatus ::= ENUMERATED {
 *		notKnownToBePorted (0),
 *		ownNumberPortedOut (1),
 *		foreignNumberPortedToForeignNetwork (2),
 *		...,
 *		ownNumberNotPortedOut (4),
 *		foreignNumberPortedIn (5)
 *	}
 *	-- exception handling:
 *	-- reception of other values than the ones listed the receiver shall ignore the
 *	-- whole NumberPortabilityStatus;
 *	-- ownNumberNotPortedOut or foreignNumberPortedIn may only be included in Any Time
 *	-- Interrogation message.
 * @author amit bhayani
 * 
 */
public enum NumberPortabilityStatus {
	notKnownToBePorted(0), ownNumberPortedOut(1), foreignNumberPortedToForeignNetwork(2), ownNumberNotPortedOut(4), foreignNumberPortedIn(5);

	private final int type;

	private NumberPortabilityStatus(int type) {
		this.type = type;
	}

	public int getType() {
		return this.type;
	}

	public static NumberPortabilityStatus getInstance(int type) {
		switch (type) {
		case 0:
			return notKnownToBePorted;
		case 1:
			return ownNumberPortedOut;
		case 2:
			return foreignNumberPortedToForeignNetwork;
		case 4:
			return ownNumberNotPortedOut;
		case 5:
			return foreignNumberPortedIn;
		default:
			return null;
		}
	}
}
