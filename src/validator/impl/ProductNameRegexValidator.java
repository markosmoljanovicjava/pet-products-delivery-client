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
public class ProductNameRegexValidator extends RegexValidator {

    public ProductNameRegexValidator() {
        message = "Product name must contain only [letters, digits, _, -]";
    }

}
