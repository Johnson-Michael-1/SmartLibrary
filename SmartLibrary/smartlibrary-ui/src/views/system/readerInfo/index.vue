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
      <el-form-item label="读者性别" prop="rdSex">
        <el-select v-model="queryParams.rdSex" placeholder="读者性别" clearable>
          <el-option
            v-for="dict in dict.type.sys_reader_sex"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="读者状态" prop="rdState">
        <el-select v-model="queryParams.rdState" placeholder="读者状态" clearable>
          <el-option
            v-for="dict in dict.type.sys_reader_state"
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
          v-hasPermi="['system:readerInfo:add']"
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
          v-hasPermi="['system:readerInfo:edit']"
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
          v-hasPermi="['system:readerInfo:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:readerInfo:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="readerInfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" prop="rdId" width="120" />
<!--      <el-table-column label="UUID编号" align="center" prop="rdNumber" />-->
      <el-table-column label="所属组织" align="left"  prop="deptName"  :show-overflow-tooltip="true" />
      <el-table-column label="姓名" align="center" prop="rdName" width="200" />
      <el-table-column label="性别" align="center" prop="rdSex" width="50">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_reader_sex" :value="scope.row.rdSex" />
        </template>
      </el-table-column>
      <el-table-column label="照片" align="center" prop="rdPic" width="80" :show-overflow-tooltip="true" />
      <el-table-column label="身份证号" align="center" prop="rdIdNumber" width="180" />
      <el-table-column label="手机号" align="center" prop="rdPhone" width="120" />
      <el-table-column label="微信号" align="center" prop="rdWxNumber" width="150" />
      <el-table-column label="用户昵称" align="center" prop="nickName" width="150"/>
      <el-table-column label="支付宝账号" align="center" prop="rdZfbNumber" width="150" />
<!--      <el-table-column label="照片" align="center"  key= "" prop="studentFeature.photo"  width="120" >-->
<!--        <template slot-scope="scope">-->
<!--          <el-popover placement="right" title="" trigger="hover">-->
<!--            <img :src="getImgUrl(scope.row.photo)" style="height: 300px;width: 300px">-->
<!--            <img slot="reference" :src="getImgUrl(scope.row.photo)" alt="" style="max-height: 100px;max-width: 100px">-->
<!--          </el-popover>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="状态" align="center" prop="rdState" width="70">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_reader_state" :value="scope.row.rdState"/>
        </template>
      </el-table-column>
<!--      <el-table-column label="备注" align="left" prop="rdRemark" width="150" />-->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:readerInfo:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:readerInfo:remove']"
          >删除</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-s-open"
            @click="handleReset(scope.row)"
            v-hasPermi="['system:readerInfo:updatePassword']"
          >密码重置</el-button>
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
        <el-form-item label="归属组织" prop="deptId">
          <treeselect v-model="form.deptId" :options="orgOptions" :show-count="true" placeholder="请选择归属组织"/>
        </el-form-item>
        <el-form-item label="姓名" prop="rdName" >
          <el-input v-model="form.rdName" placeholder="请输入读者姓名"/>
        </el-form-item>
        <el-form-item label="身份证号" prop="rdIdNumber"  >
          <el-input v-model="form.rdIdNumber" placeholder="请输入读者身份证号" />
        </el-form-item>
        <el-form-item label="昵称" prop="nickName" >
          <el-input v-model="form.nickName" placeholder="请输入昵称"/>
        </el-form-item>
        <el-form-item label="手机号" prop="rdPhone" >
          <el-input v-model="form.rdPhone" placeholder="请输入读者手机号" />
        </el-form-item>
        <el-form-item label="微信号" prop="rdWxNumber" >
          <el-input v-model="form.rdWxNumber" placeholder="请输入读者微信号" />
        </el-form-item>
        <el-form-item label="支付宝号" prop="rdZfbNumber" >
          <el-input v-model="form.rdZfbNumber" placeholder="请输入读者支付宝帐号" />
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
        <el-form-item label="状态" prop="rdState" >
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
import { listReaderInfo, updateReaderInfo, addReaderInfo, delReaderInfo, getReaderInfo, resetPassword} from "@/api/system/readerInfo";
import { treeselect } from "@/api/system/dept";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "readerInfo",
  dicts: ['sys_reader_sex','sys_reader_state'],
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

      // 读者数据信息
      readerInfoList: [],
      // 组织树选项
      orgOptions: [],
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
        rdName: undefined,
        deptId: undefined,
        rdIdNumber: undefined,
        rdPhone: undefined,
        rdSex: undefined,
        rdState: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        deptId: [
          { required: true, message: "归属组织不能为空", trigger: "blur" }
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
      listReaderInfo(this.queryParams).then(response => {
        this.readerInfoList = response.rows;
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
        rdId: undefined,
        rdIdNumber: undefined,
        rdWxNumber: undefined,
        rdZfbNumber: undefined,
        rdName: undefined,
        rdPhone: undefined,
        rdPic: undefined,
        rdPassword: undefined,
        rdSex: "0",
        rdState: "0",
        rdRemark: undefined
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
      this.ids = selection.map(item => item.rdId);
      this.single = selection.length!==1;
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加读者信息";
    },

    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const rdId = row.rdId || this.ids;
      getReaderInfo(rdId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改读者信息";
      });
    },

    /** 提交按钮 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.rdId !== undefined) {
            updateReaderInfo(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addReaderInfo(this.form).then(response => {
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
      const rdIds = row.rdId || this.ids;
      this.$modal.confirm('是否确认删除读者编号为"' + rdIds + '"的数据项？').then(function() {
        return delReaderInfo(rdIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 重置密码 */
    handleReset(row){
      const rdName = row.rdName;
      this.$modal.confirm('是否确认重置读者："' + rdName + '"的密码？').then(function() {
        return resetPassword(row.rdId);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("密码重置成功");
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
