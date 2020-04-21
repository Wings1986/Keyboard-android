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

import java.util.Timer;
import java.util.TimerTask;

import com.hbconsulting.communiji.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;


/**
 * Displays the IME preferences inside the input method setting.
 */
public class SplashActivity extends Activity {

	Timer mTimer = null;
	
	final int REQUEST_OVERLAY_PERMISSION = 100;
	
	
	@SuppressLint("NewApi") @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.activity_splash);
        
        getActionBar().hide();
        
		mTimer = new Timer();
		mTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				gotoImeActivity();
			}
		}, 3000);

    }
	

	@Override
	protected void onPause() {
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
		
		super.onPause();
	}


	private void gotoImeActivity() {
		startActivity(new Intent(SplashActivity.this, ImeActivity.class));
		finish();
	}
	

}
