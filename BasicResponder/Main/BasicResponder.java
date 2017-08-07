package Main;

/**
 *
 * @author Fairouz
 */
public class BasicResponder {

    public static void main(String[] args) {
        
        //The messages Handler
        Processor.MessageProcessor.messageProcessor.start();
        
        //The messages updater
        MessageHandler.AIMD.AIMDGetter.start();
    
    }

}
