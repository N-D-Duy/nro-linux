package ServerData.Services;

import Server.Data.Consts.ConstNpc;
import static ServerData.Models.NPC.NpcFactory.PLAYERID_OBJECT;
import ServerData.Models.Player.Player;
import ServerData.Server.Client;
import com.girlkun.network.io.Message;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SubMenuService {

    public static final int BAN = 500;
    public static final int TANG_PET = 501;
    public static final int JAIL = 502;

    private static SubMenuService i;

    private SubMenuService() {
    }

    public static SubMenuService gI() {
        if (i == null) {
            i = new SubMenuService();
        }
        return i;
    } 

    public void controller(Player player, int playerTarget, int menuId) {
        Player plTarget = Client.gI().getPlayer(playerTarget);
        switch (menuId) {
            case TANG_PET:
                if (plTarget != null) {
                if (plTarget.pet != null) {
                    String[] selectss = new String[]{"ĐỆ BƯ","ĐỆ KEFLA","ĐỆ CUMBER","ĐỆ GOKU UI"};
                    NpcService.gI().createMenuConMeo(player, ConstNpc.TANG_PET, 12639,
                             "|1|Pet Hiện Tại : " + plTarget.pet.name
                            +"\nBạn có chắc chắn muốn phát đệ tử cho " + plTarget.name+"?"
                            +"\n|7|[ Vui Lòng Tháo Đồ Trước Khi Dùng ]", selectss, plTarget);
                } else {
                    String[] selectss = new String[]{"ĐỆ THƯỜNG"};
                    NpcService.gI().createMenuConMeo(player, ConstNpc.TANG_PET, 12639,
                            "|1|Pet Hiện Tại : Player Chưa Có Pet"
                            +"\n" + "|7|Bạn có chắc chắn muốn phát đệ tử cho " + plTarget.name+"?", selectss, plTarget);
                }
                }
                break;
            case JAIL:
                if (plTarget != null) {
                    String[] selectsss = new String[]{"TỐNG GIAM","MỞ GIAM"};
                    NpcService.gI().createMenuConMeo(player, ConstNpc.JAIL, 12639,
                            "Player này đang [" + (plTarget.getSession().isJail ? " Đi Tù]\n" : " Thảnh Thơi]\n") +"Cho đi tù không?", selectsss, plTarget);
                }
                break;
            case BAN:
                if (plTarget != null) {
                    String[] selects = new String[]{"Đồng ý", "Hủy"};
                    NpcService.gI().createMenuConMeo(player, ConstNpc.BAN_PLAYER, 12639,
                            "|7|Bạn có chắc chắn muốn ban " + plTarget.name, selects, plTarget+"?");
                }
                break;    
        }
        Service.gI().hideWaitDialog(player);
    }

    public void showMenuForAdmin(Player player) {
        if(player.isAdmin()){
            Service.gI().sendThongBao(player, "Chỉ dành cho Admin");
            return;
        }
        showSubMenu(player, new SubMenu(501, "[ADMIN] BUFF PET", ""));
        showSubMenu(player, new SubMenu(502, "[ADMIN] GIAM GIỮ", ""));
        showSubMenu(player, new SubMenu(500, "[ADMIN] BAN ACC", ""));
        }

    public void showSubMenu(Player player, SubMenu... subMenus) {
        Message msg;
        try {
            msg = Service.gI().messageSubCommand((byte) 64);
            msg.writer().writeByte(subMenus.length);
            for (SubMenu subMenu : subMenus) {
                msg.writer().writeUTF(subMenu.caption1);
                msg.writer().writeUTF(subMenu.caption2);
                msg.writer().writeShort((short) subMenu.id);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (IOException e) {
            ServerData.Utils.Logger.logException(SubMenuService.class, e);
        }
    }

    public static class SubMenu {

        private int id;
        private String caption1;
        private String caption2;

        public SubMenu(int id, String caption1, String caption2) {
            this.id = id;
            this.caption1 = caption1;
            this.caption2 = caption2;
        }
    }
}
