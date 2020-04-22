/*
 * Copyright (C) 2008-2009 The Android Open Source Project
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
package com.hbconsulting.communiji;

import android.annotation.SuppressLint;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.InputMethodService;


import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.util.Log;

import android.view.KeyEvent;

import android.view.View;


import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputBinding;
import android.view.inputmethod.InputConnection;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;
import androidx.core.content.FileProvider;
import androidx.core.view.inputmethod.EditorInfoCompat;
import androidx.core.view.inputmethod.InputConnectionCompat;
import androidx.core.view.inputmethod.InputContentInfoCompat;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.hbconsulting.emojicon.EmojiconGridView.OnEmojiconClickedListener;
import com.hbconsulting.emojicon.EmojiconsPopup;
import com.hbconsulting.emojicon.EmojiconsPopup.OnEmojiconBackspaceClickedListener;

import com.hbconsulting.emojicon.emoji.Emojicon;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;


/**
 * Example of writing an input method for a soft keyboard. This code is focused
 * on simplicity over completeness, so it should in no way be considered to be a
 * complete soft keyboard implementation. Its purpose is to provide a basic
 * example for how you would get started writing an input method, to be fleshed
 * out as appropriate.
 */
@SuppressLint({ "NewApi", "RtlHardcoded" })
public class MyKeyboardService extends InputMethodService {

	private static final String TAG = "MyKeyboardService";

	private static final String AUTHORITY = "com.hbconsulting.communiji.inputcontent";

	private AudioManager mAudioManager = null;
	private Vibrator mVibrator = null;


	File imagesDir;
	boolean imgSupported = false;

	private FirebaseAnalytics mFirebaseAnalytics;


	@Override
	public void onCreate() {
		super.onCreate();
		
		Log.e("MykeyboardServiece", "onCreate +++++++++++");

		mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

		imagesDir = new File(getFilesDir(), "images");
		imagesDir.mkdirs();

	}

	/**
	 */
	@Override
	public void onInitializeInterface() {

		Log.e("MykeyboardServiece", "onInitializeInterface +++++++++++");
		
	}

	
	/**
	 * Called by the framework when your view for creating input needs to be
	 * generated. This will be called the first time your input method is
	 * displayed, and every time it needs to be re-created such as due to a
	 * configuration change.
	 */
	
	@Override
	public View onCreateInputView() {
		
		Log.e("MyKeyboardService", "onCreateInputView ===============");


		EmojiconsPopup emoticonView = (EmojiconsPopup) getLayoutInflater().inflate(R.layout.emojicons, null);

		emoticonView.setView(1);

		emoticonView.setHeight(800);

		emoticonView.setOnEmojiconClickedListener(new OnEmojiconClickedListener() {

			@Override
			public void onEmojiconClicked(Emojicon emojicon) {
				sound();
//				String text = emojicon.getEmoji();
//				getCurrentInputConnection().commitText(text, 1);

//				commitGifImage(resIdToUri(getApplicationContext(), R.drawable.ic_launcher), "");

				File mPngFile = getFileForResource(getApplicationContext(), emojicon.getEmoji(), imagesDir, "image.png");
//				File mPngFile = getFileForRaw(this, R.raw.act7, imagesDir, "image.png");

				if (imgSupported)
					doCommitContent("A droid logo", "image/png", mPngFile);
				else
					doCopyContent("A droid logo", "image/png", mPngFile);

				Bundle bundle = new Bundle();
				bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "android-keyboard");
				bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "emoji");
				bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
				mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

			}
		});
		emoticonView.setOnEmojiconBackspaceClickedListener(new OnEmojiconBackspaceClickedListener() {

			@Override
			public void onEmojiconBackspaceClicked(View v) {
				sound();

				getCurrentInputConnection().sendKeyEvent(
						new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
				getCurrentInputConnection().sendKeyEvent(
						new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
			}
		});



		setInputView(emoticonView);

		return emoticonView;
	}
	

	/**
	 * This is the main point where we do our initialization of the input method
	 * to begin operating on an application. At this point we have been bound to
	 * the client, and are now receiving all of the detailed information about
	 * the target of our edits.
	 */
	@Override
	public void onStartInput(EditorInfo attribute, boolean restarting) {
		super.onStartInput(attribute, restarting);
		
		Log.e("MyKeyboardService", "onStartInput =========");

	}


	
	@Override
	public void onStartInputView(EditorInfo info, boolean restarting) {
		super.onStartInputView(info, restarting);
		
		
		Log.e("MyKeyboardService", "onStartInputView =========" + restarting);

		String[] mimeTypes = EditorInfoCompat.getContentMimeTypes(info);



		for (String mimeType : mimeTypes) {
			if (ClipDescription.compareMimeTypes(mimeType, "image/png")) {
				imgSupported = true;
			}
		}

		if (imgSupported) {
			// the target editor supports GIFs. enable corresponding content
			Log.e("MyKeyboardService", "support PNG ========================");
		} else {
			// the target editor does not support GIFs. disable corresponding content
			Log.e("MyKeyboardService", "NOT support PNG ========================");
		}
	}


	/**
	 * This is called when the user is done editing a field. We can use this to
	 * reset our state.
	 */

	@Override
	public void onFinishInput() {
		super.onFinishInput();
		
		Log.e("MyKeyboardService", "onFinishInput =====================" );
		

	}
	

	@Override
	public void onFinishInputView(boolean finishInput) {
		
		Log.e("MyKeyboardService", "onFinishInputView =====================");
		

		super.onFinishInputView(finishInput);
	}


  ////////////////
  	public static final String ANDROID_RESOURCE = "android.resource://";
	public static final String FORESLASH = "/";

	public static Uri resIdToUri(Context context, int resId) {
//		return Uri.parse(ANDROID_RESOURCE + context.getPackageName()
//				+ FORESLASH + resId);

//		String resourceScheme = "res";
//		Uri uri = new Uri.Builder()
//				.scheme(resourceScheme)
//				.path(String.valueOf(resId))
//				.build();
//		return uri;

		return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
				context.getResources().getResourcePackageName(resId) + '/' +
				context.getResources().getResourceTypeName(resId) + '/' +
				context.getResources().getResourceEntryName(resId) );


	}
	public void commitGifImage(Uri contentUri, String imageDescription) {
		InputContentInfoCompat inputContentInfo = new InputContentInfoCompat(
				contentUri,
				new ClipDescription(imageDescription, new String[]{"image/png"}),
				null
		);
		InputConnection inputConnection = getCurrentInputConnection();
		EditorInfo editorInfo = getCurrentInputEditorInfo();
		int flags = 0;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
			flags |= InputConnectionCompat.INPUT_CONTENT_GRANT_READ_URI_PERMISSION;
		}
		InputConnectionCompat.commitContent(
				inputConnection, editorInfo, inputContentInfo, flags, null);
	}

	private void doCopyContent(@NonNull String description, @NonNull String mimeType,
								 @NonNull File file) {
		final EditorInfo editorInfo = getCurrentInputEditorInfo();

		// Validate packageName again just in case.
		if (!validatePackageName(editorInfo)) {
			return;
		}

		final Uri contentUri = FileProvider.getUriForFile(this, AUTHORITY, file);

		final int flag;
		if (Build.VERSION.SDK_INT >= 25) {
			// On API 25 and later devices, as an analogy of Intent.FLAG_GRANT_READ_URI_PERMISSION,
			// you can specify InputConnectionCompat.INPUT_CONTENT_GRANT_READ_URI_PERMISSION to give
			// a temporary read access to the recipient application without exporting your content
			// provider.
			flag = InputConnectionCompat.INPUT_CONTENT_GRANT_READ_URI_PERMISSION;
		} else {
			// On API 24 and prior devices, we cannot rely on
			// InputConnectionCompat.INPUT_CONTENT_GRANT_READ_URI_PERMISSION. You as an IME author
			// need to decide what access control is needed (or not needed) for content URIs that
			// you are going to expose. This sample uses Context.grantUriPermission(), but you can
			// implement your own mechanism that satisfies your own requirements.
			flag = 0;
			try {
				// TODO: Use revokeUriPermission to revoke as needed.
				grantUriPermission(
						editorInfo.packageName, contentUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
			} catch (Exception e){
				Log.e(TAG, "grantUriPermission failed packageName=" + editorInfo.packageName
						+ " contentUri=" + contentUri, e);
			}
		}

//		ContentValues values = new ContentValues(2);
//		values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
//		values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
//		ContentResolver theContent = getContentResolver();
//		Uri  imageUri = theContent.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);


//		ClipData theClip = ClipData.newUri(getContentResolver(),description, contentUri);
//		ClipboardManager clipboard = (android.content.ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
//		clipboard.setPrimaryClip(theClip);

/*
		ClipboardManager mClipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		ContentValues values = new ContentValues(2);
		values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
		values.put(MediaStore.Images.Media.DATA,  "file://"+file.getAbsoluteFile());

		ContentResolver theContent = getContentResolver();
		Uri imageUri = theContent.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
		ClipData theClip = ClipData.newUri(getContentResolver(), "Image", imageUri);
		mClipboard.setPrimaryClip(theClip);

		Toast.makeText(this, "Copied to clipboard successfully", Toast.LENGTH_SHORT).show();

 */
		//getPackageRunningApp();
		//printForegroundTask();

		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("image/*");
		intent.putExtra(Intent.EXTRA_STREAM, contentUri);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION|Intent.FLAG_GRANT_READ_URI_PERMISSION);

		Intent chooserIntent = Intent.createChooser(intent, "Open With");
		chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		chooserIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION|Intent.FLAG_GRANT_READ_URI_PERMISSION);
		startActivity(chooserIntent);

	}
	private void getPackageRunningApp() {
		ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
		for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {

			Log.e("iGold", "processInfo.processName = " + processInfo.processName);

			if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				for (String activeProcess : processInfo.pkgList) {

					Log.e("iGold", activeProcess);
//					if (activeProcess.equals(context.getPackageName())) {
//						//If your app is the process in foreground, then it's not in running in background
//						return false;
//					}
				}
			}
		}

	}

	private String printForegroundTask() {
		String currentApp = "NULL";
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			UsageStatsManager usm = (UsageStatsManager)this.getSystemService("usagestats");
			long time = System.currentTimeMillis();
			List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,  time - 1000*1000, time);
			if (appList != null && appList.size() > 0) {
				SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
				for (UsageStats usageStats : appList) {
					mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
				}
				if (mySortedMap != null && !mySortedMap.isEmpty()) {
					currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
				}
			}
		} else {
			ActivityManager am = (ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
			List<ActivityManager.RunningAppProcessInfo> tasks = am.getRunningAppProcesses();
			currentApp = tasks.get(0).processName;
		}

		Log.e("adapter", "Current App in foreground is: " + currentApp);
		return currentApp;
	}

	private void doCommitContent(@NonNull String description, @NonNull String mimeType,
								 @NonNull File file) {
		final EditorInfo editorInfo = getCurrentInputEditorInfo();

		// Validate packageName again just in case.
		if (!validatePackageName(editorInfo)) {
			return;
		}

		final Uri contentUri = FileProvider.getUriForFile(this, AUTHORITY, file);

		// As you as an IME author are most likely to have to implement your own content provider
		// to support CommitContent API, it is important to have a clear spec about what
		// applications are going to be allowed to access the content that your are going to share.
		final int flag;
		if (Build.VERSION.SDK_INT >= 25) {
			// On API 25 and later devices, as an analogy of Intent.FLAG_GRANT_READ_URI_PERMISSION,
			// you can specify InputConnectionCompat.INPUT_CONTENT_GRANT_READ_URI_PERMISSION to give
			// a temporary read access to the recipient application without exporting your content
			// provider.
			flag = InputConnectionCompat.INPUT_CONTENT_GRANT_READ_URI_PERMISSION;
		} else {
			// On API 24 and prior devices, we cannot rely on
			// InputConnectionCompat.INPUT_CONTENT_GRANT_READ_URI_PERMISSION. You as an IME author
			// need to decide what access control is needed (or not needed) for content URIs that
			// you are going to expose. This sample uses Context.grantUriPermission(), but you can
			// implement your own mechanism that satisfies your own requirements.
			flag = 0;
			try {
				// TODO: Use revokeUriPermission to revoke as needed.
				grantUriPermission(
						editorInfo.packageName, contentUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
			} catch (Exception e){
				Log.e(TAG, "grantUriPermission failed packageName=" + editorInfo.packageName
						+ " contentUri=" + contentUri, e);
			}
		}

		final InputContentInfoCompat inputContentInfoCompat = new InputContentInfoCompat(
				contentUri,
				new ClipDescription(description, new String[]{mimeType}),
				null /* linkUrl */);
		InputConnectionCompat.commitContent(
				getCurrentInputConnection(), getCurrentInputEditorInfo(), inputContentInfoCompat,
				flag, null);
	}

	private boolean validatePackageName(@Nullable EditorInfo editorInfo) {
		if (editorInfo == null) {
			return false;
		}
		final String packageName = editorInfo.packageName;
		if (packageName == null) {
			return false;
		}

		// In Android L MR-1 and prior devices, EditorInfo.packageName is not a reliable identifier
		// of the target application because:
		//   1. the system does not verify it [1]
		//   2. InputMethodManager.startInputInner() had filled EditorInfo.packageName with
		//      view.getContext().getPackageName() [2]
		// [1]: https://android.googlesource.com/platform/frameworks/base/+/a0f3ad1b5aabe04d9eb1df8bad34124b826ab641
		// [2]: https://android.googlesource.com/platform/frameworks/base/+/02df328f0cd12f2af87ca96ecf5819c8a3470dc8
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			return true;
		}

		final InputBinding inputBinding = getCurrentInputBinding();
		if (inputBinding == null) {
			// Due to b.android.com/225029, it is possible that getCurrentInputBinding() returns
			// null even after onStartInputView() is called.
			// TODO: Come up with a way to work around this bug....
			Log.e(TAG, "inputBinding should not be null here. "
					+ "You are likely to be hitting b.android.com/225029");
			return false;
		}
		final int packageUid = inputBinding.getUid();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			final AppOpsManager appOpsManager =
					(AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
			try {
				appOpsManager.checkPackage(packageUid, packageName);
			} catch (Exception e) {
				return false;
			}
			return true;
		}

		final PackageManager packageManager = getPackageManager();
		final String possiblePackageNames[] = packageManager.getPackagesForUid(packageUid);
		for (final String possiblePackageName : possiblePackageNames) {
			if (packageName.equals(possiblePackageName)) {
				return true;
			}
		}
		return false;
	}

	private static File getFileForResource(
			@NonNull Context context, @NonNull int res, @NonNull File outputDir,
			@NonNull String filename) {

		Drawable drawable = context.getDrawable(res);

		// Get the bitmap from drawable object
		Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();


		final File outputFile = new File(outputDir, filename);

		try {
			try {

				OutputStream dataWriter = null;
				try {
					dataWriter = new FileOutputStream(outputFile);

					bitmap.compress(Bitmap.CompressFormat.PNG,100, dataWriter);
					return outputFile;
				} finally {
					if (dataWriter != null) {
						dataWriter.flush();
						dataWriter.close();
					}
				}
			} finally {

			}
		} catch (IOException e) {
			return null;
		}
	}
	private static File getFileForRaw(
			@NonNull Context context, @RawRes int res, @NonNull File outputDir,
			@NonNull String filename) {

		final File outputFile = new File(outputDir, filename);
		final byte[] buffer = new byte[4096];
		InputStream resourceReader = null;
		try {
			try {
				resourceReader = context.getResources().openRawResource(res);
				OutputStream dataWriter = null;
				try {
					dataWriter = new FileOutputStream(outputFile);
					while (true) {
						final int numRead = resourceReader.read(buffer);
						if (numRead <= 0) {
							break;
						}
						dataWriter.write(buffer, 0, numRead);
					}
					return outputFile;
				} finally {
					if (dataWriter != null) {
						dataWriter.flush();
						dataWriter.close();
					}
				}
			} finally {
				if (resourceReader != null) {
					resourceReader.close();
				}
			}
		} catch (IOException e) {
			return null;
		}
	}

	// ---------------Start Common Functions------------------------------//

	private void sound() {

		if (this.mAudioManager == null)
			this.mAudioManager = ((AudioManager) getSystemService("audio"));

		{
			int paramInt = 0;
			switch (paramInt) {
			default:
				paramInt = 5;
				break;
//			case -5:
//				paramInt = 0;
//				break;
//			case 10:
//				paramInt = 8;
//				break;
//			case 32:
//				paramInt = 6;
//				break;
			}
			this.mAudioManager.playSoundEffect(paramInt, 1.0F);
			//this.mAudioManager.playSoundEffect(SoundEffectConstants.CLICK);
		}
	}


	@SuppressLint("WrongConstant")
	private void vibrate() {

		if (this.mVibrator == null)
			this.mVibrator = ((Vibrator) getSystemService("vibrator"));
		this.mVibrator.vibrate(500);
	}


}
