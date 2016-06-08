/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libupsystem;

//TODO supprimer cette horrible classe et réimplémenter correctement, armes, armures et tables de référence


public class BiValue {
    
    private int x,y;
    
    public BiValue(int x, int y)
    {
        this.x=x;
        this.y=y;
    }
    
    public BiValue(BiValue point)
    {
        this.x=point.getX();
        this.y=point.getY();
    }

    /**
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(int y) {
        this.y = y;
    }
    
    
    
}
