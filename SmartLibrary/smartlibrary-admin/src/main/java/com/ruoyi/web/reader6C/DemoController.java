package com.ruoyi.web.reader6C;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.web.reader6C.sdk.READERDLLAPI;
import com.ruoyi.web.reader6C.sdk.Utility;
import com.sun.jna.Native;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import com.ruoyi.web.reader6C.sdk.READERDLLAPI.READERDLL.HandsetParam;

/**
 * @author: Mei Pq
 * @description:
 * @date: create in 16:26 2022/08/02
 */
@RestController
@RequestMapping("demo")
public class DemoController extends BaseController {

    /** DLL库API */
    READERDLLAPI.READERDLL ReaderAPI = null;
    /**日志文件名 */
    String logFileName;
    /** 连接需要的参数 */
    int[] hScanner = new int[1];
    /** 判断连接状态 */
    boolean NowConState = false;
    /** 读发卡机参数传参需要 */
    HandsetParam[] gHandParam = new HandsetParam[1];
    /** 读auto参数 */
    READERDLLAPI.READERDLL.ReaderAutoParam gReaderAutoParam[] = new READERDLLAPI.READERDLL.ReaderAutoParam[1];
    /** 读基本参数 */
    READERDLLAPI.READERDLL.ReaderBasicParam gBasicParam[] = new READERDLLAPI.READERDLL.ReaderBasicParam[1];
    /** 自模下仿真参数 */
    READERDLLAPI.READERDLL.SimParam Param[] = new READERDLLAPI.READERDLL.SimParam[1];
    /** 读ID */
    public byte[] gReaderID = new byte[33];

    //读判断标志
    int ifConnectReader = 0;
    int NewConnect = 0;

    /** 表示读6C的标签 */
    boolean read6C = true;
    /** 读取进程 */
    private MyTimer mytimer = new MyTimer();
    /** 掩措 */
    byte[] mask = new byte[512];

    int res = -1;

    byte[][] EPCC1G2_IDBuffer = new byte[100][96];

    boolean nStopWrite = true;

    /** 写连接判断标志 */
    boolean nStop = true;

    //开启线程
    Thread tdThreadEPC = null;

    //0---不保护(不锁), 1---使用密码锁，2---永远锁住(死了)
    int iFlagProtectOpt;

    //要写入的数据
    String writeData = null;
    boolean bWriteSuc = false;




    /**
     * 调用加载动态库、连接
     * @return
     */
    @GetMapping("/uploadDll")
    public AjaxResult uploadDll(){
        this.formWindowOpened();
        boolean bRet = this.connectButtonFunc();
//        this.readerActionPerformed();   //持续读取
//        this.start6CWrite();  //写入
        if(bRet){
            return AjaxResult.success("RFID阅读器 连接成功");
        }else{
            return AjaxResult.error("RFID阅读器 连接失败");
        }
    }

    /**
     * 写入
     * @return
     */
    @GetMapping("/write/{bkIsbn}")
    public AjaxResult uploadDll(@PathVariable String bkIsbn){
        this.bWriteSuc = false;
        this.writeData = bkIsbn;
        System.out.println("write start--------" + writeData);
        this.start6CWrite();
        while (!bWriteSuc){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("write end--------" + writeData);
        return AjaxResult.success(writeData);
    }


    /**
     * 断开连接
     * @return
     */
    @GetMapping("/stop")
    public void stopConnect(){
        System.out.println("------准备断开----");
        this.jButtonDisConStopCon();
    }








    /** 写卡 */
    private void start6CWrite() {
        // TODO Auto-generated method stub
        nStopWrite = true;
//        PassWordWhich =false;
        if(NowConState){
            iFlagProtectOpt	= 0;
            tdThreadEPC = new ThreadEPC();
            tdThreadEPC.start();

//            iFlagProtectOpt	=	0;//0---不保护(不锁), 1---使用密码锁，2---永远锁住(死了), 3---EAS报警(add by martrin20140514)
//            //可以正常写入
//            if(jCheckBoxNeverBeWritten.isSelected()&&jCheckBoxPasswordW.isSelected()){
//                JOptionPane.showMessageDialog(null, "操作失败：请确认是否需要密码写入！");
//                jCheckBoxNeverBeWritten.setSelected(false);
//                jCheckBoxPasswordW.setSelected(false);
//                return;
//            }
//            if(jCheckBoxPasswordW.isSelected()||jCheckBoxEASAlarm.isSelected()){//使用密码写入或者EAS alarm写入
//                String m_RWAccessPassword = jTextFieldEPCPassword.getText();
//                int str_len	=	m_RWAccessPassword.length();
//                if(str_len!=8){
//                    JOptionPane.showMessageDialog(null, "操作失败：要写入的密码长度不为8！");
//                }
//                ShowStr	=	"您确定要使用密码写来锁定EPC吗？";
//                iFlagProtectOpt	=	1;//1---使用密码锁
//            }
//            if(jCheckBoxNeverBeWritten.isSelected()){
//                iFlagProtectOpt	=	2;//2---永远锁住(死了)
//                ShowStr	=	"您确定要永远不可写来锁定EPC吗？";
//            }
//            if(jCheckBoxEASAlarm.isSelected()){
//                iFlagProtectOpt	=	3;//3---EAS报警(add by martrin20140514)
//                ShowStr	=	"您确定要EAS报警吗？";
//            }

//            int k = 0;
//            if ( 0 == iFlagProtectOpt)
//            {
//            }else{
//                int result = JOptionPane.showConfirmDialog(null, ShowStr,"请谨慎选择",JOptionPane.YES_NO_OPTION);
//                if(result == 1){
//                    PassWordWhich = true;
//                    jCheckBoxEASAlarm.setSelected(false);
//                    if(jCheckBoxNeverBeWritten.isSelected())jCheckBoxNeverBeWritten.setSelected(false);
//                    if(jCheckBoxPasswordW.isSelected())jCheckBoxPasswordW.setSelected(false);
//                    return;
//                }
//            }

//            int iCount = dTM.getRowCount();
//            if(iCount==0)
//            {
//                JOptionPane.showMessageDialog(null, "待写入数据列表中没有数据!");
//                return;
//            }
//
//            String tmpd = (String) dTM.getValueAt(0, 1);
//            jLabelWriteInfor2.setText(tmpd);
//            if(jCheckBox5.isSelected()){
//                jCheckBoxPasswordW.setEnabled(false);
//                jTextFieldEPCPassword.setEnabled(false);
//                jCheckBoxNeverBeWritten.setEnabled(false);
//                nStop = true;
//                jButton7.setEnabled(false);
//                jButton8.setEnabled(true);
//
//            }else{
//
//            }
//
//        }else{
//            if(!NowConState){
//                JOptionPane.showMessageDialog(null, "尚未连接!");
//            }else if(!ListOK){
//                JOptionPane.showMessageDialog(null, "没有可写入数据！");
//            }else{
//                JOptionPane.showMessageDialog(null, "未知错误！");
//            }
//            jButton7.setEnabled(true);
        }
    }

    //写入线程
    class ThreadEPC extends Thread{
        int t=0;
        int i, j, k, ID_len = 0;// ID_len_temp = 0;
        int[] nCounter = new int[2];
        String str,str_temp;
        byte[] temp = new byte[512];
        byte[] IDBuffer = new byte[30*256];

        byte[] mask = new byte[512];
        byte[] IDTemp = new byte[512];
        int mem,ptr,len,EPC_Word;
        byte[] AccessPassword = new byte[32];

        byte[] p = new byte[3];

        int ilock	=	0;
        //此处应该写日志；

        @Override
        public void run() {
            System.out.println();
            System.out.println("##########进入读写线程啦###########");
            System.out.println("nStop的状态是： "+ nStop);
//            String toBeWrite = (String) dTM.getValueAt(0, 1);//要写入的数据，要与读出的数据比较，一致就不能写；
//            int iCounter = dTM.getRowCount();
            int iCounter = 1;
//            String toBeWrite = writeData;
            String toBeWrite = "000" + writeData;//要写入的数据，要与读出的数据比较，一致就不能写；（长度一定要是4的倍数，不然会出错！！！）

            int ilock=0;
            byte[] AccessPassword = new byte[32];
            int[] IntAccessPassword = new int[32];
            System.out.println("iCounter的值是： "+iCounter);
            while(nStop&&nStopWrite){
                iCounter = 1;
                if(iCounter==0)nStopWrite=false;//可写列表长度为0
                if(!NowConState)nStopWrite=false;//没有连接
                System.out.println("nStop的状态是： "+ nStop);
                boolean a = true;
                if(a){
                    byte[] ID_tmp = new byte[512];
                    len=0;	//掩码长度LEN
                    ptr=0;	//掩码起始地址addr
                    mem=1;	//0--密码区,1-- EPC号,2-- TID标签ID号,3--用户区User
                    ID_len=0;
                    j=0;
                    boolean overTime = true;//是否超时没有相应
                    byte[] MyMask = new byte[512];
                    int times = 200;
                    while(nStop&&nStopWrite){
                        System.out.println();
                        System.out.println("###########在读循环中############");
                        if(iCounter==0)nStop=false;//可写列表长度为0
                        if(!NowConState)nStop=false;//没有连接
                        if(nStop){
//                            toBeWrite = (String) dTM.getValueAt(0, 1);
                            System.out.println("要写入的数值是： "+toBeWrite);
//                            jLabelWriteInfor2.setText(toBeWrite);
                        }
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //先读出数据，看是否可以与要写入的一致；
                        byte[] IDBuffer = new byte[30*256];

                        //开始读取数据；
                        res = ReaderAPI.EPC1G2_ReadLabelID(hScanner[0],1,0,0,mask,IDBuffer,AccessPassword,0);

                        ID_len = 0;
                        str = ReadByteToString(IDBuffer);
                        System.out.println("读出来的数值000是： "+str);//读出来的数据是str;
                        if(res == 0&&(str.length()>=4)){//读成功；
                            String hasWrite =null;
//                            boolean hasWriteH = false;
//                            if(jTableComp.getRowCount()!=0){
////                                if(str.equalsIgnoreCase((String) jTableComp.getValueAt(jTableComp.getRowCount()-1, 1))){
////                                    hasWriteH=true;
////                                }
//                                hasWriteH=true;
//                            }else{
//                                hasWriteH=false;
//                            }
//                            System.out.println("hasWriteH   ： "+hasWriteH);
//                            if(str.equalsIgnoreCase(toBeWrite)||hasWriteH){
                            if(str.equalsIgnoreCase(toBeWrite)){
                                //读出的数据与要比较的数据比较；一致的时候设置为红色；背景
//                                jLabelWriteInfor4.setBackground(Color.RED);
//                                jLabelWriteInfor6.setText("Reptitive writing,please next tag");
                                System.out.println("------写入成功-------");
                                writeData = toBeWrite;
                                System.out.println(toBeWrite);
                                bWriteSuc = true;
                                tdThreadEPC.stop();
                            }else{
                                //不一致表示可以写入；此时跳出读循环
                                nStop = false;
                            }
                        }else{
//                            jLabelWriteInfor4.setBackground(Color.BLUE);
//                            jLabelWriteInfor6.setText("Waiting for writing tag or tag");
                            System.out.println("----------请放入标签----------");
                        }

                    }
                    nStop = true;//此时进入写循环
                    while(nStop&&nStopWrite&&overTime){
                        try {
                            Thread.sleep(30);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        System.out.println();
                        System.out.println("###########在写循环中############");
                        if(iCounter==0)nStop=false;//可写列表长度为0
                        if(!NowConState)nStop=false;//没有连接
                        String str = toBeWrite;
                        byte[] mask = new byte[512];
                        int len = str.length();
                        mask = WriteStringToByte(str);
                        for(i = 0;i<len/2;i++){
                            MyMask[i] = mask[i];
                        }
                        int res = ReaderAPI.EPC1G2_WriteEPC(hScanner[0], (len+3)/4, mask, IntAccessPassword, 0);
                        ID_tmp = mask;
                        if(res ==0){//写入成功；
                            nStop = false;//让退出写循环
                        }
                        times--;
                        if(times<0){
                            System.out.println("------读卡超时！编号001-------");
                            overTime = false;
                        }
                    }

                    nStop = true;//接下来再读，看写入是否成功；
                    while(nStop&&nStopWrite&&overTime){
                        try {
                            Thread.sleep(30);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        //开始读取数据；
                        res = ReaderAPI.EPC1G2_ReadLabelID(hScanner[0],1,0,0,mask,IDBuffer,AccessPassword,0);
                        //int ID_len_temp = 0 ;
                        ID_len = 0;

                        String str = "";
                        str = ReadByteToString(IDBuffer);
                        System.out.println("要写入的数值是： " + str);//读出来的数据是str;
                        if(res == 0){//读成功；
                            if(toBeWrite.equalsIgnoreCase(str)){
                                nStop = false;
                                ID_len = str.length()/4;

                            }else{

                            }
                        }
                        times--;
                        if(times<0){
                            System.out.println("---------读卡超时！编号002--------");
                            overTime = false;
                        }
                    }
                    nStop = true;//设置接续进行总循环，进行下面的操作或者读下一张；
//看是否有密码些人操作，或者是报警写入；
//                    if(iFlagProtectOpt==1||iFlagProtectOpt==2){
//
//                        System.out.println("000000000000000000000000000");
//                        //1----使用密码锁
//                        len=4;	//读取数据的长度
//                        ptr=0;	//
//                        mem=0;	//0--密码区,1-- EPC号,2-- TID标签ID号,3--用户区User
//                        EPC_Word	=	ID_len;//为EPC的长度1
//                        //byte[] mask = new byte[512];
//                        byte[] mask = new byte[512];
//                        while(nStop&&nStopWrite&&overTime){//先读出密码
//                            System.out.println("1111111111111111111111111111");
//                            try {
//                                Thread.sleep(30);
//                            } catch (InterruptedException e) {
//                                // TODO Auto-generated catch block
//                                e.printStackTrace();
//                            }
//                            //byte[] AccessPassword = new byte[32];
//                            res = ReaderAPI.EPC1G2_ReadWordBlock(hScanner[0],toBeWrite.length()/4, WriteStringToByte(toBeWrite), mem, ptr, len, mask, WriteStringToByte("00000000"), 0);
//                            if(res == 0){
//                                //读取成功；
//                                nStop = false;
//                            }
//                            times--;
//                            if(times<0){
//                                JOptionPane.showMessageDialog(null, "读卡超时！编号003");
//                                overTime = false;
//                            }
//                        }
//                        nStop = true;
//                        while(nStop&&nStopWrite&&overTime){
//                            System.out.println();
//                            System.out.println("2222222222222222222222222222222");
//                            //这一步写入密码；密码是jTextFieldEPCPassword里面的值；
//                            try {
//                                Thread.sleep(30);
//                            } catch (InterruptedException e) {
//                                // TODO Auto-generated catch block
//                                e.printStackTrace();
//                            }
//                            len=4;	//读取数据的长度
//                            ptr=0;	//掩码起始地址addr
//                            mem=0;	//0--密码区,1-- EPC号,2-- TID标签ID号,3--用户区User
//                            EPC_Word	=	ID_len;//为EPC的长度
////                            String str = jTextFieldEPCPassword.getText();
//                            mask = WriteStringToByte(str+str);
//
//                            res = ReaderAPI.EPC1G2_WriteWordBlock(hScanner[0], toBeWrite.length()/4, MyMask, 0, ptr, len, mask, IntAccessPassword, 0);
//                            if(res==0){
//                                nStop = false;
//                            }
//                            times--;
//                            if(times<0){
////                                myJdialog md = new myJdialog("读卡超时！编号004");
//                                System.out.println("-------读卡超时！编号004-------");
//                                //JOptionPane.showMessageDialog(null, "读卡超时！编号004");
//                                overTime = false;
//                            }
//                        }
//                        nStop = true;//写成功后，用密码来锁住EPC
//                        //int hh = 0;
//                        while(nStop&&nStopWrite&&overTime){
//                            System.out.println();
//                            System.out.println("33333333333333333333333333333333333");
//                            try {
//                                Thread.sleep(30);
//                            } catch (InterruptedException e) {
//                                // TODO Auto-generated catch block
//                                e.printStackTrace();
//                            }
//                            EPC_Word	=	ID_len;//为EPC的长度
//                            ptr=0;	//掩码起始地址addr
//                            mem=2;	//0-- Kill Password 1--Access Password 2-- EPC号 3-- TID标签ID号 4--用户区User
//
//                            if(iFlagProtectOpt ==1){////0---不保护(不锁), 1---使用密码锁，2---永远锁住(死了), 3---EAS报警(add by martrin20140514)
//                                ilock	=	2;//2—带密码写
//                            }else if(iFlagProtectOpt==2){
//                                ilock	=	3;//3-永不可写
//                            }
//                            for(int i=0;i<8;i++){
//                                //IntAccessPassword[i] = (int)mask[i]&0xff;
//                            }
//                            byte[] newMask = WriteStringToByte(jTextFieldEPCPassword.getText().trim()+jTextFieldEPCPassword.getText().trim());
//                            byte[] intMask = new byte[32];
//                            int k =0;
////					        for(int i=0;i<32;i++){
////					        	intMask[i] = (int) (k&0xff);
////					        }
//                            for(int i = 0;i<4;i++){
//                                intMask[i] = newMask[i];
//                            }
//
//
//                            //String str =  (String) jComboText3.getSelectedItem();
//                            //str  = str.substring(2, str.length());
//                            //byte[] IDBuffer = new byte[30*256];
//                            //byte[] mask = new byte[512];
//                            //guoxiaobing
//                            //res = ReaderAPI.EPC1G2_ReadWordBlock(hScanner[0], str.length()/4, WriteStringToByte(toBeWrite), 0, 0,4, mask, WriteStringToByte("00000000"), 0);
//                            if(res ==0 ){
//                                System.out.println("|||||111111111111111111||||||"+res);
//                            }
//                            for(int i = 0;i<8;i++){
//                                intMask[i] = mask[i];
//                            }
//
//                            res = ReaderAPI.EPC1G2_SetLock(hScanner[0], toBeWrite.length()/4, WriteStringToByte(toBeWrite.trim()), mem, ilock, newMask, 0);
//                            if(res ==0){
//                                nStop = false;
//                            }
//                            System.out.println("||||||2222222222222222222||||||"+res);
//                            times--;
//                            if(times<0){
//                                myJdialog md = new myJdialog("读卡超时！编号005");
//                                //JOptionPane.showMessageDialog(null, "读卡超时！编号005");
//                                overTime = false;
//                            }
//                        }
//                        nStop = true;
//                        while(nStop&&nStopWrite&&overTime){
//                            System.out.println();
//                            System.out.println("444444444444444444444444444444444444444");
//                            System.out.println(jTextFieldEPCPassword.getText().trim().substring(0, 1)+"||||||||||||||||||||");
//                            try {
//                                Thread.sleep(30);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                            EPC_Word	=	ID_len;//为EPC的长度
//                            ptr=0;	//掩码起始地址addr
//                            mem=1;	//0-- Kill Password 1--Access Password 2-- EPC号 3-- TID标签ID号 4--用户区User
//                            if ( iFlagProtectOpt == 1 )////0---不保护(不锁), 1---使用密码锁，2---永远锁住(死了), 3---EAS报警(add by martrin20140514)
//                            {
//                                ilock	=	6;//6—带密码读写
//                            }else if ( 2 == iFlagProtectOpt )
//                            {
//                                ilock	=	7;//7--永不可读写
//                            }
//                            byte[] newMask = WriteStringToByte(jTextFieldEPCPassword.getText().trim()+jTextFieldEPCPassword.getText().trim());
//                            int[] intMask = new int[32];
//                            for(int i = 0;i<8;i++){
//                                intMask[i] = newMask[i];
//                            }
//                            res = ReaderAPI.EPC1G2_SetLock(hScanner[0],toBeWrite.length()/4, WriteStringToByte(toBeWrite.trim()), mem, ilock, newMask, 0);
//                            if(res == 0){
//                                nStop = false;
//                            }
//                            times--;
//                            if(times<0){
//                                myJdialog md = new myJdialog("读卡超时！编号006");
//                                //JOptionPane.showMessageDialog(null, "读卡超时！编号006");
//                                overTime = false;
//                            }
//                        }
//                        nStop = true;
//                    }
                    if(iFlagProtectOpt==3){//此处表示要写报警
                        //1,先写密码，2，后写报警
                        while(nStop&&nStopWrite&&overTime){
                            System.out.println();
                            System.out.println("2222222222222222222222222222222");
                            try {
                                Thread.sleep(30);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            len=4;	//读取数据的长度
                            ptr=0;	//掩码起始地址addr
                            mem=0;	//0--密码区,1-- EPC号,2-- TID标签ID号,3--用户区User
                            EPC_Word	=	ID_len;//为EPC的长度
//                            String str = jTextFieldEPCPassword.getText();
                            mask = WriteStringToByte(str+str);
                            res = ReaderAPI.EPC1G2_WriteWordBlock(hScanner[0], 2, MyMask, 0, ptr, len, mask, IntAccessPassword, 0);
                            if(res==0){
                                nStop = false;
                            }
                            times--;
                            if(times<0){
                                System.out.println("---------读卡超时！编号007--------");
                                overTime = false;
                            }
                        }
                        nStop = true;
                        while(nStop&&nStopWrite&&overTime){
                            System.out.println("333333333333333333333333333333");
                            try {
                                Thread.sleep(30);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            //state	1个字节	0-不报警 1-报警,这里指1
                            res = ReaderAPI.EPC1G2_ChangeEas(hScanner[0], 2, MyMask, 1, WriteStringToByte(null), 0);
                            if(res == 0){
                                nStop = false;
                            }
                            times--;
                            if(times<0){
                                System.out.println("---------读卡超时！编号008--------");
                                overTime = false;
                            }
                        }

                    }

                }
            }
        }
    }




    /** 读取 */
    private void readerActionPerformed() {
        read6C = true;
        if (!NowConState) {
            System.out.println("------未能开始读，未连接状态-----");
            return;
        }
        mytimer = new MyTimer();
        mytimer.start();
    }
    class MyTimer extends Thread{
        String str = "";
        @Override
        public void run() {
            byte[] AccessPassword = new byte[32];
//            jButton15.setEnabled(false);
//            jButton16.setEnabled(true);
//            jComboBoxAlarm.removeAllItems();
//            jButtonSetAlarm.setEnabled(false);
//            jButtonEasAlarm.setEnabled(false);
//            int sec = Integer.valueOf(jTextField22.getText());
//            if(sec<1)sec = 1;
//            if(sec>10)sec = 10;
            int sec = 1 ;
            while(NowConState&&read6C){
//                int iCounter = jTable6cRead.getRowCount();
                int nowRow = -1;
                System.out.println();
                System.out.println("###########6C Read:在读循环中############");
                try {
//                    Thread.sleep(sec*900);
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                byte[] IDBuffer = new byte[30*256];
                //开始读取数据；
                res = ReaderAPI.EPC1G2_ReadLabelID(hScanner[0],1,0,0,mask,IDBuffer,AccessPassword,0);
                byte[] aaa = new String(IDBuffer).trim().getBytes();
                str = ReadByteToString(IDBuffer).trim();
                System.out.println("读出来的数值000是： "+ str);//读出来的数据是str;
                if(res == 0&&(str.length()>=4)){//读成功；
                    System.out.println("-----读成功了-----");

//                    if(iCounter>0){
//                        String strTemp;
//                        boolean strFlag = true;
//                        for(int i=0;i<iCounter;i++){
//                            strTemp = (String) jTable6cRead.getValueAt(i, 1);
//                            System.out.println("读出来的数值111是： "+ strTemp);
//                            if(str.equalsIgnoreCase(strTemp)){
//                                strFlag =false;
//                                nowRow = iCounter;
//                            }
//                        }
//                        System.out.println("读出来的数值222是： "+ strFlag);
////                        if(strFlag){
////                            jTable6cRead.addRow(new String[]{String.valueOf(jTable6cRead.getRowCount()),str,"","","",""});
////                            jLabel6CReadData4.setText(str);
////                            nowRow = jTable6cRead.getRowCount()-1;
////                            jComboBoxAlarm.addItem(str);
////                        }else{
////                            jLabel6CReadData2.setText("Waiting for a tag");
////                            nowRow = -1;
////                        }
//                    }else{
//                        jTable6cRead.addRow(new String[]{String.valueOf(jTable6cRead.getRowCount()),str,"","","",""});
//                        jComboBoxAlarm.addItem(str);
//                        jLabel6CReadData4.setText(str);
//                        nowRow = jTable6cRead.getRowCount()-1;
//                    }

                }else{
//                    jLabel6CReadData2.setText("Waiting for a tag");
                    System.out.println("---------请放入标签-------");
                    nowRow = -1;
                }
            }
            //此处表示已经跳出循环
//            jButton15.setEnabled(true);
//            jButtonSetAlarm.setEnabled(true);
//            jButtonEasAlarm.setEnabled(true);
            System.out.println("--------读结束了--------");
        }
    }

    /** 读出数据类型转换 */
    public  String ReadByteToString(byte[] IDBuffer){
        String mStr = "";
        int ID_len_temp = 0;
        int ID_len = 0;
        byte[] temp = new byte[64 * 2];
        int i = 0;
        ID_len_temp = IDBuffer[ID_len] * 2 + 1;
        System.arraycopy(IDBuffer, ID_len,EPCC1G2_IDBuffer[i], 0, ID_len_temp);
        ID_len += ID_len_temp;
        ID_len = EPCC1G2_IDBuffer[i][0] * 2;
        System.arraycopy(EPCC1G2_IDBuffer[i], 1, temp,0, ID_len);
        mStr = Utility.bytes2HexString(temp, ID_len).trim();
        return mStr;
    }

    /** 写入数据类型转换 */
    public byte[] WriteStringToByte(String str){
        byte[] mask = new byte[512];
        int len = str.length();
        char[] strChar = str.toCharArray();
        for(int i=0;i<len/2;i++){
            char[] strChar_tmp = new char[2];
            strChar_tmp[0] = strChar[2*i];
            strChar_tmp[1] = strChar[2*i+1];
            if(2*i+1>=len){
                char[] strEnd = new char[1];
                strEnd[0] = strChar[2*i];
                mask[i]=(byte)(Short.parseShort(new String(strEnd),16));
            }else{
                mask[i]=(byte)(Short.parseShort(new String(strChar_tmp),16));
            }
        }
        int[] data = new int[mask.length];
        for(int i=0;i<mask.length;i++){
            data[i]=mask[i]&0xff;
        }
        for(int i=0;i<mask.length;i++){
            mask[i]=(byte) data[i];
        }
        return mask;
    }

    /** 连接的一系列操作 */
    private boolean connectButtonFunc() {
        System.out.println("连接前 hScanner = " + hScanner[0]);

        int res = ReaderAPI.VH_ConnectScannerUsb(hScanner);//通过USB连接读写器,res为0连接成功
        if(res==0){
            NowConState = true;  // 判断连接状态 Boolean
            System.out.println("连接成功后 hScanner = " + hScanner[0]);
        }
        if (res != READERDLLAPI.READERDLL._OK) {
//            JOptionPane.showMessageDialog(null,
//                    "发卡机设备未接入!", "Warning",
//                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        long lopt = 3; // USB
        ReaderAPI.VH_SetCommunicationOpt(lopt, null); // 将通信方式存起来
        // 连上了，选择哪个模式通迅变恢
        short[] HardVersion = new short[1];
        short[] SoftVersion = new short[1];

        if (READERDLLAPI.READERDLL._OK == res) {
           //连接成功
            for (int i = 0; i < 3; i++) {
                //读版本
                res = ReaderAPI.VH_GetVersion(hScanner[0], HardVersion, SoftVersion);
                if (READERDLLAPI.READERDLL._OK == res) {
                    break;
                }
            }

            if (res != READERDLLAPI.READERDLL._OK) {
//                JOptionPane.showMessageDialog(null,
//                        "Connect Reader Fail!(ReadHanderParam)", "Warning",
//                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            for (int i = 0; i < 5; i++) {
                // 获取读写器基本工作参数
                res = ReaderAPI.ReadHanderParam(hScanner[0], gHandParam);
                if (res == READERDLLAPI.READERDLL._OK) {
                    break;
                }
            }

//            for (int i = 0; i < 3; i++) {
//                ////////////////////////////////////////
//                //  读auto参数 ----> 有问题(API里加载参数不够,少一个)，不知道问题出在哪！！！！
//                //  但是不影响读写程序，可正常使用,暂时不要删
//                ////////////////////////////////////////
//                res = ReaderAPI.ReadAutoParam(hScanner[0], gReaderAutoParam);
//                if (res == READERDLLAPI.READERDLL._OK) {
//                    break;
//                }
//            }

            for (int i = 0; i < 3; i++) {
                // 读基本参数
                res = ReaderAPI.ReadBasicParam(hScanner[0], gBasicParam);
                if (READERDLLAPI.READERDLL._OK == res) {
                    break;
                }
            }

            for (int i = 0; i < 3; i++) {
                //读ID
                res = ReaderAPI.GetReaderID(hScanner[0], gReaderID);
                if (READERDLLAPI.READERDLL._OK == res) {
                    break;
                }
            }

            for (int i = 0; i < 3; i++) {
                res = ReaderAPI.ReadSimParam(hScanner[0], Param);
                if (READERDLLAPI.READERDLL._OK == res) {
                    break;
                }
            }
        }

        ifConnectReader = 0;
        NewConnect = 0;

//		JOptionPane.showMessageDialog(this, "Connect Reader Success!",
//				"Notice", JOptionPane.INFORMATION_MESSAGE);

//        jTabbedPane1.setSelectedComponent(jPanel10);  //jPanel10  对应tab标签ISO18000-6C Write
        // 标签类型
        System.out.print("gHandParam[0].TagType --- " + gHandParam[0].TagType);

// if(gHandParam[0].TagType==0x01)
//        if ((gBasicParam[0].TagType) == 0x04) {
//            jRadioButton2.setSelected(true);// 6B
//            jRadioButton1.setSelected(false);// 6B
//            readerParametersDate.setOldDate("tagTypes", 4);
//            //gBasicParam[0].TagType = 0x01;
//        } else {
//            jRadioButton1.setSelected(true);// 6B
//            jRadioButton2.setSelected(false);// 6B
//            readerParametersDate.setOldDate("tagTypes", 1);
//        }

         /** 发射功率*/
        int m_Power = gBasicParam[0].Power;

//        jTextField4.setText("" + m_Power);

//        jTextField4.addKeyListener(new KeyListener() {
//            //处理RF power的输入内容；
//            @Override
//            public void keyTyped(KeyEvent e) {
//                // TODO Auto-generated method stub
//                int keyChar = e.getKeyChar();
//                if(keyChar >= KeyEvent.VK_0 && keyChar <= KeyEvent.VK_9){
//                    //输入为数字，正确，不做处理；
//                }else{
//                    //输入为非数字，屏蔽输入；
//                    e.consume();
//                }
//            }
//            @Override
//            public void keyPressed(KeyEvent e) {}
//
//            @Override
//            public void keyReleased(KeyEvent e) {}
//        });

        readerParametersDate.setOldDate("RFPowerOutput", m_Power);
        System.out.println("RFPowerOutput-----发射功率----"+ m_Power);


//        if (res == ReaderAPI._OK) {
//            String str = new String(gReaderID);
//            //jTextField3是设置Hardware ID的值
//            jTextField3.setText(str);
//            //使其不可被编辑
//            jTextField3.setEditable(false);
//        }

        //////////////////////////////////////////////////////////////////////////
        //16进制输出，还是10进制输出
        //0-16进,1-10进
//        if (0 == Param[0].DataFormat)
//
//        {
//            //        	jRadioButton3.Checked = true;
//            //        	jRadioButton4.Checked = false;
//            jRadioButton3.setSelected(true);
//            jRadioButton4.setSelected(false);
//            readerParametersDate.setOldDate("DataFormat", 0);
//        } else if (1 == Param[0].DataFormat) {
//            jRadioButton3.setSelected(false);
//            jRadioButton4.setSelected(true);
//            readerParametersDate.setOldDate("DataFormat", 1);
//        }

/*gxb del 2016-03-15
		if (Param[0].IsEnter == 1) {
			jCheckBox2.setSelected(true);
		}
@{*/

        /*gxb add start 2016-03-15
         * @{*/
        if(NowConState){
            //如果连接成功，将prefix的状态显示出来
            System.out.println();
            System.out.println("**************************************");
            System.out.println("NowConState is    "+ NowConState);
            System.out.println("**************************************");

//            if(Param[0].IsPerfix==1){
//                jCheckBox1.setSelected(true);
//            }else{
//                jCheckBox1.setSelected(false);
//            }
//            if(Param[0].IsEnter==1){
//                jCheckBox2.setSelected(true);
//            }else{
//                jCheckBox2.setSelected(false);
//            }

            if(Param[0].PerfixCode!=null){
                String jTextField5T = new String(Param[0].PerfixCode);
                String jTextField5TN = null;
                for(int i=0;i<jTextField5T.length();i++){
                    char keyChar = jTextField5T.charAt(i);
                    if(keyChar >= KeyEvent.VK_0 && keyChar <= KeyEvent.VK_9){
                        if(jTextField5TN==null){
                            jTextField5TN = String.valueOf(keyChar);
                        }else{
                            jTextField5TN = jTextField5TN + String.valueOf(keyChar);
                        }
                    }
                }
//                jTextField5.setText(jTextField5TN);
            }else{
//                jTextField5.setText(null);
                System.out.println("不知道干嘛的：Param[0].PerfixCode == null");
            }
//            jTextField5.addKeyListener(new KeyListener() {
//                //处理RF power的输入内容；
//                @Override
//                public void keyTyped(KeyEvent e) {
//                    // TODO Auto-generated method stub
//                    int keyChar = e.getKeyChar();
//                    if(keyChar >= KeyEvent.VK_0 && keyChar <= KeyEvent.VK_9){
//                        //输入为数字，正确，不做处理；
//                    }else{
//                        //输入为非数字，屏蔽输入；
//                        e.consume();
//                    }
//                }
//                @Override
//                public void keyPressed(KeyEvent e) {}
//                @Override
//                public void keyReleased(KeyEvent e) {}
//            });
//            int Interval = gReaderAutoParam[0].Interval;
//            jComboBox3.setSelectedIndex(Interval);
//            readerParametersDate.setOldDate("IntervalOfReadingTag", Interval);
        }


        /*gxb add end 2016-03-15 @}*/
        //数据起始地址
//          String strTemp = string.Format("{0,0:d}", Param.StartAddress);


//        String strTemp = String.valueOf(Param[0].StartAddress);
//        jTextField6.setText(strTemp);
//        jTextField6.addKeyListener(new KeyListener() {
//            //处理StartAddress的输入内容；
//            @Override
//            public void keyTyped(KeyEvent e) {
//                // TODO Auto-generated method stub
//                int keyChar = e.getKeyChar();
//                if(keyChar >= KeyEvent.VK_0 && keyChar <= KeyEvent.VK_9){
//                    //输入为数字，正确，不做处理；
//                }else{
//                    //输入为非数字，屏蔽输入；
//                    e.consume();
//                }
//            }
//            @Override
//            public void keyPressed(KeyEvent e) {}
//            @Override
//            public void keyReleased(KeyEvent e) {}
//        });
//
        readerParametersDate.setOldDate("StartAddress", Param[0].StartAddress);
//        String strTemp1 = String.valueOf(Param[0].DataLen);


//        jTextField7.setText(strTemp1);
//        jTextField7.addKeyListener(new KeyListener() {
//            //处理DateAddressLength的输入内容；
//            @Override
//            public void keyTyped(KeyEvent e) {
//                // TODO Auto-generated method stub
//                int keyChar = e.getKeyChar();
//                if(keyChar >= KeyEvent.VK_0 && keyChar <= KeyEvent.VK_9){
//                    //输入为数字，正确，不做处理；
//                }else{
//                    //输入为非数字，屏蔽输入；
//                    e.consume();
//                }
//            }
//            @Override
//            public void keyPressed(KeyEvent e) {}
//            @Override
//            public void keyReleased(KeyEvent e) {}
//        });


        readerParametersDate.setOldDate("DateAddressLength", Param[0].DataLen);


        String strTemp2 = String.valueOf(Param[0].OutputInterval);


//        jTextField8.setText(strTemp2);
//        jTextField8.addKeyListener(new KeyListener() {
//            //处理OutputInterval的输入内容；
//            @Override
//            public void keyTyped(KeyEvent e) {
//                // TODO Auto-generated method stub
//                int keyChar = e.getKeyChar();
//                if(keyChar >= KeyEvent.VK_0 && keyChar <= KeyEvent.VK_9){
//                    //输入为数字，正确，不做处理；
//                }else{
//                    //输入为非数字，屏蔽输入；
//                    e.consume();
//                }
//            }
//            @Override
//            public void keyPressed(KeyEvent e) {}
//            @Override
//            public void keyReleased(KeyEvent e) {}
//        });



        readerParametersDate.setOldDate("OutputInterval", Param[0].OutputInterval);



//        if (gReaderAutoParam[0].IDPosition == 0) {
//            jRadioButton5.setSelected(true);
//            jRadioButton6.setSelected(false);
//            jRadioButton7.setSelected(false);
//            readerParametersDate.setOldDate("DateArea", 0);
//        }
//        // else if ( Param.DataBank == 1 )
//        else if (gReaderAutoParam[0].IDPosition == 1) {
//            jRadioButton5.setSelected(false);
//            jRadioButton6.setSelected(true);
//            jRadioButton7.setSelected(false);
//            readerParametersDate.setOldDate("DateArea", 1);
//        }
//        // else if ( Param.DataBank == 2 )
//        else if (gReaderAutoParam[0].IDPosition == 2) {
//            jRadioButton5.setSelected(false);
//            jRadioButton6.setSelected(false);
//            jRadioButton7.setSelected(true);
//            readerParametersDate.setOldDate("DateArea", 2);
//        }
//        //
//
//        int[] ptr = new int[1];
//        int[] len = new int[1];
//        byte[] mask = new byte[32];
//        res = ReaderAPI.GetReportFilter(hScanner[0], ptr, len, mask);
//        if (res == ReaderAPI._OK) {
//            String strPos = String.format("%d", ptr[0]);
//            jTextField6.setText(strPos);
//        }
//
//        String str_temp;
//        HVer1 = gHandParam[0].RFhrdVer1;
//        HVer2 = gHandParam[0].RFhrdVer2;
//        SVer1 = gHandParam[0].RFSoftVer1;
//        SVer2 = gHandParam[0].RFSoftVer2;
//        SoftVersion[0] = (short) (gHandParam[0].RFSoftVer1 * 0x100
//                + gHandParam[0].RFSoftVer2);
//        str_temp = String.format("%02d%02d", (SoftVersion[0] >> 8) & 0xFF,
//                SoftVersion[0] & 0xFF);
//        //设置SoftVersion值
//        jTextField2.setText("" + str_temp);
//        //设置不可编辑
//        jTextField2.setEditable(false);
//        HardVersion[0] = (short) (gHandParam[0].RFhrdVer1 * 0x100
//                + gHandParam[0].RFhrdVer2);
//
//        str_temp = String.format("%02d%02d", (HardVersion[0] >> 8) & 0xFF,
//                HardVersion[0] & 0xFF);
//        //设置HardVersion值
//        jTextField1.setText("" + str_temp);
//        //设置不可编辑
//        jTextField1.setEditable(false);
//
//        jComboBox1.removeAllItems();
//        //jComboBox1.setEditable(false);
//        jComboBox2.removeAllItems();
//        //jComboBox2.setEditable(false);

        int NumofFreq;
        double Freq, jumpFreq;
        boolean ischina = false;
        boolean AM = false;
        switch (SoftVersion[0] & 0xFF) {
            case 0x00:
                NumofFreq = 63;
                Freq = 902.6;
                jumpFreq = 0.4;
                break;
            case 0x01:
                NumofFreq = 125;
                Freq = 902.6;
                jumpFreq = 0.2;
                break;
            case 0x02:
                NumofFreq = 11;
                Freq = 865.5;
                jumpFreq = 0.2;
                break;
            case 0x03:
                NumofFreq = 8;
                Freq = 902.6;
                jumpFreq = 3.6;
                break;
            case 0x04:
                NumofFreq = 10;
                Freq = 952.2;
                jumpFreq = 0.2;
                break;
            case 0x05:
                NumofFreq = 42;
                Freq = 919.2;
                jumpFreq = 0.2;
                break;
            case 0x06:
                NumofFreq = 16;
                Freq = 920.625;
                jumpFreq = 0.25;
                ischina = true;
                break;
            // 韩国
            case 0x07:
                NumofFreq = 16;
                Freq = 910.5;
                jumpFreq = 0.2;
                break;
            case 0x08:
                NumofFreq = 50;
                Freq = 902.75;
                jumpFreq = 0.5;
                AM = true;
                break;
            case 0x09:
                NumofFreq = 16;
                Freq = 840.625;
                jumpFreq = 0.25;
                ischina = true;
                break;
            default:
                NumofFreq = 63;
                Freq = 902.6;
                jumpFreq = 0.4;
        }

//        for (int i = 0; i < NumofFreq; i++) {
//
//            if (ischina) {
//                str_temp = String
//                        .format("%d--%7.3f", i + 1, (Freq + i * jumpFreq)*1000);
//
//            } else {
//                if (AM == true) {
//                    str_temp = String.format("%d--%5.2f", i + 1, Freq + i* jumpFreq);
//                } else {
//                    double m = (Freq + i* jumpFreq);
//                    int d = (int)(m*1000);
//                    str_temp = String.format("%d--%d", i + 1, d);
//                }
//            }
//            jComboBox1.addItem(str_temp);
//            jComboBox2.addItem(str_temp);
//        }

        System.out.println("================"+gBasicParam[0].Max_Frequence);
//        jComboBox1.setSelectedIndex(gBasicParam[0].Min_Frequence - 1);
//        readerParametersDate.setOldDate("MinFrequencyMHZ", gBasicParam[0].Min_Frequence - 1);
//        jComboBox2.setSelectedIndex(gBasicParam[0].Max_Frequence - 1);
//        readerParametersDate.setOldDate("MaxFrequencyMHZ", gBasicParam[0].Max_Frequence - 1);
        return true;
    }

    /** 断开连接 */
    public void jButtonDisConStopCon() {
        // TODO Auto-generated method stub
        if(NowConState){
            res = ReaderAPI.DisconnectScanner(hScanner[0]);
            if(res == 0 ){
                System.out.println("连接已断开！");
                NowConState=false;
            }else{
                System.out.println("连接已断开！");
            }
        }else{
            System.out.println("尚未进行连接！");
        }
    }

    /** 初始化DLL库 */
    public void formWindowOpened() {// GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
        // Combox串口号
        // try{
        //
        // //加载DLL文件，执行dll方法
        // 系统属性
        Properties props = System.getProperties();
        //表示是64位系统
        if(props.getProperty("os.arch").indexOf("64")>0){
            ReaderAPI = Native.load("VHLib-64",
                    READERDLLAPI.READERDLL.class);
            System.out.println("操作系统的构架：" + props.getProperty("os.arch"));
        }else{
            ReaderAPI = Native.load("VHLib-32",
                    READERDLLAPI.READERDLL.class);
            System.out.println("操作系统的构架：" + props.getProperty("os.arch"));
        }
        if (ReaderAPI != null) {
            String strLog;
            System.out.println("DllComm加载成功！");
            // 设置日期格式
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            strLog = df.format(new Date()) + ".log";
            // new Date()为获取当前系统时间
            System.out.println(strLog);

            logFileName = System.getProperty("user.dir");
            logFileName += "\\Log\\" + strLog;
            // user.dir指定了当前的路径
            System.out.println(logFileName);
            ReaderAPI.VH_SetLogFile(logFileName);
        } else {
            return;
        }
    }


}
