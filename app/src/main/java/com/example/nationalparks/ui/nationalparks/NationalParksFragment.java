package com.example.nationalparks.ui.nationalparks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nationalparks.R;
import com.example.nationalparks.databinding.FragmentNationalparksBinding;
import com.example.nationalparks.databinding.ItemNationalparkBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class NationalParksFragment extends Fragment {
    private NationalParksViewModel nationalParksViewModel;
    private FragmentNationalparksBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        nationalParksViewModel = new ViewModelProvider(this).get(NationalParksViewModel.class);
        binding = FragmentNationalparksBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = binding.recyclerviewNationalParks;
        ListAdapter<JSONObject, NationalParksViewHolder> adapter = new NationalParksAdapter();
        recyclerView.setAdapter(adapter);

        nationalParksViewModel.getTexts().observe(getViewLifecycleOwner(), adapter::submitList);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private static class NationalParksAdapter extends ListAdapter<JSONObject, NationalParksFragment.NationalParksViewHolder> {

        private final List<Integer> drawables = Arrays.asList(
                R.drawable.avatar_1,
                R.drawable.avatar_2,
                R.drawable.avatar_3,
                R.drawable.avatar_4,
                R.drawable.avatar_5,
                R.drawable.avatar_6,
                R.drawable.avatar_7,
                R.drawable.avatar_8,
                R.drawable.avatar_9,
                R.drawable.avatar_10,
                R.drawable.avatar_11,
                R.drawable.avatar_12,
                R.drawable.avatar_13,
                R.drawable.avatar_14,
                R.drawable.avatar_15,
                R.drawable.avatar_16);

        protected NationalParksAdapter() {
            super(new DiffUtil.ItemCallback<JSONObject>() {
                @Override
                public boolean areItemsTheSame(@NonNull JSONObject oldItem, @NonNull JSONObject newItem) {
                    try {
                        return oldItem.getInt("id") == newItem.getInt("id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return false;
                }

                @Override
                public boolean areContentsTheSame(@NonNull JSONObject oldItem, @NonNull JSONObject newItem) {
                    return oldItem.toString().equals(newItem.toString());
                }
            });
        }

        @NonNull
        @Override
        public NationalParksFragment.NationalParksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemNationalparkBinding binding = ItemNationalparkBinding.inflate(LayoutInflater.from(parent.getContext()));
            return new NationalParksViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull NationalParksFragment.NationalParksViewHolder holder, int position) {
            JSONObject item = getItem(position);
            String name = "";
            String address = "";

            try {
                name = item.getString("location_name");
                address = item.getString("address") + ", " + item.getString("city") + ", " + item.getString("state") + " " +item.getString("zip_code");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            holder.tvNationalParkName.setText(name);
            holder.tvNationalParkAddress.setText(address);
            holder.imageView.setImageDrawable(
                    ResourcesCompat.getDrawable(holder.imageView.getResources(),
                            drawables.get(position % drawables.size()),
                            null));
        }
    }

    private static class NationalParksViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView;
        private final TextView tvNationalParkName;
        private final TextView tvNationalParkAddress;

        public NationalParksViewHolder(ItemNationalparkBinding binding) {
            super(binding.getRoot());
            imageView = binding.imageViewItemNationalpark;
            tvNationalParkName = binding.textViewItemNationalparkName;
            tvNationalParkAddress = binding.textViewItemNationalparkAddress;
        }
    }
}
