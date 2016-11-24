package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ImageLoaderUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.jpush.im.android.api.content.MessageContent;
import cn.jpush.im.android.api.enums.ContentType;
import cn.jpush.im.android.api.enums.MessageDirect;
import cn.jpush.im.android.api.model.Message;

/**
 * 聊天界面adapter
 */
public class ChatAdapter extends ArrayAdapter<Message> {

    private final String TAG = "ChatAdapter";

    private int resourceId;
    private View view;
    private ViewHolder viewHolder;

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    public ChatAdapter(Context context, int resource, List<Message> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView != null) {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        } else {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.leftMessage = (RelativeLayout) view.findViewById(R.id.leftMessage);
            viewHolder.rightMessage = (RelativeLayout) view.findViewById(R.id.rightMessage);
            viewHolder.leftPanel = (RelativeLayout) view.findViewById(R.id.leftPanel);
            viewHolder.rightPanel = (RelativeLayout) view.findViewById(R.id.rightPanel);
            viewHolder.sending = (ProgressBar) view.findViewById(R.id.sending);
            viewHolder.error = (ImageView) view.findViewById(R.id.sendError);
            viewHolder.sender = (TextView) view.findViewById(R.id.sender);
            viewHolder.rightDesc = (TextView) view.findViewById(R.id.rightDesc);
            viewHolder.systemMessage = (TextView) view.findViewById(R.id.systemMessage);
            view.setTag(viewHolder);
        }
        if (position < getCount()) {
            final Message data = getItem(position);
            MessageContent mc = data.getContent();
            //Log.e(TAG, "getView: " + mc);
            if (mc.getContentType() == ContentType.text) {
                try {
                    JSONObject js = new JSONObject(data.getContent().toJson());
                    // Log.e(TAG, "getView: " + js.getString("text"));
                    TextView tv = new TextView(getContext());
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    tv.setLayoutParams(layoutParams);
                    tv.setText(js.getString("text"));
                    tv.setGravity(Gravity.CENTER_HORIZONTAL);
                    viewHolder.sending.setVisibility(View.GONE);
                    if (data.getDirect() == MessageDirect.send) {
                        viewHolder.rightPanel.setVisibility(View.VISIBLE);
                        viewHolder.rightMessage.setVisibility(View.VISIBLE);
                        viewHolder.leftPanel.setVisibility(View.GONE);
                        viewHolder.leftMessage.setVisibility(View.GONE);
                        viewHolder.rightMessage.addView(tv);
                    } else if (data.getDirect() == MessageDirect.receive) {
                        viewHolder.leftPanel.setVisibility(View.VISIBLE);
                        viewHolder.leftMessage.setVisibility(View.VISIBLE);
                        viewHolder.rightPanel.setVisibility(View.GONE);
                        viewHolder.rightMessage.setVisibility(View.GONE);
                        viewHolder.leftMessage.addView(tv);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (mc.getContentType() == ContentType.image) {
                try {
                    JSONObject js = new JSONObject(data.getContent().toJson());
                    Log.e(TAG, "getView: " + js);
                    ImageView iv = new ImageView(getContext());
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        ImageLoaderUtil.loadImage(iv,js.getString("localThumbnailPath"));
                    iv.setLayoutParams(layoutParams);
                    viewHolder.sending.setVisibility(View.GONE);
                    if (data.getDirect() == MessageDirect.send) {
                        viewHolder.rightPanel.setVisibility(View.VISIBLE);
                        viewHolder.rightMessage.setVisibility(View.VISIBLE);
                        viewHolder.leftPanel.setVisibility(View.GONE);
                        viewHolder.leftMessage.setVisibility(View.GONE);
                        viewHolder.rightMessage.addView(iv);
                    } else if (data.getDirect() == MessageDirect.receive) {
                        viewHolder.leftPanel.setVisibility(View.VISIBLE);
                        viewHolder.leftMessage.setVisibility(View.VISIBLE);
                        viewHolder.rightPanel.setVisibility(View.GONE);
                        viewHolder.rightMessage.setVisibility(View.GONE);
                        viewHolder.leftMessage.addView(iv);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
        return view;
    }


    public class ViewHolder {
        public RelativeLayout leftMessage;
        public RelativeLayout rightMessage;
        public RelativeLayout leftPanel;
        public RelativeLayout rightPanel;
        public ProgressBar sending;
        public ImageView error;
        public TextView sender;
        public TextView systemMessage;
        public TextView rightDesc;
    }
}
