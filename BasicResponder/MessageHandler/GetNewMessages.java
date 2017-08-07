package MessageHandler;

import java.sql.*;
import java.util.LinkedList;
import java.util.Queue;
import GeneralTools.Timer;
import GeneralTools.Error;

/**
 *
 * @author Fairouz
 */
public class GetNewMessages{
    
    private Mysql.MysqlConnection MC = null;
    private final Timer timer;
    private final Error errorHandler;
    
    public GetNewMessages(){
        this.timer = new Timer();    //timer to get execution time for the database query
        this.errorHandler = new Error();     //error reporter
    }
    
    public long getExecutionTime(){
        return this.timer.getTime();
    }
    
    public boolean readFromDataBase(Queue<SingleMessage> s,int limit){
        
        //get connection
        this.MC = new Mysql.MysqlConnection();
        
        //get statement to be used
        Statement stmt = this.MC.getStatement();

        //set starting time
        this.timer.startTimer();
        
        //query
        String sql = "SELECT messages.id, messages.fbid, messages.state, messages.message_text FROM messages INNER JOIN Users ON Users.fbid = messages.fbid WHERE messages.state = 0 AND Users.state = '-2' ORDER BY messages.ID ASC LIMIT " + limit ;
        
        //try to execute the query
        ResultSet rs = null;    //result set to hold the data
        try {
            rs = stmt.executeQuery(sql);
        } catch (SQLException ex) {
            this.errorHandler.report(this.toString(), ex.getMessage());
        }
        
        //is there data in the database ?
        boolean dataFound = false;
        
        //get the data from the result set if the query executed
        try {
            while(rs.next()){
                //set data found to true
                dataFound = true;
                
                //Retrieve the data
                int id  = rs.getInt("id");
                long fbid = rs.getLong("fbid");
                int state = rs.getInt("state");
                String messageText = rs.getString("message_text");
                
                //add the data to the queue
                s.add(new SingleMessage(id,fbid,state,messageText));
                
            }
        } catch (SQLException ex) {
            this.errorHandler.report(this.toString(), ex.getMessage());
        }

        //close connections & all it's statements
        this.MC.cleanUp();
        this.MC = null;
        
        //set the ending time
        this.timer.endTimer();
        
        //return if the data found
        return dataFound;
    }
    
    public static void main(String args[]){
       GetNewMessages x = new GetNewMessages();
       Queue<SingleMessage> y = new LinkedList();
       
        x.readFromDataBase(y, 10000);
        
        //System.out.println("ExecutionTime : " + x.getExecutionTime()/1000000);
        
        for(int i=0;i<y.size();i++){
            //System.out.println(y.peek().getId());
            y.remove();
        }
    }
}
