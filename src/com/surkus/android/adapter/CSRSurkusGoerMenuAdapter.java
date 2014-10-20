package com.surkus.android.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.surkus.android.R;

public class CSRSurkusGoerMenuAdapter  extends BaseAdapter{

	List<String> list;
	private LayoutInflater inflator = null;
	private String[] surkusGoerMenuList;
		 
	public CSRSurkusGoerMenuAdapter(Context activity){
		surkusGoerMenuList = activity.getResources().getStringArray(R.array.surkusgoer_menu);
		inflator = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		return surkusGoerMenuList.length;
	}

	@Override
	public Object getItem(int arg0) {
		return surkusGoerMenuList[arg0];
	}

	@Override
	public long getItemId(int position) {
		return surkusGoerMenuList[position].hashCode();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
        if (vi == null) {
            vi = inflator.inflate(R.layout.menu_list_item, null);
        }
        
        TextView surkusGoerMenuItem = (TextView) vi.findViewById(R.id.menu_item_textview);
        surkusGoerMenuItem.setText(surkusGoerMenuList[position]);
      return vi;
	}

}

