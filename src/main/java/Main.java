import exception.SyntaxErrorException;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.drop.Drop;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;

public class Main {
   private final ResolveQuery resolveQuery;
   private final Display display;
   Main(){
       this.resolveQuery =new ResolveQuery();
       this.display = new Display();
   }
    public void checkSyntaxError(String query){
        if(query.charAt(query.length()-1)!=';'){
            throw new SyntaxErrorException("Semicolon is missing");
        }
    }
    public void evaluateStatement(String query){
       try {
           checkSyntaxError(query);
           Statement statement = null;

           statement = CCJSqlParserUtil.parse(query);


           if(statement.getClass().equals(Select.class)){
               display.displayData(resolveQuery.resolveSelectStatement(query));
           }else if(statement.getClass().equals(CreateTable.class)){
               System.out.println(resolveQuery.resolveCreateStatement(query));
           }else if(statement.getClass().equals(Update.class)){
               System.out.println(resolveQuery.resolveUpdateStatement(query));
           }
           else if(statement.getClass().equals(Drop.class)){
               System.out.println(resolveQuery.resolveDropStatement(query));
           } else if(statement.getClass().equals(Insert.class)){
               System.out.println(resolveQuery.resolveInsertStatement(query));
           }else if(statement.getClass().equals(Delete.class)){
               System.out.println(resolveQuery.resolveDeleteQuery(query));
           }
       }catch (Exception e){
           System.out.println(e.getMessage());
       }

    }
}
