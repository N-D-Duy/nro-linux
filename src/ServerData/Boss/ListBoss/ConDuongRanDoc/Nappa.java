package ServerData.Boss.ListBoss.ConDuongRanDoc;



import ServerData.Boss.BossData;
import ServerData.Boss.BossManager;
import ServerData.Boss.BossID;
import ServerData.Boss.Boss;
import Server.Data.Consts.ConstPlayer;
import ServerData.Models.Map.ItemMap;
import ServerData.Models.Map.Zone;
import  ServerData.Models.Player.Player;
import ServerData.Models.Player.PlayerSkill.Skill;
import ServerData.Services.EffectSkillService;
import ServerData.Services.Service;
import ServerData.Utils.Util;


public class Nappa extends Boss {
    private static final int[][] FULL_DEMON = new int[][]{{Skill.DEMON, 1}, {Skill.DEMON, 2}, {Skill.DEMON, 3}, {Skill.DEMON, 4}, {Skill.DEMON, 5}, {Skill.DEMON, 6}, {Skill.DEMON, 7}};

    public Nappa(Zone zone , int level, long dame, long hp) throws Exception {
        super(BossID.ANDROID_13, new BossData(
                "Nađíc",
                ConstPlayer.TRAI_DAT,
                new short[]{648, 649, 650, -1, -1, -1},
                ((10000 + dame) * level),
                new long[]{((40000000L + hp) * level)},
                new int[]{103},
                (int[][]) Util.addArray(FULL_DEMON),
                new String[]{},
                new String[]{"|-1|Nhóc con"},
                new String[]{},
                60
        ));
        this.zone = zone;
    }
     @Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = this.nPoint.subDameInjureWithDeff(damage);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = 1;
            }
            this.nPoint.subHP(damage);
             long bossDamage = (20 * plAtt.clan.ConDuongRanDoc.level);
            long bossMaxHealth = (2 * plAtt.clan.ConDuongRanDoc.level);
            bossDamage = Math.min(bossDamage, 200000000L);
            bossMaxHealth = Math.min(bossMaxHealth, 2000000000L);
            if (this.nPoint.hp == 0) {
                try {
                    
                    new Vegeta(this.zone, plAtt.clan.ConDuongRanDoc.level, bossDamage, bossMaxHealth);
                    
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
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
     @Override
    public void reward(Player plKill) {
     
           if (Util.isTrue(100, 100)) {
               if (plKill.clan.ConDuongRanDoc != null && plKill.clan.ConDuongRanDoc.level == 110){
                    if (Util.isTrue(80, 100)) {
                   ItemMap it = new ItemMap(this.zone, Util.nextInt(16, 18), 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
                     Service.getInstance().dropItemMap(this.zone, it);
                    }else {
                        ItemMap it = new ItemMap(this.zone, 1465, 5, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            Service.getInstance().dropItemMap(this.zone, it);
                    }
                     
               } else if (plKill.clan.ConDuongRanDoc != null && plKill.clan.ConDuongRanDoc.level != 110){
                   if (Util.isTrue(95, 100)) {
                   ItemMap it = new ItemMap(this.zone, Util.nextInt(16, 18), 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
                     Service.getInstance().dropItemMap(this.zone, it);
                    }else {
                        ItemMap it = new ItemMap(this.zone, 1465, 5, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            Service.getInstance().dropItemMap(this.zone, it);
                    }
             
        }
    }
    }
  @Override
    public void active() {
        super.active();
    }

    @Override
    public void joinMap() {
        super.joinMap();
    }

    @Override
    public void leaveMap() {
        super.leaveMap();
        BossManager.gI().removeBoss(this);
        this.dispose();
    }
}