
/**
 * Write a description of class Player here.
 *
 * @author Paulina Trifonova
 * @version 01
 */
public class Player
{
    private String label;
    private double intensity; //number from 0 to 1. Represents intensity of beliefs
    private double quality; //number from 0 to 100. Represents the type of ideology
    private boolean isPerson;
    
    /**
     * Constructor for objects of class Player
     */
    public Player(String l, double i, double q)
    {
        this.label = l;
        this.intensity = i;
        this.quality = q;
    }
    
    //Get Methods
    /**
     * @param   none
     * @return  label
     */
    public String getLabel()
    {
        // put your code here
        return this.label;
    }
    /**
     * @param   none
     * @return  intensity
     */
    public double getIntensity()
    {
        // put your code here
        return this.intensity;
    }
    /**
     * @param   none
     * @return  quality
     */
    public double getQuality()
    {
        // put your code here
        return this.quality;
    }
    
    //Set Methods
    /**
     * @param   new label
     * @return  none
     */
    public void setLabel(String l)
    {
        // put your code here
        this.label = l;
    }
    /**
     * @param   new intensity
     * @return  none
     */
    public void setIntensity(double i)
    {
        // put your code here
        this.intensity = i;
    }
    /**
     * @param   new quality
     * @return  none
     */
    public void setQuality(double q)
    {
        // put your code here
        this.quality = q;
    }
    public void setPerson(){
        this.isPerson = true;
    }
    public void setInstitution(){
        this.isPerson = false;
    }
}
