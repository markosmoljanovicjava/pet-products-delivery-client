/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.controller;

import controller.Controller;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import ui.view.ViewContract;
import util.Keys;

/**
 *
 * @author marko
 */
public class ControllerContract {

    private final ViewContract viewContract;

    public ControllerContract(ViewContract viewContract) throws IOException {
        this.viewContract = viewContract;

        init();

        addListeners();
    }

    private void init() throws IOException {
        viewContract.setLocationRelativeTo(null);
        viewContract.setTitle("Contract");
        
        fillForm();
    }

    private void addListeners() {
    }

    public void open() {
        viewContract.setVisible(true);
    }

    public void close() {
        viewContract.dispose();
    }

    private void fillForm() throws IOException {
        viewContract.getjTextFieldUser().setText(Controller.getInstance().getMap().get(Keys.USER).toString());
        
        Calendar calendar = Calendar.getInstance();
        viewContract.getjTextFieldDateCreated().setText(new SimpleDateFormat("yyyy.MM.dd.").format(calendar.getTime()));
        calendar.add(Calendar.YEAR, 1);
        viewContract.getjTextFieldDateExpiration().setText(new SimpleDateFormat("yyyy.MM.dd.").format(calendar.getTime()));
    }
}
