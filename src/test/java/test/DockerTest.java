package test;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.Assert.fail;

public class DockerTest {
	@Test
	public void testConnection() {
		try {
		 Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
					"jdbc:mysql://127.0.0.1:3306/pkdd99",
					"CinecubesUser",
					"Cinecubes");
            Statement statement = con.createStatement();
			ResultSet result = statement.executeQuery("select * from loan");
			System.out.println(result.getMetaData());
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
	}
}
