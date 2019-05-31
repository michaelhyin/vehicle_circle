package net.oschina.app.chatkit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.avos.avoscloud.im.v2.AVIMChatRoom;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;

import net.oschina.app.R;
import net.oschina.app.Setting;
import net.oschina.app.bean.SimpleBackPage;
import net.oschina.app.improve.account.AccountHelper;
import net.oschina.app.improve.account.activity.LoginActivity;
import net.oschina.app.improve.base.activities.BaseActivity;
import net.oschina.app.improve.base.fragments.BaseFragment;
import net.oschina.app.improve.bean.SubTab;
import net.oschina.app.improve.bean.User;
import net.oschina.app.improve.detail.SubActivity;
import net.oschina.app.improve.git.feature.FeatureActivity;
import net.oschina.app.improve.git.gist.GistActivity;
import net.oschina.app.improve.main.discover.ShakePresentActivity;
import net.oschina.app.improve.main.dudu.DuduFragment;
import net.oschina.app.improve.main.synthesize.web.ZBWebActivity;
import net.oschina.app.improve.nearby.NearbyActivity;
import net.oschina.app.improve.notice.NoticeManager;
import net.oschina.app.improve.search.v2.SearchActivity;
import net.oschina.app.interf.OnTabReselectListener;
import net.oschina.app.util.UIHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import butterknife.Bind;
import butterknife.OnClick;
import cn.leancloud.chatkit.LCChatKit;
import cn.leancloud.chatkit.LCChatKitUser;
import cn.leancloud.chatkit.activity.LCIMContactFragment;
import cn.leancloud.chatkit.activity.LCIMConversationActivity;
import cn.leancloud.chatkit.activity.LCIMConversationListFragment;
import cn.leancloud.chatkit.utils.LCIMConstants;

/**
 * 发现界面
 * Created by huanghaibin on 2017/10/23.
 */

public class ChatFragment extends BaseFragment {

    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private View rootView;
    public static ChatFragment newInstance() {
        return new ChatFragment();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (AccountHelper.isLogin()) {
            User user = AccountHelper.getUser();
            //updateView(user);
        } else {
            //hideView();
        }
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LCChatKit.getInstance().open(String.valueOf(AccountHelper.getUserId()), new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                if (null == e) {

                } else {
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();

                }
            }
        });

        if(rootView == null)
        {
            rootView = inflater.inflate(R.layout.activity_friends_v2, container, false);
            toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
            viewPager = (ViewPager)rootView.findViewById(R.id.pager);
            tabLayout = (TabLayout)rootView.findViewById(R.id.tablayout);
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
            setHasOptionsMenu(true);
            initTabLayout();
        }
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_square, menu);
        return;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();
        if (menuId == R.id.menu_square_members) {
            gotoSquareConversation();
        } else if (menuId == R.id.menu_quit) {
            LCChatKit.getInstance().close(new AVIMClientCallback() {
                @Override
                public void done(AVIMClient avimClient, AVIMException e) {
                    if (null!= e) {
                        e.printStackTrace();
                    } else {
                        getActivity().finish();
                    }
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_emoji;
    }

    private void initTabLayout() {
        String[] tabList = new String[]{"会话", "联系人"};
        final Fragment[] fragmentList = new Fragment[] {new LCIMConversationListFragment(),
                new LCIMContactFragment()};

        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        for (int i = 0; i < tabList.length; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(tabList[i]));
        }

        TabFragmentAdapter adapter = new TabFragmentAdapter(getActivity().getSupportFragmentManager(),
                Arrays.asList(fragmentList), Arrays.asList(tabList));
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (0 == position) {
//          EventBus.getDefault().post(new ConversationFragmentUpdateEvent());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabsFromPagerAdapter(adapter);
    }
    public class TabFragmentAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> mFragments;
        private List<String> mTitles;

        public TabFragmentAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
            super(fm);
            mFragments = fragments;
            mTitles = titles;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles.get(position);
        }
    }

    private void gotoSquareConversation() {
        List<LCChatKitUser> userList = CustomUserProvider.getInstance().getAllUsers();
        List<String> idList = new ArrayList<>();
        for (LCChatKitUser user : userList) {
            idList.add(user.getUserId());
        }
        LCChatKit.getInstance().getClient().createChatRoom(
                idList, getString(R.string.square), null, true, new AVIMConversationCreatedCallback() {
                    @Override
                    public void done(AVIMConversation avimConversation, AVIMException e) {
                        if (avimConversation instanceof AVIMChatRoom) {
                            Intent intent = new Intent(getActivity(), LCIMConversationActivity.class);
                            intent.putExtra(LCIMConstants.CONVERSATION_ID, avimConversation.getConversationId());
                            startActivity(intent);
                        } else {
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
