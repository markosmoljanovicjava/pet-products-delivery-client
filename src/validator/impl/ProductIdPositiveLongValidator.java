/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package validator.impl;

/**
 *
 * @author marko
 */
public class ProductIdPositiveLongValidator extends PositiveLongValidator{

    public ProductIdPositiveLongValidator() {
        message = "Product id must be natural number";
    }
    
}
