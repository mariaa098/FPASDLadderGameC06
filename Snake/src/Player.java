import java.awt.*;

public class Player {
    private int id;
    private String name;
    private int position;
    private Color color;
    private String imagePath;
    private int bonusPoints;

    public Player(int id, String name, Color color) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.position = 1;
        this.imagePath = null;
        this.bonusPoints = 0;
    }

    public Player(int id, String name, Color color, String imagePath) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.position = 1;
        this.imagePath = imagePath;
        this.bonusPoints = 0;
    }

    public void moveForward() {
        if (this.position < 64)
            this.position++;
    }

    public void moveBackward() {
        if (this.position > 1)
            this.position--;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int pos) {
        this.position = pos;
    }

    public Color getColor() {
        return color;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean hasCustomImage() {
        return imagePath != null && !imagePath.isEmpty();
    }

    // Bonus points methods
    public int getBonusPoints() {
        return bonusPoints;
    }

    public void addBonusPoints(int points) {
        this.bonusPoints += points;
    }

    public int getTotalScore() {
        return position + bonusPoints;
    }

    // Di Player.java
    public void setBonusPoints(int points) {
        this.bonusPoints = points;
    }

}
