import React, { useState } from 'react';
import { Box, Select, MenuItem, TextField, InputAdornment, Button } from '@mui/material';
import { useTranslation } from 'react-i18next';
import SearchIcon from '@mui/icons-material/Search';
import { useToast } from "../../General/Business/ToastContext.jsx";

/**
 * 任务搜索组件
 * 负责:
 * 1. 提供任务搜索功能
 * 2. 提供任务状态筛选
 * 3. 与首页搜索组件保持一致的视觉风格
 * 
 * @component
 */
const TaskSearch = () => {
  const { t } = useTranslation();
  const { showToast } = useToast();
  const [status, setStatus] = useState('all');
  const [searchText, setSearchText] = useState('');

  const handleStatusChange = (event) => {
    setStatus(event.target.value);
  };

  const handleSearch = () => {
    showToast("搜索按钮被点击！", "success");
  };

  return (
    <Box
      sx={{
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        padding: "20px",
        backgroundColor: "#FFFFFF",
        width: "100%",
        position: "relative",
      }}
    >
      {/* 搜索栏 */}
      <Box
        sx={{
          display: "flex",
          alignItems: "center",
          gap: "10px",
          width: {
            xs: "80%",
            sm: "60%",
            md: "45%",
          },
          maxWidth: "800px",
        }}
      >
        <TextField
          fullWidth
          variant="outlined"
          placeholder={t('myTasks.searchPlaceholder')}
          value={searchText}
          onChange={(e) => setSearchText(e.target.value)}
          sx={{
            backgroundColor: "#fff",
            borderRadius: "4px",
            "& .MuiInputBase-root": {
              height: "40px",
            },
          }}
          InputProps={{
            startAdornment: (
              <InputAdornment position="start">
                <SearchIcon sx={{ color: 'text.secondary' }} />
              </InputAdornment>
            ),
          }}
        />
        <Button
          variant="contained"
          color="primary"
          onClick={handleSearch}
          sx={{
            height: "40px",
            whiteSpace: "nowrap",
          }}
        >
          {t("searchButtonPlaceHolder")}
        </Button>
      </Box>

      {/* 筛选器 - 仅在大屏幕显示 */}
      <Box
        sx={{
          position: "absolute",
          right: "13%",
          display: { xs: "none", md: "block" },
        }}
      >
        <Select
          size="small"
          value={status}
          onChange={handleStatusChange}
          sx={{
            minWidth: 120,
            height: "40px",
            bgcolor: 'white',
            '& .MuiOutlinedInput-notchedOutline': {
              borderColor: '#e0e0e0'
            }
          }}
        >
          <MenuItem value="all">{t('myTasks.all')}</MenuItem>
          <MenuItem value="active">{t('myTasks.active')}</MenuItem>
          <MenuItem value="inactive">{t('myTasks.inactive')}</MenuItem>
        </Select>
      </Box>
    </Box>
  );
};

export default TaskSearch; 