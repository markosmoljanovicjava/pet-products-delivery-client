/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import domain.Manufacturer;
import domain.Product;
import domain.User;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import transfer.RequestObject;
import transfer.ResponseObject;
import ui.view.ViewMain;
import util.Operation;
import util.ResponseStatus;

/**
 *
 * @author marko
 */
public class Controller {

    private static Controller instance;
    private final Map<String, Object> map;
    private final Socket socket;
    private final ObjectOutputStream objectOutputStream;
    private final ObjectInputStream objectInputStream;
    private ViewMain viewMain;

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

    public void login(User user) throws Exception {
        RequestObject requestObject = new RequestObject(Operation.LOGIN, user);
        objectOutputStream.writeObject(requestObject);
        objectOutputStream.flush();

        ResponseObject responseObject = (ResponseObject) objectInputStream.readObject();

        ResponseStatus status = responseObject.getStatus();
        if (status == ResponseStatus.SUCCESS) {
            map.put("user", responseObject.getData());
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
            this.map.put("current.product", product1);
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

    public Map<String, Object> getMap() {
        return map;
    }

    public ViewMain getViewMain() {
        if (viewMain == null) {
            viewMain = new ViewMain();
        }
        return viewMain;
    }

}
