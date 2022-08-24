<template>
  <div class="app-container">
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
      <el-form-item label="更新结果" prop="updateResult">
        <el-select v-model="queryParams.updateResult" placeholder="请选择更新结果" clearable>
          <el-option
            v-for="dict in dict.type.sys_common_status"
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



    <el-table v-loading="loading" :data="firmwareRecord" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column label="序号" align="center" prop="id" width="100" />
      <el-table-column label="所属组织" align="left"  prop="deptName"  :show-overflow-tooltip="true" />
      <el-table-column label="设备序列号" align="center" prop="devSn" width="180" />
      <el-table-column label="设备名称" align="center" prop="devName"  />
      <el-table-column label="升级后版本" align="center" prop="newVersion" width="100" />
      <el-table-column label="是否强制" align="center" prop="updateInstall"  width="80">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_renew_state" :value="scope.row.updateInstall"/>
        </template>
      </el-table-column>
      <el-table-column label="升级前版本" align="left" prop="oldVersion" width="100" />
      <el-table-column label="升级时间" align="center" prop="upgradeTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.upgradeTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结果" align="center" prop="updateResult"  width="80">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_common_status" :value="scope.row.updateResult"/>
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
<!--      <el-form ref="form" :model="form" :rules="rules" label-width="80px">-->
<!--        <el-form-item label="文件名称" prop="apkName" >-->
<!--          <el-input v-model="form.apkName" placeholder="请输入apk文件名" v-bind:disabled="disabledInput"  />-->
<!--        </el-form-item>-->
<!--        <el-form-item label="版本号" prop="version" >-->
<!--          <el-input v-model="form.version" placeholder="请输入apk版本号" v-bind:disabled="disabledInput" />-->
<!--        </el-form-item>-->
<!--        <el-form-item label="下载地址" prop="downloadUrl"  >-->
<!--          <el-input v-model="form.downloadUrl" placeholder="请选择上传地址" v-bind:disabled="disabledInput" />-->
<!--        </el-form-item>-->
<!--&lt;!&ndash;        <el-form-item label="上传时间" prop="uploadTime">&ndash;&gt;-->
<!--&lt;!&ndash;          <el-date-picker type="uploadTime" placeholder="选择日期" v-model="form.uploadTime"&ndash;&gt;-->
<!--&lt;!&ndash;                          value-format="yyyy-MM-dd HH:mm:ss" >&ndash;&gt;-->
<!--&lt;!&ndash;          </el-date-picker>&ndash;&gt;-->
<!--&lt;!&ndash;        </el-form-item>&ndash;&gt;-->
<!--        <el-form-item label="升级日志" prop="updateLog">-->
<!--          <el-input v-model="form.updateLog" type="textarea" placeholder="请输入升级" />-->
<!--        </el-form-item>-->
<!--      </el-form>-->
<!--      <div slot="footer" class="dialog-footer">-->
<!--        <el-button type="primary" @click="submitForm">确 定</el-button>-->
<!--        <el-button @click="cancel">取 消</el-button>-->
<!--      </div>-->
    </el-dialog>

  </div>
</template>

<script>

// import { listFirmware, updateFirmware, addFirmware, delFirmware, getFirmware, } from "@/api/system/firmwareRecord";
import { listfirmwareRecord,  } from "@/api/system/firmwareRecord";
import { treeselect } from "@/api/system/dept";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "firmwareRecord",
  dicts: ['sys_common_status','sys_renew_state' ],
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

      // 固件升级记录信息
      firmwareRecord: [],
      //组织数据
      orgOptions: [],

      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        deptId: undefined,
        devName: undefined,
        updateResult: undefined
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
  watch: {
    // 根据名称筛选组织树
    deptName(val) {
      this.$refs.tree.filter(val);
    }
  },
  created() {
    this.getList();
    this.getTreeSelect();
  },
  methods: {
    /** 查询读者列表 */
    getList() {
      this.loading = true;
      listfirmwareRecord(this.queryParams).then(response => {
        this.firmwareRecord = response.rows;
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
        apkName: undefined,
        version: undefined,
        downloadUrl: undefined,
        updateLog: undefined,
        uploadTime: undefined,
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


    /** 导出按钮操作 */
    handleExport() {
      this.download('system/reader/export', {
        ...this.queryParams
      }, `reader_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
