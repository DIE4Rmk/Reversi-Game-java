import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Класс проверки завершенности игры, обнуление, сбор данных для бота.
 */

public class AlgStatistic {

    private  int win;

    public static boolean isFull(int wh, int bl) {
        return (wh + bl == 64);
    }

    public static boolean isZero(int wh, int bl) {
        return (bl == 0 || wh == 0);
    }

    public ArrayList<Point> getMoves(DataGame dataGame, int turn) {
        ArrayList<Point> moves = new ArrayList<>();
        for (int i = 0; i < DataGame.W; ++i) {
            for (int j = 0; j < DataGame.H; ++j) {
                if ((dataGame.getData(j, i) == DataGame.EMPTY)) {
                    if (dataGame.checkPut(j, i, turn, true)) {
                        Point cell = new Point(j, i);
                        //Добавили вариант для бота.
                        moves.add(cell);
                    }
                }
            }
        }
        return moves;
    }

    public Point botMove(DataGame dataGame) {
        ArrayList<Point> moves = getMoves(dataGame, 0);
        if (moves.size() == 0) {
            System.out.println("No way\n");
            throw new IllegalArgumentException("YOU WON!");
        }
        double maxSum = -1;
        Point moveBest = new Point();
        for (Point move : moves) {
            double sum = 0;
            if (move.x == 0 || move.x == 7) {
                sum += 2;
            } else if (move.y == 0 || move.y == 7) {
                sum += 2;
            } else {
                sum += 1;
            }
            if (move.x == 0 && (move.y == 0 || move.y == 7)) {
                sum += 0.8;
            } else if (move.x == 7 && (move.y == 0 || move.y == 7)) {
                sum += 0.8;
            } else if (sum == 2) {
                sum += 0.4;
            }

            if (sum > maxSum) {
                maxSum = sum;
                moveBest = move;
            }
        }
        return moveBest;
    }

    public boolean isgameEnded(DataGame dataGame, int turn) {
        if(isFull(dataGame.whCount, dataGame.blCount)) {
            return true;
        }
        if(isZero(dataGame.whCount, dataGame.blCount)) {
            return true;
        }
        ArrayList<Point> anyMoves = getMoves(dataGame, turn);
        if(anyMoves.isEmpty()) {
            return true;
        }
        return false;
    }

    public int theWinner(DataGame dataGame) {
        if(dataGame.whCount > dataGame.blCount) {
            return DataGame.WHITE;
        }
        if(dataGame.whCount < dataGame.blCount) {
            return DataGame.BLACK;
        }
        else {
            return -1;
        }



    }
}
