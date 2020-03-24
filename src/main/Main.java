/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import ui.controller.ControllerLogin;
import ui.view.ViewLogin;

/**
 *
 * @author remar
 */
public class Main {

    public static void main(String[] args) {
        ControllerLogin controllerLogin = new ControllerLogin(new ViewLogin());
        controllerLogin.open();
    }
    
    // JOptionPane da li ste sigurni da zelite da obrisete prozivod
    // Save Contract
    // ProductsTableModel izmena preko tabele; i za ContarctTableModel
}
