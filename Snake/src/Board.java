import java.util.*;

public class Board {

    public static final int SIZE = 8;
    private Tile[][] tiles = new Tile[SIZE][SIZE];
    private Map<Integer, Integer> bonusPoints; // position -> points

    public Board() {
        int num = 1;

        for (int row = SIZE - 1; row >= 0; row--) {
            if ((SIZE - row) % 2 == 1) {
                for (int col = 0; col < SIZE; col++)
                    tiles[row][col] = new Tile(num++);
            } else {
                for (int col = SIZE - 1; col >= 0; col--)
                    tiles[row][col] = new Tile(num++);
            }
        }
        setupLimitedPrimeLadders();
        setupBonusPoints();
    }

    private void setupLimitedPrimeLadders() {
        Random rand = new Random();
        List<Tile> primeTiles = new ArrayList<>();

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                Tile t = tiles[row][col];
                if (isPrime(t.getNumber()) && t.getNumber() < 55)
                    primeTiles.add(t);
            }
        }

        Collections.shuffle(primeTiles);

        int count = 0;
        for (Tile t : primeTiles) {
            if (count >= 5) break;
            int jump = rand.nextInt(15) + 5;
            int target = t.getNumber() + jump;
            if (target > 64) target = 64;
            t.setDestination(target);
            count++;
        }
    }

    private void setupBonusPoints() {
        bonusPoints = new HashMap<>();
        Random rand = new Random();

        // Setup bonus di posisi 2 sampai 63
        for (int i = 2; i <= 63; i++) {
            boolean isPositive = rand.nextDouble() > 0.1;
            int points;
            if (isPositive) {
                // Bonus Positif: +10 s/d +100
                points = (rand.nextInt(10) + 1) * 10;
            } else {
                // Bonus Negatif: -10 s/d -50
                points = (rand.nextInt(5) + 1) * -10;
            }
            bonusPoints.put(i, points);
        }
    }

    private boolean isPrime(int n) {
        if (n <= 1) return false;
        for (int i = 2; i <= Math.sqrt(n); i++)
            if (n % i == 0) return false;
        return true;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public Tile getTileByNumber(int number) {
        if (number < 1 || number > 64) return null;
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (tiles[r][c].getNumber() == number)
                    return tiles[r][c];
            }
        }
        return null;
    }

    public int collectBonusAt(int position) {
        if (bonusPoints.containsKey(position)) {
            int points = bonusPoints.get(position);
            bonusPoints.remove(position); // Remove after collection
            return points;
        }
        return 0;
    }

    // Check if bonus exists at position
    public boolean hasBonusAt(int position) {
        return bonusPoints.containsKey(position);
    }

    // Get bonus value without collecting
    public int getBonusValueAt(int position) {
        return bonusPoints.getOrDefault(position, 0);
    }

    // Get all remaining bonus points for visualization
    public Map<Integer, Integer> getAllBonusPoints() {
        return new HashMap<>(bonusPoints);
    }
}
