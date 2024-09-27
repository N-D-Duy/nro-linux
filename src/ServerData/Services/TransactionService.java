package ServerData.Services;

import com.girlkun.database.GirlkunDB;
import Server.Connect.PlayerDAO;
import ServerData.Models.Player.Player;
import com.girlkun.network.io.Message;
import ServerData.Server.Client;
import ServerData.Server.Maintenance;
import ServerData.Services.Service;
import ServerData.Utils.Logger;
import ServerData.Utils.TimeUtil;
import ServerData.Utils.Util;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;


public class TransactionService implements Runnable {

    private static final int TIME_DELAY_TRADE = 1000;

    static final Map<Player, TradeService> PLAYER_TRADE = new HashMap<Player, TradeService>();

    private static final byte SEND_INVITE_TRADE = 0;
    private static final byte ACCEPT_TRADE = 1;
    private static final byte ADD_ITEM_TRADE = 2;
    private static final byte CANCEL_TRADE = 3;
    private static final byte LOCK_TRADE = 5;
    private static final byte ACCEPT = 7;

    private static TransactionService i;

    private TransactionService() {
    }

    public static TransactionService gI() {
        if (i == null) {
            i = new TransactionService();
            new Thread(i).start();
        }
        return i;
    }

    public void controller(Player pl, Message msg) {
        try {
            byte action = msg.reader().readByte();
            int playerId = -1;
            Player plMap = null;
            TradeService trade = PLAYER_TRADE.get(pl);
            switch (action) {
                case SEND_INVITE_TRADE:
                case ACCEPT_TRADE:
                    playerId = msg.reader().readInt();
                    plMap = pl.zone.getPlayerInMap(playerId);
                    if (plMap != null) {
                        trade = PLAYER_TRADE.get(pl);
                        if (trade == null) {
                            trade = PLAYER_TRADE.get(plMap);
                        }
                        if (trade == null) {
                            if (action == SEND_INVITE_TRADE) {
                                if (Util.canDoWithTime(pl.iDMark.getLastTimeTrade(), TIME_DELAY_TRADE)
                                        && Util.canDoWithTime(plMap.iDMark.getLastTimeTrade(), TIME_DELAY_TRADE)) {
                                    boolean checkLogout1 = false;
                                    boolean checkLogout2 = false;
                                    try (Connection con = GirlkunDB.getConnection()) {
                                        checkLogout1 = PlayerDAO.checkLogout(con, pl);
                                        checkLogout2 = PlayerDAO.checkLogout(con, plMap);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    if (checkLogout1) {
                                        Client.gI().kickSession(pl.getSession());
                                        break;
                                    }
                                    if (checkLogout2) {
                                        Client.gI().kickSession(plMap.getSession());
                                        break;
                                    }
                                    pl.iDMark.setLastTimeTrade(System.currentTimeMillis());
                                    pl.iDMark.setPlayerTradeId((int) plMap.id);
                                    sendInviteTrade(pl, plMap);
                                } else {
                                    Service.gI().sendThongBao(pl, "Thử lại sau " +
                                            TimeUtil.getTimeLeft(Math.max(pl.iDMark.getLastTimeTrade(), plMap.iDMark.getLastTimeTrade()), TIME_DELAY_TRADE / 1000));
                                }
                            } else {
                                if (plMap.iDMark.getPlayerTradeId() == pl.id) {
                                    trade = new TradeService(pl, plMap);
                                    trade.openTabTrade();
                                }
                            }
                        } else {
                            Service.gI().sendThongBao(pl, "Không thể thực hiện");
                        }
                    }
                    break;
                case ADD_ITEM_TRADE:
                    if (trade != null) {
                        byte index = msg.reader().readByte();
                        int quantity = msg.reader().readInt();
                        if(quantity<0)quantity=0;
                        if (index != -1 && quantity > TradeService.QUANLITY_MAX) {
                            Service.gI().sendThongBao(pl, "Giới hạn số lượng giao dịch là x999");
                            trade.removeItemTrade2(pl, index);
                            break;
                        }
                        trade.addItemTrade(pl, index, quantity);
                    }
                    break;
                case CANCEL_TRADE:
                    if (trade != null) {
                        trade.cancelTrade();
                    }
                    break;
                case LOCK_TRADE:
                    if (Maintenance.isRuning) {
                        trade.cancelTrade();
                        break;
                    }
                    if (trade != null) {
                        trade.lockTran(pl);
                    }
                    break;
                case ACCEPT:
                    if (Maintenance.isRuning) {
                        trade.cancelTrade();
                        break;
                    }
                    if (trade != null) {
                        trade.acceptTrade();
                        if (trade.accept == 2) {
                            trade.dispose();
                        }
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.logException(this.getClass(), e);
        }
    }

    /**
     * Mời giao dịch
     */
    private void sendInviteTrade(Player plInvite, Player plReceive) {
        Message msg;
        try {
            msg = new Message(-86);
            msg.writer().writeByte(0);
            msg.writer().writeInt((int) plInvite.id);
            plReceive.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Hủy giao dịch
     * @param player player
     */
    public void cancelTrade(Player player) {
        TradeService trade = PLAYER_TRADE.get(player);
        if (trade != null) {
            trade.cancelTrade();
        }
    }

    @Override
    public void run() {
//        while (true) {
//            try {
//                long st = System.currentTimeMillis();
//                Set<Map.Entry<Player, Trade>> entrySet = PLAYER_TRADE.entrySet();
//                for (Map.Entry entry : entrySet) {
//                    ((Trade) entry.getValue()).update();
//                }
//                Thread.sleep(300 - (System.currentTimeMillis() - st));
//            } catch (Exception e) {
//            }
//        }
    }
}
