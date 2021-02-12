package org.lattice.spectrum_backend_final.exception;

/**
 * @author RAHUL KUMAR MAURYA
 */

/**
 * Throws exception when user enter past three password on password change.
 */
public class OldPasswordException extends Exception {

    public OldPasswordException(String message) {
        super(message);
    }


}
