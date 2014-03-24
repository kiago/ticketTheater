package tickettheater.tcpserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Server {

    public static String testTitle = "sdfghjklmlkjhgf";
    Connection conn;
    //connection à la base de données
    private int val;
    private int val2;

    public static void main(String[] args) throws IOException {
        new Server().begin(4444);
    }
    ServerSocket serverSocket;

    public void begin(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        while (true) {
            System.out.println("Waiting for clients to connect on port " + port + "...");
            new ProtocolThread(serverSocket.accept()).start();
            //Thread.start() calls Thread.run()
        }
    }

    class ProtocolThread extends Thread {

        Socket socket;
        PrintWriter out_socket;
        BufferedReader in_socket;

        public ProtocolThread(Socket socket) {
            System.out.println("Accepting connection from " + socket.getInetAddress() + "...");
            this.socket = socket;
            try {
                out_socket = new PrintWriter(socket.getOutputStream(), true);
                in_socket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                System.out.println("Expecting Hello from client...");
                //sleep(5000);
                if ("Hello".equals(in_socket.readLine())) {
                    System.out.println("Client is nice :) Let's be polite...");



                    out_socket.println(fetchTitles());
                                        System.out.println(fetchTitles());

                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    System.out.println("Closing connection.");
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private int doCheck(String title) {
        int isOk = 0;
        System.out.println("[CHECKING OUT remaining spots]");
        String query = "SELECT nbPlacesRestantes FROM piecesDeTheatre WHERE title ='" + title + "' ";
        String query2 = "SELECT nbPlacesTotal FROM piecesDeTheatre WHERE title ='" + title + "' ";

        try {
            Statement st = conn.createStatement();
            ResultSet restante = st.executeQuery(query);
            ResultSet total = st.executeQuery(query2);
            val = ((Number) restante.getObject(1)).intValue();
            val2 = ((Number) total.getObject(1)).intValue();

            int num = (val2 - val);

            if (num >= 0) {
                isOk = 1;
            } else {
                isOk = 0;
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return isOk;
    }

    private String fetchTitles() {
        String toReturn = null;
        System.out.println("[FETCHING TITLES]");
        String query = "SELECT title FROM piecesDeTheatre";

        /*traitement du ResultSet en String 
         afin de les afficher dans le fenetre de reservations */
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);

            toReturn = rs.getString(1);



        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        testTitle=toReturn;
        return toReturn;
    }

    private void doInsert(String nom, String prenom, int quantité, String title) {
        if (doCheck(title) == 1) {
            System.out.print("\n[Performing reservation] ... ");
            try {
                Statement st = conn.createStatement();
                st.executeUpdate("INSERT INTO reservations (nom, prenom, quantité) "
                        + "VALUES ('" + nom + "', '" + prenom + "', " + quantité + ")");
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
        } else if (doCheck(title) == 0) {
            // plus de places
        } else {
            //surement un bug ( au cas ou )
        }
    }

    private void doDeleteTable() {
        System.out.print("\n[ERASING TABLE coffees] ... ");
        try {
            Statement st = conn.createStatement();
            st.executeUpdate("TRUNCATE TABLE  `coffees`");
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private void doDeletePrice(int maxPrice) {
        System.out.print("\n[Performing DELETE] ... ");
        try {
            Statement st = conn.createStatement();
            st.executeUpdate("DELETE FROM COFFEES "
                    + "WHERE PRICE>" + maxPrice + "");
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private void doUpdate(String str, int price) {
        System.out.print("\n[Performing UPDATE] ... ");
        try {
            Statement st = conn.createStatement();
            st.executeUpdate("UPDATE COFFEES SET PRICE=" + price + " "
                    + "WHERE COF_NAME='" + str + "'");
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }
}
