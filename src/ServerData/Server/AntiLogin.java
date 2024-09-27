package ServerData.Server;

public class AntiLogin {

    private static final byte MAX_WRONG = 3;
    private static final int TIME_ANTI = 900000;

    private long lastTimeLogin;
    private int timeCanLogin;

    public byte wrongLogin;

    public boolean canLogin() {
        return true;
//        if (lastTimeLogin != -1) {
//            if (Util.canDoWithTime(lastTimeLogin, timeCanLogin)) {
//                this.reset();
//                return true;
//            }
//        }
//        return wrongLogin < MAX_WRONG;
    }

    public void wrong() {
        wrongLogin++;
        if (wrongLogin >= MAX_WRONG) {
            this.lastTimeLogin = System.currentTimeMillis();
            this.timeCanLogin = TIME_ANTI;
        }
    }

    public void reset() {
        this.wrongLogin = 0;
        this.lastTimeLogin = -1;
        this.timeCanLogin = 0;
    }

    public String getNotifyCannotLogin() {
        return "Bạn đã đăng nhập tài khoản sai quá nhiều lần. Vui lòng thử lại sau ít phút";
    }

}
