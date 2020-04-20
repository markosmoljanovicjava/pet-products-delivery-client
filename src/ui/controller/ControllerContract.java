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
import domain.User;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import ui.component.ContractTableModel;
import ui.component.ProductsTableModel;
import ui.view.ViewContract;
import util.Keys;
import validator.impl.ProductQuantityPositiveLongValidator;

/**
 *
 * @author marko
 */
public class ControllerContract {

    private final ViewContract viewContract;
    private Product product;

    public ControllerContract(ViewContract viewContract) throws Exception {
        this.viewContract = viewContract;

        product = new Product();

        init();

        addListeners();
    }

    private void init() throws Exception {
        viewContract.setLocationRelativeTo(null);
        viewContract.setTitle("Contract");

        prepareForm();
        fillForm();
    }

    private void addListeners() {
        viewContract.getjComboBoxManufacturer().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ContractTableModel ctm = (ContractTableModel) viewContract.getjTableContractItems().getModel();
                int i = 0;
                if (!ctm.getContract().getContractItems().isEmpty()) {
                    i = JOptionPane.showConfirmDialog(null, "Are u sure? Products u already selected will be lost!");
                }
                if (i == 0) {
                    viewContract.getjTableContractItems().setModel(new ContractTableModel(new Contract()));
                    product = new Product();
                    product.setManufacturer((Manufacturer) viewContract.getjComboBoxManufacturer().getSelectedItem());
                    try {
                        fillProducts();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    viewContract.getjTextFieldProductID().setText("");
                    viewContract.getjTextFieldProductName().setText("");
                    viewContract.getjTextFieldProductPrice().setText("");
                    viewContract.getjTextFieldQuantity().setText("");
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
                try {
                    ProductsTableModel ptm = (ProductsTableModel) viewContract.getjTableProducts().getModel();
                    int rowIndex = viewContract.getjTableProducts().getSelectedRow();
                    if (rowIndex >= 0) {
                        ContractTableModel ctm = (ContractTableModel) viewContract.getjTableContractItems().getModel();
                        Product product1 = ptm.getProduct(rowIndex);
                        Long quantity = Long.valueOf(viewContract.getjTextFieldQuantity().getText().trim());
                        ctm.addContractItem(product1, quantity);
                        viewContract.getjTextFieldAmount().setText(ctm.getContract().getAmount().toString());
                    } else {
                        JOptionPane.showMessageDialog(null, "Please select product!");
                    }
                } catch (Exception ex) {
                    // (1)
                    JOptionPane.showMessageDialog(null, "You must to click on desired product!");
                }
            }
        });
        viewContract.getjButtonRemoveItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ContractTableModel ctm = (ContractTableModel) viewContract.getjTableContractItems().getModel();
                int rowIndex = viewContract.getjTableContractItems().getSelectedRow();
                if (rowIndex >= 0) {
                    ctm.removeContractItem(rowIndex);
                    viewContract.getjTextFieldAmount().setText(ctm.getContract().getAmount().toString());
                } else {
                    JOptionPane.showMessageDialog(null, "Please select Contract Item!");
                }
            }
        });
        viewContract.getjButtonSave().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ContractTableModel ctm = (ContractTableModel) viewContract.getjTableContractItems().getModel();
                if (ctm.getContract().getContractItems().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "U can not save contract with 0 items!");
                    return;
                }
                Contract contract = ctm.getContract();
                contract.setAmount(new BigDecimal(viewContract.getjTextFieldAmount().getText()));
                contract.setCustomer((Customer) viewContract.getjComboBoxCustomer().getSelectedItem());
                try {
                    contract.setDateCreation(new SimpleDateFormat("yyyy.MM.dd.").parse(viewContract.getjTextFieldDateCreated().getText()));
                    contract.setDateExpiration(new SimpleDateFormat("yyyy.MM.dd.").parse(viewContract.getjTextFieldDateExpiration().getText()));
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
                try {
                    contract.setUser((User) Controller.getInstance().getMap().get(Keys.USER));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                try {
                    Contract contract1 = Controller.getInstance().saveContract(contract);
                    JOptionPane.showMessageDialog(null, "Contract is saved!");
                    viewContract.dispose();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        viewContract.getjTextFieldQuantity().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                try {
                    new ProductQuantityPositiveLongValidator().validate(viewContract.getjTextFieldQuantity().getText());
                    viewContract.getjLabelErrorQuantity().setText("");
                    viewContract.getjButtonAddItem().setEnabled(true);
                } catch (Exception ex) {
                    viewContract.getjLabelErrorQuantity().setText(ex.getMessage());
                    viewContract.getjButtonAddItem().setEnabled(false);
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

    private void prepareForm() {
        viewContract.getjTextFieldAmount().setText("0");
        viewContract.getjTextFieldProductID().setEditable(false);
        viewContract.getjTextFieldProductName().setEditable(false);
        viewContract.getjTextFieldProductPrice().setEditable(false);
    }
}
