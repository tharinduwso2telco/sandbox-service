/*
 * SandboxError.java
 * May 22, 2014  2:23:04 PM
 * Roshan.Saputhanthri
 *
 * Copyright (C) Dialog Axiata PLC. All Rights Reserved.
 */

package com.wso2telco.services.dep.sandbox.dao.model.domain;

/**
 * <TO-DO> <code>SandboxError</code>
 * @version $Id: SandboxError.java,v 1.00.000
 */
public class SandboxError {

    private String error;

    public SandboxError(String error) {
        this.error = error;
    }

    
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
    
}
