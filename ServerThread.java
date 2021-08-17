import java.io.*;
import java.util.*;
import java.net.*;

public class ServerThread extends Thread {
    public ObjectOutputStream SToutputStream;
    public ObjectInputStream STinputStream;
    Object recv;
    public Socket sts;
    int playerid;
    static List<ObjectOutputStream> serverOutputStreams = new ArrayList<>();

    ServerThread(Socket s, int n) {
        sts = s;
        playerid = n;
        start();
    }

    public void run() {
        try {
            getStreams();
            returnClientId();
            processIncomingData();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

    public void getStreams() throws IOException {
        
        SToutputStream = new ObjectOutputStream(sts
                .getOutputStream());
        serverOutputStreams.add(SToutputStream);    
        SToutputStream.flush();
        STinputStream = new ObjectInputStream(sts
                .getInputStream());
    }


    public void processIncomingData() throws IOException{
        

        do {
            try {
                recv = STinputStream.readObject();
            } catch (ClassNotFoundException e) {
//                JOptionPane.showMessageDialog(gameFrame,
//                        "read() error: message from "
//                                + connectionHandle.getInetAddress()
//                                .getHostName() + " not received",
//                        "Notification", JOptionPane.ERROR_MESSAGE);
            }

            
           
            if (recv instanceof Move) {
                final Move m = (Move) recv;
                if(serverOutputStreams.size()==2 && this.sts == Server.client_list.get(0))  {
                    System.out.println("this is a messgae from SereThread to verify that current sts is eqial to clien_list0");
                        
                        sendData(m,serverOutputStreams.get(1));
                }
                if(serverOutputStreams.size()==2 && this.sts == Server.client_list.get(1))  {
                    System.out.println("this is a messgae from SereThread to verify that current sts is eqial to clien_list1");
                        sendData(m,serverOutputStreams.get(0));
                }

                //requestMove(chessBoard, m, false);
                //gameFrame.repaint();
            } else if (recv instanceof Board) {
                 Board b = (Board) recv;
                 System.out.println("This is from client");
                 System.out.println(b);
                // the data to be recieved is to be sent to a client that the first client requests
                 if(serverOutputStreams.size()==2 && this.sts == Server.client_list.get(0))  {
                    System.out.println("this is a messgae from SereThread to verify that current sts is eqial to clien_list0");
                        //sendData(b,serverOutputStreams.get(1));
                        sendData(b,serverOutputStreams.get(1));
                }
                if(serverOutputStreams.size()==2 && this.sts == Server.client_list.get(1))  {
                    System.out.println("this is a messgae from SereThread to verify that current sts is eqial to clien_list1");
                        sendData(b,serverOutputStreams.get(0));
                        //sendData(b,serverOutputStreams.get(1));
                }
             
            } else if (recv instanceof String) {
            }

             
        } while (recv != null);



    }

    public void returnClientId() throws IOException {
        SToutputStream.writeObject((Object)Integer.toString(playerid));
        
    }

    public void sendData(Object data , ObjectOutputStream oos) throws IOException {

        try {

            oos.writeObject((Object)data);
            oos.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    //public void main (String [] agrs) {
      // start();
    //}

}
