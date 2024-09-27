package ServerData.Models.PVP;

import ServerData.Models.PVP.PVP;
import ServerData.Models.PVP.TYPE_LOSE_PVP;
import ServerData.Models.PVP.TYPE_PVP;
import ServerData.Models.Player.Player;
import ServerData.Services.Service;
import ServerData.Services.ChangeMapService;
import ServerData.Utils.Util;

public class TraThu extends PVP {

    public TraThu(Player p1, Player p2) {
        super(TYPE_PVP.TRA_THU, p1, p2);
    }

    @Override
    public void start() {
        ChangeMapService.gI().changeMapInYard(p1,
                p2.zone.zoneId,
                p2.location.x + Util.nextInt(-5, 5), p2.location.y);
        Service.gI().sendThongBao(p2, "Có người tìm tới bạn để trả thù");
        Service.gI().chat(p1, "Mày Tới Số Rồi Con Ạ, Cho Mày Đi Đái Luôn!");
        super.start();
    }

    @Override
    public void finish() {
        
    }

    @Override
    public void update() {
        
    }

    @Override
    public void reward(Player plWin) {

    }

    @Override
    public void sendResult(Player plLose, TYPE_LOSE_PVP typeLose) {

    }

}

/**
 * Vui lòng không sao chép mã nguồn này dưới mọi hình thức. Hãy tôn trọng tác
 * giả của mã nguồn này. Xin cảm ơn! - GirlBeo
 */
