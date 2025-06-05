package com.example.gunmedia.publications;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gunmedia.R;
import com.google.android.material.card.MaterialCardView; // Добавлен импорт

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PublicationAdapter extends RecyclerView.Adapter<PublicationAdapter.PublicationViewHolder> {

    private final List<Publication> publications;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onReadClick(String publicationId);
    }

    public PublicationAdapter(List<Publication> publications, OnItemClickListener listener) {
        this.publications = publications;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PublicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_publication, parent, false);
        return new PublicationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PublicationViewHolder holder, int position) {
        Publication publication = publications.get(position);
        holder.bind(publication, listener);
    }

    @Override
    public int getItemCount() {
        return publications.size();
    }

    static class PublicationViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTextView;
        private final TextView descriptionTextView;
        private final Button readButton;
        private final TextView commentsTextView;
        private final MaterialCardView cardView; // Исправлено
        private final TextView categoryTextView;
        private final TextView metaInfoTextView;

        private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

        public PublicationViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            titleTextView = itemView.findViewById(R.id.title);
            descriptionTextView = itemView.findViewById(R.id.description);
            readButton = itemView.findViewById(R.id.btn_read);
            commentsTextView = itemView.findViewById(R.id.comments_count);
            categoryTextView = itemView.findViewById(R.id.category);
            metaInfoTextView = itemView.findViewById(R.id.meta_info);
        }

        public void bind(Publication publication, OnItemClickListener listener) {
            titleTextView.setText(publication.getTitle());

            // Мета-информация: автор и дата
            StringBuilder metaText = new StringBuilder(); // Исправлено имя переменной

            // Автор
            if (publication.getAuthor() != null && publication.getAuthor().getName() != null) {
                metaText.append("Автор: ").append(publication.getAuthor().getName());
            }

            // Дата
            if (publication.getTimestamp() > 0) {
                if (metaText.length() > 0) metaText.append(" • "); // Исправлен разделитель
                metaText.append(sdf.format(new Date(publication.getTimestamp())));
            }

            metaInfoTextView.setText(metaText.toString());
            metaInfoTextView.setVisibility(metaText.length() > 0 ? View.VISIBLE : View.GONE);

            // Краткое описание
            if (publication.getShortDescription() != null && !publication.getShortDescription().isEmpty()) {
                descriptionTextView.setText(publication.getShortDescription());
                descriptionTextView.setVisibility(View.VISIBLE);
            } else {
                descriptionTextView.setVisibility(View.GONE);
            }

            // Комментарии
            int commentCount = publication.getCommentCount();
            String commentsText;

            if (commentCount == 0) {
                commentsText = "Нет комментариев";
            } else {
                commentsText = formatComments(commentCount);
            }

            commentsTextView.setText(commentsText);

            // Категория/теги
            if (publication.getCategory() != null && !publication.getCategory().isEmpty()) {
                categoryTextView.setText(publication.getCategory());
                categoryTextView.setVisibility(View.VISIBLE);
            } else {
                categoryTextView.setVisibility(View.GONE);
            }

            readButton.setOnClickListener(v -> listener.onReadClick(publication.getId()));
        }

        private String formatComments(int count) {
            int lastDigit = count % 10;
            int lastTwoDigits = count % 100;

            if (lastTwoDigits >= 11 && lastTwoDigits <= 19) {
                return count + " комментариев";
            } else if (lastDigit == 1) {
                return count + " комментарий";
            } else if (lastDigit >= 2 && lastDigit <= 4) {
                return count + " комментария";
            } else {
                return count + " комментариев";
            }
        }
    }
}