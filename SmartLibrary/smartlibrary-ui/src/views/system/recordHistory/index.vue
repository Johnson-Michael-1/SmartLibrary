<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">

      <el-form-item label="设备名称" prop="devName">
        <el-input
          v-model="queryParams.devName"
          placeholder="请输入设备名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="开门者姓名" prop="personName" label-width="100px">
        <el-input
          v-model="queryParams.personName"
          placeholder="请输入开门者姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="开门者账号" prop="cardNo" label-width="100px">
        <el-input
          v-model="queryParams.cardNo"
          placeholder="请输入开门者账号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

<!--      <el-form-item label="开门操作" prop="openType">-->
<!--        <el-select v-model="queryParams.openType" placeholder="选择开门做的操作" clearable>-->
<!--          <el-option-->
<!--            v-for="dict in dict.type.sys_open_type"-->
<!--            :key="dict.value"-->
<!--            :label="dict.label"-->
<!--            :value="dict.value"-->
<!--          />-->
<!--        </el-select>-->
<!--      </el-form-item>-->

      <el-form-item label="开门方式" prop="openMode">
        <el-select v-model="queryParams.openMode" placeholder="选择开门方式" clearable>
          <el-option
            v-for="dict in dict.type.sys_open_mode"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="开门结果" prop="openResult">
        <el-select v-model="queryParams.openResult" placeholder="开门结果" clearable>
          <el-option
            v-for="dict in dict.type.sys_common_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>

<!--      <el-form-item label="开门时间">-->
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

    <!-- 增删改导出按钮 -->
    <!-- <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:recordHistory:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:recordHistory:edit']"
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
          v-hasPermi="['system:recordHistory:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:recordHistory:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>  -->

    <!-- 开门记录列表信息 -->
    <el-table v-loading="loading" :data="recordHistoryList" @selection-change="handleSelectionChange" >
<!--      <el-table-column type="selection" width="55" align="center" />-->
      <el-table-column label="序号" align="center" prop="openId" width="130" />
<!--      <el-table-column label="所属组织" align="left" prop="deptName" width="250" />-->
      <el-table-column label="开门时间" align="center" prop="openTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.openTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="姓名" align="center" prop="personName" width="150" />
      <el-table-column label="身份证号" align="center" prop="cardNo" width="180" />
      <el-table-column label="设备名称" align="left" prop="devName"  />
      <el-table-column label="设备序列号" align="center" prop="devSn" width="200" />
      <el-table-column label="开门方式" align="center" prop="openMode" width="120">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_open_mode" :value="scope.row.openMode"/>
        </template>
      </el-table-column>
      <el-table-column label="抓拍图片" align="center" prop="takePhotoPath" width="100" />
      <el-table-column label="开门结果" align="center" prop="openResult" width="100" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_common_status" :value="scope.row.openResult"/>
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

import { listRecordHistory, getRecordHistory, delRecordHistory, addRecordHistory, updateRecordHistory} from "@/api/system/recordHistory";


export default {
  name: "recordHistory",
  dicts: ['sys_open_mode','sys_open_type','sys_common_status'],
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

      // 开门表格数据
      recordHistoryList: [],
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
        deptId: undefined,
        devName: undefined,
        personName: undefined,
        cardNo: undefined,
        openType: undefined,
        openMode: undefined,
        openResult: undefined
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
    /** 查询岗位列表 */
    getList() {
      this.loading = true;
      listRecordHistory(this.queryParams).then(response => {
        this.recordHistoryList = response.rows;
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
        openId: undefined,
        postCode: undefined,
        postName: undefined,
        postSort: 0,
        status: "0",
        remark: undefined
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
      this.ids = selection.map(item => item.openId)
      this.single = selection.length!=1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加开门记录";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const openId = row.openId || this.ids
      getRecordHistory(openId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改开门记录";
      });
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.openId != undefined) {
            updateRecordHistory(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addRecordHistory(this.form).then(response => {
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
      const openIds = row.openId || this.ids;
      this.$modal.confirm('是否确认删除开门编号为"' + openIds + '"的数据项？').then(function() {
        return delRecordHistory(openIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/recordHistory/export', {
        ...this.queryParams
      }, `recordHistory_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
