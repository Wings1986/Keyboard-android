/*
 * Copyright 2019 by iGold
 *
 */

package com.hbconsulting.emojicon;

import com.hbconsulting.emojicon.emoji.Emojicon;

import android.content.Context;

/**
* @author Daniele Ricci
*/
public interface EmojiconRecents {
    public void addRecentEmoji(Context context, Emojicon emojicon);
}
