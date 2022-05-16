import exception.ColumnNotFoundException;
import exception.InvalidDataTypeException;
import exception.InvalidInputException;
import exception.SyntaxErrorException;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.drop.Drop;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.statement.update.UpdateSet;

import java.util.*;


public class ResolveQuery {
    StorageEngine storageEngine;
    ResolveQuery(){
        this.storageEngine = new StorageEngine();
    }

    public Table resolveSelectStatement(String query)  {
        Statement statement = null;
        try {
            statement = CCJSqlParserUtil.parse(query);
        } catch (JSQLParserException e) {
            throw new SyntaxErrorException("Unexpected token in query");
        }
        Select selectStatement = (Select) statement;
        PlainSelect pl = (PlainSelect)selectStatement.getSelectBody();

        Table table = storageEngine.getTable(pl.getFromItem().toString());
        String[] items = new String[pl.getSelectItems().size()];
        for (int i = 0 ; i<pl.getSelectItems().size();i++) {
            String name =pl.getSelectItems().get(i).toString();
            if(!name.equals("*") && !table.getColumns().containsKey(name)){
                throw new ColumnNotFoundException("no Column with name "+ name+" is present in "+table.getTableName());
            }
            items[i] = name;
        }

        if(pl.getWhere()==null){
            return storageEngine.getRows(table,items,null,null);
        }else{

            String column = pl.getWhere().toString().split("=")[0].trim();
            if(!table.getColumns().containsKey(column)){
                throw new ColumnNotFoundException("no Column with name "+ column+" is present in "+table.getTableName());
            }
            String val = (pl.getWhere().toString().split("=")[1].trim());
            try {
                int i = Integer.parseInt(val);
                table = storageEngine.getRows(table,items,column,i);
            } catch (NumberFormatException nfe) {
                table = storageEngine.getRows(table,items,column,val);
            }

        }
        return table;


    }
    public String resolveDropStatement(String query){
        Statement statement = null;
        try {
            statement = CCJSqlParserUtil.parse(query);
        } catch (JSQLParserException e) {
            throw new SyntaxErrorException("Unexpected token in query");
        }
        Drop dropStatement = (Drop) statement;
        String tableName = dropStatement.getName().getName();
        Table table = storageEngine.getTable(tableName);
        storageEngine.deleteTable(table);

        return "table "+tableName+" deleted Successfully";
    }
    public String resolveCreateStatement(String query){
        Statement statement = null;
        try {
            statement = CCJSqlParserUtil.parse(query);
        } catch (JSQLParserException e) {
            throw new SyntaxErrorException("Unexpected token in query");
        }
        CreateTable createStatement = (CreateTable) statement;
        Map<String, Enum<DataType>> map = new HashMap<>();
       List<String> dataType= Arrays.stream(DataType.values()).map(Enum::toString).toList();
        for(ColumnDefinition cd : createStatement.getColumnDefinitions()){
             if(!dataType.contains(cd.getColDataType().getDataType())){
                 throw new InvalidDataTypeException("Only INT and VARCHAR data types are valid");
             }
            map.put(cd.getColumnName(),DataType.valueOf(cd.getColDataType().getDataType()));
        }
        Table table = new Table();
        table.setTableName(createStatement.getTable().getName());
        table.setColumns(map);
        storageEngine.createTable(table);


        return "Table successfully created";
    }
    public String resolveUpdateStatement(String query){
        Statement statement = null;
        try {
            statement = CCJSqlParserUtil.parse(query);
        } catch (JSQLParserException e) {
            throw new SyntaxErrorException("Unexpected token in query");
        }
        Update updateStatement = (Update) statement;
        String tableName = updateStatement.getTable().getName();
        List<Column> colNames = new ArrayList<>();
        List<Expression> values =  new ArrayList<>();
        ArrayList<UpdateSet> updateSets = updateStatement.getUpdateSets();
        for(UpdateSet set : updateSets){
            colNames.add(set.getColumns().get(0));
            values.add(set.getExpressions().get(0));
        }
        Table table = storageEngine.getTable(tableName);

        String[] cols = new String[colNames.size()];
        Object[] val = new Object[colNames.size()];
        for(int i = 0 ; i<colNames.size();i++){
            cols[i] = colNames.get(i).getColumnName();
            if(!table.getColumns().containsKey(cols[i])){
                throw new ColumnNotFoundException("column with name "+cols[i]+" does not exists");
            }
            String value = values.get(i).toString();
            if(table.getColumns().get(cols[i]).equals(DataType.INT)){
                try {
                    int j = Integer.parseInt(value);
                    val[i]=j;
                } catch (NumberFormatException nfe) {
                    throw new InvalidInputException("integer excepted string given");
                }
            }else{
                val[i] = value;
            }


        }



        if(updateStatement.getWhere() != null){
            String conditionColumn = updateStatement.getWhere().toString().split("=")[0].trim();
            String conditionValue = (updateStatement.getWhere().toString().split("=")[1].trim());
            try {
                int i = Integer.parseInt(conditionValue);
                storageEngine.updateRows(table,cols,val,conditionColumn,i);
            } catch (NumberFormatException nfe) {
                storageEngine.updateRows(table,cols,val,conditionColumn,conditionValue);
            }
        }else{

            storageEngine.updateRows(table,cols,val,null,null);

        }


        return "update successful";
    }
    public String resolveInsertStatement(String query){
        Statement statement = null;
        try {
            statement = CCJSqlParserUtil.parse(query);
        } catch (JSQLParserException e) {
            throw new SyntaxErrorException(e.getMessage());
        }
        Insert insertStatement = (Insert) statement;
        List<Column> columns = insertStatement.getColumns();
        List<Expression> setExpressionList = insertStatement.getSetExpressionList();
       for(int i=0 ; i<columns.size();i++){
           System.out.println(columns.get(i).toString()+" = "+setExpressionList.get(i).toString());
       }
        return "b";
    }

}
