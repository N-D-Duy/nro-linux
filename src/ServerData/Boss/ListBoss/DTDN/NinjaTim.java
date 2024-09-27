package ServerData.Boss.ListBoss.DTDN;
import ServerData.Boss.BossData;
import ServerData.Boss.BossID;
import ServerData.Boss.Boss;
import Server.Data.Consts.ConstPlayer;
import static ServerData.Boss.BossStatus.ACTIVE;
import static ServerData.Boss.BossStatus.JOIN_MAP;
import static ServerData.Boss.BossStatus.RESPAWN;
import ServerData.Models.Map.ItemMap;
import ServerData.Models.Map.Zone;
import ServerData.Models.Player.Player;
import ServerData.Models.Player.PlayerSkill.Skill;
import ServerData.Services.EffectSkillService;
import ServerData.Services.PlayerService;
import ServerData.Services.Service;
import ServerData.Utils.Util;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NinjaTim extends Boss {
    private static final int[][] FULL_DEMON = new int[][]{{Skill.DEMON, 1}, {Skill.DEMON, 2}, {Skill.DEMON, 3}, {Skill.DEMON, 4}, {Skill.DEMON, 5}, {Skill.DEMON, 6}, {Skill.DEMON, 7}};
  private long lastTimeHapThu;
    private int timeHapThu;
    private int initSuper = 0;
    protected Player playerAtt;
    private int timeLive = 10;
    private boolean calledNinja;
    public NinjaTim(Zone zone, int dame, int hp) throws Exception {
        super(BossID.NINJA_AO_TIM, new BossData(
                "Ninja Áo Tím", //name
                ConstPlayer.TRAI_DAT, //gender
                new short[]{123, 124, 125, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
                ((1+ dame)), //dame
                new long[]{((1+hp ))}, //hp
                new int[]{57}, //map join
                new int[][]{
                {Skill.DEMON, 3, 1}, {Skill.DEMON, 6, 2}, {Skill.DRAGON, 7, 3}, {Skill.DRAGON, 1, 4}, {Skill.GALICK, 5, 5},
                {Skill.KAMEJOKO, 7, 6}, {Skill.KAMEJOKO, 6, 7}, {Skill.KAMEJOKO, 5, 8}, {Skill.KAMEJOKO, 4, 9}, {Skill.KAMEJOKO, 3, 10}, {Skill.KAMEJOKO, 2, 11},{Skill.KAMEJOKO, 1, 12},
              {Skill.ANTOMIC, 1, 13},  {Skill.ANTOMIC, 2, 14},  {Skill.ANTOMIC, 3, 15},{Skill.ANTOMIC, 4, 16},  {Skill.ANTOMIC, 5, 17},{Skill.ANTOMIC, 6, 19},  {Skill.ANTOMIC, 7, 20},
                {Skill.MASENKO, 1, 21}, {Skill.MASENKO, 5, 22}, {Skill.MASENKO, 6, 23},
                    {Skill.KAMEJOKO, 7, 1000},},
                new String[]{}, //text chat 1
                new String[]{"|-1|Nhóc con"}, //text chat 2
                new String[]{}, //text chat 3
                60
        ));
        
        this.zone = zone;
    }
    @Override
    public void reward(Player plKill) {
        if (Util.isTrue(100, 100)) {
              if (Util.isTrue(95, 100)) {
            ItemMap it = new ItemMap(this.zone, Util.nextInt(16, 18), 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            Service.getInstance().dropItemMap(this.zone, it);
        } else {
               ItemMap it = new ItemMap(this.zone, 1465, 3, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            Service.getInstance().dropItemMap(this.zone, it);
              }
        }
    }
    @Override
    public void active() {
    super.active();
    try {
        } catch (Exception ex) {
        }
    
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
            if (this.nPoint.hp <= 15000000 && !this.calledNinja) {
                try {
            //        new NinjaClone(this.zone, 2, Util.nextInt(1000, 10000), BossID.NINJA_AO_TIM1);
            //        new NinjaClone(this.zone, 2, Util.nextInt(1000, 10000), BossID.NINJA_AO_TIM2);
            //        new NinjaClone(this.zone, 2, Util.nextInt(1000, 10000), BossID.NINJA_AO_TIM3);
            //        new NinjaClone(this.zone, 2, Util.nextInt(1000, 10000), BossID.NINJA_AO_TIM4);
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