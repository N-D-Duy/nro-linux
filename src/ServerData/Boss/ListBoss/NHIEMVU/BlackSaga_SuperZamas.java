package ServerData.Boss.ListBoss.NHIEMVU;

import ServerData.Boss.*;
import ServerData.Models.Map.ItemMap;
import ServerData.Models.Player.Player;
import ServerData.Server.Manager;
import ServerData.Services.EffectSkillService;
import ServerData.Services.Service;
import ServerData.Utils.Util;

import java.util.Random;


public class BlackSaga_SuperZamas extends Boss {

    public BlackSaga_SuperZamas() throws Exception {
        super(BossID.ZAMASMAX, BossesData.THANZM2);
    }

    @Override
    public void reward(Player plKill) {
        byte randomDo = (byte) new Random().nextInt(Manager.itemDC12.length - 1);
        byte randomNR = (byte) new Random().nextInt(Manager.itemIds_NR_SB.length);
        int[] itemDos = new int[]{233, 237, 241, 245, 249, 253, 257, 261, 265, 269, 273, 277, 281};
        if (Util.isTrue(30, 100)) {
            if (Util.isTrue(5, 20)) {
              Service.gI().dropItemMap(this.zone, new ItemMap(zone, 725, 1, this.location.x, this.location.y, plKill.id));
            } else {
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, Manager.itemDC12[randomDo], 1, this.location.x, this.location.y, plKill.id));
            }
        } else if (Util.isTrue(70, 100)) {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, Manager.itemIds_NR_SB[randomNR], 1, this.location.x, this.location.y, plKill.id));
        } else {
            Service.gI().dropItemMap(this.zone, Util.RaitiDoc12(zone, itemDos[randomDo], 1, this.location.x, this.location.y, plKill.id));
        }
    }
    
    @Override
    public void active() {
        super.active(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void joinMap() {
        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
        st = System.currentTimeMillis();
    }

    private long st;
@Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = this.nPoint.subDameInjureWithDeff(damage/2);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = damage;
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
//    @Override
//    public void moveTo(int x, int y) {
//        if(this.currentLevel == 1){
//            return;
//        }
//        super.moveTo(x, y);
//    }
//
//    @Override
//    public void reward(Player plKill) {
//        if(this.currentLevel == 1){
//            return;
//        }
//        super.reward(plKill);
//    }
//    
//    @Override
//    protected void notifyJoinMap() {
//        if(this.currentLevel == 1){
//            return;
//        }
//        super.notifyJoinMap();
//    }
}






















