package com.architecture.zxing;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;

import com.architecture.R;
import com.architecture.activity.ProjectInfoActivity;
import com.architecture.activity.base.BaseActivity;
import com.architecture.context.IntentCode;
import com.architecture.models.api.BaseOperater;
import com.architecture.models.api.GetNodeMgrInfoOperater;
import com.architecture.models.api.GetNodeMgrListOperater;
import com.architecture.models.entry.ProjectInfo;
import com.architecture.utils.ViewInject;
import com.architecture.view.TitleBar;
import com.architecture.zxing.camera.CameraManager;
import com.architecture.zxing.view.ViewfinderView;
import com.google.zxing.BarcodeFormat;
import com.architecture.zxing.decoding.CaptureActivityHandler;
import com.google.zxing.Result;

/**
 * Initial the camera
 */
public class MipcaActivityCapture extends BaseActivity implements Callback {

	@ViewInject("viewfinder_view")
	private ViewfinderView viewfinderView;
	@ViewInject("titlebar")
	private TitleBar titleBar;
	private CaptureActivityHandler handler;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_capture);
		CameraManager.init(getApplication());
		hasSurface = false;
	}
	
	@Override
	public void onBindListener() {
		titleBar.setLeftOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		startScan();
	}
	
	private void startScan () {
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}
 
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	/**
	 * ����ɨ����
	 * @param result
	 * @param barcode
	 */
	public void handleDecode(Result result, Bitmap barcode) {
		playBeepSoundAndVibrate();
		String resultString = result.getText();
		if (resultString.equals("")) {
			showToastMessage("Scan failed!");
			if (handler != null) {
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						if (handler != null) {
							handler.restartPreviewAndDecode();
						}
					}
				}, 2000);
			}
		} else {
			getCodeSearchResult(resultString);
		}
	}

	private void getCodeSearchResult(String resultString) {
		showLoading(true);
		final GetNodeMgrListOperater operater = new GetNodeMgrListOperater(this);
		operater.setShowLoading(false);
		operater.setParams(resultString, "", "");
		operater.onReq(new BaseOperater.RspListener() {
			@Override
			public void onRsp(boolean success, Object obj) {
				if (success) {
					List<ProjectInfo> projectInfos = operater.getProjectInfo();
					if (projectInfos != null && projectInfos.size() != 0) {
						ProjectInfo projectInfo = projectInfos.get(0);
						if (projectInfo != null && !TextUtils.isEmpty(projectInfo.id)) {
							getCodeInfo(projectInfo.id);
							return;
						}
					}
					showLoading(false);
					showToastMessage(R.string.tip_search_no_data);
					if (handler != null) {
						handler.postDelayed(new Runnable() {
							@Override
							public void run() {
								if (handler != null) {
									handler.restartPreviewAndDecode();
								}
							}
						}, 2000);
					}
				} else {
					showLoading(false);
					if (handler != null) {
						handler.postDelayed(new Runnable() {
							@Override
							public void run() {
								if (handler != null) {
									handler.restartPreviewAndDecode();
								}
							}
						}, 2000);
					}
				}
			}
		});
	}

	private void getCodeInfo(String id) {
		final GetNodeMgrInfoOperater operater = new GetNodeMgrInfoOperater(this);
		operater.setParams(id);
		operater.setShowLoading(false);
		operater.onReq(new BaseOperater.RspListener() {
			@Override
			public void onRsp(boolean success, Object obj) {
				showLoading(false);
				if (success) {
					ProjectInfo projectInfo = operater.getProjectInfo();
					if (projectInfo != null && !TextUtils.isEmpty(projectInfo.id)) {
						Intent intent = new Intent();
						intent.putExtra(IntentCode.INTENT_PROJECT_INFO, projectInfo);
						startActivity(ProjectInfoActivity.class, intent);
						finish();
					}
				} else {
					if (handler != null) {
						handler.postDelayed(new Runnable() {
							@Override
							public void run() {
								if (handler != null) {
									handler.restartPreviewAndDecode();
								}
							}
						}, 2000);
					}
				}
			}
		});
	}
	
	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

}