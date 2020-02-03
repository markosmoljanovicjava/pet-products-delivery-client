/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.controller;

import controller.Controller;
import domain.Manufacturer;
import domain.Product;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import ui.view.ViewProduct;
import ui.view.ViewProductMode;

/**
 *
 * @author remar
 */
public class ControllerProduct {

    private final ViewProduct viewProduct;
    private Product product;

    public ControllerProduct(ViewProduct viewProduct, Product product, ViewProductMode viewProductMode) throws Exception {
        this.viewProduct = viewProduct;
        this.product = product;

        init(viewProductMode);

        addListeners();
    }

    public void open() {
        viewProduct.setVisible(true);
    }

    private void close() {
        viewProduct.dispose();
    }

    private void init(ViewProductMode viewProductMode) throws Exception {
        viewProduct.setLocationRelativeTo(null);
        viewProduct.setTitle("Product");

        fillManufacturers();

        setMode(viewProductMode);
    }

    private void addListeners() {
        viewProduct.getjButtonCancel().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                close();
            }
        });
        viewProduct.getjButtonSave().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    save();
                    init(ViewProductMode.VIEW);
                } catch (Exception ex) {
                    Logger.getLogger(ControllerProduct.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        viewProduct.getjButtonEnableChanges().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    init(ViewProductMode.UPDATE);
                } catch (Exception ex) {
                    Logger.getLogger(ControllerProduct.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        viewProduct.getjButtonUpdate().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                update();
                try {
                    init(ViewProductMode.VIEW);
                } catch (Exception ex) {
                    Logger.getLogger(ControllerProduct.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    private void setMode(ViewProductMode viewProductMode) {
        switch (viewProductMode) {
            case NEW:
                viewProduct.getjButtonDelete().setVisible(false);
                viewProduct.getjButtonUpdate().setVisible(false);
                viewProduct.getjButtonEnableChanges().setVisible(false);
                viewProduct.getjButtonSave().setVisible(true);
                viewProduct.getjTextFieldId().setEditable(true);
                viewProduct.getjTextFieldName().setEditable(true);
                viewProduct.getjTextFieldPrice().setEditable(true);
                viewProduct.getjComboBoxManufacturer().setEnabled(true);
                break;
            case VIEW:
                viewProduct.getjButtonDelete().setVisible(false);
                viewProduct.getjButtonUpdate().setVisible(false);
                viewProduct.getjButtonSave().setVisible(false);
                viewProduct.getjButtonEnableChanges().setVisible(true);
                viewProduct.getjTextFieldId().setEditable(false);
                viewProduct.getjTextFieldName().setEditable(false);
                viewProduct.getjTextFieldPrice().setEditable(false);
                viewProduct.getjComboBoxManufacturer().setEnabled(false);
                fillProduct();
                break;
            case UPDATE:
                viewProduct.getjButtonDelete().setVisible(false);
                viewProduct.getjButtonUpdate().setVisible(true);
                viewProduct.getjButtonEnableChanges().setVisible(false);
                viewProduct.getjButtonSave().setVisible(false);
                viewProduct.getjTextFieldId().setEditable(false);
                viewProduct.getjTextFieldName().setEditable(true);
                viewProduct.getjTextFieldPrice().setEditable(true);
                viewProduct.getjComboBoxManufacturer().setEnabled(true);
                break;
        }
    }

    private void fillManufacturers() throws Exception {
        List<Manufacturer> manufacturers = Controller.getInstance().getManufacturers();
        viewProduct.getjComboBoxManufacturer().setModel(new DefaultComboBoxModel(manufacturers.toArray()));
    }

    private void fillProduct() {
        viewProduct.getjTextFieldId().setText(product.getId().toString());
        viewProduct.getjTextFieldName().setText(product.getName());
        viewProduct.getjTextFieldPrice().setText(product.getPrice().toString());
        viewProduct.getjComboBoxManufacturer().setSelectedItem(product.getManufacturer());
    }

    private void save() throws IOException {
        product = new Product();
        product.setId(Long.parseLong(viewProduct.getjTextFieldId().getText()));
        product.setName(viewProduct.getjTextFieldName().getText());
        product.setPrice(new BigDecimal(viewProduct.getjTextFieldPrice().getText()));
        product.setManufacturer((Manufacturer) viewProduct.getjComboBoxManufacturer().getSelectedItem());
        Controller.getInstance().saveProduct(product);
    }

    private void update() {
//        product = new Product();
//        product.setId(Long.parseLong(viewProduct.getjTextFieldId().getText()));
//        product.setName(viewProduct.getjTextFieldName().getText());
//        product.setPrice(new BigDecimal(viewProduct.getjTextFieldPrice().getText()));
//        product.setManufacturer((Manufacturer) viewProduct.getjComboBoxManufacturer().getSelectedItem());
//        Controller.getInstance().updateProduct(product);
    }
}
