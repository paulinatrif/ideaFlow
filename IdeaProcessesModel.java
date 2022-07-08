import java.util.*;

public class IdeaProcessesModel
{
    // instance variables 
    private int disagreeThreshold = 25; //Used to determine type of idea exchange
    private int agreeThreshold = 10;
    private int breakThreshold = 10; //Used for breaking connections
    private double connectionThreshold = 0.5;
    private double ir = 0.1; //radicalization factor used in shifting intensities
    private double hr = 1; //how much hue radicalizes by
    
    //Constructor
    public IdeaProcessesModel(int dt, int at, int bt, double ct, double i, double h){
        disagreeThreshold = dt; //Used to determine type of idea exchange
        agreeThreshold = at;
        breakThreshold = bt; //Used for breaking connections
        connectionThreshold = ct;
        ir = i; //radicalization factor used in shifting intensities
        hr = h; //how much hue radicalizes by
    }
    
    public IdeaProcessesModel(){
    }
    
    //Main method
    public void runModel(int numPlayers, int numPeople){
        Graph community = makeCommunity(numPlayers, numPeople);
        print(community);
    }
    
    public void print(Graph community){
        System.out.println("Number of Players: " + community.getNumPlayers());
        System.out.println("Number of Connections: " + community.getNumEdges());        
    }
    
    public Graph makeCommunity(int numPlayers, int numPeople){
        Graph community = new Graph();
        for(int i=0; i<numPlayers; i++){
            //community.addPlayer(makePerson("p"+i));
            community.addPlayer("a", 0.5, 50);
        }
        
        return community;
    }   
   
    /*
    //Helper method to create a person
    // takes in label string
    // generates random quality
    // generates random intensity
    // returns a person
    */
    public Player makePerson(String label){
        Player p = new Player(label, Math.random(), (int)(100*Math.random()));
        p.setPerson();
        return p;
    }
    
    //Method to cycle through a single step
    //[FOR EACH PLAYER IN THE GRAPH]
    //1. invoke connection severing method
    //2. invoke idea flow method
    //3. invoke new connections method
    
    public void Step(Graph community){
        List<Player> allPlayers = community.getPlayerList();
        //STEP 1
        for(int i=0; i<allPlayers.size();i++){
            List<Player> connections = community.getAdjVertices(allPlayers.get(i));
            for(int j=0; j<connections.size(); j++){
                compatibilityCheck(community, allPlayers.get(i), connections.get(j));
            }    
        }
        //STEP 2
        for(int i=0; i<allPlayers.size();i++){
            List<Player> connections = community.getAdjVertices(allPlayers.get(i));
            for(int j=0; j<connections.size(); j++){
                exchangeIdeas(community, allPlayers.get(i), connections.get(j));
            }
        }
        //STEP 3
        for(int i=0; i<allPlayers.size();i++){
            newConnections(community, allPlayers.get(i));
        }
    }   
       
    /**/  
    //connection severing
    //1. Check if score is above a threshold
    //2. Remove connection
    public void compatibilityCheck(Graph community, Player p1, Player p2){
        double conflictScore = community.getConflictScore(p1, p2);
        if(conflictScore>breakThreshold){
            community.removeEdge(p1,p2);
        }
    }
    
    public void influenceHue(Graph community, Player p1, Player p2){
        double p1Change = community.getHueInfluence(p1, p2);
        double p2Change = community.getHueInfluence(p2, p1);
        if(p1.getQuality()>p2.getQuality()){
            p1.setQuality(p1.getQuality()-p1Change);
            p2.setQuality(p2.getQuality()+p1Change);
        }
        else{
            p1.setQuality(p1.getQuality()+p1Change);
            p2.setQuality(p2.getQuality()-p1Change);    
        }
    }
    
    public void influenceIntensity(Graph community, Player p1, Player p2){
        if(Math.abs(p1.getQuality()-p2.getQuality())<agreeThreshold){
            radicalizeHue(community, p1, p2);
            p1.setIntensity(p1.getIntensity()+ir);
            p2.setIntensity(p2.getIntensity()+ir);
        }
        else if(Math.abs(p1.getQuality()-p2.getQuality())>disagreeThreshold){
            p1.setIntensity(p1.getIntensity()-ir);
            p2.setIntensity(p2.getIntensity()-ir);
        }
    }
    
    //Method to exchange ideas
    //ASSUMES ALL CONNECTIONS ARE COMPATIBLE
    // TO ADD: IF THE PLAYER IS AN INSTITUTION IT CANNOT BE AFFECTED BUT IT CAN AFFECT OTHERS
    public void exchangeIdeas(Graph community, Player p1, Player p2){
        influenceHue(community, p1, p2);
        influenceIntensity(community, p1, p2);
    }
    
    public void newConnections(Graph community, Player p){
        //Define local variables in this method
        Queue<Player> friends = new LinkedList<>(); //Holds immediate connection
        HashMap<Player, Integer> degreeTwo = new HashMap<>(); //Creates a hashmap with the second degree connections and how many times each appears
        int numFriends; //Original Number of Friends
        List<Player> Hold;
        List<Player> Hold2;
        Player current;
        
        //Load All First Degree Connections into the Queue
        Hold = community.getAdjVertices(p);
        for(int i=0; i < Hold.size(); i++){
            friends.add(Hold.get(i));
        }
        numFriends = Hold.size();
        
        //for every player in the queue if not present, add to hashmap with value 1, if present update the value to be value+1
        while(friends.peek() != null){
            current = friends.remove();
            Hold2 = community.getAdjVertices(current);
            for(int i=0; i < Hold2.size(); i++){
                if(!degreeTwo.containsKey(current)){
                    degreeTwo.put(current, 1);
                }
                else{
                    degreeTwo.replace(current, degreeTwo.get(current)+1);
                }
            }
        }
        
        //For every player in the hashmap, if value/numfriends>threshold make a new connection
        degreeTwo.forEach((Player, Integer) ->
            {
                if(Integer/numFriends > connectionThreshold){
                    community.addEdge(p, Player);
                }
            }
        );
    }
    
    
    public void radicalizeHue(Graph community, Player p1, Player p2){
        if(p1.getQuality() > 50 && p2.getQuality() > 50){
            p1.setQuality(p1.getQuality()+hr);
            p2.setQuality(p2.getQuality()+hr);
        }
        else if(p1.getQuality() < 50 && p2.getQuality() < 50){
            p1.setQuality(p1.getQuality()+hr);
            p2.setQuality(p2.getQuality()+hr);
        }
    }
}
