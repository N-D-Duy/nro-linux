package ServerData.Services;

import ServerData.Models.Map.ItemMap;
import ServerData.Models.Player.Player;
import com.girlkun.network.io.Message;
import ServerData.Utils.Logger;
import ServerData.Utils.Util;


public class ItemMapService {

    private static ItemMapService i;

    public static ItemMapService gI() {
        if (i == null) {
            i = new ItemMapService();
        }
        return i;
    }

    public void pickItem(Player player, int itemMapId, boolean isThuHut) {
        if (isThuHut || Util.canDoWithTime(player.iDMark.getLastTimePickItem(), 1000)) {
            player.zone.pickItem(player, itemMapId);
            player.iDMark.setLastTimePickItem(System.currentTimeMillis());
        }
    }

    //xóa item map và gửi item map biến mất
    public void removeItemMapAndSendClient(ItemMap itemMap) {
        sendItemMapDisappear(itemMap);
        removeItemMap(itemMap);
    }

    public void sendItemMapDisappear(ItemMap itemMap) {
        Message msg;
        try {
            msg = new Message(-21);
            msg.writer().writeShort(itemMap.itemMapId);
            Service.getInstance().sendMessAllPlayerInMap(itemMap.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(ItemMapService.class, e);
        }
    }

    public void removeItemMap(ItemMap itemMap) {
        itemMap.zone.removeItemMap(itemMap);
        itemMap.dispose();
    }

    public boolean isBlackBall(int tempId) {
        return tempId >= 372 && tempId <= 378;
    }

    public boolean isNamecBall(int tempId) {
        return tempId >= 353 && tempId <= 360;
    }
}
