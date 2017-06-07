package cdccm.servicesimpl;

import java.awt.Color;
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
import cdccm.pojo.ChildReportPOJO;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class ReportFiller {
	private final Collection<ChildReportPOJO> list = new ArrayList<>();

	public ReportFiller(Collection<ChildReportPOJO> c) {
		list.addAll(c);
		Iterator<ChildReportPOJO> it = list.iterator();
		while (it.hasNext()) {
			ChildReportPOJO childreportpojo = it.next();
			System.out.println(childreportpojo.getName() + " " + childreportpojo.getPercentage() + ""
					+ childreportpojo.getTotal());
		}
	}

	public JasperPrint getReport() throws ColumnBuilderException, JRException, ClassNotFoundException {
		Style headerStyle = createHeaderStyle();
		Style detailTextStyle = createDetailTextStyle();
		Style detailNumberStyle = createDetailNumberStyle();
		DynamicReport dynaReport = getReport(headerStyle, detailTextStyle, detailNumberStyle);
		JasperPrint jp = DynamicJasperHelper.generateJasperPrint(dynaReport, new ClassicLayoutManager(),
				new JRBeanCollectionDataSource(list));
		return jp;
	}

	private Style createHeaderStyle() {
		StyleBuilder sb = new StyleBuilder(true);
		sb.setFont(Font.VERDANA_MEDIUM_BOLD);
		sb.setBorder(Border.THIN());
		sb.setBorderBottom(Border.PEN_2_POINT());

		sb.setHorizontalAlign(HorizontalAlign.CENTER);
		sb.setVerticalAlign(VerticalAlign.MIDDLE);
		sb.setTransparency(Transparency.OPAQUE);
		return sb.build();
	}

	private Style createDetailTextStyle() {
		StyleBuilder sb = new StyleBuilder(true);
		sb.setFont(Font.VERDANA_MEDIUM);
		sb.setBorder(Border.DOTTED());

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

	private DynamicReport getReport(Style headerStyle, Style detailTextStyle, Style detailNumStyle)
			throws ColumnBuilderException, ClassNotFoundException {

		DynamicReportBuilder report = new DynamicReportBuilder();
	
		
		AbstractColumn activityName = createColumn("activityName", String.class, "Activity Name", 50, headerStyle,
				detailTextStyle);
		AbstractColumn sessionname = createColumn("sessionname", String.class, "Session", 50, headerStyle,
				detailTextStyle);
		AbstractColumn mon = createColumn("mon", Integer.class, "MON", 10, headerStyle, detailNumStyle);
		AbstractColumn tue = createColumn("tue", Integer.class, "TUE", 10, headerStyle, detailNumStyle);
		AbstractColumn wen = createColumn("wen", Integer.class, "WEN", 10, headerStyle, detailNumStyle);
		AbstractColumn thu = createColumn("thu", Integer.class, "THU", 10, headerStyle, detailNumStyle);
		AbstractColumn fri = createColumn("fri", Integer.class, "FRI", 10, headerStyle, detailNumStyle);
		AbstractColumn total = createColumn("total", Integer.class, "Total", 10, headerStyle, detailNumStyle);
		AbstractColumn percent = createColumn("percentage", Float.class, "Percentage", 20, headerStyle, detailNumStyle);
		
		
		report.addColumn(activityName).addColumn(sessionname).addColumn(mon).addColumn(tue).addColumn(wen)
				.addColumn(thu).addColumn(fri).addColumn(total).addColumn(percent);
				
				 
//				
//		AbstractColumn childid = createColumn("childid", Integer.class, "Name", 30, headerStyle, detailNumStyle);
//
//		AbstractColumn name = createColumn("name", String.class, "Name", 30, headerStyle, detailNumStyle);
//		AbstractColumn surname = createColumn("surname", String.class, "Name", 30, headerStyle, detailNumStyle);
//		AbstractColumn dateOfBirth = createColumn("dateOfBirth", String.class, "Name", 30, headerStyle, detailNumStyle);
//		AbstractColumn ageGroup = createColumn("ageGroup", String.class, "Name", 30, headerStyle, detailNumStyle);

		StyleBuilder titleStyle = new StyleBuilder(true);
		titleStyle.setHorizontalAlign(HorizontalAlign.CENTER);
		titleStyle.setBackgroundColor(Color.YELLOW);
		titleStyle.setFont(Font.COMIC_SANS_BIG);
		

		StyleBuilder subTitleStyle2 = new StyleBuilder(true);
		subTitleStyle2.setHorizontalAlign(HorizontalAlign.JUSTIFY);
		subTitleStyle2.setBackgroundColor(Color.PINK);
		subTitleStyle2.setFont(Font.COMIC_SANS_BIG);
		
		report.setTitle("Child Report");
		report.setTitleStyle(titleStyle.build());

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = dateFormat.format(new Date());
		report.setSubtitle(date);
		report.setSubtitleStyle(subTitleStyle2.build());
		ChildReportPOJO childreportpojo=list.iterator().next();
        report.setSubtitle("RollNO: "+childreportpojo.getChildid()+", Name: "+childreportpojo.getName()+", Surname: "+childreportpojo.getSurname()+", Group: "+childreportpojo.getAgeGroup()+", DOB: "+childreportpojo.getDateOfBirth());
		report.setSubtitleHeight(40);
		report.setSubtitleStyle(subTitleStyle2.build());
        report.setUseFullPageWidth(true);

		return report.build();
	}
}