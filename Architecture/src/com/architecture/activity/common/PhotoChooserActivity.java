package com.architecture.activity.common;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.architecture.activity.base.BaseActivity;
import com.architecture.common.data.Account;
import com.architecture.context.IntentCode;
import com.architecture.manager.WindowManager;
import com.architecture.utils.DialogUtils;
import com.architecture.utils.FileUtils;
import com.architecture.utils.ImageUtils;
import com.architecture.R;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

public class PhotoChooserActivity extends BaseActivity implements OnClickListener {
	
	public static final String INTENT_REPLACEMODE = "replacemode";
	public static final String INTENT_CROP = "crop";
	public static final String INTENT_CROPWIDTH = "cropwidth";
	public static final String INTENT_CROPHEIGHT = "cropheight";
	public static final String INTENT_ASPECTX = "aspectx";
	public static final String INTENT_ASPECTY = "aspecty";
	public static final String INTENT_COMPRESS = "compress";
	public static final String INTENT_COMPRESSWIDTH = "compresswidth";
	public static final String INTENT_COMPRESSHEIGHT = "compressheight";
	public static final String INTENT_COMPRESSFILESIZE = "compressfilesize";

	protected static final String INTENT_URICROP = "uricrop";
	protected static final String INTENT_URICAMERA = "uricamera";
	
	public static final int RESULTCODE_OK = 1;
	public static final int RESULTCODE_DEL = 2;
	public static final int RESULTCODE_CANCEL = 3;
	
	protected static final int REQUESTCODE_PICKPHOTO_CAMERA = 1;
	protected static final int REQUESTCODE_PICKPHOTO_ALBUM = 2;
	protected static final int REQUESTCODE_CROPPHOTO = 3;
	protected static final int HANLDER_WHAT_COMPRESFINISH = 1;
	
	protected boolean replaceMode;
	protected boolean crop;
	protected int cropWidth = 200;
	protected int cropHeight = 200;
	protected int aspectX = 1;
	protected int aspectY = 1;
	protected boolean compress;
	protected int compressWidth;
	protected int compressHeight;
	protected int compressFileSize = 100 * 1024;
	
	protected Uri uriCrop;
	protected Uri uriCamera;
	
	protected LinearLayout llContent;
	protected Button btnTake;
	protected Button btnPick;
	protected Button btnCancel;
	
	protected Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HANLDER_WHAT_COMPRESFINISH:
				showLoading(false);
				Uri uri = (Uri) msg.obj;
				onSetResultCompress(uri);
				break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getContentViewResId());
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	}
	
	protected int getContentViewResId() {
		return R.layout.activity_photochooser;
	}
	
	@Override
	protected void onQueryArguments(Intent intent) {
		super.onQueryArguments(intent);
		replaceMode = intent.getBooleanExtra(INTENT_REPLACEMODE, false);
		crop = intent.getBooleanExtra(INTENT_CROP, false);
		cropWidth = intent.getIntExtra(INTENT_CROPWIDTH, cropWidth);
		cropHeight = intent.getIntExtra(INTENT_CROPHEIGHT, cropHeight);
		aspectX = intent.getIntExtra(INTENT_ASPECTX, aspectX);
		aspectY = intent.getIntExtra(INTENT_ASPECTY, aspectY);
		compress = intent.getBooleanExtra(INTENT_COMPRESS, true);
		compressWidth = intent.getIntExtra(INTENT_COMPRESSWIDTH, (int) (WindowManager.get().getScreenWidth()));
		compressHeight = intent.getIntExtra(INTENT_COMPRESSHEIGHT, (int) (WindowManager.get().getScreenHeight()));
		compressFileSize = intent.getIntExtra(INTENT_COMPRESSFILESIZE, compressFileSize);
	}

	@Override
    protected void onApplyData() {
        super.onApplyData();
        if(replaceMode) {
            if(btnCancel != null) {
                btnCancel.setText(R.string.photochooser_delete);
            }
        }
	}

	@Override
	protected void onFindView() {
		super.onFindView();
		llContent = (LinearLayout) findViewById(android.R.id.home);
		btnTake = (Button) findViewById(android.R.id.button1);
		btnPick = (Button) findViewById(android.R.id.button2);
		btnCancel = (Button) findViewById(android.R.id.button3);
	}
	
	@Override
	protected void onBindListener() {
		super.onBindListener();
		if(btnTake != null) {
			btnTake.setOnClickListener(this);
		}
		if(btnPick != null) {
			btnPick.setOnClickListener(this);
		}
		if(btnCancel != null) {
			btnCancel.setOnClickListener(this);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case android.R.id.button1:
			onCameraClicked();
			break;
		case android.R.id.button2:
			onAlbumClicked();
			break;
		case android.R.id.button3:
			onCancelClicked();
			break;
		default:
			break;
		}
	}
	
	/**
	 * 点击相机按钮
	 */
	protected void onCameraClicked() {
		try {
			File cameraFile = new File(getCachePicFolderName(), getCameraPicFileName());
			uriCamera = Uri.fromFile(cameraFile);
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, uriCamera);
			startActivityForResult(intent, REQUESTCODE_PICKPHOTO_CAMERA);
		} catch (ActivityNotFoundException e) {
			DialogUtils.showToastMessage(R.string.activity_notfound);
		}
	}
	
	/**
	 * 点击相册按钮
	 */
	protected void onAlbumClicked() {
		try {
			Intent intent = new Intent(Intent.ACTION_PICK, null);
			intent.setDataAndType(MediaStore.Images.Media.
				EXTERNAL_CONTENT_URI, "image/*");
			startActivityForResult(intent, REQUESTCODE_PICKPHOTO_ALBUM);
		} catch (ActivityNotFoundException e) {
			DialogUtils.showToastMessage(R.string.activity_notfound);
		}
	}
	
	/**
	 * 点击取消（删除）按钮
	 */
	protected void onCancelClicked() {
		if(replaceMode) {
			onSetResultDelete();
		} else {
			onSetResultCancel();
		}
	}
	
	/**
	 * 默认图片缓存文件夹目录
	 * @return
	 */
	public static String CachePicFolder() {
		String folder = FileUtils.getCachePath() + 
				File.separator + "photo" + 
				File.separator;
		return folder;
	}
	
	/**
	 * 图片缓存文件夹
	 * @return
	 */
	protected String getCachePicFolderName() {
		String folder = CachePicFolder();
		File file = new File(folder);
		if(!file.exists())
			file.mkdirs();
		return folder;
	}
	
	/**
	 * 拍照缓存文件名称
	 * @return
	 */
	protected String getCameraPicFileName() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSSS");
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("picture_").append(Account.get().userName).append("_")
				.append(format.format(new Date())).append(".jpg");
		return sBuilder.toString();
	}
	
	/**
	 * 裁剪缓存文件名称
	 * @return
	 */
	protected String getCropPicFileName() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSSS");
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("crop_").append(Account.get().userName).append("_")
		.append(format.format(new Date())).append(".jpg");
		return sBuilder.toString();
	}
	
	/**
	 * 裁剪缓存文件名称
	 * @return
	 */
	protected String getCompressPicFileName() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSSS");
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("compress_").append(Account.get().userName).append("_")
		.append(format.format(new Date())).append(".jpg");
		return sBuilder.toString();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		onSetResultCancel();
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}
		if(requestCode == REQUESTCODE_PICKPHOTO_ALBUM ||
			requestCode == REQUESTCODE_PICKPHOTO_CAMERA) {
			Uri uri = null;
			if(requestCode == REQUESTCODE_PICKPHOTO_ALBUM) {
				if(data != null && data.getData() != null){
					uri = data.getData();
				}
			} else if(requestCode == REQUESTCODE_PICKPHOTO_CAMERA) {
				uri = uriCamera;
			}
			if(uri != null) {
				if(crop) {
					startActivityForCropPhoto(uri);
				} else {
					if(compress) {
						startActivityForCompressPhoto(uri);
					} else {
						Intent intent = new Intent();
						intent.putExtra(IntentCode.INTENT_PHOTOCHOOSE_URI, uri);
						onSetResultFinish(intent);
					}
				}
			}
		} else if(requestCode == REQUESTCODE_CROPPHOTO) {
			if(compress) {
				startActivityForCompressPhoto(uriCrop);
			} else {
				Intent intent = new Intent();
				intent.putExtra(IntentCode.INTENT_PHOTOCHOOSE_URI, uriCrop);
				onSetResultCrop(intent);
			}
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if(uriCrop != null) {
			outState.putString(INTENT_URICROP, uriCrop.toString());
		}
		if(uriCamera != null) {
			outState.putString(INTENT_URICAMERA, uriCamera.toString());
		}
		outState.putBoolean(INTENT_REPLACEMODE, replaceMode);
		outState.putBoolean(INTENT_CROP, crop);
		outState.putInt(INTENT_CROPWIDTH, cropWidth);
		outState.putInt(INTENT_CROPHEIGHT, cropHeight);
		outState.putInt(INTENT_ASPECTX, aspectX);
		outState.putInt(INTENT_ASPECTY, aspectY);
		outState.putBoolean(INTENT_COMPRESS, compress);
		outState.putInt(INTENT_COMPRESSWIDTH, compressWidth);
		outState.putInt(INTENT_COMPRESSHEIGHT, compressHeight);
		outState.putInt(INTENT_COMPRESSFILESIZE, compressFileSize);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		String uricrop = savedInstanceState.getString(INTENT_URICROP);
		if(!TextUtils.isEmpty(uricrop)) {
			uriCrop = Uri.parse(uricrop);
		}
		String uricamera = savedInstanceState.getString(INTENT_URICAMERA);
		if(!TextUtils.isEmpty(uricamera)) {
			uriCamera = Uri.parse(uricamera);
		}
		replaceMode = savedInstanceState.getBoolean(INTENT_REPLACEMODE, replaceMode);
		crop = savedInstanceState.getBoolean(INTENT_CROP, crop);
		cropWidth = savedInstanceState.getInt(INTENT_CROPWIDTH, cropWidth);
		cropHeight = savedInstanceState.getInt(INTENT_CROPHEIGHT, cropHeight);
		aspectX = savedInstanceState.getInt(INTENT_ASPECTX, aspectX);
		aspectY = savedInstanceState.getInt(INTENT_ASPECTY, aspectY);
		compress = savedInstanceState.getBoolean(INTENT_COMPRESS, compress);
		compressWidth = savedInstanceState.getInt(INTENT_COMPRESSWIDTH, compressWidth);
		compressHeight = savedInstanceState.getInt(INTENT_COMPRESSHEIGHT, compressHeight);
		compressFileSize = savedInstanceState.getInt(INTENT_COMPRESSFILESIZE, compressFileSize);
	}
	
	protected void startActivityForCropPhoto(final Uri uri) {
		try {
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
			intent.putExtra("noFaceDetection", true);
			intent.putExtra("return-data", false);
			intent.putExtra("scale", true);
			intent.putExtra("crop", "true");
			intent.setDataAndType(uri, "image/*");
			if(aspectX > 0)
				intent.putExtra("aspectX", aspectX);
			if(aspectY > 0)
				intent.putExtra("aspectY", aspectY);
			intent.putExtra("outputX", cropWidth);
			intent.putExtra("outputY", cropHeight);
			File cropFile = new File(getCachePicFolderName(), getCropPicFileName());
			uriCrop = Uri.fromFile(cropFile);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, uriCrop);
			startActivityForResult(intent, REQUESTCODE_CROPPHOTO);
		} catch (ActivityNotFoundException e) {
			DialogUtils.showToastMessage(R.string.activity_notfound);
		}
	}
	
	protected void startActivityForCompressPhoto(final Uri uri) {
	    if(llContent != null){
	        llContent.setVisibility(View.GONE); 
	    }
		showLoading(true);
		new Thread(new Runnable() {
			@Override
			public void run() {
				Uri uriresult = uri;
				Uri uritemp = compressBitmap(getBaseContext(), uri);
				if(uritemp != null) {
					uriresult = uritemp;
				}
				Message msg = mHandler.obtainMessage();
				msg.what = HANLDER_WHAT_COMPRESFINISH;
				msg.obj = uriresult;
				mHandler.sendMessage(msg);
			}
		}).start();
	}
	
	protected void onSetResultCrop(Intent intent) {
		setResult(RESULTCODE_OK, intent);
		finish();
	}

	protected void onSetResultCompress(Uri uri) {
        Intent intent = new Intent();
        intent.putExtra(IntentCode.INTENT_PHOTOCHOOSE_URI, uri);
		setResult(RESULTCODE_OK, intent);
		finish();
	}
	
	protected void onSetResultFinish(Intent intent) {
		setResult(RESULTCODE_OK, intent);
		finish();
	}
	
	protected void onSetResultCancel() {
		setResult(RESULT_CANCELED);
		finish();
	}
	
	protected void onSetResultDelete() {
		setResult(RESULTCODE_DEL);
		finish();
	}
	
	public static Uri decodeUri(Context context, Intent intent) {
		try {
			Uri uri = intent.getParcelableExtra(IntentCode.INTENT_PHOTOCHOOSE_URI);
			if (uri != null) {
				return uri;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Bitmap decodeBitmap(Context context, Intent intent) {
		try {
			Uri uri = intent.getParcelableExtra(IntentCode.INTENT_PHOTOCHOOSE_URI);
			if (uri != null) {
				Bitmap bitmap = BitmapFactory.decodeStream(context.
						getContentResolver().openInputStream(uri));
				return bitmap;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	protected Uri compressBitmap(Context context, Uri uri) {  
		try {
			int degree = ImageUtils.getBitmapDegree(uri.getPath());
	        Bitmap bitmap = ImageUtils.compressBitmapByBound(context, 
	        		uri, compressWidth, compressHeight);
	        if(degree > 0) {
	        	bitmap = ImageUtils.rotateBitmap(bitmap, degree);
	        }
	        if(bitmap != null) {
	        	Uri newuri = ImageUtils.compressBitmapByQuanlity(bitmap, compressFileSize,
	        			getCachePicFolderName(), getCompressPicFileName());
	        	if(bitmap != null) {
	        		if(!bitmap.isRecycled()) {
	        			bitmap.recycle();
	        		}
	        		bitmap = null;
	        	}
	        	return newuri;
	        }
	        return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			System.gc();
		}
    } 
	
}