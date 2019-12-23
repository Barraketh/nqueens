import org.junit.Test;

import static org.junit.Assert.*;

public class NQueensTest {
    int N = 20;

    private boolean allNonColinear(int[] yPositions) {
        // Check colinear triplets
        // Three points P1,P2,P3 are colinear if
        //   (y3-y2) / (x3-x2) = (y2-y1) / (x2-x1)
        // Let dy2 = (y3-y2), dx2 = (x3-x2), dy1 = (y2-y1), dx1 = (x2-x1), then
        //   (y3-y2) / (x3-x2) = (y2-y1) / (x2-x1)
        //   dy2 / dx2 = dy1 / dx1
        //   dy2 * dx1 = dy1 * dx2

        for (int rightQueen = 2; rightQueen < yPositions.length; rightQueen++) {
            var rightQueenY = yPositions[rightQueen];
            for (int middleQueen = 1; middleQueen < rightQueen; middleQueen++) {
                var middleQueenY = yPositions[middleQueen];
                var dy2 = rightQueenY - middleQueenY;
                var dx2 = rightQueen - middleQueen;
                for (int leftQueen = 0; leftQueen < middleQueen; leftQueen++) {
                    var dy1 = middleQueenY - yPositions[leftQueen];
                    var dx1 = middleQueen - leftQueen;
                    if (dy2 * dx1 == dy1 * dx2) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Test
    public void testFirstN() {
        for (int i = 3; i < N; i++) {
            var placer = new NQueens.Placer(i);
            if (placer.placeQueens()) {
                assertTrue(allNonColinear(placer.yPositions));
            }
        }


    }

}