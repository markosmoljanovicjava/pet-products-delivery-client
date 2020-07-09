/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.controller;

import controller.Controller;
import domain.User;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import thread.ThreadConnection;
import thread.ThreadLogin;
import ui.view.ViewLogin;
import ui.view.ViewMain;

/**
 *
 * @author remar
 */
public class ControllerLogin {

    private final ViewLogin viewLogin;
    private final ThreadLogin threadLogin;

    public ControllerLogin(ViewLogin viewLogin) {
        this.viewLogin = viewLogin;
        threadLogin = new ThreadLogin(viewLogin);

        init();

        addListeners();
    }

    public void open() {
        threadLogin.start();
        viewLogin.setVisible(true);
    }

    private void close() {
        viewLogin.dispose();
    }

    private void addListeners() {
        viewLogin.getjButtonLogin().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                login();
            }
        });
        viewLogin.getjButtonCancel().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                System.exit(0);
            }
        });
        viewLogin.getjTextFieldUsername().addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent fe) {
                viewLogin.getjTextFieldUsername().setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                viewLogin.getjPasswordFieldPassword().setBorder(new JTextField().getBorder());
            }

            @Override
            public void focusLost(FocusEvent fe) {
                viewLogin.getjTextFieldUsername().setBorder(new JTextField().getBorder());
            }
        });
        viewLogin.getjPasswordFieldPassword().addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent fe) {
                viewLogin.getjPasswordFieldPassword().setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                viewLogin.getjTextFieldUsername().setBorder(new JTextField().getBorder());
            }

            @Override
            public void focusLost(FocusEvent fe) {
                viewLogin.getjPasswordFieldPassword().setBorder(new JTextField().getBorder());
            }
        });
        viewLogin.getjCheckBoxShowPassword().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (viewLogin.getjCheckBoxShowPassword().isSelected()) {
                    viewLogin.getjPasswordFieldPassword().setEchoChar((char) 0);
                } else {
                    viewLogin.getjPasswordFieldPassword().setEchoChar(new JPasswordField().getEchoChar());
                }
            }
        });
    }

    private void login() {
        User user = new User();
        user.setUsername(viewLogin.getjTextFieldUsername().getText());
        user.setPassword(String.valueOf(viewLogin.getjPasswordFieldPassword().getPassword()));
        try {
            Controller.getInstance().login(user);
            threadLogin.interrupt();
            close();
            new ControllerMain(new ViewMain()).open();
            new ThreadConnection().start();
        } catch (Exception ex) {
            System.out.println(ex.toString());
            if (ex.getMessage().equals("Your login credentials don't match an account in our system.")) {
                viewLogin.getjLabelError().setText(ex.getMessage());
                viewLogin.getjTextFieldUsername().setBorder(BorderFactory.createLineBorder(Color.RED, 1));
                viewLogin.getjPasswordFieldPassword().setBorder(BorderFactory.createLineBorder(Color.RED, 1));
            } else {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        }
    }

    private void init() {
        viewLogin.setLocationRelativeTo(null);
    }
}
