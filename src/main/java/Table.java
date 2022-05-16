import java.util.List;
import java.util.Map;

public class Table {
    private String tableName;
    private Map<String,Enum<DataType>> columns;
    private List<Object[]> data;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Map<String, Enum<DataType>> getColumns() {
        return columns;
    }

    public void setColumns(Map<String, Enum<DataType>> columns) {
        this.columns = columns;
    }

    public List<Object[]> getData() {
        return data;
    }

    public void setData(List<Object[]> data) {
        this.data = data;
    }
}
