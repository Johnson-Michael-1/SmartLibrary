<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="读者姓名" prop="rdName">
        <el-input
          v-model="queryParams.rdName"
          placeholder="请输入读者姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="身份证号" prop="rdIdNumber">
        <el-input
          v-model="queryParams.rdIdNumber"
          placeholder="请输入读者身份证号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="手机号" prop="rdPhone">
          <el-input
            v-model="queryParams.rdPhone"
            placeholder="请输入读者手机号"
            clearable
            @keyup.enter.native="handleQuery"
          />
      </el-form-item>
      <el-form-item label="登录方式" prop="loginType">
        <el-select v-model="queryParams.loginType" placeholder="请选择登录方式" clearable>
          <el-option
            v-for="dict in dict.type.sys_login_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
<!--      <el-form-item label="读者状态" prop="rdState">-->
<!--        <el-select v-model="queryParams.rdState" placeholder="请选择读者状态" clearable>-->
<!--          <el-option-->
<!--            v-for="dict in dict.type.sys_reader_state"-->
<!--            :key="dict.value"-->
<!--            :label="dict.label"-->
<!--            :value="dict.value"-->
<!--          />-->
<!--        </el-select>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="登录时间">-->
<!--        <template>-->
<!--          <div class="block">-->
<!--            <el-date-picker-->
<!--              v-model="dateTimeRange"-->
<!--              type="datetimerange"-->
<!--              range-separator="至"-->
<!--              start-placeholder="开始日期"-->
<!--              end-placeholder="结束日期"-->
<!--              :default-time="['0:0:0','23:59:59']">-->
<!--            </el-date-picker>-->
<!--          </div>-->
<!--        </template>-->
<!--      </el-form-item>-->
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

<!--    <el-row :gutter="10" class="mb8">-->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="primary"-->
<!--          plain-->
<!--          icon="el-icon-plus"-->
<!--          size="mini"-->
<!--          @click="handleAdd"-->
<!--          v-hasPermi="['system:readerInfo:add']"-->
<!--        >新增</el-button>-->
<!--      </el-col>-->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="success"-->
<!--          plain-->
<!--          icon="el-icon-edit"-->
<!--          size="mini"-->
<!--          :disabled="single"-->
<!--          @click="handleUpdate"-->
<!--          v-hasPermi="['system:readerInfo:edit']"-->
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
<!--          v-hasPermi="['system:readerInfo:remove']"-->
<!--        >删除</el-button>-->
<!--      </el-col>-->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="warning"-->
<!--          plain-->
<!--          icon="el-icon-download"-->
<!--          size="mini"-->
<!--          @click="handleExport"-->
<!--          v-hasPermi="['system:readerInfo:export']"-->
<!--        >导出</el-button>-->
<!--      </el-col>-->
<!--      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>-->
<!--    </el-row>-->

    <el-table v-loading="loading" :data="loginRecordList" @selection-change="handleSelectionChange">
<!--      <el-table-column type="selection" width="55" align="center" />-->
      <el-table-column label="序号" align="center" prop="loginId" width="120"  />
      <el-table-column label="设备名称" align="center" prop=" "  />
      <el-table-column label="设备序列号" align="center" prop=" "  />
      <el-table-column label="姓名" align="center" prop="rdName" width="150" />
      <el-table-column label="身份证号" align="center" prop="rdIdNumber" width="180"  />
      <el-table-column label="手机号" align="center" prop="rdPhone" width="120"  />
      <el-table-column label="登录时间" align="center" prop="loginTime" width="200" >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.loginTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="登录方式" align="center" prop="loginType" width="150">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_login_type" :value="scope.row.loginType"/>
        </template>
      </el-table-column>
<!--      <el-table-column label="性别" align="center" prop="rdSex">-->
<!--        <template slot-scope="scope">-->
<!--          <dict-tag :options="dict.type.sys_reader_sex" :value="scope.row.rdSex"/>-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column label="读者状态" align="center" prop="rdState">-->
<!--        <template slot-scope="scope">-->
<!--          <dict-tag :options="dict.type.sys_reader_state" :value="scope.row.rdState"/>-->
<!--        </template>-->
<!--      </el-table-column>-->

<!--      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">-->
<!--        <template slot-scope="scope">-->
<!--          <el-button-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-edit"-->
<!--            @click="handleUpdate(scope.row)"-->
<!--            v-hasPermi="['system:readerInfo:edit']"-->
<!--          >修改</el-button>-->
<!--          <el-button-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-delete"-->
<!--            @click="handleDelete(scope.row)"-->
<!--            v-hasPermi="['system:readerInfo:remove']"-->
<!--          >删除</el-button>-->
<!--        </template>-->
<!--      </el-table-column>-->
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改读者信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body >
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="姓名" prop="rdName" >
          <el-input v-model="form.rdName" placeholder="请输入读者姓名"/>
        </el-form-item>
        <el-form-item label="身份证号" prop="rdIdNumber"  >
          <el-input v-model="form.rdIdNumber" placeholder="请输入读者身份证号" />
        </el-form-item>
        <el-form-item label="手机号" prop="rdPhone" >
          <el-input v-model="form.rdPhone" placeholder="请输入读者手机号" />
        </el-form-item>
        <el-form-item label="登录时间" prop="loginTime">
          <el-date-picker type="datetime" placeholder="选择日期" v-model="form.loginTime"
                          value-format="yyyy-MM-dd HH:mm:ss" >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="登录方式" prop="loginType" >
          <el-radio-group v-model="form.loginType">
            <el-radio
              v-for="dict in dict.type.sys_login_type"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="性别" prop="rdSex" >
          <el-radio-group v-model="form.rdSex">
            <el-radio
              v-for="dict in dict.type.sys_reader_sex"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="读者状态" prop="rdState" >
          <el-radio-group v-model="form.rdState" >
            <el-radio
              v-for="dict in dict.type.sys_reader_state"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { listLoginRecord, updateLoginRecord, addLoginRecord,delLoginRecord,getLoginRecord} from "@/api/system/loginRecord";

export default {
  name: "loginRecord",
  dicts: ['sys_reader_sex','sys_reader_state','sys_login_type'],
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

      // 登录记录列表
      loginRecordList: [],
      // 日期范围
      dateTimeRange: [],

      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        rdName: undefined,
        rdIdNumber: undefined,
        rdPhone: undefined,
        rdSex: undefined,
        loginType: undefined,
        loginTime: undefined,
        rdState: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        postName: [
          { required: true, message: "岗位名称不能为空", trigger: "blur" }
        ],
        postCode: [
          { required: true, message: "岗位编码不能为空", trigger: "blur" }
        ],
        postSort: [
          { required: true, message: "岗位顺序不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询读者列表 */
    getList() {
      this.loading = true;
      listLoginRecord(this.queryParams).then(response => {
        this.loginRecordList = response.rows;
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
        loginId: undefined,
        rdName: undefined,
        rdIdNumber: undefined,
        rdPhone: undefined,
        loginTime: undefined,
        loginType:"0",
        rdSex: "0",
        rdState: "0"
      };
      this.resetForm("form");
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
      this.loginIds = selection.map(item => item.loginId)
      this.single = selection.length!=1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加登录信息";
    },

    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const loginId = row.loginId || this.loginIds;
      getLoginRecord(loginId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改登录信息";
      });
    },

    /** 提交按钮 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.loginId != undefined) {
            updateLoginRecord(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addLoginRecord(this.form).then(response => {
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
      const loginIds = row.loginId || this.loginIds;
      this.$modal.confirm('是否确认删除登录编号为"' + loginIds + '"的数据项？').then(function() {
        return delLoginRecord(loginIds);
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
