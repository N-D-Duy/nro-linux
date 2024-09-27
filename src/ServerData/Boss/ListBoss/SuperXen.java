/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerData.Boss.ListBoss;

import Server.Data.Consts.ConstPlayer;
import ServerData.Boss.Boss;
import ServerData.Boss.BossID;
import ServerData.Boss.BossManager;
import ServerData.Boss.BossStatus;
import ServerData.Boss.BossesData;
import ServerData.Models.Map.ItemMap;
import ServerData.Models.Player.Player;
import ServerData.Services.EffectSkillService;
import ServerData.Services.PlayerService;
import ServerData.Services.Service;
import ServerData.Services.TaskService;
import ServerData.Utils.Util;
import java.util.Random;


public class SuperXen extends Boss {
private long lastTimeHapThu;
    private int timeHapThu;
    public SuperXen() throws Exception {
        
        super(BossID.SUPER_XEN, BossesData.SUPER_XEN);
    }

    @Override
    public void reward(Player plKill) {
        int[] itemDosTL = new int[]{555, 557, 559,556, 558, 560,562, 564, 566,563, 565, 567};
        int randomDoTL = new Random().nextInt(itemDosTL.length);
        if(Util.isTrue(30,100)){
        ItemMap it = new ItemMap(this.zone, 16, 1, this.location.x, this.location.y, plKill.id);
        Service.gI().dropItemMap(this.zone, it);
        TaskService.gI().checkDoneTaskKillBoss(plKill, this);
    } else if (Util.isTrue(12, 100)) {
              Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, itemDosTL[randomDoTL], 1, this.location.x, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id));
     }else  if(Util.isTrue(10,100)){
         ItemMap it = new ItemMap(this.zone, 15, 1, this.location.x, this.location.y, plKill.id);
        Service.gI().dropItemMap(this.zone, it);
        }
         ItemMap it = new ItemMap(this.zone, 17, 1, this.location.x, this.location.y, plKill.id);
         Service.gI().dropItemMap(this.zone, it);
    }
    @Override
    public void active() {
        if (this.typePk == ConstPlayer.NON_PK) {
            this.changeToTypePK();
        }
        this.hapThu();
        this.attack();
    }
   
    private void hapThu() {
        if (!Util.canDoWithTime(this.lastTimeHapThu, this.timeHapThu) || !Util.isTrue(1, 100)) {
            return;
        }
    
        Player pl = this.zone.getRandomPlayerInMap();
        if (pl == null || pl.isDie()) {
            return;
        }
        this.nPoint.dameg += (pl.nPoint.dame * 5 / 100);
        this.nPoint.hpg += (pl.nPoint.hp * 2 / 100);
        this.nPoint.critg++;
        this.nPoint.calPoint();
        PlayerService.gI().hoiPhuc(this, pl.nPoint.hp, 0);
        pl.injured(null, pl.nPoint.hpMax, true, false);
        Service.gI().sendThongBao(pl, "Bạn vừa bị " + this.name + " hấp thu!");
        this.chat(2, "Ui cha cha, kinh dị quá. " + pl.name + " vừa bị tên " + this.name + " nuốt chửng kìa!!!");
        this.chat("Haha, ngọt lắm đấy " + pl.name + "..");
        this.lastTimeHapThu = System.currentTimeMillis();
        this.timeHapThu = Util.nextInt(30000, 70000);
    }
   @Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (Util.isTrue(80, 100) && plAtt != null) {//tỉ lệ hụt của thiên sứ
            Util.isTrue(this.nPoint.tlNeDon, 10000);
            if (Util.isTrue(1, 100)) {
                this.chat("Ta Chính Là Thần");
                this.chat("Ta Chính Là Thần");
            } else if (Util.isTrue(1, 100)) {
                this.chat("Ta Chính Là Thần");
                this.chat("Chỉ cần hoàn thiện nó!");
                this.chat("Các ngươi sẽ tránh được mọi nguy hiểm");
            } else if (Util.isTrue(1, 100)) {
                this.chat("Ta Chính Là Thần");
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
            if (damage >= 1) {
                damage /= 15/10;
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
    
    @Override
    public void leaveMap() {
        super.leaveMap();
        BossManager.gI().removeBoss(this);
        super.dispose();
        
    }
}
