package com.ust.leadsv2.androidlogin;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by JMB on 4/6/2018.
 */

public class ListAdapterClass extends BaseAdapter{
    Context context;
    List<Logs> valueList;

    public ListAdapterClass(List<Logs> listValue, Context context)
    {
        this.context = context;
        this.valueList = listValue;
    }

    public int getCount()
    {
        return this.valueList.size();
    }

    public Object getItem(int position)
    {
        return this.valueList.get(position);
    }

    public long getItemId(int position)
    {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewItem viewItem = null;

        if(convertView == null)
        {
            viewItem = new ViewItem();

            LayoutInflater layoutInfiater = (LayoutInflater)this.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInfiater.inflate(R.layout.layout_items, parent, false);

            viewItem.TextViewSubjectName = (TextView)convertView.findViewById(R.id.textView1);

            convertView.setTag(viewItem);
        }
        else
        {
            viewItem = (ViewItem) convertView.getTag();
        }

        viewItem.TextViewSubjectName.setText(valueList.get(position).location);

        return convertView;
    }
}

class ViewItem
{
    TextView TextViewSubjectName;

}

