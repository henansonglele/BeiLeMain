package com.dangdang.gx.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.bumptech.glide.Glide;
import com.dangdang.gx.R;
import com.dangdang.gx.ui.utils.LaunchUtils;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);

        textView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                LaunchUtils.launchStoreNormalHtmlActivity(getActivity(),"www.baidu.com","百度");
            }
        });

        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });


        //glide 展示图片
        ImageView imageView = getActivity().findViewById(R.id.image);
        String url = "https://pics1.baidu.com/feed/267f9e2f07082838a012520a33efbd094c08f114.jpeg?token=2213ac292dc9eff3e88a0b0c99253c12&s=738243AE40511BDC04236AB70300500A";
        Glide.with(getActivity().getApplicationContext()).load(url).into(imageView);
        return root;
    }
}