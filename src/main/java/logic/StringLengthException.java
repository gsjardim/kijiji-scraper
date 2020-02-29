/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

/**
 *
 * @author gsoar
 */
public class StringLengthException extends Exception {

    public StringLengthException() {
        super("String value is too long for this field");
    }
    
}
