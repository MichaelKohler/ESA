package ch.ffhs.esa.bewegungsmelder.models;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ch.ffhs.esa.bewegungsmelder.R;
import ch.ffhs.esa.bewegungsmelder.models.Kontakt;


/**
 * AdapterKlasse f�r Kontaktliste 
  * @author Mario Aloise
 */


public class KontaktAdapter extends ArrayAdapter<Kontakt> {

	private Activity activity;
	private List<Kontakt> items;
	private int row;
	private Kontakt objBean;


	/**
	 * Konstruktor f�r KontaktAdapter Objekt 
	  * @author Mario Aloise
	 */
	
	public KontaktAdapter(Activity act, int row, List<Kontakt> items) {
		super(act, row, items);

		this.activity = act;
		this.row = row;
		this.items = items;

	}

	/**
	 * Diese Methode gibt den View mit viewholder f�r Kontaktname und Kontaktnummer zur�ck 
	  * @author Mario Aloise
	 */
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolder holder;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(row, null);

			holder = new ViewHolder();
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		if ((items == null) || ((position + 1) > items.size()))
			return view;

		objBean = items.get(position);

		holder.tvname = (TextView) view.findViewById(R.id.tvname);
		holder.tvPhoneNo = (TextView) view.findViewById(R.id.tvphone);

		 if (holder.tvname != null && null != objBean.getName()
				&& objBean.getName().trim().length() > 0) {
			holder.tvname.setText((objBean.getName()));
		}
		if (holder.tvPhoneNo != null && null != objBean.getPhoneNo()
				&& objBean.getPhoneNo().trim().length() > 0) {
			holder.tvPhoneNo.setText((objBean.getPhoneNo()));
		} 
		
		return view;
	}

	public class ViewHolder {
		public TextView tvname, tvPhoneNo;
	}

}
