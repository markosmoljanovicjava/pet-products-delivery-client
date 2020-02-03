/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thread;

import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import ui.view.ViewLogin;

/**
 *
 * @author remar
 */
public class ThreadLogin extends Thread {

    private final ViewLogin viewLogin;

    public ThreadLogin(ViewLogin viewLogin) {
        this.viewLogin = viewLogin;
    }

    @Override
    public void run() {
        JTextField jTextFieldUsername = viewLogin.getjTextFieldUsername();
        JPasswordField jPasswordFieldPassword = viewLogin.getjPasswordFieldPassword();
        JButton jButtonLogin = viewLogin.getjButtonLogin();
        while (!isInterrupted()) {
            if (jTextFieldUsername.getText().isEmpty()
                    || String.valueOf(jPasswordFieldPassword.getPassword()).isEmpty()) {
                jButtonLogin.setEnabled(false);
            } else {
                jButtonLogin.setEnabled(true);
            }
            try {
                sleep(50);
            } catch (InterruptedException ex) {
                interrupt();
            }
        }
    }
}

// zasto mi ne radi bez sleep();
