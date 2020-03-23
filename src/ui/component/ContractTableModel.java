/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.component;

import domain.Contract;
import domain.ContractItem;
import domain.Product;
import java.math.BigDecimal;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author marko
 */
public class ContractTableModel extends AbstractTableModel {

    private final Contract contract;
    private final String[] colNames = {"Order No", "Product", "Price", "Quantity", "Total"};

    public ContractTableModel(Contract contract) {
        this.contract = contract;
    }

    @Override
    public int getRowCount() {
        return contract.getContractItems().size();
    }

    @Override
    public int getColumnCount() {
        return colNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ContractItem contractItem = contract.getContractItems().get(rowIndex);
        switch (columnIndex) {
            case 0:
                return contractItem.getItemNumber();
            case 1:
                return contractItem.getProduct().getName();
            case 2:
                return contractItem.getProduct().getPrice();
            case 3:
                return contractItem.getQuantity();
            case 4:
                return contractItem.getPrice().multiply(new BigDecimal(contractItem.getQuantity()));
            default:
                return "n/a";
        }
    }

    @Override
    public String getColumnName(int column) {
        return colNames[column];
    }

    public void addContractItem(Product product1, Long quantity) {
        ContractItem contractItem = new ContractItem();
        contractItem.setItemNumber(Long.valueOf(contract.getContractItems().size() + 1));
        contractItem.setProduct(product1);
        contractItem.setPrice(product1.getPrice());
        contractItem.setQuantity(quantity);
        contract.getContractItems().add(contractItem);
        contract.setAmount(contract.getAmount().add(product1.getPrice().multiply(new BigDecimal(contractItem.getQuantity()))));
        fireTableDataChanged();
    }

}
