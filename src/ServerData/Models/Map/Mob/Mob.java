package ServerData.Models.Map.Mob;

import Server.Data.Consts.ConstEvent;
import ServerData.Models.Item.ItemMobReward;
import ServerData.Services.InventoryServiceNew;
import ServerData.Services.TaskService;
import ServerData.Services.Service;
import ServerData.Services.ItemMapService;
import ServerData.Services.MapService;
import Server.Data.Consts.ConstMap;
import Server.Data.Consts.ConstMob;
import Server.Data.Consts.ConstPlayer;
import Server.Data.Consts.ConstTask;

import java.util.List;

import ServerData.Models.Map.Zone;
import ServerData.Models.Player.Location;
import ServerData.Models.Player.Pet;
import ServerData.Models.Player.Player;
import com.girlkun.network.io.Message;
import ServerData.Server.Maintenance;
import ServerData.Server.Manager;
import ServerData.Utils.Util;
import ServerData.Models.Item.Item;
import ServerData.Models.Map.ItemMap;
import ServerData.Services.ItemService;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class Mob {

    public int id;
    public Zone zone;
    public int tempId;
    public String name;
    public byte level;

    public MobPoint point;
    public MobEffectSkill effectSkill;
    public Location location;

    public byte pDame;
    public int pTiemNang;
    private long maxTiemNang;

    public long lastTimeDie;
    public int lvMob = 0;
    public int status = 5;

    public Mob(Mob mob) {
        this.point = new MobPoint(this);
        this.effectSkill = new MobEffectSkill(this);
        this.location = new Location();
        this.id = mob.id;
        this.tempId = mob.tempId;
        this.level = mob.level;
        this.point.setHpFull(mob.point.getHpFull());
        this.point.sethp(this.point.getHpFull());
        this.location.x = mob.location.x;
        this.location.y = mob.location.y;
        this.pDame = mob.pDame;
        this.pTiemNang = mob.pTiemNang;
        this.setTiemNang();
    }

    public Mob() {
        this.point = new MobPoint(this);
        this.effectSkill = new MobEffectSkill(this);
        this.location = new Location();
    }

    public void setTiemNang() {
        this.maxTiemNang = this.point.getHpFull() * (this.pTiemNang + Util.nextInt(-2, 2)) / 100;
    }

    public static void initMobBanDoKhoBau(Mob mob, byte level) {
        if (level <= 700) {
            mob.point.dame = (level * 3250 * mob.level * 4) * 5;
            mob.point.maxHp = (level * 12472 * mob.level * 2 + level * 7263 * mob.tempId) * 5;
        }
        if (level > 700 && level <= 10000) {
            mob.point.dame = (level * 3250 * mob.level * 4) * 5;
            mob.point.maxHp = 2100000000;
        }
        if (level > 10000) {
            mob.point.dame = 2000000000;
            mob.point.maxHp = 2100000000;
        }
    }

    public static void initMobKhiGaHuyDiet(Mob mob, byte level) {
        if (level <= 700) {
            mob.point.dame = (level * 3250 * mob.level * 4) * 5;
            mob.point.maxHp = (level * 12472 * mob.level * 2 + level * 7263 * mob.tempId) * 5;
        }
        if (level > 700 && level <= 10000) {
            mob.point.dame = (level * 3250 * mob.level * 4) * 5;
            mob.point.maxHp = 2100000000;
        }
        if (level > 10000) {
            mob.point.dame = 2000000000;
            mob.point.maxHp = 2100000000;
        }
    }

    public static void initMobConDuongRanDoc(Mob mob, int level) {
        if (level <= 700) {
            mob.point.dame = (level * 3250 * mob.level * 4) * 5;
            mob.point.maxHp = (level * 12472 * mob.level * 2 + level * 7263 * mob.tempId) * 5;
        }
        if (level > 700 && level <= 10000) {
            mob.point.dame = (level * 3250 * mob.level * 4) * 5;
            mob.point.maxHp = 2100000000;
        }
        if (level > 10000) {
            mob.point.dame = 2000000000;
            mob.point.maxHp = 2100000000;
        }
    }

    public static void initMopbKhiGas(Mob mob, int level) {
        if (level <= 700) {
            mob.point.dame = (level * 3250 * mob.level * 4) * 5;
            mob.point.maxHp = (level * 12472 * mob.level * 2 + level * 7263 * mob.tempId) * 5;
        }
        if (level > 700 && level <= 10000) {
            mob.point.dame = (level * 3250 * mob.level * 4) * 5;
            mob.point.maxHp = 2100000000;
        }
        if (level > 10000) {
            mob.point.dame = 2000000000;
            mob.point.maxHp = 2100000000;
        }
    }

    public static void hoiSinhMob(Mob mob) {
        mob.point.hp = mob.point.maxHp;
        mob.setTiemNang();
        Message msg;
        try {
            msg = new Message(-13);
            msg.writer().writeByte(mob.id);
            msg.writer().writeByte(mob.tempId);
            msg.writer().writeByte(0); //level mob
            msg.writer().writeInt(Util.TamkjllGH(mob.point.hp));
            Service.getInstance().sendMessAllPlayerInMap(mob.zone, msg);
            msg.cleanup();
        } catch (IOException e) {
        }
    }

    private long lastTimeAttackPlayer;

    public boolean isDie() {
        return this.point.gethp() <= 0;
    }

    public synchronized void injured(Player plAtt, double damage, boolean dieWhenHpFull) {
        if (!this.isDie()) {
            if (damage >= this.point.hp) {
                damage = this.point.hp;
            }
            if (!dieWhenHpFull) {
                if (this.point.hp == this.point.maxHp && damage >= this.point.hp) {
                    damage = this.point.hp - 1;
                }
                if (this.tempId == 0 && damage > 10) {
                    damage = 10;
                }
            }
            this.point.hp -= damage;
            if (this.isDie()) {
                this.status = 0;
                this.sendMobDieAffterAttacked(plAtt, Util.TamkjllGH(damage));
                TaskService.gI().checkDoneTaskKillMob(plAtt, this);
                TaskService.gI().checkDoneSideTaskKillMob(plAtt, this);
                this.lastTimeDie = System.currentTimeMillis();
            } else {
                this.sendMobStillAliveAffterAttacked(Util.TamkjllGH(damage), plAtt != null ? plAtt.nPoint.isCrit : false);
            }
            if (plAtt != null) {
                Service.getInstance().addSMTN(plAtt, (byte) 2, getTiemNangForPlayer(plAtt, Util.TamkjllGH(damage)), true);
            }
        }
    }

    public long getTiemNangForPlayer(Player pl, long dame) {
        int levelPlayer = Service.gI().getCurrLevel(pl);
        int n = levelPlayer - this.level;
        long pDameHit = dame * 100 / point.getHpFull();
        long tiemNang = pDameHit * maxTiemNang / 100;
        if (tiemNang <= 0) {
            tiemNang = 1;
        }
        if (n >= 0) {
            for (int i = 0; i < n; i++) {
                long sub = tiemNang * 10 / 100;
                if (sub <= 0) {
                    sub = 1;
                }
                tiemNang -= sub;
            }
        } else {
            for (int i = 0; i < -n; i++) {
                long add = tiemNang * 10 / 100;
                if (add <= 0) {
                    add = 1;
                }
                tiemNang += add;
            }
        }
        if (tiemNang <= 0) {
            tiemNang = 1;
        }
        tiemNang = (int) pl.nPoint.calSucManhTiemNang(tiemNang);
        if (pl.zone.map.mapId == 122 || pl.zone.map.mapId == 111 || pl.zone.map.mapId == 124) {
            tiemNang *= 30;
        }
      

        return tiemNang;
    }

    public void update() {
//        if (this.tempId == 71) {
//            try {
//                Message msg = new Message(102);
//                msg.writer().writeByte(5);
//                msg.writer().writeShort(this.zone.getPlayers().get(0).location.x);
//                Service.gI().sendMessAllPlayerInMap(zone, msg);
//                msg.cleanup();
//            } catch (Exception e) {
//            }
//        }

        if (this.isDie() && !Maintenance.isRuning) {
            switch (zone.map.type) {
                case ConstMap.MAP_DOANH_TRAI:
                    break;
                case ConstMap.MAP_BAN_DO_KHO_BAU:
                    if (this.tempId == 72 || this.tempId == 71) {//ro bot bao ve
                        if (System.currentTimeMillis() - this.lastTimeDie > 3000) {
                            try {
                                Message t = new Message(102);
                                t.writer().writeByte((this.tempId == 71 ? 7 : 6));
                                Service.getInstance().sendMessAllPlayerInMap(this.zone, t);
                                t.cleanup();
                            } catch (IOException e) {
                                System.out.println("loi up date");
                            }
                        }
                    }
                    break;
                case ConstMap.MAP_KHI_GA_HUY_DIET:
                    break;
                default:
                    if (Util.canDoWithTime(lastTimeDie, 5000)) {
                        this.randomSieuQuai();
                        this.hoiSinh();
                        this.sendMobHoiSinh();
                    }


            }
        }
        effectSkill.update();

        attackPlayer();

    }

    public void randomSieuQuai() {
        if (this.tempId != 0 && MapService.gI().isMapKhongCoSieuQuai(this.zone.map.mapId) && Util.nextInt(0, 150) < 1) {
            this.lvMob = 1;
        }
    }

    private void attackPlayer() {
        if (!isDie() && !effectSkill.isHaveEffectSkill() && !(tempId == 0) && Util.canDoWithTime(lastTimeAttackPlayer, 2000)) {
            Player pl = getPlayerCanAttack();
            if (pl != null) {
//                MobService.gI().mobAttackPlayer(this, pl);
                this.mobAttackPlayer(pl);
            }
            this.lastTimeAttackPlayer = System.currentTimeMillis();
        }
    }

    private Player getPlayerCanAttack() {
        int distance = 150;
        Player plAttack = null;
        try {
            List<Player> players = this.zone.getNotBosses();
            for (Player pl : players) {
                if (!pl.isDie() && !pl.isBoss && !pl.effectSkin.isVoHinh) {
                    int dis = Util.getDistance(pl, this);
                    if (dis <= distance) {
                        plAttack = pl;
                        distance = dis;
                    }
                }
            }
        } catch (Exception e) {

        }
        return plAttack;
    }

    //**************************************************************************
    private void mobAttackPlayer(Player player) {
        float dameMob = this.point.getDameAttack();
        if (player.charms.tdDaTrau > System.currentTimeMillis()) {
            dameMob /= 2;
        }
        double dame = player.injured(null, dameMob, false, true);
        this.sendMobAttackMe(player, Util.TamkjllGH(dame));
        this.sendMobAttackPlayer(player);
    }

    private void sendMobAttackMe(Player player, int dame) {
        if (!player.isPet && !player.isNewPet) {
            Message msg;
            try {
                msg = new Message(-11);
                msg.writer().writeByte(this.id);
                msg.writer().writeInt(dame); //dame
                player.sendMessage(msg);
                msg.cleanup();
            } catch (Exception e) {
            }
        }
    }

    private void sendMobAttackPlayer(Player player) {
        Message msg;
        try {
            msg = new Message(-10);
            msg.writer().writeByte(this.id);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeInt(Util.TamkjllGH(player.nPoint.hp));
            Service.getInstance().sendMessAnotherNotMeInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void hoiSinh() {
        this.status = 5;
        this.point.hp = this.point.maxHp;
        this.setTiemNang();
    }

    public void sendMobHoiSinh() {
        Message msg;
        try {
            msg = new Message(-13);
            msg.writer().writeByte(this.id);
            msg.writer().writeByte(this.tempId);
            msg.writer().writeByte(lvMob);
            msg.writer().writeInt(Util.TamkjllGH(this.point.hp));
            Service.getInstance().sendMessAllPlayerInMap(this.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    //**************************************************************************
    private void sendMobDieAffterAttacked(Player plKill, int dameHit) {
        Message msg;
        try {
            msg = new Message(-12);
            msg.writer().writeByte(this.id);
            msg.writer().writeInt(dameHit);
            msg.writer().writeBoolean(plKill.nPoint.isCrit); // crit
            List<ItemMap> items = mobReward(plKill, this.dropItemTask(plKill), msg);
            Service.getInstance().sendMessAllPlayerInMap(this.zone, msg);
            msg.cleanup();
            hutItem(plKill, items);
        } catch (Exception e) {
        }
    }

    private void hutItem(Player player, List<ItemMap> items) {
        if (!player.isPet && !player.isNewPet) {
            if (player.charms.tdThuHut > System.currentTimeMillis()) {
                for (ItemMap item : items) {
                    if (item.itemTemplate.id != 590) {
                        ItemMapService.gI().pickItem(player, item.itemMapId, true);
                    }
                }
            }
        } else {
            if (((Pet) player).master.charms.tdThuHut > System.currentTimeMillis()) {
                for (ItemMap item : items) {
                    if (item.itemTemplate.id != 590) {
                        ItemMapService.gI().pickItem(((Pet) player).master, item.itemMapId, true);
                    }
                }
            }
        }
    }

    private List<ItemMap> mobReward(Player player, ItemMap itemTask, Message msg) {
//        nplayer
        List<ItemMap> itemReward = new ArrayList<>();
        try {
            if ((!player.isPet && player.getSession().actived == 1 )
                    || (player.isPet && ((Pet) player).master.getSession().actived == 1 )) {
                if (this.zone.map.mapId >= 105 && this.zone.map.mapId <= 110) {
                    int random = Util.nextInt(1, 2);
                    if (Util.isTrue(20, 100)) {
                        Item i = Manager.THUCAN_REWARDS.get(Util.nextInt(0, Manager.THUCAN_REWARDS.size()));
                        i.quantity = random;
                        InventoryServiceNew.gI().addItemBag(player, i);
                        InventoryServiceNew.gI().sendItemBags(player);
                        Service.gI().sendThongBao(player, "Bạn vừa nhận được " + random + " thức ăn ngon nha!");
                    }
                }
            }
            /*Set huỷ diệt up mảnh thiên sứ*/
            if ((!player.isPet && player.getSession().actived == 1 )
                    || (player.isPet && ((Pet) player).master.getSession().actived == 1 )) {
                if (this.zone.map.mapId == 155) {
                    byte mts = 1;
                    if (Util.isTrue(5, 100)) {
                        Item i = Manager.MANH_THIENSU.get(Util.nextInt(0, Manager.MANH_THIENSU.size() - 1));
                        i.quantity = mts;
                        InventoryServiceNew.gI().addItemBag(player, i);
                        InventoryServiceNew.gI().sendItemBags(player);
                        Service.gI().sendThongBao(player, "Bạn vừa nhận được " + mts + " mảnh thiên sứ ngon nha!");
                    }
                }
            }
            itemReward = this.getItemMobReward(player, this.location.x + Util.nextInt(-10, 10),
                    this.zone.map.yPhysicInTop(this.location.x, this.location.y));
            if (itemTask != null) {
                itemReward.add(itemTask);
            }
            msg.writer().writeByte(itemReward.size()); //sl item roi
            for (ItemMap itemMap : itemReward) {
                msg.writer().writeShort(itemMap.itemMapId);// itemmapid
                msg.writer().writeShort(itemMap.itemTemplate.id); // id item
                msg.writer().writeShort(itemMap.x); // xend item
                msg.writer().writeShort(itemMap.y); // yend item
                msg.writer().writeInt((int) itemMap.playerId); // id nhan nat
            }
        } catch (Exception e) {
        }
        return itemReward;
    }

    public int getTilevip(Player pl) {
        switch (pl.vip) {
            case 2:
                return 5;
            case 3:
                return 10;
            case 4:
                return 15;
        }
        return 0;
    }

    public List<ItemMap> getItemMobReward(Player player, int x, int yEnd) {
        List<ItemMap> list = new ArrayList<>();
        MobReward mobReward = Manager.MOB_REWARDS.get(this.tempId);
        if (mobReward == null) {
            return list;
        }
        final Calendar rightNow = Calendar.getInstance();
        int hour = rightNow.get(11);

        List<ItemMobReward> items = mobReward.getItemReward();
        List<ItemMobReward> golds = mobReward.getGoldReward();
        if (!items.isEmpty()) {
            ItemMobReward item = items.get(Util.nextInt(0, items.size() - 1));
            ItemMap itemMap = item.getItemMap(zone, player, x, yEnd);
            if (itemMap != null) {
                list.add(itemMap);
            }
        }
        if (!golds.isEmpty()) {
            ItemMobReward gold = golds.get(Util.nextInt(0, golds.size() - 1));
            ItemMap itemMap = gold.getItemMap(zone, player, x, yEnd);
            if (itemMap != null) {
                list.add(itemMap);
            }
        }
        // Rơi spl
          if (this.zone.map.mapId >= 0) {
            if (Util.isTrue(50, 100)) { // vàng từ quái
                list.add(new ItemMap(zone, 76, Util.nextInt(1000000, 5000000), x, player.location.y, player.id));
            } else if (Util.isTrue(30, 100)) { // spl
                list.add(new ItemMap(Util.spl(zone, Util.nextInt(441, 446), 1, x, this.location.y, player.id)));
            } else if (Util.isTrue(20, 150)) { // spl tnsm
                list.add(new ItemMap(Util.spl(zone, 447, 1, x, this.location.y, player.id)));
            } else if (Util.isTrue(20, 100)) { // dnc
                list.add(new ItemMap(Util.spl(zone, Util.nextInt(220, 224), 1, x, player.location.y, player.id)));
            } else if (Util.isTrue(5, 200)) { // nro
                list.add(new ItemMap(zone, Util.nextInt(18, 20), 1, x, player.location.y, player.id));
            } else if (Util.isTrue(1, 500)) { // nro
                list.add(new ItemMap(zone, 17, 1, x, player.location.y, player.id));
            } else if (Util.isTrue(3, 100)) { // nro
                list.add(new ItemMap(zone, 1385, 1, x, player.location.y, player.id));
            } else if (Util.isTrue(5, 1000)) { // nro
                list.add(new ItemMap(zone, 457, 1, x, player.location.y, player.id));
            }

        }
        if (this.zone.map.mapId == 15 || this.zone.map.mapId == 16 || this.zone.map.mapId == 1 || this.zone.map.mapId == 2 || this.zone.map.mapId == 8 || this.zone.map.mapId == 9) {
            if (player.gender == 0) {

                if (Util.isTrue(2, 100)) { // dnc
                    list.add(new ItemMap(Util.RaitiDoAOSKH(zone, 0, 1, x, player.location.y, player.id)));
                }
                if (Util.isTrue(2, 100)) { // dnc
                    list.add(new ItemMap(Util.RaitiDoQUANSKH(zone, 6, 1, x, player.location.y, player.id)));
                }
                if (Util.isTrue(2, 100)) { // dnc
                    list.add(new ItemMap(Util.RaitiDoGANGSKH(zone, 21, 1, x, player.location.y, player.id)));
                }
                if (Util.isTrue(2, 100)) { // dnc
                    list.add(new ItemMap(Util.RaitiDoGIAYSKH(zone, 27, 1, x, player.location.y, player.id)));
                }
                if (Util.isTrue(1, 100)) { // dnc
                    list.add(new ItemMap(Util.RaitiDoRADASKHTD(zone, 12, 1, x, player.location.y, player.id)));
                }
            } else if (player.gender == 1) {

                if (Util.isTrue(2, 100)) { // dnc
                    list.add(new ItemMap(Util.RaitiDoAOSKH(zone, 1, 1, x, player.location.y, player.id)));
                }
                if (Util.isTrue(2, 100)) { // dnc
                    list.add(new ItemMap(Util.RaitiDoQUANSKH(zone, 7, 1, x, player.location.y, player.id)));
                }
                if (Util.isTrue(2, 100)) { // dnc
                    list.add(new ItemMap(Util.RaitiDoGANGSKH(zone, 22, 1, x, player.location.y, player.id)));
                }
                if (Util.isTrue(2, 100)) { // dnc
                    list.add(new ItemMap(Util.RaitiDoGIAYSKH(zone, 28, 1, x, player.location.y, player.id)));
                }
                if (Util.isTrue(1, 100)) { // dnc
                    list.add(new ItemMap(Util.RaitiDoRADASKHNM(zone, 12, 1, x, player.location.y, player.id)));
                }
            } else if (player.gender == 2) {

                if (Util.isTrue(2, 100)) { // dnc
                    list.add(new ItemMap(Util.RaitiDoAOSKH(zone, 2, 1, x, player.location.y, player.id)));
                }
                if (Util.isTrue(2, 100)) { // dnc
                    list.add(new ItemMap(Util.RaitiDoQUANSKH(zone, 8, 1, x, player.location.y, player.id)));
                }
                if (Util.isTrue(2, 100)) { // dnc
                    list.add(new ItemMap(Util.RaitiDoGANGSKH(zone, 23, 1, x, player.location.y, player.id)));
                }
                if (Util.isTrue(2, 100)) { // dnc
                    list.add(new ItemMap(Util.RaitiDoGIAYSKH(zone, 29, 1, x, player.location.y, player.id)));
                }
                if (Util.isTrue(1, 100)) { // dnc
                    list.add(new ItemMap(Util.RaitiDoRADASKHXD(zone, 12, 1, x, player.location.y, player.id)));
                }
            }
        }
        // capsule bang
        int nPlSameClan = 0;
        for (Player pl : player.zone.getPlayers()) {
            if (!pl.equals(player) && pl.clan != null) {
                nPlSameClan++;
            }
            if (MapService.gI().isMapUpTN(this.zone.map.mapId) && nPlSameClan >= 1 || MapService.gI().isMapUpTN2(this.zone.map.mapId) && player.clan != null && nPlSameClan >= 1) {
                if (Util.isTrue(1, 500)) { // hngoc
                    list.add(new ItemMap(zone, 457, 1, x, player.location.y, player.id));
                } else if (Util.isTrue(10, 100)) { // hngoc
                    list.add(new ItemMap(zone, 861, Util.nextInt(1, 5), x, player.location.y, player.id));
                } else if (Util.isTrue(10, 100)) { // cs bang
                    list.add(new ItemMap(zone, 2046, 1, x, player.location.y, player.id));
                }
            }
        }
//               if (this.zone.map.mapId >= 0){ 
//                    if (Util.isTrue(100, 100)){
//                        list.add(new ItemMap(Util.RaitiDoSKH(zone, 0, 1, x, this.location.y, player.id)));
//                    }
//                }
        /* -------------------------Item Up Su Kien-------------------------------- */
//        switch (ConstEvent.EVENT) {
//            case 1: // SK MÙA HÈ
//            Item quandibien = player.inventory.itemsBody.get(1);
//                if(quandibien.isNotNullItem()){
//                    if (quandibien.template.id == 691 || quandibien.template.id == 692 || quandibien.template.id == 693){
//                        if (this.zone.map.mapId == 29 || this.zone.map.mapId == 33){ 
//                            if (Util.isTrue(10, 100)){
//                                list.add(new ItemMap(Util.muahe(zone, Util.nextInt(695, 698), 1, x, this.location.y, player.id)));
//                            } else if (Util.isTrue(5, 150)){
//                                list.add(new ItemMap(Util.muahe(zone, 694, 1, x, this.location.y, player.id)));
//                            }
//                        }
//                    }
//                }
//            break;
//            case 2://SK HÙNG VƯƠNG
//            Item ctminuong = player.inventory.itemsBody.get(5);
//                if(ctminuong.isNotNullItem() && ctminuong.template.id == 860){
//                    if (this.zone.map.mapId >= 0){ 
//                        if (Util.isTrue(10, 100)){
//                            list.add(new ItemMap(Util.muahe(zone, 669, 1, x, this.location.y, player.id)));
//                        } else if (Util.isTrue(20, 100)){
//                            list.add(new ItemMap(Util.muahe(zone, 569, 1, x, this.location.y, player.id)));
//                        }
//                    }
//                }
//            break;
//            case 3:// SK TRUNG THU
//                if (this.zone.map.mapId >= 0){ 
//                    if (Util.isTrue(10, 100)){
//                        list.add(new ItemMap(Util.muahe(zone, Util.nextInt(1339,1341), 1, x, this.location.y, player.id)));
//                    }
//                }
//            break;
//            case 4:
//            break;
//            case 5:
//            break;
//            case 6:
//            break;
//            case 7:
//            Item quantet = player.inventory.itemsBody.get(1);
//                if(quantet.isNotNullItem() && quantet.template.id == 1436){
//                    if (player.zone.map.mapId >= 0) {
//                        if (Util.isTrue(10, 100)){
//                            list.add(new ItemMap(Util.muahe(zone, Util.nextInt(1437,1441), 1, x, this.location.y, player.id)));
//                        } else if (Util.isTrue(5, 100)){
//                            list.add(new ItemMap(Util.muahe(zone, Util.nextInt(1442,1444), 1, x, this.location.y, player.id)));
//                        }
//                    }
//                }
//            break;
//            case 8:
//            Item bohoa = player.inventory.itemsBody.get(8);
//                if(bohoa.isNotNullItem() && bohoa.template.id == 954 || bohoa.isNotNullItem() && bohoa.template.id == 955){
//                    if (player.zone.map.mapId >= 0) {
//                        if (Util.isTrue(10, 100)){
//                            list.add(new ItemMap(Util.muahe(zone, 589, 1, x, this.location.y, player.id)));
//                        }
//                    }
//                }
//            break;
//        }
        /* ----------------------------------------------------------------------------- */
        if (player.itemTime.isUseMayDo) {
           if (Util.isTrue(10, 100) && this.tempId > 57 && this.tempId < 66) {
                list.add(new ItemMap(zone, 380, 1, x, player.location.y, player.id));
            }
        }
//        if (player.itemTime.isUseMayDo2 && Util.isTrue(7, 100) && this.tempId > 1 && this.tempId < 81) {
//            list.add(new ItemMap(zone, 2036, 1, x, player.location.y, player.id));// cai nay sua sau nha
//        }
        if (player.zone.map.mapId >= 122 && this.zone.map.mapId <= 124 || player.zone.map.mapId >= 204 && this.zone.map.mapId <= 206) {
            if (Util.isTrue(5, 100)) {
                player.nhsPoint += 1;
            } else if (Util.isTrue(1, 100)) {
                Service.gI().dropItemMap(player.zone, Util.quadao(zone, 1536, 1, x, this.location.y, player.id));
            }
        }
        if (player.zone.map.mapId >= 156 && player.zone.map.mapId <= 159) {
            if (Util.isTrue(80, 100)) {
                list.add(new ItemMap(zone, 933, 1, x, this.location.y, player.id));
            } else if (Util.isTrue(20, 100)) {
                list.add(new ItemMap(zone, Util.nextInt(934, 935), 1, x, this.location.y, player.id));
            }
        }
        if (this.tempId > 0 && this.zone.map.mapId >= 156 && this.zone.map.mapId <= 159
                && player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
            if (Util.isTrue(10, 100)) { // up bí kíp
                list.add(new ItemMap(zone, 1427, 1, x, player.location.y, player.id));
            }
        }
        if (this.tempId > 0 && this.zone.map.mapId >= 156 && this.zone.map.mapId <= 159
                && player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
            if (Util.isTrue(10, 100)) { // up bí kíp
                list.add(new ItemMap(zone, 1428, 1, x, player.location.y, player.id));
            }
        }
//          if (this.tempId > 0 && this.zone.map.mapId >= 156 && this.zone.map.mapId <= 159
//                && player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
//            if (Util.isTrue(10, 100)) { // up bí kíp
//                list.add(new ItemMap(zone, 1424, 1, x, player.location.y, player.id));
//            }
//        }
        if (this.tempId > 0 && this.zone.map.mapId >= 156 && this.zone.map.mapId <= 159
                && player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
            if (Util.isTrue(1, 100)) { // đá hồn bt c3
                list.add(new ItemMap(zone, 1429, 1, x, player.location.y, player.id));
            }
        }
//         if (this.tempId > 0 && this.zone.map.mapId >= 156 && this.zone.map.mapId <= 159
//                && player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5) {
//            if (Util.isTrue(1, 100)) { // đá hồn bt c3
//                list.add(new ItemMap(zone, 1425, 1, x, player.location.y, player.id));
//            }
//        }
        if (this.tempId > 0 && this.zone.map.mapId >= 156 && this.zone.map.mapId <= 159
                && player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
            if (Util.isTrue(2, 100)) { // đá hồn bt c4
                list.add(new ItemMap(zone, 1429, 1, x, player.location.y, player.id));
            }
        }
        if (player.zone.map.mapId >= 105 && player.zone.map.mapId <= 110) {
//            if (player.id == 3067 && Util.isTrue(100, 100)) {
//                list.add(new ItemMap(zone, 673, 1, x, player.location.y, player.id));
//            }
            int[] listdoC12 = new int[]{233, 237, 241, 245, 249, 253, 257, 261, 265, 269, 273, 277};
            int randomlist = new Random().nextInt(listdoC12.length - 1);
            int[] itemDosTL = new int[]{555, 557, 559, 556, 558, 560, 562, 564, 566, 563, 565, 567};
            int randomDoTL = new Random().nextInt(itemDosTL.length - 1);
            if (Util.isTrue(1, 800)) {
                list.add(new ItemMap(Util.RaitiDoc12(zone, listdoC12[randomlist], 1, x, player.location.y, player.id)));
            } else if (Util.isTrue(1, 1200)) {
                list.add(new ItemMap(Util.UpDoTL(zone, itemDosTL[randomDoTL], 1, x, player.location.y, player.id)));
            }
        }
        return list;
    }

    private ItemMap dropItemTask(Player player) {
        ItemMap itemMap = null;
        switch (this.tempId) {
            case ConstMob.KHUNG_LONG:
            case ConstMob.LON_LOI:
            case ConstMob.QUY_DAT:
                if (TaskService.gI().getIdTask(player) == ConstTask.TASK_2_0) {
                    itemMap = new ItemMap(this.zone, 73, 1, this.location.x, this.location.y, player.id);
                }
                break;
        }
        if (itemMap != null) {
            return itemMap;
        }
        return null;
    }

    private void sendMobStillAliveAffterAttacked(int dameHit, boolean crit) {
        Message msg;
        try {
            msg = new Message(-9);
            msg.writer().writeByte(this.id);
            msg.writer().writeInt(Util.TamkjllGH(this.point.gethp()));
            msg.writer().writeInt(dameHit);
            msg.writer().writeBoolean(crit); // chí mạng
            msg.writer().writeInt(-1);
            Service.getInstance().sendMessAllPlayerInMap(this.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }
}
