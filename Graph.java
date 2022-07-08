import java.util.*;
import java.lang.Math;

/**
 * @author Paulina Trifonova
 * @version 01
 */
public class Graph
{
    Map<Player, List<Player>> adjVertices;
    int numPlayers;
    int numEdges;
    List<Player> playerList;
    
    /**
     * Constructor for objects of class Graph
     */
    public Graph()
    {
        // initialise instance variables
        numPlayers = 0;
        numEdges = 0;
    }

    public void addPlayer(String label, double intensity, double quality){
        Player p = new Player(label, intensity, quality); //PROBLEM REFERENCING THE OBJECT P IDK WHY
        adjVertices.putIfAbsent(p, new ArrayList<>());
        numPlayers++;
        playerList.add(p);
    }
    
    public void addPlayer(Player p){
        adjVertices.putIfAbsent(p, new ArrayList<>());
        numPlayers++;
        playerList.add(p);
    }
    
    public void removePlayer(String label, double intensity, double quality){
        Player p = new Player(label, intensity, quality);
        adjVertices.values().stream().forEach(e -> e.remove(p));
        adjVertices.remove(new Player(label, intensity, quality));
        numPlayers--;
        playerList.remove(findInPlayerArray(p));
    }
    
    private int findInPlayerArray(Player p){
        for(int i = 0; i<numPlayers; i++){
            if(playerList.get(i)==p){
                return i;
            }
        }
        return -1;
    }
    
    public void addEdge(Player p1, Player p2){
        adjVertices.get(p1).add(p2);
        adjVertices.get(p2).add(p1);
        numEdges++;
    }
    
    public void removeEdge(Player p1, Player p2){
        List<Player> eP1 = adjVertices.get(p1);
        List<Player> eP2 = adjVertices.get(p2);
        if(eP1 != null){
            eP1.remove(p2);
        }
        if(eP2 != null){
            eP2.remove(p1);
        }
        numEdges--;
    }
    
    public int getNumEdges(){
        return this.numEdges;
    }
    
    public List<Player> getAdjVertices(String l, double i, double q){
        return adjVertices.get(new Player(l,i,q));
    }
    
    public List<Player> getAdjVertices(Player p){
        return adjVertices.get(p);
    }
    
    public double getConflictScore(Player p1, Player p2){ //More Details found in Conflict Equation Spreadsheet
        double qDif = Math.sqrt(Math.abs(p1.getQuality()-p2.getQuality()));
        double iSum = p1.getIntensity()+p2.getIntensity();
        double iSumSq = iSum*iSum;
        return qDif*iSumSq;
    }
    
    public double getHueInfluence(Player p1, Player p2){ //influence of player 2 on player 1
        double qDif = Math.sqrt(Math.abs(p1.getQuality()-p2.getQuality()));
        return (1-p1.getIntensity())*p2.getIntensity()*qDif;
    }
    
    public List<Player> getPlayerList(){
        return this.playerList;
    }
    
    public int getNumPlayers(){
        return this.numPlayers;
    }
}
