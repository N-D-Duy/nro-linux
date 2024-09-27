package ServerData.Boss.ListBoss.BDKB;
import ServerData.Boss.BossData;
import ServerData.Boss.BossManager;
import ServerData.Boss.BossID;
import ServerData.Boss.Boss;
import Server.Data.Consts.ConstPlayer;
import ServerData.Models.Map.ListMap.BanDoKhoBau;
import ServerData.Models.Map.ItemMap;
import ServerData.Models.Map.Zone;
import ServerData.Models.Player.Player;
import ServerData.Models.Player.PlayerSkill.Skill;
import ServerData.Services.Service;
import ServerData.Utils.Util;


public class TrungUyXanhLo extends Boss {
    private static final int[][] FULL_DEMON = new int[][]{{Skill.DEMON, 1}, {Skill.DEMON, 2}, {Skill.DEMON, 3}, {Skill.DEMON, 4}, {Skill.DEMON, 5}, {Skill.DEMON, 6}, {Skill.DEMON, 7}};

    public TrungUyXanhLo(Zone zone , int level, int dame, int hp) throws Exception {
        super(BossID.TRUNG_UY_TRANG, new BossData(
                "Trung úy xanh lơ",
                ConstPlayer.TRAI_DAT,
                new short[]{135, 136, 137, -1, -1, -1},
                ((10000 + dame) * level),
                new long[]{((50000000L + hp) * level)},
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
    public void reward(Player plKill) {
              if (Util.isTrue(100, 100)) {
               if (plKill.clan.BanDoKhoBau != null && plKill.clan.BanDoKhoBau.level == 110){
                    if (Util.isTrue(80, 100)) {
                   ItemMap it = new ItemMap(this.zone, Util.nextInt(16, 18), 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
                     Service.getInstance().dropItemMap(this.zone, it);
                    }else {
                        ItemMap it = new ItemMap(this.zone, 1465, 5, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            Service.getInstance().dropItemMap(this.zone, it);
              ItemMap it1 = new ItemMap(this.zone, 1246, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            Service.getInstance().dropItemMap(this.zone, it1);
                    }
                     
               } else if (plKill.clan.BanDoKhoBau != null && plKill.clan.BanDoKhoBau.level != 110){
                   if (Util.isTrue(95, 100)) {
                   ItemMap it = new ItemMap(this.zone, Util.nextInt(16, 18), 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
                     Service.getInstance().dropItemMap(this.zone, it);
                    }else {
                        ItemMap it = new ItemMap(this.zone, 1465, 3, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
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