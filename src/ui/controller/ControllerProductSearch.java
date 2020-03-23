/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.controller;

import controller.Controller;
import domain.Product;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import ui.component.ProductsTableModel;
import ui.view.ViewProduct;
import ui.view.ViewProductMode;
import ui.view.ViewProductSearch;
import util.Keys;

/**
 *
 * @author remar
 */
public class ControllerProductSearch {

    private final ViewProductSearch viewProductSearch;
    private final List<Product> products;
    private ControllerProduct controllerProduct;

    public ControllerProductSearch(ViewProductSearch viewProductSearch) throws Exception {
        this.viewProductSearch = viewProductSearch;
        products = Controller.getInstance().getAllProducts();

        init();

        setListeners();
    }

    public void open() {
        viewProductSearch.setVisible(true);
    }

    private void close() {
        viewProductSearch.dispose();
    }

    private void init() throws IOException {
        viewProductSearch.setLocationRelativeTo(null);
        viewProductSearch.setTitle("Search products");
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
    }
}
