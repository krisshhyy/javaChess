
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Client extends NetworkEntity {

    private String hostName;
    private int serverPort;
    Player player;
    int playerID;
    final Board board;
    static Table table;
    Move move;
    public Client(final String host, final int port, final Board board) {
        super("CLIENT");
        this.board = Board.createStandardBoard();

        hostName = host;
        serverPort = port;
        System.out.println(board);
        //this.playerID=playerID;
        start();

    }

    /*public Client(final Board board) {
        super("CLIENT");
        this.board = Board.createStandardBoard();;
    }*/
    public int getPlayerId(){
        return this.playerID;
    }
    public void run() {

        System.out.println("inside client run");
        try {

            System.out.println("after client sg");
            connectToServer();
            System.out.println("after client cts");
            getStreams();
            System.out.println("after client gs");
            processIncomingData();
            System.out.println("after client pid");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public static void startGame() {
        // board = Board.createStandardBoard();
        

        table = new Table();
    }

    private void connectToServer() {
        try {
            connectionHandle = new Socket(InetAddress.getByName(hostName), serverPort);
            // connectionEstablished = true;

            System.out.println("Successfully connected to " + connectionHandle.getInetAddress().getHostName());

        } catch (IOException e) {
            System.out.println("Failed to connect to: " + hostName);
        }
    }

    public void processIncomingData() /* throws IOException */ {

        do {
            try {
                receivedMessage = inputStream.readObject();
            } catch (ClassNotFoundException | IOException e) {
                // JOptionPane.showMessageDialog(gameFrame,
                // "read() error: message from "
                // + connectionHandle.getInetAddress()
                // .getHostName() + " not received",
                // "Notification", JOptionPane.ERROR_MESSAGE);
            }
            if (receivedMessage instanceof Move) {
                final Move m = (Move) receivedMessage;
                table.moveLog.addMove(m);
                table.takenPiecesPanel.redo(table.moveLog);
                // requestMove(chessBoard, m, false);
                // gameFrame.repaint();
            } else if (receivedMessage instanceof Board) {
                System.out.println("10101011111111111111101010111111111111111111111010101001111111");
                Board b = (Board) receivedMessage;
                table.chessBoard =  b;
                System.out.println(table.chessBoard);
                table.repaintWholeBoard(table.chessBoard);
                // table.gameFrame.repaint();
            } else if (receivedMessage instanceof String) {
                System.out.println("this client ID is :" + (String) receivedMessage);
            }
        } while (receivedMessage != null);

    }

    public static void main(String[] args) {
        startGame();
        
    }

}