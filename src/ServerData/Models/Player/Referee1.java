package ServerData.Models.Player;

import ServerData.Models.Map.Map;
import ServerData.Models.Map.Zone;
import ServerData.Server.Manager;
import ServerData.Services.MapService;
import ServerData.Services.Service;
import ServerData.Utils.Util;
// đây

/**
 * @author TTS 
 */
public class Referee1 extends Player {

    private long lastTimeChat;
    private long lastTimeChat1;
    private long lastTimeChat2;
    public void initReferee1() {
        init();
    }

    @Override
    public short getHead() {
        return 1530;
    }

    @Override
    public short getBody() {
        return 1531;
    }

    @Override
    public short getLeg() {
        return 1532;
    }

    public void joinMap(Zone z, Player player) {
        MapService.gI().goToMap(player, z);
      //  z.load_Me_To_Another(player);
    }

    @Override
    public void update() {
        if (this.isDie()) Service.gI().hsChar(this, this.nPoint.hpMax, this.nPoint.hpMax);
        if (Util.canDoWithTime(lastTimeChat, 7000)) {
            Service.getInstance().chat(this, "|6|Chào mừng bạn\nđã đến với\n|7|Ngọc Rồng Kamui"); 
            lastTimeChat = System.currentTimeMillis();
        }
        if (Util.canDoWithTime(lastTimeChat1, 8500)) {
            Service.getInstance().chat(this, "|7|chat\n(thongtin hoặc info)\n|-1|để xem thông tin của bạn nha");
            lastTimeChat1 = System.currentTimeMillis();
        }
        if (Util.canDoWithTime(lastTimeChat2, 10000)) {
            Service.getInstance().chat(this, "|6|Website :\n|7|YTB:KhanhDTK");
            Service.getInstance().chat(this, "|2|Chúc các bạn chơi game vui vẻ\n|7|Yêu Yêuuuu");
            lastTimeChat2 = System.currentTimeMillis();
        }
    }
    
    private void init() {
        int id2 = -1000000;
        for (Map m : Manager.MAPS) {
            if (m.mapId == 5) {
                    for (Zone z : m.zones) {
                        Referee1 pl1 = new Referee1();
                        pl1.name = "Website : YTB:KhanhDTK";
                        pl1.gender = 0;
                        pl1.id = id2++;
                        pl1.nPoint.hpMax = 1;
                        pl1.nPoint.hpg = 1;
                        pl1.nPoint.hp = 1;
                        pl1.nPoint.tlNeDon = 1000000000;
                        pl1.nPoint.setFullHpMp();
                        pl1.location.x = 178;
                        pl1.location.y = 288;
                        joinMap(z, pl1);
                        z.setReferee1(pl1);
                    }
                }
            }
        }
    }

