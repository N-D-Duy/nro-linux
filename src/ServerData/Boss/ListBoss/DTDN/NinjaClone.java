package ServerData.Boss.ListBoss.DTDN;

/*import ServerData.Boss.BossData;
import ServerData.Boss.Boss;
import ServerData.Boss.BossStatus;
import com.girlkun.consts.ConstPlayer;
import static ServerData.Boss.BossStatus.ACTIVE;
import static ServerData.Boss.BossStatus.JOIN_MAP;
import static ServerData.Boss.BossStatus.RESPAWN;
import com.girlkun.models.map.ItemMap;
import com.girlkun.models.map.Zone;
import com.girlkun.models.player.Player;
import com.girlkun.models.skill.Skill;
import com.girlkun.services.EffectSkillService;
import com.girlkun.services.PlayerService;
import com.girlkun.services.Service;
import com.girlkun.services.SkillService;
import com.girlkun.services.func.ChangeMapService;
import com.girlkun.utils.SkillUtil;
import com.girlkun.utils.Util;
import com.girlkun.models.map.challenge.MartialCongressService;


public class NinjaClone extends Boss {
  
    private long lastUpdate = System.currentTimeMillis();
    private long timeJoinMap;
    protected Player playerAtt;
    private int timeLive = 200000000;
    public NinjaClone(Zone zone , int dame, int hp,int id) throws Exception {
        super(id, new BossData(
                "Ninja Áo Tím", //name
                ConstPlayer.TRAI_DAT, //gender
                new short[]{123, 124, 125, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
                ((5000 + dame) ), //dame
                new int[]{((50000 + hp) )}, //hp
                new int[]{49}, //map join
                new int[][]{
                {Skill.DEMON, 3, 1}, {Skill.DEMON, 6, 2}, {Skill.DRAGON, 7, 3}, {Skill.DRAGON, 1, 4}, {Skill.GALICK, 5, 5},
                {Skill.KAMEJOKO, 7, 6}, {Skill.KAMEJOKO, 6, 7}, {Skill.KAMEJOKO, 5, 8}, {Skill.KAMEJOKO, 4, 9}, {Skill.KAMEJOKO, 3, 10}, {Skill.KAMEJOKO, 2, 11},{Skill.KAMEJOKO, 1, 12},
              {Skill.ANTOMIC, 1, 13},  {Skill.ANTOMIC, 2, 14},  {Skill.ANTOMIC, 3, 15},{Skill.ANTOMIC, 4, 16},  {Skill.ANTOMIC, 5, 17},{Skill.ANTOMIC, 6, 19},  {Skill.ANTOMIC, 7, 20},
                {Skill.MASENKO, 1, 21}, {Skill.MASENKO, 5, 22}, {Skill.MASENKO, 6, 23},},
                new String[]{"Đại ca đến đây"}, //text chat 1
                new String[]{"|-1|Nhóc con"}, //text chat 2
                new String[]{"Đi tù thì ăn đòn"}, //text chat 3
                60
        ));
        this.zone = zone;
    }
    @Override
    public void reward(Player plKill) {
        if (Util.isTrue(100, 100)) {
            ItemMap it = new ItemMap(this.zone, 673, 5, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            Service.getInstance().dropItemMap(this.zone, it);
        }
    }
    @Override
    public void active() {
     if (this.typePk == ConstPlayer.NON_PK) {
            this.changeToTypePK();
        } 
       try {
            switch (this.bossStatus) {
                case RESPAWN:
                    this.respawn();
                    this.changeStatus(BossStatus.JOIN_MAP);
                case JOIN_MAP:
                    joinMap();
                    if (this.zone != null) {
                        changeStatus(BossStatus.ACTIVE);
                        timeJoinMap = System.currentTimeMillis();
                        this.typePk = 3;
                        MartialCongressService.gI().sendTypePK(playerAtt, this);
                        PlayerService.gI().changeAndSendTypePK(playerAtt, ConstPlayer.PK_PVP);
                        this.changeStatus(BossStatus.ACTIVE);
                    }
                    break;
                case ACTIVE:
                    if (this.playerSkill.prepareTuSat || this.playerSkill.prepareLaze || this.playerSkill.prepareQCKK) {
                        break;
                    } else {
                        this.attack();
                    }
                    break;
            }
            if (Util.canDoWithTime(lastUpdate, 1000)) {
                lastUpdate = System.currentTimeMillis();
                if (timeLive > 0) {
                    timeLive--;
                } else {
                    super.leaveMap();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
     @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
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
} */


