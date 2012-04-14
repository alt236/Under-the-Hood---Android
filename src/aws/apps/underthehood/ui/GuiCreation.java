package aws.apps.underthehood.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.text.ClipboardManager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import aws.apps.underthehood.R;
import aws.apps.underthehood.util.UsefulBits;

public class GuiCreation {

	private int tableIdOffset = 1;
	private Context c;
	private UsefulBits uB;
	
	public GuiCreation(Context c) {
		super();
		this.c = c;
		uB = new UsefulBits(c);
	}
	
	public TableRow createDataRow(String text, float dataTextSize){
		return createRow(text,
				dataTextSize,
				c.getResources().getColor(R.color.white),
				c.getResources().getColor(R.color.black));
	}
	
	private TableRow createRow(String text, float TextSize, int TextColor, int BgColor) {
		TableRow tr = new TableRow(c);
		tr.setId(100 + tableIdOffset);
		tr.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		tr.setMinimumHeight(UsefulBits.dipToPixels(20, c));
		tr.addView(createTextView(200 + tableIdOffset, text, TextSize, TextColor, BgColor));
		tr.setBackgroundColor(BgColor);

		tableIdOffset += 1;
		return tr;
	}
	
	public TableRow createSeperatorRow(String text){
		return createRow(text,
				c.getResources().getDimension(R.dimen.terminal_title_font),
				c.getResources().getColor(R.color.black),
				c.getResources().getColor(R.color.orange));
	}

	private TextView createTextView(int id, String text, float TextSize , int TextColor, int BgColor) {
		TextView tv = new TextView(c);
		tv.setId(id);

		tv.setText(text);
		tv.setTextColor(TextColor);
		tv.setBackgroundColor(BgColor);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, TextSize);
		tv.setMinimumHeight(UsefulBits.dipToPixels(20, c));
		tv.setTypeface(Typeface.MONOSPACE);

		tv.setTag(text);
		tv.setClickable(true);
		tv.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				TextView t = (TextView) v;
				String text = t.getText() + "";

				if (text.length() > 0) {
					String msgtext = "";
					if (text.length()>150) {
						msgtext = text.substring(0, 150) + "...";
					} else {
						msgtext = text;
					}
					String message = "'" + msgtext + "' " + c.getString(R.string.text_copied);
					uB.showToast(message, Toast.LENGTH_SHORT, Gravity.TOP,0,0);

					ClipboardManager ClipMan = (ClipboardManager) c.getSystemService(Context.CLIPBOARD_SERVICE);
					ClipMan.setText(text);
				}
			}
		});

		return tv;
	}

	public TableRow createTitleRow(String text){
		return createRow(text,
				c.getResources().getDimension(R.dimen.terminal_title_font),
				c.getResources().getColor(R.color.black),
				c.getResources().getColor(R.color.white));
	}

	public void resetOffset(){
		tableIdOffset = 1;
	}
	
}
