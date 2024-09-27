package ServerData.Models.Item;

import ServerData.Models.Player.NPoint;
import ServerData.Models.Player.Player;
import ServerData.Services.Service;
import ServerData.Utils.Util;
import ServerData.Services.ItemTimeService;


public class ItemTime {

    //id item text
     public static final byte DOANH_TRAI = 0;
    public static final byte BAN_DO_KHO_BAU = 1;
    public static final byte KHI_GA_HUY_DIET = 2;
      public static final byte KHI_GASS = 2;
        public static final byte CON_DUONG_RAN_DOC = 3;
    // time item
    public static final int TIME_ITEM = 600000;
    public static final int TIME_ITEMSC = 600000;
    public static final int TIME_BANHTRUNGTHU = 1800000;
    public static final int TIME_OPEN_POWER = 86400000;
    public static final int TIME_MAY_DO = 1800000;
    public static final int TIME_MAY_DO2 = 1800000;
    public static final int TIME_MAY_DO3 = 1800000;
    public static final int TIME_EAT_MEAL = 600000;
 public static final byte TX = 33;
    private Player player;
    
    public boolean isUseBoHuyet;
    public boolean isUseBoKhi;
    public boolean isUseGiapXen;
    public boolean isUseCuongNo;
    public boolean isUseAnDanh;

    public boolean isUseBoHuyetSC;
    public boolean isUseBoKhiSC;
    public boolean isUseGiapXenSC;
    public boolean isUseCuongNoSC;
    public boolean isUseAnDanhSC;

    public boolean isUseBanhTrungThu;
    public boolean isUseBanhChung;
    public boolean isUseBanhTet;
    
    public long lastTimeBoHuyet;
    public long lastTimeBoKhi;
    public long lastTimeGiapXen;
    public long lastTimeCuongNo;
    public long lastTimeAnDanh;
    
    public long lastTimeBoHuyetSC;
    public long lastTimeBoKhiSC;
    public long lastTimeGiapXenSC;
    public long lastTimeCuongNoSC;
    public long lastTimeAnDanhSC;
    
    public long lastTimeUseBanhTrungThu;
    public long lastTimeUseBanhChung;
    public long lastTimeUseBanhTet;
    
    public boolean isUseMayDo;
    public long lastTimeUseMayDo;//lastime de chung 1 cai neu time = nhau
    public boolean isUseMayDo2;
    public long lastTimeUseMayDo2;
     public boolean isUseMayDo3;
    public long lastTimeUseMayDo3;
    
    public boolean isOpenPower;
    public long lastTimeOpenPower;

    public boolean isUseTDLT;
    public long lastTimeUseTDLT;
    public int timeTDLT;

    public boolean isEatMeal;
    public long lastTimeEatMeal;
    public int iconMeal;
    
    // BÁNH SÀI CHUNG
    public boolean ISBANH;
    public long LTBANH;
    public int ICONBANH;
    public static final int TIMEBANH = 3600000;
    // END LIST
    // Vé ngọc
    public boolean VENGOC;
    public long LTNGOC;
    public static final long TIMENGOC = 7200000;
    //
    public boolean istrbsd;
    public boolean istrbhp;
    public boolean istrbki;
    public static final int TIME_TRB = 1800000;
    
    public long lastTimetrbsd;
    public long lastTimetrbhp;
    public long lastTimetrbki;

    public ItemTime(Player player) {
        this.player = player;
    }

    public void update() {
        if (isEatMeal) {
            if (Util.canDoWithTime(lastTimeEatMeal, TIME_EAT_MEAL)) {
                isEatMeal = false;
                Service.gI().point(player);
            }
        }
        if (ISBANH) {
            if (Util.canDoWithTime(LTBANH, TIMEBANH)) {
                ISBANH = false;
                Service.gI().point(player);
            }
        }
        if (isUseBoHuyet) {
            if (Util.canDoWithTime(lastTimeBoHuyet, TIME_ITEM)) {
                isUseBoHuyet = false;
                Service.gI().point(player);
            }
        }
        
        if (isUseBoKhi) {
            if (Util.canDoWithTime(lastTimeBoKhi, TIME_ITEM)) {
                isUseBoKhi = false;
                Service.gI().point(player);
            }
        }
       
        if (isUseGiapXen) {
            if (Util.canDoWithTime(lastTimeGiapXen, TIME_ITEM)) {
                isUseGiapXen = false;
            }
        }
        if (isUseCuongNo) {
            if (Util.canDoWithTime(lastTimeCuongNo, TIME_ITEM)) {
                isUseCuongNo = false;
                Service.gI().point(player);
            }
        }
        if (isUseAnDanh) {
            if (Util.canDoWithTime(lastTimeAnDanh, TIME_ITEM)) {
                isUseAnDanh = false;
            }
        }
       
         if (isUseBoHuyetSC) {
            if (Util.canDoWithTime(lastTimeBoHuyetSC, TIME_ITEMSC)) {
                isUseBoHuyetSC = false;
                Service.getInstance().point(player);
//                Service.getInstance().Send_Info_NV(this.player);
            }
        }
        if (isUseBoKhiSC) {
            if (Util.canDoWithTime(lastTimeBoKhiSC, TIME_ITEMSC)) {
                isUseBoKhiSC = false;
                Service.getInstance().point(player);
            }
        }
        if (isUseGiapXenSC) {
            if (Util.canDoWithTime(lastTimeGiapXenSC, TIME_ITEMSC)) {
                isUseGiapXenSC = false;
            }
        }
        if (isUseCuongNoSC) {
            if (Util.canDoWithTime(lastTimeCuongNoSC, TIME_ITEMSC)) {
                isUseCuongNoSC = false;
                Service.getInstance().point(player);
            }
        }
        if (isUseAnDanhSC) {
            if (Util.canDoWithTime(lastTimeAnDanhSC, TIME_ITEMSC)) {
                isUseAnDanhSC = false;
            }
        }
        if (isOpenPower) {
            if (Util.canDoWithTime(lastTimeOpenPower, TIME_OPEN_POWER)) {
                player.nPoint.limitPower++;
                if (player.nPoint.limitPower > NPoint.MAX_LIMIT) {
                    player.nPoint.limitPower = NPoint.MAX_LIMIT;
                }
                Service.gI().sendThongBao(player, "Giới hạn sức mạnh của bạn đã được tăng lên 1 bậc");
                isOpenPower = false;
            }
        }
        if (isUseMayDo) {
            if (Util.canDoWithTime(lastTimeUseMayDo, TIME_MAY_DO)) {
                isUseMayDo = false;
            }
        }
        if (isUseMayDo2) {
            if (Util.canDoWithTime(lastTimeUseMayDo2, TIME_MAY_DO2)) {
                isUseMayDo2 = false;
            }
        }
        if (isUseMayDo3) {
            if (Util.canDoWithTime(lastTimeUseMayDo3, TIME_MAY_DO3)) {
                isUseMayDo3 = false;
            }
        }
        if (isUseTDLT) {
            if (Util.canDoWithTime(lastTimeUseTDLT, timeTDLT)) {
                this.isUseTDLT = false;
                ItemTimeService.gI().sendCanAutoPlay(this.player);
            }
        }
                if (istrbsd) {
            if (Util.canDoWithTime(lastTimeEatMeal, TIME_TRB)) {
                istrbsd = false;
                Service.gI().point(player);
            }
        }
         
        if (istrbhp) {
            if (Util.canDoWithTime(lastTimeBoHuyet, TIME_TRB)) {
                istrbhp = false;
                Service.gI().point(player);
            }
        }
        if (istrbki) {
            if (Util.canDoWithTime(lastTimeBoKhi, TIME_TRB)) {
                istrbki = false;
                Service.gI().point(player);
            }
        }
        if (VENGOC) {
            if (Util.canDoWithTime(LTNGOC, TIMENGOC)) {
                VENGOC = false;
            }
        }
    }
    
    public void dispose(){
        this.player = null;
    }
}
