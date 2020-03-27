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
    private final String[] colNames = {"Order No", "Product", "Price", "Quantity", "Amount"};

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
                return contractItem.getAmount();
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
        for (ContractItem contractItem1 : contract.getContractItems()) {
            if (contractItem1.equals(contractItem)) {
                contractItem1.setQuantity(contractItem1.getQuantity() + quantity);
                contractItem1.setAmount(contractItem.getPrice().multiply(new BigDecimal(contractItem.getQuantity())));
                setContractAmount();
                fireTableDataChanged();
                return;
            }
        }
        contractItem.setQuantity(quantity);
        contractItem.setAmount(contractItem.getPrice().multiply(new BigDecimal(contractItem.getQuantity())));
        contract.getContractItems().add(contractItem);
        setContractAmount();
        fireTableDataChanged();
    }

    public void removeContractItem(int rowIndex) {
        contract.getContractItems().remove(rowIndex);
        setItemNumbers();
        setContractAmount();
        fireTableDataChanged();
    }

    private void setItemNumbers() {
        int i = 0;
        for (ContractItem contractItem : contract.getContractItems()) {
            contractItem.setItemNumber(Long.valueOf(++i));
        }
    }

    public Contract getContract() {
        return contract;
    }

    private void setContractAmount() {
        contract.setAmount(new BigDecimal(0));
        for (ContractItem contractItem : contract.getContractItems()) {
            contract.setAmount(contract.getAmount().add(contractItem.getAmount()));
        }
    }

}
