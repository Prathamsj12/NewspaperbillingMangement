package com.example.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.R;
import com.example.data.model.Newspaper;

import java.util.ArrayList;
import java.util.List;

public class NewspaperAdapter extends RecyclerView.Adapter<NewspaperAdapter.NewspaperViewHolder> {

    public interface OnNewspaperActionListener {
        void onEdit(Newspaper newspaper);
        void onDelete(Newspaper newspaper);
    }

    private List<Newspaper> newspapers = new ArrayList<>();
    private final OnNewspaperActionListener listener;

    public NewspaperAdapter(OnNewspaperActionListener listener) {
        this.listener = listener;
    }

    public void setNewspapers(List<Newspaper> newspapers) {
        this.newspapers = newspapers != null ? newspapers : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NewspaperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_newspaper, parent, false);
        return new NewspaperViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NewspaperViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        Newspaper paper = newspapers.get(position);

        holder.tvPaperName.setText(paper.getName());
        holder.tvPaperCode.setText(paper.getCode());
        holder.tvPaperLanguage.setText("Language: " + paper.getLanguage());
        holder.tvPaperPrice.setText(paper.getFormattedPrice());

        if (paper.isActive()) {
            holder.tvPaperBadge.setText("ACTIVE");
            holder.tvPaperBadge.setBackgroundResource(R.drawable.bg_badge_active);
            holder.tvPaperBadge.setTextColor(ContextCompat.getColor(context, R.color.badge_active_text));
        } else {
            holder.tvPaperBadge.setText("INACTIVE");
            holder.tvPaperBadge.setBackgroundResource(R.drawable.bg_badge_inactive);
            holder.tvPaperBadge.setTextColor(ContextCompat.getColor(context, R.color.badge_inactive_text));
        }

        holder.btnEditPaper.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(paper);
        });

        holder.btnDeletePaper.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(paper);
        });
    }

    @Override
    public int getItemCount() {
        return newspapers.size();
    }

    static class NewspaperViewHolder extends RecyclerView.ViewHolder {
        TextView tvPaperName, tvPaperBadge, tvPaperCode, tvPaperLanguage, tvPaperPrice;
        ImageView btnEditPaper, btnDeletePaper;

        public NewspaperViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPaperName = itemView.findViewById(R.id.tvPaperName);
            tvPaperBadge = itemView.findViewById(R.id.tvPaperBadge);
            tvPaperCode = itemView.findViewById(R.id.tvPaperCode);
            tvPaperLanguage = itemView.findViewById(R.id.tvPaperLanguage);
            tvPaperPrice = itemView.findViewById(R.id.tvPaperPrice);
            btnEditPaper = itemView.findViewById(R.id.btnEditPaper);
            btnDeletePaper = itemView.findViewById(R.id.btnDeletePaper);
        }
    }
}
