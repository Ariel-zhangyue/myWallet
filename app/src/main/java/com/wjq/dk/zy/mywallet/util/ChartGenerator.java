package com.wjq.dk.zy.mywallet.util;

import android.graphics.Color;
import android.graphics.Typeface;

import com.wjq.dk.zy.mywallet.model.Expense;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.listener.ViewportChangeListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.ComboLineColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.ComboLineColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.view.PieChartView;
import lecho.lib.hellocharts.view.PreviewColumnChartView;

/**
 * Created by wangjiaqi on 16/11/10.
 */

public class ChartGenerator {
    public static final int NUMBER_OF_MONTH = 31;
    public static final List MONTHS = Arrays.asList(new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug",
            "Sep", "Oct", "Nov", "Dec"});

    public static void generatePieChart(PieChartView pieChart, List list) {
        PieChartData pieChartData;

        List<SliceValue> values = new ArrayList<>();
        for (int i = 0; i < list.size(); ++i) {
            Expense expense = (Expense) list.get(i);
            SliceValue sliceValue = new SliceValue(Float.valueOf(expense.getAmount()), ChartUtils.pickColor());
            sliceValue.setLabel(expense.getSubcategory().getName());
            values.add(sliceValue);
        }

        pieChartData = new PieChartData(values);
        pieChartData.setHasLabels(true);
        pieChartData.setHasLabelsOnlyForSelected(false);
        pieChartData.setHasLabelsOutside(true);
        pieChartData.setHasCenterCircle(true);
        pieChartData.setValueLabelBackgroundColor(Color.TRANSPARENT);

        pieChart.setPieChartData(pieChartData);
        pieChart.setCircleFillRatio(0.85f);
        pieChart.setValueSelectionEnabled(true);

    }

    /**
     * draw a lineColumnDependencyChart where users can see lineChart details by clicking column
     */
    public static void generateLineColumnDependencyForYear(LineChartView lineChart, ColumnChartView columnChart, List listforLineChart, List listForColumnChart) {
        LineChartData lineChartData = initLineChartDataForDependency();
        lineChartData.setAxisXBottom(initAxisX(getDayInfo(listforLineChart)));
        lineChartData.setAxisYLeft(initAxisY());
        lineChart.setLineChartData(lineChartData);

        // For build-up animation you have to disable viewport recalculation.
        lineChart.setViewportCalculationEnabled(false);

        // And set initial max viewport and current viewport- remember to set viewports after data.
        Viewport v = new Viewport(0, 10, NUMBER_OF_MONTH, 0);
        lineChart.setMaximumViewport(v);
        lineChart.setCurrentViewport(v);
        lineChart.setZoomType(ZoomType.HORIZONTAL);

        ColumnChartData columnChartData = initColumnChartData(listForColumnChart, false);
        columnChartData.setAxisXBottom(initAxisX(geMonthInfo(listForColumnChart)));
        columnChartData.setAxisYLeft(initAxisY());

        columnChart.setColumnChartData(columnChartData);
        columnChart.setZoomType(ZoomType.HORIZONTAL);


        columnChart.setOnValueTouchListener(new ValueTouchListener(lineChart, columnChart, listforLineChart));
    }

    /**
     * get the LineChartData when wanna draw a LineColumnDependency
     */
    private static LineChartData initLineChartDataForDependency() {

        LineChartData lineChartData = new LineChartData();
        List<Line> lines = new ArrayList<Line>();
        List<PointValue> pointValues = new ArrayList<PointValue>();
        for (int i = 0; i < NUMBER_OF_MONTH; i++) {
            PointValue pointValue = new PointValue(i, 0);
            pointValues.add(pointValue);
        }

        Line line = new Line();
        line.setValues(pointValues);

        line.setCubic(false);
        line.setColor(Color.GREEN);// 设置折线颜色
        line.setStrokeWidth(1);// 设置折线宽度
        line.setFilled(false);// 设置折线覆盖区域是否填充
        line.setCubic(true);// 是否设置为立体的
        line.setPointColor(Color.GREEN);// 设置节点颜色
        line.setPointRadius(2);// 设置节点半径
        line.setHasLabels(false);// 是否显示节点数据
        line.setHasLines(true);// 是否显示折线
        line.setHasPoints(true);// 是否显示节点
        line.setShape(ValueShape.CIRCLE);// 节点图形样式 DIAMOND菱形、SQUARE方形、CIRCLE圆形
        line.setHasLabelsOnlyForSelected(!false);// 隐藏数据，触摸可以显示

        lines.add(line);
        lineChartData.setLines(lines);
        return lineChartData;
    }

    /**
     * generate linedata when clicking one column
     */
    private static void generateLineDataByColumn(LineChartView lineChart, ColumnChartView columnChart, List list, SubcolumnValue subcolumnValue, int columnIndex) {

        // Cancel last animation if not finished.
        lineChart.cancelDataAnimation();

        //get the line data for the selected column
        List lineValueList = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            Expense expense = (Expense) list.get(i);
            Date date = expense.getDayCreated();
            String dateString;
            if (Calendar.getInstance().get(Calendar.YEAR) == DateUtils.toCalendar(date).get(Calendar.YEAR)) {
                dateString = DateFormatUtils.format(date, "MM", Locale.ENGLISH);
            } else {
                dateString = DateFormatUtils.format(date, "MM/yyyy");
            }
            //get line data if the month of the data is equal to the column's label
            if (dateString.equals(String.valueOf(columnChart.getChartData().getAxisXBottom().getValues().get(columnIndex).getLabelAsChars()))) {
                lineValueList.add(Float.valueOf(expense.getAmount()));
            }
        }

        // Modify data targets
        Line line = lineChart.getLineChartData().getLines().get(0);// For this example there is always only one line.
        line.setColor(subcolumnValue.getColor());
        line.setPointColor(subcolumnValue.getColor());


        List<PointValue> pointValues = new ArrayList<PointValue>();
        for (int i = 0; i < lineValueList.size(); i++) {
            PointValue pointValue = new PointValue(i, line.getValues().size() > i ? line.getValues().get(i).getY() : line.getValues().get(line.getValues().size() - 1).getY());
            pointValues.add(pointValue);
        }
        line.setValues(pointValues);


        for (int i = 0; i < lineValueList.size(); i++) {
            PointValue pointValue = line.getValues().get(i);
            // Change target only for Y value.
            pointValue.setTarget(pointValue.getX(), (float) lineValueList.get(i));
        }

        Collections.sort(lineValueList);

        Viewport viewport = new Viewport(0, (float)lineValueList.get(lineValueList.size()-1), lineValueList.size(), 0);
        lineChart.setMaximumViewport(viewport);
        lineChart.setCurrentViewport(viewport);

        // Start new data animation with 300ms duration;
        lineChart.startDataAnimation(300);


    }

    /**
     * listener invoked when touching the column
     */
    private static class ValueTouchListener implements ColumnChartOnValueSelectListener {
        private LineChartView lineChart;
        private ColumnChartView columnChart;
        private List lineDataList;

        ValueTouchListener(LineChartView lineChart, ColumnChartView columnChart, List list) {
            this.lineChart = lineChart;
            this.columnChart = columnChart;
            this.lineDataList = list;
        }

        @Override
        public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
            generateLineDataByColumn(lineChart, columnChart, lineDataList, value, columnIndex);
        }

        @Override
        public void onValueDeselected() {

            // generateLineDataByColumn(lineChart,ChartUtils.COLOR_GREEN);

        }
    }


    /**
     * draw a columnChart which can be previewed to show the data in a month
     */
    public static void generatePreviewColumnChartForMonth(ColumnChartView columnChart, PreviewColumnChartView previewColumnChart, List list) {
        ColumnChartData columnChartData = initColumnChartData(list, false);
        columnChartData.setAxisXBottom(initAxisX(getDayInfo(list)));
        columnChartData.setAxisYLeft(initAxisY());

        columnChart.setColumnChartData(columnChartData);
        columnChart.setZoomEnabled(false);
        columnChart.setScrollEnabled(false);

        ColumnChartData previewData = initColumnChartData(list, false);
        for (Column column : previewData.getColumns()) {
            for (SubcolumnValue value : column.getValues()) {
                value.setColor(ChartUtils.DEFAULT_DARKEN_COLOR);
            }
        }
        previewData.setAxisXBottom(initAxisX(getDayInfo(list)));
        previewData.setAxisYLeft(initAxisY());
        previewData.setValueLabelsTextColor(Color.BLACK);// 设置数据文字颜色
        previewData.setValueLabelTextSize(15);// 设置数据文字大小
        previewData.setValueLabelTypeface(Typeface.MONOSPACE);// 设置数据文字样式

        previewColumnChart.setColumnChartData(previewData);

        previewColumnChart.setZoomEnabled(true);//设置是否支持缩放
        previewColumnChart.setInteractive(true);//设置图表是否可以与用户互动
        previewColumnChart.setValueSelectionEnabled(false);//设置图表数据是否选中进行显示
        previewColumnChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        previewColumnChart.setPreviewColor(Color.TRANSPARENT);

        previewColumnChart.setViewportChangeListener(new ViewportListener(columnChart));

        //Optional step: disable viewport recalculations, thanks to this animations will not change viewport automatically.
        previewColumnChart.setViewportCalculationEnabled(false);
        previewX(columnChart, previewColumnChart);
    }

    private static class ViewportListener implements ViewportChangeListener {
        private ColumnChartView columnChart;

        ViewportListener(ColumnChartView columnChart) {
            this.columnChart = columnChart;
        }

        @Override
        public void onViewportChanged(Viewport newViewport) {
            // don't use animation, it is unnecessary when using preview chart because usually viewport changes
            // happens to often.
            columnChart.setCurrentViewport(newViewport);
        }

    }

    private static void previewX(ColumnChartView columnChart, PreviewColumnChartView previewColumnChart) {
        // Better to not modify viewport of any chart directly so create a copy.
        Viewport tempViewport = new Viewport(columnChart.getMaximumViewport());
        // Make temp viewport smaller.
        float dx = tempViewport.width() / 4;
        tempViewport.inset(dx, 0);
        previewColumnChart.setCurrentViewportWithAnimation(tempViewport);
    }

    /**
     * draw a linechart combined with column chart to show the data in a week
     */
    public static void generateComboChartForDay(ComboLineColumnChartView comboLineColumnChart, List list) {

        ComboLineColumnChartData comboLineColumnChartData = new ComboLineColumnChartData();

        comboLineColumnChartData.setLineChartData(initLineChartData(list, true));
        comboLineColumnChartData.setColumnChartData(initColumnChartData(list, false));
        comboLineColumnChartData.setAxisXBottom(initAxisX(getDayInfo(list)));
        comboLineColumnChartData.setAxisYLeft(initAxisY());

        comboLineColumnChart.setComboLineColumnChartData(comboLineColumnChartData);
        comboLineColumnChart.setZoomEnabled(true);//设置是否支持缩放
        //comboLineColumnChart.setOnValueTouchListener(ColumnChartOnValueSelectListener touchListener);//为图表设置值得触摸事件
        comboLineColumnChart.setInteractive(true);//设置图表是否可以与用户互动
        comboLineColumnChart.setValueSelectionEnabled(false);//设置图表数据是否选中进行显示

    }


    /**
     * get the LineChartData to display line chart
     */
    private static LineChartData initLineChartData(List list, boolean isHasLable) {

        LineChartData lineChartData = new LineChartData();
        List<Line> lines = new ArrayList<Line>();
        List<PointValue> pointValues = new ArrayList<PointValue>();
        for (int i = 0; i < list.size(); i++) {
            PointValue pointValue = new PointValue(i, Float.valueOf(((Expense) list.get(i)).getAmount()));
            pointValues.add(pointValue);
        }

        Line line = new Line();
        line.setValues(pointValues);

        line.setCubic(false);
        line.setColor(Color.GREEN);// 设置折线颜色
        line.setStrokeWidth(3);// 设置折线宽度
        line.setFilled(false);// 设置折线覆盖区域是否填充
        line.setCubic(true);// 是否设置为立体的
        line.setPointColor(Color.GREEN);// 设置节点颜色
        line.setPointRadius(4);// 设置节点半径
        line.setHasLabels(isHasLable);// 是否显示节点数据
        line.setHasLines(true);// 是否显示折线
        line.setHasPoints(true);// 是否显示节点
        line.setShape(ValueShape.CIRCLE);// 节点图形样式 DIAMOND菱形、SQUARE方形、CIRCLE圆形
        line.setHasLabelsOnlyForSelected(!isHasLable);// 隐藏数据，触摸可以显示

        lines.add(line);
        lineChartData.setLines(lines);
        lineChartData.setValueLabelBackgroundColor(Color.TRANSPARENT);
        lineChartData.setValueLabelBackgroundEnabled(true);
        return lineChartData;
    }

    /**
     * get the ColumnChartData to display Column chart
     */
    private static ColumnChartData initColumnChartData(List list, boolean isHasLable) {
        ColumnChartData columnChartData = new ColumnChartData();

        List<Column> columns = new ArrayList<Column>();

        for (int i = 0; i < list.size(); i++) {
            SubcolumnValue subcolumnValue = new SubcolumnValue(Float.valueOf(((Expense) list.get(i)).getAmount()), ChartUtils.pickColor());
            List<SubcolumnValue> subcolumnValues = new ArrayList<SubcolumnValue>();
            subcolumnValues.add(subcolumnValue);

            Column column = new Column();
            column.setValues(subcolumnValues);
            column.setHasLabels(isHasLable);
            column.setHasLabelsOnlyForSelected(!isHasLable);
            columns.add(column);
        }
        columnChartData.setColumns(columns);
        columnChartData.setValueLabelsTextColor(Color.BLACK);// 设置数据文字颜色
        columnChartData.setValueLabelTextSize(15);// 设置数据文字大小
        columnChartData.setValueLabelTypeface(Typeface.MONOSPACE);// 设置数据文字样式
        return columnChartData;
    }

    /**
     * get the day information from the parameter Expense list
     */
    private static List getDayInfo(List list) {
        List dates = new ArrayList();
        if (list != null && list.size() != 0) {
            if (list.get(0) instanceof Expense) {
                for (int i = 0; i < list.size(); i++) {
                    Expense expense = (Expense) list.get(i);
                    Date date = expense.getDayCreated();
                    if (Calendar.getInstance().YEAR == DateUtils.toCalendar(date).YEAR) {
                        dates.add(DateFormatUtils.format(date, "dd/MM"));
                    } else {
                        dates.add(DateFormatUtils.format(date, "dd/MM/yyyy"));
                    }
                }
            }
        }
        return dates;
    }

    /**
     * get the month information from the parameter Expense list
     */
    private static List geMonthInfo(List list) {
        List dates = new ArrayList();
        if (list != null && list.size() != 0) {
            if (list.get(0) instanceof Expense) {
                for (int i = 0; i < list.size(); i++) {
                    Expense expense = (Expense) list.get(i);
                    Date date = expense.getDayCreated();
                    if (Calendar.getInstance().get(Calendar.YEAR) == DateUtils.toCalendar(date).get(Calendar.YEAR)) {
                        dates.add(DateFormatUtils.format(date, "MM", Locale.ENGLISH));
                    } else {
                        dates.add(DateFormatUtils.format(date, "MM/yyyy"));
                    }
                }
            }
        }
        return dates;
    }

    /**
     * initiate the axis X for chart in week query
     */
    private static Axis initAxisX(List datelist) {
        Axis axisX = new Axis();
        axisX.setHasTiltedLabels(true);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.BLACK);  //设置字体颜色
        axisX.setLineColor(Color.BLACK);
        //axisX.setName("date");  //表格名称
        axisX.setTextSize(10);//设置字体大小
        //axisX.setMaxLabelChars(7); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length
        axisX.setHasLines(false); //x 轴分割线

        List axisValues = new ArrayList();
        for (int i = 0; i < datelist.size(); i++) {
            AxisValue value = new AxisValue(i);
            value.setLabel((String) datelist.get(i));
            axisValues.add(value);
        }
        axisX.setValues(axisValues);  //填充X轴的坐标名称

        return axisX;
    }

    /**
     * initiate the axis y for chart in week query
     */
    private static Axis initAxisY() {
        Axis axisY = new Axis();
        axisY.setName("");//y轴标注
        axisY.setTextSize(10);//设置字体大小
        axisY.setLineColor(Color.BLACK);
        axisY.setTextColor(Color.BLACK);
        return axisY;
    }

}
