package ServerData.Server;

import ServerData.Models.Player.OptionCard;
import ServerData.Models.Player.RadarCard;
import com.girlkun.database.GirlkunDB;
import Server.Data.Consts.ConstPlayer;
import Server.Data.Consts.ConstEvent;
import Server.Data.Consts.ConstMap;
import Server.Data.DataGame;
import Server.Connect.ShopData;
import ServerData.Models.Item.Template.*;
import ServerData.Models.Clan.Clan;
import ServerData.Models.Clan.ClanMember;
import ServerData.Models.Intrinsic.Intrinsic;
import ServerData.Models.Item.Item;
import ServerData.Models.Item.ItemMobReward;
import ServerData.Models.Item.ItemOptionMobReward;
import ServerData.Models.Item.Template.ArchivementTemplate;
import ServerData.Models.Map.WayPoint;
import ServerData.Models.PVP.TOP;
import ServerData.Models.PVP.Matches.DaiHoiVoThuat;
import ServerData.Models.NPC.Npc;
import ServerData.Models.NPC.NpcFactory;
import ServerData.Models.Player.Referee;
import ServerData.Models.Player.Referee1;
import ServerData.Models.Map.Mob.MobReward;
import ServerData.Models.Map.Zone;
import ServerData.Models.Player.Player;
import ServerData.Models.Shop.Shop;
import ServerData.Models.Player.PlayerSkill.NClass;
import ServerData.Models.Player.PlayerSkill.Skill;
import ServerData.Models.Player.PlayerTask.SideTaskTemplate;
import ServerData.Models.Player.PlayerTask.SubTaskMain;
import ServerData.Models.Player.PlayerTask.TaskMain;
import ServerData.Services.ItemService;
import ServerData.Services.MapService;
import ServerData.Utils.Logger;
import ServerData.Utils.TimeUtil;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import ServerData.Utils.Util;
import ServerData.Models.Shop.ItemKyGui;
import ServerData.Models.Shop.ShopKyGuiManager;
import com.girlkun.result.GirlkunResultSet;
import java.util.Collections;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Manager {

    private static Manager i;

    public static byte SERVER = 1;
    public static byte SECOND_WAIT_LOGIN = 5;
    public static int MAX_PER_IP = 3;
    public static int MAX_PLAYER = 1000;
    public static boolean LOCAL = false;

    // Update //
    public static short RATE_EXP_SERVER = 100; // max rate exp
    public static byte TNPET = 5; // tn đệ
    public static float TLDOIHN = 3; // tile đổi hn
    public static int TLDOITV = 6; // tl đổi tv
    public static int TLDOIXUBAC = 6;
    public static int TLDOIXUVANG = 3;
    public static int TLDOITHANGTINHTHACH = 3;
    // *** //
    public static MapTemplate[] MAP_TEMPLATES;
    public static final List<AchievementTemplate> ACHIEVEMENTS = new ArrayList<>();
    public static final List<ArchivementTemplate> A_TEMPLATES = new ArrayList<>();
    public static final List<ServerData.Models.Map.Map> MAPS = new ArrayList<>();
    public static final List<ItemOptionTemplate> ITEM_OPTION_TEMPLATES = new ArrayList<>();
    public static final Map<Integer, MobReward> MOB_REWARDS = new HashMap<>();
    public static final List<ItemLuckyRound> LUCKY_ROUND_REWARDS = new ArrayList();
    public static final Map<String, Byte> IMAGES_BY_NAME = new HashMap<>();
    public static final List<ItemTemplate> ITEM_TEMPLATES = new ArrayList<>();
    public static final List<Item> CAITRANG = new ArrayList<>();
    public static final List<MobTemplate> MOB_TEMPLATES = new ArrayList<>();
    public static final List<NpcTemplate> NPC_TEMPLATES = new ArrayList<>();
    public static final List<String> CAPTIONS = new ArrayList<>();
    public static final List<TaskMain> TASKS = new ArrayList<>();
    public static final List<Item> DEOLUNG = new ArrayList<>();
    public static final List<SideTaskTemplate> SIDE_TASKS_TEMPLATE = new ArrayList<>();
    public static final List<Intrinsic> INTRINSICS = new ArrayList<>();
    public static final List<Intrinsic> INTRINSIC_TD = new ArrayList<>();
    public static final List<Intrinsic> INTRINSIC_NM = new ArrayList<>();
    public static final List<Intrinsic> INTRINSIC_XD = new ArrayList<>();
    public static final List<HeadAvatar> HEAD_AVATARS = new ArrayList<>();
    public static final List<FlagBag> FLAGS_BAGS = new ArrayList<>();
    public static final List<NClass> NCLASS = new ArrayList<>();
    public static final List<Npc> NPCS = new ArrayList<>();
    public static List<Shop> SHOPS = new ArrayList<>();
    public static final List<Clan> CLANS = new ArrayList<>();
    public static final List<String> NOTIFY = new ArrayList<>();
    public static final ArrayList<DaiHoiVoThuat> LIST_DHVT = new ArrayList<>();
    public static final List<Item> RUBY_REWARDS = new ArrayList<>();
    public static final List<Item> THUCAN_REWARDS = new ArrayList<>();
    public static final List<Item> MANH_THIENSU = new ArrayList<>();
    public static final String queryTopSM = "SELECT player.id as id, name, CAST(JSON_UNQUOTE(JSON_EXTRACT(data_point, '$[1]')) AS UNSIGNED) AS sm FROM player inner join account on account.id = player.account_id where account.is_admin = 0 AND account.is_admin2 = 0 AND account.ban = 0 ORDER BY sm DESC LIMIT 10;";
    public static final String queryTopSD = "SELECT player.id as id, name, gender, CAST(JSON_UNQUOTE(JSON_EXTRACT(data_point, '$[13]')) AS UNSIGNED) AS sd, CAST(JSON_UNQUOTE(JSON_EXTRACT(data_point, '$[7]')) AS UNSIGNED) AS sd2 FROM player inner join account on account.id = player.account_id where account.is_admin = 0 AND account.is_admin2 = 0 AND account.ban = 0 ORDER BY sd DESC LIMIT 10;";
    public static final String queryTopHP = "SELECT player.id as id, name, gender, CAST(JSON_UNQUOTE(JSON_EXTRACT(data_point, '$[11]')) AS UNSIGNED) AS hp, CAST(JSON_UNQUOTE(JSON_EXTRACT(data_point, '$[5]')) AS UNSIGNED) AS hp2 FROM player inner join account on account.id = player.account_id where account.is_admin = 0 AND account.is_admin2 = 0 AND account.ban = 0 ORDER BY hp DESC LIMIT 10;";
    public static final String queryTopKI = "SELECT player.id as id, name, gender, CAST(JSON_UNQUOTE(JSON_EXTRACT(data_point, '$[12]')) AS UNSIGNED) AS ki, CAST(JSON_UNQUOTE(JSON_EXTRACT(data_point, '$[6]')) AS UNSIGNED) AS ki2 FROM player inner join account on account.id = player.account_id where account.is_admin = 0 AND account.is_admin2 = 0 AND account.ban = 0 ORDER BY ki DESC LIMIT 10;";
 
    public static final String queryTopSK = "SELECT player.id as id, name, CAST(JSON_UNQUOTE(JSON_EXTRACT(data_inventory, '$[4]')) AS UNSIGNED) AS event FROM player inner join account on account.id = player.account_id where account.is_admin = 0 AND account.is_admin2 = 0 AND account.ban = 0 ORDER BY event DESC LIMIT 10;";
    public static final String queryTopGT = "SELECT player.id as id, name, CAST(JSON_UNQUOTE(JSON_EXTRACT(data_diem, '$[1]')) AS UNSIGNED) AS gt FROM player inner join account on account.id = player.account_id where account.is_admin = 0 AND account.is_admin2 = 0 AND account.ban = 0 ORDER BY gt DESC LIMIT 10;";
    public static final String queryTopPVP = "SELECT player.id as id, name, CAST(JSON_UNQUOTE(JSON_EXTRACT(data_diem, '$[5]')) AS UNSIGNED) AS pointPVP FROM player inner join account on account.id = player.account_id where account.is_admin = 0 AND account.is_admin2 = 0 AND account.ban = 0 ORDER BY pointPVP DESC LIMIT 10;";
    public static final String queryTopNHS = "SELECT player.id as id, name, CAST(JSON_UNQUOTE(JSON_EXTRACT(data_diem, '$[0]')) AS UNSIGNED) AS nhs FROM player inner join account on account.id = player.account_id where account.is_admin = 0 AND account.is_admin2 = 0 AND account.ban = 0 ORDER BY nhs DESC LIMIT 10;";
    public static final String queryTopHN = "SELECT player.id as id, name, CAST(JSON_UNQUOTE(JSON_EXTRACT(data_inventory, '$[2]')) AS UNSIGNED) AS hn FROM player inner join account on account.id = player.account_id where account.is_admin = 0 AND account.is_admin2 = 0 AND account.ban = 0 ORDER BY hn DESC LIMIT 10;";
    public static final String queryTopVND = "SELECT player.id AS id, player.account_id, account.id AS account_id, account.tongnap AS tongnap FROM player INNER JOIN account ON account.id = player.account_id where account.is_admin = 0 AND account.is_admin2 = 0 AND account.ban = 0 ORDER BY tongnap DESC LIMIT 10;";
     public static final String queryTopNV = "SELECT \r\n" + //
            "    id,\r\n" + //
            "    CAST(\r\n" + //
            "        SUBSTRING_INDEX(SUBSTRING_INDEX(data_task, ',', 2), '[', -1) AS UNSIGNED\r\n" + //
            "    ) AS nv\r\n" + //
            "FROM \r\n" + //
            "    player\r\n" + //
            "ORDER BY \r\n" + //
            "    CAST(SUBSTRING_INDEX(SUBSTRING_INDEX(data_task, ',', 2), '[', -1) AS UNSIGNED) DESC,\r\n" + //
            "    CAST(SUBSTRING_INDEX(data_task, ',', -1) AS UNSIGNED) DESC,\r\n" + //
            "    CAST(SUBSTRING_INDEX(data_point, ',', -1) AS UNSIGNED) DESC\r\n" + //
            "LIMIT 50;";
    public static List<TOP> topSM;
    public static List<TOP> topSD;
    public static List<TOP> topHP;
    public static List<TOP> topKI;
    public static List<TOP> topNV;
    public static List<TOP> topSK;
    public static List<TOP> topGT;
    public static List<TOP> topPVP = new ArrayList<>();
    public static List<TOP> topNHS;
    public static List<TOP> topHN;
    public static List<TOP> topVND;
      public static List<TOP> topSieuHang;

    public static long timeRealTop = 0;
    public static final short[] itemIds_TL = {555, 557, 559, 556, 558, 560, 562, 564, 566, 563, 565, 567, 561};
    public static final byte[] itemIds_NR_SB = {16, 17, 18};
    public static final short[] itemDC12 = {233, 237, 241, 245, 249, 253, 257, 261, 265, 269, 273, 277};
    // SKH VIP
    public static final short[] aotd = {138, 139, 230, 231, 232, 233, 555};
    public static final short[] quantd = {142, 143, 242, 243, 244, 245, 556};
    public static final short[] gangtd = {146, 147, 254, 255, 256, 257, 562};
    public static final short[] giaytd = {150, 151, 266, 267, 268, 269, 563};
    public static final short[] aoxd = {170, 171, 238, 239, 240, 241, 559};
    public static final short[] quanxd = {174, 175, 250, 251, 252, 253, 560};
    public static final short[] gangxd = {178, 179, 262, 263, 264, 265, 566};
    public static final short[] giayxd = {182, 183, 274, 275, 276, 277, 567};
    public static final short[] aonm = {154, 155, 234, 235, 236, 237, 557};
    public static final short[] quannm = {158, 159, 246, 247, 248, 249, 558};
    public static final short[] gangnm = {162, 163, 258, 259, 260, 261, 564};
    public static final short[] giaynm = {166, 167, 270, 271, 272, 273, 565};
    public static final short[] radaSKHVip = {186, 187, 278, 279, 280, 281, 561};
    // SKH THƯỜNG
    public static final short[] aotdt = {0};
    public static final short[] quantdt = {6};
    public static final short[] gangtdt = {21};
    public static final short[] giaytdt = {27};
    public static final short[] aoxdt = {2};
    public static final short[] quanxdt = {8};
    public static final short[] gangxdt = {23};
    public static final short[] giayxdt = {29};
    public static final short[] aonmt = {1};
    public static final short[] quannmt = {7};
    public static final short[] gangnmt = {22};
    public static final short[] giaynmt = {28};
    public static final short[] radaSKH = {12};
    // đồ hd
    public static final short[] aotdhd = {1056};
    public static final short[] quantdhd = {1057};
    public static final short[] gangtdhd = {1163};
    public static final short[] giaytdhd = {1164};
    public static final short[] aoxdhd = {1160};
    public static final short[] quanxdhd = {1161};
    public static final short[] gangxdhd = {1168};
    public static final short[] giayxdhd = {1167};
    public static final short[] aonmhd = {1158};
    public static final short[] quannmhd = {1159};
    public static final short[] gangnmhd = {1165};
    public static final short[] giaynmhd = {1166};
    public static final short[] radaHD = {1162};
    public static final short[][][] doHD = {{aotdhd, quantdhd, gangtdhd, giaytdhd}, {aonmhd, quannmhd, gangnmhd, giaynmhd}, {aoxdhd, quanxdhd, gangxdhd, giayxdhd}};
    //ist mts
    public static final short[] manhts = {1067, 1068, 1069, 1070, 1066};
    //isst t.a
    public static final short[] thucan = {663, 664, 665, 666, 667};
    public static final short[][][] doSKHVip = {{aotd, quantd, gangtd, giaytd, radaSKHVip}, {aonm, quannm, gangnm, giaynm, radaSKHVip}, {aoxd, quanxd, gangxd, giayxd, radaSKHVip}};
    //doSKHVip[gender][typeDo][randomLVDo]
    public static final short[][][] doSKH = {{aotdt, quantdt, gangtdt, giaytdt, radaSKH}, {aonmt, quannmt, gangnmt, giaynmt, radaSKH}, {aoxdt, quanxdt, gangxdt, giayxdt, radaSKH}};

    public static Manager gI() {
        if (i == null) {
            i = new Manager();
        }
        return i;
    }

    private Manager() {
        try {
            loadProperties();
        } catch (IOException ex) {
            Logger.logException(Manager.class, ex, "Lỗi load properites");
            System.exit(0);
        }
        this.loadDatabase();
        NpcFactory.createNpcConMeo();
        NpcFactory.createNpcRongThieng();
        this.initMap();
    }

    private void initMap() {
        int[][] tileTyleTop = readTileIndexTileType(ConstMap.TILE_TOP);
        for (MapTemplate mapTemp : MAP_TEMPLATES) {
            int[][] tileMap = readTileMap(mapTemp.id);
            int[] tileTop = tileTyleTop[mapTemp.tileId - 1];
            ServerData.Models.Map.Map map = new ServerData.Models.Map.Map(mapTemp.id,
                    mapTemp.name, mapTemp.planetId, mapTemp.tileId, mapTemp.bgId,
                    mapTemp.bgType, mapTemp.type, tileMap, tileTop,
                    mapTemp.zones,
                    mapTemp.maxPlayerPerZone, mapTemp.wayPoints);
            MAPS.add(map);
            map.initMob(mapTemp.mobTemp, mapTemp.mobLevel, mapTemp.mobHp, mapTemp.mobX, mapTemp.mobY);
            map.initNpc(mapTemp.npcId, mapTemp.npcX, mapTemp.npcY);
//            new Thread(map, "Update map " + map.mapName).start();
        }
        new Thread(() -> {
            try {
                while (!Maintenance.isRuning) {
                    long st = System.currentTimeMillis();
                    MAPS.forEach(map -> {
                        for (Zone zone : map.zones) {
                            try {
                                zone.update();
                            } catch (Exception e) {

                            }
                        }
                    });
                    long timeDo = System.currentTimeMillis() - st;
                    if (1000 - timeDo > 0) {
                        Thread.sleep(1000 - timeDo);
                    }
                }
            } catch (Exception ex) {

            }
        }, "Update maps").start();
        Referee r = new Referee();
        r.initReferee();
        Referee1 r1 = new Referee1();
        r1.initReferee1();
        Logger.log(Logger.RED_BOLD, "Init map thành công!\n");
    }

    public static void loadPart() {
        JSONValue jv = new JSONValue();
        JSONArray dataArray = null;
        JSONObject dataObject = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try (Connection con = GirlkunDB.getConnection();) {
            //load part
            ps = con.prepareStatement("select * from part");
            rs = ps.executeQuery();
            List<Part> parts = new ArrayList<>();
            while (rs.next()) {
                Part part = new Part();
                part.id = rs.getShort("id");
                part.type = rs.getByte("type");
                dataArray = (JSONArray) jv.parse(rs.getString("data").replaceAll("\\\"", ""));
                for (int j = 0; j < dataArray.size(); j++) {
                    JSONArray pd = (JSONArray) jv.parse(String.valueOf(dataArray.get(j)));
                    part.partDetails.add(new PartDetail(Short.parseShort(String.valueOf(pd.get(0))),
                            Byte.parseByte(String.valueOf(pd.get(1))),
                            Byte.parseByte(String.valueOf(pd.get(2)))));
                    pd.clear();
                }
                parts.add(part);
                dataArray.clear();
            }
            DataOutputStream dos = new DataOutputStream(new FileOutputStream("data/girlkun/update_data/part"));
            dos.writeShort(parts.size());
            for (Part part : parts) {
                dos.writeByte(part.type);
                for (PartDetail partDetail : part.partDetails) {
                    dos.writeShort(partDetail.iconId);
                    dos.writeByte(partDetail.dx);
                    dos.writeByte(partDetail.dy);
                }
            }
            dos.flush();
            dos.close();
            Logger.success("Load part thành công (" + parts.size() + ")\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
public static List<TOP> realTopSieuHang2(Player pl) {
        List<TOP> tops = new ArrayList<>();
        try {
            //System.out.println("SELECT id, rank_sieu_hang AS rank FROM player WHERE rank_sieu_hang <= " + pl.rankSieuHang + " ORDER BY rank_sieu_hang DESC LIMIT 10");
            GirlkunResultSet rs = GirlkunDB.executeQuery("SELECT id, rank_sieu_hang AS rank FROM player WHERE rank_sieu_hang <= " + pl.rankSieuHang + " ORDER BY rank_sieu_hang DESC LIMIT 10");
            while (rs.next()) {
                int rank = rs.getInt("rank_sieu_hang");
                 if (Math.abs(rank - pl.rankSieuHang) >=11  && Math.abs(rank - pl.rankSieuHang) <= 100) {
                    TOP top = TOP.builder().id_player(rs.getInt("id")).build();
                    top.setInfo1("50k hồng ngọc/ ngày");
                    top.setInfo2("50k hồng ngọc/ ngày");
                    top.setRank(rank);
                    tops.add(top);
                } 
                if (Math.abs(rank - pl.rankSieuHang) >=4  && Math.abs(rank - pl.rankSieuHang) <= 10) {
                    TOP top = TOP.builder().id_player(rs.getInt("id")).build();
                    top.setInfo1("70k hồng ngọc/ ngày");
                    top.setInfo2("70k hồng ngọc/ ngày");
                    top.setRank(rank);
                    tops.add(top);
                }
                if (Math.abs(rank - pl.rankSieuHang) >=2  && Math.abs(rank - pl.rankSieuHang) <= 3) {
                    TOP top = TOP.builder().id_player(rs.getInt("id")).build();
                    top.setInfo1("100k hồng ngọc/ ngày");
                    top.setInfo2("100k hồng ngọc/ ngày");
                    top.setRank(rank);
                    tops.add(top);
                }
                 if (Math.abs(rank - pl.rankSieuHang) == 1) {
                    TOP top = TOP.builder().id_player(rs.getInt("id")).build();
                    top.setInfo1("200k hồng ngọc/ ngày");
                    top.setInfo2("200k hồng ngọc/ ngày");
                    top.setRank(rank);
                    tops.add(top);
                }
            }
            Collections.reverse(tops);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tops;
    }

    public static List<TOP> realTopSieuHang2(Connection con) {
        List<TOP> tops = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT id, rank_sieu_hang AS rank FROM player ORDER BY rank_sieu_hang ASC LIMIT 100");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                long rank = rs.getLong("rank");
                if (rank ==1) {
                    TOP top = TOP.builder().id_player(rs.getInt("id")).build();
                   top.setInfo1("200k hồng ngọc/ ngày");
                    top.setInfo2("200k hồng ngọc/ ngày");
                    tops.add(top);
                }
                 if (rank >=2 && rank <=3) {
                    TOP top = TOP.builder().id_player(rs.getInt("id")).build();
                   top.setInfo1("100k hồng ngọc/ ngày");
                    top.setInfo2("100k hồng ngọc/ ngày");
                    tops.add(top);
                }
                  if (rank >=4 && rank <=10) {
                    TOP top = TOP.builder().id_player(rs.getInt("id")).build();
                    top.setInfo1("70k hồng ngọc/ ngày");
                    top.setInfo2("70k hồng ngọc/ ngày");
                    tops.add(top);
                }
                   if (rank >=10 && rank <=100) {
                    TOP top = TOP.builder().id_player(rs.getInt("id")).build();
                   top.setInfo1("50k hồng ngọc/ ngày");
                    top.setInfo2("50k hồng ngọc/ ngày");
                    tops.add(top);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tops;
    }
    private void loadDatabase() {
        long st = System.currentTimeMillis();
        JSONValue jv = new JSONValue();
        JSONArray dataArray = null;
        JSONObject dataObject = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try (Connection con = GirlkunDB.getConnection();) {
            //load part
            ps = con.prepareStatement("select * from part");
            rs = ps.executeQuery();
            List<Part> parts = new ArrayList<>();
            while (rs.next()) {
                Part part = new Part();
                part.id = rs.getShort("id");
                part.type = rs.getByte("type");
                dataArray = (JSONArray) jv.parse(rs.getString("data").replaceAll("\\\"", ""));
                for (int j = 0; j < dataArray.size(); j++) {
                    JSONArray pd = (JSONArray) jv.parse(String.valueOf(dataArray.get(j)));
                    part.partDetails.add(new PartDetail(Short.parseShort(String.valueOf(pd.get(0))),
                            Byte.parseByte(String.valueOf(pd.get(1))),
                            Byte.parseByte(String.valueOf(pd.get(2)))));
                    pd.clear();
                }
                parts.add(part);
                dataArray.clear();
            }
            DataOutputStream dos = new DataOutputStream(new FileOutputStream("data/girlkun/update_data/part"));
            dos.writeShort(parts.size());
            for (Part part : parts) {
                dos.writeByte(part.type);
                for (PartDetail partDetail : part.partDetails) {
                    dos.writeShort(partDetail.iconId);
                    dos.writeByte(partDetail.dx);
                    dos.writeByte(partDetail.dy);
                }
            }
            dos.flush();
            dos.close();
            Logger.success("Load part thành công (" + parts.size() + ")\n");

            //load small version
            ps = con.prepareStatement("select count(id) from small_version");
            rs = ps.executeQuery();
            List<byte[]> smallVersion = new ArrayList<>();
            if (rs.first()) {
                int size = rs.getInt(1);
                for (int i = 0; i < 4; i++) {
                    smallVersion.add(new byte[size]);
                }
            }
            ps = con.prepareStatement("select * from small_version order by id");
            rs = ps.executeQuery();
            int index = 0;
            while (rs.next()) {
                smallVersion.get(0)[index] = rs.getByte("x1");
                smallVersion.get(1)[index] = rs.getByte("x2");
                smallVersion.get(2)[index] = rs.getByte("x3");
                smallVersion.get(3)[index] = rs.getByte("x4");
                index++;
            }
            for (int i = 0; i < 4; i++) {
                dos = new DataOutputStream(new FileOutputStream("data/girlkun/data_img_version/x" + (i + 1) + "/img_version"));
                dos.writeShort(smallVersion.get(i).length);
                for (int j = 0; j < smallVersion.get(i).length; j++) {
                    dos.writeByte(smallVersion.get(i)[j]);
                }
                dos.flush();
                dos.close();
            }

            //load clan
            ps = con.prepareStatement("select * from clan_sv" + SERVER);
            rs = ps.executeQuery();
            while (rs.next()) {
                Clan clan = new Clan();
                clan.id = rs.getInt("id");
                clan.name = rs.getString("name");
                clan.slogan = rs.getString("slogan");
                clan.imgId = rs.getByte("img_id");
                clan.powerPoint = rs.getLong("power_point");
                clan.maxMember = rs.getByte("max_member");
                clan.capsuleClan = rs.getInt("clan_point");
                clan.level = rs.getByte("level");
                clan.createTime = (int) (rs.getTimestamp("create_time").getTime() / 1000);
                dataArray = (JSONArray) jv.parse(rs.getString("members"));
                for (int i = 0; i < dataArray.size(); i++) {
                    dataObject = (JSONObject) jv.parse(String.valueOf(dataArray.get(i)));
                    ClanMember cm = new ClanMember();
                    cm.clan = clan;
                    cm.id = Integer.parseInt(String.valueOf(dataObject.get("id")));
                    cm.name = String.valueOf(dataObject.get("name"));
                    cm.head = Short.parseShort(String.valueOf(dataObject.get("head")));
                    cm.body = Short.parseShort(String.valueOf(dataObject.get("body")));
                    cm.leg = Short.parseShort(String.valueOf(dataObject.get("leg")));
                    cm.role = Byte.parseByte(String.valueOf(dataObject.get("role")));
                    cm.donate = Integer.parseInt(String.valueOf(dataObject.get("donate")));
                    cm.receiveDonate = Integer.parseInt(String.valueOf(dataObject.get("receive_donate")));
                    cm.memberPoint = Integer.parseInt(String.valueOf(dataObject.get("member_point")));
                    cm.clanPoint = Integer.parseInt(String.valueOf(dataObject.get("clan_point")));
                    cm.joinTime = Integer.parseInt(String.valueOf(dataObject.get("join_time")));
                    cm.timeAskPea = Long.parseLong(String.valueOf(dataObject.get("ask_pea_time")));
                    try {
                        cm.powerPoint = Long.parseLong(String.valueOf(dataObject.get("power")));
                    } catch (Exception e) {
                    }
                    clan.addClanMember(cm);
                }
                CLANS.add(clan);
                dataArray.clear();
                dataObject.clear();
            }

            ps = con.prepareStatement("select id from clan_sv" + SERVER + " order by id desc limit 1");
            rs = ps.executeQuery();
            if (rs.first()) {
                Clan.NEXT_ID = rs.getInt("id") + 1;
            }

            Logger.success("Load clan thành công (" + CLANS.size() + "), clan id tiếp theo: " + Clan.NEXT_ID + "\n");

            ps = con.prepareStatement("select * from dhvt_template");
            rs = ps.executeQuery();
            if (rs.next()) {
                DaiHoiVoThuat dhvt = new DaiHoiVoThuat();
                dhvt.NameCup = rs.getString(2);
                dhvt.Time = rs.getString(3).split("\n");
                dhvt.gem = rs.getInt(4);
                dhvt.gold = rs.getInt(5);
                dhvt.min_start = rs.getInt(6);
                dhvt.min_start_temp = rs.getInt(6);
                dhvt.min_limit = rs.getInt(7);
                LIST_DHVT.add(dhvt);
            }

            Logger.success("Load DHVT thành công (" + LIST_DHVT.size() + ")\n");

            //load skill
            ps = con.prepareStatement("select * from skill_template order by nclass_id, slot");
            rs = ps.executeQuery();
            byte nClassId = -1;
            NClass nClass = null;
            while (rs.next()) {
                byte id = rs.getByte("nclass_id");
                if (id != nClassId) {
                    nClassId = id;
                    nClass = new NClass();
                    nClass.name = id == ConstPlayer.TRAI_DAT ? "Trái Đất" : id == ConstPlayer.NAMEC ? "Namếc" : "Xayda";
                    nClass.classId = nClassId;
                    NCLASS.add(nClass);
                }
                SkillTemplate skillTemplate = new SkillTemplate();
                skillTemplate.classId = nClassId;
                skillTemplate.id = rs.getByte("id");
                skillTemplate.name = rs.getString("name");
                skillTemplate.maxPoint = rs.getByte("max_point");
                skillTemplate.manaUseType = rs.getByte("mana_use_type");
                skillTemplate.type = rs.getByte("type");
                skillTemplate.iconId = rs.getShort("icon_id");
                skillTemplate.damInfo = rs.getString("dam_info");
                nClass.skillTemplatess.add(skillTemplate);

                dataArray = (JSONArray) jv.parse(
                        rs.getString("skills")
                                .replaceAll("\\[\"", "[")
                                .replaceAll("\"\\[", "[")
                                .replaceAll("\"\\]", "]")
                                .replaceAll("\\]\"", "]")
                                .replaceAll("\\}\",\"\\{", "},{")
                );
                for (int j = 0; j < dataArray.size(); j++) {
                    JSONObject dts = (JSONObject) jv.parse(String.valueOf(dataArray.get(j)));
                    Skill skill = new Skill();
                    skill.template = skillTemplate;
                    skill.skillId = Short.parseShort(String.valueOf(dts.get("id")));
                    skill.point = Byte.parseByte(String.valueOf(dts.get("point")));
                    skill.powRequire = Long.parseLong(String.valueOf(dts.get("power_require")));
                    skill.manaUse = Integer.parseInt(String.valueOf(dts.get("mana_use")));
                    skill.coolDown = Integer.parseInt(String.valueOf(dts.get("cool_down")));
                    skill.dx = Integer.parseInt(String.valueOf(dts.get("dx")));
                    skill.dy = Integer.parseInt(String.valueOf(dts.get("dy")));
                    skill.maxFight = Integer.parseInt(String.valueOf(dts.get("max_fight")));
                    skill.damage = Short.parseShort(String.valueOf(dts.get("damage")));
                    skill.price = Short.parseShort(String.valueOf(dts.get("price")));
                    skill.moreInfo = String.valueOf(dts.get("info"));
                    skillTemplate.skillss.add(skill);
                }
            }
            Logger.success("Load skill thành công (" + NCLASS.size() + ")\n");

            //load head avatar
            ps = con.prepareStatement("select * from head_avatar");
            rs = ps.executeQuery();
            while (rs.next()) {
                HeadAvatar headAvatar = new HeadAvatar(rs.getInt("head_id"), rs.getInt("avatar_id"));
                HEAD_AVATARS.add(headAvatar);
            }
            Logger.success("Load head avatar thành công (" + HEAD_AVATARS.size() + ")\n");

            //load flag bag
            ps = con.prepareStatement("select * from flag_bag");
            rs = ps.executeQuery();
            while (rs.next()) {
                FlagBag flagBag = new FlagBag();
                flagBag.id = rs.getInt("id");
                flagBag.name = rs.getString("name");
                flagBag.gold = rs.getInt("gold");
                flagBag.gem = rs.getInt("gem");
                flagBag.iconId = rs.getShort("icon_id");
                String[] iconData = rs.getString("icon_data").split(",");
                flagBag.iconEffect = new short[iconData.length];
                for (int j = 0; j < iconData.length; j++) {
                    flagBag.iconEffect[j] = Short.parseShort(iconData[j].trim());
                }
                FLAGS_BAGS.add(flagBag);
            }
            Logger.success("Load flag bag thành công (" + FLAGS_BAGS.size() + ")\n");

            //load intrinsic
            ps = con.prepareStatement("select * from intrinsic");
            rs = ps.executeQuery();
            while (rs.next()) {
                Intrinsic intrinsic = new Intrinsic();
                intrinsic.id = rs.getByte("id");
                intrinsic.name = rs.getString("name");
                intrinsic.paramFrom1 = rs.getShort("param_from_1");
                intrinsic.paramTo1 = rs.getShort("param_to_1");
                intrinsic.paramFrom2 = rs.getShort("param_from_2");
                intrinsic.paramTo2 = rs.getShort("param_to_2");
                intrinsic.icon = rs.getShort("icon");
                intrinsic.gender = rs.getByte("gender");
                switch (intrinsic.gender) {
                    case ConstPlayer.TRAI_DAT:
                        INTRINSIC_TD.add(intrinsic);
                        break;
                    case ConstPlayer.NAMEC:
                        INTRINSIC_NM.add(intrinsic);
                        break;
                    case ConstPlayer.XAYDA:
                        INTRINSIC_XD.add(intrinsic);
                        break;
                    default:
                        INTRINSIC_TD.add(intrinsic);
                        INTRINSIC_NM.add(intrinsic);
                        INTRINSIC_XD.add(intrinsic);
                }
                INTRINSICS.add(intrinsic);
            }
            Logger.success("Load intrinsic thành công (" + INTRINSICS.size() + ")\n");

            //load task
            ps = con.prepareStatement("SELECT id, task_main_template.name, detail, "
                    + "task_sub_template.name AS 'sub_name', max_count, notify, npc_id, map "
                    + "FROM task_main_template JOIN task_sub_template ON task_main_template.id = "
                    + "task_sub_template.task_main_id");
            rs = ps.executeQuery();
            int taskId = -1;
            TaskMain task = null;
            while (rs.next()) {
                int id = rs.getInt("id");
                if (id != taskId) {
                    taskId = id;
                    task = new TaskMain();
                    task.id = taskId;
                    task.name = rs.getString("name");
                    task.detail = rs.getString("detail");
                    TASKS.add(task);
                }
                SubTaskMain subTask = new SubTaskMain();
                subTask.name = rs.getString("sub_name");
                subTask.maxCount = rs.getShort("max_count");
                subTask.notify = rs.getString("notify");
                subTask.npcId = rs.getByte("npc_id");
                subTask.mapId = rs.getShort("map");
                task.subTasks.add(subTask);
            }
            Logger.success("Load task thành công (" + TASKS.size() + ")\n");

            //load side task
            ps = con.prepareStatement("select * from side_task_template");
            rs = ps.executeQuery();
            while (rs.next()) {
                SideTaskTemplate sideTask = new SideTaskTemplate();
                sideTask.id = rs.getInt("id");
                sideTask.name = rs.getString("name");
                String[] mc1 = rs.getString("max_count_lv1").split("-");
                String[] mc2 = rs.getString("max_count_lv2").split("-");
                String[] mc3 = rs.getString("max_count_lv3").split("-");
                String[] mc4 = rs.getString("max_count_lv4").split("-");
                String[] mc5 = rs.getString("max_count_lv5").split("-");
                sideTask.count[0][0] = Integer.parseInt(mc1[0]);
                sideTask.count[0][1] = Integer.parseInt(mc1[1]);
                sideTask.count[1][0] = Integer.parseInt(mc2[0]);
                sideTask.count[1][1] = Integer.parseInt(mc2[1]);
                sideTask.count[2][0] = Integer.parseInt(mc3[0]);
                sideTask.count[2][1] = Integer.parseInt(mc3[1]);
                sideTask.count[3][0] = Integer.parseInt(mc4[0]);
                sideTask.count[3][1] = Integer.parseInt(mc4[1]);
                sideTask.count[4][0] = Integer.parseInt(mc5[0]);
                sideTask.count[4][1] = Integer.parseInt(mc5[1]);
                SIDE_TASKS_TEMPLATE.add(sideTask);
            }
            Logger.success("Load side task thành công (" + SIDE_TASKS_TEMPLATE.size() + ")\n");

            //load item template
            ps = con.prepareStatement("select * from item_template");
            rs = ps.executeQuery();
            while (rs.next()) {
                ItemTemplate itemTemp = new ItemTemplate();
                itemTemp.id = rs.getShort("id");
                itemTemp.type = rs.getByte("type");
                itemTemp.gender = rs.getByte("gender");
                itemTemp.name = rs.getString("name");
                itemTemp.description = rs.getString("description");
                itemTemp.iconID = rs.getShort("icon_id");
                itemTemp.part = rs.getShort("part");
                itemTemp.isUpToUp = rs.getBoolean("is_up_to_up");
                itemTemp.strRequire = rs.getInt("power_require");
                itemTemp.gold = rs.getInt("gold");
                itemTemp.gem = rs.getInt("gem");
                itemTemp.head = rs.getInt("head");
                itemTemp.body = rs.getInt("body");
                itemTemp.leg = rs.getInt("leg");
                ITEM_TEMPLATES.add(itemTemp);
            }
            for (int i = 0; i < ITEM_TEMPLATES.size(); i++) {
                if (ITEM_TEMPLATES.get(i).type == 5) {
                    CAITRANG.add(ItemService.gI().createNewItem(((short) ITEM_TEMPLATES.get(i).id)));
                }
            }
            Logger.success("Load map item template thành công (" + ITEM_TEMPLATES.size() + ")\n");

            //load item option template
            ps = con.prepareStatement("select id, name from item_option_template");
            rs = ps.executeQuery();
            while (rs.next()) {
                ItemOptionTemplate optionTemp = new ItemOptionTemplate();
                optionTemp.id = rs.getInt("id");
                optionTemp.name = rs.getString("name");
                ITEM_OPTION_TEMPLATES.add(optionTemp);
            }
            Logger.success("Load map item option template thành công (" + ITEM_OPTION_TEMPLATES.size() + ")\n");

            //load shop
            SHOPS = ShopData.getShops(con);
            Logger.success("Load shop thành công (" + SHOPS.size() + ")\n");

            //load reward lucky round
            File folder = new File("data/girlkun/data_lucky_round_reward");
            for (File fileEntry : folder.listFiles()) {
                if (!fileEntry.isDirectory()) {
                    BufferedReader br = new BufferedReader(new FileReader(fileEntry));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        line = line.replaceAll("[{}\\[\\]]", "");
                        String[] arrSub = line.split("\\|");
                        String[] data1 = arrSub[0].split(":");
                        ItemLuckyRound item = new ItemLuckyRound();
                        item.temp = ItemService.gI().getTemplate(Integer.parseInt(data1[0]));
                        item.ratio = Integer.parseInt(data1[1]);
                        item.typeRatio = Integer.parseInt(data1[2]);
                        if (arrSub.length > 1) {
                            String[] data2 = arrSub[1].split(";");
                            for (String str : data2) {
                                String[] data = str.split(":");
                                ItemOptionLuckyRound io = new ItemOptionLuckyRound();
                                Item.ItemOption itemOption = new Item.ItemOption(Integer.parseInt(data[0]), 0);
                                io.itemOption = itemOption;
                                String[] param = data[1].split("-");
                                io.param1 = Integer.parseInt(param[0]);
                                if (param.length == 2) {
                                    io.param2 = Integer.parseInt(param[1]);
                                }
                                item.itemOptions.add(io);
                            }
                        }
                        LUCKY_ROUND_REWARDS.add(item);
                    }
                }
            }
            Logger.success("Load reward lucky round thành công (" + LUCKY_ROUND_REWARDS.size() + ")\n");

            // load reward mob
            folder = new File("data/girlkun/mob_reward");
            for (File fileEntry : folder.listFiles()) {
                if (!fileEntry.isDirectory()) {
                    DataInputStream dis = new DataInputStream(new FileInputStream(fileEntry));
                    int size = dis.readInt();
                    for (int i = 0; i < size; i++) {
                        int mobId = dis.readInt();
                        MobReward mobReward = MOB_REWARDS.get(mobId);
                        if (mobReward == null) {
                            mobReward = new MobReward(mobId);
                            MOB_REWARDS.put(mobId, mobReward);
                        }
                        int itemId = dis.readInt();
                        String[] quantity = dis.readUTF().split("-");
                        String[] ratio = dis.readUTF().split("-");
                        int gender = dis.readInt();
                        String map = dis.readUTF();
                        String[] arrMap = map.replaceAll("[\\]\\[]", "").split(",");
                        int[] mapDrop = new int[arrMap.length];
                        for (int g = 0; g < mapDrop.length; g++) {
                            mapDrop[g] = Integer.parseInt(arrMap[g]);
                        }
                        ItemMobReward item = new ItemMobReward(itemId, mapDrop,
                                new int[]{Integer.parseInt(quantity[0]), Integer.parseInt(quantity[1])},
                                new int[]{Integer.parseInt(ratio[0]), Integer.parseInt(ratio[1])}, gender);
                        if (item.getTemp().type == 30) { // sao pha lê
                            item.setRatio(new int[]{20, Integer.parseInt(ratio[1])});
                        }
                        if (item.getTemp().type == 14) { // 14 đá nâng cấp
                            item.setRatio(new int[]{20, Integer.parseInt(ratio[1])});
                        }
                        if (item.getTemp().type < 5) {
                            item.setRatio(new int[]{Integer.parseInt(ratio[0]), Integer.parseInt(ratio[1]) / 4 * 3});
                        }
                        if (item.getTemp().type == 9) { // vàng
                            mobReward.getGoldReward().add(item);
                        } else {
                            mobReward.getItemReward().add(item);
                        }
                        int sizeOption = dis.readInt();
                        for (int j = 0; j < sizeOption; j++) {
                            int optionId = dis.readInt();
                            String[] param = dis.readUTF().split("-");
                            String[] ratioOption = dis.readUTF().split("-");
                            ItemOptionMobReward option = new ItemOptionMobReward(optionId,
                                    new int[]{Integer.parseInt(param[0]), Integer.parseInt(param[1])},
                                    new int[]{Integer.parseInt(ratioOption[0]), Integer.parseInt(ratioOption[1])});
                            item.getOption().add(option);
                        }
                    }
                }
            }
            Logger.success("Load reward mob thành công (" + MOB_REWARDS.size() + ")\n");

            //load notify
            folder = new File("data/girlkun/notify");
            for (File fileEntry : folder.listFiles()) {
                if (!fileEntry.isDirectory()) {
                    StringBuffer notify = new StringBuffer(fileEntry.getName().substring(0, fileEntry.getName().lastIndexOf("."))).append("<>");
                    BufferedReader br = new BufferedReader(new FileReader(fileEntry));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        notify.append(line + "\n");
                    }
                    NOTIFY.add(notify.toString());
                }
            }
            Logger.success("Load notify thành công (" + NOTIFY.size() + ")\n");

            //load caption
            ps = con.prepareStatement("select * from caption");
            rs = ps.executeQuery();
            while (rs.next()) {
                CAPTIONS.add(rs.getString("name"));
            }
            Logger.success("Load caption thành công (" + CAPTIONS.size() + ")\n");

            //load image by name
            ps = con.prepareStatement("select name, n_frame from img_by_name");
            rs = ps.executeQuery();
            while (rs.next()) {
                IMAGES_BY_NAME.put(rs.getString("name"), rs.getByte("n_frame"));
            }
            Logger.success("Load images by name thành công (" + IMAGES_BY_NAME.size() + ")\n");

            //load mob template
            ps = con.prepareStatement("select * from mob_template");
            rs = ps.executeQuery();
            while (rs.next()) {
                MobTemplate mobTemp = new MobTemplate();
                mobTemp.id = rs.getByte("id");
                mobTemp.type = rs.getByte("type");
                mobTemp.name = rs.getString("name");
                mobTemp.hp = rs.getInt("hp");
                mobTemp.rangeMove = rs.getByte("range_move");
                mobTemp.speed = rs.getByte("speed");
                mobTemp.dartType = rs.getByte("dart_type");
                mobTemp.percentDame = rs.getByte("percent_dame");
                mobTemp.percentTiemNang = rs.getByte("percent_tiem_nang");
                MOB_TEMPLATES.add(mobTemp);
            }
            Logger.success("Load mob template thành công (" + MOB_TEMPLATES.size() + ")\n");

            //load npc template
            ps = con.prepareStatement("select * from npc_template");
            rs = ps.executeQuery();
            while (rs.next()) {
                NpcTemplate npcTemp = new NpcTemplate();
                npcTemp.id = rs.getByte("id");
                npcTemp.name = rs.getString("name");
                npcTemp.head = rs.getShort("head");
                npcTemp.body = rs.getShort("body");
                npcTemp.leg = rs.getShort("leg");
                npcTemp.avatar = rs.getInt("avatar");
                NPC_TEMPLATES.add(npcTemp);
            }
            Logger.success("Load npc template thành công (" + NPC_TEMPLATES.size() + ")\n");

            // Thanh Tuu
            ps = con.prepareStatement("select * from archive_template");
            rs = ps.executeQuery();
            while (rs.next()) {
                ArchivementTemplate a = new ArchivementTemplate();
                a.id = rs.getInt("id");
                a.info1 = rs.getString("info1");
                a.info2 = rs.getString("info2");
                a.money = rs.getInt("money");
                A_TEMPLATES.add(a);
            }
            Logger.log(Logger.PURPLE, "Load archive template thành công (" + A_TEMPLATES.size() + ")\n");

            //load map template
            ps = con.prepareStatement("select count(id) from map_template");
            rs = ps.executeQuery();
            if (rs.first()) {
                int countRow = rs.getShort(1);
                MAP_TEMPLATES = new MapTemplate[countRow];
                ps = con.prepareStatement("select * from map_template");
                rs = ps.executeQuery();
                short i = 0;
                while (rs.next()) {
                    MapTemplate mapTemplate = new MapTemplate();
                    int mapId = rs.getInt("id");
                    String mapName = rs.getString("name");
                    mapTemplate.id = mapId;
                    mapTemplate.name = mapName;
                    //load data

//                    dataArray = (JSONArray) jv.parse(rs.getString("data"));
//                    mapTemplate.type = Byte.parseByte(String.valueOf(dataArray.get(0)));
//                    mapTemplate.planetId = Byte.parseByte(String.valueOf(dataArray.get(1)));
//                    mapTemplate.bgType = Byte.parseByte(String.valueOf(dataArray.get(2)));
//                    mapTemplate.tileId = Byte.parseByte(String.valueOf(dataArray.get(3)));
//                    mapTemplate.bgId = Byte.parseByte(String.valueOf(dataArray.get(4)));
//                    dataArray.clear();
                    mapTemplate.type = rs.getByte("type");
                    mapTemplate.planetId = rs.getByte("planet_id");
                    mapTemplate.bgType = rs.getByte("bg_type");
                    mapTemplate.tileId = rs.getByte("tile_id");
                    mapTemplate.bgId = rs.getByte("bg_id");
                    mapTemplate.zones = rs.getByte("zones");
                    mapTemplate.maxPlayerPerZone = rs.getByte("max_player");
                    //load waypoints
                    dataArray = (JSONArray) jv.parse(rs.getString("waypoints")
                            .replaceAll("\\[\"\\[", "[[")
                            .replaceAll("\\]\"\\]", "]]")
                            .replaceAll("\",\"", ",")
                    );
                    for (int j = 0; j < dataArray.size(); j++) {
                        WayPoint wp = new WayPoint();
                        JSONArray dtwp = (JSONArray) jv.parse(String.valueOf(dataArray.get(j)));
                        wp.name = String.valueOf(dtwp.get(0));
                        wp.minX = Short.parseShort(String.valueOf(dtwp.get(1)));
                        wp.minY = Short.parseShort(String.valueOf(dtwp.get(2)));
                        wp.maxX = Short.parseShort(String.valueOf(dtwp.get(3)));
                        wp.maxY = Short.parseShort(String.valueOf(dtwp.get(4)));
                        wp.isEnter = Byte.parseByte(String.valueOf(dtwp.get(5))) == 1;
                        wp.isOffline = Byte.parseByte(String.valueOf(dtwp.get(6))) == 1;
                        wp.goMap = Short.parseShort(String.valueOf(dtwp.get(7)));
                        wp.goX = Short.parseShort(String.valueOf(dtwp.get(8)));
                        wp.goY = Short.parseShort(String.valueOf(dtwp.get(9)));
                        mapTemplate.wayPoints.add(wp);
                        dtwp.clear();
                    }
                    dataArray.clear();
                    //load mobs
                    dataArray = (JSONArray) jv.parse(rs.getString("mobs").replaceAll("\\\"", ""));
                    mapTemplate.mobTemp = new byte[dataArray.size()];
                    mapTemplate.mobLevel = new byte[dataArray.size()];
                    mapTemplate.mobHp = new int[dataArray.size()];
                    mapTemplate.mobX = new short[dataArray.size()];
                    mapTemplate.mobY = new short[dataArray.size()];
                    for (int j = 0; j < dataArray.size(); j++) {
                        JSONArray dtm = (JSONArray) JSONValue.parse(String.valueOf(dataArray.get(j)));
                        mapTemplate.mobTemp[j] = Byte.parseByte(String.valueOf(dtm.get(0)));
                        mapTemplate.mobLevel[j] = Byte.parseByte(String.valueOf(dtm.get(1)));
                        mapTemplate.mobHp[j] = Integer.parseInt(String.valueOf(dtm.get(2)));
                        mapTemplate.mobX[j] = Short.parseShort(String.valueOf(dtm.get(3)));
                        mapTemplate.mobY[j] = Short.parseShort(String.valueOf(dtm.get(4)));
                        dtm.clear();
                    }
                    dataArray.clear();
                    //load npcs
                    dataArray = (JSONArray) jv.parse(rs.getString("npcs").replaceAll("\\\"", ""));
                    mapTemplate.npcId = new byte[dataArray.size()];
                    mapTemplate.npcX = new short[dataArray.size()];
                    mapTemplate.npcY = new short[dataArray.size()];
                    for (int j = 0; j < dataArray.size(); j++) {
                        JSONArray dtn = (JSONArray) JSONValue.parse(String.valueOf(dataArray.get(j)));
                        mapTemplate.npcId[j] = Byte.parseByte(String.valueOf(dtn.get(0)));
                        mapTemplate.npcX[j] = Short.parseShort(String.valueOf(dtn.get(1)));
                        mapTemplate.npcY[j] = Short.parseShort(String.valueOf(dtn.get(2)));
                        dtn.clear();
                    }
                    dataArray.clear();
                    MAP_TEMPLATES[i++] = mapTemplate;
                }
                Logger.success("Load map template thành công (" + MAP_TEMPLATES.length + ")\n");
                RUBY_REWARDS.add(Util.sendDo(861, 0, new ArrayList<>()));
                THUCAN_REWARDS.add(Util.sendDo(663, 0, new ArrayList<>()));
                THUCAN_REWARDS.add(Util.sendDo(664, 0, new ArrayList<>()));
                THUCAN_REWARDS.add(Util.sendDo(665, 0, new ArrayList<>()));
                THUCAN_REWARDS.add(Util.sendDo(666, 0, new ArrayList<>()));
                THUCAN_REWARDS.add(Util.sendDo(667, 0, new ArrayList<>()));
                MANH_THIENSU.add(Util.sendDo(1066, 0, new ArrayList<>()));
                MANH_THIENSU.add(Util.sendDo(1067, 0, new ArrayList<>()));
                MANH_THIENSU.add(Util.sendDo(1068, 0, new ArrayList<>()));
                MANH_THIENSU.add(Util.sendDo(1069, 0, new ArrayList<>()));
                MANH_THIENSU.add(Util.sendDo(1070, 0, new ArrayList<>()));
            }

            ps = con.prepareStatement("SELECT * FROM shop_ky_gui");
            rs = ps.executeQuery();
            while (rs.next()) {
                int i = rs.getInt("id");
                int idPl = rs.getInt("player_id");
                byte tab = rs.getByte("tab");
                short itemId = rs.getShort("item_id");
                int gold = rs.getInt("gold");
                int gem = rs.getInt("gem");
                int quantity = rs.getInt("quantity");
                byte isUp = rs.getByte("isUpTop");
                boolean isBuy = rs.getByte("isBuy") == 1;
                List<Item.ItemOption> op = new ArrayList<>();
                JSONArray jsa2 = (JSONArray) JSONValue.parse(rs.getString("itemOption"));
                for (int j = 0; j < jsa2.size(); ++j) {
                    JSONObject jso2 = (JSONObject) jsa2.get(j);
                    int idOptions = Integer.parseInt(jso2.get("id").toString());
                    int param = Integer.parseInt(jso2.get("param").toString());
                    op.add(new Item.ItemOption(idOptions, param));
                }
                ShopKyGuiManager.gI().listItem.add(new ItemKyGui(i, itemId, idPl, tab, gold, gem, quantity, isUp, op, isBuy));
            }
            Logger.log(Logger.BLUE_BOLD, "Load thành công (" + ShopKyGuiManager.gI().listItem.size() + ") Item Ký Gửi!\n");

            ps = con.prepareStatement("select * from radar");
            rs = ps.executeQuery();
            while (rs.next()) {
                RadarCard rd = new RadarCard();
                rd.Id = rs.getShort("id");
                rd.IconId = rs.getShort("iconId");
                rd.Rank = rs.getByte("rank");
                rd.Max = rs.getByte("max");
                rd.Type = rs.getByte("type");
                rd.Template = rs.getShort("template");
                rd.Name = rs.getString("name");
                rd.Info = rs.getString("info");
                JSONArray arr = (JSONArray) JSONValue.parse(rs.getString("body"));
                for (int i = 0; i < arr.size(); i++) {
                    JSONObject ob = (JSONObject) arr.get(i);
                    if (ob != null) {
                        rd.Head = Short.parseShort(ob.get("head").toString());
                        rd.Body = Short.parseShort(ob.get("body").toString());
                        rd.Leg = Short.parseShort(ob.get("leg").toString());
                        rd.Bag = Short.parseShort(ob.get("bag").toString());
                    }
                }
                rd.Options.clear();
                arr = (JSONArray) JSONValue.parse(rs.getString("options"));
                for (int i = 0; i < arr.size(); i++) {
                    JSONObject ob = (JSONObject) arr.get(i);
                    if (ob != null) {
                        rd.Options.add(new OptionCard(Integer.parseInt(ob.get("id").toString()), Short.parseShort(ob.get("param").toString()), Byte.parseByte(ob.get("activeCard").toString())));
                    }
                }
                rd.Require = rs.getShort("require");
                rd.RequireLevel = rs.getShort("require_level");
                rd.AuraId = rs.getShort("aura_id");
            }

            topSM = realTop(queryTopSM, con);
            Logger.success("Load top sm thành công (" + topSM.size() + ")\n");
            topHP = realTop(queryTopHP, con);
            Logger.success("Load top HP thành công (" + topHP.size() + ")\n");
            
            topKI = realTop(queryTopKI, con);
            Logger.success("Load top KI thành công (" + topKI.size() + ")\n");
            topNV = realTop(queryTopNV, con);
            Logger.success("Load top nv thành công (" + topNV.size() + ")\n");
            topSK = realTop(queryTopSK, con);
            Logger.success("Load top sk thành công (" + topSK.size() + ")\n");
            topGT = realTop(queryTopGT, con);
            Logger.success("Load top sk thành công (" + topGT.size() + ")\n");
            topPVP = realTop(queryTopPVP, con);
            Logger.success("Load top PVP thành công (" + topPVP.size() + ")\n");
            topSD = realTop(queryTopSD, con);
            Logger.success("Load top Sức Đánh thành công (" + topSD.size() + ")\n");
            topHN = realTop(queryTopHN, con);
            Logger.success("Load top Hồng ngọc thành công (" + topHN.size() + ")\n");
            topVND = realTop(queryTopVND, con);
            Logger.success("Load top Nạp thành công (" + topVND.size() + ")\n");
            topNHS = realTop(queryTopNHS, con);
            Logger.success("Load top NHS thành công (" + topNHS.size() + ")\n");
             topNV = realTop(queryTopNV, con);
            Logger.success("Load top nv thành công (" + topNV.size() + ")\n");
               topSieuHang = realTopSieuHang2(con);
            Logger.success("Load top Siêu hạng thành công (" + topSieuHang.size() + ")\n");
            Manager.timeRealTop = System.currentTimeMillis();
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
            }
        } catch (Exception e) {
            Logger.logException(Manager.class, e, "Lỗi load database");
            System.exit(0);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
            }
        }
        Logger.log(Logger.RED_BOLD, "Tổng thời gian load database: " + (System.currentTimeMillis() - st) + "(ms)\n");
    }

    public static List<TOP> realTop(String query, Connection con) {
        List<TOP> tops = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                TOP top = TOP.builder().id_player(rs.getInt(1)).build();
                Player pl = Client.gI().getPlayer(rs.getInt(1));
                switch (query) {
                    case queryTopSM:
                        if (pl != null) {
                            top.setInfo1("SM : " + Util.format(pl.nPoint.power));
                        } else {
                            top.setInfo1("SM : " + Util.format(rs.getLong("sm")));
                        }
                        top.setInfo2("Update Time : " + TimeUtil.getTimeNow("HH:mm:ss") + "\n" + "|7|[ DragonBall Kamui ]\nWebsite: YTB:KhanhDTK");
                        break;
                    case queryTopNV:
                        if (pl != null) {
                            top.setInfo1("Nhiệm Vụ : " + pl.playerTask.taskMain.id);
                        } else {
                            top.setInfo1("Nhiệm Vụ : " + rs.getByte("nv"));
                        }
                        top.setInfo2("Update Time : " + TimeUtil.getTimeNow("HH:mm:ss") + "\n" + "|7|[ DragonBall Kamui ]\nWebsite: YTB:KhanhDTK");
                        break;
                    case queryTopSK:
                        if (pl != null) {
                            top.setInfo1(pl.inventory.event + " Điểm Event");
                        } else {
                            top.setInfo1(rs.getInt("event") + " Điểm Event");
                        }
                        top.setInfo2("Update Time : " + TimeUtil.getTimeNow("HH:mm:ss") + "\n" + "|7|[ DragonBall Kamui ]\nWebsite: YTB:KhanhDTK");
                        break;
                    case queryTopGT:
                        if (pl != null) {
                            top.setInfo1(pl.gtPoint + " Điểm Gắp Thú");
                        } else {
                            top.setInfo1(rs.getInt("gt") + " Điểm Gắp Thú");
                        }
                        top.setInfo2("Update Time : " + TimeUtil.getTimeNow("HH:mm:ss") + "\n" + "|7|[ DragonBall Kamui ]\nWebsite: YTB:KhanhDTK");
                        break;
                    case queryTopPVP:
                        if (pl != null) {
                            top.setInfo1(pl.pointPvp + " Điểm PvP");
                        } else {
                            top.setInfo1(rs.getInt("pointPVP") + " Điểm PvP");
                        }
                        top.setInfo2("Update Time : " + TimeUtil.getTimeNow("HH:mm:ss") + "\n" + "|7|[ DragonBall Kamui ]\nWebsite: YTB:KhanhDTK");
                        break;
                    case queryTopSD:
                        if (pl != null) {
                            top.setInfo1("SD : " + Util.format(pl.nPoint.dame));
                        } else {
                            top.setInfo1("SD : " + Util.format(rs.getLong("sd")));
                        }
                        top.setInfo2("Update Time : " + TimeUtil.getTimeNow("HH:mm:ss") + "\n" + "|7|[ DragonBall Kamui ]\nWebsite: YTB:KhanhDTK");
                        break;
                    case queryTopHP:
                        if (pl != null) {
                            top.setInfo1("HP : " + Util.format(pl.nPoint.hpMax));
                        } else {
                            top.setInfo1("HP : " + Util.format(rs.getLong("hp")));
                        }
                        top.setInfo2("Update Time : " + TimeUtil.getTimeNow("HH:mm:ss") + "\n" + "|7|[ DragonBall Kamui ]\nWebsite: YTB:KhanhDTK");
                        break;
                    case queryTopKI:
                        if (pl != null) {
                            top.setInfo1("KI : " + Util.format(pl.nPoint.mpMax));
                        } else {
                            top.setInfo1("KI : " + Util.format(rs.getLong("ki")));
                        }
                        top.setInfo2("Update Time : " + TimeUtil.getTimeNow("HH:mm:ss") + "\n" + "|7|[ DragonBall Kamui ]\nWebsite: YTB:KhanhDTK");
                        break;
                    case queryTopHN:
                        if (pl != null && pl.getSession().isConnected()) {
                            top.setInfo1("Ruby : " + Util.format(pl.inventory.ruby));
                        } else {
                            top.setInfo1("Ruby : " + Util.format(rs.getLong("hn")));
                        }
                        top.setInfo2("Update Time : " + TimeUtil.getTimeNow("HH:mm:ss") + "\n" + "|7|[ DragonBall Kamui ]\nWebsite: YTB:KhanhDTK");
                        break;
                    case queryTopVND:
                        if (pl != null) {
                            top.setInfo1("Tổng Nạp : " + Util.format(pl.getSession().tongnap));
                        } else {
                            top.setInfo1("Tổng Nạp : " + Util.format(rs.getLong("tongnap")));
                        }
                        top.setInfo2("Update Time : " + TimeUtil.getTimeNow("HH:mm:ss") + "\n" + "|7|[ DragonBall Kamui ]\nWebsite: YTB:KhanhDTK");
                        break;
                    case queryTopNHS:
                        if (pl != null) {
                            top.setInfo1(pl.nhsPoint + " Điểm");
                        } else {
                            top.setInfo1(rs.getInt("nhs") + " Điểm");
                        }
                        top.setInfo2("Update Time : " + TimeUtil.getTimeNow("HH:mm:ss") + "\n" + "|7|[ DragonBall Kamui ]\nWebsite: YTB:KhanhDTK");
                        break;
                }
                tops.add(top);
            }
        } catch (Exception e) {
        }
        return tops;
    }

    public static List<TOP> allTop(String query, Connection con) {
        List<TOP> tops = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                TOP top = TOP.builder().id_player(rs.getInt(1)).build();
                Player pl = Client.gI().getPlayer(rs.getInt(1));
                switch (query) {
                    case queryTopSM:
                        top.setInfo1("SM : " + Util.format(rs.getLong("sm")));
                        top.setInfo2("Update Time : " + TimeUtil.getTimeNow("HH:mm:ss") + "\n" + "|7|[ DragonBall Kamui ]\nWebsite: YTB:KhanhDTK");
                        break;
                    case queryTopNV:
                        top.setInfo1("Nhiệm Vụ : " + rs.getByte("nv"));
                        top.setInfo2("Update Time : " + TimeUtil.getTimeNow("HH:mm:ss") + "\n" + "|7|[ DragonBall Kamui ]\nWebsite: YTB:KhanhDTK");
                        break;
                    case queryTopSK:
                        top.setInfo1(rs.getInt("event") + " Điểm Event");
                        top.setInfo2("Update Time : " + TimeUtil.getTimeNow("HH:mm:ss") + "\n" + "|7|[ DragonBall Kamui ]\nWebsite: YTB:KhanhDTK");
                        break;
                    case queryTopGT:
                        top.setInfo1(rs.getInt("gt") + " Điểm Gắp Thú");
                        top.setInfo2("Update Time : " + TimeUtil.getTimeNow("HH:mm:ss") + "\n" + "|7|[ DragonBall Kamui ]\nWebsite: YTB:KhanhDTK");
                        break;
                    case queryTopPVP:
                        top.setInfo1(rs.getInt("pointPVP") + " Điểm PvP");
                        top.setInfo2("Update Time : " + TimeUtil.getTimeNow("HH:mm:ss") + "\n" + "|7|[ DragonBall Kamui ]\nWebsite: YTB:KhanhDTK");
                        break;
                    case queryTopSD:
                        top.setInfo1("Sức Đánh : " + Util.format(rs.getLong("sd")));
                        top.setInfo2("Update Time : " + TimeUtil.getTimeNow("HH:mm:ss") + "\n" + "|7|[ DragonBall Kamui ]\nWebsite: YTB:KhanhDTK");
                        break;
                    case queryTopHP:
                        top.setInfo1("HP : " + Util.format(rs.getLong("hp")));
                        top.setInfo2("Update Time : " + TimeUtil.getTimeNow("HH:mm:ss") + "\n" + "|7|[ DragonBall Kamui ]\nWebsite: YTB:KhanhDTK");
                        break;
                    case queryTopKI:
                        top.setInfo1("KI : " + Util.format(rs.getLong("ki")));
                        top.setInfo2("Update Time : " + TimeUtil.getTimeNow("HH:mm:ss") + "\n" + "|7|[ DragonBall Kamui ]\nWebsite: YTB:KhanhDTK");
                        break;
                    case queryTopHN:
                        top.setInfo1("Ruby : " + Util.format(rs.getLong("hn")));
                        top.setInfo2("Update Time : " + TimeUtil.getTimeNow("HH:mm:ss") + "\n" + "|7|[ DragonBall Kamui ]\nWebsite: YTB:KhanhDTK");
                        break;
                    case queryTopVND:
                        top.setInfo1("Tổng Nạp : " + Util.format(rs.getLong("tongnap")));
                        top.setInfo2("Update Time : " + TimeUtil.getTimeNow("HH:mm:ss") + "\n" + "|7|[ DragonBall Kamui ]\nWebsite: YTB:KhanhDTK");
                        break;
                    case queryTopNHS:
                        top.setInfo1(rs.getInt("nhs") + " Điểm");
                        top.setInfo2("Update Time : " + TimeUtil.getTimeNow("HH:mm:ss") + "\n" + "|7|[ DragonBall Kamui ]\nWebsite: www.YTB: Khanh");
                        break;
                }
                tops.add(top);
            }
        } catch (Exception e) {
        }
        return tops;
    }

    public void loadShop() {
        try (Connection con = GirlkunDB.getConnection()) {
            // load shop
            SHOPS = ShopData.getShops(con);
            Logger.success("Load shop thành công (" + SHOPS.size() + ")\n");
        } catch (Exception e) {
        }
    }

    public void loadTop() {
        try (Connection con = GirlkunDB.getConnection()) {
            topSM = realTop(queryTopSM, con);
            topHP = realTop(queryTopHP, con);
            topKI = realTop(queryTopKI, con);
            topSD = realTop(queryTopSD, con);
            topNV = realTop(queryTopNV, con);
            topSK = realTop(queryTopSK, con);
            topGT = realTop(queryTopGT, con);
            topPVP = realTop(queryTopPVP, con);
            topHN = realTop(queryTopHN, con);
            topVND = realTop(queryTopVND, con);
            topNHS = realTop(queryTopNHS, con);
        } catch (Exception e) {
        }
    }

    public void loadAllTop() {
        try (Connection con = GirlkunDB.getConnection()) {
            topSM = allTop(queryTopSM, con);
            topHP = allTop(queryTopHP, con);
              topSD = allTop(queryTopSD, con);
            topKI = allTop(queryTopKI, con);
            topNV = allTop(queryTopNV, con);
            topSK = allTop(queryTopSK, con);
            topGT = allTop(queryTopGT, con);
            topPVP = allTop(queryTopPVP, con);
            topHN = allTop(queryTopHN, con);
            topVND = allTop(queryTopVND, con);
            topNHS = allTop(queryTopNHS, con);
        } catch (Exception e) {
        }
    }

    public void loadProperties() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream("data/girlkun/girlkun.properties"));
        Object value = null;
        //###Config sv
        if ((value = properties.get("server.girlkun.port")) != null) {
            ServerManager.PORT = Integer.parseInt(String.valueOf(value));
        }
        if ((value = properties.get("server.girlkun.name")) != null) {
            ServerManager.NAME = String.valueOf(value);
        }
        if ((value = properties.get("server.girlkun.sv")) != null) {
            SERVER = Byte.parseByte(String.valueOf(value));
        }
        if ((value = properties.get("server.girlkun.event")) != null) {
            ConstEvent.EVENT = Byte.parseByte(String.valueOf(value));
        }
        String linkServer = "";
        for (int i = 1; i <= 10; i++) {
            value = properties.get("server.girlkun.sv" + i);
            if (value != null) {
                linkServer += String.valueOf(value) + ":0,";
            }
        }
        DataGame.LINK_IP_PORT = linkServer.substring(0, linkServer.length() - 1);
        if ((value = properties.get("server.girlkun.waitlogin")) != null) {
            SECOND_WAIT_LOGIN = Byte.parseByte(String.valueOf(value));
        }
        if ((value = properties.get("server.girlkun.maxperip")) != null) {
            MAX_PER_IP = Integer.parseInt(String.valueOf(value));
        }
        if ((value = properties.get("server.girlkun.maxplayer")) != null) {
            MAX_PLAYER = Integer.parseInt(String.valueOf(value));
        }
        if ((value = properties.get("server.girlkun.expserver")) != null) {
            RATE_EXP_SERVER = Byte.parseByte(String.valueOf(value));
        }
        if ((value = properties.get("server.girlkun.local")) != null) {
            LOCAL = String.valueOf(value).toLowerCase().equals("true");
        }
    }

    /**
     * @param tileTypeFocus tile type: top, bot, left, right...
     * @return [tileMapId][tileType]
     */
    private int[][] readTileIndexTileType(int tileTypeFocus) {
        int[][] tileIndexTileType = null;
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream("data/girlkun/map/tile_set_info"));
            int numTileMap = dis.readByte();
            tileIndexTileType = new int[numTileMap][];
            for (int i = 0; i < numTileMap; i++) {
                int numTileOfMap = dis.readByte();
                for (int j = 0; j < numTileOfMap; j++) {
                    int tileType = dis.readInt();
                    int numIndex = dis.readByte();
                    if (tileType == tileTypeFocus) {
                        tileIndexTileType[i] = new int[numIndex];
                    }
                    for (int k = 0; k < numIndex; k++) {
                        int typeIndex = dis.readByte();
                        if (tileType == tileTypeFocus) {
                            tileIndexTileType[i][k] = typeIndex;
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.logException(MapService.class, e);
        }
        return tileIndexTileType;
    }

    /**
     * @param mapId mapId
     * @return tile map for paint
     */
    private int[][] readTileMap(int mapId) {
        int[][] tileMap = null;
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream("data/girlkun/map/tile_map_data/" + mapId));
            int w = dis.readByte();
            int h = dis.readByte();
            tileMap = new int[h][w];
            for (int i = 0; i < tileMap.length; i++) {
                for (int j = 0; j < tileMap[i].length; j++) {
                    tileMap[i][j] = dis.readByte();
                }
            }
            dis.close();
        } catch (Exception e) {
        }
        return tileMap;
    }

    //service*******************************************************************
    public static Clan getClanById(int id) throws Exception {
        for (Clan clan : CLANS) {
            if (clan.id == id) {
                return clan;
            }
        }
        throw new Exception("Không tìm thấy clan id: " + id);
    }

    public static void addClan(Clan clan) {
        CLANS.add(clan);
    }

    public static int getNumClan() {
        return CLANS.size();

    }

    public static MobTemplate getMobTemplateByTemp(int mobTempId) {
        for (MobTemplate mobTemp : MOB_TEMPLATES) {
            if (mobTemp.id == mobTempId) {
                return mobTemp;
            }
        }
        return null;
    }

    public static byte getNFrameImageByName(String name) {
        Object n = IMAGES_BY_NAME.get(name);
        if (n != null) {
            return Byte.parseByte(String.valueOf(n));
        } else {
            return 0;
        }
    }

}
