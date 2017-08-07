package Processor.SingleMessageHandler.ObserverList;

import MessageHandler.MessageReplier;
import MessageHandler.MessageUpdater;
import MessageHandler.SingleMessage;
import Processor.User;

/**
 *
 * @author Fairouz
 */
public class ErrorRespond implements SingleObserverInterface{
    
    //database Connection
    private Mysql.MysqlConnection Mconn;

    //handlers
    private MessageReplier Mreplier;
    private MessageUpdater Mupdater;
    private User user;
    
    
    public ErrorRespond(Mysql.MysqlConnection Mconn){
        this.Mconn = Mconn;
        this.Mreplier = new MessageReplier(this.Mconn);
        this.Mupdater = new MessageUpdater(this.Mconn);
        this.user = new User(this.Mconn);
    }
    
    public boolean Observe(SingleMessage y){
        //set the replay to the message
        y.setReplay("\"text\":\""+"I don't know what do you mean, please tell me what to search for in English."+"\"");
        int id = this.Mreplier.replay(y);
        
        //update the user state
        this.user.updateState(y.getFBID(),id);
        
        //update the message state
        y.setState(1);
        this.Mupdater.UpdateState(y);
        
        //return true
        return true;
    }    
}
