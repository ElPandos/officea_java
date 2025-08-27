package pos_client.reporting;

import static pos_client.reporting.Templates.italicStyle;
import static pos_client.reporting.Templates.boldCenteredStyle;
import pos_client.reporting.datasource.DaylieReportDataSource;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

/**
 *
 * @author alves
 */
public class DailyReportBuilder
{

    DaylieReportDataSource daylieReportDataSource;

    public DailyReportBuilder(DaylieReportDataSource daylieReportDataSource)
    {
        this.daylieReportDataSource = daylieReportDataSource;
    }

    public JasperReportBuilder build() throws Exception
    {
        JasperReportBuilder reportBuilder = report();
        reportBuilder.setTemplate(Templates.reportTemplate)
                .title(createTitleComponent(daylieReportDataSource.getCompanyName(),
                        daylieReportDataSource.getOrgNo(),
                        daylieReportDataSource.getCashRegisterId(),
                        daylieReportDataSource.getReportTitle(),
                        daylieReportDataSource.getReportId()))
                .pageFooter(Templates.footerComponent)
                .setDataSource(new JRMapCollectionDataSource(daylieReportDataSource.getData()));

        /* Create columns based on data source */
        if (daylieReportDataSource.getColumnNames().isEmpty())
        {
            throw new Exception("Report columns can't be empty");
        } else
        {
            for (String columnName : daylieReportDataSource.getColumnNames())
            {
                reportBuilder.addColumn(col.column(columnName, columnName, type.stringType()));
            }
        }

        return reportBuilder;
    }

    private ComponentBuilder<?, ?> createTitleComponent(String companyName, String orgNo, String checkoutId, String reportTitle, String reportId)
    {
        String printDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        String printTime = LocalTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME);

        ComponentBuilder<?, ?> dynamicReportsComponent
                = cmp.horizontalList(//cmp.image(Templates.class.getResource("/images/officea-systems_small.png")).setFixedDimension(60, 60),
                        cmp.verticalList(cmp.text(companyName).setStyle(italicStyle).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT),
                                cmp.text(orgNo).setStyle(italicStyle).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT),
                                cmp.text("Kassa ID: " + checkoutId).setStyle(italicStyle).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT)),
                        cmp.text(reportTitle).setStyle(boldCenteredStyle),
                        cmp.verticalList(
                                cmp.text("Utskriven: " + printDate).setStyle(italicStyle).setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT),
                                cmp.text("Klockan: " + printTime).setStyle(italicStyle).setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT),
                                cmp.text("Rapport ID:" + reportId).setStyle(italicStyle).setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT)
                        )
                );

        return cmp.horizontalList()
                .add(
                        dynamicReportsComponent)
                .newRow()
                .add(cmp.line())
                .newRow()
                .add(cmp.verticalGap(10));
    }

}
