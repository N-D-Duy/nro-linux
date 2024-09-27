package ServerData.Models.Player;

import ServerData.Models.Item.Item;
import ServerData.Models.Map.Mob.Mob;
import ServerData.Services.EffectSkillService;
import ServerData.Services.ItemService;
import ServerData.Services.PlayerService;
import ServerData.Services.Service;
import ServerData.Services.InventoryServiceNew;
import ServerData.Services.ItemTimeService;
import ServerData.Services.MapService;
import ServerData.Utils.Logger;
import ServerData.Utils.Util;
import java.util.ArrayList;
import java.util.List;


public class EffectSkin {

    private static final String[] textOdo = new String[]{ //Cải trang Drabura Frost
        "Có những lúc một mình ta", "Ngồi nhìn Hôm Qua",
        "Xong mới biết là Đúng hay sai"
    };
    private static final String[] textDraburaFrost = new String[]{ //Cải trang Drabura Frost
        "Ui lạnh quá..", "Đông cứng rồi", "Tránh xa ta ra",
    };
    private static final String[] textDrabura = new String[]{ //Cải trang Drabura
        "AAA Nặng Quá", "Chết tiệt", "Hóa đá rồi", "Tránh xa ta ra"
    };
   
    private static final String[] test = new String[]{
        "Ra Xã Hội Làm Ăn" ,"Liều Thì Ăn Nhiều","Không liều thì ăn cứt"
    };
    private Player player;

    public EffectSkin(Player player) {
        this.player = player;
        this.xHPKI = 1;
    }
    private long lastTimeHoaBang;  //Cải trang Dracula Frost
    private long lastTimeHoaDa; //Cải trang Dracula 
    private long lastTimeHoaCaRot; //Cải trang Thỏ Đại Ca
    public long lastTimeAttack;
    private long lastTimeOdo;
    private long lastTimeTest;
    private long lastTimeXenHutHpKi;

    public long lastTimeAddTimeTrainArmor;
    public long lastTimeSubTimeTrainArmor;

    public boolean isVoHinh;

    public long lastTimeXHPKI;
    public int xHPKI;

    public long lastTimeUpdateCTHT;

    public void update() {
        updateDraburaFrost();
        updateDrabura();
        updateVoHinh();
        if (this.player.zone != null && !MapService.gI().isMapOffline(this.player.zone.map.mapId)) {
            updateOdo();
            updateXenHutXungQuanh();
        }
        if (!this.player.isBoss && !this.player.isPet &&!player.isNewPet) {
            updateTrainArmor();
        }
        if (xHPKI != 1 && Util.canDoWithTime(lastTimeXHPKI, 1800000)) {
            xHPKI = 1;
            Service.gI().point(player);
        }
        updateCTHaiTac();
    }
    
    private void updateDraburaFrost() {
        try {
            if (this.player.nPoint.isDraburaFrost == true) {
                if (Util.canDoWithTime(lastTimeHoaBang, 10000)) {
                    List<Player> players = new ArrayList<>();
                    List<Player> playersMap = this.player.zone.getNotBosses();
                    for (Player pl : playersMap) {
                        if (!this.player.equals(pl)  && !pl.isBoss && !pl.isDie()
                                && Util.getDistance(this.player, pl) <= 200) {
                            players.add(pl);
                        }
                    }
                    for (Player pl : players) {
                         EffectSkillService.gI().SetHoaBang(pl, System.currentTimeMillis(), 90000);
                         EffectSkillService.gI().setBlindDCTT(pl, System.currentTimeMillis(), 5000);
                    EffectSkillService.gI().sendEffectPlayer(player, pl, EffectSkillService.TURN_ON_EFFECT, (byte) EffectSkillService.BLIND_EFFECT);
                         Service.gI().Send_Caitrang(pl);
                    ItemTimeService.gI().sendItemTime(pl, 11085, 9000 / 1000);
                    Service.gI().chat(player, "Ối Bạn Ơi , Hic !");
                    Service.gI().chat(pl, textDraburaFrost[Util.nextInt(0, textDraburaFrost.length - 1)]);
                    PlayerService.gI().sendInfoHpMpMoney(pl);
                    Service.gI().Send_Info_NV(pl);
                    }
                    this.lastTimeHoaBang = System.currentTimeMillis();
                }
            } else {
            }
        } catch (Exception e) {
            Logger.error("");
        }
    }
    
    private void updateDrabura() { // Dracula Hóa Đá
        try {
            if (this.player.nPoint.isDrabura == true) {
                if (Util.canDoWithTime(lastTimeHoaDa, 10000)) {
                    List<Player> players = new ArrayList<>();
                    List<Player> playersMap = this.player.zone.getNotBosses();
                    for (Player pl : playersMap) {
                        if (!this.player.equals(pl) && !pl.isBoss && !pl.isDie()
                                && Util.getDistance(this.player, pl) <= 200) {
                            players.add(pl);
                        }
                    }
                    for (Player pl : players) {
                         EffectSkillService.gI().SetHoaDa(pl, System.currentTimeMillis(),80000);
                         EffectSkillService.gI().setBlindDCTT(pl, System.currentTimeMillis(),9000);
                    EffectSkillService.gI().sendEffectPlayer(player, pl, EffectSkillService.TURN_ON_EFFECT, EffectSkillService.BLIND_EFFECT);
                         Service.gI().Send_Caitrang(pl);
                    ItemTimeService.gI().sendItemTime(pl, 4392, 9000 / 1000);
                    Service.gI().chat(player, "Bạn Bị Hoá Đá");
                        Service.gI().chat(pl, textDrabura[Util.nextInt(0, textDrabura.length - 1)]);
                        PlayerService.gI().sendInfoHpMpMoney(pl);
                        Service.gI().Send_Info_NV(pl);
                    }
                    this.lastTimeHoaDa = System.currentTimeMillis();
                }
            } else {
            }
        } catch (Exception e) {
            Logger.error("");
        }
    }
    
   
   
    private void updateCTHaiTac() {
        if (this.player.setClothes.ctHaiTac != -1
                && this.player.zone != null
                && Util.canDoWithTime(lastTimeUpdateCTHT, 5000)) {
            int count = 0;
            int[] cts = new int[9];
            cts[this.player.setClothes.ctHaiTac - 618] = this.player.setClothes.ctHaiTac;
            List<Player> players = new ArrayList<>();
            players.add(player);
            try {
                for (Player pl : player.zone.getNotBosses()) {
                    if (!player.equals(pl) && pl.setClothes.ctHaiTac != -1 && Util.getDistance(player, pl) <= 300) {
                        cts[pl.setClothes.ctHaiTac - 618] = pl.setClothes.ctHaiTac;
                        players.add(pl);
                    }
                }
            } catch (Exception e) {
            }
            for (int i = 0; i < cts.length; i++) {
                if (cts[i] != 0) {
                    count++;
                }
            }
            for (Player pl : players) {
                Item ct = pl.inventory.itemsBody.get(5);
                if (ct.isNotNullItem() && ct.template.id >= 618 && ct.template.id <= 626) {
                    for (Item.ItemOption io : ct.itemOptions) {
                        if (io.optionTemplate.id == 147
                                || io.optionTemplate.id == 77
                                || io.optionTemplate.id == 103) {
                            io.param = count * 3;
                        }
                    }
                }
                if (!pl.isPet &&!pl.isNewPet&& Util.canDoWithTime(lastTimeUpdateCTHT, 5000)) {
                    InventoryServiceNew.gI().sendItemBody(pl);
                }
                pl.effectSkin.lastTimeUpdateCTHT = System.currentTimeMillis();
            }
        }
    }

    private void updateXenHutXungQuanh() {
        try {
            int param = this.player.nPoint.tlHutHpMpXQ;
            if (param > 0) {
                if (!this.player.isDie() && Util.canDoWithTime(lastTimeXenHutHpKi, 5000)) {
                    long hpHut = 0;
                    long mpHut = 0;
                    List<Player> players = new ArrayList<>();
                    List<Player> playersMap = this.player.zone.getNotBosses();
                    for (Player pl : playersMap) {
                        if (!this.player.equals(pl) && !pl.isBoss && !pl.isDie()
                                && Util.getDistance(this.player, pl) <= 200) {
                            players.add(pl);
                        }

                    }
                    for (Mob mob : this.player.zone.mobs) {
                        if (mob.point.gethp() > 1) {
                            if (Util.getDistance(this.player, mob) <= 200) {
                                long subHp = mob.point.getHpFull() * param / 100;
                                if (subHp >= mob.point.gethp()) {
                                    subHp = mob.point.gethp() - 1;
                                }
                                hpHut += subHp;
                                mob.injured(null, subHp, false);
                            }
                        }
                    }
                    for (Player pl : players) {
                        long subHp = (pl.nPoint.hpMax * param / 100);
                        long subMp = (pl.nPoint.mpMax * param / 100);
                        if (subHp >= pl.nPoint.hp) {
                            subHp = (pl.nPoint.hp - 1);
                        }
                        if (subMp >= pl.nPoint.mp) {
                            subMp = (pl.nPoint.mp - 1);
                        }
                        hpHut += subHp;
                        mpHut += subMp;
                        PlayerService.gI().sendInfoHpMpMoney(pl);
                        Service.gI().Send_Info_NV(pl);
                        pl.injured(null, subHp, true, false);
                    }
                    this.player.nPoint.addHp(hpHut);
                    this.player.nPoint.addMp(mpHut);
                    PlayerService.gI().sendInfoHpMpMoney(this.player);
                    Service.gI().Send_Info_NV(this.player);
                    this.lastTimeXenHutHpKi = System.currentTimeMillis();
                }
            }
        } catch (Exception e) {
            Logger.error("");
        }
    }

    private void updateOdo() {
        try {
            int param = this.player.nPoint.tlHpGiamODo;
            if (param > 0) {
                if (Util.canDoWithTime(lastTimeOdo, 10000)) {
                    List<Player> players = new ArrayList<>();

                    List<Player> playersMap = this.player.zone.getNotBosses();
                    for (Player pl : playersMap) {
                        if (!this.player.equals(pl) && !pl.isBoss && !pl.isDie()
                                && Util.getDistance(this.player, pl) <= 200) {
                            players.add(pl);
                        }

                    }
                    for (Player pl : players) {
                        long subHp = (int) (pl.nPoint.hpMax * param / 100);
                        if (subHp >= pl.nPoint.hp) {
                            subHp = (int) (pl.nPoint.hp - 1);
                        }
                        Service.gI().chat(pl, textOdo[Util.nextInt(0, textOdo.length - 1)]);
                        PlayerService.gI().sendInfoHpMpMoney(pl);
                        Service.gI().Send_Info_NV(pl);
                        pl.injured(null, subHp, true, false);
                    }
                    this.lastTimeOdo = System.currentTimeMillis();
                }
            }
        } catch (Exception e) {
            Logger.error("");
        }
    }
    private void Test() {
        try {
            int param = this.player.nPoint.test;
            if (param > 0) {
                if (Util.canDoWithTime(lastTimeTest, 10000)) {
                    List<Player> players = new ArrayList<>();

                   
                    for (Player pl : players) {
                        int subHp = (int) (pl.nPoint.hpMax * param * 100);
                        if (subHp >= pl.nPoint.hp) {
                            subHp = (int) (pl.nPoint.hp + 1);
                        }
                        Service.gI().chat(pl, test[Util.nextInt(0, test.length + 1)]);
                        PlayerService.gI().sendInfoHpMpMoney(pl);
                        Service.gI().Send_Info_NV(pl);
                        pl.injured(null, subHp, true, false);
                    }
                    this.lastTimeTest = System.currentTimeMillis();
                }
            }
        } catch (Exception e) {
            Logger.error("");
        }
    }

    //giáp tập luyện
    private void updateTrainArmor() {
        if (Util.canDoWithTime(lastTimeAddTimeTrainArmor, 60000) && !Util.canDoWithTime(lastTimeAttack, 30000)) {
            if (this.player.nPoint.wearingTrainArmor) {
                for (Item.ItemOption io : this.player.inventory.trainArmor.itemOptions) {
                    if (io.optionTemplate.id == 9) {
                        if (io.param < 1000) {
                            io.param++;
                            InventoryServiceNew.gI().sendItemBody(player);
                        }
                        break;
                    }
                }
            }
            this.lastTimeAddTimeTrainArmor = System.currentTimeMillis();
        }
        if (Util.canDoWithTime(lastTimeSubTimeTrainArmor, 60000)) {
            for (Item item : this.player.inventory.itemsBag) {
                if (item.isNotNullItem()) {
                    if (ItemService.gI().isTrainArmor(item)) {
                        for (Item.ItemOption io : item.itemOptions) {
                            if (io.optionTemplate.id == 9) {
                                if (io.param > 0) {
                                    io.param--;
                                }
                            }
                        }
                    }
                } else {
                    break;
                }
            }
            for (Item item : this.player.inventory.itemsBox) {
                if (item.isNotNullItem()) {
                    if (ItemService.gI().isTrainArmor(item)) {
                        for (Item.ItemOption io : item.itemOptions) {
                            if (io.optionTemplate.id == 9) {
                                if (io.param > 0) {
                                    io.param--;
                                }
                            }
                        }
                    }
                } else {
                    break;
                }
            }
            this.lastTimeSubTimeTrainArmor = System.currentTimeMillis();
            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().point(this.player);
        }
    }

    private void updateVoHinh() {
        if (this.player.nPoint.wearingVoHinh) {
            if (Util.canDoWithTime(lastTimeAttack, 5000)) {
                isVoHinh = true;
            } else {
                isVoHinh = false;
            }
        }
    }

    public void dispose() {
        this.player = null;
    }
}
