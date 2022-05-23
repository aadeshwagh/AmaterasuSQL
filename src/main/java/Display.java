import java.util.*;
import java.util.stream.Collectors;

public class Display {
    public void printStarDashPattern(List<String> columns , int largestLength){
        for (int i = 0 ; i<columns.size();i++) {
            if(i==0){
                System.out.print("+" + "-".repeat(largestLength+2));
            }else if(i<columns.size()-1){
                System.out.print("+" + "-".repeat(largestLength+1));
            }else{
                System.out.print("+" + "-".repeat(largestLength+1)+"+");
            }

        }
    }

    public void describeTable( Map<String, String> map ){
        List<String> cols = map.keySet().stream().toList();
        List<String> val = map.values().stream().toList();
        int largestLength = Math.max(Collections.max(map.keySet()).length(),7);
        List<String> columns = new ArrayList<>();
        columns.add("Fields");
        columns.add("Type");
        printStarDashPattern(columns,largestLength);
        System.out.println();
        System.out.println("| Fields" +  " ".repeat(largestLength - 6)+" | " + " Type" +  " ".repeat(largestLength - 6)+" |" );
        printStarDashPattern(columns,largestLength);
        System.out.println();
       for(int i = 0 ; i<map.size();i++){
               System.out.print("| " + cols.get(i) + " ".repeat(largestLength - cols.get(i).length()) + " | "+ val.get(i) + " ".repeat(largestLength - val.get(i).length())+ "| ");
               System.out.println();

       }
       printStarDashPattern(columns,largestLength);
       System.out.println();
    }

    public void displayData(Table table) {
        List<String> columns = new ArrayList<>(table.getColumns().keySet());
        List<Object[]> data = table.getData();
        int largestLength = Collections.max(columns).length();
        if (data.size()>0) {
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
                            System.out.print("| " + o[i] + " ".repeat(largestLength - o[i].toString().length()) + " | ");
                        } else {
                            System.out.print(o[i] + " ".repeat(largestLength - o[i].toString().length()) + "| ");
                        }
                    } else {
                        if (columns.size() != o.length) {
                            if (i == 0) {
                                System.out.print("| ");
                            }
                        } else {
                            if (i == 0) {
                                System.out.print("| " + " ".repeat(largestLength) + " | ");
                            } else if (i < columns.size() - 1) {
                                System.out.print(" ".repeat(largestLength) + "| ");
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

}
