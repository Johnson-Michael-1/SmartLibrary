<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="标准书号" prop="bkIsbn">
        <el-input
          v-model="queryParams.bkIsbn"
          placeholder="请输入书籍ISBN号"
          clearable
          @keyup.enter.native="handleQuery"
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
      <el-form-item label="读者身份证号" label-width="100px" prop="rdIdNumber">
        <el-input
          v-model="queryParams.rdIdNumber"
          placeholder="请输入读者身份证号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="bkState">
        <el-select v-model="queryParams.bkState" placeholder="请选择状态" clearable>
          <el-option
            v-for="dict in dict.type.sys_borrow_state"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="是否超期" prop="bkOvertime">
        <el-select v-model="queryParams.bkOvertime" placeholder="请选择状态" clearable>
          <el-option
            v-for="dict in dict.type.sys_overTime_state"
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
          v-hasPermi="['system:borrow:add']"
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
          v-hasPermi="['system:borrow:edit']"
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
          v-hasPermi="['system:borrow:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:borrow:export']"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleRenew"
          v-hasPermi="['system:renew:edit']"
        >续借</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="borrowList" @selection-change="handleSelectionChange">
<!--      <el-table-column type="selection" width="55" align="center" />-->
      <el-table-column label="序号" align="center" prop="borId" width="120" />
      <el-table-column label="设备名称" align="center" prop="devName" width="150" />
      <el-table-column label="设备序列号" align="center" prop="devSn" width="100" />
      <el-table-column label="书籍名称" align="center" prop="bkName" width="200" />
      <el-table-column label="ISBN书号" align="center" prop="bkIsbn" width="200" />
<!--      <el-table-column label="RFID标签" align="center" prop="bkRfid" />-->
      <el-table-column label="读者姓名" align="center" prop="rdName" width="120" />
      <el-table-column label="身份证号" align="center" prop="rdIdNumber" width="180" />
      <el-table-column label="手机号" align="center" prop="rdPhone" width="100" />
      <el-table-column label="借阅时间" align="center" prop="borrowTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.borrowTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="归还时间" align="center" prop="returnTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.returnTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="还书人" align="center" prop="backName" width="150" />
      <el-table-column label="还书人身份证号" align="center" prop="backIdNumber" width="180" />
      <el-table-column label="状态" align="center" prop="bkState" width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_borrow_state" :value="scope.row.bkState"/>
        </template>
      </el-table-column>
      <el-table-column label="是否续借" align="center" prop="renewState" width="80">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_renew_state" :value="scope.row.renewState"/>
        </template>
      </el-table-column>
      <el-table-column label="续借时间" align="center" prop="renewTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.renewTime) }}</span>
        </template>
      </el-table-column>
<!--      <el-table-column label="是否超期" align="center" prop="bkOvertime" width="100">-->
<!--        <template slot-scope="scope">-->
<!--          <dict-tag :options="dict.type.sys_overTime_state" :value="scope.row.bkOvertime"/>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="到期时间" align="center" prop="shouldBackTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.shouldBackTime) }}</span>
        </template>
      </el-table-column>
<!--      <el-table-column label="超期费用(元)" align="center" prop="bkPrice" />-->
<!--      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="120">-->
<!--        <template slot-scope="scope">-->
<!--          <el-button-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-edit"-->
<!--            @click="handleUpdate(scope.row)"-->
<!--            v-hasPermi="['system:borrow:edit']"-->
<!--          >修改</el-button>-->
<!--          <el-button-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-delete"-->
<!--            @click="handleDelete(scope.row)"-->
<!--            v-hasPermi="['system:borrow:remove']"-->
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

    <!-- 添加或修改借阅记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="标准书号" prop="bkIsbn">
          <el-input v-model="form.bkIsbn" placeholder="请输入标准书号" />
        </el-form-item>
        <el-form-item label="RFID标签" prop="bkIsbn">
          <el-input v-model="form.bkRfid" placeholder="请输入标签号" />
        </el-form-item>
        <el-form-item label="读者身份证号" prop="rdIdNumber" label-width="100px">
          <el-input v-model="form.rdIdNumber" placeholder="请输入读者身份证号" />
        </el-form-item>
        <el-form-item label="读者姓名" prop="rdName" label-width="100px">
          <el-input v-model="form.rdName" placeholder="请输入读者姓名" />
        </el-form-item>
        <el-form-item label="读者手机号" prop="rdPhone" label-width="100px">
          <el-input v-model="form.rdPhone" placeholder="请输入读者手机号" />
        </el-form-item>
        <el-form-item label="借阅时间" prop="borrowTime">
          <el-date-picker type="datetime" placeholder="选择日期" v-model="form.borrowTime"
                          value-format="yyyy-MM-dd HH:mm:ss" >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="应还时间" prop="shouldBackTime">
          <el-date-picker type="datetime" placeholder="选择日期" v-model="form.shouldBackTime"
                          value-format="yyyy-MM-dd HH:mm:ss" >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="状态" prop="bkState" label-width="100px">
          <el-radio-group v-model="form.bkState">
            <el-radio
              v-for="dict in dict.type.sys_borrow_state"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
<!--        <el-form-item label="是否超期" prop="bkOvertime">-->
<!--          <el-radio-group v-model="form.bkOvertime">-->
<!--            <el-radio-->
<!--              v-for="dict in dict.type.sys_overTime_state"-->
<!--              :key="dict.value"-->
<!--              :label="dict.value"-->
<!--            >{{dict.label}}</el-radio>-->
<!--          </el-radio-group>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="超期待付金额" prop="remark">-->
<!--          <el-input-number v-model="form.remark" controls-position="right" :min="0" />-->
<!--        </el-form-item>-->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>

import { listBorrow, getBorrow, delBorrow, addBorrow, updateBorrow, bkRenew } from "@/api/system/borrow";

export default {
  name: "borrow",
  dicts: ['sys_borrow_state','sys_overTime_state', 'sys_renew_state'],
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

      // 借阅表格数据
      borrowList: [],

      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        bkIsbn: undefined,
        rd_name: undefined,
        rdIdNumber: undefined,
        bkState: undefined,
        bkOvertime: undefined
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
      listBorrow(this.queryParams).then(response => {
        this.borrowList = response.rows;
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
        borId: undefined,
        bkRfid: undefined,
        bkIsbn: undefined,
        rdName: undefined,
        rdPhone: undefined,
        borrowTime: undefined,
        renewTime: undefined,
        shouldBackTime: undefined,
        renewState:undefined,
        returnTime: undefined,
        bkState: "0",
        bkOvertime: "0",
        bkPrice: undefined
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
      this.ids = selection.map(item => item.borId)
      this.single = selection.length!=1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加借阅信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const borId = row.borId || this.ids
      getBorrow(borId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改借阅信息";
      });
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.borId != undefined) {
            updateBorrow(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addBorrow(this.form).then(response => {
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
      const borIds = row.borId || this.ids;
      this.$modal.confirm('是否确认删除借阅编号为"' + borIds + '"的数据项？').then(function() {
        return delBorrow(borIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },

    /** 续借按钮操作 */
    handleRenew(row) {
      const borId = row.borId || this.ids;
      getBorrow(borId).then(response => {
        bkRenew(response.data).then(() => {
          this.$modal.msgSuccess("续借成功");
          this.getList();
        }).catch(() => {});
      });
    },

//     function() {
//       return bkRenew(borId);
//     }).then(() => {
//   this.getList();
//   this.$modal.msgSuccess("续借成功");
// }).catch(() => {});


//     then(response =>{
//   bkRenew(borId).then(() => {
//     this.$modal.msgSuccess("续借成功");
//     this.getList();
//   }).catch(() => {});
// });


    // /** 修改按钮操作 */
    // handleUpdate(row) {
    //   this.reset();
    //   const borId = row.borId || this.ids
    //   getBorrow(borId).then(response => {
    //     this.form = response.data;
    //     this.open = true;
    //     this.title = "修改借阅信息";
    //   });
    // },



    /** 导出按钮操作 */
    handleExport() {
      this.download('system/borrow/export', {
        ...this.queryParams
      }, `borrow_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
