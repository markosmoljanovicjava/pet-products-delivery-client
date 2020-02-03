/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import domain.User;
import ui.controller.ControllerLogin;
import ui.view.ViewLogin;

/**
 *
 * @author remar
 */
public class Main {

    public static void main(String[] args) {
        ControllerLogin controllerLogin = new ControllerLogin(new ViewLogin(), new User());
        controllerLogin.open();
    }

}
