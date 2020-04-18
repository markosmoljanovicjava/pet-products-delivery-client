/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package validator.impl;

import java.math.BigDecimal;
import validator.Validator;

/**
 *
 * @author marko
 */
public abstract class BigDecimalValidator implements Validator {

    protected String message;
    
    @Override
    public void validate(Object object) throws Exception {
        try {
            new BigDecimal(object.toString());
        } catch (NumberFormatException exception) {
            throw new NumberFormatException(message);
        }
    }
}
