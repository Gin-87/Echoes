import React, { useState, useEffect } from 'react';
import {
  Box,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Button,
  Typography,
  Chip,
  Divider,
  CircularProgress,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogContentText,
  DialogActions,
} from '@mui/material';
import { useTranslation } from 'react-i18next';
import FilterBar from '../Common/FilterBar';
import { useToast } from '../../../../Comp/components/General/Business/ToastContext';
import request from '../../../../utils/request';

// 表头样式
const tableHeaderStyle = {
  fontWeight: 600,
  fontSize: '0.875rem',
  color: 'text.secondary',
  borderBottom: '2px solid',
  borderColor: 'divider',
  padding: '8px 16px',
};

const cellStyle = {
  padding: '4px 16px',
  height: '32px',
};

const UserManagement = () => {
  const { t } = useTranslation();
  const { showToast } = useToast();
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);

  // 加载用户数据
  const loadUsers = async () => {
    try {
      setLoading(true);
      const response = await request.get('/api/users/admin/getAll');
      
      if (response && response.code === 200) {
        const mappedData = response.data.map(user => ({
          id: user.id,
          nickname: user.nickname,
          role: user.role_id === 2 ? 'admin' : 'user',  // 根据role_id判断角色
          status: user.status.toLowerCase(),  // 转换为小写以匹配前端状态
          email: user.email,
          phone: user.phone
        }));
        setUsers(mappedData);
      } else {
        showToast(t('admin.loadFailed'), 'error');
      }
    } catch (error) {
      console.error('Failed to load users:', error);
      showToast(t('admin.loadFailed'), 'error');
    } finally {
      setLoading(false);
    }
  };

  // 组件挂载时加载数据
  useEffect(() => {
    loadUsers();
  }, []);

  // 分页相关状态
  const [page, setPage] = useState(0);
  const [rowsPerPage] = useState(10);

  // 计算分页数据
  const paginatedUsers = users.slice(
    page * rowsPerPage,
    page * rowsPerPage + rowsPerPage
  );

  // 处理页面变化
  const handlePageChange = (newPage) => {
    setPage(newPage);
  };

  // 过滤器状态
  const [filters, setFilters] = useState({
    id: '',
    nickname: '',
    role: '',
    status: '',
  });

  // 过滤器配置
  const filterConfig = [
    {
      field: 'id',
      label: 'ID',
      type: 'text',
    },
    {
      field: 'nickname',
      label: t('admin.nickname'),
      type: 'text',
    },
    {
      field: 'role',
      label: t('admin.role_title'),
      type: 'select',
      options: [
        { value: '', label: t('admin.all') },
        { value: 'admin', label: t('admin.role.admin') },
        { value: 'user', label: t('admin.role.user') },
      ],
    },
    {
      field: 'status',
      label: t('admin.status_title'),
      type: 'select',
      options: [
        { value: '', label: t('admin.all') },
        { value: 'active', label: t('admin.status.active') },
        { value: 'inactive', label: t('admin.status.inactive') },
      ],
    },
  ];

  const handleFilterChange = (field, value) => {
    setFilters(prev => ({ ...prev, [field]: value }));
  };

  const handleSearch = () => {
    // TODO: 实现搜索逻辑
    console.log('Search with filters:', filters);
  };

  const handleReset = () => {
    setFilters({
      id: '',
      nickname: '',
      role: '',
      status: '',
    });
  };

  // 处理状态切换
  const handleStatusToggle = async (userId) => {
    try {
      const user = users.find(u => u.id === userId);
      const newStatus = user.status === 'active' ? 'INACTIVE' : 'ACTIVE';
      
      const response = await request.put(`/api/users/update/${userId}`, {
        status: newStatus
      });

      if (response && response.code === 200) {
        loadUsers();  // 重新加载用户列表
        showToast(t('admin.updateSuccess'), 'success');
      }
    } catch (error) {
      console.error('Failed to update user status:', error);
      showToast(t('admin.updateFailed'), 'error');
    }
  };

  // 添加删除对话框状态
  const [deleteDialogOpen, setDeleteDialogOpen] = useState(false);
  const [userToDelete, setUserToDelete] = useState(null);

  // 打开删除确认对话框
  const handleDeleteClick = (user) => {
    setUserToDelete(user);
    setDeleteDialogOpen(true);
  };

  // 关闭删除确认对话框
  const handleDeleteCancel = () => {
    setUserToDelete(null);
    setDeleteDialogOpen(false);
  };

  // 处理删除用户
  const handleDeleteConfirm = async () => {
    try {
      const response = await request.delete(`/api/users/delete/${userToDelete.id}`);
      
      if (response && response.code === 200) {
        showToast(t('admin.deleteSuccess'), 'success');
        loadUsers();  // 重新加载用户列表
      } else {
        showToast(response.message || t('admin.deleteFailed'), 'error');
      }
    } catch (error) {
      console.error('Delete failed:', error);
      showToast(t('admin.deleteFailed'), 'error');
    } finally {
      handleDeleteCancel();  // 关闭对话框
    }
  };

  return (
    <Box 
      sx={{ 
        height: '100%',
        display: 'flex',
        flexDirection: 'column',
        overflow: 'hidden',
        pt: 3, // 添加顶部内边距
      }}
    >
      <Box
        sx={{
          px: 3,
          flex: 1,
          overflow: 'auto',
        }}
      >
        {/* 标题 */}
        <Typography variant="h5" component="h2" sx={{ fontWeight: 500, mb: 3 }}>
          {t('admin.userManagement')}
        </Typography>

        {/* 查询过滤器 */}
        <Paper
          sx={{
            p: 2,
            mb: 3,
            border: '1px solid',
            borderColor: 'divider',
            borderRadius: 1,
            boxShadow: 'none',
          }}
        >
          <FilterBar
            filters={filterConfig.map(config => ({
              ...config,
              value: filters[config.field]
            }))}
            onFilterChange={handleFilterChange}
            onSearch={handleSearch}
            onReset={handleReset}
          />
        </Paper>

        {/* "添加用户"按钮 */}
        <Box
          sx={{
            display: 'flex',
            justifyContent: 'space-between',
            mb: 3,
            alignItems: 'center',
          }}
        >
          <Button
            variant="contained"
            color="primary"
            sx={{
              borderRadius: '20px',
              minWidth: '120px',
              boxShadow: 'none',
              '&:hover': {
                boxShadow: 'none',
              },
            }}
          >
            {t('admin.addUser')}
          </Button>
        </Box>

        {/* 数据表格 */}
        <TableContainer
          component={Paper}
          sx={{
            boxShadow: 'none',
            border: '1px solid',
            borderColor: 'divider',
            borderRadius: 1,
            height: '900px',
            display: 'flex',
            flexDirection: 'column',
          }}
        >
          <Table>
            <TableHead>
              <TableRow sx={{ backgroundColor: 'grey.50' }}>
                <TableCell 
                  sx={{ 
                    ...tableHeaderStyle,
                    height: '48px',
                  }} 
                  width="80px"
                >
                  ID
                </TableCell>
                <TableCell 
                  sx={{ 
                    ...tableHeaderStyle,
                    height: '48px',
                  }} 
                  width="150px"
                >
                  {t('admin.nickname')}
                </TableCell>
                <TableCell 
                  sx={{ 
                    ...tableHeaderStyle,
                    height: '48px',
                  }} 
                  width="150px"
                >
                  {t('admin.phone')}
                </TableCell>
                <TableCell 
                  sx={{ 
                    ...tableHeaderStyle,
                    height: '48px',
                  }} 
                  width="200px"
                >
                  {t('admin.email')}
                </TableCell>
                <TableCell 
                  sx={{ 
                    ...tableHeaderStyle,
                    height: '48px',
                  }} 
                  width="120px"
                >
                  {t('admin.role_title')}
                </TableCell>
                <TableCell 
                  sx={{ 
                    ...tableHeaderStyle,
                    height: '48px',
                  }} 
                  width="120px"
                >
                  {t('admin.status_title')}
                </TableCell>
                <TableCell 
                  sx={{ 
                    ...tableHeaderStyle,
                    height: '48px',
                  }} 
                  width="200px"
                  align="center"
                >
                  {t('admin.actions')}
                </TableCell>
              </TableRow>
            </TableHead>

            <TableBody>
              {loading ? (
                <TableRow>
                  <TableCell colSpan={7} align="center">
                    <CircularProgress size={24} />
                  </TableCell>
                </TableRow>
              ) : users.length === 0 ? (
                <TableRow>
                  <TableCell colSpan={7} align="center">
                    {t('admin.noData')}
                  </TableCell>
                </TableRow>
              ) : (
                paginatedUsers.map((user, index) => (
                  <React.Fragment key={user.id}>
                    <TableRow
                      sx={{
                        '&:hover': {
                          backgroundColor: 'action.hover',
                        },
                        height: '32px',
                      }}
                    >
                      <TableCell sx={{ ...cellStyle }}>
                        {user.id}
                      </TableCell>
                      <TableCell>{user.nickname}</TableCell>
                      <TableCell>
                        {user.phone || '-'}
                      </TableCell>
                      <TableCell>
                        {user.email || '-'}
                      </TableCell>
                      <TableCell>
                        {t(`admin.role.${user.role}`)}
                      </TableCell>
                      <TableCell>
                        <Chip
                          label={t(`admin.status.${user.status}`)}
                          size="small"
                          sx={{
                            fontWeight: 500,
                            borderRadius: 1,
                            backgroundColor: user.status === 'active' ? '#E6F4FF' : '#F5F5F5',
                            color: user.status === 'active' ? '#0958D9' : '#00000073',
                            border: 'none',
                          }}
                        />
                      </TableCell>
                      <TableCell>
                        <Box
                          sx={{
                            display: 'flex',
                            justifyContent: 'center',
                            gap: 1,
                            '& .MuiButton-root': {
                              minWidth: '80px',
                              fontSize: '0.875rem',
                            },
                          }}
                        >
                          <Button
                            variant="text"
                            color="primary"
                            onClick={() => handleStatusToggle(user.id)}
                            sx={{ fontWeight: 500 }}
                          >
                            {user.status === 'active' ? t('admin.deactivate') : t('admin.activate')}
                          </Button>
                          <Divider orientation="vertical" flexItem />
                          <Button
                            variant="text"
                            color="error"
                            onClick={() => handleDeleteClick(user)}
                            sx={{ fontWeight: 500 }}
                          >
                            {t('admin.delete')}
                          </Button>
                        </Box>
                      </TableCell>
                    </TableRow>
                    {index < rowsPerPage - 1 && index < paginatedUsers.length - 1 && (
                      <TableRow>
                        <TableCell 
                          colSpan={7} 
                          sx={{ 
                            padding: 0,
                            height: 0,
                          }}
                        >
                          <Divider />
                        </TableCell>
                      </TableRow>
                    )}
                  </React.Fragment>
                ))
              )}
              {/* 如果当前页数据不足10条，填充空行 */}
              {paginatedUsers.length < rowsPerPage && (
                <TableRow sx={{ height: `${32 * (rowsPerPage - paginatedUsers.length)}px` }}>
                  <TableCell colSpan={7} />
                </TableRow>
              )}
            </TableBody>
          </Table>
          {/* 分页控件 */}
          <Box
            sx={{
              display: 'flex',
              justifyContent: 'flex-end',
              alignItems: 'center',
              gap: 2,
              p: 2,
              borderTop: '1px solid',
              borderColor: 'divider',
              backgroundColor: 'white',
            }}
          >
            <Box>共 {users.length} 条</Box>
            <Box sx={{ display: 'flex', gap: 1 }}>
              <Button
                size="small"
                disabled={page === 0}
                onClick={() => handlePageChange(page - 1)}
              >
                上一页
              </Button>
              {Array.from({ length: Math.ceil(users.length / rowsPerPage) }).map((_, index) => (
                <Button
                  key={index}
                  size="small"
                  variant={page === index ? 'contained' : 'text'}
                  onClick={() => handlePageChange(index)}
                >
                  {index + 1}
                </Button>
              ))}
              <Button
                size="small"
                disabled={page >= Math.ceil(users.length / rowsPerPage) - 1}
                onClick={() => handlePageChange(page + 1)}
              >
                下一页
              </Button>
            </Box>
          </Box>
        </TableContainer>
      </Box>

      {/* 添加删除确认对话框 */}
      <Dialog
        open={deleteDialogOpen}
        onClose={handleDeleteCancel}
        aria-labelledby="delete-dialog-title"
        aria-describedby="delete-dialog-description"
      >
        <DialogTitle id="delete-dialog-title">
          {t('admin.deleteConfirmTitle')}
        </DialogTitle>
        <DialogContent>
          <DialogContentText id="delete-dialog-description">
            {userToDelete && t('admin.deleteUserConfirmMessage', {
              nickname: userToDelete.nickname,
              id: userToDelete.id
            })}
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button
            onClick={handleDeleteCancel}
            sx={{ color: 'text.secondary' }}
          >
            {t('common.cancel')}
          </Button>
          <Button
            onClick={handleDeleteConfirm}
            color="error"
            variant="contained"
            autoFocus
          >
            {t('common.confirm')}
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default UserManagement; 