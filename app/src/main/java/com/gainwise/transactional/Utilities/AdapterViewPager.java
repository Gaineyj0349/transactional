package com.gainwise.transactional.Utilities;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.gainwise.transactional.Fragments.FragPurchase;
import com.gainwise.transactional.Fragments.FragStatistics;
import com.gainwise.transactional.Fragments.FragTransactions;

public class AdapterViewPager extends FragmentPagerAdapter {

    private Context mContext;

    public AdapterViewPager(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
      //  if (position == 0) {
       //     return new FragQuickLinks();
     //   } else
            if (position == 1){
            return new FragTransactions();
        } else if (position == 0){
            return new FragPurchase();
        }else {
            return new FragStatistics();
        }
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 3;
    }
    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
      //      case 0:
        //        return "HOME";
            case 0:
                return "TRANSACTION";
            case 1:
                return "RECENT HISTORY";
            case 2:
                return "STATISTICS";
            default:
                return null;
        }
    }
    }

