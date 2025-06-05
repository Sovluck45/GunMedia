package com.example.gunmedia.publications;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gunmedia.R;
import com.example.gunmedia.databinding.FragmentPublicationsBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PublicationsFragment extends Fragment implements PublicationAdapter.OnItemClickListener {

    private FragmentPublicationsBinding binding;
    private PublicationAdapter adapter;
    private final List<Publication> publications = new ArrayList<>();
    private ValueEventListener publicationsListener;
    private DatabaseReference publicationsRef;
    private TextView tvTitle;
    private String currentFilterTag = "";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPublicationsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Инициализация элементов UI
        tvTitle = binding.tvTitle;

        // Настройка RecyclerView
        adapter = new PublicationAdapter(publications, this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setNestedScrollingEnabled(false); // Важно для корректной прокрутки

        // Загрузка данных
        loadPublications();

        // Обработчик кнопки фильтра
        binding.btnFilter.setOnClickListener(v -> showFilterBottomSheet());
    }

    private void loadPublications() {
        // Удаляем предыдущий слушатель
        if (publicationsListener != null && publicationsRef != null) {
            publicationsRef.removeEventListener(publicationsListener);
        }

        // Инициализация Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance(
                "https://gunmedia-de381-default-rtdb.europe-west1.firebasedatabase.app"
        );
        publicationsRef = database.getReference("publications");

        publicationsListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                publications.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Publication publication = dataSnapshot.getValue(Publication.class);
                    if (publication != null) {
                        publication.setId(dataSnapshot.getKey());

                        // Если активен фильтр - проверяем тег
                        if (currentFilterTag.isEmpty() ||
                                (publication.getTags() != null && publication.getTags().containsKey(currentFilterTag))) {
                            publications.add(publication);
                        }
                    }
                }
                Collections.reverse(publications);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error loading publications", error.toException());
            }
        };

        publicationsRef.orderByChild("timestamp").addValueEventListener(publicationsListener);
    }

    private void showFilterBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_filter, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        // Обработчики для основных категорий
        bottomSheetView.findViewById(R.id.all_publications).setOnClickListener(v -> {
            resetFilter();
            bottomSheetDialog.dismiss();
        });

        bottomSheetView.findViewById(R.id.news).setOnClickListener(v -> {
            applyFilter("Новости");
            bottomSheetDialog.dismiss();
        });

        // Обработчики для тегов
        bottomSheetView.findViewById(R.id.tag_sport).setOnClickListener(v -> {
            applyFilter("Спорт");
            bottomSheetDialog.dismiss();
        });

        bottomSheetView.findViewById(R.id.tag_hunting).setOnClickListener(v -> {
            applyFilter("Охота");
            bottomSheetDialog.dismiss();
        });

        bottomSheetView.findViewById(R.id.tag_useful).setOnClickListener(v -> {
            applyFilter("Полезно знать");
            bottomSheetDialog.dismiss();
        });

        bottomSheetView.findViewById(R.id.tag_history).setOnClickListener(v -> {
            applyFilter("История");
            bottomSheetDialog.dismiss();
        });

        bottomSheetView.findViewById(R.id.tag_tech).setOnClickListener(v -> {
            applyFilter("Технологии");
            bottomSheetDialog.dismiss();
        });

        bottomSheetView.findViewById(R.id.tag_law).setOnClickListener(v -> {
            applyFilter("По закону");
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }

    private void resetFilter() {
        currentFilterTag = "";
        tvTitle.setText("Все публикации");
        loadPublications();
    }

    private void applyFilter(String tag) {
        currentFilterTag = tag;
        tvTitle.setText(tag);
        loadPublications();
    }

    @Override
    public void onReadClick(String publicationId) {
        ArticleFragment articleFragment = ArticleFragment.newInstance(publicationId);

        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment, articleFragment)
                .addToBackStack("publications")
                .commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (publicationsRef != null && publicationsListener != null) {
            publicationsRef.removeEventListener(publicationsListener);
        }
        binding = null;
    }
}