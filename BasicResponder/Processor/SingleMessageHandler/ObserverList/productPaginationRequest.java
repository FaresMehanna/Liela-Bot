package Processor.SingleMessageHandler.ObserverList;

import MessageHandler.MessageReplier;
import MessageHandler.MessageUpdater;
import MessageHandler.SingleMessage;
import Processor.User;

/**
 *
 * @author Fairous
 */
public class productPaginationRequest implements SingleObserverInterface{
    
    //database Connection
    private Mysql.MysqlConnection Mconn;

    //handlers
    private MessageReplier Mreplier;
    private MessageUpdater Mupdater;
    private User user;
    
    
    public productPaginationRequest(Mysql.MysqlConnection Mconn){
        this.Mconn = Mconn;
        this.Mreplier = new MessageReplier(this.Mconn);
        this.Mupdater = new MessageUpdater(this.Mconn);
        this.user = new User(this.Mconn);
    }
    
    private String prepare(String x){
	return x.replace ("\n"," ").replace ("\t"," ");
    }
    
    private boolean isNumeric(String toCheck){
	for(int i=0;i<toCheck.length();i++)
	    if(toCheck.charAt(i) > 'z' || toCheck.charAt(i) > 'a')
		return false;
	return true;
    }
    
    public boolean Observe(SingleMessage y){
        
        if(y.getMessage().length() >= 18 && y.getMessage().substring(0, 15).equals(new String("lielapagination")) && this.isNumeric(y.getMessage().substring(15, 18))){

            y.setMessage(y.getMessage().substring(15, 18) + this.prepare(y.getMessage().substring(18,y.getMessage().length())));
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
