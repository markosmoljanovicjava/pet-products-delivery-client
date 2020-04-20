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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import ui.component.ProductsTableModel;
import ui.view.ViewProduct;
import ui.view.ViewProductMode;
import util.Keys;
import validator.impl.ProductNameRegexValidator;
import validator.impl.ProductPriceBigDecimalValidator;
import validator.util.RegexTuple;

/**
 *
 * @author remar
 */
public class ControllerProduct {

    private final ViewProduct viewProduct;
    private final Map<String, Boolean> validation;

    public ControllerProduct(ViewProduct viewProduct, ViewProductMode viewProductMode) throws Exception {
        this.viewProduct = viewProduct;

        setNamesForValidation();

        validation = new HashMap() {
            {
                put(viewProduct.getjTextFieldName().getName(), false);
                put(viewProduct.getjTextFieldPrice().getName(), false);
            }
        };

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

        AutoCompleteDecorator.decorate(viewProduct.getjComboBoxManufacturer());

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
                    JOptionPane.showMessageDialog(null, "Product is saved!");
                    init(ViewProductMode.VIEW);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        viewProduct.getjButtonEnableChanges().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    validation.put(viewProduct.getjTextFieldName().getName(), true);
                    validation.put(viewProduct.getjTextFieldPrice().getName(), true);
                    init(ViewProductMode.UPDATE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        viewProduct.getjButtonUpdate().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    update();
                    fillTable();
                    JOptionPane.showMessageDialog(null, "Product is updated!");
                    init(ViewProductMode.VIEW);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        viewProduct.getjButtonDelete().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int i = JOptionPane.showConfirmDialog(null, "Are you sure?");
                    if (i == 0) {
                        delete();
                        fillTable();
                        JOptionPane.showMessageDialog(null, "Product is deleted!");
                        close();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        viewProduct.getjTextFieldPrice().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                try {
                    new ProductPriceBigDecimalValidator().validate(viewProduct.getjTextFieldPrice().getText());
                    viewProduct.getjLabelErrorPrice().setText("");
                    validation.put(viewProduct.getjTextFieldPrice().getName(), true);
                    validate();
                } catch (Exception ex) {
                    viewProduct.getjLabelErrorPrice().setText(ex.getMessage());
                    validation.put(viewProduct.getjTextFieldPrice().getName(), false);
                    validate();
                }
            }
        });
        viewProduct.getjTextFieldName().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                try {
                    new ProductNameRegexValidator().validate(
                            new RegexTuple("^[A-Za-z0-9 _-]+$", viewProduct.getjTextFieldName().getText()));
                    viewProduct.getjLabelErrorName().setText("");
                    validation.put(viewProduct.getjTextFieldName().getName(), true);
                    validate();
                } catch (Exception ex) {
                    viewProduct.getjLabelErrorName().setText(ex.getMessage());
                    validation.put(viewProduct.getjTextFieldName().getName(), false);
                    validate();
                }
            }
        });
    }

    private void setMode(ViewProductMode viewProductMode) throws Exception {
        switch (viewProductMode) {
            case NEW:
                viewProduct.getjButtonDelete().setVisible(false);
                viewProduct.getjButtonUpdate().setVisible(false);
                viewProduct.getjButtonEnableChanges().setVisible(false);
                viewProduct.getjButtonSave().setVisible(true);
                viewProduct.getjButtonSave().setEnabled(false);
                viewProduct.getjTextFieldId().setEditable(false);
                viewProduct.getjTextFieldName().setEditable(true);
                viewProduct.getjTextFieldPrice().setEditable(true);
                viewProduct.getjComboBoxManufacturer().setEnabled(true);
                break;
            case VIEW:
                viewProduct.getjButtonDelete().setVisible(true);
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
                viewProduct.getjButtonUpdate().setEnabled(false);
                fillProduct();
                break;
        }
    }

    private void fillManufacturers() throws Exception {
        List<Manufacturer> manufacturers = Controller.getInstance().getAllManufacturers();
        viewProduct.getjComboBoxManufacturer().setModel(new DefaultComboBoxModel(manufacturers.toArray()));
    }

    private void fillProduct() throws IOException {
        Product product = (Product) Controller.getInstance().getMap().get(Keys.PRODUCT);
        viewProduct.getjTextFieldId().setText(product.getId().toString());
        viewProduct.getjTextFieldName().setText(product.getName());
        viewProduct.getjTextFieldPrice().setText(product.getPrice().toString());
        viewProduct.getjComboBoxManufacturer().setSelectedItem(product.getManufacturer());
    }

    private void save() throws Exception {
        Product product = new Product();
        product.setName(viewProduct.getjTextFieldName().getText());
        product.setPrice(new BigDecimal(viewProduct.getjTextFieldPrice().getText()));
        product.setManufacturer((Manufacturer) viewProduct.getjComboBoxManufacturer().getSelectedItem());
        Controller.getInstance().saveProduct(product);
    }

    private void update() throws Exception {
        Product product = new Product();
        product.setId(Long.parseLong(viewProduct.getjTextFieldId().getText()));
        product.setName(viewProduct.getjTextFieldName().getText());
        product.setPrice(new BigDecimal(viewProduct.getjTextFieldPrice().getText()));
        product.setManufacturer((Manufacturer) viewProduct.getjComboBoxManufacturer().getSelectedItem());
        Controller.getInstance().updateProduct(product);
    }

    private void delete() throws Exception {
        Product product = new Product();
        product.setId(Long.parseLong(viewProduct.getjTextFieldId().getText()));
        product.setName(viewProduct.getjTextFieldName().getText());
        product.setPrice(new BigDecimal(viewProduct.getjTextFieldPrice().getText()));
        product.setManufacturer((Manufacturer) viewProduct.getjComboBoxManufacturer().getSelectedItem());
        product.setId(Long.parseLong(viewProduct.getjTextFieldId().getText()));
        Controller.getInstance().deleteProduct(product);
    }

    private void fillTable() throws Exception {
        ProductsTableModel ptb = (ProductsTableModel) Controller.getInstance().getMap().get(Keys.PRODUCTS_TABLE_MODEL);
        if (ptb != null) {
            ptb.setProducts(Controller.getInstance().getAllProducts());
            ptb.refreash();
        }
    }

    private void setNamesForValidation() {
        viewProduct.getjTextFieldName().setName("Name");
        viewProduct.getjTextFieldPrice().setName("Price");
    }

    private void validate() {
        viewProduct.getjButtonSave().setEnabled(true);
        viewProduct.getjButtonUpdate().setEnabled(true);
        for (String key : validation.keySet()) {
            if (!validation.get(key)) {
                viewProduct.getjButtonSave().setEnabled(false);
                viewProduct.getjButtonUpdate().setEnabled(false);
                return;
            }
        }
    }
}
