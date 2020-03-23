/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.controller;

import controller.Controller;
import domain.Contract;
import domain.Customer;
import domain.Manufacturer;
import domain.Product;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import ui.component.ContractTableModel;
import ui.component.ProductsTableModel;
import ui.view.ViewContract;
import util.Keys;

/**
 *
 * @author marko
 */
public class ControllerContract {

    private final ViewContract viewContract;
    private Product product;

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
        viewContract.getjComboBoxManufacturer().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                product = new Product();
                product.setManufacturer((Manufacturer) viewContract.getjComboBoxManufacturer().getSelectedItem());
                try {
                    fillProducts();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        viewContract.getjTableProducts().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                ProductsTableModel ptm = (ProductsTableModel) viewContract.getjTableProducts().getModel();
                int rowIndex = viewContract.getjTableProducts().getSelectedRow();
                if (rowIndex >= 0) {
                    Product product1 = ptm.getProduct(rowIndex);
                    viewContract.getjTextFieldProductID().setText(product1.getId().toString());
                    viewContract.getjTextFieldProductName().setText(product1.getName());
                    viewContract.getjTextFieldProductPrice().setText(product1.getPrice().toString());
                    viewContract.getjTextFieldQuantity().setText("1");
                    viewContract.getjTextFieldQuantity().grabFocus();
                    viewContract.getjTextFieldQuantity().setSelectionStart(0);
                }
            }
        });
        viewContract.getjButtonAddItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ProductsTableModel ptm = (ProductsTableModel) viewContract.getjTableProducts().getModel();
                int rowIndex = viewContract.getjTableProducts().getSelectedRow();
                if (rowIndex >= 0) {
                    ContractTableModel ctm = (ContractTableModel) viewContract.getjTableContractItems().getModel();
                    Product product1 = ptm.getProduct(rowIndex);
                    Long quantity = Long.valueOf(viewContract.getjTextFieldQuantity().getText().trim());
                    ctm.addContractItem(product1, quantity);
                    
                }
            }
        });
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
        fillManufacturers();
        product = new Product();
        product.setManufacturer((Manufacturer) viewContract.getjComboBoxManufacturer().getSelectedItem());
        fillProducts();
        fillContractTable();
    }

    private void fillCustomers() throws Exception {
        List<Customer> customers = Controller.getInstance().getAllCustomers();
        viewContract.getjComboBoxCustomer().setModel(new DefaultComboBoxModel(customers.toArray()));
    }

    private void fillProducts() throws Exception {
        List<Product> products = Controller.getInstance().getAllProductsForManufacturer(product);
        viewContract.getjTableProducts().setModel(new ProductsTableModel(products));
    }

    private void fillManufacturers() throws Exception {
        List<Manufacturer> manufacturers = Controller.getInstance().getAllManufacturers();
        viewContract.getjComboBoxManufacturer().setModel(new DefaultComboBoxModel(manufacturers.toArray()));
    }

    private void fillContractTable() {
        viewContract.getjTableContractItems().setModel(new ContractTableModel(new Contract()));
    }
}
