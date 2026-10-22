package com.github.tvbox.osc.ui.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.github.tvbox.osc.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SelectDialogAdapter<T> extends ListAdapter<T, SelectDialogAdapter.SelectViewHolder> {

    class SelectViewHolder extends RecyclerView.ViewHolder {
        public SelectViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }
    }

    public interface SelectDialogInterface<T> {
        void click(T value, int pos);

        String getDisplay(T val);
    }

    public static DiffUtil.ItemCallback<String> stringDiff = new DiffUtil.ItemCallback<String>() {
        @Override
        public boolean areItemsTheSame(@NonNull @NotNull String oldItem, @NonNull @NotNull String newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull @NotNull String oldItem, @NonNull @NotNull String newItem) {
            return oldItem.equals(newItem);
        }
    };

    private ArrayList<T> data = new ArrayList<>();
    private int select = 0;
    private SelectDialogInterface dialogInterface;

    public SelectDialogAdapter(SelectDialogInterface dialogInterface, DiffUtil.ItemCallback diffCallback) {
        super(diffCallback);
        this.dialogInterface = dialogInterface;
    }

    public void setData(List<T> newData, int defaultSelect) {
        data.clear();
        data.addAll(newData);
        select = defaultSelect;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public SelectDialogAdapter.SelectViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new SelectDialogAdapter.SelectViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dialog_select, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SelectDialogAdapter.SelectViewHolder holder, @SuppressLint("RecyclerView") int position) {
        T value = data.get(position);
        String name = dialogInterface.getDisplay(value);
        TextView view = holder.itemView.findViewById(R.id.tvName);

        // 1. 初始化当前项的文字状态（默认未聚焦时的状态）
        if (position == select) {
            view.setTextColor(0xff02f8e1); // 当前选中的项：青色
            view.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        } else {
            view.setTextColor(Color.WHITE); // 普通项：白色
            view.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }
        view.setText(name);

        // 2. 【新增】添加焦点监听，处理遥控器移动时的效果
        view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 当获得焦点时（配合 XML 里的亮蓝色背景）
                    // 文字统一变纯白加粗，对比度最高
                    view.setTextColor(Color.WHITE);
                    view.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                } else {
                    // 当失去焦点时，恢复原本的逻辑
                    if (position == select) {
                        view.setTextColor(0xff02f8e1);
                        view.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    } else {
                        view.setTextColor(Color.WHITE);
                        view.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    }
                }
            }
        });

        // 3. 点击事件保持不变
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == select)
                    return;
                notifyItemChanged(select);
                select = position;
                notifyItemChanged(select);
                dialogInterface.click(value, position);
            }
        });
    }
}
