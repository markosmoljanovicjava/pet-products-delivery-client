/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package validator.util;

/**
 *
 * @author marko
 */
public class RegexTuple {

    private String pattern;
    private String input;

    public RegexTuple() {
    }

    public RegexTuple(String pattern, String input) {
        this.pattern = pattern;
        this.input = input;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

}
