/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp;

import org.mobicents.protocols.ss7.tcapAnsi.api.asn.ParseException;

/**
 *
 * @author sergey vetyutnev

    --Applications using T1.114-1988 report Transaction portion problems using a
    --Reject component with a problem code in the range 1281-1286. It is preferred
    --that other applications report these problems using the Abort package type.

Problem ::= INTEGER {
    general-unrecognisedComponentType   (257),
    general-incorrectComponentPortion   (258),
    general-badlyStructuredCompPortion  (259),
    general-incorrectComponentCoding    (260),
    invoke-duplicateInvocation      (513),
    invoke-unrecognisedOperation        (514),
    invoke-incorrectParameter       (515),
    invoke-unrecognisedCorrelationId    (516),
    returnResult-unrecognisedCorrelationId  (769),
    returnResult-unexpectedReturnResult (770),
    returnResult-incorrectParameter     (771),
    returnError-unrecognisedCorrelationId   (1025),
    returnError-unexpectedReturnError   (1026),
    returnError-unrecognisedError       (1027),
    returnError-unexpectedError     (1028),
    returnError-incorrectParameter      (1029),
    --Applications using T1.114-1988 report Transaction portion problems using a
    --Reject component with a problem code in the range 1281-1286. It is preferred
    --that other applications report these problems using the Abort package type.
    transaction-unrecognizedPackageType (1281),
    transaction-incorrectTransPortion   (1282),
    transaction-badlyStructuredTransPortion (1283),
    transaction-unassignedRespondingTransID (1284),
    transaction-permissionToReleaseProblem  (1285),
    transaction-resourceUnavailable     (1286)
}

 *
 */
public enum RejectProblem {

    // general
    /**
     * The Component Type has not been defined.
     */
    generalUnrecognisedComponentType(257),

    /**
     * An unexpected or undefined identifier within the Component Portion.
     */
    generalIncorrectComponentPortion(258),

    /**
     * A fundamental encoding problem (e.g., bad length).
     */
    generalBadlyStructuredCompPortion(259),

    /**
     * General encoding problems not covered under Items (257) to (259).
     */
    generalIncorrectComponentCoding(260),

    // Invoke
    /**
     * An Invoke ID is received which has already become assigned to another
     * operation in progress.
     */
    invokeDuplicateInvocation(513),

    /**
     * The operation has not been defined by the Application Process.
     */
    invokeUnrecognisedOperation(514),

    /**
     * An unexpected or undefined Parameter was received.
     */
    invokeIncorrectParameter(515),

    /**
     * The received Correlation ID does not reflect an operation that is currently in progress.
     */
    invokeUnrecognisedCorrelationId(516),

    // ReturnResult
    /**
     * The received Correlation ID does not reflect an operation that is currently in progress.
     */
    returnResultUnrecognisedCorrelationId(769),

    /**
     * The invoked operation does not report success.
     */
    returnResultUnexpectedReturnResult(770),

    /**
     * An unexpected or undefined Parameter (result) was received.
     */
    returnResultIncorrectParameter(771),

    // ReturnError
    /**
     * The received Correlation ID does not reflect an operation that is currently in progress.
     */
    returnErrorUnrecognisedCorrelationId(1025),

    /**
     * The Return Error Component did not report failure of the invoked operation.
     */
    returnErrorUnexpectedReturnError(1026),

    /**
     * The reported error has not been defined by the Application Process.
     */
    returnErrorUnrecognisedError(1027),

    /**
     * The reported error is not applicable to the invoked operation
     */
    returnErrorUnexpectedError(1028),

    /**
     * An unexpected or undefined Parameter was received.
     */
    returnErrorIncorrectParameter(1029),

    // Transaction Portion
    // Applications using ANSI T1.114-1988 report Transaction Portion problems using a Reject component.
    // It is preferred that other applications report these problems using the Abort package type.
    /**
     * The Package Type has not been defined.
     */
    transactionUnrecognizedPackageType(1281),
    /**
     * An unexpected or undefined identifier was received within the
     * Transaction Portion.
     */
    transactionIncorrectTransPortion(1282),
    /**
     * A fundamental encoding problem (e.g. bad length).
     */
    transactionBadlyStructuredTransPortion(1283),
    /**
     * The received Transaction ID is derivable but does not reflect
     * a transaction currently in progress.
     */
    transactionUnassignedRespondingTransID(1284),
    /**
     * This problem is for further study.
     */
    transactionPermissionToReleaseProblem(1285),
    /**
     * Sufficient resources are not available at the Transaction sub-layer to establish a transaction.
     */
    transactionResourceUnavailable(1286);

    private long type;

    RejectProblem(long l) {
        this.type = l;
    }

    /**
     * @return the type
     */
    public long getType() {
        return type;
    }

    public static RejectProblem getFromInt(long t) throws ParseException {
        if (t == 257) {
            return generalUnrecognisedComponentType;
        } else if (t == 258) {
            return generalIncorrectComponentPortion;
        } else if (t == 259) {
            return generalBadlyStructuredCompPortion;
        } else if (t == 260) {
            return generalIncorrectComponentCoding;
        } else if (t == 513) {
            return invokeDuplicateInvocation;
        } else if (t == 514) {
            return invokeUnrecognisedOperation;
        } else if (t == 515) {
            return invokeIncorrectParameter;
        } else if (t == 516) {
            return invokeUnrecognisedCorrelationId;
        } else if (t == 769) {
            return returnResultUnrecognisedCorrelationId;
        } else if (t == 770) {
            return returnResultUnexpectedReturnResult;
        } else if (t == 771) {
            return returnResultIncorrectParameter;

        } else if (t == 1025) {
            return returnErrorUnrecognisedCorrelationId;
        } else if (t == 1026) {
            return returnErrorUnexpectedReturnError;
        } else if (t == 1027) {
            return returnErrorUnrecognisedError;
        } else if (t == 1028) {
            return returnErrorUnexpectedError;
        } else if (t == 1029) {
            return returnErrorIncorrectParameter;

        } else if (t == 1281) {
            return transactionUnrecognizedPackageType;
        } else if (t == 1282) {
            return transactionIncorrectTransPortion;
        } else if (t == 1283) {
            return transactionBadlyStructuredTransPortion;
        } else if (t == 1284) {
            return transactionUnassignedRespondingTransID;
        } else if (t == 1285) {
            return transactionPermissionToReleaseProblem;
        } else if (t == 1286) {
            return transactionResourceUnavailable;
        }

        throw new ParseException(RejectProblem.generalIncorrectComponentCoding, "Wrong value of type RejectProblem: " + t);
    }

    public static RejectProblem getFromPAbortCause(PAbortCause t) {

        if (t == PAbortCause.UnrecognizedPackageType) {
            return transactionUnrecognizedPackageType;
        } else if (t == PAbortCause.IncorrectTransactionPortion) {
            return transactionIncorrectTransPortion;
        } else if (t == PAbortCause.BadlyStructuredTransactionPortion) {
            return transactionBadlyStructuredTransPortion;
        } else if (t == PAbortCause.UnassignedRespondingTransactionID) {
            return transactionUnassignedRespondingTransID;
        } else if (t == PAbortCause.PermissionToReleaseProblem) {
            return transactionPermissionToReleaseProblem;
        } else if (t == PAbortCause.ResourceUnavailable) {
            return transactionResourceUnavailable;
        } else if (t == PAbortCause.UnrecognizedDialoguePortionID) {
            return generalUnrecognisedComponentType;
        } else if (t == PAbortCause.InconsistentDialoguePortion) {
            return generalIncorrectComponentCoding;
        }

        return null;
    }

}
