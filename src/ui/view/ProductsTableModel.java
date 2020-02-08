/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.view;

import domain.Product;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author remar
 */
public class ProductsTableModel extends AbstractTableModel {

    private List<Product> products;
    String columnsNames[] = new String[]{"Id", "Name", "Price", "Manufacturer"};

    public ProductsTableModel(List<Product> products) {
        this.products = products;
    }

    @Override
    public int getRowCount() {
        return products.size();
    }

    @Override
    public int getColumnCount() {
        return columnsNames.length;
    }

    @Override
    public Object getValueAt(int i, int j) {
        Product product = products.get(i);
        switch (j) {
            case 0:
                return product.getId();
            case 1:
                return product.getName();
            case 2:
                return product.getPrice();
            case 3:
                return product.getManufacturer();
            default:
                return "N/A";
        }
    }

    @Override
    public String getColumnName(int i) {
        return columnsNames[i];
    }

    public void refreash() {
        fireTableDataChanged();
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

}
