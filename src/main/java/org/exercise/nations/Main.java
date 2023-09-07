package org.exercise.nations;

import java.sql.*;
import java.util.Scanner;

public class Main {

    private final static String url = "jdbc:mysql://localhost:3306/db-nations";
    private final static String user = "root";
    private final static String password = "root";

    // query presi dal database
    private final static String db_nations = "select c.name as nome_nazione, c.country_id as id, r.name as nome_regione, c2.name as nome_continente\n" +
            "from countries c \n" +
            "join regions r \n" +
            "on c.region_id = r.region_id \n" +
            "join continents c2 \n" +
            "on r.continent_id = c2.continent_id\n" +
            "where c.name like ?\n" +
            "order by c.name;\n" +
            "\n";
    private final static String db_languages = "select c.name as nome, l.`language` as lingue\n" +
            "from languages l \n" +
            "join country_languages cl \n" +
            "on cl.language_id = l.language_id \n" +
            "join countries c \n" +
            "on cl.country_id = c.country_id\n" +
            "where c.country_id =?;";
    private final static String db_stats = "select cs.`year`  as anno, cs.population as numero_popolazione, cs.gdp as gdp\n" +
            "from country_stats cs \n" +
            "where country_id =?\n" +
            "order by `year` desc ;";

    public static void main(String[] args) {
        // inizializzo lo scanner
        Scanner input = new Scanner(System.in);
        // mi connetto al database
        // uso un try-with-resources
        try(Connection connection = DriverManager.getConnection(url, user, password)){
            System.out.print("Cerca: ");
            String ricerca = input.nextLine();
            // qui ho le connessioni aperte
            // preparo il PreparedStatement con la query che ho salvato nella costante db_nations
            try(PreparedStatement ps = connection.prepareStatement(db_nations)){
                // uso il parameter binding per evitare l'injection
                ps.setString(1, "%" + ricerca + "%");
                // qui ho il preparedStatement aperto
                try(ResultSet rs = ps.executeQuery()){
                    // qui ho il resultSet con il risultato della query
                    // stampo a video i nomi delle tabelle
                    System.out.print(" ID: ");
                    System.out.print("Nome Nazione: ");
                    System.out.print(" Nome Regione: ");
                    System.out.println(" Nome Continente: ");
                    // itero il resultSet in un ciclo while
                    while(rs.next()){ // ad ogni iterazione chiamo next e testo il risultato ( che sia true o false)
                        // ad ogni iterazione del while posso leggere i dati di una riga del risultato
                        int id = rs.getInt("id");
                        String nameCountry = rs.getString("nome_nazione");
                        String nameRegion = rs.getString("nome_regione");
                        String nameContinent = rs.getString("nome_continente");
                        // stampo a video i dati
                        System.out.print(id + "   ");
                        System.out.print(nameCountry + "   ");
                        System.out.print(nameRegion + "   ");
                        System.out.println(nameContinent);
                    }
                }
            }
            // chiedo all'utente di scegliere l'id di una nazione
            System.out.print("Scegli l'id di una nazione: ");
            int scelta = Integer.parseInt(input.nextLine());
            // preparo un preparedStatement con la query che ho salvato nella costante db_languages
            try(PreparedStatement ps = connection.prepareStatement(db_languages)){
                // parameter binding per evitare l'injection
                ps.setInt(1, scelta);
                try(ResultSet rs = ps.executeQuery()){
                    // stampo il nome della nazione scelta dall'utente
                    // iterizzo la variabile booleana per iterare il nome della nazione
                    boolean isFirstStat = true;
                    // itero un ciclo while
                    while (rs.next()) {
                        // se isFirstStat è true, legge i dati sul nome della nazione
                        if (isFirstStat) {
                            String details = rs.getString("nome");
                            // stampo a video i dati
                            System.out.println("Dettagli della nazione: " + details);
                            System.out.print("Lingue parlate: ");
                            // a questo punto, valorizzo la booleana come false
                            isFirstStat = false;
                        }
                        // leggo i dati delle lingue con il ciclo while
                        String language = rs.getString("lingue");
                        // stampo a video i dati delle lingue parlate
                        System.out.print(language + "  ");
                    }
                }
            }
            // statistiche più recenti della nazione selezionata
            try(PreparedStatement ps = connection.prepareStatement(db_stats)){
                // parameter binding per evitare l'injection
                ps.setInt(1, scelta);
                try(ResultSet rs = ps.executeQuery()){
                    // stampo il nome della tabella statistiche
                    System.out.println("\nStatistiche più recenti: ");
                    if(rs.next()){  // ad ogni iterazione chiamo next e testo il risultato ( che sia true o false)
                        // se trovo un elemento corrispondente all'id, stampo le informazioni
                        // ad ogni iterazione leggo i dati sulle statistiche
                        int year = rs.getInt("anno");
                        long population = rs.getLong("numero_popolazione");
                        long gdp = rs.getLong("gdp");
                        // stampo a video i dati
                        System.out.println("Anno: " + year);
                        System.out.println("Popolazione: " + population);
                        System.out.println("GDP: " + gdp);
                    }
                }
            }
        }catch (SQLException e){
            System.out.println("An error occured");

        }finally {
            input.close();
        }


    }
}
