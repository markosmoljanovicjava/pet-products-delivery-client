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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import ui.component.ProductsTableModel;
import ui.view.ViewProduct;
import ui.view.ViewProductMode;
import ui.view.ViewProductSearch;
import util.Keys;
import validator.impl.ProductIdPositiveLongValidator;
import validator.impl.ProductNameRegexValidator;
import validator.impl.ProductPriceBigDecimalValidator;
import validator.util.RegexTuple;

/**
 *
 * @author remar
 */
public class ControllerProductSearch {

    private final ViewProductSearch viewProductSearch;
    private final List<Product> products;
    private ControllerProduct controllerProduct;
    private final Map<String, Boolean> validation;

    public ControllerProductSearch(ViewProductSearch viewProductSearch) throws Exception {
        this.viewProductSearch = viewProductSearch;
        products = Controller.getInstance().getAllProducts();

        setNamesForValidation();
        validation = new HashMap() {
            {
                put(viewProductSearch.getjTextFieldId().getName(), true);
                put(viewProductSearch.getjTextFieldName().getName(), true);
                put(viewProductSearch.getjTextFieldPrice().getName(), true);
            }
        };

        AutoCompleteDecorator.decorate(viewProductSearch.getjComboBoxManufacturer());

        init();

        setListeners();
    }

    public void open() {
        viewProductSearch.setVisible(true);
    }

    private void close() {
        viewProductSearch.dispose();
    }

    private void init() throws Exception {
        viewProductSearch.setLocationRelativeTo(null);
        viewProductSearch.setTitle("Search products");
        fillManufacturers();
        viewProductSearch.getjTableProducts().setModel(new ProductsTableModel(products));
        Controller.getInstance().getMap().put(Keys.PRODUCTS_TABLE_MODEL, viewProductSearch.getjTableProducts().getModel());
    }

    private void setListeners() {
        viewProductSearch.getjButtonCancel().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                close();
            }
        });
        viewProductSearch.getjButtonDetails().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                int i = viewProductSearch.getjTableProducts().getSelectedRow();
                if (i != -1) {
                    Long id = (Long) viewProductSearch.getjTableProducts().getValueAt(i, 0);
                    for (Product product : products) {
                        if (product.getId().equals(id)) {
                            try {
                                Controller.getInstance().getMap().put(Keys.PRODUCT, product);
                                controllerProduct = new ControllerProduct(
                                        new ViewProduct(null, true), ViewProductMode.VIEW);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            controllerProduct.open();
                        }
                    }
                }
            }
        });
        viewProductSearch.getjTextFieldId().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (viewProductSearch.getjTextFieldId().getText().isEmpty()) {
                    viewProductSearch.getjLabelErrorId().setText("");
                    validation.put(viewProductSearch.getjTextFieldId().getName(), true);
                    validate();
                    return;
                }
                try {
                    new ProductIdPositiveLongValidator().validate(viewProductSearch.getjTextFieldId().getText());
                    viewProductSearch.getjLabelErrorId().setText("");
                    validation.put(viewProductSearch.getjTextFieldId().getName(), true);
                    validate();
                } catch (Exception ex) {
                    viewProductSearch.getjLabelErrorId().setText(ex.getMessage());
                    validation.put(viewProductSearch.getjTextFieldId().getName(), false);
                    validate();
                }
            }
        });
        viewProductSearch.getjTextFieldPrice().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (viewProductSearch.getjTextFieldPrice().getText().isEmpty()) {
                    viewProductSearch.getjLabelErrorPrice().setText("");
                    validation.put(viewProductSearch.getjTextFieldPrice().getName(), true);
                    validate();
                    return;
                }
                try {
                    new ProductPriceBigDecimalValidator().validate(viewProductSearch.getjTextFieldPrice().getText());
                    viewProductSearch.getjLabelErrorPrice().setText("");
                    validation.put(viewProductSearch.getjTextFieldPrice().getName(), true);
                    validate();
                } catch (Exception ex) {
                    viewProductSearch.getjLabelErrorPrice().setText(ex.getMessage());
                    validation.put(viewProductSearch.getjTextFieldPrice().getName(), false);
                    validate();
                }
            }
        });
        viewProductSearch.getjTextFieldName().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (viewProductSearch.getjTextFieldName().getText().isEmpty()) {
                    viewProductSearch.getjLabelErrorName().setText("");
                    validation.put(viewProductSearch.getjTextFieldName().getName(), true);
                    validate();
                    return;
                }
                try {
                    new ProductNameRegexValidator().validate(
                            new RegexTuple("^[A-Za-z0-9 _-]+$", viewProductSearch.getjTextFieldName().getText()));
                    viewProductSearch.getjLabelErrorName().setText("");
                    validation.put(viewProductSearch.getjTextFieldName().getName(), true);
                    validate();
                } catch (Exception ex) {
                    viewProductSearch.getjLabelErrorName().setText(ex.getMessage());
                    validation.put(viewProductSearch.getjTextFieldName().getName(), false);
                    validate();
                }
            }
        });
        viewProductSearch.getjButtonFilter().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ProductsTableModel ptm = (ProductsTableModel) Controller.getInstance().getMap().get(Keys.PRODUCTS_TABLE_MODEL);
                    ptm.setProducts(Controller.getInstance().getSearchedProducts(new Product()));
                    ptm.refreash();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void setNamesForValidation() {
        viewProductSearch.getjTextFieldId().setName("Id");
        viewProductSearch.getjTextFieldName().setName("Name");
        viewProductSearch.getjTextFieldPrice().setName("Price");
    }

    private void validate() {
        viewProductSearch.getjButtonFilter().setEnabled(true);
        for (String key : validation.keySet()) {
            if (!validation.get(key)) {
                viewProductSearch.getjButtonFilter().setEnabled(false);
                return;
            }
        }
    }

    private void fillManufacturers() throws Exception {
        List<Manufacturer> manufacturers = Controller.getInstance().getAllManufacturers();
        viewProductSearch.getjComboBoxManufacturer().setModel(new DefaultComboBoxModel(manufacturers.toArray()));
    }
}
