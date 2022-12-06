import java.util.Scanner;
import java.util.Objects;

public class Navigation implements ConsoleMenu {
    public static Scanner in = new Scanner(System.in);
    public void startGame() {
        System.out.println(ANSI_BLUE + "WELCOME!Press Enter to start, or any letter for exit" +
                ANSI_RESET);
        String input = in.nextLine();
        while ("".equals(input)) {
            System.out.println("\n\n Выберите режим игры: ");
            System.out.println("1) Игрок против игрока  \n 2) Игрок против бота ");
            System.out.println(ANSI_GREEN + "Введите 1 или 2: " + ANSI_RESET);

            int gameMode = getMode();
            Game game = new Game(gameMode);
            System.out.println("Press Enter to start again, or any letter to exit");
            input = in.nextLine();
        }
        System.out.println(ANSI_BLACK_BACKGROUND + "GOODBYE" + ANSI_RESET);
    }

    public static int getMode() {
        while (true) {
            String input = in.nextLine();
            if (!Objects.equals(input, "1") && !Objects.equals(input, "2")) {
                System.out.println(ANSI_RED + "Некорректный ввод, введите число 1 или 2" +
                        ANSI_RESET);
            } else {
                return Integer.parseInt(input);
            }

        }
    }
}