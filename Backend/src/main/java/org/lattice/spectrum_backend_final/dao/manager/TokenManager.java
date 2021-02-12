package org.lattice.spectrum_backend_final.dao.manager;

/**
 * @author RAHUL KUMAR MAURYA
 */

import java.util.HashMap;
import java.util.Random;

/**
 * This class is used to manage the token.
 * For e.g - creation of a token , provide token.
 */
public class TokenManager {

    private String token;
    private int userId;
    private String userType;
    private String userEmail;
    private String userFullName;
    private String username;
    private boolean isSessionTimeout = false;

    private HashMap<String, Integer> userLoginLimitMap = new HashMap<>();


    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isSessionTimeout() {
        return isSessionTimeout;
    }

    public void setSessionTimeout(boolean sessionTimeout) {
        isSessionTimeout = sessionTimeout;
    }

    public HashMap<String, Integer> getUserLoginLimitMap() {
        return userLoginLimitMap;
    }

    public void setUserLoginLimitMap(HashMap<String, Integer> userLoginLimitMap) {
        this.userLoginLimitMap = userLoginLimitMap;
    }


    /**
     * Generates a random token of desired length.
     *
     * @param tokenLength Takes token length as input.
     */

    public void generateToken(int tokenLength) {
        Random r = new Random();
        StringBuffer sb = new StringBuffer();
        while (sb.length() < tokenLength) {
            sb.append(Integer.toHexString(r.nextInt()));
        }

        token = sb.toString().substring(0, tokenLength);
    }

}
