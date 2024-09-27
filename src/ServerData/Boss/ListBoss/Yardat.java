
package ServerData.Boss.ListBoss;

import ServerData.Boss.Boss;
import ServerData.Boss.BossID;
import ServerData.Boss.BossesData;
import ServerData.Services.EffectSkillService;
import ServerData.Services.Service;
import ServerData.Utils.Util;
import ServerData.Models.Player.Player;

public class Yardat extends Boss {

    public Yardat() throws Exception {
        super(BossID.TAPSU4, BossesData.TAPSU1, BossesData.TAPSU2, BossesData.TAPSU3, BossesData.TAPSU4);
    }

    @Override
    public void reward(Player plKill) {
            if (Util.isTrue(50, 50)) {
                Service.gI().dropItemMap(this.zone, Util.bikip(zone, 590, 50, this.location.x, this.location.y, plKill.id));
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
            damage = this.nPoint.subDameInjureWithDeff(damage / 2);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = damage ;
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
}