/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerData.Boss.ListBoss.KAMI;

import Server.Data.Consts.ConstPlayer;
import ServerData.Boss.Boss;
import ServerData.Boss.BossID;
import ServerData.Boss.BossStatus;
import ServerData.Boss.BossesData;
import ServerData.Boss.ListBoss.NHIEMVU.AndroidSaga_S15_NV;
import ServerData.Models.Map.ItemMap;
import ServerData.Models.Player.Player;
import ServerData.Services.PlayerService;
import ServerData.Services.Service;
import ServerData.Utils.Util;

import ServerData.Boss.Boss;
import ServerData.Boss.BossID;
import ServerData.Boss.BossManager;
import ServerData.Boss.BossesData;
import ServerData.Models.Map.ItemMap;
import ServerData.Models.Player.Player;
import ServerData.Services.EffectSkillService;
import ServerData.Services.Service;
import ServerData.Utils.Util;


/*public class kamiRin extends Boss {

    public kamiRin() throws Exception {
        super(BossID.KAMIRIN, BossesData.KAMIRIN);
    }

    @Override
    public void reward(Player plKill) {
        ItemMap it = new ItemMap(this.zone, 2044, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                this.location.y - 24), plKill.id);
        Service.gI().dropItemMap(this.zone, it);
    }
     @Override
    public void leaveMap() {
        super.leaveMap();
        BossManager.gI().removeBoss(this);
        this.dispose();
    }
 @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (Util.isTrue(30, 100) && plAtt != null) {//tỉ lệ hụt của thiên sứ
            Util.isTrue(this.nPoint.tlNeDon, 100000);
            if (Util.isTrue(1, 100)) {
                this.chat("Ta Chính Là Thần SooMe");
                this.chat("Ta Chính Là Thần SooMe");
            } else if (Util.isTrue(1, 100)) {
                this.chat("Ta Chính Là Thần SooMe");
                this.chat("Chỉ cần hoàn thiện nó!");
                this.chat("Các ngươi sẽ tránh được mọi nguy hiểm");
            } else if (Util.isTrue(1, 100)) {
                this.chat("Ta Chính Là Thần SooMe");
            }
            damage = 0;

        }
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
                 if (damage > nPoint.mpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = damage; 
                 if (damage > nPoint.tlNeDon) {
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
    public void active() {
        super.active(); //To change body of generated methods, choose Tools | Templates.
        if (Util.canDoWithTime(st, 900000)) {
            this.changeStatus(BossStatus.LEAVE_MAP);
        }
    }
     
    @Override
    public void joinMap() {
        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
        st = System.currentTimeMillis();
    }
    private long st;

} */