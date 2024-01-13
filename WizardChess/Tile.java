import greenfoot.*;
public class Tile extends Actor
{
    private Piece occupyingPiece;
    private int r,c,status;//0: normal, 1:burning, 2:idk make something up
    private boolean isNew=false,isBlue=false, isGreen=false;
    
    public void addedToWorld(World w){
        if(!isNew){//prevent z sort problems
            isNew=true;
            BoardManager.placeTile(this,r,c);
        }
    }
    public Tile(int r,int c){
        this.r=r;
        this.c=c;
        turnNormal();
    }
    public void act()
    {
        //if(occupyingPiece!=null&&occupyingPiece.isDying())occupyingPiece=null;
        
        //if(Greenfoot.mouseClicked(this)){
            //if(Game.isSpellActivated()){ 
                // spell thing goes here
                // for now its magic fire but must change to be reusable
                
                //Spell spell = new Spell(Spell.getSpellType());
                //getWorld().addObject(spell, getX(), getY());
                // deactivate spell here? right now its deactivated in spell class
            //}
        //}
    }
    public void turnRed(){
        setImage(new GreenfootImage("Tile_"+(((r+c)%2==0)?1:0)+"_r.png"));
    }
    public void turnGreen(){
        setImage(new GreenfootImage("Tile_"+(((r+c)%2==0)?1:0)+"_g.png"));
        isGreen=true;
    }
    public void turnBlue(){
        setImage(new GreenfootImage("Tile_"+(((r+c)%2==0)?1:0)+"_b.png"));
        isBlue=true;
    }
    public void turnNormal(){
        setImage(new GreenfootImage("Tile_"+(((r+c)%2==0)?1:0)+".png"));
        isBlue=false;
    }
    public boolean isBlue() {
        return isBlue;
    }
    public boolean isGreen() {
        return isGreen;
    }
    public Piece getOccupyingPiece(){
        return occupyingPiece;
    }
    public void placePiece(Piece p){
        getWorld().addObject(p,p.getTargetH(),p.getTargetV()-30);//-30 for now
        occupyingPiece=p;
    }
    public void empty(){
        occupyingPiece=null;
    }
}
