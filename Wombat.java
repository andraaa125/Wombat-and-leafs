import greenfoot.*;
public class Wombat extends Actor
{
    private static final int EAST = 0;
    private static final int WEST = 1;
    private static final int NORTH = 2;
    private static final int SOUTH = 3;

    private int direction = EAST;
    private int leavesEaten = 0;
    private int stonesEaten = 0;
    private int stepCounter = 0;
    private int stepLimit;
    private boolean isDead = false;
    private int countNumber = 49;

    public Wombat() {
        setDirection(EAST);
        resetStepLimit();
    }

    public void act() {
        if (foundLeaf() || foundStone()) {
            eatLeaf();
            eatStone();
        } else if (canMove()) {
            moveForward();
            stepCounter++;
            if (stepCounter >= stepLimit) {
                if (Greenfoot.getRandomNumber(2) == 0) {  // 50% chance to turn
                    turnRandomly();  // Turn 90 degrees randomly
                }
                resetStepLimit();
            }
        } else {
            turnUntilCanMove();
        }

        if (!isDead) {
            WombatWorld ww = (WombatWorld) getWorld();
            ww.updateCounter(countNumber);
        }
    }

    private void resetStepLimit() {
        stepCounter = 0;
        stepLimit = Greenfoot.getRandomNumber(4) + 2;  // Random number between 2 and 5
    }

    private void updateDirection(int degrees) {
        direction = (direction + degrees / 90 + 4) % 4;  // Update direction (0 to 3), ensure it's within range
        setDirection(direction);  // Update visual rotation
    }

    public boolean foundLeaf() {
        Actor leaf = getOneObjectAtOffset(0, 0, Leaf.class);
        return leaf != null;
    }

      public boolean foundStone() {
        Actor stone = getOneObjectAtOffset(0, 0, Stone.class);
        return stone != null;
    }
    
    public void eatLeaf() {
        Actor leaf = getOneObjectAtOffset(0, 0, Leaf.class);
        if (leaf != null) {
            getWorld().removeObject(leaf);
            leavesEaten++;
            countNumber=countNumber+25;
        }
    }

     public void eatStone() {
        Actor stone = getOneObjectAtOffset(0, 0, Stone.class);
        if (stone != null) {
            getWorld().removeObject(stone);
            stonesEaten++;
            countNumber=countNumber-5;
        }
    }
    
    public void moveForward() {
        switch (direction) {
            case SOUTH:
                setLocation(getX(), getY() + 1);
                break;
            case EAST:
                setLocation(getX() + 1, getY());
                break;
            case NORTH:
                setLocation(getX(), getY() - 1);
                break;
            case WEST:
                setLocation(getX() - 1, getY());
                break;
        }
        countNumber--;
        if (countNumber <= -1) {
            getWorld().removeObject(this);
            isDead = true;
        }
    }

    public boolean canMove() {
        World myWorld = getWorld();
        int x = getX();
        int y = getY();
        switch (direction) {
            case SOUTH:
                y++;
                break;
            case EAST:
                x++;
                break;
            case NORTH:
                y--;
                break;
            case WEST:
                x--;
                break;
        }
        // Test for outside border
        if (x >= myWorld.getWidth() || y >= myWorld.getHeight() || x < 0 || y < 0) {
            return false;
        }
        return true;
    }

    private void turnLeft() {
        direction = (direction + 3) % 4; // Update direction first
        setDirection(direction); // Update visual rotation
    }

    private void turnRandomly() {
        int degrees = Greenfoot.getRandomNumber(2) == 0 ? 90 : -90;
        updateDirection(degrees);  // Update direction first
    }

    private void turnUntilCanMove() {
        for (int i = 0; i < 4; i++) { // Try all 4 directions if necessary
            turnLeft();
            if (canMove()) {
                break;
            }
        }
    }

    public void setDirection(int direction) {
        this.direction = direction;
        switch (direction) {
            case SOUTH:
                setRotation(90);
                break;
            case EAST:
                setRotation(0);
                break;
            case NORTH:
                setRotation(270);
                break;
            case WEST:
                setRotation(180);
                break;
        }
    }

    public int getLeavesEaten() {
        return leavesEaten;
    }
    
     public int getStonesEaten() {
        return stonesEaten;
    }

}