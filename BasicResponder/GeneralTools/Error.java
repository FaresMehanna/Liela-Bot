package GeneralTools;

/**
 *
 * @author Fairouz
 */
public class Error {
    
    public Error(){}
    
    public void report(String identifier, String error){
        System.out.println("Error in \"" + identifier + "\" : " + error);
    }
}
