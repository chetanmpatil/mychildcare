package cdccm.servicesimpl;

import java.awt.Color;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;
import ar.com.fdvs.dj.domain.builders.DynamicReportBuilder;
import ar.com.fdvs.dj.domain.builders.StyleBuilder;
import ar.com.fdvs.dj.domain.constants.Border;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import ar.com.fdvs.dj.domain.constants.Transparency;
import ar.com.fdvs.dj.domain.constants.VerticalAlign;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import cdccm.dbServices.MySQLDBConnector;
import cdccm.pojo.ChildNamePlate;
import cdccm.pojo.ChildReportPOJO;
import cdccm.pojo.SchedulePOJO;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class ReportFiller {
	private Collection<ChildReportPOJO> reportlist = new ArrayList<>();
	private final Collection<SchedulePOJO> schedulelist = new ArrayList<>();
	private MySQLDBConnector dbConnector;

	public ReportFiller(Collection<? extends Object> listOfObjectsforReport) {
		dbConnector = MySQLDBConnector.getInstance();
		for (Object obj : listOfObjectsforReport) {
			if (obj.getClass().getName().equals("cdccm.pojo.ChildReportPOJO")) {
				System.out.println("Inside cdccm.pojo.ChildReportPOJO");
				this.reportlist = (Collection<ChildReportPOJO>) listOfObjectsforReport;
				// reportlist.addAll((Collection<? extends
				// ChildReportPOJO>)option);
				break;
			} else if (obj.getClass().getName().equals("cdccm.pojo.SchedulePOJO")) {
				schedulelist.addAll((Collection<? extends SchedulePOJO>) listOfObjectsforReport);
				break;
			}
		}
	}

	public JasperPrint getReport(String typeofreport, int childid)
			throws ColumnBuilderException, JRException, ClassNotFoundException {
		JasperPrint jp = null;
		Style headerStyle = createHeaderStyle();// style setup
		Style detailTextStyle = createDetailTextStyle();
		Style detailNumberStyle = createDetailNumberStyle();
		/* check whether report request is for schedule or performance */
		if (typeofreport.equals("performancereport")) {
			/* make call for performance report skeleton creation */
			DynamicReport dynaReport = getPerformanceReport(headerStyle, detailTextStyle, detailNumberStyle);
			/* fill the empty skeleton */
			jp = DynamicJasperHelper.generateJasperPrint(dynaReport, new ClassicLayoutManager(),
					new JRBeanCollectionDataSource(reportlist));
		} else if (typeofreport.equals("schedulereport")) {
			System.out.println("cdccm.pojo.SchedulePOJO");
			/* make call for performance report skeleton creation */
			DynamicReport dynaReport = getScheduleReport(headerStyle, detailTextStyle, detailNumberStyle, childid);
			/* fill the empty skeleton */
			jp = DynamicJasperHelper.generateJasperPrint(dynaReport, new ClassicLayoutManager(),
					new JRBeanCollectionDataSource(schedulelist));
		}
		return jp;
	}

	private DynamicReport getScheduleReport(Style headerStyle, Style detailTextStyle, Style detailNumberStyle,
			int childid) throws ColumnBuilderException, ClassNotFoundException {
		ChildNamePlate childnameplate = null;
		DynamicReportBuilder report = new DynamicReportBuilder();

		childnameplate = getchildNameplate(childid);// takes child nameplate

		AbstractColumn plantime = createColumn("plantime", String.class, "Time", 50, headerStyle, detailTextStyle);// creates
																													// column
		AbstractColumn plan = createColumn("plan", String.class, "Activity", 50, headerStyle, detailTextStyle);
		report.addColumn(plantime).addColumn(plan);

		StyleBuilder titleStyle = new StyleBuilder(true);
		titleStyle.setHorizontalAlign(HorizontalAlign.CENTER);
		titleStyle.setFont(Font.COMIC_SANS_BIG);

		StyleBuilder subTitleStyle1 = new StyleBuilder(true);
		subTitleStyle1.setHorizontalAlign(HorizontalAlign.JUSTIFY);
		subTitleStyle1.setFont(Font.COMIC_SANS_BIG);

		StyleBuilder subTitleStyle2 = new StyleBuilder(true);
		subTitleStyle2.setHorizontalAlign(HorizontalAlign.JUSTIFY);
		subTitleStyle2.setFont(Font.COMIC_SANS_BIG);

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = dateFormat.format(new Date());
		report.setTitle("Child Plan of the week,  Date:" + date + ")");
		report.setTitleStyle(titleStyle.build());

		report.setSubtitle("Roll No: " + childid + ". Name: " + childnameplate.getChild_first_name() + ". Surname: "
				+ childnameplate.getChild_last_name() + ". DOB: " + childnameplate.getDate_of_birth() + ". Group: "
				+ childnameplate.getAge_group())
				.setDefaultStyles(headerStyle, headerStyle, headerStyle, detailTextStyle);
		report.setSubtitleHeight(40);
		report.setSubtitleStyle(subTitleStyle1.build());
		report.setUseFullPageWidth(true);
		System.out.println("insede printReport 2 " + childid);
		return report.build();

	}

	// a service which communicates with db and takes child name plate
	private ChildNamePlate getchildNameplate(int childid) {

		ChildNamePlate childnameplate = null;

		final String sql = "select ci.name,ci.surname,ci.dob,ag.name " + "from child_info ci join age_group ag "
				+ "on(ci.fk_age_group=ag.idage_group) " + "where ci.idchild=?;";
		try {
			ResultSet resultset = dbConnector.getReport(sql, childid);
			if (resultset.next()) {
				childnameplate = new ChildNamePlate(resultset.getString(1), resultset.getString(2),
						resultset.getString(3), resultset.getString(4));
			}

		} catch (SQLException e) {

			e.printStackTrace();
		}
		return childnameplate;

	}

	// follwoing method return the style object for: headers,content,numbers
	// ezc.
	private Style createHeaderStyle() {
		StyleBuilder sb = new StyleBuilder(true);
		sb.setFont(Font.COMIC_SANS_BIG_BOLD);
		sb.setBorder(Border.THIN());

		sb.setBorder(Border.PEN_2_POINT());
		sb.setHorizontalAlign(HorizontalAlign.CENTER);
		sb.setVerticalAlign(VerticalAlign.MIDDLE);
		sb.setTextColor(Color.BLACK);
		sb.setBackgroundColor(Color.PINK);
		sb.setTransparency(Transparency.OPAQUE);
		return sb.build();
	}

	private Style createDetailTextStyle() {
		StyleBuilder sb = new StyleBuilder(true);
		sb.setFont(Font.VERDANA_MEDIUM);
		sb.setBorder(Border.THIN());
		sb.setTextColor(Color.GREEN);
		sb.setHorizontalAlign(HorizontalAlign.LEFT);
		sb.setVerticalAlign(VerticalAlign.MIDDLE);
		sb.setPaddingLeft(5);
		return sb.build();
	}

	private Style createDetailNumberStyle() {
		StyleBuilder sb = new StyleBuilder(true);
		sb.setFont(Font.VERDANA_MEDIUM);
		sb.setBorder(Border.DOTTED());
		sb.setHorizontalAlign(HorizontalAlign.RIGHT);
		sb.setVerticalAlign(VerticalAlign.MIDDLE);
		sb.setPaddingRight(5);
		return sb.build();
	}

	private AbstractColumn createColumn(String property, Class type, String title, int width, Style headerStyle,
			Style detailStyle) throws ColumnBuilderException {
		AbstractColumn columnState = ColumnBuilder.getNew().setColumnProperty(property, type.getName()).setTitle(title)
				.setWidth(Integer.valueOf(width)).setStyle(detailStyle).setHeaderStyle(headerStyle).build();
		return columnState;
	}

	private DynamicReport getPerformanceReport(Style headerStyle, Style detailTextStyle, Style detailNumStyle)
			throws ColumnBuilderException, ClassNotFoundException {
		Iterator<ChildReportPOJO> it = reportlist.iterator();
		while (it.hasNext()) {
			ChildReportPOJO p = (ChildReportPOJO) it.next();
			System.out.println(p.getChildid() + p.getName() + p.getSurname() + p.getDateOfBirth() + p.getMon());
		}

		DynamicReportBuilder report = new DynamicReportBuilder();

		AbstractColumn activityName = createColumn("activityName", String.class, "Activity Name", 50, headerStyle,
				detailTextStyle);
		AbstractColumn sessionname = createColumn("sessionname", String.class, "Session", 50, headerStyle,
				detailTextStyle);
		AbstractColumn providername = createColumn("providername", String.class, "Provider Name", 50, headerStyle,
				detailTextStyle);
		AbstractColumn feedback = createColumn("feedback", String.class, "feedback", 100, headerStyle, detailTextStyle);
		// AbstractColumn mon = createColumn("mon", Integer.class, "MON", 20,
		// headerStyle, detailNumStyle);
		// AbstractColumn tue = createColumn("tue", Integer.class, "TUE", 20,
		// headerStyle, detailNumStyle);
		// AbstractColumn wen = createColumn("wen", Integer.class, "WEN", 20,
		// headerStyle, detailNumStyle);
		// AbstractColumn thu = createColumn("thu", Integer.class, "THU", 20,
		// headerStyle, detailNumStyle);
		// AbstractColumn fri = createColumn("fri", Integer.class, "FRI", 20,
		// headerStyle, detailNumStyle);
		// AbstractColumn total = createColumn("total", Integer.class, "Total",
		// 20, headerStyle, detailNumStyle);
		// AbstractColumn percent = createColumn("percentage", Float.class,
		// "Percentage", 20, headerStyle, detailNumStyle);

		report.addColumn(activityName).addColumn(sessionname).addColumn(providername).addColumn(feedback);
		// addColumn(mon).addColumn(tue).addColumn(wen)
		// .addColumn(thu).addColumn(fri).addColumn(total).addColumn(percent);

		StyleBuilder titleStyle = new StyleBuilder(true);
		titleStyle.setHorizontalAlign(HorizontalAlign.CENTER);
		titleStyle.setBackgroundColor(Color.YELLOW);
		titleStyle.setFont(Font.COMIC_SANS_BIG);

		StyleBuilder subTitleStyle1 = new StyleBuilder(true);
		subTitleStyle1.setHorizontalAlign(HorizontalAlign.JUSTIFY);
		subTitleStyle1.setBackgroundColor(Color.PINK);
		subTitleStyle1.setFont(Font.COMIC_SANS_BIG);

		StyleBuilder subTitleStyle2 = new StyleBuilder(true);
		subTitleStyle2.setHorizontalAlign(HorizontalAlign.JUSTIFY);
		subTitleStyle2.setBackgroundColor(Color.PINK);
		subTitleStyle2.setFont(Font.COMIC_SANS_BIG);

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = dateFormat.format(new Date());
		report.setTitle("Child Report   on:" + date + ")");
		report.setTitleStyle(titleStyle.build());

		report.setSubtitle(date).setDefaultStyles(headerStyle, headerStyle, headerStyle, detailTextStyle);
		report.setSubtitleStyle(subTitleStyle2.build());

		ChildReportPOJO childreportpojo = reportlist.iterator().next();

		report.setSubtitle("RollNO: " + childreportpojo.getChildid() + ". Name: " + childreportpojo.getName()
				+ ". Surname: " + childreportpojo.getSurname() + ". Group: " + childreportpojo.getAgeGroup() + ". DOB: "
				+ childreportpojo.getDateOfBirth())
				.setDefaultStyles(headerStyle, headerStyle, headerStyle, detailTextStyle);
		report.setSubtitleHeight(40);
		report.setSubtitleStyle(subTitleStyle1.build());

		report.setUseFullPageWidth(true);

		return report.build();
	}

}