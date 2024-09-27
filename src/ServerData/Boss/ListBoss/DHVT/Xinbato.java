package ServerData.Boss.ListBoss.DHVT;

import ServerData.Boss.BossID;
import ServerData.Boss.BossesData;
import ServerData.Models.Player.Player;

/**
 * @author TTS  
 */
public class Xinbato extends BossDHVT {

    public Xinbato(Player player) throws Exception {
        super(BossID.XINBATO, BossesData.XINBATO);
        this.playerAtt = player;
    }
}