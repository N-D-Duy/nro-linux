package ServerData.Boss.ListBoss.DHVT;

import ServerData.Boss.BossID;
import ServerData.Boss.BossesData;
import ServerData.Models.Player.Player;
/**
 * @author TTS  
 */
public class ChanXu extends BossDHVT {

    public ChanXu(Player player) throws Exception {
        super(BossID.CHAN_XU, BossesData.CHAN_XU);
        this.playerAtt = player;
    }
}