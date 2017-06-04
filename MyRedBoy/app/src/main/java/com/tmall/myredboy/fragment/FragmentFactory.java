package com.tmall.myredboy.fragment;

import android.support.v4.app.Fragment;

import java.util.HashMap;

public class FragmentFactory {

    private static HashMap<Integer, Fragment> savedFragment = new HashMap<Integer, Fragment>();

    public static Fragment getFragment(int position) {
        Fragment fragment = savedFragment.get(position);
        if (fragment == null) {
            switch (position) {
                case 0:
                    fragment = new HomeFragment();
                    break;
                case 1:
                    fragment = new SearchFragment();
                    break;
                case 2:
                    fragment = new CategoryFragment();
                    break;
                case 3:
                    fragment = new ShoppingCarFragment();
                    break;
                case 4:
                    fragment = new MoreFragment();
                    break;

            }
            savedFragment.put(position, fragment);
        }

        return fragment;
    }

}
