package com.surkus.android.surkusgoer.fragment;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.surkus.android.R;
import com.surkus.android.model.CSRSurkusApiResponse;
import com.surkus.android.networking.CSRWebConstants;
import com.surkus.android.networking.CSRWebServices;
import com.surkus.android.surkusgoer.component.ASRSurkusGoerDashboardActivity;
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
		mPhoneNoEditText.addTextChangedListener(mMobileNumberFormatterWathcher);
		
		String phoneNo = getUserMobileNumber();
		if(phoneNo != null && !TextUtils.isEmpty(phoneNo))
			mPhoneNoEditText.setText(phoneNo);
		
		mSurkusGoerRegisterButton = (Button)mSurkusGoerRegistationParentView.findViewById(R.id.surkus_goer_register_btn);
		mSurkusGoerRegisterButton.setOnClickListener(this);
		
        return mSurkusGoerRegistationParentView;
	}
	
	private String getUserMobileNumber()
	{
		TelephonyManager tMgr = (TelephonyManager)getActivity().getSystemService(Context.TELEPHONY_SERVICE);
		return tMgr.getLine1Number();
	}
	
    TextWatcher mMobileNumberFormatterWathcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			String phoneNumber = s.toString().trim().replaceAll("-", "");
			if (!phoneNumber.equalsIgnoreCase("")) {
				mPhoneNoEditText.removeTextChangedListener(this);
				mPhoneNoEditText.setText("");
				mPhoneNoEditText.append(CSRUtils.getUSFormattedMobileNumber(Double.parseDouble(phoneNumber)));
				mPhoneNoEditText.addTextChangedListener(this);
			}
		}
	};
	
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
			
			CSRSurkusApiResponse surkusApiResponse = null;

			try {
				
				JSONObject jObject = new JSONObject();
				jObject.put(CSRWebConstants.ZIPCODE_KEY, mZipCode);
				jObject.put(CSRWebConstants.CELL_PHONE_KEY, mCellPhoneNumber);
				surkusApiResponse = webServiceSingletonObject.registerSurkusUser(mSurkusToken,jObject.toString());

			} catch (JSONException e) {

			}

			return surkusApiResponse;

		}

		@Override
		protected void onPostExecute(CSRSurkusApiResponse surkusTokenResponse) {
			dismissRegisterSurkusGoerDiloag();	
			
			if(surkusTokenResponse == null)
			{
				CSRUtils.showAlertDialog(getActivity(),
						CSRWebConstants.SERVER_ERROR,
						CSRWebConstants.NO_RESPONSE_FROM_SERVER);
			}
			else if((surkusTokenResponse.getStatusCode() == CSRWebConstants.STATUS_CODE_401))
			{
				CSRUtils.showAlertDialog(getActivity(),
						CSRWebConstants.SERVER_ERROR,
						getString(R.string.invalid_token_server_error_msg));
			}	else if((surkusTokenResponse.getStatusCode() == CSRWebConstants.STATUS_CODE_400))
			{CSRUtils.showAlertDialog(getActivity(),
					CSRWebConstants.SERVER_ERROR,
					getString(R.string.zipcode_server_error_msg));
				
			}	else if((surkusTokenResponse.getStatusCode() == CSRWebConstants.STATUS_CODE_500))
			{
				CSRUtils.showAlertDialog(getActivity(),
						CSRWebConstants.SERVER_ERROR,
						getString(R.string.server_error_msg));
			}
			else
			{
				//getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new FSRSurkusGoerRegistrationThankYouFragment()).commit();
				  Intent surkusGoerRegistrationIntent = new Intent(getActivity(),ASRSurkusGoerDashboardActivity.class);
				  startActivity(surkusGoerRegistrationIntent); 
				  getActivity().finish();
			}
			
		}
	}

	private String isInputFieldsValid()
	{
		String errorMessage ="";
		
		if(TextUtils.isEmpty(mZipOrPostalCodeEditText.getText()) && TextUtils.isEmpty(mPhoneNoEditText.getText()))
			errorMessage = getString(R.string.input_field_registration_warning_msg);
		else
		if(TextUtils.isEmpty(mZipOrPostalCodeEditText.getText()) || mZipOrPostalCodeEditText.getText().length() <5)
			errorMessage = getString(R.string.input_field_zipcode_warning_msg);
		else
		if(TextUtils.isEmpty(mPhoneNoEditText.getText()) || mPhoneNoEditText.getText().length()<10)
			errorMessage = getString(R.string.input_field_phoneno_warning_msg);
		
	   return errorMessage;

		
	}


	@Override
	public void onClick(View v) {
	   int viewID = v.getId();
	   
	   switch (viewID) {
	     case R.id.surkus_goer_register_btn:
	    	 String errorMessage = isInputFieldsValid();
	    	if(TextUtils.isEmpty(errorMessage))
	    	{			    
	    		mRegisterSurkusUserContactInfoTask = new RegisterSurkusUserContactInfoTask(mZipOrPostalCodeEditText.getText().toString(), mPhoneNoEditText.getText().toString(),mSurkusToken); //"07980", "123456789"
	    		mRegisterSurkusUserContactInfoTask.execute();
	    	}
	    	else
	    	{
	    		CSRUtils.showAlertDialog(getActivity(), getString(R.string.error), errorMessage);
	    	}
	    	
		break;

	default:
		break;
	}
		
	}
	
}
