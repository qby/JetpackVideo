package com.baronqi.jetpackvideo.ui.sofa;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.baronqi.libannotation.FragmentDestination;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;


@FragmentDestination(path = "main/tab/sofa", isDefault = false)
public class SofaFragment extends Fragment {
    protected ViewPager2 viewPager2;
    protected TabLayout tabLayout;
    private TabLayoutMediator mediator;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        return new View(getContext());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            if (fragment.isAdded() && fragment.isVisible()) {
                fragment.onHiddenChanged(hidden);
                break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}