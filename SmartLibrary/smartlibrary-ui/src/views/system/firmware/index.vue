<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="文件名" prop="apkName">
        <el-input
          v-model="queryParams.apkName"
          placeholder="请输入apk文件名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="版本号" prop="version">
        <el-input
          v-model="queryParams.version"
          placeholder="请输入版本号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:firmware:add']"
        >上传</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:firmware:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:firmware:remove']"
        >删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="firmwareList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" prop="id" width="120" />
      <el-table-column label="文件名称" align="center" prop="apkName" width="200"  :show-overflow-tooltip="true" />
      <el-table-column label="版本号" align="center" prop="version" width="100" />
      <el-table-column label="下载地址" align="center" prop="downloadUrl"  :show-overflow-tooltip="true"/>
      <el-table-column label="升级日志" align="left" prop="updateLog"  :show-overflow-tooltip="true" />
      <el-table-column label="下载次数" align="center" prop="downloadCount" width="80" :show-overflow-tooltip="true" />
      <el-table-column label="上传时间" align="center" prop="uploadTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.uploadTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:firmware:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:firmware:remove']"
          >删除</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-share"
            @click="handleSend(scope.row)"
            v-hasPermi="['system:firmware:issue ']"
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

    <!-- 添加或修改固件信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body >
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">

<!--        <el-form-item label="上传文件:" prop="excel">-->
<!--          <el-upload-->
<!--            class="upload-demo"-->
<!--            ref="upload"-->
<!--            action&lt;!&ndash; 这里比填,异步时写后端接口,就可以,我们不用,所以不谢&ndash;&gt;-->
<!--          :http-request="httpRequest"&lt;!&ndash;覆盖默认的上传行为，可以自定义上传的实现&ndash;&gt;-->
<!--          :before-upload="beforeUpload"&lt;!&ndash;这是上传前的处理方法&ndash;&gt;-->
<!--          :on-exceed="handleExceed"&lt;!&ndash;文件超出个数限制时的钩子&ndash;&gt;-->
<!--          :limit="1">-->
<!--          <el-button slot="trigger" size="small" type="primary">选取文件</el-button>-->
<!--          <div slot="tip" class="el-upload__tip">只能上传.xlsx文件，且不超过5M</div>-->
<!--          </el-upload>-->
<!--        </el-form-item>-->

        <el-form-item label="升级日志" prop="updateLog">
          <el-input v-model="form.updateLog" type="textarea" placeholder="请输入升级日志" />
        </el-form-item>

        <el-form-item label="上传文件:" prop="downloadUrl">
          <el-upload
            ref="upload"
            :action="imgUpload.url"
            :headers="imgUpload.headers"
            :http-request="uploadFile"
            accept=".apk"
            :limit="limit"
            :auto-upload="true"
            :on-exceed="handleExceed"
            :on-success="handlePictureSuccess"
            :before-upload="beforeAvatarUpload"
            drag
          >
            <i class="el-icon-upload" ></i>
            <div class="el-upload__text" >将文件拖到此处，或<em>点击上传</em></div>
            <div class="el-upload__tip text-center" slot="tip" >
              <span>仅允许上传apk文件。</span>
            </div>
          </el-upload>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitData">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { listFirmware, updateFirmware, addFirmware, delFirmware, getFirmware, uploadImgFile} from "@/api/system/firmware";
import { getToken } from "@/utils/auth";
import Profile from "../user/profile/index";

export default {
  name: "firmware",
  components: {Profile},
  dicts: ['sys_reader_sex','sys_reader_state'],
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

      // 固件数据信息
      firmwareList: [],
      disabledInput:false,

      
      //图片详情弹窗显示状态
      dialogVisible:false,
      imgUpload: {
        // 设置上传的请求头部
        headers: {
          Authorization: "Bearer " + getToken()
        },
        // 上传的方法地址:
        url: process.env.VUE_APP_BASE_API + "/common",
      },
      //选中的文件
      File: undefined,
      //允许上传的图片数量
      limit: 1,



      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        apkName: undefined,
        version: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        rdIdNumber: [
          { required: true, message: "身份证号不能为空", trigger: "blur" }
        ],
        rdName: [
          { required: true, message: "姓名不能为空", trigger: "blur" }
        ],
        rdPhone: [
          { required: true, message: "手机号不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
    //页面刚进入时开启长连接
    this.initWebSocket();
  },
  methods: {

    // handleSend 下发
    handleSend(row) {
      this.$router.push("/firmware/manage/" + row.id);
    },



    //上传前
    beforeAvatarUpload(file) {
      console.info("beforeAvatarUpload",file);
      const isAPK = file.type === 'application/vnd.android.package-archive';
      if (!isAPK) {
        this.$message.error('上传文件只能是 APK 格式!');
      }
      return isAPK;
    },

    //图片上传成功后的回调
    handlePictureSuccess(res, file) {
      console.log("上传成功后的回调",file);
      //设置图片访问路径 （uploadUrl 后台传过来的的上传地址）
      // this.imageUrl = file.response.uploadUrl;
    },

    // 文件个数超出
    handleExceed() {
      this.$modal.msgError(`上传图片数量不能超过 ${this.limit} 个!`);
    },
    //自定义上传实现
    uploadFile(item) {
      console.log("自定义上传实现", item);
      this.File = item.file;
    },
    // // 文件上传中处理
    // handleFileUploadProgress(event, file, fileList) {
    //   this.upload.isUploading = true;
    // },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      this.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", { dangerouslyUseHTMLString: true });
      this.getList();
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
    },
    //移除已选择的图片
    handleRemove(file, fileList) {
      console.log(file, fileList);
      this.File = undefined;
      //删除已上传的图片文件
      // delImgFile({"path":file.response.uploadUrl}).then(response => {
      //   console.log("handleRemove",response)
      // });

    },

    /** 提交按钮 */
    submitData: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if(this.File !== undefined){
            console.log("file===========");
            // 开始上传文件 新建一个formData
            const formData = new FormData();
            // 通过append向form对象添加数据
            formData.append("file", this.File);
            let log = this.form.updateLog;
            console.log("updateLog==========="+log);
            uploadImgFile(formData).then(response => {
              // console.info("updateLog==========="+log);
              if(response.code === 200){
                // this.form.studentFeature.photo = response.uploadUrl;
                this.form.downloadUrl = response.uploadUrl;
                console.log(this.form);
                this.$modal.loading("正在上传文件，请稍候...");
                // this.submitDat();
              }else{
                this.$modal.msgError(response.msg);
              }
            })
          }
        }
      });
    },

    submitDat(){
      console.info("submitData",this.form );
      if (this.form.id == null || this.form.id == undefined){
        addFirmware(this.form).then(response => {
          if (response.code === 200){
            this.$modal.msgSuccess("添加成功");
            this.open = false;
            this.getList();
            this.$refs.upload.clearFiles();
          }else{
            this.$message.error(response.msg);
          }
        });
      }else{// 更新人员信息
        updateFirmware(this.form).then(response => {
          this.$modal.msgSuccess("更新成功");
          this.open = false;
          this.getList();
        });
      }
    },
















    // 表单重置
    reset() {
      this.form = {
        updateLog: undefined,
        downloadUrl: undefined,
      };
      this.fileList = [];
      this.File = undefined;
      this.resetForm("form");
    },




    /** 查询读者列表 */
    getList() {
      this.loading = true;
      listFirmware(this.queryParams).then(response => {
        this.firmwareList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },

    // /** 下发 */
    // handleSend(row){
    //     this.$router.push("/scBs/dev-cell/manage/" + row.devSn);
    // },

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
    /** 多选框选中数据 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id);
      this.single = selection.length!==1;
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.disabledInput = false;
      this.title = "上传固件信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getFirmware(id).then(response => {
        this.form = response.data;
        this.disabledInput = true;
        this.open = true;
        this.title = "修改固件信息";
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除序号为"' + ids + '"的数据项？').then(function() {
        return delFirmware(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },

    /** 提交按钮 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id !== undefined) {
            updateFirmware(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addFirmware(this.form).then(response => {
              this.$modal.msgSuccess("上传成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },



    /** 导出按钮操作 */
    handleExport() {
      this.download('system/reader/export', {
        ...this.queryParams
      }, `reader_${new Date().getTime()}.xlsx`)
    },


    /** websocket 动态刷新*/
    initWebSocket(){ //初始化weosocket
      if(this.websocket != undefined){
        return;
      }
      const hostName = window.location.hostname;
      const port = window.location.port;
      // const wsUri = "ws://"+hostName+":16002"  + "/firmware/upload?uid=dev";//ws地址
      const wsUri = "ws://"+hostName+":16002/android"   + "?uploadFile";//ws地址
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
      this.$modal.closeLoading();
      if(e.data.indexOf('action') === -1){
        //表单重载
        this.getList();
        return;
      }
      const data = JSON.parse(e.data);
      console.log("data",data);
      const action = data.action;
      const result = data.code;
      let msg = data.msg;

      if(result === 0){
        this.loading = false;
        this.$modal.msgSuccess(msg);
        //提交表单数据，保存数据至数据库
        this.submitDat();
      }else{
        this.loading = false;
        this.$modal.msgError(msg);
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
  }
};
</script>
