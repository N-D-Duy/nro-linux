//package ServerData.Boss.ListBoss.KGHD;
//
//import ServerData.Boss.BossData;
//import ServerData.Boss.BossManager;
//import ServerData.Boss.BossID;
//import ServerData.Boss.Boss;
//import Server.Data.Consts.ConstPlayer;
//import static ServerData.Models.Item.ItemTime.CON_DUONG_RAN_DOC;
//import static ServerData.Models.Item.ItemTime.KHI_GA_HUY_DIET;
//import ServerData.Models.Map.ListMap.BanDoKhoBau;
//import ServerData.Models.Map.ItemMap;
//import ServerData.Models.Map.Zone;
//import ServerData.Models.Player.Player;
//import ServerData.Models.Player.PlayerSkill.Skill;
//import ServerData.Services.ChangeMapService;
//import ServerData.Services.EffectSkillService;
//import ServerData.Services.ItemTimeService;
//import ServerData.Services.Service;
//import ServerData.Utils.Util;
//
//
//public class Hatchiyack extends Boss {
//    private static final int[][] FULL_DEMON = new int[][]{{Skill.DEMON, 1}, {Skill.DEMON, 2}, {Skill.DEMON, 3}, {Skill.DEMON, 4}, {Skill.DEMON, 5}, {Skill.DEMON, 6}, {Skill.DEMON, 7}};
//
//    public Hatchiyack(Zone zone , int level, long dame, long hp) throws Exception {
//        super(BossID.TRUNG_UY_TRANG, new BossData(
//                "Hatchiyack",
//                ConstPlayer.TRAI_DAT,
//                new short[]{1437	,1438	,1439, -1, -1, -1},
//                ((10000 + dame) * level),
//              new long[]{((50000000L + hp) * level)},
//                new int[]{103},
//                (int[][]) Util.addArray(FULL_DEMON),
//                new String[]{},
//                new String[]{"|-1|Nhóc con"},
//                new String[]{},
//                60
//        ));
//        this.zone = zone;
//    }
//@Override
//    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
//        if (!this.isDie()) {
//            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
//                this.chat("Xí hụt");
//                return 0;
//            }
//            damage = this.nPoint.subDameInjureWithDeff(damage/2);
//            if (!piercing && effectSkill.isShielding) {
//                if (damage > nPoint.hpMax) {
//                    EffectSkillService.gI().breakShield(this);
//                }
//                damage = damage/2;
//            }
//            this.nPoint.subHP(damage);
//            if (this.nPoint.hp == 0) {
//                try {
//                    
//                    
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//            }
//            if (isDie()) {
//this.setDie(plAtt);
//die(plAtt);
//}
//            return damage;
//        } else {
//            return 0;
//        }
//    }
//
//       @Override
//    public void reward(Player plKill) {
//     
//           if (Util.isTrue(100, 100)) {
//               if (plKill.clan.KhiGaHuyDiet != null && plKill.clan.KhiGaHuyDiet.level == 110){
//                    if (Util.isTrue(80, 100)) {
//                   ItemMap it = new ItemMap(this.zone, Util.nextInt(16, 18), 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
//                    this.location.y - 24), plKill.id);
//                     Service.getInstance().dropItemMap(this.zone, it);
//                    }else {
//                        ItemMap it = new ItemMap(this.zone, 1465, 5, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
//                    this.location.y - 24), plKill.id);
//            Service.getInstance().dropItemMap(this.zone, it);
//                    }
//                     
//               } else if (plKill.clan.KhiGaHuyDiet != null && plKill.clan.KhiGaHuyDiet.level != 110){
//                   if (Util.isTrue(95, 100)) {
//                   ItemMap it = new ItemMap(this.zone, Util.nextInt(16, 18), 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
//                    this.location.y - 24), plKill.id);
//                     Service.getInstance().dropItemMap(this.zone, it);
//                    }else {
//                        ItemMap it = new ItemMap(this.zone, 1465, 5, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
//                    this.location.y - 24), plKill.id);
//            Service.getInstance().dropItemMap(this.zone, it);
//                    }
//             
//        }
//    }
//                for (Player pl : plKill.clan.membersInGame) {
//            ItemTimeService.gI().sendTextTime(pl, (byte) KHI_GA_HUY_DIET, "Khi Gas Hủy diệt sắp kết thúc : ", 30);
//            ChangeMapService.gI().goToHome(pl);
//            pl.clan.KhiGaHuyDiet = null;
//            
//            pl.clan.haveGoneGas = true;
//        }
//    }
//    @Override
//    public void active() {
//        super.active();
//    }
//
//    @Override
//    public void joinMap() {
//        super.joinMap();
//    }
//
//    @Override
//    public void leaveMap() {
//        super.leaveMap();
//        BossManager.gI().removeBoss(this);
//        this.dispose();
//    }
//}