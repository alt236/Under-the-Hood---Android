package aws.apps.underthehood.ui;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import aws.apps.underthehood.R;
import aws.apps.underthehood.util.UsefulBits;

public class GuiCreation {
	private final Context mContext;
	private final UsefulBits uB;
	private final LayoutInflater inflater;
	private View.OnClickListener textViewCopyClickListener;
	
	public GuiCreation(Context context) {
		super();
		this.mContext = context;
		uB = new UsefulBits(mContext);
		inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		textViewCopyClickListener = new View.OnClickListener() {
			@SuppressWarnings("deprecation")
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
					String message = "'" + msgtext + "' " + mContext.getString(R.string.text_copied);
					uB.showToast(message, Toast.LENGTH_SHORT, Gravity.TOP,0,0);

					android.text.ClipboardManager ClipMan = (android.text.ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
					ClipMan.setText(text);
				}
			}
		};
	}
	
	public TableRow createDataRow(String text){
		TableRow tr = (TableRow) inflater.inflate(R.layout.table_row_data, null);

		TextView text1 = (TextView) tr.findViewById(R.id.text);

		text1.setClickable(true);
		text1.setOnClickListener(textViewCopyClickListener);
		text1.setText(text);
		
		return tr;
	}
	
	public TableRow createSeperatorRow(String text){
		TableRow tr = (TableRow) inflater.inflate(R.layout.table_row_section, null);

		TextView text1 = (TextView) tr.findViewById(R.id.text);
		text1.setText(text);
		
		return tr;
	}

	public TableRow createTitleRow(String text){
		TableRow tr = (TableRow) inflater.inflate(R.layout.table_row_title, null);

		TextView text1 = (TextView) tr.findViewById(R.id.text);
		text1.setText(text);
		
		return tr;
	}
	
	public View getScrollableTable(){
		return inflater.inflate(R.layout.page_scrollable_table, null);
	}
	
	public View getScrollableTextView(){
		return inflater.inflate(R.layout.page_scrollable_textview, null);
	}
}
