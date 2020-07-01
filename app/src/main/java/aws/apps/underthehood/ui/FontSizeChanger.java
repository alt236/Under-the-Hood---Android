package aws.apps.underthehood.ui;

import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class FontSizeChanger {
    final String TAG = this.getClass().getName();

    public void changeFontSize(TableLayout t, float fontSize) {
        for (int i = 0; i <= t.getChildCount() - 1; i++) {
            TableRow row = (TableRow) t.getChildAt(i);

            for (int j = 0; j <= row.getChildCount() - 1; j++) {
                View v = row.getChildAt(j);

                try {
                    if (v.getClass() == Class.forName("android.widget.TextView")) {
                        TextView tv = (TextView) v;
                        if (i % 2 == 0) {
                        } else {
                            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "^ changeFontSize: ");
                }
            }
        }
    }
}
