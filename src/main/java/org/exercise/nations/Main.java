package org.exercise.nations;

import java.sql.*;
import java.util.Scanner;

public class Main {

    private final static String url = "jdbc:mysql://localhost:3306/db-nations";
    private final static String user = "root";
    private final static String password = "root";
    // query
    private final static String db_nations = "select c.name as nome_nazione, c.country_id as id, r.name as nome_regione, c2.name as nome_continente\n" +
            "from countries c \n" +
            "join regions r \n" +
            "on c.region_id = r.region_id \n" +
            "join continents c2 \n" +
            "on r.continent_id = c2.continent_id\n" +
            "where c.name like ?\n" +
            "order by c.name;\n" +
            "\n";
    public static void main(String[] args) {
        // inizializzo lo scanner
        Scanner input = new Scanner(System.in);
        // mi connetto al database
        // uso un try-with-resources
        try(Connection connection = DriverManager.getConnection(url, user, password)){
            System.out.println("Cerca: ");
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
        }catch (SQLException e){
            System.out.println("An error occured");

        }
        input.close();

    }
}
