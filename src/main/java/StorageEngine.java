import exception.TableAlreadyExistException;
import exception.TableNotFoundException;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class StorageEngine {
    private BufferedReader br;
    private BufferedWriter bw;

    public void createTable(Table table){

            File file = new File(table.getTableName()+".txt");
            if(file.exists()){
                throw new TableAlreadyExistException("table with name "+table.getTableName()+" already exists");
            }
            saveTable(table);

    }
    public void saveTable(Table table){
        try {
            deleteTable(table);
            bw = new BufferedWriter(new FileWriter(table.getTableName()+".txt",true));
            int i = 0;
            for(String cName: table.getColumns().keySet()){
                if(i == table.getColumns().size()-1){
                    bw.write(cName);
                }else{
                    bw.write(cName+",");
                    i++;
                }

            }
            bw.newLine();
            int k=0;
            for(Enum<DataType> type: table.getColumns().values()){
                if(k == table.getColumns().size()-1){
                    bw.write(String.valueOf(type));
                }else{
                    bw.write(type+",");
                    k++;
                }

            }
            bw.newLine();

            if(table.getData()!=null){
                for(Object [] obj : table.getData()){
                    int j = 0;
                    for(Object obj1 : obj){
                        if(j == obj.length -1){
                            if(obj1==null){
                                bw.write("null");
                            }else{
                                bw.write(obj1.toString());
                            }

                        }else{
                            if(obj1==null){
                                bw.write("null"+",");
                            }else{
                                bw.write(obj1.toString()+",");
                            }
                            j++;
                        }

                    }
                    bw.newLine();
                }
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void insertRow(Table table , Object[] list){
        List<Object[]> data = table.getData();
        data.add(list);
        table.setData(data);
        saveTable(table);

    }
    //insert into table by column names
    public void deleteTable(Table table){
        File f = new File(table.getTableName()+".txt");
        if(f.exists()){
           f.delete();
        }


    }
    public Table getTable(String tableName){
        File file = new File(tableName+".txt");
        if(!file.exists()){
            throw new TableNotFoundException("Table with name "+tableName+" Does not Exist");
        }
        try {
            br = new BufferedReader(new FileReader(file));

            List<String> cNames =new ArrayList<>();
            List<Enum<DataType>> types = new ArrayList<>();
            List<String[]> tempData = new ArrayList<>();
            int lineNo = 0;
            String line;
            while( (line=br.readLine())!=null){
                if(lineNo==0){
                    Collections.addAll(cNames, line.split(","));
                }
                else if(lineNo==1) {
                    for (String s : line.split(",")) {
                        types.add(DataType.valueOf(s));
                    }
                }
                else{
                    tempData.add(line.split(","));

                }
                lineNo++;
            }
            Map<String,Enum<DataType>> map = new HashMap<>();
            for(int i = 0 ; i<cNames.size();i++){
                map.put(cNames.get(i),types.get(i));
            }
            List<Object[]> data = new ArrayList<>();
            for(String [] str: tempData){
                Object[] obj = new Object[str.length];
                for(int i = 0 ;i< str.length ;i++){
                    if(str[i].equals("null")){
                        obj[i] = null;
                    }else{
                        if(map.get(cNames.get(i)).equals(DataType.INT)){
                            obj[i] = Integer.parseInt(str[i]);
                        }else{
                            obj[i] = str[i];
                        }
                    }

                }
                data.add(obj);
            }
            br.close();
            Table table = new Table();
            table.setTableName(tableName);
            table.setColumns(map);
            table.setData(data);

            return table;

        } catch (IOException e) {
           e.printStackTrace();
   }

        return null;
    }
    public Table getRows(Table table , String []column,String conditionColumn , Object ConditionValue ){
        if(column.length==1 && column[0].equals("*")){
            if(conditionColumn != null && ConditionValue!=null){
                int index = table.getColumns().keySet().stream().toList().indexOf(conditionColumn);
                table.setData(table.getData().stream().filter(object -> (object[index].equals(ConditionValue))).collect(Collectors.toList()));
            }
            return table;

        }
        if(conditionColumn != null && ConditionValue!=null){
            int index = table.getColumns().keySet().stream().toList().indexOf(conditionColumn);
            List<Object[]> newData = table.getData().stream().filter(object -> (object[index].equals(ConditionValue))).toList();
            Map<String,Enum<DataType>> newColumns = new HashMap<>();
              List<Object[]> tempData = new ArrayList<>();
            for(String s : column) {
                newColumns.put(s, table.getColumns().get(s));
            }
                for(Object[] o : newData){
                    Object []temp = new Object[o.length];
                    for(String s : column){
                        int index1 = table.getColumns().keySet().stream().toList().indexOf(s);
                        temp[index1] = o[index1];
                }
                    tempData.add(temp);
            }
            table.setData(tempData);
            table.setColumns(newColumns);
            return table;

        }
        Map<String,Enum<DataType>> newColumns = new HashMap<>();
        List<Object[]> tempData = new ArrayList<>();
        for(Object[] o : table.getData()){
            Object []temp = new Object[o.length];
            for(String s : column){
                newColumns.put(s,table.getColumns().get(s));
                int index1 = table.getColumns().keySet().stream().toList().indexOf(s);
                temp[index1] = o[index1];
            }
            tempData.add(temp);
        }
        table.setData(tempData);
        table.setColumns(newColumns);
        return table;


    }
    public void deleteRows(Table table , String column , Object value){
        int index = table.getColumns().keySet().stream().toList().indexOf(column);
        List<Object[]> data = table.getData();
        data.removeAll(table.getData().stream().filter(object -> (object[index].equals(value))).toList());
        table.setData(data);
        saveTable(table);

    }
    public void updateRows(Table table,String[] column , Object[] value ,String conditionColumn ,Object conditionValue ){
        List<Object[]> originalData = table.getData();
        if(conditionColumn !=null && conditionValue!=null){
            int index = table.getColumns().keySet().stream().toList().indexOf(conditionColumn);
            List<Object[]> data = table.getData().stream().filter(object -> (object[index].equals(conditionValue))).toList();
            originalData.removeAll(data);
            for(Object []o : data){
                for(int i = 0 ; i< column.length ;i++){
                    int index1 = table.getColumns().keySet().stream().toList().indexOf(column[i]);
                    o[index1] = value[i];
                }

            }
            originalData.addAll(data);
        }else{
            for(Object []o : originalData){
                for(int i = 0 ; i< column.length ;i++){
                    int index1 = table.getColumns().keySet().stream().toList().indexOf(column[i]);
                    o[index1] = value[i];
                }

            }
        }
        table.setData(originalData);
        saveTable(table);
    }
//    public void updateTableColumns(Table table , Map<String,Object> values){
//
//    }
//    public void updateTableColumnsInRow(Table table , Map<String,Object> values,Object id){
//
//    }



}
