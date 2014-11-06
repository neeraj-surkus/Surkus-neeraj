package com.surkus.android.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
	
	public ArrayList<CSRRatingQuestion> getRatingQuestions()
	{
		return mRatingQuestionList;
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
	
	public boolean isAllQuestionsAnswered()
	{
		for(int i=0;i<mRatingQuestionList.size();i++)
		{
			 CSRRatingQuestion ratingQuestion = mRatingQuestionList.get(i);
			 ArrayList<CSRRatingOption> ratingOptionList = ratingQuestion.getRatingOptionList();
			 
			 for(int j=0;j<ratingOptionList.size();j++)
			 {
				  CSRRatingOption ratingOption =  ratingOptionList.get(j);
				  if(ratingOption.getRating() == 0)
					  return false;
			 }
		}
		return true;
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
        	
        	 final RelativeLayout ratingOptionLayout = ((RelativeLayout) inflator.inflate(R.layout.rating_option_item, null));
        	 
        	 final CSRRatingOption ratingOption =  ratingOptionList.get(i);
    
        	 int rating = ratingOption.getRating();
        	 if(rating == 3)
        	 {
        	   ((ImageView)ratingOptionLayout.findViewById(R.id.row_rate_1_imageview)).setSelected(true);
        	   ((ImageView)ratingOptionLayout.findViewById(R.id.row_rate_2_imageview)).setSelected(true);
        	   ((ImageView)ratingOptionLayout.findViewById(R.id.row_rate_3_imageview)).setSelected(true);        	 
        	 }
        	 else  if(rating == 2)
        	 {
        	   ((ImageView)ratingOptionLayout.findViewById(R.id.row_rate_1_imageview)).setSelected(true);
          	   ((ImageView)ratingOptionLayout.findViewById(R.id.row_rate_2_imageview)).setSelected(true);
          	   ((ImageView)ratingOptionLayout.findViewById(R.id.row_rate_3_imageview)).setSelected(false); 
        		 
        	 }else if(rating == 1)
        	 {
        		  ((ImageView)ratingOptionLayout.findViewById(R.id.row_rate_1_imageview)).setSelected(true);
             	   ((ImageView)ratingOptionLayout.findViewById(R.id.row_rate_2_imageview)).setSelected(false);
             	   ((ImageView)ratingOptionLayout.findViewById(R.id.row_rate_3_imageview)).setSelected(false); 
        	 }
        	 else
        	 {
        		  ((ImageView)ratingOptionLayout.findViewById(R.id.row_rate_1_imageview)).setSelected(false);
            	  ((ImageView)ratingOptionLayout.findViewById(R.id.row_rate_2_imageview)).setSelected(false);
            	  ((ImageView)ratingOptionLayout.findViewById(R.id.row_rate_3_imageview)).setSelected(false); 
        	 }
        	 
        	 ((ImageView)ratingOptionLayout.findViewById(R.id.row_rate_1_imageview)).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					ratingOption.setRating(1);
					updateRatings(ratingOption, ratingOptionLayout);
					//notifyDataSetInvalidated();
					
				}
			});
        	 
        	 ((ImageView)ratingOptionLayout.findViewById(R.id.row_rate_2_imageview)).setOnClickListener(new OnClickListener() {
 				
 				@Override
 				public void onClick(View v) {
 				
 					ratingOption.setRating(2);
 					updateRatings(ratingOption, ratingOptionLayout);
 					//notifyDataSetInvalidated();
 				}
 			});
        	 
        	 
        	 ((ImageView)ratingOptionLayout.findViewById(R.id.row_rate_3_imageview)).setOnClickListener(new OnClickListener() {
 				
 				@Override
 				public void onClick(View v) {
 				
 					ratingOption.setRating(3);
 					updateRatings(ratingOption, ratingOptionLayout);
 					//notifyDataSetInvalidated();
 				}
 			});
        	 
        	 ((TextView)ratingOptionLayout.findViewById(R.id.row_title_textview)).setText(ratingOption.getTitle());
        	 
        	 ratingQuestionHolder.mRatingOptionsLayout.addView(ratingOptionLayout);
        }
        
      return convertView;
	}
	
	
	void updateRatings(CSRRatingOption ratingOption,  RelativeLayout ratingOptionLayout)
	{
		 int rating = ratingOption.getRating();
    	 if(rating == 3)
    	 {
    	   ((ImageView)ratingOptionLayout.findViewById(R.id.row_rate_1_imageview)).setSelected(true);
    	   ((ImageView)ratingOptionLayout.findViewById(R.id.row_rate_2_imageview)).setSelected(true);
    	   ((ImageView)ratingOptionLayout.findViewById(R.id.row_rate_3_imageview)).setSelected(true);        	 
    	 }
    	 else  if(rating == 2)
    	 {
    	   ((ImageView)ratingOptionLayout.findViewById(R.id.row_rate_1_imageview)).setSelected(true);
      	   ((ImageView)ratingOptionLayout.findViewById(R.id.row_rate_2_imageview)).setSelected(true);
      	   ((ImageView)ratingOptionLayout.findViewById(R.id.row_rate_3_imageview)).setSelected(false); 
    		 
    	 }else if(rating == 1)
    	 {
    		  ((ImageView)ratingOptionLayout.findViewById(R.id.row_rate_1_imageview)).setSelected(true);
         	   ((ImageView)ratingOptionLayout.findViewById(R.id.row_rate_2_imageview)).setSelected(false);
         	   ((ImageView)ratingOptionLayout.findViewById(R.id.row_rate_3_imageview)).setSelected(false); 
    	 }
    	 else
    	 {
    		  ((ImageView)ratingOptionLayout.findViewById(R.id.row_rate_1_imageview)).setSelected(false);
        	  ((ImageView)ratingOptionLayout.findViewById(R.id.row_rate_2_imageview)).setSelected(false);
        	  ((ImageView)ratingOptionLayout.findViewById(R.id.row_rate_3_imageview)).setSelected(false); 
    	 }
	}
	
	private class RatingQuestionHolder
	{
		TextView mRatingQuestionTextview;
		LinearLayout mRatingOptionsLayout;
		
    }

}

