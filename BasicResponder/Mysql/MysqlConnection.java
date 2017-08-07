package Mysql;

import java.sql.*;
import java.util.Set;
import GeneralTools.Error;
import java.util.HashSet;

/**
 *
 * @author Fairouz
 */
public class MysqlConnection{
    
    // JDBC driver name and database URL
    private String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    private String DB_URL = "jdbc:mysql://localhost/test?autoReconnect=true&useSSL=false";

    //  Database credentials
    private String USER = "USERNAME";
    private String PASS = "PASSWORD";
    
    //linked list of used statements
    Set<Statement> used_Statement;
    //linked list of used statements
    Set<PreparedStatement> used_Prepared_Statement;
    //linked list of used statements
    Set<Connection> used_Connection;

    //connection to the database
    Connection conn;

    //error handler
    private final Error errorHandler;

    public MysqlConnection(){
        this.conn = null;
        this.errorHandler = new Error();
        this.registerJDBC();
        this.used_Statement = new HashSet<>();
        this.used_Prepared_Statement = new HashSet<>();
        this.used_Connection = new HashSet<>();
    }
    
    private void registerJDBC(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            System.out.println("FAIL : Error in registering the MYSQL driver");
        }
    }
    
    public Connection getConnection(){
        try {
            this.conn = DriverManager.getConnection(this.DB_URL,this.USER,this.PASS);
        } catch (SQLException ex) {
            this.errorHandler.report(this.toString(), ex.getMessage());
        }
        used_Connection.add(this.conn);
        return this.conn;
    }
    
    public Statement getStatement(){
        //check conn
        if(this.conn == null)
            this.getConnection();
        
        Statement toReturn = null;
        try {
            toReturn = this.conn.createStatement();
        } catch (SQLException ex) {
            this.errorHandler.report(this.toString(), ex.getMessage());
        }
        used_Statement.add(toReturn);
        return toReturn;
    }
    
    public PreparedStatement getPreparedStatementWithKeys(String statement){
        //check conn
        if(this.conn == null)
            this.getConnection();
        
        PreparedStatement toReturn = null;
        try {
            toReturn = this.conn.prepareStatement(statement,PreparedStatement.RETURN_GENERATED_KEYS);
        } catch (SQLException ex) {
            this.errorHandler.report(this.toString(), ex.getMessage());
        }
        used_Prepared_Statement.add(toReturn);
        return toReturn;
    }
    
    public PreparedStatement getPreparedStatement(String statement){
        //check conn
        if(this.conn == null)
            this.getConnection();
        
        PreparedStatement toReturn = null;
        try {
            toReturn = this.conn.prepareStatement(statement);
        } catch (SQLException ex) {
            this.errorHandler.report(this.toString(), ex.getMessage());
        }
        used_Prepared_Statement.add(toReturn);
        return toReturn;
    }
    
    public void closeConnection(Connection conn){
        try {
            conn.close();
        } catch (SQLException ex) {
            this.errorHandler.report(this.toString(), ex.getMessage());
        }
        used_Connection.remove(conn);
    }
    
    public void closeStatement(Statement stm){
        try {
            stm.close();
        } catch (SQLException ex) {
            this.errorHandler.report(this.toString(), ex.getMessage());
        }
        used_Statement.remove(stm);
    }

    public void closePreparedStatement(PreparedStatement stm){
        try {
            stm.close();
        } catch (SQLException ex) {
            this.errorHandler.report(this.toString(), ex.getMessage());
        }
        used_Prepared_Statement.remove(stm);
    }
    
    public void cleanUp(){
        used_Statement.forEach((stm) -> {
	    try {
		stm.close();
	    } catch (SQLException ex) {
		this.errorHandler.report(this.toString(), ex.getMessage());
	    }
	});
        
        used_Prepared_Statement.forEach((stm) -> {
	    try {
		stm.close();
	    } catch (SQLException ex) {
		this.errorHandler.report(this.toString(), ex.getMessage());
	    }
	});
        
        used_Connection.forEach((Sconn) ->{
	    try {
		Sconn.close();
	    } catch (SQLException ex) {
		this.errorHandler.report(this.toString(), ex.getMessage());
	    }
	});
    }
    
    public static void main(String args[]){
        MysqlConnection MC = new MysqlConnection();
        
        Connection conn = MC.getConnection();
        Statement stm = MC.getStatement();
        MC.closeConnection(conn);
        MC.closeStatement(stm);
    }
    
}
