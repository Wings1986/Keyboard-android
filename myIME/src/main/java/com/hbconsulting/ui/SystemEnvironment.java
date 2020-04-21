package com.hbconsulting.ui;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;

public class SystemEnvironment {

	static public boolean isEnabledInputMethod(Context context) {
		
		String packagename = context.getApplicationInfo().packageName;
		
		InputMethodManager imManager = (InputMethodManager)context.getSystemService("input_method");

		 List<InputMethodInfo> infoList = ((InputMethodManager)imManager).getEnabledInputMethodList();
		 
		    
		 for (int i = 0 ; i < infoList.size() ; i ++) {
			 InputMethodInfo info = (InputMethodInfo)infoList.get(i);
	    	 if (info != null && info.getPackageName().equals(packagename)) {
	    		 return true;
	    	 }
		    	 
		 }
		 
		 return false;
	}

	static public void setInputMethod(Context context) {
//		InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
//		imm.setInputMethod(null, "");
		
		Intent enableIntent = new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS);
        enableIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(enableIntent);
		 
	}
	
	static public void showInputMethodPicker(Context context) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showInputMethodPicker();
	}

	static public boolean isDefaultInputMethod(Context context) {
		String packagename = context.getApplicationInfo().packageName;
		
		String str = Settings.Secure.getString(context.getContentResolver(), "default_input_method");
		
		if (str.startsWith(packagename)) {
			return true;
		}
		
		return false;
	}
}
