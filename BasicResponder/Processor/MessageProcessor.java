package Processor;

import java.util.concurrent.ConcurrentLinkedQueue;
import MessageHandler.AIMD;
import MessageHandler.SingleMessage;

/**
 *
 * @author Fairouz
 */
public class MessageProcessor extends Thread{
    
    //Message to be processed
    private final ConcurrentLinkedQueue<SingleMessage> ML = AIMD.messagesBufferFromDatabase;
    
    //Mysql connection
    Mysql.MysqlConnection Mconn;
    
    //Singleton design pattern
    public final static MessageProcessor messageProcessor = new MessageProcessor();
    private MessageProcessor(){}
    
    private void process(){
        while(true){
            
            while(ML.isEmpty()){
		//Notify the AIMD to work
		synchronized(MessageHandler.AIMD.AIMDGetter){
		    MessageHandler.AIMD.AIMDGetter.notifyAll();
		}
                
		//SLEEP
		synchronized(this){
		    try {this.wait();} catch (InterruptedException ex) {System.out.println("ERROR IN SLEEPING PORCESSOR");}
		}
	    }
            
            //set the database & user object
            this.Mconn = new Mysql.MysqlConnection();
            
            while(!ML.isEmpty()){
		new SingleMessageProcessor(ML.peek(),this.Mconn).process();
                ML.remove();   
            }
            
            //clean up the objects
            this.Mconn.cleanUp();
            this.Mconn = null;
        }
    }
    
    synchronized public void Not(){
        this.notifyAll();
    }
    
    @Override
    public void run() {
        this.process();
    }
    
    public static void main(String args[]){
        MessageProcessor.messageProcessor.start();
    }
}
