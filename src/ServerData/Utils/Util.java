package ServerData.Utils;

import ServerData.Boss.BossManager;
import ServerData.Models.Item.Item;
import ServerData.Models.Map.ItemMap;
import ServerData.Models.Map.Zone;
import java.text.NumberFormat;
import java.util.*;

import ServerData.Models.Map.Mob.Mob;
import ServerData.Models.NPC.Npc;
import ServerData.Models.Player.Player;
import ServerData.Server.Client;
import ServerData.Services.ItemService;
import ServerData.Services.MapService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.lang.ArrayUtils;
import java.security.MessageDigest;
import java.math.BigInteger;
import java.text.SimpleDateFormat;

public class Util {

    private static final Random rand;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final Locale locale = new Locale("vi", "VN");
    private static final NumberFormat num = NumberFormat.getInstance(locale);

    static {
        rand = new Random();
    }

    public static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (Exception e) {
        }
        return "";
    }

    public static int createIdBossClone(int idPlayer) {
        return -idPlayer - 100000;
    }
       
   public static int TamkjllGH(double a) {
        // Telegram: @Tamkjll
        if (a > Integer.MAX_VALUE) {
            a = Integer.MAX_VALUE;
        }
        return (int) a;
    }
    public static long GioiHannext(double from, double to) {
        //code by Việt
        return (long) (from + rand.nextInt((int) (to - from + 1)));
    }
   public static int createIdDuongTank(int idPlayer) {
        return -idPlayer - 100_000_000;
    }
   public static int randomBossId() {
        int bossId = Util.nextInt(99999999);
        while (BossManager.gI().getBossById(bossId) != null) {
            bossId = Util.nextInt(99999999);
        }
        if(bossId > 0){
            bossId *= -1;
        }
        return bossId;
    }

    public static boolean contains(String[] arr, String key) {
        return Arrays.toString(arr).contains(key);
    }
    public static int randomMapBossBroly() {
        int[] listMap = new int[]{6, 10, 11, 12, 13, 19, 20, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38};
        int mapId = Util.nextInt(listMap.length);
        while (!MapService.gI().getZone(mapId).getBosses().isEmpty()) {
            mapId = Util.nextInt(listMap.length);
        }
        return listMap[mapId];
    }

    public static void setTimeout(Runnable runnable, int delay) {
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }).start();
    }

    public static int getDistanceMob(Player pl, Mob m) {
        return getDistance(pl.location.x, pl.location.y, m.location.x, m.location.y);
    }
    
    public static String numberToMoney(double power) {
        num.setMaximumFractionDigits(1);
        if (power >= 1000000000) {
            return num.format(power / 1000000000) + "Tỉ";
        } else if (power >= 1000000) {
            return num.format(power / 1000000) + "M";
        } else if (power >= 1000) {
            return num.format(power / 1000) + "K";
        } else {
            return num.format(power);
        }
    }
    public static String tinhgiay(long power) {
        num.setMaximumFractionDigits(1);
        if (power >= 60) {
            return num.format((double) power / 60) + " phút";
        } else {
            return num.format(power);
        }
    }
    
public static String format(double tsnehi) {
        return num.format(tsnehi);
    }

public static String MoGioihan(double a) {
    if (a > 1000000000) {
            return num.format((double) a / 1000000000) + "Tỉ";
    }
     else if (a >= 1000000) {
            return num.format((double) a / 1000000) + "M";
    } else if (a >= 1000) {
            return num.format((double) a / 1000) + "K";
    }
        return num.format(a);
}

    public static String powerToString(double power) {
        num.setMaximumFractionDigits(1);
        if (power >= 100000000000000000L) {
            return num.format((double) power / 1000000000000000l) + "B Tỉ";
        } else if (power >= 1000000000000000L) {
            return num.format((double) power / 1000000000000000l) + "M Tỉ";
        } else if (power >= 1000000000000L) {
            return num.format((double) power / 1000000000000l) + "K Tỉ";
        } else if (power >= 1000000000) {
            return num.format((double) power / 1000000000) + "B";
        } else if (power >= 1000000) {
            return num.format((double) power / 1000000) + "M";
        } else if (power >= 1000) {
            return num.format((double) power / 1000) + "K";
        } else {
            return num.format(power);
        }
    }
    public static String tinhgio(long ms) {
        ms = ms - System.currentTimeMillis();
        if (ms < 0) {
            ms = 0;
        }
        long mm;
        long ss;
        long hh;
        ss = ms / 1000;
        mm = (long) (ss / 60);
        ss = ss % 60;
        hh = (long) (mm / 60);
        mm = mm % 60;
        String ssString = String.valueOf(ss);
        String mmString = String.valueOf(mm);
        String hhString = String.valueOf(hh);
        String time;
        if (hh != 0) {
            time = hhString + " giờ, " + mmString + " phút, " + ssString + " giây";
        } else if (mm != 0) {
            time = mmString + " phút, " + ssString + "giây";
        } else if (ss != 0) {
            time = ssString + " giây";
        } else {
            time = ssString + " giây";
        }
        return time;
    }
    public static String msToTime(long ms) {
        ms = ms - System.currentTimeMillis();
        if (ms < 0) {
            ms = 0;
        }
        long mm;
        long ss;
        long hh;
        long hhd;
        long dd;
        ss = ms / 1000;
        mm = (long) (ss / 60);
        ss = ss % 60;
        hh = (long) (mm / 60);
        hhd = hh % 24;
        mm = mm % 60;
        dd = hh / 24;
        String ssString = String.valueOf(ss);
        String mmString = String.valueOf(mm);
        String hhString = String.valueOf(hh);
        String hhdString = String.valueOf(hhd);
        String ddString = String.valueOf(dd);
        String time;
        if (dd != 0) {
            time = ddString + " Ngày, " + hhdString + " giờ, " + mmString + " phút, " + ssString + " giây";
        } else if (hh != 0) {
            time = hhString + " giờ, " + mmString + " phút, " + ssString + " giây";
        } else if (mm != 0) {
            time = mmString + " phút, " + ssString + "giây";
        } else if (ss != 0) {
            time = ssString + " giây";
        } else {
            time = "0s";
        }
        return time;
    }
    public static String demgiay(long ms) {
        ms = ms - System.currentTimeMillis();
        if (ms < 0) {
            ms = 0;
        }
        long ss;
        ss = ms / 1000;
        String ssString = String.valueOf(ss);
        String time;
        if (ss != 0) {
            time = ssString + " giây";
        } else {
            time = "Hết hạn";
        }
        return time;
    }
    public static String msToThang(long ms) {
        ms = ms - System.currentTimeMillis();
        if (ms < 0) {
            ms = 0;
        }
        long mm ;
        long ss ;
        long hh;
        long hhd ;
        long dd;
        ss = (ms / 1000);
        mm = (ss / 60);
        ss = ss % 60;
        hh = (mm / 60);
        hhd = hh % 24;
        mm = mm % 60;
        dd = hh / 24;
        String ssString = String.valueOf(ss);
        String mmString = String.valueOf(mm);
        String hhString = String.valueOf(hh);
        String hhdString = String.valueOf(hhd);
        String ddString = String.valueOf(dd);
        String time;
        if (dd != 0) {
            time = ddString + " Ngày (" + hhdString + "H, " + mmString + "M, " + ssString + "s)";
        } else if (hh != 0) {
            time = " (" + hhString + "H, " + mmString + "M, " + ssString + "s)";
        } else if (mm != 0) {
            time = " (" +mmString + "M, " + ssString + "s)";
        } else if (ss != 0) {
            time = ssString + " giây";
        } else {
            time = "Hết hạn";
        }
        return time;
    }
    
     public static String toDateString(Date date) {
        try {
            String a = Util.dateFormat.format(date);
            return a;
        } catch (Exception e) {
            return "2021-01-01 01:01:00";
        }
    }

    public static int getDistance(int x1, int y1, int x2, int y2) {
        return (int) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    public static int getDistance(Player pl1, Player pl2) {
        return getDistance(pl1.location.x, pl1.location.y, pl2.location.x, pl2.location.y);
    }

    public static int getDistance(Player pl, Npc npc) {
        return getDistance(pl.location.x, pl.location.y, npc.cx, npc.cy);
    }

    public static int getDistance(Player pl, Mob mob) {
        return getDistance(pl.location.x, pl.location.y, mob.location.x, mob.location.y);
    }

    public static int getDistance(Mob mob1, Mob mob2) {
        return getDistance(mob1.location.x, mob1.location.y, mob2.location.x, mob2.location.y);
    }
    public static long nextLong(long from, long to) {
        if(to<=0) return 0;
        return from + rand.nextInt((int) (to - from + 1));
    }
    public static int nextInt(int from, int to) {
        if(to<=0) return 0;
        return from + rand.nextInt(to - from + 1);
    }
    public static int nextInt(int max) {
        if(max<=0) return 0;
        return rand.nextInt(max);
    }
    public static int nextInt(int[] percen) {
        int next = nextInt(1000), i;
        for (i = 0; i < percen.length; i++) {
            if (next < percen[i]) {
                return i;
            }
            next -= percen[i];
        }
        return i;
    }

    public static int getOne(int n1, int n2) {
        return rand.nextInt() % 2 == 0 ? n1 : n2;
    }

    public static int currentTimeSec() {
        return (int) System.currentTimeMillis() / 1000;
    }

    public static String replace(String text, String regex, String replacement) {
        return text.replace(regex, replacement);
    }

    public static boolean isTrue(int ratio, int typeRatio) {
        int num = Util.nextInt(typeRatio);
        if (num < ratio) {
            return true;
        }
        return false;
    }

    public static boolean isTrue(float ratio, int typeRatio) {
        if (ratio < 1) {
            ratio *= 10;
            typeRatio *= 10;
        }
        int num = Util.nextInt(typeRatio);
        if (num < ratio) {
            return true;
        }
        return false;
    }

    public static boolean haveSpecialCharacter(String text) {
        Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(text);
        boolean b = m.find();
        return b || text.contains(" ");
    }

    public static boolean canDoWithTime(long lastTime, long miniTimeTarget) {
        return System.currentTimeMillis() - lastTime > miniTimeTarget;
    }

    private static final char[] SOURCE_CHARACTERS = {'À', 'Á', 'Â', 'Ã', 'È', 'É',
        'Ê', 'Ì', 'Í', 'Ò', 'Ó', 'Ô', 'Õ', 'Ù', 'Ú', 'Ý', 'à', 'á', 'â',
        'ã', 'è', 'é', 'ê', 'ì', 'í', 'ò', 'ó', 'ô', 'õ', 'ù', 'ú', 'ý',
        'Ă', 'ă', 'Đ', 'đ', 'Ĩ', 'ĩ', 'Ũ', 'ũ', 'Ơ', 'ơ', 'Ư', 'ư', 'Ạ',
        'ạ', 'Ả', 'ả', 'Ấ', 'ấ', 'Ầ', 'ầ', 'Ẩ', 'ẩ', 'Ẫ', 'ẫ', 'Ậ', 'ậ',
        'Ắ', 'ắ', 'Ằ', 'ằ', 'Ẳ', 'ẳ', 'Ẵ', 'ẵ', 'Ặ', 'ặ', 'Ẹ', 'ẹ', 'Ẻ',
        'ẻ', 'Ẽ', 'ẽ', 'Ế', 'ế', 'Ề', 'ề', 'Ể', 'ể', 'Ễ', 'ễ', 'Ệ', 'ệ',
        'Ỉ', 'ỉ', 'Ị', 'ị', 'Ọ', 'ọ', 'Ỏ', 'ỏ', 'Ố', 'ố', 'Ồ', 'ồ', 'Ổ',
        'ổ', 'Ỗ', 'ỗ', 'Ộ', 'ộ', 'Ớ', 'ớ', 'Ờ', 'ờ', 'Ở', 'ở', 'Ỡ', 'ỡ',
        'Ợ', 'ợ', 'Ụ', 'ụ', 'Ủ', 'ủ', 'Ứ', 'ứ', 'Ừ', 'ừ', 'Ử', 'ử', 'Ữ',
        'ữ', 'Ự', 'ự',};

    private static final char[] DESTINATION_CHARACTERS = {'A', 'A', 'A', 'A', 'E',
        'E', 'E', 'I', 'I', 'O', 'O', 'O', 'O', 'U', 'U', 'Y', 'a', 'a',
        'a', 'a', 'e', 'e', 'e', 'i', 'i', 'o', 'o', 'o', 'o', 'u', 'u',
        'y', 'A', 'a', 'D', 'd', 'I', 'i', 'U', 'u', 'O', 'o', 'U', 'u',
        'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A',
        'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'E', 'e',
        'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E',
        'e', 'I', 'i', 'I', 'i', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o',
        'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O',
        'o', 'O', 'o', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u',
        'U', 'u', 'U', 'u',};

    public static char removeAccent(char ch) {
        int index = Arrays.binarySearch(SOURCE_CHARACTERS, ch);
        if (index >= 0) {
            ch = DESTINATION_CHARACTERS[index];
        }
        return ch;
    }

    public static String removeAccent(String str) {
        StringBuilder sb = new StringBuilder(str);
        for (int i = 0; i < sb.length(); i++) {
            sb.setCharAt(i, removeAccent(sb.charAt(i)));
        }
        return sb.toString();
    }

    public static String generateRandomText(int len) {
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijk"
                + "lmnopqrstuvwxyz!@#$%&";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }

    public static Object[] addArray(Object[]... arrays) {
        if (arrays == null || arrays.length == 0) {
            return null;
        }
        if (arrays.length == 1) {
            return arrays[0];
        }
        Object[] arr0 = arrays[0];
        for (int i = 1; i < arrays.length; i++) {
            arr0 = ArrayUtils.addAll(arr0, arrays[i]);
        }
        return arr0;
    }

    public static ItemMap manhTS(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        return new ItemMap(zone, tempId, quantity, x, y, playerId);
    }

    public static ItemMap ratiDTL(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, zone.map.yPhysicInTop(x, y - 24), playerId);
        List<Integer> ao = Arrays.asList(555, 557, 559);
        List<Integer> quan = Arrays.asList(556, 558, 560);
        List<Integer> gang = Arrays.asList(562, 564, 566);
        List<Integer> giay = Arrays.asList(563, 565, 567);
        int ntl = 561;
        if (ao.contains(tempId)) {
            it.options.add(new Item.ItemOption(47, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(501) + 1300)));
        }
        if (quan.contains(tempId)) {
            it.options.add(new Item.ItemOption(22, highlightsItem(it.itemTemplate.gender == 0, new Random().nextInt(11) + 45)));
        }
        if (gang.contains(tempId)) {
            it.options.add(new Item.ItemOption(0, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(1001) + 3500)));
        }
        if (giay.contains(tempId)) {
            it.options.add(new Item.ItemOption(23, highlightsItem(it.itemTemplate.gender == 1, new Random().nextInt(11) + 35)));
        }
        if (ntl == tempId) {
            it.options.add(new Item.ItemOption(14, new Random().nextInt(2) + 15));
        }
        it.options.add(new Item.ItemOption(209, 1)); // đồ rơi từ boss
        it.options.add(new Item.ItemOption(21, 18)); // ycsm 18 tỉ
        //it.options.add(new Item.ItemOption(30, 1)); // ko thể gd
        if (Util.isTrue(90, 100)) {// tỉ lệ ra spl
            it.options.add(new Item.ItemOption(107, new Random().nextInt(1) + 1));
        } else if (Util.isTrue(4, 100)) {
            it.options.add(new Item.ItemOption(107, new Random().nextInt(2) + 1));
        } else {
            it.options.add(new Item.ItemOption(107, new Random().nextInt(3) + 1));
        }
        return it;
    }

    public static ItemMap RaitiDoc12(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, y, playerId);
        List<Integer> ao = Arrays.asList(233, 237, 241);
        List<Integer> quan = Arrays.asList(245, 249, 253);
        List<Integer> gang = Arrays.asList(257, 261, 265);
        List<Integer> giay = Arrays.asList(269, 273, 277);
        int rd12 = 281;
        if (ao.contains(tempId)) {
            it.options.add(new Item.ItemOption(47, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(121) + 350)));//giáp 350-470
        }
        if (quan.contains(tempId)) {
            it.options.add(new Item.ItemOption(22, highlightsItem(it.itemTemplate.gender == 0, new Random().nextInt(5) + 20)));//hp 20-24k
        }
        if (gang.contains(tempId)) {
            it.options.add(new Item.ItemOption(0, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(51) + 2200)));//2200-2250
        }
        if (giay.contains(tempId)) {
            it.options.add(new Item.ItemOption(23, highlightsItem(it.itemTemplate.gender == 1, new Random().nextInt(4) + 20)));//20-23k ki
        }
        if (rd12 == tempId) {
            it.options.add(new Item.ItemOption(14, new Random().nextInt(3) + 10));//10-12cm
        }
        it.options.add(new Item.ItemOption(209, 1));//đồ rơi từ boss
        if (Util.isTrue(70, 100)) {// tỉ lệ ra spl 1-3 sao 70%
            it.options.add(new Item.ItemOption(107, new Random().nextInt(1) + 3));
        } else if (Util.isTrue(4, 100)) {// tỉ lệ ra spl 5-7 sao 4%
            it.options.add(new Item.ItemOption(107, new Random().nextInt(3) + 5));
        } else {// tỉ lệ ra spl 1-5 sao 6%
            it.options.add(new Item.ItemOption(107, new Random().nextInt(2) + 3));
        }
        return it;
    }

    public static Item ratiItemTL(int tempId) {
        Item it = ItemService.gI().createItemSetKichHoat(tempId, 1);
        List<Integer> ao = Arrays.asList(555, 557, 559);
        List<Integer> quan = Arrays.asList(556, 558, 560);
        List<Integer> gang = Arrays.asList(562, 564, 566);
        List<Integer> giay = Arrays.asList(563, 565, 567);
        int ntl = 561;
        if (ao.contains(tempId)) {
            it.itemOptions.add(new Item.ItemOption(47, highlightsItem(it.template.gender == 2, new Random().nextInt(501) + 1000)));
        }
        if (quan.contains(tempId)) {
            it.itemOptions.add(new Item.ItemOption(22, highlightsItem(it.template.gender == 0, new Random().nextInt(11) + 45)));
        }
        if (gang.contains(tempId)) {
            it.itemOptions.add(new Item.ItemOption(0, highlightsItem(it.template.gender == 2, new Random().nextInt(1001) + 3500)));
        }
        if (giay.contains(tempId)) {
            it.itemOptions.add(new Item.ItemOption(23, highlightsItem(it.template.gender == 1, new Random().nextInt(11) + 35)));
        }
        if (ntl == tempId) {
            it.itemOptions.add(new Item.ItemOption(14, new Random().nextInt(3) + 15));
        }
        it.itemOptions.add(new Item.ItemOption(21, 18));
        return it;
    }
 public static Item ratiItemThuong(int tempId) {
        Item it = ItemService.gI().createItemSetKichHoat(tempId, 1);
        List<Integer> ao = Arrays.asList(0, 1, 2);
        List<Integer> quan = Arrays.asList(6, 7, 8);
        List<Integer> gang = Arrays.asList(21, 22, 23);
        List<Integer> giay = Arrays.asList(27, 28, 29);
        int ntl = 12;
        if (ao.contains(tempId)) {
            it.itemOptions.add(new Item.ItemOption(47, highlightsItem(it.template.gender == 2, new Random().nextInt(5) + 10)));
        }
        if (quan.contains(tempId)) {
            it.itemOptions.add(new Item.ItemOption(6, highlightsItem(it.template.gender == 0, new Random().nextInt(11) + 45)));
        }
        if (gang.contains(tempId)) {
            it.itemOptions.add(new Item.ItemOption(0, highlightsItem(it.template.gender == 2, new Random().nextInt(1) + 5)));
        }
        if (giay.contains(tempId)) {
            it.itemOptions.add(new Item.ItemOption(7, highlightsItem(it.template.gender == 1, new Random().nextInt(11) + 35)));
        }
        if (ntl == tempId) {
            it.itemOptions.add(new Item.ItemOption(14, new Random().nextInt(2) + 1));
        }
  
        return it;
    }

    public static ItemMap useItem(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, zone.map.yPhysicInTop(x, y - 24), playerId);
        List<Integer> tanjiro = Arrays.asList(1087, 1088, 1091, 1090);
        if (tanjiro.contains(tempId)) {
            it.options.add(new Item.ItemOption(77, highlightsItem(it.itemTemplate.gender == 3, new Random().nextInt(30) + 1)));
            it.options.add(new Item.ItemOption(103, highlightsItem(it.itemTemplate.gender == 3, new Random().nextInt(30) + 1)));
            it.options.add(new Item.ItemOption(50, highlightsItem(it.itemTemplate.gender == 3, new Random().nextInt(30) + 1)));
        }
        it.options.add(new Item.ItemOption(209, 1)); // đồ rơi từ boss
        it.options.add(new Item.ItemOption(30, 1)); // ko thể gd

        return it;
    }
    
    public static ItemMap bikip(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, y, playerId);
        return it;
    }
    public static ItemMap quadao(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, y, playerId);
        return it;
    }

    public static ItemMap ratiItem(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, y, playerId);
        List<Integer> ao = Arrays.asList(555, 557, 559);
        List<Integer> quan = Arrays.asList(556, 558, 560);
        List<Integer> gang = Arrays.asList(562, 564, 566);
        List<Integer> giay = Arrays.asList(563, 565, 567);
        int ntl = 561;
        if (ao.contains(tempId)) {
            it.options.add(new Item.ItemOption(47, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(501) + 1000)));
        }
        if (quan.contains(tempId)) {
            it.options.add(new Item.ItemOption(22, highlightsItem(it.itemTemplate.gender == 0, new Random().nextInt(11) + 45)));
        }
        if (gang.contains(tempId)) {
            it.options.add(new Item.ItemOption(0, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(1001) + 3500)));
        }
        if (giay.contains(tempId)) {
            it.options.add(new Item.ItemOption(23, highlightsItem(it.itemTemplate.gender == 1, new Random().nextInt(11) + 35)));
        }
        if (ntl == tempId) {
            it.options.add(new Item.ItemOption(14, new Random().nextInt(3) + 15));
        }
        it.options.add(new Item.ItemOption(209, 1));
        it.options.add(new Item.ItemOption(21, 18));
        return it;
    }
    public static Item randomthanlinh(short tempId) {
        Item it = ItemService.gI().createNewItem((short) tempId);
        List<Integer> ao = Arrays.asList(555, 557, 559);
        List<Integer> quan = Arrays.asList(556, 558, 560);
        List<Integer> gang = Arrays.asList(562, 564, 566);
        List<Integer> giay = Arrays.asList(563, 565, 567);
        int ntl = 561;
        if (ao.contains((int) tempId)) {
            it.itemOptions.add(new Item.ItemOption(47, highlightsItem(it.template.gender == 2, new Random().nextInt(501) + 1000)));
        }
        if (quan.contains((int) tempId)) {
            it.itemOptions.add(new Item.ItemOption(22, highlightsItem(it.template.gender == 0, new Random().nextInt(11) + 45)));
        }
        if (gang.contains((int) tempId)) {
            it.itemOptions.add(new Item.ItemOption(0, highlightsItem(it.template.gender == 2, new Random().nextInt(1001) + 3500)));
        }
        if (giay.contains((int) tempId)) {
            it.itemOptions.add(new Item.ItemOption(23, highlightsItem(it.template.gender == 1, new Random().nextInt(11) + 35)));
        }
        if (ntl == tempId) {
            it.itemOptions.add(new Item.ItemOption(14, new Random().nextInt(3) + 15));
        }
        it.itemOptions.add(new Item.ItemOption(21, 18));
        it.itemOptions.add(new Item.ItemOption(30, 1));
        return it;
    }
    public static ItemMap UpDoTL(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, y, playerId);
        List<Integer> ao = Arrays.asList(555, 557, 559);
        List<Integer> quan = Arrays.asList(556, 558, 560);
        List<Integer> gang = Arrays.asList(562, 564, 566);
        List<Integer> giay = Arrays.asList(563, 565, 567);
        int ntl = 561;
        if (ao.contains(tempId)) {
            it.options.add(new Item.ItemOption(47, highlightsItem(it.itemTemplate.gender == 0 && it.itemTemplate.gender == 1 && it.itemTemplate.gender == 2, new Random().nextInt(501) + 1000)));
            it.options.add(new Item.ItemOption(21, 18));
        }
        if (quan.contains(tempId)) {
            it.options.add(new Item.ItemOption(22, highlightsItem(it.itemTemplate.gender == 0 && it.itemTemplate.gender == 1 && it.itemTemplate.gender == 2, new Random().nextInt(11) + 45)));
            it.options.add(new Item.ItemOption(21, 18));
        }
        if (gang.contains(tempId)) {
            it.options.add(new Item.ItemOption(0, highlightsItem(it.itemTemplate.gender == 0 && it.itemTemplate.gender == 1 && it.itemTemplate.gender == 2, new Random().nextInt(1001) + 3500)));
            it.options.add(new Item.ItemOption(21, 20));
        }
        if (giay.contains(tempId)) {
            it.options.add(new Item.ItemOption(23, highlightsItem(it.itemTemplate.gender == 0 && it.itemTemplate.gender == 1 && it.itemTemplate.gender == 2, new Random().nextInt(11) + 35)));
            it.options.add(new Item.ItemOption(21, 18));
        }
        if (ntl == tempId) {
            it.options.add(new Item.ItemOption(14, new Random().nextInt(3) + 15));
            it.options.add(new Item.ItemOption(21, 20));
        }
        it.options.add(new Item.ItemOption(208, 1));
        return it;
    }
    
    public static ItemMap dohuydiet(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, y, playerId);
        List<Integer> ao = Arrays.asList(650, 652, 654);
        List<Integer> quan = Arrays.asList(651, 653, 655);
        List<Integer> gang = Arrays.asList(657, 659, 661);
        List<Integer> giay = Arrays.asList(658, 660, 662);
        int ntl = 656;
        if (ao.contains(tempId)) {
            it.options.add(new Item.ItemOption(47, highlightsItem(it.itemTemplate.gender == 0 && it.itemTemplate.gender == 1 && it.itemTemplate.gender == 2, new Random().nextInt(360) + 1800)));
            it.options.add(new Item.ItemOption(21, 42));
        }
        if (quan.contains(tempId)) {
            it.options.add(new Item.ItemOption(22, highlightsItem(it.itemTemplate.gender == 0 && it.itemTemplate.gender == 1 && it.itemTemplate.gender == 2, new Random().nextInt(20) + 96)));
            it.options.add(new Item.ItemOption(21, 46));
        }
        if (gang.contains(tempId)) {
            it.options.add(new Item.ItemOption(0, highlightsItem(it.itemTemplate.gender == 0 && it.itemTemplate.gender == 1 && it.itemTemplate.gender == 2, new Random().nextInt(1800) + 9000)));
            it.options.add(new Item.ItemOption(21, 50));
        }
        if (giay.contains(tempId)) {
            it.options.add(new Item.ItemOption(23, highlightsItem(it.itemTemplate.gender == 0 && it.itemTemplate.gender == 1 && it.itemTemplate.gender == 2, new Random().nextInt(18) + 92)));
            it.options.add(new Item.ItemOption(21, 44));
        }
        if (ntl == tempId) {
            it.options.add(new Item.ItemOption(14, new Random().nextInt(3) + 16));
            it.options.add(new Item.ItemOption(21, 48));
        }
        it.options.add(new Item.ItemOption(209, 1));
        it.options.add(new Item.ItemOption(30, 1));
        return it;
    }

    public static ItemMap thoivang(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, y, playerId);
        int thoivang = 457;
        if (thoivang == tempId) {
            it.options.add(new Item.ItemOption(209, 1));
            it.options.add(new Item.ItemOption(30, 1));
        }
        return it;
    }
    
    public static ItemMap caitrangblackgoku(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, y, playerId);
        int thoivang = 1098;
        if (thoivang == tempId) {
            it.options.add(new Item.ItemOption(50, Util.nextInt(15, 25)));
            it.options.add(new Item.ItemOption(77, Util.nextInt(15, 25)));
            it.options.add(new Item.ItemOption(103, Util.nextInt(15, 25)));
            it.options.add(new Item.ItemOption(4, Util.nextInt(10, 30)));
            it.options.add(new Item.ItemOption(209, 0));
            it.options.add(new Item.ItemOption(93, highlightsItem(it.itemTemplate.gender == 0 && it.itemTemplate.gender == 1 && it.itemTemplate.gender == 2, new Random().nextInt(29) + 1)));
        }
        return it;
    }
      public static ItemMap itemsukien(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, y, playerId);
        int minuong = 860;
        int qdbtd = 691;
        int qdbnm = 692;
        int qdbxd = 693;
        if (minuong == tempId) {
            it.options.add(new Item.ItemOption(214, 0));
            it.options.add(new Item.ItemOption(93, highlightsItem(it.itemTemplate.gender == 0 && it.itemTemplate.gender == 1 && it.itemTemplate.gender == 2, new Random().nextInt(2) + 1)));
        }
        if (qdbtd == tempId) {
            it.options.add(new Item.ItemOption(210, 0));
            it.options.add(new Item.ItemOption(93, highlightsItem(it.itemTemplate.gender == 0 && it.itemTemplate.gender == 1 && it.itemTemplate.gender == 2, new Random().nextInt(2) + 1)));
        }
        if (qdbnm == tempId) {
            it.options.add(new Item.ItemOption(210, 0));
            it.options.add(new Item.ItemOption(93, highlightsItem(it.itemTemplate.gender == 0 && it.itemTemplate.gender == 1 && it.itemTemplate.gender == 2, new Random().nextInt(2) + 1)));
        }
        if (qdbxd == tempId) {
            it.options.add(new Item.ItemOption(210, 0));
            it.options.add(new Item.ItemOption(93, highlightsItem(it.itemTemplate.gender == 0 && it.itemTemplate.gender == 1 && it.itemTemplate.gender == 2, new Random().nextInt(2) + 1)));
        }
        return it;
    }
      
        public static ItemMap MabuCt(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, y, playerId);
        int ct = 937;
        int tkb = 1147;
        if (ct == tempId) {
            it.options.add(new Item.ItemOption(50, Util.nextInt(1, 30)));
            it.options.add(new Item.ItemOption(77, Util.nextInt(5, 25)));
            it.options.add(new Item.ItemOption(103, Util.nextInt(5, 25)));
            it.options.add(new Item.ItemOption(104, Util.nextInt(5, 25)));
            it.options.add(new Item.ItemOption(106, Util.nextInt(5, 25)));
            it.options.add(new Item.ItemOption(93, highlightsItem(it.itemTemplate.gender == 0 && it.itemTemplate.gender == 1 && it.itemTemplate.gender == 2, new Random().nextInt(29) + 1)));
        }
        if (ct == tempId) {
            it.options.add(new Item.ItemOption(209, 1));
            it.options.add(new Item.ItemOption(93, highlightsItem(it.itemTemplate.gender == 0 && it.itemTemplate.gender == 1 && it.itemTemplate.gender == 2, new Random().nextInt(29) + 1)));
        }
        return it;
    }
        
     public static ItemMap spl(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, y, playerId);
        int spl = 441;
        int spl1 = 442;
        int spl2 = 443;
        int spl3 = 444;
        int spl4 = 445;
        int spl5 = 446;
        int spl6 = 447;  
        int dnc1 = 220;
        int dnc2 = 221;
        int dnc3 = 222;
        int dnc4 = 223;
        int dnc5 = 224;
        int spldb = 1412;
        int spldb1 = 1413;
        int spldb2 = 1414;
        int spldb3 = 1415;
        int spldb4 = 1416;
        if (spldb == tempId) {
            it.options.add(new Item.ItemOption(50, 4));
        }
        if (spldb1 == tempId) {
            it.options.add(new Item.ItemOption(77, 7));
        }
        if (spldb2 == tempId) {
            it.options.add(new Item.ItemOption(103, 7));
        }
        if (spldb3 == tempId) {
            it.options.add(new Item.ItemOption(14, (int) (float) 0.5f));
        }
        if (spldb4 == tempId) {
            it.options.add(new Item.ItemOption(94, 3));
        }
        if (spl == tempId) {
            it.options.add(new Item.ItemOption(95, 5));
        }
        if (spl1 == tempId) {
            it.options.add(new Item.ItemOption(96, 5));
        }
        if (spl2 == tempId) {
            it.options.add(new Item.ItemOption(97, 5));
        }
        if (spl3 == tempId) {
            it.options.add(new Item.ItemOption(99, 3));
        }
        if (spl4 == tempId) {
            it.options.add(new Item.ItemOption(98, 3));
        }
        if (spl5 == tempId) {
            it.options.add(new Item.ItemOption(100, 5));
        }
        if (spl6 == tempId) {
            it.options.add(new Item.ItemOption(101, 5));
        }
        if (dnc1 == tempId) {
            it.options.add(new Item.ItemOption(71, 5));
        }
        if (dnc2 == tempId) {
            it.options.add(new Item.ItemOption(68, 3));
        }
        if (dnc3 == tempId) {
            it.options.add(new Item.ItemOption(69, 3));
        }
        if (dnc4 == tempId) {
            it.options.add(new Item.ItemOption(70, 5));
        }
        if (dnc5 == tempId) {
            it.options.add(new Item.ItemOption(67, 5));
        }
        return it;
    }
    
     public static ItemMap muahe(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, y, playerId);
        int traidua = 669;
        int oc = 695;
        int so = 696;
        int cua = 697;
        int saobien = 698;
        if (traidua == tempId) {
            it.options.add(new Item.ItemOption(214, 0));
        }
        if (oc == tempId) {
            it.options.add(new Item.ItemOption(210, 0));
        }
        if (so == tempId) {
            it.options.add(new Item.ItemOption(210, 0));
        }
        if (cua == tempId) {
            it.options.add(new Item.ItemOption(210, 0));
        }
        if (saobien == tempId) {
            it.options.add(new Item.ItemOption(210, 0));
        }
        return it;
    }
     
    public static Item petrandom(int tempId) {
        Item gapthuong = ItemService.gI().createNewItem((short) tempId);
        if(Util.isTrue(90, 100)) {    
            gapthuong.itemOptions.add(new Item.ItemOption(50, Util.nextInt(5,10)));
            gapthuong.itemOptions.add(new Item.ItemOption(103, Util.nextInt(5,10)));
            gapthuong.itemOptions.add(new Item.ItemOption(77, Util.nextInt(5,10)));
        if(Util.isTrue(80, 100)) {
            gapthuong.itemOptions.add(new Item.ItemOption(93,Util.nextInt(1,9)));    
        }} else {   
            gapthuong.itemOptions.add(new Item.ItemOption(0, Util.nextInt(1511,4512)));
            gapthuong.itemOptions.add(new Item.ItemOption(6, Util.nextInt(1151,4515)));
            gapthuong.itemOptions.add(new Item.ItemOption(7, Util.nextInt(1152,4115)));
        if(Util.isTrue(80, 100)) {
            gapthuong.itemOptions.add(new Item.ItemOption(93,Util.nextInt(1,9)));    
        }}
        return gapthuong;
    } 
    
    public static Item petccrandom(int tempId) {
        Item gapcc = ItemService.gI().createNewItem((short) tempId);
        if(Util.isTrue(90, 100)) {    
            gapcc.itemOptions.add(new Item.ItemOption(50, Util.nextInt(10,20)));
            gapcc.itemOptions.add(new Item.ItemOption(103, Util.nextInt(10,20)));
            gapcc.itemOptions.add(new Item.ItemOption(77, Util.nextInt(10,20)));
        if(Util.isTrue(80, 100)) {
            gapcc.itemOptions.add(new Item.ItemOption(93,Util.nextInt(5,12)));    
        }} else {   
            gapcc.itemOptions.add(new Item.ItemOption(0, Util.nextInt(3511,7512)));
            gapcc.itemOptions.add(new Item.ItemOption(6, Util.nextInt(3151,8515)));
            gapcc.itemOptions.add(new Item.ItemOption(7, Util.nextInt(3152,8115)));
        if(Util.isTrue(80, 100)) {
            gapcc.itemOptions.add(new Item.ItemOption(93,Util.nextInt(5,12)));    
        }}
        return gapcc;
    }
//     public static ItemMap RaitiDoSKH(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
//        ItemMap it = new ItemMap(zone, tempId, quantity, x, y, playerId);
//        List<Integer> ao = Arrays.asList(0, 1, 2);
//        List<Integer> quan = Arrays.asList(6, 7, 8);
//        List<Integer> gang = Arrays.asList(21, 22, 23);
//        List<Integer> giay = Arrays.asList(27, 28, 29);
//        int rd12 = 12;
//        if (ao.contains(tempId)) {
//            it.options.add(new Item.ItemOption(47, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(2) + 3)));//giáp 350-470
//      if (ao.contains(0)) {
//           if(Util.isTrue(100, 100)) {
//                if(Util.isTrue(10, 100)) {
//                          it.options.add(new Item.ItemOption(129, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                        it.options.add(new Item.ItemOption(141, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                } else  if(Util.isTrue(10, 100)) {
//                          it.options.add(new Item.ItemOption(128, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                        it.options.add(new Item.ItemOption(140, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                } else if(Util.isTrue(10, 100)) {
//                          it.options.add(new Item.ItemOption(127, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                        it.options.add(new Item.ItemOption(139, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                }  
//           } 
//      } else
//         if (ao.contains(1)) {
//           if(Util.isTrue(100, 100)) {
//                if(Util.isTrue(10, 100)) {
//                          it.options.add(new Item.ItemOption(130, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                        it.options.add(new Item.ItemOption(142, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                } else  if(Util.isTrue(10, 100)) {
//                          it.options.add(new Item.ItemOption(131, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                        it.options.add(new Item.ItemOption(143, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                } else if(Util.isTrue(10, 100)) {
//                          it.options.add(new Item.ItemOption(132, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                        it.options.add(new Item.ItemOption(144, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                }  
//           } 
//      } else
//          if (ao.contains(2)) {
//           if(Util.isTrue(100, 100)) {
//                if(Util.isTrue(10, 100)) {
//                          it.options.add(new Item.ItemOption(133, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                        it.options.add(new Item.ItemOption(136, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                } else  if(Util.isTrue(10, 100)) {
//                          it.options.add(new Item.ItemOption(134, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                        it.options.add(new Item.ItemOption(137, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                } else if(Util.isTrue(10, 100)) {
//                          it.options.add(new Item.ItemOption(135, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                        it.options.add(new Item.ItemOption(138, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                }  
//           } 
//      }
//           it.options.add(new Item.ItemOption(30, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//        }
//        
//        if (quan.contains(tempId)) {
//            it.options.add(new Item.ItemOption(6, highlightsItem(it.itemTemplate.gender == 0, new Random().nextInt(5) + 20)));//hp 20-24k
//        if (quan.contains(6)) {
//           if(Util.isTrue(100, 100)) {
//                if(Util.isTrue(10, 100)) {
//                          it.options.add(new Item.ItemOption(129, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                        it.options.add(new Item.ItemOption(141, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                } else  if(Util.isTrue(10, 100)) {
//                          it.options.add(new Item.ItemOption(128, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                        it.options.add(new Item.ItemOption(140, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                } else if(Util.isTrue(10, 100)) {
//                          it.options.add(new Item.ItemOption(127, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                        it.options.add(new Item.ItemOption(139, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                }  
//           } 
//      } else
//         if (quan.contains(7)) {
//           if(Util.isTrue(100, 100)) {
//                if(Util.isTrue(10, 100)) {
//                          it.options.add(new Item.ItemOption(130, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                        it.options.add(new Item.ItemOption(142, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                } else  if(Util.isTrue(10, 100)) {
//                          it.options.add(new Item.ItemOption(131, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                        it.options.add(new Item.ItemOption(143, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                } else if(Util.isTrue(10, 100)) {
//                          it.options.add(new Item.ItemOption(132, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                        it.options.add(new Item.ItemOption(144, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                }  
//           } 
//      } else
//          if (quan.contains(8)) {
//           if(Util.isTrue(100, 100)) {
//                if(Util.isTrue(10, 100)) {
//                          it.options.add(new Item.ItemOption(133, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                        it.options.add(new Item.ItemOption(136, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                } else  if(Util.isTrue(10, 100)) {
//                          it.options.add(new Item.ItemOption(134, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                        it.options.add(new Item.ItemOption(137, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                } else if(Util.isTrue(10, 100)) {
//                          it.options.add(new Item.ItemOption(135, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                        it.options.add(new Item.ItemOption(138, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                }  
//           } 
//      }
//           it.options.add(new Item.ItemOption(30, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//        }
//        
//        if (gang.contains(tempId)) {
//            it.options.add(new Item.ItemOption(0, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(12) + 1)));//2200-2250
//         if (gang.contains(21)) {
//           if(Util.isTrue(100, 100)) {
//                if(Util.isTrue(10, 100)) {
//                          it.options.add(new Item.ItemOption(129, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                        it.options.add(new Item.ItemOption(141, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                } else  if(Util.isTrue(10, 100)) {
//                          it.options.add(new Item.ItemOption(128, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                        it.options.add(new Item.ItemOption(140, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                } else if(Util.isTrue(10, 100)) {
//                          it.options.add(new Item.ItemOption(127, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                        it.options.add(new Item.ItemOption(139, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                }  
//           } 
//      } else
//         if (gang.contains(22)) {
//           if(Util.isTrue(100, 100)) {
//                if(Util.isTrue(10, 100)) {
//                          it.options.add(new Item.ItemOption(130, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                        it.options.add(new Item.ItemOption(142, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                } else  if(Util.isTrue(10, 100)) {
//                          it.options.add(new Item.ItemOption(131, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                        it.options.add(new Item.ItemOption(143, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                } else if(Util.isTrue(10, 100)) {
//                          it.options.add(new Item.ItemOption(132, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                        it.options.add(new Item.ItemOption(144, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                }  
//           } 
//      } else
//          if (gang.contains(23)) {
//           if(Util.isTrue(100, 100)) {
//                if(Util.isTrue(10, 100)) {
//                          it.options.add(new Item.ItemOption(133, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                        it.options.add(new Item.ItemOption(136, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                } else  if(Util.isTrue(10, 100)) {
//                          it.options.add(new Item.ItemOption(134, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                        it.options.add(new Item.ItemOption(137, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                } else if(Util.isTrue(10, 100)) {
//                          it.options.add(new Item.ItemOption(135, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                        it.options.add(new Item.ItemOption(138, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                }  
//           } 
//      }
//           it.options.add(new Item.ItemOption(30, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//        }
//        
//        if (giay.contains(tempId)) {
//            it.options.add(new Item.ItemOption(7, highlightsItem(it.itemTemplate.gender == 1, new Random().nextInt(4) + 20)));//20-23k ki
//          if (giay.contains(27)) {
//           if(Util.isTrue(100, 100)) {
//                if(Util.isTrue(10, 100)) {
//                          it.options.add(new Item.ItemOption(129, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                        it.options.add(new Item.ItemOption(141, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                } else  if(Util.isTrue(10, 100)) {
//                          it.options.add(new Item.ItemOption(128, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                        it.options.add(new Item.ItemOption(140, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                } else if(Util.isTrue(10, 100)) {
//                          it.options.add(new Item.ItemOption(127, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                        it.options.add(new Item.ItemOption(139, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                }  
//           } 
//      } else
//         if (giay.contains(28)) {
//           if(Util.isTrue(100, 100)) {
//                if(Util.isTrue(10, 100)) {
//                          it.options.add(new Item.ItemOption(130, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                        it.options.add(new Item.ItemOption(142, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                } else  if(Util.isTrue(10, 100)) {
//                          it.options.add(new Item.ItemOption(131, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                        it.options.add(new Item.ItemOption(143, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                } else if(Util.isTrue(10, 100)) {
//                          it.options.add(new Item.ItemOption(132, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                        it.options.add(new Item.ItemOption(144, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                }  
//           } 
//      } else
//          if (giay.contains(29)) {
//           if(Util.isTrue(100, 100)) {
//                if(Util.isTrue(10, 100)) {
//                          it.options.add(new Item.ItemOption(133, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                        it.options.add(new Item.ItemOption(136, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                } else  if(Util.isTrue(10, 100)) {
//                          it.options.add(new Item.ItemOption(134, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                        it.options.add(new Item.ItemOption(137, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                } else if(Util.isTrue(10, 100)) {
//                          it.options.add(new Item.ItemOption(135, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                        it.options.add(new Item.ItemOption(138, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                }  
//           } 
//      }
//           it.options.add(new Item.ItemOption(30, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//        }
//        
//        if (rd12 == tempId) {
//            it.options.add(new Item.ItemOption(14, new Random().nextInt(3) + 10));//10-12cm
//         if(Util.isTrue(100, 100)) {
//                if(Util.isTrue(10, 100)) {
//                          it.options.add(new Item.ItemOption(133, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                        it.options.add(new Item.ItemOption(136, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                } else  if(Util.isTrue(10, 100)) {
//                          it.options.add(new Item.ItemOption(134, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                        it.options.add(new Item.ItemOption(137, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                } else if(Util.isTrue(10, 100)) {
//                          it.options.add(new Item.ItemOption(135, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                        it.options.add(new Item.ItemOption(138, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                }  if(Util.isTrue(10, 100)) {
//                          it.options.add(new Item.ItemOption(130, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                        it.options.add(new Item.ItemOption(142, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                } else  if(Util.isTrue(10, 100)) {
//                          it.options.add(new Item.ItemOption(131, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                        it.options.add(new Item.ItemOption(143, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                } else if(Util.isTrue(10, 100)) {
//                          it.options.add(new Item.ItemOption(132, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                        it.options.add(new Item.ItemOption(144, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                } else  if(Util.isTrue(10, 100)) {
//                          it.options.add(new Item.ItemOption(129, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                        it.options.add(new Item.ItemOption(141, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                } else  if(Util.isTrue(10, 100)) {
//                          it.options.add(new Item.ItemOption(128, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                        it.options.add(new Item.ItemOption(140, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                } else if(Util.isTrue(10, 100)) {
//                          it.options.add(new Item.ItemOption(127, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                        it.options.add(new Item.ItemOption(139, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//                } 
//         }
//    it.options.add(new Item.ItemOption(30, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(0) + 0)));//giáp 350-470
//        }
//     
//        if (Util.isTrue(40, 100)) {// tỉ lệ ra spl 1-3 sao 70%
//            it.options.add(new Item.ItemOption(107, new Random().nextInt(1) + 3));
//        } else if (Util.isTrue(4, 100)) {// tỉ lệ ra spl 5-7 sao 4%
//            it.options.add(new Item.ItemOption(107, new Random().nextInt(3) + 5));
//        } else {// tỉ lệ ra spl 1-5 sao 6%
//            it.options.add(new Item.ItemOption(107, new Random().nextInt(2) + 3));
//        }
//        return it;
//    }
       public static ItemMap RaitiDoAOSKH(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, y, playerId);
        int ao0 = 0;
        int ao1 = 1;
        int ao2 = 2;
        
        it.options.add(new Item.ItemOption(47, Util.nextInt(2, 6)));
        if (ao0 == tempId) {
       if(Util.isTrue(100, 100)) {
                if(Util.isTrue(30, 100)) {
                        it.options.add(new Item.ItemOption(129, 0));//giáp 350-470
                        it.options.add(new Item.ItemOption(141, 0));//giáp 350-470
                } else  if(Util.isTrue(30, 100)) {
                          it.options.add(new Item.ItemOption(128, 0));//giáp 350-470
                        it.options.add(new Item.ItemOption(140, 0));//giáp 350-470
                } else  if(Util.isTrue(30, 100)) {
                          it.options.add(new Item.ItemOption(127, 0));//giáp 350-470
                        it.options.add(new Item.ItemOption(139, 0));//giáp 350-470
                } 
       }
            
      } else
      if (ao1 == tempId) {
            if(Util.isTrue(100, 100)) {
    
                if(Util.isTrue(30, 100)) {
                         it.options.add(new Item.ItemOption(130, 0));//giáp 350-470
                        it.options.add(new Item.ItemOption(142, 0));//giáp 350-470
                }  else  if(Util.isTrue(30, 100)) {
                        it.options.add(new Item.ItemOption(131, 0));//giáp 350-470
                        it.options.add(new Item.ItemOption(143, 0));//giáp 350-470
          } else  if(Util.isTrue(30, 100)) {
                       it.options.add(new Item.ItemOption(132, 0));//giáp 350-470
                        it.options.add(new Item.ItemOption(144, 0));//giáp 350-470
                }
            }
      } else
       if (ao2 == tempId) {
              if(Util.isTrue(100, 100)) {
                if(Util.isTrue(30, 100)) {
                          it.options.add(new Item.ItemOption(133, 0));//giáp 350-470
                        it.options.add(new Item.ItemOption(136, 0));//giáp 350-470
                } else  if(Util.isTrue(30, 100)) {
                           it.options.add(new Item.ItemOption(134, 0));//giáp 350-470
                        it.options.add(new Item.ItemOption(137, 0));//giáp 350-470
             } else  if(Util.isTrue(30, 100)) {
                          it.options.add(new Item.ItemOption(135, 0));//giáp 350-470
                        it.options.add(new Item.ItemOption(138, 0));//giáp 350-470
                }
           
              }
         
        }
          it.options.add(new Item.ItemOption(30,0));
       
        
        return it;
    }
         public static ItemMap RaitiDoQUANSKH(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, y, playerId);
        int ao0 = 6;
        int ao1 = 7;
        int ao2 = 8;
        
         it.options.add(new Item.ItemOption(6, Util.nextInt(20, 25)));//hp 20-24k
        if (ao0 == tempId) {
          if(Util.isTrue(100, 100)) {
                if(Util.isTrue(30, 100)) {
                        it.options.add(new Item.ItemOption(129, 0));//giáp 350-470
                        it.options.add(new Item.ItemOption(141, 0));//giáp 350-470
                } else  if(Util.isTrue(30, 100)) {
                          it.options.add(new Item.ItemOption(128, 0));//giáp 350-470
                        it.options.add(new Item.ItemOption(140, 0));//giáp 350-470
                } else  if(Util.isTrue(30, 100)) {
                          it.options.add(new Item.ItemOption(127, 0));//giáp 350-470
                        it.options.add(new Item.ItemOption(139, 0));//giáp 350-470
                } 
           } 
      } else
      if (ao1 == tempId) {
          
           if(Util.isTrue(100, 100)) {
                if(Util.isTrue(30, 100)) {
                         it.options.add(new Item.ItemOption(130, 0));//giáp 350-470
                        it.options.add(new Item.ItemOption(142, 0));//giáp 350-470
                }  else  if(Util.isTrue(30, 100)) {
                        it.options.add(new Item.ItemOption(131, 0));//giáp 350-470
                        it.options.add(new Item.ItemOption(143, 0));//giáp 350-470
               } else  if(Util.isTrue(30, 100)) {
                       it.options.add(new Item.ItemOption(132, 0));//giáp 350-470
                        it.options.add(new Item.ItemOption(144, 0));//giáp 350-470
                }
           } 
      } else
       if (ao2 == tempId) {
           if(Util.isTrue(100, 100)) {
                if(Util.isTrue(30, 100)) {
                          it.options.add(new Item.ItemOption(133, 0));//giáp 350-470
                        it.options.add(new Item.ItemOption(136, 0));//giáp 350-470
                } else  if(Util.isTrue(30, 100)) {
                           it.options.add(new Item.ItemOption(134, 0));//giáp 350-470
                        it.options.add(new Item.ItemOption(137, 0));//giáp 350-470
                } else  if(Util.isTrue(30, 100)) {
                          it.options.add(new Item.ItemOption(135, 0));//giáp 350-470
                        it.options.add(new Item.ItemOption(138, 0));//giáp 350-470
                }
           } 
      
          
        }
        it.options.add(new Item.ItemOption(30,0));
       
        
        return it;
    }
              public static ItemMap RaitiDoGANGSKH(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, y, playerId);
        int ao0 = 21;
        int ao1 = 22;
        int ao2 = 23;
        
       it.options.add(new Item.ItemOption(0, Util.nextInt(9, 13)));//2200-2250
        if (ao0 == tempId) {
          if(Util.isTrue(100, 100)) {
                if(Util.isTrue(30, 100)) {
                        it.options.add(new Item.ItemOption(129, 0));//giáp 350-470
                        it.options.add(new Item.ItemOption(141, 0));//giáp 350-470
                } else  if(Util.isTrue(30, 100)) {
                          it.options.add(new Item.ItemOption(128, 0));//giáp 350-470
                        it.options.add(new Item.ItemOption(140, 0));//giáp 350-470
                } else  if(Util.isTrue(30, 100)) {
                          it.options.add(new Item.ItemOption(127, 0));//giáp 350-470
                        it.options.add(new Item.ItemOption(139, 0));//giáp 350-470
                } 
           } 
      } else
      if (ao1 == tempId) {
          
           if(Util.isTrue(100, 100)) {
                if(Util.isTrue(30, 100)) {
                         it.options.add(new Item.ItemOption(130, 0));//giáp 350-470
                        it.options.add(new Item.ItemOption(142, 0));//giáp 350-470
                }  else  if(Util.isTrue(30, 100)) {
                        it.options.add(new Item.ItemOption(131, 0));//giáp 350-470
                        it.options.add(new Item.ItemOption(143, 0));//giáp 350-470
                }  else {
                       it.options.add(new Item.ItemOption(132, 0));//giáp 350-470
                        it.options.add(new Item.ItemOption(144, 0));//giáp 350-470
                }
           } 
      } else
       if (ao2 == tempId) {
           if(Util.isTrue(100, 100)) {
                if(Util.isTrue(30, 100)) {
                          it.options.add(new Item.ItemOption(133, 0));//giáp 350-470
                        it.options.add(new Item.ItemOption(136, 0));//giáp 350-470
                } else  if(Util.isTrue(30, 100)) {
                           it.options.add(new Item.ItemOption(134, 0));//giáp 350-470
                        it.options.add(new Item.ItemOption(137, 0));//giáp 350-470
               } else  if(Util.isTrue(30, 100)) {
                          it.options.add(new Item.ItemOption(135, 0));//giáp 350-470
                        it.options.add(new Item.ItemOption(138, 0));//giáp 350-470
                }
           } 
      
        
        }
         it.options.add(new Item.ItemOption(30,0));
       
        
        return it;
    }
                  public static ItemMap RaitiDoGIAYSKH(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, y, playerId);
        int ao0 = 27;
        int ao1 = 28;
        int ao2 = 29;
        
      it.options.add(new Item.ItemOption(7, Util.nextInt(20, 25)));//20-23k ki
        if (ao0 == tempId) {
          if(Util.isTrue(100, 100)) {
                if(Util.isTrue(30, 100)) {
                        it.options.add(new Item.ItemOption(129, 0));//giáp 350-470
                        it.options.add(new Item.ItemOption(141, 0));//giáp 350-470
                } else  if(Util.isTrue(30, 100)) {
                          it.options.add(new Item.ItemOption(128, 0));//giáp 350-470
                        it.options.add(new Item.ItemOption(140, 0));//giáp 350-470
                } else  if(Util.isTrue(30, 100)) {
                          it.options.add(new Item.ItemOption(127, 0));//giáp 350-470
                        it.options.add(new Item.ItemOption(139, 0));//giáp 350-470
                } 
           } 
      } else
      if (ao1 == tempId) {
          
           if(Util.isTrue(100, 100)) {
                if(Util.isTrue(30, 100)) {
                         it.options.add(new Item.ItemOption(130, 0));//giáp 350-470
                        it.options.add(new Item.ItemOption(142, 0));//giáp 350-470
                }  else  if(Util.isTrue(30, 100)) {
                        it.options.add(new Item.ItemOption(131, 0));//giáp 350-470
                        it.options.add(new Item.ItemOption(143, 0));//giáp 350-470
                 } else  if(Util.isTrue(30, 100)) {
                       it.options.add(new Item.ItemOption(132, 0));//giáp 350-470
                        it.options.add(new Item.ItemOption(144, 0));//giáp 350-470
                }
           } 
      } else
       if (ao2 == tempId) {
           if(Util.isTrue(100, 100)) {
                if(Util.isTrue(30, 100)) {
                          it.options.add(new Item.ItemOption(133, 0));//giáp 350-470
                        it.options.add(new Item.ItemOption(136, 0));//giáp 350-470
                } else  if(Util.isTrue(30, 100)) {
                           it.options.add(new Item.ItemOption(134, 0));//giáp 350-470
                        it.options.add(new Item.ItemOption(137, 0));//giáp 350-470
            } else  if(Util.isTrue(30, 100)) {
                          it.options.add(new Item.ItemOption(135, 0));//giáp 350-470
                        it.options.add(new Item.ItemOption(138, 0));//giáp 350-470
                }
           } 
      
        
        }
         it.options.add(new Item.ItemOption(30,0));
       
        
        return it;
    }
                       public static ItemMap RaitiDoRADASKHTD(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, y, playerId);
        int ao0 = 12;
       
        
      it.options.add(new Item.ItemOption(14, Util.nextInt(1, 3)));//20-23k ki
       
        if (ao0 == tempId) {
         if(Util.isTrue(100, 100)) {
                if(Util.isTrue(30, 100)) {
                        it.options.add(new Item.ItemOption(129, 0));//giáp 350-470
                        it.options.add(new Item.ItemOption(141, 0));//giáp 350-470
                } else  if(Util.isTrue(30, 100)) {
                          it.options.add(new Item.ItemOption(128, 0));//giáp 350-470
                        it.options.add(new Item.ItemOption(140, 0));//giáp 350-470
               } else  if(Util.isTrue(30, 100)) {
                          it.options.add(new Item.ItemOption(127, 0));//giáp 350-470
                        it.options.add(new Item.ItemOption(139, 0));//giáp 350-470
                } 
           } 
     
        
       }
          it.options.add(new Item.ItemOption(30,0));
       
        
        return it;
    }
                            public static ItemMap RaitiDoRADASKHNM(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, y, playerId);
        int ao0 = 12;
       
        
      it.options.add(new Item.ItemOption(14, Util.nextInt(1, 3)));//20-23k ki
   
        if (ao0 == tempId) {
            
        if(Util.isTrue(100, 100)) {
                if(Util.isTrue(30, 100)) {
                         it.options.add(new Item.ItemOption(130, 0));//giáp 350-470
                        it.options.add(new Item.ItemOption(142, 0));//giáp 350-470
                }  else  if(Util.isTrue(30, 100)) {
                        it.options.add(new Item.ItemOption(131, 0));//giáp 350-470
                        it.options.add(new Item.ItemOption(143, 0));//giáp 350-470
                } else  if(Util.isTrue(30, 100)) {
                       it.options.add(new Item.ItemOption(132, 0));//giáp 350-470
                        it.options.add(new Item.ItemOption(144, 0));//giáp 350-470
                }
           
      
        
        }
       }
          it.options.add(new Item.ItemOption(30,0));
       
        
        return it;
    }
        public static ItemMap RaitiDoRADASKHXD(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, y, playerId);
        int ao0 = 12;
       
        
      it.options.add(new Item.ItemOption(14, Util.nextInt(1, 3)));//20-23k ki
 
        if (ao0 == tempId) {
         if(Util.isTrue(100, 100)) {
            if(Util.isTrue(30, 100)) {
                          it.options.add(new Item.ItemOption(133, 0));//giáp 350-470
                        it.options.add(new Item.ItemOption(136, 0));//giáp 350-470
                } else  if(Util.isTrue(30, 100)) {
                           it.options.add(new Item.ItemOption(134, 0));//giáp 350-470
                        it.options.add(new Item.ItemOption(137, 0));//giáp 350-470
              } else  if(Util.isTrue(30, 100)) {
                          it.options.add(new Item.ItemOption(135, 0));//giáp 350-470
                        it.options.add(new Item.ItemOption(138, 0));//giáp 350-470
                }
           
         }
        
        
       }
          it.options.add(new Item.ItemOption(30,0));
       
        
        return it;
    }
     public static ItemMap RaitiDoSKH(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, y, playerId);
        int ao0 = 0;
        int ao1 = 1;
        int ao2 = 2;
        int quan0 = 6;
        int quan1= 7;
        int quan2= 8;
        int gang0= 21;  
        int gang1= 22;
        int gang2= 23;
        int giay0 = 27;
        int giay1= 28;
        int giay2= 29;
        int rada = 12;
       
        if (ao0 == tempId) {
            it.options.add(new Item.ItemOption(47, Util.nextInt(2, 6)));
               if (Util.isTrue(100, 100)) {
                   
               }
        }
        if (ao1 == tempId) {
            it.options.add(new Item.ItemOption(77, 7));
        }
        if (ao2 == tempId) {
            it.options.add(new Item.ItemOption(103, 7));
        }
        if (quan0 == tempId) {
            it.options.add(new Item.ItemOption(14, (int) (float) 0.5f));
        }
        if (quan1 == tempId) {
            it.options.add(new Item.ItemOption(94, 3));
        }
        if (quan2 == tempId) {
            it.options.add(new Item.ItemOption(95, 5));
        }
        if (gang0 == tempId) {
            it.options.add(new Item.ItemOption(96, 5));
        }
        if (gang1 == tempId) {
            it.options.add(new Item.ItemOption(97, 5));
        }
        if (gang2 == tempId) {
            it.options.add(new Item.ItemOption(99, 3));
        }
        if (giay0 == tempId) {
            it.options.add(new Item.ItemOption(98, 3));
        }
        if (giay1 == tempId) {
            it.options.add(new Item.ItemOption(100, 5));
        }
        if (giay2 == tempId) {
            it.options.add(new Item.ItemOption(101, 5));
        }
        if (rada == tempId) {
            it.options.add(new Item.ItemOption(71, 5));
        }
        
        return it;
    }
    
     public static Item ratiItemSKH(int tempId) {
     Item it = ItemService.gI().createNewItem((short) tempId);
        List<Integer> ao = Arrays.asList(0, 1, 2);
        List<Integer> quan = Arrays.asList(6, 7, 8);
        List<Integer> gang = Arrays.asList(21, 22, 23);
        List<Integer> giay = Arrays.asList(27, 28, 29);
        int rd12 = 12;
        if (ao.contains(tempId)) {
            it.itemOptions.add(new Item.ItemOption(47, 5));//giáp 350-470
      if (ao.contains(0)) {
           if(Util.isTrue(100, 100)) {
                if(Util.isTrue(10, 100)) {
                        it.itemOptions.add(new Item.ItemOption(129, 0));//giáp 350-470
                        it.itemOptions.add(new Item.ItemOption(141, 0));//giáp 350-470
                } else  if(Util.isTrue(10, 100)) {
                          it.itemOptions.add(new Item.ItemOption(128, 0));//giáp 350-470
                        it.itemOptions.add(new Item.ItemOption(140, 0));//giáp 350-470
                }  else if(Util.isTrue(10, 100)) {
                          it.itemOptions.add(new Item.ItemOption(127, 0));//giáp 350-470
                        it.itemOptions.add(new Item.ItemOption(139, 0));//giáp 350-470
                } 
           } 
      } else
         if (ao.contains(1)) {
           if(Util.isTrue(100, 100)) {
                if(Util.isTrue(10, 100)) {
                         it.itemOptions.add(new Item.ItemOption(130, 0));//giáp 350-470
                        it.itemOptions.add(new Item.ItemOption(142, 0));//giáp 350-470
                }  else  if(Util.isTrue(10, 100)) {
                        it.itemOptions.add(new Item.ItemOption(131, 0));//giáp 350-470
                        it.itemOptions.add(new Item.ItemOption(143, 0));//giáp 350-470
                }  else if(Util.isTrue(10, 100)) {
                       it.itemOptions.add(new Item.ItemOption(132, 0));//giáp 350-470
                        it.itemOptions.add(new Item.ItemOption(144, 0));//giáp 350-470
                }
           } 
      } else
          if (ao.contains(2)) {
           if(Util.isTrue(100, 100)) {
                if(Util.isTrue(10, 100)) {
                          it.itemOptions.add(new Item.ItemOption(133, 0));//giáp 350-470
                        it.itemOptions.add(new Item.ItemOption(136, 0));//giáp 350-470
                } else  if(Util.isTrue(10, 100)) {
                           it.itemOptions.add(new Item.ItemOption(134, 0));//giáp 350-470
                        it.itemOptions.add(new Item.ItemOption(137, 0));//giáp 350-470
                } else if(Util.isTrue(10, 100)) {
                          it.itemOptions.add(new Item.ItemOption(135, 0));//giáp 350-470
                        it.itemOptions.add(new Item.ItemOption(138, 0));//giáp 350-470
                }
           } 
      }
          it.itemOptions.add(new Item.ItemOption(30,0));//giáp 350-470
        }
        
        if (quan.contains(tempId)) {
            it.itemOptions.add(new Item.ItemOption(6, 25));//hp 20-24k
        if (quan.contains(6)) {
           if(Util.isTrue(100, 100)) {
                if(Util.isTrue(10, 100)) {
                       it.itemOptions.add(new Item.ItemOption(129, 0));//giáp 350-470
                        it.itemOptions.add(new Item.ItemOption(141, 0));//giáp 350-470
                } else  if(Util.isTrue(10, 100)) {
                          it.itemOptions.add(new Item.ItemOption(128, 0));//giáp 350-470
                        it.itemOptions.add(new Item.ItemOption(140, 0));//giáp 350-470
                }  else if(Util.isTrue(10, 100)) {
                           it.itemOptions.add(new Item.ItemOption(127, 0));//giáp 350-470
                        it.itemOptions.add(new Item.ItemOption(139, 0));//giáp 350-470
                } 
           } 
      } else
         if (quan.contains(7)) {
           if(Util.isTrue(100, 100)) {
                if(Util.isTrue(10, 100)) {
                       it.itemOptions.add(new Item.ItemOption(130, 0));//giáp 350-470
                        it.itemOptions.add(new Item.ItemOption(142, 0));//giáp 350-470
                }  else  if(Util.isTrue(10, 100)) {
                        it.itemOptions.add(new Item.ItemOption(131, 0));//giáp 350-470
                        it.itemOptions.add(new Item.ItemOption(143, 0));//giáp 350-470
                }  else if(Util.isTrue(10, 100)) {
                        it.itemOptions.add(new Item.ItemOption(132, 0));//giáp 350-470
                        it.itemOptions.add(new Item.ItemOption(144, 0));//giáp 350-470
                }
           } 
      } else
          if (quan.contains(8)) {
           if(Util.isTrue(100, 100)) {
                if(Util.isTrue(10, 100)) {
                           it.itemOptions.add(new Item.ItemOption(133, 0));//giáp 350-470
                        it.itemOptions.add(new Item.ItemOption(136, 0));//giáp 350-470
                } else  if(Util.isTrue(10, 100)) {
                          it.itemOptions.add(new Item.ItemOption(134, 0));//giáp 350-470
                        it.itemOptions.add(new Item.ItemOption(137, 0));//giáp 350-470
                } else if(Util.isTrue(10, 100)) {
                           it.itemOptions.add(new Item.ItemOption(135, 0));//giáp 350-470
                        it.itemOptions.add(new Item.ItemOption(138, 0));//giáp 350-470
                }
           } 
      }
         it.itemOptions.add(new Item.ItemOption(30,0));//giáp 350-470
        }
        
        if (gang.contains(tempId)) {
            it.itemOptions.add(new Item.ItemOption(0, 13));//2200-2250
         if (gang.contains(21)) {
           if(Util.isTrue(100, 100)) {
                if(Util.isTrue(10, 100)) {
                         it.itemOptions.add(new Item.ItemOption(129, 0));//giáp 350-470
                        it.itemOptions.add(new Item.ItemOption(141, 0));//giáp 350-470
                } else  if(Util.isTrue(10, 100)) {
                           it.itemOptions.add(new Item.ItemOption(128, 0));//giáp 350-470
                        it.itemOptions.add(new Item.ItemOption(140, 0));//giáp 350-470
                } else if(Util.isTrue(10, 100)) {
                          it.itemOptions.add(new Item.ItemOption(127, 0));//giáp 350-470
                        it.itemOptions.add(new Item.ItemOption(139, 0));//giáp 350-470
                } 
           } 
      } else
         if (gang.contains(22)) {
           if(Util.isTrue(100, 100)) {
                if(Util.isTrue(10, 100)) {
                           it.itemOptions.add(new Item.ItemOption(130, 0));//giáp 350-470
                        it.itemOptions.add(new Item.ItemOption(142, 0));//giáp 350-470
                }  else  if(Util.isTrue(10, 100)) {
                          it.itemOptions.add(new Item.ItemOption(131, 0));//giáp 350-470
                        it.itemOptions.add(new Item.ItemOption(143, 0));//giáp 350-470
                }  else if(Util.isTrue(10, 100)) {
                          it.itemOptions.add(new Item.ItemOption(132, 0));//giáp 350-470
                        it.itemOptions.add(new Item.ItemOption(144, 0));//giáp 350-470
                }
           } 
      } else
          if (gang.contains(23)) {
           if(Util.isTrue(100, 100)) {
                if(Util.isTrue(10, 100)) {
     it.itemOptions.add(new Item.ItemOption(133, 0));//giáp 350-470
                        it.itemOptions.add(new Item.ItemOption(136, 0));//giáp 350-470
                } else  if(Util.isTrue(10, 100)) {
                    
                           it.itemOptions.add(new Item.ItemOption(134, 0));//giáp 350-470
                        it.itemOptions.add(new Item.ItemOption(137, 0));//giáp 350-470
                } else if(Util.isTrue(10, 100)) {
                           it.itemOptions.add(new Item.ItemOption(135, 0));//giáp 350-470
                        it.itemOptions.add(new Item.ItemOption(138, 0));//giáp 350-470
                } 
           } 
      }
            it.itemOptions.add(new Item.ItemOption(30,0));//giáp 350-470
        }
        
        if (giay.contains(tempId)) {
            it.itemOptions.add(new Item.ItemOption(7, 24));//20-23k ki
          if (giay.contains(27)) {
           if(Util.isTrue(100, 100)) {
                if(Util.isTrue(10, 100)) {
                          it.itemOptions.add(new Item.ItemOption(129, 0));//giáp 350-470
                        it.itemOptions.add(new Item.ItemOption(141, 0));//giáp 350-470
                } else  if(Util.isTrue(10, 100)) {
                           it.itemOptions.add(new Item.ItemOption(128, 0));//giáp 350-470
                        it.itemOptions.add(new Item.ItemOption(140, 0));//giáp 350-470
                }  else if(Util.isTrue(10, 100)) {
                            it.itemOptions.add(new Item.ItemOption(127, 0));//giáp 350-470
                        it.itemOptions.add(new Item.ItemOption(139, 0));//giáp 350-470
                } 
           } 
      } else
         if (giay.contains(28)) {
           if(Util.isTrue(100, 100)) {
                if(Util.isTrue(10, 100)) {
                            it.itemOptions.add(new Item.ItemOption(130, 0));//giáp 350-470
                        it.itemOptions.add(new Item.ItemOption(142, 0));//giáp 350-470
                }  else  if(Util.isTrue(10, 100)) {
                         it.itemOptions.add(new Item.ItemOption(131, 0));//giáp 350-470
                        it.itemOptions.add(new Item.ItemOption(143, 0));//giáp 350-470
                }  else if(Util.isTrue(10, 100)) {
                          it.itemOptions.add(new Item.ItemOption(132, 0));//giáp 350-470
                        it.itemOptions.add(new Item.ItemOption(144, 0));//giáp 350-470
                }
           } 
      } else
          if (giay.contains(29)) {
           if(Util.isTrue(100, 100)) {
                if(Util.isTrue(10, 100)) {
                        it.itemOptions.add(new Item.ItemOption(133, 0));//giáp 350-470
                        it.itemOptions.add(new Item.ItemOption(136, 0));//giáp 350-470
                } else  if(Util.isTrue(10, 100)) {
                          it.itemOptions.add(new Item.ItemOption(134, 0));//giáp 350-470
                        it.itemOptions.add(new Item.ItemOption(137, 0));//giáp 350-470
                } else if(Util.isTrue(10, 100)) {
                         it.itemOptions.add(new Item.ItemOption(135, 0));//giáp 350-470
                        it.itemOptions.add(new Item.ItemOption(138, 0));//giáp 350-470
                }
           } 
      }
          it.itemOptions.add(new Item.ItemOption(30,0));//giáp 350-470
        }
        
        if (rd12 == tempId) {
            it.itemOptions.add(new Item.ItemOption(14, new Random().nextInt(3) + 10));//10-12cm
         if(Util.isTrue(100, 100)) {
                if(Util.isTrue(10, 100)) {
                          it.itemOptions.add(new Item.ItemOption(133, 0));//giáp 350-470
                        it.itemOptions.add(new Item.ItemOption(136, 0));//giáp 350-470
                } else  if(Util.isTrue(10, 100)) {
                          it.itemOptions.add(new Item.ItemOption(134, 0));//giáp 350-470
                        it.itemOptions.add(new Item.ItemOption(137, 0));//giáp 350-470
                } else if(Util.isTrue(10, 100)) {
                          it.itemOptions.add(new Item.ItemOption(135, 0));//giáp 350-470
                        it.itemOptions.add(new Item.ItemOption(138, 0));//giáp 350-470
                }  if(Util.isTrue(10, 100)) {
                          it.itemOptions.add(new Item.ItemOption(130, 0));//giáp 350-470
                        it.itemOptions.add(new Item.ItemOption(142, 0));//giáp 350-470
                } else  if(Util.isTrue(10, 100)) {
                          it.itemOptions.add(new Item.ItemOption(131, 0));//giáp 350-470
                        it.itemOptions.add(new Item.ItemOption(143, 0));//giáp 350-470
                } else if(Util.isTrue(10, 100)) {
                          it.itemOptions.add(new Item.ItemOption(132, 0));//giáp 350-470
                        it.itemOptions.add(new Item.ItemOption(144, 0));//giáp 350-470
                } else  if(Util.isTrue(10, 100)) {
                          it.itemOptions.add(new Item.ItemOption(129, 0));//giáp 350-470
                        it.itemOptions.add(new Item.ItemOption(141, 0));//giáp 350-470
                } else  if(Util.isTrue(10, 100)) {
                          it.itemOptions.add(new Item.ItemOption(128, 0));//giáp 350-470
                        it.itemOptions.add(new Item.ItemOption(140, 0));//giáp 350-470
                } else if(Util.isTrue(10, 100)) {
                          it.itemOptions.add(new Item.ItemOption(127, 0));//giáp 350-470
                        it.itemOptions.add(new Item.ItemOption(139, 0));//giáp 350-470
                } 
         }
    it.itemOptions.add(new Item.ItemOption(30,0));//giáp 350-470
        }
     
        if (Util.isTrue(40, 100)) {// tỉ lệ ra spl 1-3 sao 70%
            it.itemOptions.add(new Item.ItemOption(107, new Random().nextInt(1) + 3));
        } else if (Util.isTrue(4, 100)) {// tỉ lệ ra spl 5-7 sao 4%
            it.itemOptions.add(new Item.ItemOption(107, new Random().nextInt(3) + 5));
        } else {// tỉ lệ ra spl 1-5 sao 6%
            it.itemOptions.add(new Item.ItemOption(107, new Random().nextInt(2) + 3));
        }
        return it;
    }
    public static Item petviprandom(int tempId) {
        Item gapvip = ItemService.gI().createNewItem((short) tempId);
        if(Util.isTrue(90, 100)) {    
            gapvip.itemOptions.add(new Item.ItemOption(50, Util.nextInt(15,25)));
            gapvip.itemOptions.add(new Item.ItemOption(103, Util.nextInt(15,25)));
            gapvip.itemOptions.add(new Item.ItemOption(77, Util.nextInt(15,25)));
        if(Util.isTrue(80, 100)) {
            gapvip.itemOptions.add(new Item.ItemOption(93,Util.nextInt(7,15)));    
        }} else {   
            gapvip.itemOptions.add(new Item.ItemOption(0, Util.nextInt(5511,10512)));
            gapvip.itemOptions.add(new Item.ItemOption(6, Util.nextInt(7151,12515)));
            gapvip.itemOptions.add(new Item.ItemOption(7, Util.nextInt(7152,12115)));
        if(Util.isTrue(80, 100)) {
            gapvip.itemOptions.add(new Item.ItemOption(93,Util.nextInt(7,15)));    
        }}
        return gapvip;
    } 
    
    public static ItemMap caitrangcooler(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, y, playerId);
        int thoivang = 1098;
        if (thoivang == tempId) {
            it.options.add(new Item.ItemOption(209, 1));
            it.options.add(new Item.ItemOption(30, 1));
            it.options.add(new Item.ItemOption(93, highlightsItem(it.itemTemplate.gender == 0 && it.itemTemplate.gender == 1 && it.itemTemplate.gender == 2, new Random().nextInt(99) + 1)));
        }
        return it;
    }

    public static int highlightsItem(boolean highlights, int value) {
        double highlightsNumber = 1.1;
        return highlights ? (int) (value * highlightsNumber) : value;
    }

    public static Item sendDo(int itemId, int sql, List<Item.ItemOption> ios) {
//        InventoryServiceNew.gI().addItemBag(player, ItemService.gI().createItemFromItemShop(is));
//        InventoryServiceNew.gI().sendItemBags(player);
        Item item = ItemService.gI().createNewItem((short) itemId);
        item.itemOptions.addAll(ios);
        item.itemOptions.add(new Item.ItemOption(107, sql));
        return item;
    }

    public static boolean checkDo(Item.ItemOption itemOption) {
        switch (itemOption.optionTemplate.id) {
            case 0:// tấn công
                if (itemOption.param > 12000) {
                    return false;
                }
                break;
            case 14:// chí mạng
                if (itemOption.param > 30) {
                    return false;
                }
                break;
            case 107:// spl
            case 102:// spl
                if (itemOption.param > 8) {
                    return false;
                }
                break;
            case 77:
            case 103:
            case 95:
            case 96:
                if (itemOption.param > 41) {
                    return false;
                }
                break;
            case 50:// sd 3%
                if (itemOption.param > 24) {
                    return false;
                }
                break;
            case 6:// hp
            case 7:// ki
                if (itemOption.param > 120000) {
                    return false;
                }
                break;
            case 47:// giáp
                if (itemOption.param > 3500) {
                    return false;
                }
                break;
        }
        return true;
    }

    public static void useCheckDo(Player player, Item item, String position) {
        try {
            if (item.template != null) {
                if (item.template.id >= 381 && item.template.id <= 385) {
                    return;
                }
                if (item.template.id >= 66 && item.template.id <= 135) {
                    return;
                }
                if (item.template.id >= 474 && item.template.id <= 515) {
                    return;
                }
                item.itemOptions.forEach(itemOption -> {
                    if (!Util.checkDo(itemOption)) {
                        Logger.error(player.name + "-" + item.template.name + "-" + position + "\n");
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String phanthuong(int i) {
        switch (i) {
            case 1:
                return "5tr";
            case 2:
                return "3tr";
            case 3:
                return "1tr";
            default:
                return "100k";
        }
    }

   

    public static long tinhLuyThua(int coSo, int soMu) {
        long ketQua = 1;

        for (int i = 0; i < soMu; i++) {
            ketQua *= coSo;
        }
        return ketQua;
    }

    public static void checkPlayer(Player player) {
        new Thread(() -> {
            List<Player> list = Client.gI().getPlayers().stream().filter(p -> !p.isPet && !p.isNewPet && p.getSession().userId == player.getSession().userId).collect(Collectors.toList());
            if (list.size() > 1) {
                list.forEach(pp -> Client.gI().kickSession(pp.getSession()));
                list.clear();
            }
        }).start();
    }

    public static int[] pickNRandInArr(int[] array, int n) {
        List<Integer> list = new ArrayList<Integer>(array.length);
        for (int i : array) {
            list.add(i);
        }
        Collections.shuffle(list);
        int[] answer = new int[n];
        for (int i = 0; i < n; i++) {
            answer[i] = list.get(i);
        }
        Arrays.sort(answer);
        return answer;
    }
}
