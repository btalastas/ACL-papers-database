import java.io.*;
import java.sql.*;
import java.util.*;
import oracle.jdbc.driver.*;

public class Student {
    static Connection con;
    static Statement stmt;

    public static void main(String[] argv) {
        connectToDatabase();
    }

    /**
     * Takes url.csv file and adds it to a list.
     * User inputs the publication ID that they want to update the URL for.
     * Print updated tuple after making updates.
     */
    public static void readURL() {
        Scanner scanner = new Scanner(System.in);
        List<String[]> dataList = new ArrayList<>();
        System.out.println("Enter path for url.csv");
        String path = scanner.nextLine();
        try {
            Scanner file = new Scanner(new File(path));
            file.useDelimiter(",");

            while (file.hasNextLine()) {
                String line = file.nextLine();
                String[] split = line.split(",");
                dataList.add(split);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Input PublicationID to be updated.");

        String id = scanner.nextLine();
        int idInt = Integer.parseInt(id);
        String[] pubid = dataList.get((idInt - 1));
        String url = pubid[1];
        String updateQuery = "UPDATE PUBLICATIONS SET URL = ? WHERE PUBLICATIONID = ?";
        PreparedStatement updateStatement;
        try {
            updateStatement = con.prepareStatement(updateQuery);
            updateStatement.setString(1, url);
            updateStatement.setString(2, id);
            int rowsAffected = updateStatement.executeUpdate();

            if (rowsAffected > 0) {
                updateStatement.close();
                // Create a SELECT query to retrieve the updated record
                String selectQuery = "SELECT * FROM PUBLICATIONS WHERE PUBLICATIONID = ?";
                PreparedStatement selectStatement = con.prepareStatement(selectQuery);
                selectStatement.setString(1, id);
                ResultSet resultSet = selectStatement.executeQuery();
                String publication1 = "ID";
                String publication2 = "YEAR";
                String publication3 = "TYPE";
                String publication4 = "TITLE";
                String publication5 = "URL";
                System.out.printf("%-4s %-4s %-6s %-80s %s%n", publication1, publication2, publication3,
                        publication4, publication5);
                System.out.println(
                        "----------------------------------------------------------------------------------------------------------------------------------");

                while (resultSet.next()) {
                    // Retrieve and print the updated record
                    String publicationId = resultSet.getString("PUBLICATIONID");
                    String year = resultSet.getString("YEAR");
                    String type = resultSet.getString("TYPE");
                    String title = resultSet.getString("TITLE");
                    String urlString = resultSet.getString("URL");

                    System.out.printf("%-4s %-4s %-6s %-80s %s%n", publicationId, year, type, title, urlString);
                }

                resultSet.close();
                selectStatement.close();
            } else {
                System.out.println("No record found for the provided PUBLICATIONID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void connectToDatabase() {
        Scanner scanner = new Scanner(System.in);
        String driverPrefixURL = "jdbc:oracle:thin:@";
        String jdbc_url = "artemis.vsnet.gmu.edu:1521/vse18c.vsnet.gmu.edu";
        String username, password;

        // IMPORTANT: DO NOT PUT YOUR LOGIN INFORMATION HERE. INSTEAD, PROMPT USER FOR
        // HIS/HER LOGIN/PASSWD
        System.out.println("Enter username: ");
        username = scanner.nextLine();
        System.out.println("Enter password: ");
        password = scanner.nextLine();

        try {
            // Register Oracle driver
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        } catch (Exception e) {
            System.out.println("Failed to load JDBC/ODBC driver.");
            return;
        }

        try {
            System.out.println(driverPrefixURL + jdbc_url);
            con = DriverManager.getConnection(driverPrefixURL + jdbc_url, username, password);
            DatabaseMetaData dbmd = con.getMetaData();
            stmt = con.createStatement();

            System.out.println("Connected.");

            if (dbmd == null) {
                System.out.println("No database meta data");
            } else {
                System.out.println("Database Product Name: " + dbmd.getDatabaseProductName());
                System.out.println("Database Product Version: " + dbmd.getDatabaseProductVersion());
                System.out.println("Database Driver Name: " + dbmd.getDriverName());
                System.out.println("Database Driver Version: " + dbmd.getDriverVersion());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }// End of connectToDatabase()

    /**
     * User inputs the publication ID to be retrieved from database.
     */
    public static void searchForPublicationID() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter PublicationID: ");

        String publicationIdInput = scanner.nextLine();

        String selectQuery = "SELECT * FROM PUBLICATIONS WHERE PUBLICATIONID = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(selectQuery)) {
            preparedStatement.setString(1, publicationIdInput);

            ResultSet rs = preparedStatement.executeQuery();

            if (!rs.isBeforeFirst()) {
                System.out.println("PublicationID not found.");
            } else {
                String publication1 = "ID";
                String publication2 = "YEAR";
                String publication3 = "TYPE";
                String publication4 = "TITLE";
                String publication5 = "URL";
                System.out.printf("%-4s %-4s %-6s %-80s %s%n", publication1, publication2, publication3,
                        publication4, publication5);
                System.out.println(
                        "----------------------------------------------------------------------------------------------------------------------------------");

                while (rs.next()) {
                    String publicationID = rs.getString("PUBLICATIONID");
                    String year = rs.getString("YEAR");
                    String type = rs.getString("TYPE");
                    String title = rs.getString("TITLE");
                    String url = rs.getString("URL");

                    System.out.printf("%-4s %-4s %-6s %-80s %s%n", publicationID, year, type, title, url);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Asks user what table they would like to see.
     * Prints out the desired table.
     */
    public static void getTables() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Which tables would you like to see?");
        System.out.println("\t1) Publications\n\t2) Authors");
        int choice;
        String query = "";
        do {
            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    query = "SELECT * FROM PUBLICATIONS ORDER BY CAST(PUBLICATIONID AS INT)";
                    printPublications(query);

                    break;
                case 2:
                    query = "SELECT * FROM AUTHORS ORDER BY CAST(PUBLICATIONID AS INT)";
                    printAuthors(query);

                    break;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        } while (choice != 1 && choice != 2);

    }

    /**
     * Prints the authors table.
     * 
     * @param query
     */
    public static void printAuthors(String query) {
        String author1 = "ID";
        String author2 = "AUTHOR";

        System.out.printf("%-4s %s%n", author1, author2);
        System.out.println("------------------------------");

        // https://docs.oracle.com/javase/tutorial/jdbc/basics/retrieving.html
        try (Statement getTables = con.createStatement()) {
            ResultSet rs = getTables.executeQuery(query);
            while (rs.next()) {
                String publicationID = rs.getString("PUBLICATIONID");
                String author = rs.getString("AUTHOR");

                System.out.printf("%-4s %s%n", publicationID, author);
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Prints the publication table.
     * 
     * @param query
     */
    public static void printPublications(String query) {
        String publication1 = "ID";
        String publication2 = "YEAR";
        String publication3 = "TYPE";
        String publication4 = "TITLE";
        String publication5 = "URL";
        System.out.printf("%-4s %-4s %-6s %-80s %s%n", publication1, publication2, publication3,
                publication4, publication5);
        System.out.println(
                "----------------------------------------------------------------------------------------------------------------------------------");
        // https://docs.oracle.com/javase/tutorial/jdbc/basics/retrieving.html
        try (Statement getTables = con.createStatement()) {
            ResultSet rs = getTables.executeQuery(query);
            while (rs.next()) {
                String publicationID = rs.getString("PUBLICATIONID");
                String year = rs.getString("YEAR");
                String type = rs.getString("TYPE");
                String title = rs.getString("TITLE");
                String url = rs.getString("URL");

                System.out.printf("%-4s %-4s %-6s %-80s %s%n", publicationID, year, type, title, url);

            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}// End of class
