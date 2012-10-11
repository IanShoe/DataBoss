/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idk.databoss.exception;

/**
 * Exception to throw when database required attributes are missing
 *
 * @author shoemaki
 */
public class IllegalRequiredAttribute extends Exception {

    public IllegalRequiredAttribute(String message) {
        super(message);
    }
}