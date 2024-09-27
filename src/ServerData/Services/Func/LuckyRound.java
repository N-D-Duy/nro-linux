package ServerData.Services.Func;

import ServerData.Models.Item.Item;
import ServerData.Models.Player.Player;
import ServerData.Services.InventoryServiceNew;
import com.girlkun.network.io.Message;
import ServerData.Services.RewardService;
import ServerData.Services.Service;
import java.util.List;


public class LuckyRound {

    private static final short MAX_ITEM_IN_BOX = 200;
//    private static final int IFOX_BOX = 400;

    //1 gem and ruby
    public static final byte USING_GEM = 2;
    public static final byte USING_GOLD = 0;

    private static final int PRICE_GEM = 500;
    private static final int PRICE_GOLD = 10000000;

    private static LuckyRound i;

    private LuckyRound() {

    }

    public static LuckyRound gI() {
        if (i == null) {
            i = new LuckyRound();
        }
        return i;
    }

    public void openCrackBallUI(Player pl, byte type) {
        pl.iDMark.setTypeLuckyRound(type);
        Message msg;
        try {
            msg = new Message(-127);
            msg.writer().writeByte(0);
            msg.writer().writeByte(7);
            for (int i = 0; i < 7; i++) {
                msg.writer().writeShort(419 + i);
            }
            msg.writer().writeByte(type); //type price
            msg.writer().writeInt(type == USING_GEM ? PRICE_GEM : PRICE_GOLD); //price
            msg.writer().writeShort(-1); //id ticket
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void readOpenBall(Player player, Message msg) {
        try {
            byte type = msg.reader().readByte();
            byte count = msg.reader().readByte();
            switch (player.iDMark.getTypeLuckyRound()) {
                case USING_GEM:
                    openBallByGem(player, count);
                    break;
                case USING_GOLD:
                    openBallByGold(player, count);
                    break;
            }
        } catch (Exception e) {
            openCrackBallUI(player, player.iDMark.getTypeLuckyRound());
        }
    }

    private void openBallByGem(Player player, byte count) {
        int gemNeed = (count * PRICE_GEM);
        if (player.inventory.ruby < gemNeed) {
            Service.gI().sendThongBao(player, "Bạn không đủ ngọc để mở");
            return;
        }
            if (count + player.inventory.itemsBoxCrackBall.size() <= MAX_ITEM_IN_BOX) {
                player.inventory.ruby -= gemNeed;
                List<Item> list = RewardService.gI().getListItemLuckyRound(player, count);
                addItemToBox(player, list);
                sendReward(player, list);
                Service.gI().sendMoney(player);
            } else {
                Service.gI().sendThongBao(player, "Rương phụ đã đầy");
            }
        }

    private void openBallByGold(Player player, byte count) {
        int goldNeed = (count * PRICE_GOLD);
        if (player.inventory.gold < goldNeed) {
            Service.gI().sendThongBao(player, "Bạn không đủ vàng để mở");
            return;
        }
            if (count + player.inventory.itemsBoxCrackBall.size() <= MAX_ITEM_IN_BOX) {
                Item ngocrong = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 18);
                if (ngocrong.quantity > count) {
                    ngocrong.quantity -= count;
                    InventoryServiceNew.gI().subQuantityItemsBag(player,ngocrong,count);
                } else {
                player.inventory.gold -= goldNeed;
                }
                List<Item> list = RewardService.gI().getListItemLuckyRound(player, count);
                addItemToBox(player, list);
                sendReward(player, list);
                Service.gI().sendMoney(player);
            } else {
                Service.gI().sendThongBao(player, "Rương phụ đã đầy");
            }
        }

    private void sendReward(Player player, List<Item> items) {
        Message msg;
        try {
            msg = new Message(-127);
            msg.writer().writeByte(1);
            msg.writer().writeByte(items.size());
            for (Item item : items) {
                msg.writer().writeShort(item.template.iconID);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    private void addItemToBox(Player player, List<Item> items) {
        for (Item item : items) {
            player.inventory.itemsBoxCrackBall.add(item);
        }
    }
}
