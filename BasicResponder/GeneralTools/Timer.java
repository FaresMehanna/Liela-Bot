package GeneralTools;

/**
 *
 * @author Fairouz
 */
public class Timer {
    
    private long startTime;
    private long endTime;
    
    public Timer(){}
    
    public void startTimer(){
        this.startTime = System.nanoTime();
    }
    
    public void endTimer(){
        this.endTime = System.nanoTime();
    }
    
    public long getTime(){
        return startTime-endTime;
    }
}
