//DECIDED SCALE: 3X

public class Car {
    
    /* SPAWN POINTS AND CURRENT DIRECTION
     * 0 - North
     * 1 - East
     * 2 - South
     * 3 - West
     * 4 - East Bottom
     * 5 - West Bottom
     */
    
    /* TURN CHOICES
     * 0 - Left
     * 1 - Straight
     * 2 - Right
     */
    
    public SpawnPoint spawnPoint;
    public int turnChoice, currentDir;
    public double xPos, yPos;
    double dx, dy, rotation;
    public boolean isAtIntersection = false;
    public boolean wasAtIntersection = false;
    public double intersectionDistance = 40;
    
    public Car(SpawnPoint spawn, int turn, int mapWidth, int mapHeight) {
        //DO NOT FORGET TO ADJUST XPOS AND YPOS TO SPAWN AND TURN
        spawnPoint = spawn;
        turnChoice = turn;
        dx = spawn.getDx();
        dy = spawn.getDy();
        rotation = spawn.getRotation();
        xPos = spawn.getStartXPos(turn, mapWidth);
        yPos = spawn.getStartYPos(turn, mapHeight);
    }
    
    public void increment(int dxAdjust, int dyAdjust) {
        dx += dxAdjust;
        dy += dyAdjust;
    }
    
    //when given car comes onto the area in between the two intersections
    public void changeTurn(int newTurn) {
        turnChoice = newTurn;
    }
    
    public boolean getAtIntersection() { return isAtIntersection; }
    
    public void setAtIntersection(boolean isThere) {
    	isAtIntersection = isThere;
    }
    
    public void move() {
        xPos += dx;
        yPos += dy;
    }
    
    
    
}
