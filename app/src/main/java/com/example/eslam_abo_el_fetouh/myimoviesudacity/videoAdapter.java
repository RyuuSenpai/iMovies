package com.example.eslam_abo_el_fetouh.myimoviesudacity;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import com.example.eslam_abo_el_fetouh.myimoviesudacity.contentprovider.Contract;

public class videoAdapter extends ResourceCursorAdapter {

    public videoAdapter(Context context, int layout, Cursor c, int flags) {
        super(context, layout, c, flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView title = (TextView) view.findViewById(R.id.text1);
        title.setText(cursor.getString(cursor.getColumnIndex(Contract.VideoEntry.COLUMN_NAME)));

    }
}
