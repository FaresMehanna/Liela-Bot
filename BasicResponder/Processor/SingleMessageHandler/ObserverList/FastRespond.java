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
public class FastRespond implements SingleObserverInterface{
    
    private HashMap<String, String> MessageReplay;
    private EditDistanceAlgo dist;
    private MessageReplier Mreplier;
    private MessageUpdater Mupdater;
    private User user;
    
    //database Connection
    private Mysql.MysqlConnection Mconn;
    
    public FastRespond(Mysql.MysqlConnection Mconn){
        this.MessageReplay = new HashMap<String, String>();
        this.dist = new EditDistanceAlgo();
        this.AddMess();
        this.Mconn = Mconn;
        this.Mreplier = new MessageReplier(this.Mconn);
        this.Mupdater = new MessageUpdater(this.Mconn);
        this.user = new User(this.Mconn);
    }
    
    private Word hits(String word){
        
        int best = 1000000;
	String toReturn = null;
	
	for (String key : this.MessageReplay.keySet()) {
	    
	    String test = this.MessageReplay.get(key);
	    int diff = this.dist.minDistance(key, word);
	    
	    if(diff <= 5 && diff < word.length()/2){
		if(diff < best){
		    best = diff;
		    toReturn = test;
		}
	    }
	    
	}
	
	Word x = new Word(toReturn,best);
	return x;
    }
    
    private void AddMess(){
        this.MessageReplay.put("hello", "Hello");
        this.MessageReplay.put("lielaerrorerrorerrorerrorerror", "I don't know what do you mean, please tell me what to search for in English.");
        this.MessageReplay.put("hello liela", "Hello");
        this.MessageReplay.put("hi", "Hi");
        this.MessageReplay.put("hi liela", "Hi");
        this.MessageReplay.put("hey", "Hey");
        this.MessageReplay.put("hey liela", "Hey");
        this.MessageReplay.put("bye", "Bye");
        this.MessageReplay.put("bye liela", "Bye");
        this.MessageReplay.put("ok", "Ok");
        this.MessageReplay.put("ok liela", "Ok");
        this.MessageReplay.put("good liela", "nice");
        this.MessageReplay.put("good", "nice");
        this.MessageReplay.put("you are great", "thanks");
        this.MessageReplay.put("you are great liela", "thanks");
        this.MessageReplay.put("okay", "Okay");
        this.MessageReplay.put("okay liela", "Okay");
        this.MessageReplay.put("liela", "Yes ?");
        this.MessageReplay.put("ok liela", "Ok");
        this.MessageReplay.put("how are you", "fine");
        this.MessageReplay.put("are you good", "fine");
        this.MessageReplay.put("are you good liela", "fine");
        this.MessageReplay.put("are you liela", "yes");
        this.MessageReplay.put("how are you liela", "fine");
        this.MessageReplay.put("who are you", "liela");
        this.MessageReplay.put("where are you", "Egypt");
        this.MessageReplay.put("where are you from", "Egypt");
        this.MessageReplay.put("where are you from liela", "Egypt");
        this.MessageReplay.put("what is your name", "Liela");
        this.MessageReplay.put("your name", "Liela");
        this.MessageReplay.put("good morning", "Good morning");
        this.MessageReplay.put("good morning liela", "Good morning");
        this.MessageReplay.put("good afternoon", "Good Afternoon");
        this.MessageReplay.put("good afternoon liela", "Good Afternoon");
        this.MessageReplay.put("good evening", "Good Evening");
        this.MessageReplay.put("good evening liela", "Good Evening");
        this.MessageReplay.put("see ya", "Bye");
	this.MessageReplay.put("thank you", "You are welcome");
	this.MessageReplay.put("thank u", "You are welcome");
	this.MessageReplay.put("thank you liela", "You are welcome");
	this.MessageReplay.put("thank u liela", "You are welcome");
	this.MessageReplay.put("thanks", "You are welcome");
	this.MessageReplay.put("thanks liela", "You are welcome");
        this.MessageReplay.put("see you", "Bye");
        this.MessageReplay.put("see you later", "Bye");
        this.MessageReplay.put("show me list", "Here is your list");
        this.MessageReplay.put("show me list liela", "Here is your list");
        this.MessageReplay.put("show me my list", "Here is your list");
        this.MessageReplay.put("show me my list liela", "Here is your list");
        this.MessageReplay.put("my list", "Here is your list");
        this.MessageReplay.put("my list liela", "Here is your list");
        this.MessageReplay.put("list", "Here is your list");
        this.MessageReplay.put("list liela", "Here is your list");
        this.MessageReplay.put("my track list", "Here is your list");
        this.MessageReplay.put("my track list liela", "Here is your list");
        this.MessageReplay.put("track list", "Here is your list");
        this.MessageReplay.put("track list liela", "Here is your list");
        this.MessageReplay.put("show track list", "Here is your list");
        this.MessageReplay.put("show track list liela", "Here is your list");
        this.MessageReplay.put("clear track list", "Your track list has been cleared!");
        this.MessageReplay.put("clear track list liela", "Your track list has been cleared!");
        this.MessageReplay.put("delete track list", "Your track list has been cleared!");
        this.MessageReplay.put("delete track list liela", "Your track list has been cleared!");
        this.MessageReplay.put("clear my track list", "Your track list has been cleared!");
        this.MessageReplay.put("clear my track list liela", "Your track list has been cleared!");
        this.MessageReplay.put("delete my track list", "Your track list has been cleared!");
        this.MessageReplay.put("delete my track list liela", "Your track list has been cleared!");
        this.MessageReplay.put("start123456789", "Hello, My name is Liela."
		+ " I'll help you to shop faster,"
		+ " You can start writing the product you want to search for"
		+ ", you can also make me keep an eye on specific product and tell you"
		+ " incase any price change happens to it");
    }
    
    public boolean Observe(SingleMessage y){
        Word result = this.hits(y.getMessage());
        
        if(result.getWord() == null)
            return false;
        
        //set the replay to the message
        y.setReplay("\"text\":\""+result.getWord()+"\"");
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

class Word {
    
    private String word;
    private int diff;
    
    public Word(String word, int diff){
	this.word = word;
	this.diff = diff;
    }
    
    public int getDiff(){
	return this.diff;
    }
    
    public String getWord(){
	return this.word;
    }
}