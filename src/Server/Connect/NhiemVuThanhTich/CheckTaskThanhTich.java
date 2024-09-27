/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.Connect.NhiemVuThanhTich;

import ServerData.Models.Player.Player;

/**
 *
 * @author Administrator
 */
public class CheckTaskThanhTich 
{
    public static boolean CheckSKH(Player pl,int type)
    {
        boolean flag1= pl.setClothes.nappa == 20 || pl.setClothes.kakarot == 20 ||pl.setClothes.cadic == 20;
        boolean flag2= pl.setClothes.kirin == 20 || pl.setClothes.songoku == 20 ||pl.setClothes.thienXinHang == 20;
        boolean flag3= pl.setClothes.picolo == 20 || pl.setClothes.ocTieu == 20 ||pl.setClothes.pikkoroDaimao == 20;
          boolean flag4= pl.setClothes.nappa == 30 || pl.setClothes.kakarot == 30 ||pl.setClothes.cadic == 30;
        boolean flag5= pl.setClothes.kirin == 30 || pl.setClothes.songoku == 30 ||pl.setClothes.thienXinHang == 30;
        boolean flag6= pl.setClothes.picolo == 30 || pl.setClothes.ocTieu == 30 ||pl.setClothes.pikkoroDaimao == 30;
          boolean flag7= pl.setClothes.nappa == 40 || pl.setClothes.kakarot == 40 ||pl.setClothes.cadic == 40;
        boolean flag8= pl.setClothes.kirin == 40 || pl.setClothes.songoku == 40 ||pl.setClothes.thienXinHang == 40;
        boolean flag9= pl.setClothes.picolo == 40 || pl.setClothes.ocTieu == 40 ||pl.setClothes.pikkoroDaimao == 40;
        if(type == 1)
        {
            return flag1 || flag2 || flag3;
        }
        if(type == 2)
        {
            return (flag4 || flag5 || flag6) ;
        }
        if(type == 3)
        {
            return (flag7 || flag8 || flag9);
        }
        return false;
    }
    public static boolean CheckKillBoss(Player pl,int type)
    {
        int param = Integer.MAX_VALUE;
        switch(type)
        {
            case 1 :
                param = 50;
                break;
            case 2 :
                param = 500;
                break;
            case 3 :
                param = 5000;
                break;
        }
        return pl.pointSb >= param;
    }
    public static String SCheckKillBoss(Player pl,int type)
    {
        int param = 0;
        switch(type)
        {
            case 1 :
                param = 50;
                break;
            case 2 :
                param = 500;
                break;
            case 3 :
                param = 5000;
                break;
        }
        return " ("+pl.pointSb + "/" + param + ")";
    }
    public static boolean CheckTask(Player pl,int type)
    {
        int param = Integer.MAX_VALUE;
        switch(type)
        {
            case 1 :
                param = 21 ;
                break;
            case 2 :
                param = 29;
                break;
        }
        return pl.playerTask.taskMain.id >= param;
    }
    public static String SCheckTask(Player pl,int type)
    {
        int param = 0;
        switch(type)
        {
            case 1 :
                param = 21 ;
                break;
            case 2 :
                param = 29;
                break;
        }
        return " ("+pl.playerTask.taskMain.id + "/" + param + ")";
    }
    public static boolean CheckTongNap(Player pl,int type)
    {
        int param = Integer.MAX_VALUE;
        switch(type)
        {
            case 1 :
                param = 50000;
                break;
            case 2 :
                param = 100000;
                break;
            case 3 :
                param = 500000;
                break;
        }
        return pl.getSession().tongnap >= param;
    }
    public static String SCheckTongNap(Player pl,int type)
    {
        int param = 0;
        switch(type)
        {
            case 1 :
                param = 50000;
                break;
            case 2 :
                param = 100000;
                break;
            case 3 :
                param = 500000;
                break;
        }
        return " ("+pl.getSession().tongnap + "/" + param + ")";
    }
    public static boolean CheckSMTN(Player pl,int type)
    {
        long param = Long.MAX_VALUE;
        switch(type)
        {
            case 1 :
                param = 200000000000l;
                break;
            case 2 :
                param = 500000000000l;
                break;
            case 3 :
                param = 1000000000000l;
                break;
        }
        return pl.nPoint.power >= param && pl.pet.nPoint.power >= param;
    }
}
