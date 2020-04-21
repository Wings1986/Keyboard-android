/*
 * Copyright 2019 by iGold
 *
 */

package com.hbconsulting.emojicon;


import java.util.List;

import com.hbconsulting.emojicon.EmojiconGridView.OnEmojiconClickedListener;
import com.hbconsulting.emojicon.emoji.Emojicon;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import github.ankushsachdeva.emojicon.R;

/**
 * @author Ankush Sachdeva (sankush@yahoo.co.in)
 */
class EmojiAdapter extends ArrayAdapter<Emojicon> {
	OnEmojiconClickedListener emojiClickListener;
    public EmojiAdapter(Context context, List<Emojicon> data) {
        super(context, R.layout.emojicon_item, data);
    }

    public EmojiAdapter(Context context, Emojicon[] data) {
        super(context, R.layout.emojicon_item, data);
    }
    
    public void setEmojiClickListener(OnEmojiconClickedListener listener){
    	this.emojiClickListener = listener;
    }
    
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = View.inflate(getContext(), R.layout.emojicon_item, null);
            ViewHolder holder = new ViewHolder();
            holder.icon =  v.findViewById(R.id.emojicon_icon);
            v.setTag(holder);
        }
        Emojicon emoji = getItem(position);
        ViewHolder holder = (ViewHolder) v.getTag();
        holder.icon.setImageResource(emoji.getEmoji());
        holder.icon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				emojiClickListener.onEmojiconClicked(getItem(position));
			}
		});
        return v;
    }

    class ViewHolder {
        ImageView icon;
    }
}