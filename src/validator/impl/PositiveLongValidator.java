/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package validator.impl;

import validator.Validator;

/**
 *
 * @author marko
 */
public abstract class PositiveLongValidator implements Validator {

    protected String message;

    @Override
    public void validate(Object object) throws Exception {
        try {
            Long l = Long.parseLong(object.toString());
            if (l <= 0) {
                throw new Exception(message);
            }
        } catch (NumberFormatException ex) {
            throw new NumberFormatException(message);
        }
    }

}
