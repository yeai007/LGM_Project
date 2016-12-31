package com.hopeofseed.hopeofseed.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.R;
import com.hopeofseed.hopeofseed.curView.TreeNode;

import java.util.ArrayList;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/12/30 19:21
 * 修改人：whisper
 * 修改时间：2016/12/30 19:21
 * 修改备注：
 */
public class TreeViewAdapter extends BaseAdapter {
    /**
     * 所有的数据集合
     */
    private ArrayList<TreeNode> allNodes;
    /**
     * 顶层元素结合
     */
    private ArrayList<TreeNode> topNodes;
    /**
     * LayoutInflater
     */
    private LayoutInflater inflater;
    /**
     * item的行首缩进基数
     */
    private int indentionBase;

    public TreeViewAdapter(ArrayList<TreeNode> topNodes, ArrayList<TreeNode> allNodes, LayoutInflater inflater) {
        this.topNodes = topNodes;
        this.allNodes = allNodes;
        this.inflater = inflater;
        indentionBase = 20;
    }

    public ArrayList<TreeNode> getTopNodes() {
        return topNodes;
    }

    public ArrayList<TreeNode> getAllNodes() {
        return allNodes;
    }

    @Override
    public int getCount() {
        return topNodes.size();
    }

    @Override
    public Object getItem(int position) {
        return topNodes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.tree_item, null);
            holder.homeImg = (ImageView) convertView.findViewById(R.id.homeImg);
            holder.treeText = (TextView) convertView.findViewById(R.id.treeText);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TreeNode element = topNodes.get(position);
        int level = element.getLevel();
        holder.homeImg.setPadding(
                indentionBase * (level + 1),
                holder.homeImg.getPaddingTop(),
                holder.homeImg.getPaddingRight(),
                holder.homeImg.getPaddingBottom());
        holder.treeText.setText(element.getContentText());
        if (element.isHasChildren() && !element.isExpanded()) {
            holder.homeImg.setImageResource(R.drawable.img_group_default);
            //这里要主动设置一下icon可见，因为convertView有可能是重用了"设置了不可见"的view，下同。
            holder.homeImg.setVisibility(View.VISIBLE);
        } else if (element.isHasChildren() && element.isExpanded()) {
            holder.homeImg.setImageResource(R.drawable.img_group_default);
            holder.homeImg.setVisibility(View.VISIBLE);
        } else if (!element.isHasChildren()) {
            holder.homeImg.setImageResource(R.drawable.img_group_default);
            holder.homeImg.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    static class ViewHolder {
        ImageView homeImg;
        TextView treeText;
    }
}