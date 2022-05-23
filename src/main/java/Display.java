import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Display {
    public void printStarDashPattern(List<String> columns, int largestLength) {
        if (columns.size() == 1) {
            System.out.print("+" + "-".repeat(largestLength + 2) + "+");
        } else {
            for (int i = 0; i < columns.size(); i++) {
                if (i == 0) {
                    System.out.print("+" + "-".repeat(largestLength + 2));
                } else if (i < columns.size() - 1) {
                    System.out.print("+" + "-".repeat(largestLength + 1));
                } else {
                    System.out.print("+" + "-".repeat(largestLength + 1) + "+");
                }

            }
        }

    }

    public void describeTable(Map<String, String> map) {
        List<String> cols = map.keySet().stream().toList();
        List<String> val = map.values().stream().toList();
        int largestLength = Math.max(Collections.max(map.keySet()).length(), 7);
        List<String> columns = new ArrayList<>();
        columns.add("Fields");
        columns.add("Type");
        printStarDashPattern(columns, largestLength);
        System.out.println();
        System.out.println(
                "| Fields" + " ".repeat(largestLength - 6) + " | " + " Type" + " ".repeat(largestLength - 6) + " |");
        printStarDashPattern(columns, largestLength);
        System.out.println();
        for (int i = 0; i < map.size(); i++) {
            System.out.print("| " + cols.get(i) + " ".repeat(largestLength - cols.get(i).length()) + " | " + val.get(i)
                    + " ".repeat(largestLength - val.get(i).length()) + "| ");
            System.out.println();

        }
        printStarDashPattern(columns, largestLength);
        System.out.println();
    }

    public void displayData(Table table) {
        List<String> columns = new ArrayList<>(table.getColumns().keySet());
        List<Object[]> data = table.getData();
        int largestLength = Integer.MIN_VALUE;
        for (String str : columns) {
            largestLength = Math.max(str.length(), largestLength);
        }
        if (data.size() > 0) {
            for (Object[] objs : data) {
                for (Object o : objs) {
                    if (o == null) {
                        largestLength = Math.max(largestLength, 4);
                    } else {
                        largestLength = Math.max(largestLength, o.toString().length());
                    }

                }
            }
        }
        printStarDashPattern(columns, largestLength);
        System.out.println();
        for (int i = 0; i < columns.size(); i++) {
            if (i == 0) {
                System.out.print("| " + columns.get(i) + " ".repeat(largestLength - columns.get(i).length()) + " | ");

            } else {
                System.out.print(columns.get(i) + " ".repeat(largestLength - columns.get(i).length()) + "| ");

            }

        }
        System.out.println();
        printStarDashPattern(columns, largestLength);
        System.out.println();
        if (data.size() > 0) {
            for (Object[] o : data) {
                for (int i = 0; i < o.length; i++) {
                    if (o[i] != null) {
                        if (i == 0) {
                            System.out
                                    .print("| " + o[i] + " ".repeat(largestLength - o[i].toString().length()) + " | ");
                        } else {
                            System.out.print(o[i] + " ".repeat(largestLength - o[i].toString().length()) + "|");
                        }
                    } else {
                        if (columns.size() != o.length) {
                            if (i == 0) {
                                System.out.print("| ");
                            }
                        } else {
                            if (i == 0) {
                                System.out.print("| " + " ".repeat(largestLength) + "|");
                            } else if (i < columns.size()) {
                                System.out.print(" ".repeat(largestLength) + "|");
                            }
                        }
                    }

                }
                System.out.println();
            }
            printStarDashPattern(columns, largestLength);
            System.out.println();

        }
    }

    public void showTables() {

        List<String> cols = new ArrayList<>();
        List<String> colums = new ArrayList<>();
        colums.add("tables");
        try {
            File file = new File("schema.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedReader br = new BufferedReader(new FileReader(file));
            List<String> fileLines = br.lines().toList();
            List<String> lines = new ArrayList<>();
            for (String line : fileLines) {
                if (!line.isBlank()) {
                    lines.add(line);
                }
            }

            if (lines.size() > 0) {
                for (int i = 0; i < lines.size() - 2; i += 3) {
                    cols.add(lines.get(i));
                }
            }
            br.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        int largestLength = 6;
        for (String str : cols) {
            largestLength = Math.max(str.length(), largestLength);
        }
        printStarDashPattern(colums, largestLength);
        System.out.println();
        System.out.println("| Tables" + " ".repeat(largestLength - 6) + " |");
        printStarDashPattern(colums, largestLength);
        System.out.println();
        if (cols.size() > 0) {
            for (String col : cols) {
                System.out.print("| " + col + " ".repeat(largestLength - col.length()) + " |");
                System.out.println();

            }

            printStarDashPattern(colums, largestLength);
            System.out.println();
        }
    }

    public void help() {
        System.out.println("Avalabel queries");
        System.out.println("1) SHOW TABLES;\n" +
                "2) DESCRIBE table_name;\n" +
                "3) CREATE TABLE table_name (column1 datatype,column2 datatype,column3 datatype,....);\n" +
                "4) DROP TABLE table_name\n" +
                "5) SELECT * FROM table_name;\n" +
                "6) SELECT column1, column2, ... FROM table_name;\n" +
                "7) SELECT * FROM table_name where condition;\n" +
                "8) INSERT INTO table_name (column1, column2, column3, ...)VALUES (value1, value2, value3, ...);\n" +
                "9) UPDATE table_name SET column1 = value1, column2 = value2;\n" +
                "10) UPDATE table_name SET column1 = value1, column2 = value2, ...WHERE condition;\n" +
                "11) DELETE FROM table_name;\n" +
                "12) DELETE FROM table_name WHERE condition;\n" +
                "13) ALTER TABLE table_name ADD column_name datatype;\n" +
                "14) ALTER TABLE table_name DROP column_name;");
    }
}
