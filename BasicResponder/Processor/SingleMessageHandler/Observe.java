package Processor.SingleMessageHandler;

import Processor.SingleMessageHandler.ObserverList.*;
import MessageHandler.SingleMessage;
import java.util.ArrayList;

/**
 *
 * @author Fairouz
 */
public class Observe {
    
    //database Connection
    private Mysql.MysqlConnection Mconn;
    
    public Observe(Mysql.MysqlConnection Mconn){
        this.Mconn = Mconn;
        this.Initialize();
    }
    
    private ArrayList<SingleObserverInterface> observers = new ArrayList();
    
    private void addToList(SingleObserverInterface observer){
        this.observers.add(observer);
    }
    
    public void observe(SingleMessage y){
        //try to get a match for the message to be handled
        for(int i=0;i<this.observers.size();i++)
            if(this.observers.get(i).Observe(y))
                return;
        //if there is no match then there is an error
        new ErrorRespond(this.Mconn).Observe(y);
    }
    
    private void Initialize(){
	this.addToList(new FastRespond(this.Mconn));
	this.addToList(new ProductRequestRespond(this.Mconn));
	this.addToList(new TrackProductRequests(this.Mconn));
	this.addToList(new productPaginationRequest(this.Mconn));
    }
}
