/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package regub;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;

/**
 * Singleton représantant les fichiers de configuration
 *
 * @author antoine
 */
public class Config implements Iterable<String> {

    private static Config instance;

    private Properties prop;

    private Config() {
        String global_config = "config.properties";
        prop = new Properties();
        File f = new File(global_config);
        if (f.isFile() && f.canRead()) {
            System.out.println("Chargement de la configuration globale :\n* "
                    + global_config);
            try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
                prop.load(reader);
            } catch (IOException e) {
                throw new Error("Fichier de configuration principal invalide :\n* "
                        + global_config);
            }
        } else {
            throw new Error("Fichier de configuration principal introuvable :\n* "
                    + global_config);
        }
        String local_config = prop.getProperty("local_config")
                .replace("$(HOME)", System.getProperty("user.home"));
        prop.setProperty("local_config", local_config);
        f = new File(local_config);
        if (f.isFile() && f.canRead()) {
            System.out.println("Chargement de la configuration locale : \n* " + local_config);
            try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
                prop.load(reader);
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    /**
     * retourne la valeur associée à la clée dan le fichier
     *
     * @param key
     * @return
     */
    public String get(String key) {
        return prop.getProperty(key);
    }

    /**
     * modifier la valeur d'une propriété
     *
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        prop.setProperty(key, value);
    }

    /**
     * sauve la configuration actuelle dans le fichier utilisateur
     *
     * @throws IOException
     */
    public void saveTolocal() throws IOException {
        File f = new File(get("local_config"));
        if (f.createNewFile() || f.canWrite()) {
            try (BufferedWriter writter = new BufferedWriter(new FileWriter(f))) {
                prop.store(writter, null);
            } catch (IOException e) {
                throw e;
            }
        }
    }

    /**
     * retourne l'unique instance
     * @return
     */
    public static Config getConfig() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        String s = null;
        Iterator<String> it = iterator();
        str.append('{');
        while (it.hasNext()) {
            s = it.next();
            str.append('"').append(s).append('"').append(':')
                    .append('"').append(get(s)).append('"').append(',');
        }
        str.setCharAt(str.length() - 1, '}');
        return str.toString();
    }

    @Override
    public Iterator<String> iterator() {
        return prop.stringPropertyNames().iterator();
    }

}
