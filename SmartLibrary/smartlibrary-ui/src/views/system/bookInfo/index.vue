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
      <el-form-item label="书籍名称" prop="bkName">
        <el-input
          v-model="queryParams.bkName"
          placeholder="请输入书名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="作者名称" prop="bkAuthor">
        <el-input
          v-model="queryParams.bkAuthor"
          placeholder="请输入作者姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="书籍类型" prop="bkType">
          <el-input
            v-model="queryParams.bkType"
            placeholder="请输入书籍类型"
            clearable
            @keyup.enter.native="handleQuery"
          />
      </el-form-item>
      <el-form-item label="状态" prop="bkState">
        <el-select v-model="queryParams.bkState" placeholder="请选择书籍状态" clearable>
          <el-option
            v-for="dict in dict.type.sys_bk_state"
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
          v-hasPermi="['system:bookInfo:add']"
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
          v-hasPermi="['system:bookInfo:edit']"
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
          v-hasPermi="['system:bookInfo:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-upload2"
          size="mini"
          @click="handleImport"
          v-hasPermi="['system:user:import']"
        >导入</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:bookInfo:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="bookList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" prop="bkId" width="120" />
      <el-table-column label="所属组织" align="left"  prop="deptName"  :show-overflow-tooltip="true" width="300" />
      <el-table-column label="书籍类型" align="center" prop="bkType" width="100" />
      <el-table-column label="书籍名称" align="left" prop="bkName" width="250" />
      <el-table-column label="作者" align="center" prop="bkAuthor" width="200" />
      <el-table-column label="ISBN书号" align="center" prop="bkIsbn" width="150" />
<!--      <el-table-column label="rfid标签" align="center" prop="bkRfid" />-->
<!--      <el-table-column label="封面图片" align="center" prop="bkImg" />-->
      <el-table-column label="出版社" align="center" prop="bkPress" width="200" />
      <el-table-column label="出版时间" align="center" prop="publishTime" width="150" />
      <el-table-column label="页数" align="center" prop="bkPagination"  width="60"/>
      <el-table-column label="定价(元)" align="center" prop="bkPrice" width="80"/>
      <el-table-column label="状态" align="center" prop="bkState" width="100" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_bk_state" :value="scope.row.bkState"/>
        </template>
      </el-table-column>
      <el-table-column label="简介" align="left" prop="bkAbstract" :show-overflow-tooltip="true" width="150" />

<!--      <el-table-column label="创建时间" align="center" prop="createTime" width="180">-->
<!--        <template slot-scope="scope">-->
<!--          <span>{{ parseTime(scope.row.createTime) }}</span>-->
<!--        </template>-->
<!--      </el-table-column>-->

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="160">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-document"
            @click="handleDetail(scope.row)"
            v-hasPermi="['system:bookInfo:detail']"
          >详情</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:bookInfo:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:bookInfo:remove']"
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

    <!-- 添加或修改岗位对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="归属组织" prop="deptId">
          <treeselect v-model="form.deptId" :options="orgOptions" :show-count="true" placeholder="请选择归属组织"/>
        </el-form-item>
        <el-form-item label="标准书号" prop="bkIsbn" >
          <el-input v-model="form.bkIsbn" placeholder="请输入标准书号" v-bind:disabled="disableInput" />
        </el-form-item>
<!--        <el-form-item label="RFID标签" prop="bkRfid">-->
<!--          <el-input v-model="form.bkRfid" placeholder="请输入RFID标签号" />-->
<!--        </el-form-item>-->
        <el-form-item label="书名" prop="bkName">
          <el-input v-model="form.bkName" placeholder="请输入书名" v-bind:disabled="disabledInput" />
        </el-form-item>
        <el-row>
          <el-col :span="20">
            <el-form-item label="RFID标签" prop="bkRfid">
              <el-input v-model="form.bkRfid" placeholder="请输入RFID标签号" v-bind:disabled="disableInput" />
            </el-form-item>
          </el-col>
<!--          <el-col :span="2" >-->
<!--            <el-button type="primary"   @click="gotoWrite">写入</el-button>-->
<!--          </el-col>-->
        </el-row>
        <el-form-item label="作者" prop="bkAuthor">
          <el-input v-model="form.bkAuthor" placeholder="请输入作者名" v-bind:disabled="disabledInput" />
        </el-form-item>
        <el-form-item label="价格" prop="bkPrice">
          <el-input v-model="form.bkPrice" placeholder="请输入书籍价格" v-bind:disabled="disabledInput" />
        </el-form-item>
        <el-form-item label="类型" prop="bkType">
          <el-input v-model="form.bkType" placeholder="请输入书籍类型" v-bind:disabled="disabledInput" />
        </el-form-item>
        <el-form-item label="出版社" prop="bkPress">
          <el-input v-model="form.bkPress" placeholder="请输入出版社" v-bind:disabled="disabledInput" />
        </el-form-item>
        <el-form-item label="出版时间" prop="publishTime">
          <el-date-picker type="datetime" placeholder="选择日期" v-model="form.publishTime"
                          value-format="yyyy-MM-dd HH:mm:ss" v-bind:disabled="disabledInput" >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="页数" prop="bkPagination">
          <el-input v-model="form.bkPagination" placeholder="请输入页数" v-bind:disabled="disabledInput" />
        </el-form-item>
        <el-form-item label="状态" prop="bkState">
          <el-radio-group v-model="form.bkState">
            <el-radio
              v-for="dict in dict.type.sys_bk_state"
              :key="dict.value"
              :label="dict.value"
              v-bind:disabled="disabledInput"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="简介" prop="bkAbstract">
          <el-input v-model="form.bkAbstract" type="textarea" placeholder="请输入书籍简介" v-bind:disabled="disabledInput" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm" >确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 书籍导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload
        ref="upload"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip text-center" slot="tip">
<!--          <div class="el-upload__tip" slot="tip">-->
<!--            <el-checkbox v-model="upload.updateSupport" /> 是否更新已经存在的用户数据-->
<!--          </div>-->
          <span>仅允许导入xls、xlsx格式文件。</span>
          <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;" @click="importBkTemplate">下载模板</el-link>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm"  >确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { listBookInfo, updateBookInfo, addBookInfo, delBookInfo, getBookInfo} from "@/api/system/bookInfo";
import { getToken } from "@/utils/auth";
// import { uploadDll, gotoWrite, stopConnect} from "@/api/system/demo";
import { treeselect } from "@/api/system/dept";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "bookInfo",
  dicts: ['sys_bk_state'],
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

      // 书籍表格数据
      bookList: [],
      // 禁止编辑
      disabledInput:false,
      disableInput: false,
      // 组织树选项
      orgOptions: [],
      // 组织名称
      deptName: undefined,

      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,

      // 用户导入参数
      upload: {
        // 是否显示弹出层（用户导入）
        open: false,
        // 弹出层标题（用户导入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的用户数据
        updateSupport: 0,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/bookInfo/importData"
      },

      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        deptId:undefined,
        bkName: undefined,
        bkAuthor: undefined,
        bkType: undefined,
        bkState: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        deptId: [
          { required: true, message: "归属组织不能为空", trigger: "blur" }
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
      listBookInfo(this.queryParams).then(response => {
        this.bookList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      // this.loading = true;
      this.open = false;
      this.reset();
      this.loading = false;
      // stopConnect().then(response => {
      //   this.open = false;
      //   this.reset();
      //   this.loading = false;
      // });
    },
    // 表单重置
    reset() {
      this.form = {
        bkId: undefined,
        bkIsbn: undefined,
        bkRfid: undefined,
        bkName: undefined,
        bkImg: undefined,
        bkAuthor: undefined,
        bkPrice: undefined,
        bkType: undefined,
        bkPress: undefined,
        publishTime: undefined,
        bkPagination: undefined,
        bkState: "0",
        bkAbstract: undefined
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
      this.ids = selection.map(item => item.bkId);
      this.single = selection.length!==1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      // uploadDll().then(); 加载动态库，连接发卡机
      this.title = "添加书籍信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const bkId = row.bkId || this.ids;
      getBookInfo(bkId).then(response => {
        this.form = response.data;
        this.open = true;
        this.disabledInput = false;
        this.disableInput = true;
        this.title = "修改图书信息";
      });
    },
    /** 查看书籍详情 */
    handleDetail(row){
      this.reset();
      const bkId = row.bkId || this.ids;
      getBookInfo(bkId).then(response => {
        this.form = response.data;
        this.open = true;
        this.disabledInput = true;
        this.disableInput = true;
        this.title = "书籍详细信息";
      });
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.bkId !== undefined) {
            updateBookInfo(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addBookInfo(this.form).then(response => {
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
      const bkIds = row.bkId || this.ids;
      this.$modal.confirm('是否确认删除图书编号为"' + bkIds + '"的数据项？').then(function() {
        return delBookInfo(bkIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/bookInfo/export', {
        ...this.queryParams
      }, `post_${new Date().getTime()}.xlsx`)
    },

    /** 导入按钮操作 */
    handleImport() {
      this.upload.title = "书籍信息导入";
      this.upload.open = true;
    },
    /** 下载模板操作 */
    importBkTemplate() {
      this.download('bookInfo/importTemplate', {
      }, `bookInfo_template_${new Date().getTime()}.xlsx`)
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
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

    /** 写RFID */
    gotoWrite: function(){
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.bkIsbn !== undefined) {
            gotoWrite(this.form.bkIsbn).then(response => {
              console.log(response);
              if(response.code === 200){
                this.$modal.msgSuccess("写入成功");
                this.form.bkRfid = response.msg;
              }else {
                this.$modal.msgWarning("写入失败");
              }
            });
          }else {
            this.$modal.msgWarning("写入失败,请输入标准书号");
          }
          // else {
          //   addBookInfo(this.form).then(response => {
          //     this.$modal.msgSuccess("新增成功");
          //     this.open = false;
          //     this.getList();
          //   });
          // }
        }
      });
    },

  }
};
</script>
