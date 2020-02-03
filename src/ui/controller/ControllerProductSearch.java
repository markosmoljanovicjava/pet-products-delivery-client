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
import java.util.List;
import ui.view.ProductsTableModel;
import ui.view.ViewProduct;
import ui.view.ViewProductMode;
import ui.view.ViewProductSearch;

/**
 *
 * @author remar
 */
public class ControllerProductSearch {

    private final ViewProductSearch viewProductSearch;
    private final List<Product> products;
    private ControllerProduct controllerProduct;

    public ControllerProductSearch(ViewProductSearch viewProductSearch) {
        this.viewProductSearch = viewProductSearch;
//        products = Controller.getInstance().getProducts();
//
        products = null;
//

        init();
        setListeners();
    }

    public void open() {
        viewProductSearch.setVisible(true);
    }

    private void close() {
        viewProductSearch.dispose();
    }

    private void init() {
        viewProductSearch.setLocationRelativeTo(null);
        viewProductSearch.setTitle("Search products");
        viewProductSearch.getjTableProducts().setModel(new ProductsTableModel(products));
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
//                            controllerProduct = new ControllerProduct(new ViewProduct(Controller.getInstance().getViewMain(), true), product, ViewProductMode.VIEW);
                            controllerProduct.open();
                        }
                    }
                }
            }
        });
        viewProductSearch.getjButtonRefreashTable().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                ProductsTableModel tableModel = (ProductsTableModel) viewProductSearch.getjTableProducts().getModel();
                tableModel.refreash();
            }
        });
    }
}
