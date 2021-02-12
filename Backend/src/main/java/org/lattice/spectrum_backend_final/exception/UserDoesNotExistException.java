package org.lattice.spectrum_backend_final.exception;

/**
 * @author RAHUL KUMAR MAURYA
 */

/**
 * Throws exception when user doesn't exist.
 */
public class UserDoesNotExistException extends Exception {

    public UserDoesNotExistException(String message) {
        super(message);
    }
}
