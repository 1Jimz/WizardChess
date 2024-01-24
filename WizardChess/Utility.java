import greenfoot.*;
/**
 * Useful toolkit
 * 
 * @author Jimmy Zhu
 * @version January 22nd, 2023
 **/
public class Utility
{
    //creates an image that is customized based on various values
    public static class Vector{
        private double x,y;
        /**
         * Creates a new instance of the Vector class with specified x and y components.
         * 
         * @param a
         * The x-component of the vector.
         * 
         * @param b
         * The y-component of the vector.
         */
        public Vector(double a, double b){
            x = a;
            y = b;
        }

        /**
         * Retrieves the x-component of the vector.
         * 
         * @return double
         * The x-component of the vector.
         */
        public double xPart(){
            return x;
        }

        /**
         * Retrieves the y-component of the vector.
         * 
         * @return double
         * The y-component of the vector.
         */
        public double yPart(){
            return y;
        }

        /**
         * Calculates the magnitude (length) of the vector.
         * 
         * @return double
         * The magnitude of the vector.
         */
        public double magnitude(){
            return Math.sqrt(x*x+y*y);
        }

        /**
         * Calculates and returns the unit vector (normalized vector) of the current vector.
         * 
         * @return Vector
         * The unit vector of the current vector.
         */
        public Vector getUnitVector(){
            return new Vector(x/magnitude(),y/magnitude());
        }

        /**
         * Calculates the dot product of the current vector with another vector.
         * 
         * @param v
         * The other vector for the dot product calculation.
         * 
         * @return double
         * The result of the dot product.
         */
        public double dot(Vector v){
            return x*v.x + y*v.y;
        }

        /**
         * Calculates the cross product of the current vector with another vector.
         * 
         * @param v
         * The other vector for the cross product calculation.
         * 
         * @return double
         * The result of the cross product.
         */
        public double cross(Vector v){
            return x*v.y - y*v.x;
        }

        /**
         * Scales the current vector by a specified factor.
         * 
         * @param s
         * The scaling factor.
         * 
         * @return Vector
         * The scaled vector.
         */
        public Vector scale(double s){
            return new Vector(x*s,y*s);
        }

        /**
         * Subtracts another vector from the current vector.
         * 
         * @param v
         * The vector to be subtracted.
         * 
         * @return Vector
         * The result of the subtraction.
         */
        public Vector subtract(Vector v){
            return new Vector(x-v.x,y-v.y);
        }

        /**
         * Adds another vector to the current vector.
         * 
         * @param v
         * The vector to be added.
         * 
         * @return Vector
         * The result of the addition.
         */
        public Vector add(Vector v){
            return new Vector(x+v.x,y+v.y);
        }
    }

    /**
     * Creates a customized GreenfootImage with the specified width, height, and image source.
     * 
     * @param w
     * The width of the image.
     * 
     * @param h
     * The height of the image.
     * 
     * @param s
     * The image source.
     * 
     * @return GreenfootImage
     * The customized GreenfootImage.
     */
    public static GreenfootImage customizeAndCreate(int w, int h, String s){
        GreenfootImage gfi = new GreenfootImage(s);
        gfi.scale(w,h);
        return gfi;
    }

    /**
     * Creates a customized GreenfootImage with the specified width, height, image source, and transparency.
     * 
     * @param w
     * The width of the image.
     * 
     * @param h
     * The height of the image.
     * 
     * @param s
     * The image source.
     * 
     * @param t
     * The transparency level.
     * 
     * @return GreenfootImage
     * The customized GreenfootImage.
     */
    public static GreenfootImage customizeAndCreate(int w, int h, String s, int t){
        GreenfootImage gfi = new GreenfootImage(s);
        gfi.setTransparency(t);
        gfi.scale(w,h);
        return gfi;
    }

    /**
     * Creates a customized GreenfootImage with the specified image source and transparency.
     * 
     * @param s
     * The image source.
     * 
     * @param t
     * The transparency level.
     * 
     * @return GreenfootImage
     * The customized GreenfootImage.
     */
    public static GreenfootImage customizeAndCreate(String s, int t){
        GreenfootImage gfi = new GreenfootImage(s);
        gfi.setTransparency(t);
        return gfi;
    }

    /**
     * Modifies an existing GreenfootImage with the specified width and height.
     * 
     * @param w
     * The width of the image.
     * 
     * @param h
     * The height of the image.
     * 
     * @param gfi
     * The GreenfootImage to be modified.
     * 
     * @return GreenfootImage
     * The modified GreenfootImage.
     */
    public static GreenfootImage customize(int w, int h, GreenfootImage gfi){
        gfi.scale(w,h);
        return gfi;
    }

    /**
     * Modifies an existing GreenfootImage with the specified width, height, and transparency.
     * 
     * @param w
     * The width of the image.
     * 
     * @param h
     * The height of the image.
     * 
     * @param gfi
     * The GreenfootImage to be modified.
     * 
     * @param t
     * The transparency level.
     * 
     * @return GreenfootImage
     * The modified GreenfootImage.
     */
    public static GreenfootImage customize(int w, int h, GreenfootImage gfi, int t){
        gfi.setTransparency(t);
        gfi.scale(w,h);
        return gfi;
    }

    /**
     * Modifies an existing GreenfootImage with the specified width, height, transparency, and rotation.
     * 
     * @param w
     * The width of the image.
     * 
     * @param h
     * The height of the image.
     * 
     * @param gfi
     * The GreenfootImage to be modified.
     * 
     * @param t
     * The transparency level.
     * 
     * @param r
     * The rotation angle.
     * 
     * @return GreenfootImage
     * The modified GreenfootImage.
     */
    public static GreenfootImage customize(int w, int h, GreenfootImage gfi, int t, int r){
        gfi.setTransparency(t);
        gfi.scale(w,h);
        gfi.rotate(r);
        return gfi;
    }

    /**
     * Modifies an existing GreenfootImage by setting its transparency.
     * 
     * @param gfi
     * The GreenfootImage to be modified.
     * 
     * @param t
     * The transparency level.
     * 
     * @return GreenfootImage
     * The modified GreenfootImage.
     */
    public static GreenfootImage customize(GreenfootImage gfi, int t){
        gfi.setTransparency(t);
        return gfi;
    }

    /**
     * Modifies an existing GreenfootImage by setting its transparency and rotation.
     * 
     * @param gfi
     * The GreenfootImage to be modified.
     * 
     * @param t
     * The transparency level.
     * 
     * @param r
     * The rotation angle.
     * 
     * @return GreenfootImage
     * The modified GreenfootImage.
     */
    public static GreenfootImage customize(GreenfootImage gfi, int t, int r){
        gfi.setTransparency(t);
        gfi.rotate(r);
        return gfi;
    }

    /**
     * Creates a filled rectangle or square with specified width, height, transparency, and color.
     * 
     * @param w
     * The width of the rectangle.
     * 
     * @param h
     * The height of the rectangle.
     * 
     * @param t
     * The transparency level.
     * 
     * @param c
     * The color of the rectangle.
     * 
     * @return GreenfootImage
     * The created rectangle.
     */
    public static GreenfootImage createBox(int w, int h, int t, Color c){
        GreenfootImage gfi = new GreenfootImage(w,h);
        gfi.setColor(c);
        gfi.fill();
        gfi.setTransparency(t);
        return gfi;
    }

    /**
     * Creates a text image with specified width, height, font size, font type, and text content.
     * 
     * @param w
     * The width of the text image.
     * 
     * @param h
     * The height of the text image.
     * 
     * @param s
     * The font size.
     * 
     * @param font
     * The font type.
     * 
     * @param text
     * The content of the text.
     * 
     * @return GreenfootImage
     * The created text image.
     */
    public static GreenfootImage createText(int w, int h, int s, String font, String text){
        GreenfootImage gfi = new GreenfootImage(w, h);
        gfi.setColor(Color.WHITE);
        gfi.setFont(new Font(font, true, false, s/text.length())); 
        gfi.drawString(text, w/4, h/2);
        return gfi;
    }

    /**
     * Calculates the bearing in degrees from point A to point B.
     * 
     * @param x1
     * The x-coordinate of point A.
     * 
     * @param y1
     * The y-coordinate of point A.
     * 
     * @param x2
     * The x-coordinate of point B.
     * 
     * @param y2
     * The y-coordinate of point B.
     * 
     * @return double
     * The bearing in degrees from point A to point B.
     */
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

    /**
     * Converts degrees into a cardinal direction.
     * 
     * @param angle
     * The angle in degrees.
     * 
     * @return int
     * The cardinal direction: E=0, NE=1, N=2, NW=3, W=4, SW=5, S=6, SE=7.
     */
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

    /**
     * Adjusts the vertical position based on the current frame.
     * 
     * @param y
     * The initial vertical position.
     * 
     * @param frame
     * The current frame.
     * 
     * @return int
     * The adjusted vertical position.
     */
    public static int adjustToFrame(int y, int frame){
        if(frame==0)return y+10;
        if(frame==1)return y+13;
        if(frame==2)return y;
        if(frame==3)return y-5;
        if(frame==4)return y-10;
        if(frame==5)return y-3;
        return -1;
    }

    /**

     * Calculates the Euclidean distance between two points in 2D space.

     * @param x1
    The x-coordinate of the first point.

     * @param y1
    The y-coordinate of the first point.

     * @param x2
    The x-coordinate of the second point.

     * @param y2
    The y-coordinate of the second point.

     * @return double
    The Euclidean distance between the two points.

     */

    public static double distance(int x1, int y1, int x2, int y2){
        return Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
    }

    /**

     * Converts degrees to radians.

     * @param degrees
    The angle in degrees.

     * @return double
    The angle in radians.

     */

    public static double degreesToRadians(double degrees){
        return degrees * (Math.PI/180.0);
    }

    /**

     * Retrieves the color letter ("B", "G", "R", "W") corresponding to the inputted integer.

     * @param n
    The integer representing the color.

     * @return String
    The color letter or "NOT FOUND" if the input is invalid.

     */

    public static String colourLetter(int n){
        switch(n){
            case 0: return "B";
            case 1: return "G";
            case 2: return "R";
            case 3: return "W";
        }
        return "NOT FOUND";
    }

    /**

     * Checks if a given value is within a specified inclusive range.

     * @param val
    The value to be checked.

     * @param l
    The lower bound of the range.

     * @param r
    The upper bound of the range.

     * @return boolean
    True if the value is within the range (inclusive), otherwise false.

     */

    public static boolean inRangeInclusive(int val, int l, int r){
        return val >= l && val <= r;
    }
}
