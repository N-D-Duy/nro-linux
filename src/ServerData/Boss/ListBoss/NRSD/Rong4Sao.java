package ServerData.Boss.ListBoss.NRSD;

import ServerData.Boss.Boss;
import ServerData.Boss.BossesData;
import ServerData.Models.Map.ItemMap;
import ServerData.Models.Player.Player;
import ServerData.Services.EffectSkillService;
import ServerData.Services.Service;
import ServerData.Utils.Util;


public class Rong4Sao extends Boss {

    public Rong4Sao() throws Exception {
        super(Util.randomBossId(), BossesData.Rong_4Sao);
    }

    @Override
    public void reward(Player plKill) {
        ItemMap it = new ItemMap(this.zone, 375, 1, this.location.x, this.location.y, -1);
        Service.gI().dropItemMap(this.zone, it);
    }
@Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = this.nPoint.subDameInjureWithDeff(damage/7);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                  damage = damage/4;
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


