/*
 * Copyright 2019 by iGold
 *
 */

package com.hbconsulting.emojicon.emoji;

import java.io.Serializable;

/**
 * @author Hieu Rocker (rockerhieu@gmail.com)
 */
public class Emojicon implements Serializable {
    private static final long serialVersionUID = 1L;
    private int emoji;

    private Emojicon() {
    }

    public static Emojicon fromCodePoint(int codePoint) {
        Emojicon emoji = new Emojicon();
        emoji.emoji = codePoint;
        return emoji;
    }


    public Emojicon(String emoji) {

        this.emoji = Integer.parseInt(emoji);
    }

    public int getEmoji() {
        return emoji;
    }


}
