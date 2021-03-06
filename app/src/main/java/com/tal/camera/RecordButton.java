package com.tal.camera;

import android.app.Activity;
import android.content.Context;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import cn.com.xpai.core.Manager;


public class RecordButton extends Button {

	public RecordButton(final Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				MainHandler mainHandler = new MainHandler((Activity)context ,true);
				Message msg = new Message();
				msg.what = MainHandler.MSG_RECODER_BTN_VISIBLE;
				mainHandler.sendMessage(msg);
				if (Manager.RecordStatus.RECORDING == Manager.getRecordStatus() ||
						Manager.RecordStatus.PAUSE == Manager.getRecordStatus() ) {
					Manager.stopRecord();
					Message msg1 = new Message();
					msg1.what = MainHandler.MSG_RECODER_BTN_UNVISIBLE;
					mainHandler.sendMessage(msg1);
					//setBackgroundResource(R.drawable.record);
				} else if(Manager.RecordStatus.IDLE == Manager.getRecordStatus()) {
					//Manager.startPreview();
					String output_tag = null;
					if("".equals(Config.output_tag)) {
						output_tag = null;
					} else {
						output_tag = Config.output_tag;
					}
					boolean isSavingVideoFile = Manager.isConnected()? Config.isSavingVideoFile:true;
					if (!Manager.startRecord(Config.recordMode, Config.videoBitRate * 1024,
							isSavingVideoFile, false, output_tag, "testing opaque string")){
						//@Todo 启动录制失败，显示错误信息
					}
				}
				TALAndroid.mainHandler.sendEmptyMessage(MainHandler.MSG_SWITCH_BTN_VISIBILITY);
			}
		});
	}
	
	boolean flipFlag;
	long lastFlip;
	
	public void update() {
		if (Manager.RecordStatus.RECORDING == Manager.getRecordStatus()) {
			long now = System.currentTimeMillis();
			//让按钮用两个图片切换产闪烁
			if (now - lastFlip > 400) {
				lastFlip = now;
				flipFlag = !flipFlag;
				if (flipFlag) {
					//this.setBackgroundResource(R.drawable.record_active);
				} else {
					//this.setBackgroundResource(R.drawable.record);
				}
			}
			
		} else if (flipFlag) {
			flipFlag = false;
		}
	}
}