import java.io.*;
import java.sql.*;
import java.util.*;
import oracle.jdbc.driver.*;
import org.apache.ibatis.jdbc.ScriptRunner;

public class ACL_Papers extends Student {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        connectToDatabase();
        ScriptRunner sr = new ScriptRunner(con);
        // Creating a reader object
        Reader reader;

        try {
            reader = new BufferedReader(new FileReader("./paper(1).sql"));

            // Running the script
            sr.runScript(reader);
            String choice;

            do {

                System.out.println("1. View table contents\n2. Search by PUBLICATIONID");
                System.out.println("3. Update URL by PUBLICATIONID\n4. Exit");

                choice = scanner.nextLine();

                switch (choice) {
                    case "1": /* View tables */
                        getTables();
                        break;
                    case "2": /* Search by publication ID */
                        searchForPublicationID();
                        break;
                    case "3": /* Update URL by publication ID */
                        readURL();
                        break;
                    case "4": /* Exit */
                        scanner.close();
                        break;
                    default:
                        System.out.println("Invalid input. . . .");
                        break;
                }
            } while (!choice.equals("4"));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
