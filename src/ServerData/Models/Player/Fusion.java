package ServerData.Models.Player;

import Server.Data.Consts.ConstPlayer;
import ServerData.Utils.Util;


public class Fusion {
    public boolean isBTC2;
    public boolean isBTC3;
    public boolean isBTC4;
    public boolean isBTC5; 
    public static final int TIME_FUSION = 600000;
    public static final int TIME_FUSION2 = 1800000;
    private Player player;
    public byte typeFusion;
    public long lastTimeFusion;

    public Fusion(Player player) {
        this.player = player;
    }

    public void update() {
        if (typeFusion == ConstPlayer.HOP_THE_GOGETA && Util.canDoWithTime(lastTimeFusion, TIME_FUSION2)) {
            this.player.pet.unFusion2();
        }
        if (typeFusion == ConstPlayer.LUONG_LONG_NHAT_THE && Util.canDoWithTime(lastTimeFusion, TIME_FUSION)) {
            this.player.pet.unFusion();
        }
    }
    
    public void dispose(){
        this.player = null;
    }

}
