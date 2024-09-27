package ServerData.Boss.ListBoss.DTDN;

//package ServerData.Boss.list_boss.doanh_trai;
//
//import ServerData.Boss.Boss;
//import ServerData.Boss.BossData;
//import com.girlkun.models.map.ItemMap;
//import com.girlkun.models.map.DoanhTrai.DoanhTrai;
//import com.girlkun.models.player.Player;
//import com.girlkun.services.Service;
//import com.girlkun.utils.Util;
//
//
//public class BossDoanhTrai extends Boss {
//
//    private DoanhTrai DoanhTrai;
//    private int xHp;
//    private int xDame;
//
//    public BossDoanhTrai(DoanhTrai DoanhTrai, int xHp, int xDame, int id, BossData... data) throws Exception {
//        super(id, data);
//        this.DoanhTrai = DoanhTrai;
//    }
//    @Override
//    public void reward(Player plKill) {
//        if (Util.isTrue(100, 100)) {
//            ItemMap it = new ItemMap(this.zone, 19, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
//                    this.location.y - 24), plKill.id);
//        Service.gI().dropItemMap(this.zone, it);
//        }
//    }
//
//    @Override
//    public void initBase() {
//        if (this.DoanhTrai.getClan() == null) {
//            return;
//        }
//        BossData data = this.data[this.currentLevel];
//        this.name = String.format(data.getName(), Util.nextInt(0, 100));
//        this.gender = data.getGender();
//        this.nPoint.mpg = 7_5_2002;
//        long totalDame = 0;
//        long totalHp = 0;
//        for (Player pl : this.DoanhTrai.getClan().membersInGame) {
//            totalDame += pl.nPoint.dame;
//            totalHp += pl.nPoint.hpMax;
//        }
//        this.nPoint.hpg = (int) (totalDame * xHp);
//        this.nPoint.dameg = (int) (totalHp / xDame);
//        this.nPoint.calPoint();
//        this.initSkill();
//        this.resetBase();
//    }
//
//}
//
///**
// * Vui lòng không sao chép mã nguồn này dưới mọi hình thức. Hãy tôn trọng tác
// * giả của mã nguồn này. Xin cảm ơn! - Duy Péo Đẹp Zai
// */
