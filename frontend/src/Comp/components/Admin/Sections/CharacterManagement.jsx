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
  TablePagination,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  DialogContentText,
} from '@mui/material';
import { useTranslation } from 'react-i18next';
import FilterBar from '../Common/FilterBar';
import { getAdminCharacters } from '../../../../services/characterService';
import { useToast } from '../../../../Comp/components/General/Business/ToastContext';
import request from '../../../../utils/request';

// 表头样式，可根据实际需要微调
const tableHeaderStyle = {
  fontWeight: 600,
  fontSize: '0.875rem',
  color: 'text.secondary',
  borderBottom: '2px solid',
  borderColor: 'divider',
  padding: '12px 16px',
};

// 操作按钮之间的分割线样式
const actionDividerStyle = {
  height: '20px',  // 设置高度
  margin: '0 4px', // 设置左右间距
  backgroundColor: 'rgba(0, 0, 0, 0.12)' // 统一颜色
};

// 行之间的分割线样式
const rowDividerStyle = {
  borderColor: 'rgba(0, 0, 0, 0.12)', // 统一颜色
  margin: 0  // 移除默认边距
};

const CharacterManagement = () => {
  const [characters, setCharacters] = useState([]);
  const { showToast } = useToast();
  const { t } = useTranslation();

  // 添加加载状态
  const [loading, setLoading] = useState(true);

  // 状态映射
  const statusMap = {
    'PUBLIC': 'active',
    'PRIVATE': 'inactive'
  };

  // 加载角色列表数据
  const loadCharacters = async () => {
    try {
      setLoading(true);
      const response = await request.get('/api/characters/admin/getAll');
      
      if (response && response.code === 200) {
        const mappedData = response.data.map(char => ({
          id: char.id,
          code: char.code,
          name: char.name,
          creator: char.creator,
          createTime: new Date(char.creationDateTime).toLocaleString('zh-CN', {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit',
            second: '2-digit'
          }),
          status: statusMap[char.status] || 'inactive'
        }));
        setCharacters(mappedData);
      } else {
        showToast(t('admin.loadFailed'), 'error');
      }
    } catch (error) {
      console.error('Failed to load characters:', error);
      showToast(t('admin.loadFailed'), 'error');
    } finally {
      setLoading(false);
    }
  };

  // 确保在组件挂载时调用 loadCharacters
  useEffect(() => {
    loadCharacters();
  }, []); // 空依赖数组，只在组件挂载时执行

  // 过滤器的状态
  const [filters, setFilters] = useState({
    code: '',
    name: '',
    creator: '',
    status: '',
  });

  // 过滤器配置数组
  const filterConfig = [
    {
      field: 'code',
      label: t('admin.characterCode'),
      type: 'text',
    },
    {
      field: 'name',
      label: t('admin.characterName'),
      type: 'text',
    },
    {
      field: 'creator',
      label: t('admin.creator'),
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

  // 当用户修改过滤器时触发
  const handleFilterChange = (field, value) => {
    setFilters((prev) => ({ ...prev, [field]: value }));
  };

  // 点击搜索
  const handleSearch = () => {
    // TODO: 实现实际搜索逻辑
    console.log('Search with filters:', filters);
  };

  // 点击重置
  const handleReset = () => {
    setFilters({
      code: '',
      name: '',
      creator: '',
      status: '',
    });
  };

  // 切换角色状态
  const handleStatusToggle = (characterId) => {
    setCharacters((prev) =>
      prev.map((character) => {
        if (character.id === characterId) {
          return {
            ...character,
            status: character.status === 'active' ? 'inactive' : 'active',
          };
        }
        return character;
      })
    );
  };

  // 编辑角色
  const handleEdit = (characterId) => {
    // TODO: 实现编辑功能
    console.log('Edit character:', characterId);
  };

  // 删除角色
  const handleDelete = async (id) => {
    try {
      const data = await request.delete(`/api/characters/${id}`);
      if (data && data.code === 200) {
        loadCharacters();
        showToast("删除成功", "success");
      }
    } catch (error) {
      console.error('Delete failed:', error);
    }
  };

  // 添加分页状态
  const [page, setPage] = useState(0);
  const [rowsPerPage] = useState(10);

  // 处理页码变化
  const handleChangePage = (event, newPage) => {
    setPage(newPage);
  };

  // 获取当前页的数据
  const getCurrentPageData = () => {
    const startIndex = page * rowsPerPage;
    return characters.slice(startIndex, startIndex + rowsPerPage);
  };

  const [deleteDialogOpen, setDeleteDialogOpen] = useState(false);
  const [characterToDelete, setCharacterToDelete] = useState(null);

  // 打开删除确认对话框
  const handleDeleteClick = (character) => {
    console.log('Character to delete:', character);
    setCharacterToDelete(character);
    setDeleteDialogOpen(true);
  };

  // 关闭删除确认对话框
  const handleDeleteCancel = () => {
    setCharacterToDelete(null);
    setDeleteDialogOpen(false);
  };

  // 执行删除操作
  const handleDeleteConfirm = async () => {
    try {
      const token = localStorage.getItem('accessToken');
      const response = await fetch(`/api/characters/delete/${characterToDelete.id}`, {
        method: 'DELETE',
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });

      const data = await response.json();

      if (data.code === 200) {
        showToast(t('admin.deleteSuccess'), 'success');
        // 重新加载角色列表
        loadCharacters();
      } else {
        showToast(data.message || t('admin.deleteFailed'), 'error');
      }
    } catch (error) {
      console.error('Delete failed:', error);
      showToast(t('admin.deleteFailed'), 'error');
    } finally {
      handleDeleteCancel();
    }
  };

  return (
    <Box
      sx={{
        height: '100%',
        display: 'flex',
        flexDirection: 'column',
        overflow: 'hidden',
      }}
    >
      <Box
        sx={{
          px: 3,
          py: 2,
          flex: 1,
          overflow: 'auto',
        }}
      >
        {/* 标题 */}
        <Typography variant="h5" component="h2" sx={{ fontWeight: 500, mb: 3 }}>
          {t('admin.characterManagement')}
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
            // 给每个 filterConfig 增加实际的 value
            filters={filterConfig.map((config) => ({
              ...config,
              // 把 filters 里对应的值赋给 value
              value: filters[config.field],
            }))}
            onFilterChange={handleFilterChange}
            onSearch={handleSearch}
            onReset={handleReset}
          />
        </Paper>

        {/* “新增角色”按钮 */}
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
            onClick={() => window.open('/create-character', '_blank')}
            sx={{
              borderRadius: '20px',
              minWidth: '120px',
              boxShadow: 'none',
              '&:hover': {
                boxShadow: 'none',
              },
            }}
          >
            {t('admin.addCharacter')}
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
            display: 'flex',
            flexDirection: 'column',
            width: '100%',
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
                  width="150px"
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
                  {t('admin.characterCode')}
                </TableCell>
                <TableCell
                  sx={{
                    ...tableHeaderStyle,
                    height: '48px',
                  }}
                  width="150px"
                >
                  {t('admin.characterName')}
                </TableCell>
                <TableCell
                  sx={{
                    ...tableHeaderStyle,
                    height: '48px',
                  }}
                  width="120px"
                >
                  {t('admin.creator')}
                </TableCell>
                <TableCell
                  sx={{
                    ...tableHeaderStyle,
                    height: '48px',
                  }}
                  width="180px"
                >
                  {t('admin.createTime')}
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
                  width="250px"
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
              ) : characters.length === 0 ? (
                <TableRow>
                  <TableCell colSpan={7} align="center">
                    {t('admin.noData')}
                  </TableCell>
                </TableRow>
              ) : (
                getCurrentPageData().map((character, index) => (
                  <React.Fragment key={character.id}>
                    <TableRow
                      sx={{
                        '&:hover': {
                          backgroundColor: 'action.hover',
                        },
                        height: '48px',  // 统一行高
                      }}
                    >
                      <TableCell>{character.id}</TableCell>
                      <TableCell>{character.code}</TableCell>
                      <TableCell>{character.name}</TableCell>
                      <TableCell>{character.creator}</TableCell>
                      <TableCell>{character.createTime}</TableCell>
                      <TableCell>
                        <Chip
                          label={t(`admin.status.${character.status}`)}
                          size="small"
                          sx={{
                            fontWeight: 500,
                            borderRadius: 1,
                            backgroundColor:
                              character.status === 'active'
                                ? '#E6F4FF'
                                : '#F5F5F5',
                            color:
                              character.status === 'active'
                                ? '#0958D9'
                                : '#00000073',
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
                            onClick={() => handleStatusToggle(character.id)}
                            sx={{ fontWeight: 500 }}
                          >
                            {character.status === 'active'
                              ? t('admin.deactivate')
                              : t('admin.activate')}
                          </Button>
                          <Divider orientation="vertical" flexItem sx={actionDividerStyle} />
                          <Button
                            variant="text"
                            color="primary"
                            onClick={() => handleEdit(character.id)}
                            sx={{ fontWeight: 500 }}
                          >
                            {t('admin.edit')}
                          </Button>
                          <Divider orientation="vertical" flexItem sx={actionDividerStyle} />
                          <Button
                            variant="text"
                            color="error"
                            onClick={() => handleDeleteClick({
                              id: character.id,
                              name: character.name,
                              code: character.code
                            })}
                            sx={{ fontWeight: 500 }}
                          >
                            {t('admin.delete')}
                          </Button>
                        </Box>
                      </TableCell>
                    </TableRow>
                    {index < getCurrentPageData().length - 1 && (
                      <TableRow>
                        <TableCell colSpan={7} sx={{ padding: 0 }}>
                          <Divider sx={rowDividerStyle} />
                        </TableCell>
                      </TableRow>
                    )}
                  </React.Fragment>
                ))
              )}
            </TableBody>
          </Table>

          {/* 添加分页控件 */}
          <TablePagination
            component="div"
            count={characters.length}
            page={page}
            onPageChange={handleChangePage}
            rowsPerPage={rowsPerPage}
            rowsPerPageOptions={[10]}
            labelDisplayedRows={({ from, to, count }) => {
              const adjustedFrom = count === 0 ? 0 : from + 1;
              const adjustedTo = Math.min(to + 1, count);
              if (count === 0) {
                return t('admin.paginationEmpty');
              }
              return t('admin.pagination', {
                from: adjustedFrom,
                to: adjustedTo,
                count
              });
            }}
            sx={{
              borderTop: '1px solid',
              borderColor: 'divider',
            }}
          />
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
            {characterToDelete && t('admin.deleteConfirmMessage', {
              name: characterToDelete.name,
              code: characterToDelete.code
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

export default CharacterManagement;
