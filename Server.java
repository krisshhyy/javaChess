
import java.io.*;
import java.net.ServerSocket;
import java.util.*;
import java.net.*;
public class Server extends NetworkEntity {
    int n =1;
    public ServerSocket server;
    public static List<Socket> client_list = new ArrayList<>();
    int listenPort;

    public Server( final int listen_port) {
        super("SERVER");
        listenPort = listen_port;
        start();
        
    }
    public Server(){
        super("Server");
    }


    public void run() {

        try {
            server = new ServerSocket(listenPort, 1);
            //chatPanel.writeToDisplay("Listening on port " + listenPort);
            System.out.println("inside Server run");
            try {
                waitForConnection();
               //getStreams();
               //processIncomingData();
                
            }
            catch (IOException ioe) {
                ioe.printStackTrace();
            } finally {
                System.out.println("..................................///////////////////////////////**********************************");
                closeConnection();
            }
        } catch (IOException e) {
        //    JOptionPane.showMessageDialog(gameFrame, "Network Error: " + e, "Notification",
        //            JOptionPane.ERROR_MESSAGE);
        }

    }

    private void waitForConnection() throws IOException {
        while ( true /*n<=2*/){

        connectionHandle = server.accept();
        client_list.add(connectionHandle);
        new ServerThread(connectionHandle,n);    
        n++;
        

        //System.out.println("Client" + n + " is connected");
       
        }

        //System.out.println("not accepting anu more connections");
       
        //connectionEstablished = true;
        //chatPanel.writeToDisplay("Connection received from:"
        //        + connectionHandle.getInetAddress().getHostName());
    }

    public void closeConnection() {
        super.closeConnection();
        try {
            server.close();
        } catch (IOException e) {
        //    chatPanel.writeToDisplay(getName()
        //            + "failed to disconnect from the network");
        }
    }
   public static List<Socket> getClientList() {
       return client_list;
   }



    public static void main(String [] args){
        new Server(2222);
        
        
    }
}