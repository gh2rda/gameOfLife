import java.lang.Math;
import java.lang.Thread;
// автор Рычагов Денис

public class LifeRDA {
    static boolean[][] parent, child;//текущее поколение и следующее
    static boolean[][][] history;//история поколений

    static int maxX = 30, maxY = 10, maxZ = 200; // размер поля - X, Y и глубина истории поколений - Z
    static int numberGeneration = 0; //номер текущего поколения
    static int curentInHistory = 0; //индекс текущего поколения в истории
    static int numberOldRepeatGeneration = 0; //номер предыдущего совпавшего поколения в истории

    //static int maxState = 1000;// ограничение по количеству шагов(поколений)

    static int pause = 0; //задержка времени отображения следующего поколения
    static boolean stopGameAllEmpty = false; // истина-если нет живых
    static boolean stopGameImmutable = false;// истина-если следующее поколение не отличается от предыдущего
    static String livingCell = " O ", deadCell = " . ";//символы для отображения живых и пустых ячеек

    //    инициализация все живые
    static public void initStart() {
        numberGeneration = 1;
        curentInHistory = 0;
        for (int i = 0; i < maxY; i++) {
            for (int j = 0; j < maxX; j++) {
                parent[i][j] = true;

            }
        }
    }
    //    инициализация c планером
    static public void initStartGlider() {
        numberGeneration = 1;
        curentInHistory = 0;
        for (int i = 0; i < maxY; i++) {
            for (int j = 0; j < maxX; j++) {
                parent[i][j] = false;
            }
        }
        parent[2][0] = true;
        parent[2][1] = true;
        parent[2][2] = true;
        parent[1][2] = true;
        parent[0][1] = true;

   }

    //    инициализация случайным образом
    static public void initStartRandom() {
        numberGeneration = 1;
        curentInHistory = 0;
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

    //    расчет следующего поколения и заполнение истории
    static public void nextState() {
        int countLivingCell;
//        numberGeneration++;
        stopGameAllEmpty = true;
//        stopGameImmutable = true;
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
//                if (parent[i][j] ^ child[i][j]) {
//                    stopGameImmutable = false;
//                }
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

    // запись истории поколений и ???увеличение номера текущего поколения
    static public void saveHistory(boolean[][] h) {

        curentInHistory = numberGeneration % maxZ - 1;
        if (curentInHistory < 0) {
            curentInHistory = maxZ - 1;
        }

        for (int i = 0; i < maxY; i++) {
            for (int j = 0; j < maxX; j++) {
                history[curentInHistory][i][j] = h[i][j];
            }
        }
    }

    static public boolean equalMatrix(boolean[][] a, boolean[][] b) {
        boolean result = true;
        outer:
        for (int i = 0; i < maxY; i++) {
            for (int j = 0; j < maxX; j++) {
                if (a[i][j] ^ b[i][j]) {
                    result = false;
                    break outer;
                }
            }
        }
        return result;
    }

    // поиск совпадений в истории поколений и присвоение(инициализация) найденного номера совпавшего поколения
    static public void lookHistory(boolean[][] curent) {
        boolean fullHistory = false;
        if (numberGeneration / maxZ >= 1) {
            fullHistory = true;
        }
        if (fullHistory) {
            outer1:
            for (int k = 0; k < maxZ; k++) {
                boolean[][] h = history[k];
                if (k != curentInHistory) {
                    if (equalMatrix(h, curent)) {
                        stopGameImmutable = true;
                        if (k < curentInHistory) {
                            numberOldRepeatGeneration = numberGeneration - (curentInHistory - k);
                        } else {
                            numberOldRepeatGeneration = numberGeneration - curentInHistory - (maxZ - k);
                        }

                        break outer1;
                    }
                }
            }
        } else {
            outer2:
            for (int k = 0; k < curentInHistory; k++) {
                boolean[][] h = history[k];
                if (equalMatrix(h, curent)) {
                    stopGameImmutable = true;
                    numberOldRepeatGeneration = k + 1;// проверить какое именно
                    break outer2;
                }
            }

        }

    }


    public static void main(String[] args) {
        try {
            parent = new boolean[maxY][maxX];
            child = new boolean[maxY][maxX];
            history = new boolean[maxZ][maxY][maxX];
//            initStartRandom();
            initStartGlider();
            saveHistory(parent);
            //            for (int k = 1; k < maxState; k++) {
            while (true) {
                printCurentState2();
                // альтернативный вариант вывода - без дубля поля с числами соседей
                //                printCurentState();
                if (stopGameAllEmpty) {
                    System.out.println("СТОП - Нет живых ячеек.");
                    break;
                } else {
                    if (stopGameImmutable) {
                        System.out.println("СТОП - Поколение №"+numberGeneration+" = №" + numberOldRepeatGeneration + " в истории.");
                        break;
                    } else {
                        nextState();
                        numberGeneration++;

                        saveHistory(parent);
                        lookHistory(parent);
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
