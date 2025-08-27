package pos_client.reporting.datasource;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import pos_client.DeleteArticle;

/**
 *
 * @author alves
 */
public abstract class AbstractDataSource implements DaylieReportDataSource
{

    @Override
    public List getData()
    {
        List rows = new ArrayList();

        if (getColumnNames().isEmpty())
        {
            Logger.getLogger(DeleteArticle.class.getName()).log(Level.WARNING, null, "ColumnNames cannot be empty");
        } else
        {

            getRows().stream().map((rowData)
                    -> 
                    {
                        Map rowToAdd = new LinkedHashMap();
                        for (int columnIndex = 0; columnIndex < rowData.length; columnIndex++)
                        {
                            rowToAdd.put(getColumnNames().get(columnIndex), rowData[columnIndex]);
                        }
                        return rowToAdd;
            }).forEach((rowToAdd)
                    -> 
                    {
                        rows.add(rowToAdd);
            });
        }

        return rows;
    }

    abstract List<String[]> getRows();

}
