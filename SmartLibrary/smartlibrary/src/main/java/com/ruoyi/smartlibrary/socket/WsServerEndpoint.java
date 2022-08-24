package com.ruoyi.smartlibrary.socket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.WebSocketConstants;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.Threads;
import com.ruoyi.smartlibrary.pojo.*;
import com.ruoyi.smartlibrary.service.*;
import org.apache.catalina.connector.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

//访问时的路径
@ServerEndpoint("/android")
@Component
public class WsServerEndpoint {

    private static IDevInfoService devInfoService;
    private static IBorrowService borrowService;
    private static IBookInfoService bookInfoService;
    private static IAlarmInfoService alarmInfoService;
    private static IReaderInfoService readerInfoService;
    private static ILoginRecordService loginRecordService;
    private static IRecordHistoryService recordHistoryService;
    private static IFirmwareRecordService firmwareRecordService;

    @Autowired
    public void setStudentService (IDevInfoService devInfoService,
                                   IBorrowService borrowService,
                                   IBookInfoService bookInfoService,
                                   IAlarmInfoService alarmInfoService,
                                   IReaderInfoService readerInfoService,
                                   ILoginRecordService loginRecordService,
                                   IRecordHistoryService recordHistoryService,
                                   IFirmwareRecordService firmwareRecordService)
    {
        WsServerEndpoint.devInfoService = devInfoService;
        WsServerEndpoint.borrowService = borrowService;
        WsServerEndpoint.bookInfoService = bookInfoService;
        WsServerEndpoint.alarmInfoService = alarmInfoService;
        WsServerEndpoint.readerInfoService = readerInfoService;
        WsServerEndpoint.loginRecordService = loginRecordService;
        WsServerEndpoint.recordHistoryService = recordHistoryService;
        WsServerEndpoint.firmwareRecordService = firmwareRecordService;

    }


   private static final Logger log = LoggerFactory.getLogger(WsServerEndpoint.class);
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    private String message="";
    /** 终端通信sessionid devSn 保存记录拿到设备序列号 */
    private static ConcurrentHashMap<String,String> webSocketMap = new ConcurrentHashMap<>();
    /** 保存devSn Session 跟指定设备通信 */
    public static ConcurrentHashMap<String, Session> concurrentHashMap = new ConcurrentHashMap<>();
    /** 与前端通信  sessionId 与 uid */
    public static ConcurrentHashMap<String, String> webSocketSessionIdUid = new ConcurrentHashMap<String, String>(2 );
    /** uid 与 session  通过session跟前端通信 */
    public static ConcurrentHashMap<String, Session> webSocketSessionMap = new ConcurrentHashMap<String, Session>(2);


    /** 给终端发消息 session:与设备对应的 */
    public Boolean upload(JSONObject jsonObject){
        String devSn = jsonObject.getString("devSn");
        String downloadUrl = jsonObject.getString("downloadUrl");
        String version = jsonObject.getString("version");
        System.out.println(devSn+"---"+downloadUrl+"----"+ version);
        Session session = concurrentHashMap.get(devSn);
        if(null != session){
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("action","remote_upgrade");
            jsonObj.put("version",version);
            jsonObj.put("url",downloadUrl);
            System.out.println(devSn+"---"+downloadUrl+"----"+version);
            try {
                // 发消息给终端升级
                session.getBasicRemote().sendText(jsonObj.toJSONString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("更新信息已发送终端");
            return true;
        }else {
            System.out.println("session为空");
            return false;
        }
    }

    //往前端发送信息
    public static void sendToWebView(String msg, String uid) {
        if(concurrentHashMap.size()> 0){
            Session socketSession = webSocketSessionMap.get(uid);
            if(socketSession != null){
                try {
                     log.info("向WEB页面发送消息" + msg);
                    socketSession.getBasicRemote().sendText(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                String sessionPath = socketSession.getUri().getPath();
//                if (sessionPath.contains(path)) {
//                    try {
////                        log.info(path + "向WEB页面发送消息" + msg);
//                        socketSession.sendMessage(new TextMessage(msg));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
            }
        }
    }



    @OnOpen
    public void onOpen(Session session, EndpointConfig config){
        this.session = session;
        log.info("========>建立连接"+ session);
        String uid = session.getRequestURI().getQuery();
        System.out.println(session.getId() + "open..." + uid);
            // 前端页面的Websocket连接
        if ("uploadFile".equals(uid) || "downloadFirmware".equals(uid)){
            webSocketSessionIdUid.put(session.getId(),uid);
            webSocketSessionMap.put(uid, session);
        }
        return ;
    }


//    @OnOpen  运行有问题
//    public void onOpen(Session session, EndpointConfig config, WebSocketSession webSocketSession){
//        this.session = session;
////        log.info("========>建立连接"+ session);
//        System.out.println(session.getId() + "open...");
//
//        String url = webSocketSession.getUri().getPath();
//        System.out.println("url____url_____" + url);
//        if(url.contains("firmware/upload")){
//            // 前端页面的Websocket连接
//            String uid = (String) webSocketSession.getAttributes().get("uid");
//            webSocketSessionIdUid.put(webSocketSession.getId(),uid);
//            webSocketSessionMap.put(uid, webSocketSession);
//            return ;
//        }
//    }


    @OnError
    public void onError(Session session,Throwable throwable){
        webSocketMap.remove(message);
        log.info("socket连接断开或异常error："+throwable.getMessage());
        System.out.println("error : " + throwable.getMessage());
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason){

        String devSn = webSocketMap.get(session.getId());

        if (StringUtils.isNotNull(devSn)){
            //设备离线  新增离线报警记录  设备状态更新
            DevInfo devInfo = devInfoService.selectDevSn(devSn);
            if(null == devInfo){
                AlarmInfo alarmInfo = new AlarmInfo();
                alarmInfo.setAlarmTime(new Date());
                alarmInfo.setDevName(devInfo.getDevName());
                alarmInfo.setAlarmType("2");
                alarmInfo.setDevSn(devSn);
                alarmInfo.setDevName(devInfo.getDevName());
                alarmInfoService.insert(alarmInfo);
                devInfo.setDevState("1");
                devInfoService.updateDevInfo(devInfo);
            }
        }

        log.info(session.getId() + "=========>关闭连接"+ devSn);
        log.info(session.getId() + "=========>关闭连接"+ closeReason);
        webSocketMap.remove(session.getId());
        System.out.println(session.getId() + "close..." + closeReason);
    }

    @OnMessage
    public String onMessage(String message,Session session){

        System.out.println("终端通信session : " + session);
        System.out.println("onMessage : " + message);
        String returnStr = null;
        JSONObject jsonObject = new JSONObject();
        if (StringUtils.isBlank(message)){
            log.error("收到的消息为空");
            jsonObject.put("msg","收到的消息为空");
            return jsonObject.toJSONString();
        }
        JSONObject msgJSONObject = JSONObject.parseObject(message);
        if (null == msgJSONObject){
            log.error("收到信息错误,可能是JSON格式有问题");
            jsonObject.put("msg","收到信息错误,可能是JSON格式有问题");
            return jsonObject.toJSONString();
        }

        String action = msgJSONObject.getString("action");

        if(StringUtils.isNotBlank(action)) {
            this.message = message;
            returnStr = handleAction(action, msgJSONObject, session);
        }else {
            jsonObject.put("msg","action为空");
            return jsonObject.toJSONString();
        }
        return returnStr;
    }




    /**
     * 收到消息后要做的操作
     * @param action
     * @param msgJSONObject
     * @param session
     */
    private String handleAction(String action, JSONObject msgJSONObject, Session session)
    {
        JSONObject jsonObject = new JSONObject();

        // 远程升级返回的消息
        if ("remote_upgrade".equals(action)) {
            jsonObject.put("action","remote_upgrade");
            if (-1 != msgJSONObject.getInteger("code")){
                //更新成功
                jsonObject.put("result",true);
//                sendToWebView(jsonObject.toJSONString(),"/firmware/upload", "dev");
                sendToWebView(jsonObject.toJSONString(),"downloadFirmware");
            }else {
                //更新失败
                jsonObject.put("result",false);
//                sendToWebView(jsonObject.toJSONString(),"/firmware/upload", "dev");
                sendToWebView(jsonObject.toJSONString(),"downloadFirmware");
            }
        }


        //设备注册
        if (WebSocketConstants.DEV_REGISTER.equals(action)){
            String devSn = msgJSONObject.getString("devSn");
            DevInfo devInfo = devInfoService.selectDevSn(devSn);
            if (null != devInfo) {
                devInfoService.updateDevState(devSn);
                jsonObject.put("action","register");
                jsonObject.put("code",0);
                jsonObject.put("address",devInfo.getDevAddress());
                webSocketMap.put(session.getId(),devSn);
                concurrentHashMap.put(devSn,session);
                return jsonObject.toJSONString();
            }else {
                jsonObject.put("action","register");
                jsonObject.put("code",-1);
                jsonObject.put("msg","该设备未注册");
                return jsonObject.toJSONString();
            }
        }

        //心跳
        if(WebSocketConstants.DEV_HEARTBEAT.equals(action)){
            jsonObject.put("action","heartbeat");
            return jsonObject.toJSONString();
        }

        //获取图书信息
        if(WebSocketConstants.DEV_BOOK_INFO.equals(action))
        {
            List<BookInfo> list = new ArrayList<>();
            String bodyJson = msgJSONObject.getString("body");
            if(StringUtils.isNotBlank(bodyJson)) {
                List<JSONObject> jsonObjectList = JSON.parseArray(bodyJson, JSONObject.class);
                for (JSONObject object : jsonObjectList) {
                    if (null != object){
                        //获取书籍信息
                        BookInfo bookInfo = bookInfoService.selectByBkRfid(object.getString("bkRfid"));
                            if(null != bookInfo){
                                list.add(bookInfo);
                            }
                    }else {
                        jsonObject.put("action","book_detail_info");
                        jsonObject.put("msg","body格式异常");
                        return jsonObject.toJSONString();
                    }
                }
                jsonObject.put("action","book_detail_info");
                jsonObject.put( "body",list);
                return jsonObject.toJSONString();
            }
            jsonObject.put("action","book_detail_info");
            jsonObject.put("msg","body为空");
            return jsonObject.toJSONString();
        }

        // 上传借还记录
        if (WebSocketConstants.UPLOAD_BR_RECORD.equals(action)) {
            String bodyJson = msgJSONObject.getString("body");
            String header = msgJSONObject.getString("header");
            JSONObject headerObj = JSON.parseObject(header);
            if(StringUtils.isNotBlank(bodyJson)) {
                Borrow borrow;
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date;
                int update;
                int insert;
                List<String> list = new ArrayList<>();
                List<JSONObject> jsonObjectList = JSON.parseArray(bodyJson, JSONObject.class);
                for (JSONObject object : jsonObjectList) {
                    borrow = new Borrow();
                    if (null != object){
                        try {
                            date = simpleDateFormat.parse(object.getString("time"));
                            String bkRfid = object.getString("bkRfid");
                            borrow.setBkRfid(bkRfid);
                            BookInfo bookInfo = bookInfoService.selectByBkRfid(bkRfid);
                            borrow.setBkName(bookInfo.getBkName());
                            //设备序列号
                            borrow.setDevSn(webSocketMap.get(session.getId()));
                            //设备名
                            borrow.setDevName(devInfoService.selectDevSn(webSocketMap.get(session.getId())).getDevName());
                            if ("borrow".equals(object.getString("type"))){
                                borrow.setBkIsbn(object.getString("bkISBN"));
                                borrow.setBorrowTime(date);
                                borrow.setBkState("0");
                                borrow.setBkOvertime("0");
                                //借书后设置应还时间为30天后
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(date);
                                calendar.add(Calendar.DATE, 30);
                                borrow.setShouldBackTime(calendar.getTime());
                                borrow.setRdName(object.getString("rdName"));
                                borrow.setRdIdNumber(object.getString("rdIdNumber"));
                                //存入数据库
                                insert = borrowService.insert(borrow);
                                //存入失败
                                if (1 != insert){
                                    list.add(borrow.getBkIsbn());
                                    jsonObject.put("bkISBN",list);
                                    jsonObject.put("code",-1);
//                                    borrow = null;
                                }else {
                                    jsonObject.put("code",0);

                                }
                                borrow = null;
                            }else if("return".equals(object.getString("type"))){
                                borrow.setBkState("1");
                                borrow.setBkIsbn(object.getString("bkISBN"));
                                borrow.setReturnTime(date);
                                // 归还人信息
                                borrow.setBkName(object.getString("rdName"));
                                borrow.setBackIdNumber(object.getString("rdIdNumber"));
                                //根据RFID更新借阅状态
                                System.out.println("----要归还的书籍是-----"+ borrow.toString());
                                update = borrowService.updateBorrow(borrow);
                                //更新失败
                                if (1 != (update)){
                                    list.add(borrow.getBkIsbn());
                                    jsonObject.put("bkISBN",list);
                                    jsonObject.put("code",-1);
                                }else {
                                    jsonObject.put("code",0);
                                    //返回借阅时间，指定某一个借阅记录
                                }
                                borrow = null;
                            }
                        }catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }else {
                        list.add(object.getString("bkISBN"));
                        jsonObject.put("action","upload_BR_record");
                        jsonObject.put("msg", "body格式错误");
                        jsonObject.put("code",-1);
                        jsonObject.put("bkISBN",list);
                        return jsonObject.toJSONString();
                    }
                }
                jsonObject.put("header",headerObj);
                jsonObject.put("action","upload_BR_record");
                return jsonObject.toJSONString();
            }
            jsonObject.put("action","upload_BR_record");
            jsonObject.put("msg", "body为空");
            return jsonObject.toJSONString();
        }

        // 续借
        if (WebSocketConstants.UPLOAD_RENEW.equals(action)) {
            String bodyJson = msgJSONObject.getString("body");
            if(StringUtils.isNotBlank(bodyJson)) {
                Borrow borrow = new Borrow();
                List<String> list = new ArrayList<>();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                List<JSONObject> jsonObjectList = JSON.parseArray(bodyJson, JSONObject.class);
                for (JSONObject object : jsonObjectList) {
                    if (null != object){
                        String bkISBN = object.getString("bkISBN");
                        Date renewTime =null;
                        try {
                            renewTime = simpleDateFormat.parse(object.getString("time"));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        borrow.setBkIsbn(bkISBN);
                        borrow.setRenewTime(renewTime);
                        //去续借
                        int i = borrowService.bkRenew(borrow);
                        if (0 == i){
                            //续借失败，已经续借过了
                            jsonObject.put("action","upload_renew_record");
                            jsonObject.put("bkISBN",bkISBN);
                            jsonObject.put("code",1);
                            return jsonObject.toJSONString();
                        }else {
                            list.add(bkISBN);
                        }
                    }else {
                        jsonObject.put("action","upload_renew_record");
                        jsonObject.put("msg","body格式异常");
                        return jsonObject.toJSONString();
                    }
                }
                jsonObject.put("action","upload_renew_record");
                //续借成功，返回ISBN
                jsonObject.put("bkISBN",list);
                jsonObject.put("code",0);
                return jsonObject.toJSONString();
            }
            jsonObject.put("action","upload_renew_record");
            jsonObject.put("msg","body为空");
            return jsonObject.toJSONString();
        }

        //上传报警记录
        if(WebSocketConstants.UPLOAD_ALARM.equals(action)){
            String bodyJson = msgJSONObject.getString("body");
            String header = msgJSONObject.getString("header");
            JSONObject headerObj = JSON.parseObject(header);
            if(StringUtils.isNotBlank(bodyJson)) {
                AlarmInfo alarmInfo = new AlarmInfo();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date= null;
                List<JSONObject> jsonObjectList = JSON.parseArray(bodyJson, JSONObject.class);
                for (JSONObject object : jsonObjectList) {
                    if (null != object){
                        try {
                            date = simpleDateFormat.parse(object.getString("time"));
                            alarmInfo.setRdName(object.getString("rdName"));
                            alarmInfo.setRdIdNumber(object.getString("rdIdNumber"));
                            alarmInfo.setAlarmTime(date);
                            alarmInfo.setAlarmType("0");
                            alarmInfo.setDevSn(webSocketMap.get(session.getId()));
                            DevInfo devInfo = devInfoService.selectDevSn(webSocketMap.get(session.getId()));
                            alarmInfo.setDevName(devInfo.getDevName());
                            alarmInfo.setDeptId(devInfo.getDeptId());
                            alarmInfo.setDeptName(devInfo.getDeptName());
                            alarmInfoService.insert(alarmInfo);
                        } catch (ParseException e) {

                            e.printStackTrace();
                        }
                    }else {
                        jsonObject.put("action","upload_renew_record");
                        jsonObject.put("msg","body格式异常");
                        return jsonObject.toJSONString();
                    }
                }
                jsonObject.put("action","upload_alarm_record");
                jsonObject.put("code",0);
                jsonObject.put("header",headerObj);
                return jsonObject.toJSONString();
            }
            jsonObject.put("action","upload_alarm_record");
            jsonObject.put("msg","body为空");
            return jsonObject.toJSONString();
        }


        //上传开门记录
        if("upload_open_record".equals(action)){
            String bodyJson = msgJSONObject.getString("body");
            String header = msgJSONObject.getString("header");
            JSONObject headerObj = JSON.parseObject(header);
            if(StringUtils.isNotBlank(bodyJson)) {
                List<JSONObject> jsonObjectList = JSON.parseArray(bodyJson, JSONObject.class);
                for (JSONObject object : jsonObjectList) {
                    if (null != object){
                        RecordHistory recordHistory = new RecordHistory();
                        recordHistory.setPersonName( object.getString("rdName"));
                        recordHistory.setCardNo(object.getString("rdIdNumber"));
                        String result = object.getString("result");
                        if ("true".equals(result)){
                            recordHistory.setOpenResult("0");
                        }else {
                            recordHistory.setOpenResult("1");
                        }
                        Date time = null;
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        try {
                            time = simpleDateFormat.parse(object.getString("time"));
                        } catch (ParseException e) {
                            log.info("--------------上传开门记录失败--------");
                            e.printStackTrace();
                        }
                        recordHistory.setOpenTime(time);
                        //设备序列号
                        recordHistory.setDevSn(webSocketMap.get(session.getId()));
                        //设备名
                        recordHistory.setDevName(devInfoService.selectDevSn(webSocketMap.get(session.getId())).getDevName());
                        //登录方式
                        recordHistory.setOpenMode(webSocketMap.get(webSocketMap.get(session.getId())));
                        recordHistoryService.insert(recordHistory);
                    }else {
                        jsonObject.put("action","upload_open_record");
                        jsonObject.put("code" ,1);
                        jsonObject.put("msg","body格式异常");
                        return jsonObject.toJSONString();
                    }
                }
                jsonObject.put("action","upload_open_record");
                jsonObject.put("code" ,0);
                jsonObject.put("header", headerObj);
                return jsonObject.toJSONString();
            }
            jsonObject.put("action","upload_open_record");
            jsonObject.put("code" ,1);
            jsonObject.put("msg","body为空");
            return jsonObject.toJSONString();
        }


        // 获取读者借阅记录
        if(WebSocketConstants.READER_BORROW.equals(action)){
            String bodyJson = msgJSONObject.getString("body");
            List<Borrow> list =  new ArrayList<>();
            if(StringUtils.isNotBlank(bodyJson)) {
                List<JSONObject> jsonObjectList = JSON.parseArray(bodyJson, JSONObject.class);
                for (JSONObject object : jsonObjectList) {
                    if (null != object){
                        Borrow borrow = new Borrow();
                        String rdName = object.getString("rdName");
                        String rdIdNumber = object.getString("rdIdNumber");
                        borrow.setRdName(rdName);
                        borrow.setRdIdNumber(rdIdNumber);
                        list = borrowService.readerBorrowList(borrow);
                    }else {
                        jsonObject.put("action","reader_borrow_detail");
                        jsonObject.put("msg","body格式异常");
                        return jsonObject.toJSONString();
                    }
                }
                jsonObject.put("action","reader_borrow_detail");
                jsonObject.put("body", list);
                return jsonObject.toJSONString();
            }
            jsonObject.put("action","reader_borrow_detail");
            jsonObject.put("msg","body为空");
            return jsonObject.toJSONString();
        }


        //  登录请求
        if(WebSocketConstants.READER_LOGIN.equals(action)){
            String bodyJson = msgJSONObject.getString("body");
            if(StringUtils.isNotBlank(bodyJson)) {
                JSONObject object = JSON.parseObject(bodyJson);
                if (null != object){
                    String rdName = object.getString("rdName");
                    String rdIdNumber = object.getString("rdIdNumber");
                    String rdPassword = object.getString("rdPassword");
                    String rdPhone = object.getString("rdPhone");
                    ReaderInfo queryInfo = new ReaderInfo();
                        // 密码为空，可能是刷卡登录
                        if (StringUtils.isBlank(rdPassword)){
                            queryInfo.setRdName(rdName);
                            queryInfo.setRdIdNumber(rdIdNumber);
                            queryInfo.setRdState("0");
                            ReaderInfo readerInfo= readerInfoService.selectReaderInfo(queryInfo);
                            //查询结果不为空
                            if(null != readerInfo){
                                /** 查询是否存在超期书籍 */
                                List<Borrow> borrowList = borrowService.selectBorrowState(readerInfo);
                                if (null != borrowList){
                                    boolean flag = false;
                                    // 存在未还书籍
                                    for (Borrow borrow : borrowList) {
                                        //判断是否存在超期
                                        int i = borrow.getShouldBackTime().compareTo(new Date());
                                        if (0 > i){
                                            // 存在超期书籍不能登录
                                            flag = true;
                                            break;
                                        }else {
                                            //不存在超期书籍
                                            flag = false;
                                        }
                                    }
                                    if (flag){
                                            jsonObject.put("action","reader_login");
                                            jsonObject.put("code","-3");
                                            jsonObject.put("msg","存在超期未还书籍，请联系管理员处理！");
                                            return jsonObject.toJSONString();
                                    }else {
                                            /** 去新增登录记录 */
                                            return addLoginRecord(jsonObject, rdName, rdIdNumber, object, readerInfo);
                                    }
                                }else {
                                    //不存在未还书籍
                                    /** 去新增登录记录 */
                                    return addLoginRecord(jsonObject, rdName, rdIdNumber, object, readerInfo);
                                }
                            }else {
                                // 未注册
                                jsonObject.put("action","reader_login");
                                jsonObject.put("code","-2");
                                return jsonObject.toJSONString();
                            }
                         //密码不为空，密码登录
                        }else if(StringUtils.isNotBlank(rdPassword)){
                            queryInfo.setRdPassword(rdPassword);
                            queryInfo.setRdPhone(rdPhone);
                            queryInfo.setRdState("0");
                            ReaderInfo readerInfo = readerInfoService.selectReaderInfo(queryInfo);
                            if(null != readerInfo){
                                /** 查询是否存在超期书籍 */
                                List<Borrow> borrowList = borrowService.selectBorrowState(readerInfo);
                                if (null != borrowList){
                                    boolean flag = false;
                                    // 存在未还书籍
                                    for (Borrow borrow : borrowList) {
                                        //判断是否存在超期
                                        int i = borrow.getShouldBackTime().compareTo(new Date());
                                        if (0 > i){
                                            // 存在超期书籍不能登录
                                            flag = true;
                                            break;
                                        }else {
                                            //不存在超期书籍
                                            flag = false;
                                        }
                                    }
                                    if (flag){
                                        jsonObject.put("action","reader_login");
                                        jsonObject.put("code","-3");
                                        jsonObject.put("msg","存在超期未还书籍，请联系管理员处理！");
                                        return jsonObject.toJSONString();
                                    }else {
                                        /** 去新增登录记录 */
                                        return addLoginRecord(jsonObject, rdName, rdIdNumber, object, readerInfo);
                                    }
                                }else {
                                    //不存在未还书籍
                                    /** 去新增登录记录 */
                                    return addLoginRecord(jsonObject, rdName, rdIdNumber, object, readerInfo);
                                }
                            }else {
                                // 未注册
                                jsonObject.put("action","reader_login");
                                jsonObject.put("code","-2");
                                return jsonObject.toJSONString();
                            }
                        }
                }else {
                    jsonObject.put("action","reader_login");
                    jsonObject.put("msg","body格式异常");
                    return jsonObject.toJSONString();
                }
            }else{
                jsonObject.put("action","reader_login");
                jsonObject.put("msg","body为空");
                return jsonObject.toJSONString();
            }
        }



        //读者注册
        if(WebSocketConstants.READER_REGISTER.equals(action)){
            String bodyJson = msgJSONObject.getString("body");
            if(StringUtils.isNotBlank(bodyJson)) {
                JSONObject object = JSON.parseObject(bodyJson);
                ReaderInfo readerInfo = new ReaderInfo();
                    if (null != object){
                        readerInfo.setRdName(object.getString("rdName"));
                        readerInfo.setRdIdNumber(object.getString("rdIdNumber"));
                        readerInfo.setRdPhone(object.getString("rdPhone"));
                        readerInfo.setRdPassword(object.getString("rdPassword"));
                        readerInfo.setRdSex(object.getString("rdSex"));
                        try {
                            //查询设备所在组织，读者与该组织关联
                            String devSn = webSocketMap.get(session.getId());
                            DevInfo devInfo = devInfoService.selectDevSn(devSn);
                            readerInfo.setDeptId(devInfo.getDeptId());
                            readerInfo.setDeptName(devInfo.getDeptName());
                            ReaderInfo info = readerInfoService.selectReaderInfo(readerInfo);
                            if (null == info){
                                readerInfoService.insert(readerInfo);
                            }else {
                                readerInfo.setRdState("0");
                                readerInfoService.updateReaderInfo(readerInfo);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            jsonObject.put("action","reader_register");
                            jsonObject.put("code","-1");
                            return jsonObject.toJSONString();
                        }
                    }else {
                        jsonObject.put("action","reader_register");
                        jsonObject.put("msg","body格式异常");
                        return jsonObject.toJSONString();
                    }
                jsonObject.put("action","reader_register");
                jsonObject.put("code","0");
                return jsonObject.toJSONString();
            }
            jsonObject.put("action","reader_register");
            jsonObject.put("msg","body为空");
            return jsonObject.toJSONString();
        }

        //保存固件升级记录
        if("upload_firmware_record".equals(action)){
            String header = msgJSONObject.getString("header");
            String bodyJson = msgJSONObject.getString("body");
            JSONObject object = JSON.parseObject(bodyJson);
            FirmwareRecord firmwareRecord = new FirmwareRecord();
            firmwareRecord.setOldVersion(object.getString("localVersion"));
            firmwareRecord.setNewVersion(object.getString("downloadVersion"));
            String result = object.getString("result");
            if ("true".equals(result)){
                firmwareRecord.setUpdateResult("0");
            }else {
                firmwareRecord.setUpdateResult("1");
            }
            Date time = null;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                time = simpleDateFormat.parse(object.getString("time"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            firmwareRecord.setUpgradeTime(time);
            String devSn = webSocketMap.get(session.getId());
            DevInfo devInfo = devInfoService.selectDevSn(devSn);
            firmwareRecord.setDevSn(devSn);
            firmwareRecord.setDevName(devInfo.getDevName());
            firmwareRecord.setDeptId(devInfo.getDeptId());
            firmwareRecord.setDeptName(devInfo.getDeptName());
            // 新增升级记录
            int insert = firmwareRecordService.insert(firmwareRecord);
            jsonObject.put("header",header);
            jsonObject.put("action","upload_firmware_record");
            if (1 != insert){
                jsonObject.put("code",0);
            }else {
                jsonObject.put("code",1);
            }
            return jsonObject.toJSONString();
        }







        jsonObject.put("action","null");
        jsonObject.put("msg","未找到对应操作");
        return jsonObject.toJSONString();
    }



    /** 新增登录记录 */
    private String addLoginRecord(JSONObject jsonObject,String rdName,String rdIdNumber,JSONObject object,ReaderInfo readerInfo){
        LoginRecord loginRecord = new LoginRecord();
        loginRecord.setRdName(rdName);
        loginRecord.setRdIdNumber(rdIdNumber);
        loginRecord.setRdPhone(object.getString("rdPhone"));
        loginRecord.setLoginTime(new Date());
        String type = object.getString("type");
        loginRecord.setLoginType(type);
        //登录方式保存下来，开门记录要用
        String devSn = webSocketMap.get(session.getId());
        webSocketMap.put(devSn,type);
        loginRecord.setDevSn(devSn);
        loginRecord.setDevName(devInfoService.selectDevSn(devSn).getDevName());
        // 新增登录记录
        if (0 < loginRecordService.insert(loginRecord)){
            jsonObject.put("action","reader_login");
            jsonObject.put("code","0");
            jsonObject.put("body",readerInfo);
            return jsonObject.toJSONString();
        }else {
            jsonObject.put("action","reader_login");
            jsonObject.put("code","0");
            jsonObject.put("msg","新增登录记录失败");
            jsonObject.put("body",readerInfo);
            return jsonObject.toJSONString();
        }
    }



}

