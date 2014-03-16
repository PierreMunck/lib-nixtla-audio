package com.nibsa.nixtla.android.demo.captureeco;

import com.nibsa.nixtla.android.demo.captureeco.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	private DemoNativeInterface nativeIntf = null;
	private Spinner cmbChannels 		= null;
	private Spinner cmbSampleRate 		= null;
	private Spinner cmbBitsPerSample 	= null;
	private Button btnCaptureStop 		= null;
	private TextView txtLog 			= null;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
		cmbChannels 		= (Spinner)findViewById(R.id.cmbChannels);
		cmbSampleRate 		= (Spinner)findViewById(R.id.cmbSampleRate);
		cmbBitsPerSample 	= (Spinner)findViewById(R.id.cmbBitsPerSample); cmbBitsPerSample.setSelection(1);
		btnCaptureStop 		= (Button)findViewById(R.id.btnPlayStop);
		txtLog 				= (TextView)findViewById(R.id.txtLog);
		//
		btnCaptureStop.setOnClickListener(this);
    } 

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        if(nativeIntf==null){
        	nativeIntf = new DemoNativeInterface();
        	new Thread(){
        		public void run() {
        			System.out.println("------------ THREAD RUN START ------------");
        			while(nativeIntf!=null){
        				nativeIntf.tick();
        				//
        				runOnUiThread(
        						new Runnable() {
        							@Override
        							public void run() {
        								txtLog.setText(nativeIntf.getStatusLog());
        							}
        						} 
        				);
        				//
        				try {
        					Thread.sleep(1000 / 30);
        				} catch(Exception e){
        					System.out.println(e);
        				}
        			}
        		}
        	}.start();
        }
        System.out.println("------ onResume");
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        if(nativeIntf!=null){
        	nativeIntf.finalizeDemo();
        	nativeIntf = null;
        }
        System.out.println("------ onPause");
    }
    
	@Override
	public void onDestroy(){
		super.onDestroy();
		if(nativeIntf!=null){
        	nativeIntf.finalizeDemo();
        	nativeIntf = null;
        }
		System.out.println("------ onDestroy");
	}
	
	public void onClick(View v) {
		 if(v==btnCaptureStop){
			 if(nativeIntf!=null){
				 nativeIntf.actionCapture(cmbChannels.getSelectedItemPosition()+1, Integer.parseInt(cmbSampleRate.getSelectedItem().toString()), Integer.parseInt(cmbBitsPerSample.getSelectedItem().toString()));
			 }
		 }
	 }
	
}
