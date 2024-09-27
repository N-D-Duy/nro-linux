package ServerData.Boss.ListBoss.BROLY;

import Server.Data.Consts.ConstPlayer;
import ServerData.Boss.*;
import ServerData.Models.Map.Zone;
import ServerData.Models.Player.Player;
import ServerData.Models.Player.PlayerSkill.Skill;
import ServerData.Services.EffectSkillService;
import ServerData.Services.PetService;
import ServerData.Services.Service;
import ServerData.Utils.Util;

public class SuperBroly extends Boss {

    private long lastUpdate = System.currentTimeMillis();
    private long timeJoinMap;
    protected Player playerAtt;
    private int timeLive = 200000000;
    public int petgender = Util.nextInt(0,2);
    public Player mypett;


    public SuperBroly(BossData bossData) throws Exception {
        super(Util.randomBossId(), bossData);
    }
    // public Superbroly(Zone zone,long hp,long dame) throws Exception {
    //     super(Util.randomBossId(), BossesData.SUPER_BROLY);
    //     this.zone = zone;
    //     this.nPoint.hpg = hp;
    //     this.nPoint.dame =dame;
    // }

    public SuperBroly(Zone zone, long dame, long hp) throws Exception {
        super(Util.randomBossId(), new BossData(
                "Super Broly", //name
                ConstPlayer.TRAI_DAT, //gender
                new short[]{1318	,1319,	1320, 28, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
                ((5000 + dame)), //dame
                new long[]{((Util.nextInt(1000000000, 2000000000) + hp))}, //hp
                new int[]{zone.map.mapId}, //map join
                new int[][]{
                    {Skill.LIEN_HOAN, 7, 2000},
                    {Skill.DRAGON, 7, 2000},
                    {Skill.KAMEJOKO, 7, 2000},
                    {Skill.MASENKO, 7, 2000},
                    {Skill.ANTOMIC, 7, 2000},
                    {Skill.TAI_TAO_NANG_LUONG, 1, 15000},},
                new String[]{
                    "|-1|Gaaaaaa",
                    "|-2|Tới đây đi!"
                }, //text chat 1
                new String[]{"|-1|Các ngươi tới số rồi mới gặp phải ta",
                    "|-1|Gaaaaaa",
                    "|-2|Không ngờ..Hắn mạnh cỡ này sao..!!"
                }, //text chat 2
                new String[]{"|-1|Gaaaaaaaa!!!"}, //text chat 3
                1_000_000
                
        ));
        this.zone = zone;
    }
    @Override
    public void reward(Player plKill) {
        if (plKill.pet != null) {
            if (Util.isTrue(100, 100)) {
                  if (Util.isTrue(80, 100)) {
                       Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 1990, 1, this.location.x, this.location.y, plKill.id));
                return;
                  } else  if (Util.isTrue(18, 100)) {
                          Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 1989, 1, this.location.x, this.location.y, plKill.id));
                return;
                  }else  if (Util.isTrue(2, 100)) {
                            Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 1992, 1, this.location.x, this.location.y, plKill.id));
                return;
                  }
        }
        }
        
        if (plKill.pet == null) {     
            if (Util.isTrue(100, 100)) {
                  if (Util.isTrue(70, 100)) {
                       Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 1990, 1, this.location.x, this.location.y, plKill.id));
                return;
                  } else  if (Util.isTrue(20, 100)) {
                          Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 1989, 1, this.location.x, this.location.y, plKill.id));
                return;
                  }else  if (Util.isTrue(10, 100)) {
                            Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 1992, 1, this.location.x, this.location.y, plKill.id));
                return;
                  }
        }
        }
        this.pet = null;
    }

    @Override
    public void active() {
        super.active();
        if (this.pet == null) {
            PetService.gI().createNormalPet(this,petgender);
            this.pet.nPoint.tlNeDon = 20000000;
        }
    }

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
              if(damage > this.nPoint.hpMax*0.3)  
                  damage = this.nPoint.hpMax*3/10;
            }
            this.nPoint.subHP(damage);
            if (isDie()) {
                this.pet.dispose();
                this.pet = null;
                this.setDie(plAtt);
                die(plAtt);
               BossManager.gI().createBoss(BossID.BROLY_THUONG);
            }
            return damage;
        } else {
            return 0;
        }
    }
    @Override
    public void leaveMap() {
        super.leaveMap();
        BossManager.gI().removeBoss(this);
        this.dispose();
    }
    @Override
    public void joinMap() {
        super.joinMap();
    }
    
}
