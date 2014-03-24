/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tickettheater;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author kiago
 */
public class database {
    public static String testTitle = "decfrgtvl";
    Connection conn;
//connection à la base de données
    private int val;
    private int val2;

    private void database() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            //ATTENTION AU PORT
            String url = "jdbc:mysql://localhost:3306/resaTheater";
            conn = DriverManager.getConnection(url, "root", "root");


            conn.close();
        } catch (ClassNotFoundException | IllegalAccessException |
                InstantiationException | SQLException ex) {
            System.err.println(ex.getMessage());
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

    public String fetchTitles() {

        String title = null;
        System.out.println("[FETCHING TITLES]");
        String query = "SELECT title FROM piecesDeTheatre";

        /*traitement du ResultSet en String 
         afin de les afficher dans le fenetre de reservations */
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                String em = rs.getString("EM_ID");
                title = em.replace("\n", ",");
                //System.out.println(title);
            }


        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        testTitle=title;
        return title;
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
