/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thread;

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

    public ThreadConnection(ObjectOutputStream objectOutputStream,
            ObjectInputStream objectInputStream) {
        this.objectOutputStream = objectOutputStream;
        this.objectInputStream = objectInputStream;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                objectOutputStream.writeObject(new RequestObject(Operation.IS_CONNECTED));
                objectOutputStream.flush();
                objectInputStream.readObject();
                sleep(1000);
            } catch (Exception ex) {
                interrupt();
                JOptionPane.showMessageDialog(null, "Disconnected!");
                System.exit(1);
            }
        }
    }
}
