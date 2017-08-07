package MessageHandler;
import MessageHandler.SingleMessage;
import MessageHandler.GetNewMessages;
import static java.lang.Math.floor;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import Processor.MessageProcessor;

/**
 *
 * @author Fairous
 */
public class AIMD  implements Runnable {
    
    //constants
    private double allowedOutOfTimeRequestsPercentage = 0.2;
    private long max = 30;    //max and min requests
    private long min = 5;
    
    
    //requests counetrs
    private long Requests;
    private long requestesToBeExecuted;
    private long allowedOutOfTimeRequests;
    
    //requesting timing
    private double singleRequestTimeLimit;
    private double totalTimeTakenPerRequests;
    
    //Thread to run
    private Thread t;
    
    //Message getter from the database
    private final GetNewMessages messagesUpdater;
    
    //Buffer to hold the messages
    public final static ConcurrentLinkedQueue<SingleMessage> messagesBufferFromDatabase = new ConcurrentLinkedQueue();

    //The Instance
    public static AIMD AIMDGetter = new AIMD();
    
    private AIMD(){
        this.Requests = 10;
        this.messagesUpdater = new GetNewMessages();
        this.updateAll();
    }
    
    private void increaseReq(){
	System.out.println(this.Requests);
        //if it's max don't do anything
        if(this.Requests == this.max){
	    this.updateAll();
            return;
	}
        
        this.Requests++;
        this.updateAll();
    }
    
    private void halfTimeSleep(double currTime, double time){
        if(currTime < time){
            this.totalTimeTakenPerRequests += time-currTime;
            this.Sleep((long) floor((time*1000-currTime*1000)));
        }
    }
    
    private void fullTimeSleep(){
        if(this.totalTimeTakenPerRequests < 1){
            this.Sleep((long) floor(1000-this.totalTimeTakenPerRequests*1000));
        }
    }
    
    private void Sleep(long n){
        try {
            Thread.sleep(n);
        } catch (InterruptedException ex) {
            Logger.getLogger(AIMD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void halveDown(){
        //if it's min don't do anything
        if(this.Requests == this.min){
	    this.updateAll();
	    return;
	}
        this.Requests = (long) (0.5 * this.Requests);
        this.updateAll();
    }
    
    private void updateAll(){
        this.singleRequestTimeLimit = 1.0/this.Requests;
        this.requestesToBeExecuted = this.Requests;
        this.allowedOutOfTimeRequests = (long) (allowedOutOfTimeRequestsPercentage * this.Requests);
    }
    
    private void updateMessages(){
        while(true){

            //if the requests in this second is done start a new one
            if(requestesToBeExecuted == Requests)
                totalTimeTakenPerRequests = 0;
            
            //if we get data then sleep the AIMD and start the processor
	    while(messagesBufferFromDatabase.size() > 0){
                //start the processor of the messages
	        synchronized(Processor.MessageProcessor.messageProcessor){
		    Processor.MessageProcessor.messageProcessor.notifyAll();
	        }
	        //sleep the AIMD
	        synchronized(this){
		    try {this.wait();} catch (InterruptedException ex) {}
	        }
	    }
	    
            //read the data from the database
            messagesUpdater.readFromDataBase(messagesBufferFromDatabase, 1000);
            //time taken by the database read
            double currTime = (double) messagesUpdater.getExecutionTime()/1000000000;
            //add the time to the total time for the requests in this second
            totalTimeTakenPerRequests += currTime;

            //if the process done in it's time limit
            if(currTime < singleRequestTimeLimit){
                
                //if it's too soon then sleep for some time
                halfTimeSleep(currTime,singleRequestTimeLimit);
                
                //if the all request done increase the requests/second
                if(--requestesToBeExecuted == 0){
                    increaseReq();
                }
                
            }else{
                //if the time taken more than twice the allowed halve down
                if(currTime > singleRequestTimeLimit*2)
                    halveDown();
                //if the number allowed for take more than average exhausted
                else if(--allowedOutOfTimeRequests == 0)
                    halveDown();
            }
        }
    }
    
    synchronized public void Not(){
	this.notifyAll();
    }
    
    public static void main(String args[]){
        AIMD.AIMDGetter.start();
    }
    
    public void start () {
      if (t == null) {
         t = new Thread (this, "AIMD");
         t.start();
        }
    }
    
    @Override
    public void run() {
        this.updateMessages();
    }
}
