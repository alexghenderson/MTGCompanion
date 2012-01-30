package net.ilcid.apps.magiccompanion;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

class PlayerAdapter extends ArrayAdapter<Player>{
	private List<Player> items;
	
	public PlayerAdapter(Context context, int textViewResourceId,
			List<Player> items) {
		super(context, textViewResourceId, items);
		this.items = items;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if(v == null) {
			LayoutInflater vi = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.player_row, null);
		}
		final Player p = items.get(position);
		if(p != null) {
			TextView hpValueTextView = (TextView)v.findViewById(R.id.hp_value);
			TextView poisonValueTextView = (TextView)v.findViewById(R.id.poison_value);
			TextView nameTextView = (TextView)v.findViewById(R.id.player_name);
			if(hpValueTextView != null) {
				hpValueTextView.setText(String.valueOf(p.getHealth()));
			}
			if(poisonValueTextView != null) {
				poisonValueTextView.setText(String.valueOf(p.getPoisonCounters()));
			}
			if(nameTextView != null) {
				nameTextView.setText(p.getName());
			}
			final PlayerAdapter pa = this;
			Button garbageCanView = (Button)v.findViewById(R.id.garbage_icon);
			if(garbageCanView != null) {
				garbageCanView.setOnClickListener(new View.OnClickListener() {
					
					public void onClick(View v) {
						v.clearAnimation();
						pa.remove(p);
						
					}
				});
			}
			v.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					View garbage = v.findViewById(R.id.garbage_icon);
					if(garbage != null) {
						garbage.setVisibility(View.VISIBLE);
						garbage.startAnimation(AnimationUtils.loadAnimation(garbage.getContext(), R.animator.slow_fadeout));
						garbage.setVisibility(View.INVISIBLE);
						}
				}
			});
		}
		return v;
	}

}

