package ServerData.Boss.ListBoss.DTDN;

import ServerData.Boss.BossData;
import ServerData.Boss.BossID;
import ServerData.Boss.Boss;
import Server.Data.Consts.ConstPlayer;
import ServerData.Models.Map.ItemMap;
import ServerData.Models.Map.Zone;
import ServerData.Models.Map.Mob.Mob;
import ServerData.Models.Map.ListMap.DoanhTrai;
import ServerData.Models.Player.Player;
import ServerData.Models.Player.PlayerSkill.Skill;
import ServerData.Services.EffectSkillService;
import ServerData.Services.Service;
import ServerData.Services.TaskService;
import ServerData.Utils.Util;


public class TrungUyTrang extends Boss {
    protected Player playerAtt;
    
    public TrungUyTrang(Zone zone,  int dame, int hp) throws Exception {
        super(BossID.TRUNG_UY_TRANG, new BossData(
                "Trung Uý Trắng", //name
                ConstPlayer.TRAI_DAT, //gender
                new short[]{141, 142, 143, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
                ((1 + dame )), //dame
                new long[]{((1 + hp))}, //hp
                new int[]{62}, //map join
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
               ItemMap it = new ItemMap(this.zone, 1465, 2, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            Service.getInstance().dropItemMap(this.zone, it);
              }
        }
    }
    @Override
    public void active() {
    super.active();
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
