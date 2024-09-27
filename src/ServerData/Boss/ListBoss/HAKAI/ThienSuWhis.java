package ServerData.Boss.ListBoss.HAKAI;

import ServerData.Boss.Boss;
import ServerData.Boss.BossStatus;
import ServerData.Boss.BossesData;
import ServerData.Models.Player.Player;
import ServerData.Services.EffectSkillService;
import ServerData.Services.Service;
import ServerData.Utils.Util;
import java.util.Random;

public class ThienSuWhis extends Boss {

    public ThienSuWhis() throws Exception {
        super(Util.randomBossId(), BossesData.THIEN_SU_WHIS);
    }

    @Override
    public void reward(Player plKill) {
        int[] manhthuong = new int[]{1065,1066,1068};
        int[] manhhiem = new int[]{1997,1070};

        int randomAWJ = new Random().nextInt(manhthuong.length);
        int randomGR = new Random().nextInt(manhhiem.length);
        if (Util.isTrue(95, 100)) {
            Service.gI().dropItemMap(this.zone, Util.manhTS(zone, manhthuong[randomAWJ], 1, this.location.x, this.location.y, plKill.id));
        } else {
            Service.gI().dropItemMap(this.zone, Util.manhTS(zone, manhhiem[randomGR], 1, this.location.x, this.location.y, plKill.id));
        }
    }

    @Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (Util.isTrue(30, 100) && plAtt != null) {//tỉ lệ hụt của thiên sứ
            Util.isTrue(this.nPoint.tlNeDon, 10000);
            if (Util.isTrue(1, 100)) {
                this.chat("Hãy để bản năng tự vận động");
                this.chat("Tránh các động tác thừa");
            } else if (Util.isTrue(1, 100)) {
                this.chat("Chậm lại,các ngươi quá nhanh rồi");
                this.chat("Chỉ cần hoàn thiện nó!");
                this.chat("Các ngươi sẽ tránh được mọi nguy hiểm");
            } else if (Util.isTrue(1, 100)) {
                this.chat("Đây chính là bản năng vô cực");
            }
            damage = 0;

        }
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 100)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = this.nPoint.subDameInjureWithDeff(damage);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
            }
            if (damage > 1) damage /= 15/10;
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
