package ServerData.Models.Player;

import Server.Connect.NhiemVuThanhTich.ThanhTich;
import ServerData.Models.Player.PlayerSkill.SkillSpecial;
import ServerData.Models.Map.ListMap.NgocRongSaoDenService;
import ServerData.Models.Map.ListMap.MapMaBu;
import ServerData.Models.Player.PlayerSkill.PlayerSkill;

import java.util.List;

import ServerData.Models.Clan.Clan;
import ServerData.Models.Intrinsic.IntrinsicPlayer;
import ServerData.Models.Item.Item;
import ServerData.Models.Item.ItemTime;
import ServerData.Models.NPC.MagicTree;
import Server.Data.Consts.ConstPlayer;
import Server.Data.Consts.ConstTask;
import ServerData.Models.NPC.MabuEgg;
import ServerData.Models.Map.Mob.MobMe;
import Server.Data.DataGame;
import ServerData.Boss.ListBoss.DHVT.BossDHVT;
import ServerData.Models.Clan.ClanMember;
import ServerData.Models.Map.TrapMap;
import ServerData.Models.Map.Zone;
import ServerData.Models.Map.ListMap.NgocRongSaoDen;
import ServerData.Models.PVP.IPVP;
import ServerData.Models.PVP.TYPE_LOSE_PVP;
import ServerData.Models.Map.Mob.Mob;
import ServerData.Models.NPC.BillEgg;
import ServerData.Models.NPC.NauBanh;
import ServerData.Models.Player.PlayerSkill.Skill;
import ServerData.Services.Service;
import ServerData.Server.MySession;
import ServerData.Models.Player.PlayerTask.TaskPlayer;
import com.girlkun.network.io.Message;
import ServerData.Server.Client;
import ServerData.Services.EffectSkillService;
import ServerData.Services.FriendAndEnemyService;
import ServerData.Services.InventoryServiceNew;
import ServerData.Services.PetService;
import ServerData.Services.PlayerService;
import ServerData.Models.Player.PlayerSkill.SkillService;
import ServerData.Services.TaskService;
import ServerData.Services.ChangeMapService;
//import ServerData.Services.Func.TaiXiu.ChonAiDay;
import ServerData.Services.Func.CombineNew;
import ServerData.Utils.Logger;
import ServerData.Utils.SkillUtil;
import ServerData.Utils.Util;
import java.io.IOException;

import java.util.ArrayList;

public class Player {
        public String tt = "";
    public String Hppl = "\n";
    public long rankSieuHang;
    public long numKillSieuHang;
    public BoMong achievement;
    public List<ThanhTich> Archivement = new ArrayList<>();
    public Player TrieuHoiPlayerAttack;
    public boolean isBot = false;
    public int goldChallenge = 100000000;
    public int gemChallenge = 10000;
    public boolean receivedWoodChest;
    public List<String> textRuongGo = new ArrayList<>();
    public MySession session;
  public List<Integer> idEffChar = new ArrayList<>();
    public boolean beforeDispose;
 public boolean isPet3;
    public boolean isPet;
    public boolean isNewPet;
    public boolean isNewPet1;
    public boolean haveDuongTang;
    public boolean isBoss;
        public long lastTimePickDuongTanK;
     public int diemhotong = 0; 
    //public int NguHanhSonPoint = 0;
    public int nhsPoint;
    public int gtPoint;
    public long last_time_dd;
    public IPVP pvp;
    public int pointPvp;
       public int capChuyenSinh;
    public int pointSb;
    public int mocnap;
    public int mocsanboss;
    public int sm;
    public int hp;
    public int ki;
    public int sd;
    public int nv;
    public int cuocX = 0;
    public int cuocT = 0;
    public int tempId;

    public byte maxTime = 30;
    public byte type = 0;

    public int mapIdBeforeLogout;
    public List<Zone> mapBlackBall;
    public List<Zone> mapMaBu;
    public long limitgold = 0;
    public Zone zone;
    public Zone mapBeforeCapsule;
    public List<Zone> mapCapsule;
    public Pet pet;
    public NewPet newpet;
    public NewPet newpet1;
    public MobMe mobMe;
    public Location location;
    public SetClothes setClothes;
    public EffectSkill effectSkill;
    public MabuEgg mabuEgg;
    public BillEgg billEgg;
    public TaskPlayer playerTask;
    public ItemTime itemTime;
    public Fusion fusion;
    public MagicTree magicTree;
    public IntrinsicPlayer playerIntrinsic;
    public Inventory inventory;
    public PlayerSkill playerSkill;
    public CombineNew combineNew;
    public IDMark iDMark;
    public Charms charms;
    public EffectSkin effectSkin;
    public Gift gift;
    public NPoint nPoint;
    public NgocRongSaoDenService rewardBlackBall;
    public EffectFlagBag effectFlagBag;
    public FightMabu fightMabu;
    public SkillSpecial skillSpecial;
    public Clan clan;
    public ClanMember clanMember;

    public List<Friend> friends;
    public List<Enemy> enemies;
    
    //title danh hiệu
    public boolean titleitem;
    public boolean titlett;
    public boolean usedh1;
    public long timedh1;
    public boolean usedh2;
    public long timedh2;
    public boolean usedh3;
    public long timedh3;
    public boolean dhtang1;
    public boolean dhtang2;
    public boolean dhhanhtinh;
    public boolean dhphongba = false;
    public boolean dhtuoitho = false;
    
    public byte topsm;
    public byte topnv;
    public byte topnap;

    public long ltggtv4;
    public boolean autobv;
    public boolean autobvrun;
    public long sovangauto=0;
    public boolean autoHP = false;
    public boolean autoKI = false;
    public boolean autoSD = false;
    public boolean autoGiap = false;
    
    public boolean autodapdo = false;
    public boolean reloadtitle = true;

    public long id;
    public int idboss;
    public String name;
    public byte gender;
    public boolean isNewMember;
    public short head;

    public byte typePk;

    public byte cFlag;

    public boolean haveTennisSpaceShip;

    public boolean justRevived;
    public long lastTimeRevived;

    public byte diemdanh;
    public byte diemdanhtet;
    public byte nhanban;
    public byte CheckDayOnl;
    public byte vip;
    public long timevip;
    public byte totalPlayerViolate;
    public long timeChangeZone;
    public long lastTimeUseOption;
    public int actived;
    public int vnd;
    public int account_id;

    public short idNRNM = -1;
    public short idGo = -1;
    public long lastTimePickNRNM;
    public int goldNormar;
    public int goldVIP;
    public int goldTai;
    public int goldXiu;
    public long lastTimeWin;
    public boolean isWin;
    public List<Card> Cards = new ArrayList<>();
    public short idAura = -1;
 
    public int levelWoodChest;
    // Nấu Bánh
    public int BanhChung;
    public int BanhTet;
    public int BanhChungDaNau;
    public int BanhTetDaNau;
    public Player() {
        lastTimeUseOption = System.currentTimeMillis();
        location = new Location();
        nPoint = new NPoint(this);
        inventory = new Inventory();
        playerSkill = new PlayerSkill(this);
        setClothes = new SetClothes(this);
        effectSkill = new EffectSkill(this);
        fusion = new Fusion(this);
        playerIntrinsic = new IntrinsicPlayer();
        rewardBlackBall = new NgocRongSaoDenService(this);
        effectFlagBag = new EffectFlagBag();
        fightMabu = new FightMabu(this);
        //----------------------------------------------------------------------
        iDMark = new IDMark();
        combineNew = new CombineNew();
        playerTask = new TaskPlayer();
        friends = new ArrayList<>();
        enemies = new ArrayList<>();
        itemTime = new ItemTime(this);
        charms = new Charms();
        gift = new Gift(this);
        effectSkin = new EffectSkin(this);
        skillSpecial = new SkillSpecial(this);
        achievement = new BoMong(this);
    }

    public void UpdateSMTN(Player player, byte type, long param) {
        if (param > 0) {
            Message msg;
            try {
                msg = new Message(-3);
                msg.writer().writeByte(type);// 0 là cộng sm, 1 cộng tn, 2 là cộng cả 2
                msg.writer().writeInt((int) param);// số tn cần cộng
                player.sendMessage(msg);
                msg.cleanup();
            } catch (IOException e) {
            }
        }
    }

    //chim thông báo
    public void sendAddchatYellow(String str) {
        try {
            Message m = new Message(-25);
            m.writer().writeUTF(str);
            m.writer().flush();
            session.sendMessage(m);
            m.cleanup();
        } catch (IOException ex) {
        }
    }

    public boolean isDie() {
        if (this.nPoint != null) {
            return this.nPoint.hp <= 0;
        }
        return true;
    }

    //--------------------------------------------------------------------------
    public void setSession(MySession session) {
        this.session = session;
    }

   public void sendMessage(Message msg) {
    if (this.session != null) {
        this.session.sendMessage(msg);
    }
}

    public MySession getSession() {
        return this.session;
    }

    public boolean isPl() {
        return !isPet && !isBoss && !isNewPet && !isNewPet1;
    }
public boolean isTargerDe(Player plAtt) {
        return plAtt.isNewPet || plAtt.isBoss || plAtt.isPet || plAtt.isPl();
    }
public void chat(String text) {
        Service.gI().chat(this, text);
    }

    public void update() {
        if(this.isBot){
            active();
        }
        if (!this.beforeDispose && !isBot) {
            try {
                if (iDMark != null && !iDMark.isBan()) {
                    if (nPoint != null) {
                        nPoint.update();
                    }
                    if (fusion != null) {
                        fusion.update();
                    }
                    if (effectSkin != null) {
                        effectSkill.update();
                    }
                    if (mobMe != null) {
                        mobMe.update();
                    }
                    if (effectSkin != null) {
                        effectSkin.update();
                    }
                    
                    if (pet != null) {
                        pet.update();
                    }
                    if (newpet != null) {
                        newpet.update();
                    }
                    if (newpet1 != null) {
                        newpet1.update();
                    }
                    if (magicTree != null) {
                        magicTree.update();
                    }
                    if (itemTime != null) {
                        itemTime.update();
                    }
                    if(this.autobv==true&&this.sovangauto!=0&&this.inventory.gold+500000000<InventoryServiceNew.gI().getGodMax(this)){try{Item tv = InventoryServiceNew.gI().findItem(this.inventory.itemsBag, 457);    
                    if(tv!=null&&tv.quantity>=1){if(this.inventory.gold<sovangauto){this.autobvrun=true;while(this.autobvrun==true){if(this.inventory.gold<InventoryServiceNew.gI().getGodMax(this)){
                    this.inventory.gold += 500000000;
                    InventoryServiceNew.gI().subQuantityItemsBag(this, tv, 1);
                    InventoryServiceNew.gI().sendItemBags(this);
                    PlayerService.gI().sendInfoHpMpMoney(this); 
                    Service.gI().sendThongBao(this,"|2|Auto bán vàng thành công\nNhận được 1 tỉ vàng!"); 
                    }else{this.autobvrun = false;Service.gI().sendThongBao(this,"Đã đạt giới hạn vàng\nAuto bán vàng đã tạm dừng!");
                    }}}}else{this.autobv = false;Service.gI().sendThongBao(this,"|7|Không đủ thỏi vàng\nAuto bán vàng đã tắt!");  
                    }}catch(Exception e) {
                    }}
                    
//                    if (this.isPl() && this.titleitem == true && this.titlett == true || this.isPl() && this.titleitem == true || this.isPl() && this.titlett == true) {
//                        Service.getInstance().reloadTitle(this);
//                    }
                    if (this.ltggtv4 != 0 && Util.canDoWithTime(this.ltggtv4, 1000)) {
                        ltggtv4 = 0;
                    }
                    if (this.timevip != 0 && Util.canDoWithTime(this.timevip, 1000)) {
                        timevip = 0;
                        vip = 0;
                    }
                    if (this.timedh1 != 0 && Util.canDoWithTime(this.timedh1, 1000)) {
                        timedh1 = 0;
                        usedh1 = false;
                    }
                    if (this.timedh2 != 0 && Util.canDoWithTime(this.timedh2, 1000)) {
                        timedh2 = 0;
                        usedh2 = false;
                    }
                    if (this.timedh3 != 0 && Util.canDoWithTime(this.timedh3, 1000)) {
                        timedh3 = 0;
                        usedh3 = false;
                    }
                    if (this.isPl() && NauBanh.gI().NauXong == true) {
                        this.BanhChung += this.BanhChungDaNau;
                        this.BanhTet += this.BanhTetDaNau;
                        this.BanhChungDaNau = 0;
                        this.BanhTetDaNau = 0;
                    }
                    NgocRongSaoDen.gI().update(this);
                    MapMaBu.gI().update(this);
                   if (!isBoss && this.iDMark != null && this.iDMark.isGotoFuture() && Util.canDoWithTime(this.iDMark.getLastTimeGoToFuture(), 6000)) {
                     ChangeMapService.gI().changeMapBySpaceShip(this, 102, -1, Util.nextInt(60, 200));
                  this.iDMark.setGotoFuture(false);
            }
                     if (!isBoss && this.iDMark != null&& this.iDMark.isGoToCDRD() && Util.canDoWithTime(this.iDMark.getLastTimeGoToCDRD(), 6000)) {
                        ChangeMapService.gI().changeMapInYard(this, 143, -1, 1090);
                        this.iDMark.setGoToCDRD(false);
                    }
                    else if (!isBoss && this.iDMark != null&& this.iDMark.isGoToBDKB() && Util.canDoWithTime(this.iDMark.getLastTimeGoToBDKB(), 6000)) {
                        ChangeMapService.gI().changeMapInYard(this, 135, -1, Util.nextInt(60, 200));
                        this.iDMark.setGoToBDKB(false);
                    }else if (!isBoss && this.iDMark != null&& this.iDMark.isGoToKGHD() && Util.canDoWithTime(this.iDMark.getLastTimeGoToKGHD(), 6000)) {
                        ChangeMapService.gI().changeMapInYard(this, 149, -1, Util.nextInt(60, 200));
                        this.iDMark.setGoToKGHD(false);
                    }
                    if (this.zone != null) {
                        TrapMap trap = this.zone.isInTrap(this);
                        if (trap != null) {
                            trap.doPlayer(this);
                        }
                    }
                 if (this.isPl() && this.inventory != null && this.inventory.itemsBody.get(7) != null && this.reloadtitle == false) {
                                                     Item it = this.inventory.itemsBody.get(7);
             if (it != null && it.isNotNullItem() && this.newpet == null && this.newpet1 == null) {
                           switch (it.template.id) {
                                case 942:
                                    PetService.Pet2(this, 966, 967, 968);
                                    Service.getInstance().point(this);
                                    break;
                                case 943:
                                    PetService.Pet2(this, 969, 970, 971);
                                    Service.getInstance().point(this);
                                    break;
                                case 944:
                                    PetService.Pet2(this, 972, 973, 974);
                                    Service.getInstance().point(this);
                                    break;
                                case 967:
                                    PetService.Pet2(this, 1050, 1051, 1052);
                                    Service.getInstance().point(this);
                                    break;
                                case 1107:
                                    PetService.Pet2(this, 1183, 1184, 1185);
                                    Service.getInstance().point(this);
                                    break;
                                case 1140:
                                    PetService.Pet2(this, 1285, 1286, 1287);
                                    Service.getInstance().point(this);
                                    break;
                                case 1133:
                                    PetService.Pet2(this, 1261, 1262, 1263);
                                    Service.getInstance().point(this);
                                    break;
                                case 1180:
                                    PetService.Pet2(this, 1270, 1271, 1272);
                                    Service.getInstance().point(this);
                                    break;
                                case 1181:
                                    PetService.Pet2(this, 1273, 1274, 1275);
                                    Service.getInstance().point(this);
                                    break;
                                case 1196:
                                    PetService.Pet2(this, 1294, 1295, 1296);
                                    Service.getInstance().point(this);
                                    break;
                                case 1197:
                                    PetService.Pet2(this, 1297, 1298, 1299);
                                    Service.getInstance().point(this);
                                    break;
                                case 1198:
                                    PetService.Pet2(this, 1300, 1301, 1302);
                                    Service.getInstance().point(this);
                                    break;
                                case 1221:
                                    PetService.Pet2(this, 1333, 1334, 1335);
                                    Service.getInstance().point(this);
                                    break;
                                case 1222:
                                    PetService.Pet2(this, 1336, 1337, 1338);
                                    Service.getInstance().point(this);
                                    break;
                                case 1223:
                                    PetService.Pet2(this, 1339, 1340, 1341);
                                    Service.getInstance().point(this);
                                    break;
                                case 1229:
                                    PetService.Pet2(this, 1345, 1346, 1347);
                                    Service.getInstance().point(this);
                                    break;
                                case 1230:
                                    PetService.Pet2(this, 1348, 1349, 1350);
                                    Service.getInstance().point(this);
                                    break;
                                case 1999:
                                    PetService.Pet2(this, 573, 574, 575);
                                    Service.getInstance().point(this);
                                    break;
                                case 1996:
                                    PetService.Pet2(this, 1434, 1435, 1436);
                                    Service.getInstance().point(this);
                                    break;
                                case 1482:
                                    PetService.Pet2(this, 1474, 1475, 1476);
                                    Service.getInstance().point(this);
                                    break;
                               case 1487:
                                    PetService.Pet2(this, 1489, 1490, 1491);
                                    Service.getInstance().point(this);
                                    break;
                                case 1488:
                                    PetService.Pet2(this, 1492, 1493, 1494);
                                    Service.getInstance().point(this);
                                    break;
                            }
                        }
                    } else
                        if (this.isPl() && newpet != null && newpet1 != null && !this.inventory.itemsBody.get(7).isNotNullItem()) {
                        newpet.dispose();
                        newpet = null;
                        newpet1.dispose();
                        newpet1 = null;
                    }
                      if ((this.isPl() || this.isPet) && this.inventory.itemsBody.size() >= 13
                            && this.inventory.itemsBody.get(12) != null) {
                        Item it = this.inventory.itemsBody.get(12);
                        if (it != null && it.isNotNullItem()) {
                            if (it.template.type == 36) {
                                Service.gI().sendTitle(this, it.template.part);
                            }
                        }
                    }
                    if (this.isPl() && isWin && this.zone.map.mapId == 51 && Util.canDoWithTime(lastTimeWin, 2000)) {
                        ChangeMapService.gI().changeMapBySpaceShip(this, 52, 0, -1);
                        isWin = false;
                    }
                    if (location.lastTimeplayerMove < System.currentTimeMillis() - 30 * 60 * 1000) {
                        Client.gI().kickSession(getSession());
                    }
                    if (Client.gI().getPlayer(this.name) != null) {
                            this.achievement.plusCount(8);
                    }
                } else {
                    if (Util.canDoWithTime(iDMark.getLastTimeBan(), 5000)) {
                        Client.gI().kickSession(session);
                    }
                }
            } catch (Exception e) {
                e.getStackTrace();
                Logger.logException(Player.class, e, "Lỗi tại player: " + this.name);
            }
        }
    }

    //--------------------------------------------------------------------------
    /*
     * {380, 381, 382}: ht lưỡng long nhất thể xayda trái đất
     * {383, 384, 385}: ht porata xayda trái đất
     * {391, 392, 393}: ht namếc
     * {870, 871, 872}: ht c2 trái đất
     * {873, 874, 875}: ht c2 namếc
     * {867, 878, 869}: ht c2 xayda
     */
    private static final short[][] idOutfitFusion = {
       /* {380, 381, 382}, {383, 384, 385}, {391, 392, 393},//luong long nhat the, va cap 1
        {870, 871, 872}, {873, 874, 875}, {867, 868, 869},//bt cap 2
        {630, 631, 632}, {633, 634, 635}, {627, 628, 629}, //bt cap 3
        {2036, 2037, 2038}, {2039, 3040, 2041}, {2042, 2043, 2044},//bt cap 4
        {1970, 868, 872}, {2051, 2052, 2053}, {1974, 1975, 629},//bt cap 5
    }; */
        {380, 381, 382}, {383, 384, 385}, {391, 392, 393},// btc1
        {870, 871, 872}, {873, 874, 875}, {867, 868, 869},//bt cap 2
        {1375, 1376, 1377}, {1372, 1373, 1374}, {1369, 1370, 1371}, //btc3
        {1516,1992,1993}, {1522,1995,1996}, {1519,1995,1996}, //btc4
        {1970, 868, 872}, {2051, 2052, 2053}, {1974, 1975, 629},//bt cap 5
        {1443, 1444, 1445},
    };

    // Sua id vat pham muon co aura lai
    public byte getAura() {
        if (this.inventory.itemsBody.isEmpty() || this.inventory.itemsBody.size() < 10) {
            return -1;
        }
        if (this.isPl()&&this.fusion.typeFusion == ConstPlayer.HOP_THE_GOGETA) {
            return 22;
        }
         if (this.isPl()&&this.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4 && this.gender==2) {
            return 30;
        }
          if (this.isPl()&&this.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4 && this.gender==0) {
            return 29;
        }
            if (this.isPl()&&this.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4 && this.gender==1) {
            return 31;
        }
        Item item = this.inventory.itemsBody.get(5);
        if (!item.isNotNullItem()) {
            return -1;
        }
        else if (item.template.id == 1121) {
            return 10;
        }else if (item.template.id == 1125) {
            return 14;
        } else if (item.template.id == 2092) {
            return 13;
        } else if (item.template.id == 1432) {
            return 38;
        } else if (item.template.id == 1433) {
            return 37;
        } else if (item.template.id == 1450) {
            return 29;
        } else if (item.template.id == 1451) {
            return 30;
        } else if (item.template.id == 1452) {
            return 31;
        } else if (item.template.id == 1468) {
            return 38;
        } else if (item.template.id == 1469) {
            return 37;
        }  else if (item.template.id == 1472) {
            return 41;
        }  else if (item.template.id == 1477) {
            return 29;
        }  else {
          return -1;  
        }
        

    }


    // hieu ung theo set
    public byte getEffFront() {
        if (this.inventory.itemsBody.isEmpty() || this.inventory.itemsBody.size() < 10) {
            return -1;
        }
        int levelAo = 0;
        Item.ItemOption optionLevelAo = null;
        int levelQuan = 0;
        Item.ItemOption optionLevelQuan = null;
        int levelGang = 0;
        Item.ItemOption optionLevelGang = null;
        int levelGiay = 0;
        Item.ItemOption optionLevelGiay = null;
        int levelNhan = 0;
        Item.ItemOption optionLevelNhan = null;
        Item itemAo = this.inventory.itemsBody.get(0);
        Item itemQuan = this.inventory.itemsBody.get(1);
        Item itemGang = this.inventory.itemsBody.get(2);
        Item itemGiay = this.inventory.itemsBody.get(3);
        Item itemNhan = this.inventory.itemsBody.get(4);
        for (Item.ItemOption io : itemAo.itemOptions) {
            if (io.optionTemplate.id == 72) {
                levelAo = io.param;
                optionLevelAo = io;
                break;
            }
        }
        for (Item.ItemOption io : itemQuan.itemOptions) {
            if (io.optionTemplate.id == 72) {
                levelQuan = io.param;
                optionLevelQuan = io;
                break;
            }
        }
        for (Item.ItemOption io : itemGang.itemOptions) {
            if (io.optionTemplate.id == 72) {
                levelGang = io.param;
                optionLevelGang = io;
                break;
            }
        }
        for (Item.ItemOption io : itemGiay.itemOptions) {
            if (io.optionTemplate.id == 72) {
                levelGiay = io.param;
                optionLevelGiay = io;
                break;
            }
        }
        for (Item.ItemOption io : itemNhan.itemOptions) {
            if (io.optionTemplate.id == 72) {
                levelNhan = io.param;
                optionLevelNhan = io;
                break;
            }
        }
        if (optionLevelAo != null && optionLevelQuan != null && optionLevelGang != null && optionLevelGiay != null && optionLevelNhan != null
                && levelAo >= 8 && levelQuan >= 8 && levelGang >= 8 && levelGiay >= 8 && levelNhan >= 8) {
            return 8;
        } else if (optionLevelAo != null && optionLevelQuan != null && optionLevelGang != null && optionLevelGiay != null && optionLevelNhan != null
                && levelAo >= 7 && levelQuan >= 7 && levelGang >= 7 && levelGiay >= 7 && levelNhan >= 7) {
            return 7;
        } else if (optionLevelAo != null && optionLevelQuan != null && optionLevelGang != null && optionLevelGiay != null && optionLevelNhan != null
                && levelAo >= 6 && levelQuan >= 6 && levelGang >= 6 && levelGiay >= 6 && levelNhan >= 6) {
            return 6;
        } else if (optionLevelAo != null && optionLevelQuan != null && optionLevelGang != null && optionLevelGiay != null && optionLevelNhan != null
                && levelAo >= 5 && levelQuan >= 5 && levelGang >= 5 && levelGiay >= 5 && levelNhan >= 5) {
            return 5;
        } else if (optionLevelAo != null && optionLevelQuan != null && optionLevelGang != null && optionLevelGiay != null && optionLevelNhan != null
                && levelAo >= 4 && levelQuan >= 4 && levelGang >= 4 && levelGiay >= 4 && levelNhan >= 4) {
            return 4;
        } else {
            return -1;
        }
    }

    public short getHead() {
        if (effectSkill != null && effectSkill.isMonkey) {
            return (short) ConstPlayer.HEADMONKEY[effectSkill.levelMonkey - 1];
        } else if (effectSkill != null && effectSkill.isCaiBinhChua) {
            return 1988;
        } else if (effectSkill != null && effectSkill.isSocola) {
            return 412;
        } else if (fusion != null && fusion.typeFusion != ConstPlayer.NON_FUSION) {
            if (fusion.typeFusion == ConstPlayer.LUONG_LONG_NHAT_THE) {
                return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 0][0];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA) {
                return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 1][0];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
                return idOutfitFusion[3 + this.gender][0];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
                return idOutfitFusion[6 + this.gender][0];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
                return idOutfitFusion[9 + this.gender][0];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5) {
                return idOutfitFusion[12 + this.gender][0];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_GOGETA) {
                return idOutfitFusion[15][0];
            }
        } else if (inventory != null && inventory.itemsBody.get(5).isNotNullItem()) {
            int head = inventory.itemsBody.get(5).template.head;
            if (head != -1) {
                return (short) head;
            }
        }
        return this.head;
    }

    public short getBody() {
        if (effectSkill != null && effectSkill.isMonkey) {
            return 193;
        } else if (effectSkill != null && effectSkill.isCaiBinhChua) {
            return 1989;
        } else if (effectSkill != null && effectSkill.isSocola) {
            return 413;
        } else if (fusion != null && fusion.typeFusion != ConstPlayer.NON_FUSION) {
            if (fusion.typeFusion == ConstPlayer.LUONG_LONG_NHAT_THE) {
                return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 0][1];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA) {
                return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 1][1];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
                return idOutfitFusion[3 + this.gender][1];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
                return idOutfitFusion[6 + this.gender][1];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
                return idOutfitFusion[9 + this.gender][1];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5) {
                return idOutfitFusion[12 + this.gender][1];
            }  else if (fusion.typeFusion == ConstPlayer.HOP_THE_GOGETA) {
                return idOutfitFusion[15][1];
            }
        } else if (inventory != null && inventory.itemsBody.get(5).isNotNullItem()) {
            int body = inventory.itemsBody.get(5).template.body;
            if (body != -1) {
                return (short) body;
            }
        }
        if (inventory != null && inventory.itemsBody.get(0).isNotNullItem()) {
            return inventory.itemsBody.get(0).template.part;
        }
        return (short) (gender == ConstPlayer.NAMEC ? 59 : 57);
    }

    public short getLeg() {
        if (effectSkill != null && effectSkill.isMonkey) {
            return 194;
        } else if (effectSkill != null && effectSkill.isCaiBinhChua) {
            return 1990;
        } else if (effectSkill != null && effectSkill.isSocola) {
            return 414;
        } else if (fusion != null && fusion.typeFusion != ConstPlayer.NON_FUSION) {
            if (fusion.typeFusion == ConstPlayer.LUONG_LONG_NHAT_THE) {
                return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 0][2];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA) {
                return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 1][2];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
                return idOutfitFusion[3 + this.gender][2];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
                return idOutfitFusion[6 + this.gender][2];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
                return idOutfitFusion[9 + this.gender][2];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5) {
                return idOutfitFusion[12 + this.gender][2];
            }  else if (fusion.typeFusion == ConstPlayer.HOP_THE_GOGETA) {
                return idOutfitFusion[15][2];
            }
        } else if (inventory != null && inventory.itemsBody.get(5).isNotNullItem()) {
            int leg = inventory.itemsBody.get(5).template.leg;
            if (leg != -1) {
                return (short) leg;
            }
        }
        if (inventory != null && inventory.itemsBody.get(1).isNotNullItem()) {
            return inventory.itemsBody.get(1).template.part;
        }
        return (short) (gender == 1 ? 60 : 58);
    }

    public short getFlagBag() {
        if (this.iDMark.isHoldBlackBall()) {
            return 31;
        } else if (this.idNRNM >= 353 && this.idNRNM <= 359) {
            return 30;
        }
        //if(this.isPl()&&this.fusion.typeFusion==ConstPlayer.HOP_THE_GOGETA)return 108;
        if (this.isPl() && !(this instanceof BossDHVT) && !(this instanceof Referee) && !(this instanceof Referee1)) {
            if (this.inventory.itemsBody.get(8).isNotNullItem()) {
                return this.inventory.itemsBody.get(8).template.part;
            }
        }
        if (this.isPet) {
            if (this.inventory.itemsBody.get(7).isNotNullItem()) {
                return this.inventory.itemsBody.get(7).template.part;
            }
        }
        if (TaskService.gI().getIdTask(this) == ConstTask.TASK_3_2) {
            return 28;
        }
        if (this.clan != null) {
            return (short) this.clan.imgId;
        }
        return -1;
    }

    public short getMount() {
        if (this.inventory.itemsBody.isEmpty() || this.inventory.itemsBody.size() < 10) {
            return -1;
        }
        Item item = this.inventory.itemsBody.get(9);
        if (!item.isNotNullItem()) {
            return -1;
        }
        if (this.isPet) {
            if (this.inventory.itemsBody.get(9).isNotNullItem()) {
                return this.inventory.itemsBody.get(9).template.part;
            }
        }
        if (item.template.type == 24) {
            if (item.template.gender == 3 || item.template.gender == this.gender) {
                return item.template.id;
            } else {
                return -1;
            }
        } else {
            if (item.template.id < 500) {
                return item.template.id;
            } else {
                return (short) DataGame.MAP_MOUNT_NUM.get(String.valueOf(item.template.id));
            }
        }

    }

    //--------------------------------------------------------------------------
     public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (plAtt != null) {
                switch (plAtt.playerSkill.skillSelect.template.id) {
                    case Skill.KAMEJOKO:
                    case Skill.MASENKO:
                    case Skill.ANTOMIC:
                        if (this.nPoint.voHieuChuong > 0) {
                            ServerData.Services.PlayerService.gI().hoiPhuc(this, 0, (long)(damage * this.nPoint.voHieuChuong / 100));
                            return 0;
                        }
                }
            }
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 100)) {
                return 0;
            }
            damage = this.nPoint.subDameInjureWithDeff(damage);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = 1;
            }
            if (isMobAttack && this.charms.tdBatTu > System.currentTimeMillis() && damage >= this.nPoint.hp) {
                damage = this.nPoint.hp - 1;
            }
            this.nPoint.subHP(damage);
            if (isDie()) {
                setDie(plAtt);
            }
            return damage;
        } else {
            return 0;
        }
    }

    public void setDie(Player plAtt) {
        //xóa phù
        if (this.effectSkin.xHPKI > 1) {
            this.effectSkin.xHPKI = 1;
            Service.gI().point(this);
        }
        //xóa tụ skill đặc biệt
        this.playerSkill.prepareQCKK = false;
        this.playerSkill.prepareLaze = false;
        this.playerSkill.prepareTuSat = false;
        //xóa hiệu ứng skill
        this.effectSkill.removeSkillEffectWhenDie();
        //
        nPoint.setHp(0);
        nPoint.setMp(0);
        //xóa trứng
        if (this.mobMe != null) {
            this.mobMe.mobMeDie();
        }
        Service.gI().charDie(this);
        //add kẻ thù
        if (!this.isPet && !this.isNewPet && !this.isNewPet1 && !this.isBoss && plAtt != null && !plAtt.isPet && !plAtt.isNewPet && !plAtt.isNewPet1 && !plAtt.isBoss) {
            if (!plAtt.itemTime.isUseAnDanh) {
                FriendAndEnemyService.gI().addEnemy(this, plAtt);
            }
        }
        //kết thúc pk
        if (this.pvp != null) {
            this.pvp.lose(this, TYPE_LOSE_PVP.DEAD);
        }
        if(plAtt != null && this.isBoss && this.zone.map.mapId != 129 && this.zone.map.mapId != 140) {
            byte diemsb = (byte) Util.nextInt(1,3);
            plAtt.pointSb += diemsb;
            Service.getInstance().sendThongBao(plAtt, "|7|Tiêu diệt được\n|2|"+this.name+"\n|7|+"+diemsb+ " điểm Săn Boss");
        }
        if (this.zone.map.mapId == 196) {
            PlayerService.gI().lthsmapvodai = System.currentTimeMillis();
            if(plAtt != null && this.isPl()) {
            plAtt.pointPvp++;
            Service.getInstance().sendThongBao(plAtt, "|2|Đã đánh bại "+this.name+"\n|2|+1 điểm PvP");
            }
        }
        NgocRongSaoDen.gI().dropBlackBall(this);
    }

    //--------------------------------------------------------------------------
    public void setClanMember() {
        if (this.clanMember != null) {
            this.clanMember.powerPoint = this.nPoint.power;
            this.clanMember.head = this.getHead();
            this.clanMember.body = this.getBody();
            this.clanMember.leg = this.getLeg();
        }
    }

    public boolean isAdmin() {
        return this.session.isAdmin;
    }
    public boolean isAdmin2() {
        return this.session.isAdmin2;
    }
     public boolean isJail() {
        return this.session.isJail;
    }
    public void setJustRevivaled() {
        this.justRevived = true;
        this.lastTimeRevived = System.currentTimeMillis();
    }

    public void dispose() {
        if (pet != null) {
            pet.dispose();
            pet = null;
        }
        if (newpet != null) {
            newpet.dispose();
            newpet = null;
        }
        if (newpet1 != null) {
            newpet1.dispose();
            newpet1 = null;
        }
        if (mapBlackBall != null) {
            mapBlackBall.clear();
            mapBlackBall = null;
        }
        zone = null;
        mapBeforeCapsule = null;
        if (mapMaBu != null) {
            mapMaBu.clear();
            mapMaBu = null;
        }
        if (skillSpecial != null) {
            skillSpecial.dispose();
            skillSpecial = null;
        }
        if (billEgg != null) {
            billEgg.dispose();
            billEgg = null;
        }
        zone = null;
        mapBeforeCapsule = null;
        if (mapCapsule != null) {
            mapCapsule.clear();
            mapCapsule = null;
        }
        if (mobMe != null) {
            mobMe.dispose();
            mobMe = null;
        }
        location = null;
        if (setClothes != null) {
            setClothes.dispose();
            setClothes = null;
        }
        if (effectSkill != null) {
            effectSkill.dispose();
            effectSkill = null;
        }
        if (mabuEgg != null) {
            mabuEgg.dispose();
            mabuEgg = null;
        }
        if (playerTask != null) {
            playerTask.dispose();
            playerTask = null;
        }
        if (itemTime != null) {
            itemTime.dispose();
            itemTime = null;
        }
        if (fusion != null) {
            fusion.dispose();
            fusion = null;
        }
        if (magicTree != null) {
            magicTree.dispose();
            magicTree = null;
        }
        if (playerIntrinsic != null) {
            playerIntrinsic.dispose();
            playerIntrinsic = null;
        }
        if (inventory != null) {
            inventory.dispose();
            inventory = null;
        }
        if (playerSkill != null) {
            playerSkill.dispose();
            playerSkill = null;
        }
        if (combineNew != null) {
            combineNew.dispose();
            combineNew = null;
        }
        if (iDMark != null) {
            iDMark.dispose();
            iDMark = null;
        }
        if (charms != null) {
            charms.dispose();
            charms = null;
        }
        if (effectSkin != null) {
            effectSkin.dispose();
            effectSkin = null;
        }
        if (gift != null) {
            gift.dispose();
            gift = null;
        }
        if (nPoint != null) {
            nPoint.dispose();
            nPoint = null;
        }
        if (rewardBlackBall != null) {
            rewardBlackBall.dispose();
            rewardBlackBall = null;
        }
        if (effectFlagBag != null) {
            effectFlagBag.dispose();
            effectFlagBag = null;
        }
        if (pvp != null) {
            pvp.dispose();
            pvp = null;
        }
        effectFlagBag = null;
        clan = null;
        clanMember = null;
        friends = null;
        enemies = null;
        session = null;
        name = null;
    }
//    public String percentGold(int type) {
//        try {
//            if (type == 0) {
//                long percent = (this.goldNormar / ChonAiDay.gI().goldNormar) * 100;
//                return String.valueOf((percent));
//            } else if (type == 1) {
//                long percent = (this.goldVIP / ChonAiDay.gI().goldVip) * 100;
//                return String.valueOf((percent));
//            }
//        } catch (ArithmeticException e) {
//            return "0";
//        }
//        return "0";
//    }
    //cho bot
    public Mob mobTarget;
    
    public long lastTimeTargetMob;
    
    public long timeTargetMob;
    
    public long lastTimeAttack;
    
    public void moveTo(int x, int y) {
        byte dir = (byte) (this.location.x - x < 0 ? 1 : -1);
        byte move = (byte) Util.nextInt(40, 60);
        if(isBot) move = (byte) (move*(byte)2);
        PlayerService.gI().playerMove(this, this.location.x + (dir == 1 ? move : -move), y + (Util.isTrue(3, 10) ? -50 : 0));
    }
    
    public Mob getMobAttack(){
        if (this.mobTarget != null && (this.mobTarget.isDie() || !this.zone.equals(this.mobTarget.zone))) {
            this.mobTarget = null;
        }
        if (this.mobTarget == null && Util.canDoWithTime(lastTimeTargetMob, timeTargetMob)) {
            this.mobTarget = this.zone.getRandomMobInMap();
            this.lastTimeTargetMob = System.currentTimeMillis();
            this.timeTargetMob = 500;
        }
        return this.mobTarget;
    }
    
    public void active(){
        if(this.isBot){
            if(this.isDie()) this.nPoint.hp = this.nPoint.hpMax;
            if(this.nPoint.mp<=0) this.nPoint.mp = this.nPoint.mpMax;
            this.attack();
        }
    }
    
    public int getRangeCanAttackWithSkillSelect() {
        int skillId = this.playerSkill.skillSelect.template.id;
        if (skillId == Skill.KAMEJOKO || skillId == Skill.MASENKO || skillId == Skill.ANTOMIC) {
            return Skill.RANGE_ATTACK_CHIEU_CHUONG;
        } else if (skillId == Skill.DRAGON || skillId == Skill.DEMON || skillId == Skill.GALICK) {
            return Skill.RANGE_ATTACK_CHIEU_DAM;
        }
        return 752002;
    }
    
    public void attack(){
        if(this.isBot){
            //this.mobTarget = this.getMobAttack();
            if(Util.canDoWithTime(lastTimeAttack,100) && this.mobTarget != null){
                this.lastTimeAttack = System.currentTimeMillis();
                try{
                    Mob m = this.getMobAttack();
                    if(m == null || m.isDie()) return;
                    this.playerSkill.skillSelect = this.playerSkill.skills.get(Util.nextInt(0, this.playerSkill.skills.size() - 1));
                    //System.out.println(m.name);
                    if(Util.nextInt(100)<70){
                        this.playerSkill.skillSelect = this.playerSkill.skills.get(0);
                    }
                    if (Util.getDistance(this, m) <= this.getRangeCanAttackWithSkillSelect()) {
                        if (Util.isTrue(5, 20)) {
                            if (SkillUtil.isUseSkillChuong(this)) {
                                this.moveTo(m.location.x + (Util.getOne(-1, 1) * Util.nextInt(20, 200)),
                                        Util.nextInt(10) % 2 == 0 ? m.location.y : m.location.y );
                            } else {
                                this.moveTo(m.location.x + (Util.getOne(-1, 1) * Util.nextInt(10, 40)),
                                        Util.nextInt(10) % 2 == 0 ? m.location.y : m.location.y );
                            }
                        }   
                        SkillService.gI().useSkill(this, null, m,null);
                    }else {
                        this.moveTo(m.location.x, m.location.y);
                }
                }catch(Exception e){
                }
            }else{
                this.mobTarget = getMobAttack();
            }
        }
    }
}
