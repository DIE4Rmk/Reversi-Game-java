import java.util.MissingFormatArgumentException;

/**
 * Класс основных объектов, проверка хода по правилам, алгоритм.
 */

public class DataGame implements ConsoleMenu{
    public static final int WHITE = 0;
    public static final int BLACK = 1;
    public static final int EMPTY = -1;
    public static final int H = 8;
    public static final int W = 8;

    //Статистика.
    public int whCount = 2;
    public int blCount = 2;

    private int[][] data;

    //Конструктор выделяет поле и превращает в стартовую позицию.
    DataGame() {
        CreateData();
        data[3][3] = BLACK;
        data[3][4] = WHITE;
        data[4][3] = WHITE;
        data[4][4] = BLACK;
    }

    private void CreateData() {
        data = new int[W][H];
        for (int i = 0; i < H; ++i) {
            for (int j = 0; j < W; ++j) {
                data[i][j] = EMPTY;
            }
        }
    }

    /*
        GET/SET methods
     */
    private void setData(int x, int y, int turn) {
        if(x < 0 || x > 8 || y < 0 || y > 8) {
            throw new IllegalArgumentException(ANSI_RED  +"Wrong Range" + ANSI_RESET);
        }
        if(turn < -1 || turn > 1) {
            throw new IllegalArgumentException(ANSI_RED + "Should be WHITE/BLACK/EMPTY - 0/1/-1" +
                    ANSI_RESET);
        }
        data[y][x] = turn;
    }

    public int[][] getAllData() {
        return data;
    }
    public int getData(int x, int y) {
        if(x < 0 || x > 8 || y < 0 || y > 8) {
            throw new IllegalArgumentException(ANSI_RED + "Wrong Range" + ANSI_RESET);
        }
        return data[y][x];
    }
//-------------------------------------------------------------------------------

    public boolean checkPut(int x, int y, int turn, boolean isBot) {
        if(x < 0 || x > 8 || y < 0 || y > 8) {
            throw new IllegalArgumentException(ANSI_RED  +"Wrong Range" + ANSI_RESET);
        }
        else {
            if(data[y][x] != EMPTY) {
                return false;
            }
            if(checkCells(x, y, turn, isBot)) {
                if(!isBot) {
                    if(turn == BLACK) {
                        blCount++;
                    } else {
                        whCount++;
                    }
                }
                return true;
            }
            else {
                return false;
            }

        }

    }


    private boolean checkCells(int x, int y, int turn, boolean isBot) {
        boolean canPut = false;
        for(int i = -1; i <= 1; ++i) {
            for(int j = -1; j <= 1; ++j) {
                int X = x + j;
                int Y = y + i;
                if(X < 0 || X > 7 || Y < 0 || Y > 7) {
                    continue;
                }

                if(isP2Turn(X, Y, turn)) {
                    if (checkLines(x, y, j, i, turn, isBot)) {
                        canPut = true;
                    }
                }
            }
        }
        return canPut;
    }
    //Находим противоположный цвет и проходя по линии ищем наш цвет.
    private boolean checkLines(int x, int y, int j, int i, int turn, boolean isBot) {
        boolean fl = false;
        int rx = x;
        int ry = y;

        while(true) {
            rx += j;
            ry += i;
            if(rx < 0 || rx >= W || ry < 0 || ry >= H) {
                break;
            }
            if(isEmpty(rx, ry)) {
                break;
            }

            if(isP1Turn(rx, ry, turn)) {
                fl = true;
                if(!isBot) {
                    swapLine(x, y, j, i, rx, ry, turn);
                    break;
                }
            }
        }
        return fl;
    }
    // Наш цвет найден, заменяем все цвета по линии.
    private void swapLine(int x, int y, int j, int i, int borderX, int borderY, int turn) {
        int rx = x;
        int ry = y;
        int changedCount = 0;
        //System.out.println(x + " " + y + " " + i + " " + j + " " + borderX +
        //        " " + borderY);
        do {
            if(isP2Turn(rx, ry, turn)) {
                changedCount++;
            }
            try {
                setData(rx, ry, turn);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
            rx += j;
            ry += i;
        }while(rx != borderX || ry != borderY);
        changeCount(changedCount, turn);
    }

    private void changeCount(int num, int turn) {
        if(turn == WHITE) {
            whCount += num;
            blCount = blCount - num;
        } else {
            blCount += num;
            whCount = whCount - num;
        }
    }
    //Является текущая ячейка одного цвета с цветом игрока.
    private boolean isP1Turn(int x, int y, int turn) {
        return data[y][x] == turn;
    }

    //Является текущая ячейка одного цвета одного цвета с цветом второго игрока.
    private boolean isP2Turn(int x, int y, int turn) {
        return data[y][x] == ((turn == BLACK) ? WHITE : BLACK);
    }
    //Является текущая ячейка пустой.
    private boolean isEmpty(int x, int y) {
        return data[y][x] == EMPTY;
    }

}
