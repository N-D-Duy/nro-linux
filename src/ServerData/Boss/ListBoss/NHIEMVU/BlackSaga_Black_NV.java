package ServerData.Boss.ListBoss.NHIEMVU;

import ServerData.Boss.*;
import ServerData.Models.Item.Item;
import ServerData.Models.Map.ItemMap;
import ServerData.Models.Player.Player;
import ServerData.Server.Manager;
import ServerData.Services.EffectSkillService;
import ServerData.Services.ItemService;
import ServerData.Services.Service;
import ServerData.Services.TaskService;
import ServerData.Utils.Util;

import java.util.Random;


public class BlackSaga_Black_NV extends Boss {

    public BlackSaga_Black_NV() throws Exception {
        super(BossID.BLACK, BossesData.BLACK_GOKU);
    }

    @Override
//    public void reward(Player plKill) {
//        byte randomDo = (byte) new Random().nextInt(Manager.itemIds_TL.length - 1);
//       // byte randomNR = (byte) new Random().nextInt(Manager.itemIds_NR_SB.length);
//        if (Util.isTrue(BossManager.ratioReward, 100)) {
//            if (Util.isTrue(100, 100)) {
//              Service.gI().dropItemMap(this.zone, new ItemMap(zone, 992, 1, this.location.x, this.location.y, plKill.id));
//            } else {
//                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, Manager.itemIds_TL[randomDo], 1, this.location.x, this.location.y, plKill.id));
//            }
//        } /*else {
//            Service.gI().dropItemMap(this.zone, new ItemMap(zone, Manager.itemIds_NR_SB[randomNR], 1, this.location.x, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id));
//        }*/
//    }
   public void reward(Player plKill) {
      //  int randomDo = new Random().nextInt(itemDos.length);
        int[] itemDosTL = new int[]{555, 557, 559, 556, 558, 560, 562, 564, 566, 563, 565, 567};
        int randomDoTL = new Random().nextInt(itemDosTL.length);
           if (Util.isTrue(100, 100)) {
        //int randomDo12 = new Random().nextInt(itemDos.length);
      //  int[] itemDos12 = new int[]{233, 237, 241, 245, 249, 253, 257, 261, 265, 269, 273, 277};
      if (Util.isTrue(1, 14)) {
                Service.gI().dropItemMap(this.zone, new ItemMap(zone, 992, 1, this.location.x, this.location.y, plKill.id));
            }
      if (Util.isTrue(20, 100)) {
              Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, itemDosTL[randomDoTL], 1, this.location.x, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id));
            } else if (Util.isTrue(10, 100)) {
                Service.gI().dropItemMap(this.zone, Util.caitrangblackgoku(zone, 1098, 1, this.location.x, this.location.y, plKill.id));
            } else  if (Util.isTrue(50, 100)){
            int[] NRs = new int[]{16,17,18};
            int randomNR = new Random().nextInt(NRs.length);
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, NRs[randomNR], 1, this.location.x, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id));
        }
      TaskService.gI().checkDoneTaskKillBoss(plKill, this);
    }
   }

    @Override
    public void active() {
        super.active(); //To change body of generated methods, choose Tools | Templates.
    }
    @Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 90)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = this.nPoint.subDameInjureWithDeff(damage/5);
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
    @Override
    public void joinMap() {
        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
        st = System.currentTimeMillis();
    }

    private long st;

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






















