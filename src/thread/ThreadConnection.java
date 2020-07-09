/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thread;

import controller.Controller;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.JOptionPane;
import transfer.RequestObject;
import util.Operation;

/**
 *
 * @author marko
 */
public class ThreadConnection extends Thread {

    private final ObjectOutputStream objectOutputStream;
    private final ObjectInputStream objectInputStream;

    public ThreadConnection() throws IOException {
        this.objectOutputStream = Controller.getInstance().getObjectOutputStream();
        this.objectInputStream = Controller.getInstance().getObjectInputStream();
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                synchronized (Controller.getInstance().getLock()) {
                    objectOutputStream.writeObject(new RequestObject(Operation.IS_CONNECTED));
                    objectOutputStream.flush();
                    objectInputStream.readObject();
                }
                sleep(1000);
            } catch (Exception ex) {
                interrupt();
                JOptionPane.showMessageDialog(null, "Disconnected!");
                System.exit(1);
            }
        }
    }
}
