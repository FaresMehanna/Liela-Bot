package Processor;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import MessageHandler.SingleMessage;

/**
 *
 * @author Fairouz
 */
public class User{
    
    private int state;
    private Mysql.MysqlConnection Mconn;
    
    public User(Mysql.MysqlConnection Mconn){
        this.state = -1;
        this.Mconn = Mconn;
    }
    
    public boolean ready(SingleMessage y){
	synchronized(User.class){
            //query
	    String sql = "SELECT state from Users where fbid = ?";
            
            //resultset to store the data
	    ResultSet rs = null;
            
            //execute the statement
	    try {
		PreparedStatement Pstmt = this.Mconn.getPreparedStatement(sql);
                //set parameters
		Pstmt.setLong(1, y.getFBID());
                //execute
		rs = Pstmt.executeQuery();
	    } catch (SQLException ex) {
		 System.out.println("Error in mysql" + ex.getMessage());
	    }

	    try {
		if(rs != null && rs.next()){
		    //Retrieve the data
		    this.state = rs.getInt("state");
		}
	    } catch (SQLException ex) {
		 System.out.println("Error in mysql" + ex.getMessage());
	    }

	    if(this.state == -2)
		return true;
	    else
		return false;
	}
    }
    
    public void updateState(long fbid, int state){
        
        //query
        String sql = "UPDATE Users SET state = ?, time = ? WHERE fbid = ?";
        
        try {
            //get prepared statement
            PreparedStatement Pstmt = this.Mconn.getPreparedStatement(sql);
	    
            //set parameters
	    Pstmt.setInt(1, state);
	    Pstmt.setLong(2, System.currentTimeMillis());
	    Pstmt.setLong(3, fbid);
	    
            //execute
            Pstmt.executeUpdate();
	    
        } catch (SQLException ex) {
             System.out.println("Error in mysqli " + ex.getMessage());
        }
    }
}
