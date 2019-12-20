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
            for (int i= 0; i < size; i++) {
                System.out.println("(" + i + "," + placer.yPositions[i] + ")");
            }
        } else {
            System.out.println("Failed to place queens");
        }


    }

    public static class Placer {
        final int[] yPositions;  // The index of this array is the x position of the queen
        final int size;

        public Placer(int size) {
            yPositions = new int[size];
            this.size = size;
        }

        public boolean placeQueens() {
            return placeQueens(0);
        }

        private boolean placeQueens(int numPlaced) {
            if (numPlaced == size)
                return true;

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

        private boolean checkNext(int proposedX, int proposedY) {
            // Check basic attacks
            var posDiagValue = proposedY - proposedX;
            var negDiagValue = proposedY + proposedX;

            for (int x = 0; x < proposedX; x++) {
                var y = yPositions[x];
                if (y == proposedY)
                    return false;

                if ((y - x) == posDiagValue || (y + x) == negDiagValue)
                    return false;
            }

            // Check colinear triplets
            // Three points P1,P2,P3 are colinear if
            //   (y3-y2) / (x3-x2) = (y2-y1) / (x2-x1)
            // Let dy2 = (y3-y2), dx2 = (x3-x2), dy1 = (y2-y1), dx1 = (x2-x1), then
            //   (y3-y2) / (x3-x2) = (y2-y1) / (x2-x1)
            //   dy2 / dx2 = dy1 / dx1
            //   dy2 * dx1 = dy1 * dx2

            for (int middleQueen = 1; middleQueen < proposedX; middleQueen++) {
                var middleQueenY = yPositions[middleQueen];
                var dy2 = proposedY - middleQueenY;
                var dx2 = proposedX - middleQueen;
                for (int leftQueen = 0; leftQueen < middleQueen; leftQueen++) {
                    var dy1 = middleQueenY - yPositions[leftQueen];
                    var dx1 = middleQueen - leftQueen;
                    if (dy2 * dx1 == dy1 * dx2) {
                        return false;
                    }
                }
            }
            return true;
        }
    }
}
