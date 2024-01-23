import greenfoot.*;
/**
 * Useful toolkit
 * 
 * @author Jimmy Zhu
 * @version January 22nd, 2023
**/
//add api for the rest of them pls
public class Utility
{
    //creates an image that is customized based on various values
    public static class Vector{
        private double x,y;
        public Vector(double a, double b){
            x = a;
            y = b;
        }
        public double xPart(){
            return x;
        }
        public double yPart(){
            return y;
        }
        public double magnitude(){
            return Math.sqrt(x*x+y*y);
        }
        public Vector getUnitVector(){
            return new Vector(x/magnitude(),y/magnitude());
        }
        public double dot(Vector v){
            return x*v.x + y*v.y;
        }
        public double cross(Vector v){
            return x*v.y - y*v.x;
        }
        public Vector scale(double s){
            return new Vector(x*s,y*s);
        }
        public Vector subtract(Vector v){
            return new Vector(x-v.x,y-v.y);
        }
        public Vector add(Vector v){
            return new Vector(x+v.x,y+v.y);
        }
    }
    public static GreenfootImage customizeAndCreate(int w, int h, String s){
        GreenfootImage gfi = new GreenfootImage(s);
        gfi.scale(w,h);
        return gfi;
    }
    public static GreenfootImage customizeAndCreate(int w, int h, String s, int t){
        GreenfootImage gfi = new GreenfootImage(s);
        gfi.setTransparency(t);
        gfi.scale(w,h);
        return gfi;
    }
    public static GreenfootImage customizeAndCreate(String s, int t){
        GreenfootImage gfi = new GreenfootImage(s);
        gfi.setTransparency(t);
        return gfi;
    }
    //modifies an image based on various values
    public static GreenfootImage customize(int w, int h, GreenfootImage gfi){
        gfi.scale(w,h);
        return gfi;
    }
    public static GreenfootImage customize(int w, int h, GreenfootImage gfi, int t){
        gfi.setTransparency(t);
        gfi.scale(w,h);
        return gfi;
    }
    public static GreenfootImage customize(int w, int h, GreenfootImage gfi, int t, int r){
        gfi.setTransparency(t);
        gfi.scale(w,h);
        gfi.rotate(r);
        return gfi;
    }
    public static GreenfootImage customize(GreenfootImage gfi, int t){
        gfi.setTransparency(t);
        return gfi;
    }
    public static GreenfootImage customize(GreenfootImage gfi, int t, int r){
        gfi.setTransparency(t);
        gfi.rotate(r);
        return gfi;
    }
    //creates a rectangle or square with properties depending on values
    public static GreenfootImage createBox(int w, int h, int t, Color c){
        GreenfootImage gfi = new GreenfootImage(w,h);
        gfi.setColor(c);
        gfi.fill();
        gfi.setTransparency(t);
        return gfi;
    }
    //creates text depending on values
    public static GreenfootImage createText(int w, int h, int s, String font, String text){
        GreenfootImage gfi = new GreenfootImage(w, h);
        gfi.setColor(Color.WHITE);
        gfi.setFont(new Font(font, true, false, s/text.length())); 
        gfi.drawString(text, w/4, h/2);
        return gfi;
    }
    //finds what direction in degrees point B is at from point A using bearing from point A.
    public static double bearingDegreesAToB(double x1, double y1, double x2, double y2){
        if(x2>x1&&y2>y1)return Math.atan((x2-x1)/(y2-y1))*(180.0/Math.PI)+270;
        if(x2>x1&&y2<y1)return 90-Math.atan((x2-x1)/(y1-y2))*(180.0/Math.PI);
        if(x2<x1&&y2<y1)return Math.atan((x1-x2)/(y1-y2))*(180.0/Math.PI)+90;
        if(x2<x1&&y2>y1)return 270-Math.atan((x1-x2)/(y2-y1))*(180.0/Math.PI);
        if(x2>x1&&y2==y1)return 0;
        if(x2<x1&&y2==y1)return 180;
        if(x2==x1&&y2<y1)return 90;
        return 270;
    }
    //turns degrees into cardinal direction
    public static int direction(double angle){
        //angle based on unconventional 0~359 bearing that starts at E and goes CCW. E=0, NE=1, N=2, NW=3, W=4, SW=5, S=6, SE=7
        if(angle<=22.5||angle>337.5)return 2;
        if(angle<=67.5&&angle>22.5)return 1;
        if(angle<=112.5&&angle>67.5)return 0;
        if(angle<=157.5&&angle>112.5)return 7;
        if(angle<=202.5&&angle>157.5)return 6;
        if(angle<=247.5&&angle>202.5)return 5;
        if(angle<=292.5&&angle>247.5)return 4;
        if(angle<=337.5&&angle>292.5)return 3;
        return -1;
    }
    //this is used to move with the slime(player) depending on the frame
    public static int adjustToFrame(int y, int frame){
        if(frame==0)return y+10;
        if(frame==1)return y+13;
        if(frame==2)return y;
        if(frame==3)return y-5;
        if(frame==4)return y-10;
        if(frame==5)return y-3;
        return -1;
    }
    //finds distance between 2 points
    public static double distance(int x1, int y1, int x2, int y2){
        return Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1));
    }
    //deg-->rad
    public static double degreesToRadians(double degrees){
        return degrees*(Math.PI/180.0);
    }
    //returns colour that corresponds with the integer inputted
    public static String colourLetter(int n){
        switch(n){
            case 0:return "B";
            case 1:return "G";
            case 2:return "R";
            case 3:return "W";
        }
        return "NOT FOUND";
    }
    public static boolean inRangeInclusive(int val, int l, int r){
        return val>=l&&val<=r;
    }
}
