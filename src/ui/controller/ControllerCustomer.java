/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.controller;

import controller.Controller;
import domain.Customer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import ui.view.ViewCustomer;

/**
 *
 * @author marko
 */
public class ControllerCustomer {

    private final ViewCustomer viewCustomer;

    public ControllerCustomer(ViewCustomer viewCustomer) {
        this.viewCustomer = viewCustomer;

        init();

        addListeners();
    }

    private void init() {
        viewCustomer.setLocationRelativeTo(null);
    }

    void open() {
        viewCustomer.setVisible(true);
    }

    private void addListeners() {
        viewCustomer.getjButtonRegister().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Customer customer = new Customer();
                    customer.setFirstName(viewCustomer.getjTextFieldFirstName().getText());
                    customer.setLastName(viewCustomer.getjTextFieldLastName().getText());
                    customer.setPhoneNumber(viewCustomer.getjTextFieldPhoneNumber().getText());
                    customer.setAdress(viewCustomer.getjTextFieldAdress().getText());
                    Controller.getInstance().registerCustomer(customer);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

}
