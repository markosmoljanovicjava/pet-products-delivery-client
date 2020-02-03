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
import java.lang.reflect.Array;
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

    public void login(User user) throws IOException, ClassNotFoundException, Exception {
        RequestObject requestObject = new RequestObject();
        requestObject.setOperation(Operation.LOGIN);
        requestObject.setData(user);

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

    public Map<String, Object> getMap() {
        return map;
    }

    public ViewMain getViewMain() {
        if (viewMain == null) {
            viewMain = new ViewMain();
        }
        return viewMain;
    }

    public List<Manufacturer> getManufacturers() {
        List<Manufacturer> list = new ArrayList();
        list.add(new Manufacturer("Manufacturer-1", "Adress-1", "111-111-111"));
        list.add(new Manufacturer("Manufacturer-2", "Adress-2", "222-222-222"));
        list.add(new Manufacturer("Manufacturer-3", "Adress-3", "333-333-333"));
        return list;
    }

    public void saveProduct(Product product) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
