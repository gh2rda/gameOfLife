package ru.rzn.sbt.javaschool.rda.tests;
import java.lang.Math;
import java.lang.Thread;
// автор Рычагов Денис

public class LifeRDA {
    static boolean[][] parent, child;//текущее поколение и следующее
    static int maxX = 15, maxY = 15; // размер поля
    static int numberGeneration = 0; //номер текущего поколения
    static int maxState = 1000;// ограничение по количеству шагов(поколений)
    static int pause = 700; //задержка времени отображения следующего поколения
    static boolean stopGameAllEmpty = false; // истина-если нет живых
    static boolean stopGameImmutable = false;// истина-если следующее поколение не отличается от предыдущего
    static String livingCell = " O ", deadCell = " . ";//символы для отображения живых и пустых ячеек

    //    инициализация все живые
    static public void initStart() {
        numberGeneration = 1;
        for (int i = 0; i < maxY; i++) {
            for (int j = 0; j < maxX; j++) {
                parent[i][j] = true;

            }
        }
    }

    //    инициализация случайным образом
    static public void initStartRandom() {
        numberGeneration = 1;
        stopGameAllEmpty = true;
        for (int i = 0; i < maxY; i++) {
            for (int j = 0; j < maxX; j++) {
                if (Math.random() > 0.5) {
                    parent[i][j] = true;
                } else {
                    parent[i][j] = false;
                }
                if (parent[i][j]) {
                    stopGameAllEmpty = false;
                }
            }
        }
    }

    // подсчет количества живых соседей
    static public int countNeighbors(int i, int j) {
        int result = 0;
        if (parent[(i - 1 + maxY) % maxY][(j - 1 + maxX) % maxX]) {
            result++;
        }
        if (parent[(i - 1 + maxY) % maxY][(j + maxX) % maxX]) {
            result++;
        }
        if (parent[(i - 1 + maxY) % maxY][(j + 1 + maxX) % maxX]) {
            result++;
        }
        if (parent[(i + 1 + maxY) % maxY][(j - 1 + maxX) % maxX]) {
            result++;
        }
        if (parent[(i + 1 + maxY) % maxY][(j + maxX) % maxX]) {
            result++;
        }
        if (parent[(i + 1 + maxY) % maxY][(j + 1 + maxX) % maxX]) {
            result++;
        }
        if (parent[(i + maxY) % maxY][(j - 1 + maxX) % maxX]) {
            result++;
        }
        if (parent[(i + maxY) % maxY][(j + 1 + maxX) % maxX]) {
            result++;
        }
        return result;
    }

    //    расчет следующего поколения
    static public void nextState() {
        int countLivingCell;
        numberGeneration++;
        stopGameAllEmpty = true;
        stopGameImmutable = true;
        for (int i = 0; i < maxY; i++) {
            for (int j = 0; j < maxX; j++) {
                countLivingCell = countNeighbors(i, j);
                if (!parent[i][j] && (countLivingCell == 3)) {
                    child[i][j] = true;
                } else {
                    if (parent[i][j] && (!(countLivingCell == 3 || countLivingCell == 2))) {
                        child[i][j] = false;
                    } else {
                        child[i][j] = parent[i][j];
                    }
                }
                if (child[i][j]) {
                    stopGameAllEmpty = false;
                }
                if (parent[i][j] ^ child[i][j]) {
                    stopGameImmutable = false;
                }
            }
        }
        for (int i = 0; i < maxY; i++) {
            for (int j = 0; j < maxX; j++) {
                parent[i][j] = child[i][j];
            }
        }

    }

    // вывод на экран текущего поколения с использованием заданных симолов
    static public void printCurentState() {
        System.out.println("Поколение №: " + numberGeneration);
        for (int i = 0; i < maxY; i++) {
            for (int j = 0; j < maxX; j++) {
                if (parent[i][j]) {
                    System.out.print(livingCell);
                } else {
                    System.out.print(deadCell);
                }
            }
            System.out.print("\n");
        }
    }
    // вывод на экран текущего поколения с использованием заданных симолов
    // правее дублируется копия в которой указано количество соседей у живой ячейки

    static public void printCurentState2() {
        System.out.println("Поколение №: " + numberGeneration);
        for (int i = 0; i < maxY; i++) {
            for (int j = 0; j < maxX; j++) {
                if (parent[i][j]) {
                    System.out.print(livingCell);
                } else {
                    System.out.print(deadCell);
                }
            }
            System.out.print("  ||  ");

            for (int j = 0; j < maxX; j++) {
                if (parent[i][j]) {
                    System.out.print(" " + countNeighbors(i, j) + " ");
                } else {
                    System.out.print(deadCell);
                }
            }
            System.out.print("\n");
        }
    }


    public static void main(String[] args) {
        try {
            parent = new boolean[maxY][maxX];
            child = new boolean[maxY][maxX];
            initStartRandom();
//            for (int k = 1; k < maxState; k++) {
            while (true) {
                printCurentState2();
//                printCurentState();
                if (stopGameAllEmpty) {
                    System.out.println("СТОП - Нет живых ячеек.");
                    break;
                } else {
                    if (stopGameImmutable) {
                        System.out.println("СТОП - Состояние не изменяется.");
                        break;
                    } else {
                        nextState();
                        Thread.sleep(pause);
                        System.out.print("\n\n\n\n\n");
                    }
                }
            }

        } catch (
                Exception e) {
        }
    }
}
