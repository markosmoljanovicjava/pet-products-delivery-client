/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import domain.Contract;
import domain.Customer;
import domain.Manufacturer;
import domain.Product;
import domain.User;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import transfer.RequestObject;
import transfer.ResponseObject;
import util.Keys;
import util.Operation;
import util.ResponseStatus;

/**
 *
 * @author marko
 */
public class Controller {

    private static Controller instance;
    private final Map<Integer, Object> map;
    private final Socket socket;
    private final ObjectOutputStream objectOutputStream;
    private final ObjectInputStream objectInputStream;

    private Controller() throws IOException {
        map = new HashMap<>();
        socket = new Socket("localhost", 9000);
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectInputStream = new ObjectInputStream(socket.getInputStream());
    }

    public static Controller getInstance() throws IOException {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    public Map<Integer, Object> getMap() {
        return map;
    }

    public void login(User user) throws Exception {
        RequestObject requestObject = new RequestObject(Operation.LOGIN, user);
        objectOutputStream.writeObject(requestObject);
        objectOutputStream.flush();

        ResponseObject responseObject = (ResponseObject) objectInputStream.readObject();

        ResponseStatus status = responseObject.getStatus();
        if (status == ResponseStatus.SUCCESS) {
            map.put(Keys.USER, responseObject.getData());
        } else {
            throw new Exception(responseObject.getErrorMessage());
        }
    }

    public Product saveProduct(Product product) throws Exception {
        RequestObject requestObject = new RequestObject(Operation.SAVE_PRODUCT, product);
        objectOutputStream.writeObject(requestObject);
        objectOutputStream.flush();

        ResponseObject responseObject = (ResponseObject) objectInputStream.readObject();
        if (responseObject.getStatus().equals(ResponseStatus.SUCCESS)) {
            Product product1 = (Product) responseObject.getData();
            this.map.put(Keys.PRODUCT, product1);
            return product1;
        }
        throw new Exception(responseObject.getErrorMessage());
    }

    public List<Manufacturer> getAllManufacturers() throws Exception {
        RequestObject requestObject = new RequestObject(Operation.GET_ALL_MANUFACTURERS);
        objectOutputStream.writeObject(requestObject);
        objectOutputStream.flush();

        ResponseObject responseObject = (ResponseObject) objectInputStream.readObject();
        if (responseObject.getStatus().equals(ResponseStatus.SUCCESS)) {
            return (List<Manufacturer>) responseObject.getData();
        }
        throw new Exception(responseObject.getErrorMessage());
    }

    public Product updateProduct(Product product) throws Exception {
        RequestObject requestObject = new RequestObject(Operation.UPDATE_PRODUCT, product);
        objectOutputStream.writeObject(requestObject);
        objectOutputStream.flush();

        ResponseObject responseObject = (ResponseObject) objectInputStream.readObject();
        if (responseObject.getStatus().equals(ResponseStatus.SUCCESS)) {
            Product product1 = (Product) responseObject.getData();
            this.map.put(Keys.PRODUCT, product1);
            return product1;
        }
        throw new Exception(responseObject.getErrorMessage());
    }

    public List<Product> getAllProducts() throws Exception {
        RequestObject requestObject = new RequestObject(Operation.GET_ALL_PRODUCTS);
        objectOutputStream.writeObject(requestObject);
        objectOutputStream.flush();

        ResponseObject responseObject = (ResponseObject) objectInputStream.readObject();
        if (responseObject.getStatus().equals(ResponseStatus.SUCCESS)) {
            return (List<Product>) responseObject.getData();
        }
        throw new Exception(responseObject.getErrorMessage());
    }

    public Product deleteProduct(Product product) throws Exception {
        RequestObject requestObject = new RequestObject(Operation.DELETE_PRODUCT, product);
        objectOutputStream.writeObject(requestObject);
        objectOutputStream.flush();

        ResponseObject responseObject = (ResponseObject) objectInputStream.readObject();
        if (responseObject.getStatus().equals(ResponseStatus.SUCCESS)) {
            Product product1 = (Product) responseObject.getData();
            return product1;
        }
        throw new Exception(responseObject.getErrorMessage());
    }

    public List<Customer> getAllCustomers() throws Exception {
        RequestObject requestObject = new RequestObject(Operation.GET_ALL_CUSTOMERS);
        objectOutputStream.writeObject(requestObject);
        objectOutputStream.flush();

        ResponseObject responseObject = (ResponseObject) objectInputStream.readObject();
        if (responseObject.getStatus().equals(ResponseStatus.SUCCESS)) {
            return (List<Customer>) responseObject.getData();
        }
        throw new Exception(responseObject.getErrorMessage());
    }

    public List<Product> getAllProductsForManufacturer(Product product) throws Exception {
        RequestObject requestObject = new RequestObject(Operation.GET_ALL_PRODUCST_FOR_MANUFACTURER, product);
        objectOutputStream.writeObject(requestObject);
        objectOutputStream.flush();

        ResponseObject responseObject = (ResponseObject) objectInputStream.readObject();
        if (responseObject.getStatus().equals(ResponseStatus.SUCCESS)) {
            return (List<Product>) responseObject.getData();
        }
        throw new Exception(responseObject.getErrorMessage());
    }

    public Contract saveContract(Contract contract) throws Exception {
        RequestObject requestObject = new RequestObject(Operation.SAVE_CONTRACT, contract);
        objectOutputStream.writeObject(requestObject);
        objectOutputStream.flush();

        ResponseObject responseObject = (ResponseObject) objectInputStream.readObject();
        if (responseObject.getStatus().equals(ResponseStatus.SUCCESS)) {
            Contract contract1 = (Contract) responseObject.getData();
            return contract1;
        }
        throw new Exception(responseObject.getErrorMessage());
    }

}
