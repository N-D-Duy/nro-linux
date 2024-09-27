package ServerData.Boss.ListBoss.HAKAI;

import Server.Data.Consts.ConstPlayer;
import ServerData.Boss.Boss;
import ServerData.Boss.BossID;
import ServerData.Boss.BossesData;
import ServerData.Models.Item.Item;
import ServerData.Models.Map.ItemMap;
import ServerData.Models.Player.Player;
import ServerData.Server.Manager;
import ServerData.Services.EffectSkillService;
import ServerData.Services.PlayerService;
import ServerData.Services.Service;
import ServerData.Services.TaskService;
import ServerData.Utils.Util;
import java.util.Random;


public class ThanHuyDiet extends Boss {
    private long lasttimehakai;
    private int timehakai;

    public ThanHuyDiet() throws Exception {
        super(BossID.THAN_HUY_DIET, BossesData.THAN_HUY_DIET);
    }

    @Override
    public void reward(Player plKill) {
        byte randomDo = (byte) new Random().nextInt(Manager.itemIds_TL.length - 1);
        byte randomNR = (byte) new Random().nextInt(Manager.itemIds_NR_SB.length);
        ItemMap itemMap;
        if (Util.isTrue(5, 100)) {
            if (Util.isTrue(1, 50)) {
                itemMap = Util.ratiItem(zone,1142, 1, this.location.x, this.location.y, plKill.id);
            } else {
                itemMap = Util.ratiItem(zone, Manager.itemIds_TL[randomDo], 1, this.location.x, this.location.y, plKill.id);
            }
        } else {
            itemMap = Util.ratiItem(zone, Manager.itemIds_NR_SB[randomNR], 1, this.location.x, this.location.y, plKill.id);
        }
        itemMap.options.add(new Item.ItemOption(30, 1));
        Service.gI().dropItemMap(this.zone, itemMap);
        TaskService.gI().checkDoneTaskKillBoss(plKill, this);
    }

      @Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = this.nPoint.subDameInjureWithDeff(damage/3);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = 1;
            }
            this.nPoint.subHP(damage);
            if (isDie()) {
                this.setDie(plAtt);
                die(plAtt);
            }
            return damage;
        } else {
            return 0;
        }
    }


    @Override
    public void active() {
        super.attack();
        if (this.typePk == ConstPlayer.NON_PK) {
            this.changeToTypePK();
        }
        this.huydiet();
        }
    @Override
    public void joinMap() {
        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
    }

    private void huydiet() {
        if (!Util.canDoWithTime(this.lasttimehakai, this.timehakai) || !Util.isTrue(1, 100)) {
            return;
        }

        Player pl = this.zone.getRandomPlayerInMap();
        if (pl == null || pl.isDie()) {
            return;
        }
        this.nPoint.dameg += (pl.nPoint.dame * 5 / 100);
        this.nPoint.hpg += (pl.nPoint.hp * 2 / 100);
        this.nPoint.critg++;
        this.nPoint.calPoint();
        PlayerService.gI().hoiPhuc(this, pl.nPoint.hp, 0);
        pl.injured(null, pl.nPoint.hpMax, true, false);
        Service.gI().sendThongBao(pl, "Bạn vừa bị " + this.name + " cho bay màu");
        this.chat(2, "Hắn ta mạnh quá,coi chừng " + pl.name + ",tên " + this.name + " hắn không giống như những kẻ thù trước đây");
        this.chat("Thật là yếu ớt " + pl.name);
        this.lasttimehakai = System.currentTimeMillis();
        this.timehakai = Util.nextInt(20000, 30000);
    }
}


