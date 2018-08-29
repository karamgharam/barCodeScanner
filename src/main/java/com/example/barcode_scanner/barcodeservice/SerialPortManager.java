package com.example.barcode_scanner.barcodeservice;

import android.util.Log;

import com.example.barcode_scanner.android_serialport_api.SerialPort;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class SerialPortManager{
	private static final String TAG = "SerialPortManager";
	
	public static SerialPortManager  mInstance;
		
	private SerialPort mSerialPort = null;
	private OutputStream mOutputStream;
	private InputStream mInputStream;
	private ReadThread   mReadThread;
	
	private int  mLocalRate  = 0;
	
	private SerialPortListener listener;
	private Lock mlock = new ReentrantLock();
	
	public static Object mObject = new Object();
	
	public static SerialPortManager getInstance(){
		
		if( mInstance == null){
			mInstance = new SerialPortManager();
		}
		return mInstance;
	}
	
	public SerialPort getSerialPort() throws SecurityException, IOException, InvalidParameterException {
		  
		if (mSerialPort == null) {
			Log.e("getSerialPort", "mBaudrate = " + mLocalRate);
		    mSerialPort = new SerialPort(new File("/dev/ttyMT1"), mLocalRate, 0);
		}
		
		return mSerialPort;
  }
	

   public void open(int mRate) {
		
		if(mlock.tryLock()){

			mLocalRate = mRate;
			try {
				if( mSerialPort != null){
					mSerialPort.close();
					mSerialPort = null;
				}
				mSerialPort   = getSerialPort();
				mOutputStream = mSerialPort.getOutputStream();
				mInputStream  = mSerialPort.getInputStream();
			} catch (InvalidParameterException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				mlock.unlock();
			}

		}else{
			Log.e(TAG, " tryLock  fail ");
		}

  }
	


	 /* 关闭串口设备*/
	public void close() {
		
		if( mSerialPort != null){
			mSerialPort.close();
			mInputStream = null;
			mOutputStream = null;
			mSerialPort = null;
		}
		
		if(mReadThread != null){
			if (mReadThread.isAlive() ){
				mReadThread.interrupt();
			}
		}
	}
	
	/*向串口写数据*/
	private byte[] bytePre = new byte[] { 0x16, (byte) 0x4D, (byte) 0x0D };
	public int write(String data) {
		int res = 0;
		try {
			mOutputStream.write(bytePre);
			Log.d(TAG, "data:"+data);
			mOutputStream.write(data.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			res = -1;
			e.printStackTrace();
		}
		return 0;
	}

	/*该线程中从串口读取数据*/
	private class ReadThread extends Thread {
		
		@Override
		public void run() {
			super.run();
			
			while(!isInterrupted()) {
				
					int size = -1;
					final byte[] buffer = new byte[128];

					if (mInputStream == null) return;

					try {
						size = mInputStream.read(buffer);
					}catch (Exception e) {
						e.printStackTrace();
					}

					if (size > 0) {
						listener.onResult(new String(buffer, 0, size));
						Log.d(TAG,new String(buffer, 0, size));
					}

					try {
						Thread.sleep(150);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
                
				}
			
		}
   }
	
	/*开启从串口读取数据线程*/
	public void read() {
		  mReadThread = new ReadThread();
	      mReadThread.setName("MyReadThread");
		  mReadThread.start();
	}
	
	/*实现该回调获取数据*/
	public interface SerialPortListener {

		public abstract void onResult(String data);
	}
	
	public void setListener(SerialPortListener listener){
		
		SerialPortManager.this.listener = listener;
	}
}
