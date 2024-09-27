package ServerData.Models.Player;

import Server.Data.Consts.ConstEvent;
import ServerData.Models.Map.ListMap.NgocRongSaoDenService;
import Server.Data.Consts.ConstPlayer;
import Server.Data.Consts.ConstRatio;
import ServerData.Models.Intrinsic.Intrinsic;
import ServerData.Models.Item.Item;
import ServerData.Models.Player.PlayerSkill.Skill;
import ServerData.Server.Manager;
import ServerData.Services.EffectSkillService;
import ServerData.Services.ItemService;
import ServerData.Services.MapService;
import ServerData.Services.PlayerService;
import ServerData.Services.Service;
import ServerData.Services.TaskService;
import ServerData.Utils.Logger;
import ServerData.Utils.SkillUtil;
import ServerData.Utils.Util;

import java.util.ArrayList;
import java.util.List;

public class NPoint {

    public static final byte MAX_LIMIT = 9;

    private Player player;
    private Pet pet;

    public NPoint(Player player) {
        this.player = player;
        this.tlHp = new ArrayList<>();
        this.tlMp = new ArrayList<>();
        this.tlDef = new ArrayList<>();
        this.tlDame = new ArrayList<>();
        this.tlDameAttMob = new ArrayList<>();
        this.tlSDDep = new ArrayList<>();
        this.tlTNSM = new ArrayList<>();
        this.tlDameCrit = new ArrayList<>();
    }

    public boolean isCrit;
    public boolean isCrit100;

    private Intrinsic intrinsic;
    private int percentDameIntrinsic;
    public int dameAfter;

    /*-----------------------Chỉ số cơ bản------------------------------------*/
    public byte numAttack;
    public short stamina, maxStamina;

    public byte limitPower;
    public long power;
    public long tiemNang;

    public long hp, hpMax, hpg;
    public long mp, mpMax, mpg;
    public long dame, dameg;
    public int def, defg;
    public int crit, critg;
    public byte speed = 8;

    public boolean teleport;

    public boolean khangTDHS;
    public boolean isDraburaFrost; //Cải trang Dracula Frost
    public boolean isDrabura; //Cải trang Dracula

    /**
     * Chỉ số cộng thêm
     */
    public double hpAdd, mpAdd, dameAdd, defAdd, critAdd, hpHoiAdd, mpHoiAdd;

    /**
     * //+#% sức đánh chí mạng
     */
    public List<Integer> tlDameCrit;

    /**
     * Tỉ lệ hp, mp cộng thêm
     */
    public List<Integer> tlHp, tlMp;

    /**
     * Tỉ lệ giáp cộng thêm
     */
    public List<Integer> tlDef;

    /**
     * Tỉ lệ sức đánh/ sức đánh khi đánh quái
     */
    public List<Integer> tlDame, tlDameAttMob;

    /**
     * Lượng hp, mp hồi mỗi 30s, mp hồi cho người khác
     */
    public long hpHoi, mpHoi, mpHoiCute;

    /**
     * Tỉ lệ hp, mp hồi cộng thêm
     */
    public short tlHpHoi, tlMpHoi;

    /**
     * Tỉ lệ hp, mp hồi bản thân và đồng đội cộng thêm
     */
    public short tlHpHoiBanThanVaDongDoi, tlMpHoiBanThanVaDongDoi;

    /**
     * Tỉ lệ hút hp, mp khi đánh, hp khi đánh quái
     */
    public short tlHutHp, tlHutMp, tlHutHpMob;

    /**
     * Tỉ lệ hút hp, mp xung quanh mỗi 5s
     */
    public short tlHutHpMpXQ;

    /**
     * Tỉ lệ phản sát thương
     */
    public short tlPST;

    /**
     * Tỉ lệ tiềm năng sức mạnh
     */
    public List<Integer> tlTNSM;

    /**
     * Tỉ lệ vàng cộng thêm
     */
    public short tlGold;

    /**
     * Tỉ lệ né đòn
     */
    public int tlNeDon;

    /**
     * Tỉ lệ sức đánh đẹp cộng thêm cho bản thân và người xung quanh
     */
    public List<Integer> tlSDDep;

    /**
     * Tỉ lệ giảm sức đánh
     */
    public short tlSubSD;

    public int voHieuChuong;

    /*------------------------Effect skin-------------------------------------*/
    public Item trainArmor;
    public boolean wornTrainArmor;
    public boolean wearingTrainArmor;

    public boolean wearingVoHinh;
    public boolean isKhongLanh;
    public boolean goteta;
    public short tlHpGiamODo;
    public short test;

    /*-------------------------------------------------------------------------*/
    /**
     * Tính toán mọi chỉ số sau khi có thay đổi
     */
    public void calPoint() {
        if (this.player.pet != null) {
            this.player.pet.nPoint.setPointWhenWearClothes();
        }
        this.setPointWhenWearClothes();
    }

    private void setPointWhenWearClothes() {
        resetPoint();
        if (this.player.rewardBlackBall.timeOutOfDateReward[1] > System.currentTimeMillis()) {
            tlHutMp += NgocRongSaoDenService.R2S_1;
        }
        if (this.player.rewardBlackBall.timeOutOfDateReward[3] > System.currentTimeMillis()) {
            tlDameAttMob.add(NgocRongSaoDenService.R4S_2);
        }
        if (this.player.rewardBlackBall.timeOutOfDateReward[4] > System.currentTimeMillis()) {
            tlPST += NgocRongSaoDenService.R5S_1;
        }
        if (this.player.rewardBlackBall.timeOutOfDateReward[5] > System.currentTimeMillis()) {
            tlPST += NgocRongSaoDenService.R6S_1;
            tlNeDon += NgocRongSaoDenService.R6S_2;
        }
        if (this.player.rewardBlackBall.timeOutOfDateReward[6] > System.currentTimeMillis()) {
            tlHpHoi += NgocRongSaoDenService.R7S_1;
            tlHutHp += NgocRongSaoDenService.R7S_2;
        }

        this.player.setClothes.worldcup = 0;
        for (Item item : this.player.inventory.itemsBody) {
            if (item.isNotNullItem()) {
                switch (item.template.id) {
                    case 966:
                    case 982:
                    case 983:
                    case 883:
                    case 904:
                        player.setClothes.worldcup++;
                }
                if (item.template.id == 1218) {
                    teleport = true;
                }
                if (item.template.id >= 592 && item.template.id <= 594) {
                    teleport = true;
                }
                Card card = player.Cards.stream().filter(r -> r != null && r.Used == 1).findFirst().orElse(null);
                if (card != null) {
                    for (OptionCard cardopt : card.Options) {
                        if (cardopt.active == card.Level || (card.Level == -1 && cardopt.active == 0)) {
                            switch (cardopt.id) {
                                case 0: //Tấn công +#
                                    this.dameAdd += cardopt.param;
                                    break;
                                case 2: //HP, KI+#000
                                    this.hpAdd += cardopt.param * 1000;
                                    this.mpAdd += cardopt.param * 1000;
                                    break;
                                case 3:// fake
                                    this.voHieuChuong += cardopt.param;
                                    break;
                                case 5: //+#% sức đánh chí mạng
                                    this.tlDameCrit.add(cardopt.param);
                                    break;
                                case 6: //HP+#
                                    this.hpAdd += cardopt.param;
                                    break;
                                case 7: //KI+#
                                    this.mpAdd += cardopt.param;
                                    break;
                                case 8: //Hút #% HP, KI xung quanh mỗi 5 giây
                                    this.tlHutHpMpXQ += cardopt.param;
                                    break;
                                case 14: //Chí mạng+#%
                                    this.critAdd += cardopt.param;
                                    break;
                                case 19: //Tấn công+#% khi đánh quái
                                    this.tlDameAttMob.add(cardopt.param);
                                    break;
                                case 22: //HP+#K
                                    this.hpAdd += cardopt.param * 1000;
                                    break;
                                case 23: //MP+#K
                                    this.mpAdd += cardopt.param * 1000;
                                    break;
                                case 27: //+# HP/30s
                                    this.hpHoiAdd += cardopt.param;
                                    break;
                                case 28: //+# KI/30s
                                    this.mpHoiAdd += cardopt.param;
                                    break;
                                case 33: //dịch chuyển tức thời
                                    this.teleport = true;
                                    break;
                                case 47: //Giáp+#
                                    this.defAdd += cardopt.param;
                                    break;
                                case 48: //HP/KI+#
                                    this.hpAdd += cardopt.param;
                                    this.mpAdd += cardopt.param;
                                    break;
                                case 49: //Tấn công+#%
                                case 50: //Sức đánh+#%
                                    this.tlDame.add(cardopt.param);
                                    break;
                                case 77: //HP+#%
                                    this.tlHp.add(cardopt.param);
                                    break;
                                case 80: //HP+#%/30s
                                    this.tlHpHoi += cardopt.param;
                                    break;
                                case 81: //MP+#%/30s
                                    this.tlMpHoi += cardopt.param;
                                    break;
                                case 88: //Cộng #% exp khi đánh quái
                                    this.tlTNSM.add(cardopt.param);
                                    break;
                                case 94: //Giáp #%
                                    this.tlDef.add(cardopt.param);
                                    break;
                                case 95: //Biến #% tấn công thành HP
                                    this.tlHutHp += cardopt.param;
                                    break;
                                case 96: //Biến #% tấn công thành MP
                                    this.tlHutMp += cardopt.param;
                                    break;
                                case 97: //Phản #% sát thương
                                    this.tlPST += cardopt.param;
                                    break;
                                case 100: //+#% vàng từ quái
                                    this.tlGold += cardopt.param;
                                    break;
                                case 101: //+#% TN,SM
                                    this.tlTNSM.add(cardopt.param);
                                    break;
                                case 103: //KI +#%
                                    this.tlMp.add(cardopt.param);
                                    break;
                                case 104: //Biến #% tấn công quái thành HP
                                    this.tlHutHpMob += cardopt.param;
                                    break;
                                case 147: //+#% sức đánh
                                    this.tlDame.add(cardopt.param);
                                    break;
                                    case 206:
                                    this.goteta=true;
                                    break;
                            }
                        }
                    }
                }
                for (Item.ItemOption io : item.itemOptions) {
                    switch (io.optionTemplate.id) {
                        case 0: //Tấn công +#
                            this.dameAdd += io.param;
                            break;
                        case 2: //HP, KI+#000
                            this.hpAdd += io.param * 1000;
                            this.mpAdd += io.param * 1000;
                            break;
                        case 3:// fake
                            this.voHieuChuong += io.param;
                            break;
                        case 5: //+#% sức đánh chí mạng
                            this.tlDameCrit.add(io.param);
                            break;
                        case 6: //HP+#
                            this.hpAdd += io.param;
                            break;
                        case 7: //KI+#
                            this.mpAdd += io.param;
                            break;
                        case 8: //Hút #% HP, KI xung quanh mỗi 5 giây
                            this.tlHutHpMpXQ += io.param;
                            break;
                        case 14: //Chí mạng+#%
                            this.critAdd += io.param;
                            break;
                        case 19: //Tấn công+#% khi đánh quái
                            this.tlDameAttMob.add(io.param);
                            break;
                        case 22: //HP+#K
                            this.hpAdd += io.param * 1000;
                            break;
                        case 23: //MP+#K
                            this.mpAdd += io.param * 1000;
                            break;
                        case 27: //+# HP/30s
                            this.hpHoiAdd += io.param;
                            break;
                        case 28: //+# KI/30s
                            this.mpHoiAdd += io.param;
                            break;
                        case 33: //dịch chuyển tức thời
                            this.teleport = true;
                            break;
                        case 47: //Giáp+#
                            this.defAdd += io.param;
                            break;
                        case 48: //HP/KI+#
                            this.hpAdd += io.param;
                            this.mpAdd += io.param;
                            break;
                        case 49: //Tấn công+#%
                        case 50: //Sức đánh+#%
                            this.tlDame.add(io.param);
                            break;
                        case 77: //HP+#%
                            this.tlHp.add(io.param);
                            break;
                        case 80: //HP+#%/30s
                            this.tlHpHoi += io.param;
                            break;
                        case 81: //MP+#%/30s
                            this.tlMpHoi += io.param;
                            break;
                        case 88: //Cộng #% exp khi đánh quái
                            this.tlTNSM.add(io.param);
                            break;
                        case 94: //Giáp #%
                            this.tlDef.add(io.param);
                            break;
                        case 95: //Biến #% tấn công thành HP
                            this.tlHutHp += io.param;
                            break;
                        case 96: //Biến #% tấn công thành MP
                            this.tlHutMp += io.param;
                            break;
                        case 97: //Phản #% sát thương
                            this.tlPST += io.param;
                            break;
                        case 100: //+#% vàng từ quái
                            this.tlGold += io.param;
                            break;
                        case 101: //+#% TN,SM
                            this.tlTNSM.add(io.param);
                            break;
                        case 103: //KI +#%
                            this.tlMp.add(io.param);
                            break;
                        case 104: //Biến #% tấn công quái thành HP
                            this.tlHutHpMob += io.param;
                            break;
                        case 105: //Vô hình khi không đánh quái và boss
                            this.wearingVoHinh = true;
                            break;
                        case 106: //Không ảnh hưởng bởi cái lạnh
                            this.isKhongLanh = true;
                            break;
                        case 108: //#% Né đòn
                            this.tlNeDon += io.param;// đối nghịch
                            break;
                        case 109: //Hôi, giảm #% HP
                            this.tlHpGiamODo += io.param;
                            break;
                        case 116: //Kháng thái dương hạ san
                            this.khangTDHS = true;
                            break;
                        case 117: //Đẹp +#% SĐ cho mình và người xung quanh
                            this.tlSDDep.add(io.param);
                            break;
                        case 147: //+#% sức đánh
                            this.tlDame.add(io.param);
                            break;
                        case 75: //Giảm 50% sức đánh, HP, KI và +#% SM, TN, vàng từ quái
                            this.tlSubSD += 50;
                            this.tlTNSM.add(io.param);
                            this.tlGold += io.param;
                            break;
                        case 162: //Cute hồi #% KI/s bản thân và xung quanh
                            this.mpHoiCute += io.param;
                            break;
                        case 173: //Phục hồi #% HP và KI cho đồng đội
                            this.tlHpHoiBanThanVaDongDoi += io.param;
                            this.tlMpHoiBanThanVaDongDoi += io.param;
                            break;
                             case 26: //Dracula hóa đá
                            this.isDrabura = true;
                            break;
                        case 205: // Dracula Frost
                            this.isDraburaFrost = true;
                            break;
                        case 211: //test
                            this.test += io.param;
                            break;
                             case 206:
                                    this.goteta=true;
                                    break;
                    }
                }
            }
        }
        if (this.player.isPl() && this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
            for (Item item : this.player.inventory.itemsBag) {
                if (item.isNotNullItem() && item.template.id == 921) {
                    for (Item.ItemOption io : item.itemOptions) {
                        switch (io.optionTemplate.id) {
                            case 14: //Chí mạng+#%
                                this.critAdd += io.param;
                                break;
                            case 50: //Sức đánh+#%
                                this.tlDame.add(io.param);
                                break;
                            case 77: //HP+#%
                                this.tlHp.add(io.param);
                                break;
                            case 80: //HP+#%/30s
                                this.tlHpHoi += io.param;
                                break;
                            case 81: //MP+#%/30s
                                this.tlMpHoi += io.param;
                                break;
                            case 94: //Giáp #%
                                this.tlDef.add(io.param);
                                break;
                            case 103: //KI +#%
                                this.tlMp.add(io.param);
                                break;
                            case 108: //#% Né đòn
                                this.tlNeDon += io.param;
                                break;
                        }
                    }
                    break;
                }
            }
        } else if (this.player.isPl() && this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
            for (Item item : this.player.inventory.itemsBag) {
                if (item.isNotNullItem() && item.template.id == 1165) {
                    for (Item.ItemOption io : item.itemOptions) {
                        switch (io.optionTemplate.id) {
                            case 14: //Chí mạng+#%
                                this.critAdd += io.param;
                                break;
                            case 50: //Sức đánh+#%
                                this.tlDame.add(io.param);
                                break;
                            case 77: //HP+#%
                                this.tlHp.add(io.param);
                                break;
                            case 80: //HP+#%/30s
                                this.tlHpHoi += io.param;
                                break;
                            case 81: //MP+#%/30s
                                this.tlMpHoi += io.param;
                                break;
                            case 94: //Giáp #%
                                this.tlDef.add(io.param);
                                break;
                            case 103: //KI +#%
                                this.tlMp.add(io.param);
                                break;
                            case 108: //#% Né đòn
                                this.tlNeDon += io.param;
                                break;
                                 case 206:
                                    this.goteta=true;
                                    break;
                        }
                    }
                    break;
                }
            }
        } else if (this.player.isPl() && this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
            for (Item item : this.player.inventory.itemsBag) {
                if (item.isNotNullItem() && item.template.id == 1129) {
                    for (Item.ItemOption io : item.itemOptions) {
                        switch (io.optionTemplate.id) {
                            case 14: //Chí mạng+#%
                                this.critAdd += io.param;
                                break;
                            case 50: //Sức đánh+#%
                                this.tlDame.add(io.param);
                                break;
                            case 77: //HP+#%
                                this.tlHp.add(io.param);
                                break;
                            case 80: //HP+#%/30s
                                this.tlHpHoi += io.param;
                                break;
                            case 81: //MP+#%/30s
                                this.tlMpHoi += io.param;
                                break;
                            case 94: //Giáp #%
                                this.tlDef.add(io.param);
                                break;
                            case 103: //KI +#%
                                this.tlMp.add(io.param);
                                break;
                            case 108: //#% Né đòn
                                this.tlNeDon += io.param;
                                break;
                                 case 206:
                                    this.goteta=true;
                                    break;
                        }
                    }
                    break;
                }
            }
        } else if (this.player.isPl() && this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5) {
            for (Item item : this.player.inventory.itemsBag) {
                if (item.isNotNullItem() && item.template.id == 2117) {
                    for (Item.ItemOption io : item.itemOptions) {
                        switch (io.optionTemplate.id) {
                            case 14: //Chí mạng+#%
                                this.critAdd += io.param;
                                break;
                            case 50: //Sức đánh+#%
                                this.tlDame.add(io.param);
                                break;
                            case 77: //HP+#%
                                this.tlHp.add(io.param);
                                break;
                            case 80: //HP+#%/30s
                                this.tlHpHoi += io.param;
                                break;
                            case 81: //MP+#%/30s
                                this.tlMpHoi += io.param;
                                break;
                            case 94: //Giáp #%
                                this.tlDef.add(io.param);
                                break;
                            case 103: //KI +#%
                                this.tlMp.add(io.param);
                                break;
                            case 108: //#% Né đòn
                                this.tlNeDon += io.param;
                                break;
                                 case 206:
                                    this.goteta=true;
                                    break;
                        }
                    }
                    break;
                }
            }
        } else if (this.player.isPl() && this.player.fusion.typeFusion == ConstPlayer.HOP_THE_GOGETA) {
            for (Item item : this.player.inventory.itemsBag) {
                if (item.isNotNullItem() && item.template.id == 454 ||item.isNotNullItem() && item.template.id == 921||item.isNotNullItem() && item.template.id == 1129||item.isNotNullItem() && item.template.id == 1165) {
                    for (Item.ItemOption io : item.itemOptions) {
                        switch (io.optionTemplate.id) {
                            case 14: //Chí mạng+#%
                                this.critAdd += io.param;
                                break;
                            case 50: //Sức đánh+#%
                                this.tlDame.add(io.param);
                                break;
                            case 77: //HP+#%
                                this.tlHp.add(io.param);
                                break;
                            case 80: //HP+#%/30s
                                this.tlHpHoi += io.param;
                                break;
                            case 81: //MP+#%/30s
                                this.tlMpHoi += io.param;
                                break;
                            case 94: //Giáp #%
                                this.tlDef.add(io.param);
                                break;
                            case 103: //KI +#%
                                this.tlMp.add(io.param);
                                break;
                            case 108: //#% Né đòn
                                this.tlNeDon += io.param;
                                break;
                                 case 206:
                                    this.goteta=true;
                                    break;
                        }
                    }
                    break;
                }
            }
        }
        setDameTrainArmor();
        setBasePoint();
    }

    private void setDameTrainArmor() {
        if (!this.player.isPet && !this.player.isBoss) {
            if (this.player.inventory.itemsBody.size() < 7) {
                return;
            }
            try {
                Item gtl = this.player.inventory.itemsBody.get(6);
                if (gtl.isNotNullItem()) {
                    this.wearingTrainArmor = true;
                    this.wornTrainArmor = true;
                    this.player.inventory.trainArmor = gtl;
                    this.tlSubSD += ItemService.gI().getPercentTrainArmor(gtl);
                } else {
                    if (this.wornTrainArmor) {
                        this.wearingTrainArmor = false;
                        for (Item.ItemOption io : this.player.inventory.trainArmor.itemOptions) {
                            if (io.optionTemplate.id == 9 && io.param > 0) {
                                this.tlDame.add(ItemService.gI().getPercentTrainArmor(this.player.inventory.trainArmor));
                                break;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                Logger.error("Lỗi get giáp tập luyện " + this.player.name + "\n");
            }
        }
    }

    public void setBasePoint() {
        setHpMax();
        setHp();
        setMpMax();
        setMp();
        setDame();
        setDef();
        setCrit();
        setHpHoi();
        setMpHoi();
        setNeDon();
    }

    private void setNeDon() {

    }

    private void setHpHoi() {
        this.hpHoi = this.hpMax / 100;
        this.hpHoi += this.hpHoiAdd;
        this.hpHoi += ((long) this.hpMax * this.tlHpHoi / 100);
        this.hpHoi += ((long) this.hpMax * this.tlHpHoiBanThanVaDongDoi / 100);
    }

    private void setMpHoi() {
        this.mpHoi = this.mpMax / 100;
        this.mpHoi += this.mpHoiAdd;
        this.mpHoi += ((long) this.mpMax * this.tlMpHoi / 100);
        this.mpHoi += ((long) this.mpMax * this.tlMpHoiBanThanVaDongDoi / 100);
    }

    private void setHpMax() {
        this.hpMax = this.hpg;
        this.hpMax += this.hpAdd;
        if (player.timedh1 > 0) {
            this.hpMax += ((long) this.hpMax * 20 / 100);
        }
        if (player.timedh2 > 0) {
            this.hpMax += ((long) this.hpMax * 20 / 100);
        }
        if (player.timedh3 > 0) {
            this.hpMax += ((long) this.hpMax * 20 / 100);
        }
        if (this.player.titlett == true) {
            this.hpMax += ((long) this.hpMax * 5 / 100);
        }
//        if (this.player.titleitem == true) {
//            this.hpMax += ((long) this.hpMax * 10 / 100);
//        }
        //đồ
        for (Integer tl : this.tlHp) {
            this.hpMax += ((long) this.hpMax * tl / 100);
        }
        //set nappa
        if (this.player.setClothes.nappa == 5) {
            this.hpMax += ((long) this.hpMax * 80 / 100);
        }
        //set nappa 2
        if (this.player.setClothes.nappa2 == 5) {
            this.hpMax += ((long) this.hpMax * 100 / 100);
        }
        //set nappa
        if (this.player.setClothes.nappa3 == 5) {
            this.hpMax += ((long) this.hpMax * 130 / 100);
        }
        //set worldcup
        if (this.player.setClothes.worldcup == 2) {
            this.hpMax += ((long) this.hpMax * 10 / 100);
        }
        //ngọc rồng đen 1 sao
        if (this.player.rewardBlackBall.timeOutOfDateReward[0] > System.currentTimeMillis()) {
            this.hpMax += ((long) this.hpMax * NgocRongSaoDenService.R1S_1 / 100);
        }
        //khỉ
        if (this.player.effectSkill.isMonkey) {
            if (!this.player.isPet || (this.player.isPet
                    && ((Pet) this.player).status != Pet.FUSION)) {
                int percent = SkillUtil.getPercentHpMonkey(player.effectSkill.levelMonkey);
                this.hpMax += ((long) this.hpMax * percent / 100);
            }
        }
        //pet mabu
        if (this.player.isPet && ((Pet) this.player).typePet == 1
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA) {
            this.hpMax += ((long) this.hpMax * 20 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 1
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
            this.hpMax += ((long) this.hpMax * 20 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 1
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
            this.hpMax += ((long) this.hpMax * 20 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 1
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
            this.hpMax += ((long) this.hpMax * 20 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 1
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5) {
            this.hpMax += ((long) this.hpMax * 20 / 100);//chi so hp
        }
        //pet beerus
        if (this.player.isPet && ((Pet) this.player).typePet == 2
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA) {
            this.hpMax += ((long) this.hpMax * 25 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 2
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
            this.hpMax += ((long) this.hpMax * 25 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 2
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
            this.hpMax += ((long) this.hpMax * 25 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 2
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
            this.hpMax += ((long) this.hpMax * 25 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 2
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5) {
            this.hpMax += ((long) this.hpMax * 25 / 100);//chi so hp
        }
        //pet kelf
        if (this.player.isPet && ((Pet) this.player).typePet == 3
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA) {
            this.hpMax += ((long) this.hpMax * 45 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 3
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
            this.hpMax += ((long) this.hpMax * 45 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 3
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
            this.hpMax += ((long) this.hpMax * 45 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 3
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
            this.hpMax += ((long) this.hpMax * 45 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 3
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5) {
            this.hpMax += ((long) this.hpMax * 45 / 100);//chi so hp
        }
        // đệ cum
        if (this.player.isPet && ((Pet) this.player).typePet == 4
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA) {
            this.hpMax += ((long) this.hpMax * 30 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 4
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
            this.hpMax += ((long) this.hpMax * 30 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 4
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
            this.hpMax += ((long) this.hpMax * 30 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 4
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
            this.hpMax += ((long) this.hpMax * 30 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 4
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5) {
            this.hpMax += ((long) this.hpMax * 30 / 100);//chi so hp
        }
        // ĐỆ GOKU-VEGETA SSJ4
         if (this.player.isPet && ((Pet) this.player).typePet == 5
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA
                || this.player.isPet && ((Pet) this.player).typePet == 6
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA) {
            this.hpMax += ((long) this.hpMax * 50 / 100);//chi so hp
        }
         if (this.player.isPet && ((Pet) this.player).typePet == 5
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2
                || this.player.isPet && ((Pet) this.player).typePet == 6
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
            this.hpMax += ((long) this.hpMax * 50 / 100);//chi so hp
        }
         if (this.player.isPet && ((Pet) this.player).typePet == 5
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3
                || this.player.isPet && ((Pet) this.player).typePet == 6
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
            this.hpMax += ((long) this.hpMax * 50 / 100);//chi so hp
        }
         if (this.player.isPet && ((Pet) this.player).typePet == 5
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4
                || this.player.isPet && ((Pet) this.player).typePet == 6
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
            this.hpMax += ((long) this.hpMax * 50 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 5
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5
                || this.player.isPet && ((Pet) this.player).typePet == 6
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5) {
            this.hpMax += ((long) this.hpMax * 50 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 5 && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_GOGETA
           || this.player.isPet && ((Pet) this.player).typePet == 6 && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_GOGETA) {
            this.hpMax += ((long) this.hpMax * 70 / 100);//chi so hp
        }
        
        //phù
        if (this.player.zone != null && MapService.gI().isMapBlackBallWar(this.player.zone.map.mapId)) {
            this.hpMax *= this.player.effectSkin.xHPKI;
        }
        //+hp đệ
        if (this.player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            this.hpMax += this.player.pet.nPoint.hpMax;
        }
        //huýt sáo
        if (!this.player.isPet
                || (this.player.isPet
                && ((Pet) this.player).status != Pet.FUSION)) {
            if (this.player.effectSkill.tiLeHPHuytSao != 0) {
                this.hpMax += ((long) this.hpMax * this.player.effectSkill.tiLeHPHuytSao / 100L);

            }
        }
        //bổ huyết
        if (this.player.itemTime != null && this.player.itemTime.isUseBoHuyet) {
            this.hpMax += ((double) this.hpMax * 100 / 100);
        }// item sieu cawsp
        if (this.player.itemTime != null && this.player.itemTime.isUseBoHuyetSC) {
            this.hpMax += ((double) this.hpMax * 120 / 100);
        }
        if (this.player.zone != null && MapService.gI().isMapCold(this.player.zone.map)
                && !this.isKhongLanh) {
            this.hpMax /= 2;
        }
        //mèo mun
        if (this.player.effectFlagBag.useMeoMun) {
            this.hpMax += ((long) this.hpMax * 15 / 100);
        }
        if (this.player.itemTime != null && this.player.itemTime.istrbhp) {
            this.hpMax += (this.mpMax * 30 / 100);
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseBanhChung) {
            this.hpMax *= 1.5;
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseBanhTet) {
            this.hpMax *= 1.5;
        }
            //btc2
        if (this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
            this.hpMax *= 1.15;
        }
        //btc3
        if (this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
            this.hpMax *= 1.2;
        }
        if (this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
            this.hpMax *= 1.25;
        }
    }

    // (hp sư phụ + hp đệ tử ) + 15%
    // (hp sư phụ + 15% +hp đệ tử)
    private void setHp() {
        if (this.hp > this.hpMax) {
            this.hp = this.hpMax;
        }
    }

    private void setMpMax() {
        this.mpMax = this.mpg;
        this.mpMax += this.mpAdd;
        //đồ
        for (Integer tl : this.tlMp) {
            this.mpMax += (this.mpMax * tl / 100);
        }
        if (this.player.setClothes.picolo == 5) {
              this.mpMax += ((long) this.mpMax * 100 / 100);
        }
       if (this.player.setClothes.picolo2 == 5) {
              this.mpMax += ((long) this.mpMax * 120 / 100);
        }
       if (this.player.setClothes.picolo3 == 5) {
              this.mpMax += ((long) this.mpMax * 150 / 100);
        }
        if (player.timedh1 > 0) {
            this.mpMax += ((long) this.mpMax * 20 / 100);
        }
        if (player.timedh2 > 0) {
            this.mpMax += ((long) this.mpMax * 20 / 100);
        }
        if (player.timedh3 > 0) {
            this.mpMax += ((long) this.mpMax * 20 / 100);
        }
        if (this.player.titlett == true) {
            this.mpMax += ((long) this.mpMax * 5 / 100);
        }
//        if (this.player.titleitem == true) {
//            this.mpMax += ((long) this.mpMax * 10 / 100);
//        }
        //ngọc rồng đen 3 sao
        if (this.player.rewardBlackBall.timeOutOfDateReward[2] > System.currentTimeMillis()) {
            this.mpMax += (this.mpMax * NgocRongSaoDenService.R3S_1 / 100);
        }
        //set worldcup
        if (this.player.setClothes.worldcup == 2) {
            this.mpMax += ((long) this.mpMax * 10 / 100);
        }
       //pet mabu
        if (this.player.isPet && ((Pet) this.player).typePet == 1// chi so lam sao bac tu cho dj
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA) {
            this.mpMax += ((long) this.mpMax * 20 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 1// chi so lam sao bac tu cho dj
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
            this.mpMax += ((long) this.mpMax * 20 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 1// chi so lam sao bac tu cho dj
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
            this.mpMax += ((long) this.mpMax * 20 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 1// chi so lam sao bac tu cho dj
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
            this.mpMax += ((long) this.mpMax * 20 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 1// chi so lam sao bac tu cho dj
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5) {
            this.mpMax += ((long) this.mpMax * 20 / 100);//chi so hp
        }
        //pet berus
        if (this.player.isPet && ((Pet) this.player).typePet == 2// chi so lam sao bac tu cho dj
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA) {
            this.mpMax += ((long) this.mpMax * 25 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 2// chi so lam sao bac tu cho dj
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
            this.mpMax += ((long) this.mpMax * 25 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 2// chi so lam sao bac tu cho dj
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
            this.mpMax += ((long) this.mpMax * 25 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 2// chi so lam sao bac tu cho dj
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
            this.mpMax += ((long) this.mpMax * 25 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 2// chi so lam sao bac tu cho dj
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5) {
            this.mpMax += ((long) this.mpMax * 25 / 100);//chi so hp
        }
        //pet kelf
        if (this.player.isPet && ((Pet) this.player).typePet == 3// chi so lam sao bac tu cho dj
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA) {
            this.mpMax += ((long) this.mpMax * 45 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 3// chi so lam sao bac tu cho dj
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
            this.mpMax += ((long) this.mpMax * 45 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 3// chi so lam sao bac tu cho dj
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
            this.mpMax += ((long) this.mpMax * 45 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 3// chi so lam sao bac tu cho dj
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
            this.mpMax += ((long) this.mpMax * 45 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 3// chi so lam sao bac tu cho dj
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5) {
            this.mpMax += ((long) this.mpMax * 45 / 100);//chi so hp
        }
        // đệ cum
        if (this.player.isPet && ((Pet) this.player).typePet == 4
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA) {
            this.mpMax += ((long) this.mpMax * 30 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 4
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
            this.mpMax += ((long) this.mpMax * 30 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 4
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
            this.mpMax += ((long) this.mpMax * 30 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 4
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
            this.mpMax += ((long) this.mpMax * 30 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 4
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5) {
            this.mpMax += ((long) this.mpMax * 30 / 100);//chi so hp
        }
        // ĐỆ GOKU-VEGETA SSJ4
        if (this.player.isPet && ((Pet) this.player).typePet == 5 
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA
                || this.player.isPet && ((Pet) this.player).typePet == 6
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA) {
            this.mpMax += ((long) this.mpMax * 50 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 5 
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2
                || this.player.isPet && ((Pet) this.player).typePet == 6
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
            this.mpMax += ((long) this.mpMax * 50 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 5 
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3
                || this.player.isPet && ((Pet) this.player).typePet == 6
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
            this.mpMax += ((long) this.mpMax * 50 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 5 
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4
                || this.player.isPet && ((Pet) this.player).typePet == 6
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
            this.mpMax += ((long) this.mpMax * 50 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 5 
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5
                || this.player.isPet && ((Pet) this.player).typePet == 6
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5) {
            this.mpMax += ((long) this.mpMax * 50 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 5 && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_GOGETA
           || this.player.isPet && ((Pet) this.player).typePet == 6 && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_GOGETA) {
            this.mpMax += ((long) this.mpMax * 70 / 100);//chi so hp
        }
        //hợp thể
        if (this.player.fusion.typeFusion != 0) {
            this.mpMax += this.player.pet.nPoint.mpMax;
        }
        //bổ khí
        if (this.player.itemTime != null && this.player.itemTime.isUseBoKhi) {
             this.mpMax += ((double) this.mpMax * 100 / 100);
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseBoKhiSC) {
             this.mpMax += ((double) this.mpMax * 120 / 100);
        }
        //phù
        if (this.player.zone != null && MapService.gI().isMapBlackBallWar(this.player.zone.map.mapId)) {
            this.mpMax *= this.player.effectSkin.xHPKI;
        }
        //xiên cá
        if (this.player.effectFlagBag.useXienCa) {
            this.mpMax += ((long) this.mpMax * 15 / 100);
        }
        if (this.player.itemTime != null && this.player.itemTime.istrbki) {
            this.mpMax += (this.mpMax * 30 / 100);
        }
           //BTc2
        if (this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
            this.mpMax *= 1.15;
        }
        if (this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
            this.mpMax *= 1.2;
        }
        if (this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
            this.mpMax *= 1.25;
        }
    }

    private void setMp() {
        if (this.mp > this.mpMax) {
            this.mp = this.mpMax;
        }
    }

    private void setDame() {
        this.dame = this.dameg;
        this.dame += this.dameAdd;
        //đồ
       for (Integer tl : this.tlDame) {
        if (tl != null) { // Kiểm tra null trước khi sử dụng
            this.dame += ((long) this.dame * tl / 100);
        }
    }
    for (Integer tl : this.tlSDDep) {
        if (tl != null) { // Kiểm tra null trước khi sử dụng
            this.dame += ((long) this.dame * tl / 100);
        }
    }
        if (player.timedh1 > 0) {
            this.dame += ((long) this.dame * 20 / 100);
        }
        if (player.timedh2 > 0) {
            this.dame += ((long) this.dame * 20 / 100);
        }
        if (player.timedh3 > 0) {
            this.dame += ((long) this.dame * 20 / 100);
        }
        if (this.player.titlett == true) {
            this.dame += ((long) this.dame * 5 / 100);
        }
//        if (this.player.titleitem == true) {
//            this.dame += ((long) this.dame * 10 / 100);
//        }
        //pet mabu
        if (this.player.isPet && ((Pet) this.player).typePet == 1
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA) {
            this.dame += ((long) this.dame * 20 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 1
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
            this.dame += ((long) this.dame * 20 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 1
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
            this.dame += ((long) this.dame * 20 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 1
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
            this.dame += ((long) this.dame * 20 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 1
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5) {
            this.dame += ((long) this.dame * 20 / 100);//chi so hp
        }
        //pet beerus
        if (this.player.isPet && ((Pet) this.player).typePet == 2
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA) {
            this.dame += ((long) this.dame * 25 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 2
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
            this.dame += ((long) this.dame * 25 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 2
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
            this.dame += ((long) this.dame * 25 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 2
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
            this.dame += ((long) this.dame * 25 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 2
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5) {
            this.dame += ((long) this.dame * 25 / 100);//chi so hp
        }
        //pet kelf
        if (this.player.isPet && ((Pet) this.player).typePet == 3
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA) {
            this.dame += ((long) this.dame * 45 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 3
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
            this.dame += ((long) this.dame * 45 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 3
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
            this.dame += ((long) this.dame * 45 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 3
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
            this.dame += ((long) this.dame * 45 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 3
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5) {
            this.dame += ((long) this.dame * 45 / 100);//chi so hp
        }
        //đệ cum
        if (this.player.isPet && ((Pet) this.player).typePet == 4
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA) {
            this.dame += ((long) this.dame * 30 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 4
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
            this.dame += ((long) this.dame * 30 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 4
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
            this.dame += ((long) this.dame * 30 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 4
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
            this.dame += ((long) this.dame * 30 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 4
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5) {
            this.dame += ((long) this.dame * 30 / 100);//chi so hp
        }
        //ĐỆ GOKU-VEGETA SSJ4
        if (this.player.isPet && ((Pet) this.player).typePet == 5 
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA
                || this.player.isPet && ((Pet) this.player).typePet == 6
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA) {
            this.dame += ((long) this.dame * 50 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 5 
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2
                || this.player.isPet && ((Pet) this.player).typePet == 6
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
            this.dame += ((long) this.dame * 50 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 5 
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3
                || this.player.isPet && ((Pet) this.player).typePet == 6
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
            this.dame += ((long) this.dame * 50 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 5 
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4
                || this.player.isPet && ((Pet) this.player).typePet == 6
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
            this.dame += ((long) this.dame * 50 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 5 
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5
                || this.player.isPet && ((Pet) this.player).typePet == 6
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5) {
            this.dame += ((long) this.dame * 50 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 5 && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_GOGETA
           || this.player.isPet && ((Pet) this.player).typePet == 6 && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_GOGETA) {
            this.dame += ((long) this.dame * 70 / 100);//chi so hp
        }
        //thức ăn
        if (!this.player.isPet && this.player.itemTime.isEatMeal
                || this.player.isPet && ((Pet) this.player).master.itemTime.isEatMeal) {
            this.dame += ((long) this.dame * 10 / 100);
        }
        //hợp thể
        if (this.player.fusion.typeFusion != 0) {
            this.dame += this.player.pet.nPoint.dame;
        }
        //cuồng nộ
        if (this.player.itemTime != null && this.player.itemTime.isUseCuongNo) {
              this.dame += ((double) this.dame * 100 / 100);
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseCuongNoSC) {
            this.dame += ((double) this.dame * 120 / 100);
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseBanhChung) {
            this.dame *= 1.5;
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseBanhTet) {
            this.dame *= 1.5;
        }
        //giảm dame
        this.dame -= ((long) this.dame * tlSubSD / 100);
        //map cold
        if (this.player.zone != null && MapService.gI().isMapCold(this.player.zone.map)
                && !this.isKhongLanh) {
            this.dame /= 2;
        }
        //ngọc rồng đen 1 sao
        if (this.player.rewardBlackBall.timeOutOfDateReward[0] > System.currentTimeMillis()) {
            this.dame += ((long) this.dame * NgocRongSaoDenService.R1S_2 / 100);
        }
        //set worldcup
        if (this.player.setClothes.worldcup == 2) {
            this.dame += ((long) this.dame * 10 / 100);
            this.tlDameCrit.add(20);
        }
        //phóng heo
        if (this.player.effectFlagBag.usePhongHeo) {
            this.dame += ((long) this.dame * 15 / 100);
        }
        //khỉ
        if (this.player.effectSkill.isMonkey) {
            if (!this.player.isPet || (this.player.isPet
                    && ((Pet) this.player).status != Pet.FUSION)) {
                int percent = SkillUtil.getPercentDameMonkey(player.effectSkill.levelMonkey);
                this.dame += ((long) this.dame * percent / 100);
            }
        }
        if (this.player.itemTime != null && this.player.itemTime.istrbsd) {
            this.dame += (this.dame * 30 / 100);
        }
         //btc2
        if (this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
            this.dame *= 1.15;
        }
        //btc3
        if (this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
            this.dame *= 1.2;
        }
        if (this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
            this.dame *= 1.25;
        }
    }

    private void setDef() {
        this.def = this.defg * 4;
        this.def += this.defAdd;
        //đồ
        for (Integer tl : this.tlDef) {
            this.def += (this.def * tl / 100);
        }
        //ngọc rồng đen 2 sao
        if (this.player.rewardBlackBall.timeOutOfDateReward[1] > System.currentTimeMillis()) {
            this.def += ((long) this.def * NgocRongSaoDenService.R2S_2 / 100);
        }
    }

    private void setCrit() {
        this.crit = this.critg;
        this.crit += this.critAdd;
        //ngọc rồng đen 3 sao
        if (this.player.rewardBlackBall.timeOutOfDateReward[2] > System.currentTimeMillis()) {
            this.crit += NgocRongSaoDenService.R3S_2;
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 5 
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_GOGETA
                || this.player.isPet && ((Pet) this.player).typePet == 6 
                && ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_GOGETA) {
            this.crit += (this.crit * 100 / 100);//chi so hp
        }
        //biến khỉ
        if (this.player.effectSkill.isMonkey) {
            this.crit = 110;
        }
    }

    private void resetPoint() {
        this.voHieuChuong = 0;
        this.isDrabura = false; //Cải trang Dracula
        this.isDraburaFrost = false; //Cải trang Dracula Frost
        this.hpAdd = 0;
        this.mpAdd = 0;
        this.dameAdd = 0;
        this.defAdd = 0;
        this.critAdd = 0;
        this.tlHp.clear();
        this.tlMp.clear();
        this.tlDef.clear();
        this.tlDame.clear();
        this.tlDameCrit.clear();
        this.tlDameAttMob.clear();
        this.tlHpHoiBanThanVaDongDoi = 0;
        this.tlMpHoiBanThanVaDongDoi = 0;
        this.hpHoi = 0;
        this.mpHoi = 0;
        this.mpHoiCute = 0;
        this.tlHpHoi = 0;
        this.tlMpHoi = 0;
        this.tlHutHp = 0;
        this.tlHutMp = 0;
        this.tlHutHpMob = 0;
        this.tlHutHpMpXQ = 0;
        this.tlPST = 0;
        this.tlTNSM.clear();
        this.tlDameAttMob.clear();
        this.tlGold = 0;
        this.tlNeDon = 0;
        this.tlSDDep.clear();
        this.tlSubSD = 0;
        this.tlHpGiamODo = 0;
        this.test = 0;
        this.teleport = false;

        this.wearingVoHinh = false;
        this.isKhongLanh = false;
        this.khangTDHS = false;
    }

    public void addHp(long hp) {
        this.hp += hp;
        if (this.hp > this.hpMax) {
            this.hp = this.hpMax;
        }
    }

    public void addMp(long mp) {
        this.mp += mp;
        if (this.mp > this.mpMax) {
            this.mp = this.mpMax;
        }
    }

    public void setHp(long hp) {
        if (hp > this.hpMax) {
            this.hp = this.hpMax;
        } else {
            this.hp = hp;
        }
    }

    public void setMp(long mp) {
        if (mp > this.mpMax) {
            this.mp = this.mpMax;
        } else {
            this.mp = mp;
        }
    }

    private void setIsCrit() {
        if (intrinsic != null && intrinsic.id == 25
                && this.getCurrPercentHP() <= intrinsic.param1) {
            isCrit = true;
        } else if (isCrit100) {
            isCrit100 = false;
            isCrit = true;
        } else {
            isCrit = Util.isTrue(this.crit, ConstRatio.PER100);
        }
    }

    public double getDameAttack(boolean isAttackMob) {
        setIsCrit(); 
        long dameAttack = this.dame; 
        intrinsic = this.player.playerIntrinsic.intrinsic; // này nội tại
        percentDameIntrinsic = 0; // % nội tại
        int percentDameSkill = 0; // này cái skill + them vd như liên hoàn galick..... +#%
        int percentXDame = 0;
        Skill skillSelect = player.playerSkill.skillSelect; // Skill mình dùng đánh PlayerTaget
        switch (skillSelect.template.id) {
            case Skill.DRAGON:
                if (intrinsic.id == 1) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                break;
            case Skill.KAMEJOKO:
                if (intrinsic.id == 2) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                if (this.player.setClothes.songoku == 5) {
                       percentXDame = 100;
                }
                if (this.player.setClothes.songoku2 == 5) {
                      percentXDame = 120;
                }
                if (this.player.setClothes.songoku3 == 5) {
                      percentXDame = 150;
                }
                break;
            case Skill.GALICK:
                if (intrinsic.id == 16) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                if (this.player.setClothes.kakarot == 5) {
                       percentXDame = 100;
                }
                if (this.player.setClothes.kakarot2 == 5) {
                    percentXDame = 120;
                }
                if (this.player.setClothes.kakarot3 == 5) {
                    percentXDame = 150;
                }
                break;
            case Skill.ANTOMIC:
                if (intrinsic.id == 17) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                break;
            case Skill.DEMON:
                if (intrinsic.id == 8) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                break;
            case Skill.MASENKO:
                if (intrinsic.id == 9) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                break;
            case Skill.KAIOKEN:
                if (intrinsic.id == 26) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                if (this.player.setClothes.kirin == 5) {
                     percentXDame = 100;
                }
                if (this.player.setClothes.kirin2 == 5) {
                      percentXDame = 120;
                }
                if (this.player.setClothes.kirin3 == 5) {
                      percentXDame = 150;
                }
                break;
            case Skill.LIEN_HOAN:
                if (intrinsic.id == 13) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                if (this.player.setClothes.ocTieu == 5) {
                       percentXDame = 100;
                }
                if (this.player.setClothes.ocTieu2 == 5) {
                     percentXDame = 120;
                }
                 if (this.player.setClothes.ocTieu3 == 5) {
                       percentXDame = 150;
                }
                break;
            case Skill.DICH_CHUYEN_TUC_THOI:
                dameAttack *= 2;
                dameAttack = Util.nextLong((dameAttack - (dameAttack * 5 / 100)),(dameAttack + (dameAttack * 5 / 100)));
                return dameAttack;
            case Skill.MAKANKOSAPPO:
                percentDameSkill = skillSelect.damage;
                long dameSkill =  (this.mpMax * percentDameSkill / 100);
                return dameSkill;
            case Skill.QUA_CAU_KENH_KHI:
                long damage = this.dame * 40;
                
                damage = damage + (Util.nextInt(-5, 5) * damage / 100);
                return damage;
        }
        // nội tại khỉ  +#%
        if (intrinsic.id == 18 && this.player.effectSkill.isMonkey) {
            percentDameIntrinsic = intrinsic.param1;
        }
        // này là cộng mấy cái nội tại mình có ấy ví dụ galick +20% thì nó là :< dameAttack = dameAttack * 20 / 100  >
        if (percentDameSkill != 0) {
            dameAttack = dameAttack * percentDameSkill / 100;
        }
        dameAttack += (dameAttack * percentDameIntrinsic / 100);
        dameAttack += (dameAttack * dameAfter / 100);
        // Dưới là check đồ + chỉ số các kiểu
        if (isAttackMob) {
            for (Integer tl : this.tlDameAttMob) {
                dameAttack += (dameAttack * tl / 100);
            }
        }
        dameAfter = 0;
        if (this.player.isPet && ((Pet) this.player).master.charms.tdDeTu > System.currentTimeMillis()) {
            dameAttack *= 2;
        }
        if (isCrit) {
            dameAttack *= 2;
            for (Integer tl : this.tlDameCrit) {
                dameAttack += (dameAttack * tl / 100);
            }
        }
          dameAttack += dameAttack * percentXDame / 100;
        dameAttack = Util.GioiHannext(dameAttack - (dameAttack * 5 / 100), dameAttack + (dameAttack * 5 / 100));
        if (player.isPl()) {
            if (player.inventory.haveOption(player.inventory.itemsBody, 5, 159)) {
                if (Util.canDoWithTime(player.lastTimeUseOption, 60000)
                        && (player.playerSkill.skillSelect.skillId == Skill.KAMEJOKO
                                || player.playerSkill.skillSelect.skillId == Skill.ANTOMIC
                                || player.playerSkill.skillSelect.skillId == Skill.MASENKO)) {
                    dameAttack *= player.inventory.getParam(player.inventory.itemsBody.get(5), 159);
                    player.lastTimeUseOption = System.currentTimeMillis();
                }
            }
        }
        return dameAttack;  
    }

    public int getCurrPercentHP() {
        if (this.hpMax == 0) {
            return 100;
        }
        return (int) ((long) this.hp * 100 / this.hpMax);
    }

    public int getCurrPercentMP() {
        return (int) ((long) this.mp * 100 / this.mpMax);
    }

    public void setFullHpMp() {
        this.hp = this.hpMax;
        this.mp = this.mpMax;

    }

    public void subHP(double sub) {
        this.hp -= sub;
        if (this.hp < 0) {
            this.hp = 0;
        }
    }

    public void subMP(int sub) {
        this.mp -= sub;
        if (this.mp < 0) {
            this.mp = 0;
        }
    }

    public long calSucManhTiemNang(long tiemNang) {
        if (power < getPowerLimit()) {
            for (Integer tl : this.tlTNSM) {
                tiemNang += ((long) tiemNang * tl / 100);
            }
            if (this.player.cFlag != 0) {
                if (this.player.cFlag == 8) {
                    tiemNang += ((long) tiemNang * 10 / 100);
                } else {
                    tiemNang += ((long) tiemNang * 5 / 100);
                }
            }
            if (this.player.vip != 0) {
                switch (this.player.vip) {
                    case 1:
                    case 2:
                        tiemNang += ((long) tiemNang * 10 / 100);
                        break;
                    case 3:
                        tiemNang += ((long) tiemNang * 15 / 100);
                        break;
                    case 4:
                        tiemNang += ((long) tiemNang * 20 / 100);
                        break;
                }
            }
            long tn = tiemNang;
            if (this.player.charms.tdTriTue > System.currentTimeMillis()) {
                tiemNang += tn;
            }
            if (this.player.charms.tdTriTue3 > System.currentTimeMillis()) {
                tiemNang += tn * 2;
            }
            if (this.player.charms.tdTriTue4 > System.currentTimeMillis()) {
                tiemNang += tn * 3;
            }
            if (this.intrinsic != null && this.intrinsic.id == 24) {
                tiemNang += ((long) tiemNang * this.intrinsic.param1 / 100);
            }
            if (this.player.setClothes.tiemnang == 5) {
                tiemNang += ((long) tiemNang * 100 / 100);
            }
          
            if (this.player.clan != null && this.player.clan.level >= 5 && this.player.clan.level <= 7) {
                tiemNang += ((long) tiemNang * 10 / 100);
            if (this.player.clan != null && this.player.clan.level >= 8) {
                tiemNang += ((long) tiemNang * 15 / 100);
            if (this.player.clan != null && this.player.clan.level >= 10) {
                tiemNang += ((long) tiemNang * 20 / 100);
            }}}
            if (this.player.isPet) {
                if (((Pet) this.player).master.charms.tdDeTu > System.currentTimeMillis()) {
                    tiemNang += tn * 2;
                }
            }
            if ((this.player.zone.map.mapId >= 122 && this.player.zone.map.mapId <= 124 && this.power < 100000000000L)
            || (this.player.zone.map.mapId >= 204 && this.player.zone.map.mapId <= 206 && this.power < 100000000000L)) {
            tiemNang += tn * 3;
            }
            tiemNang *= Manager.RATE_EXP_SERVER;
            tiemNang = calSubTNSM(tiemNang);
            if (tiemNang <= 0) {
                tiemNang = 1;
            }
            if (tiemNang >= 2_000_000_000) {
                tiemNang = 2_000_000_000;
            }
        } else {
            tiemNang = 10;
        }
        return tiemNang;

    }

    public long calSubTNSM(long tiemNang) {
if (this.power >= 1_000_000_000_000L) {
            tiemNang /= 120;
        } else if (this.power >= 900_000_000_000L) {
            tiemNang /= 110;
        } else if (this.power >= 800_000_000_000L) {
            tiemNang /=100;
        } else if (this.power >= 700_000_000_000L) {
            tiemNang /= 90;
        } else if (this.power >= 600_000_000_000L) {
            tiemNang /= 80;
        } else if (this.power >= 500_000_000_000L) {
            tiemNang /= 70;
        } else if (this.power >= 400_000_000_000L) {
            tiemNang /= 60;
        } else if (this.power >= 300_000_000_000L) {
            tiemNang /= 50;
        } else if (this.power >= 250_000_000_000L) {
            tiemNang /= 40;
        } else if (this.power >= 200_000_000_000L) {
            tiemNang /= 30;
        } else if (this.power >= 160_000_000_000L) {
            tiemNang /= 20;
        } else if (this.power >= 120_000_000_000L) {
            tiemNang /= 10;
        } else if (this.power >= 80_000_000_000L) {
            tiemNang /= 5;
        } else if (this.power >= 60000000000L) {
            tiemNang *=1;
        } else if (this.power >= 40000000000L) {
            tiemNang *= 5;
        }
        if (player.zone.map.mapId >= 156 && player.zone.map.mapId <= 159
           || player.zone.map.mapId >= 160 && player.zone.map.mapId <= 163) {
            tiemNang = tiemNang;
        }
        return tiemNang;
    }

    public short getTileHutHp(boolean isMob) {
        if (isMob) {
            return (short) (this.tlHutHp + this.tlHutHpMob);
        } else {
            return this.tlHutHp;
        }
    }

    public short getTiLeHutMp() {
        return this.tlHutMp;
    }

    public double subDameInjureWithDeff(double dame) {
        int def = this.def;
        dame -= def;
        if (this.player.itemTime.isUseGiapXen) {
            dame /= 2;
        }
        if (dame < 0) {
            dame = 1;
        }
        return dame;
    }

    /*------------------------------------------------------------------------*/
    public boolean canOpenPower() {
        return this.power >= getPowerLimit();
    }

    public long getPowerLimit() {
        switch (limitPower) {
            case 0:
                return 17999999999L;
            case 1:
                return 20000000000L;
            case 2:
                return 25000000000L;
            case 3:
                return 30000000000L;
            case 4:
                return 40000000000L;
            case 5:
                return 50000000000L;
            case 6:
                return 70000000000L;
            case 7:
                return 80000000000L;
            case 8:
                return 120999999999L;
            case 9:
                return Long.MAX_VALUE;
            default:
                return 0;
        }
    }

    public long getPowerNextLimit() {
        switch (limitPower + 1) {
            case 0:
                return 17999999999L;
            case 1:
                return 19999999999L;
            case 2:
                return 24999999999L;
            case 3:
                return 29999999999L;
            case 4:
                return 40000000000L;
            case 5:
                return 50000000000L;
            case 6:
                return 70000000000L;
            case 7:
                return 80000000000L;
           case 8:
                return 120999999999L;
            case 9:
                return Long.MAX_VALUE;
            default:
                return 0;
        }
    }

    public int getHpMpLimit() {
        if (limitPower == 0) {
            return 180000;
        }
        if (limitPower == 1) {
            return 240000;
        }
        if (limitPower == 2) {
            return 280000;
        }
        if (limitPower == 3) {
            return 320000;
        }
        if (limitPower == 4) {
            return 360000;
        }
        if (limitPower == 5) {
            return 400000;
        }
        if (limitPower == 6) {
            return 450000;
        }
        if (limitPower == 7) {
            return 500000;
        }
        if (limitPower == 8) {
            return 550000;
        }
         if (limitPower == 9) {
            return 600000;
        }
        return 0;
    }

    public int getDameLimit() {
        if (limitPower == 0) {
            return 11000;
        }
        if (limitPower == 1) {
            return 12500;
        }
        if (limitPower == 2) {
            return 15500;
        }
        if (limitPower == 3) {
            return 18000;
        }
        if (limitPower == 4) {
            return 20500;
        }
        if (limitPower == 5) {
            return 25000;
        }
        if (limitPower == 6) {
            return 27000;
        }
        if (limitPower == 7) {
            return 29000;
        }
        if (limitPower == 8) {
            return 30000;
        }
        if (limitPower == 9) {
            return 32000;
        }
        return 0;
    }

    public short getDefLimit() {
        if (limitPower == 0) {
            return 550;
        }
        if (limitPower == 1) {
            return 600;
        }
        if (limitPower == 2) {
            return 700;
        }
        if (limitPower == 3) {
            return 800;
        }
        if (limitPower == 4) {
            return 1000;
        }
        if (limitPower == 5) {
            return 1300;
        }
        if (limitPower == 6) {
            return 1500;
        }
        if (limitPower == 7) {
            return 1600;
        }
        if (limitPower == 8) {
            return 1700;
        }
         if (limitPower == 9) {
            return 1800;
        }
        return 0;
    }

    public byte getCritLimit() {
        if (limitPower == 0) {
            return 1;
        }
        if (limitPower == 1) {
            return 2;
        }
        if (limitPower == 2) {
            return 3;
        }
        if (limitPower == 3) {
            return 4;
        }
        if (limitPower == 4) {
            return 5;
        }
        if (limitPower == 5) {
            return 6;
        }
        if (limitPower == 6) {
            return 7;
        }
        if (limitPower == 7) {
            return 8;
        }
        if (limitPower == 8) {
            return 9;
        }
        if (limitPower == 9) {
            return 10;
        }
        return 0;
    }

    //**************************************************************************
    //POWER - TIEM NANG
    public void powerUp(long power) {
//        if (power > 80000000000L) {
//            tiemNang /= 399;
//        } else if (power > 110000000000L) {
//            tiemNang /= 299;
//        } else if (power > 130000000000L) {
//            tiemNang /= 199;
//        } else if (power > 150000000000L) {
//            tiemNang /= 99;
//        }
        this.power += power;
        TaskService.gI().checkDoneTaskPower(player, this.power);
    }

    public void tiemNangUp(long tiemNang) {
        this.tiemNang += tiemNang;
    }

    public void increasePoint(byte type, short point) {
        if (point <= 0 || point > 1000) {
            return;
        }
        long tiemNangUse = 0;
        if (type == 0) {
            int pointHp = point * 20;
            tiemNangUse = point * (3 * (this.hpg + 1000) + pointHp - 20) / 2;
            if ((this.hpg + pointHp) <= getHpMpLimit()) {
                if (doUseTiemNang(tiemNangUse)) {
                    hpg += pointHp;
                }
            } else {
                Service.gI().sendThongBao(player, "Vui lòng mở giới hạn sức mạnh");
                return;
            }
        }
        if (type == 1) {
            int pointMp = point * 20;
            tiemNangUse = point * (3 * (this.mpg + 1000) + pointMp - 20) / 2;
            if ((this.mpg + pointMp) <= getHpMpLimit()) {
                if (doUseTiemNang(tiemNangUse)) {
                    mpg += pointMp;
                }
            } else {
                Service.gI().sendThongBao(player, "Vui lòng mở giới hạn sức mạnh");
                return;
            }
        }
        if (type == 2) {
            tiemNangUse = point * (2 * this.dameg + point - 1) / 2 * 150;
            if ((this.dameg + point) <= getDameLimit()) {
                if (doUseTiemNang(tiemNangUse)) {
                    dameg += point;
                }
            } else {
                Service.gI().sendThongBao(player, "Vui lòng mở giới hạn sức mạnh");
                return;
            }
        }
        if (type == 3) {
            tiemNangUse = 2 * (this.defg + 5) / 2 * 100000;
            if ((this.defg + point) <= getDefLimit()) {
                if (doUseTiemNang(tiemNangUse)) {
                    defg += point;
                }
            } else {
                Service.gI().sendThongBao(player, "Vui lòng mở giới hạn sức mạnh");
                return;
            }
        }
        if (type == 4) {
            tiemNangUse = 50000000L;
            for (int i = 0; i < this.critg; i++) {
                tiemNangUse *= 4L;
            }
            if ((this.critg + point) <= getCritLimit()) {
                if (doUseTiemNang(tiemNangUse)) {
                    critg += point;
                }
            } else {
                Service.gI().sendThongBao(player, "Vui lòng mở giới hạn sức mạnh");
                return;
            }
        }
        if (type == 5) {
            if (player.nPoint.limitPower > NPoint.MAX_LIMIT) {
                player.nPoint.limitPower = NPoint.MAX_LIMIT;
            }
            Service.getInstance().sendThongBaoOK(player, "Vui lòng mở giới hạn sức mạnh");
            return;
        }
        Service.gI().point(player);
    }
    public void increasePointPet(byte type, short point) {
        if (point <= 0 || point > 100) {
            return;
        }
        long tiemNangUse = 0;
        if (type == 0) {
            int pointHp = point * 20;
            tiemNangUse = point * (2 * (this.hpg + 1000) + pointHp - 20) / 2;
            if ((this.hpg + pointHp) <= getHpMpLimit()) {
                if (doUseTiemNang(tiemNangUse)) {
                    hpg += pointHp;
                }
            } else {
                Service.getInstance().sendThongBaoOK(player, "Vui lòng mở giới hạn sức mạnh");
                return;
            }
        }
        if (type == 1) {
            int pointMp = point * 20;
            tiemNangUse = point * (2 * (this.mpg + 1000) + pointMp - 20) / 2;
            if ((this.mpg + pointMp) <= getHpMpLimit()) {
                if (doUseTiemNang(tiemNangUse)) {
                    mpg += pointMp;
                }
            } else {
                Service.getInstance().sendThongBaoOK(player, "Vui lòng mở giới hạn sức mạnh");
                return;
            }
        }
        if (type == 2) {
            tiemNangUse = point * (2 * this.dameg + point - 1) / 2 * 100;
            if ((this.dameg + point) <= getDameLimit()) {
                if (doUseTiemNang(tiemNangUse)) {
                    dameg += point;
                }
            } else {
                Service.getInstance().sendThongBaoOK(player, "Vui lòng mở giới hạn sức mạnh");
                return;
            }
        }
        if (type == 3) {
            tiemNangUse = 2 * (this.defg + 5) / 2 * 100000;
            if ((this.defg + point) <= getDefLimit()) {
                if (doUseTiemNang(tiemNangUse)) {
                    defg += point;
                }
            } else {
                Service.getInstance().sendThongBaoOK(player, "Vui lòng mở giới hạn sức mạnh");
                return;
            }
        }
        if (type == 4) {
            tiemNangUse = 50000000L;
            for (int i = 0; i < this.critg; i++) {
                tiemNangUse *= 5L;
            }
            if ((this.critg + point) <= getCritLimit()) {
                if (doUseTiemNang(tiemNangUse)) {
                    critg += point;
                }
            } else {
                Service.getInstance().sendThongBaoOK(player, "Vui lòng mở giới hạn sức mạnh");
                return;
            }
        }
        if (type == 5) {

            if (player.nPoint.limitPower > NPoint.MAX_LIMIT) {
                player.nPoint.limitPower = NPoint.MAX_LIMIT;
            }
            Service.getInstance().sendThongBaoOK(player, "Vui lòng mở giới hạn sức mạnh");
            return;
        }
        Service.getInstance().showInfoPet(player);
    }
        
    private boolean doUseTiemNang(long tiemNang) {
        if (this.tiemNang < tiemNang) {
            Service.gI().sendThongBao(player, "Bạn không đủ tiềm năng");
            return false;
        }
        if (this.tiemNang >= tiemNang && this.tiemNang - tiemNang >= 0) {
            this.tiemNang -= tiemNang;
            TaskService.gI().checkDoneTaskUseTiemNang(player);
            return true;
        }
        return false;
    }

    //--------------------------------------------------------------------------
    private long lastTimeHoiPhuc;
    private long lastTimeHoiStamina;

    public void update() {
        if (player != null && player.effectSkill != null) {
            if (player.effectSkill.isCharging && player.effectSkill.countCharging < 10) {
                long tiLeHoiPhuc = SkillUtil.getPercentCharge(player.playerSkill.skillSelect.point);
                if (player.effectSkill.isCharging && !player.isDie() && !player.effectSkill.isHaveEffectSkill()
                        && (hp < hpMax || mp < mpMax)) {
                    PlayerService.gI().hoiPhuc(player, hpMax / 100 * tiLeHoiPhuc,
                            mpMax / 100 * tiLeHoiPhuc);
                    if (player.effectSkill.countCharging % 3 == 0) {
                        Service.gI().chat(player, "Phục hồi năng lượng " + getCurrPercentHP() + "%");
                    }
                } else {
                    EffectSkillService.gI().stopCharge(player);
                }
                if (++player.effectSkill.countCharging >= 10) {
                    EffectSkillService.gI().stopCharge(player);
                }
            }
            if (Util.canDoWithTime(lastTimeHoiPhuc, 30000)) {
                PlayerService.gI().hoiPhuc(this.player, hpHoi, mpHoi);
                this.lastTimeHoiPhuc = System.currentTimeMillis();
            }
            if (Util.canDoWithTime(lastTimeHoiStamina, 60000) && this.stamina < this.maxStamina) {
                this.stamina++;
                this.lastTimeHoiStamina = System.currentTimeMillis();
                if (!this.player.isBoss && !this.player.isPet) {
                    PlayerService.gI().sendCurrentStamina(this.player);
                }
            }
        }
        //hồi phục 30s
        //hồi phục thể lực
    }

    public void dispose() {
        this.intrinsic = null;
        this.player = null;
        this.tlHp = null;
        this.tlMp = null;
        this.tlDef = null;
        this.tlDame = null;
        this.tlDameAttMob = null;
        this.tlSDDep = null;
        this.tlTNSM = null;
    }
     public double getHP() {
        return this.hp <= this.hpMax ? this.hp : this.hpMax;
    }

    public double getMP() {
        return this.mp <= this.mpMax ? this.mp : this.mpMax;
    }
}
