package com.gs.saveu;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;

public class RingtoneService extends Service {
	Context mContext;
	Ringtone mRingtone;
	MediaPlayer mMediaPlayer;
	AudioManager mAudioManager;
	int previousVolume;
	int previousRingerMode;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = getApplicationContext();
		startRingtone();
		Intent intent = new Intent(mContext, MainActivity.class)
		.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		mContext.startActivity(intent);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		stopRingtone();
	}

	private void stopRingtone() {
		mAudioManager.setStreamVolume(AudioManager.STREAM_RING, previousVolume, AudioManager.FLAG_PLAY_SOUND);
		mAudioManager.setRingerMode(previousRingerMode);
		mMediaPlayer.stop();
	}

	private void startRingtone() {
		try {
			mAudioManager = (AudioManager)getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
			int maxVolumeRing = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
			previousRingerMode = mAudioManager.getRingerMode();
			mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
			previousVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_RING);
			mAudioManager.setStreamVolume(AudioManager.STREAM_RING, maxVolumeRing, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
			//mAudioManager.setStreamVolume(AudioManager.STREAM_RING, 1, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
			
			Uri ringtoneUri =  RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
			mMediaPlayer = new MediaPlayer();
			mMediaPlayer.setDataSource(this, ringtoneUri);
			mMediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
			mMediaPlayer.setLooping(true);
			mMediaPlayer.prepare();
			mMediaPlayer.start();
		} catch (Exception e) {
		}
	}
}
