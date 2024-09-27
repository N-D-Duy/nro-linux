package ServerData.Server;

import ServerData.Services.Service;
import ServerData.Utils.Logger;


public class Maintenance extends Thread {

    public static boolean isRuning = false;
    public boolean canUseCode;
    private static Maintenance i;

    private int giay;

    private Maintenance() {

    }

    public static Maintenance gI() {
        if (i == null) {
            i = new Maintenance();
        }
        return i;
    }

    public void start(int s) {
        if (!isRuning) {
            isRuning = true;
            this.giay = s;
            this.start();
        }
    }

   @Override
    public void run() {
        while (this.giay > 0) {
            this.giay--;
            Service.gI().sendThongBaoAllPlayer("Hệ thống sẽ bảo trì sau " + giay
                    + " giây nữa, vui lòng thoát game để tránh mất vật phẩm");
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
          
        }
        Logger.error("BEGIN MAINTENANCE...............................\n");
        ServerManager.gI().close(100);
    }

}

