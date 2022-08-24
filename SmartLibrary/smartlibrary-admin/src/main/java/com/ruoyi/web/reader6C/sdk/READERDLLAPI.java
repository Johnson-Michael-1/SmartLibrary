/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ruoyi.web.reader6C.sdk;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Structure;

//import com.sun.jna.Library;

/**
 * 
 * @author Administrator
 */
public class READERDLLAPI {

	// 1.实现VHLib.dll 文件接口
	public interface READERDLL extends Library {

		public short _OK = 0;

		public static class BYTEA extends Structure {
			public byte[] Data = new byte[256];
			// public byte[255] data;

		}

		public static class ReaderDate extends Structure {
			public byte Year;
			public byte Month;
			public byte Day;
			public byte Hour;
			public byte Minute;
			public byte Second;

		}

		@Structure.FieldOrder(value={"RFhrdVer2","TagType","Alarm","OutputMode","USBBaudRate","BtBaudRate","Min_Frequence","Max_Frequence","Power","RFhrdVer1",
				"RFSoftVer1","RFSoftVer2","ISTID","TIDAddr","TIDLen","ISUSER","USERAddr","USERLen","ISVibrate","Modutype","Reserve21","Reserve22","Reserve23",
				"Reserve24","Reserve25","Reserve26","Reserve27","Reserve28","Reserve29","Reserve30","Reserve31","Reserve32"})
		public static class HandsetParam extends Structure {
			public byte TagType; // 标签种类：01H－ISO18000-6B，02H－EPCC1，04H－EPCC1G2，08H－EM4442。
									// (0)
			public byte Alarm; // bit0-bit7 bit0:0-不报警，1-报警 bit1 0-不使用白名单
								// 1-使用白名单。 (1)
			public byte OutputMode; // 数据输出模式：0-保存并直接输出,1-保存但不直接输出,2-不保存但直接输出
									// (2)
			public byte USBBaudRate; // USB接口波特率 04-9600 05-19200 06-38400
										// 07-57600 08-115200 (3)
			public byte BtBaudRate; // 蓝牙串口通讯波特率 (4)
			public byte Min_Frequence; // 发射微波信号频率的起始点，取值： 1~63。 (5)
			public byte Max_Frequence; // 发射微波信号频率的起始点，取值： 1~63。 (6)
			public byte Power; // 发射功率值，取值：0~160。 (7)
			public byte RFhrdVer1; // RF模块硬件主版本 (8)
			public byte RFhrdVer2; // RF模块硬件次版本 (9)
			public byte RFSoftVer1; // RF模块软件主版本 (10)
			public byte RFSoftVer2; // RF模块软件次版本 (11)
			public byte ISTID; // 是否读TID区 (12)
			public byte TIDAddr; // TID读取起始位置 (13)
			public byte TIDLen; // TID读取长度 (14)
			public byte ISUSER; // 是否读USER (15)
			public byte USERAddr; // USER读取起始位置 (16)
			public byte USERLen; // USER读取长度 (17)
			public byte ISVibrate; // 电机振动,0-不，1-振动 (18)
			public byte Modutype; // 模块类型,0---R2000, 1---VM5E (19)
			public byte Reserve21; // 保留 (20)
			public byte Reserve22; // 保留 (21)
			public byte Reserve23; // 保留 (22)
			public byte Reserve24; // 保留 (23)
			public byte Reserve25; // 保留 (24)
			public byte Reserve26; // 保留 (25)
			public byte Reserve27; // 保留 (26)
			public byte Reserve28; // 保留 (27)
			public byte Reserve29; // 保留 (28)
			public byte Reserve30; // 保留 (29)
			public byte Reserve31; // 保留 (30)
			public byte Reserve32; // 保留 (31)
		}

		@Structure.FieldOrder(value={"BaudRate", "EnableBuzzer", "Gateway1", "Gateway2", "Gateway3", "Gateway4", "IP1", "IP2", "IP3", "IP4", "MAC1", "MAC2", "MAC3", "MAC4", "MAC5", "MAC6", "Mask1", "Mask2", "Mask3", "Mask4", "Max_Frequence", "Min_Frequence", "NumofCard",
				"Port1", "Port2", "Power", "ReadDuration", "ReadTimes", "ReaderAddress", "Reserve5", "TagType", "WorkMode"})
		public static class ReaderBasicParam extends Structure {
			public byte BaudRate; // 串口的通信速率，取值：00H~08H，代表速率同"设定波特率"命令。
			public byte Power; // 发射功率值，取值：20~30dBm。0-63
			public byte Min_Frequence; // 发射微波信号频率的起始点，取值： 0~59。
			public byte Max_Frequence; // 发射微波信号频率的终止点，取值： 0~59。
			public byte Reserve5; // 保留
			public byte WorkMode; // 读写器工作方式：0-主动方式，1-命令方式
			public byte ReaderAddress; // RS485地址:1--254
			public byte NumofCard; // 最多读卡数目。
			public byte TagType; // 标签种类：01H－ISO18000-6B，02H－EPCC1，04H－EPCC1G2，08H－EM4442。
			public byte ReadDuration; // (10)读卡持续时间：射频发射持续时间，只针对EM标签有效；0－10ms，1－20ms，2－30ms，3－40ms。
			public byte ReadTimes; // 读卡次数M：收到上位机读卡命令，读写器执行M次此命令。
			public byte EnableBuzzer; // 1:使能蜂鸣器0:不使能蜂鸣器
			public byte IP1; // 读写器IP地址
			public byte IP2; //
			public byte IP3; //
			public byte IP4; //
			public byte Port1; // 读写器端口高位
			public byte Port2; //
			public byte Mask1; // 读写器掩码1
			public byte Mask2; // 读写器掩码2
			public byte Mask3; // 读写器掩码3
			public byte Mask4; // 读写器掩码4
			public byte Gateway1; // 读写器地址网关
			public byte Gateway2; //
			public byte Gateway3; //
			public byte Gateway4; //
			public byte MAC1; // 读写器MAC地址
			public byte MAC2; //
			public byte MAC3; //
			public byte MAC4; //
			public byte MAC5; //
			public byte MAC6; //
		}

		// 读写器主动工作参数
		@Structure.FieldOrder(value={"AutoMode","TimeH", "TimeL","Interval","NumH, NumL","OutputManner","OutInterface","WiegandWidth","WiegandInterval","ID_Start","IDPosition","Report_Interval","Report_Condition",
				"Report_Output","Antenna","TriggerMode","HostIP1", "HostIP2", "HostIP3","HostIP4","Port1", "Port2","Reserve24","ArgentinaSim","CardTime1", "CardTime2","ArgentinaMode","AnSleepTime","Alarm", "Reserve31","EnableRelay"})
		public static class ReaderAutoParam extends Structure {
			public byte AutoMode; // (1)读标签模式：0-定时方式，1-触发方式。
			public byte TimeH; // (2)标签保留时间：单位：秒s。缺省值为1。
			public byte TimeL; // (3)
			public byte Interval; // (4)0-10ms，1-20ms，2-30ms，3-50ms，4-100ms。缺省值为2。每隔设定时间主动读取一次标签。
			public byte NumH; // (5)标签保留数目：缺省值为1。已读取的标签ID在读写器内存中保留的数目。
			public byte NumL; // (6)
			public byte OutputManner; // (7)数据输出格式：0-简化格式，1-标准格式，2-XML格式。缺省值为0。
			public byte OutInterface; // (8)输出接口：0－RS232口，1－RS485口，2－RJ45口。缺省值为0。
										// 3- Wiegand26 4- Wiegand34
			public byte WiegandWidth; // (9)Weigand脉冲宽度值。
			public byte WiegandInterval; // (10)Weigand脉冲间隔值。
			public byte ID_Start; // (11)输出卡号的起始位，取值0～4。
			public byte IDPosition; // (12)卡号在电子标签上的存放地址。EPC区--0,
									// USER区--1，TID区--2
			public byte Report_Interval; // (13)通知间隔：单位秒s。缺省值为1。每隔设定时间主动通知上位机一次。
			public byte Report_Condition; // (14)通知条件：缺省值为1。0-被动通知，1-定时通知，2-增加新标签，3-减少标签，4-标签数变化
			public byte Report_Output; // (15)通知输出端
			public byte Antenna; // (16)天线选择。1-ant1,2-ant2,4-ant4,8-ant8
			public byte TriggerMode; // (17)触发方式(缺省值为0): 0-低电平 1-高电平
			public byte HostIP1; // (18)被通知IP地址
			public byte HostIP2; // (19)
			public byte HostIP3; // (20)
			public byte HostIP4; // (21)
			public byte Port1; // (22)被通知端口
			public byte Port2; // (23)
			public byte Reserve24; // (24)被通知MAC,mofi by mqs 20121207 保留
			public byte ArgentinaSim; // (25)//仿真模式(阿根廷),0--非仿真，1--仿真
			public byte CardTime1; // (26)//读卡总超时1
			public byte CardTime2; // (27)//读卡总超时2
			public byte ArgentinaMode; // (28)//阿根的五种模式,0---Only ATA
										// 只读ATA;1---Only EPC 只读EPC;2---Only EPC
										// & TID 只读EPC+TID;3---ATA + EPC
										// 读ATA+EPC;4---ATA + EPC &
										// TID读ATA+EPC&TID
			public byte AnSleepTime; // (29)//天线睡眠时间，单位：ms,(1-255)
			public byte Alarm; // (30)0-不报警，1-报警。在定时和触发方式下是否检测报警。
			public byte Reserve31; // (31)标准输出时间间隔，缺省值为120秒，1～255。
			public byte EnableRelay; // (32)自动工作模式是否控制继电器1:控制 0:不控制
		}

		public static int PERFIXLEN = 20; // 前缀值的长度


		@Structure.FieldOrder(value = {"DataFormat", "DataLen", "IsEnter", "IsPerfix", "OutputInterval", "PerfixCode", "StartAddress"})
		public static class SimParam extends Structure {
			public byte DataFormat; // (0)数据格式两种：16进制--0, 10进制--1
			// public byte DataBank; //(1)EPC区--0, USER区--1，TID区--2
			public byte IsPerfix; // (2)是否前缀，1--带,0--不带
			// public byte []PerfixCode; //(3-22)前缀值,不够0x00填充
			// [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 20)]
			// [MarshalAs(UnmanagedType.ByValArray, SizeConst = 20)]
			// public byte[] num;
			// public byte[] num1;
			public byte[] PerfixCode = new byte[20];
			// public byte[] PerfixCode;
			public byte IsEnter; // (23)是否回车换行, 1--带,0--不带
			public byte StartAddress; // (24)数据起始地址
			public byte DataLen; // (25)数据长度
			public byte OutputInterval; // (26)标准输出间隔(0-255)(0--输出ID之间无间隔)
			// public byte Demo1;
			// public byte Demo2;
			// public SimParam()
			// {
			// PerfixCode = new byte[20];
			// }
		}

		public static class tagReaderFreq extends Structure {
			public String chFreq; // 国家频率字符串

			public int iGrade; // 级数 = 50;
			public int iSkip; // 步进 = 500KHz;
			public int dwFreq; // 起始频率 = 902750;
			// 公式：902750 + 级数*步进
		}

		// ////////////////////////////////////////////////////////////////////////
		// 用途: 取仿真键盘小模块参数
		// 函数名: ReadSimParam
		// 功能: 将小模块的参数读出来
		// 输入参数: hScanner 为通信句柄,
		// 输出参数: pParam 为结构体指针
		// 返回值: 返回 0 为成功，否则失败
		// 备注:
		// ////////////////////////////////////////////////////////////////////////

		public short ReadSimParam(int hScanner, SimParam param[]);

		/* gxb add start 2016-03-15 @{ */
		public short WriteSimParam(int hScanner, SimParam param[]);

		public short WriteBasicParam(int hScanner,
                                     ReaderBasicParam gBasicParam[]);

		// 写EPC
		// [DllImport(PUBLIC_COM_PATH, CharSet = CharSet.Ansi)]

		// public static extern UInt16 EPC1G2_ReadLabelID(int hScanner, int mem,
		// int ptr, int len, byte[] mask, byte[] IDBuffer, ref int nCounter, int
		// Address);
		// public short EPC1G2_ReadLabelID(int hScanner, int mem, int ptr, int
		// len, byte[] mask, byte[] IDBuffer, int[] nCounter, int Address);
		// public short EPC1G2_WriteEPC(int hScanner, byte len, byte[] Data,
		// byte[] AccessPassword, int Address);
		public short EPC1G2_WriteEPC(int hScanner, int len, byte[] Data,
                                     int[] intAccessPassword, int Address);

		/* gxb add end 2016-03-15 @} */

		// 获得读写器ID

		public short GetReaderID(int hScanner, byte[] ReaderID);

		// public void VH_GetVersion(int hScanner[], int wHardVer, int
		// wSoftVer);

		// 连接读写器
		// apiReturn _stdcall ConnectScanner(HANDLE *hScanner, char *szPort, int
		// *nBaudRate);
		public short ConnectScanner(int hScanner[], String szPort, int nBaudRate);

		// 断开连接
		// apiReturn _stdcall DisconnectScanner(HANDLE hScanner);
		public short DisconnectScanner(int hScanner);

		// 通过USB连接读写器
		// apiReturn _stdcall VH_ConnectScannerUsb(HANDLE *hScanner);
		public short VH_ConnectScannerUsb(int hScanner[]);

		// 断开连接
		// apiReturn _stdcall VH_DisconnectScannerUsb(HANDLE hScanner);
		public short VH_DisconnectScannerUsb(int hScanner);

		// 读取版本号
		// apiReturn _stdcall VH_GetVersion(HANDLE hScanner, WORD *wHardVer,
		// WORD *wSoftVer);
		// 读取版本号
		public short VH_GetVersion(int hScanner, short wHardVer[],
                                   short wSoftVer[]);

		// ////////////////////////////////////////////////////////////////////////
		// 用途: 设置输出日志文件
		// 函数名: VH_SetLogFile
		// 功能: 将日志功能写入到指定文件中
		// 输入参数: FileName 指全路径名
		// 输出参数: 无
		// 返回值: 返回0为成功，否则-1为失败
		// 备注:
		// ////////////////////////////////////////////////////////////////////////
		public short VH_SetLogFile(String FileName);

		// ////////////////////////////////////////////////////////////////////////
		// 用途: 设置通信方式
		// 函数名: Mux_SetCommunicationOpt
		// 功能: 对手持机的通信模式进行设置
		// 输入参数: lOpt 为何种方式通信(0--TCP/IP, 1--RS232,2--RS485, 3--USB)
		// lpRHand 为读写器的句柄
		// 输出参数:
		// 返回值: 返回 0 为成功，其它失败
		// 备注: 外部接口
		// ////////////////////////////////////////////////////////////////////////
		// apiReturn _stdcall VH_SetCommunicationOpt(long lOpt, void *lpRWHand)
		public short VH_SetCommunicationOpt(long lOpt, char lpRWHand[]);

		// 获取读写器基本工作参数
		// apiReturn _stdcall ReadHanderParam(HANDLE hScanner, HandsetParam *
		// pParam);
		public short ReadHanderParam(int hScanner, HandsetParam pParam[]);

		// 设置读写器基本工作参数
		public short WriteHanderParam(int hScanner, HandsetParam pParam[]);

		public short ReadBasicParam(int hScanner,
                                    ReaderBasicParam gBasicParam[]);

		// public short WriteReaderBasicParam(int hScanner, ReaderBasicParam
		// gBasicParam[]);

		// 设置读写器自动工作参数
		public short ReadAutoParam(int hScanner,
                                   ReaderAutoParam[] gReaderAutoParam);

		// 设置读写器基本工作参数
		public short WriteAutoParam(int hScanner,
                                    ReaderAutoParam[] gReaderAutoParam);

		// 读取EPC1G2标签ID号
		// apiReturn _stdcall EPC1G2_ReadLabelID(HANDLE hScanner, BYTE mem, int
		// ptr, BYTE len, BYTE *mask, BYTE *IDBuffer, int *nCounter,int
		// Address);
		public short EPC1G2_ReadLabelID(int hScanner, int mem, int ptr,
                                        int len, byte[] mask, byte[] IDBuffer, byte[] accessPassword,
                                        int Address);

		// 设置手机进入读写器模式，即模块电源打开，1--打开，0--关闭
		// apiReturn _stdcall SetReaderMode(HANDLE hScanner, BYTE nMode);
		public short SetReaderMode(int hScanner, byte nMode);

		// 永久休眠标签
		// apiReturn _stdcall EPC1G2_KillTag(HANDLE hScanner, BYTE EPC_WORD,
		// BYTE *IDBuffer, BYTE *KillPassword,int Address);
		public short EPC1G2_KillTag(int hScanner, int EPC_WORD,
                                    byte[] IDBuffer, byte[] KillPassword, int Address);

		// 读一块数据
		// apiReturn _stdcall EPC1G2_ReadWordBlock(HANDLE hScanner, BYTE
		// EPC_WORD, BYTE *IDBuffer, BYTE mem, BYTE ptr, BYTE len, BYTE *Data,
		// BYTE *AccessPassword,int Address);
		public short EPC1G2_ReadWordBlock(int hScanner, int EPC_WORD,
                                          byte[] IDBuffer, int mem, int ptr, int len, byte[] Data,
                                          byte[] intAccessPassword, int Address);

		// 写一块数据
		// apiReturn _stdcall EPC1G2_WriteWordBlock(HANDLE hScanner, BYTE
		// EPC_WORD, BYTE *IDBuffer, BYTE mem, BYTE ptr, BYTE len, BYTE *Data,
		// BYTE *AccessPassword,int Address);
		public short EPC1G2_WriteWordBlock(int hScanner, int EPC_WORD,
                                           byte[] IDBuffer, int mem, int ptr, int len, byte[] Data,
                                           int[] intAccessPassword, int Address);

		// 设置读写保护状态
		// apiReturn _stdcall EPC1G2_SetLock(HANDLE hScanner, BYTE EPC_WORD,
		// BYTE *IDBuffer, BYTE mem, BYTE Lock, BYTE *AccessPassword,int
		// Address);
		public short EPC1G2_SetLock(int hScanner, int EPC_WORD,
                                    byte[] IDBuffer, int mem, int Lock, byte[] bs, int Address);

		// 块锁命令
		// apiReturn _stdcall EPC1G2_BlockLock(HANDLE hScanner, BYTE EPC_WORD,
		// BYTE *IDBuffer, BYTE ptr, BYTE *AccessPassword,int Address);
		public short EPC1G2_BlockLock(int hScanner, int EPC_WORD,
                                      byte[] IDBuffer, int ptr, byte[] AccessPassword, int Address);

		// EAS状态操作命令
		// apiReturn _stdcall EPC1G2_ChangeEas(HANDLE hScanner, BYTE EPC_WORD,
		// BYTE *IDBuffer, BYTE State, BYTE *AccessPassword,int Address);
		public short EPC1G2_ChangeEas(int hScanner, int EPC_WORD,
                                      byte[] IDBuffer, int State, byte[] AccessPassword, int Address);

		// EAS报警命令
		// apiReturn _stdcall EPC1G2_EasAlarm(HANDLE hScanner,int Address);
		public short EPC1G2_EasAlarm(int hScanner, int Address);

		// 读保护设置
		// apiReturn _stdcall EPC1G2_ReadProtect(HANDLE hScanner,BYTE
		// *AccessPassword, BYTE EPC_WORD, BYTE *IDBuffer,int Address);
		public short EPC1G2_ReadProtect(int hScanner, byte[] AccessPassword,
                                        int EPC_WORD, byte[] IDBuffer, int Address);

		// 复位读保护设置
		// apiReturn _stdcall EPC1G2_RStreadProtect(HANDLE hScanner, BYTE
		// *AccessPassword,int Address);
		public short EPC1G2_RStreadProtect(int hScanner, byte[] AccessPassword,
                                           int Address);

		// 设置读写器ID
		public short SetHandsetID(int hScanner, byte[] HandsetID);

		// 获得读写器ID
		public short GetHandsetID(int hScanner, byte[] HandsetID);

		public short SetReaderTime(int hScanner, ReaderDate time);

		// 获得时间
		public short GetReaderTime(int hScanner, byte[] time);

		// 设置标签过滤器
		public short SetReportFilter(int hScanner, int ptr, int len, byte[] mask);

		// 获得标签过滤器
		public short GetReportFilter(int hScanner, int[] ptr, int[] len,
                                     byte[] mask);

		// 设置蓝牙波特率
		public short SetBtBaudRate(int hScanner, int nBaudRate);

		// 获取蓝牙波特率
		public short GetBtBaudRate(int hScanner, int nBaudRate[]);

		// 获得蓝牙名称
		public short SetBluetoothName(int hScanner, int nLen,
                                      byte BluetoothName[]);

		// 设置蓝牙名称
		public short GetBluetoothName(int hScanner, byte BluetoothName[]);

		// ////////////////////////////////////////////////////////////////////////
		// 用途: 获取时间段的记录总数
		// 函数名: VH_GetRecordNum
		// 功能: 取记录总数
		// 输入参数: hScanner 通信句柄
		// stime 为起始时间，etime 为结束时间
		// 输出参数: nTotal 为记录总数
		// 返回值: 0--成功，其它失败
		// 备注: 注意：如果起始时间和结束时间都为0,则取机器中全部记录。
		// ////////////////////////////////////////////////////////////////////////
		// apiReturn _stdcall VH_GetRecordNum(HANDLE hScanner, ReaderDate
		// *stime, ReaderDate *etime, int *nTotal);
		public short VH_GetRecordNum(int hScanner, ReaderDate stime,
                                     ReaderDate etime, int nTotal[]);

		// ////////////////////////////////////////////////////////////////////////
		// 用途: 获取时间段的记录
		// 函数名: VH_GetRecord
		// 功能: 取记录
		// 输入参数: hScanner 通信句柄
		// stime 为起始时间，etime 为结束时间
		// 输出参数: nTotal 为记录总数, nDiNum 为第几条，从0开始算, data 指记录指针
		// 返回值: 0--成功，其它失败
		// 备注: 注意：如果起始时间和结束时间都为0,则取机器中全部记录。
		// ////////////////////////////////////////////////////////////////////////
		// apiReturn _stdcall VH_GetRecord(HANDLE hScanner, ReaderDate *stime,
		// ReaderDate *etime, int *nTotal, int *nDiNum, BYTE * data);
		public short VH_GetRecord(int hScanner, ReaderDate stime,
                                  ReaderDate etime, int nTotal[], int nDiNum[], byte data[]);

		/* ans = first + second */
		// 实现两个16进制字符串的大数相加,如：
		// first:ABCDEF123456ABCDEF123456ABCDEF123456
		// second:ABCDEF123456ABCDEF123456ABCDEF123456
		// 结果存入 ans:1579BDE2468AD579BDE2468AD579BDE2468AC
		// void _stdcall VH_big_num_add(char *ans, char *first, char *second);
		// public void VH_big_num_add(StringBuilder ans, char first[], char
		// second[]);
		public void VH_big_num_add(byte[] ans, String first, String second);

		// 删除全部记录
		// apiReturn _stdcall DeleteAllRecord(HANDLE hScanner);
		public short DeleteAllRecord(int hScanner);

		public interface MyCallInterface extends Callback {
			short invoke(byte data[]);
		}

		// 高速获得记录
		// apiReturn _stdcall VH_GetRecordHigh(HANDLE hScanner,
		// VH_fGetRecordData fFunc);
		public short VH_GetRecordHigh(int hScanner, MyCallInterface fFunc);

		public interface MyCallInterfaceTest extends Callback {
			short invoke();
		}

		// 高速获得记录
		// apiReturn _stdcall VH_GetRecordHigh(HANDLE hScanner,
		// VH_fGetRecordData fFunc);
		public short VH_GetRecordHighTest(int hScanner,
                                          MyCallInterfaceTest fFunc);

		public interface MyCallInterfaceTestS extends Callback {
			short invoke(String Data);
		}

		// 高速获得记录
		// apiReturn _stdcall VH_GetRecordHigh(HANDLE hScanner,
		// VH_fGetRecordData fFunc);
		public short VH_GetRecordHighTestS(int hScanner,
                                           MyCallInterfaceTestS fFunc);

		// 增加名单
		// apiReturn _stdcall AddLableID(HANDLE hScanner, int Count, int Len,
		// BYTE * data);
		public short AddLableID(int hScanner, int Count, int Len, byte data[]);

		// 删除名单
		// apiReturn _stdcall DelLableID(HANDLE hScanner);
		public short DelLableID(int hScanner);

		// 增加单个或多个名单
		// apiReturn _stdcall DelSingleLableID(HANDLE hScanner, int Count, int
		// Len, BYTE * data);
		public short DelSingleLableID(int hScanner, int Count, int Len,
                                      byte data[]);

		// 获得名单
		// apiReturn _stdcall GetLableID(HANDLE hScanner, int startaddr, int
		// listlen, int *nTotal, int *DataLen, BYTE * data);
		public short GetLableID(int hScanner, int startaddr, int listlen,
                                int nTotal[], int DataLen[], byte data[]);

		// 提交名单
		// apiReturn _stdcall SaveLableID(HANDLE hScanner);
		public short SaveLableID(int hScanner);

	}

	// public static void VH_big_num_add(StringBuilder chixC, String chixA,
	// String chixB) {
	// // TODO Auto-generated method stub
	//
	// }

}
