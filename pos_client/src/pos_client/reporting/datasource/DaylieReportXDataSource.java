package pos_client.reporting.datasource;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 *
 * @author alves
 */
public class DaylieReportXDataSource extends AbstractDataSource
{

    @Override
    List<String[]> getRows()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getReportTitle()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getReportId()
    {
        String printTime = LocalTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME);
        printTime = printTime.replace(":", "");
        printTime = printTime.replace(".", "");
        return printTime;
    }

    @Override
    public String getCompanyName()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getOrgNo()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getCashRegisterId()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<String> getColumnNames()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
