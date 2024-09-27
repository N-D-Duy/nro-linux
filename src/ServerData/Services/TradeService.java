package ServerData.Services;

import ServerData.Services.TransactionService;
import Server.Connect.HistoryTrade;
import ServerData.Models.Item.Item;
import ServerData.Models.Player.Inventory;
import ServerData.Models.Player.Player;
import com.girlkun.network.io.Message;
import ServerData.Services.ItemService;
import ServerData.Services.PlayerService;
import ServerData.Services.Service;
import ServerData.Services.InventoryServiceNew;
import ServerData.Utils.Logger;
import ServerData.Utils.Util;

import java.util.ArrayList;
import java.util.List;

public class TradeService {

    public static final int TIME_TRADE = 180000;
    public static final int QUANLITY_MAX = 999;

    private Player player1;
    private Player player2;

    private long gold1Before;
    private long gold2Before;
    private List<Item> bag1Before;
    private List<Item> bag2Before;

    private List<Item> itemsBag1;
    private List<Item> itemsBag2;

    private List<Item> itemsTrade1;
    private List<Item> itemsTrade2;
    private int goldTrade1;
    private int goldTrade2;

    public byte accept;

    private long lastTimeStart;
    private boolean start;

    public TradeService(Player pl1, Player pl2) {
        this.player1 = pl1;
        this.player2 = pl2;
        this.gold1Before = pl1.inventory.gold;
        this.gold2Before = pl2.inventory.gold;
        this.bag1Before = InventoryServiceNew.gI().copyItemsBag(player1);
        this.bag2Before = InventoryServiceNew.gI().copyItemsBag(player2);
        this.itemsBag1 = InventoryServiceNew.gI().copyItemsBag(player1);
        this.itemsBag2 = InventoryServiceNew.gI().copyItemsBag(player2);
        this.itemsTrade1 = new ArrayList<>();
        this.itemsTrade2 = new ArrayList<>();
        TransactionService.PLAYER_TRADE.put(pl1, this);
        TransactionService.PLAYER_TRADE.put(pl2, this);
    }

    public void openTabTrade() {
        this.lastTimeStart = System.currentTimeMillis();
        this.start = true;
        Message msg;
        try {
            msg = new Message(-86);
            msg.writer().writeByte(1);
            msg.writer().writeInt((int) player1.id);
            player2.sendMessage(msg);
            msg.cleanup();

            msg = new Message(-86);
            msg.writer().writeByte(1);
            msg.writer().writeInt((int) player2.id);
            player1.sendMessage(msg);
            msg.cleanup();
            Service.gI().hideWaitDialog(player1);
            Service.gI().hideWaitDialog(player2);
        } catch (Exception ignored) {
        }
    }

    public void addItemTrade(Player pl, byte index, int quantity) {
//        System.out.println("quantity: " + quantity);
//        if (pl.getSession() != null && pl.isJail()) {
//            Service.gI().sendThongBaoOK(pl, "ĐI TÙ RÒI CÒN BUÔN BÁN");
//            return;
//        }
        if (pl.getSession().actived == 1 && pl.getSession().player.nPoint.power >= 100000000000L) {
        if (this.player2.getSession().actived!=1&& pl.getSession().player.nPoint.power < 100000000000L) {
            this.cancelTrade();
            Service.gI().sendThongBaoFromAdmin(pl,
                  "|5|Đối phương chưa mở thành viên và đạt đủ 100 tỷ không thể giao dịch!");
            removeItemTrade(pl, index);
            return;
        }
        if (true) {
            if (index == -1) {
                if (pl.equals(this.player1)) {
                    goldTrade1 = quantity;
                } else {
                    goldTrade2 = quantity;
                }
            } else {
                Item item = null;
                if (pl.equals(this.player1)) {
                    item = itemsBag1.get(index);
                } else {
                    item = itemsBag2.get(index);
                }
                if (quantity > item.quantity || quantity < 0) {
                    return;
                }
                if (isItemCannotTran(item)) {
                    removeItemTrade(pl, index);
                    Service.getInstance().sendThongBao(pl, "Vật phẩm bị khóa, hoặc không được phép giao dịch");
                } else {
                    if (quantity > 999) {
                        int n = quantity / 999;
                        int left = quantity % 999;
                        for (int i = 0; i < n; i++) {
                            Item itemTrade = ItemService.gI().copyItem(item);
                            itemTrade.quantity = 999;
                            if (pl.equals(this.player1)) {
                                InventoryServiceNew.gI().subQuantityItem(itemsBag1, item, itemTrade.quantity);
                                itemsTrade1.add(itemTrade);
                            } else {
                                InventoryServiceNew.gI().subQuantityItem(itemsBag2, item, itemTrade.quantity);
                                itemsTrade2.add(itemTrade);
                            }
                        }
                        if (left > 0) {
                            Item itemTrade = ItemService.gI().copyItem(item);
                            itemTrade.quantity = left;
                            if (pl.equals(this.player1)) {
                                InventoryServiceNew.gI().subQuantityItem(itemsBag1, item, itemTrade.quantity);
                                itemsTrade1.add(itemTrade);
                            } else {
                                InventoryServiceNew.gI().subQuantityItem(itemsBag2, item, itemTrade.quantity);
                                itemsTrade2.add(itemTrade);
                            }
                        }
                    } else {
                        Item itemTrade = ItemService.gI().copyItem(item);
                        itemTrade.quantity = quantity != 0 ? quantity : 1;
                        if (pl.equals(this.player1)) {
                            InventoryServiceNew.gI().subQuantityItem(itemsBag1, item, itemTrade.quantity);
                            itemsTrade1.add(itemTrade);
                        } else {
                            InventoryServiceNew.gI().subQuantityItem(itemsBag2, item, itemTrade.quantity);
                            itemsTrade2.add(itemTrade);
                        }
                    }
                }
            }
        } 
    } else {
           this.cancelTrade();
            Service.gI().sendThongBaoFromAdmin(pl,
                  "|5|Bạn chưa mở thành viên và đạt đủ 100 tỷ không thể giao dịch!");
            removeItemTrade(pl, index);
    }
    }
    private void removeItemTrade(Player pl, byte index) {
        Message msg;
        try {
            msg = new Message(-86);
            msg.writer().writeByte(2);
            msg.writer().write(index);
            pl.sendMessage(msg);
            msg.cleanup();
            Service.gI().sendThongBao(pl, "Không thể giao dịch vật phẩm này");
        } catch (Exception e) {
        }
    }

    public void removeItemTrade2(Player pl, byte index) {
        Message msg;
        try {
            msg = new Message(-86);
            msg.writer().writeByte(2);
            msg.writer().write(index);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    private boolean isItemCannotTran(Item item) {
        for (Item.ItemOption io : item.itemOptions) {
            if (io.optionTemplate.id == 30) {
                return true;
            }}

        switch (item.template.type) {
            case 27: //
                if (item.template.id == 590) {
                    return true;
                } else {
                    return false;
                }
            case 99:
            case 5: //cải trang
                return false;
            case 6: //đậu thần
            case 7: //sách skill
            case 8: //vật phẩm nhiệm vụ
            case 11: //flag bag
            case 13: //bùa
            case 22: //vệ tinh
            case 23: //ván bay
            case 24: //ván bay vip
            case 28: //cờ
            case 31: //bánh trung thu, bánh tết
            case 32: //giáp tập luyện
           
                return true;
            default:
                return false;
        }
    }

    public void cancelTrade() {
        String notifiText = "Giao dịch bị hủy bỏ, không thể giao dịch vật phẩm này";
        Service.gI().sendThongBao(player1, notifiText);
        Service.gI().sendThongBao(player2, notifiText);
        closeTab();
        dispose();
    }

    private void closeTab() {
        Message msg;
        try {
            msg = new Message(-86);
            msg.writer().writeByte(7);
            player1.sendMessage(msg);
            player2.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void dispose() {
        player1.iDMark.setPlayerTradeId(-1);
        player2.iDMark.setPlayerTradeId(-1);
        TransactionService.PLAYER_TRADE.remove(player1);
        TransactionService.PLAYER_TRADE.remove(player2);
        this.player1 = null;
        this.player2 = null;
        this.itemsBag1 = null;
        this.itemsBag2 = null;
        this.itemsTrade1 = null;
        this.itemsTrade2 = null;
    }

    public void lockTran(Player pl) {
        Message msg;
        try {
            msg = new Message(-86);
            msg.writer().writeByte(6);
            if (pl.equals(player1)) {
                msg.writer().writeInt(goldTrade1);
                msg.writer().writeByte(itemsTrade1.size());
                for (Item item : itemsTrade1) {
                    msg.writer().writeShort(item.template.id);
                    msg.writer().writeInt(item.quantity);
                    msg.writer().writeByte(item.itemOptions.size());
                    for (Item.ItemOption io : item.itemOptions) {
                        msg.writer().writeByte(io.optionTemplate.id);
                        msg.writer().writeShort(io.param);
                    }
                }
                player2.sendMessage(msg);
            } else {
                msg.writer().writeInt(goldTrade2);
                msg.writer().writeByte(itemsTrade2.size());
                for (Item item : itemsTrade2) {
                    msg.writer().writeShort(item.template.id);
                    msg.writer().writeInt(item.quantity);
                    msg.writer().writeByte(item.itemOptions.size());
                    for (Item.ItemOption io : item.itemOptions) {
                        msg.writer().writeByte(io.optionTemplate.id);
                        msg.writer().writeShort(io.param);
                    }
                }
                player1.sendMessage(msg);
            }
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(TradeService.class, e);
        }
    }

    public void acceptTrade() {
        this.accept++;
        if (this.accept == 2) {
            this.startTrade();
        }
    }

    private void startTrade() {
        byte tradeStatus = SUCCESS;
        if (player1.inventory.gold + goldTrade2 > InventoryServiceNew.gI().getGodMax(player1)) {
            tradeStatus = FAIL_MAX_GOLD_PLAYER1;
        } else if (player2.inventory.gold + goldTrade1 > InventoryServiceNew.gI().getGodMax(player2)) {
            tradeStatus = FAIL_MAX_GOLD_PLAYER2;
        }
        if (tradeStatus != SUCCESS) {
            sendNotifyTrade(tradeStatus);
        } else {
            for (Item item : itemsTrade1) {
                if (!InventoryServiceNew.gI().addItemList(itemsBag2, item)) {
                    tradeStatus = FAIL_NOT_ENOUGH_BAG_P1;
                    break;
                }
            }
            if (tradeStatus != SUCCESS) {
                sendNotifyTrade(tradeStatus);
            } else {
                for (Item item : itemsTrade2) {
                    if (!InventoryServiceNew.gI().addItemList(itemsBag1, item)) {
                        tradeStatus = FAIL_NOT_ENOUGH_BAG_P2;
                        break;
                    }
                }
                if (tradeStatus == SUCCESS) {
                    player1.inventory.gold += goldTrade2;
                    player2.inventory.gold += goldTrade1;
                    player1.inventory.gold -= goldTrade1;
                    player2.inventory.gold -= goldTrade2;
                    player1.inventory.itemsBag = itemsBag1;
                    player2.inventory.itemsBag = itemsBag2;

                    InventoryServiceNew.gI().sendItemBags(player1);
                    InventoryServiceNew.gI().sendItemBags(player2);
                    PlayerService.gI().sendInfoHpMpMoney(player1);
                    PlayerService.gI().sendInfoHpMpMoney(player2);

                    HistoryTrade.insert(player1, player2, goldTrade1, goldTrade2, itemsTrade1, itemsTrade2,
                            bag1Before, bag2Before, this.player1.inventory.itemsBag, this.player2.inventory.itemsBag,
                            gold1Before, gold2Before, this.player1.inventory.gold, this.player2.inventory.gold);
                }
                sendNotifyTrade(tradeStatus);
            }
        }

    }

    private static final byte SUCCESS = 0;
    private static final byte FAIL_MAX_GOLD_PLAYER1 = 1;
    private static final byte FAIL_MAX_GOLD_PLAYER2 = 2;
    private static final byte FAIL_NOT_ENOUGH_BAG_P1 = 3;
    private static final byte FAIL_NOT_ENOUGH_BAG_P2 = 4;

    private void sendNotifyTrade(byte status) {
        player1.iDMark.setLastTimeTrade(System.currentTimeMillis());
        player2.iDMark.setLastTimeTrade(System.currentTimeMillis());
        switch (status) {
            case SUCCESS:
                Service.gI().sendThongBao(player1, "Giao dịch thành công");
                Service.gI().sendThongBao(player2, "Giao dịch thành công");
                break;
            case FAIL_MAX_GOLD_PLAYER1:
                Service.gI().sendThongBao(player1, "Giao dịch thất bại do số lượng vàng sau giao dịch vượt tối đa");
                Service.gI().sendThongBao(player2, "Giao dịch thất bại do số lượng vàng " + player1.name + " sau giao dịch vượt tối đa");
                break;
            case FAIL_MAX_GOLD_PLAYER2:
                Service.gI().sendThongBao(player2, "Giao dịch thất bại do số lượng vàng sau giao dịch vượt tối đa");
                Service.gI().sendThongBao(player1, "Giao dịch thất bại do số lượng vàng " + player2.name + " sau giao dịch vượt tối đa");
                break;
            case FAIL_NOT_ENOUGH_BAG_P1:
            case FAIL_NOT_ENOUGH_BAG_P2:
                Service.gI().sendThongBao(player1, "Giao dịch thất bại do 1 trong 2 không đủ ô trống trong hành trang");
                Service.gI().sendThongBao(player2, "Giao dịch thất bại do 1 trong 2 không đủ ô trống trong hành trang");
                break;
        }
    }

    public void update() {
        if (this.start && Util.canDoWithTime(lastTimeStart, TIME_TRADE)) {
            this.cancelTrade();
        }
    }
}
