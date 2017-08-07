package MessageHandler;

/**
 *
 * @author Fairouz
 */
public class SingleMessage {
    
    private int id;
    private long FBID;
    private int state;
    private String message;
    private String replay;
    
    public SingleMessage(int id, long FBID, int state, String message){
        this.id = id;
        this.FBID = FBID;
        this.state = state;
        this.message = message;
    }
    
    public SingleMessage(){}
    
    public void setId(int id){
        this.id = id;
    }
    
    public void setFBID(long FBID){
        this.FBID = FBID;
    }
    
    public void setState(int state){
        this.state = state;
    }
    
    public void setMessage(String message){
        this.message = message;
    }
    
    public void setReplay(String replay){
        this.replay = replay;
    }
    
    
    
    public String getReplay(){
        return this.replay;
    }
    
    public int getId(){
        return this.id;
    }
    
    public long getFBID(){
        return this.FBID;
    }
    
    public int getState(){
        return this.state; 
    }
    
    public String getMessage(){
        return this.message;
    }
}