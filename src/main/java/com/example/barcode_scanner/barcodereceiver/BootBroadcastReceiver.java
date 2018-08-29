package com.example.barcode_scanner.barcodereceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.barcode_scanner.barcodeservice.SerialPortService;


public class BootBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			Intent screenMonitorServiceIntent=new Intent(context,SerialPortService.class);
			context.startService(screenMonitorServiceIntent);
		}
	}

}
