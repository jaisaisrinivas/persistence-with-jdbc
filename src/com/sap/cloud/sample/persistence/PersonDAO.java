package com.sap.cloud.sample.persistence;



import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

/**
 * Data access object encapsulating all JDBC operations for a person.
 */
public class PersonDAO {
    private DataSource dataSource;

    /**
     * Create new data access object with data source.
     */
    public PersonDAO(DataSource newDataSource) throws SQLException {
        setDataSource(newDataSource);
    }

    /**
     * Get data source which is used for the database operations.
     */
    public DataSource getDataSource() {
        return dataSource;
    }

    /**
     * Set data source to be used for the database operations.
     */
    public void setDataSource(DataSource newDataSource) throws SQLException {
        this.dataSource = newDataSource;
        checkTable();
    }

    /**
     * Add a person to the table.
     */
    public void addPerson(Person person) throws SQLException {
        Connection connection = dataSource.getConnection();

        try {
        	PreparedStatement pstmt = connection
                    .prepareStatement("INSERT INTO LOGIN1 (ID, FIRSTNAME, ROLE, PASSWORD, LASTNAME) VALUES (?, ?, ?, ?, ?)");
            pstmt.setString(1, UUID.randomUUID().toString());
            pstmt.setString(2, person.getFirstName());
            pstmt.setString(3, person.getRole());
            pstmt.setString(4, person.getPassword());
            pstmt.setString(5, person.getLastName());
        
            
            
            
            
            pstmt.executeUpdate();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    /**
     * Get all persons from the table.
     */
    public List<Person> selectAllPersons() throws SQLException {
        Connection connection = dataSource.getConnection();
        try {
            PreparedStatement pstmt = connection
                    .prepareStatement("SELECT ID, FIRSTNAME, ROLE, PASSWORD, LASTNAME FROM LOGIN1");
            ResultSet rs = pstmt.executeQuery();
            ArrayList<Person> list = new ArrayList<Person>();
            while (rs.next()) {
                Person p = new Person();
                p.setId(rs.getString(1));
                p.setFirstName(rs.getString(2));
                p.setRole(rs.getString(3));
                p.setPassword(rs.getString(4));
                p.setLastName(rs.getString(5));
                list.add(p);
            }
            return list;
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    /**
     * Check if the person table already exists and create it if not.
     */
    private void checkTable() throws SQLException {
        Connection connection = null;

        try {
            connection = dataSource.getConnection();
            if (!existsTable(connection)) {
                createTable(connection);
            }
            
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    /**
     * Check if the person table already exists.
     */
    private boolean existsTable(Connection conn) throws SQLException {
        DatabaseMetaData meta = conn.getMetaData();
        ResultSet rs = meta.getTables(null, null, "LOGIN1", null);
        while (rs.next()) {
            String name = rs.getString("TABLE_NAME");
            if (name.equals("LOGIN1")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Create the person table.
     */
    private void createTable(Connection connection) throws SQLException {
        PreparedStatement pstmt = connection
                .prepareStatement("CREATE TABLE LOGIN1 "
                        + "(ID VARCHAR(255) PRIMARY KEY, "
                        + "FIRSTNAME VARCHAR (255),"
                        + "ROLE VARCHAR (255),"
                        + "PASSWORD VARCHAR (255),"
                        + "LASTNAME VARCHAR (255))");
        pstmt.executeUpdate();
        
        PreparedStatement pstmt1 = connection
                .prepareStatement("INSERT INTO LOGIN1 (ID, FIRSTNAME, ROLE, PASSWORD, LASTNAME) VALUES (?, ?, ?, ?, ?)");
        pstmt1.setString(1, UUID.randomUUID().toString());
        pstmt1.setString(2, "sri");
        pstmt1.setString(3, "manager");
        pstmt1.setString(4, "test");
        pstmt1.setString(5, "dalavai");
        
        
        pstmt1.executeUpdate();
    }

	public String checkPerson(Person person) throws SQLException {

        Connection connection = dataSource.getConnection();

        try {
        	 Statement stmt = connection.createStatement();
		      String sql;
		      sql = "SELECT ROLE FROM LOGIN1 where LASTNAME='"+person.getLastName()+"' and  PASSWORD='"+person.getPassword()+"'";
		      ResultSet rs = stmt.executeQuery(sql);

		      //STEP 5: Extract data from result set
		      if(rs.next()){
		    	System.out.println("found"); 
		    	System.out.println(rs.getString("ROLE"));
		    	return rs.getString("ROLE")+","+person.getLastName();
		      }

        	
        	
        	
            
            
            
            
            
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    	return null;
	}
}