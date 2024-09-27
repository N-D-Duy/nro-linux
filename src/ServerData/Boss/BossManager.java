package ServerData.Boss;

import ServerData.Boss.ListBoss.NHIEMVU.AndroidSaga_KingKong_NV;
import ServerData.Boss.ListBoss.NHIEMVU.AndroidSaga_S19_NV;
import ServerData.Boss.ListBoss.NHIEMVU.AndroidSaga_Poc_NV;
import ServerData.Boss.ListBoss.NHIEMVU.AndroidSaga_Pic_NV;
import ServerData.Boss.ListBoss.NHIEMVU.AndroidSaga_DrKore_NV;
import ServerData.Boss.ListBoss.NHIEMVU.AndroidSaga_SuperAdr17;
import ServerData.Boss.ListBoss.NHIEMVU.BlackSaga_Black_NV;
import ServerData.Boss.ListBoss.NHIEMVU.BlackSaga_SuperZamas;
import ServerData.Boss.ListBoss.NHIEMVU.BlackSaga_BlackGokuRose;
import ServerData.Boss.ListBoss.NHIEMVU.BlackSaga_SuperBlack;
import ServerData.Boss.ListBoss.NHIEMVU.BlackSaga_ZamasKaio_NV;
import ServerData.Boss.ListBoss.NHIEMVU.BlackSaga_BlackGoku;
import ServerData.Boss.ListBoss.Yardat;
import ServerData.Boss.ListBoss.NHIEMVU.FideSaga_Kuku_NV;
import ServerData.Boss.ListBoss.NHIEMVU.FideSaga_Rambo_NV;
import ServerData.Boss.ListBoss.NHIEMVU.FideSaga_MapDauDinh_NV;
import ServerData.Boss.ListBoss.NHIEMVU.AndroidSaga_S15_NV;
import ServerData.Boss.ListBoss.NHIEMVU.AndroidSaga_S14_NV;
import ServerData.Boss.ListBoss.NHIEMVU.AndroidSaga_S13_NV;
import Server.Connect.GodGK;
//import ServerData.Boss.ListBoss.AnTrom;
import ServerData.Boss.ListBoss.COLD.Cooler;
import ServerData.Boss.ListBoss.HAKAI.Champa;
import ServerData.Boss.ListBoss.HAKAI.ThanHuyDiet;
import ServerData.Boss.ListBoss.HAKAI.ThienSuWhis;
import ServerData.Boss.ListBoss.HAKAI.Vados;
import ServerData.Boss.ListBoss.HTNT.CoolerGold;
import ServerData.Boss.ListBoss.NHIEMVU.DRMSaga_Doraemon;
import ServerData.Boss.ListBoss.COLD.Kingcold;
import ServerData.Boss.ListBoss.Mabu;
import ServerData.Boss.ListBoss.SuperXen;
import ServerData.Boss.ListBoss.HTNT.Cumber;
import ServerData.Boss.ListBoss.HTNT.KamiOren;
import ServerData.Boss.ListBoss.NHIEMVU.CellSaga_Xencon_NV;
import ServerData.Boss.ListBoss.NHIEMVU.FideSaga_TDST_NV;
import ServerData.Boss.ListBoss.NHIEMVU.CellSaga_SieuBoHung_NV;
import ServerData.Boss.ListBoss.NHIEMVU.CellSaga_XenBoHung_NV;
//import ServerData.Boss.list_boss.Broly.Along;
//import ServerData.Boss.list_boss.Broly.BrolyClone;
import ServerData.Boss.ListBoss.NHIEMVU.DRMSaga_Nobita;
import ServerData.Boss.ListBoss.NHIEMVU.DRMSaga_Xeko;
import ServerData.Boss.ListBoss.NHIEMVU.DRMSaga_Xuka;
import ServerData.Boss.ListBoss.COLD.FideRobot;
import ServerData.Boss.ListBoss.HTNT.SongokuTaAc;
import ServerData.Boss.ListBoss.NHIEMVU.FideSaga_Fide_NV;
import ServerData.Boss.ListBoss.NHIEMVU.DRMSaga_Chaien;
import ServerData.Boss.ListBoss.NRSD.Rong1Sao;
import ServerData.Boss.ListBoss.NRSD.Rong2Sao;
import ServerData.Boss.ListBoss.NRSD.Rong3Sao;
import ServerData.Boss.ListBoss.NRSD.Rong4Sao;
import ServerData.Boss.ListBoss.NRSD.Rong5Sao;
import ServerData.Boss.ListBoss.NRSD.Rong6Sao;
import ServerData.Boss.ListBoss.NRSD.Rong7Sao;
import ServerData.Boss.ListBoss.MABU_12H.MabuBoss;
import ServerData.Boss.ListBoss.MABU_12H.BuiBui;
import ServerData.Boss.ListBoss.MABU_12H.BuiBui2;
import ServerData.Boss.ListBoss.MABU_12H.Drabura;
import ServerData.Boss.ListBoss.MABU_12H.Drabura2;
import ServerData.Boss.ListBoss.MABU_12H.Yacon;
import ServerData.Boss.ListBoss.Noel;
import ServerData.Boss.ListBoss.BROLY.Broly;
import ServerData.Boss.ListBoss.Sontinh;
import ServerData.Boss.ListBoss.Thuytinh;
import ServerData.Models.Map.ListMap.SieuHangService;
import ServerData.Models.Map.Zone;
import ServerData.Models.Player.Player;
import com.girlkun.network.io.Message;
import ServerData.Server.ServerManager;
import ServerData.Services.ItemMapService;
import ServerData.Services.MapService;
import ServerData.Services.Service;
import ServerData.Services.ChangeMapService;
import ServerData.Utils.Util;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

public class BossManager implements Runnable {

    private static BossManager I;
    public static final byte ratioReward = 2;

    public static BossManager gI() {
        if (BossManager.I == null) {
            BossManager.I = new BossManager();
        }
        return BossManager.I;
    }

    private BossManager() {
        this.bosses = new ArrayList<>();
    }

    public List<Boss> getBosses() {
        return this.bosses;
    }
    public boolean loadedBoss;
    public final List<Boss> bosses;

    public void addBoss(Boss boss) {
        this.bosses.add(boss);
    }

    public void removeBoss(Boss boss) {
        this.bosses.remove(boss);
    }

    public void loadBoss() {
        if (this.loadedBoss) {
            return;
        }
        try {
            this.createBoss(BossID.ANDROID_14);
            this.createBoss(BossID.ALONG);
            this.createBoss(BossID.SUPER_ANDROID_17);
            this.createBoss(BossID.MABU);
            this.createBoss(BossID.MABU);
            this.createBoss(BossID.MABU);
            this.createBoss(BossID.KAMIOREN);
            this.createBoss(BossID.SUPER_XEN);
            this.createBoss(BossID.TDST);
            this.createBoss(BossID.PIC);
            this.createBoss(BossID.POC);
            this.createBoss(BossID.KING_KONG);
            this.createBoss(BossID.SONGOKU_TA_AC);
            this.createBoss(BossID.CUMBER);
            this.createBoss(BossID.COOLER);
            this.createBoss(BossID.COOLER_GOLD);
            this.createBoss(BossID.XEN_BO_HUNG);
            this.createBoss(BossID.SIEU_BO_HUNG);
            this.createBoss(BossID.XEN_CON_1);
            this.createBoss(BossID.XEN_CON_1);
            this.createBoss(BossID.XEN_CON_1);
            this.createBoss(BossID.XEN_CON_1);
            this.createBoss(BossID.THAN_HUY_DIET_CHAMPA);
            this.createBoss(BossID.THAN_HUY_DIET);
            this.createBoss(BossID.THAN_HUY_DIET_CHAMPA);
            this.createBoss(BossID.THAN_HUY_DIET);
            this.createBoss(BossID.THIEN_SU_VADOS);
            this.createBoss(BossID.THIEN_SU_WHIS);
            this.createBoss(BossID.THIEN_SU_VADOS);
            this.createBoss(BossID.THIEN_SU_WHIS);
            this.createBoss(BossID.DORAEMON);
            this.createBoss(BossID.NOBITA);
            this.createBoss(BossID.XUKA);
            this.createBoss(BossID.CHAIEN);
            this.createBoss(BossID.XEKO);
            this.createBoss(BossID.BLACK);
            this.createBoss(BossID.ZAMASZIN);
            this.createBoss(BossID.BLACK2);
            this.createBoss(BossID.ZAMASMAX);
            this.createBoss(BossID.BLACK);
            this.createBoss(BossID.BLACK3);
            this.createBoss(BossID.KUKU);
            this.createBoss(BossID.MAP_DAU_DINH);
            this.createBoss(BossID.RAMBO);
            this.createBoss(BossID.FIDE);
            this.createBoss(BossID.DR_KORE);
            this.createBoss(BossID.SON_TINH);
            this.createBoss(BossID.THUY_TINH);
            this.createBoss(BossID.ELEC);
            this.createBoss(BossID.OIL);
            //this.createBoss(BossID.NOEL_NE);
            this.createBoss(BossID.MACKI);
            this.createBoss(BossID.GAS);
            this.createBoss(BossID.TAPSU4);
            this.createBoss(BossID.TAPSU4);
           
            for (int i = 1; i <= 5; i++) {
                this.createBoss(BossID.BROLY_THUONG + i);
            }
        } catch (Exception ex) {
        }
        this.loadedBoss = true;
        new Thread(BossManager.I, "Update boss").start();
    }

    public Boss createBoss(int bossID) {
        try {
            if (bossID <= BossID.BROLY_THUONG + 5 && bossID >= BossID.BROLY_THUONG) {

                return new Broly(MapService.gI().getZone(Util.randomMapBossBroly()), 500, 10000);
            }
            switch (bossID) {

//                case BossID.CUMBERYELLOW:
//                    return new cumberYellow();
//                case BossID.CUMBERBLACK:
//                    return new cumberBlack();
//                case BossID.KAMIRIN:
//                    return new kamiRin();
//                case BossID.KAMILOC:
//                    return new kamiLoc();
//                case BossID.KAMI_SOOME:
//                    return new kamiSooMe();
                case BossID.TAPSU4:
                    return new Yardat();
                case BossID.KUKU:
                    return new FideSaga_Kuku_NV();
                case BossID.MAP_DAU_DINH:
                    return new FideSaga_MapDauDinh_NV();
                case BossID.RAMBO:
                    return new FideSaga_Rambo_NV();
                case BossID.DRABURA:
                    return new Drabura();
                case BossID.DRABURA_2:
                    return new Drabura2();
                case BossID.BUI_BUI:
                    return new BuiBui();
                case BossID.BUI_BUI_2:
                    return new BuiBui2();
                case BossID.YA_CON:
                    return new Yacon();
                case BossID.MABU_12H:
                    return new MabuBoss();
                case BossID.Rong_1Sao:
                    return new Rong1Sao();
                case BossID.Rong_2Sao:
                    return new Rong2Sao();
                case BossID.Rong_3Sao:
                    return new Rong3Sao();
                case BossID.Rong_4Sao:
                    return new Rong4Sao();
                case BossID.Rong_5Sao:
                    return new Rong5Sao();
                case BossID.Rong_6Sao:
                    return new Rong6Sao();
                case BossID.Rong_7Sao:
                    return new Rong7Sao();
                case BossID.FIDE:
                    return new FideSaga_Fide_NV();
                case BossID.DR_KORE:
                    return new AndroidSaga_DrKore_NV();
                case BossID.ANDROID_19:
                    return new AndroidSaga_S19_NV();
                case BossID.ANDROID_13:
                    return new AndroidSaga_S13_NV();
                case BossID.ANDROID_14:
                    return new AndroidSaga_S14_NV();
                case BossID.ANDROID_15:
                    return new AndroidSaga_S15_NV();
                case BossID.SUPER_XEN:
                    return new SuperXen();
                case BossID.SUPER_ANDROID_17:
                    return new AndroidSaga_SuperAdr17();
                case BossID.PIC:
                    return new AndroidSaga_Pic_NV();
                case BossID.POC:
                    return new AndroidSaga_Poc_NV();
                case BossID.KING_KONG:
                    return new AndroidSaga_KingKong_NV();
                case BossID.XEN_BO_HUNG:
                    return new CellSaga_XenBoHung_NV();
                case BossID.SIEU_BO_HUNG:
                    return new CellSaga_SieuBoHung_NV();
//                case BossID.TRUNG_UY_TRANG:
//                    return new TrungUyTrang();
                case BossID.XUKA:
                    return new DRMSaga_Xuka();
                case BossID.NOBITA:
                    return new DRMSaga_Nobita();
                case BossID.XEKO:
                    return new DRMSaga_Xeko();
                case BossID.CHAIEN:
                    return new DRMSaga_Chaien();
                case BossID.DORAEMON:
                    return new DRMSaga_Doraemon();
                case BossID.VUA_COLD:
                    return new Kingcold();
                case BossID.FIDE_ROBOT:
                    return new FideRobot();
                case BossID.COOLER:
                    return new Cooler();
                case BossID.ZAMASMAX:
                    return new BlackSaga_SuperZamas();
                case BossID.ZAMASZIN:
                    return new BlackSaga_ZamasKaio_NV();
                case BossID.BLACK2:
                    return new BlackSaga_SuperBlack();
                case BossID.BLACK1:
                    return new BlackSaga_BlackGoku();
                case BossID.BLACK:
                    return new BlackSaga_Black_NV();
                case BossID.BLACK3:
                    return new BlackSaga_BlackGokuRose();
                case BossID.XEN_CON_1:
                    return new CellSaga_Xencon_NV();
                case BossID.MABU:
                    return new Mabu();
                case BossID.TDST:
                    return new FideSaga_TDST_NV();
                case BossID.COOLER_GOLD:
                    return new CoolerGold();
                case BossID.CUMBER:
                    return new Cumber();
                case BossID.THAN_HUY_DIET_CHAMPA:
                    return new Champa();
                case BossID.THIEN_SU_VADOS:
                    return new Vados();
                case BossID.THAN_HUY_DIET:
                    return new ThanHuyDiet();
                case BossID.THIEN_SU_WHIS:
                    return new ThienSuWhis();
                case BossID.SON_TINH:
                    return new Sontinh();
                case BossID.THUY_TINH:
                    return new Thuytinh();
                case BossID.SONGOKU_TA_AC:
                    return new SongokuTaAc();
//                case BossID.AN_TROM:
//                    return new AnTrom();
                case BossID.KAMIOREN:
                    return new KamiOren();
                case BossID.NOEL_NE:
                    return new Noel();
                default:
                    return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public boolean existBossOnPlayer(Player player) {
        return !player.zone.getBosses().isEmpty();
    }

    public Boss getBossById(int bossId) {
        return BossManager.gI().bosses.stream().filter(boss -> boss.id == bossId && !boss.isDie()).findFirst().orElse(null);
    }

    public void teleBoss(Player pl, Message _msg) throws Exception {
        if (_msg != null) {
            try {
                int id = _msg.reader().readInt();
                
                Boss b = getBossById(id);
                if (b == null) {
                    Player player = GodGK.loadById(id);
                    if (player != null && player.zone != null) {
                        ChangeMapService.gI().changeMapYardrat(pl, player.zone, player.location.x, player.location.y);
                        return;
                    } else {
                        Service.gI().sendThongBao(pl, "Nó trốn rồi");
                        return;
                    }
                }
                   if (pl != null) {
                     
                    if (id != -1 && pl.id != id) {
                        SieuHangService.gI().startChallenge(pl, id);
//                      
                    }
                   }
                if (!b.isDie() && b.zone != null) {
                    ChangeMapService.gI().changeMapYardrat(pl, b.zone, b.location.x, b.location.y);
                } else {
                    Service.gI().sendThongBao(pl, "Boss Hẹo Rồi");
                }
            } catch (IOException e) {
                System.out.println("Loi tele boss");
                e.printStackTrace();
            }
        }
    }

    public void summonBoss(Player pl, Message _msg) {
        if (!pl.getSession().isAdmin2) {
            Service.gI().sendThongBao(pl, "Chỉ dành cho Admin");
            return;
        }
        if (_msg != null) {
            try {
                int id = _msg.reader().readInt();
                Boss b = getBossById(id);
                if (b != null && b.zone != null) {
                    ChangeMapService.gI().changeMapYardrat(b, pl.zone, pl.location.x, pl.location.y);
                    return;
                }
                if (b == null) {
                    Player player = GodGK.loadById(id);
                    if (player != null && player.zone != null) {
                        ChangeMapService.gI().changeMapYardrat(player, pl.zone, pl.location.x, pl.location.y);
                    } else {
                        Service.gI().sendThongBao(pl, "Nó trốn rồi");
                    }
                }
            } catch (IOException e) {
                System.out.println("Loi summon boss");
            }
        }
    }

    public void showListBoss(Player player) {
        if ( !player.isAdmin2()) {
            return;
        }
        Message msg;
        try {
            msg = new Message(-96);
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Boss");
            msg.writer()
                    .writeByte(
                            (int) bosses.stream()
                                    .filter(boss -> !MapService.gI().isMapMaBu(boss.data[0].getMapJoin()[0])
                                    && !MapService.gI().isMapKhiGaHuyDiet(boss.data[0].getMapJoin()[0])
                                    && !MapService.gI().isMapConDuongRanDoc(boss.data[0].getMapJoin()[0])
                                    && !MapService.gI().isMapDoanhTrai(boss.data[0].getMapJoin()[0])
                                    && !MapService.gI().isMapKhiGaHuyDiet(boss.data[0].getMapJoin()[0])
                                    && !MapService.gI().isMapBlackBallWar(boss.data[0].getMapJoin()[0]))
                                    .count());
            for (int i = 0; i < bosses.size(); i++) {
                Boss boss = this.bosses.get(i);
                if (MapService.gI().isMapMaBu(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapBlackBallWar(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapConDuongRanDoc(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapBanDoKhoBau(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapDoanhTrai(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapKhiGaHuyDiet(boss.data[0].getMapJoin()[0])) {
                    continue;
                }
                msg.writer().writeInt((int) boss.id);
                msg.writer().writeInt((int) boss.id);
                msg.writer().writeShort(boss.data[0].getOutfit()[0]);
                if (player.getSession().version > 214) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(boss.data[0].getOutfit()[1]);
                msg.writer().writeShort(boss.data[0].getOutfit()[2]);
                msg.writer().writeUTF(boss.data[0].getName());
                if (boss.zone != null) {
                    msg.writer().writeUTF("Sống");
                    msg.writer().writeUTF("Thông Tin Boss\n" + "|7|Map : " + boss.zone.map.mapName + "(" + boss.zone.map.mapId + ") \nZone: " + boss.zone.zoneId + "\nHP: " + Util.powerToString(boss.nPoint.hp) + "\nDame: " + Util.powerToString(boss.nPoint.dame));
                } else {
                    msg.writer().writeUTF("Chết");
                    msg.writer().writeUTF("Boss Respawn\n|7|Time to Reset : " + (boss.secondsRest <= 0 ? "BossAppear" : boss.secondsRest + " giây"));
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
//            Logger.logException(Manager.class, e, "Lỗi show list boss");
        }
    }

    public void showListBoss1(Player player) {
        if (!player.isAdmin2()) {
            return;
        }
        Message msg;
        try {
            msg = new Message(-96);
            msg.writer().writeByte(1);
            msg.writer().writeUTF("Menu Boss For Admin");
            msg.writer().writeByte((int) bosses.stream().filter(boss -> !MapService.gI().isMapMaBu(boss.data[0].getMapJoin()[0])
                                    && !MapService.gI().isMapKhiGaHuyDiet(boss.data[0].getMapJoin()[0])
                                    && !MapService.gI().isMapConDuongRanDoc(boss.data[0].getMapJoin()[0])
                                    && !MapService.gI().isMapDoanhTrai(boss.data[0].getMapJoin()[0])
                                    && !MapService.gI().isMapKhiGaHuyDiet(boss.data[0].getMapJoin()[0])
                                    && !MapService.gI().isMapBlackBallWar(boss.data[0].getMapJoin()[0]))
                                    .count());
            for (int i = 0; i < bosses.size(); i++) {
                Boss boss = this.bosses.get(i);
                if (MapService.gI().isMapMaBu(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapBlackBallWar(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapConDuongRanDoc(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapBanDoKhoBau(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapDoanhTrai(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapKhiGaHuyDiet(boss.data[0].getMapJoin()[0])) {
                    continue;
                }
                msg.writer().writeInt(i);
                msg.writer().writeInt(i);
                msg.writer().writeShort(boss.data[0].getOutfit()[0]);
                if (player.getSession().version > 214) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(boss.data[0].getOutfit()[1]);
                msg.writer().writeShort(boss.data[0].getOutfit()[2]);
                msg.writer().writeUTF(boss.data[0].getName());
                if (boss.zone != null) {
                    msg.writer().writeUTF("Sống");
                    msg.writer().writeUTF("Thông Tin Boss\n" + "|7|Map : " + boss.zone.map.mapName + "(" + boss.zone.map.mapId + ") \nZone: " + boss.zone.zoneId + "\nHP: " + Util.powerToString(boss.nPoint.hp) + "\nDame: " + Util.powerToString(boss.nPoint.dame));
                } else {
                    msg.writer().writeUTF("Chết");
                    msg.writer().writeUTF("Boss Respawn\n|7|Time to Reset : " + (boss.secondsRest <= 0 ? "BossAppear" : boss.secondsRest + " giây"));
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showListBoss2(Player player) {
        if (player.getSession().actived == 0) {
            Service.gI().sendThongBaoOK(player, "Vui Lòng Mở Thành Viên");
            return;
        }
        Message msg;
        try {
            msg = new Message(-96);
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Boss");
            msg.writer()
                    .writeByte(
                            (int) bosses.stream()
                                    .filter(boss -> !MapService.gI().isMapMaBu(boss.data[0].getMapJoin()[0])
                                    && !MapService.gI().isMapConDuongRanDoc(boss.data[0].getMapJoin()[0])
                                    && !MapService.gI().isMapKhiGaHuyDiet(boss.data[0].getMapJoin()[0])
                                    && !MapService.gI().isMapDoanhTrai(boss.data[0].getMapJoin()[0])
                                    && !MapService.gI().isMapKhiGaHuyDiet(boss.data[0].getMapJoin()[0])
                                    && !MapService.gI().isMapBlackBallWar(boss.data[0].getMapJoin()[0]))
                                    .count());
            for (int i = 0; i < bosses.size(); i++) {
                Boss boss = this.bosses.get(i);
                if (MapService.gI().isMapMaBu(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapBlackBallWar(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapConDuongRanDoc(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapBanDoKhoBau(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapDoanhTrai(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapKhiGaHuyDiet(boss.data[0].getMapJoin()[0])) {
                    continue;
                }
                msg.writer().writeInt((int) boss.id);
                msg.writer().writeInt((int) boss.id);
                msg.writer().writeShort(boss.data[0].getOutfit()[0]);
                if (player.getSession().version > 214) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(boss.data[0].getOutfit()[1]);
                msg.writer().writeShort(boss.data[0].getOutfit()[2]);
                msg.writer().writeUTF(boss.data[0].getName());
                if (boss.zone != null) {
                    msg.writer().writeUTF("Sống");
                    msg.writer().writeUTF("(Dịch chuyển ngay đến Boss)");
                } else {
                    msg.writer().writeUTF("Chết");
                    msg.writer().writeUTF("Time to Reset : " + boss.secondsRest-- + " Giây");
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
//            Logger.logException(Manager.class, e, "Lỗi show list boss");
        }
    }

    public void showListBoss3(Player player) {
        if (player.getSession().actived == 0) {
            Service.gI().sendThongBaoOK(player, "Vui Lòng Mở Thành Viên");
            return;
        }
        Message msg;
        try {
            msg = new Message(-96);
            msg.writer().writeByte(1);
            msg.writer().writeUTF("Menu Boss For Mem");
            msg.writer()
                    .writeByte(
                            (int) bosses.stream()
                                    .filter(boss -> !MapService.gI().isMapMaBu(boss.data[0].getMapJoin()[0])
                                    && !MapService.gI().isMapConDuongRanDoc(boss.data[0].getMapJoin()[0])
                                    && !MapService.gI().isMapKhiGaHuyDiet(boss.data[0].getMapJoin()[0])
                                    && !MapService.gI().isMapDoanhTrai(boss.data[0].getMapJoin()[0])
                                    && !MapService.gI().isMapKhiGaHuyDiet(boss.data[0].getMapJoin()[0])
                                    && !MapService.gI().isMapBlackBallWar(boss.data[0].getMapJoin()[0]))
                                    .count());
            for (int i = 0; i < bosses.size(); i++) {
                Boss boss = this.bosses.get(i);
                if (MapService.gI().isMapMaBu(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapBlackBallWar(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapBanDoKhoBau(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapDoanhTrai(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapConDuongRanDoc(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapKhiGaHuyDiet(boss.data[0].getMapJoin()[0])) {
                    continue;
                }
                msg.writer().writeInt((int) boss.id);
                msg.writer().writeInt((int) boss.id);
                msg.writer().writeShort(boss.data[0].getOutfit()[0]);
                if (player.getSession().version > 214) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(boss.data[0].getOutfit()[1]);
                msg.writer().writeShort(boss.data[0].getOutfit()[2]);
                msg.writer().writeUTF(boss.data[0].getName());
                if (boss.zone != null) {
                    msg.writer().writeUTF("Sống");
                    msg.writer().writeUTF("(Dịch chuyển ngay đến Boss)");
                } else {
                    msg.writer().writeUTF("Chết");
                    msg.writer().writeUTF("Time to Reset : " + boss.secondsRest-- + " Giây");
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void timBoss(Player player, int id) {
        Boss boss = BossManager.gI().getBossById(id);
        if (boss != null && !boss.isDie()) {

            Zone z = null;
            if (boss.zone != null) {
                z = MapService.gI().getMapCanJoin(player, boss.zone.map.mapId,
                        boss.zone.zoneId);
            }
            if (z != null && z.getNumOfPlayers() < z.maxPlayer) {
                player.inventory.gold -= 0;
                ChangeMapService.gI().changeMapBySpaceShip(player, boss.zone, boss.location.x);
                Service.gI().sendMoney(player);
            } else {
                Service.gI().sendThongBao(player, "Khu vực đang full.");
            }
        }

    }

    public synchronized void callBoss(Player player, int mapId) {
        try {
            if (BossManager.gI().existBossOnPlayer(player)
                    || player.zone.items.stream().anyMatch(itemMap -> ItemMapService.gI().isBlackBall(itemMap.itemTemplate.id))
                    || player.zone.getPlayers().stream().anyMatch(p -> p.iDMark.isHoldBlackBall())) {
                return;
            }
            Boss k = null;
            switch (mapId) {
                case 85:
                    k = BossManager.gI().createBoss(BossID.Rong_1Sao);
                    break;
                case 86:
                    k = BossManager.gI().createBoss(BossID.Rong_2Sao);
                    break;
                case 87:
                    k = BossManager.gI().createBoss(BossID.Rong_3Sao);
                    break;
                case 88:
                    k = BossManager.gI().createBoss(BossID.Rong_4Sao);
                    break;
                case 89:
                    k = BossManager.gI().createBoss(BossID.Rong_5Sao);
                    break;
                case 90:
                    k = BossManager.gI().createBoss(BossID.Rong_6Sao);
                    break;
                case 91:
                    k = BossManager.gI().createBoss(BossID.Rong_7Sao);
                    break;
            }
            if (k != null) {
                k.currentLevel = 0;
                k.joinMapByZone(player);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (ServerManager.isRunning) {
            try {
                long st = System.currentTimeMillis();
                for (Boss boss : this.bosses) {
                    boss.update();
                }
                Thread.sleep(150 - (System.currentTimeMillis() - st));
            } catch (Exception ignored) {
            }

        }
    }
}
