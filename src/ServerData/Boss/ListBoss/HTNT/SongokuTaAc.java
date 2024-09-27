/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerData.Boss.ListBoss.HTNT;

import ServerData.Boss.Boss;
import ServerData.Boss.BossID;
import ServerData.Boss.BossManager;
import ServerData.Boss.BossesData;
import ServerData.Models.Map.ItemMap;
import ServerData.Models.Player.Player;
import ServerData.Models.Player.PlayerSkill.Skill;
import ServerData.Services.EffectSkillService;
import ServerData.Services.PetService;
import ServerData.Services.Service;
import ServerData.Utils.Util;
import java.util.Random;

public class SongokuTaAc extends Boss {

    public SongokuTaAc() throws Exception {
        super(BossID.SONGOKU_TA_AC, BossesData.SONGOKU_TA_AC);
    }

    @Override
    public void reward(Player plKill) {
        if (Util.isTrue(40, 100)) {
            Service.gI().dropItemMap(this.zone, Util.quadao(zone, 15, 1, this.location.x, this.location.y, plKill.id));
        } else if (Util.isTrue(1, 1)) {
            Service.gI().dropItemMap(this.zone, Util.quadao(zone, 16, 1, this.location.x, this.location.y, plKill.id));
            return;
        }
    }
    @Override
    public void active() {
        super.active(); //To change body of generated methods, choose Tools | Templates.
    }
//
    @Override
    public void joinMap() {
        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
        st = System.currentTimeMillis();
    }
    private long st;

    @Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = this.nPoint.subDameInjureWithDeff(damage);
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
}
