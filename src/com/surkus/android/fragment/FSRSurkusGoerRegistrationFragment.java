package com.surkus.android.fragment;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.surkus.android.R;
import com.surkus.android.component.ASRSurkusGoerDashboardActivity;
import com.surkus.android.model.CSRSurkusApiResponse;
import com.surkus.android.networking.CSRWebConstants;
import com.surkus.android.networking.CSRWebServices;
import com.surkus.android.utils.CSRConstants;
import com.surkus.android.utils.CSRUtils;

public class FSRSurkusGoerRegistrationFragment extends Fragment implements OnClickListener{
	
	String mSurkusToken;
	private CSRWebServices webServiceSingletonObject;
	RegisterSurkusUserContactInfoTask mRegisterSurkusUserContactInfoTask;
	
	View mSurkusGoerRegistationParentView;
	EditText mZipOrPostalCodeEditText;
	EditText mPhoneNoEditText;	
	Button mSurkusGoerRegisterButton;
	private ProgressDialog mSurkusGoerRegsiterDialog;
	
	 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		webServiceSingletonObject = CSRWebServices.getSingletonRef();
		mSurkusToken = CSRUtils.getStringSharedPref(getActivity(), CSRConstants.SURKUS_TOKEN_SHARED_PREFERENCE_KEY);		
		
		mSurkusGoerRegistationParentView = inflater.inflate(R.layout.fragment_user_registration, container, false);
		mZipOrPostalCodeEditText = (EditText) mSurkusGoerRegistationParentView.findViewById(R.id.zipcode_edittext);
		mPhoneNoEditText = (EditText) mSurkusGoerRegistationParentView.findViewById(R.id.mobile_phoneno_edittext);
		
		mSurkusGoerRegisterButton = (Button)mSurkusGoerRegistationParentView.findViewById(R.id.surkus_goer_register_btn);
		mSurkusGoerRegisterButton.setOnClickListener(this);
		
        return mSurkusGoerRegistationParentView;
	}
	
	
	@Override
	public void onStart() {
	   super.onStart();

	   getView().setFocusableInTouchMode(true);
	   getView().requestFocus();
	   getView().setOnKeyListener( new OnKeyListener()
	   {
	       @Override
	       public boolean onKey( View v, int keyCode, KeyEvent event )
	       {
	           if( keyCode == KeyEvent.KEYCODE_BACK )
	           {
	        	   if(mRegisterSurkusUserContactInfoTask != null && !mRegisterSurkusUserContactInfoTask.isCancelled())
	        	   {
	        		   dismissRegisterSurkusGoerDiloag();
	        		   mRegisterSurkusUserContactInfoTask.cancel(true);
	        	   }
	           }
	           return false;
	       }
	   } );
	}
	

	void displayRegisterSurkusGoerDiloag()
	{
	  try
	 {
		mSurkusGoerRegsiterDialog = new ProgressDialog(getActivity());
		mSurkusGoerRegsiterDialog.setMessage(getActivity().getResources().getString(R.string.registering_user));
		mSurkusGoerRegsiterDialog.setCancelable(false);
		mSurkusGoerRegsiterDialog.setCanceledOnTouchOutside(false);
		mSurkusGoerRegsiterDialog.setIndeterminate(true);
		mSurkusGoerRegsiterDialog.show();
	   }
		catch(Exception e)
		{
			
		}
	}
	
	void dismissRegisterSurkusGoerDiloag()
	{
        if(mSurkusGoerRegsiterDialog != null)
        {
	       mSurkusGoerRegsiterDialog.dismiss();
        }
	}
	

	private class RegisterSurkusUserContactInfoTask extends
			AsyncTask<String, Integer, CSRSurkusApiResponse> {

		String mCellPhoneNumber;
		String mZipCode;
		String mSurkusToken;

		RegisterSurkusUserContactInfoTask(String zipCode, String cellPhoneNumber,String surkusToken) {
			mZipCode = zipCode;
			mCellPhoneNumber = cellPhoneNumber;
			mSurkusToken = surkusToken;
		}
		
    @Override
     protected void onPreExecute() {
	       super.onPreExecute();
           displayRegisterSurkusGoerDiloag();
        }

		@Override
		protected CSRSurkusApiResponse doInBackground(String... args) {
			try {
				
				JSONObject jObject = new JSONObject();
				jObject.put(CSRWebConstants.ZIPCODE_KEY, mZipCode);
				jObject.put(CSRWebConstants.CELL_PHONE_KEY, mCellPhoneNumber);
				webServiceSingletonObject.registerSurkusUser(mSurkusToken,jObject.toString());

			} catch (JSONException e) {

			}

			return null;

		}

		@Override
		protected void onPostExecute(CSRSurkusApiResponse surkusTokenResponse) {
			dismissRegisterSurkusGoerDiloag();	
			getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new FSRSurkusGoerRegistrationThankYouFragment()).commit();			
		}
	}

	private boolean isInputFieldsValid()
	{

		if(TextUtils.isEmpty(mZipOrPostalCodeEditText.getText()) || TextUtils.isEmpty(mPhoneNoEditText.getText()))
			return false;
		else
			return true;
		
	}


	@Override
	public void onClick(View v) {
	   int viewID = v.getId();
	   
	   switch (viewID) {
	     case R.id.surkus_goer_register_btn:
	    	
	    	if(isInputFieldsValid())
	    	{			    
	    		mRegisterSurkusUserContactInfoTask = new RegisterSurkusUserContactInfoTask(mZipOrPostalCodeEditText.getText().toString(), mPhoneNoEditText.getText().toString(),mSurkusToken); //"07980", "123456789"
	    		mRegisterSurkusUserContactInfoTask.execute();
	    	}
	    	else
	    	{
	    		CSRUtils.showAlertDialog(getActivity(), getString(R.string.error), getString(R.string.input_field_warning_msg));
	    	}

	    	
		break;

	default:
		break;
	}
		
	}
	
}
