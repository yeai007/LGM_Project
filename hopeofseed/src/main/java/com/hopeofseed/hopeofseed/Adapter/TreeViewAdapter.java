package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.Activitys.DistributorCountReportActivity;
import com.hopeofseed.hopeofseed.Activitys.DistributorListForReport;
import com.hopeofseed.hopeofseed.Activitys.MyCommodity;
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
    Context cContext;

    public TreeViewAdapter(Context context, ArrayList<TreeNode> topNodes, ArrayList<TreeNode> allNodes, LayoutInflater inflater) {
        this.topNodes = topNodes;
        this.allNodes = allNodes;
        this.inflater = inflater;
        indentionBase = 20;
        cContext = context;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
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
        holder.treeText.setPadding(indentionBase * (level + 1),
                holder.treeText.getPaddingTop(),
                holder.treeText.getPaddingRight(),
                holder.treeText.getPaddingBottom());
        holder.treeText.setText(element.getContentText());
        if (element.isHasChildren() && !element.isExpanded()) {
            holder.homeImg.setImageResource(R.drawable.img_zhankai);
            //这里要主动设置一下icon可见，因为convertView有可能是重用了"设置了不可见"的view，下同。
            holder.homeImg.setVisibility(View.VISIBLE);
        } else if (element.isHasChildren() && element.isExpanded()) {
            holder.homeImg.setImageResource(R.drawable.img_heqi);
            holder.homeImg.setVisibility(View.VISIBLE);
        } else if (!element.isHasChildren()) {
            holder.homeImg.setImageResource(R.drawable.img_zhankai);
            holder.homeImg.setVisibility(View.INVISIBLE);
        }
        holder.treeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(cContext, DistributorListForReport.class);
                intent.putExtra("Class", 1);
                intent.putExtra("AreaId", ((TreeNode) getItem(position)).getId());
                intent.putExtra("Condition", ((DistributorCountReportActivity) cContext).GetCondition());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                cContext.startActivity(intent);
            }
        });
        holder.homeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击的item代表的元素
                TreeNode treeNode = (TreeNode) getItem(position);
                //树中顶层的元素
                ArrayList<TreeNode> topNodes = getTopNodes();
                //元素的数据源
                ArrayList<TreeNode> allNodes = getAllNodes();
                //点击没有子项的item直接返回
                if (!treeNode.isHasChildren()) {
                    return;
                }

                if (treeNode.isExpanded()) {
                    treeNode.setExpanded(false);
                    //删除节点内部对应子节点数据，包括子节点的子节点...
                    ArrayList<TreeNode> elementsToDel = new ArrayList<TreeNode>();
                    for (int i = position + 1; i < topNodes.size(); i++) {
                        if (treeNode.getLevel() >= topNodes.get(i).getLevel())
                            break;
                        elementsToDel.add(topNodes.get(i));
                    }

                    topNodes.removeAll(elementsToDel);
                    notifyDataSetChanged();
                } else {
                    treeNode.setExpanded(true);
                    //从数据源中提取子节点数据添加进树，注意这里只是添加了下一级子节点，为了简化逻辑
                    int i = 1;//注意这里的计数器放在for外面才能保证计数有效
                    for (TreeNode e : allNodes) {
                        if (e.getParendId() == treeNode.getId()) {
                            e.setExpanded(false);
                            topNodes.add(position + i, e);
                            i++;
                        }
                    }

                    notifyDataSetChanged();
                }
            }
        });


        return convertView;
    }

    static class ViewHolder {
        ImageView homeImg;
        TextView treeText;
    }
}