import greenfoot.*;
public class Tile extends Actor
{
    private Piece occupyingPiece;
    private int r,c,status;//0: normal, 1:burning, 2:idk make something up
    private boolean isNew=false;
    public void addedToWorld(World w){
        if(!isNew){//prevent z sort problems
            isNew=true;
            BoardManager.placeTile(this,r,c);
        }
    }
    public Tile(int r, int c){
        this.r=r;
        this.c=c;
        turnNormal();
    }
    public void turnRed(){
        setImage(new GreenfootImage("Tile_"+(((r+c)%2==0)?1:0)+"_r.png"));
    }
    public void turnNormal(){
        setImage(new GreenfootImage("Tile_"+(((r+c)%2==0)?1:0)+".png"));
    }
    public Piece getOccupyingPiece(){
        return occupyingPiece;
    }
    public void placePiece(Piece p){
        getWorld().addObject(p,p.getTargetH(),p.getTargetV()-30);//-30 for now
        occupyingPiece=p;
    }
}
