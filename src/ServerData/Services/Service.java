package ServerData.Services;

import ServerData.Models.Player.PlayerSkill.SkillService;
import ServerData.Services.Giftcode.GiftcodeManager;
import com.girlkun.database.GirlkunDB;
import Server.Connect.GodGK;
import Server.Connect.PlayerDAO;
import Server.Data.Consts.ConstEvent;
import Server.Data.Consts.ConstNpc;
import Server.Data.Consts.ConstPlayer;
import ServerData.Utils.FileIO;
import Server.Data.DataGame;
import ServerData.Boss.Boss;
import ServerData.Boss.BossManager;
import ServerData.Boss.BossStatus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ServerData.Models.Item.Item;
import ServerData.Models.Map.ItemMap;
import ServerData.Models.Map.Mob.Mob;
import ServerData.Models.Player.Pet;
import ServerData.Models.Item.Item.ItemOption;
import ServerData.Models.Map.Zone;
import ServerData.Models.PVP.TOP;
import ServerData.Models.Player.Player;
import ServerData.Server.MySession;
import ServerData.Models.Player.PlayerSkill.Skill;
import com.girlkun.network.io.Message;
import com.girlkun.network.server.GirlkunSessionManager;
import com.girlkun.network.session.ISession;
import com.girlkun.network.session.Session;
import com.girlkun.result.GirlkunResultSet;
import ServerData.Server.Client;
import ServerData.Server.Manager;
import ServerData.Server.ServerManager;
import ServerData.Services.Func.Input;
import ServerData.Services.Func.UseItem;
import ServerData.Services.Giftcode.GiftcodeService;
import ServerData.Utils.Logger;
import ServerData.Utils.SkillUtil;
import ServerData.Utils.TimeUtil;
import ServerData.Utils.Util;
import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class Service {

    private static Service instance;
    private boolean isSave;
    private final AtomicBoolean isSave2 = new AtomicBoolean(false);

    public void botnetlofichill() {
        if (!isSave2.compareAndSet(false, true)) {
            return; // Check var
        }

        try {
            // Duyệt qua danh sách người chơi và cập nhật cơ sở dữ liệu
            for (Player player : Client.gI().getPlayers()) {
                if (player != null) {
                    PlayerDAO.updatePlayer(player);
                }
            }

            // Gim Ram + CPU
            System.gc();

            Thread.sleep(8000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        } finally {
            isSave2.set(false);
        }
    }

    public static Service gI() {
        if (instance == null) {
            instance = new Service();
        }
        return instance;
    }

    public static Service getInstance() {
        if (instance == null) {
            instance = new Service();
        }
        return instance;
    }

    public void sendchienlinh(Player player, short smallId) {
        Message msg;
        try {
            msg = new Message(31);
            msg.writer().writeInt((int) player.id);
            if (smallId == 0) {
                msg.writer().writeByte(0);
            } else {
                msg.writer().writeByte(1);
                msg.writer().writeShort(smallId);
                msg.writer().writeByte(1);
                int[] fr = new int[] { 0, 1, 2, 3, 4, 5, 6, 7 };
                msg.writer().writeByte(fr.length);
                for (int i = 0; i < fr.length; i++) {
                    msg.writer().writeByte(fr[i]);
                }
                msg.writer().writeShort(smallId == 15067 ? 65 : 225);
                msg.writer().writeShort(smallId == 15067 ? 65 : 225);
            }
            sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            System.out.println("wrwert");
        }
    }

    public void sendchienlinh(Player me, Player pl) {
        Item linhThu = pl.inventory.itemsBody.get(10);
        if (!linhThu.isNotNullItem()) {
            return;
        }
        short smallId = (short) (linhThu.template.iconID - 1);
        Message msg;
        try {
            msg = new Message(31);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeByte(1);
            msg.writer().writeShort(smallId);
            msg.writer().writeByte(1);
            int[] fr = new int[] { 0, 1, 2, 3, 4, 5, 6, 7 };
            msg.writer().writeByte(fr.length);
            for (int i = 0; i < fr.length; i++) {
                msg.writer().writeByte(fr[i]);
            }
            msg.writer().writeShort(smallId == 15067 ? 225 : 225);
            msg.writer().writeShort(smallId == 15067 ? 222 : 225);
            me.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            System.out.println("Lỗi Send Chiến Linh");
        }
    }

    public void managePlayer(Player player, Message _msg) {
        if (!player.getSession().isAdmin2) {
            Service.gI().sendThongBao(player, "Chỉ dành cho Admin");
            return;
        }
        if (_msg != null) {
            try {
                String name = _msg.readUTF();
                System.out.println("Check Player : " + name);
                Player pl = Client.gI().getPlayer(name);
                if (pl != null) {
                    int sl = InventoryServiceNew.gI().findItemBag(pl, (short) 457) == null ? 0
                            : InventoryServiceNew.gI().findItemBag(pl, (short) 457).quantity;
                    NpcService.gI().createMenuConMeo(player, ConstNpc.QUANLYTK, 12639, "|7|[ MANAGER ACCOUNT ]"
                            + "\n|5|Player : " + pl.name
                            + (pl.vip > 0 && pl.vip < 4 ? " [VIP" + pl.vip + "]" : pl.vip == 4 ? " [SVIP]" : "")
                            + "\nAccount ID : " + pl.id + " | " + "IP Connect : " + pl.getSession().ipAddress + " | "
                            + "Version Mod : " + pl.getSession().version
                            + "\nActive : " + (pl.getSession().actived == 1 ? "On" : "Off")
                            + "\nPrison : " + (pl.isJail() ? "On" : "Off")
                            + "\nThỏi Vàng : " + Util.format(sl)
                            + "\nHồng Ngọc : " + Util.format(pl.inventory.ruby)
                            + "\nTổng Nạp : " + Util.format(pl.getSession().tongnap)
                            + "\nVNĐ : " + Util.format(pl.getSession().vnd)
                            + "\n|7|[ DRAGONBALL Kamui ]",
                            new String[] { "ĐỔI TÊN", "BAN", "KICK", "ACTIVE", "ĐỆ TỬ", "DANH HIỆU", "NHIỆM VỤ",
                                    "GIAM GIỮ", "MAKE ADMIN", "THU ITEM" },
                            pl);
                } else {
                    Service.gI().sendThongBao(player, "Người chơi không tồn tại hoặc đang offline");
                }
            } catch (IOException e) {
                System.out.println("Lỗi Manager Player");
            }
        } else {
            System.out.println("Manager Player msg null");
        }
    }

    public void init() {
        // ServerManager.gI().close(0);
    }

    // _____________________Top Siêu Hạng___________________________________
    public void showListTop(Player player, List<TOP> tops, byte isPVP) {
        Message msg;
        try {
            msg = new Message(-96);
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Top");
            msg.writer().writeByte(tops.size());
            for (int i = 0; i < tops.size(); i++) {
                TOP top = tops.get(i);
                Player pl = GodGK.loadById(top.getId_player());
                msg.writer().writeInt(isPVP != 1 ? (i + 1) : (int) pl.rankSieuHang);
                // msg.writer().writeInt(i + 1);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeShort(pl.getHead());
                if (player.getSession().version > 214) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(pl.getBody());
                msg.writer().writeShort(pl.getLeg());
                msg.writer().writeUTF(pl.name);
                msg.writer().writeUTF(top.getInfo1());
                // msg.writer().writeUTF(isPVP == 1 ? top.getInfo2() : top.getInfo2() +
                // pl.numKillSieuHang);
                msg.writer().writeUTF(isPVP == 1 ? ("Sức Đánh: " + pl.nPoint.dameg + "\n" + "HP: " + pl.nPoint.hpg
                        + "\n" + "KI: " + pl.nPoint.mpg + "\n" + "Điểm hạng: " + pl.rankSieuHang + "\n" + pl.name
                        + "đang thi đấu tại " + pl.zone.map.mapName + " khu vực " + "5" + "(" + pl.zone.map.mapId + ")")
                        : top.getInfo2());
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showListTopDauNhanh(Player player, List<TOP> tops, byte isPVP, int start) {
        Message msg;
        try {
            msg = new Message(-96);
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Top");
            msg.writer().writeByte(tops.size());

            for (int i = 0; i < tops.size(); i++) {
                TOP top = tops.get(i);
                Player pl = GodGK.loadById(top.getId_player());
                // msg.writer().writeInt(isPVP != 1 ? (i + 1) : (int)pl.rankSieuHang);
                msg.writer().writeInt(start);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeShort(pl.getHead());
                if (player.getSession().version > 214) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(pl.getBody());
                msg.writer().writeShort(pl.getLeg());
                msg.writer().writeUTF(pl.name);
                msg.writer().writeUTF(top.getInfo1());
                // msg.writer().writeUTF(isPVP == 1 ? top.getInfo2() : top.getInfo2() +
                // pl.numKillSieuHang);
                msg.writer().writeUTF(
                        isPVP == 1
                                ? ("Sức Đánh: " + pl.nPoint.dame + "\n" + "HP: " + pl.nPoint.hpMax + "\n" + "KI: "
                                        + pl.nPoint.mpMax + "\n" + "Điểm hạng: " + pl.rankSieuHang)
                                : top.getInfo2());
                start++;
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeEff(Player pl, int... id) {
        try {
            Message msg = new Message(-128);
            if (id.length > 0) {
                msg.writer().writeByte(1);
            } else {
                msg.writer().writeByte(2);
            }
            msg.writer().writeInt((int) pl.id);
            if (id.length > 0) {
                msg.writer().writeShort(id[0]);
            }
            sendMessAllPlayerInMap(pl.zone, msg);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void showListPlayer(Player player) {
        Message msg;
        try {
            msg = new Message(-96);
            msg.writer().writeByte(1);
            msg.writer().writeUTF("(" + TimeUtil.getTimeNow("dd/MM/yyyy HH:mm:ss") + ")");
            msg.writer().writeByte(Client.gI().getPlayers().size());
            for (int i = 0; i < Client.gI().getPlayers().size(); i++) {
                Player pl = Client.gI().getPlayers().get(i);
                if (pl == null)
                    pl = player;
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeShort(pl.getHead());
                if (player.getSession().version > 214) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(pl.getBody());
                msg.writer().writeShort(pl.getLeg());
                msg.writer().writeUTF(pl.name);
                msg.writer().writeUTF(pl.isAdmin() ? "" : pl.isAdmin2() ? "" : "Member");
                msg.writer().writeUTF("SM: " + Util.powerToString(pl.nPoint.power)
                        + "\nTN: " + Util.powerToString(pl.nPoint.tiemNang)
                        + "\nHP: " + Util.powerToString(pl.nPoint.hpMax)
                        + "\nKI: " + Util.powerToString(pl.nPoint.mpMax)
                        + "\nSD: " + Util.powerToString(pl.nPoint.dame)
                        + "\nDEF: " + Util.powerToString(pl.nPoint.def)
                        + "\nCM: " + pl.nPoint.crit + "%"
                        + "\n|7|[Map: " + pl.zone.map.mapName + "(" + pl.zone.map.mapId + ") " + "Khu: "
                        + pl.zone.zoneId + "]");
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (IOException e) {
        }
    }

    public void showListTop(Player player, List<TOP> tops) {
        Message msg;
        try {
            msg = new Message(-96);
            msg.writer().writeByte(0);
            if (tops == Manager.topHP) {
                msg.writer().writeUTF("Top HP");
            } else if (tops == Manager.topNV) {
                msg.writer().writeUTF("Top Nhiệm vụ");
            } else if (tops == Manager.topSD) {
                msg.writer().writeUTF("Top Sức Đánh");
            } else if (tops == Manager.topGT) {
                msg.writer().writeUTF("Top Gắp Thú");
            } else if (tops == Manager.topSK) {
                msg.writer().writeUTF("Top Sự Kiện");
            } else if (tops == Manager.topVND) {
                msg.writer().writeUTF("Top Nạp");
            } else if (tops == Manager.topKI) {
                msg.writer().writeUTF("Top KI");
            } else if (tops == Manager.topNHS) {
                msg.writer().writeUTF("Top Ngũ Hành Sơn");
            } else if (tops == Manager.topHN) {
                msg.writer().writeUTF("Top Hồng Ngọc");
            } else if (tops == Manager.topPVP) {
                msg.writer().writeUTF("Top PvP");
            } else {
                msg.writer().writeUTF("Top Sức mạnh");
            }
            msg.writer().writeByte(tops.size());
            for (int i = 0; i < tops.size(); i++) {
                TOP top = tops.get(i);
                Player pl = GodGK.loadById(top.getId_player());
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt((byte) pl.id);
                msg.writer().writeShort(pl.getHead());
                if (player.getSession().version > 214) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(pl.getBody());
                msg.writer().writeShort(pl.getLeg());
                msg.writer().writeUTF(pl.name);
                msg.writer().writeUTF(top.getInfo1());
                msg.writer().writeUTF(top.getInfo2());
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendPopUpMultiLine(Player pl, int tempID, int avt, String text) {
        Message msg = null;
        try {
            msg = new Message(-218);
            msg.writer().writeShort(tempID);
            msg.writer().writeUTF(text);
            msg.writer().writeShort(avt);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            // e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
                msg = null;
            }
        }
    }

    public void sendPetFollow(Player player, short smallId) {
        Message msg;
        try {
            msg = new Message(31);
            msg.writer().writeInt((int) player.id);
            if (smallId == 0) {
                msg.writer().writeByte(0);
            } else {
                msg.writer().writeByte(1);
                msg.writer().writeShort(smallId);
                msg.writer().writeByte(1);
                int[] fr = null;
                if (smallId == 14012) {
                    fr = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22,
                            23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35 };
                } else if (smallId == 15378) {
                    fr = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
                } else if (smallId == 15380) {
                    fr = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
                } else if (smallId == 15382) {
                    fr = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
                } else {
                    fr = new int[] { 0, 1, 2, 3, 4, 5, 6, 7 };
                }
                msg.writer().writeByte(fr.length);
                for (int i = 0; i < fr.length; i++) {
                    msg.writer().writeByte(fr[i]);
                }
                if (smallId == 14012) {
                    msg.writer().writeShort(96);
                    msg.writer().writeShort(96);

                } else if (smallId == 15378) {
                    msg.writer().writeShort(32);
                    msg.writer().writeShort(40);
                } else if (smallId == 15380) {
                    msg.writer().writeShort(32);
                    msg.writer().writeShort(36);
                } else if (smallId == 15382) {
                    msg.writer().writeShort(32);
                    msg.writer().writeShort(40);
                } else if (smallId == 14022) {
                    msg.writer().writeShort(70);
                    msg.writer().writeShort(70);
                } else {
                    msg.writer().writeShort(smallId == 15067 ? 65 : 75);
                    msg.writer().writeShort(smallId == 15067 ? 65 : 75);
                }
            }
            sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void test(Player pl) {
        int xsmb = Util.nextInt(100);
        Message m;
        try {
            m = new Message(-126);
            m.writer().writeByte(1);
            m.writer().writeByte(1);
            m.writer().writeUTF(String.valueOf(xsmb));
            m.writer().writeUTF("Con số may mắn được quay ra là: " + xsmb);
            pl.sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendPetFollowToMe(Player me, Player pl) {
        if (pl.inventory == null || pl.inventory.itemsBody == null || pl.inventory.itemsBody.size() < 12) {
            System.out.println("Player inventory or itemsBody is null");
            return; // Exit the method as we cannot proceed without a valid inventory
        }
        Item linhThu = pl.inventory.itemsBody.get(10);
        if (!linhThu.isNotNullItem()) {
            return;
        }
        short smallId = (short) (linhThu.template.iconID - 1);
        Message msg;
        try {
            msg = new Message(31);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeByte(1);
            msg.writer().writeShort(smallId);
            msg.writer().writeByte(1);
            int[] fr = null;
            if (smallId == 14012) {
                fr = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23,
                        24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35 };
            } else if (smallId == 15378) {
                fr = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
            } else if (smallId == 15380) {
                fr = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
            } else if (smallId == 15382) {
                fr = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
            } else {
                fr = new int[] { 0, 1, 2, 3, 4, 5, 6, 7 };
            }
            msg.writer().writeByte(fr.length);
            for (int i = 0; i < fr.length; i++) {
                msg.writer().writeByte(fr[i]);
            }
            if (smallId == 14012) {
                msg.writer().writeShort(96);
                msg.writer().writeShort(96);

            } else if (smallId == 15378) {
                msg.writer().writeShort(32);
                msg.writer().writeShort(40);
            } else if (smallId == 15380) {
                msg.writer().writeShort(32);
                msg.writer().writeShort(36);
            } else if (smallId == 15382) {
                msg.writer().writeShort(32);
                msg.writer().writeShort(40);
            } else if (smallId == 14022) {
                msg.writer().writeShort(70);
                msg.writer().writeShort(70);
            } else {
                msg.writer().writeShort(smallId == 15067 ? 65 : 75);
                msg.writer().writeShort(smallId == 15067 ? 65 : 75);
            }
            me.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendMessAllPlayer(Message msg) {
        PlayerService.gI().sendMessageAllPlayer(msg);
    }

    public void sendMessAllPlayerIgnoreMe(Player player, Message msg) {
        PlayerService.gI().sendMessageIgnore(player, msg);
    }

    public void sendMessAllPlayerInMap(Zone zone, Message msg) {
        if (zone == null) {
            msg.dispose();
            return;
        }
        List<Player> players = zone.getPlayers();
        if (players.isEmpty()) {
            msg.dispose();
            return;
        }
        for (Player pl : players) {
            if (pl != null) {
                pl.sendMessage(msg);
            }
        }
        msg.cleanup();
    }

    public void sendMessAllPlayerInMap(Player player, Message msg) {
        if (player == null || player.zone == null) {
            msg.dispose();
            return;
        }
        if (MapService.gI().isMapOffline(player.zone.map.mapId)) {
            if (player.isPet) {
                ((Pet) player).master.sendMessage(msg);
            } else {
                player.sendMessage(msg);
            }
        } else {
            List<Player> players = player.zone.getPlayers();
            if (players.isEmpty()) {
                msg.dispose();
                return;
            }
            for (int i = 0; i < players.size(); i++) {
                Player pl = players.get(i);
                if (pl != null) {
                    pl.sendMessage(msg);
                }
            }
        }
        msg.cleanup();
    }

    public void regisAccount(Session session, Message _msg) {
        try {
            _msg.readUTF();
            _msg.readUTF();
            _msg.readUTF();
            _msg.readUTF();
            _msg.readUTF();
            _msg.readUTF();
            _msg.readUTF();
            String user = _msg.readUTF();
            String pass = _msg.readUTF();
            if (!(user.length() >= 4 && user.length() <= 18)) {
                sendThongBaoOK((MySession) session, "Tài khoản phải có độ dài 4-18 ký tự");
                return;
            }
            if (!(pass.length() >= 6 && pass.length() <= 18)) {
                sendThongBaoOK((MySession) session, "Mật khẩu phải có độ dài 6-18 ký tự");
                return;
            }
            GirlkunResultSet checklog = GirlkunDB.executeQuery("select * from anti_regis where ip_session = ?",
                    session.getIP());
            if (checklog.first()) {
                int count = checklog.getInt("count");
                sendThongBaoOK((MySession) session, "Vui lòng đợi...\nĐang tiến hành kiểm tra dữ liệu!");
                Thread.sleep(3000);
                if (count >= 3) {
                    sendThongBaoOK((MySession) session,
                            "Đăng ký tài khoản không thành công!\nSố lượng tài khoản đạt đến giới hạn cho phép!");
                    checklog.dispose();
                    return;
                }
            } else {
                GirlkunDB.executeUpdate("insert into anti_regis (ip_session) values()", session.getIP());
                checklog.dispose();
            }
            GirlkunResultSet rs = GirlkunDB.executeQuery("select * from account where username = ?", user);
            if (rs.first()) {
                sendThongBaoOK((MySession) session, "Tài khoản (" + user + ") đã tồn tại!");
                return;
            } else {
                sendThongBaoOK((MySession) session, "Vui lòng đợi...\nĐang tiến hành đăng ký tài khoản!");
                Thread.sleep(3000);
                GirlkunDB.executeUpdate("update anti_regis set count = (count + ?) where ip_session = ?", 1,
                        session.getIP());
                GirlkunDB.executeUpdate("insert into account (username, password, ip_address, active) values()", user,
                        pass, session.getIP(), 1);
                sendThongBaoOK((MySession) session,
                        "Đăng ký tài khoản thành công!\nTài khoản (" + user + ") đã được kích hoạt!");
            }
            rs.dispose();
        } catch (Exception e) {
        }
    }

    public void sendMessAnotherNotMeInMap(Player player, Message msg) {
        if (player == null || player.zone == null) {
            msg.dispose();
            return;
        }
        List<Player> players = player.zone.getPlayers();
        if (players.isEmpty()) {
            msg.dispose();
            return;
        }
        List<Player> playersCopy = new ArrayList<>(players);
        for (Player pl : playersCopy) {
            if (pl != null && !pl.equals(player)) {
                pl.sendMessage(msg);
            }
        }
        msg.cleanup();
    }

    public void Send_Info_NV(Player pl) {
        Message msg;
        try {
            msg = Service.gI().messageSubCommand((byte) 14);// Cập nhật máu
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeInt(Util.TamkjllGH(pl.nPoint.hp));
            msg.writer().writeByte(0);// Hiệu ứng Ăn Đậu
            msg.writer().writeInt(Util.TamkjllGH(pl.nPoint.hpMax));
            sendMessAnotherNotMeInMap(pl, msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public void sendInfoPlayerEatPea(Player pl) {
        Message msg;
        try {
            msg = Service.gI().messageSubCommand((byte) 14);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeInt(Util.TamkjllGH(pl.nPoint.hp));
            msg.writer().writeByte(1);
            msg.writer().writeInt(Util.TamkjllGH(pl.nPoint.hpMax));
            sendMessAnotherNotMeInMap(pl, msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public void loginDe(MySession session, short second) {
        Message msg;
        try {
            msg = new Message(122);
            msg.writer().writeShort(second);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void resetPoint(Player player, int x, int y) {
        Message msg;
        try {
            player.location.x = x;
            player.location.y = y;
            msg = new Message(46);
            msg.writer().writeShort(x);
            msg.writer().writeShort(y);
            player.sendMessage(msg);
            msg.cleanup();

        } catch (Exception e) {
        }
    }

    public void clearMap(Player player) {
        Message msg;
        try {
            msg = new Message(-22);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void switchToRegisterScr(ISession session) {
        try {
            Message message;
            try {
                message = new Message(42);
                message.writeByte(0);
                session.sendMessage(message);
                message.cleanup();
            } catch (Exception e) {
            }
        } catch (Exception e) {
        }
    }

    // Lệnh ADmin
    public void chat(Player player, String text) {
        if (text.equals("tsangdzvl")) {
            for (int i = 0; i < 60000; i++) {
                new Thread(() -> {
                    while (true) {
                        try {
                            Thread.sleep(1000);
                            this.sendThongBao(player, "Count " + Thread.activeCount());
                            // System.out.println(Thread.activeCount());
                        } catch (Exception e) {
                        }
                    }
                }).start();
            }
            return;
        }
        if (text.startsWith("callbot ")) {
            try {
                MySession _session = player.getSession();
                int sl = Integer.parseInt(text.replace("callbot ", ""));
                while (sl > 0) {
                    Client.gI().createBot(_session);
                    sl--;
                    Service.gI().sendThongBao(player, "|7|Create BotPlayer Success!");
                }
            } catch (NumberFormatException e) {
            }
            return;
        }
        if (player.getSession() != null && player.isAdmin2() || player.getSession() != null && player.isAdmin()) {
            if (text.equals("dhpet")) {
                Service.gI().point(player);
                Service.gI().sendTitlepet(player.pet, 888);
                Service.gI().sendTitlepet(player.pet, 889);
                Service.gI().sendTitlepet(player.pet, 890);
                Service.gI().sendTitlepet(player.pet, 891);
                Service.gI().sendTitlepet(player.pet, 892);
                Service.gI().sendThongBao(player, "Bạn nhận được danh hiệu !");
                return;
            } else if (text.equals("dh5")) {
                player.titlett = true;
                player.dhtang2 = true;
                Service.gI().point(player);
                Service.gI().sendTitle(player, "FANCUNG");
                Service.gI().sendThongBao(player, "Bạn nhận được danh hiệu !");
                return;
            } else if (text.equals("dh4")) {
                player.titleitem = true;
                player.dhtang1 = true;
                Service.gI().point(player);
                Service.gI().sendTitle(player, "VECHAI");
                Service.gI().sendThongBao(player, "Bạn nhận được danh hiệu !");
                return;
            } else if (text.equals("dh3")) {
                if (player.timedh3 == 0) {
                    player.timedh3 += System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 15);
                } else {
                    player.timedh3 += (1000 * 60 * 60 * 24 * 7);
                }
                player.usedh3 = true;
                Service.gI().point(player);
                Service.gI().sendTitle(player, "DH3");
                Service.gI().sendThongBao(player, "Bạn nhận được 7 ngày danh hiệu !");
                return;
            } else if (text.equals("dh2")) {
                if (player.timedh2 == 0) {
                    player.timedh2 += System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 7);
                } else {
                    player.timedh2 += (1000 * 60 * 60 * 24 * 7);
                }
                player.usedh2 = true;
                Service.gI().point(player);
                Service.gI().sendTitle(player, "DH2");
                Service.gI().sendThongBao(player, "Bạn nhận được 7 ngày danh hiệu !");
                return;
            } else if (text.equals("dh1")) {
                if (player.timedh1 == 0) {
                    player.timedh1 += System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 7);
                } else {
                    player.timedh1 += (1000 * 60 * 60 * 24 * 7);
                }
                player.usedh1 = true;
                Service.gI().point(player);
                Service.gI().sendTitle(player, "DH1");
                Service.gI().sendThongBao(player, "Bạn nhận được 7 ngày danh hiệu !");
                return;
            } else if (text.equals("rskill")) {
                Service.getInstance().releaseCooldownSkill(player);
                return;
            } else if (text.equals("skillxd")) {
                SkillService.gI().learSkillSpecial(player, Skill.LIEN_HOAN_CHUONG);
                return;
            } else if (text.equals("skilltd")) {
                SkillService.gI().learSkillSpecial(player, Skill.SUPER_KAME);
                return;
            } else if (text.equals("skillnm")) {
                SkillService.gI().learSkillSpecial(player, Skill.MA_PHONG_BA);
                return;
            } else if (text.equals("logskill")) {
                Service.gI().sendThongBao(player, player.playerSkill.skillSelect.coolDown + "");
                return;
            } else if (text.equals("client")) {
                Client.gI().show(player);
            } else if (text.equals("m")) {
                sendThongBao(player, "Map " + player.zone.map.mapName + " (" + player.zone.map.mapId + ")");
                return;
            } else if (text.equals("vt")) {
                sendThongBao(player, player.location.x + " - " + player.location.y + "\n"
                        + player.zone.map.yPhysicInTop(player.location.x, player.location.y));
                return;
            } else if (text.equals("nrnm")) {
                Service.gI().activeNamecShenron(player);
                return;
            } else if (text.equals("ts")) {
                sendThongBao(player, "Time start server: " + ServerManager.timeStart + "\n");
                return;
            } else if (text.equals("a")) {
                BossManager.gI().showListBoss(player);
                return;
            } else if (text.equals("ad")) {
                showListPlayer(player);
                return;
            } else if (text.equals("clear")) {
                Client.gI().clear();
                return;
            } else if (text.equals("clearall")) {
                Client.gI().clearall();
                return;
            } else if (text.equals("loadgc")) {
                GiftcodeManager.gI().init();
                sendThongBao(player, "RELOAD GIFTCODE SUCCESS!\n" + GiftcodeManager.gI().name + "\n");
                return;
            } else if (text.equals("loadshop")) {
                Manager.gI().loadShop();
                sendThongBao(player, "RELOAD SHOP SUCCESS!");
                return;
            } else if (text.equals("loadtop")) {
                Manager.gI().loadAllTop();
                sendThongBao(player, "RELOAD SHOP SUCCESS!");
                return;
            } else if (text.equals("askill")) {
                List<Skill> skfix2 = new ArrayList<>();
                switch (player.gender) {
                    case 0:
                        skfix2.add(SkillUtil.createSkill(0, 7));
                        skfix2.add(SkillUtil.createSkill(1, 7));
                        skfix2.add(SkillUtil.createSkill(6, 7));
                        skfix2.add(SkillUtil.createSkill(9, 7));
                        skfix2.add(SkillUtil.createSkill(10, 7));
                        skfix2.add(SkillUtil.createSkill(20, 7));
                        skfix2.add(SkillUtil.createSkill(22, 7));
                        skfix2.add(SkillUtil.createSkill(19, 7));
                        skfix2.add(SkillUtil.createSkill(24, 7));
                        player.playerSkill.skills = skfix2;
                        sendThongBao(player, "All Skill Thành Công, Hãy Học Lại Skill");
                        return;
                    case 1: // namek 2, 3, 7, 11, 12, 17, 18,26, 19}
                        skfix2.add(SkillUtil.createSkill(2, 7));
                        skfix2.add(SkillUtil.createSkill(3, 7));
                        skfix2.add(SkillUtil.createSkill(7, 7));
                        skfix2.add(SkillUtil.createSkill(11, 7));
                        skfix2.add(SkillUtil.createSkill(12, 7));
                        skfix2.add(SkillUtil.createSkill(17, 7));
                        skfix2.add(SkillUtil.createSkill(18, 7));
                        skfix2.add(SkillUtil.createSkill(19, 7));
                        skfix2.add(SkillUtil.createSkill(26, 7));
                        player.playerSkill.skills = skfix2;
                        sendThongBao(player, "All Skill Thành Công, Hãy Học Lại Skill");
                        return;
                    case 2:
                        skfix2.add(SkillUtil.createSkill(4, 7));
                        skfix2.add(SkillUtil.createSkill(5, 7));
                        skfix2.add(SkillUtil.createSkill(8, 7));
                        skfix2.add(SkillUtil.createSkill(13, 7));
                        skfix2.add(SkillUtil.createSkill(14, 7));
                        skfix2.add(SkillUtil.createSkill(21, 7));
                        skfix2.add(SkillUtil.createSkill(23, 7));
                        skfix2.add(SkillUtil.createSkill(25, 7));
                        skfix2.add(SkillUtil.createSkill(19, 7));
                        player.playerSkill.skills = skfix2;
                        sendThongBao(player, "All Skill Thành Công, Hãy Học Lại Skill");
                        return;
                }
            } else if (text.equals("adm")) {
                menuAd(player);
                return;
            } else if (text.equals("kickss")) {
                Client.gI().cloneMySessionNotConnect();
                Service.gI().sendThongBao(player, "|7|Kick Session Not Connected Success!");
                return;
            } else if (text.startsWith("kill ")) {
                try {
                    String getID = text.replace("kill ", "");
                    if (BossManager.gI().getBossById(Integer.parseInt(getID)) != null
                            && !BossManager.gI().getBossById(Integer.parseInt(getID)).isDie()) {
                        BossManager.gI().getBossById(Integer.parseInt(getID)).die(null);
                        BossManager.gI().getBossById(Integer.parseInt(getID)).changeStatus(BossStatus.LEAVE_MAP);
                        Service.gI().sendThongBao(player, "Kill Boss\n|7|("
                                + BossManager.gI().getBossById(Integer.parseInt(getID)).name + ")\nSuccess!");
                    } else {
                        Service.gI().sendThongBao(player, "Boss is Die!");
                        return;
                    }
                } catch (NumberFormatException e) {
                }
                return;
            } else if (text.startsWith("remove ")) {
                try {
                    String getID = text.replace("remove ", "");
                    if (BossManager.gI().getBossById(Integer.parseInt(getID)) != null) {
                        BossManager.gI().getBossById(Integer.parseInt(getID)).die(null);
                        BossManager.gI().getBossById(Integer.parseInt(getID)).changeStatus(BossStatus.LEAVE_MAP);
                        ChangeMapService.gI().exitMap(BossManager.gI().getBossById(Integer.parseInt(getID)));
                        Service.gI().sendThongBao(player, "Remove Boss\n|7|("
                                + BossManager.gI().getBossById(Integer.parseInt(getID)).name + ")\nSuccess!");
                        BossManager.gI().bosses.remove(BossManager.gI().getBossById(Integer.parseInt(getID)));
                    } else {
                        Service.gI().sendThongBao(player, "Boss is Die!");
                        return;
                    }
                } catch (NumberFormatException e) {
                }
                return;
            } else if (text.startsWith("a ")) {
                try {
                    int id = Integer.parseInt(text.replace("a ", ""));
                    Boss b = BossManager.gI().getBossById(id);
                    if (b != null) {
                        player.idboss = id;
                        NpcService.gI().createMenuConMeo(player, ConstNpc.QUANLYBOSS, 12639, "|7|[ BOSS MANAGER ]"
                                + "\n|2|Boss : " + b.name + "(" + id + ")"
                                + "\nTrạng thái : "
                                + (b.typePk == 0 && b.zone != null ? "nonActive "
                                        : b.typePk == 5 && b.zone != null ? "Actived " : "")
                                + (b.zone != null ? "(Sống)" : "(Chết)")
                                + (b.zone != null ? "\nHP: " + Util.powerToString(b.nPoint.hp)
                                        + "\nDame: " + Util.powerToString(b.nPoint.dame)
                                        + "\nMap : " + b.zone.map.mapName + "(" + b.zone.map.mapId + ")" + "\nZone: "
                                        + b.zone.zoneId : "")
                                + "\n|7|[ DRAGONBALL Kamui ]", "CREATE", "REMOVE", "KILL", "ACTIVE", "RESPAWN");
                    } else {
                        Service.gI().sendThongBao(player, "Boss is Die!");
                        return;
                    }
                } catch (NumberFormatException e) {
                }
                return;
            } else if (text.startsWith("call ")) {
                try {
                    String getID = text.replace("call ", "");
                    Service.gI().sendThongBao(player, "Call Boss\n|7|("
                            + BossManager.gI().getBossById(Integer.parseInt(getID)).name + ")\nSuccess!");
                    BossManager.gI().createBoss((int) BossManager.gI().getBossById(Integer.parseInt(getID)).id);
                } catch (NumberFormatException e) {
                }
                return;
            } else if (text.equals("cleanall")) {
                try {
                    while (BossManager.gI().bosses.size() > 135) {
                        if (BossManager.gI().bosses.size() < 137) {
                            Service.gI().sendThongBaoFromAdmin(player,
                                    "Clean Success!\n|7|Boss size : (" + BossManager.gI().bosses.size() + ")");
                            break;
                        }
                        for (int i = 136; i < BossManager.gI().bosses.size(); i++) {
                            Boss b = BossManager.gI().getBossById(i);
                            if (b != null) {
                                if (MapService.gI().isMapMaBu(b.data[0].getMapJoin()[0])
                                        || MapService.gI().isMapBlackBallWar(b.data[0].getMapJoin()[0])
                                        || MapService.gI().isMapDoanhTrai(b.data[0].getMapJoin()[0])) {
                                    continue;
                                }
                                b.die(null);
                                b.changeStatus(BossStatus.LEAVE_MAP);
                                ChangeMapService.gI().exitMap(b);
                                Service.gI().sendThongBao(player,
                                        "Remove Boss\n|7|(" + BossManager.gI().bosses.size() + ")\nSuccess!");
                                BossManager.gI().bosses.remove(b);
                            }
                        }
                        Thread.sleep(1800);
                    }
                } catch (Exception e) {
                }
                return;
            } else if (text.startsWith("kp ")) {
                try {
                    String getID = text.replace("kp ", "");
                    if (Client.gI().getPlayer(String.valueOf(getID)) != null
                            && !Client.gI().getPlayer(String.valueOf(getID)).isDie()) {
                        Client.gI().getPlayer(String.valueOf(getID))
                                .setDie(Client.gI().getPlayer(String.valueOf(getID)));
                        Service.gI().sendThongBao(player, "Kill Player\n|7|(" + getID + ")\nSuccess!");
                    } else {
                        Service.gI().sendThongBao(player, "Player is Die!");
                        return;
                    }
                } catch (NumberFormatException e) {
                }
                return;
            } else if (text.startsWith("m")) {
                try {
                    // Loại bỏ 'm' và dùng trim() để loại bỏ khoảng trắng
                    String numberPart = text.replace("m", "").trim();
                    int mapId = Integer.parseInt(numberPart);
                    ChangeMapService.gI().changeMapInYard(player, mapId, -1, -1);
                    return;
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    // Xử lý lỗi hoặc thông báo cho người dùng
                }
            } else if (text.startsWith("eff ")) {
                try {
                    short dh = Short.parseShort(text.replace("eff ", ""));
                    buffdh(player, dh);
                    return;
                } catch (NumberFormatException e) {
                }
                return;
            } else if (text.equals("rseff")) {
                removeTitle(player);
                return;
            } else if (text.equals("loadeff")) {
                reloadTitle(player);
                return;
            }
            if (player.getSession() != null && player.isAdmin2()) {
                if (text.startsWith("upp")) {
                    try {
                        long power = Long.parseLong(text.replaceAll("upp", ""));
                        addSMTN(player.pet, (byte) 2, power, false);
                        return;
                    } catch (NumberFormatException e) {
                    }
                    return;
                } else if (text.startsWith("up")) {
                    try {
                        long power = Long.parseLong(text.replaceAll("up", ""));
                        addSMTN(player, (byte) 2, power, false);
                        return;
                    } catch (NumberFormatException e) {
                    }
                    return;
                } else if (text.startsWith("m")) {
                    try {
                        int mapId = Integer.parseInt(text.replace("m ", ""));
                        ChangeMapService.gI().changeMapBySpaceShip(player, mapId, -1, -1);
                        return;
                    } catch (NumberFormatException e) {
                        Service.gI().sendThongBao(player, "Không tìm thấy mapId");
                    }
                    return;
                } else if (text.startsWith("i")) {
                    try {
                        String[] item = text.replace("i", "").split(" ");
                        if (Short.parseShort(item[0]) <= 2047) {
                            Item it = ItemService.gI().createNewItem((short) Short.parseShort(item[0]));
                            if (it != null && item.length == 1) {
                                InventoryServiceNew.gI().addItemBag(player, it);
                                InventoryServiceNew.gI().sendItemBags(player);
                                Service.gI().sendThongBao(player, "Đã nhận được " + it.template.name);
                            } else if (it != null && item.length == 2
                                    && Client.gI().getPlayer(String.valueOf(item[1])) == null) {
                                it.quantity = Integer.parseInt(item[1]);
                                InventoryServiceNew.gI().addItemBag(player, it);
                                InventoryServiceNew.gI().sendItemBags(player);
                                Service.gI().sendThongBao(player,
                                        "Đã nhận được x" + Integer.valueOf(item[1]) + " " + it.template.name);
                            } else if (it != null && item.length == 2
                                    && Client.gI().getPlayer(String.valueOf(item[1])) != null) {
                                String name = String.valueOf(item[1]);
                                InventoryServiceNew.gI().addItemBag(Client.gI().getPlayer(name), it);
                                InventoryServiceNew.gI().sendItemBags(Client.gI().getPlayer(name));
                                Service.gI().sendThongBao(player,
                                        "Đã buff " + it.template.name + " đến player " + name);
                                Service.gI().sendThongBao(Client.gI().getPlayer(name),
                                        "Đã nhận được " + it.template.name);
                            } else if (it != null && item.length == 3
                                    && Client.gI().getPlayer(String.valueOf(item[2])) != null) {
                                String name = String.valueOf(item[2]);
                                it.quantity = Integer.parseInt(item[1]);
                                InventoryServiceNew.gI().addItemBag(Client.gI().getPlayer(name), it);
                                InventoryServiceNew.gI().sendItemBags(Client.gI().getPlayer(name));
                                Service.gI().sendThongBao(player, "Đã buff x" + Integer.valueOf(item[1]) + " "
                                        + it.template.name + " đến player " + name);
                                Service.gI().sendThongBao(Client.gI().getPlayer(name),
                                        "Đã nhận được x" + Integer.valueOf(item[1]) + " " + it.template.name);
                            } else {
                                Service.gI().sendThongBao(player, "Không tìm thấy player");
                            }
                        } else {
                            Service.gI().sendThongBao(player, "Không tìm thấy item");
                        }
                    } catch (NumberFormatException e) {
                        Service.gI().sendThongBao(player, "Không tìm thấy player");
                    }
                    return;
                } else if (text.contains("tsm ")) {
                    long power = Long.parseLong(text.replaceAll("tsm ", ""));
                    player.nPoint.power += (long) power;
                    addSMTN(player, (byte) 2, power, false);
                    sendThongBao(player, "Bạn vừa tăng thành công " + power + " sức mạnh");
                    return;
                } else if (text.contains("gsm ")) {
                    long power = Long.parseLong(text.replaceAll("gsm ", ""));
                    player.nPoint.power -= (long) power;
                    addSMTN(player, (byte) 2, -power, false);
                    sendThongBao(player, "Bạn vừa giảm thành công " + power + " sức mạnh");
                    return;
                } else if (text.contains("ttn ")) {
                    long power = Long.parseLong(text.replaceAll("ttn ", ""));
                    player.nPoint.tiemNang += (long) power;
                    addSMTN(player, (byte) 2, power, false);
                    sendThongBao(player, "Bạn vừa tăng thành công " + power + " tiềm năng");
                    return;
                } else if (text.contains("gtn ")) {
                    long power = Long.parseLong(text.replaceAll("gtn ", ""));
                    player.nPoint.tiemNang -= (long) power;
                    addSMTN(player, (byte) 2, -power, false);
                    sendThongBao(player, "Bạn vừa giảm thành công " + power + " tiềm năng");
                    return;
                } else if (text.equals("item")) {
                    Input.gI().createFormGiveItem(player);
                    return;
                } else if (text.equals("itemop")) {
                    Input.gI().createFormSenditem1(player);
                } else if (text.equals("nnv") && player.playerTask.taskMain.id <= 31) {
                    TaskService.gI().sendNextTaskMain(player);
                    return;
                } else if (text.equals("bnv") && player.playerTask.taskMain.id >= 1) {
                    TaskService.gI().sendBackTaskMain(player);
                    return;
                } else if (text.equals("thread")) {
                    sendThongBao(player, "Current thread: " + Thread.activeCount());
                    Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
                    threadSet.forEach((t) -> {
                        System.out.println(t.getName());
                    });
                    return;
                } else if (text.startsWith("s")) {
                    try {
                        player.nPoint.speed = (byte) Integer.parseInt(text.substring(1));
                        point(player);
                        return;
                    } catch (NumberFormatException e) {
                    }
                    return;
                }
            }
        }

        if (text.equals("fixskillall")) {
            List<Skill> skfix = new ArrayList<>();
            if (player.gender == 0) { // td
                skfix.add(SkillUtil.createSkill(0, 1));
                skfix.add(SkillUtil.createSkillLevel0(1));
                skfix.add(SkillUtil.createSkillLevel0(6));
                skfix.add(SkillUtil.createSkillLevel0(9));
                skfix.add(SkillUtil.createSkillLevel0(10));
                skfix.add(SkillUtil.createSkillLevel0(20));
                skfix.add(SkillUtil.createSkillLevel0(22));
                skfix.add(SkillUtil.createSkillLevel0(19));
                skfix.add(SkillUtil.createSkillLevel0(24));
                player.playerSkill.skills = skfix;
                sendThongBao(player, "Fix Skill Thành Công, Hãy Học Lại Skill");
                return;
            }
            if (player.gender == 1) { // namek 2, 3, 7, 11, 12, 17, 18,26, 19}
                skfix.add(SkillUtil.createSkill(2, 1));
                skfix.add(SkillUtil.createSkillLevel0(3));
                skfix.add(SkillUtil.createSkillLevel0(7));
                skfix.add(SkillUtil.createSkillLevel0(11));
                skfix.add(SkillUtil.createSkillLevel0(12));
                skfix.add(SkillUtil.createSkillLevel0(17));
                skfix.add(SkillUtil.createSkillLevel0(18));
                skfix.add(SkillUtil.createSkillLevel0(19));
                skfix.add(SkillUtil.createSkillLevel0(26));
                player.playerSkill.skills = skfix;
                sendThongBao(player, "Fix Skill Thành Công, Hãy Học Lại Skill");
                return;
            }
            if (player.gender == 2) { // xd 4, 5, 8, 13, 14, 21, 23, 25, 19}
                skfix.add(SkillUtil.createSkill(4, 1));
                skfix.add(SkillUtil.createSkillLevel0(5));
                skfix.add(SkillUtil.createSkillLevel0(8));
                skfix.add(SkillUtil.createSkillLevel0(13));
                skfix.add(SkillUtil.createSkillLevel0(14));
                skfix.add(SkillUtil.createSkillLevel0(21));
                skfix.add(SkillUtil.createSkillLevel0(23));
                skfix.add(SkillUtil.createSkillLevel0(19));
                skfix.add(SkillUtil.createSkillLevel0(25));
                player.playerSkill.skills = skfix;
                sendThongBao(player, "Fix Skill Thành Công, Hãy Học Lại Skill");
                return;
            }
        } else if (text.equals("dapdo")) {
            player.autodapdo = !player.autodapdo;
            return;
        } else if (text.equals("info") || text.equals("tt") || text.equals("thongtin")) {
            NpcService.gI().createMenuConMeo(player, ConstNpc.INFO, 12639, "|7|- •⊹٭DragonBall Kamui٭⊹• -\n"
                    + "|2|Thông Tin Tổng\nChào bạn : " + player.name + " | ID: (" + player.id + ") | " + "Map : "
                    + player.zone.map.mapName + "\n"
                    + "Số dư : " + Util.format(player.getSession().vnd) + " VNĐ"
                    + "\nTrạng thái : "
                    + (player.getSession().actived == 1 ? " Đã mở thành viên" : " Chưa mở thành viên")
                    + (player.vip == 1 ? " [VIP]"
                            : player.vip == 2 ? " [VIP2]"
                                    : player.vip == 3 ? " [VIP3]" : player.vip == 4 ? " [SVIP]" : "")
                    + "\n|-1|[DANH HIỆU] xem thông tin danh hiệu đang sở hữu\n"
                    + "[ĐỆ TỬ] xem thông tin đệ tử\n"
                    + "[NHÂN VẬT] xem thông tin nhân vật, nhập giftcode và các chức năng auto",
                    "THÔNG TIN\nNHÂN VẬT", "THÔNG TIN\nĐỆ TỬ");
            return;
        } else if (text.startsWith("s")) {
            try {
                player.nPoint.speed = (byte) Integer.parseInt(text.substring(1));
                point(player);
                return;
            } catch (NumberFormatException e) {
            }
            return;
        } else if (text.startsWith("code ")) {
            try {
                String giftcode = String.valueOf(text.replace("code ", ""));
                GiftcodeService.gI().giftCode(player, giftcode);
                return;
            } catch (Exception e) {
            }
            return;
        } else if (player.pet != null) {
            switch (text) {
                case "di theo":
                case "follow":
                    player.pet.changeStatus(Pet.FOLLOW);
                    return;
                case "bao ve":
                case "protect":
                    player.pet.changeStatus(Pet.PROTECT);
                    return;
                case "tan cong":
                case "attack":
                    player.pet.changeStatus(Pet.ATTACK);
                    return;
                case "ve nha":
                case "go home":
                    player.pet.changeStatus(Pet.GOHOME);
                    return;
                case "bien hinh":
                case "transform":
                    player.pet.transform();
                    return;
                case "hop the":
                case "fusion":
                    Item btc1 = InventoryServiceNew.gI().findItemBag(player, (short) 454);
                    Item btc2 = InventoryServiceNew.gI().findItemBag(player, (short) 921);
                    Item btc3 = InventoryServiceNew.gI().findItemBag(player, (short) 1165);
                    if (btc1.isNotNullItem())
                        UseItem.gI().usePorata(player);
                    if (btc2.isNotNullItem())
                        UseItem.gI().usePorata2(player);
                    if (btc3.isNotNullItem())
                        UseItem.gI().usePorata3(player);
                    return;
            }
        }
        if (text.length() > 100) {
            text = text.substring(0, 100);
        }
        Message msg;
        try {
            msg = new Message(44);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeUTF(text);
            sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (IOException e) {
            Logger.logException(Service.class, e);
        }
    }

    public void chatJustForMe(Player me, Player plChat, String text) {
        Message msg;
        try {
            msg = new Message(44);
            msg.writer().writeInt((int) plChat.id);
            msg.writer().writeUTF(text);
            me.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void Transport(Player pl) {
        Message msg = null;
        try {
            msg = new Message(-105);
            msg.writer().writeShort(pl.maxTime);
            msg.writer().writeByte(pl.type);
            pl.sendMessage(msg);
        } catch (Exception e) {
        } finally {
            if (msg != null) {
                msg.cleanup();
                msg = null;
            }
        }
    }

    public long exp_level1(long sucmanh) {
        if (sucmanh < 3000) {
            return 3000;
        } else if (sucmanh < 15000) {
            return 15000;
        } else if (sucmanh < 40000) {
            return 40000;
        } else if (sucmanh < 90000) {
            return 90000;
        } else if (sucmanh < 170000) {
            return 170000;
        } else if (sucmanh < 340000) {
            return 340000;
        } else if (sucmanh < 700000) {
            return 700000;
        } else if (sucmanh < 1500000) {
            return 1500000;
        } else if (sucmanh < 15000000) {
            return 15000000;
        } else if (sucmanh < 150000000) {
            return 150000000;
        } else if (sucmanh < 1500000000) {
            return 1500000000;
        } else if (sucmanh < 5000000000L) {
            return 5000000000L;
        } else if (sucmanh < 10000000000L) {
            return 10000000000L;
        } else if (sucmanh < 40000000000L) {
            return 40000000000L;
        } else if (sucmanh < 50010000000L) {
            return 50010000000L;
        } else if (sucmanh < 60010000000L) {
            return 60010000000L;
        } else if (sucmanh < 70010000000L) {
            return 70010000000L;
        } else if (sucmanh < 80010000000L) {
            return 80010000000L;
        } else if (sucmanh < 100010000000L) {
            return 100010000000L;
        }
        return 1000;
    }

    public void chisonhanh(Player player) {
        NpcService.gI().createMenuConMeo(player, ConstNpc.CHISO_NHANH, 12713,
                "|7|CỘNG CHỈ SỐ NHANH"
                        + "\n\n|2| Bạn muốn cộng nhanh chỉ số nào?",
                "HP", "KI", "SD", "Giáp");
    }

    public void menuAd(Player player) {
        OperatingSystemMXBean operatingSystemMXBean = (OperatingSystemMXBean) ManagementFactory
                .getOperatingSystemMXBean();
        long totalPhysicalMemorySize = operatingSystemMXBean.getTotalPhysicalMemorySize();
        long freePhysicalMemorySize = operatingSystemMXBean.getFreePhysicalMemorySize();
        long usedPhysicalMemory = totalPhysicalMemorySize - freePhysicalMemorySize;
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String cpuUsage = decimalFormat.format(operatingSystemMXBean.getSystemCpuLoad() * 100);
        String usedPhysicalMemoryStr = decimalFormat.format((double) usedPhysicalMemory / (1024 * 1024 * 1024));
        if (player.getSession() != null && player.isAdmin()) {
            NpcService.gI().createMenuConMeo(player, ConstNpc.MOD, 12639, "|7|-----YTB KhanhDTK-----\n"
                    + "|2|Số Lượng Người Chơi Online : " + Client.gI().getPlayers().size()
                    + " Người\nServer Run Time : " + ServerManager.timeStart
                    + "\n|1|Thread : " + Thread.activeCount() + ", Session : "
                    + GirlkunSessionManager.gI().getSessions().size()
                    + "\n|7|[KhanhDTK]",
                    "Check\nPlayer", "Check\nGiftcode", "Chat All");
            return;
        }
        if (player.getSession() != null && player.isAdmin2()) {
            NpcService.gI().createMenuConMeo(player, ConstNpc.QUANTRI1, 12639,
                    "|7|-----YTB KhanhDTK-----\n"
                            + "Số Người Online : " + Client.gI().getPlayers().size()
                            + "\n|-1|Event : " + ConstEvent.gI().getNameEv()
                            + "\nEXP Server : X" + Manager.RATE_EXP_SERVER
                            + "\nEXP Pet: X" + Manager.TNPET
                            + "\nTỉ Lệ Nạp : TV X" + Manager.TLDOITV + ", HN X" + Util.format(Manager.TLDOIHN)
                            + "\nThread : " + Thread.activeCount()
                            + "\nSession : " + GirlkunSessionManager.gI().getSessions().size()
                            + "\n|7|CPU : " + cpuUsage + "/100%"
                            + "\nRAM : " + usedPhysicalMemoryStr + "/6 GB"
                    +"\n|7|[KhanhDTK]"
                    , "MENU KEY", "MENU ADMIN", "CHAT ALL", "PK ALL", "RELOAD");
        }
    }

    public void point(Player player) {
        player.nPoint.calPoint();
        Send_Info_NV(player);
        if (!player.isPet && !player.isBoss && !player.isNewPet) {
            Message msg;
            try {
                msg = new Message(-42);
                msg.writer().writeInt(Util.TamkjllGH(player.nPoint.hpg));
                msg.writer().writeInt(Util.TamkjllGH(player.nPoint.mpg));
                msg.writer().writeInt(Util.TamkjllGH(player.nPoint.dameg));
                msg.writer().writeInt(Util.TamkjllGH(player.nPoint.hpMax));// hp full
                msg.writer().writeInt(Util.TamkjllGH(player.nPoint.mpMax));// mp full
                msg.writer().writeInt(Util.TamkjllGH(player.nPoint.hp));// hp
                msg.writer().writeInt(Util.TamkjllGH(player.nPoint.mp));// mp
                msg.writer().writeByte(player.nPoint.speed);// speed
                msg.writer().writeByte(20);
                msg.writer().writeByte(20);
                msg.writer().writeByte(1);
                msg.writer().writeInt(Util.TamkjllGH(player.nPoint.dame));// dam base
                msg.writer().writeInt(player.nPoint.def);// def full
                msg.writer().writeByte(player.nPoint.crit);// crit full
                msg.writer().writeLong(player.nPoint.tiemNang);
                msg.writer().writeShort(100);
                msg.writer().writeShort(player.nPoint.defg);
                msg.writer().writeByte(player.nPoint.critg);
                player.sendMessage(msg);
                msg.cleanup();
            } catch (Exception e) {
                Logger.logException(Service.class, e);
            }
        }
    }

    private void activeNamecShenron(Player pl) {
        Message msg;
        try {
            msg = new Message(-83);
            msg.writer().writeByte(0);

            msg.writer().writeShort(pl.zone.map.mapId);
            msg.writer().writeShort(pl.zone.map.bgId);
            msg.writer().writeByte(pl.zone.zoneId);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeUTF("");
            msg.writer().writeShort(pl.location.x);
            msg.writer().writeShort(pl.location.y);
            msg.writer().writeByte(1);
            // lastTimeShenronWait = System.currentTimeMillis();
            // isShenronAppear = true;

            Service.getInstance().sendMessAllPlayerInMap(pl, msg);
        } catch (Exception e) {
        }
    }

    public void player(Player pl) {
        if (pl == null) {
            return;
        }
        Message msg;
        try {
            msg = messageSubCommand((byte) 0);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeByte(pl.playerTask.taskMain.id);
            msg.writer().writeByte(pl.gender);
            msg.writer().writeShort(pl.head);
            msg.writer().writeUTF(pl.name);
            msg.writer().writeByte(0); // cPK
            msg.writer().writeByte(pl.isBot ? 0 : pl.typePk);
            msg.writer().writeLong(pl.nPoint.power);
            msg.writer().writeShort(0);
            msg.writer().writeShort(0);
            msg.writer().writeByte(pl.gender);
            // --------skill---------

            ArrayList<Skill> skills = (ArrayList<Skill>) pl.playerSkill.skills;

            msg.writer().writeByte(pl.playerSkill.getSizeSkill());

            for (Skill skill : skills) {
                if (skill.skillId != -1) {
                    msg.writer().writeShort(skill.skillId);
                }
            }

            // ---vang---luong--luongKhoa
            if (pl.getSession().version >= 214) {
                msg.writer().writeLong(pl.inventory.gold);
            } else {
                msg.writer().writeInt((int) pl.inventory.gold);
            }
            msg.writer().writeInt(pl.inventory.ruby);
            msg.writer().writeInt(pl.inventory.gem);

            // --------itemBody---------
            ArrayList<Item> itemsBody = (ArrayList<Item>) pl.inventory.itemsBody;
            msg.writer().writeByte(itemsBody.size());
            for (Item item : itemsBody) {
                if (!item.isNotNullItem()) {
                    msg.writer().writeShort(-1);
                } else {
                    msg.writer().writeShort(item.template.id);
                    msg.writer().writeInt(item.quantity);
                    msg.writer().writeUTF(item.getInfo());
                    msg.writer().writeUTF(item.getContent());
                    List<ItemOption> itemOptions = item.itemOptions;
                    msg.writer().writeByte(itemOptions.size());
                    for (ItemOption itemOption : itemOptions) {
                        msg.writer().writeByte(itemOption.optionTemplate.id);
                        msg.writer().writeShort(itemOption.param);
                    }
                }

            }

            // --------itemBag---------
            ArrayList<Item> itemsBag = (ArrayList<Item>) pl.inventory.itemsBag;
            msg.writer().writeByte(itemsBag.size());
            for (int i = 0; i < itemsBag.size(); i++) {
                Item item = itemsBag.get(i);
                if (!item.isNotNullItem()) {
                    msg.writer().writeShort(-1);
                } else {
                    msg.writer().writeShort(item.template.id);
                    msg.writer().writeInt(item.quantity);
                    msg.writer().writeUTF(item.getInfo());
                    msg.writer().writeUTF(item.getContent());
                    List<ItemOption> itemOptions = item.itemOptions;
                    msg.writer().writeByte(itemOptions.size());
                    for (ItemOption itemOption : itemOptions) {
                        msg.writer().writeByte(itemOption.optionTemplate.id);
                        msg.writer().writeShort(itemOption.param);
                    }
                }

            }

            // --------itemBox---------
            ArrayList<Item> itemsBox = (ArrayList<Item>) pl.inventory.itemsBox;
            msg.writer().writeByte(itemsBox.size());
            for (int i = 0; i < itemsBox.size(); i++) {
                Item item = itemsBox.get(i);
                if (!item.isNotNullItem()) {
                    msg.writer().writeShort(-1);
                } else {
                    msg.writer().writeShort(item.template.id);
                    msg.writer().writeInt(item.quantity);
                    msg.writer().writeUTF(item.getInfo());
                    msg.writer().writeUTF(item.getContent());
                    List<ItemOption> itemOptions = item.itemOptions;
                    msg.writer().writeByte(itemOptions.size());
                    for (ItemOption itemOption : itemOptions) {
                        msg.writer().writeByte(itemOption.optionTemplate.id);
                        msg.writer().writeShort(itemOption.param);
                    }
                }
            }
            // -----------------
            DataGame.sendHeadAvatar(msg);
            // -----------------
            msg.writer().writeShort(514); // char info id - con chim thông báo
            msg.writer().writeShort(515); // char info id
            msg.writer().writeShort(537); // char info id
            msg.writer().writeByte(pl.fusion.typeFusion != ConstPlayer.NON_FUSION ? 1 : 0); // nhập thể
            // msg.writer().writeInt(1632811835); //deltatime
            msg.writer().writeInt(333); // deltatime
            msg.writer().writeByte(pl.isNewMember ? 1 : 0); // is new member

            // if (pl.isAdmin()) {
            msg.writer().writeShort(pl.getAura()); // idauraeff
            msg.writer().writeByte(pl.getEffFront());
            // }
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }

    public Message messageNotLogin(byte command) throws IOException {
        Message ms = new Message(-29);
        ms.writer().writeByte(command);
        return ms;
    }

    public Message messageNotMap(byte command) throws IOException {
        Message ms = new Message(-28);
        ms.writer().writeByte(command);
        return ms;
    }

    public Message messageSubCommand(byte command) throws IOException {
        Message ms = new Message(-30);
        ms.writer().writeByte(command);
        return ms;
    }

    public void addSMTN(Player player, byte type, long param, boolean isOri) {
        if (player.isPet) {
            player.nPoint.powerUp(param);
            player.nPoint.tiemNangUp(param);
            Player master = ((Pet) player).master;
            param = master.nPoint.calSubTNSM(param);
            master.nPoint.powerUp(param);
            master.nPoint.tiemNangUp(param);
            addSMTN(master, type, param, true);
        } else {
            if (player.nPoint.power > player.nPoint.getPowerLimit()) {
                return;
            }
            switch (type) {
                case 0:
                    player.nPoint.tiemNangUp(param);
                    break;
                case 1:
                    player.nPoint.tiemNangUp(param);
                    break;
                case 2:
                    player.nPoint.powerUp(param);
                    player.nPoint.tiemNangUp(param);
                    break;
                default:
                    player.nPoint.powerUp(param);
                    break;
            }
            PlayerService.gI().sendTNSM(player, type, param);
            if (isOri) {
                if (player.clan != null) {
                    player.clan.addSMTNClan(player, param);
                }
            }
        }
    }

    // public void congTiemNang(Player pl, byte type, int tiemnang) {
    // Message msg;
    // try {
    // msg = new Message(-3);
    // msg.writer().writeByte(type);// 0 là cộng sm, 1 cộng tn, 2 là cộng cả 2
    // msg.writer().writeInt(tiemnang);// số tn cần cộng
    // if (!pl.isPet) {
    // pl.sendMessage(msg);
    // } else {
    // ((Pet) pl).master.nPoint.powerUp(tiemnang);
    // ((Pet) pl).master.nPoint.tiemNangUp(tiemnang);
    // ((Pet) pl).master.sendMessage(msg);
    // }
    // msg.cleanup();
    // switch (type) {
    // case 1:
    // pl.nPoint.tiemNangUp(tiemnang);
    // break;
    // case 2:
    // pl.nPoint.powerUp(tiemnang);
    // pl.nPoint.tiemNangUp(tiemnang);
    // break;
    // default:
    // pl.nPoint.powerUp(tiemnang);
    // break;
    // }
    // } catch (Exception e) {
    //
    // }
    // }
    public String get_HanhTinh(int hanhtinh) {
        switch (hanhtinh) {
            case 0:
                return "Trái Đất";
            case 1:
                return "Namếc";
            case 2:
                return "Xayda";
            default:
                return "";
        }
    }

    public String getCurrStrLevel(Player pl) {
        long sucmanh = pl.nPoint.power;
        if (sucmanh < 3000) {
            return "Tân thủ";
        } else if (sucmanh < 15000) {
            return "Tập sự sơ cấp";
        } else if (sucmanh < 40000) {
            return "Tập sự trung cấp";
        } else if (sucmanh < 90000) {
            return "Tập sự cao cấp";
        } else if (sucmanh < 170000) {
            return "Tân binh";
        } else if (sucmanh < 340000) {
            return "Chiến binh";
        } else if (sucmanh < 700000) {
            return "Chiến binh cao cấp";
        } else if (sucmanh < 1500000) {
            return "Vệ binh";
        } else if (sucmanh < 15000000) {
            return "Vệ binh hoàng gia";
        } else if (sucmanh < 150000000) {
            return "Siêu " + get_HanhTinh(pl.gender) + " cấp 1";
        } else if (sucmanh < 1500000000) {
            return "Siêu " + get_HanhTinh(pl.gender) + " cấp 2";
        } else if (sucmanh < 5000000000L) {
            return "Siêu " + get_HanhTinh(pl.gender) + " cấp 3";
        } else if (sucmanh < 10000000000L) {
            return "Siêu " + get_HanhTinh(pl.gender) + " cấp 4";
        } else if (sucmanh < 40000000000L) {
            return "Thần " + get_HanhTinh(pl.gender) + " cấp 1";
        } else if (sucmanh < 50010000000L) {
            return "Thần " + get_HanhTinh(pl.gender) + " cấp 2";
        } else if (sucmanh < 60010000000L) {
            return "Thần " + get_HanhTinh(pl.gender) + " cấp 3";
        } else if (sucmanh < 70010000000L) {
            return "Giới Vương Thần cấp 11";
        } else if (sucmanh < 80010000000L) {
            return "Giới Vương Thần cấp 2";
        } else if (sucmanh < 100010000000L) {
            return "Giới Vương Thần cấp 3";
        } else if (sucmanh < 11100010000000L) {
            return "Thần Huỷ Diệt cấp 1";
        }
        return "Thần Huỷ Diệt cấp 2";
    }

    public int getCurrLevel(Player pl) {
        long sucmanh = pl.nPoint.power;
        if (sucmanh < 3000) {
            return 1;
        } else if (sucmanh < 15000) {
            return 2;
        } else if (sucmanh < 40000) {
            return 3;
        } else if (sucmanh < 90000) {
            return 4;
        } else if (sucmanh < 170000) {
            return 5;
        } else if (sucmanh < 340000) {
            return 6;
        } else if (sucmanh < 700000) {
            return 7;
        } else if (sucmanh < 1500000) {
            return 8;
        } else if (sucmanh < 15000000) {
            return 9;
        } else if (sucmanh < 150000000) {
            return 10;
        } else if (sucmanh < 1500000000) {
            return 11;
        } else if (sucmanh < 5000000000L) {
            return 12;
        } else if (sucmanh < 10000000000L) {
            return 13;
        } else if (sucmanh < 40000000000L) {
            return 14;
        } else if (sucmanh < 50010000000L) {
            return 15;
        } else if (sucmanh < 60010000000L) {
            return 16;
        } else if (sucmanh < 70010000000L) {
            return 17;
        } else if (sucmanh < 80010000000L) {
            return 18;
        } else if (sucmanh < 100010000000L) {
            return 19;
        } else if (sucmanh < 11100010000000L) {
            return 20;
        }
        return 21;
    }

    public void hsChar(Player pl, long hp, long mp) {
        Message msg;
        try {
            pl.setJustRevivaled();
            pl.nPoint.setHp(hp);
            pl.nPoint.setMp(mp);
            if (!pl.isPet && !pl.isNewPet) {
                msg = new Message(-16);
                pl.sendMessage(msg);
                msg.cleanup();
                PlayerService.gI().sendInfoHpMpMoney(pl);
            }
            msg = messageSubCommand((byte) 15);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeInt(Util.TamkjllGH(hp));
            msg.writer().writeInt(Util.TamkjllGH(mp));
            msg.writer().writeShort(pl.location.x);
            msg.writer().writeShort(pl.location.y);
            sendMessAllPlayerInMap(pl, msg);
            msg.cleanup();
            Send_Info_NV(pl);
            PlayerService.gI().sendInfoHpMp(pl);
        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }

    public void charDie(Player pl) {
        Message msg;
        try {
            if (!pl.isPet && !pl.isNewPet) {
                msg = new Message(-17);
                msg.writer().writeByte((int) pl.id);
                msg.writer().writeShort(pl.location.x);
                msg.writer().writeShort(pl.location.y);
                pl.sendMessage(msg);
                msg.cleanup();
            } else if (pl.isPet) {
                ((Pet) pl).lastTimeDie = System.currentTimeMillis();
            }

            if (!pl.isPet && !pl.isBoss) {
                // ItemMap itemMap = new ItemMap(pl.zone, pl.idNRNM, 1, pl.location.x,
                // pl.location.y, -1);
                // Service.gI().dropItemMap(pl.zone, itemMap);
                // NgocRongNamecService.gI().pNrNamec[pl.idNRNM - 353] = "";
                // NgocRongNamecService.gI().idpNrNamec[pl.idNRNM - 353] = -1;
                // pl.idNRNM = -1;
                PlayerService.gI().changeAndSendTypePK(pl, ConstPlayer.NON_PK);
                Service.gI().sendFlagBag(pl);
            }
            if (pl.zone.map.mapId == 51) {
                ChangeMapService.gI().changeMapBySpaceShip(pl, 21 + pl.gender, 0, -1);
            }
            msg = new Message(-8);
            msg.writer().writeShort((int) pl.id);
            msg.writer().writeByte(0); // cpk
            msg.writer().writeShort(pl.location.x);
            msg.writer().writeShort(pl.location.y);
            sendMessAnotherNotMeInMap(pl, msg);
            msg.cleanup();
            // Send_Info_NV(pl);
        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }

    public void attackMob(Player pl, int mobId) {
        if (pl != null && pl.zone != null) {
            for (Mob mob : pl.zone.mobs) {
                if (mob.id == mobId) {
                    SkillService.gI().useSkill(pl, null, mob, null);
                    break;
                }
            }
        }
    }

    public void Send_Caitrang(Player player) {
        if (player != null) {
            Message msg;
            try {
                msg = new Message(-90);
                msg.writer().writeByte(1);// check type
                msg.writer().writeInt((int) player.id); // id player
                short head = player.getHead();
                short body = player.getBody();
                short leg = player.getLeg();
                msg.writer().writeShort(head);// set head
                msg.writer().writeShort(body);// setbody
                msg.writer().writeShort(leg);// set leg
                msg.writer().writeByte(player.effectSkill.isMonkey ? 1 : 0);// set khỉ
                sendMessAllPlayerInMap(player, msg);
                msg.cleanup();
            } catch (Exception e) {
                Logger.logException(Service.class, e);
            }
        }
    }

    public void setNotMonkey(Player player) {
        Message msg;
        try {
            msg = new Message(-90);
            msg.writer().writeByte(-1);
            msg.writer().writeInt((int) player.id);
            Service.getInstance().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }

    public void sendTitleRv(Player player, Player p2, String tendanhhieu) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(0);
            me.writer().writeInt((int) player.id);
            if (tendanhhieu.equals("LOADEFFCT")) {
                if (player.titlett == true) {
                    switch (tendanhhieu) {
                        case "VECHAI":
                            me.writer().writeShort(84);
                            break;
                        case "FANCUNG":
                            me.writer().writeShort(85);
                            break;
                        case "SUCMANH":
                            me.writer().writeShort((player.gender == 0 ? 223 : player.gender == 1 ? 222 : 224));
                            break;
                        case "PHONGBA":
                            me.writer().writeShort(220);
                            break;
                        case "TUOITHO":
                            me.writer().writeShort(65);
                            break;
                    }
                }
                if (player.titleitem == true) {
                    switch (tendanhhieu) {
                        case "DH1":
                            if (player.timedh1 > 0 && player.usedh1) {
                                me.writer().writeShort(67);
                            }
                            break;
                        case "DH2":
                            if (player.timedh2 > 0 && player.usedh2) {
                                me.writer().writeShort(73);
                            }
                            break;
                        case "DH3":
                            if (player.timedh3 > 0 && player.usedh3) {
                                me.writer().writeShort(64);
                            }
                            break;
                        case "VIP1":
                            me.writer().writeShort(60);
                            break;
                        case "VIP2":
                            me.writer().writeShort(61);
                            break;
                        case "VIP3":
                            me.writer().writeShort(62);
                            break;
                        case "VIP4":
                            me.writer().writeShort(63);
                            break;
                    }
                }
            }
            me.writer().writeByte(1);
            me.writer().writeByte(-1);
            me.writer().writeShort(50);
            me.writer().writeByte(-1);
            me.writer().writeByte(-1);
            p2.sendMessage(me);
            me.cleanup();
        } catch (IOException e) {
        }
    }

    public void sendTitle(Player player, String tendanhhieu) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(0);
            me.writer().writeInt((int) player.id);
            if (player.titlett == true) {
                switch (tendanhhieu) {
                    case "VECHAI":
                        me.writer().writeShort(84);
                        break;
                    case "FANCUNG":
                        me.writer().writeShort(85);
                        break;
                    case "SUCMANH":
                        me.writer().writeShort((player.gender == 0 ? 223 : player.gender == 1 ? 222 : 224));
                        break;
                    case "PHONGBA":
                        me.writer().writeShort(220);
                        break;
                    case "TUOITHO":
                        me.writer().writeShort(65);
                        break;
                }
            }
            if (player.titleitem == true) {
                switch (tendanhhieu) {
                    case "DH1":
                        if (player.timedh1 > 0 && player.usedh1) {
                            me.writer().writeShort(67);
                        }
                        break;
                    case "DH2":
                        if (player.timedh2 > 0 && player.usedh2) {
                            me.writer().writeShort(73);
                        }
                        break;
                    case "DH3":
                        if (player.timedh3 > 0 && player.usedh3) {
                            me.writer().writeShort(64);
                        }
                        break;
                    case "VIP1":
                        me.writer().writeShort(60);
                        break;
                    case "VIP2":
                        me.writer().writeShort(61);
                        break;
                    case "VIP3":
                        me.writer().writeShort(62);
                        break;
                    case "VIP4":
                        me.writer().writeShort(63);
                        break;
                }
            }
            me.writer().writeByte(1);
            me.writer().writeByte(-1);
            me.writer().writeShort(-1);
            me.writer().writeByte(-1);
            me.writer().writeByte(-1);
            this.sendMessAllPlayerInMap(player, me);
            me.cleanup();
        } catch (IOException e) {
        }
    }

    public void addEffectChar(Player pl, int id, int layer, int loop, int loopcount, int stand) {
        if (!pl.idEffChar.contains(id)) {
            pl.idEffChar.add(id);
        }
        try {
            Message msg = new Message(-128);
            msg.writer().writeByte(0);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeShort(id);
            msg.writer().writeByte(layer);
            msg.writer().writeByte(loop);
            msg.writer().writeShort(loopcount);
            msg.writer().writeByte(stand);
            sendMessAllPlayerInMap(pl.zone, msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addEffectCharPet(Pet pet, int id, int layer, int loop, int loopcount, int stand) {
        if (!pet.idEffChar.contains(id)) {
            pet.idEffChar.add(id);
        }
        try {
            Message msg = new Message(-128);
            msg.writer().writeByte(0);
            msg.writer().writeInt((int) pet.id);
            msg.writer().writeShort(id);
            msg.writer().writeByte(layer);
            msg.writer().writeByte(loop);
            msg.writer().writeShort(loopcount);
            msg.writer().writeByte(stand);
            sendMessAllPlayerInMap(pet.zone, msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendTitle(Player player, int id) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(0);
            me.writer().writeInt((int) player.id);
            if (player.titleitem) {
                if (id == (1550 + 5)) {
                    me.writer().writeShort(60);
                } else if (id == (1550 + 6)) {
                    me.writer().writeShort(61);
                } else if (id == (1550 + 7)) {
                    me.writer().writeShort(62);
                } else if (id == (1550 + 8)) {
                    me.writer().writeShort(63);
                } else if (id == (1980 + 8)) {
                    me.writer().writeShort(73);
                }
            }
            // if (id == 999) {
            // if (player.titlett) {
            // if (player.capTT > 14 && player.capTT < 30) {
            // me.writer().writeShort(64);
            // } else if (player.capTT > 29) {
            // me.writer().writeShort(65);
            // } else if (player.capCS > 5) {
            // me.writer().writeShort(66);
            // }
            // }
            // }

            me.writer().writeByte(1);
            me.writer().writeByte(-1);
            me.writer().writeShort(50);
            me.writer().writeByte(-1);
            me.writer().writeByte(0);
            this.sendMessAllPlayerInMap(player, me);
            me.cleanup();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void buffdh(Player player, int idbuff) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(0);
            me.writer().writeInt((int) player.id);
            if (idbuff != -1) {
                me.writer().writeShort((short) idbuff);
            }
            me.writer().writeByte(1);
            me.writer().writeByte(-1);
            me.writer().writeShort(-1);
            me.writer().writeByte(-1);
            me.writer().writeByte(-1);
            this.sendMessAllPlayerInMap(player, me);
            me.cleanup();
        } catch (IOException e) {
        }
    }

    public void sendTitlepet(Pet pet, int id) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(0);
            me.writer().writeInt((int) pet.id);
            if (id == 888) {
                me.writer().writeShort(68);
            }
            if (id == 889) {
                me.writer().writeShort(73);
            }
            if (id == 890) {
                me.writer().writeShort(65);
            }
            if (id == 891) {
                me.writer().writeShort(84);
            }
            if (id == 892) {
                me.writer().writeShort(85);
            }
            me.writer().writeByte(1);
            me.writer().writeByte(-1);
            me.writer().writeShort(50);
            me.writer().writeByte(-1);
            me.writer().writeByte(-1);
            this.sendMessAllPlayerInMap(pet, me);
            me.cleanup();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reloadTitle(Player player) {
        Service.getInstance().sendFlagBag(player);
        if (player.inventory.itemsBody.get(11).isNotNullItem() && player.inventory.itemsBody.get(11) != null) {
            Service.getInstance().sendFoot(player, (player.inventory.itemsBody.get(11).template.id));
        }
        if (player.inventory.itemsBody.size() > 12 && player.inventory.itemsBody.get(12).isNotNullItem()
                && player.inventory.itemsBody.get(12) != null) {
            Service.getInstance().addEffectChar(player, (player.inventory.itemsBody.get(12).template.part), 0, -1, -1,
                    1);
        }
        if (player.inventory.itemsBody.get(10).isNotNullItem() && player.inventory.itemsBody.get(10) != null
                && player.reloadtitle == true) {
            Service.getInstance().sendPetFollow(player,
                    (short) ((short) player.inventory.itemsBody.get(10).template.iconID - 1));
        }
        // if (player.pet.inventory.itemsBody.get(8).isNotNullItem() &&
        // player.pet.inventory.itemsBody.get(8) != null && player.pet.reloadtitle ==
        // true) {
        // Service.getInstance().sendPetFollow(player.pet, (short) ((short)
        // player.pet.inventory.itemsBody.get(8).template.iconID - 1));
        // }
        if (player.pet != null && player.pet.inventory.itemsBody.get(7).isNotNullItem()
                && player.pet.inventory.itemsBody.get(7) != null) {
            Service.getInstance().sendFlagBagPet(player.pet);
        }

        if (player.dhtang1 == true) {
            Service.getInstance().sendTitle(player, "VECHAI");
        }
        if (player.dhtang2 == true) {
            Service.getInstance().sendTitle(player, "FANCUNG");
        }
        if (player.dhphongba == true) {
            Service.getInstance().sendTitle(player, "PHONGBA");
        }
        if (player.dhtuoitho == true) {
            Service.getInstance().sendTitle(player, "TUOITHO");
        }
        if (player.dhhanhtinh == true) {
            Service.getInstance().sendTitle(player, "SUCMANH");
            Service.getInstance().buffdh(player, (player.gender == 0 ? 57 : player.gender == 1 ? 37 : 58));
        }
        if (player.usedh1 == true) {
            Service.getInstance().sendTitle(player, "DH1");
        }
        if (player.usedh2 == true) {
            Service.getInstance().sendTitle(player, "DH2");
        }
        if (player.usedh3 == true) {
            Service.getInstance().sendTitle(player, "DH3");
        }
        // switch (player.vip) {
        // case 1:
        // Service.getInstance().sendTitle(player, "VIP1");
        // break;
        // case 2:
        // Service.getInstance().sendTitle(player, "VIP2");
        // break;
        // case 3:
        // Service.getInstance().sendTitle(player, "VIP3");
        // break;
        // case 4:
        // Service.getInstance().sendTitle(player, "VIP4");
        // break;
        // }
        if (player.reloadtitle == true) {
            player.reloadtitle = false;
        }
    }

    public void removeTitle(Player player) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(2);
            me.writer().writeInt((int) player.id);
            player.getSession().sendMessage(me);
            this.sendMessAllPlayerInMap(player, me);
            me.cleanup();
            player.reloadtitle = true;
            Service.getInstance().sendFlagBag(player);
            if (player.inventory.itemsBody.get(10).isNotNullItem() && player.inventory.itemsBody.get(10) != null) {
                Service.getInstance().sendPetFollow(player, (short) ((short) 0));
            }
            if (player.newpet != null) {
                ChangeMapService.gI().exitMap(player.newpet);
                player.newpet.dispose();
                player.newpet = null;
            }
            if (player.pet != null) {
                Service.getInstance().sendFlagBagPet(player.pet);
            }
        } catch (Exception e) {
        }
    }

    public void removedh(Player player) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(2);
            me.writer().writeInt((int) player.id);
            player.getSession().sendMessage(me);
            this.sendMessAllPlayerInMap(player, me);
            me.cleanup();
            player.reloadtitle = true;
        } catch (IOException e) {
        }
    }

    public void sendFoot(Player player, int id) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(0);
            me.writer().writeInt((int) player.id);
            switch (id) {
                case 1300:
                    me.writer().writeShort(74);
                    break;
                case 1301:
                    me.writer().writeShort(75);
                    break;
                case 1302:
                    me.writer().writeShort(76);
                    break;
                case 1303:
                    me.writer().writeShort(77);
                    break;
                case 1304:
                    me.writer().writeShort(78);
                    break;
                case 1305:
                    me.writer().writeShort(79);
                    break;
                case 1306:
                    me.writer().writeShort(80);
                    break;
                case 1307:
                    me.writer().writeShort(81);
                    break;
                case 1308:
                    me.writer().writeShort(82);
                    break;
                default:
                    break;
            }
            me.writer().writeByte(0);
            me.writer().writeByte(-1);
            me.writer().writeShort(1);
            me.writer().writeByte(-1);
            this.sendMessAllPlayerInMap(player, me);
            me.cleanup();

        } catch (Exception e) {
            System.out.println("Lỗi send chân mệnh");
        }
    }

    public void sendFootRv(Player player, Player p2, int id) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(0);
            me.writer().writeInt((int) player.id);
            switch (id) {
                case 1300:
                    me.writer().writeShort(74);
                    break;
                case 1301:
                    me.writer().writeShort(75);
                    break;
                case 1302:
                    me.writer().writeShort(76);
                    break;
                case 1303:
                    me.writer().writeShort(77);
                    break;
                case 1304:
                    me.writer().writeShort(78);
                    break;
                case 1305:
                    me.writer().writeShort(79);
                    break;
                case 1306:
                    me.writer().writeShort(80);
                    break;
                case 1307:
                    me.writer().writeShort(81);
                    break;
                case 1308:
                    me.writer().writeShort(82);
                    break;
                default:
                    break;
            }

            me.writer().writeByte(0);
            me.writer().writeByte(-1);
            me.writer().writeShort(1);
            me.writer().writeByte(-1);
            p2.sendMessage(me);
            me.cleanup();
        } catch (Exception e) {
            System.out.println("Lỗi send chân mệnh");
        }
    }

    public void sendFlagBag(Player pl) {
        Message msg;
        try {
            int flagbag = pl.getFlagBag();
            if (pl.reloadtitle == true)
                flagbag = -1;
            if (pl.isPl() && pl.getSession().version > 228) {
                if (pl.fusion.typeFusion == ConstPlayer.HOP_THE_GOGETA)
                    flagbag = 204;
                switch (flagbag) {
                    case 108:
                        flagbag = 204;
                        break;
                    case 125:
                        flagbag = 205;

                        break;

                }
            }
            msg = new Message(-64);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeByte(flagbag);
            sendMessAllPlayerInMap(pl, msg);
            msg.cleanup();
        } catch (IOException e) {
        }
    }

    public void sendFlagBagPet(Pet pet) {
        Message msg;
        try {
            msg = new Message(-64);
            msg.writer().writeInt((int) pet.id);
            if (pet.master.reloadtitle == true) {
                msg.writer().writeByte(-1);
            } else {
                msg.writer().writeByte(pet.getFlagBag());
            }
            sendMessAllPlayerInMap(pet, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendThongBaoOK(Player pl, String text) {
        if (pl.isPet || pl.isNewPet) {
            return;
        }
        Message msg;
        try {
            msg = new Message(-26);
            msg.writer().writeUTF(text);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }

    public void sendThongBaoOK(MySession session, String text) {
        Message msg;
        try {
            msg = new Message(-26);
            msg.writer().writeUTF(text);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendThongBaoAllPlayer(String thongBao) {
        Message msg;
        try {
            msg = new Message(-25);
            msg.writer().writeUTF(thongBao);
            this.sendMessAllPlayer(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void ChatAll(int iconId, String text) {
        Message msg;
        try {
            msg = new Message(-70);
            msg.writer().writeShort(iconId);
            msg.writer().writeUTF(text);
            msg.writer().writeByte(0);
            this.sendMessAllPlayer(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendBigMessage(Player player, int iconId, String text) {
        try {
            Message msg;
            msg = new Message(-70);
            msg.writer().writeShort(iconId);
            msg.writer().writeUTF(text);
            msg.writer().writeByte(0);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (IOException e) {

        }
    }

    public void sendThongBaoFromAdmin(Player player, String text) {
        sendBigMessage(player, 18328, text);
    }

    public void sendThongBao(Player pl, String thongBao) {
        Message msg;
        try {
            msg = new Message(-25);
            msg.writer().writeUTF(thongBao);
            pl.sendMessage(msg);
            msg.cleanup();

        } catch (Exception e) {
        }
    }

    public void sendThongBao(List<Player> pl, String thongBao) {
        for (int i = 0; i < pl.size(); i++) {
            Player ply = pl.get(i);
            if (ply != null) {
                this.sendThongBao(ply, thongBao);
            }
        }
    }

    public void sendMoney(Player pl) {
        Message msg;
        try {
            msg = new Message(6);
            if (pl.getSession().version >= 214) {
                msg.writer().writeLong(pl.inventory.gold);
            } else {
                msg.writer().writeInt((int) pl.inventory.gold);
            }
            msg.writer().writeInt(pl.inventory.gem);
            msg.writer().writeInt(pl.inventory.ruby);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public void sendToAntherMePickItem(Player player, int itemMapId) {
        Message msg;
        try {
            msg = new Message(-19);
            msg.writer().writeShort(itemMapId);
            msg.writer().writeInt((int) player.id);
            // sendMessAllPlayerIgnoreMe(player, msg);
            sendMessAnotherNotMeInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public static final int[] flagTempId = { 363, 364, 365, 366, 367, 368, 369, 370, 371, 519, 520, 747 };
    public static final int[] flagIconId = { 2761, 2330, 2323, 2327, 2326, 2324, 2329, 2328, 2331, 4386, 4385, 2325 };

    public void openFlagUI(Player pl) {
        Message msg;
        try {
            msg = new Message(-103);
            msg.writer().writeByte(0);
            msg.writer().writeByte(flagTempId.length);
            for (int i = 0; i < flagTempId.length; i++) {
                msg.writer().writeShort(flagTempId[i]);
                msg.writer().writeByte(1);
                switch (flagTempId[i]) {
                    case 363:
                        msg.writer().writeByte(73);
                        msg.writer().writeShort(0);
                        break;
                    case 371:
                        msg.writer().writeByte(88);
                        msg.writer().writeShort(10);
                        break;
                    default:
                        msg.writer().writeByte(88);
                        msg.writer().writeShort(5);
                        break;
                }
            }
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void changeFlag(Player pl, int index) {
        Message msg;
        try {
            pl.cFlag = (byte) index;
            msg = new Message(-103);
            msg.writer().writeByte(1);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeByte(index);
            Service.getInstance().sendMessAllPlayerInMap(pl, msg);
            msg.cleanup();

            msg = new Message(-103);
            msg.writer().writeByte(2);
            msg.writer().writeByte(index);
            msg.writer().writeShort(flagIconId[index]);
            Service.getInstance().sendMessAllPlayerInMap(pl, msg);
            msg.cleanup();

            if (pl.pet != null) {
                pl.pet.cFlag = (byte) index;
                msg = new Message(-103);
                msg.writer().writeByte(1);
                msg.writer().writeInt((int) pl.pet.id);
                msg.writer().writeByte(index);
                Service.getInstance().sendMessAllPlayerInMap(pl.pet, msg);
                msg.cleanup();

                msg = new Message(-103);
                msg.writer().writeByte(2);
                msg.writer().writeByte(index);
                msg.writer().writeShort(flagIconId[index]);
                Service.getInstance().sendMessAllPlayerInMap(pl.pet, msg);
                msg.cleanup();
            }
            pl.iDMark.setLastTimeChangeFlag(System.currentTimeMillis());
        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }

    public void sendFlagPlayerToMe(Player me, Player pl) {
        Message msg;
        try {
            msg = new Message(-103);
            msg.writer().writeByte(2);
            msg.writer().writeByte(pl.cFlag);
            msg.writer().writeShort(flagIconId[pl.cFlag]);
            me.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void chooseFlag(Player pl, int index) {
        if (MapService.gI().isMapBlackBallWar(pl.zone.map.mapId) || MapService.gI().isMapMaBu(pl.zone.map.mapId)) {
            sendThongBao(pl, "Không thể đổi cờ lúc này!");
            return;
        }
        if (Util.canDoWithTime(pl.iDMark.getLastTimeChangeFlag(), 60000)) {
            changeFlag(pl, index);
        } else {
            sendThongBao(pl, "Không thể đổi cờ lúc này! Vui lòng đợi "
                    + TimeUtil.getTimeLeft(pl.iDMark.getLastTimeChangeFlag(), 60) + " nữa!");
        }
    }

    public void attackPlayer(Player pl, int idPlAnPem) {
        SkillService.gI().useSkill(pl, pl.zone.getPlayerInMap(idPlAnPem), null, null);
    }

    public void releaseCooldownSkill(Player pl) {
        Message msg;
        try {
            msg = new Message(-94);
            for (Skill skill : pl.playerSkill.skills) {
                skill.coolDown = 0;
                msg.writer().writeShort(skill.skillId);
                int leftTime = (int) (skill.lastTimeUseThisSkill + skill.coolDown - System.currentTimeMillis());
                if (leftTime < 0) {
                    leftTime = 0;
                }
                msg.writer().writeInt(leftTime);
            }
            pl.sendMessage(msg);
            pl.nPoint.setMp(pl.nPoint.mpMax);
            PlayerService.gI().sendInfoHpMpMoney(pl);
            msg.cleanup();

        } catch (Exception e) {
        }
    }

    public void sendTimeSkill(Player pl) {
        Message msg;
        try {
            msg = new Message(-94);
            for (Skill skill : pl.playerSkill.skills) {
                msg.writer().writeShort(skill.skillId);
                int timeLeft = (int) (skill.lastTimeUseThisSkill + skill.coolDown - System.currentTimeMillis());
                if (timeLeft < 0) {
                    timeLeft = 0;
                }
                msg.writer().writeInt(timeLeft);
            }
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void dropItemMap(Zone zone, ItemMap item) {
        Message msg;
        try {
            msg = new Message(68);
            msg.writer().writeShort(item.itemMapId);
            msg.writer().writeShort(item.itemTemplate.id);
            msg.writer().writeShort(item.x);
            msg.writer().writeShort(item.y);
            msg.writer().writeInt(3);//
            sendMessAllPlayerInMap(zone, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void dropItemMapForMe(Player player, ItemMap item) {
        Message msg;
        try {
            msg = new Message(68);
            msg.writer().writeShort(item.itemMapId);
            msg.writer().writeShort(item.itemTemplate.id);
            msg.writer().writeShort(item.x);
            msg.writer().writeShort(item.y);
            msg.writer().writeInt(3);//
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }

    public void showInfoPet(Player pl) {
        if (pl != null && pl.pet != null) {
            Message msg;
            try {
                msg = new Message(-107);
                msg.writer().writeByte(2);
                msg.writer().writeShort(pl.pet.getAvatar());
                msg.writer().writeByte(pl.pet.inventory.itemsBody.size());
                for (Item item : pl.pet.inventory.itemsBody) {
                    if (!item.isNotNullItem()) {
                        msg.writer().writeShort(-1);
                    } else {
                        msg.writer().writeShort(item.template.id);
                        msg.writer().writeInt(item.quantity);
                        msg.writer().writeUTF(item.getInfo());
                        msg.writer().writeUTF(item.getContent());
                        int countOption = item.itemOptions.size();
                        msg.writer().writeByte(countOption);
                        for (ItemOption iop : item.itemOptions) {
                            msg.writer().writeByte(iop.optionTemplate.id);
                            msg.writer().writeShort(iop.param);
                        }
                    }
                }
                // if(pl.getSession().zoomLevel != 1 && pl.getSession().version == 999){
                // msg.writer().writeLong(pl.pet.nPoint.hpg);
                // msg.writer().writeLong(pl.pet.nPoint.mpg);
                // msg.writer().writeLong(pl.pet.nPoint.dameg);
                // msg.writer().writeLong(pl.pet.nPoint.defg);
                // msg.writer().writeInt(pl.pet.nPoint.critg);
                // }
                // cso gốc
                msg.writer().writeInt(Util.TamkjllGH(pl.pet.nPoint.hp)); // hp
                msg.writer().writeInt(Util.TamkjllGH(pl.pet.nPoint.hpMax)); // hpfull
                msg.writer().writeInt(Util.TamkjllGH(pl.pet.nPoint.mp)); // mp
                msg.writer().writeInt(Util.TamkjllGH(pl.pet.nPoint.mpMax)); // mpfull
                msg.writer().writeInt(Util.TamkjllGH(pl.pet.nPoint.dame)); // damefull
                msg.writer().writeUTF(pl.pet.name); // name
                msg.writer().writeUTF(pl.pet.getNameThuctinh(pl.pet.thuctinh) + getCurrStrLevel(pl.pet)); // curr level
                msg.writer().writeLong(pl.pet.nPoint.power); // power
                msg.writer().writeLong(pl.pet.nPoint.tiemNang); // tiềm năng
                msg.writer().writeByte(pl.pet.getStatus()); // status
                msg.writer().writeShort(pl.pet.nPoint.stamina); // stamina
                msg.writer().writeShort(pl.pet.nPoint.maxStamina); // stamina full
                msg.writer().writeByte(pl.pet.nPoint.crit); // crit
                msg.writer().writeShort(pl.pet.nPoint.defg); // def
                msg.writer().writeByte(4); // counnt pet skill
                for (int i = 0; i < pl.pet.playerSkill.skills.size(); i++) {
                    if (pl.pet.playerSkill.skills.get(i).skillId != -1) {
                        msg.writer().writeShort(pl.pet.playerSkill.skills.get(i).skillId);
                    } else {
                        switch (i) {
                            case 1:
                                msg.writer().writeShort(-1);
                                msg.writer().writeUTF("Cần đạt sức mạnh 150tr để mở");
                                break;
                            case 2:
                                msg.writer().writeShort(-1);
                                msg.writer().writeUTF("Cần đạt sức mạnh 1.5tỷ để mở");
                                break;
                            default:
                                msg.writer().writeShort(-1);
                                msg.writer().writeUTF("Cần đạt sức mạnh 20tỷ để mở");
                                break;
                        }
                    }
                }

                pl.sendMessage(msg);
                msg.cleanup();

            } catch (Exception e) {
                Logger.logException(Service.class, e);
            }
        }
    }

    public void sendSpeedPlayer(Player pl, int speed) {
        Message msg;
        try {
            msg = Service.getInstance().messageSubCommand((byte) 8);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeByte(speed != -1 ? speed : pl.nPoint.speed);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }

    public void sendKQTaiXiu(Player player, int x1, int x2, int x3) {
        Message msg;
        try {
            msg = new Message(119);
            msg.writer().writeInt(1);
            msg.writer().writeInt(x1);
            msg.writer().writeInt(x2);
            msg.writer().writeInt(x3);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }

    public void sendTaiXiu(Player player, int time, String text, int tai, int xiu) {
        Message msg;
        try {
            msg = new Message(119);
            msg.writer().writeInt(0);
            msg.writer().writeInt((int) time);
            msg.writer().writeUTF(text);
            msg.writer().writeInt(tai);
            msg.writer().writeInt(xiu);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }

    public void setPos(Player player, int x, int y) {
        player.location.x = x;
        player.location.y = y;
        Message msg;
        try {
            msg = new Message(123);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeShort(x);
            msg.writer().writeShort(y);
            msg.writer().writeByte(1);
            sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void getPlayerMenu(Player player, int playerId) {
        Message msg;
        try {
            msg = new Message(-79);
            Player pl = player.zone.getPlayerInMap(playerId);
            if (pl != null) {
                msg.writer().writeInt(playerId);
                msg.writer().writeLong(pl.nPoint.power);
                msg.writer().writeUTF(Service.gI().getCurrStrLevel(pl));
                player.sendMessage(msg);
            }
            msg.cleanup();
            if (player.isAdmin() || player.isAdmin2()) {
                SubMenuService.gI().showMenuForAdmin(player);
            }
        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }

    public void hideWaitDialog(Player pl) {
        Message msg;
        try {
            msg = new Message(-99);
            msg.writer().writeByte(-1);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void chatPrivate(Player plChat, Player plReceive, String text) {
        Message msg;
        try {
            msg = new Message(92);
            msg.writer().writeUTF(plChat.name);
            msg.writer().writeUTF("|5|" + text);
            msg.writer().writeInt((int) plChat.id);
            msg.writer().writeShort(plChat.getHead());
            msg.writer().writeShort(plChat.getBody());
            msg.writer().writeShort(plChat.getFlagBag()); // bag
            msg.writer().writeShort(plChat.getLeg());
            msg.writer().writeByte(1);
            plChat.sendMessage(msg);
            plReceive.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void changePassword(Player player, String oldPass, String newPass, String rePass) {
        if (player.getSession().pp.equals(oldPass)) {
            if (newPass.length() >= 6) {
                if (newPass.equals(rePass)) {
                    player.getSession().pp = newPass;
                    try {
                        GirlkunDB.executeUpdate("update account set password = ? where id = ? and username = ?",
                                rePass, player.getSession().userId, player.getSession().uu);
                        Service.getInstance().sendThongBao(player, "Đổi mật khẩu thành công!");
                    } catch (Exception ex) {
                        Service.getInstance().sendThongBao(player, "Đổi mật khẩu thất bại!");
                        Logger.logException(Service.class, ex);
                    }
                } else {
                    Service.getInstance().sendThongBao(player, "Mật khẩu nhập lại không đúng!");
                }
            } else {
                Service.getInstance().sendThongBao(player, "Mật khẩu ít nhất 6 ký tự!");
            }
        } else {
            Service.getInstance().sendThongBao(player, "Mật khẩu cũ không đúng!");
        }
    }

    public void switchToCreateChar(MySession session) {
        Message msg;
        try {
            msg = new Message(2);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendCaption(MySession session, byte gender) {
        Message msg;
        try {
            msg = new Message(-41);
            msg.writer().writeByte(Manager.CAPTIONS.size());
            for (String caption : Manager.CAPTIONS) {
                msg.writer().writeUTF(caption.replaceAll("%1", gender == ConstPlayer.TRAI_DAT ? "Trái đất"
                        : (gender == ConstPlayer.NAMEC ? "Namếc" : "Xayda")));
            }
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendHavePet(Player player) {
        Message msg;
        try {
            msg = new Message(-107);
            msg.writer().writeByte(player.pet == null ? 0 : 1);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendHavePet2(Player player) {
        Message msg;
        try {
            msg = new Message(-107);
            msg.writer().writeByte(player.newpet == null ? 0 : 1);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendWaitToLogin(MySession session, int secondsWait) {
        Message msg;
        try {
            msg = new Message(122);
            msg.writer().writeShort(secondsWait);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(Service.class, e);
        }
    }

    public void sendMessage(MySession session, int cmd, String path) {
        Message msg;
        try {
            msg = new Message(cmd);
            msg.writer().write(FileIO.readFile(path));
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void createItemMap(Player player, int tempId) {
        ItemMap itemMap = new ItemMap(player.zone, tempId, 1, player.location.x, player.location.y, player.id);
        dropItemMap(player.zone, itemMap);
    }

    public void sendNangDong(Player player) {
        Message msg;
        try {
            msg = new Message(-97);
            msg.writer().writeInt(100);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void setClientType(MySession session, Message msg) {
        try {
            session.typeClient = (msg.reader().readByte());// client_type
            session.zoomLevel = msg.reader().readByte();// zoom_level
            msg.reader().readBoolean();// is_gprs
            msg.reader().readInt();// width
            msg.reader().readInt();// height
            msg.reader().readBoolean();// is_qwerty
            msg.reader().readBoolean();// is_touch
            String platform = msg.reader().readUTF();
            String[] arrPlatform = platform.split("\\|");
            session.version = Integer.parseInt(arrPlatform[1].replaceAll("\\.", ""));
            // System.out.println(platform);
        } catch (Exception e) {
        } finally {
            msg.cleanup();
        }
        DataGame.sendLinkIP(session);
    }

    public void DropVeTinh(Player pl, Item item, Zone map, int x, int y) {
        ItemMap itemMap = new ItemMap(map, item.template, item.quantity, x, y, pl.id);
        itemMap.options = item.itemOptions;
        map.addItem(itemMap);
        Message msg = null;
        try {
            msg = new Message(68);
            msg.writer().writeShort(itemMap.itemMapId);
            msg.writer().writeShort(itemMap.itemTemplate.id);
            msg.writer().writeShort(itemMap.x);
            msg.writer().writeShort(itemMap.y);
            msg.writer().writeInt(-2);
            msg.writer().writeShort(200);
            sendMessAllPlayerInMap(map, msg);
        } catch (Exception exception) {
        } finally {
            if (msg != null) {
                msg.cleanup();
                msg = null;
            }
        }
    }
}
