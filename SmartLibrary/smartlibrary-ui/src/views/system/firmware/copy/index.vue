<!--<template>-->
<!--  <div class="app-container">-->
<!--    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">-->
<!--      <el-form-item label="文件名" prop="apkName">-->
<!--        <el-input-->
<!--          v-model="queryParams.apkName"-->
<!--          placeholder="请输入apk文件名"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
<!--      <el-form-item label="版本号" prop="version">-->
<!--        <el-input-->
<!--          v-model="queryParams.version"-->
<!--          placeholder="请输入版本号"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
<!--      <el-form-item>-->
<!--        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>-->
<!--        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>-->
<!--      </el-form-item>-->
<!--    </el-form>-->

<!--    <el-row :gutter="10" class="mb8">-->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="primary"-->
<!--          plain-->
<!--          icon="el-icon-plus"-->
<!--          size="mini"-->
<!--          @click="handleAdd"-->
<!--          v-hasPermi="['system:firmware:add']"-->
<!--        >上传</el-button>-->
<!--      </el-col>-->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="success"-->
<!--          plain-->
<!--          icon="el-icon-edit"-->
<!--          size="mini"-->
<!--          :disabled="single"-->
<!--          @click="handleUpdate"-->
<!--          v-hasPermi="['system:firmware:edit']"-->
<!--        >修改</el-button>-->
<!--      </el-col>-->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="danger"-->
<!--          plain-->
<!--          icon="el-icon-delete"-->
<!--          size="mini"-->
<!--          :disabled="multiple"-->
<!--          @click="handleDelete"-->
<!--          v-hasPermi="['system:firmware:remove']"-->
<!--        >删除</el-button>-->
<!--      </el-col>-->
<!--      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>-->
<!--    </el-row>-->

<!--    <el-table v-loading="loading" :data="firmwareList" @selection-change="handleSelectionChange">-->
<!--      <el-table-column type="selection" width="55" align="center" />-->
<!--      <el-table-column label="序号" align="center" prop="id" width="120" />-->
<!--      <el-table-column label="文件名称" align="center" prop="apkName" width="200"  :show-overflow-tooltip="true" />-->
<!--      <el-table-column label="版本号" align="center" prop="version" width="100" />-->
<!--      <el-table-column label="下载地址" align="center" prop="downloadUrl"  :show-overflow-tooltip="true"/>-->
<!--      <el-table-column label="升级日志" align="left" prop="updateLog"  :show-overflow-tooltip="true" />-->
<!--      <el-table-column label="下载次数" align="center" prop="downloadCount" width="80" :show-overflow-tooltip="true" />-->
<!--      <el-table-column label="上传时间" align="center" prop="uploadTime" width="180">-->
<!--        <template slot-scope="scope">-->
<!--          <span>{{ parseTime(scope.row.uploadTime) }}</span>-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">-->
<!--        <template slot-scope="scope">-->
<!--          <el-button-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-edit"-->
<!--            @click="handleUpdate(scope.row)"-->
<!--            v-hasPermi="['system:firmware:edit']"-->
<!--          >修改</el-button>-->
<!--          <el-button-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-delete"-->
<!--            @click="handleDelete(scope.row)"-->
<!--            v-hasPermi="['system:firmware:remove']"-->
<!--          >删除</el-button>-->
<!--          <el-button-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-share"-->
<!--            @click="handleSend(scope.row)"-->
<!--            v-hasPermi="['system:firmware:issue ']"-->
<!--          >下发</el-button>-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--    </el-table>-->

<!--    <pagination-->
<!--      v-show="total>0"-->
<!--      :total="total"-->
<!--      :page.sync="queryParams.pageNum"-->
<!--      :limit.sync="queryParams.pageSize"-->
<!--      @pagination="getList"-->
<!--    />-->

<!--    &lt;!&ndash; 添加或修改固件信息对话框 &ndash;&gt;-->
<!--    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body >-->
<!--      <el-form ref="form" :model="form" :rules="rules" label-width="80px">-->

<!--&lt;!&ndash;        <el-form-item label="上传文件:" prop="excel">&ndash;&gt;-->
<!--&lt;!&ndash;          <el-upload&ndash;&gt;-->
<!--&lt;!&ndash;            class="upload-demo"&ndash;&gt;-->
<!--&lt;!&ndash;            ref="upload"&ndash;&gt;-->
<!--&lt;!&ndash;            action&lt;!&ndash; 这里比填,异步时写后端接口,就可以,我们不用,所以不谢&ndash;&gt;&ndash;&gt;-->
<!--&lt;!&ndash;          :http-request="httpRequest"&lt;!&ndash;覆盖默认的上传行为，可以自定义上传的实现&ndash;&gt;&ndash;&gt;-->
<!--&lt;!&ndash;          :before-upload="beforeUpload"&lt;!&ndash;这是上传前的处理方法&ndash;&gt;&ndash;&gt;-->
<!--&lt;!&ndash;          :on-exceed="handleExceed"&lt;!&ndash;文件超出个数限制时的钩子&ndash;&gt;&ndash;&gt;-->
<!--&lt;!&ndash;          :limit="1">&ndash;&gt;-->
<!--&lt;!&ndash;          <el-button slot="trigger" size="small" type="primary">选取文件</el-button>&ndash;&gt;-->
<!--&lt;!&ndash;          <div slot="tip" class="el-upload__tip">只能上传.xlsx文件，且不超过5M</div>&ndash;&gt;-->
<!--&lt;!&ndash;          </el-upload>&ndash;&gt;-->
<!--&lt;!&ndash;        </el-form-item>&ndash;&gt;-->

<!--        <el-form-item label="升级日志" prop="updateLog">-->
<!--          <el-input v-model="form.updateLog" type="textarea" placeholder="请输入升级日志" />-->
<!--        </el-form-item>-->

<!--        <el-form-item label="上传文件:" >-->
<!--          <el-upload-->
<!--            ref="upload"-->
<!--            :limit="1"-->
<!--            accept=".apk"-->
<!--            :http-request="httpRequest"-->
<!--            :before-upload="beforeUpload"-->
<!--            :on-exceed="handleExceed"-->
<!--            :auto-upload="false"-->
<!--            drag-->
<!--          >-->
<!--            <i class="el-icon-upload"></i>-->
<!--            <div class="el-upload__text" >将文件拖到此处，或<em>点击上传</em></div>-->
<!--            <div class="el-upload__tip text-center" slot="tip">-->
<!--              <span>仅允许上传apk文件。</span>-->
<!--            </div>-->
<!--          </el-upload>-->
<!--        </el-form-item>-->

<!--&lt;!&ndash;        <el-dialog :title="upload.title1" :visible.sync="upload.open" width="100px" append-to-body>&ndash;&gt;-->
<!--&lt;!&ndash;          <el-upload&ndash;&gt;-->
<!--&lt;!&ndash;            ref="upload"&ndash;&gt;-->
<!--&lt;!&ndash;            :limit="1"&ndash;&gt;-->
<!--&lt;!&ndash;            accept=".apk"&ndash;&gt;-->
<!--&lt;!&ndash;            :headers="upload.headers"&ndash;&gt;-->
<!--&lt;!&ndash;            :action="upload.url + '?updateSupport=' + upload.updateSupport"&ndash;&gt;-->
<!--&lt;!&ndash;            :disabled="upload.isUploading"&ndash;&gt;-->
<!--&lt;!&ndash;            :on-progress="handleFileUploadProgress"&ndash;&gt;-->
<!--&lt;!&ndash;            :on-success="handleFileSuccess"&ndash;&gt;-->
<!--&lt;!&ndash;            :auto-upload="false"&ndash;&gt;-->
<!--&lt;!&ndash;            drag&ndash;&gt;-->
<!--&lt;!&ndash;          >&ndash;&gt;-->
<!--&lt;!&ndash;            <i class="el-icon-upload"></i>&ndash;&gt;-->
<!--&lt;!&ndash;            <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>&ndash;&gt;-->
<!--&lt;!&ndash;            <div class="el-upload__tip text-center" slot="tip">&ndash;&gt;-->
<!--&lt;!&ndash;              <span>仅允许上传apk文件。</span>&ndash;&gt;-->
<!--&lt;!&ndash;            </div>&ndash;&gt;-->
<!--&lt;!&ndash;            <div slot="footer" class="dialog-footer">&ndash;&gt;-->
<!--&lt;!&ndash;              <el-button type="primary" @click="submitFileForm"  >确 定</el-button>&ndash;&gt;-->
<!--&lt;!&ndash;              <el-button @click="upload.open = false">取 消</el-button>&ndash;&gt;-->
<!--&lt;!&ndash;            </div>&ndash;&gt;-->
<!--&lt;!&ndash;          </el-upload>&ndash;&gt;-->
<!--&lt;!&ndash;        </el-dialog>&ndash;&gt;-->



<!--      </el-form>-->
<!--      <div slot="footer" class="dialog-footer">-->
<!--        <el-button type="primary" @click="submitForm">确 定</el-button>-->
<!--        <el-button @click="cancel">取 消</el-button>-->
<!--      </div>-->
<!--    </el-dialog>-->

<!--  </div>-->
<!--</template>-->

<!--<script>-->
<!--import { listFirmware, updateFirmware, addFirmware, delFirmware, getFirmware, } from "@/api/system/firmware";-->

<!--export default {-->
<!--  name: "firmware",-->
<!--  dicts: ['sys_reader_sex','sys_reader_state'],-->
<!--  data() {-->
<!--    return {-->
<!--      // 遮罩层-->
<!--      loading: true,-->
<!--      // 选中数组-->
<!--      ids: [],-->
<!--      // 非单个禁用-->
<!--      single: true,-->
<!--      // 非多个禁用-->
<!--      multiple: true,-->
<!--      // 显示搜索条件-->
<!--      showSearch: true,-->
<!--      // 总条数-->
<!--      total: 0,-->

<!--      // 固件数据信息-->
<!--      firmwareList: [],-->
<!--      disabledInput:false,-->


<!--      // 弹出层标题-->
<!--      title: "",-->
<!--      // 是否显示弹出层-->
<!--      open: false,-->
<!--      // 查询参数-->
<!--      queryParams: {-->
<!--        pageNum: 1,-->
<!--        pageSize: 10,-->
<!--        apkName: undefined,-->
<!--        version: undefined-->
<!--      },-->
<!--      // 表单参数-->
<!--      form: {},-->
<!--      // 表单校验-->
<!--      rules: {-->
<!--        rdIdNumber: [-->
<!--          { required: true, message: "身份证号不能为空", trigger: "blur" }-->
<!--        ],-->
<!--        rdName: [-->
<!--          { required: true, message: "姓名不能为空", trigger: "blur" }-->
<!--        ],-->
<!--        rdPhone: [-->
<!--          { required: true, message: "手机号不能为空", trigger: "blur" }-->
<!--        ]-->
<!--      }-->
<!--    };-->
<!--  },-->
<!--  created() {-->
<!--    this.getList();-->
<!--  },-->
<!--  methods: {-->
<!--    /** 查询读者列表 */-->
<!--    getList() {-->
<!--      this.loading = true;-->
<!--      listFirmware(this.queryParams).then(response => {-->
<!--        this.firmwareList = response.rows;-->
<!--        this.total = response.total;-->
<!--        this.loading = false;-->
<!--      });-->
<!--    },-->
<!--    // 取消按钮-->
<!--    cancel() {-->
<!--      this.open = false;-->
<!--      this.reset();-->
<!--    },-->
<!--    // 表单重置-->
<!--    reset() {-->
<!--      this.form = {-->
<!--        updateLog: undefined,-->
<!--      };-->
<!--      this.resetForm("form");-->
<!--    },-->

<!--    // /** 下发 */-->
<!--    // handleSend(row){-->
<!--    //     this.$router.push("/scBs/dev-cell/manage/" + row.devSn);-->
<!--    // },-->

<!--    /** 搜索按钮操作 */-->
<!--    handleQuery() {-->
<!--      this.queryParams.pageNum = 1;-->
<!--      this.getList();-->
<!--    },-->
<!--    /** 重置按钮操作 */-->
<!--    resetQuery() {-->
<!--      this.resetForm("queryForm");-->
<!--      this.handleQuery();-->
<!--    },-->
<!--    /** 多选框选中数据 */-->
<!--    handleSelectionChange(selection) {-->
<!--      this.ids = selection.map(item => item.id);-->
<!--      this.single = selection.length!==1;-->
<!--      this.multiple = !selection.length-->
<!--    },-->
<!--    /** 新增按钮操作 */-->
<!--    handleAdd() {-->
<!--      this.reset();-->
<!--      this.open = true;-->
<!--      this.disabledInput = false;-->
<!--      this.title = "上传固件信息";-->
<!--    },-->
<!--    /** 修改按钮操作 */-->
<!--    handleUpdate(row) {-->
<!--      this.reset();-->
<!--      const id = row.id || this.ids;-->
<!--      getFirmware(id).then(response => {-->
<!--        this.form = response.data;-->
<!--        this.disabledInput = true;-->
<!--        this.open = true;-->
<!--        this.title = "修改固件信息";-->
<!--      });-->
<!--    },-->
<!--    /** 删除按钮操作 */-->
<!--    handleDelete(row) {-->
<!--      const ids = row.id || this.ids;-->
<!--      this.$modal.confirm('是否确认删除序号为"' + ids + '"的数据项？').then(function() {-->
<!--        return delFirmware(ids);-->
<!--      }).then(() => {-->
<!--        this.getList();-->
<!--        this.$modal.msgSuccess("删除成功");-->
<!--      }).catch(() => {});-->
<!--    },-->

<!--    /** 提交按钮 */-->
<!--    submitForm: function() {-->
<!--      this.$refs["form"].validate(valid => {-->
<!--        if (valid) {-->
<!--          if (this.form.id !== undefined) {-->
<!--            updateFirmware(this.form).then(response => {-->
<!--              this.$modal.msgSuccess("修改成功");-->
<!--              this.open = false;-->
<!--              this.getList();-->
<!--            });-->
<!--          } else {-->
<!--            addFirmware(this.form).then(response => {-->
<!--              this.$modal.msgSuccess("上传成功");-->
<!--              this.open = false;-->
<!--              this.getList();-->
<!--            });-->
<!--          }-->
<!--        }-->
<!--      });-->
<!--    },-->



<!--    /** 导出按钮操作 */-->
<!--    handleExport() {-->
<!--      this.download('system/reader/export', {-->
<!--        ...this.queryParams-->
<!--      }, `reader_${new Date().getTime()}.xlsx`)-->
<!--    }-->
<!--  }-->
<!--};-->
<!--</script>-->
