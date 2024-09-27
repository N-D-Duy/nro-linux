package ServerData.Models.Clan;

import com.girlkun.database.GirlkunDB;
import ServerData.Models.Map.ListMap.BanDoKhoBau;
import ServerData.Models.Map.ListMap.DoanhTrai;
import ServerData.Services.ClanService;
import ServerData.Models.Map.ListMap.KhiGasHuyDiet;
import ServerData.Models.Map.gas.Gas;
import java.util.ArrayList;
import java.util.List;

import ServerData.Models.Player.Player;
import ServerData.Server.Client;
import ServerData.Server.Manager;
import ServerData.Services.Service;
import com.girlkun.network.io.Message;
import ServerData.Utils.Logger;
import ServerData.Utils.Util;
import ServerData.Models.Map.ConDuongRanDoc.ConDuongRanDoc;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class Clan {

    public static int NEXT_ID = 0;

    public int clanMessageId = 0;
    private final List<ClanMessage> clanMessages;

    public static final byte LEADER = 0;
    public static final byte DEPUTY = 1;
    public static final byte MEMBER = 2;

    public int id;
    public int imgId;
    public String name;
    public String slogan;
    public int createTime;
    public long powerPoint;
    public byte maxMember;
    public int level;
    public boolean active;
    public int capsuleClan;
 public long lastTimeOpenConDuongRanDoc;
    public boolean haveGoneConDuongRanDoc;
    public boolean gobosscdrd = false;

    public ConDuongRanDoc ConDuongRanDoc;
    public int timeEndConDuongRanDoc;
    public long timeOpenConDuongRanDoc;
    public Player playerOpenConDuongRanDoc;
    
     public boolean haveGoneGas;
    public byte pointGas;
    public long timeOpenKhiGas;
    public Player playerOpenKhiGas;
    public Gas khiGas;
    public long lastTimeOpenGas;

    
    public long lastTimeOpenDoanhTrai;
    public boolean haveGoneDoanhTrai;
    public long timeOpenDoanhTrai;
    public Player playerOpenDoanhTrai;
    public DoanhTrai doanhTrai;

    public final List<ClanMember> members;
    public final List<Player> membersInGame;
    public long timeOpenBanDoKhoBau;
    public Player playerOpenBanDoKhoBau;
    public BanDoKhoBau BanDoKhoBau;

    public KhiGasHuyDiet KhiGaHuyDiet;
    public long timeOpenKhiGaHuyDiet;
    public Player playerOpenKhiGaHuyDiet;
    
    
    public Clan() {
        this.id = NEXT_ID++;
        this.name = "";
        this.slogan = "";
        this.maxMember = 10;
        this.createTime = (int) (System.currentTimeMillis() / 1000);
        this.members = new ArrayList<>();
        this.membersInGame = new ArrayList<>();
        this.clanMessages = new ArrayList<>();
    }

    public ClanMember getLeader() {
        for (ClanMember cm : members) {
            if (cm.role == LEADER) {
                return cm;
            }
        }
        ClanMember cm = new ClanMember();
        cm.name = "Bang chủ";
        return cm;
    }

    public byte getRole(Player player) {
        for (ClanMember cm : members) {
            if (cm.id == player.id) {
                return cm.role;
            }
        }
        return -1;
    }

    public boolean isLeader(Player player) {
        for (ClanMember cm : members) {
            if (cm.id == player.id && cm.role == LEADER) {
                return true;
            }
        }
        return false;
    }

    public boolean isDeputy(Player player) {
        for (ClanMember cm : members) {
            if (cm.id == player.id && cm.role == DEPUTY) {
                return true;
            }
        }
        return false;
    }

    public void addSMTNClan(Player plOri, long param) {
        for (Player pl : this.membersInGame) {
            if (!plOri.equals(pl) && plOri.zone.equals(pl.zone)) {
                Service.gI().addSMTN(pl, (byte) 1, param, false);
            }
        }
    }

    public void sendMessageClan(ClanMessage cmg) {
        Message msg;
        try {
            msg = new Message(-51);
            msg.writer().writeByte(cmg.type);
            msg.writer().writeInt(cmg.id);
            msg.writer().writeInt(cmg.playerId);
            if (cmg.type == 2) {
                msg.writer().writeUTF(cmg.playerName + " (" + Util.numberToMoney(cmg.playerPower) + ")");
            } else {
                msg.writer().writeUTF(cmg.playerName);
            }
            msg.writer().writeByte(cmg.role);
            msg.writer().writeInt(cmg.time);
            if (cmg.type == 0) {
                msg.writer().writeUTF(cmg.text);
                msg.writer().writeByte(cmg.color);
            } else if (cmg.type == 1) {
                msg.writer().writeByte(cmg.receiveDonate);
                msg.writer().writeByte(cmg.maxDonate);
                msg.writer().writeByte(cmg.isNewMessage);
            }
            for (Player pl : this.membersInGame) {
                pl.sendMessage(msg);
            }
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void addClanMessage(ClanMessage cmg) {
        this.clanMessages.add(0, cmg);
        if (clanMessages.size() > 20) {
            for (int i = clanMessages.size() - 1; i >= 20; i--) {
                clanMessages.remove(i).dispose();
            }
        }
    }

    public ClanMessage getClanMessage(int clanMessageId) {
        for (ClanMessage cmg : this.clanMessages) {
            if (cmg.id == clanMessageId) {
                return cmg;
            }
        }
        return null;
    }

    public List<ClanMessage> getCurrClanMessages() {
        List<ClanMessage> list = new ArrayList();
        if (this.clanMessages.size() <= 20) {
            list.addAll(this.clanMessages);
        } else {
            for (int i = 0; i < 20; i++) {
                list.add(this.clanMessages.get(i));
            }
        }
        return list;
    }

    public void sendMyClanForAllMember() {
        for (Player pl : this.membersInGame) {
            if (pl != null) {
                ClanService.gI().sendMyClan(pl);
            }
        }
    }

    public void sendFlagBagForAllMember() {
        for (Player pl : this.membersInGame) {
            if (pl != null) {
                Service.gI().sendFlagBag(pl);
            }
        }
    }

    public void addMemberOnline(Player player) {
        this.membersInGame.add(player);
    }

    public void removeMemberOnline(ClanMember cm, Player player) {
        if (player != null) {
            this.membersInGame.remove(player);
        }
        if (cm != null) {
            for (int i = this.membersInGame.size() - 1; i >= 0; i--) {
                if (this.membersInGame.get(i).id == cm.id) {
                    this.membersInGame.remove(i);
                    break;
                }
            }
        }
    }

    public Player getPlayerOnline(int playerId) {
        for (Player player : this.membersInGame) {
            if (player.id == playerId) {
                return player;
            }
        }
        return null;
    }

    //load db danh sách member
    public void addClanMember(ClanMember cm) {
        this.members.add(cm);
    }

    //thêm vào khi player tạo mới clan or mới vào clan
    public void addClanMember(Player player, byte role) {
        ClanMember cm = new ClanMember(player, this, role);
        this.members.add(cm);
        player.clanMember = cm;
    }

    //xóa khi member rời clan or bị kích
    public void removeClanMember(ClanMember cm) {
        this.members.remove(cm);
        cm.dispose();
    }

    public byte getCurrMembers() {
        return (byte) this.members.size();
    }

    public List<ClanMember> getMembers() {
        return this.members;
    }

    public ClanMember getClanMember(int memberId) {
        for (ClanMember cm : members) {
            if (cm.id == memberId) {
                return cm;
            }
        }
        return null;
    }

    public void reloadClanMember() {
        for (ClanMember cm : this.members) {
            Player pl = Client.gI().getPlayer(cm.id);
            if (pl != null) {
                cm.name = pl.name;
                cm.powerPoint = pl.nPoint.power;
                cm.clanPoint = pl.clanMember.clanPoint;
                cm.memberPoint = pl.clanMember.memberPoint;
            }
        }
    }

    public void insert() {
        JSONArray dataArray = new JSONArray();
        JSONObject dataObject = new JSONObject();
        for (ClanMember cm : this.members) {
            dataObject.put("id", cm.id);
            dataObject.put("name", cm.name);
            dataObject.put("head", cm.head);
            dataObject.put("body", cm.body);
            dataObject.put("leg", cm.leg);
            dataObject.put("role", cm.role);
            dataObject.put("donate", cm.donate);
            dataObject.put("receive_donate", cm.receiveDonate);
            dataObject.put("member_point", cm.memberPoint);
            dataObject.put("clan_point", cm.clanPoint);
            dataObject.put("join_time", cm.joinTime);
            dataObject.put("ask_pea_time", cm.timeAskPea);
            dataObject.put("power", cm.powerPoint);
            dataArray.add(dataObject.toJSONString());
            dataObject.clear();
        }
        String member = dataArray.toJSONString();
        PreparedStatement ps = null;
        try (Connection con = GirlkunDB.getConnection();) {
            ps = con.prepareStatement("insert into clan_sv" + Manager.SERVER
                    + "(id, name, slogan, img_id, power_point, max_member, clan_point, level, members) "
                    + "values (?,?,?,?,?,?,?,?,?)");
            ps.setInt(1, this.id);
            ps.setString(2, this.name);
            ps.setString(3, this.slogan);
            ps.setInt(4, this.imgId);
            ps.setLong(5, this.powerPoint);
            ps.setByte(6, this.maxMember);
            ps.setInt(7, this.capsuleClan);
            ps.setInt(8, this.level);
            ps.setString(9, member);
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            Logger.logException(Clan.class, e, "Có lỗi khi insert clan vào db");
        } finally {
            try {
                ps.close();
            } catch (Exception e) {
            }
        }

    }

    public void update() {
        JSONArray dataArray = new JSONArray();
        JSONObject dataObject = new JSONObject();
        for (ClanMember cm : this.members) {
            dataObject.put("id", cm.id);
            dataObject.put("name", cm.name);
            dataObject.put("head", cm.head);
            dataObject.put("body", cm.body);
            dataObject.put("leg", cm.leg);
            dataObject.put("role", cm.role);
            dataObject.put("donate", cm.donate);
            dataObject.put("receive_donate", cm.receiveDonate);
            dataObject.put("member_point", cm.memberPoint);
            dataObject.put("clan_point", cm.clanPoint);
            dataObject.put("join_time", cm.joinTime);
            dataObject.put("ask_pea_time", cm.timeAskPea);
            dataArray.add(dataObject.toJSONString());
            dataObject.clear();
        }
        String member = dataArray.toJSONString();
        PreparedStatement ps = null;
        try (Connection con = GirlkunDB.getConnection();) {
            ps = con.prepareStatement("update clan_sv" + Manager.SERVER
                    + " set slogan = ?, img_id = ?, power_point = ?, max_member = ?, clan_point = ?, "
                    + "level = ?, members = ? where id = ? limit 1");
            ps.setString(1, this.slogan);
            ps.setInt(2, this.imgId);
            ps.setLong(3, this.powerPoint);
            ps.setByte(4, this.maxMember);
            ps.setInt(5, this.capsuleClan);
            ps.setInt(6, this.level);
            ps.setString(7, member);
            ps.setInt(8, this.id);
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            Logger.logException(Clan.class, e, "Có lỗi khi insert clan vào db");
        } finally {
            try {
                ps.close();
            } catch (Exception e) {
            }
        }
    }

    public void deleteDB(int id) {
        PreparedStatement ps;
        try (Connection con = GirlkunDB.getConnection();) {
            ps = con.prepareStatement("delete from clan_sv" + Manager.SERVER + " where id = ?");
            ps.setInt(1, id);
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            Logger.logException(Clan.class, e, "Có lỗi khi delete clan");
        }
    }

}
