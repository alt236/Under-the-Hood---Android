package aws.apps.underthehood.ui.main;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import aws.apps.underthehood.R;
import aws.apps.underthehood.ui.views.GuiCreation;

public class TableConverter {
    private final String TAG = this.getClass().getName();
    private final GuiCreation gui;

    public TableConverter(final GuiCreation gui) {
        this.gui = gui;
    }

    void listToTable(TableLayout.LayoutParams lp, TableLayout t, List<String> l) {
        String chr;
        String seperator = gui.getString(R.string.seperator_identifier);

        try {
            if (l.size() == 0) {
                return;
            }

            for (int i = 0; i < l.size(); i++) {
                chr = l.get(i).substring(0, 1);

                if (chr.equals("#") || chr.equals("$") || chr.equals(">")) {
                    t.addView(gui.createTitleRow(l.get(i)), lp);
                } else if (chr.equals(seperator)) {
                    t.addView(gui.createSeperatorRow(l.get(i)), lp);
                } else {
                    t.addView(gui.createDataRow(l.get(i)), lp);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "^ listToTable() Exception: " + e.getMessage());
        }
    }

    public List<String> tableToList(TableLayout t) {
        List<String> l = new ArrayList<>();

        l.clear();

        for (int i = 0; i <= t.getChildCount() - 1; i++) {
            TableRow row = (TableRow) t.getChildAt(i);

            for (int j = 0; j <= row.getChildCount() - 1; j++) {
                View v = row.getChildAt(j);

                try {
                    if (v.getClass() == Class.forName("android.widget.TextView")) {
                        TextView tmp = (TextView) v;
                        l.add(tmp.getText() + "");
                    }
                } catch (Exception e) {
                    Log.e(TAG, "^ tableToString: " + e.getMessage());
                }
            }
        }
        return l;
    }

    public String tableToString(TableLayout t) {
        String res = "";

        for (int i = 0; i <= t.getChildCount() - 1; i++) {
            TableRow row = (TableRow) t.getChildAt(i);

            for (int j = 0; j <= row.getChildCount() - 1; j++) {
                View v = row.getChildAt(j);

                try {
                    if (v.getClass() == Class.forName("android.widget.TextView")) {
                        TextView tmp = (TextView) v;
                        res += tmp.getText();

                        if (j == 0) {
                            res += " ";
                        }
                    } else if (v.getClass() == Class.forName("android.widget.EditText")) {
                        EditText tmp = (EditText) v;
                        res += tmp.getText().toString();
                    }
                } catch (Exception e) {
                    res = e.toString();
                    Log.e(TAG, "^ tableToString: " + res);
                }
            }
            res += "\n";
        }
        return res;
    }

}
