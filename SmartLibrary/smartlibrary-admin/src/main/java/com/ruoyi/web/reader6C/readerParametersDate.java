package com.ruoyi.web.reader6C;

import java.util.HashMap;

public class readerParametersDate {
	public static HashMap<String, Object> newHashMap = new HashMap<String, Object>();
	public static HashMap<String, Object> oldHashMap = new HashMap<String, Object>();
	public static Object readNewDate(String key){
		return newHashMap.get(key);
	}
	public static void setNewDate(String key,Object val){
		if(key.equals("LetNull")){
			//如果是LetNull则清空NewHashMap中的所有内容；
			newHashMap.clear();
		}else{			
			newHashMap.put(key, val);
		}
	}
	public static void setOldDate(String key,Object val){
		if(key.equals("LetNull")){
			//如果是LetNull表示清空OldHashMap中的多有内容；
			oldHashMap.clear();
		}else{
			oldHashMap.put(key, val);
		}
	}
	public static Object readOldDate(String key){
		Object Obj = oldHashMap.get(key);
		return Obj;
	}
	
}


/*key val 对应，所有val都是String类型，获取时候需要有相应的转化*/
//tagTypes对应Tag types to be read: 4代表6B，1代表6C；
//DataFormat对应Date Format;
//DateArea对应Date Area;
//MinFrequencyMHZ
//MaxFrequencyMHZ
//RFPowerOutput对应RF Power Output发射功率
//PrefixCheckBox对应Prefix值为1表示选中，0表示没有选中
//PerfixCodeValue对应Perfix value
//StartAddress对应StartAddress值；
//DateAddressLength对应DateAddress下的Length的值
//OutputInterval对应于Standard output interval的值
//EnterAndWrap对应enter and wrap,对应dll下的IsEnter，是否回车换行, 1--带,0--不带	
//IntervalOfReadingTag对应interval of reading tag的下标，对应dll中的Interval，0-10ms，1-20ms，2-30ms，3-50ms，4-100ms。缺省值为2。每隔设定时间主动读取一次标签。