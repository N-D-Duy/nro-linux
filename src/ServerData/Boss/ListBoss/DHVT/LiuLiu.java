package ServerData.Boss.ListBoss.DHVT;

import ServerData.Boss.BossData;
import ServerData.Boss.BossID;
import ServerData.Boss.BossesData;
import ServerData.Models.Player.Player;
/**
 * @author TTS  
 */
public class LiuLiu extends BossDHVT {

    public LiuLiu(Player player) throws Exception {
        super(BossID.LIU_LIU, BossesData.LIU_LIU);
        this.playerAtt = player;
    }
}