package Processor;

import MessageHandler.SingleMessage;
import Processor.SingleMessageHandler.Observe;

/**
 *
 * @author Fairous
 */
public class SingleMessageProcessor{
    
    //Message to be handled
    private SingleMessage y;
    //connection to databae
    Mysql.MysqlConnection Mconn;
    //user Object
    User user;
    //single message handler
    Observe singleMessageHandler;
    
    public SingleMessageProcessor(SingleMessage x, Mysql.MysqlConnection Mconn){
        this.y = x;
        this.Mconn = Mconn;
        this.user = new User(this.Mconn);
        this.singleMessageHandler = new Observe(this.Mconn);
    }
    
    public void process(){
        
	if(!user.ready(y))
	    return;
	
        y.setMessage(y.getMessage().trim().replaceAll(" +", " ").toLowerCase().replace ("\"","\\\"").replace ("\n","\\\n"));
        this.singleMessageHandler.observe(y);
        
    }

}
