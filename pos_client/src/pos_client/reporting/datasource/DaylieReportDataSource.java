package pos_client.reporting.datasource;

import java.util.List;

/**
 *
 * @author alves
 */
public interface DaylieReportDataSource
{

    public List getData();

    public String getReportTitle();

    public String getReportId();

    public String getCompanyName();

    public String getOrgNo();

    public String getCashRegisterId();

    public List<String> getColumnNames();
}
