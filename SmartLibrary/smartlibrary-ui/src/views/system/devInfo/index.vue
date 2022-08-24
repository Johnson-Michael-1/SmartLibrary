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
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="warning"-->
<!--          plain-->
<!--          icon="el-icon-download"-->
<!--          size="mini"-->
<!--          @click="handleExport"-->
<!--          v-hasPermi="['system:devInfo:export']"-->
<!--        >导出</el-button>-->
<!--      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

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
<!--      <el-table-column label="创建日期" align="center" prop="linkTime" width="180">-->
<!--        <template slot-scope="scope">-->
<!--          <span>{{ parseTime(scope.row.linkTime) }}</span>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width"  >
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
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:devInfo:remove']"
          >更多</el-button>
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
        <el-form-item label="所属组织" prop="deptId">
          <treeselect v-model="form.deptId" :options="orgOptions" :show-count="true" placeholder="请选择归属组织"/>
        </el-form-item>
        <el-form-item label="设备名称" prop="devName"  >
          <el-input v-model="form.devName" placeholder="请输入设备名称" />
        </el-form-item>
        <el-form-item label="序列号" prop="devSn" >
          <el-input v-model="form.devSn" v-bind:disabled="disabledInput" placeholder="请输入设备序列号"/>
        </el-form-item>
        <el-form-item label="设备地点" prop="devAddress" >
          <el-input v-model="form.devAddress" placeholder="请输入设备地点" />
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

      // 读者数据信息
      devInfoList: [],
      // 组织树选项
      orgOptions: [],
      // 设备序列号编辑禁用
      disabledInput: false,
      // 组织名称
      deptName: undefined,

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
      // this.getTreeselect();
      this.disabledInput = false;
      this.open = true;
      this.title = "添加书柜信息";
    },

    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const devId = row.devId || this.ids;
      getDevInfo(devId).then(response => {
        this.form = response.data;
        this.disabledInput = true;
        this.open = true;
        this.title = "修改书柜信息";
      });
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
