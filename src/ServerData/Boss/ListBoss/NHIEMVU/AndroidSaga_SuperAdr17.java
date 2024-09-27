package ServerData.Boss.ListBoss.NHIEMVU;

import ServerData.Boss.Boss;
import ServerData.Boss.BossID;
import ServerData.Boss.BossesData;
import ServerData.Models.Map.ItemMap;
import ServerData.Models.Player.Player;
import ServerData.Services.EffectSkillService;
import ServerData.Services.Service;
import ServerData.Utils.Util;
import java.util.Random;

public class AndroidSaga_SuperAdr17 extends Boss {

    public AndroidSaga_SuperAdr17() throws Exception {
        super(BossID.SUPER_ANDROID_17, BossesData.SUPER_ANDROID_17);
          this.nPoint.defg = (short) (this.nPoint.hpg / 1000);
        if (this.nPoint.defg < 0) {
            this.nPoint.defg = (short) -this.nPoint.defg;
    }
    }
   @Override
   public void reward(Player plKill) {
        int[] itemDos = new int[]{1098};
        int randomDo = new Random().nextInt(itemDos.length);
        int[] itemDosTL = new int[]{555, 557, 559,556, 558, 560,562, 564, 566,563, 565, 567};
        int randomDoTL = new Random().nextInt(itemDos.length);
        int randomDo12 = new Random().nextInt(itemDos.length);
        int[] itemDos12 = new int[]{233, 237, 241, 245, 249, 253, 257, 261, 265, 269, 273, 277};
            if (Util.isTrue(20, 100)) {
                Service.gI().dropItemMap(this.zone, Util.RaitiDoc12(zone, itemDos12[randomDo12], 1, this.location.x, this.location.y, plKill.id));
            } else if (Util.isTrue(50, 200)) {
              Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, itemDosTL[randomDoTL], 1, this.location.x, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id));
            } else  if (Util.isTrue(50, 100)){
            int[] NRs = new int[]{17,18};
            int randomNR = new Random().nextInt(itemDos.length);
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, NRs[randomNR], 1, this.location.x, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id));
        }
    }
    @Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = this.nPoint.subDameInjureWithDeff(damage/13);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = damage/2;
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
        super.active();}


    @Override
    public void joinMap() {
        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
        st = System.currentTimeMillis();
    }
    private long st;
 @Override
    public void leaveMap() {
        super.leaveMap();
    }
}
