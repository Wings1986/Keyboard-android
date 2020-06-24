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
    private String category = "";

    private Emojicon() {
    }

    public static Emojicon fromCodePoint(int codePoint, String category) {
        Emojicon emoji = new Emojicon();
        emoji.emoji = codePoint;
        emoji.category = category;
        return emoji;
    }


    public Emojicon(String emoji, String category) {

        this.emoji = Integer.parseInt(emoji);
        this.category = category;
    }

    public int getEmoji() {
        return emoji;
    }
    public String getCategory() {return  category; };

}
