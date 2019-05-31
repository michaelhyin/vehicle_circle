package net.oschina.app.chatkit;

import java.util.ArrayList;
import java.util.List;

import cn.leancloud.chatkit.LCChatKitUser;
import cn.leancloud.chatkit.LCChatProfileProvider;
import cn.leancloud.chatkit.LCChatProfilesCallBack;

/**
 * Created by wli on 15/12/4.
 * 实现自定义用户体系
 */
public class CustomUserProvider implements LCChatProfileProvider {

  private static CustomUserProvider customUserProvider;

  public synchronized static CustomUserProvider getInstance() {
    if (null == customUserProvider) {
      customUserProvider = new CustomUserProvider();
    }
    return customUserProvider;
  }

  private CustomUserProvider() {
  }

  private static List<LCChatKitUser> partUsers = new ArrayList<LCChatKitUser>();

  // 此数据均为 fake，仅供参考
  static {
    partUsers.add(new LCChatKitUser("随风", "随风", "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3702304829,2844936810&fm=26&gp=0.jpg"));
    partUsers.add(new LCChatKitUser("王依依", "王依依", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555868412498&di=2f9554cdabcf1096c06188d124d3e901&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201710%2F02%2F20171002181805_y2jPZ.jpeg"));
    partUsers.add(new LCChatKitUser("旺仔", "旺仔", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555868144895&di=b91d2803c66d4f71ac17ff02ee6b4440&imgtype=0&src=http%3A%2F%2F5b0988e595225.cdn.sohucs.com%2Fimages%2F20181013%2F92d246de9f0b464985dc4574673bb409.jpeg"));
    partUsers.add(new LCChatKitUser("Cat", "Cat", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1556462893&di=ff91450c3b73bd9ea1ee19315bb015e6&imgtype=jpg&er=1&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201801%2F23%2F20180123214101_gclhp.jpeg"));
    partUsers.add(new LCChatKitUser("肖刚", "肖刚", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555868210087&di=8333c1e2cc6fcbb574e8ef95168db6ed&imgtype=0&src=http%3A%2F%2Fdownhdlogo.yy.com%2Fhdlogo%2F640640%2F640%2F640%2F05%2F1314050887%2Fu1314050887vqKjBOu.jpg"));
  }

  @Override
  public void fetchProfiles(List<String> list, LCChatProfilesCallBack callBack) {
    List<LCChatKitUser> userList = new ArrayList<LCChatKitUser>();
    for (String userId : list) {
      for (LCChatKitUser user : partUsers) {
        if (user.getUserId().equals(userId)) {
          userList.add(user);
          break;
        }
      }
    }
    callBack.done(userList, null);
  }

  @Override
  public List<LCChatKitUser> getAllUsers() {
    return partUsers;
  }
}
