/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import domain.Contract;
import domain.Customer;
import domain.DomainObject;
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
    private final Object lock;

    private Controller() throws IOException {
        map = new HashMap<>();
        socket = new Socket("localhost", 9000);
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectInputStream = new ObjectInputStream(socket.getInputStream());
        lock = new Object();
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

    public Socket getSocket() {
        return socket;
    }

    public ObjectOutputStream getObjectOutputStream() {
        return objectOutputStream;
    }

    public ObjectInputStream getObjectInputStream() {
        return objectInputStream;
    }

    public Object getLock() {
        return lock;
    }

    public void login(User user) throws Exception {
        ResponseObject responseObject = communication(Operation.LOGIN, user);
        ResponseStatus status = responseObject.getStatus();
        if (status == ResponseStatus.SUCCESS) {
            map.put(Keys.USER, responseObject.getData());
        } else {
            throw new Exception(responseObject.getErrorMessage());
        }
    }

    public Product saveProduct(Product product) throws Exception {
        ResponseObject responseObject = communication(Operation.SAVE_PRODUCT, product);
        if (responseObject.getStatus().equals(ResponseStatus.SUCCESS)) {
            Product product1 = (Product) responseObject.getData();
            this.map.put(Keys.PRODUCT, product1);
            return product1;
        }
        throw new Exception(responseObject.getErrorMessage());
    }

    public List<Manufacturer> getAllManufacturers() throws Exception {
        ResponseObject responseObject = communication(Operation.GET_ALL_MANUFACTURERS, null);
        if (responseObject.getStatus().equals(ResponseStatus.SUCCESS)) {
            return (List<Manufacturer>) responseObject.getData();
        }
        throw new Exception(responseObject.getErrorMessage());
    }

    public Product updateProduct(Product product) throws Exception {
        ResponseObject responseObject = communication(Operation.UPDATE_PRODUCT, product);
        if (responseObject.getStatus().equals(ResponseStatus.SUCCESS)) {
            Product product1 = (Product) responseObject.getData();
            this.map.put(Keys.PRODUCT, product1);
            return product1;
        }
        throw new Exception(responseObject.getErrorMessage());
    }

    public List<Product> getAllProducts() throws Exception {
        ResponseObject responseObject = communication(Operation.GET_ALL_PRODUCTS, null);
        if (responseObject.getStatus().equals(ResponseStatus.SUCCESS)) {
            return (List<Product>) responseObject.getData();
        }
        throw new Exception(responseObject.getErrorMessage());
    }

    public Product deleteProduct(Product product) throws Exception {
        ResponseObject responseObject = communication(Operation.DELETE_PRODUCT, product);
        if (responseObject.getStatus().equals(ResponseStatus.SUCCESS)) {
            Product product1 = (Product) responseObject.getData();
            return product1;
        }
        throw new Exception(responseObject.getErrorMessage());
    }

    public List<Customer> getAllCustomers() throws Exception {
        ResponseObject responseObject = communication(Operation.GET_ALL_CUSTOMERS, null);
        if (responseObject.getStatus().equals(ResponseStatus.SUCCESS)) {
            return (List<Customer>) responseObject.getData();
        }
        throw new Exception(responseObject.getErrorMessage());
    }

    public List<Product> getAllProductsForManufacturer(Product product) throws Exception {
        ResponseObject responseObject = communication(Operation.GET_ALL_PRODUCST_FOR_MANUFACTURER, product);
        if (responseObject.getStatus().equals(ResponseStatus.SUCCESS)) {
            return (List<Product>) responseObject.getData();
        }
        throw new Exception(responseObject.getErrorMessage());
    }

    public Contract saveContract(Contract contract) throws Exception {
        ResponseObject responseObject = communication(Operation.SAVE_CONTRACT, contract);
        if (responseObject.getStatus().equals(ResponseStatus.SUCCESS)) {
            Contract contract1 = (Contract) responseObject.getData();
            return contract1;
        }
        throw new Exception(responseObject.getErrorMessage());
    }

    public Customer registerCustomer(Customer customer) throws Exception {
        ResponseObject responseObject = communication(Operation.REGISTER_CUSTOMER, customer);
        if (responseObject.getStatus().equals(ResponseStatus.SUCCESS)) {
            Customer customer1 = (Customer) responseObject.getData();
            return customer1;
        }
        throw new Exception(responseObject.getErrorMessage());
    }

    private ResponseObject communication(Integer operation, DomainObject domainObject) throws IOException, ClassNotFoundException {
        synchronized (lock) {
            RequestObject requestObject = new RequestObject(operation, domainObject);
            objectOutputStream.writeObject(requestObject);
            objectOutputStream.flush();
            ResponseObject responseObject = (ResponseObject) objectInputStream.readObject();
            return responseObject;
        }
    }

}
