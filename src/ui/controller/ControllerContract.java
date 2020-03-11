/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.controller;

import controller.Controller;
import domain.Customer;
import domain.Product;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.TableModel;
import ui.view.ProductsTableModel;
import ui.view.ViewContract;
import util.Keys;

/**
 *
 * @author marko
 */
public class ControllerContract {

    private final ViewContract viewContract;

    public ControllerContract(ViewContract viewContract) throws Exception {
        this.viewContract = viewContract;

        init();

        addListeners();
    }

    private void init() throws Exception {
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

    private void fillForm() throws Exception {
        viewContract.getjTextFieldUser().setText(Controller.getInstance().getMap().get(Keys.USER).toString());

        Calendar calendar = Calendar.getInstance();
        viewContract.getjTextFieldDateCreated().setText(new SimpleDateFormat("yyyy.MM.dd.").format(calendar.getTime()));
        calendar.add(Calendar.YEAR, 1);
        viewContract.getjTextFieldDateExpiration().setText(new SimpleDateFormat("yyyy.MM.dd.").format(calendar.getTime()));

        fillCustomers();
        fillProducts();
    }

    private void fillCustomers() throws Exception {
        List<Customer> customers = Controller.getInstance().getAllCustomers();
        viewContract.getjComboBoxCustomer().setModel(new DefaultComboBoxModel(customers.toArray()));
    }

    private void fillProducts() throws Exception {
        List<Product> products = Controller.getInstance().getAllProducts();
        viewContract.getjTableProducts().setModel(new ProductsTableModel(products));
    }
}
