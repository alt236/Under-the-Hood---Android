package aws.apps.underthehood;

import android.app.Activity;
import android.content.res.Resources;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

import androidx.viewpager.widget.ViewPager;
import aws.apps.underthehood.adapters.ViewPagerAdapter;
import aws.apps.underthehood.ui.FontSizeChanger;
import aws.apps.underthehood.ui.GuiCreation;

public class ViewAbstraction {
    private static final String TAG = ViewAbstraction.class.getSimpleName();
    private final TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
    private final FontSizeChanger fontSizeChanger = new FontSizeChanger();
    private final List<TableLayout> tableList = new ArrayList<>();

    private final TextView lblRootStatus;
    private final TextView lblTimeDate;
    private final TextView lblDevice;
    private final Resources resources;
    private final GuiCreation gui;

    private final TableLayout tableIpconfig;
    private final TableLayout tableIpRoute;
    private final TableLayout tablePs;
    private final TableLayout tableOther;
    private final TableLayout tableNetstat;
    private final TableLayout tableProc;
    private final TableLayout tableSysProp;
    private final TableConverter tableConverter;

    private final TableLayout tableDeviceInfo;

    private float mDataTextSize = 0;

    public ViewAbstraction(Activity activity) {
        resources = activity.getResources();
        gui = new GuiCreation(activity);
        tableConverter = new TableConverter(gui);

        tableDeviceInfo = activity.findViewById(R.id.main_table_device_info);
        lblRootStatus = activity.findViewById(R.id.tvRootStatus);
        lblTimeDate = activity.findViewById(R.id.tvTime);
        lblDevice = activity.findViewById(R.id.tvDevice);
        final ViewPager mViewPager = activity.findViewById(R.id.pager);
        final TabPageIndicator mIndicator = activity.findViewById(R.id.indicator);

        final ViewPagerAdapter adapter = new ViewPagerAdapter();

        tableIpconfig = createTable(adapter, "netcfg / ipconfig");
        tableIpRoute = createTable(adapter, "ip");
        tableNetstat = createTable(adapter, "netstat");
        tableProc = createTable(adapter, "proc");
        tablePs = createTable(adapter, "ps");
        tableSysProp = createTable(adapter, "prop");
        tableOther = createTable(adapter, "other");

        mViewPager.setAdapter(adapter);
        mIndicator.setViewPager(mViewPager);
    }


    private TableLayout createTable(ViewPagerAdapter adapter, String title) {
        final View v = gui.createScrollableTable();
        adapter.addView(v, title);

        final TableLayout table = v.findViewById(R.id.table);
        tableList.add(table);
        return table;
    }


    public void clear() {
        for (TableLayout table : tableList) {
            table.removeAllViews();
        }

        lblRootStatus.setText("");
        lblTimeDate.setText("");
    }

    public void fontSizeDecrease() {
        mDataTextSize = mDataTextSize - 2f;
        if (mDataTextSize < resources.getDimension(R.dimen.min_font_size)) {
            mDataTextSize = resources.getDimension(R.dimen.min_font_size);
        }
        setTableFontSize(mDataTextSize);
    }

    public void fontSizeIncrease() {
        mDataTextSize = mDataTextSize + 2f;
        if (mDataTextSize > resources.getDimension(R.dimen.max_font_size)) {
            mDataTextSize = resources.getDimension(R.dimen.max_font_size);
        }
        setTableFontSize(mDataTextSize);
    }

    private void setTableFontSize(float fontSize) {
        for (TableLayout table : tableList) {
            fontSizeChanger.changeFontSize(table, fontSize);
        }
    }

    public void setTimestamp(final String timestamp) {
        lblTimeDate.setText(timestamp);
    }

    public void setRootStatus(final String text) {
        lblRootStatus.setText(text);
    }

    public void setDeviceInfo(final String text) {
        lblDevice.setText(text);
    }

    public void setTableOtherData(final List<String> list) {
        tableConverter.listToTable(tableLayoutParams, tableOther, list);
    }

    public void setTableProcData(final List<String> list) {
        tableConverter.listToTable(tableLayoutParams, tableProc, list);
    }

    public void setTableIpRouteData(final List<String> list) {
        tableConverter.listToTable(tableLayoutParams, tableIpRoute, list);
    }

    public void setTablePsData(final List<String> list) {
        tableConverter.listToTable(tableLayoutParams, tablePs, list);
    }

    public void setTableSysPropData(final List<String> list) {
        tableConverter.listToTable(tableLayoutParams, tableSysProp, list);
    }

    public void setTableIpConfigData(final List<String> list) {
        tableConverter.listToTable(tableLayoutParams, tableIpconfig, list);
    }

    public void setTableNetstatData(final List<String> list) {
        tableConverter.listToTable(tableLayoutParams, tableNetstat, list);
    }

    public void setTextSize(final float textSize) {
        mDataTextSize = textSize;
    }
}
