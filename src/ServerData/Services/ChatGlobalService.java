package ServerData.Services;

import ServerData.Models.Player.Player;
import com.girlkun.network.io.Message;
import ServerData.Server.MySession;
import ServerData.Utils.Logger;
import ServerData.Utils.TimeUtil;
import ServerData.Utils.Util;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class ChatGlobalService implements Runnable {

    private static int COUNT_CHAT = 50;
    private static int COUNT_WAIT = 50;
    private static ChatGlobalService i;

    private List<ChatGlobal> listChatting;
    private List<ChatGlobal> waitingChat;

    private ChatGlobalService() {
        this.listChatting = new ArrayList<>();
        this.waitingChat = new LinkedList<>();
        new Thread(this, "**Chat Global").start();
    }

    public static ChatGlobalService gI() {
        if (i == null) {
            i = new ChatGlobalService();
        }
        return i;
    }

    public void chat(Player player, String text) {
        if (player.getSession().actived == 0) {
            Service.gI().sendThongBaoFromAdmin(player,
                    "|5|Vui Lòng Kích Hoạt Thành Viên");
            return;
        }
        if (waitingChat.size() >= COUNT_WAIT) {
            Service.gI().sendThongBao(player, "Kênh thế giới hiện đang quá tải, không thể chat lúc này");
            return;
        }
        boolean haveInChatting = false;
        for (ChatGlobal chat : listChatting) {
            if (chat.text.equals(text)) {
                haveInChatting = true;
                break;
            }
        }
        if (haveInChatting) {
            return;
        }

        if (player.inventory.ruby >= 5) {
            if ( player.isAdmin2() || Util.canDoWithTime(player.iDMark.getLastTimeChatGlobal(), 360000)) {
                if (player.isAdmin2() || player.nPoint.power > 2000000000) {
                    player.inventory.subRuby(5); 
                    Service.gI().sendMoney(player);
                    player.iDMark.setLastTimeChatGlobal(System.currentTimeMillis());
                    waitingChat.add(new ChatGlobal(player, text.length() > 100 ? text.substring(0, 100) : text));
                } else {
                    Service.gI().sendThongBao(player, "Sức mạnh phải ít nhất 2tỷ mới có thể chat thế giới");
                }
            } else {
                Service.gI().sendThongBao(player, "Không thể chat thế giới lúc này, vui lòng đợi "
                        + TimeUtil.getTimeLeft(player.iDMark.getLastTimeChatGlobal(), 240));
            }
        } else {
            Service.gI().sendThongBao(player, "Không đủ ngọc chat thế giới");
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (!listChatting.isEmpty()) {
                    ChatGlobal chat = listChatting.get(0);
                    if (Util.canDoWithTime(chat.timeSendToPlayer, 1000)) {
                        listChatting.remove(0).dispose();
                    }
                }

                if (!waitingChat.isEmpty()) {
                    ChatGlobal chat = waitingChat.get(0);
                    if (listChatting.size() < COUNT_CHAT) {
                        waitingChat.remove(0);
                        chat.timeSendToPlayer = System.currentTimeMillis();
                        listChatting.add(chat);
                        chatGlobal(chat);
                    }
                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Logger.logException(ChatGlobalService.class, e);
            }
        }
    }

    private void chatGlobal(ChatGlobal chat) {
        Message msg;
        try {
            msg = new Message(92);
            msg.writer().writeUTF(chat.playerName);
            msg.writer().writeUTF("|5|" + chat.text);
            msg.writer().writeInt((int) chat.playerId);
            msg.writer().writeShort(chat.head);
            msg.writer().writeShort(-1);
            msg.writer().writeShort(chat.body);
            msg.writer().writeShort(chat.bag); //bag
            msg.writer().writeShort(chat.leg);
            msg.writer().writeByte(0);
            Service.gI().sendMessAllPlayer(msg);
            msg.cleanup();
        } catch (IOException e) {
            System.err.print("Lỗi Chat Global");
        }
    }

    private void transformText(ChatGlobal chat) {
        String text = chat.text;
        text = text.replaceAll("Sang", "***")
                .replaceAll("đm", "**");
        chat.text = text;
    }

    private class ChatGlobal {

        public String playerName;
        public int playerId;
        public short head;
        public short body;
        public short leg;
        public short bag;
        public String text;
        public long timeSendToPlayer;
    
        public ChatGlobal(Player player, String text) {
            this.playerName = player.name;
            this.playerId = (byte) player.id;
            this.head = player.getHead();
            this.body = player.getBody();
            this.leg = player.getLeg();
            this.bag = player.getFlagBag();
            this.text = text;
            transformText(this);
        }

        private void dispose() {
            this.playerName = null;
            this.text = null;
        }

    }

}
