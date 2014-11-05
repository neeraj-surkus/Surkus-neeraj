package com.surkus.android.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.surkus.android.R;
import com.surkus.android.model.CSRRatingOption;
import com.surkus.android.model.CSRRatingQuestion;

public class CSRSurkusGoerRatingQuestionsAdapter extends BaseAdapter{

	List<String> list;
	private LayoutInflater inflator = null;
	private ArrayList<CSRRatingQuestion> mRatingQuestionList;
		 
	public CSRSurkusGoerRatingQuestionsAdapter(Context activity){
		
		inflator = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public void setRatingQuestions(ArrayList<CSRRatingQuestion> ratingQuestionList)
	{
		mRatingQuestionList = ratingQuestionList;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		
		if(mRatingQuestionList != null && mRatingQuestionList.size() >0)
		    return mRatingQuestionList.size();
		else 
			return 0;
	}

	@Override
	public Object getItem(int arg0) {
		return mRatingQuestionList.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return mRatingQuestionList.get(position).hashCode();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	
        CSRRatingQuestion ratingQuestion = mRatingQuestionList.get(position);
		RatingQuestionHolder ratingQuestionHolder = null;
		
        if (convertView == null) {
        	ratingQuestionHolder = new RatingQuestionHolder();
        	convertView = inflator.inflate(R.layout.rating_question_list_item, null);
            
            ratingQuestionHolder.mRatingQuestionTextview = ((TextView) convertView.findViewById(R.id.rate_title_textview));   
            ratingQuestionHolder.mRatingOptionsLayout = ((LinearLayout) convertView.findViewById(R.id.rating_options_layout));
            
            convertView.setTag(ratingQuestionHolder);	
        }
        else
        {
        	ratingQuestionHolder = (RatingQuestionHolder) convertView.getTag();
        }
          
        ratingQuestionHolder.mRatingQuestionTextview.setText(ratingQuestion.getRatingQuestion());  
        
        if(ratingQuestionHolder.mRatingOptionsLayout.getChildCount() >0)
        {
        	ratingQuestionHolder.mRatingOptionsLayout.removeAllViews();
        }
        
        ArrayList<CSRRatingOption> ratingOptionList = ratingQuestion.getRatingOptionList();
        
        for(int i=0;i<ratingOptionList.size();i++)
        {
        	
        	 RelativeLayout ratingOptionLayout = ((RelativeLayout) inflator.inflate(R.layout.rating_option_item, null));
        	 
        	 CSRRatingOption ratingOption =  ratingOptionList.get(i);
        	 
        	/* for(int j=0;j< ratingOption.getRating();j++)
        	 {
        	
        	 }*/
      	 
      
        	 ((TextView)ratingOptionLayout.findViewById(R.id.row_title_textview)).setText(ratingOption.getTitle());
        	 
        	 ratingQuestionHolder.mRatingOptionsLayout.addView(ratingOptionLayout);
        }
        
      return convertView;
	}
	
	
	private class RatingQuestionHolder
	{
		TextView mRatingQuestionTextview;
		LinearLayout mRatingOptionsLayout;
		
    }

}

