package ServerData.Models.NPC;

import ServerData.Services.ChangeMapService;
import ServerData.Services.PetService;
import ServerData.Models.Player.Player;
import ServerData.Utils.Util;
import com.girlkun.network.io.Message;
import ServerData.Services.Service;
import ServerData.Utils.Logger;
import java.io.IOException;


public class MabuEgg {

    private static final long DEFAULT_TIME_DONE = 7776000000L;

    private Player player;
    public long lastTimeCreate;
    public long timeDone;

    private final short id = 50;

    public MabuEgg(Player player, long lastTimeCreate, long timeDone) {
        this.player = player;
        this.lastTimeCreate = lastTimeCreate;
        this.timeDone = timeDone;
    }

    public static void createMabuEgg(Player player) {
        player.mabuEgg = new MabuEgg(player, System.currentTimeMillis(), DEFAULT_TIME_DONE);
    }

    public void sendMabuEgg() {
        Message msg;
        try {
            msg = new Message(-122);
            msg.writer().writeShort(this.id);
            msg.writer().writeByte(1);
            msg.writer().writeShort(4664);
            msg.writer().writeByte(0);
            msg.writer().writeInt(this.getSecondDone());
            this.player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(MabuEgg.class, e);
        }
    }

    public int getSecondDone() {
        int seconds = (int) ((lastTimeCreate + timeDone - System.currentTimeMillis()) / 1000);
        return seconds > 0 ? seconds : 0;
    }

    public void openEgg(int gender) {
        if (player.pet != null) {
            try {
                destroyEgg();
                Thread.sleep(4000);
                if (this.player.pet == null) {
                    PetService.gI().createMabuPet(this.player, gender);
                } else {
                    if (player.pet.inventory.itemsBody.get(0).isNotNullItem()==false&&player.pet.inventory.itemsBody.get(1).isNotNullItem()==false&&player.pet.inventory.itemsBody.get(2).isNotNullItem()==false&&player.pet.inventory.itemsBody.get(3).isNotNullItem()==false&&player.pet.inventory.itemsBody.get(4).isNotNullItem()==false&&player.pet.inventory.itemsBody.get(5).isNotNullItem()==false&&player.pet.inventory.itemsBody.get(6).isNotNullItem()==false&&player.pet.inventory.itemsBody.get(7).isNotNullItem()==false) {
                        PetService.gI().changeMabuPet(this.player, gender);
                    } else {
                        Service.gI().sendThongBao(player, "Vui lòng tháo đồ!");
                    }
                }
                ChangeMapService.gI().changeMapInYard(this.player, this.player.gender * 7, -1, Util.nextInt(300, 500));
                player.mabuEgg = null;
            } catch (InterruptedException e) {
            }
        } else {
            Service.gI().sendThongBao(player, "Yêu cầu phải có đệ tử!");
        }
    }

    public void destroyEgg() {
        try {
            Message msg = new Message(-117);
            msg.writer().writeByte(101);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (IOException e) {
        }
        this.player.mabuEgg = null;
    }

    public void subTimeDone(int d, int h, int m, int s) {
        this.timeDone -= ((d * 24 * 60 * 60 * 1000) + (h * 60 * 60 * 1000) + (m * 60 * 1000) + (s * 1000));
        this.sendMabuEgg();
    }
    
    public void dispose(){
        this.player = null;
    }
}
