<template>
  <div class="app-container">
    <h4 class="form-header h4">固件信息</h4>
    <el-form ref="firmwareForm" :model="firmwareForm" label-width="100px">
      <el-row>
        <el-col :span="7" :offset="0">
          <el-form-item label="文件名称" prop="apkName">
            <el-input v-model="firmwareForm.apkName" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="4" :offset="0">
          <el-form-item label="版本号" prop="version">
            <el-input  v-model="firmwareForm.version" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="9" :offset="0">
          <el-form-item label="下载地址" prop="downloadUrl">
            <el-input  v-model="firmwareForm.downloadUrl" disabled />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>


    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="归属组织" prop="deptId">
        <treeselect
          v-model="queryParams.deptId"
          :options="orgOptions"
          :show-count="true"
          placeholder="请选择归属组织"
          clearable
          style="width: 480px"
        />
      </el-form-item>
      <el-form-item label="设备名称" prop="devName">
        <el-input
          v-model="queryParams.devName"
          placeholder="请输入设备名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="设备状态" prop="devState">
        <el-select v-model="queryParams.devState" placeholder="请选择设备状态" clearable>
          <el-option
            v-for="dict in dict.type.sys_dev_state"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>


    <h4 class="form-header h4">设备信息</h4>
    <el-table v-loading="loading" :data="devInfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" prop="devId" width="120"/>
      <el-table-column label="所属组织" align="left"  prop="deptName"  :show-overflow-tooltip="true" />
      <el-table-column label="设备名称" align="left" prop="devName"  />
      <el-table-column label="设备序列号" align="center" prop="devSn" width="180"/>
      <el-table-column label="设备二维码" align="center" prop="  "  width="100" />
      <el-table-column label="设备地点" align="left" prop="devAddress"  width="450" />
      <el-table-column label="在线状态" align="center" prop="devState"  width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_dev_state" :value="scope.row.devState"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width"  >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:devInfo:edit']"
          >下发</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

  </div>
</template>

<script>

import { listDevInfo, updateDevInfo, addDevInfo,delDevInfo,getDevInfo,listDev} from "@/api/system/devInfo";
import {getFirmware,uploadFirmware} from "@/api/system/firmware";
import { treeselect } from "@/api/system/dept";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "devInfo",
  dicts: ['sys_dev_state',],
  components: { Treeselect },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 岗位表格数据
      postList: [],
      //设备序列号
      devSn:undefined,

      // 读者数据信息
      devInfoList: [],
      // 组织树选项
      orgOptions: [],
      // 设备序列号编辑禁用
      disabledInput: false,
      // 组织名称
      deptName: undefined,

      //固件信息
      firmwareForm:{},
      // 固件id
      id: "",


      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        deptId:undefined,
        devName: undefined,
        devAddress: undefined,
        devState: undefined
      },

      // 表单参数
      form: {},
      // 表单校验
      rules: {
        deptId: [
          { required: true, message: "归属组织不能为空", trigger: "blur" }
        ],
        devName: [
          { required: true, message: "设备名称不能为空", trigger: "blur" }
        ],
      }
    };
  },
  watch: {
    // 根据名称筛选组织树
    deptName(val) {
      this.$refs.tree.filter(val);
    }
  },

  created() {
    console.log("参数",this.$route.params);
    this.id = this.$route.params && this.$route.params.id;
    this.getList();
    this.getTreeSelect();
    //页面刚进入时开启长连接
    this.initWebSocket()
  },
  methods: {

    /** 下发操作 */
    handleUpdate(row) {
      let data = {devSn: row.devSn, downloadUrl: this.firmwareForm.downloadUrl, version:this.firmwareForm.version};
      this.loading = true;
      uploadFirmware(data).then(res => {
        // if (res.code === 200){
        //   this.loading = false;
        //   this.$message.error("升级成功");
        // }else{
        //     this.$message.error("升级失败");
        // }
      })
    },

    /** websocket 动态刷新*/
    initWebSocket(){ //初始化weosocket
      if(this.websocket != undefined){
        return;
      }
      const hostName = window.location.hostname;
      const port = window.location.port;
      // const wsUri = "ws://"+hostName+":16002"  + "/firmware/upload?uid=dev";//ws地址
      const wsUri = "ws://"+hostName+":16002/android"   + "?downloadFirmware";//ws地址
      console.log("firmware uri:"+ wsUri);
      this.websocket = new WebSocket(wsUri);
      this.websocket.onopen = this.webSocketOnopen;
      this.websocket.onerror = this.webSocketOnerror;
      this.websocket.onmessage = this.webSocketOnmessage;
      this.websocket.onclose = this.webSocketClose;
    },

    webSocketOnopen() {
      console.log("firmware WebSocket连接成功");
    },
    webSocketOnerror(e) { //错误
      console.log("firmware WebSocket连接发生错误:",e);
    },
    webSocketOnmessage(e){ //数据接收
      console.log("firmware websocket 数据接收",e);
      if(e.data.indexOf('action') === -1){
        //表单重载
        this.getList();
        return;
      }

      const data = JSON.parse(e.data);
      console.log("data",data);
      const action = data.action;
      const result = data.result;
      let msg = "";
      switch (action) {
        case "remote_upgrade":
          msg = "远程升级";
          break;
        // case "zy-remove-locker":
        //   msg = "删除配置";
        //   break;
        // case "zy-open-locker":
        //   msg = "远程开门";
        //   break;
        default:
          // msg = "终端操作";
          break;
      }
      if(msg != ""){
        if(result){
          this.loading = false;
          this.$modal.msgSuccess(msg + "成功");
        }else{
          this.loading = false;
          this.$modal.msgError(msg + "失败");
        }
      }

    },

    websocketsend(agentData){//数据发送
      //this.websock.send(agentData);
    },

    webSocketClose(e){ //触发关闭
      //CONNECTING：值为0，表示正在连接。
      //OPEN：值为1，表示连接成功，可以通信了。
      //CLOSING：值为2，表示连接正在关闭。
      //CLOSED：值为3，表示连接已经关闭，或者打开连接失败。
      console.log("firmware connection closed ",this.websocket.readyState);
      this.websocket = undefined;
      console.log("firmware connection closed ",e);
    },






    /** 查询设备列表 */
    getList() {
      this.loading = true;
      //查固件信息
      getFirmware(this.id).then(response => {
        this.firmwareForm = response.data;
      });
      listDev(this.queryParams).then(response => {
        this.devInfoList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },

    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        devId: undefined,
        devSn: undefined,
        devName: undefined,
        orgName: undefined,
        devType: undefined,
        devAddress: undefined,
        devState: "1",
        devRemark: undefined
      };
      this.resetForm("form");
    },
    /** 查询组织下拉树结构 */
    getTreeSelect() {
      treeselect().then(response => {
        this.orgOptions = response.data;
      });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.devId);
      this.single = selection.length!==1;
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.disabledInput = false;
      this.open = true;
      this.title = "添加书柜信息";
    },


    /** 提交按钮 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.devId !== undefined) {
            updateDevInfo(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addDevInfo(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const devIds = row.devId || this.ids;
      this.$modal.confirm('是否确认删除设备编号为"' + devIds + '"的数据项？').then(function() {
        return delDevInfo(devIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/devInfo/export', {
        ...this.queryParams
      }, `devInfo_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
