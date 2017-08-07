package Processor.SingleMessageHandler.ObserverList;

import MessageHandler.MessageReplier;
import MessageHandler.MessageUpdater;
import MessageHandler.SingleMessage;
import Processor.User;
import java.util.HashMap;

/**
 *
 * @author Fairouz
 */
public class ProductRequestRespond implements SingleObserverInterface{
    
    private HashMap<String, String> requestStarts;

    //database Connection
    private Mysql.MysqlConnection Mconn;

    //classes to execute the request
    private MessageReplier Mreplier;
    private MessageUpdater Mupdater;
    private User user;
    
    public ProductRequestRespond(Mysql.MysqlConnection Mconn){
        this.requestStarts = new HashMap<String, String>();
        this.AddMess();
        this.Mconn = Mconn;
        this.Mreplier = new MessageReplier(this.Mconn);
        this.Mupdater = new MessageUpdater(this.Mconn);
        this.user = new User(this.Mconn);
    }
    
    private String hits(String word){
		
	for (String key : this.requestStarts.keySet()) {
	    if(word.length() <= key.length())
		continue;
            
	    if(key.equals(word.substring(0,key.length()))){
                return word.substring(key.length(),word.length());
            }
	}
  
        return null;
    }
    
    private void AddMess(){
        this.requestStarts.put("iam searching for","");
        this.requestStarts.put("i am searching for","");
        this.requestStarts.put("searching for","");
        this.requestStarts.put("iam looking for","");
        this.requestStarts.put("i am looking for","");
        this.requestStarts.put("looking for","");
        this.requestStarts.put("i want to buy","");
        this.requestStarts.put("search for","");
        this.requestStarts.put("look for","");
        this.requestStarts.put("i need","");
        this.requestStarts.put("find","");
    }
    
    private String prepare(String x){
	return x.replace ("\n"," ").replace ("\t"," ");
    }
    
    public boolean Observe(SingleMessage y){
        String result = this.hits(y.getMessage());
        if(result == null)
            return false;
        
        y.setMessage("000"+this.prepare(result));

	this.Mupdater.UpdateMessage(y);
        
        y.setState(2);
        this.Mupdater.UpdateState(y);
        
	this.user.updateState(y.getFBID(), -3);
        
        return true;
    }
}
