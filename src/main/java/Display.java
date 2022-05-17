import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
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

    public void displayData(Table table){
        List<String> columns = new ArrayList<>(table.getColumns().keySet());
        List<Object[]> data = table.getData();
        int largestLength = 0;
        for(Object[] objs : data){
            for(Object o : objs){
                if(o==null){
                    largestLength = Math.max(largestLength,4);
                }else{
                    largestLength =Math.max(largestLength,o.toString().length());
                }

            }
        }
        printStarDashPattern(columns,largestLength);
        System.out.println();
        for(int i = 0 ; i<columns.size();i++){
            if(i==0 ){
                System.out.print("| "+columns.get(i)+" ".repeat(largestLength-columns.get(i).length())+" | ");

            }else{
                System.out.print(columns.get(i)+" ".repeat(largestLength-columns.get(i).length())+"| ");

            }

        }
        System.out.println();
        printStarDashPattern(columns,largestLength);
        System.out.println();

        for(Object[] o : data){
            for(int i = 0 ; i<o.length;i++){
                if(o[i]!=null){
                    if(i==0 ){
                        System.out.print("| "+o[i] +" ".repeat(largestLength-o[i].toString().length())+" | ");
                    }else{
                        System.out.print(o[i] +" ".repeat(largestLength-o[i].toString().length())+"| ");
                    }
                }else{
                    if(i==0 ){
                        System.out.print("| "+" ".repeat(largestLength)+" | ");
                    }else{
                        System.out.print(" ".repeat(largestLength)+"| ");
                    }
                }

            }
            System.out.println();
        }
        printStarDashPattern(columns,largestLength);
        System.out.println();


    }

}
