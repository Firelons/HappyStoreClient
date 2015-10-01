/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package regub;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.NoSuchElementException;

/**
 * classe static gérant la connexion à la bas de donnée
 *
 * @author antoine
 */
public class Auth {

    private static String loginDB;
    private static String passwDB;
    private static String url;
    private static final HashMap<String, String> userInfo = new HashMap<>();
    private static boolean connected = false;

    /**
     * Nécéssite d'être connecter avec connect crée un objet de type Connection,
     * doit être fermer ou utiliser dans un try with ressources
     *
     * @return Connection l'objet Connection
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        if (connected) {
            return DriverManager.getConnection(url, loginDB, passwDB);
        }
        return null;
    }

    /**
     * se connecte à la base avec les login,mdp d'un utilisateur
     *
     * @param login
     * @param pass
     * @throws ClassNotFoundException si mariadb-java-client-1.*.* n'est pas
     * dans le ClassPath
     * @throws SQLException
     */
    public static void connect(String login, String pass) throws ClassNotFoundException, SQLException {
        Class.forName("org.mariadb.jdbc.Driver");
        Config conf = Config.getConfig();
        url = "jdbc:mysql://" + conf.get("db_address") + ':' + conf.get("db_port") + '/' + conf.get("db_name")
                + "/?noAccessToProcedureBodies=true";
        try (Connection cn = DriverManager.getConnection(url, conf.get("db_login"), conf.get("db_password"))) {

            PreparedStatement st = cn.prepareCall("call " + conf.get("db_proc_name") + "(?,?)");
            st.setString(1, login);
            st.setString(2, pass);
            st.execute();
            ResultSetMetaData meta = st.getMetaData();
            ResultSet res = st.getResultSet();
            if (!res.next()) {
                throw new NoSuchElementException("mauvais login ou mot de passe");
            }
            for (int i = 1; i <= meta.getColumnCount(); i++) {
                userInfo.put(meta.getColumnLabel(i), res.getString(i));
            }
            loginDB = userInfo.get(conf.get("db_col_login"));
            passwDB = userInfo.get(conf.get("db_col_password"));
            userInfo.remove(conf.get("db_col_login"));
            userInfo.remove(conf.get("db_col_password"));
            connected = true;
            getConnection();
        } catch (SQLException e) {
            throw e;
        }
    }

    /**
     * verifie l'utilisation de la méthode connect
     *
     * @return true si connect() à réussi
     */
    public static boolean isConnected() {
        return connected;
    }

    /**
     * Déconnecte l'utilisateur en supprimant ses données locales, ferme pas les
     * connexion à la base de donnée
     */
    public static void disconnect() {
        connected = false;
        loginDB = null;
        passwDB = null;
        url = null;
        userInfo.clear();
    }

    /**
     * retourne les info de lutilisateur connecté sous la forme d'un conteneur
     * clée valeur (HashMap)
     *
     * @return userInfo
     */
    public static HashMap getUserInfo() {
        return userInfo;
    }

    /**
     * Permet de changer le mot de passe de l'utilisateur connecte
     *
     * @param oldpass ancien mot de passe
     * @param newpass nouveau mot de passe
     * @return true si le mot de passe a change
     * @throws SQLException
     */
    public static boolean changePassword(String oldpass, String newpass) throws SQLException {
        try (Connection con = Auth.getConnection();
                PreparedStatement st = con.prepareStatement("call updPass(?,?,?);")) {
            st.setString(1, userInfo.get("login"));
            st.setString(2, oldpass);
            st.setString(3, newpass);
            st.execute();
            ResultSet r = st.getResultSet();
            r.next();
            return r.getInt(1) == 1;
        } catch (SQLException ex) {
            throw ex;
        }
    }
}
