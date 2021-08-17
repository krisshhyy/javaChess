

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;



public abstract class NetworkEntity extends Thread {

    protected ObjectOutputStream outputStream;
    protected ObjectInputStream inputStream;
    protected Socket connectionHandle;
    protected Object receivedMessage;


    NetworkEntity(final String name) {
        super(name);
        
        
    }

    public abstract void run();

    public void getStreams() throws IOException {
        
        outputStream = new ObjectOutputStream(connectionHandle
                .getOutputStream());
        outputStream.flush();
        inputStream = new ObjectInputStream(connectionHandle
                .getInputStream());
    }

    public void closeConnection() {

        try {
            if (outputStream != null) {
                outputStream.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            if (connectionHandle != null) {
                connectionHandle.close();
                //chatPanel.writeToDisplay("Connection closed with "
                //        + connectionHandle.getInetAddress().getHostName());
            }
        } catch (IOException e) {
            //JOptionPane.showMessageDialog(gameFrame, "Problems experienced when closing connection", "Notification",
            //        JOptionPane.ERROR_MESSAGE);
        }

    }
    
    /*public void processIncomingData() throws IOException {
        

        do {
            try {
                receivedMessage = inputStream.readObject();
            } catch (ClassNotFoundException e) {
//                JOptionPane.showMessageDialog(gameFrame,
//                        "read() error: message from "
//                                + connectionHandle.getInetAddress()
//                                .getHostName() + " not received",
//                        "Notification", JOptionPane.ERROR_MESSAGE);
            }
            if (receivedMessage instanceof Move) {
                final Move m = (Move) receivedMessage;
                //requestMove(chessBoard, m, false);
                //gameFrame.repaint();
            } else if (receivedMessage instanceof Board) {
                System.out.println("10101011111111111111101010111111111111111111111010101001111111");
                 Board b = (Board) receivedMessage;
                 System.out.println(b);
                // table.repaintWholeBoard(b);
                // table.gameFrame.repaint();
            } else if (receivedMessage instanceof String) {
                System.out.println("this client ID is :"+(String) receivedMessage);
            }
        } while (receivedMessage != null);

    }*/

    public void sendData( Object obj_to_send) {
        try {
            outputStream.writeObject(obj_to_send);
            outputStream.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

}