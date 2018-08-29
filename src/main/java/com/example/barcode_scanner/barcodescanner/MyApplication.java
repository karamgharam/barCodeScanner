package com.example.barcode_scanner.barcodescanner;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

public class MyApplication extends Application {

	    public static final String Tag = "MyApplication";
        public static final String SYMBOL_CONFIG_SIG =  "com.android.barcodescanner.sig";
        public static final String SYMBOL_CONFIG_QR  =  "com.android.barcodescanner.qr";
        public static final String SCAN_MODE_SIG     =  "com.android.barcodescanner.scanmodesig";
        public static final String SCAN_MODE_LEFT    =  "com.android.barcodescanner.leftenable";
        public static final String SCAN_MODE_RIGHT    =  "com.android.barcodescanner.rightenable";
        public static final String SCAN_TIME_OUT     =  "com.android.barcodescanner.timeout";

        public static final String MODUL_INIITED     =  "com.android.barcodescanner.init";
        public static final String FIRST_INSTALL     =  "com.android.barcodescanner.firstinstall";
        public static final String FACTORY_DEFAULT   =  "defalt.";
        public static final String[] FACTORY_RESULT = {"0x44", "0x45", "0x46", "0x41", "0x4c", "0x54", "0x06", "0x2e"};

        public static boolean onSettings = false;


    private String[] symbolsSig = {				//一维码制名称
    			"Straight 2 of 5 IATA", "China Post", "codebar", "code 11",
    			"Code 128",  "code 32 Pharmaceutical",  "Code 39",  "code 93",
    			"EAN/JAN-13", "EAN/JAN-8",  "GS1 DataBar Expanded", "GS1 DataBar Limited",
    			"GS1 DataBar Omnidirectional",  "GS1-128",  "Interleaved 2 of 5", "ISBT 128",
    			"Matrix 2 of 5",  "MSI",  "NEC 2 of 5",  "Plessey Code",
    			"Postal Codes", "Straight 2 of 5 Industrial",  "Telepen",  "Trioptic Code",
    			"UPC-A/EAN-13(Allow Concatenation)", "UPC-A/EAN-13(Require Concatenation)", "UPC-E0",  "GS1-128 Emulation",
    			"GS1 DataBar Emulation",  "GS1 Code Expansion Off",  "EAN8 to EAN13 Conversion"
    			};

    private String[] commandsSig_default = {	//一维默认码制
				"a25ena0.",  "cpcena0.", "cbrena1.",  "c11ena0.",
				"128ena1.",  "c39b320.", "c39ena1.",  "c93ena1.",
				"e13ena1.",  "ea8ena1.", "rseena1.",  "rslena1.",
				"rssena1.",  "gs1ena1.", "i25ena1.",  "isbena0.",
				"x25ena0.",  "msiena0.", "n25ena0.",  "plsena0.",
				"cpcena0.",  "r25ena0.", "telena0.",  "triena0.",
				"cpnena0.",  "cpnena0.", "upeen01.",  "eanemu0.",
				"eanemu0.",  "eanemu0.", "eanemu0."
	};

     private String[] symbolsQR = {			//二维码制名称
			    "Aztec Code",  "China Post (Hong Kong 2 of 5)",  "Chinese Sensible (Han Xin) Code",  "Codabar",
			    "Codablock A",  "Codablock F",  "Code 11",  "Code 128",
                "Code 32 Pharmaceutical (PARAF)", "Code 39",  "Code 93",  "Data Matrix",
                "EAN/JAN-13",  "EAN/JAN-8",  "GS1 Composite Codes",  "GS1 DataBar Expanded",
                "GS1 DataBar Limited",  "GS1 DataBar Omnidirectional",  "GS1-128 Emulation", "GS1 DataBar Emulation",
                "GS1 Code Expansion Off", "EAN8 to EAN13 Conversion",  "GS1-128",  "Interleaved 2 of 5",
                "Korea Post",  "Matrix 2 of 5",  "MaxiCode",  "MicroPDF417",
                "MSI",  "NEC 2 of 5",  "China Post (Hong Kong 2 of 5)", "Korea Post",
                "Korea Post Check Digit",  "PDF417",  "QR Code",  "Straight 2 of 5 IATA (two-bar start/stop)",
                "Straight 2 of 5 Industrial (three-bar start/stop)",  "TCIF Linked Code 39 (TLC39)",  "UPC-A",  "UPC-A/EAN-13 with Extended Coupon Code(Allow Concatenation)",
                "UPC-A/EAN-13 with Extended Coupon Code(Require Concatenation)", "UPC-E0",  "UPC-E1"
	};

	private String[] commandsQR_default = {		//二维默认码制
			    "aztena1.", "cpcena0.", "hx_ena0.", "cbrena1.",
	            "cbaena0.", "cbfena0.", "c11ena0.", "128ena1.",
	            "c39b320.", "c39ena1.", "c93ena1.", "idmena1.",
	            "e13ena1.", "ea8ena1.", "comena0.", "rseena1.",
	            "rslena1.", "rssena1.", "eanemu0.", "eanemu0.",
	            "eanemu0.", "eanemu0.", "gs1ena1.", "i25ena1.",
	            "kpcena0.", "x25ena0.", "maxena0.", "mpdena0.",
	            "msiena0.", "n25ena1.", "cpcena0.", "kpcena0.",
	            "kpcchk0.", "pdfena1.", "qrcena1.", "a25ena0.",
	            "r25ena0.", "t39ena0.", "upbena1.", "cpnena0.",
	            "cpnena0.", "upeen01.", "upeen10."
    };
    public static final String KEY_POSTAL_2D   = "PostalCodes2D";




	private void LoadFactorySettings(String[] symbols, String[] defaultCommands, String SP_KEY){

	         if(symbols.length  != defaultCommands.length){
	        	 Log.e(Tag, "LoadFactorySettings  fail");
	        	 return;
	         }

	         SharedPreferences mSP = getSharedPreferences(SP_KEY, 0);
		  	 SharedPreferences.Editor editor = mSP.edit();

	         for( int i = 0;  i < defaultCommands.length;  i ++){
	        	 if(defaultCommands[i].endsWith("0.")){
			        editor.putBoolean(symbols[i], false);
	        	 }else{
	        		editor.putBoolean(symbols[i], true);
	        	 }
	         }

	         if( SP_KEY.equals(SYMBOL_CONFIG_QR) ){
	        	 editor.putInt(KEY_POSTAL_2D, 0);
	         }

		  	 editor.commit();
	}

	public void LoadAllFactorySettings(){

	         LoadFactorySettings(symbolsSig, commandsSig_default, SYMBOL_CONFIG_SIG);
		     LoadFactorySettings(symbolsQR, commandsQR_default, SYMBOL_CONFIG_QR);
	}




}