package Processor.SingleMessageHandler.ObserverList;

import MessageHandler.MessageReplier;
import MessageHandler.MessageUpdater;
import MessageHandler.SingleMessage;
import Processor.User;

/**
 *
 * @author Fairous
 */
public class TrackProductRequests implements SingleObserverInterface{
    
    //database Connection
    private Mysql.MysqlConnection Mconn;

    //handlers
    private MessageReplier Mreplier;
    private MessageUpdater Mupdater;
    private User user;
    
    
    public TrackProductRequests(Mysql.MysqlConnection Mconn){
        this.Mconn = Mconn;
        this.Mreplier = new MessageReplier(this.Mconn);
        this.Mupdater = new MessageUpdater(this.Mconn);
        this.user = new User(this.Mconn);
    }
    
    private String prepare(String x){
	return x.replace ("\n"," ").replace ("\t"," ");
    }
    
    public boolean Observe(SingleMessage y){
        
	if(y.getMessage().length() >= 10 && y.getMessage().substring(0, 10).equals(new String("lielatrack"))){
            
            y.setMessage("000"+this.prepare(y.getMessage()));
            this.Mupdater.UpdateMessage(y);
            
            y.setState(2);
            this.Mupdater.UpdateState(y);
            
	    user.updateState(y.getFBID(), -3);
            return true;
	
        }
        
        //return true
        return false;
    }    
}
