package MessageHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import GeneralTools.Error;

/**
 *
 * @author Fairouz
 */
public class MessageReplier{
    
    private final Error errorHandler;
    private final Mysql.MysqlConnection Mconn;
    
    public MessageReplier(Mysql.MysqlConnection Mconn){
        this.errorHandler = new Error();
        this.Mconn = Mconn;
    }

    public int replay(SingleMessage x){
        //the query
        String sql = "INSERT INTO Rmessages (messId,replay,state) VALUES (?,?,0)";
        //make the insertion
        try {
	    //get the prepared statement to be executed
            PreparedStatement Pstmt = this.Mconn.getPreparedStatementWithKeys(sql);
	    //set the parameters
            Pstmt.setInt(1, x.getId());
	    Pstmt.setString(2,x.getReplay());
            //execute the insert
            Pstmt.executeUpdate();
	    //return the Id of the new record
	    try (ResultSet Ids = Pstmt.getGeneratedKeys()){
	       if(Ids.next())
		   return Ids.getInt(1);
	    }
        } catch (SQLException ex) {
            this.errorHandler.report(this.toString(), ex.getMessage());
        }
	return -2;
    }
    
}
