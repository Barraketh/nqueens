public class NQueens {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Invalid input length - please enter exactly one positive integer");
            return;
        }

        int size;
        try {
            size = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("Failed to parse input " + args[0]);
            return;
        }

        if (size <= 0) {
            System.out.println("Invalid size - size must be greater than 0");
            return;
        }

        var placer = new Placer(size);
        var placeSuccess = placer.placeQueens();
        if (placeSuccess) {
            for (int i = 0; i < size; i++) {
                System.out.println("(" + i + "," + placer.yPositions[i] + ")");
            }
        } else {
            System.out.println("Failed to place queens");
        }


    }

    public static class Placer {
        final int[] yPositions;  // The index of this array is the x position of the queen
        final int size;

        final int[][] gcds; // For a,b integers < size, gcds[a][b] = gcd(a, b)

        public Placer(int size) {
            yPositions = new int[size];
            this.size = size;

            gcds = new int[size][size];
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (i >= j) {
                        gcds[i][j] = gcd(i, j);
                    } else {
                        gcds[i][j] = gcd(j, i);
                    }
                }
            }
        }

        public boolean placeQueens() {
            return placeQueens(0);
        }

        // Euclid's algorithm for GCD
        private int gcd(int a, int b) {
            if (b == 0)
                return a;
            else
                return gcd(b, a % b);
        }

        private boolean placeQueens(int numPlaced) {
            if (numPlaced == size) {
                return true;
            }

            for (int proposedY = 0; proposedY < size; proposedY++) {
                if (checkNext(numPlaced, proposedY)) {
                    yPositions[numPlaced] = proposedY;
                    if (placeQueens(numPlaced + 1)) {
                        return true;
                    }
                }
            }

            return false;
        }

        private boolean checkNext(int rightQueen, int rightQueenY) {
            // Check basic attacks
            var posDiagValue = rightQueenY - rightQueen;
            var negDiagValue = rightQueenY + rightQueen;

            for (int x = 0; x < rightQueen; x++) {
                var y = yPositions[x];
                if (y == rightQueenY)
                    return false;

                if ((y - x) == posDiagValue || (y + x) == negDiagValue)
                    return false;
            }

            // Check co-linear points
            // Given (for example) queens at (0, 0) and (4, 6), we know that colinear points
            // Can only be at (2,3), (6, 9) etc - at multiples of (2,3).
            // This pair - (2,3) is the GCD of (4-0, 6-0).
            // We will call 2 the xStep, and 3 the yStep
            for (int middleQueen = rightQueen - 1; middleQueen >= 1; middleQueen--) {
                var middleQueenY = yPositions[middleQueen];

                var dxMiddle = rightQueen - middleQueen;
                var dyMiddle = rightQueenY - middleQueenY;

                int divisor;
                if (dyMiddle > 0) divisor = gcds[dyMiddle][dxMiddle];
                else divisor = gcds[-dyMiddle][dxMiddle];

                var xStep = dxMiddle / divisor;
                var yStep = dyMiddle / divisor;

                for (int leftQueen = middleQueen - xStep; leftQueen >= 0; leftQueen -= xStep) {
                    var dyLeft = rightQueenY - yPositions[leftQueen];
                    if (dyLeft == (rightQueen - leftQueen) / xStep * yStep) {
                        return false;
                    }
                }
            }
            return true;
        }
    }
}
