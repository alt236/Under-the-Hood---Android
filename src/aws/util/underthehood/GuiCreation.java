package aws.util.underthehood;

import android.content.Context;
import android.graphics.Typeface;
import android.text.ClipboardManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import aws.apps.underthehood.R;

public class GuiCreation {

	private int tableIdOffset = 1;
	private Context c;
	private UsefulBits uB;
	
	public GuiCreation(Context c) {
		super();
		this.c = c;
		uB = new UsefulBits(c);
	}
	
	public void resetOffset(){
		tableIdOffset = 1;
	}
	
	public TableRow createSeperatorRow(String text){
		return createRow(text,
				c.getResources().getInteger(R.integer.terminal_title_font),
				c.getResources().getColor(R.color.black),
				c.getResources().getColor(R.color.orange));
	}
	
	public TableRow createTitleRow(String text){
		return createRow(text,
				c.getResources().getInteger(R.integer.terminal_title_font),
				c.getResources().getColor(R.color.black),
				c.getResources().getColor(R.color.white));
	}

	public TableRow createDataRow(String text, int dataTextSize){
		return createRow(text,
				dataTextSize,
				c.getResources().getColor(R.color.white),
				c.getResources().getColor(R.color.black));
	}

	private TableRow createRow(String text, int TextSize, int TextColor, int BgColor) {
		TableRow tr = new TableRow(c);
		tr.setId(100 + tableIdOffset);
		tr.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		tr.setMinimumHeight(35);
		tr.addView(createTextView(200 + tableIdOffset, text, TextSize, TextColor, BgColor));
		tr.setBackgroundColor(BgColor);

		tableIdOffset += 1;
		return tr;
	}

	private TextView createTextView(int id, String text, int TextSize , int TextColor, int BgColor) {
		TextView tv = new TextView(c);
		tv.setId(id);

		tv.setText(text);
		tv.setTextColor(TextColor);
		tv.setBackgroundColor(BgColor);
		tv.setTextSize(TextSize);
		tv.setMinHeight(20);
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
	
}
