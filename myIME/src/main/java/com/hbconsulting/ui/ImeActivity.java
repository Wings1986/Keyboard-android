/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hbconsulting.ui;

import com.hbconsulting.communiji.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.widget.Button;


/**
 * Displays the IME preferences inside the input method setting.
 */
public class ImeActivity extends Activity {

	Button btnSwitch;

	int mode = 0;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        
        getActionBar().hide();

        Button btnFacebook = findViewById(R.id.btn_fb);
        btnFacebook.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/Communiji-101834561512764"));
				startActivity(browserIntent);
			}
		});

		Button btnInstagram = findViewById(R.id.btn_in);
		btnInstagram.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://instagram.com/communiji?igshid=1h7ahm2r1iqd"));
				startActivity(browserIntent);
			}
		});

		Button btnLinked = findViewById(R.id.btn_linked);
		btnLinked.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/company/communiji"));
				startActivity(browserIntent);
			}
		});

        btnSwitch = (Button) findViewById(R.id.btn_switch);
        btnSwitch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				if (mode == 0) {
					SystemEnvironment.setInputMethod(ImeActivity.this);
				} else if (mode == 1) {
					SystemEnvironment.showInputMethodPicker(ImeActivity.this);
				} else {

				}
				
			}
		});
       
        
        final Animation anim1 = new ScaleAnimation(1, 1.03f, 1, 1.03f, Animation.RELATIVE_TO_SELF, 0.5f, 
				Animation.RELATIVE_TO_SELF, 0.5f);
        anim1.setDuration(1500);
        
        final Animation anim2 = new ScaleAnimation(1.03f, 1, 1.03f, 1, Animation.RELATIVE_TO_SELF, 0.5f, 
				Animation.RELATIVE_TO_SELF, 0.5f);
        anim2.setDuration(1500);
        
        anim1.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				btnSwitch.startAnimation(anim2);
			}
		});
        
        anim2.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				btnSwitch.startAnimation(anim1);
			}
		});
        
		btnSwitch.startAnimation(anim1);
		
    }

	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub		
		
		if (hasFocus == true && btnSwitch != null) {
			
			boolean enableInput = SystemEnvironment.isEnabledInputMethod(this);
			boolean defaultInput = SystemEnvironment.isDefaultInputMethod(this);
			
			if (enableInput && defaultInput) {
				mode = 2;
				

			}
			else if (enableInput) {
				mode = 1;
			} else {
				mode = 0;
			}
			
			String text = "";
			int resImg = 0;
			if (mode == 0) {
				text = getString(R.string.main_choose_keyboard);
			} else if (mode == 1) {
				text = getString(R.string.main_switch_keyboard);
			} else {
				text = getString(R.string.main_go_setting_keyboard);
			}
			btnSwitch.setText(text);

		}
		
		
		super.onWindowFocusChanged(hasFocus);
		
	}


}
