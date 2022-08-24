<template>
  <div class="app-container">

    <!-- 报警信息  -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
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
      <el-form-item label="设备名称" prop="devName" >
        <el-input
          v-model="queryParams.devName"
          placeholder="请输入设备名称"
          clearable
          style="width: 300px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="设备序列号" prop="devSn" >
        <el-input
          v-model="queryParams.devSn"
          placeholder="请输入设备序列号"
          clearable
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
<!--      <el-form-item label="报警时间">-->
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
      <el-form-item label="报警类型" prop="alarmType">
        <el-select
          v-model="queryParams.alarmType"
          placeholder="请输入报警类型"
          clearable
          style="width: 240px"
        >
          <el-option
            v-for="dict in dict.type.sys_alarm_type"
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



    <el-table v-loading="loading" :data="alarmInfoList" @selection-change="handleSelectionChange">
<!--      <el-table-column type="selection" width="55" align="center" />-->
      <el-table-column label="序号" align="center" prop="alarmId" width="150" />
      <el-table-column label="所属组织" align="center" prop="deptName"/>
      <el-table-column label="报警时间" align="center" prop="alarmTime" width="180">
      <template slot-scope="scope">
          <span>{{ parseTime(scope.row.alarmTime) }}</span>
        </template>
      </el-table-column>
        <el-table-column label="报警类型" align="center" prop="alarmType" width="200">
          <template slot-scope="scope">
            <dict-tag :options="dict.type.sys_alarm_type" :value="scope.row.alarmType"/>
          </template>
        </el-table-column>
      <el-table-column label="设备名称" align="left" prop="devName" width="350" />
      <el-table-column label="设备序列号" align="center" prop="devSn" width="200" />
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

import { listAlarmInfo, getAlarmInfo, delAlarmInfo, addAlarmInfo, updateAlarmInfo } from "@/api/system/alarmInfo";
import { treeselect } from "@/api/system/dept";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "alarmInfo",
  dicts: ['sys_alarm_type'],
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

      //报警记录数据
      alarmInfoList: [],
      // 日期范围
      dateTimeRange: [],
      // 组织树选项
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
        devSn: undefined,
        alarmTime: undefined,
        alarmType: undefined,
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
    /** 查询岗位列表 */
    getList() {
      this.loading = true;
      listAlarmInfo(this.queryParams).then(response => {
        this.alarmInfoList = response.rows;
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
        alarmId: undefined,
        postCode: undefined,
        postName: undefined,
        postSort: 0,
        status: "0",
        remark: undefined
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
      this.ids = selection.map(item => item.alarmId)
      this.single = selection.length!=1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const alarmId = row.alarmId || this.ids
      getAlarmInfo(alarmId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改";
      });
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.alarmId != undefined) {
            updateAlarmInfo(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addAlarmInfo(this.form).then(response => {
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
      const alarmIds = row.alarmId || this.ids;
      this.$modal.confirm('是否确认删除报警编号为"' + alarmIds + '"的数据项？').then(function() {
        return delAlarmInfo(alarmIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/post/export', {
        ...this.queryParams
      }, `post_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
