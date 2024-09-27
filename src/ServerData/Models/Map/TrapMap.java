package ServerData.Models.Map;

import ServerData.Models.Player.Player;
import ServerData.Services.PlayerService;
import ServerData.Services.EffectMapService;
import ServerData.Utils.Util;


public class TrapMap {

    public int x;
    public int y;
    public int w;
    public int h;
    public int effectId;
    public int dame;

    public void doPlayer(Player player) {
        switch (this.effectId) {
            case 49:
                if (!player.isDie() && Util.canDoWithTime(player.iDMark.getLastTimeAnXienTrapBDKB(), 1000)&& !player.isBoss) {
                    player.injured(null, dame + (Util.nextInt(-10, 10) * dame / 100), false, false);
                    PlayerService.gI().sendInfoHp(player);
                    EffectMapService.gI().sendEffectMapToAllInMap(player.zone,
                            effectId, 2, 1, player.location.x - 32, 1040, 1);
                    player.iDMark.setLastTimeAnXienTrapBDKB(System.currentTimeMillis());
                }
                break;
        }
    }

}
