package ServerData.Services;

import Server.Data.Consts.ConstNpc;
import ServerData.Models.Intrinsic.Intrinsic;
import ServerData.Models.Player.Player;
import ServerData.Server.Manager;
import com.girlkun.network.io.Message;
import ServerData.Utils.Util;
import java.util.List;


public class IntrinsicService {

    private static IntrinsicService I;
    private static final int[] COST_OPEN = {10, 20, 40, 80, 160, 320, 640, 1280};

    public static IntrinsicService gI() {
        if (IntrinsicService.I == null) {
            IntrinsicService.I = new IntrinsicService();
        }
        return IntrinsicService.I;
    }

    public List<Intrinsic> getIntrinsics(byte playerGender) {
        switch (playerGender) {
            case 0:
                return Manager.INTRINSIC_TD;
            case 1:
                return Manager.INTRINSIC_NM;
            default:
                return Manager.INTRINSIC_XD;
        }
    }

    public Intrinsic getIntrinsicById(int id) {
        for (Intrinsic intrinsic : Manager.INTRINSICS) {
            if (intrinsic.id == id) {
                return new Intrinsic(intrinsic);
            }
        }
        return null;
    }

    public void sendInfoIntrinsic(Player player) {
        Message msg;
        try {
            msg = new Message(112);
            msg.writer().writeByte(0);
            msg.writer().writeShort(player.playerIntrinsic.intrinsic.icon);
            msg.writer().writeUTF(player.playerIntrinsic.intrinsic.getName());
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void showAllIntrinsic(Player player) {
        List<Intrinsic> listIntrinsic = getIntrinsics(player.gender);
        Message msg;
        try {
            msg = new Message(112);
            msg.writer().writeByte(1);
            msg.writer().writeByte(1); //count tab
            msg.writer().writeUTF("List Nội Tại");
            msg.writer().writeByte(listIntrinsic.size() - 1);
            for (int i = 1; i < listIntrinsic.size(); i++) {
                msg.writer().writeShort(listIntrinsic.get(i).icon);
                msg.writer().writeUTF(listIntrinsic.get(i).getDescription());
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void showMenu(Player player) {
        NpcService.gI().createMenuConMeo(player, ConstNpc.INTRINSIC, -1,
                "Nội tại là một kỹ năng bị động hỗ trợ đặc biệt\nBạn có muốn mở hoặc thay đổi nội tại không?",
                "Xem\ntất cả\nNội Tại", "Mở\nNội Tại", "Mở VIP", "Từ chối");
    }
    
    public void sattd(Player player) {
        NpcService.gI().createMenuConMeo(player, ConstNpc.menutd, -1,
                                "chọn lẹ đi để tau đi chơi với ny", "Set\nTaiyoken", "Set\nGenki", "Set\nKamejoko", "Từ chối");
            }
    
    public void satnm(Player player) {
        NpcService.gI().createMenuConMeo(player, ConstNpc.menunm, -1,
                                "chọn lẹ đi để tau đi chơi với ny", "Set\nKi", "Set\nDame", "Set\nPikkoro", "Từ chối");
            }
    
    public void setxd(Player player) {
        NpcService.gI().createMenuConMeo(player, ConstNpc.menuxd, -1,
                                "chọn lẹ đi để tau đi chơi với ny", "Set\nKakarot", "Set\nVegeta", "Set\nNappa", "Từ chối");
            }
      //sku tl
    public void sattdtl(Player player) {
        NpcService.gI().createMenuConMeo(player, ConstNpc.menutdtl, -1,
                                "chọn lẹ đi để tau đi chơi với ny", "Set\nThên Xin Hăng", "Set\nGenki", "Set\nKame", "Từ chối");
                    
            }
    
    public void satnmtl(Player player) {
        NpcService.gI().createMenuConMeo(player, ConstNpc.menunmtl, -1,
                                "chọn lẹ đi để tau đi chơi với ny", "Set\nKI", "Set\nLiên Hoàn", "Set\nChim", "Từ chối");
                    
            }
    
    public void setxdtl(Player player) {
        NpcService.gI().createMenuConMeo(player, ConstNpc.menuxdtl, -1,
                                "chọn lẹ đi để tau đi chơi với ny", "Set\nGalick", "Set\nKhỉ", "Set\nHP", "Từ chối");
                    
            }
 public void showMenuhoisinh(Player player) {
        NpcService.gI().createMenuConMeo(player, ConstNpc.HOISINH, -1,
                "Bạn đang trong map đặc biệt\nBạn có muốn hồi sinh với phí là 1000 hồng ngọc không?",
                "Đồng ý", "Từ chối");
    }
    public void showConfirmOpen(Player player) {
        if (COST_OPEN.length == 8) {
            Service.gI().sendThongBao(player, "Đã quá số lần mở thường!");
            return;
        }
        NpcService.gI().createMenuConMeo(player, ConstNpc.CONFIRM_OPEN_INTRINSIC, -1, "Bạn muốn đổi Nội Tại khác\nvới giá là "
                + COST_OPEN[player.playerIntrinsic.countOpen] + " Tr vàng ?", "Mở\nNội Tại\nThường", "Từ chối");
    }

    public void showConfirmOpenVip(Player player) {
        NpcService.gI().createMenuConMeo(player, ConstNpc.CONFIRM_OPEN_INTRINSIC_VIP, -1,
                "Bạn có muốn mở Nội Tại VIP\nvới giá là 50 hồng ngọc và\ntái lập giá vàng quay lại ban đầu không?", "Mở\nNội Tại\nVIP", "Từ chối");
    }

    private void changeIntrinsic(Player player) {
        List<Intrinsic> listIntrinsic = getIntrinsics(player.gender);
        player.playerIntrinsic.intrinsic = new Intrinsic(listIntrinsic.get(Util.nextInt(1, listIntrinsic.size() - 1)));
        player.playerIntrinsic.intrinsic.param1 = (short) Util.nextInt(player.playerIntrinsic.intrinsic.paramFrom1, player.playerIntrinsic.intrinsic.paramTo1);
        player.playerIntrinsic.intrinsic.param2 = (short) Util.nextInt(player.playerIntrinsic.intrinsic.paramFrom2, player.playerIntrinsic.intrinsic.paramTo2);
        Service.gI().sendThongBao(player, "Bạn nhận được Nội tại:\n" + player.playerIntrinsic.intrinsic.getName().substring(0, player.playerIntrinsic.intrinsic.getName().indexOf(" [")));
        sendInfoIntrinsic(player);
    }

    public void open(Player player) {
        if (player.nPoint.power >= 10000000000L) {
            int goldRequire = COST_OPEN[player.playerIntrinsic.countOpen] * 1000000;
            if (player.inventory.gold >= goldRequire) {
                player.inventory.gold -= goldRequire;
                PlayerService.gI().sendInfoHpMpMoney(player);
                changeIntrinsic(player);
                player.playerIntrinsic.countOpen++;
            } else {
                Service.gI().sendThongBao(player, "Bạn không đủ vàng, còn thiếu "
                        + Util.numberToMoney(goldRequire - player.inventory.gold) + " vàng nữa");
            }
        } else {
            Service.gI().sendThongBao(player, "Yêu cầu sức mạnh tối thiểu 10 tỷ");
        }
    }

    public void openVip(Player player) {
        if (player.nPoint.power >= 10000000000L) {
            int ngoc = 5;
            if (player.inventory.ruby >= 5) {
                player.inventory.ruby -= 5;
                PlayerService.gI().sendInfoHpMpMoney(player);
                changeIntrinsic(player);
                player.playerIntrinsic.countOpen = 0;
            } else {
                Service.gI().sendThongBao(player, "Bạn không có đủ vàng, còn thiếu "
                        + (ngoc - player.inventory.ruby) + " hồng ngọc nữa");
            }
        } else {
            Service.gI().sendThongBao(player, "Yêu cầu sức mạnh tối thiểu 10 tỷ");
        }
    }

}
