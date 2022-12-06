import java.awt.Point;
import java.util.Scanner;

/**
 * Класс запуска цикла всех частей игры.
 */
public class Game implements ConsoleMenu {
    public int curPlayer;
    private DataGame dataGame;
    private Scanner scanner;
    private Point point;

    private AlgStatistic stat;
    private int turn = 0;

    public Game(int gameMod) {
        run(gameMod);
    }
    // Создаем объекты перед запуском игры.
    private void initProcess() {
        scanner = new Scanner(System.in);
        dataGame = new DataGame();
        point = new Point();
        curPlayer = DataGame.BLACK;
        stat = new AlgStatistic();
    }
    //Запуск резултата хода для режима двух игроков
    private void oneTurn() {
        try {
            if (dataGame.checkPut(point.x, point.y, curPlayer, false)) {
                curPlayer = ((curPlayer == DataGame.BLACK) ? DataGame.WHITE : DataGame.BLACK);
            } else {
                System.out.println(ANSI_RED + "Wrong position! Try again!" + ANSI_RESET);
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void botTurn() {
        try {
            if(curPlayer == DataGame.BLACK) {
                while (true) {
                    if (dataGame.checkPut(point.x, point.y, curPlayer, false)) {
                        changeCurPlayer();
                        break;
                    } else {
                        System.out.println(ANSI_RED + "You can't put it here! Try again!" + ANSI_RESET);
                        insert();
                    }
                }
            }
            else {
                Point botPoint = new Point(stat.botMove(dataGame));
                if(!dataGame.checkPut(botPoint.x, botPoint.y, curPlayer, false)) {
                    System.out.println("BOT LOST! GGWP");
                }
                changeCurPlayer();
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void changeCurPlayer() {
        curPlayer = ((curPlayer == DataGame.BLACK) ? DataGame.WHITE : DataGame.BLACK);
    }

    // Цикличный процесс самой игры.
    public void run(int mode) {
        initProcess();
        while(!stat.isgameEnded(dataGame, curPlayer)) {
            //На первом ходу выводим просто таблицу.
            if(turn != 0) {
                if(mode == 1) {
                    insert();
                    oneTurn();
                }
                else {
                    //Вариант для игры с ботом.
                    if(turn % 2 == 1) {
                        insert();
                    }
                    botTurn();
                }
            }
            System.out.println("White Count: " + dataGame.whCount +
                    "   Black Count: " + dataGame.blCount);
            switch (curPlayer) {
                case 0 -> System.out.println(ANSI_WHITE_BACKGROUND + "WHITE turn" + ANSI_RESET);
                case 1 -> System.out.println(ANSI_BLACK_BACKGROUND + "BlACK turn" + ANSI_RESET);
                default -> System.out.println("Err");
            }
            showData();
            turn++;
        }
        int win = stat.theWinner(dataGame);
        switch (win) {
            case 0 -> System.out.println(ANSI_BLUE + "WHITE WIN" + ANSI_RESET);
            case 1 -> System.out.println(ANSI_BLUE + "BLACK WIN" + ANSI_RESET);
            default -> System.out.println(ANSI_BLUE + "NO one WIN" + ANSI_RESET);
        }
    }

    // Ввод данных исопльзуя AWT Point
    private void insert() {
        while (true) {
            System.out.print(ANSI_GREEN + "X:" + ANSI_RESET);
            if(scanner.hasNextInt()) {
                point.x = scanner.nextInt();
                break;
            } else {
                System.out.println(ANSI_RED + "WRONG TYPE OF INPUT" + ANSI_RESET);
                String inp = scanner.nextLine();
            }

        }
        while (true) {
            System.out.print(ANSI_GREEN + "Y:" + ANSI_RESET);
            if(scanner.hasNextInt()) {
                point.y = scanner.nextInt();
                break;
            } else {
                System.out.println(ANSI_RED + "WRONG TYPE OF INPUT" + ANSI_RESET);
                String inp = scanner.nextLine();
            }

        }
    }
    // Rendering datagame.
    private void showData() {

        for(int i = 0; i < 8; ++i) {
            System.out.print(ANSI_BLUE + "  " + i + ANSI_RESET);
        }
        System.out.println();
        for(int i = 0; i < DataGame.H; ++i) {
            System.out.print(ANSI_BLUE + i + ANSI_RESET);
            for(int j = 0; j < DataGame.W; ++j) {
                int value;
                try {
                    value = dataGame.getData(j, i);
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                    break;
                }

                if(value == -1)
                    System.out.print(ANSI_BLACK + " X " + ANSI_RESET);
                if(value == 0)
                    System.out.print(ANSI_WHITE_BACKGROUND +" W " + ANSI_RESET);
                if(value == 1)
                    System.out.print(ANSI_BLACK_BACKGROUND + " B " + ANSI_RESET);
            }
            System.out.println();
        }
    }

}

