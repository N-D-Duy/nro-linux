package ServerData.Models.NPC;

import ServerData.Services.ChangeMapService;
import ServerData.Services.PetService;
import ServerData.Models.Player.Player;
import ServerData.Utils.Util;
import com.girlkun.network.io.Message;
import ServerData.Services.Service;
import ServerData.Utils.Logger;
import java.io.IOException;


public class BillEgg {

    private static final long DEFAULT_TIME_DONE = 86400000L;

    private Player player;
    public long lastTimeCreate;
    public long timeDone;

    private final short id = 51;

    public BillEgg(Player player, long lastTimeCreate, long timeDone) {
        this.player = player;
        this.lastTimeCreate = lastTimeCreate;
        this.timeDone = timeDone;
    }

    public static void createBillEgg(Player player) {
        player.billEgg = new BillEgg(player, System.currentTimeMillis(), DEFAULT_TIME_DONE);
    }

    public void sendBillEgg() {
        Message msg;
        try {
            msg = new Message(-122);
            msg.writer().writeShort(this.id);
            msg.writer().writeByte(1);
            msg.writer().writeShort(4672);
            msg.writer().writeByte(0);
            msg.writer().writeInt(this.getSecondDone());
            this.player.sendMessage(msg);
            msg.cleanup();
        } catch (IOException e) {
            Logger.logException(BillEgg.class, e);
        }
    }

    public int getSecondDone() {
        int seconds = (int) ((lastTimeCreate + timeDone - System.currentTimeMillis()) / 1000);
        return seconds > 0 ? seconds : 0;
    }

    public void openEgg(int gender) {
        if (this.player.pet != null) {
            try {
                destroyEgg();
                Thread.sleep(4000);
                if (this.player.pet == null) {
                    PetService.gI().createBerusPet(this.player, gender);
                } else {
                    PetService.gI().changeBerusPet(this.player, gender);
                }
                ChangeMapService.gI().changeMapInYard(this.player, this.player.gender * 7, -1, Util.nextInt(300, 500));
                player.billEgg = null;
            } catch (Exception e) {
            }
        } else {
            Service.gI().sendThongBao(player, "Yêu cầu phải có đệ tử");
        }
    }

    public void destroyEgg() {
        try {
            Message msg = new Message(-117);
            msg.writer().writeByte(101);
            player.sendMessage(msg);
            msg.cleanup();
            Service.gI().sendThongBaoOK(player, "Dùng Dưa Thành Công Vui Lòng Đổi Lại Khu Khởi Tạo Lại..");
        } catch (IOException e) {
        }
        this.player.billEgg = null;
    }

    public void subTimeDone(int d, int h, int m, int s) {
        this.timeDone -= ((d * 24 * 60 * 60 * 1000) + (h * 60 * 60 * 1000) + (m * 60 * 1000) + (s * 1000));
        this.sendBillEgg();
    }
    
    public void dispose(){
        this.player = null;
    }
}
