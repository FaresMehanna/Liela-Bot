package MessageHandler;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import GeneralTools.Error;

/**
 *
 * @author Fairouz
 */
public class MessageUpdater{
    
    private final Error errorHandler;
    private final Mysql.MysqlConnection Mconn;
    
    public MessageUpdater(Mysql.MysqlConnection Mconn){
        this.errorHandler = new GeneralTools.Error();
        this.Mconn = Mconn;
    }
    
    public void UpdateState(SingleMessage x){
        //query
        String sql = "UPDATE messages set state = ? WHERE id = ?";
        //make the update
	try {	    
	    //get prepared statement
            PreparedStatement Pstmt = this.Mconn.getPreparedStatement(sql);
            //set the parameters
	    Pstmt.setInt(1, x.getState());
	    Pstmt.setInt(2,x.getId());
            //update
            Pstmt.executeUpdate();
	    
        } catch (SQLException ex) {
            this.errorHandler.report(this.toString(), ex.getMessage());
        }
    }
    
    public void UpdateMessage(SingleMessage x){
        //query
        String sql = "UPDATE messages set message_text = ? WHERE id = ?";
        //make the update in the message string
	try {
            //get prepared statement
	    PreparedStatement Pstmt = this.Mconn.getPreparedStatement(sql);
            //set the parameters
	    Pstmt.setString(1, x.getMessage());
	    Pstmt.setInt(2,x.getId());
            //make the update
            Pstmt.executeUpdate();
        } catch (SQLException ex) {
            this.errorHandler.report(this.toString(), ex.getMessage());
        }
    }
}
