/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerData.Boss.ListBoss.HTNT;

import ServerData.Boss.Boss;
import ServerData.Boss.BossID;
import ServerData.Boss.BossStatus;
import ServerData.Boss.BossesData;
import ServerData.Models.Item.Item;
import ServerData.Models.Map.ItemMap;
import ServerData.Models.Player.Player;
import ServerData.Services.EffectSkillService;
import ServerData.Services.PetService;
import ServerData.Services.Service;
import ServerData.Utils.Util;
import java.util.Random;
import ServerData.Models.Player.Pet;
import ServerData.Models.Player.Player;

public class KamiOren extends Boss {

    public KamiOren() throws Exception {
        super(BossID.KAMIOREN, BossesData.KAMI, BossesData.OREN, BossesData.KAMIOREN);
    }

    @Override
    public void reward(Player plKill) {
           int[] itemDos = new int[]{457,1295,1296,1297};
        int[] NRs = new int[]{17, 18};
        int randomDo = new Random().nextInt(itemDos.length);
        int randomNR = new Random().nextInt(NRs.length);
        if (Util.isTrue(100, 100)) {
            if (Util.isTrue(30, 100)) {
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 2031, 1, this.location.x, this.location.y, plKill.id));
           
            } else  {
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 1504, Util.nextInt(1,3), this.location.x, this.location.y, plKill.id));
                return;
            } 
        
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
                damage = damage/10 ;
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
