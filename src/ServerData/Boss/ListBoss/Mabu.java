/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerData.Boss.ListBoss;

import ServerData.Boss.Boss;
import ServerData.Boss.BossID;
import ServerData.Boss.BossStatus;
import ServerData.Boss.BossesData;
import ServerData.Models.Player.Player;
import ServerData.Server.ServerNotify;
import ServerData.Services.EffectSkillService;
import ServerData.Services.Service;
import ServerData.Utils.Util;

public class Mabu extends Boss {

    public Mabu() throws Exception {
        super(BossID.MABU, BossesData.MABU);
    }

    @Override
    public void reward(Player plKill) {
//        Service.gI().dropItemMap(this.zone, Util.MabuCt(zone, 568, 1, this.location.x, this.location.y, plKill.id));
        if (Util.isTrue(1, 100)) {
            if (Util.isTrue(70, 100)) {
                Service.gI().dropItemMap(this.zone, Util.MabuCt(zone, 1147, 1, this.location.x, this.location.y, plKill.id));
                return;
            }
            Service.gI().dropItemMap(this.zone, Util.MabuCt(zone, 937, 1, this.location.x, this.location.y, plKill.id));
        } 
    }
    @Override
    public void active() {
        super.active(); //To change body of generated methods, choose Tools | Templates.
        if (Util.canDoWithTime(st, 1800000)) {
            this.changeStatus(BossStatus.RESPAWN);
    ServerNotify.gI().sendThongBaoBenDuoi("BOSS MABU ĐÃ HỒI SINH");
        }
    }
         @Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 90)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = this.nPoint.subDameInjureWithDeff(damage/3);
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
    
}
