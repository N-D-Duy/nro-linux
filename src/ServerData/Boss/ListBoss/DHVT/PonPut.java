package ServerData.Boss.ListBoss.DHVT;

import ServerData.Boss.BossData;
import ServerData.Boss.BossID;
import ServerData.Boss.BossesData;
import ServerData.Models.Player.Player;

/**
 * @author TTS  
 */
public class PonPut extends BossDHVT {

    public PonPut(Player player) throws Exception {
        super(BossID.PON_PUT, BossesData.PON_PUT);
        this.playerAtt = player;
    }
}