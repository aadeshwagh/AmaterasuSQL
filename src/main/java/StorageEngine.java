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
            File file = new File(table.getTableName()+".txt");
            bw = new BufferedWriter(new FileWriter(file,true));
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
            System.out.println(e.getMessage());
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
    public void deleteSchemaFile(){
        File f = new File("schema.txt");
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
           System.out.println(e.getMessage());
   }

        return null;
    }
    public Table getRows(Table table , String []column,String conditionColumn , Object ConditionValue ){
        if(table.getData().size()==0){
            return table;
        }
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
        List<Object[]> data = table.getData();
        if(column==null && value==null){
            data.clear();
            table.setData(data);
        }else{
            int index = table.getColumns().keySet().stream().toList().indexOf(column);
            data.removeAll(table.getData().stream().filter(object -> (object[index].equals(value))).toList());
            table.setData(data);
        }
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
    public void saveSchemaFile(Map<String,Schema> map){
        try {
            deleteSchemaFile();
            bw = new BufferedWriter(new FileWriter("schema.txt", true));
            for(String tableName : map.keySet()){
                bw.write(tableName);
                bw.newLine();
                int i = 0;
                for(String cName: map.get(tableName).getColumns()){
                    if(i == map.get(tableName).getColumns().size()-1){
                        bw.write(cName);
                    }else{
                        bw.write(cName+",");
                        i++;
                    }

                }
                bw.newLine();
                int k=0;
                for(String type: map.get(tableName).getDataTypes()){
                    if(k == map.get(tableName).getDataTypes().size()-1){
                        bw.write(type);
                    }else{
                        bw.write(type+",");
                        k++;
                    }

                }

                bw.newLine();
            }

            bw.flush();
            bw.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }


    }
    public Map<String,Schema> getSchema(){
        Map<String ,Schema> map = new HashMap<>();
        File file = new File("schema.txt");
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        try {
            br = new BufferedReader(new FileReader(file));
            List<String> fileLines = br.lines().toList();
            List<String> lines = new ArrayList<>();
            for(String s : fileLines){
                if(!s.isBlank()){
                    lines.add(s);
                }
            }
            if(lines.size() > 0) {
                for (int i = 0; i < lines.size() - 2; i += 3) {
                    Schema o = new Schema();
                    String name = lines.get(i);
                    List<String> cNames = new ArrayList<>(Arrays.stream(lines.get(i + 1).split(",")).toList());
                    o.setColumns(cNames);
                    List<String> types = new ArrayList<>(Arrays.stream(lines.get(i + 2).split(",")).toList());
                    o.setDataTypes(types);
                    map.put(name,o);

                }
            }

            br.close();

        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        return map;

    }

    public void deleteSchema(String tableName , Map<String, Schema> map){
       map.remove(tableName);
       saveSchemaFile(map);

    }

    public void alterTable(Table table,String column , Enum<DataType> type){
        //Alter table table_name
    }


}
