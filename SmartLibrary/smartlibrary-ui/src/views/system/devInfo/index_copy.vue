<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="组织名称" prop="orgName">
        <el-input
          v-model="queryParams.orgName"
          placeholder="请输入组织名称"
          clearable
          @keyup.enter.native="handleQuery"
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
      <el-form-item label="设备地址" prop="devAddress">
          <el-input
            v-model="queryParams.devAddress"
            placeholder="请输入设备地址"
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

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:devInfo:add']"
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
          v-hasPermi="['system:devInfo:edit']"
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
          v-hasPermi="['system:devInfo:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:devInfo:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="devInfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" prop="devId" />
      <el-table-column label="设备序列号" align="center" prop="devSn" />
      <el-table-column label="设备名称" align="center" prop="devName" />
      <el-table-column label="所属组织" align="center" prop="orgName" />
      <el-table-column label="设备类型" align="center" prop="devType" />
      <el-table-column label="关联日期" align="center" prop="linkTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.linkTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="地址" align="center" prop="devAddress" />
      <el-table-column label="状态" align="center" prop="devState">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_dev_state" :value="scope.row.devState"/>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="devRemark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:devInfo:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:devInfo:remove']"
          >删除</el-button>
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

    <!-- 添加或修改读者信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body >
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="序列号" prop="devSn" >
          <el-input v-model="form.devSn" placeholder="请输入设备序列号"/>
        </el-form-item>
        <el-form-item label="设备名称" prop="devName"  >
          <el-input v-model="form.devName" placeholder="请输入设备名称" />
        </el-form-item>
        <el-form-item label="所属组织" prop="orgName" >
          <el-input v-model="form.orgName" placeholder="请输入所属组织名称" />
        </el-form-item>
        <el-form-item label="设备类型" prop="devType" >
          <el-input v-model="form.devType" placeholder="请输入设备类型" />
        </el-form-item>
        <el-form-item label="关联时间" prop="linkTime">
          <el-date-picker type="datetime" placeholder="选择日期" v-model="form.linkTime"
                          value-format="yyyy-MM-dd HH:mm:ss" >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="地址" prop="devAddress" >
          <el-input v-model="form.devAddress" placeholder="请输入设备地址" />
        </el-form-item>
        <el-form-item label="状态" prop="devState" >
          <el-radio-group v-model="form.devState" >
            <el-radio
              v-for="dict in dict.type.sys_dev_state"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="备注" prop="devRemark">
          <el-input v-model="form.devRemark" type="textarea" placeholder="请输入设备备注" />
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

import { listDevInfo, updateDevInfo, addDevInfo,delDevInfo,getDevInfo} from "@/api/system/devInfo";

export default {
  name: "devInfo",
  dicts: ['sys_dev_state',],
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

      // 读者数据信息
      devInfoList: [],

      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orgName: undefined,
        devName: undefined,
        devAddress: undefined,
        devState: undefined
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
      listDevInfo(this.queryParams).then(response => {
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
        devState: "0",
        devRemark: undefined
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
      this.ids = selection.map(item => item.devId)
      this.single = selection.length!=1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加设备信息";
    },

    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const devId = row.devId || this.ids;
      getDevInfo(devId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改设备信息";
      });
    },

    /** 提交按钮 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.devId != undefined) {
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
