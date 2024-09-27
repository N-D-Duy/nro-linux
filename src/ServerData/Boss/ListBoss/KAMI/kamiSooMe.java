/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerData.Boss.ListBoss.KAMI;
import Server.Data.Consts.ConstMob;
import Server.Data.Consts.ConstPlayer;
import ServerData.Boss.Boss;
import ServerData.Boss.BossID;
import ServerData.Boss.BossManager;
import ServerData.Boss.BossStatus;
import ServerData.Boss.BossesData;
//import ServerData.Boss.list_boss.kami.kamiLoc;
import ServerData.Models.Map.ItemMap;
import ServerData.Models.Map.Mob.Mob;
import ServerData.Models.Player.Player;
import ServerData.Services.EffectSkillService;
import ServerData.Services.PlayerService;
import ServerData.Services.Service;
import ServerData.Utils.Util;


/*public class kamiSooMe extends Boss {

    public boolean callRin;

    public kamiSooMe() throws Exception {
        super(BossID.KAMI_SOOME, BossesData.KAMI_SOOME);
    }
     @Override
    public void leaveMap() {
        super.leaveMap();
        BossManager.gI().removeBoss(this);
        this.dispose();
    }
      @Override
    public void reward(Player plKill) {
        ItemMap it = new ItemMap(this.zone, 1142, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                this.location.y - 24), plKill.id);
        Service.gI().dropItemMap(this.zone, it);
    }

    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (Util.isTrue(50, 100) && plAtt != null) {//tỉ lệ hụt của thiên sứ
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
            if (damage >= 1000000) {
                damage = 10000000;
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

    public void recoverHP() {
        PlayerService.gI().hoiPhuc(this, this.nPoint.hpMax, 0);
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
}*/

/**
 * Vui lòng không sao chép mã nguồn này dưới mọi hình thức. Hãy tôn trọng tác
 * giả của mã nguồn này. Xin cảm ơn! - GirlBeo
 */
