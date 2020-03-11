/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.controller;

import controller.Controller;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import ui.view.ViewContract;
import ui.view.ViewMain;
import ui.view.ViewProduct;
import ui.view.ViewProductMode;
import ui.view.ViewProductSearch;
import util.Keys;

/**
 *
 * @author remar
 */
public class ControllerMain {

    private final ViewMain viewMain;
    private ControllerProduct controllerProduct;
    private ControllerProductSearch controllerProductSearch;
    private ControllerContract controllerContract;

    public ControllerMain(ViewMain viewMain) throws IOException {
        this.viewMain = viewMain;

        init();

        addListeners();
    }

    private void init() throws IOException {
        viewMain.setLocationRelativeTo(null);
        viewMain.setExtendedState(ViewMain.MAXIMIZED_BOTH);
        viewMain.setTitle(Controller.getInstance().getMap().get(Keys.USER).toString());
    }

    private void addListeners() {
        viewMain.getjMenuItemProductNew().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    controllerProduct = new ControllerProduct(new ViewProduct(viewMain, true), ViewProductMode.NEW);
                } catch (Exception ex) {
                    Logger.getLogger(ControllerMain.class.getName()).log(Level.SEVERE, null, ex);
                }
                controllerProduct.open();
            }
        });
        viewMain.getjMenuItemProductSearch().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    controllerProductSearch = new ControllerProductSearch(new ViewProductSearch(viewMain, true));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                controllerProductSearch.open();
            }
        });
        viewMain.getjMenuItemContractNew().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controllerContract = new ControllerContract(new ViewContract());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                controllerContract.open();
            }
        });
    }

    public void open() {
        viewMain.setVisible(true);
    }
}
