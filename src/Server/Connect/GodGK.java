package Server.Connect;

import Server.Connect.NhiemVuThanhTich.ThanhTich;
import Server.Connect.NhiemVuThanhTich.ThanhTichPlayer;
import ServerData.Models.Player.Card;
import ServerData.Models.Player.OptionCard;
import com.girlkun.database.GirlkunDB;
import com.girlkun.result.GirlkunResultSet;
import Server.Data.Consts.ConstPlayer;
import Server.Data.DataGame;
import ServerData.Models.Clan.Clan;
import ServerData.Models.Clan.ClanMember;
import ServerData.Models.Item.Item;
import ServerData.Models.Item.ItemTime;
import ServerData.Models.Item.Template.ArchivementTemplate;
import ServerData.Models.NPC.MabuEgg;
import ServerData.Models.NPC.BillEgg;
import ServerData.Models.NPC.MagicTree;
import ServerData.Models.Player.Enemy;
import ServerData.Models.Player.Friend;
import ServerData.Models.Player.Fusion;
import ServerData.Models.Player.Pet;
import ServerData.Models.Player.Player;
import ServerData.Models.Player.PlayerSkill.Skill;
import ServerData.Models.Player.PlayerTask.TaskMain;
import ServerData.Server.Client;
import ServerData.Server.Manager;
import ServerData.Server.ServerNotify;
import ServerData.Server.MySession;
import ServerData.Server.AntiLogin;
import ServerData.Server.ServerManager;
import ServerData.Services.ClanService;
import ServerData.Services.IntrinsicService;
import ServerData.Services.InventoryServiceNew;
import ServerData.Services.ItemService;
import ServerData.Services.MapService;
import ServerData.Services.Service;
import ServerData.Services.TaskService;
import ServerData.Utils.Logger;
import ServerData.Utils.SkillUtil;
import ServerData.Utils.TimeUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ServerData.Utils.Util;
import java.util.Calendar;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class GodGK {

    public static List<OptionCard> loadOptionCard(JSONArray json) {
        List<OptionCard> ops = new ArrayList<>();
        try {
            for (int i = 0; i < json.size(); i++) {
                JSONObject ob = (JSONObject) json.get(i);
                if (ob != null) {
                    ops.add(new OptionCard(Integer.parseInt(ob.get("id").toString()),
                            Integer.parseInt(ob.get("param").toString()), Byte.parseByte(ob.get("active").toString())));
                }
            }
        } catch (Exception e) {
                System.out.println("Lỗi Card Opt");
        }
        return ops;
    }
    public static Boolean baotri = false;

    public static synchronized Player login(MySession session, AntiLogin al) {
        Player player = null;
        GirlkunResultSet rs = null;
        try {
            rs = GirlkunDB.executeQuery("select * from account where username = ? and password = ?", session.uu, (session.pp));
            if (rs.first()) {
                session.userId = rs.getInt("account.id");
                session.isJail = rs.getBoolean("is_jail");
                session.isAdmin = rs.getBoolean("is_admin");
                session.isAdmin2 = rs.getBoolean("is_admin2");
                session.lastTimeLogout = rs.getTimestamp("last_time_logout").getTime();
                session.actived = rs.getInt("active");
                session.vnd = rs.getInt("vnd");
               
                session.goldBar = rs.getInt("account.thoi_vang");
                session.bdPlayer = rs.getDouble("account.bd_player");
                session.vnd = rs.getInt("account.vnd");
                session.tongnap = rs.getInt("account.tongnap");
                session.coinBar = rs.getInt("account.coin");
                
                long lastTimeLogin = rs.getTimestamp("last_time_login").getTime();
                int secondsPass1 = (int) ((System.currentTimeMillis() - lastTimeLogin)/1000);
                long lastTimeLogout = rs.getTimestamp("last_time_logout").getTime();
                int secondsPass = (int) ((System.currentTimeMillis() - lastTimeLogout)/1000);
              /*  if (!session.isAdmin && !session.isAdmin2) {
                    Service.gI().sendThongBaoOK(session, "Đang Bảo Trì Chỉ Admin Mới Có Thể Vào");
                } else */if (rs.getBoolean("ban")) {
                    Service.gI().sendThongBaoOK(session, "Tài khoản đã bị khóa, do liên tục thực hiện hành vi xấu!");
                } else if (baotri && session.isAdmin) {
                    Service.gI().sendThongBaoOK(session, "Máy chủ đang bảo trì, bạn không thể vào khi đang bảo trì!");
                } else if (secondsPass1 < Manager.SECOND_WAIT_LOGIN) {
                    if (secondsPass < secondsPass1) {
                        Service.gI().sendThongBaoOK(session, "Vui lòng chờ " + (Manager.SECOND_WAIT_LOGIN - secondsPass) + "s");
                        return null;
                    }
                    Service.gI().sendThongBaoOK(session, "Vui lòng chờ " + (Manager.SECOND_WAIT_LOGIN - secondsPass1) + "s");
                    return null;
                } else if (rs.getTimestamp("last_time_login").getTime() > session.lastTimeLogout) {
                    Player plInGame = Client.gI().getPlayerByUser(session.userId);
                    if (plInGame != null) {
                        Client.gI().kickSession(plInGame.getSession());
                        Service.gI().sendThongBaoOK(session, "Tài khoản đang được đăng nhập tại máy chủ khác");
                    } else {
                    }
//                    Service.gI().sendThongBaoOK(session, "Tài khoản đang được đăng nhập tại máy chủ khác");
                } else {
                    if (secondsPass < Manager.SECOND_WAIT_LOGIN) {
                        Service.gI().sendThongBaoOK(session, "Vui lòng chờ " + (Manager.SECOND_WAIT_LOGIN - secondsPass) + "s");
                    } else {//set time logout trước rồi đọc data player
                        rs = GirlkunDB.executeQuery("select * from player where account_id = ? limit 1", session.userId);
                        if (!rs.first()) {
                            Service.gI().switchToCreateChar(session);
                            DataGame.sendDataItemBG(session);
                            DataGame.sendVersionGame(session);
                            DataGame.sendTileSetInfo(session);
                            Service.gI().sendMessage(session, -93, "1630679752231_-93_r");
                            DataGame.updateData(session);
                        } else {
                            Player plInGame = Client.gI().getPlayerByUser(session.userId);
                            if (plInGame != null) {
                                Client.gI().kickSession(plInGame.getSession());
                            }
                            long plHp = 200000000;
                            long plMp = 200000000;
                            JSONValue jv = new JSONValue();
                            JSONArray dataArray = null;
                            player = new Player();
                            //base info
                            player.id = rs.getInt("id");
                            player.name = rs.getString("name");
                            player.head = rs.getShort("head");
                            player.account_id = rs.getInt("account_id");
                            player.gender = rs.getByte("gender");
                             player.capChuyenSinh = rs.getInt("capChuyenSinh");
                               player.diemhotong = rs.getInt("diemhotong");
                            player.haveTennisSpaceShip = rs.getBoolean("have_tennis_space_ship");
                            int clanId = rs.getInt("clan_id_sv" + Manager.SERVER);
                            if (clanId != -1) {
                                Clan clan = ClanService.gI().getClanById(clanId);
                                for (ClanMember cm : clan.getMembers()) {
                                    if (cm.id == player.id) {
                                        clan.addMemberOnline(player);
                                        player.clan = clan;
                                        player.clanMember = cm;
                                        break;
                                    }
                                }
                            }

                            // load max gold
                            player.inventory.Elon_Musk = rs.getByte("Elon_Musk");
                            //data kim lượng
                            dataArray = (JSONArray) jv.parse(rs.getString("data_inventory"));
                            player.inventory.gold = Long.parseLong(String.valueOf(dataArray.get(0)));
                            player.inventory.gem = Integer.parseInt(String.valueOf(dataArray.get(1)));
                            player.inventory.ruby = Integer.parseInt(String.valueOf(dataArray.get(2)));
                            player.inventory.coupon = Integer.parseInt(String.valueOf(dataArray.get(3)));
                            player.inventory.event = Integer.parseInt(String.valueOf(dataArray.get(4)));
                            dataArray.clear();

                            // data rada card
                            dataArray = (JSONArray) jv.parse(rs.getString("data_card"));
                            for (int i = 0; i < dataArray.size(); i++) {
                                JSONObject obj = (JSONObject) dataArray.get(i);
                                player.Cards.add(new Card(Short.parseShort(obj.get("id").toString()), Byte.parseByte(obj.get("amount").toString()), Byte.parseByte(obj.get("max").toString()), Byte.parseByte(obj.get("level").toString()), loadOptionCard((JSONArray) JSONValue.parse(obj.get("option").toString())), Byte.parseByte(obj.get("used").toString())));
                            }
                            dataArray.clear();
                            
                            //data danh hiệu
                            dataArray = (JSONArray) JSONValue.parse(rs.getString("dhieune"));
                            player.titleitem = Byte.parseByte(String.valueOf(dataArray.get(0))) == 1 ? true : false;
                            player.titlett = Byte.parseByte(String.valueOf(dataArray.get(1))) == 1 ? true : false;
                            player.dhtang1 = Byte.parseByte(String.valueOf(dataArray.get(2))) == 1 ? true : false;
                            player.dhtang2 = Byte.parseByte(String.valueOf(dataArray.get(3))) == 1 ? true : false;
                            player.dhhanhtinh = Byte.parseByte(String.valueOf(dataArray.get(4))) == 1 ? true : false;
                            player.usedh1 = Byte.parseByte(String.valueOf(dataArray.get(5))) == 1 ? true : false;
                            player.timedh1 = Long.parseLong(String.valueOf(dataArray.get(6)));
                            player.usedh2 = Byte.parseByte(String.valueOf(dataArray.get(7))) == 1 ? true : false;
                            player.timedh2 = Long.parseLong(String.valueOf(dataArray.get(8)));
                            player.usedh3 = Byte.parseByte(String.valueOf(dataArray.get(9))) == 1 ? true : false;
                            player.timedh3 = Long.parseLong(String.valueOf(dataArray.get(10)));
                            dataArray.clear();
                            //data điểm nè
                            dataArray = (JSONArray) JSONValue.parse(rs.getString("data_diem"));
                            player.nhsPoint = Integer.parseInt(String.valueOf(dataArray.get(0)));
                            player.gtPoint = Integer.parseInt(String.valueOf(dataArray.get(1)));
                            player.ltggtv4 = Long.parseLong(String.valueOf(dataArray.get(2)));
                            player.vip = Byte.parseByte(String.valueOf(dataArray.get(3)));
                            player.timevip = Long.parseLong(String.valueOf(dataArray.get(4)));
                            player.pointPvp = Integer.parseInt(String.valueOf(dataArray.get(5)));
                            player.pointSb = Integer.parseInt(String.valueOf(dataArray.get(6)));
                            dataArray.clear();
                            //data điểm danh
                            dataArray = (JSONArray) JSONValue.parse(rs.getString("diemdanh"));
                            player.CheckDayOnl = Byte.parseByte(String.valueOf(dataArray.get(0)));
                            player.diemdanhtet = Byte.parseByte(String.valueOf(dataArray.get(1)));
                            player.nhanban = Byte.parseByte(String.valueOf(dataArray.get(2)));
                            player.diemdanh = Byte.parseByte(String.valueOf(dataArray.get(3)));
                            dataArray.clear();
                            try {
                            //    if ((new java.sql.Date(player.diemdanh)).getDay() != (new java.sql.Date(System.currentTimeMillis())).getDay()) {
                            //        player.diemdanh = 0;
                            //    }
                            Calendar ngayvps = Calendar.getInstance();
                            int ngayhomnay = ngayvps.get(Calendar.DAY_OF_MONTH);
                            if (player.CheckDayOnl == 0) player.CheckDayOnl =(byte)(ngayhomnay-1);
                                if (ngayhomnay > player.CheckDayOnl) {
                                    player.CheckDayOnl = (byte)ngayhomnay;
                                    player.diemdanhtet = 0;
                                    player.diemdanh = 0;
                                    player.nhanban = 0;
                                }
                            } catch (Exception e) {
                            }
                            //data tọa độ
                            try {
                                dataArray = (JSONArray) jv.parse(rs.getString("data_location"));
                                int mapId = Integer.parseInt(String.valueOf(dataArray.get(0)));
                                player.location.x = Integer.parseInt(String.valueOf(dataArray.get(1)));
                                player.location.y = Integer.parseInt(String.valueOf(dataArray.get(2)));
                                player.location.lastTimeplayerMove = System.currentTimeMillis();
                                if (MapService.gI().isMapDoanhTrai(mapId) || MapService.gI().isMapBlackBallWar(mapId)
                                        || MapService.gI().isMapBanDoKhoBau(mapId) || MapService.gI().isMapMaBu(mapId)
                                        || MapService.gI().isMapConDuongRanDoc(mapId) || MapService.gI().isMapKhiGaHuyDiet(mapId)) {
                                    mapId = player.gender + 21;
                                    player.location.x = 300;
                                    player.location.y = 336;
                                }
                                player.zone = MapService.gI().getMapCanJoin(player, mapId, -1);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            dataArray.clear();

                            //data chỉ số
                            dataArray = (JSONArray) JSONValue.parse(rs.getString("data_point"));
                            player.nPoint.limitPower = Byte.parseByte(String.valueOf(dataArray.get(0)));
                            player.nPoint.power = Long.parseLong(String.valueOf(dataArray.get(1)));
                            player.nPoint.tiemNang = Long.parseLong(String.valueOf(dataArray.get(2)));
                            player.nPoint.stamina = Short.parseShort(String.valueOf(dataArray.get(3)));
                            player.nPoint.maxStamina = Short.parseShort(String.valueOf(dataArray.get(4)));
                            player.nPoint.hpg = Long.parseLong(String.valueOf(dataArray.get(5)));
                            player.nPoint.mpg = Long.parseLong(String.valueOf(dataArray.get(6)));
                            player.nPoint.dameg = Long.parseLong(String.valueOf(dataArray.get(7)));
                            player.nPoint.defg = Integer.parseInt(String.valueOf(dataArray.get(8)));
                            player.nPoint.critg = Byte.parseByte(String.valueOf(dataArray.get(9)));
                            dataArray.get(10); //** Năng động
                            plHp = Long.parseLong(String.valueOf(dataArray.get(11)));
                            plMp = Long.parseLong(String.valueOf(dataArray.get(12)));
                            dataArray.clear();
                             player.rankSieuHang = rs.getInt("rank_sieu_hang");
                            if(player.rankSieuHang == -99){
                                player.rankSieuHang =  ServerManager.gI().getNumPlayer();
                            }

                            //data đậu thần
                            dataArray = (JSONArray) jv.parse(rs.getString("data_magic_tree"));
                            byte level = Byte.parseByte(String.valueOf(dataArray.get(0)));
                            byte currPea = Byte.parseByte(String.valueOf(dataArray.get(1)));
                            boolean isUpgrade = Byte.parseByte(String.valueOf(dataArray.get(2))) == 1;
                            long lastTimeHarvest = Long.parseLong(String.valueOf(dataArray.get(3)));
                            long lastTimeUpgrade = Long.parseLong(String.valueOf(dataArray.get(4)));
                            player.magicTree = new MagicTree(player, level, currPea, lastTimeHarvest, isUpgrade, lastTimeUpgrade);
                            dataArray.clear();

                            //data phần thưởng sao đen
                            dataArray = (JSONArray) jv.parse(rs.getString("data_black_ball"));
                            JSONArray dataBlackBall = null;
                            for (int i = 0; i < dataArray.size(); i++) {
                                dataBlackBall = (JSONArray) jv.parse(String.valueOf(dataArray.get(i)));
                                player.rewardBlackBall.timeOutOfDateReward[i] = Long.parseLong(String.valueOf(dataBlackBall.get(0)));
                                player.rewardBlackBall.lastTimeGetReward[i] = Long.parseLong(String.valueOf(dataBlackBall.get(1)));
                                try {
                                    player.rewardBlackBall.quantilyBlackBall[i] = dataBlackBall.get(2) != null ? Integer.parseInt(String.valueOf(dataBlackBall.get(2))) : 0;
                                } catch (Exception e) {
                                    player.rewardBlackBall.quantilyBlackBall[i] = player.rewardBlackBall.timeOutOfDateReward[i] != 0 ? 1 : 0;
                                }
                                dataBlackBall.clear();
                            }
                            dataArray.clear();

                            //data body
                            dataArray = (JSONArray) jv.parse(rs.getString("items_body"));
                            for (int i = 0; i < dataArray.size(); i++) {
                                Item item = null;
                                JSONArray dataItem = (JSONArray) jv.parse(dataArray.get(i).toString());
                                short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                                if (tempId != -1) {
                                    item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                                    JSONArray options = (JSONArray) jv.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                                    for (int j = 0; j < options.size(); j++) {
                                        JSONArray opt = (JSONArray) jv.parse(String.valueOf(options.get(j)));
                                        item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                                Integer.parseInt(String.valueOf(opt.get(1)))));
                                    }
                                    item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                                    if (ItemService.gI().isOutOfDateTime(item)) {
                                        item = ItemService.gI().createItemNull();
                                    }
                                } else {
                                    item = ItemService.gI().createItemNull();
                                }
                                player.inventory.itemsBody.add(item);
                            }
                            if (player.inventory.itemsBody.size() == 11) {
                                player.inventory.itemsBody.add(ItemService.gI().createItemNull());
                            }
                            dataArray.clear();

                            //data bag
                            dataArray = (JSONArray) jv.parse(rs.getString("items_bag"));
                            for (int i = 0; i < dataArray.size(); i++) {
                                Item item = null;
                                JSONArray dataItem = (JSONArray) jv.parse(dataArray.get(i).toString());
                                short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                                if (tempId != -1) {
                                    item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                                    JSONArray options = (JSONArray) jv.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                                    for (int j = 0; j < options.size(); j++) {
                                        JSONArray opt = (JSONArray) jv.parse(String.valueOf(options.get(j)));
                                        item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                                Integer.parseInt(String.valueOf(opt.get(1)))));
                                    }
                                    item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                                    if (ItemService.gI().isOutOfDateTime(item)) {
                                        item = ItemService.gI().createItemNull();
                                    }
                                } else {
                                    item = ItemService.gI().createItemNull();
                                }
                                player.inventory.itemsBag.add(item);
                            }
                            dataArray.clear();

                            //data box
                            dataArray = (JSONArray) jv.parse(rs.getString("items_box"));
                            for (int i = 0; i < dataArray.size(); i++) {
                                Item item = null;
                                JSONArray dataItem = (JSONArray) jv.parse(dataArray.get(i).toString());
                                short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                                if (tempId != -1) {
                                    item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                                    JSONArray options = (JSONArray) jv.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                                    for (int j = 0; j < options.size(); j++) {
                                        JSONArray opt = (JSONArray) jv.parse(String.valueOf(options.get(j)));
                                        item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                                Integer.parseInt(String.valueOf(opt.get(1)))));
                                    }
                                    item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                                    if (ItemService.gI().isOutOfDateTime(item)) {
                                        item = ItemService.gI().createItemNull();
                                    }
                                } else {
                                    item = ItemService.gI().createItemNull();
                                }
                                player.inventory.itemsBox.add(item);
                            }
                            dataArray.clear();

                            //data box lucky round
                            dataArray = (JSONArray) jv.parse(rs.getString("items_box_lucky_round"));
                            for (int i = 0; i < dataArray.size(); i++) {
                                Item item = null;
                                JSONArray dataItem = (JSONArray) jv.parse(dataArray.get(i).toString());
                                short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                                if (tempId != -1) {
                                    item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                                    JSONArray options = (JSONArray) jv.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                                    for (int j = 0; j < options.size(); j++) {
                                        JSONArray opt = (JSONArray) jv.parse(String.valueOf(options.get(j)));
                                        item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                                Integer.parseInt(String.valueOf(opt.get(1)))));
                                    }
                                    player.inventory.itemsBoxCrackBall.add(item);
                                }
                            }
                            dataArray.clear();

                            //data friends
                            dataArray = (JSONArray) jv.parse(rs.getString("friends"));
                            if (dataArray != null) {
                                for (int i = 0; i < dataArray.size(); i++) {
                                    JSONArray dataFE = (JSONArray) jv.parse(String.valueOf(dataArray.get(i)));
                                    Friend friend = new Friend();
                                    friend.id = Integer.parseInt(String.valueOf(dataFE.get(0)));
                                    friend.name = String.valueOf(dataFE.get(1));
                                    friend.head = Short.parseShort(String.valueOf(dataFE.get(2)));
                                    friend.body = Short.parseShort(String.valueOf(dataFE.get(3)));
                                    friend.leg = Short.parseShort(String.valueOf(dataFE.get(4)));
                                    friend.bag = Byte.parseByte(String.valueOf(dataFE.get(5)));
                                    friend.power = Long.parseLong(String.valueOf(dataFE.get(6)));
                                    player.friends.add(friend);
                                    dataFE.clear();
                                }
                                dataArray.clear();
                            }

                            //data enemies
                            dataArray = (JSONArray) jv.parse(rs.getString("enemies"));
                            if (dataArray != null) {
                                for (int i = 0; i < dataArray.size(); i++) {
                                    JSONArray dataFE = (JSONArray) jv.parse(String.valueOf(dataArray.get(i)));
                                    Enemy enemy = new Enemy();
                                    enemy.id = Integer.parseInt(String.valueOf(dataFE.get(0)));
                                    enemy.name = String.valueOf(dataFE.get(1));
                                    enemy.head = Short.parseShort(String.valueOf(dataFE.get(2)));
                                    enemy.body = Short.parseShort(String.valueOf(dataFE.get(3)));
                                    enemy.leg = Short.parseShort(String.valueOf(dataFE.get(4)));
                                    enemy.bag = Byte.parseByte(String.valueOf(dataFE.get(5)));
                                    enemy.power = Long.parseLong(String.valueOf(dataFE.get(6)));
                                    player.enemies.add(enemy);
                                    dataFE.clear();
                                }
                                dataArray.clear();
                            }

                            //data nội tại
                            dataArray = (JSONArray) jv.parse(rs.getString("data_intrinsic"));
                            byte intrinsicId = Byte.parseByte(String.valueOf(dataArray.get(0)));
                            player.playerIntrinsic.intrinsic = IntrinsicService.gI().getIntrinsicById(intrinsicId);
                            player.playerIntrinsic.intrinsic.param1 = Short.parseShort(String.valueOf(dataArray.get(1)));
                            player.playerIntrinsic.intrinsic.param2 = Short.parseShort(String.valueOf(dataArray.get(2)));
                            player.playerIntrinsic.countOpen = Byte.parseByte(String.valueOf(dataArray.get(3)));
                            dataArray.clear();

                            //0
                            dataArray = (JSONArray) jv.parse(rs.getString("data_item_time"));
                            int timeBoHuyet = Integer.parseInt(String.valueOf(dataArray.get(0)));
                            int timeBoKhi = Integer.parseInt(String.valueOf(dataArray.get(1)));
                            int timeGiapXen = Integer.parseInt(String.valueOf(dataArray.get(2)));
                            int timeCuongNo = Integer.parseInt(String.valueOf(dataArray.get(3)));
                            int timeAnDanh = Integer.parseInt(String.valueOf(dataArray.get(4)));
                            int timeOpenPower = Integer.parseInt(String.valueOf(dataArray.get(5)));
                            int timeMayDo = Integer.parseInt(String.valueOf(dataArray.get(6)));
                            int timeMayDo2 = Integer.parseInt(String.valueOf(dataArray.get(7)));

                            int timeMeal = Integer.parseInt(String.valueOf(dataArray.get(8)));
                            int iconMeal = Integer.parseInt(String.valueOf(dataArray.get(9)));

                            int timeUseTDLT = 0;
                            if (dataArray.size() == 11) {
                                timeUseTDLT = Integer.parseInt(String.valueOf(dataArray.get(10)));
                            }

                            player.itemTime.lastTimeBoHuyet = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeBoHuyet);
                            player.itemTime.lastTimeBoKhi = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeBoKhi);
                            player.itemTime.lastTimeGiapXen = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeGiapXen);
                            player.itemTime.lastTimeCuongNo = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeCuongNo);
                            player.itemTime.lastTimeAnDanh = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeAnDanh);
                            player.itemTime.lastTimeOpenPower = System.currentTimeMillis() - (ItemTime.TIME_OPEN_POWER - timeOpenPower);
                            player.itemTime.lastTimeUseMayDo = System.currentTimeMillis() - (ItemTime.TIME_MAY_DO - timeMayDo);
                            player.itemTime.lastTimeUseMayDo2 = System.currentTimeMillis() - (ItemTime.TIME_MAY_DO2 - timeMayDo2);
                            player.itemTime.lastTimeEatMeal = System.currentTimeMillis() - (ItemTime.TIME_EAT_MEAL - timeMeal);
                            player.itemTime.timeTDLT = timeUseTDLT * 60 * 1000;
                            player.itemTime.lastTimeUseTDLT = System.currentTimeMillis();

                            player.itemTime.iconMeal = iconMeal;
                            player.itemTime.isUseBoHuyet = timeBoHuyet != 0;
                            player.itemTime.isUseBoKhi = timeBoKhi != 0;
                            player.itemTime.isUseGiapXen = timeGiapXen != 0;
                            player.itemTime.isUseCuongNo = timeCuongNo != 0;
                            player.itemTime.isUseAnDanh = timeAnDanh != 0;
                            player.itemTime.isOpenPower = timeOpenPower != 0;
                            player.itemTime.isUseMayDo = timeMayDo != 0;
                            player.itemTime.isUseMayDo2 = timeMayDo2 != 0;

                            player.itemTime.isEatMeal = timeMeal != 0;
                            player.itemTime.isUseTDLT = timeUseTDLT != 0;
                            dataArray.clear();

                            // data thanh tuu
                            for (ArchivementTemplate a : Manager.A_TEMPLATES) {
                                ThanhTich thanhtich = new ThanhTich();
                                thanhtich.Template = a;
                                thanhtich.isFinish = false;
                                thanhtich.isRecieve = false;
                                player.Archivement.add(new ThanhTich(thanhtich));
                            }
                            ThanhTichPlayer.GetRecieve(player);
                            
                            //data nhiệm vụ
                            dataArray = (JSONArray) jv.parse(rs.getString("data_task"));
                            TaskMain taskMain = TaskService.gI().getTaskMainById(player, Byte.parseByte(String.valueOf(dataArray.get(0))));
                            taskMain.index = Byte.parseByte(String.valueOf(dataArray.get(1)));
                            taskMain.subTasks.get(taskMain.index).count = Short.parseShort(String.valueOf(dataArray.get(2)));
                            player.playerTask.taskMain = taskMain;
                            dataArray.clear();

                            //data nhiệm vụ hàng ngày
                            dataArray = (JSONArray) jv.parse(rs.getString("data_side_task"));
                            String format = "dd-MM-yyyy";
                            long receivedTime = Long.parseLong(String.valueOf(dataArray.get(1)));
                            Date date = new Date(receivedTime);
                            if (TimeUtil.formatTime(date, format).equals(TimeUtil.formatTime(new Date(), format))) {
                                player.playerTask.sideTask.template = TaskService.gI().getSideTaskTemplateById(Integer.parseInt(String.valueOf(dataArray.get(0))));
                                player.playerTask.sideTask.count = Integer.parseInt(String.valueOf(dataArray.get(2)));
                                player.playerTask.sideTask.maxCount = Integer.parseInt(String.valueOf(dataArray.get(3)));
                                player.playerTask.sideTask.leftTask = Integer.parseInt(String.valueOf(dataArray.get(4)));
                                player.playerTask.sideTask.level = Integer.parseInt(String.valueOf(dataArray.get(5)));
                                player.playerTask.sideTask.receivedTime = receivedTime;
                            }
                            //                              data item time siêu cấp
                            dataArray = (JSONArray) jv.parse(rs.getString("data_item_time_sieu_cap"));
                            int timeBoHuyetSC = Integer.parseInt(String.valueOf(dataArray.get(0)));
                            int timeBoKhiSC = Integer.parseInt(String.valueOf(dataArray.get(1)));
                            int timeGiapXenSC = Integer.parseInt(String.valueOf(dataArray.get(2)));
                            int timeCuongNoSC = Integer.parseInt(String.valueOf(dataArray.get(3)));
                            int timeAnDanhSC = Integer.parseInt(String.valueOf(dataArray.get(4)));
                            int TIMEBANH = Integer.parseInt(String.valueOf(dataArray.get(5)));
                            int ICONBANH = Integer.parseInt(String.valueOf(dataArray.get(6)));
                            long TIMENGOC = Long.parseLong(String.valueOf(dataArray.get(7)));
                            player.itemTime.lastTimeBoHuyetSC = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeBoHuyetSC);
                            player.itemTime.lastTimeBoKhiSC = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeBoKhiSC);
                            player.itemTime.lastTimeGiapXenSC = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeGiapXenSC);
                            player.itemTime.lastTimeCuongNoSC = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeCuongNoSC);
                            player.itemTime.lastTimeAnDanhSC = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeAnDanhSC);
                            player.itemTime.LTBANH = System.currentTimeMillis() - (ItemTime.TIMEBANH - TIMEBANH);
                            player.itemTime.LTNGOC = System.currentTimeMillis() - (ItemTime.TIMENGOC - TIMENGOC);
                            
                            player.itemTime.isUseBoHuyetSC = timeBoHuyetSC != 0;
                            player.itemTime.isUseBoKhiSC = timeBoKhiSC != 0;
                            player.itemTime.isUseGiapXenSC = timeGiapXenSC != 0;
                            player.itemTime.isUseCuongNoSC = timeCuongNoSC != 0;
                            player.itemTime.isUseAnDanhSC = timeAnDanhSC != 0;
                            player.itemTime.VENGOC = TIMENGOC != 0;
                            player.itemTime.ISBANH = TIMEBANH != 0;
                            player.itemTime.ICONBANH = ICONBANH;
                            dataArray.clear();
                            
                            //data trứng bư
                            dataArray = (JSONArray) jv.parse(rs.getString("data_mabu_egg"));
                            if (dataArray.size() != 0) {
                                player.mabuEgg = new MabuEgg(player, Long.parseLong(String.valueOf(dataArray.get(0))),
                                        Long.parseLong(String.valueOf(dataArray.get(1))));
                            }
                            dataArray.clear();

                            //data trứng bill
                            dataArray = (JSONArray) jv.parse(rs.getString("bill_data"));
                            if (dataArray.size() != 0) {
                                player.billEgg = new BillEgg(player, Long.parseLong(String.valueOf(dataArray.get(0))),
                                        Long.parseLong(String.valueOf(dataArray.get(1))));
                            }
                            dataArray.clear();

                            //data bùa
                            dataArray = (JSONArray) jv.parse(rs.getString("data_charm"));
                            player.charms.tdTriTue = Long.parseLong(String.valueOf(dataArray.get(0)));
                            player.charms.tdManhMe = Long.parseLong(String.valueOf(dataArray.get(1)));
                            player.charms.tdDaTrau = Long.parseLong(String.valueOf(dataArray.get(2)));
                            player.charms.tdOaiHung = Long.parseLong(String.valueOf(dataArray.get(3)));
                            player.charms.tdBatTu = Long.parseLong(String.valueOf(dataArray.get(4)));
                            player.charms.tdDeoDai = Long.parseLong(String.valueOf(dataArray.get(5)));
                            player.charms.tdThuHut = Long.parseLong(String.valueOf(dataArray.get(6)));
                            player.charms.tdDeTu = Long.parseLong(String.valueOf(dataArray.get(7)));
                            player.charms.tdTriTue3 = Long.parseLong(String.valueOf(dataArray.get(8)));
                            player.charms.tdTriTue4 = Long.parseLong(String.valueOf(dataArray.get(9)));
                            dataArray.clear();

                            //data skill
                            dataArray = (JSONArray) jv.parse(rs.getString("skills"));
                            for (int i = 0; i < dataArray.size(); i++) {
                                JSONArray dataSkill = (JSONArray) jv.parse(String.valueOf(dataArray.get(i)));
                                int tempId = Integer.parseInt(String.valueOf(dataSkill.get(0)));
                                byte point = Byte.parseByte(String.valueOf(dataSkill.get(1)));
                                Skill skill = null;
                                if (point != 0) {
                                    skill = SkillUtil.createSkill(tempId, point);
                                } else {
                                    skill = SkillUtil.createSkillLevel0(tempId);
                                }
                                skill.lastTimeUseThisSkill = Long.parseLong(String.valueOf(dataSkill.get(2)));
                                if (dataSkill.size() > 3) {
                                    skill.currLevel = Short.parseShort(String.valueOf(dataSkill.get(3)));
                                }
                                player.playerSkill.skills.add(skill);
                            }
                            dataArray.clear();

                            //data skill shortcut
                            dataArray = (JSONArray) jv.parse(rs.getString("skills_shortcut"));
                            for (int i = 0; i < dataArray.size(); i++) {
                                player.playerSkill.skillShortCut[i] = Byte.parseByte(String.valueOf(dataArray.get(i)));
                            }
                            for (int i : player.playerSkill.skillShortCut) {
                                if (player.playerSkill.getSkillbyId(i) != null && player.playerSkill.getSkillbyId(i).damage > 0) {
                                    player.playerSkill.skillSelect = player.playerSkill.getSkillbyId(i);
                                    break;
                                }
                            }
                            if (player.playerSkill.skillSelect == null) {
                                player.playerSkill.skillSelect = player.playerSkill.getSkillbyId(player.gender == ConstPlayer.TRAI_DAT
                                        ? Skill.DRAGON : (player.gender == ConstPlayer.NAMEC ? Skill.DEMON : Skill.GALICK));
                            }
                            dataArray.clear();

                            //data pet
                            JSONArray petData = (JSONArray) jv.parse(rs.getString("pet"));
                            if (!petData.isEmpty()) {
                                dataArray = (JSONArray) jv.parse(String.valueOf(petData.get(0)));
                                Pet pet = new Pet(player);
                                pet.id = -player.id;
                                pet.typePet = Byte.parseByte(String.valueOf(dataArray.get(0)));
                                pet.gender = Byte.parseByte(String.valueOf(dataArray.get(1)));
                                pet.name = String.valueOf(dataArray.get(2));
                                player.fusion.typeFusion = Byte.parseByte(String.valueOf(dataArray.get(3)));
                                player.fusion.lastTimeFusion = System.currentTimeMillis()
                                        - (Fusion.TIME_FUSION - Integer.parseInt(String.valueOf(dataArray.get(4))));
                                if(player.fusion.typeFusion == ConstPlayer.HOP_THE_GOGETA) {
                                    player.fusion.lastTimeFusion = System.currentTimeMillis()
                                        - (Fusion.TIME_FUSION2 - Integer.parseInt(String.valueOf(dataArray.get(4))));
                                }
                                pet.status = Byte.parseByte(String.valueOf(dataArray.get(5)));
                                pet.thuctinh = Byte.parseByte(String.valueOf(dataArray.get(6)));

                                //data chỉ số
                                dataArray = (JSONArray) jv.parse(String.valueOf(petData.get(1)));
                                pet.nPoint.limitPower = Byte.parseByte(String.valueOf(dataArray.get(0)));
                                pet.nPoint.power = Long.parseLong(String.valueOf(dataArray.get(1)));
                                pet.nPoint.tiemNang = Long.parseLong(String.valueOf(dataArray.get(2)));
                                pet.nPoint.stamina = Short.parseShort(String.valueOf(dataArray.get(3)));
                                pet.nPoint.maxStamina = Short.parseShort(String.valueOf(dataArray.get(4)));
                                pet.nPoint.hpg = Integer.parseInt(String.valueOf(dataArray.get(5)));
                                pet.nPoint.mpg = Integer.parseInt(String.valueOf(dataArray.get(6)));
                                pet.nPoint.dameg = Integer.parseInt(String.valueOf(dataArray.get(7)));
                                pet.nPoint.defg = Integer.parseInt(String.valueOf(dataArray.get(8)));
                                pet.nPoint.critg = Integer.parseInt(String.valueOf(dataArray.get(9)));
                                long hp = Long.parseLong(String.valueOf(dataArray.get(10)));
                                long mp = Long.parseLong(String.valueOf(dataArray.get(11)));

                                //data body
                                dataArray = (JSONArray) jv.parse(String.valueOf(petData.get(2)));
                                for (int i = 0; i < dataArray.size(); i++) {
                                    Item item = null;
                                    JSONArray dataItem = (JSONArray) jv.parse(String.valueOf(dataArray.get(i)));
                                    short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                                    if (tempId != -1) {
                                        item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                                        JSONArray options = (JSONArray) jv.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                                        for (int j = 0; j < options.size(); j++) {
                                            JSONArray opt = (JSONArray) jv.parse(String.valueOf(options.get(j)));
                                            item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                                    Integer.parseInt(String.valueOf(opt.get(1)))));
                                        }
                                        item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                                        if (ItemService.gI().isOutOfDateTime(item)) {
                                            item = ItemService.gI().createItemNull();
                                        }
                                    } else {
                                        item = ItemService.gI().createItemNull();
                                        ;
                                    }
                                    pet.inventory.itemsBody.add(item);
                                }
                                //data skills
                                dataArray = (JSONArray) jv.parse(String.valueOf(petData.get(3)));
                                for (int i = 0; i < dataArray.size(); i++) {
                                    JSONArray skillTemp = (JSONArray) jv.parse(String.valueOf(dataArray.get(i)));
                                    int tempId = Integer.parseInt(String.valueOf(skillTemp.get(0)));
                                    byte point = Byte.parseByte(String.valueOf(skillTemp.get(1)));
                                    Skill skill = null;
                                    if (point != 0) {
                                        skill = SkillUtil.createSkill(tempId, point);
                                    } else {
                                        skill = SkillUtil.createSkillLevel0(tempId);
                                    }
                                    switch (skill.template.id) {
                                        case Skill.KAMEJOKO:
                                        case Skill.MASENKO:
                                        case Skill.ANTOMIC:
                                            skill.coolDown = 1000;
                                            break;
                                    }
                                    pet.playerSkill.skills.add(skill);
                                }
                                if (pet.playerSkill.skills.size() < 5) {
                                    pet.playerSkill.skills.add(4, SkillUtil.createSkillLevel0(-1));
                                }
                                pet.nPoint.hp = hp;
                                pet.nPoint.mp = mp;
                                player.pet = pet;
                                dataArray.clear();
                            }
//                            if (player.vip == 4) {
//                                ServerNotify.gI().sendThongBaoBenDuoi("Thành viên SVIP "+player.name+" đã đăng nhập!");
//                                Service.gI().sendThongBaoAllPlayer("|2|Thành viên SVIP\n|7|["+player.name+"]\n|2| đã đăng nhập!");
//                            }
                            player.nPoint.hp = plHp;
                            player.nPoint.mp = plMp;
                            player.iDMark.setLoadedAllDataPlayer(true);
                            GirlkunDB.executeUpdate("update account set last_time_login = '" + new Timestamp(System.currentTimeMillis()) + "', ip_address = '" + session.ipAddress + "' where id = " + session.userId);
                        
                        /* █████████ EVENT ██████████ */
                        GirlkunResultSet checksql = GirlkunDB.executeQuery("select * from a_player_sukien where id = ?", player.id);
                            if (checksql.first()) {
                                dataArray = (JSONArray) JSONValue.parse(checksql.getString("sk_tet"));
                                player.BanhChung = Integer.parseInt(String.valueOf(dataArray.get(0)));
                                player.BanhTet = Integer.parseInt(String.valueOf(dataArray.get(1)));
                                player.BanhChungDaNau = Integer.parseInt(String.valueOf(dataArray.get(2)));
                                player.BanhTetDaNau = Integer.parseInt(String.valueOf(dataArray.get(3)));
                                dataArray.clear();
                                dataArray = (JSONArray) JSONValue.parse(checksql.getString("top"));
                                player.topsm = Byte.parseByte(String.valueOf(dataArray.get(0)));
                                player.topnv = Byte.parseByte(String.valueOf(dataArray.get(1)));
                                player.topnap = Byte.parseByte(String.valueOf(dataArray.get(2)));
                                dataArray.clear();
                                dataArray = (JSONArray) JSONValue.parse(checksql.getString("mocnap_mocsb"));
                                player.mocnap = Integer.parseInt(String.valueOf(dataArray.get(0)));
                                player.mocsanboss = Integer.parseInt(String.valueOf(dataArray.get(1)));
                                dataArray.clear();
                            } else {
                                GirlkunDB.executeUpdate("insert into a_player_sukien (id,name) values()",player.id,player.name);
                                Logger.success("Tạo data player event mới thành công!\n");
                                checksql.dispose();
                            }
                        }
                    }
                }
                al.reset();
            } else {
                Service.gI().sendThongBaoOK(session, "Thông tin tài khoản hoặc mật khẩu không chính xác");
                al.wrong();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.error(session.uu);
            player.dispose();
            player = null;
            Logger.logException(GodGK.class, e);
        } finally {
            if (rs != null) {
                rs.dispose();
            }
        }
        return player;
    }

    public static void checkDo() {
        long st = System.currentTimeMillis();
        JSONValue jv = new JSONValue();
        JSONArray dataArray = null;
        JSONObject dataObject = null;
        Player player;
        PreparedStatement ps = null;
        String name = "";
        ResultSet rs = null;
        try (Connection con = GirlkunDB.getConnection()) {
            ps = con.prepareStatement("select * from player");
            rs = ps.executeQuery();
            while (rs.next()) {
                int plHp = 200000000;
                int plMp = 200000000;
                player = new Player();
                player.id = rs.getInt("id");
                player.name = rs.getString("name");
                name = rs.getString("name");
                player.head = rs.getShort("head");
                player.gender = rs.getByte("gender");
                player.haveTennisSpaceShip = rs.getBoolean("have_tennis_space_ship");
                //data kim lượng
                dataArray = (JSONArray) JSONValue.parse(rs.getString("data_inventory"));
                player.inventory.gold = Integer.parseInt(String.valueOf(dataArray.get(0)));
                player.inventory.gem = Integer.parseInt(String.valueOf(dataArray.get(1)));
                player.inventory.ruby = Integer.parseInt(String.valueOf(dataArray.get(2)));
                if (dataArray.size() == 4) {
                    player.inventory.coupon = Integer.parseInt(String.valueOf(dataArray.get(3)));
                } else {
                    player.inventory.coupon = 0;
                }
                dataArray.clear();

                //data chỉ số
                dataArray = (JSONArray) JSONValue.parse(rs.getString("data_point"));
                player.nPoint.limitPower = Byte.parseByte(String.valueOf(dataArray.get(0)));
                player.nPoint.power = Long.parseLong(String.valueOf(dataArray.get(1)));
                player.nPoint.tiemNang = Long.parseLong(String.valueOf(dataArray.get(2)));
                player.nPoint.stamina = Short.parseShort(String.valueOf(dataArray.get(3)));
                player.nPoint.maxStamina = Short.parseShort(String.valueOf(dataArray.get(4)));
                player.nPoint.hpg = Integer.parseInt(String.valueOf(dataArray.get(5)));
                player.nPoint.mpg = Integer.parseInt(String.valueOf(dataArray.get(6)));
                player.nPoint.dameg = Integer.parseInt(String.valueOf(dataArray.get(7)));
                player.nPoint.defg = Integer.parseInt(String.valueOf(dataArray.get(8)));
                player.nPoint.critg = Byte.parseByte(String.valueOf(dataArray.get(9)));
                dataArray.get(10); //** Năng động
                plHp = Integer.parseInt(String.valueOf(dataArray.get(11)));
                plMp = Integer.parseInt(String.valueOf(dataArray.get(12)));
                dataArray.clear();

                //data body
                dataArray = (JSONArray) JSONValue.parse(rs.getString("items_body"));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    JSONArray dataItem = (JSONArray) JSONValue.parse(dataArray.get(i).toString());
                    short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                        JSONArray options = (JSONArray) JSONValue.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                        for (int j = 0; j < options.size(); j++) {
                            JSONArray opt = (JSONArray) JSONValue.parse(String.valueOf(options.get(j)));
                            item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                    Integer.parseInt(String.valueOf(opt.get(1)))));

                        }
                        item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                        if (ItemService.gI().isOutOfDateTime(item)) {
                            item = ItemService.gI().createItemNull();
                        }
                    } else {
                        item = ItemService.gI().createItemNull();
                    }
                    Util.useCheckDo(player, item, "body");
                    player.inventory.itemsBody.add(item);
                }
                dataArray.clear();

                //data bag
                dataArray = (JSONArray) JSONValue.parse(rs.getString("items_bag"));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    JSONArray dataItem = (JSONArray) JSONValue.parse(dataArray.get(i).toString());
                    short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                        JSONArray options = (JSONArray) JSONValue.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                        for (int j = 0; j < options.size(); j++) {
                            JSONArray opt = (JSONArray) JSONValue.parse(String.valueOf(options.get(j)));
                            item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                    Integer.parseInt(String.valueOf(opt.get(1)))));
                        }
                        item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                        if (ItemService.gI().isOutOfDateTime(item)) {
                            item = ItemService.gI().createItemNull();
                        }
                    } else {
                        item = ItemService.gI().createItemNull();
                    }
                    Util.useCheckDo(player, item, "bag");
                    player.inventory.itemsBag.add(item);
                }
                dataArray.clear();

                //data box
                dataArray = (JSONArray) JSONValue.parse(rs.getString("items_box"));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    JSONArray dataItem = (JSONArray) JSONValue.parse(dataArray.get(i).toString());
                    short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                        JSONArray options = (JSONArray) JSONValue.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                        for (int j = 0; j < options.size(); j++) {
                            JSONArray opt = (JSONArray) JSONValue.parse(String.valueOf(options.get(j)));
                            item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                    Integer.parseInt(String.valueOf(opt.get(1)))));
                        }
                        item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                        if (ItemService.gI().isOutOfDateTime(item)) {
                            item = ItemService.gI().createItemNull();
                        }
                    } else {
                        item = ItemService.gI().createItemNull();
                    }
                    Util.useCheckDo(player, item, "box");
                    player.inventory.itemsBox.add(item);
                }
                dataArray.clear();

                //data box lucky round
                dataArray = (JSONArray) JSONValue.parse(rs.getString("items_box_lucky_round"));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    JSONArray dataItem = (JSONArray) JSONValue.parse(dataArray.get(i).toString());
                    short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                        JSONArray options = (JSONArray) JSONValue.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                        for (int j = 0; j < options.size(); j++) {
                            JSONArray opt = (JSONArray) JSONValue.parse(String.valueOf(options.get(j)));
                            item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                    Integer.parseInt(String.valueOf(opt.get(1)))));
                        }
                        player.inventory.itemsBoxCrackBall.add(item);
                    }
                }
                dataArray.clear();

                //data pet
                JSONArray petData = (JSONArray) jv.parse(rs.getString("pet"));
                if (!petData.isEmpty()) {
                    dataArray = (JSONArray) jv.parse(String.valueOf(petData.get(0)));
                    Pet pet = new Pet(player);
                    pet.id = -player.id;
                    pet.typePet = Byte.parseByte(String.valueOf(dataArray.get(0)));
                    pet.gender = Byte.parseByte(String.valueOf(dataArray.get(1)));
                    pet.name = String.valueOf(dataArray.get(2));
                    player.fusion.typeFusion = Byte.parseByte(String.valueOf(dataArray.get(3)));
                    player.fusion.lastTimeFusion = System.currentTimeMillis()
                            - (Fusion.TIME_FUSION - Integer.parseInt(String.valueOf(dataArray.get(4))));
                    pet.status = Byte.parseByte(String.valueOf(dataArray.get(5)));
                    pet.thuctinh = Byte.parseByte(String.valueOf(dataArray.get(6)));

                    //data chỉ số
                    dataArray = (JSONArray) jv.parse(String.valueOf(petData.get(1)));
                    pet.nPoint.limitPower = Byte.parseByte(String.valueOf(dataArray.get(0)));
                    pet.nPoint.power = Long.parseLong(String.valueOf(dataArray.get(1)));
                    pet.nPoint.tiemNang = Long.parseLong(String.valueOf(dataArray.get(2)));
                    pet.nPoint.stamina = Short.parseShort(String.valueOf(dataArray.get(3)));
                    pet.nPoint.maxStamina = Short.parseShort(String.valueOf(dataArray.get(4)));
                    pet.nPoint.hpg = Integer.parseInt(String.valueOf(dataArray.get(5)));
                    pet.nPoint.mpg = Integer.parseInt(String.valueOf(dataArray.get(6)));
                    pet.nPoint.dameg = Integer.parseInt(String.valueOf(dataArray.get(7)));
                    pet.nPoint.defg = Integer.parseInt(String.valueOf(dataArray.get(8)));
                    pet.nPoint.critg = Integer.parseInt(String.valueOf(dataArray.get(9)));
                    int hp = Integer.parseInt(String.valueOf(dataArray.get(10)));
                    int mp = Integer.parseInt(String.valueOf(dataArray.get(11)));

                    //data body
                    dataArray = (JSONArray) jv.parse(String.valueOf(petData.get(2)));
                    for (int i = 0; i < dataArray.size(); i++) {
                        Item item = null;
                        JSONArray dataItem = (JSONArray) jv.parse(String.valueOf(dataArray.get(i)));
                        short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                        if (tempId != -1) {
                            item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                            JSONArray options = (JSONArray) jv.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                            for (int j = 0; j < options.size(); j++) {
                                JSONArray opt = (JSONArray) jv.parse(String.valueOf(options.get(j)));
                                item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                        Integer.parseInt(String.valueOf(opt.get(1)))));
                            }
                            item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                            if (ItemService.gI().isOutOfDateTime(item)) {
                                item = ItemService.gI().createItemNull();
                            }
                        } else {
                            item = ItemService.gI().createItemNull();
                        }
                        Util.useCheckDo(player, item, "pet");
                        pet.inventory.itemsBody.add(item);
                    }

                }

            }
        } catch (Exception e) {
            System.out.println(name);
            e.printStackTrace();
            Logger.logException(Manager.class, e, "Lỗi load database");
            System.exit(0);
        }
    }

    public static void checkVang(int x) {
        int thoi_vang = 0;
        long st = System.currentTimeMillis();
        JSONValue jv = new JSONValue();
        JSONArray dataArray = null;
        JSONObject dataObject = null;
        Player player;
        PreparedStatement ps = null;
        String name = "";
        ResultSet rs = null;
        try (Connection con = GirlkunDB.getConnection()) {
            ps = con.prepareStatement("select * from player");
            rs = ps.executeQuery();
            while (rs.next()) {
                int plHp = 200000000;
                int plMp = 200000000;
                player = new Player();
                player.id = rs.getInt("id");
                player.name = rs.getString("name");
                name = rs.getString("name");
                player.head = rs.getShort("head");
                player.gender = rs.getByte("gender");
                player.haveTennisSpaceShip = rs.getBoolean("have_tennis_space_ship");
                //data kim lượng
                dataArray = (JSONArray) JSONValue.parse(rs.getString("data_inventory"));
                player.inventory.gold = Integer.parseInt(String.valueOf(dataArray.get(0)));
                player.inventory.gem = Integer.parseInt(String.valueOf(dataArray.get(1)));
                player.inventory.ruby = Integer.parseInt(String.valueOf(dataArray.get(2)));
                if (dataArray.size() == 4) {
                    player.inventory.coupon = Integer.parseInt(String.valueOf(dataArray.get(3)));
                } else {
                    player.inventory.coupon = 0;
                }
                dataArray.clear();

                //data chỉ số
                dataArray = (JSONArray) JSONValue.parse(rs.getString("data_point"));
                player.nPoint.limitPower = Byte.parseByte(String.valueOf(dataArray.get(0)));
                player.nPoint.power = Long.parseLong(String.valueOf(dataArray.get(1)));
                player.nPoint.tiemNang = Long.parseLong(String.valueOf(dataArray.get(2)));
                player.nPoint.stamina = Short.parseShort(String.valueOf(dataArray.get(3)));
                player.nPoint.maxStamina = Short.parseShort(String.valueOf(dataArray.get(4)));
                player.nPoint.hpg = Integer.parseInt(String.valueOf(dataArray.get(5)));
                player.nPoint.mpg = Integer.parseInt(String.valueOf(dataArray.get(6)));
                player.nPoint.dameg = Integer.parseInt(String.valueOf(dataArray.get(7)));
                player.nPoint.defg = Integer.parseInt(String.valueOf(dataArray.get(8)));
                player.nPoint.critg = Byte.parseByte(String.valueOf(dataArray.get(9)));
                dataArray.get(10); //** Năng động
                plHp = Integer.parseInt(String.valueOf(dataArray.get(11)));
                plMp = Integer.parseInt(String.valueOf(dataArray.get(12)));
                dataArray.clear();

                //data body
                dataArray = (JSONArray) JSONValue.parse(rs.getString("items_body"));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    JSONArray dataItem = (JSONArray) JSONValue.parse(dataArray.get(i).toString());
                    short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                        JSONArray options = (JSONArray) JSONValue.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                        for (int j = 0; j < options.size(); j++) {
                            JSONArray opt = (JSONArray) JSONValue.parse(String.valueOf(options.get(j)));
                            item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                    Integer.parseInt(String.valueOf(opt.get(1)))));

                        }
                        item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                        if (ItemService.gI().isOutOfDateTime(item)) {
                            item = ItemService.gI().createItemNull();
                        }
                    } else {
                        item = ItemService.gI().createItemNull();
                    }
                    Util.useCheckDo(player, item, "body");
                    player.inventory.itemsBody.add(item);
                    if (item.template.id == 457) {
                        thoi_vang += item.quantity;
                    }
                }
                dataArray.clear();

                //data bag
                dataArray = (JSONArray) JSONValue.parse(rs.getString("items_bag"));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    JSONArray dataItem = (JSONArray) JSONValue.parse(dataArray.get(i).toString());
                    short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                        JSONArray options = (JSONArray) JSONValue.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                        for (int j = 0; j < options.size(); j++) {
                            JSONArray opt = (JSONArray) JSONValue.parse(String.valueOf(options.get(j)));
                            item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                    Integer.parseInt(String.valueOf(opt.get(1)))));
                        }
                        item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                        if (ItemService.gI().isOutOfDateTime(item)) {
                            item = ItemService.gI().createItemNull();
                        }
                    } else {
                        item = ItemService.gI().createItemNull();
                    }
                    Util.useCheckDo(player, item, "bag");
                    if (item.template.id == 457) {
                        thoi_vang += item.quantity;
                    }
                    player.inventory.itemsBag.add(item);
                }
                dataArray.clear();

                //data box
                dataArray = (JSONArray) JSONValue.parse(rs.getString("items_box"));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    JSONArray dataItem = (JSONArray) JSONValue.parse(dataArray.get(i).toString());
                    short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                        JSONArray options = (JSONArray) JSONValue.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                        for (int j = 0; j < options.size(); j++) {
                            JSONArray opt = (JSONArray) JSONValue.parse(String.valueOf(options.get(j)));
                            item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                    Integer.parseInt(String.valueOf(opt.get(1)))));
                        }
                        item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                        if (ItemService.gI().isOutOfDateTime(item)) {
                            item = ItemService.gI().createItemNull();
                        }
                    } else {
                        item = ItemService.gI().createItemNull();
                    }
                    Util.useCheckDo(player, item, "box");
                    if (item.template.id == 457) {
                        thoi_vang += item.quantity;
                    }
                    player.inventory.itemsBox.add(item);
                }
                dataArray.clear();
                if (thoi_vang > x) {
                    Logger.error("play:" + player.name);
                    Logger.error("thoi_vang:" + thoi_vang);
                }

            }

        } catch (Exception e) {
            System.out.println(name);
            e.printStackTrace();
            Logger.logException(Manager.class, e, "Lỗi load database");
        }
    }

    public static Player loadById(int id) {
        Player player = null;
        GirlkunResultSet rs = null;
        if (Client.gI().getPlayer(id) != null) {
            player = Client.gI().getPlayer(id);
            return player;
        }
        try {
            rs = GirlkunDB.executeQuery("select * from player where id = ? limit 1", id);
        if (rs.first()) {            
            long plHp = 200000000;
            long plMp = 200000000;
            JSONValue jv = new JSONValue();
            JSONArray dataArray = null;
            player = new Player();
            //base info
            player.id = rs.getInt("id");
            player.name = rs.getString("name");
            player.head = rs.getShort("head");
            player.gender = rs.getByte("gender");
            player.haveTennisSpaceShip = rs.getBoolean("have_tennis_space_ship");
            player.totalPlayerViolate = 0;
            int clanId = rs.getInt("clan_id_sv" + Manager.SERVER);
            if (clanId != -1) {
                Clan clan = ClanService.gI().getClanById(clanId);
                for (ClanMember cm : clan.getMembers()) {
                    if (cm.id == player.id) {
                        clan.addMemberOnline(player);
                        player.clan = clan;
                        player.clanMember = cm;
                        break;
                    }
                }
            }

            //data kim lượng
            dataArray = (JSONArray) jv.parse(rs.getString("data_inventory"));
            player.inventory.gold = Long.parseLong(String.valueOf(dataArray.get(0)));
            player.inventory.gem = Integer.parseInt(String.valueOf(dataArray.get(1)));
            player.inventory.ruby = Integer.parseInt(String.valueOf(dataArray.get(2)));
            player.inventory.coupon = Integer.parseInt(String.valueOf(dataArray.get(3)));
            if (dataArray.size() >= 4) {
                player.inventory.coupon = Integer.parseInt(String.valueOf(dataArray.get(3)));
            } else {
                player.inventory.coupon = 0;
            }
            if (dataArray.size() >= 5) {
                player.inventory.event = Integer.parseInt(String.valueOf(dataArray.get(4)));
            } else {
                player.inventory.event = 0;
            }
            dataArray.clear();
             // data rada card
             dataArray = (JSONArray) jv.parse(rs.getString("data_card"));
             for(int i = 0 ; i < dataArray.size();i++){
                 JSONObject obj = (JSONObject)dataArray.get(i);
                 player.Cards.add(new Card(Short.parseShort(obj.get("id").toString()) ,Byte.parseByte(obj.get("amount").toString()), Byte.parseByte(obj.get("max").toString()) , Byte.parseByte(obj.get("level").toString()) , loadOptionCard((JSONArray)JSONValue.parse(obj.get("option").toString())), Byte.parseByte(obj.get("used").toString())));
             }
             dataArray.clear();
            //data tọa độ
            try {
                dataArray = (JSONArray) jv.parse(rs.getString("data_location"));
                int mapId = Integer.parseInt(String.valueOf(dataArray.get(0)));
                player.location.x = Integer.parseInt(String.valueOf(dataArray.get(1)));
                player.location.y = Integer.parseInt(String.valueOf(dataArray.get(2)));
                player.location.lastTimeplayerMove = System.currentTimeMillis();
                if (MapService.gI().isMapDoanhTrai(mapId) || MapService.gI().isMapBlackBallWar(mapId)
                        || MapService.gI().isMapBanDoKhoBau(mapId)  || MapService.gI().isMapMaBu(mapId)
                        || MapService.gI().isMapConDuongRanDoc(mapId)|| MapService.gI().isMapKhiGaHuyDiet(mapId)) {
                    mapId = player.gender + 21;
                    player.location.x = 300;
                    player.location.y = 336;
                }
                player.zone = MapService.gI().getMapCanJoin(player, mapId, -1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            dataArray.clear();

            //data chỉ số
            dataArray = (JSONArray) jv.parse(rs.getString("data_point"));
            player.nPoint.limitPower = Byte.parseByte(String.valueOf(dataArray.get(0)));
            player.nPoint.power = Long.parseLong(String.valueOf(dataArray.get(1)));
            player.nPoint.tiemNang = Long.parseLong(String.valueOf(dataArray.get(2)));
            player.nPoint.stamina = Short.parseShort(String.valueOf(dataArray.get(3)));
            player.nPoint.maxStamina = Short.parseShort(String.valueOf(dataArray.get(4)));
            player.nPoint.hpg = Long.parseLong(String.valueOf(dataArray.get(5)));
            player.nPoint.mpg = Long.parseLong(String.valueOf(dataArray.get(6)));
            player.nPoint.dameg = Long.parseLong(String.valueOf(dataArray.get(7)));
            player.nPoint.defg = Integer.parseInt(String.valueOf(dataArray.get(8)));
            player.nPoint.critg = Byte.parseByte(String.valueOf(dataArray.get(9)));
            dataArray.get(10); //** Năng động
            plHp = Long.parseLong(String.valueOf(dataArray.get(11)));
            plMp = Long.parseLong(String.valueOf(dataArray.get(12)));
            dataArray.clear();
              player.rankSieuHang = rs.getInt("rank_sieu_hang");
                            if(player.rankSieuHang == -99){
                                player.rankSieuHang =  ServerManager.gI().getNumPlayer();
                            }
            //data đậu thần
            dataArray = (JSONArray) jv.parse(rs.getString("data_magic_tree"));
            byte level = Byte.parseByte(String.valueOf(dataArray.get(0)));
            byte currPea = Byte.parseByte(String.valueOf(dataArray.get(1)));
            boolean isUpgrade = Byte.parseByte(String.valueOf(dataArray.get(2))) == 1;
            long lastTimeHarvest = Long.parseLong(String.valueOf(dataArray.get(3)));
            long lastTimeUpgrade = Long.parseLong(String.valueOf(dataArray.get(4)));
            player.magicTree = new MagicTree(player, level, currPea, lastTimeHarvest, isUpgrade, lastTimeUpgrade);
            dataArray.clear();

            //data phần thưởng sao đen
            dataArray = (JSONArray) jv.parse(rs.getString("data_black_ball"));
            JSONArray dataBlackBall = null;
            for (int i = 0; i < dataArray.size(); i++) {
                dataBlackBall = (JSONArray) jv.parse(String.valueOf(dataArray.get(i)));
                player.rewardBlackBall.timeOutOfDateReward[i] = Long.parseLong(String.valueOf(dataBlackBall.get(0)));
                player.rewardBlackBall.lastTimeGetReward[i] = Long.parseLong(String.valueOf(dataBlackBall.get(1)));
                try {
                    player.rewardBlackBall.quantilyBlackBall[i] = dataBlackBall.get(2) != null ? Integer.parseInt(String.valueOf(dataBlackBall.get(2))) : 0;
                } catch (Exception e) {
                    player.rewardBlackBall.quantilyBlackBall[i] = player.rewardBlackBall.timeOutOfDateReward[i] != 0 ? 1 : 0;
                }
                dataBlackBall.clear();
            }
            dataArray.clear();

            //data body
            dataArray = (JSONArray) jv.parse(rs.getString("items_body"));
            for (int i = 0; i < dataArray.size(); i++) {
                Item item = null;
                JSONArray dataItem = (JSONArray) jv.parse(dataArray.get(i).toString());
                short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                if (tempId != -1) {
                    item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                    JSONArray options = (JSONArray) jv.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                    for (int j = 0; j < options.size(); j++) {
                        JSONArray opt = (JSONArray) jv.parse(String.valueOf(options.get(j)));
                        item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                Integer.parseInt(String.valueOf(opt.get(1)))));
                    }
                    item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                    if (ItemService.gI().isOutOfDateTime(item)) {
                        item = ItemService.gI().createItemNull();
                    }
                } else {
                    item = ItemService.gI().createItemNull();
                }
                player.inventory.itemsBody.add(item);
            }
            if (player.inventory.itemsBody.size() == 10) {
                player.inventory.itemsBody.add(ItemService.gI().createItemNull());
            }
            dataArray.clear();

            //data bag
            dataArray = (JSONArray) jv.parse(rs.getString("items_bag"));
            for (int i = 0; i < dataArray.size(); i++) {
                Item item = null;
                JSONArray dataItem = (JSONArray) jv.parse(dataArray.get(i).toString());
                short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                if (tempId != -1) {
                    item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                    JSONArray options = (JSONArray) jv.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                    for (int j = 0; j < options.size(); j++) {
                        JSONArray opt = (JSONArray) jv.parse(String.valueOf(options.get(j)));
                        item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                Integer.parseInt(String.valueOf(opt.get(1)))));
                    }
                    item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                    if (ItemService.gI().isOutOfDateTime(item)) {
                        item = ItemService.gI().createItemNull();
                    }
                } else {
                    item = ItemService.gI().createItemNull();
                }
                player.inventory.itemsBag.add(item);
            }
            dataArray.clear();

            //data box
            dataArray = (JSONArray) jv.parse(rs.getString("items_box"));
            for (int i = 0; i < dataArray.size(); i++) {
                Item item = null;
                JSONArray dataItem = (JSONArray) jv.parse(dataArray.get(i).toString());
                short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                if (tempId != -1) {
                    item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                    JSONArray options = (JSONArray) jv.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                    for (int j = 0; j < options.size(); j++) {
                        JSONArray opt = (JSONArray) jv.parse(String.valueOf(options.get(j)));
                        item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                Integer.parseInt(String.valueOf(opt.get(1)))));
                    }
                    item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                    if (ItemService.gI().isOutOfDateTime(item)) {
                        item = ItemService.gI().createItemNull();
                    }
                } else {
                    item = ItemService.gI().createItemNull();
                }
                player.inventory.itemsBox.add(item);
            }
            dataArray.clear();

            //data box lucky round
            dataArray = (JSONArray) jv.parse(rs.getString("items_box_lucky_round"));
            for (int i = 0; i < dataArray.size(); i++) {
                Item item = null;
                JSONArray dataItem = (JSONArray) jv.parse(dataArray.get(i).toString());
                short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                if (tempId != -1) {
                    item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                    JSONArray options = (JSONArray) jv.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                    for (int j = 0; j < options.size(); j++) {
                        JSONArray opt = (JSONArray) jv.parse(String.valueOf(options.get(j)));
                        item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                Integer.parseInt(String.valueOf(opt.get(1)))));
                    }
                    player.inventory.itemsBoxCrackBall.add(item);
                }
            }
            dataArray.clear();

            //data friends
            dataArray = (JSONArray) jv.parse(rs.getString("friends"));
            if (dataArray != null) {
                for (int i = 0; i < dataArray.size(); i++) {
                    JSONArray dataFE = (JSONArray) jv.parse(String.valueOf(dataArray.get(i)));
                    Friend friend = new Friend();
                    friend.id = Integer.parseInt(String.valueOf(dataFE.get(0)));
                    friend.name = String.valueOf(dataFE.get(1));
                    friend.head = Short.parseShort(String.valueOf(dataFE.get(2)));
                    friend.body = Short.parseShort(String.valueOf(dataFE.get(3)));
                    friend.leg = Short.parseShort(String.valueOf(dataFE.get(4)));
                    friend.bag = Byte.parseByte(String.valueOf(dataFE.get(5)));
                    friend.power = Long.parseLong(String.valueOf(dataFE.get(6)));
                    player.friends.add(friend);
                    dataFE.clear();
                }
                dataArray.clear();
            }

            //data enemies
            dataArray = (JSONArray) jv.parse(rs.getString("enemies"));
            if (dataArray != null) {
                for (int i = 0; i < dataArray.size(); i++) {
                    JSONArray dataFE = (JSONArray) jv.parse(String.valueOf(dataArray.get(i)));
                    Enemy enemy = new Enemy();
                    enemy.id = Integer.parseInt(String.valueOf(dataFE.get(0)));
                    enemy.name = String.valueOf(dataFE.get(1));
                    enemy.head = Short.parseShort(String.valueOf(dataFE.get(2)));
                    enemy.body = Short.parseShort(String.valueOf(dataFE.get(3)));
                    enemy.leg = Short.parseShort(String.valueOf(dataFE.get(4)));
                    enemy.bag = Byte.parseByte(String.valueOf(dataFE.get(5)));
                    enemy.power = Long.parseLong(String.valueOf(dataFE.get(6)));
                    player.enemies.add(enemy);
                    dataFE.clear();
                }
                dataArray.clear();
            }

            //data nội tại
            dataArray = (JSONArray) jv.parse(rs.getString("data_intrinsic"));
            byte intrinsicId = Byte.parseByte(String.valueOf(dataArray.get(0)));
            player.playerIntrinsic.intrinsic = IntrinsicService.gI().getIntrinsicById(intrinsicId);
            player.playerIntrinsic.intrinsic.param1 = Short.parseShort(String.valueOf(dataArray.get(1)));
            player.playerIntrinsic.intrinsic.param2 = Short.parseShort(String.valueOf(dataArray.get(2)));
            player.playerIntrinsic.countOpen = Byte.parseByte(String.valueOf(dataArray.get(3)));
            dataArray.clear();

            //data item time
            dataArray = (JSONArray) jv.parse(rs.getString("data_item_time"));
            int timeBoHuyet = Integer.parseInt(String.valueOf(dataArray.get(0)));
            int timeBoKhi = Integer.parseInt(String.valueOf(dataArray.get(1)));
            int timeGiapXen = Integer.parseInt(String.valueOf(dataArray.get(2)));
            int timeCuongNo = Integer.parseInt(String.valueOf(dataArray.get(3)));
            int timeAnDanh = Integer.parseInt(String.valueOf(dataArray.get(4)));
            int timeOpenPower = Integer.parseInt(String.valueOf(dataArray.get(5)));
            int timeMayDo = Integer.parseInt(String.valueOf(dataArray.get(6)));                             
            int timeDuoi = Integer.parseInt(String.valueOf(dataArray.get(7)));
            int iconDuoi = Integer.parseInt(String.valueOf(dataArray.get(8)));
            //int timeMayDo2 = Integer.parseInt(String.valueOf(dataArray.get(10)));
            int timeUseTDLT = 0;
            if (dataArray.size() == 10) {
                timeUseTDLT = Integer.parseInt(String.valueOf(dataArray.get(9)));
            }


            dataArray.clear();

            //data nhiệm vụ
            dataArray = (JSONArray) jv.parse(rs.getString("data_task"));
            TaskMain taskMain = TaskService.gI().getTaskMainById(player, Byte.parseByte(String.valueOf(dataArray.get(0))));
            taskMain.index = Byte.parseByte(String.valueOf(dataArray.get(1)));
            taskMain.subTasks.get(taskMain.index).count = Short.parseShort(String.valueOf(dataArray.get(2)));
            player.playerTask.taskMain = taskMain;
            dataArray.clear();

            //data nhiệm vụ hàng ngày
            dataArray = (JSONArray) jv.parse(rs.getString("data_side_task"));
            String format = "dd-MM-yyyy";
            long receivedTime = Long.parseLong(String.valueOf(dataArray.get(1)));
            Date date = new Date(receivedTime);
            if (TimeUtil.formatTime(date, format).equals(TimeUtil.formatTime(new Date(), format))) {
                player.playerTask.sideTask.template = TaskService.gI().getSideTaskTemplateById(Integer.parseInt(String.valueOf(dataArray.get(0))));
                player.playerTask.sideTask.count = Integer.parseInt(String.valueOf(dataArray.get(2)));
                player.playerTask.sideTask.maxCount = Integer.parseInt(String.valueOf(dataArray.get(3)));
                player.playerTask.sideTask.leftTask = Integer.parseInt(String.valueOf(dataArray.get(4)));
                player.playerTask.sideTask.level = Integer.parseInt(String.valueOf(dataArray.get(5)));
                player.playerTask.sideTask.receivedTime = receivedTime;
            }

            //data trứng bư
            dataArray = (JSONArray) jv.parse(rs.getString("data_mabu_egg"));
            if (dataArray.size() != 0) {
                player.mabuEgg = new MabuEgg(player, Long.parseLong(String.valueOf(dataArray.get(0))),
                        Long.parseLong(String.valueOf(dataArray.get(1))));
            }
            dataArray.clear();

            //data bùa
            dataArray = (JSONArray) jv.parse(rs.getString("data_charm"));
            player.charms.tdTriTue = Long.parseLong(String.valueOf(dataArray.get(0)));
            player.charms.tdManhMe = Long.parseLong(String.valueOf(dataArray.get(1)));
            player.charms.tdDaTrau = Long.parseLong(String.valueOf(dataArray.get(2)));
            player.charms.tdOaiHung = Long.parseLong(String.valueOf(dataArray.get(3)));
            player.charms.tdBatTu = Long.parseLong(String.valueOf(dataArray.get(4)));
            player.charms.tdDeoDai = Long.parseLong(String.valueOf(dataArray.get(5)));
            player.charms.tdThuHut = Long.parseLong(String.valueOf(dataArray.get(6)));
            player.charms.tdDeTu = Long.parseLong(String.valueOf(dataArray.get(7)));
            player.charms.tdTriTue3 = Long.parseLong(String.valueOf(dataArray.get(8)));
            player.charms.tdTriTue4 = Long.parseLong(String.valueOf(dataArray.get(9)));
            dataArray.clear();

            //data skill
            dataArray = (JSONArray) jv.parse(rs.getString("skills"));
            for (int i = 0; i < dataArray.size(); i++) {
                JSONArray dataSkill = (JSONArray) jv.parse(String.valueOf(dataArray.get(i)));
                int tempId = Integer.parseInt(String.valueOf(dataSkill.get(0)));
                byte point = Byte.parseByte(String.valueOf(dataSkill.get(1)));
                Skill skill = null;
                if (point != 0) {
                    skill = SkillUtil.createSkill(tempId, point);
                } else {
                    skill = SkillUtil.createSkillLevel0(tempId);
                }
                skill.lastTimeUseThisSkill = Long.parseLong(String.valueOf(dataSkill.get(2)));
                if (dataSkill.size() > 3) {
                    skill.currLevel= Short.parseShort(String.valueOf(dataSkill.get(3)));
                }
                player.playerSkill.skills.add(skill);
            }
            dataArray.clear();

            //data skill shortcut
            dataArray = (JSONArray) jv.parse(rs.getString("skills_shortcut"));
            for (int i = 0; i < dataArray.size(); i++) {
                player.playerSkill.skillShortCut[i] = Byte.parseByte(String.valueOf(dataArray.get(i)));
            }
            for (int i : player.playerSkill.skillShortCut) {
                if (player.playerSkill.getSkillbyId(i) != null && player.playerSkill.getSkillbyId(i).damage > 0) {
                    player.playerSkill.skillSelect = player.playerSkill.getSkillbyId(i);
                    break;
                }
            }
            if (player.playerSkill.skillSelect == null) {
                player.playerSkill.skillSelect = player.playerSkill.getSkillbyId(player.gender == ConstPlayer.TRAI_DAT
                        ? Skill.DRAGON : (player.gender == ConstPlayer.NAMEC ? Skill.DEMON : Skill.GALICK));
            }
            dataArray.clear();

            //data pet
            JSONArray petData = (JSONArray) jv.parse(rs.getString("pet"));
            if (!petData.isEmpty()) {
                dataArray = (JSONArray) jv.parse(String.valueOf(petData.get(0)));
                Pet pet = new Pet(player);
                pet.id = -player.id;
                pet.typePet = Byte.parseByte(String.valueOf(dataArray.get(0)));
                pet.gender = Byte.parseByte(String.valueOf(dataArray.get(1)));
                pet.name = String.valueOf(dataArray.get(2));
                player.fusion.typeFusion = Byte.parseByte(String.valueOf(dataArray.get(3)));
                player.fusion.lastTimeFusion = System.currentTimeMillis()
                        - (Fusion.TIME_FUSION - Integer.parseInt(String.valueOf(dataArray.get(4))));
                pet.status = Byte.parseByte(String.valueOf(dataArray.get(5)));
                pet.thuctinh = Byte.parseByte(String.valueOf(dataArray.get(6)));

                //data chỉ số
                dataArray = (JSONArray) jv.parse(String.valueOf(petData.get(1)));
                pet.nPoint.limitPower = Byte.parseByte(String.valueOf(dataArray.get(0)));
                pet.nPoint.power = Long.parseLong(String.valueOf(dataArray.get(1)));
                pet.nPoint.tiemNang = Long.parseLong(String.valueOf(dataArray.get(2)));
                pet.nPoint.stamina = Short.parseShort(String.valueOf(dataArray.get(3)));
                pet.nPoint.maxStamina = Short.parseShort(String.valueOf(dataArray.get(4)));
                pet.nPoint.hpg = Integer.parseInt(String.valueOf(dataArray.get(5)));
                pet.nPoint.mpg = Integer.parseInt(String.valueOf(dataArray.get(6)));
                pet.nPoint.dameg = Integer.parseInt(String.valueOf(dataArray.get(7)));
                pet.nPoint.defg = Integer.parseInt(String.valueOf(dataArray.get(8)));
                pet.nPoint.critg = Integer.parseInt(String.valueOf(dataArray.get(9)));
                long hp = Long.parseLong(String.valueOf(dataArray.get(10)));
                long mp = Long.parseLong(String.valueOf(dataArray.get(11)));

                //data body
                dataArray = (JSONArray) jv.parse(String.valueOf(petData.get(2)));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    JSONArray dataItem = (JSONArray) jv.parse(String.valueOf(dataArray.get(i)));
                    short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                        JSONArray options = (JSONArray) jv.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                        for (int j = 0; j < options.size(); j++) {
                            JSONArray opt = (JSONArray) jv.parse(String.valueOf(options.get(j)));
                            item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                    Integer.parseInt(String.valueOf(opt.get(1)))));
                        }
                        item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                        if (ItemService.gI().isOutOfDateTime(item)) {
                            item = ItemService.gI().createItemNull();
                        }
                    } else {
                        item = ItemService.gI().createItemNull();
                        ;
                    }
                    pet.inventory.itemsBody.add(item);
                }

                //data skills
                dataArray = (JSONArray) jv.parse(String.valueOf(petData.get(3)));
                for (int i = 0; i < dataArray.size(); i++) {
                    JSONArray skillTemp = (JSONArray) jv.parse(String.valueOf(dataArray.get(i)));
                    int tempId = Integer.parseInt(String.valueOf(skillTemp.get(0)));
                    byte point = Byte.parseByte(String.valueOf(skillTemp.get(1)));
                    Skill skill = null;
                    if (point != 0) {
                        skill = SkillUtil.createSkill(tempId, point);
                    } else {
                        skill = SkillUtil.createSkillLevel0(tempId);
                    }
                    switch (skill.template.id) {
                        case Skill.KAMEJOKO:
                        case Skill.MASENKO:
                        case Skill.ANTOMIC:
                            skill.coolDown = 1000;
                            break;
                    }
                    pet.playerSkill.skills.add(skill);
                }
                if(pet.playerSkill.skills.size() < 5){
                    pet.playerSkill.skills.add(4,SkillUtil.createSkillLevel0(-1));
                }
                pet.nPoint.hp = hp;
                pet.nPoint.mp = mp;
                player.pet = pet;
            }
            dataArray.clear();
            // nhiem vu bo mong 
            // JSONObject achievementObject = (JSONObject) JSONValue.parse(rs.getString("info_achievement"));
            // player.achievement.numPvpWin = Integer.parseInt(String.valueOf(achievementObject.get("numPvpWin")));
            // player.achievement.numSkillChuong = Integer.parseInt(String.valueOf(achievementObject.get("numSkillChuong")));
            // player.achievement.numFly = Integer.parseInt(String.valueOf(achievementObject.get("numFly")));
            // player.achievement.numKillMobFly = Integer.parseInt(String.valueOf(achievementObject.get("numKillMobFly")));
            // player.achievement.numKillNguoiRom = Integer.parseInt(String.valueOf(achievementObject.get("numKillNguoiRom")));
            // player.achievement.numHourOnline = Long.parseLong(String.valueOf(achievementObject.get("numHourOnline")));
            // player.achievement.numGivePea = Integer.parseInt(String.valueOf(achievementObject.get("numGivePea")));
            // player.achievement.numSellItem = Integer.parseInt(String.valueOf(achievementObject.get("numSellItem")));
            // player.achievement.numPayMoney = Integer.parseInt(String.valueOf(achievementObject.get("numPayMoney")));
            // player.achievement.numKillSieuQuai = Integer.parseInt(String.valueOf(achievementObject.get("numKillSieuQuai")));
            // player.achievement.numHoiSinh = Integer.parseInt(String.valueOf(achievementObject.get("numHoiSinh")));
            // player.achievement.numSkillDacBiet = Integer.parseInt(String.valueOf(achievementObject.get("numSkillDacBiet")));
            // player.achievement.numPickGem = Integer.parseInt(String.valueOf(achievementObject.get("numPickGem")));
            // dataArray = (JSONArray) JSONValue.parse(String.valueOf(achievementObject.get("listReceiveGem")));
            // for (Byte i = 0; i < dataArray.size(); i++) {
            //     player.achievement.listReceiveGem.add(Boolean.valueOf(String.valueOf(dataArray.get(i))));
            // }
            // dataArray.clear();
            // player.bdkb_isJoinBdkb = false;
            // if ((new java.sql.Date(player.bdkb_lastTimeJoin)).getDay() != (new java.sql.Date(System.currentTimeMillis())).getDay()) {
            //     player.bdkb_countPerDay = 0;
            // }
            player.nPoint.hp = plHp;
            player.nPoint.mp = plMp;
            player.iDMark.setLoadedAllDataPlayer(true);
        }
        } catch (Exception e) {
            System.out.println("yyy");
            player.dispose();
            player = null;
            Logger.logException(GodGK.class, e);
        } finally {
            if (rs != null) {
                rs.dispose();
            }
        }
        return player;
    }
}
