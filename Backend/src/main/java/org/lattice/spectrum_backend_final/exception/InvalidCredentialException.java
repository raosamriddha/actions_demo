package org.lattice.spectrum_backend_final.exception;

/**
 * @author RAHUL KUMAR MAURYA
 */

/**
 * Throws exception when user enters invalid credentials.
 */
public class InvalidCredentialException extends Exception {

    public InvalidCredentialException(String message) {
        super(message);
    }
}
