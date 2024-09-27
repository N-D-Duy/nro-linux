package ServerData.Boss.ListBoss.CDRD;


import ServerData.Boss.Boss;
import ServerData.Boss.BossID;
import ServerData.Boss.BossStatus;
import ServerData.Boss.BossesData;
import ServerData.Boss.ListBoss.NHIEMVU.CellSaga_SieuBoHung_NV;
import ServerData.Models.Player.Player;
import ServerData.Services.EffectSkillService;
import ServerData.Models.Player.PlayerSkill.SkillService;
import ServerData.Utils.Util;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Saibamen extends Boss {
    
    private long lastTimeHP;
    private int timeHP;
    private boolean calledNinja;

    public Saibamen() throws Exception {
        super(BossID.SAIBAMEN, BossesData.BROLY_1);
    }
    
    @Override
public void active() {
    super.active();
    if(Util.canDoWithTime(st,300000)){
        this.changeStatus(BossStatus.LEAVE_MAP);
    }
    try {
            this.TuSat();
        } catch (Exception ex) {
            Logger.getLogger(CellSaga_SieuBoHung_NV.class.getName()).log(Level.SEVERE, null, ex);
        }
}

private void TuSat() throws Exception {
        if (this.nPoint.hp == 0) {
            
         SkillService.gI().selectSkill(playerTarger, 14);
            
        }
    }
    
    @Override
    public void joinMap() {
        super.joinMap();
        st= System.currentTimeMillis();
    }
    private long st;
    
    @Override
    public void moveToPlayer(Player player) {
        this.moveTo(player.location.x, player.location.y);
    }
    
     @Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = this.nPoint.subDameInjureWithDeff(damage/2);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = damage/2;
            }
            this.nPoint.subHP(damage);
            if (this.nPoint.hp >= 49999999 && !this.calledNinja) {
                try {
//                    new BrolySuper(this.zone, 2, Util.nextInt(1000, 10000), BossID.BROLY_SUPER);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                this.calledNinja = true;
            }
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
