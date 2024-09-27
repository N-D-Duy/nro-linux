package ServerData.Services;

import Server.Data.Consts.ConstPlayer;
import ServerData.Models.Item.Item;
import static ServerData.Models.Item.ItemTime.*;
import ServerData.Models.Map.ListMap.BanDoKhoBau;
import ServerData.Models.Map.ListMap.DoanhTrai;
import ServerData.Models.Player.Fusion;
import ServerData.Models.Player.Player;
import com.girlkun.network.io.Message;
import ServerData.Models.Map.ListMap.KhiGasHuyDiet;
import ServerData.Models.Map.gas.Gas;
import ServerData.Utils.Logger;
import ServerData.Models.Map.ConDuongRanDoc.ConDuongRanDoc;
//import ServerData.Services.Func.TaiXiu.TaiXiu_Old;


public class ItemTimeService {

    private static ItemTimeService i;

    public static ItemTimeService gI() {
        if (i == null) {
            i = new ItemTimeService();
        }
        return i;
    }

    //gửi cho client
    public void sendAllItemTime(Player player) {
        sendTextDoanhTrai(player);
        sendTextBanDoKhoBau(player);
           sendTextConDuongRanDoc(player);
        if (player.fusion.typeFusion == ConstPlayer.LUONG_LONG_NHAT_THE) {
            sendItemTime(player, player.gender == ConstPlayer.NAMEC ? 3901 : 3790,
                    (int) ((Fusion.TIME_FUSION - (System.currentTimeMillis() - player.fusion.lastTimeFusion)) / 1000));
        }
        if (player.fusion.typeFusion == ConstPlayer.HOP_THE_GOGETA) {
            sendItemTime(player, 16597,
            (int) ((Fusion.TIME_FUSION2 - (System.currentTimeMillis() - player.fusion.lastTimeFusion)) / 1000));
        }
        if (player.itemTime.isUseBoHuyet) {
            sendItemTime(player, 2755, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBoHuyet)) / 1000));
        }
        if (player.itemTime.isUseBoKhi) {
            sendItemTime(player, 2756, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBoKhi)) / 1000));
        }
        if (player.itemTime.isUseGiapXen) {
            sendItemTime(player, 2757, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeGiapXen)) / 1000));
        }
        if (player.itemTime.isUseCuongNo) {
            sendItemTime(player, 2754, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeCuongNo)) / 1000));
        }
        
        if (player.itemTime.isUseAnDanh) {
            sendItemTime(player, 2760, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeAnDanh)) / 1000));
        }
        if (player.itemTime.isUseBoHuyetSC) {
            sendItemTime(player, 10714, (int) ((TIME_ITEMSC - (System.currentTimeMillis() - player.itemTime.lastTimeBoHuyetSC)) / 1000));
        }
        if (player.itemTime.isUseBoKhiSC) {
            sendItemTime(player, 10715, (int) ((TIME_ITEMSC - (System.currentTimeMillis() - player.itemTime.lastTimeBoKhiSC)) / 1000));
        }
        if (player.itemTime.isUseGiapXenSC) {
            sendItemTime(player, 10712, (int) ((TIME_ITEMSC - (System.currentTimeMillis() - player.itemTime.lastTimeGiapXenSC)) / 1000));
        }
        if (player.itemTime.isUseCuongNoSC) {
            sendItemTime(player, 10716, (int) ((TIME_ITEMSC - (System.currentTimeMillis() - player.itemTime.lastTimeCuongNoSC)) / 1000));
        }
        if (player.itemTime.isUseAnDanhSC) {
            sendItemTime(player, 10717, (int) ((TIME_ITEMSC - (System.currentTimeMillis() - player.itemTime.lastTimeAnDanhSC)) / 1000));
        }
        if (player.itemTime.isOpenPower) {
            sendItemTime(player, 3783, (int) ((TIME_OPEN_POWER - (System.currentTimeMillis() - player.itemTime.lastTimeOpenPower)) / 1000));
        }
        if (player.itemTime.isUseMayDo) {
            sendItemTime(player, 2758, (int) ((TIME_MAY_DO - (System.currentTimeMillis() - player.itemTime.lastTimeUseMayDo)) / 1000));
        }
        if (player.itemTime.ISBANH) {
            sendItemTime(player, player.itemTime.ICONBANH, (int) ((TIMEBANH - (System.currentTimeMillis() - player.itemTime.LTBANH)) / 1000));
        }
        if (player.itemTime.isEatMeal) {
            sendItemTime(player, player.itemTime.iconMeal, (int) ((TIME_EAT_MEAL - (System.currentTimeMillis() - player.itemTime.lastTimeEatMeal)) / 1000));
        }
        if (player.itemTime.isUseTDLT) {
            sendItemTime(player, 4387,player.itemTime.timeTDLT / 1000);
        }
        if (player.itemTime.VENGOC) {
            sendItemTime(player, 13327, (int) ((TIMENGOC - (System.currentTimeMillis() - player.itemTime.LTNGOC)) / 1000));
        }
        if (player.itemTime.isUseBanhChung) {
            sendItemTime(player, 25509, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeUseBanhChung)) / 1000));
        }
        if (player.itemTime.isUseBanhTet) {
            sendItemTime(player, 25510, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeUseBanhTet)) / 1000));
        }
    }

    //bật tđlt
    public void turnOnTDLT(Player player, Item item) {
        int min = 0;
        for (Item.ItemOption io : item.itemOptions) {
            if (io.optionTemplate.id == 1) {
                min = io.param;
                io.param = 0;
                break;
            }
        }
        player.itemTime.isUseTDLT = true;
        player.itemTime.timeTDLT = min * 60 * 1000;
        player.itemTime.lastTimeUseTDLT = System.currentTimeMillis();
        sendCanAutoPlay(player);
        sendItemTime(player, 4387, player.itemTime.timeTDLT / 1000);
        InventoryServiceNew.gI().sendItemBags(player);
    }

    //tắt tđlt
    public void turnOffTDLT(Player player, Item item) {
        player.itemTime.isUseTDLT = false;
        for (Item.ItemOption io : item.itemOptions) {
            if (io.optionTemplate.id == 1) {
                io.param += (short) ((player.itemTime.timeTDLT - (System.currentTimeMillis() - player.itemTime.lastTimeUseTDLT)) / 60 / 1000);
                break;
            }
        }
        sendCanAutoPlay(player);
        removeItemTime(player, 4387);
        InventoryServiceNew.gI().sendItemBags(player);
    }
//  public void sendTextTX(Player player) {
//        if (TaiXiu_Old.gI().goldTai > 0 || TaiXiu_Old.gI().goldXiu > 0) {
//            int secondsLeft = (int) ((TaiXiu_Old.gI().lastTimeEnd - System.currentTimeMillis())/1000);
//            sendTextTime(player, TX, "Tài Xỉu", secondsLeft);
//            if (secondsLeft <=0){
//                removeTextTX(player);
//            }
//        }
//    }
   public void removeTextTX(Player player) {
        removeTextTime(player, TX);
    }
    public void sendCanAutoPlay(Player player) {
        Message msg;
        try {
            msg = new Message(-116);
            msg.writer().writeByte(player.itemTime.isUseTDLT ? 1 : 0);
            player.sendMessage(msg);
        } catch (Exception e) {
            Logger.logException(ItemTimeService.class, e);
        }
    }

    public void sendTextDoanhTrai(Player player) {
        if (player.clan != null && !player.clan.haveGoneDoanhTrai
                && player.clan.timeOpenDoanhTrai != 0) {
            int secondPassed = (int) ((System.currentTimeMillis() - player.clan.timeOpenDoanhTrai) / 1000);
            int secondsLeft = (DoanhTrai.TIME_DOANH_TRAI / 1000) - secondPassed;
            sendTextTime(player, DOANH_TRAI, "Doanh trại độc nhãn: ", secondsLeft);
        }
    }
     public void sendTextGas(Player player) {
        if (player.clan != null
                && player.clan.timeOpenKhiGas != 0) {
            int secondPassed = (int) ((System.currentTimeMillis() - player.clan.timeOpenKhiGas) / 1000);
            int secondsLeft = (Gas.TIME_KHI_GAS / 1000) - secondPassed;
            sendTextTime(player, KHI_GASS, "Khí Gas Hủy Diệt: ", secondsLeft);
        }
    }
 public void sendTextConDuongRanDoc(Player player) {
        if (player.clan != null && !player.clan.haveGoneConDuongRanDoc
                && player.clan.timeOpenConDuongRanDoc != 0) {
            int secondPassed = (int) ((System.currentTimeMillis() - player.clan.timeOpenConDuongRanDoc) / 1000);
            int secondsLeft = (ConDuongRanDoc.TIME_CON_DUONG_RAN_DOC / 1000) - secondPassed;
            sendTextTime(player, CON_DUONG_RAN_DOC, "Con đường rắn độc: ", secondsLeft);
        }
    }
    public void sendTextBanDoKhoBau(Player player) {
        if (player.clan != null
                && player.clan.timeOpenBanDoKhoBau != 0) {
            int secondPassed = (int) ((System.currentTimeMillis() - player.clan.timeOpenBanDoKhoBau) / 1000);
            int secondsLeft = (BanDoKhoBau.TIME_BAN_DO_KHO_BAU / 1000) - secondPassed;
            sendTextTime(player, BAN_DO_KHO_BAU, "Đi tìm kho báu: ", secondsLeft);
        }
    }
    
    public void sendTextKhiGaHuyDiet(Player player) {
        if (player.clan != null
                && player.clan.timeOpenKhiGaHuyDiet != 0) {
            int secondPassed = (int) ((System.currentTimeMillis() - player.clan.timeOpenKhiGaHuyDiet) / 1000);
            int secondsLeft = (KhiGasHuyDiet.TIME_KHI_GA_HUY_DIET / 1000) - secondPassed;
            sendTextTime(player, KHI_GA_HUY_DIET, "Khí gas hủy diệt: ", secondsLeft);
        }
    }

    public void removeTextDoanhTrai(Player player) {
        removeTextTime(player, DOANH_TRAI);
    }

    public void removeTextTime(Player player, byte id) {
        sendTextTime(player, id, "", 0);
    }

    public void sendTextTime(Player player, byte id, String text, int seconds) {
        Message msg;
        try {
            msg = new Message(65);
            msg.writer().writeByte(id);
            msg.writer().writeUTF(text);
            msg.writer().writeShort(seconds);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendItemTime(Player player, int itemId, int time) {
        Message msg;
        try {
            msg = new Message(-106);
            msg.writer().writeShort(itemId);
            msg.writer().writeShort(time);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void removeItemTime(Player player, int itemTime) {
        sendItemTime(player, itemTime, 0);
    }

}
