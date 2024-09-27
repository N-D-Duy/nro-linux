package ServerData.Server;

import ServerData.Services.Giftcode.GiftcodeManager;
import com.girlkun.database.GirlkunDB;

import java.net.ServerSocket;

import Server.Connect.HistoryTrade;
import Server.Data.Consts.ConstEvent;
import ServerData.Boss.Boss;
import ServerData.Boss.BossManager;
import ServerData.Models.Map.ListMap.SieuHangManager;
import ServerData.Models.Map.Zone;
import ServerData.Models.PVP.Matches.MartialCongressManager;

import com.girlkun.network.session.ISession;
import com.girlkun.network.example.MessageSendCollect;
import com.girlkun.network.server.GirlkunServer;
import com.girlkun.network.server.ISessionAcceptHandler;
import ServerData.Services.ClanService;
//import ServerData.Services.NgocRongNamecService;
//import ServerData.Services.Func.TaiXiu.ChonAiDay;
//import ServerData.Services.Func.TaiXiu.TaiXiu_Old;
import ServerData.Utils.GetSizeObject;
import ServerData.Utils.Logger;
import ServerData.Utils.TimeUtil;
import ServerData.Models.Shop.ShopKyGuiManager;
import static ServerData.Server.Manager.MAPS;
import ServerData.Services.MapService;
import ServerData.Services.Service;
import ServerData.Utils.Util;
import com.girlkun.result.GirlkunResultSet;

import java.util.*;
import java.util.logging.Level;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class ServerManager {
    private static final long DELAY = 8000; // Thời gian delay là 8000ms (8 giây)
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    // private static boolean isRunning = true; // Đặt isRunning theo nhu cầu của
    // ứng dụng của bạn

    public static String timeStart;

    public static final Map CLIENTS = new HashMap();

    public static String NAME = "Tsneee";
    public static int PORT = 14445;

    private static ServerManager instance;

    public static ServerSocket listenSocket;
    public static boolean isRunning;

    public void init() {
        Manager.gI();
        GiftcodeManager.gI().init();
        ConstEvent.gI().init();
        try {
            if (Manager.LOCAL)
                return;
            GirlkunDB.executeUpdate("update account set last_time_login = '2000-01-01', "
                    + "last_time_logout = '2001-01-01'");
        } catch (Exception e) {
        }
        HistoryTrade.deleteHistory();
    }

    private long TimeTop;

    public static ServerManager gI() {
        if (instance == null) {
            instance = new ServerManager();
            instance.init();
        }
        return instance;
    }

    public long getNumPlayer() {
        long num = 0;
        try {
            GirlkunResultSet rs = GirlkunDB.executeQuery("SELECT COUNT(*) FROM `player`");
            rs.first();
            num = rs.getLong(1);
        } catch (Exception e) {
        }
        return num;
    }

    public static void main(String[] args) {
        timeStart = TimeUtil.getTimeNow("dd/MM/yyyy HH:mm:ss");
        ServerManager.gI().run();
    }

    public void run() {
        long delay = 500;
        isRunning = true;
        activeCommandLine();
        activeGame();
        activeServerSocket();
        // TaiXiu_Old.gI().lastTimeEnd = System.currentTimeMillis() + 50000;
        // new Thread(NauBanh.gI(), "NauBanh").start();
        // new Thread(ChonAiDay.gI() , "CAD").start();
        // new Thread(TaiXiu_Old.gI() , "TaiXiu").start();
        // NgocRongNamecService.gI().initNgocRongNamec((byte)0);
        // new Thread(NgocRongNamecService.gI() , "NRNM").start();
        new Thread(() -> {
            while (isRunning) {
                try {
                    long start = System.currentTimeMillis();
                    MartialCongressManager.gI().update();
                    long timeUpdate = System.currentTimeMillis() - start;
                    if (timeUpdate < delay) {
                        Thread.sleep(delay - timeUpdate);
                    }
                } catch (InterruptedException e) {
                }
            }
        }, "DHVT").start();
        new Thread(() -> {
            try {
                while (true) {
                    for (int i = 0; i < 75; i++) {
                        Boss b = BossManager.gI().bosses.get(i);
                        Thread.sleep(10000);
                        if (b != null && !b.isDie() && b.zone != null
                                && !MapService.gI().isMapMaBu(b.data[0].getMapJoin()[0])
                                && !MapService.gI().isMapBlackBallWar(b.data[0].getMapJoin()[0])
                                && !MapService.gI().isMapDoanhTrai(b.data[0].getMapJoin()[0])
                                && !MapService.gI().isMapConDuongRanDoc(b.data[0].getMapJoin()[0])
                                && !MapService.gI().isMapKhiGaHuyDiet(b.data[0].getMapJoin()[0])) {
                            b.notifyJoinMap();
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Continue Boss");
            }
        }, "TBB").start();
        SieuHangManager.gI().update();

        try {
            Thread.sleep(1000);
            BossManager.gI().loadBoss();
            Manager.MAPS.forEach(ServerData.Models.Map.Map::initBoss);
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(BossManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        new Thread(() -> {
            while (isRunning) {
                try {
                    long start = System.currentTimeMillis();
                    ShopKyGuiManager.gI().save();
                    MartialCongressManager.gI().update();
                    SieuHangManager.gI().update();
                    // Player player = null;
                    // for (int i = 0; i < Client.gI().getPlayers().size(); ++i) {
                    // if (Client.gI().getPlayers().get(i) != null) {
                    // player = (Client.gI().getPlayers().get(i));
                    // PlayerDAO.updatePlayer(player);
                    // }
                    // }

                    long timeUpdate = System.currentTimeMillis() - start;
                    if (timeUpdate < delay) {
                        Thread.sleep(delay - timeUpdate);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Logger.logException(Manager.class, e, "Lỗi running sever");
                }
            }
        }, "Update dai hoi vo thuat").start();

    }

    private void act() throws Exception {
        Logger.log(Logger.RED_BOLD, "ServerManager Is Runing!\n");
        GirlkunServer.gI().init().setAcceptHandler(new ISessionAcceptHandler() {
            @Override
            public void sessionInit(ISession is) {
                if (!canConnectWithIp(is.getIP())) {
                    is.disconnect();
                    is.dispose();
                    return;
                }
                is.setMessageHandler(Controller.getInstance()).setSendCollect(new MessageSendCollect())
                        .setKeyHandler(new MyKeyHandler()).startCollect();
            }

            @Override
            public void sessionDisconnect(ISession session) {
                Client.gI().kickSession((MySession) session);
            }
        }).setTypeSessioClone(MySession.class).setDoSomeThingWhenClose(() -> {
            System.out.println("server close");
            System.exit(0);
        }).start(PORT);
    }

    private void activeServerSocket() {
        if (true) {
            try {
                this.act();
            } catch (Exception e) {
            }
        }
    }

    private boolean canConnectWithIp(String ipAddress) {
        Object o = CLIENTS.get(ipAddress);
        if (o == null) {
            CLIENTS.put(ipAddress, 1);
            return true;
        } else {
            int n = Integer.parseInt(String.valueOf(o));
            if (n < Manager.MAX_PER_IP) {
                n++;
                CLIENTS.put(ipAddress, n);
                return true;
            } else {
                return false;
            }
        }
    }

    public void disconnect(MySession session) {
        Object o = CLIENTS.get(session.getIP());
        if (o != null) {
            int n = Integer.parseInt(String.valueOf(o));
            n--;
            if (n < 0) {
                n = 0;
            }
            CLIENTS.put(session.getIP(), n);
        }
    }

    private void activeCommandLine() {
        new Thread(() -> {
            Scanner sc = new Scanner(System.in);
            while (true) {
                if (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    if (line.equals("baotri")) {
                        Maintenance.gI().start(20);
                    } else if (line.equals("size")) {
                        System.out.println("Size List PLayer: " + GetSizeObject.sizeListPlayer());
                        System.out.println("Size List Boss: " + GetSizeObject.sizeListBoss());
                    } else if (line.equals("checkthread")) {
                        ServerNotify.gI().notify("Nro Kamui Debug Server: " + Thread.activeCount());
                    } else if (line.equals("nplayer")) {
                        Logger.error("Player in game: " + Client.gI().getPlayers().size() + "\n");
                    } else if (line.equals("admin")) {
                        new Thread(() -> {
                            Client.gI().close();
                        }).start();
                    } else if (line.startsWith("bang")) {
                        new Thread(() -> {
                            try {
                                ClanService.gI().close();
                                Logger.error("Save " + Manager.CLANS.size() + " bang");
                            } catch (Exception e) {
                                Logger.error("Lỗi save clan!...................................\n");
                            }
                        }).start();
                    }
                } else{
                    try {
                        Thread.sleep(100); // Avoid busy waiting
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt(); // Restore interrupt status
                    }
                }
            }
        }, "Active line").start();
    }

    public static void activeGame() {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                if (!isRunning) {
                    scheduler.shutdown();
                    return;
                }

                // Cập nhật cơ sở dữ liệu
                Service.gI().botnetlofichill();
            } catch (Exception e) {
                // Xử lý ngoại lệ một cách cẩn thận
                System.err.println("Lỗi ActiveGame " + e.getMessage());
                e.printStackTrace();
            }
        }, 0, DELAY, TimeUnit.MILLISECONDS);
    }

    public void close(long delay) {
        GirlkunServer.gI().stopConnect();

        isRunning = false;
        try {
            ClanService.gI().close();
        } catch (Exception e) {
            Logger.error("Lỗi save clan!...................................\n");
        }
        Client.gI().close();
        Logger.success("SUCCESSFULLY MAINTENANCE!...................................\n");
        System.exit(0);
        Client.gI().close();

        ShopKyGuiManager.gI().save();
        Logger.success("SUCCESSFULLY MAINTENANCE!...................................\n");
        System.exit(0);
    }
}
