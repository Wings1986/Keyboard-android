/*
 * Copyright 2019 by iGold
 *
 */

package com.hbconsulting.emojicon;


import java.util.Arrays;

import com.hbconsulting.emojicon.emoji.Category1;
import com.hbconsulting.emojicon.emoji.Emojicon;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;

import github.ankushsachdeva.emojicon.R;

/**
 * @author Hieu Rocker (rockerhieu@gmail.com)
 * @author 	Ankush Sachdeva (sankush@yahoo.co.in)
 */
public class EmojiconGridView{
	public View rootView;
	EmojiconsPopup mEmojiconPopup;
    EmojiconRecents mRecents;
    Emojicon[] mData;
    
    public EmojiconGridView(Context context, Emojicon[] emojicons, EmojiconRecents recents, EmojiconsPopup emojiconPopup) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		mEmojiconPopup = emojiconPopup;
		rootView = inflater.inflate(R.layout.emojicon_grid, null);
		setRecents(recents);
		 GridView gridView = (GridView) rootView.findViewById(R.id.Emoji_GridView);
	        if (emojicons== null) {
	            mData = Category1.DATA;
	        } else {
	            Object[] o = (Object[]) emojicons;
	            mData = Arrays.asList(o).toArray(new Emojicon[o.length]);
	        }
	        EmojiAdapter mAdapter = new EmojiAdapter(rootView.getContext(), mData);
	        mAdapter.setEmojiClickListener(new OnEmojiconClickedListener() {
				
				@Override
				public void onEmojiconClicked(Emojicon emojicon) {
					
					if (mEmojiconPopup.onEmojiconClickedListener != null) {
			            mEmojiconPopup.onEmojiconClickedListener.onEmojiconClicked(emojicon);
			        }
			        if (mRecents != null) {
			            mRecents.addRecentEmoji(rootView.getContext(), emojicon);
			        }
				}
			});
	        gridView.setAdapter(mAdapter);
	}
    
	private void setRecents(EmojiconRecents recents) {
        mRecents = recents;
    }

    public interface OnEmojiconClickedListener {
        void onEmojiconClicked(Emojicon emojicon);
    }
    
}
