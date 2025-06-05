package com.example.gunmedia.publications;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.gunmedia.R;
import com.example.gunmedia.databinding.FragmentArticleBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ArticleFragment extends Fragment {

    private FragmentArticleBinding binding;
    private String publicationId;
    private DatabaseReference databaseRef;
    private int currentCommentCount = 0;
    private ViewTreeObserver.OnScrollChangedListener scrollListener; // Для отслеживания прокрутки

    public static ArticleFragment newInstance(String publicationId) {
        ArticleFragment fragment = new ArticleFragment();
        Bundle args = new Bundle();
        args.putString("publication_id", publicationId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            publicationId = getArguments().getString("publication_id");
        }

        // Инициализация Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance(
                "https://gunmedia-de381-default-rtdb.europe-west1.firebasedatabase.app"
        );
        databaseRef = database.getReference();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentArticleBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadArticleContent();

        // Кнопка "Назад" - исправленная версия
        binding.backButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        // Кнопка "Отправить комментарий"
        binding.sendCommentButton.setOnClickListener(v -> {
            String comment = binding.commentEditText.getText().toString();
            if (!comment.isEmpty()) {
                sendComment(comment);
                binding.commentEditText.setText("");
            }
        });

        // Кнопка "Наверх"
        binding.topButton.setOnClickListener(v -> {
            binding.scrollView.smoothScrollTo(0, 0);
        });

        // Инициализация слушателя прокрутки
        scrollListener = new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                // Проверка на случай, если binding уже уничтожен
                if (binding != null && binding.scrollView != null) {
                    int scrollY = binding.scrollView.getScrollY();
                    if (scrollY > 300) {
                        binding.topButton.setVisibility(View.VISIBLE);
                    } else {
                        binding.topButton.setVisibility(View.GONE);
                    }
                }
            }
        };

        // Регистрация слушателя прокрутки
        binding.scrollView.getViewTreeObserver().addOnScrollChangedListener(scrollListener);
    }

    private void navigateToPublications() {
        // Переключаем на вкладку публикаций в нижней навигации
        BottomNavigationView navView = requireActivity().findViewById(R.id.bottom_navigation);
        navView.setSelectedItemId(R.id.nav_publications);

        // Закрываем текущий фрагмент (статью)
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    private void sendComment(String comment) {
        // Здесь будет код отправки комментария на сервер
        Toast.makeText(requireContext(), "Комментарий отправлен", Toast.LENGTH_SHORT).show();

        // Обновляем счетчик комментариев
        currentCommentCount++;
        binding.commentsCount.setText(formatComments(currentCommentCount));
    }

    private void loadArticleContent() {
        if (publicationId == null || publicationId.isEmpty()) {
            showError("Ошибка загрузки статьи");
            return;
        }

        binding.progressBar.setVisibility(View.VISIBLE);

        databaseRef.child("publications").child(publicationId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        binding.progressBar.setVisibility(View.GONE);

                        if (snapshot.exists()) {
                            // Заголовок
                            String title = snapshot.child("title").getValue(String.class);
                            binding.title.setText(title != null ? title : "");

                            // Контент
                            String content = snapshot.child("full_content").getValue(String.class);
                            if (content != null && !content.isEmpty()) {
                                binding.content.setText(content);
                                binding.content.setVisibility(View.VISIBLE);
                            } else {
                                binding.content.setVisibility(View.GONE);
                            }

                            // Комментарии
                            int commentCount = 0;
                            if (snapshot.hasChild("comment_count")) {
                                Object commentCountObj = snapshot.child("comment_count").getValue();
                                if (commentCountObj instanceof Long) {
                                    commentCount = ((Long) commentCountObj).intValue();
                                } else if (commentCountObj instanceof Integer) {
                                    commentCount = (Integer) commentCountObj;
                                }
                            }
                            currentCommentCount = commentCount;
                            binding.commentsCount.setText(formatComments(commentCount));

                            // Мета-информация (автор и дата)
                            String author = snapshot.child("author/name").getValue(String.class);
                            Long timestamp = snapshot.child("timestamp").getValue(Long.class);

                            if (author != null || timestamp != null) {
                                String metaText = "";
                                if (author != null) {
                                    metaText += "Автор: " + author;
                                }
                                if (timestamp != null) {
                                    Date date = new Date(timestamp);
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                                    metaText += (author != null ? " • " : "") + sdf.format(date);
                                }
                                binding.metaInfo.setText(metaText);
                                binding.metaInfo.setVisibility(View.VISIBLE);
                            } else {
                                binding.metaInfo.setVisibility(View.GONE);
                            }

                            // Изображение (если есть)
                            String imageUrl = snapshot.child("image_url").getValue(String.class);
                            if (imageUrl != null && !imageUrl.isEmpty()) {
                                Glide.with(requireContext())
                                        .load(imageUrl)
                                        .into(binding.articleImage);
                                binding.articleImage.setVisibility(View.VISIBLE);
                            } else {
                                binding.articleImage.setVisibility(View.GONE);
                            }

                        } else {
                            showError("Статья не найдена");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        binding.progressBar.setVisibility(View.GONE);
                        showError("Ошибка загрузки: " + error.getMessage());
                    }
                });
    }

    private String formatComments(int count) {
        if (count % 10 == 1 && count % 100 != 11) {
            return count + " комментарий";
        } else if (count % 10 >= 2 && count % 10 <= 4 &&
                (count % 100 < 10 || count % 100 >= 20)) {
            return count + " комментария";
        } else {
            return count + " комментариев";
        }
    }

    private void showError(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
        binding.errorText.setText(message);
        binding.errorText.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        // 1. Удаляем слушатель прокрутки
        if (binding != null && binding.scrollView != null && scrollListener != null) {
            binding.scrollView.getViewTreeObserver().removeOnScrollChangedListener(scrollListener);
        }

        // 2. Скрываем клавиатуру
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && binding != null && binding.commentEditText != null) {
            imm.hideSoftInputFromWindow(binding.commentEditText.getWindowToken(), 0);
        }

        // 3. Освобождаем ресурсы
        super.onDestroyView();
        binding = null;
    }
}