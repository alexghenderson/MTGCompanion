package net.ilcid.apps.magiccompanion;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

public class CardAdapter  extends ArrayAdapter<Card> {
	private List<Card> items;
	
	public CardAdapter(Context context, int textViewResourceId,
			List<Card> items) {
		super(context, textViewResourceId, items);
		this.items = items;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if(v == null) {
			LayoutInflater vi = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.card_row, null);
		}
		final Card c = items.get(position);
		if(c != null) {
			final TextView nameView = (TextView)v.findViewById(R.id.card_name);
			final TextView typeView = (TextView)v.findViewById(R.id.card_type);
			final TextView setView = (TextView)v.findViewById(R.id.card_set);
			typeView.setText(c.getType());
			nameView.setText(c.getName());
			setView.setText(c.getSet());
			
			v.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					Intent i = new Intent(getContext(), CardDetailsActivity.class);
					i.putExtra("card_name", c.getName());
					getContext().startActivity(i);
					
				}
			});
		}
		return v;
	}

}
