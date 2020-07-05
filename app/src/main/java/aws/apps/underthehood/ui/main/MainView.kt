package aws.apps.underthehood.ui.main

import android.app.Activity
import android.widget.TableLayout
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import aws.apps.underthehood.R
import aws.apps.underthehood.adapters.ViewPagerAdapter
import aws.apps.underthehood.time.TimeFormatter
import aws.apps.underthehood.ui.views.GuiCreation
import com.viewpagerindicator.TabPageIndicator
import uk.co.alt236.underthehood.commandrunner.model.Result
import java.util.*

class MainView(activity: Activity) {
    private val timeFormatter = TimeFormatter()
    private val gui: GuiCreation = GuiCreation(activity)
    private val tableConverter = TableConverter(gui)
    private val tableList: MutableList<TableLayout> = ArrayList()

    private val lblRootStatus: TextView
    private val lblTimeDate: TextView
    private val lblDevice: TextView
    private val tableHardware: TableLayout
    private val tableIpRoute: TableLayout
    private val tablePs: TableLayout
    private val tableOther: TableLayout
    private val tableNetstat: TableLayout
    private val tableProc: TableLayout
    private val tableSysProp: TableLayout
    private val tableDeviceInfo: TableLayout

    private fun createTable(adapter: ViewPagerAdapter, title: String): TableLayout {
        val v = gui.createScrollableTable()
        val table = v.findViewById<TableLayout>(R.id.table)
        adapter.addView(v, title)
        tableList.add(table)
        return table
    }

    fun clear() {
        for (table in tableList) {
            table.removeAllViews()
        }
        lblRootStatus.text = ""
        lblTimeDate.text = ""
    }

    fun showResults(result: Result) {
        lblRootStatus.text = if (result.isRooted) "Rooted" else "Not rooted"
        lblTimeDate.text = timeFormatter.getIsoDateTime(result.timestamp)
        lblDevice.text = result.deviceinfo

        tableConverter.listToTable(tableOther, result.otherData)
        tableConverter.listToTable(tableIpRoute, result.ipRouteData)
        tableConverter.listToTable(tablePs, result.psData)
        tableConverter.listToTable(tableProc, result.procData)
        tableConverter.listToTable(tableHardware, result.hardwareData)
        tableConverter.listToTable(tableNetstat, result.netstatData)
        tableConverter.listToTable(tableSysProp, result.sysPropData)
    }

    init {
        val viewPager: ViewPager = activity.findViewById(R.id.pager)
        val indicator: TabPageIndicator = activity.findViewById(R.id.indicator)
        val adapter = ViewPagerAdapter()
        tableDeviceInfo = activity.findViewById(R.id.main_table_device_info)
        lblRootStatus = activity.findViewById(R.id.tvRootStatus)
        lblTimeDate = activity.findViewById(R.id.tvTime)
        lblDevice = activity.findViewById(R.id.tvDevice)
        tableSysProp = createTable(adapter, "prop")
        tableIpRoute = createTable(adapter, "ip")
        tableProc = createTable(adapter, "proc")
        tableHardware = createTable(adapter, "hardware")
        tableNetstat = createTable(adapter, "netstat")
        tablePs = createTable(adapter, "ps")
        tableOther = createTable(adapter, "other")
        viewPager.adapter = adapter
        indicator.setViewPager(viewPager)
    }
}